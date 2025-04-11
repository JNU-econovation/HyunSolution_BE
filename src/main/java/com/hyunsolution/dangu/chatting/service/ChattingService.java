package com.hyunsolution.dangu.chatting.service;

import com.hyunsolution.dangu.chatRoom.domain.ChatRoom;
import com.hyunsolution.dangu.chatRoom.domain.ChatRoomRepository;
import com.hyunsolution.dangu.chatRoom.service.ChatRoomService;
import com.hyunsolution.dangu.chatlog.domain.ChatLog;
import com.hyunsolution.dangu.chatlog.domain.ChatLogRepository;
import com.hyunsolution.dangu.chatlog.service.ChatlogService;
import com.hyunsolution.dangu.chatting.domain.ChatSession;
import com.hyunsolution.dangu.chatting.domain.Chatting;
import com.hyunsolution.dangu.chatting.domain.ChattingRepository;
import com.hyunsolution.dangu.chatting.domain.MessageType;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageDetailResponse;
import com.hyunsolution.dangu.chatting.dto.response.ChattingsDto;
import com.hyunsolution.dangu.chatting.dto.response.GetChatRoomsResponse;
import com.hyunsolution.dangu.chatting.dto.response.GetChattingsResponse;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.service.UserService;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingService {
    private final Map<String, ChatSession> chatParticipantInfos = new HashMap<>();
    private final ChattingRepository chattingRepository;
    private final ChatLogRepository chatLogRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final ChatlogService chatlogService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;
    private final ParticipantRepository participantRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public GetChattingsResponse getChattings(Long loginUserId, Long chatRoomId) {
        chatRoomService.validateIsAlreadyMatched(chatRoomId);
        List<Chatting> chattings = chattingRepository.findByChatRoomId(chatRoomId);
        List<ChattingsDto> chattingsDtos = convertToChattingsDto(chattings, loginUserId);
        ChatRoom chatRoom = chatRoomRepository.findByIdWithFetchJoinParticipantsAndUSer(chatRoomId);
        return GetChattingsResponse.of(getOtherPeople(chatRoom, loginUserId), chattingsDtos);
    }

    private List<ChattingsDto> convertToChattingsDto(List<Chatting> chattings, Long loginUserId) {
        return chattings.stream()
                .map(
                        chatting -> {
                            boolean isOwn = isOwn(loginUserId, chatting.getSender());
                            return ChattingsDto.of(
                                    chatting.getContent(),
                                    chatting.getId(),
                                    isOwn,
                                    chatting.getMessageType());
                        })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GetChatRoomsResponse> getChatRooms(Long userId) {
        List<ChatRoom> chatRooms =
                chatRoomRepository.findByParticipantUserIdWithEntityGraph(userId);
        return chatRooms.stream()
                .filter(chatRoom -> chatRoom.getChatUpdateAt() != null)
                .sorted(Comparator.comparing(ChatRoom::getChatUpdateAt).reversed())
                .map(chatRoom -> buildGetChatRoomsResponse(chatRoom, userId))
                .toList();
    }

    private boolean isOwn(Long loginUserId, User sender) {
        if (sender == null) {
            return false;
        }
        return loginUserId.equals(sender.getId());
    }

    public GetChatRoomsResponse buildGetChatRoomsResponse(ChatRoom chatRoom, Long userId) {
        return GetChatRoomsResponse.of(
                chatRoom.getId(),
                getLastMessage(chatRoom.getId()),
                getOtherPeople(chatRoom, userId),
                getUnReadCount(chatRoom.getId(), userId));
    }

    public List<String> getOtherPeople(ChatRoom chatRoom, Long loginUserId) {
        return chatRoom.getParticipants().stream()
                .filter(participant -> !participant.getUser().getId().equals(loginUserId))
                .map(participant -> participant.getUser().getUid())
                .toList();
    }

    public String getLastMessage(Long chatRoomId) {
        return chattingRepository.findLastChattingContentByChatRoomId(chatRoomId);
    }

    public int getUnReadCount(Long chatRoomId, Long userPk) {
        ChatLog chatLog = chatlogService.findChatLog(chatRoomId, userPk);
        int total = chattingRepository.findByChatRoomId(chatRoomId).size();
        int read = chatLog.getReadCount();
        return total - read;
    }

    @Transactional
    public ChatMessageDetailResponse sendMessage(Long chatRoomId, String message, Long userId) {

        ChatRoom chatRoom = chatRoomService.findChatRoom(chatRoomId);
        chatRoomService.updateChatRoom(chatRoom);
        User user = userService.findUser(userId);
        Chatting chatMessage = buildChatMessage(chatRoom, user, message);
        chattingRepository.save(chatMessage);
        return new ChatMessageDetailResponse(user.getUid(), message, MessageType.TEXT);
    }

    public Chatting buildChatMessage(ChatRoom chatRoom, User user, String message) {
        Chatting chatMessage =
                Chatting.builder().chatRoom(chatRoom).sender(user).content(message).build();
        return chatMessage;
    }

    @Transactional
    public void readMessageCnt(Long chatRoomId, Long userId) {
        // 채팅방 나갈 시점에서의 메세지 개수 조회
        int messageCnt = chattingRepository.countMessageByChatRoomId(chatRoomId);
        // chatlog 테이블 속 readCount 업데이트
        chatlogService.updateReadCount(chatRoomId, userId, messageCnt);
    }

    //  채팅방에 입장했을 때 (웹소켓 연결)
    public void getChatRoom(String sessionId, Long userId, Long roomId) {
        chatParticipantInfos.put(sessionId, new ChatSession(userId, roomId));
        log.info("getChatRoom");
    }

    // 채팅방에 퇴장했을 때 (웹소켓 끊김)
    public void leaveChatRoom(String sessionId) {
        ChatSession chatSession = chatParticipantInfos.get(sessionId);
        if (chatSession != null) {
            readMessageCnt(chatSession.getRoomId(), chatSession.getUserId());
            chatParticipantInfos.remove(sessionId);
            log.info(
                    "채팅방에 퇴장했을 때-> userPk: "
                            + chatSession.getUserId()
                            + ", roomId: "
                            + chatSession.getRoomId());
        }
    }
}

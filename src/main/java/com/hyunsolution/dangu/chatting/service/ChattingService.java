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
                .map(
                        chatRoom ->
                                GetChatRoomsResponse.of(
                                        chatRoom.getId(),
                                        getLastMessage(chatRoom.getId()),
                                        getOtherPeople(chatRoom, userId),
                                        getUnReadCount(chatRoom.getId(), userId)))
                .toList();
    }

    private boolean isOwn(Long loginUserId, User sender) {
        if (sender == null) {
            return false;
        }
        return loginUserId.equals(sender.getId());
    }

    private List<String> getOtherPeople(ChatRoom chatRoom, Long loginUserId) {
        return chatRoom.getParticipants().stream()
                .filter(participant -> !participant.getUser().getId().equals(loginUserId))
                .map(participant -> participant.getUser().getUid())
                .toList();
    }

    private String getLastMessage(Long chatRoomId) {
        return chattingRepository.findLastChattingContentByChatRoomId(chatRoomId);
    }

    private int getUnReadCount(Long chatRoomId, Long userId) {
        ChatLog chatLog =
                chatLogRepository
                        .findByChatRoomIdAndUserId(chatRoomId, userId)
                        .orElseThrow(() -> ChatLogNotFoundException.EXCEPTION);
        int total = chattingRepository.findByChatRoomId(chatRoomId).size();
        int read = chatLog.getReadCount();
        return total - read;
    }

    @Transactional
    public ChatMessageDetailResponse sendMessage(Long chatRoomId, String message, Long userPk) {

        ChatRoom chatRoom = chatRoomService.findChatRoom(chatRoomId);
        chatRoomService.updateChatRoom(chatRoom);
        User user = userService.findUserByUserPK(userPk);
        saveMessage(chatRoom, user, message);
        return buildChatMessage(user, message);
    }

    public ChatMessageDetailResponse buildChatMessage(User user, String message) {
        return new ChatMessageDetailResponse(user.getUid(), message, MessageType.TEXT);
    }

    public void saveMessage(ChatRoom chatRoom, User user, String message){
        Chatting chatMessage =
                Chatting.builder().chatRoom(chatRoom).sender(user).content(message).build();
        chattingRepository.save(chatMessage);
        return chatMessage;
    }

    @Transactional
    public void readMessageCnt(Long chatRoomId, Long userPk) {
        // 채팅방 나갈 시점에서의 메세지 개수 조회
        int messageCnt = chattingRepository.countMessageByChatRoomId(chatRoomId);
        // chatlog 테이블 속 readCount 업데이트
        chatlogService.updateReadCount(chatRoomId, userPk, messageCnt);
    }

    //  채팅방에 입장했을 때 (웹소켓 연결)
    public void getChatRoom(String sessionId, Long userPk, Long roomId) {
        chatParticipantInfos.put(sessionId, new ChatSession(userPk, roomId));
        log.info("getChatRoom");
    }

    // ⚠️ 채팅방에 퇴장했을 때 (웹소켓 끊김)
    public void leaveChatRoom(String sessionId) {
        for (Map.Entry<String, ChatSession> entry : chatParticipantInfos.entrySet()) {
            if (entry.getKey().equals(sessionId)) {
                Long userPk = entry.getValue().getUserId();
                Long roomId = entry.getValue().getRoomId();
                readMessageCnt(roomId, userPk);
                chatParticipantInfos.remove(sessionId);
                System.out.println("채팅방에 퇴장했을 때-> userPk: " + userPk + ", roomId: " + roomId);
                break;
            }
        }
    }
}

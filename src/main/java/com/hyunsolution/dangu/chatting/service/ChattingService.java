package com.hyunsolution.dangu.chatting.service;

import com.hyunsolution.dangu.chatlog.domain.ChatLog;
import com.hyunsolution.dangu.chatlog.domain.ChatLogRepository;
import com.hyunsolution.dangu.chatlog.exception.ChatLogNotFoundException;
import com.hyunsolution.dangu.chatlog.service.ChatlogService;
import com.hyunsolution.dangu.chatting.domain.Chatting;
import com.hyunsolution.dangu.chatting.domain.ChattingRepository;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageDetailResponse;
import com.hyunsolution.dangu.chatting.dto.response.GetChatRoomsResponse;
import com.hyunsolution.dangu.chatting.dto.response.GetChattingsResponse;
import com.hyunsolution.dangu.chatting.exception.ChatRoomNotFoundException;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.exception.UserNotFoundException;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChattingService {
    private final ChattingRepository chattingRepository;
    private final ChatLogRepository chatLogRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final ChatlogService chatlogService;

    @Transactional(readOnly = true)
    public List<GetChattingsResponse> getChattings(Long loginUserId, Long chatRoomId) {
        List<Chatting> chattings = chattingRepository.findByWorkspaceId(chatRoomId);
        return chattings.stream()
                .map(
                        chatting -> {
                            boolean isOwn = isOwn(loginUserId, chatting.getSender().getId());
                            return GetChattingsResponse.of(
                                    chatting.getContent(), chatting.getId(), isOwn);
                        })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GetChatRoomsResponse> getChatRooms(Long userId) {
        List<Workspace> chatRooms = workspaceRepository.findByParticipantUserId(userId);
        return chatRooms.stream()
                .filter(chatRoom -> chatRoom.getParticipants().size() > 1)
                .map(
                        chatRoom ->
                                GetChatRoomsResponse.of(
                                        chatRoom.getId(),
                                        getLastMessage(chatRoom.getId()),
                                        getOtherPerson(chatRoom, userId),
                                        getUnReadCount(chatRoom.getId(), userId)))
                .toList();
    }

    private boolean isOwn(Long loginUserId, Long chattingUserId) {
        return loginUserId.equals(chattingUserId);
    }

    private List<String> getOtherPerson(Workspace chatRoom, Long loginUserId) {
        return chatRoom.getParticipants().stream()
                .filter(participant -> !participant.getUser().getId().equals(loginUserId))
                .map(participant -> participant.getUser().getUid())
                .toList();
    }

    private String getLastMessage(Long chatRoomId) {
        return chattingRepository.findLastChattingContentByWorkspaceId(chatRoomId);
    }

    private int getUnReadCount(Long chatRoomId, Long userId) {
        ChatLog chatLog =
                chatLogRepository
                        .findByWorkspaceIdAndUserId(chatRoomId, userId)
                        .orElseThrow(() -> ChatLogNotFoundException.EXCEPTION);
        int total = chattingRepository.findByWorkspaceId(chatRoomId).size();
        int read = chatLog.getReadCount();
        return total - read;
    }

    @Transactional
    public ChatMessageDetailResponse sendMessage(Long chatRoomId, String message, Long userPk) {

        Workspace workspace =
                workspaceRepository
                        .findById(chatRoomId)
                        .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
        User user =
                userRepository.findById(userPk).orElseThrow(() -> UserNotFoundException.EXCEPTION);

        Chatting chatMessage =
                Chatting.builder().workspace(workspace).sender(user).content(message).build();

        chattingRepository.save(chatMessage);

        ChatMessageDetailResponse detailResponse =
                new ChatMessageDetailResponse(message, user.getUid(), chatMessage.getCreatedAt());
        return detailResponse;
    }

    @Transactional
    public void readMessageCnt(Long chatRoomId, Long userPk) {
        // 채팅방 나갈 시점에서의 메세지 개수 조회
        int messageCnt = chattingRepository.countMessageByRoomId(chatRoomId);
        // chatlog 테이블 속 readCount 업데이트
        chatlogService.updateReadCount(chatRoomId, userPk, messageCnt);
    }
}

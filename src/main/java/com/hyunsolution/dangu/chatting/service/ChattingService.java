package com.hyunsolution.dangu.chatting.service;

import com.hyunsolution.dangu.chatlog.domain.ChatLog;
import com.hyunsolution.dangu.chatlog.domain.ChatLogRepository;
import com.hyunsolution.dangu.chatlog.exception.ChatLogNotFoundException;
import com.hyunsolution.dangu.chatting.domain.Chatting;
import com.hyunsolution.dangu.chatting.domain.ChattingRepository;
import com.hyunsolution.dangu.chatting.dto.response.GetChatRoomsResponse;
import com.hyunsolution.dangu.chatting.dto.response.GetChattingsResponse;
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
}

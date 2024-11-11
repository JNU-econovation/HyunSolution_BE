package com.hyunsolution.dangu.chatting.dto.service;

import com.hyunsolution.dangu.chatting.domain.ChatRepository;
import com.hyunsolution.dangu.chatting.domain.Chatting;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageDetailResponse;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatMessageDetailResponse sendMessage(Long chatRoomId, String message, Long userPk) {

        Workspace workspace =
                workspaceRepository
                        .findById(chatRoomId)
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 채팅방 id입니다."));
        User user =
                userRepository
                        .findById(userPk)
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 id입니다."));

        Chatting chatMessage =
                Chatting.builder().workspace(workspace).user(user).content(message).build();

        chatRepository.save(chatMessage);

        ChatMessageDetailResponse detailResponse =
                new ChatMessageDetailResponse(message, user.getUid(), chatMessage.getCreatedAt());
        return detailResponse;
    }
}

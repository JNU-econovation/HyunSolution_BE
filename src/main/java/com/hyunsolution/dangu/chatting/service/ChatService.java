package com.hyunsolution.dangu.chatting.service;

import com.hyunsolution.dangu.chatlog.service.ChatlogService;
import com.hyunsolution.dangu.chatting.domain.ChatRepository;
import com.hyunsolution.dangu.chatting.domain.Chatting;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageDetailResponse;
import com.hyunsolution.dangu.chatting.exception.ChatRoomNotFoundException;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.exception.UserNotFoundException;
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
    private final ChatlogService chatlogService;

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

        chatRepository.save(chatMessage);

        ChatMessageDetailResponse detailResponse =
                new ChatMessageDetailResponse(message, user.getUid(), chatMessage.getCreatedAt());
        return detailResponse;
    }

    @Transactional
    public void readMessageCnt(Long chatRoomId, Long userPk) {
        // 채팅방 나갈 시점에서의 메세지 개수 조회
        int messageCnt = chatRepository.countMessageByRoomId(chatRoomId);
        // chatlog 테이블 속 readCount 업데이트
        chatlogService.updateReadCount(chatRoomId, userPk, messageCnt);
    }
}

package com.hyunsolution.dangu.chatlog.service;

import com.hyunsolution.dangu.chatlog.domain.ChatLog;
import com.hyunsolution.dangu.chatlog.domain.ChatLogRepository;
import com.hyunsolution.dangu.chatlog.exception.ChatLogNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatlogService {
    private final ChatLogRepository chatLogRepository;

    // 채팅방별 사용자의 읽은 채팅 개수 업데이트
    @Transactional
    public void updateReadCount(Long chatRoomId, Long userPk, int messageCnt) {
        chatLogRepository.updateCount(chatRoomId, userPk, messageCnt);
    }

    public ChatLog findChatLog(Long chatRoomId, Long userPk) {
        return chatLogRepository
                .findByChatRoomIdAndUserId(chatRoomId, userPk)
                .orElseThrow(() -> ChatLogNotFoundException.EXCEPTION);
    }
}

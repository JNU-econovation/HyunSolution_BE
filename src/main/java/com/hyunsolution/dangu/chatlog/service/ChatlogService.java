package com.hyunsolution.dangu.chatlog.service;

import com.hyunsolution.dangu.chatlog.chatlogRepository.ChatlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatlogService {
    private final ChatlogRepository chatlogRepository;

    // 채팅방별 사용자의 읽은 채팅 개수 업데이트
    @Transactional
    public void updateReadCount(Long chatRoomId, Long userPk, int messageCnt) {
        chatlogRepository.updateCount(chatRoomId, userPk, messageCnt);
    }
}

package com.hyunsolution.dangu.chatRoom.service;

import com.hyunsolution.dangu.chatRoom.domain.ChatRoom;
import com.hyunsolution.dangu.chatRoom.domain.ChatRoomRepository;
import com.hyunsolution.dangu.chatRoom.exception.ChatRoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    @Transactional
    public ChatRoom findChatRoom(Long chatRoomId) {
        ChatRoom chatRoom =
                chatRoomRepository
                        .findById(chatRoomId)
                        .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
        return chatRoom;
    }

    @Transactional
    public void updateChatRoom(ChatRoom chatRoom) {
        chatRoom.updateChatTime(); // 채팅 입력 시간에 따른 채팅방 ch_update_at 업데이트
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }
}

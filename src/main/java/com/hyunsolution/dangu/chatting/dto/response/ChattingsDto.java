package com.hyunsolution.dangu.chatting.dto.response;

import com.hyunsolution.dangu.chatting.domain.MessageType;

public record ChattingsDto(
        String content, Long chattingId, boolean isOwn, MessageType messageType) {
    public static ChattingsDto of(
            String content, Long chattingId, boolean isOwn, MessageType messageType) {
        return new ChattingsDto(content, chattingId, isOwn, messageType);
    }
}

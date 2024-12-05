package com.hyunsolution.dangu.chatting.dto.response;

public record ChattingsDto(String content, Long chattingId, boolean isOwn) {
    public static ChattingsDto of(String content, Long chattingId, boolean isOwn) {
        return new ChattingsDto(content, chattingId, isOwn);
    }
}

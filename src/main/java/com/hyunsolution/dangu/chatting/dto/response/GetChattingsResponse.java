package com.hyunsolution.dangu.chatting.dto.response;

public record GetChattingsResponse(String content, Long chattingId, boolean isOwn) {
    public static GetChattingsResponse of(String content, Long chattingId, boolean isOwn) {
        return new GetChattingsResponse(content, chattingId, isOwn);
    }
}

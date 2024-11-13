package com.hyunsolution.dangu.chatting.dto.response;

public record ChatMessageResponse(
        String status, ChatMessageDetailResponse response, String error) {}

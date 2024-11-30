package com.hyunsolution.dangu.chatting.dto.response;

import com.hyunsolution.dangu.chatting.domain.MessageType;

public record ChatMessageDetailResponse(String senderId, String message, MessageType messageType) {}

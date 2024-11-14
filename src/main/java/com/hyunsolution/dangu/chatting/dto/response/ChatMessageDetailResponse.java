package com.hyunsolution.dangu.chatting.dto.response;

import java.time.LocalDateTime;

public record ChatMessageDetailResponse(String message, String senderId, LocalDateTime createAt) {}

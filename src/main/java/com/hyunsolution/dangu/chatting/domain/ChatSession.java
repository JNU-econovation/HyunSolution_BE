package com.hyunsolution.dangu.chatting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatSession {
    private Long userId;
    private Long roomId;
}

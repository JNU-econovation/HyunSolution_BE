package com.hyunsolution.dangu.chatting.domain;

public enum MessageType {
    TEXT, // 일반 텍스트
    SYSTEM, // 시스템 메시지(매칭 신청 메시지)
    STARTGAME //시스템 메시지(매칭 완료 및 게임 시작 안내)
}

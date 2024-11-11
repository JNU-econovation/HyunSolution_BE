package com.hyunsolution.dangu.common.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DomainEvent {
    private final LocalDateTime publishAt;

    public DomainEvent() {
        this.publishAt = LocalDateTime.now();
    }
}
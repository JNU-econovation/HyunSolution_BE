package com.hyunsolution.dangu.game.dto;

public record GetBillingDto(long cost, int gameRound) {
    public static GetBillingDto of(long cost, int gameRound) {
        return new GetBillingDto(cost, gameRound);
    }
}

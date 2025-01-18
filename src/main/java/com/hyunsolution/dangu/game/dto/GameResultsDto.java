package com.hyunsolution.dangu.game.dto;

import com.hyunsolution.dangu.game.domain.GameResult;

public record GameResultsDto(
        String nickname, boolean isOwn, Integer startScore, Integer finalScore, boolean winner) {
    public static GameResultsDto of(String nickname, boolean isOwn, GameResult gameResult) {
        return new GameResultsDto(
                nickname,
                isOwn,
                gameResult.getStartScore(),
                gameResult.getFinalScore(),
                gameResult.getWinner());
    }
}

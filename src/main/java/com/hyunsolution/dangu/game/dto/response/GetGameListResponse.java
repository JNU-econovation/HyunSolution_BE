package com.hyunsolution.dangu.game.dto.response;

public record GetGameListResponse(
        Long gameId, String myNickname, String opponentNickname, String winnerNickname) {}

package com.hyunsolution.dangu.game.dto;

import com.hyunsolution.dangu.user.domain.User;

public record GetGameListDto(
        Long gameId, User myself, User opponent, Boolean myWin, Boolean opponentWin) {}

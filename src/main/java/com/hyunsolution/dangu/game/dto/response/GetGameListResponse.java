package com.hyunsolution.dangu.game.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record GetGameListResponse(
        Long gameId,
        String myNickname,
        String opponentNickname,
        String winnerNickname,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
                LocalDateTime date) {}

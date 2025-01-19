package com.hyunsolution.dangu.game.dto.response;

import com.hyunsolution.dangu.game.dto.GameResultsDto;
import java.util.List;

public record GetGameResultsResponse(
        Long workspaceId, List<GameResultsDto> results, long gameTime) {
    public static GetGameResultsResponse of(
            Long gameId, List<GameResultsDto> results, long gameTime) {
        return new GetGameResultsResponse(gameId, results, gameTime);
    }
}

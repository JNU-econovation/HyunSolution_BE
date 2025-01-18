package com.hyunsolution.dangu.game.controller;

import com.hyunsolution.dangu.common.apiResponse.ApiResponse;
import com.hyunsolution.dangu.game.dto.request.GetGameScoreRequest;
import com.hyunsolution.dangu.game.dto.request.MoreGameRequest;
import com.hyunsolution.dangu.game.dto.response.GetBillingResponse;
import com.hyunsolution.dangu.game.dto.response.GetGameListResponse;
import com.hyunsolution.dangu.game.dto.response.GetGameResultsResponse;
import com.hyunsolution.dangu.game.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping("/games/{gameId}") // e.g.  /game/{workspaceId}?tableNumber=5
    @Operation(summary = "당구대 번호를 입력한다.", description = "방장이 입력한 당구대 번호를 저장한다.")
    public ApiResponse<Void> saveTableNumber(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable("gameId") Long gameId,
            @RequestParam("tableNumber") int tableNumber) {
        gameService.saveTableNumber(gameId, tableNumber);
        return ApiResponse.success(null);
    }

    @PostMapping("/games/{gameId}/score")
    @Operation(summary = "참여자 별 게임 시작점수와 득점(최종)점수를 입력한다.")
    public ApiResponse<Void> saveScore(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable("gameId") Long gameId,
            @RequestBody GetGameScoreRequest request) {
        gameService.saveGameScore(gameId, request, userId);
        return ApiResponse.success(null);
    }

    @GetMapping("/games")
    @Operation(
            summary = "대전 페이지에서 경기목록을 조회한다.",
            description =
                    "게임방 id, 사용자 닉네임, 상대방 닉네임, 승자 닉네임를 응답으로 보내준다. 다만, 승자가 아직 없을 경우에는 'none'을 반환한다.")
    public ApiResponse<List<GetGameListResponse>> getGameList(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId) {
        List<GetGameListResponse> response = gameService.getGameList(userId);
        return ApiResponse.success(response);
    }

    @PostMapping("/games/{gameId}/more")
    @Operation(
            summary = "게임이 끝나고 한 게임 더 경기를 진행한다.",
            description = "사용자가 한 판 더 버튼을 클릭했을 때 보내는 요청으로 사용자는 당구대 번호 입력부터 다시 진행하게 됩니다.")
    public ApiResponse<Void> moreGame(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable("gameId") Long gameId,
            @RequestBody MoreGameRequest request) {
        request.getOpponentIds().add(userId);
        gameService.moreGame(gameId, request.getOpponentIds());
        return ApiResponse.success(null);
    }

    @GetMapping("/games/{gameId}/results")
    @Operation(
            summary = "게임 결과를 조회한다.",
            description = "게임 결과를 조회한다. 게임 결과는 사용자 id, 시작점수, 최종점수, 승자 여부를 응답으로 보내준다.")
    public ApiResponse<GetGameResultsResponse> getGameResults(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable("gameId") Long gameId) {
        GetGameResultsResponse response = gameService.getGameResults(userId, gameId);
        return ApiResponse.success(response);
    }

    @GetMapping("/games/{workspaceId}/billing")
    @Operation(summary = "게임 최종 계산서를 조회한다.", description = "게임 최종 계산서를 조회한다.")
    public ApiResponse<GetBillingResponse> getBilling(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable("workspaceId") Long workspaceId) {
        GetBillingResponse response = gameService.getBilling(userId, workspaceId);
        return ApiResponse.success(response);
    }
}

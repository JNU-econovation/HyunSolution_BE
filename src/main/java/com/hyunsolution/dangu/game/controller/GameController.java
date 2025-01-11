package com.hyunsolution.dangu.game.controller;

import com.hyunsolution.dangu.common.apiResponse.ApiResponse;
import com.hyunsolution.dangu.game.dto.request.GetGameScoreRequest;
import com.hyunsolution.dangu.game.dto.response.EnterGameRoomResponse;
import com.hyunsolution.dangu.game.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping("/game/{workspaceId}")
    @Operation(
            summary = "게임방에 입장한다.",
            description = "게임방에 입장할 시 방장이라면 당구대 번호 입력창을, 방문자면 대기화면을 띄운다.")
    public ApiResponse<EnterGameRoomResponse> getGameRoom(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable("workspaceId") Long workspaceId) {
        EnterGameRoomResponse response = gameService.enterGameRoom(workspaceId, userId);
        return ApiResponse.success(response);
    }

    @PostMapping("/game/{workspaceId}") // e.g.  /game/{workspaceId}?tableNumber=5
    @Operation(summary = "당구대 번호를 입력한다.", description = "방장이 입력한 당구대 번호를 저장한다.")
    public ApiResponse<Void> saveTableNumber(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable("workspaceId") Long workspaceId,
            HttpServletRequest request) {
        int tableNumber = Integer.parseInt(request.getParameter("tableNumber"));
        gameService.saveTableNumber(workspaceId, tableNumber);
        return ApiResponse.success(null);
    }

    @PostMapping("/game/{worspaceId}/score")
    @Operation(summary = "참여자 별 게임 시작점수와 득점(최종)점수를 입력한다.")
    public ApiResponse<Void> saveScore(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable("workspaceId") Long workspaceId,
            @RequestBody GetGameScoreRequest request) {
        gameService.saveGameScore(workspaceId, userId, request);
        return ApiResponse.success(null);
    }
}

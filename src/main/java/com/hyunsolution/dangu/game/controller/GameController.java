package com.hyunsolution.dangu.game.controller;

import com.hyunsolution.dangu.common.apiResponse.ApiResponse;
import com.hyunsolution.dangu.game.dto.request.GetGameScoreRequest;
import com.hyunsolution.dangu.game.dto.response.EnterGameRoomResponse;
import com.hyunsolution.dangu.game.dto.response.GetGameListResponse;
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

    @GetMapping("/games/{workspaceId}")
    @Operation(
            summary = "게임방에 입장한다.",
            description = "게임방에 입장할 시 방장이라면 당구대 번호 입력창을, 방문자면 대기화면을 띄운다.")
    public ApiResponse<EnterGameRoomResponse> enterGameRoom(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable("workspaceId") Long workspaceId) {
        EnterGameRoomResponse response = gameService.enterGameRoom(workspaceId, userId);
        return ApiResponse.success(response);
    }

    @PostMapping("/games/{workspaceId}") // e.g.  /game/{workspaceId}?tableNumber=5
    @Operation(summary = "당구대 번호를 입력한다.", description = "방장이 입력한 당구대 번호를 저장한다.")
    public ApiResponse<Void> saveTableNumber(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable("workspaceId") Long workspaceId,
            @RequestParam("tableNumber") int tableNumber) {
        gameService.saveTableNumber(workspaceId, tableNumber);
        return ApiResponse.success(null);
    }

    @PostMapping("/games/{workspaceId}/score")
    @Operation(summary = "참여자 별 게임 시작점수와 득점(최종)점수를 입력한다.")
    public ApiResponse<Void> saveScore(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable("workspaceId") Long workspaceId,
            @RequestBody GetGameScoreRequest request) {
        gameService.saveGameScore(workspaceId, userId, request);
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
}

package com.hyunsolution.dangu.workspace.controller;

import com.hyunsolution.dangu.common.apiResponse.ApiResponse;
import com.hyunsolution.dangu.workspace.dto.response.GetWorkspacesResponse;
import com.hyunsolution.dangu.workspace.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WorkSpaceController {
    private final WorkspaceService workSpaceService;

    @PostMapping("/workspace")
    @Operation(summary = "워크스페이스(매칭) 생성", description = "워크스페이스(매칭)를 생성한다.")
    public ApiResponse<Void> addWorkSpace(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long id) {
        workSpaceService.addWorkspace(id);
        return ApiResponse.successResponseNull();
    }

    @GetMapping("/workspaces")
    @Operation(summary = "워크스페이스(매칭) 목록조회", description = "매칭이 완료되지 않고 대기 하고 있는 매칭만 조회합니다.")
    public ApiResponse<List<GetWorkspacesResponse>> getWorkspaces(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long id) {
        List<GetWorkspacesResponse> responses = workSpaceService.getWorkspaces(id);
        return ApiResponse.success(responses);
    }

    @PostMapping("/workspace/{workspaceId}")
    @Operation(summary = "게임방 등록 취소", description = "게임방을 등록한 방장은 게임방 등록을 취소할 수 있다.")
    public ApiResponse<Void> deleteWorkspace(@PathVariable Long workspaceId) {
        workSpaceService.deleteWorkspace(workspaceId);
        return ApiResponse.successResponseNull();
    }
}

package com.hyunsolution.dangu.workspace.controller;

import com.hyunsolution.dangu.workspace.dto.response.GetWorkspacesResponse;
import com.hyunsolution.dangu.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorkSpaceController {
    private final WorkspaceService workSpaceService;

    // TODO: @다다 님이 ApiResponse 개발하면 수정할 예정
    @PostMapping("/workspace")
    public void addWorkSpace(@RequestHeader("Authorization") String id) {
        workSpaceService.addWorkspace(Long.valueOf(id));
    }

    @GetMapping("/workspaces")
    public List<GetWorkspacesResponse> getWorkspaces() {
        return workSpaceService.getWorkspaces();
    }
}

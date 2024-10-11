package com.hyunsolution.dangu.workspace.controller;

import com.hyunsolution.dangu.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WorkSpaceController {
    private final WorkspaceService workSpaceService;

    // TODO: @다다 님이 ApiResponse 개발하면 수정할 예정
    @PostMapping("/workspace")
    public void addWorkSpace(@RequestHeader("Authorization") String id) {
        workSpaceService.addWorkspace(Long.valueOf(id));
    }
}

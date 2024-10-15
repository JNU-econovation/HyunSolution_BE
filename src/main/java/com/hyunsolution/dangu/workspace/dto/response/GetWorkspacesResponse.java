package com.hyunsolution.dangu.workspace.dto.response;

public record GetWorkspacesResponse(Long workspaceId, String creatorUid) {
    public static GetWorkspacesResponse of(Long workspaceId, String creatorUid) {
        return new GetWorkspacesResponse(workspaceId, creatorUid);
    }
}

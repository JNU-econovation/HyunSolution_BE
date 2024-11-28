package com.hyunsolution.dangu.workspace.dto.response;

public record GetWorkspacesResponse(Long workspaceId, String creatorUid, boolean isOwn) {
    public static GetWorkspacesResponse of(Long workspaceId, String creatorUid, boolean isOwn) {
        return new GetWorkspacesResponse(workspaceId, creatorUid, isOwn);
    }
}

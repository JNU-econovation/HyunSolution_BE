package com.hyunsolution.dangu.workspace.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "워크스페이스 목록 조회 응답")
public record GetWorkspacesResponse(
        @Schema(description = "워크스페이스 pk") Long workspaceId,
        @Schema(description = "워크스페이스 방장 uid") String creatorUid,
        @Schema(description = "자신이 만든 워크스페이스 인지 아닌지") boolean isOwn) {
    public static GetWorkspacesResponse of(Long workspaceId, String creatorUid, boolean isOwn) {
        return new GetWorkspacesResponse(workspaceId, creatorUid, isOwn);
    }
}

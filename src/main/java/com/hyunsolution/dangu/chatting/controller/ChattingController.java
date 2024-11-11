package com.hyunsolution.dangu.chatting.controller;

import com.hyunsolution.dangu.chatting.dto.response.GetChattingsResponse;
import com.hyunsolution.dangu.chatting.service.ChattingService;
import com.hyunsolution.dangu.common.apiResponse.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChattingController {
    private final ChattingService chattingService;

    @GetMapping("/chattings/{workspaceId}")
    @Operation(summary = "채팅방에 대한 채팅을 조회한다.", description = "워크 스페이스(채팅방)에 대한 채팅을 조회한다.")
    public ApiResponse<List<GetChattingsResponse> >findByWorkspace(@RequestHeader("Authorization") Long userId, @PathVariable Long workspaceId) {
        List<GetChattingsResponse> responses = chattingService.findByWorkspaceId(userId, workspaceId);
        return ApiResponse.success(responses);
    }
}

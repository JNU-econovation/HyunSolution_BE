package com.hyunsolution.dangu.chatting.controller;

import com.hyunsolution.dangu.chatting.dto.response.GetChatRoomsResponse;
import com.hyunsolution.dangu.chatting.dto.response.GetChattingsResponse;
import com.hyunsolution.dangu.chatting.service.ChattingService;
import com.hyunsolution.dangu.common.apiResponse.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChattingController {
    private final ChattingService chattingService;

    @GetMapping("/chattings/{chatRoomId}")
    @Operation(
            summary = "채팅방에 대한 채팅을 조회한다.",
            description = "워크스페이스(채팅방)에 대한 채팅을 조회한다. 워크스페이스 당 채팅방 하나 이기 때문에 워크스페이스를 채팅방이랑 같다고 생각")
    public ApiResponse<List<GetChattingsResponse>> getChattings(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable Long chatRoomId) {
        List<GetChattingsResponse> responses = chattingService.getChattings(userId, chatRoomId);
        return ApiResponse.success(responses);
    }

    @GetMapping("/chattings")
    @Operation(summary = "채팅방 목록을 조회한다.", description = "자신이 들어가 있는 채팅방 목록을 조회한다.")
    public ApiResponse<List<GetChatRoomsResponse>> getChatRooms(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId) {
        List<GetChatRoomsResponse> responses = chattingService.getChatRooms(userId);
        return ApiResponse.success(responses);
    }
}

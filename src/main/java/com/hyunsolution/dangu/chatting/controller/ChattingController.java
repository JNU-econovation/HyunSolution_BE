package com.hyunsolution.dangu.chatting.controller;

import com.hyunsolution.dangu.chatting.dto.request.ChatMessageRequest;
import com.hyunsolution.dangu.chatting.dto.response.*;
import com.hyunsolution.dangu.chatting.service.ChattingService;
import com.hyunsolution.dangu.common.apiResponse.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChattingController {
    private final ChattingService chattingService;

    @GetMapping("/chattings/{chatRoomId}")
    @Operation(
            summary = "채팅방에 대한 채팅을 조회한다.",
            description =
                    "워크스페이스(채팅방)에 대한 채팅을 조회한다. "
                            + "메세지 타입은 TEXT, SYSTEM, STARTGAME 세가지로 나뉘며 각각 기본 메시지, 매칭신청에 따른 시스템 문자, 매칭확정에 따른 시스템 문자를 의미한다.")
    public ApiResponse<GetChattingsResponse> getChattings(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId,
            @PathVariable Long chatRoomId) {
        GetChattingsResponse responses = chattingService.getChattings(userId, chatRoomId);
        return ApiResponse.success(responses);
    }

    @GetMapping("/chattings")
    @Operation(summary = "채팅방 목록을 조회한다.", description = "자신이 들어가 있는 채팅방 목록을 조회한다.")
    public ApiResponse<List<GetChatRoomsResponse>> getChatRooms(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long userId) {
        List<GetChatRoomsResponse> responses = chattingService.getChatRooms(userId);
        return ApiResponse.success(responses);
    }

    // 채팅기능
    @MessageMapping("/chat/{chatRoomId}")
    @SendTo("/topic/chat/{chatRoomId}")
    public ChatMessageResponse sendChatMessage(
            @DestinationVariable Long chatRoomId,
            @Payload ChatMessageRequest requestMessage,
            StompHeaderAccessor headerAccessor) {
        Long userPk = Long.valueOf(headerAccessor.getFirstNativeHeader("Authorization"));

        ChatMessageDetailResponse response =
                chattingService.sendMessage(chatRoomId, requestMessage.message(), userPk);

        return new ChatMessageResponse("success", response, null);
    }
}

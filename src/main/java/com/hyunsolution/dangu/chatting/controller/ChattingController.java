package com.hyunsolution.dangu.chatting.controller;

import com.hyunsolution.dangu.chatting.dto.request.ChatMessageRequest;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageDetailResponse;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageResponse;
import com.hyunsolution.dangu.chatting.dto.response.GetChatRoomsResponse;
import com.hyunsolution.dangu.chatting.dto.response.GetChattingsResponse;
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

    @Operation(summary = "채팅방 나가기", description = "채팅방을 나갈 시 해당 채팅방의 전체 메세지 개수를 저장합니다.")
    @PostMapping("/chat/{chatRoomId}/exit")
    public ApiResponse exitChat(
            @PathVariable Long chatRoomId, @RequestHeader("Authorization") Long userPk) {
        chattingService.readMessageCnt(userPk, chatRoomId);
        return ApiResponse.success(true);
    }
}

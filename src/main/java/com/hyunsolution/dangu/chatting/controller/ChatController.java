package com.hyunsolution.dangu.chatting.controller;

import com.hyunsolution.dangu.chatting.dto.request.ChatMessageRequest;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageDetailResponse;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageResponse;
import com.hyunsolution.dangu.chatting.service.ChatService;
import com.hyunsolution.dangu.common.apiResponse.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // 채팅기능
    @MessageMapping("/chat/{chatRoomId}")
    @SendTo("/topic/chat/{chatRoomId}")
    public ChatMessageResponse sendChatMessage(
            @DestinationVariable Long chatRoomId,
            @Payload ChatMessageRequest requestMessage,
            StompHeaderAccessor headerAccessor) {
        Long userPk = Long.valueOf(headerAccessor.getFirstNativeHeader("Authorization"));

        ChatMessageDetailResponse response =
                chatService.sendMessage(chatRoomId, requestMessage.message(), userPk);

        return new ChatMessageResponse("success", response, null);
    }

    @Operation(summary = "채팅방 나가기", description = "채팅방을 나갈 시 해당 채팅방의 전체 메세지 개수를 저장합니다.")
    @PostMapping("/chat/{chatRoomId}/exit")
    public ApiResponse exitChat(
            @PathVariable Long chatRoomId, @RequestHeader("Authorization") Long userPk) {
        chatService.readMessageCnt(userPk, chatRoomId);
        return ApiResponse.success(true);
    }
}

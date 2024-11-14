package com.hyunsolution.dangu.chatting.controller;

import com.hyunsolution.dangu.chatting.dto.request.ChatMessageRequest;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageDetailResponse;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageResponse;
import com.hyunsolution.dangu.chatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
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
}

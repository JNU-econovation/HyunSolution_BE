package com.hyunsolution.dangu.common.config.webSocket;

import com.hyunsolution.dangu.chatting.service.ChattingService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketListenerConfig {
    private final ChattingService chattingService;

    public WebSocketListenerConfig(ChattingService chattingService) {
        this.chattingService = chattingService;
    }

    // 연결
    @EventListener(SessionConnectEvent.class)
    public void onConnect(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("onConnect here");
        String sessionId = getSessionId(event);
        Long userId = Long.valueOf(headerAccessor.getFirstNativeHeader("Authorization"));
        Long roomId = Long.valueOf(headerAccessor.getFirstNativeHeader("RoomId"));
        chattingService.getChatRoom(sessionId, userId, roomId);
    }

    // 연결 종료
    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
        chattingService.leaveChatRoom(sessionId);
        System.out.println("session 연결 종료: " + sessionId);
    }


    private String getSessionId(AbstractSubProtocolEvent event) {
        return (String) event.getMessage().getHeaders().get("simpSessionId");
    }

}

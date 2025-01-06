package com.hyunsolution.dangu.common.config.webSocket;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketSwaggerDocsController {
    @Operation(
            summary = "WebSocket 연결 정보",
            description =
                    """
            WebSocket URL, 구독 경로 및 발행 경로 안내
            - WebSocket 접속 URL: wss://hyunsolution.duckdns.org/chat
            - STOMP 구독 주소: /topic/chat/{chatRoomId}
            - 메시지 전송 주소: /app/chat/{chatRoomId}
            - 헤더 속 정보: "Authorization"에 사용자 id, "RoomId"에 방번호 필요
            """)
    @GetMapping("/websocket/info") // 스웨거 웹소켓관련 정보 제공을 위한 api
    public void getWebSocketInfo() {}
}

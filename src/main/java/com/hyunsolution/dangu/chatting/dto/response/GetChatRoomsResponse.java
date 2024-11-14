package com.hyunsolution.dangu.chatting.dto.response;

import java.util.List;

public record GetChatRoomsResponse(
        Long chatRoomId, String lastMessage, List<String> otherPerson, int unReadCount) {
    public static GetChatRoomsResponse of(
            Long chatRoomId, String lastMessage, List<String> otherPerson, int unReadCount) {
        return new GetChatRoomsResponse(chatRoomId, lastMessage, otherPerson, unReadCount);
    }
}

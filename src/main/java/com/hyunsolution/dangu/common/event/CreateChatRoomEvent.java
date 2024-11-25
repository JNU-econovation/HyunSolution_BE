package com.hyunsolution.dangu.common.event;

import com.hyunsolution.dangu.chatRoom.domain.ChatRoom;
import com.hyunsolution.dangu.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateChatRoomEvent extends DomainEvent {
    private ChatRoom chatRoom;
    private User visitor;
    private User creator;

    public static CreateChatRoomEvent of(ChatRoom chatRoom, User visitor, User creator) {
        return new CreateChatRoomEvent(chatRoom, visitor, creator);
    }
}

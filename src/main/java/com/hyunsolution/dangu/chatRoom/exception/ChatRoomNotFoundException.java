package com.hyunsolution.dangu.chatRoom.exception;

import com.hyunsolution.dangu.common.exception.CustomException;

public class ChatRoomNotFoundException extends CustomException {
    public static final ChatRoomNotFoundException EXCEPTION = new ChatRoomNotFoundException();

    private ChatRoomNotFoundException() {
        super(ChatRoomError.CHATROOM_NOT_FOUND);
    }
}

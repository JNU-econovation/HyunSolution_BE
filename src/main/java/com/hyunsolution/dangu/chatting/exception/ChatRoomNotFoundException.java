package com.hyunsolution.dangu.chatting.exception;

import com.hyunsolution.dangu.common.exception.CustomException;

public class ChatRoomNotFoundException extends CustomException {
    public static final ChatRoomNotFoundException EXCEPTION = new ChatRoomNotFoundException();

    private ChatRoomNotFoundException() {
        super(ChatError.CHATROOM_NOT_FOUND);
    }
}

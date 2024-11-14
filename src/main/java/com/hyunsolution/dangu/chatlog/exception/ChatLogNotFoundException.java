package com.hyunsolution.dangu.chatlog.exception;

import com.hyunsolution.dangu.common.exception.CustomException;

public class ChatLogNotFoundException extends CustomException {
    public static final ChatLogNotFoundException EXCEPTION = new ChatLogNotFoundException();

    private ChatLogNotFoundException() {
        super(ChatLogError.CHAT_LOG_NOT_FOUND);
    }
}

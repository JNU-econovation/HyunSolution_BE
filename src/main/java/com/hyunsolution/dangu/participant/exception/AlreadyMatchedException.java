package com.hyunsolution.dangu.participant.exception;

import com.hyunsolution.dangu.common.exception.CustomException;

public class AlreadyMatchedException extends CustomException {
    public static final AlreadyMatchedException EXCEPTION = new AlreadyMatchedException();

    private AlreadyMatchedException() {
        super(ParticipantError.ALREADY_MATCHED);
    }
}

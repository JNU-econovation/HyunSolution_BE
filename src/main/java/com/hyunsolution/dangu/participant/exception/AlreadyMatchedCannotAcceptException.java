package com.hyunsolution.dangu.participant.exception;

import com.hyunsolution.dangu.common.exception.CustomException;

public class AlreadyMatchedCannotAcceptException extends CustomException {
    public static final AlreadyMatchedCannotAcceptException EXCEPTION =
            new AlreadyMatchedCannotAcceptException();

    private AlreadyMatchedCannotAcceptException() {
        super(ParticipantError.ALREADY_MATCHED_CANNOT_ACCEPT);
    }
}

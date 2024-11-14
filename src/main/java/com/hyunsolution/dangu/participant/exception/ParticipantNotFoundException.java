package com.hyunsolution.dangu.participant.exception;

import com.hyunsolution.dangu.common.exception.CustomException;

public class ParticipantNotFoundException extends CustomException {
    public static final ParticipantNotFoundException EXCEPTION = new ParticipantNotFoundException();

    private ParticipantNotFoundException() {
        super(ParticipantError.PARTICIPANT_NOT_FOUND);
    }
}

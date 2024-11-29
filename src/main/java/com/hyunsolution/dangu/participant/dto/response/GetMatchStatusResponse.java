package com.hyunsolution.dangu.participant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMatchStatusResponse {
    private boolean counterpart;
    private boolean myself;
    private boolean matchResult;
}

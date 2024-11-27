package com.hyunsolution.dangu.participant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class GetMatchStatusResponse {
    private boolean counterpart;
    private boolean myself;
    private boolean matchResult;
}

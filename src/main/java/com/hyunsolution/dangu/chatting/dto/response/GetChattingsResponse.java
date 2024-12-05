package com.hyunsolution.dangu.chatting.dto.response;

import java.util.List;

public record GetChattingsResponse(List<String> otherPeople, List<ChattingsDto> chattings) {
    public static GetChattingsResponse of(List<String> otherName, List<ChattingsDto> chattings) {
        return new GetChattingsResponse(otherName, chattings);
    }
}

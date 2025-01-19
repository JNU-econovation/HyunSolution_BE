package com.hyunsolution.dangu.game.dto.response;

import com.hyunsolution.dangu.game.dto.GetBillingDto;
import java.util.List;

public record GetBillingResponse(long totalCost, List<GetBillingDto> info) {

    public static GetBillingResponse of(long totalCost, List<GetBillingDto> info) {
        return new GetBillingResponse(totalCost, info);
    }
}

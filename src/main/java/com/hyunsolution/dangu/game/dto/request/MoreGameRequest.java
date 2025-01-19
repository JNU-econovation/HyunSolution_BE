package com.hyunsolution.dangu.game.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MoreGameRequest {
    private List<Long> opponentIds;
}

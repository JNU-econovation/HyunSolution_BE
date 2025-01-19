package com.hyunsolution.dangu.game.exception;

import com.hyunsolution.dangu.common.exception.CustomException;

public class GameNotFoundException extends CustomException {
    public static final GameNotFoundException EXCEPTION = new GameNotFoundException();

    private GameNotFoundException() {
        super(GameError.GAME_NOT_FOUND);
    }
}

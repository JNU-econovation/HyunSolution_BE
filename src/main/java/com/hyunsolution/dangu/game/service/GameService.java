package com.hyunsolution.dangu.game.service;

import com.hyunsolution.dangu.game.domain.Game;
import com.hyunsolution.dangu.game.domain.GameRepository;
import com.hyunsolution.dangu.game.dto.response.EnterGameRoomResponse;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import com.hyunsolution.dangu.workspace.exception.WorkspaceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {
    private GameRepository gameRepository;
    private WorkspaceRepository workspaceRepository;

    @Transactional
    public EnterGameRoomResponse getGameRoom(Long workspaceId, Long userId) {

//게임방 멤버 수만큼 행이 생겨야함
    }

    @Transactional
    public void saveTableNumber(Long workspaceId, int tableNumber) {

    }
}

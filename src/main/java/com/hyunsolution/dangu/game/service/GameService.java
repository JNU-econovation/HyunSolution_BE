package com.hyunsolution.dangu.game.service;

import com.hyunsolution.dangu.game.domain.Game;
import com.hyunsolution.dangu.game.domain.GameRepository;
import com.hyunsolution.dangu.game.dto.response.EnterGameRoomResponse;
import com.hyunsolution.dangu.participant.domain.Participant;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import com.hyunsolution.dangu.workspace.exception.WorkspaceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameService {
    private GameRepository gameRepository;
    private WorkspaceRepository workspaceRepository;
    private ParticipantRepository participantRepository;

    @Transactional
    public EnterGameRoomResponse getGameRoom(Long workspaceId, Long userId) {

        Workspace workspace= workspaceRepository.findById(workspaceId)
                .orElseThrow(()-> WorkspaceNotFoundException.EXCEPTION);

        List<Participant> allUsersInRoom = participantRepository.findByWorkspaceId(workspaceId);

        for (Participant participant : allUsersInRoom) {
            User user= participant.getUser();
            Game gameDefault=Game.builder()
                    .user(user)
                    .workspace(workspace)
                    .build();
            gameRepository.save(gameDefault);
        }

        //방장 여부 확인
        boolean isRoomManager = workspace.getCreator().getId().equals(userId);
        return new EnterGameRoomResponse(isRoomManager);
    }

    @Transactional
    public void saveTableNumber(Long workspaceId, int tableNumber) {

    }
}

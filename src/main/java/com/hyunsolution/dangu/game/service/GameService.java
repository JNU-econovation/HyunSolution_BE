package com.hyunsolution.dangu.game.service;

import com.hyunsolution.dangu.chatRoom.domain.ChatRoom;
import com.hyunsolution.dangu.chatlog.domain.ChatLog;
import com.hyunsolution.dangu.chatlog.domain.ChatLogRepository;
import com.hyunsolution.dangu.game.domain.Game;
import com.hyunsolution.dangu.game.domain.GameRepository;
import com.hyunsolution.dangu.game.dto.request.GetGameScoreRequest;
import com.hyunsolution.dangu.game.dto.response.EnterGameRoomResponse;
import com.hyunsolution.dangu.game.dto.response.GetGameListResponse;
import com.hyunsolution.dangu.participant.domain.Participant;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import com.hyunsolution.dangu.workspace.exception.WorkspaceNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class GameService {
    private final UserRepository userRepository;
    private GameRepository gameRepository;
    private WorkspaceRepository workspaceRepository;
    private ParticipantRepository participantRepository;
    private ChatLogRepository chatLogRepository;

    @Transactional
    public EnterGameRoomResponse enterGameRoom(Long workspaceId, Long userId) {

        Workspace workspace =
                workspaceRepository
                        .findById(workspaceId)
                        .orElseThrow(() -> WorkspaceNotFoundException.EXCEPTION);

        List<Participant> allUsersInRoom = participantRepository.findByWorkspaceId(workspaceId);

        // 기본 데이터 삽입
        for (Participant participant : allUsersInRoom) {
            User user = participant.getUser();
            Game gameDefault = Game.builder().user(user).workspace(workspace).build();
            gameRepository.save(gameDefault);
        }

        // 방장 여부 확인
        boolean isRoomManager = workspace.getCreator().getId().equals(userId);
        return new EnterGameRoomResponse(isRoomManager);
    }

    @Transactional
    public void saveTableNumber(Long workspaceId, int tableNumber) {
        Workspace workspace =
                workspaceRepository
                        .findById(workspaceId)
                        .orElseThrow(() -> WorkspaceNotFoundException.EXCEPTION);

        workspace.setTableNumber(tableNumber);
    }

    @Transactional
    public void saveGameScore(Long workspaceId, Long userId, GetGameScoreRequest request) {
        Game game = gameRepository.findByWorkspaceIdAndUserId(workspaceId, userId);
        List<Game> games = gameRepository.findByWorkspaceId(workspaceId);
        for (Game gamePerPerson : games) {
            if (gamePerPerson.getEndTime() == null) {
                gamePerPerson.setEndTime(LocalDateTime.now());
            } else {
                break;
            }
        }

        // 점수 저장
        game.setStartScore(request.startScore());
        game.setFinalScore(request.finalScore());
    }

    @Transactional
    public List<GetGameListResponse> getGameList(Long userId) {
        List<GetGameListResponse> gameList = new ArrayList<>();

        // 사용자 닉네임
        String uid =
                userRepository
                        .findById(userId)
                        .map(user -> user.getUid())
                        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));
        List<ChatLog> allChatLog = chatLogRepository.findByUserId(userId);

        for (ChatLog chatLog : allChatLog) {
            ChatRoom eachChatRoom = chatLog.getChatRoom();

            if (eachChatRoom.isMatched()) {
                // 상대방 닉네임
                String opponentNickname =
                        chatLogRepository
                                .findUidByChatRoomIdAndUserId(eachChatRoom.getId(), userId)
                                .orElseThrow(() -> new NoSuchElementException("상대가 없는 채팅방입니다."));

                // 게임방 아이디
                Long workspaceId = eachChatRoom.getWorkspace().getId();
                // 게임방 승자 유무 및 닉네임
                List<Game> games = gameRepository.findByWorkspaceId(workspaceId);
                String winnerNickname = "none";
                for (Game game : games) {
                    log.info("game.getWinner():" + game.getWinner());
                    if (game.getWinner() == true) {
                        winnerNickname = game.getUser().getUid();
                        break;
                    }
                }
                GetGameListResponse response =
                        new GetGameListResponse(workspaceId, uid, opponentNickname, winnerNickname);
                gameList.add(response);
            }
        }
        return gameList;
    }
}

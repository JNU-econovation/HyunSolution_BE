package com.hyunsolution.dangu.game.service;

import com.hyunsolution.dangu.chatlog.domain.ChatLogRepository;
import com.hyunsolution.dangu.game.domain.Game;
import com.hyunsolution.dangu.game.domain.GameRepository;
import com.hyunsolution.dangu.game.domain.GameResult;
import com.hyunsolution.dangu.game.domain.GameResultRepository;
import com.hyunsolution.dangu.game.dto.GameResultsDto;
import com.hyunsolution.dangu.game.dto.GetBillingDto;
import com.hyunsolution.dangu.game.dto.GetGameListDto;
import com.hyunsolution.dangu.game.dto.request.GetGameScoreRequest;
import com.hyunsolution.dangu.game.dto.response.GetBillingResponse;
import com.hyunsolution.dangu.game.dto.response.GetGameListResponse;
import com.hyunsolution.dangu.game.dto.response.GetGameResultsResponse;
import com.hyunsolution.dangu.game.exception.GameNotFoundException;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.exception.UserNotFoundException;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
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
    private GameResultRepository gameResultRepository;

    @Transactional
    public void saveTableNumber(Long gameId, int tableNumber) {
        Game game = findGameById(gameId);
        game.setTableNumber(tableNumber);
    }

    @Transactional
    public void saveGameScore(Long gameId, GetGameScoreRequest request, Long userId) {
        Game game = findGameById(gameId);
        game.setEndTime(LocalDateTime.now());
        GameResult gameResult = gameResultRepository.findByGameIdAndUserId(gameId, userId);
        // 점수 저장
        gameResult.setStartScore(request.startScore());
        gameResult.setFinalScore(request.finalScore());

        if (allGameResultsHaveScores(game)) {
            saveWinner(game);
        }
    }

    private boolean allGameResultsHaveScores(Game game) {
        return game.getGameResults().stream()
                .allMatch(gr -> gr.getStartScore() != null && gr.getFinalScore() != null);
    }

    private void saveWinner(Game game) {
        game.getGameResults().stream()
                .max(Comparator.comparing(GameResult::calculateWin))
                .ifPresent(gameResult -> gameResult.setWinner(true));
    }

    @Transactional
    public List<GetGameListResponse> getGameList(Long userId) {
        List<GetGameListDto> gameListDto = gameResultRepository.findGameListDtoByMyId(userId);
        return gameListDto.stream()
                .map(
                        dto -> {
                            String myNickname = dto.myself().getUid();
                            String opponentNickname = dto.opponent().getUid();
                            String winnerNickname = calculateWinnerNickname(dto);
                            LocalDateTime endTime = dto.date();
                            return new GetGameListResponse(
                                    dto.gameId(),
                                    myNickname,
                                    opponentNickname,
                                    winnerNickname,
                                    endTime);
                        })
                .toList();
    }

    private String calculateWinnerNickname(GetGameListDto dto) {
        if (!dto.myWin() && !dto.opponentWin()) {
            return "none";
        }
        return dto.myWin() ? dto.myself().getUid() : dto.opponent().getUid();
    }

    @Transactional
    public void moreGame(Long gameId, List<Long> userIds) {
        Game game = findGameById(gameId);
        Integer newGameRound = game.getGameRound() + 1;
        Game newGame = Game.createGameWithRound(newGameRound, game.getWorkspace());
        List<GameResult> gameResults =
                userIds.stream()
                        .map(
                                userId -> {
                                    User user =
                                            userRepository
                                                    .findById(userId)
                                                    .orElseThrow(
                                                            () -> UserNotFoundException.EXCEPTION);
                                    return GameResult.builder().game(newGame).user(user).build();
                                })
                        .toList();
        gameRepository.save(newGame);
        gameResultRepository.saveAll(gameResults);
    }

    private Game findGameById(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> GameNotFoundException.EXCEPTION);
    }

    public GetGameResultsResponse getGameResults(Long myUserId, Long gameId) {
        List<GameResult> gameResults = gameResultRepository.findByGameId(gameId);
        List<GameResultsDto> gameResultsDto =
                gameResults.stream()
                        .map(
                                gameResult -> {
                                    String nickname = gameResult.getUser().getUid();
                                    boolean isOwn = gameResult.getUser().getId().equals(myUserId);
                                    return GameResultsDto.of(nickname, isOwn, gameResult);
                                })
                        .toList();
        long gameTime =
                gameRepository
                        .findById(gameId)
                        .orElseThrow(() -> GameNotFoundException.EXCEPTION)
                        .getGameTime();
        Game game = findGameById(gameId);
        return GetGameResultsResponse.of(game.getWorkspace().getId(), gameResultsDto, gameTime);
    }

    public GetBillingResponse getBilling(Long myUserId, Long workspaceId) {
        List<Game> games = gameRepository.findByWorkspaceId(workspaceId);

        List<GetBillingDto> billingDtos =
                games.stream()
                        .flatMap(
                                game ->
                                        game.getGameResults().stream()
                                                .filter(
                                                        gameResult ->
                                                                gameResult
                                                                                .getUser()
                                                                                .getId()
                                                                                .equals(myUserId)
                                                                        && !gameResult.getWinner())
                                                .map(
                                                        gameResult ->
                                                                GetBillingDto.of(
                                                                        game.calculateCost(),
                                                                        game.getGameRound())))
                        .toList();

        long totalCost = billingDtos.stream().mapToLong(GetBillingDto::cost).sum();

        return GetBillingResponse.of(totalCost, billingDtos);
    }
}

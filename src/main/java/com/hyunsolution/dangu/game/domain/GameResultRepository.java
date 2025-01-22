package com.hyunsolution.dangu.game.domain;

import com.hyunsolution.dangu.game.dto.GetGameListDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    @Query("select gr from GameResult gr where gr.user.id = :userId")
    List<GameResult> findByUserId(Long userId);

    @Query("select gr from GameResult gr where gr.game.id = :gameId and gr.user.id = :userId")
    GameResult findByGameIdAndUserId(Long gameId, Long userId);

    @Query(
            "SELECT new com.hyunsolution.dangu.game.dto.GetGameListDto("
                    + "g.id, gr1.user, gr2.user, gr1.winner, gr2.winner, g.endTime) "
                    + "FROM Game g "
                    + "JOIN GameResult gr1 ON g.id = gr1.game.id "
                    + "LEFT JOIN GameResult gr2 ON g.id = gr2.game.id AND gr1.user.id != gr2.user.id "
                    + "WHERE gr1.user.id = :myId")
    List<GetGameListDto> findGameListDtoByMyId(@Param("myId") Long myId);

    List<GameResult> findByGameId(Long gameId);
}

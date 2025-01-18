package com.hyunsolution.dangu.game.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByWorkspaceId(Long workspaceId);

    @Query(
            "select distinct g from Game g join g.gameResults gr on g.id = gr.game.id where gr.user.id = :userId and g.endTime is null")
    List<Game> findByGameResultUserIdAndEndTimeNull(Long userId);
}

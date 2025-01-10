package com.hyunsolution.dangu.game.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

    Game findByWorkspaceIdAndUserId(Long workspaceId, Long userId);

    List<Game> findByWorkspaceId(Long workspaceId);
}

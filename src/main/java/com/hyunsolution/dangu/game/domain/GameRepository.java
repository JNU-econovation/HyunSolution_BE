package com.hyunsolution.dangu.game.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    Game findByWorkspaceIdAndUserId (Long workspaceId, Long userId);

    List<Game> findByWorkspaceId(Long workspaceId);
}

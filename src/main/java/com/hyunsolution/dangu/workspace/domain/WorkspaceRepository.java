package com.hyunsolution.dangu.workspace.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    @Query(
            "select w from Workspace w where w.isMatched = false and w.createdAt > current_timestamp - 1 and w.isDeleted = false")
    List<Workspace> findUnmatchedAndCreatedWithinLastDay();

    @Query(
            "select count(w.id) > 0 from Workspace w where w.creator.id = :creatorId and w.createdAt > current_timestamp - 1")
    Boolean existsByCreatorId(Long creatorId);
}

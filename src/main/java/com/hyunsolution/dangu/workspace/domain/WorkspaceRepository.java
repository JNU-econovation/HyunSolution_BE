package com.hyunsolution.dangu.workspace.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    @Query(
            "select w from Workspace w where w.isMatched = false and w.createdAt >= :startTime and w.createdAt <= :endTime and w.isDeleted = false order by w.id desc ")
    List<Workspace> findUnmatchedAndCreatedWithinDay(
            LocalDateTime startTime, LocalDateTime endTime);

    @Query(
            "select count(w.id) > 0 from Workspace w where w.creator.id = :creatorId and  w.createdAt >= :startTime and w.createdAt <= :endTime and w.isDeleted = false")
    Boolean existsByCreatorIdWithinDay(
            Long creatorId, LocalDateTime startTime, LocalDateTime endTime);
}

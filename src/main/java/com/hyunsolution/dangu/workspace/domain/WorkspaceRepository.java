package com.hyunsolution.dangu.workspace.domain;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    @EntityGraph(attributePaths = {"participants"})
    @Query(
            "select w from Workspace w where exists (select 1 from Participant p where p.workspace = w and p.user.id = :userId) and w.chatUpdateAt is not null")
    List<Workspace> findByParticipantUserId(Long userId);
}

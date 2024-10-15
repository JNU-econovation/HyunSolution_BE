package com.hyunsolutiondemo.dangu.workspace.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query(value = "UPDATE Workspace w SET w.isMatched=true WHERE w.id=:workspaceId")
    void UpdateWorkspaceStatus(Long workspaceId);


}

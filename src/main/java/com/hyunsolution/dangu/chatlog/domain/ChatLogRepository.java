package com.hyunsolution.dangu.chatlog.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
    @Query("select cl from ChatLog cl where cl.workspace.id =:workspaceId and cl.user.id =:userId")
    Optional<ChatLog> findByWorkspaceIdAndUserId(Long workspaceId, Long userId);
}

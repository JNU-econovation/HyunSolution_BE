package com.hyunsolution.dangu.chatlog.chatlogRepository;

import com.hyunsolution.dangu.chatlog.domain.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatlogRepository extends JpaRepository<ChatLog, Long> {

    @Modifying
    @Query(
            "UPDATE ChatLog c SET c.readCount=:readCount WHERE c.user.id=:userId AND c.workspace.id=:workspaceId")
    void updateCount(
            @Param("userId") Long userId,
            @Param("workspaceId") Long workspaceId,
            @Param("readCount") int readCount);
}

package com.hyunsolution.dangu.chatting.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChattingRepository extends JpaRepository<Chatting, Long> {
    @Query("select c from Chatting c where c.workspace.id =:workspaceId")
    List<Chatting> findByWorkspaceId(Long workspaceId);

    @Query(
            value =
                    "select c.content from chatting c where c.id = ("
                            + "select MAX(id) from chatting where workspace_id = :workspaceId)",
            nativeQuery = true)
    String findLastChattingContentByWorkspaceId(Long workspaceId);
}

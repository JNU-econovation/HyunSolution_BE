package com.hyunsolution.dangu.chatting.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRepository extends JpaRepository<Chatting, Long> {
    List<Chatting> findByWorkspaceId(Long workspaceId);
}

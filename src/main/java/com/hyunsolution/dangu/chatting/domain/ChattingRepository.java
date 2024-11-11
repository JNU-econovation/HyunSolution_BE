package com.hyunsolution.dangu.chatting.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChattingRepository extends JpaRepository<Chatting, Long> {
    List<Chatting> findByWorkspaceId(Long workspaceId);
}

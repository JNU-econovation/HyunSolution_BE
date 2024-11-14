package com.hyunsolution.dangu.chatting.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chatting, Long> {

    @Query("SELECT COUNT(c) FROM Chatlog c WHERE c.workspace.id=:roomId")
    int countMessageByRoomId(@Param("roomId") Long roomId);
}

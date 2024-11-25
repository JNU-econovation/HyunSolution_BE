package com.hyunsolution.dangu.chatlog.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
    @Query("select cl from ChatLog cl where cl.chatRoom.id =:chatRoomId and cl.user.id =:userId")
    Optional<ChatLog> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    @Modifying
    @Query(
            "UPDATE ChatLog c SET c.readCount=:readCount WHERE c.user.id=:userId AND c.chatRoom.id=:chatRoomId")
    void updateCount(
            @Param("userId") Long userId,
            @Param("chatRoomId") Long chatRoomId,
            @Param("readCount") int readCount);
}

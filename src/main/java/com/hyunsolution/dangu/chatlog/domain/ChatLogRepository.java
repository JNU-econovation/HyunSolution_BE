package com.hyunsolution.dangu.chatlog.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
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

    // chatLog 속 사용자 id가 포함된 항목 추출
    @Query("SELECT c FROM ChatLog c WHERE c.user.id=:userId ")
    List<ChatLog> findByUserId(@Param("userId") Long userId);

    // 사용자가 포함된 게임방 속 상대방 닉네임 추출
    @Query(
            "SELECT c.user.uid FROM ChatLog c WHERE c.chatRoom.id= :chatRoomId AND c.user.id <> :userId ")
    Optional<String> findUidByChatRoomIdAndUserId(
            @Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);
}

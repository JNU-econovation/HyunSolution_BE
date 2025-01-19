package com.hyunsolution.dangu.chatting.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChattingRepository extends JpaRepository<Chatting, Long> {
    @Query("select c from Chatting c where c.chatRoom.id =:chatRoomId")
    List<Chatting> findByChatRoomId(Long chatRoomId);

    @Query(
            value =
                    "select c.content from chatting c where c.chat_room_id = :chatRoomId order by c.id desc limit 1",
            nativeQuery = true)
    String findLastChattingContentByChatRoomId(Long chatRoomId);

    @Query("SELECT COUNT(c) FROM ChatLog c WHERE c.chatRoom.id=:chatRoomId")
    int countMessageByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}

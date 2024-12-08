package com.hyunsolution.dangu.chatRoom.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query(
            "select c from ChatRoom c join fetch c.participants p join fetch p.user u where u.id = :userId")
    List<ChatRoom> findByParticipantUserIdWithEntityGraph(Long userId);

    @Query(
            "select distinct c from ChatRoom c "
                    + "join fetch c.participants p "
                    + "join fetch p.user "
                    + "where c.id = :chatRoomId")
    ChatRoom findByChatRoomIdWithFetchJoin(Long chatRoomId);
}

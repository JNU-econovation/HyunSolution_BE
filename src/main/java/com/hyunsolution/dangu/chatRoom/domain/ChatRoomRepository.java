package com.hyunsolution.dangu.chatRoom.domain;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @EntityGraph(attributePaths = {"participants.user"})
    @Query(
            "select c from ChatRoom c where exists (select 1 from Participant p where p.chatRoom = c and p.user.id = :userId)")
    List<ChatRoom> findByParticipantUserIdWithEntityGraph(Long userId);

    @Query(
            "select distinct c from ChatRoom c "
                    + "join fetch c.participants p "
                    + "join fetch p.user "
                    + "where c.id = :chatRoomId")
    ChatRoom findByIdWithFetchJoinParticipantsAndUSer(Long chatRoomId);
}

package com.hyunsolution.dangu.participant.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    // 같은 방에 있는 사람들 id(PK) 리스트
    @Query(value = "SELECT p FROM Participant p join fetch p.user WHERE p.chatRoom.id=:chatRoomId")
    List<Participant> findIdByChatRoomId(Long chatRoomId);

    // ID별 매칭버튼 클릭 여부
    @Query(
            "select count(p) > 0 from Participant p where p.id in :ids and p.participantMatch = false")
    Boolean existsByIdAndParticipantMatchFalse(List<Long> ids);

    // 개인 ID 찾기
    @Query(
            value =
                    "SELECT p from Participant p join fetch p.chatRoom c where p.user.id =:id and p.chatRoom.id=:chatRoomId")
    Optional<Participant> findByUserIdAndChatRoomId(
            @Param("id") Long id, @Param("chatRoomId") Long chatRoomId);

    @Query("SELECT p FROM Participant p WHERE p.user.id =:userId AND p.id IN :participantIds")
    Optional<Participant> findByUserIdInParticipantsId(Long userId, List<Long> participantIds);

    @Query(
            "SELECT p FROM Participant p WHERE p.chatRoom.isMatched=TRUE AND p.chatRoom.workspace.id=:workspaceId")
    List<Participant> findByWorkspaceId(Long workspaceId);
}

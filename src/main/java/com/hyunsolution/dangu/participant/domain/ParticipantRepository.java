package com.hyunsolution.dangu.participant.domain;

import com.hyunsolution.dangu.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<User, Long> {

    //같은 방에 있는 사람들 id(PK) 리스트
    @Query(value = "SELECT p.id FROM Participant p WHERE p.workspace.id=:workspaceId")
    List<Long> findByVisitorMatch(Long workspaceId);


    //ID별 매칭버튼 클릭 여부
    @Query(value = "SELECT COUNT(p) >0 FROM Participant p WHERE p.id=:id AND p.participantMatch=true")
    Boolean existsById(long id);


    //개인 ID 찾기
    @Query(value = "SELECT p from Participant p join p.workspace w where p.user.id =:id and p.workspace.id=:workspaceId")
    Optional<Participant> findByUserIdAndWorkspaceId(@Param("id") Long id, @Param("workspaceId") Long workspaceId);
}

package com.hyunsolutiondemo.dangu.participant.domain;

import com.hyunsolutiondemo.dangu.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<User, Long> {

    //같은 방에 있는 사람들 id(PK) 리스트
    @Query(value = "SELECT p.id FROM Participant p WHERE p.workspace.id=:workspaceId")
    List<Long> findByVisitorMatch(Long workspaceId);

    //개인 ID 찾기
    @Query(value = "UPDATE Participant p SET p.participantMatch=true WHERE p.workspace.id=:workspaceId AND p.user.id=:id")
    void updateByIdAndWorkspace(Long id, Long workspaceId);

    //ID별 매칭버튼 클릭 여부
    @Query(value = "SELECT COUNT(p) >0 FROM Participant p WHERE p.id=:id AND p.participantMatch=true")
    Boolean existsById(long id);

}

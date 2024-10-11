package com.hyunsolutiondemo.dangu.participant.service;

import com.hyunsolutiondemo.dangu.participant.domain.ParticipantRepository;
import com.hyunsolutiondemo.dangu.user.domain.UserRepository;
import com.hyunsolutiondemo.dangu.workspace.domain.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    public void changeMatching(Long id, Long workspaceId){
        //상태 변경
        participantRepository.updateByIdAndWorkspace(id, workspaceId);

        //participant테이블에서 roomNumber로 들어온 숫자를 통해 누가 있는지 파악
        List<Long> participants = participantRepository.findByVisitorMatch(workspaceId);

        for(Long participant : participants){
            boolean mathingCheck=participantRepository.existsById(participant);
            if(!mathingCheck){
                return;
            }
        }
        //방안에 모든 참가자가 "확정"버튼을 눌렀는지 확인하고 게임방 테이블 속 매칭 결과를 true로 바꿈
        workspaceRepository.UpdateWorkspaceStatus(workspaceId);

    }



}

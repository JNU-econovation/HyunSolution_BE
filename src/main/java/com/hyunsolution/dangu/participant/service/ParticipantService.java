package com.hyunsolution.dangu.participant.service;

import com.hyunsolution.dangu.participant.domain.Participant;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    @Transactional
    public void changeMatching(Long id, Long workspaceId){
        //상태 변경

        Optional<Participant> participantOptional = participantRepository.findByUserIdAndWorkspaceId(id,workspaceId);
        participantOptional.get().accept();
//        participantRepository.updateByIdAndWorkspace(id, workspaceId);

        //participant테이블에서 roomNumber로 들어온 숫자를 통해 누가 있는지 파악
        List<Long> participants = participantRepository.findByVisitorMatch(workspaceId);



        for(Long participant : participants){
            boolean mathingCheck=participantRepository.existsById(participant);
            if(!mathingCheck){
                return;
            }
        }
        //방안에 모든 참가자가 "확정"버튼을 눌렀는지 확인하고 게임방 테이블 속 매칭 결과를 true로 바꿈
        Workspace workspace1 = workspaceRepository.findById(workspaceId).get();
        workspace1.finalAccept();
//        workspaceRepository.UpdateWorkspaceStatus(workspaceId);

    }



}

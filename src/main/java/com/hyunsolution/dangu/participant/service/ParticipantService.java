package com.hyunsolution.dangu.participant.service;

import com.hyunsolution.dangu.participant.domain.Participant;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    @Transactional
    public void changeMatching(Long id, Long workspaceId) {
        // 상태 변경
        Participant participantOptional =
                participantRepository.findByUserIdAndWorkspaceId(id, workspaceId)
                        .orElseThrow(()-> new NoSuchElementException("Participant not found"));

        participantOptional.accept();

        // participant테이블에서 roomNumber로 들어온 숫자를 통해 누가 있는지 파악
        List<Long> participantIds = participantRepository.findParticipantIdByWorkspaceId(workspaceId);

        // 방안에 모든 참가자가 "확정"버튼을 눌렀는지 확인
        for (Long participant : participantIds) {
            boolean mathingCheck = participantRepository.isClickedById(participant);
            if (!mathingCheck) {
                return;
            }
        }
        // 게임방 테이블 속 매칭 결과를 true로 바꿈
        Workspace workspace1 = workspaceRepository.findById(workspaceId).orElseThrow(()-> new NoSuchElementException("Workspace not found"));
        workspace1.acceptFinal();
    }
}

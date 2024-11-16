package com.hyunsolution.dangu.participant.service;

import com.hyunsolution.dangu.participant.domain.Participant;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import com.hyunsolution.dangu.participant.dto.response.EnterChatRoomResponse;
import com.hyunsolution.dangu.participant.exception.ParticipantNotFoundException;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import com.hyunsolution.dangu.workspace.exception.WorkspaceNotFoundException;
import java.util.List;
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
                participantRepository
                        .findByUserIdAndWorkspaceId(id, workspaceId)
                        .orElseThrow(() -> ParticipantNotFoundException.EXCEPTION);

        participantOptional.accept();

        // participant테이블에서 roomNumber로 들어온 숫자를 통해 누가 있는지 파악
        List<Long> participantIds =
                participantRepository.findParticipantIdByWorkspaceId(workspaceId);

        // 방안에 모든 참가자가 "확정"버튼을 눌렀는지 확인
        for (Long participant : participantIds) {
            boolean mathingCheck =
                    participantRepository.existsByIdAndParticipantMatchTrue(participant);
            if (!mathingCheck) {
                return;
            }
        }
        // 게임방 테이블 속 매칭 결과를 true로 바꿈
        Workspace workspace1 = workspaceRepository.findById(workspaceId).orElseThrow(()-> WorkspaceNotFoundException.EXCEPTION);
        workspace1.acceptFinal();
    }

    // 채팅방 입장 메시지 전송
    public EnterChatRoomResponse sendEnteringMessage(Long id, Long workspaceId) {
        User user = userRepository.findById(id).orElseThrow();
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow();
        String message;
        // 기존 채팅방에 1명(방장)만 존재하며 & 입장자가 방장이 아닐때 입장 메시지를 보낸다.
        if (participantRepository.findByWorkspaceId(workspaceId).size() != 2
                && workspaceRepository.findById(workspaceId).get().getCreator().getId() != id) {

            // 입장자 participant 테이블에 저장
            Participant participant =
                    Participant.builder()
                            .user(user)
                            .workspace(workspace)
                            .participantMatch(false)
                            .build();
            participantRepository.save(participant);

            String userName = userRepository.findById(id).get().getUid();
            message = userName + "님이 입장하셨습니다.";
        } else {
            message = "";
        }
        return new EnterChatRoomResponse(message);
    }
}

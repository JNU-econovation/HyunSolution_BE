package com.hyunsolution.dangu.participant.service;

import com.hyunsolution.dangu.chatRoom.domain.ChatRoom;
import com.hyunsolution.dangu.chatRoom.domain.ChatRoomRepository;
import com.hyunsolution.dangu.chatRoom.exception.ChatRoomNotFoundException;
import com.hyunsolution.dangu.participant.domain.Participant;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import com.hyunsolution.dangu.participant.dto.request.UpdateParticipantMatchRequest;
import com.hyunsolution.dangu.participant.dto.response.EnterChatRoomResponse;
import com.hyunsolution.dangu.participant.exception.AlreadyMatchedException;
import com.hyunsolution.dangu.participant.exception.ParticipantNotFoundException;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.exception.UserNotFoundException;
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
    private final ChatRoomRepository chatRoomRepository;

    //채팅방 생성
    @Transactional
    public EnterChatRoomResponse addChatRoom(Long userId, Long workspaceId) {
        User user = userRepository.findById(userId).orElseThrow(()-> UserNotFoundException.EXCEPTION);
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(()-> WorkspaceNotFoundException.EXCEPTION);
        //채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder().workspace(workspace).isMatched(false).build();
        return addParticipant(user, chatRoom);
    }

    //참여자에 입장자 추가
    @Transactional
    public EnterChatRoomResponse addParticipant(User user, ChatRoom chatRoom) {
        //참여자 추가
        Participant participant =
                Participant.builder()
                        .user(user)
                        .chatRoom(chatRoom)
                        .participantMatch(false)
                        .gameAttend(false)
                        .build();
        participantRepository.save(participant);

        return new EnterChatRoomResponse(chatRoom.getId());
    }



    @Transactional
    public void updateMatching(Long id, Long chatRoomId, UpdateParticipantMatchRequest request) {
        // 1. 참가자 조회
        Participant participant =
                participantRepository
                        .findByUserIdAndChatRoomId(id, chatRoomId)
                        .orElseThrow(() -> ParticipantNotFoundException.EXCEPTION);

        // 2. 이미 매칭된 상태인지 확인
        if (participant.getChatRoom().isMatched()) {
            throw AlreadyMatchedException.EXCEPTION;
        }

        // 3. 참가자의 매칭 상태 업데이트
        participant.updateParticipantMatch(request.isMatch());

        // 4. 매칭 요청 처리
        if (request.isMatch() && allParticipantsMatched(chatRoomId)) {
            finalizeWorkspaceMatching(chatRoomId);
        }
    }

    // 모든 참가자가 매칭되었는지 확인
    private boolean allParticipantsMatched(Long chatRoomId) {
        List<Long> participantIds =
                participantRepository.findParticipantIdByChatRoomId(chatRoomId);
        return participantRepository.existsByIdAndParticipantMatchTrue(participantIds);
    }

    // 채팅방의 매칭을 최종 확정
    private void finalizeWorkspaceMatching(Long chatRoomId) {
        ChatRoom chatRoom =
                chatRoomRepository
                        .findById(chatRoomId)
                        .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
        chatRoom.acceptFinal();
    }



    // 채팅방 입장 메시지 전송
    /* //입장 메시지는 기획에 없어 일단 주석처리해둠
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

     */
}

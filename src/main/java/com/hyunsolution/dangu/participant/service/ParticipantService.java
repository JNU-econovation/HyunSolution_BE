package com.hyunsolution.dangu.participant.service;

import com.hyunsolution.dangu.chatRoom.domain.ChatRoom;
import com.hyunsolution.dangu.chatRoom.domain.ChatRoomRepository;
import com.hyunsolution.dangu.chatRoom.exception.ChatRoomNotFoundException;
import com.hyunsolution.dangu.chatRoom.service.ChatRoomService;
import com.hyunsolution.dangu.chatting.domain.Chatting;
import com.hyunsolution.dangu.chatting.domain.ChattingRepository;
import com.hyunsolution.dangu.chatting.domain.MessageType;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageDetailResponse;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageResponse;
import com.hyunsolution.dangu.chatting.service.ChattingService;
import com.hyunsolution.dangu.common.event.CreateChatRoomEvent;
import com.hyunsolution.dangu.common.event.EventPublish;
import com.hyunsolution.dangu.common.event.Events;
import com.hyunsolution.dangu.game.domain.Game;
import com.hyunsolution.dangu.game.domain.GameRepository;
import com.hyunsolution.dangu.game.domain.GameResult;
import com.hyunsolution.dangu.game.domain.GameResultRepository;
import com.hyunsolution.dangu.participant.domain.Participant;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import com.hyunsolution.dangu.participant.dto.request.UpdateParticipantMatchRequest;
import com.hyunsolution.dangu.participant.dto.response.EnterChatRoomResponse;
import com.hyunsolution.dangu.participant.dto.response.GetMatchStatusResponse;
import com.hyunsolution.dangu.participant.exception.AlreadyMatchedCannotAcceptException;
import com.hyunsolution.dangu.participant.exception.ParticipantNotFoundException;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.exception.UserNotFoundException;
import com.hyunsolution.dangu.user.service.UserService;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import com.hyunsolution.dangu.workspace.exception.WorkspaceNotFoundException;
import java.util.List;

import com.hyunsolution.dangu.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChattingRepository chattingRepository;
    private final GameRepository gameRepository;
    private final GameResultRepository gameResultRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final WorkspaceService workspaceService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final ChattingService chattingService;

    // 채팅방 생성
    @Transactional
    @EventPublish
    public EnterChatRoomResponse addChatRoom(Long userId, Long workspaceId) {
        User visitor =
                userRepository.findById(userId).orElseThrow(() -> UserNotFoundException.EXCEPTION);
        Workspace workspace = workspaceService.findWorkspace(workspaceId);
        Long creatorId =workspace.getCreator().getId();
        User creator = userService.findUser(creatorId);

        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder().workspace(workspace).build();
        chatRoom = chatRoomRepository.save(chatRoom);
        Events.raise(CreateChatRoomEvent.of(chatRoom, visitor, creator));
        return new EnterChatRoomResponse(chatRoom.getId());
    }

    @Transactional
    public void updateMatching(Long userId, Long chatRoomId, UpdateParticipantMatchRequest request) {
        // 참가자 조회
        Participant participant = findParticipant(userId, chatRoomId);

        // 이미 매칭된 상태인지 확인
        isAlreadyMatched(participant);

        // 참가자 매칭 신청 및 취소 메시지 저장
        String content = createSystemMessage(participant, request);

        //참가자의 매칭 상태 업데이트
        participant.updateParticipantMatch(request.isMatch());

        ChatRoom chatRoom =chatRoomService.findChatRoom(chatRoomId);
        Chatting chatting = chattingService.buildChatMessage(chatRoom,content,MessageType.SYSTEM);

        chattingRepository.save(chatting);
        // STOMP 메세지 전송
        sendStompSystemMessage(content, chatRoomId);

        // workspaceId 변수 저장
        Long workspaceId = workspaceService.findWorkspace(chatRoom.getWorkspace().getId()).getId();
        List<Participant> participants = participantRepository.findIdByChatRoomId(chatRoomId);

        //매칭 요청 처리
        if (request.isMatch() && allParticipantsMatched(participants)) {
            finalizeChatRoomMatching(chatRoomId);
            finalizeWorkspaceMatching(workspaceId, participants);
        }
    }

    @Transactional(readOnly = true)
    public Participant findParticipant(Long userId, Long chatRoomId) {
        return participantRepository
                .findByUserIdAndChatRoomId(userId, chatRoomId)
                .orElseThrow(() -> ParticipantNotFoundException.EXCEPTION);
    }
    @Transactional(readOnly = true)
    public Participant findParticipant(Long userId) {
        return participantRepository
                .findById(userId)
                .orElseThrow(() -> ParticipantNotFoundException.EXCEPTION);
    }
    @Transactional(readOnly = true)
    public Participant findParticipant(Long userId, List<Long> participantIds) {
        return participantRepository
                .findByUserIdInParticipantsId(userId, participantIds)
                .orElseThrow(() -> ParticipantNotFoundException.EXCEPTION);
    }

    @Transactional(readOnly = true)
    public void isAlreadyMatched(Participant participant) {
        if (participant.getChatRoom().getWorkspace().isMatched()) {
            throw AlreadyMatchedCannotAcceptException.EXCEPTION;
        }
    }

    private String createSystemMessage(Participant participant, UpdateParticipantMatchRequest request) {
        String uid = participant.getUser().getUid();
        return participant.isParticipantMatch() && !request.isMatch()
                ? uid + "님이 매칭을 신청하셨습니다."
                : uid + "님이 매칭을 취소하셨습니다.";
    }

    // 모든 참가자가 매칭되었는지 확인
    private boolean allParticipantsMatched(List<Participant> participants) {
        List<Long> participantIds = participants.stream().map(Participant::getId).toList();
        return !participantRepository.existsByIdAndParticipantMatchFalse(participantIds);
    }

    @Transactional
    // 채팅방의 매칭을 최종 확정
    public void finalizeChatRoomMatching(Long chatRoomId) {
        ChatRoom chatRoom =chatRoomService.findChatRoom(chatRoomId);
        // 채팅방상태 확정으로 변경
        chatRoom.acceptMatching();

        // 매칭 확정 메시지 저장
        String content = "매칭되었습니다.\n대전에서 게임을 시작하세요";
        Chatting chatting = chattingService.buildChatMessage(chatRoom,content,MessageType.STARTGAME);
        chattingRepository.save(chatting);
        // STOMP 메세지 전송
        sendStompSystemMessage(content, chatRoomId);
    }

    private void sendStompSystemMessage(String content, Long chatRoomId) {
        ChatMessageResponse chatSystemMessage =
                new ChatMessageResponse(
                        "success",
                        new ChatMessageDetailResponse(null, content, MessageType.SYSTEM),
                        null);
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, chatSystemMessage);
    }

    @Transactional
    // 채팅방의 매칭을 최종 확정
    public void finalizeWorkspaceMatching(Long workspaceId, List<Participant> participants) {
        Workspace workspace =
                workspaceRepository
                        .findById(workspaceId)
                        .orElseThrow(() -> WorkspaceNotFoundException.EXCEPTION);
        workspace.acceptMatchingFinal();
        Game gameDefault = Game.createDefaultGame(workspace);
        Game savedGame = gameRepository.save(gameDefault);

        participants.forEach(
                participant -> {
                    User user = participant.getUser();
                    GameResult gameResult = GameResult.builder().game(savedGame).user(user).build();
                    gameResultRepository.save(gameResult);
                });
    }

    @Transactional(readOnly = true)
    // 매칭 현황 조회
    public GetMatchStatusResponse getMatchResult(Long userId, Long chatRoomId) {
        List<Long> participantIds =
                participantRepository.findIdByChatRoomId(chatRoomId).stream()
                        .map(Participant::getId)
                        .toList();
        Participant userParticipant =
                participantRepository
                        .findByUserIdInParticipantsId(userId, participantIds)
                        .orElseThrow(() -> ParticipantNotFoundException.EXCEPTION);
        // 자신의 매칭 신청 현황
        boolean myself = userParticipant.isParticipantMatch();

        // 상대방의 매칭 신청 현황
        boolean counterpart = false;
        for (Long participant : participantIds) {
            // id와 해당 채팅방에 있는 사람들을 비교 같으면 owner
            if (!participant.equals(userParticipant.getId())) {
                Participant par =
                        participantRepository
                                .findById(participant)
                                .orElseThrow(() -> ParticipantNotFoundException.EXCEPTION);
                counterpart = par.isParticipantMatch();
            }
        }
        // 전체 게임방의 매칭 결과
        boolean matchResult = chatRoomService.findChatRoom(chatRoomId)
                        .getWorkspace()
                        .isMatched();
        return new GetMatchStatusResponse(counterpart, myself, matchResult);
    }
    // 매칭 현황 조회

    // 채팅방 입장 메시지 전송
    /* //입장 메시지는 기획에 없어 일단 주석처리해둠
    public EnterChatRoomResponse sendEnteringMessage(Long id, Long workspaceId) {
        User user = userRepository.findById(id).orElseThrow();
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow();
        String message;
        // 기존 채팅방에 1명(방장)만 존재하며 & 입장자가 방장이 아닐때 입장 메시지를 보낸다.
        if (participantRepository.findByWorkspaceId(workspaceId).size() != 2
                && workspaceRepository.findByIdF(workspaceId).get().getCreator().getId() != id) {

            // 입장자 participant 테이블에 저장
            Participant participant =
                    Participant.builder()
                            .user(user)
                            .workspace(workspace)
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

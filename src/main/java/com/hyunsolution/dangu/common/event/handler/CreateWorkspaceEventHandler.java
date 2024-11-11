package com.hyunsolution.dangu.common.event.handler;

import com.hyunsolution.dangu.chatlog.domain.ChatLog;
import com.hyunsolution.dangu.chatlog.domain.ChatLogRepository;
import com.hyunsolution.dangu.common.event.CreateWorkspaceEvent;
import com.hyunsolution.dangu.participant.domain.Participant;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateWorkspaceEventHandler {
    private final ParticipantRepository participantRepository;
    private final ChatLogRepository chatLogRepository;

    @TransactionalEventListener(classes = CreateWorkspaceEvent.class)
    @Async
    @Transactional
    public void handle(CreateWorkspaceEvent event) {
        ChatLog chatLog = ChatLog.builder()
                .workspace(event.getWorkspace())
                .enterTime(LocalDateTime.now())
                .user(event.getUser())
                .build();
        chatLogRepository.save(chatLog);

        Participant participant = Participant.builder()
                .workspace(event.getWorkspace())
                .user(event.getUser())
                .build();

        participantRepository.save(participant);
    }
}

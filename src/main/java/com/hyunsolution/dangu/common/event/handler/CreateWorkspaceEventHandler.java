package com.hyunsolution.dangu.common.event.handler;

import com.hyunsolution.dangu.chatlog.domain.ChatLog;
import com.hyunsolution.dangu.chatlog.domain.ChatLogRepository;
import com.hyunsolution.dangu.common.event.CreateChatRoomEvent;
import com.hyunsolution.dangu.participant.domain.Participant;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CreateWorkspaceEventHandler {
    private final ParticipantRepository participantRepository;
    private final ChatLogRepository chatLogRepository;

    @TransactionalEventListener(classes = CreateChatRoomEvent.class)
    @Async
    @Transactional
    public void handle(CreateChatRoomEvent event) {
        ChatLog chatLog =
                ChatLog.builder()
                        .chatRoom(event.getChatRoom())
                        .enterTime(LocalDateTime.now())
                        .user(event.getUser())
                        .build();
        chatLogRepository.save(chatLog);

        Participant participant =
                Participant.builder().chatRoom(event.getChatRoom()).user(event.getUser()).build();

        participantRepository.save(participant);
    }
}

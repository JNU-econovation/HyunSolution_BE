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
public class CreateChatRoomEventHandler {
    private final ParticipantRepository participantRepository;
    private final ChatLogRepository chatLogRepository;

    @TransactionalEventListener(classes = CreateChatRoomEvent.class)
    @Async
    @Transactional
    public void handle(CreateChatRoomEvent event) {
        ChatLog chatLogVisitor =
                ChatLog.builder()
                        .chatRoom(event.getChatRoom())
                        .enterTime(LocalDateTime.now())
                        .user(event.getVisitor())
                        .build();

        chatLogRepository.save(chatLogVisitor);
        ChatLog chatLogCreator =
                ChatLog.builder()
                        .chatRoom(event.getChatRoom())
                        .enterTime(LocalDateTime.now())
                        .user(event.getCreator())
                        .build();

        chatLogRepository.save(chatLogCreator);

        Participant creator =
                Participant.builder()
                        .chatRoom(event.getChatRoom())
                        .user(event.getCreator())
                        .build();
        participantRepository.save(creator);
        Participant visitor =
                Participant.builder()
                        .chatRoom(event.getChatRoom())
                        .user(event.getVisitor())
                        .build();

        participantRepository.save(visitor);
    }
}

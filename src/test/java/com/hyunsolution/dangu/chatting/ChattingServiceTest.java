package com.hyunsolution.dangu.chatting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.hyunsolution.dangu.chatRoom.domain.ChatRoom;
import com.hyunsolution.dangu.chatRoom.service.ChatRoomService;
import com.hyunsolution.dangu.chatting.domain.Chatting;
import com.hyunsolution.dangu.chatting.domain.ChattingRepository;
import com.hyunsolution.dangu.chatting.domain.MessageType;
import com.hyunsolution.dangu.chatting.dto.response.ChatMessageDetailResponse;
import com.hyunsolution.dangu.chatting.service.ChattingService;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.service.UserService;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChattingServiceTest {
    @InjectMocks private ChattingService chattingService;
    @Mock private ChattingRepository chattingRepository;
    @Mock private ChatRoomService chatRoomService;
    @Mock private UserService userService;

    @Test
    void sendMassage_메세지저장_응답반환() {
        // given
        Long chatRoomId = 1L;
        Long userId = 1L;
        String message = "hi!";

        User user = User.builder().uid("testUid").build();
        ChatRoom chatRoom = mock(ChatRoom.class);
        // when
        when(chatRoom.getId()).thenReturn(chatRoomId);
        when(userService.findUser(userId)).thenReturn(user);
        when(chatRoomService.findChatRoom(chatRoomId)).thenReturn(chatRoom);

        ChatMessageDetailResponse response =
                chattingService.sendMessage(chatRoom.getId(), message, userId);

        verify(chatRoomService).updateChatRoom(chatRoom);
        verify(chattingRepository).save(any(Chatting.class));
        // then
        assertEquals("testUid", response.senderId());
        assertEquals("hi!", response.message());
        assertEquals(MessageType.TEXT, response.messageType());
    }

    @Test
    void saveChatMessage_메세지저장성공() {
        ChatRoom chatRoom =
                ChatRoom.builder().workspace(mock(Workspace.class)).isMatched(false).build();
        chatRoom.setId(1L);
        User user = User.builder().uid("testUid").password("123").build();
        String message = "hi!";
        Chatting chatMessage =
                Chatting.builder().chatRoom(chatRoom).sender(user).content(message).build();

        chattingRepository.save(chatMessage);

        // when
        when(chatRoomService.findChatRoom(chatRoom.getId())).thenReturn(chatRoom);
        Chatting result = chattingService.buildChatMessage(chatRoom, user, message);
        // then
        assertEquals(message, result.getContent());
        assertEquals(chatRoom, result.getChatRoom());
        assertEquals(user, result.getSender());
    }
}

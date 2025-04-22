package com.hyunsolution.dangu.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.exception.UserNotFoundException;
import com.hyunsolution.dangu.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock private UserRepository userRepository;

    @InjectMocks private UserService userService;

    @Test
    void findUser_존재시_유저반환() {
        // given
        Long userId = 1L;
        User user = User.builder().uid("testUid").password("123").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        // when
        User result = userService.findUser(userId);

        // then
        assertEquals("testUid", result.getUid());
    }

    @Test
    void findUser_없을때_예외발생() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        // then
        assertThrows(
                UserNotFoundException.class,
                () -> {
                    userService.findUser(1L);
                });
    }
}

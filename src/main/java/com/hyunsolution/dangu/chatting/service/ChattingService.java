package com.hyunsolution.dangu.chatting.service;

import com.hyunsolution.dangu.chatting.domain.Chatting;
import com.hyunsolution.dangu.chatting.domain.ChattingRepository;
import com.hyunsolution.dangu.chatting.dto.response.GetChattingsResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingService {
    private final ChattingRepository chattingRepository;

    public List<GetChattingsResponse> findByWorkspaceId(Long loginUserId, Long workspaceId) {
        List<Chatting> chattings = chattingRepository.findByWorkspaceId(workspaceId);
        return chattings.stream()
                .map(
                        chatting -> {
                            boolean isOwn = isOwn(loginUserId, chatting.getUser().getId());
                            return GetChattingsResponse.of(
                                    chatting.getContent(), chatting.getId(), isOwn);
                        })
                .toList();
    }

    private boolean isOwn(Long loginUserId, Long chattingUserId) {
        return loginUserId.equals(chattingUserId);
    }
}

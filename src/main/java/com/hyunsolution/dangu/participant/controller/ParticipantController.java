package com.hyunsolution.dangu.participant.controller;

import com.hyunsolution.dangu.common.apiResponse.ApiResponse;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import com.hyunsolution.dangu.participant.dto.response.EnterChatRoomResponse;
import com.hyunsolution.dangu.participant.service.ParticipantService;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ParticipantController {

    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ParticipantService participantService;

    // 사용자 매칭 버튼 클릭
    @PostMapping("/participant/matching/{roomNumber}")
    public ApiResponse<?> matching(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long id,
            @PathVariable("roomNumber") Long workspaceId) {

        // 매칭확인버튼 누른 사용자 매칭칼럼 상태 변경
        participantService.changeMatching(id, workspaceId);
        return ApiResponse.success(null);
    }

    // 사용자 채팅방 입장
    @PostMapping("/participant/{roomNumber}")
    public ApiResponse<EnterChatRoomResponse> enterChatRoom(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long id,
            @PathVariable("roomNumber") Long workspaceId) {
        EnterChatRoomResponse response = participantService.sendEnteringMessage(id, workspaceId);
        return ApiResponse.success(response);
    }
}

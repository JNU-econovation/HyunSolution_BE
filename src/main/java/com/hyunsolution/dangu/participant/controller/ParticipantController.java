package com.hyunsolution.dangu.participant.controller;

import com.hyunsolution.dangu.common.apiResponse.ApiResponse;
import com.hyunsolution.dangu.participant.domain.ParticipantRepository;
import com.hyunsolution.dangu.participant.dto.request.UpdateParticipantMatchRequest;
import com.hyunsolution.dangu.participant.dto.response.EnterChatRoomResponse;
import com.hyunsolution.dangu.participant.service.ParticipantService;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ParticipantController {

    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ParticipantService participantService;

    // 사용자 매칭 버튼 클릭
    @PostMapping("/participant/matching/{chatRoomId}")
    @Operation(summary = "참가자 매칭 수락/거절", description = "참가자 매칭 수락/거절 한다. (isMatch가 true이면 매칭 수락)" +
            "workspace의 매칭이 확정된 경우 참여자는 매칭 거절을 할 수 없다.")
    public ApiResponse<Void> matching(
            @Parameter(hidden = true) @RequestHeader("Authorization") Long id,
            @PathVariable("chatRoomId") Long chatRoomId,
            @RequestBody UpdateParticipantMatchRequest request) {

        // 매칭확인버튼 누른 사용자 매칭칼럼 상태 변경
        participantService.updateMatching(id, chatRoomId, request);
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

package com.hyunsolutiondemo.dangu.participant.controller;

import com.hyunsolutiondemo.dangu.common.apiResponse.ApiResponse;
import com.hyunsolutiondemo.dangu.participant.domain.ParticipantRepository;
import com.hyunsolutiondemo.dangu.participant.service.ParticipantService;
import com.hyunsolutiondemo.dangu.user.domain.UserRepository;
import com.hyunsolutiondemo.dangu.workspace.domain.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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


    @PostMapping("/participant/matching/{roomNumber}")
    public ApiResponse<?> matching(@RequestHeader("Authorization") Long id, @PathVariable("roomNumber") Long workspaceId) {

        //매칭확인버튼 누른 사용자 매칭칼럼 상태 변경
        participantService.changeMatching(id, workspaceId);
        return ApiResponse.success(null);
    }
}

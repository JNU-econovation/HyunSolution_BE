package com.hyunsolution.dangu.workspace.service;

import com.hyunsolution.dangu.common.event.EventPublish;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.exception.UserNotFoundException;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import com.hyunsolution.dangu.workspace.dto.response.GetWorkspacesResponse;

import java.time.LocalDateTime;
import java.util.List;

import com.hyunsolution.dangu.workspace.exception.WorkspaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workSpaceRepository;
    private final UserRepository userRepository;

    //게임방 등록
    @Transactional
    @EventPublish
    public void addWorkspace(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> UserNotFoundException.EXCEPTION);
        Workspace workSpace = Workspace.builder().creator(user).build();
        workSpaceRepository.save(workSpace);
    }

    //게임방 목록 조회
    public List<GetWorkspacesResponse> getWorkspaces() {
        LocalDateTime dateFilter = LocalDateTime.now().minusDays(1);
        return workSpaceRepository.findAll().stream().filter(workspace->!workspace.isMatched())
                .filter(workspace -> workspace.getCreatedAt().isAfter(dateFilter)) // 게임방 조회: 유지 시간은 24h
                .map(workspace -> GetWorkspacesResponse.of(workspace.getId(), workspace.getCreator().getUid())).toList();
    }

    //게임방 등록 취소
    @Transactional
    public void deleteWorkspace(Long workspaceId) {
        Workspace workspace= workSpaceRepository.findById(workspaceId).orElseThrow(() -> WorkspaceNotFoundException.EXCEPTION);
        workspace.isDeleted();
    }


}

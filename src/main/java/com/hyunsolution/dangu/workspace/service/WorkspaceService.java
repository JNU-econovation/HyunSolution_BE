package com.hyunsolution.dangu.workspace.service;

import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import com.hyunsolution.dangu.workspace.dto.response.GetWorkspacesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workSpaceRepository;
    private final UserRepository userRepository;

    //TODO: @다다 님이 GlobalExceptionHandler 개발하면 수정할 예정
    public void addWorkspace(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Workspace workSpace = Workspace.builder()
                .creator(user)
                .build();
        workSpaceRepository.save(workSpace);
    }

    public List<GetWorkspacesResponse> getWorkspaces() {
        return workSpaceRepository.findAll().stream()
                .filter(workspace -> !workspace.isMatched())
                .map(workspace -> GetWorkspacesResponse.of(workspace.getId(), workspace.getCreator().getUid())).toList();
    }
}


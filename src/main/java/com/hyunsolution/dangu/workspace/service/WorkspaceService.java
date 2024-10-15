package com.hyunsolution.dangu.workspace.service;

import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.exception.UserNotFoundException;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import com.hyunsolution.dangu.workspace.dto.response.GetWorkspacesResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workSpaceRepository;
    private final UserRepository userRepository;

    public void addWorkspace(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> UserNotFoundException.EXCEPTION);
        Workspace workSpace = Workspace.builder().creator(user).build();
        workSpaceRepository.save(workSpace);
    }

    public List<GetWorkspacesResponse> getWorkspaces() {
        return workSpaceRepository.findAll().stream()
                .filter(workspace -> !workspace.isMatched())
                .map(
                        workspace ->
                                GetWorkspacesResponse.of(
                                        workspace.getId(), workspace.getCreator().getUid()))
                .toList();
    }
}

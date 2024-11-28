package com.hyunsolution.dangu.workspace.service;

import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.exception.UserNotFoundException;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import com.hyunsolution.dangu.workspace.dto.response.GetWorkspacesResponse;
import com.hyunsolution.dangu.workspace.exception.WorkspaceAlreadyExistsException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workSpaceRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addWorkspace(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> UserNotFoundException.EXCEPTION);
        Workspace workSpace = Workspace.builder().creator(user).build();
        workSpaceRepository.save(workSpace);
    }

    public List<GetWorkspacesResponse> getWorkspaces(Long loginUserId) {
        return workSpaceRepository.findUnmatchedAndCreatedWithinLastDay().stream()
                .map(
                        workspace ->
                                GetWorkspacesResponse.of(
                                        workspace.getId(),
                                        workspace.getCreator().getUid(),
                                        isOwn(loginUserId, workspace)))
                .toList();
    }
    private boolean isOwn(Long loginUserId, Workspace workspace) {
        return loginUserId.equals(workspace.getCreator().getId());
    }
}

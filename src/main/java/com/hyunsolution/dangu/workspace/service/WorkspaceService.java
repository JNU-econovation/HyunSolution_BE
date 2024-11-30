package com.hyunsolution.dangu.workspace.service;

import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.exception.UserNotFoundException;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import com.hyunsolution.dangu.workspace.dto.response.GetWorkspacesResponse;
import com.hyunsolution.dangu.workspace.exception.WorkspaceAlreadyExistsException;
import com.hyunsolution.dangu.workspace.exception.WorkspaceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceService {
    private final WorkspaceRepository workSpaceRepository;
    private final UserRepository userRepository;

    // 게임방 등록
    @Transactional
    public void addWorkspace(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> UserNotFoundException.EXCEPTION);
        validateAlreadyExists(user.getId());
        Workspace workSpace = Workspace.builder().creator(user).build();
        workSpaceRepository.save(workSpace);
    }

    public List<GetWorkspacesResponse> getWorkspaces(Long loginUserId) {
        log.info("CI/CD 테스트 loginUserId: {}", loginUserId);
        return workSpaceRepository.findUnmatchedAndCreatedWithinLastDay().stream()
                .map(
                        workspace ->
                                GetWorkspacesResponse.of(
                                        workspace.getId(),
                                        workspace.getCreator().getUid(),
                                        isOwn(loginUserId, workspace)))
                .toList();
    }

    private void validateAlreadyExists(Long creatorId) {
        if (Boolean.TRUE.equals(workSpaceRepository.existsByCreatorId(creatorId))) {
            throw WorkspaceAlreadyExistsException.EXCEPTION;
        }
    }

    private boolean isOwn(Long loginUserId, Workspace workspace) {
        return loginUserId.equals(workspace.getCreator().getId());
    }

    // 게임방 등록 취소
    @Transactional
    public void deleteWorkspace(Long workspaceId) {
        Workspace workspace =
                workSpaceRepository
                        .findById(workspaceId)
                        .orElseThrow(() -> WorkspaceNotFoundException.EXCEPTION);
        workspace.isDeleted();
    }
}

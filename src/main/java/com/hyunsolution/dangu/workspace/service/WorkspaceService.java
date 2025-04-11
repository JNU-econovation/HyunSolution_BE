package com.hyunsolution.dangu.workspace.service;

import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.user.domain.UserRepository;
import com.hyunsolution.dangu.user.service.UserService;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import com.hyunsolution.dangu.workspace.domain.WorkspaceRepository;
import com.hyunsolution.dangu.workspace.dto.response.CheckWorkspaceManager;
import com.hyunsolution.dangu.workspace.dto.response.GetWorkspacesResponse;
import com.hyunsolution.dangu.workspace.exception.WorkspaceAlreadyExistsException;
import com.hyunsolution.dangu.workspace.exception.WorkspaceNotFoundException;
import java.time.LocalDateTime;
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
    private final UserService userService;

    @Transactional(readOnly = true)
    public CheckWorkspaceManager checkWorkspaceManager(Long workspaceId, Long userId) {
        Workspace workspace = findWorkspace(workspaceId);
        boolean isRoomManager = workspace.getCreator().getId().equals(userId);
        return new CheckWorkspaceManager(isRoomManager);
    }

    @Transactional(readOnly = true)
    public Workspace findWorkspace(Long workspaceId) {
        return workSpaceRepository
                .findById(workspaceId)
                .orElseThrow(() -> WorkspaceNotFoundException.EXCEPTION);
    }

    // 게임방 등록
    @Transactional
    public void addWorkspace(Long userPK) {

        User user = userService.findUser(userPK);
        validateAlreadyExists(user.getId());
        Workspace workspace = buildWorkspace(user);
        workSpaceRepository.save(workspace);
    }

    public Workspace buildWorkspace(User user) {
        return Workspace.builder().creator(user).build();
    }

    @Transactional(readOnly = true)
    public List<GetWorkspacesResponse> getWorkspaces(Long userId) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        log.info("startTime " + startTime + " / endTime" + endTime);
        return getUnmatchedWorkspacesWithinPeriod(startTime, endTime, userId);
    }

    @Transactional(readOnly = true)
    public List<GetWorkspacesResponse> getUnmatchedWorkspacesWithinPeriod(
            LocalDateTime startTime, LocalDateTime endTime, Long userId) {
        return workSpaceRepository.findUnmatchedAndCreatedWithinDay(startTime, endTime).stream()
                .map(
                        workspace ->
                                GetWorkspacesResponse.of(
                                        workspace.getId(),
                                        workspace.getCreator().getUid(),
                                        isOwn(userId, workspace)))
                .toList();
    }

    private void validateAlreadyExists(Long creatorId) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        if (Boolean.TRUE.equals(
                workSpaceRepository.existsByCreatorIdWithinDay(creatorId, startTime, endTime))) {
            throw WorkspaceAlreadyExistsException.EXCEPTION;
        }
    }

    private boolean isOwn(Long userId, Workspace workspace) {
        return userId.equals(workspace.getCreator().getId());
    }

    // 게임방 등록 취소
    @Transactional
    public void deleteWorkspace(Long workspaceId) {
        Workspace workspace =
                workSpaceRepository
                        .findById(workspaceId)
                        .orElseThrow(() -> WorkspaceNotFoundException.EXCEPTION);
        workspace.toggleDeleted();
    }
}

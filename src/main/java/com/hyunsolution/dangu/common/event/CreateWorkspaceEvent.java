package com.hyunsolution.dangu.common.event;

import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateWorkspaceEvent extends DomainEvent{
    private Workspace workspace;
    private User user;

    public static CreateWorkspaceEvent of (Workspace workspace, User user) {
        return new CreateWorkspaceEvent(workspace, user);
    }
}

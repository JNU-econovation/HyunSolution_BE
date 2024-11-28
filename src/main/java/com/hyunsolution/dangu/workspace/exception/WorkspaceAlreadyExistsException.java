package com.hyunsolution.dangu.workspace.exception;

import com.hyunsolution.dangu.common.exception.CustomException;

public class WorkspaceAlreadyExistsException extends CustomException {
    public static final WorkspaceAlreadyExistsException EXCEPTION =
            new WorkspaceAlreadyExistsException();

    private WorkspaceAlreadyExistsException() {
        super(WorkspaceError.WORKSPACE_ALREADY_EXISTS);
    }
}

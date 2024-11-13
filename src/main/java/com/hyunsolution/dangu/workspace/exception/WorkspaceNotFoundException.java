package com.hyunsolution.dangu.workspace.exception;

import com.hyunsolution.dangu.common.exception.CustomException;

public class WorkspaceNotFoundException extends CustomException {
    public static final WorkspaceNotFoundException EXCEPTION = new WorkspaceNotFoundException();

    private WorkspaceNotFoundException() {
        super(WorkspaceError.WORKSPACE_NOT_FOUND);
    }
}

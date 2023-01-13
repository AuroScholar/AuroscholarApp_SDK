package com.auro.application.util.permission;


import com.auro.application.core.common.Status;

public class PermissionModel {
    private final Status status;
    private final boolean  result;

    public PermissionModel(Status status, boolean result){
        this.status = status;
        this.result = result;
    }

    public Status getStatus() {
        return status;
    }


    public boolean isResult() {
        return result;
    }
}

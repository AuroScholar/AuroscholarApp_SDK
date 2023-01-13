package com.auro.application.core.common;

public class FieldValidateStatus {

    private final boolean isValid;
    private final String msg;
    private final Status status;

    public FieldValidateStatus(boolean isValid, String msg, Status status){
        this.msg = msg;
        this.isValid = isValid;
        this.status = status;
    }
    public boolean isValid() {
        return isValid;
    }


    public String getMsg() {
        return msg;
    }

    public Status getStatus() {
        return status;
    }
}

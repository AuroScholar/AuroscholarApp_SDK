package com.auro.application.core.common;

public class ResponseStatus {

    private final Status status;
    private final Object object;


    public ResponseStatus(Object object, Status status){

        this.status = status;
        this.object = object;
    }

    public Status getStatus() {
        return status;
    }

    public Object getObject() {
        return object;
    }
}

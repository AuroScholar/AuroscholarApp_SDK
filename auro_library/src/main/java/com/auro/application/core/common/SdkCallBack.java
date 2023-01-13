package com.auro.application.core.common;

public interface SdkCallBack {

    void callBack(String message);

    void logOut();

    void commonCallback(Status status, Object o);
}

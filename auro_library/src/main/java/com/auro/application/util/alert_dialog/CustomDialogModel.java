package com.auro.application.util.alert_dialog;

import android.content.Context;

public class CustomDialogModel {

    String title;
    String content;
    long totalMemory;
    long freeSpace;
    boolean twoButtonRequired;
    Context context;


    public CustomDialogModel() {

    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public long getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(long freeSpace) {
        this.freeSpace = freeSpace;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isTwoButtonRequired() {
        return twoButtonRequired;
    }

    public void setTwoButtonRequired(boolean twoButtonRequired) {
        this.twoButtonRequired = twoButtonRequired;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

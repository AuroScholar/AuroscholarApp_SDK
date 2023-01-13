package com.auro.application.teacher.data.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelWebinarSlot {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("webinar_id")
    @Expose
    private int webinarId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getWebinarId() {
        return webinarId;
    }

    public void setWebinarId(int webinarId) {
        this.webinarId = webinarId;
    }
}

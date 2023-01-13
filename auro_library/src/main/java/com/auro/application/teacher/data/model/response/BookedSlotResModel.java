package com.auro.application.teacher.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookedSlotResModel {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("webinar_name")
    @Expose
    private String webinarName;
    @SerializedName("webinar_id")
    @Expose
    private Integer webinarId;
    @SerializedName("start_time")
    @Expose
    private String startTime;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWebinarName() {
        return webinarName;
    }

    public void setWebinarName(String webinarName) {
        this.webinarName = webinarName;
    }

    public Integer getWebinarId() {
        return webinarId;
    }

    public void setWebinarId(Integer webinarId) {
        this.webinarId = webinarId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}

package com.auro.application.teacher.data.model;

import com.auro.application.teacher.data.model.request.TeacherDasboardSummaryResModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherNewResModel {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private TeacherDasboardSummaryResModel data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TeacherDasboardSummaryResModel getData() {
        return data;
    }

    public void setData(TeacherDasboardSummaryResModel data) {
        this.data = data;
    }
}


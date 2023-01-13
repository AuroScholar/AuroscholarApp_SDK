package com.auro.application.teacher.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherCreateGroupResModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("teacher_group_id")
    @Expose
    private Integer teacherGroupId;
    @SerializedName("teacher_group_name")
    @Expose
    private String teacherGroupName;

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

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTeacherGroupId() {
        return teacherGroupId;
    }

    public void setTeacherGroupId(Integer teacherGroupId) {
        this.teacherGroupId = teacherGroupId;
    }

    public String getTeacherGroupName() {
        return teacherGroupName;
    }

    public void setTeacherGroupName(String teacherGroupName) {
        this.teacherGroupName = teacherGroupName;
    }

}
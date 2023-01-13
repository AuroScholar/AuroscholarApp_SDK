package com.auro.application.home.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Generated;

import java.util.List;
import javax.annotation.Generated;

import com.auro.application.home.data.model.response.UserDetailResModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckUserResModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response_code")
    @Expose
    private String responseCode;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("user_details")
    @Expose
    private List<UserDetailResModel> userDetails = null;
    @SerializedName("classes")
    @Expose
    private List<String> classes = null;

    private String studentName;

    private String userMobile;

    private String grade;

    private String userId;

    private String emailId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UserDetailResModel> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<UserDetailResModel> userDetails) {
        this.userDetails = userDetails;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

}
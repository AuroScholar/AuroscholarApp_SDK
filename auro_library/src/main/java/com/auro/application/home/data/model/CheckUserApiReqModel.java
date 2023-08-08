package com.auro.application.home.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckUserApiReqModel {

    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("user_name")
    @Expose
    private String userName;

    @SerializedName("user_type")
    @Expose
    private String userType;


    @SerializedName("language_version")
    String langVersion;

    @SerializedName("api_version")
    String apiVersion;

    @SerializedName("user_prefered_language_id")
    Integer userPreferedLanguageId;


    public Integer getUserPreferedLanguageId() {
        return userPreferedLanguageId;
    }

    public void setUserPreferedLanguageId(Integer userPreferedLanguageId) {
        this.userPreferedLanguageId = userPreferedLanguageId;
    }

    public String getLangVersion() {
        return langVersion;
    }

    public void setLangVersion(String langVersion) {
        this.langVersion = langVersion;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
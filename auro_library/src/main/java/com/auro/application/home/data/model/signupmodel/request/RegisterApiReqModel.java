package com.auro.application.home.data.model.signupmodel.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterApiReqModel
{
    @SerializedName("mobile_no")
    String mobileNumber;

    @SerializedName("user_type")
    String userType;


    @SerializedName("sr_id")
    String srId;

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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public String getSrId() {
        return srId;
    }

    public void setSrId(String srId) {
        this.srId = srId;
    }


}

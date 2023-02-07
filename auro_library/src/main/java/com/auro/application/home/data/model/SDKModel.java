package com.auro.application.home.data.model;

import com.auro.application.home.data.model.response.UserDetailResModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SDKModel {

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
    private int responseCode;

    @SerializedName("user_details")
    @Expose
    private List<SDKDetailModel> userDetails = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isError() {
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

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
    public List<SDKDetailModel> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<SDKDetailModel> userDetails) {
        this.userDetails = userDetails;
    }
}
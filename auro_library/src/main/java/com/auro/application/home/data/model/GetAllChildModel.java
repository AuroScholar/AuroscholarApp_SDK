package com.auro.application.home.data.model;

import com.auro.application.home.data.model.response.ChildDetailResModel;
import com.auro.application.home.data.model.response.GetAllChildDetailResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllChildModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("data")
    @Expose
    private List<GetAllChildDetailResModel> userDetails = null;

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

    public List<GetAllChildDetailResModel> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<GetAllChildDetailResModel> userDetails) {
        this.userDetails = userDetails;
    }
}
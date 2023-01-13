package com.auro.application.home.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefferalReqModel {

    @SerializedName("referred_by_id")
    @Expose
    private String referredById;
    @SerializedName("referred_user_id")
    @Expose
    private String referredUserId;
    @SerializedName("referred_by_type")
    @Expose
    private String referredByType;
    @SerializedName("referred_user_mobile")
    @Expose
    private String referredUserMobile;

    public String getReferredById() {
        return referredById;
    }

    public void setReferredById(String referredById) {
        this.referredById = referredById;
    }

    public String getReferredUserId() {
        return referredUserId;
    }

    public void setReferredUserId(String referredUserId) {
        this.referredUserId = referredUserId;
    }

    public String getReferredByType() {
        return referredByType;
    }

    public void setReferredByType(String referredByType) {
        this.referredByType = referredByType;
    }

    public String getReferredUserMobile() {
        return referredUserMobile;
    }

    public void setReferredUserMobile(String referredUserMobile) {
        this.referredUserMobile = referredUserMobile;
    }

}

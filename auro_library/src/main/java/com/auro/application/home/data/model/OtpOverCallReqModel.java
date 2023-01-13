package com.auro.application.home.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpOverCallReqModel {

    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("is_type")
    @Expose
    private Integer isType;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Integer getIsType() {
        return isType;
    }

    public void setIsType(Integer isType) {
        this.isType = isType;
    }

}
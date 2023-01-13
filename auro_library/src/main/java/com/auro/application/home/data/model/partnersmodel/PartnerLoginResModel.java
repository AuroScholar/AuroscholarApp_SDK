package com.auro.application.home.data.model.partnersmodel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PartnerLoginResModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("partner_response_code")
    @Expose
    private String partnerResponseCode;
    @SerializedName("message")
    @Expose
    private String partnerResponseMessage;
    @SerializedName("partner_url")
    @Expose
    private String partnerUrl;

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

    public String getPartnerResponseCode() {
        return partnerResponseCode;
    }

    public void setPartnerResponseCode(String partnerResponseCode) {
        this.partnerResponseCode = partnerResponseCode;
    }

    public String getPartnerResponseMessage() {
        return partnerResponseMessage;
    }

    public void setPartnerResponseMessage(String partnerResponseMessage) {
        this.partnerResponseMessage = partnerResponseMessage;
    }

    public String getPartnerUrl() {
        return partnerUrl;
    }

    public void setPartnerUrl(String partnerUrl) {
        this.partnerUrl = partnerUrl;
    }

}
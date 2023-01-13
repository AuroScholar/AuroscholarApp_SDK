package com.auro.application.home.data.model.partnersmodel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PartnerResModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("partner")
    @Expose
    private List<PartnerDataModel> partner = null;

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

    public List<PartnerDataModel> getPartner() {
        return partner;
    }

    public void setPartner(List<PartnerDataModel> partner) {
        this.partner = partner;
    }

}
package com.auro.application.home.data.model;

import com.auro.application.home.data.model.response.GetAllChildDetailResModel;
import com.auro.application.home.data.model.response.GetAllReferChildDetailResModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllChildReferListModel {



    @SerializedName("refferal_users")
    @Expose
    private List<GetAllReferChildDetailResModel> refferalusers = null;
    @SerializedName("reffered_users")
    @Expose
    private List<GetAllReferChildDetailResModel> refferedusers = null;

    public List<GetAllReferChildDetailResModel> getRefferalusers() {
        return refferalusers;
    }

    public void setRefferalusers(List<GetAllReferChildDetailResModel> refferalusers) {
        this.refferalusers = refferalusers;
    }

    public List<GetAllReferChildDetailResModel> getRefferedusers() {
        return refferedusers;
    }

    public void setRefferedusers(List<GetAllReferChildDetailResModel> refferedusers) {
        this.refferedusers = refferedusers;
    }
}
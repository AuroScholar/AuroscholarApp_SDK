package com.auro.application.home.data.model.response;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CertificateResModel {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("studentuseridbasedcertificate")
    @Expose
    private List<APIcertificate> studentuseridbasedcertificate = null;

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

    public List<APIcertificate> getStudentuseridbasedcertificate() {
        return studentuseridbasedcertificate;
    }

    public void setStudentuseridbasedcertificate(List<APIcertificate> studentuseridbasedcertificate) {
        this.studentuseridbasedcertificate = studentuseridbasedcertificate;
    }
}

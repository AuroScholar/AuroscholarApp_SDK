package com.auro.application.home.data.model.passportmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassportQuizMonthModel {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private List<PassportSubjectQuizMonthModel> passportSubjectModelList = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PassportSubjectQuizMonthModel> getPassportSubjectModelList() {
        return passportSubjectModelList;
    }

    public void setPassportSubjectModelList(List<PassportSubjectQuizMonthModel> passportSubjectModelList) {
        this.passportSubjectModelList = passportSubjectModelList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.auro.application.home.data.model.passportmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassportQuizTopicModel {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("topics")
    @Expose
    private List<PassportTopicQuizMonthModel> passportSubjectModelList = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PassportTopicQuizMonthModel> getPassportSubjectModelList() {
        return passportSubjectModelList;
    }

    public void setPassportSubjectModelList(List<PassportTopicQuizMonthModel> passportSubjectModelList) {
        this.passportSubjectModelList = passportSubjectModelList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

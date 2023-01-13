package com.auro.application.home.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrefSubjectResModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subjectname")
    @Expose
    private String subjectname;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("no_of_quiz")
    @Expose
    private String noOfQuiz;
    @SerializedName("max_attempted")
    @Expose
    private String maxAttempted;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("attempted")
    @Expose
    private Integer attempted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNoOfQuiz() {
        return noOfQuiz;
    }

    public void setNoOfQuiz(String noOfQuiz) {
        this.noOfQuiz = noOfQuiz;
    }

    public String getMaxAttempted() {
        return maxAttempted;
    }

    public void setMaxAttempted(String maxAttempted) {
        this.maxAttempted = maxAttempted;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Integer getAttempted() {
        return attempted;
    }

    public void setAttempted(Integer attempted) {
        this.attempted = attempted;
    }

}

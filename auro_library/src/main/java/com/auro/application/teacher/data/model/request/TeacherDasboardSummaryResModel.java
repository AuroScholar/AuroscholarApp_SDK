package com.auro.application.teacher.data.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherDasboardSummaryResModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("response_code")
    @Expose
    private String responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_student")
    @Expose
    private String totalStudent;
    @SerializedName("all_win_quiz")
    @Expose
    private String allWinQuiz;
    @SerializedName("total_quiz_taken")
    @Expose
    private String totalQuizTaken;
    @SerializedName("unique_student_taken_quiz")
    @Expose
    private String uniqueStudentTakenQuiz;
    @SerializedName("kyc_upload")
    @Expose
    private String kycUpload;
    @SerializedName("winning_student")
    @Expose
    private String winningStudent;
    @SerializedName("kyc_approved")
    @Expose
    private String kycApproved;
    @SerializedName("disbursed_scholarship")
    @Expose
    private String disbursedScholarship;

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

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotalStudent() {
        return totalStudent;
    }

    public void setTotalStudent(String totalStudent) {
        this.totalStudent = totalStudent;
    }

    public String getAllWinQuiz() {
        return allWinQuiz;
    }

    public void setAllWinQuiz(String allWinQuiz) {
        this.allWinQuiz = allWinQuiz;
    }

    public String getTotalQuizTaken() {
        return totalQuizTaken;
    }

    public void setTotalQuizTaken(String totalQuizTaken) {
        this.totalQuizTaken = totalQuizTaken;
    }

    public String getUniqueStudentTakenQuiz() {
        return uniqueStudentTakenQuiz;
    }

    public void setUniqueStudentTakenQuiz(String uniqueStudentTakenQuiz) {
        this.uniqueStudentTakenQuiz = uniqueStudentTakenQuiz;
    }

    public String getKycUpload() {
        return kycUpload;
    }

    public void setKycUpload(String kycUpload) {
        this.kycUpload = kycUpload;
    }

    public String getWinningStudent() {
        return winningStudent;
    }

    public void setWinningStudent(String winningStudent) {
        this.winningStudent = winningStudent;
    }

    public String getKycApproved() {
        return kycApproved;
    }

    public void setKycApproved(String kycApproved) {
        this.kycApproved = kycApproved;
    }

    public String getDisbursedScholarship() {
        return disbursedScholarship;
    }

    public void setDisbursedScholarship(String disbursedScholarship) {
        this.disbursedScholarship = disbursedScholarship;
    }
}
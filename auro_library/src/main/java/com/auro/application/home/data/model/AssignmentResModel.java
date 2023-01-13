package com.auro.application.home.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignmentResModel  implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("StudentID")
    @Expose
    private String StudentID;

    @SerializedName("exam_name")
    @Expose
    private String exam_name;

    @SerializedName("quiz_attempt")
    @Expose
    private String quiz_attempt;

    @SerializedName("examlang")
    @Expose
    private String examlang;

    @SerializedName("ExamAssignmentID")
    @Expose
    private String ExamAssignmentID;

    @SerializedName("img_normal_path")
    @Expose
    private String imgNormalPath;

    @SerializedName("img_path")
    @Expose
    private String imgPath;

    @SerializedName("quiz_id")
    @Expose
    private String quizId;

    @SerializedName("exam_id")
    @Expose
    private String examId;

    @SerializedName("time_interval")
    @Expose
    private  String timeInterval;

    protected AssignmentResModel(Parcel in) {
        status = in.readString();
        error = in.readByte() != 0;
        message = in.readString();
        StudentID = in.readString();
        exam_name = in.readString();
        quiz_attempt = in.readString();
        examlang = in.readString();
        ExamAssignmentID = in.readString();
        imgNormalPath = in.readString();
        imgPath = in.readString();
        quizId = in.readString();
        examId = in.readString();
        timeInterval = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeString(message);
        dest.writeString(StudentID);
        dest.writeString(exam_name);
        dest.writeString(quiz_attempt);
        dest.writeString(examlang);
        dest.writeString(ExamAssignmentID);
        dest.writeString(imgNormalPath);
        dest.writeString(imgPath);
        dest.writeString(quizId);
        dest.writeString(examId);
        dest.writeString(timeInterval);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AssignmentResModel> CREATOR = new Creator<AssignmentResModel>() {
        @Override
        public AssignmentResModel createFromParcel(Parcel in) {
            return new AssignmentResModel(in);
        }

        @Override
        public AssignmentResModel[] newArray(int size) {
            return new AssignmentResModel[size];
        }
    };

    public String getImgNormalPath() {
        return imgNormalPath;
    }

    public void setImgNormalPath(String imgNormalPath) {
        this.imgNormalPath = imgNormalPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getQuiz_attempt() {
        return quiz_attempt;
    }

    public void setQuiz_attempt(String quiz_attempt) {
        this.quiz_attempt = quiz_attempt;
    }

    public String getExamlang() {
        return examlang;
    }

    public void setExamlang(String examlang) {
        this.examlang = examlang;
    }

    public String getExamAssignmentID() {
        return ExamAssignmentID;
    }

    public void setExamAssignmentID(String examAssignmentID) {
        ExamAssignmentID = examAssignmentID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }
}
package com.auro.application.home.data.model.passportmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassportTopicQuizMonthModel {


    @SerializedName("quiz_id")
    @Expose
    private int quiz_id;

    @SerializedName("quiz_name")
    @Expose
    private String quiz_name;

    @SerializedName("subject")
    @Expose
    private String subject;

    @SerializedName("student_class")
    @Expose
    private int student_class;

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getStudent_class() {
        return student_class;
    }

    public void setStudent_class(int student_class) {
        this.student_class = student_class;
    }
}

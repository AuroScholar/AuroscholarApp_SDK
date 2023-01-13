package com.auro.application.home.data.model.passportmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassportQuizGridModel {

    String quizHead;

    String quizData;
    int quizImagePath;
    int quizColor;

    public String getQuizHead() {
        return quizHead;
    }

    public void setQuizHead(String quizHead) {
        this.quizHead = quizHead;
    }

    public String getQuizData() {
        return quizData;
    }

    public void setQuizData(String quizData) {
        this.quizData = quizData;
    }

    public int getQuizImagePath() {
        return quizImagePath;
    }

    public void setQuizImagePath(int quizImagePath) {
        this.quizImagePath = quizImagePath;
    }

    public int getQuizColor() {
        return quizColor;
    }

    public void setQuizColor(int quizColor) {
        this.quizColor = quizColor;
    }
}

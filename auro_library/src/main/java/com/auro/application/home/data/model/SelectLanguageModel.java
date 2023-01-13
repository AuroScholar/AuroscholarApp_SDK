package com.auro.application.home.data.model;

public class SelectLanguageModel {

    public String language;

    private boolean isCheck;

    private String languageCode;

    String studentClassName;

    String languageId;

    String languageShortCode;

    public String getLanguageShortCode() {
        return languageShortCode;
    }

    public void setLanguageShortCode(String languageShortCode) {
        this.languageShortCode = languageShortCode;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getStudentClassName() {
        return studentClassName;
    }

    public void setStudentClassName(String studentClassName) {
        this.studentClassName = studentClassName;
    }

    public SelectLanguageModel() {
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}

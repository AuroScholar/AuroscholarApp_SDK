package com.auro.application.home.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguageResModel {

    @SerializedName("language_id")
    @Expose
    private Integer languageId;
    @SerializedName("language_name")
    @Expose
    private String languageName;
    @SerializedName("translated_language_name")
    @Expose
    private String translatedLanguageName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("language_code")
    @Expose
    private String languageCode;
    @SerializedName("short_code")
    @Expose
    private String shortCode;


    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getTranslatedLanguageName() {
        return translatedLanguageName;
    }

    public void setTranslatedLanguageName(String translatedLanguageName) {
        this.translatedLanguageName = translatedLanguageName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}

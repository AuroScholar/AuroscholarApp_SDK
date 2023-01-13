package com.auro.application.home.data.model.response;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguageListResModel {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("auro_logo")
    @Expose
    private String auroLogo;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("languages")
    @Expose
    private List<LanguageResModel> languages = null;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public List<LanguageResModel> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguageResModel> languages) {
        this.languages = languages;
    }

    public String getAuroLogo() {
        return auroLogo;
    }

    public void setAuroLogo(String auroLogo) {
        this.auroLogo = auroLogo;
    }

}

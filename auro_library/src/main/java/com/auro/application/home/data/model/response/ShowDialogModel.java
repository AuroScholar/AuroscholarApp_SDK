package com.auro.application.home.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pradeep Kumar Baral on 1/5/22.
 */
public class ShowDialogModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("show_dailogue")
    @Expose
    private String showDailogue;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("response_code")
    @Expose
    private Integer responseCode;

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

    public String getShowDailogue() {
        return showDailogue;
    }

    public void setShowDailogue(String showDailogue) {
        this.showDailogue = showDailogue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }
}

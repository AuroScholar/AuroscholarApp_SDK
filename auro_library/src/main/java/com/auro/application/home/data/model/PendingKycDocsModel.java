package com.auro.application.home.data.model;

/**
 * Created by Pradeep Kumar Baral on 1/5/22.
 */
public class PendingKycDocsModel {


    public String userId;
    public String isAgree;
    Integer userPreferedLanguageId;


    public Integer getUserPreferedLanguageId() {
        return userPreferedLanguageId;
    }

    public void setUserPreferedLanguageId(Integer userPreferedLanguageId) {
        this.userPreferedLanguageId = userPreferedLanguageId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(String isAgree) {
        this.isAgree = isAgree;
    }
}

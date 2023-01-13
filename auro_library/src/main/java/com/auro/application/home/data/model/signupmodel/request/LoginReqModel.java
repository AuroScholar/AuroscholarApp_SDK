package com.auro.application.home.data.model.signupmodel.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginReqModel implements Parcelable {

    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("user_type")
    @Expose
    private String userType;



    @SerializedName("language_version")
    String langVersion;

    @SerializedName("api_version")
    String apiVersion;

    @SerializedName("user_prefered_language_id")
    Integer userPreferedLanguageId;


    public Integer getUserPreferedLanguageId() {
        return userPreferedLanguageId;
    }

    public void setUserPreferedLanguageId(Integer userPreferedLanguageId) {
        this.userPreferedLanguageId = userPreferedLanguageId;
    }


    protected LoginReqModel(Parcel in) {
        userName = in.readString();
        password = in.readString();
        userType = in.readString();
        langVersion = in.readString();
        apiVersion = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(userType);
        dest.writeString(langVersion);
        dest.writeString(apiVersion);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoginReqModel> CREATOR = new Creator<LoginReqModel>() {
        @Override
        public LoginReqModel createFromParcel(Parcel in) {
            return new LoginReqModel(in);
        }

        @Override
        public LoginReqModel[] newArray(int size) {
            return new LoginReqModel[size];
        }
    };

    public String getLangVersion() {
        return langVersion;
    }

    public void setLangVersion(String langVersion) {
        this.langVersion = langVersion;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public  LoginReqModel() {

    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

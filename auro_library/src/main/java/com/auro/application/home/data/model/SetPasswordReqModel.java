package com.auro.application.home.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetPasswordReqModel implements Parcelable {
    @SerializedName("user_name")
    @Expose
    private String mobileNo;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("language_version")
    String langVersion;

    @SerializedName("api_version")
    String apiVersion;

    @SerializedName("user_prefered_language_id")
    Integer userPreferedLanguageId;

    protected SetPasswordReqModel(Parcel in) {
        mobileNo = in.readString();
        password = in.readString();
        userId = in.readString();
        langVersion = in.readString();
        apiVersion = in.readString();
        userPreferedLanguageId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobileNo);
        dest.writeString(password);
        dest.writeString(userId);
        dest.writeString(langVersion);
        dest.writeString(apiVersion);
        dest.writeInt(userPreferedLanguageId);
    }


    public Integer getUserPreferedLanguageId() {
        return userPreferedLanguageId;
    }

    public void setUserPreferedLanguageId(Integer userPreferedLanguageId) {
        this.userPreferedLanguageId = userPreferedLanguageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SetPasswordReqModel> CREATOR = new Creator<SetPasswordReqModel>() {
        @Override
        public SetPasswordReqModel createFromParcel(Parcel in) {
            return new SetPasswordReqModel(in);
        }

        @Override
        public SetPasswordReqModel[] newArray(int size) {
            return new SetPasswordReqModel[size];
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

    public SetPasswordReqModel() {

    }


    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

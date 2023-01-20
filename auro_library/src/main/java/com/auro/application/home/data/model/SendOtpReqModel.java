package com.auro.application.home.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendOtpReqModel implements Parcelable {

    @SerializedName("mobile_no")
    @Expose
    String mobileNo;

    @SerializedName("is_type")
    @Expose
    String isType;

    @SerializedName("language_version")
    String langVersion;

    @SerializedName("api_version")
    String apiVersion;

    public String getUser_prefered_language_id() {
        return user_prefered_language_id;
    }

    public void setUser_prefered_language_id(String user_prefered_language_id) {
        this.user_prefered_language_id = user_prefered_language_id;
    }

    @SerializedName("user_prefered_language_id")
    String user_prefered_language_id;

    protected SendOtpReqModel(Parcel in) {
        mobileNo = in.readString();
        isType = in.readString();
        langVersion = in.readString();
        apiVersion = in.readString();
        user_prefered_language_id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobileNo);
        dest.writeString(isType);
        dest.writeString(langVersion);
        dest.writeString(apiVersion);
        dest.writeString(user_prefered_language_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SendOtpReqModel> CREATOR = new Creator<SendOtpReqModel>() {
        @Override
        public SendOtpReqModel createFromParcel(Parcel in) {
            return new SendOtpReqModel(in);
        }

        @Override
        public SendOtpReqModel[] newArray(int size) {
            return new SendOtpReqModel[size];
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

    public SendOtpReqModel() {

    }



    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }


}

package com.auro.application.home.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompareKYCDetailsModel implements Parcelable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("confidence")
    @Expose
    private String confidence;


    protected CompareKYCDetailsModel(Parcel in) {
        message = in.readString();
        type = in.readString();
        confidence = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(type);
        dest.writeString(confidence);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CompareKYCDetailsModel> CREATOR = new Creator<CompareKYCDetailsModel>() {
        @Override
        public CompareKYCDetailsModel createFromParcel(Parcel in) {
            return new CompareKYCDetailsModel(in);
        }

        @Override
        public CompareKYCDetailsModel[] newArray(int size) {
            return new CompareKYCDetailsModel[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }
}
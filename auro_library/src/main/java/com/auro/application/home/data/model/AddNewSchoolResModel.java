package com.auro.application.home.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AddNewSchoolResModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response_code")
    @Expose
    private int responseCode;
    @SerializedName("schoolID")
    @Expose
    private int schoolID;

    public int getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }

    protected AddNewSchoolResModel(Parcel in) {
        status = in.readString();
        error = in.readByte() != 0;
        message = in.readString();
        responseCode = in.readInt();
        schoolID = in.readInt();



    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeString(message);
        dest.writeInt(responseCode);
        dest.writeInt(schoolID);


    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddNewSchoolResModel> CREATOR = new Creator<AddNewSchoolResModel>() {
        @Override
        public AddNewSchoolResModel createFromParcel(Parcel in) {
            return new AddNewSchoolResModel(in);
        }

        @Override
        public AddNewSchoolResModel[] newArray(int size) {
            return new AddNewSchoolResModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
















}

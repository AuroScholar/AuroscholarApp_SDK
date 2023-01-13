package com.auro.application.home.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeGradeResModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("oldgrade")
    @Expose
    private String oldgrade;
    @SerializedName("newgrade")
    @Expose
    private String newgrade;
    @SerializedName("message")
    @Expose
    private String message;

    protected ChangeGradeResModel(Parcel in) {
        status = in.readString();
        byte tmpError = in.readByte();
        error = tmpError == 0 ? null : tmpError == 1;
        oldgrade = in.readString();
        newgrade = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error == null ? 0 : error ? 1 : 2));
        dest.writeString(oldgrade);
        dest.writeString(newgrade);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChangeGradeResModel> CREATOR = new Creator<ChangeGradeResModel>() {
        @Override
        public ChangeGradeResModel createFromParcel(Parcel in) {
            return new ChangeGradeResModel(in);
        }

        @Override
        public ChangeGradeResModel[] newArray(int size) {
            return new ChangeGradeResModel[size];
        }
    };

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

    public String getOldgrade() {
        return oldgrade;
    }

    public void setOldgrade(String oldgrade) {
        this.oldgrade = oldgrade;
    }

    public String getNewgrade() {
        return newgrade;
    }

    public void setNewgrade(String newgrade) {
        this.newgrade = newgrade;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
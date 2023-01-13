package com.auro.application.home.data.model.signupmodel.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetUsernamePinResModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_mobile")
    @Expose
    private String userMobile;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response_code")
    @Expose
    private Integer responseCode;

    protected SetUsernamePinResModel(Parcel in) {
        status = in.readString();
        userMobile = in.readString();
        byte tmpError = in.readByte();
        error = tmpError == 0 ? null : tmpError == 1;
        message = in.readString();
        if (in.readByte() == 0) {
            responseCode = null;
        } else {
            responseCode = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(userMobile);
        dest.writeByte((byte) (error == null ? 0 : error ? 1 : 2));
        dest.writeString(message);
        if (responseCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(responseCode);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SetUsernamePinResModel> CREATOR = new Creator<SetUsernamePinResModel>() {
        @Override
        public SetUsernamePinResModel createFromParcel(Parcel in) {
            return new SetUsernamePinResModel(in);
        }

        @Override
        public SetUsernamePinResModel[] newArray(int size) {
            return new SetUsernamePinResModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
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


}

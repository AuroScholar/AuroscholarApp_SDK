package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeacherStudentPassportDetailResModel implements Parcelable {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("response_code")
    @Expose
    private int responseCode;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<PassportMonthResModel> data = null;

    public List<PassportMonthResModel> getData() {
        return data;
    }

    public void setData(List<PassportMonthResModel> data) {
        this.data = data;
    }

    protected TeacherStudentPassportDetailResModel(Parcel in) {
        status = in.readString();
        error = in.readByte() != 0;
        responseCode = in.readInt();
        message = in.readString();

        data = in.createTypedArrayList(PassportMonthResModel.CREATOR);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeInt(responseCode);
        dest.writeString(message);
        dest.writeTypedList(data);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeacherStudentPassportDetailResModel> CREATOR = new Creator<TeacherStudentPassportDetailResModel>() {
        @Override
        public TeacherStudentPassportDetailResModel createFromParcel(Parcel in) {
            return new TeacherStudentPassportDetailResModel(in);
        }

        @Override
        public TeacherStudentPassportDetailResModel[] newArray(int size) {
            return new TeacherStudentPassportDetailResModel[size];
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

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

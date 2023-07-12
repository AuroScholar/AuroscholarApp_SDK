package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassportMonthResModel implements Parcelable {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("dateName")
    @Expose
    private String dateName;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public PassportMonthResModel()
    {

    }

    protected PassportMonthResModel(Parcel in) {
        date = in.readString();
        dateName = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(dateName);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PassportMonthResModel> CREATOR = new Creator<PassportMonthResModel>() {
        @Override
        public PassportMonthResModel createFromParcel(Parcel in) {
            return new PassportMonthResModel(in);
        }

        @Override
        public PassportMonthResModel[] newArray(int size) {
            return new PassportMonthResModel[size];
        }
    };




}

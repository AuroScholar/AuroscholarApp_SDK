package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.auro.application.home.data.model.SubjectResModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AvailableSlotResModel implements Parcelable {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time_slots")
    @Expose
    private List<TimeSlotResModel> timeSlots = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<TimeSlotResModel> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlotResModel> timeSlots) {
        this.timeSlots = timeSlots;
    }

    protected  AvailableSlotResModel(Parcel parcel){
        timeSlots = parcel.createTypedArrayList(TimeSlotResModel.CREATOR);
        date = parcel.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(timeSlots);
        dest.writeString(date);
    }
}

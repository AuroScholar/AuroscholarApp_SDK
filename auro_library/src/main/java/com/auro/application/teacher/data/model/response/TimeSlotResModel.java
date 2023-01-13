package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.auro.application.home.data.model.QuizResModel;
import com.auro.application.home.data.model.SubjectResModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlotResModel  implements Parcelable{

    @SerializedName("webinar_name")
    @Expose
    private String webinarName;
    @SerializedName("webinar_id")
    @Expose
    private Integer webinarId;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("seats_left")
    @Expose
    private Integer seatsLeft;

    private boolean isSelected;

    public static final Parcelable.Creator<TimeSlotResModel> CREATOR = new Parcelable.Creator<TimeSlotResModel>() {
        @Override
        public TimeSlotResModel createFromParcel(Parcel in) {
            return new TimeSlotResModel(in);
        }

        @Override
        public TimeSlotResModel[] newArray(int size) {
            return new TimeSlotResModel[size];
        }
    };


    public String getWebinarName() {
        return webinarName;
    }

    public void setWebinarName(String webinarName) {
        this.webinarName = webinarName;
    }

    public Integer getWebinarId() {
        return webinarId;
    }

    public void setWebinarId(Integer webinarId) {
        this.webinarId = webinarId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getSeatsLeft() {
        return seatsLeft;
    }

    public void setSeatsLeft(Integer seatsLeft) {
        this.seatsLeft = seatsLeft;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    protected TimeSlotResModel(Parcel in) {
        webinarName = in.readString();
        webinarId = in.readInt();
        startTime = in.readString();
        seatsLeft = in.readInt();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(webinarName);
        dest.writeInt(webinarId);
        dest.writeString(startTime);
        dest.writeInt(seatsLeft);

    }
}

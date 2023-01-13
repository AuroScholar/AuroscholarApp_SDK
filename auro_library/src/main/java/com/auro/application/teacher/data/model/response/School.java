package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class School  implements Parcelable {
    @SerializedName("school_name")
    @Expose
    private String schoolName;
    @SerializedName("udise_code")
    @Expose
    private String udiseCode;
    @SerializedName("school_id")
    @Expose
    private int schoolId;
    @SerializedName("village_name")
    @Expose
    private String villageName;

    protected School(Parcel in) {
        schoolName = in.readString();
        udiseCode = in.readString();
        schoolId = in.readInt();
        villageName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(schoolName);
        dest.writeString(udiseCode);
        dest.writeInt(schoolId);
        dest.writeString(villageName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<School> CREATOR = new Creator<School>() {
        @Override
        public School createFromParcel(Parcel in) {
            return new School(in);
        }

        @Override
        public School[] newArray(int size) {
            return new School[size];
        }
    };

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getUdiseCode() {
        return udiseCode;
    }

    public void setUdiseCode(String udiseCode) {
        this.udiseCode = udiseCode;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
}

package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalStudentResModel implements Parcelable {
    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("student_name")
    @Expose
    private String studentName;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("total_score")
    @Expose
    private String totalScore;
    @SerializedName("group_id")
    @Expose
    private String groupId;

    public TotalStudentResModel()
    {

    }

    protected TotalStudentResModel(Parcel in) {
        studentId = in.readString();
        studentName = in.readString();
        profilePic = in.readString();
        totalScore = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentId);
        dest.writeString(studentName);
        dest.writeString(profilePic);
        dest.writeString(totalScore);
        dest.writeString(groupId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TotalStudentResModel> CREATOR = new Creator<TotalStudentResModel>() {
        @Override
        public TotalStudentResModel createFromParcel(Parcel in) {
            return new TotalStudentResModel(in);
        }

        @Override
        public TotalStudentResModel[] newArray(int size) {
            return new TotalStudentResModel[size];
        }
    };

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}

package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeacherGroupRes implements Parcelable {
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("group_creation_date")
    @Expose
    private String groupCreationDate;
    @SerializedName("group_members")
    @Expose
    private List<String> groupMembers = null;

    public TeacherGroupRes() {

    }


    protected TeacherGroupRes(Parcel in) {
        groupName = in.readString();
        groupId = in.readString();
        groupCreationDate = in.readString();
        groupMembers = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupName);
        dest.writeString(groupId);
        dest.writeString(groupCreationDate);
        dest.writeStringList(groupMembers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeacherGroupRes> CREATOR = new Creator<TeacherGroupRes>() {
        @Override
        public TeacherGroupRes createFromParcel(Parcel in) {
            return new TeacherGroupRes(in);
        }

        @Override
        public TeacherGroupRes[] newArray(int size) {
            return new TeacherGroupRes[size];
        }
    };

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupCreationDate() {
        return groupCreationDate;
    }

    public void setGroupCreationDate(String groupCreationDate) {
        this.groupCreationDate = groupCreationDate;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<String> groupMembers) {
        this.groupMembers = groupMembers;
    }
}

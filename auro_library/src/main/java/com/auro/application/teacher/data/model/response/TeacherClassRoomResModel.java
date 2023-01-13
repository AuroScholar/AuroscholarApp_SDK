package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeacherClassRoomResModel implements Parcelable {


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

    @SerializedName("total_student_list")
    @Expose
    private List<TotalStudentResModel> totalStudentList = null;
    @SerializedName("groups")
    @Expose
    private List<TeacherGroupRes> groups = null;


    protected TeacherClassRoomResModel(Parcel in) {
        status = in.readString();
        error = in.readByte() != 0;
        responseCode = in.readInt();
        message = in.readString();
        totalStudentList = in.createTypedArrayList(TotalStudentResModel.CREATOR);
        groups = in.createTypedArrayList(TeacherGroupRes.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeInt(responseCode);
        dest.writeString(message);
        dest.writeTypedList(totalStudentList);
        dest.writeTypedList(groups);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeacherClassRoomResModel> CREATOR = new Creator<TeacherClassRoomResModel>() {
        @Override
        public TeacherClassRoomResModel createFromParcel(Parcel in) {
            return new TeacherClassRoomResModel(in);
        }

        @Override
        public TeacherClassRoomResModel[] newArray(int size) {
            return new TeacherClassRoomResModel[size];
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

    public List<TotalStudentResModel> getTotalStudentList() {
        return totalStudentList;
    }

    public void setTotalStudentList(List<TotalStudentResModel> totalStudentList) {
        this.totalStudentList = totalStudentList;
    }

    public List<TeacherGroupRes> getGroups() {
        return groups;
    }

    public void setGroups(List<TeacherGroupRes> groups) {
        this.groups = groups;
    }


}

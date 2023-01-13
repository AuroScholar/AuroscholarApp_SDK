package com.auro.application.home.data.model.response;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckUserValidResModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("student_class")
    @Expose
    private String studentClass;

    @SerializedName("classes")
    @Expose
    private List<String> classes;

    public CheckUserValidResModel() {

    }


    protected CheckUserValidResModel(Parcel in) {
        status = in.readString();
        byte tmpError = in.readByte();
        error = tmpError == 0 ? null : tmpError == 1;
        message = in.readString();
        mobileNo = in.readString();
        emailId = in.readString();
        studentClass = in.readString();
        classes = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error == null ? 0 : error ? 1 : 2));
        dest.writeString(message);
        dest.writeString(mobileNo);
        dest.writeString(emailId);
        dest.writeString(studentClass);
        dest.writeStringList(classes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckUserValidResModel> CREATOR = new Creator<CheckUserValidResModel>() {
        @Override
        public CheckUserValidResModel createFromParcel(Parcel in) {
            return new CheckUserValidResModel(in);
        }

        @Override
        public CheckUserValidResModel[] newArray(int size) {
            return new CheckUserValidResModel[size];
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }


    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }
}

package com.auro.application.home.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailResModel implements Parcelable {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("registration_id")
    @Expose
    private String registrationId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("user_mobile")
    @Expose
    private String userMobile;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("pin")
    @Expose
    private String pin;
    @SerializedName("is_master")
    @Expose
    private String isMaster;
    @SerializedName("student_name")
    @Expose
    private String studentName;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("is_password_set")
    @Expose
    private boolean setUserPass;
    @SerializedName("is_pin_set")
    @Expose
    private boolean setPin;

    @SerializedName("is_username_set")
    @Expose
    private boolean isUsername;
    @SerializedName("profile_pic")
    @Expose
    private String profilepic;
    @SerializedName("wallet")
    @Expose
    private String wallet;

    public boolean isSetUserPass() {
        return setUserPass;
    }

    public boolean isSetPin() {
        return setPin;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public UserDetailResModel()
    {

    }

    protected UserDetailResModel(Parcel in) {
        userId = in.readString();
        registrationId = in.readString();
        userName = in.readString();
        password = in.readString();
        userMobile = in.readString();
        grade = in.readString();
        pin = in.readString();
        isMaster = in.readString();
        studentName = in.readString();
        message = in.readString();
        setUserPass = in.readByte() != 0;
        setPin = in.readByte() != 0;
        isUsername = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(registrationId);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(userMobile);
        dest.writeString(grade);
        dest.writeString(pin);
        dest.writeString(isMaster);
        dest.writeString(studentName);
        dest.writeString(message);
        dest.writeByte((byte) (setUserPass ? 1 : 0));
        dest.writeByte((byte) (setPin ? 1 : 0));
        dest.writeByte((byte) (isUsername ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserDetailResModel> CREATOR = new Creator<UserDetailResModel>() {
        @Override
        public UserDetailResModel createFromParcel(Parcel in) {
            return new UserDetailResModel(in);
        }

        @Override
        public UserDetailResModel[] newArray(int size) {
            return new UserDetailResModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(String isMaster) {
        this.isMaster = isMaster;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSetUserPass() {
        return setUserPass;
    }

    public void setSetUserPass(boolean setUserPass) {
        this.setUserPass = setUserPass;
    }

    public boolean getSetPin() {
        return setPin;
    }

    public void setSetPin(boolean setPin) {
        this.setPin = setPin;
    }

    public boolean isUsername() {
        return isUsername;
    }

    public void setUsername(boolean username) {
        isUsername = username;
    }
}
package com.auro.application.teacher.data.model.response;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MyProfileResModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response_code")
    @Expose
    private int responseCode;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("teacher_id")
    @Expose
    private int teacherId;
    @SerializedName("teacher_name")
    @Expose
    private String teacherName;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("mobile_no_verified")
    @Expose
    private int mobileNoVerified;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("email_id_verified")
    @Expose
    private int emailIdVerified;
    @SerializedName("country_id")
    @Expose
    private int countryId;
    @SerializedName("state_id")
    @Expose
    private int stateId;
    @SerializedName("district_id")
    @Expose
    private int districtId;
    @SerializedName("school_id")
    @Expose
    private int schoolId;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("teacher_profile_pic")
    @Expose
    private String teacherProfilePic;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("schools")
    @Expose
    private List<School> schools = null;
    @SerializedName("teacher_subject_grade_mappings")
    @Expose
    private List<Object> teacherSubjectGradeMappings = null;

    protected MyProfileResModel(Parcel in) {
        status = in.readString();
        error = in.readByte() != 0;
        message = in.readString();
        responseCode = in.readInt();
        userId = in.readInt();
        teacherId = in.readInt();
        teacherName = in.readString();
        mobileNo = in.readString();
        mobileNoVerified = in.readInt();
        emailId = in.readString();
        emailIdVerified = in.readInt();
        countryId = in.readInt();
        stateId = in.readInt();
        districtId = in.readInt();
        schoolId = in.readInt();
        gender = in.readString();
        dateOfBirth = in.readString();
        deviceToken = in.readString();
        teacherProfilePic = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeString(message);
        dest.writeInt(responseCode);
        dest.writeInt(userId);
        dest.writeInt(teacherId);
        dest.writeString(teacherName);
        dest.writeString(mobileNo);
        dest.writeInt(mobileNoVerified);
        dest.writeString(emailId);
        dest.writeInt(emailIdVerified);
        dest.writeInt(countryId);
        dest.writeInt(stateId);
        dest.writeInt(districtId);
        dest.writeInt(schoolId);
        dest.writeString(gender);
        dest.writeString(dateOfBirth);
        dest.writeString(deviceToken);
        dest.writeString(teacherProfilePic);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyProfileResModel> CREATOR = new Creator<MyProfileResModel>() {
        @Override
        public MyProfileResModel createFromParcel(Parcel in) {
            return new MyProfileResModel(in);
        }

        @Override
        public MyProfileResModel[] newArray(int size) {
            return new MyProfileResModel[size];
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getMobileNoVerified() {
        return mobileNoVerified;
    }

    public void setMobileNoVerified(int mobileNoVerified) {
        this.mobileNoVerified = mobileNoVerified;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getEmailIdVerified() {
        return emailIdVerified;
    }

    public void setEmailIdVerified(int emailIdVerified) {
        this.emailIdVerified = emailIdVerified;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getTeacherProfilePic() {
        return teacherProfilePic;
    }

    public void setTeacherProfilePic(String teacherProfilePic) {
        this.teacherProfilePic = teacherProfilePic;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }

    public List<Object> getTeacherSubjectGradeMappings() {
        return teacherSubjectGradeMappings;
    }

    public void setTeacherSubjectGradeMappings(List<Object> teacherSubjectGradeMappings) {
        this.teacherSubjectGradeMappings = teacherSubjectGradeMappings;
    }
}

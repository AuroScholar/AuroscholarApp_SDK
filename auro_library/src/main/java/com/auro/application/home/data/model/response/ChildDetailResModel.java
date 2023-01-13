package com.auro.application.home.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChildDetailResModel {

    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;
    @SerializedName("student_id")
    @Expose
    private int student_id;
    @SerializedName("student_name")
    @Expose
    private String student_name;
    @SerializedName("email_id")
    @Expose
    private String email_id;
    @SerializedName("studentclass")
    @Expose
    private int studentclass;
    @SerializedName("registration_id")
    @Expose
    private int registration_id;
    @SerializedName("auroid")
    @Expose
    private int auroid;
    @SerializedName("registrationdate")
    @Expose
    private String registrationdate;
    @SerializedName("partner_source")
    @Expose
    private String partner_source;
    @SerializedName("device_token")
    @Expose
    private String device_token;
    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;
    @SerializedName("schoolid")
    @Expose
    private int schoolid;

    @SerializedName("school_type")
    @Expose
    private String school_type;

    @SerializedName("board_type")
    @Expose
    private String board_type;
    @SerializedName("private_tution_type")
    @Expose
    private String private_tution_type;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("email_verified")
    @Expose
    private String email_verified;
    @SerializedName("user_type_id")
    @Expose
    private int user_type_id;
    @SerializedName("regitration_source")
    @Expose
    private String regitration_source;
    @SerializedName("user_id")
    @Expose
    private int user_id;
    @SerializedName("school_name")
    @Expose
    private String school_name;
    @SerializedName("is_kyc_verified")
    @Expose
    private String is_kyc_verified;
    @SerializedName("is_kyc_uploaded")
    @Expose
    private String is_kyc_uploaded;
    @SerializedName("state_id")
    @Expose
    private int state_id;
    @SerializedName("district_id")
    @Expose
    private int district_id;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("father_name")
    @Expose
    private String father_name;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("is_private_tution")
    @Expose
    private String is_private_tution;
    @SerializedName("tution_mode")
    @Expose
    private String tution_mode;
    @SerializedName("build_version")
    @Expose
    private String build_version;
    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public int getStudentclass() {
        return studentclass;
    }

    public void setStudentclass(int studentclass) {
        this.studentclass = studentclass;
    }

    public int getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(int registration_id) {
        this.registration_id = registration_id;
    }

    public int getAuroid() {
        return auroid;
    }

    public void setAuroid(int auroid) {
        this.auroid = auroid;
    }

    public String getRegistrationdate() {
        return registrationdate;
    }

    public void setRegistrationdate(String registrationdate) {
        this.registrationdate = registrationdate;
    }

    public String getPartner_source() {
        return partner_source;
    }

    public void setPartner_source(String partner_source) {
        this.partner_source = partner_source;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public int getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(int schoolid) {
        this.schoolid = schoolid;
    }

    public String getSchool_type() {
        return school_type;
    }

    public void setSchool_type(String school_type) {
        this.school_type = school_type;
    }

    public String getBoard_type() {
        return board_type;
    }

    public void setBoard_type(String board_type) {
        this.board_type = board_type;
    }

    public String getPrivate_tution_type() {
        return private_tution_type;
    }

    public void setPrivate_tution_type(String private_tution_type) {
        this.private_tution_type = private_tution_type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(String email_verified) {
        this.email_verified = email_verified;
    }

    public int getUser_type_id() {
        return user_type_id;
    }

    public void setUser_type_id(int user_type_id) {
        this.user_type_id = user_type_id;
    }

    public String getRegitration_source() {
        return regitration_source;
    }

    public void setRegitration_source(String regitration_source) {
        this.regitration_source = regitration_source;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getIs_kyc_verified() {
        return is_kyc_verified;
    }

    public void setIs_kyc_verified(String is_kyc_verified) {
        this.is_kyc_verified = is_kyc_verified;
    }

    public String getIs_kyc_uploaded() {
        return is_kyc_uploaded;
    }

    public void setIs_kyc_uploaded(String is_kyc_uploaded) {
        this.is_kyc_uploaded = is_kyc_uploaded;
    }

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIs_private_tution() {
        return is_private_tution;
    }

    public void setIs_private_tution(String is_private_tution) {
        this.is_private_tution = is_private_tution;
    }

    public String getTution_mode() {
        return tution_mode;
    }

    public void setTution_mode(String tution_mode) {
        this.tution_mode = tution_mode;
    }

    public String getBuild_version() {
        return build_version;
    }

    public void setBuild_version(String build_version) {
        this.build_version = build_version;
    }
}
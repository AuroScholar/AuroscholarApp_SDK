package com.auro.application.teacher.data.model.request;

public class TeacherReqModel {


    String user_id ="";
    String teacher_name="";
    String user_name="";
    String mobile_no="";
    String email_id="";
    String country_id="";
    String state_id="";
    String district_id="";
    String school_id="";
    String prefered_language="";
    String gender="";
    String date_of_birth="";
    String device_token="";

    byte[] teacher_profile_pic ;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public byte[] getTeacher_profile_pic() {
        return teacher_profile_pic;
    }

    public void setTeacher_profile_pic(byte[] teacher_profile_pic) {
        this.teacher_profile_pic = teacher_profile_pic;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getPrefered_language() {
        return prefered_language;
    }

    public void setPrefered_language(String prefered_language) {
        this.prefered_language = prefered_language;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}

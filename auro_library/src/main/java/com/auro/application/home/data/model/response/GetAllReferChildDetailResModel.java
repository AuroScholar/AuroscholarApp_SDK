package com.auro.application.home.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllReferChildDetailResModel {


    @SerializedName("student_name")
    @Expose
    private String student_name;


    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;



    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;
    @SerializedName("request_status")
    @Expose
    private String request_status;

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }
}
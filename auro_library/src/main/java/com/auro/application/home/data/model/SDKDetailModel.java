package com.auro.application.home.data.model;

import com.auro.application.home.data.model.response.UserDetailResModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SDKDetailModel {

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("student_name")
    @Expose
    private String student_name;
    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;
    @SerializedName("user_prefered_language_id")
    @Expose
    private String user_prefered_language_id;
    @SerializedName("partner_source")
    @Expose
    private String partner_source;
    @SerializedName("kyc_status")
    @Expose
    private String kyc_status;
    @SerializedName("grade")
    @Expose
    private int grade;
    @SerializedName("mobile_verified")
    @Expose
    private String mobile_verified;
    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;
    @SerializedName("partner_logo")
    @Expose
    private String partner_logo;
    @SerializedName("is_mapped")
    @Expose
    private int is_mapped;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

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

    public String getUser_prefered_language_id() {
        return user_prefered_language_id;
    }

    public void setUser_prefered_language_id(String user_prefered_language_id) {
        this.user_prefered_language_id = user_prefered_language_id;
    }

    public String getPartner_source() {
        return partner_source;
    }

    public void setPartner_source(String partner_source) {
        this.partner_source = partner_source;
    }

    public String getKyc_status() {
        return kyc_status;
    }

    public void setKyc_status(String kyc_status) {
        this.kyc_status = kyc_status;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getMobile_verified() {
        return mobile_verified;
    }

    public void setMobile_verified(String mobile_verified) {
        this.mobile_verified = mobile_verified;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getPartner_logo() {
        return partner_logo;
    }

    public void setPartner_logo(String partner_logo) {
        this.partner_logo = partner_logo;
    }

    public int getIs_mapped() {
        return is_mapped;
    }

    public void setIs_mapped(int is_mapped) {
        this.is_mapped = is_mapped;
    }
}
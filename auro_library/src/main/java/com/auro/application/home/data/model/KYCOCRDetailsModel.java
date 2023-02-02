package com.auro.application.home.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KYCOCRDetailsModel implements Parcelable {

    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("aadhaar_no")
    @Expose
    private String aadhaar_no;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("student_name")
    @Expose
    private String student_name;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("address")
    @Expose
    private String address;



    protected KYCOCRDetailsModel(Parcel in) {
        dob = in.readString();
        aadhaar_no = in.readString();
        gender = in.readString();
        student_name = in.readString();
        pincode = in.readString();
        address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dob);
        dest.writeString(aadhaar_no);
        dest.writeString(gender);
        dest.writeString(student_name);
        dest.writeString(pincode);
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KYCOCRDetailsModel> CREATOR = new Creator<KYCOCRDetailsModel>() {
        @Override
        public KYCOCRDetailsModel createFromParcel(Parcel in) {
            return new KYCOCRDetailsModel(in);
        }

        @Override
        public KYCOCRDetailsModel[] newArray(int size) {
            return new KYCOCRDetailsModel[size];
        }
    };

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAadhaar_no() {
        return aadhaar_no;
    }

    public void setAadhaar_no(String aadhaar_no) {
        this.aadhaar_no = aadhaar_no;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
package com.auro.application.home.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PartnersLoginReqModel implements Parcelable {

    String partnerId;
    String partnerName;
    String studentName;
    String studentEmail;
    String studentPassword;
    String studentMobile;
    String studentClass;
    public Integer userPreferedLanguageId;

    public PartnersLoginReqModel() {

    }


    protected PartnersLoginReqModel(Parcel in) {
        partnerId = in.readString();
        partnerName = in.readString();
        studentName = in.readString();
        studentEmail = in.readString();
        studentPassword = in.readString();
        studentMobile = in.readString();
        studentClass = in.readString();
        userPreferedLanguageId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(partnerId);
        dest.writeString(partnerName);
        dest.writeString(studentName);
        dest.writeString(studentEmail);
        dest.writeString(studentPassword);
        dest.writeString(studentMobile);
        dest.writeString(studentClass);
        dest.writeInt(userPreferedLanguageId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PartnersLoginReqModel> CREATOR = new Creator<PartnersLoginReqModel>() {
        @Override
        public PartnersLoginReqModel createFromParcel(Parcel in) {
            return new PartnersLoginReqModel(in);
        }

        @Override
        public PartnersLoginReqModel[] newArray(int size) {
            return new PartnersLoginReqModel[size];
        }
    };

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }

    public Integer getUserPreferedLanguageId() {
        return userPreferedLanguageId;
    }

    public void setUserPreferedLanguageId(Integer userPreferedLanguageId) {
        this.userPreferedLanguageId = userPreferedLanguageId;
    }

    public String getStudentMobile() {
        return studentMobile;
    }

    public void setStudentMobile(String studentMobile) {
        this.studentMobile = studentMobile;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }
}

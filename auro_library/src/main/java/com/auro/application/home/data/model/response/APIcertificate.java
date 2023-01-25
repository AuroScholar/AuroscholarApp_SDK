package com.auro.application.home.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIcertificate implements Parcelable {


    @SerializedName("UserId")
    @Expose
    private String UserId="";
    @SerializedName("CertificateNo")
    @Expose
    private String CertificateNo="";
    @SerializedName("downloalink")
    @Expose
    private String downloalink="";
    @SerializedName("certificateviewlink")
    @Expose
    private String certificateviewlink="";

    protected APIcertificate(Parcel in) {
        UserId = in.readString();
        CertificateNo = in.readString();
        downloalink = in.readString();
        certificateviewlink = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UserId);
        dest.writeString(CertificateNo);
        dest.writeString(downloalink);
        dest.writeString(certificateviewlink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<APIcertificate> CREATOR = new Creator<APIcertificate>() {
        @Override
        public APIcertificate createFromParcel(Parcel in) {
            return new APIcertificate(in);
        }

        @Override
        public APIcertificate[] newArray(int size) {
            return new APIcertificate[size];
        }
    };

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCertificateNo() {
        return CertificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        CertificateNo = certificateNo;
    }

    public String getDownloalink() {
        return downloalink;
    }

    public void setDownloalink(String downloalink) {
        this.downloalink = downloalink;
    }

    public String getCertificateviewlink() {
        return certificateviewlink;
    }

    public void setCertificateviewlink(String certificateviewlink) {
        this.certificateviewlink = certificateviewlink;
    }
}

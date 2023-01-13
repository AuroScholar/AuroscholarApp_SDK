package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TeacherKycStatusResModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("kyc_status")
    @Expose
    private String kycStatus;
    @SerializedName("is_kyc_uploaded")
    @Expose
    private String isKycUploaded;
    @SerializedName("kyc_docs_data")
    @Expose
    private List<KycItemDataResModel> kycDocsData = null;

    public TeacherKycStatusResModel()
    {

    }

    protected TeacherKycStatusResModel(Parcel in) {
        status = in.readString();
        byte tmpError = in.readByte();
        error = tmpError == 0 ? null : tmpError == 1;
        message = in.readString();
        if (in.readByte() == 0) {
            responseCode = null;
        } else {
            responseCode = in.readInt();
        }
        kycStatus = in.readString();
        isKycUploaded = in.readString();
        kycDocsData = in.createTypedArrayList(KycItemDataResModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error == null ? 0 : error ? 1 : 2));
        dest.writeString(message);
        if (responseCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(responseCode);
        }
        dest.writeString(kycStatus);
        dest.writeString(isKycUploaded);
        dest.writeTypedList(kycDocsData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeacherKycStatusResModel> CREATOR = new Creator<TeacherKycStatusResModel>() {
        @Override
        public TeacherKycStatusResModel createFromParcel(Parcel in) {
            return new TeacherKycStatusResModel(in);
        }

        @Override
        public TeacherKycStatusResModel[] newArray(int size) {
            return new TeacherKycStatusResModel[size];
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

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    public String getIsKycUploaded() {
        return isKycUploaded;
    }

    public void setIsKycUploaded(String isKycUploaded) {
        this.isKycUploaded = isKycUploaded;
    }

    public List<KycItemDataResModel> getKycDocsData() {
        return kycDocsData;
    }

    public void setKycDocsData(List<KycItemDataResModel> kycDocsData) {
        this.kycDocsData = kycDocsData;
    }

}
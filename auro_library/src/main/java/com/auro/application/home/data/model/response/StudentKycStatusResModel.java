package com.auro.application.home.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class StudentKycStatusResModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("kyc_status")
    @Expose
    private String kycStatus;
    @SerializedName("is_kyc_uploaded")
    @Expose
    private String isKycUploaded;
    @SerializedName("kyc_docs_data")
    @Expose
    private List<KycDocResModel> kycDocsData = null;

    protected StudentKycStatusResModel(Parcel in) {
        status = in.readString();
        byte tmpError = in.readByte();
        error = tmpError == 0 ? null : tmpError == 1;
        if (in.readByte() == 0) {
            responseCode = null;
        } else {
            responseCode = in.readInt();
        }
        message = in.readString();
        kycStatus = in.readString();
        isKycUploaded = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error == null ? 0 : error ? 1 : 2));
        if (responseCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(responseCode);
        }
        dest.writeString(message);
        dest.writeString(kycStatus);
        dest.writeString(isKycUploaded);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StudentKycStatusResModel> CREATOR = new Creator<StudentKycStatusResModel>() {
        @Override
        public StudentKycStatusResModel createFromParcel(Parcel in) {
            return new StudentKycStatusResModel(in);
        }

        @Override
        public StudentKycStatusResModel[] newArray(int size) {
            return new StudentKycStatusResModel[size];
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

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public List<KycDocResModel> getKycDocsData() {
        return kycDocsData;
    }

    public void setKycDocsData(List<KycDocResModel> kycDocsData) {
        this.kycDocsData = kycDocsData;
    }

}
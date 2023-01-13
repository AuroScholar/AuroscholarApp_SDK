package com.auro.application.home.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentWalletResModel implements Parcelable {

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
    @SerializedName("walletbalance")
    @Expose
    private Integer walletbalance;
    @SerializedName("unapproved_scholarship_money")
    @Expose
    private Integer unapprovedScholarshipMoney;
    @SerializedName("approved_scholarship_money")
    @Expose
    private Integer approvedScholarshipMoney;
    @SerializedName("disapproved_scholarship_money")
    @Expose
    private Integer disapprovedScholarshipMoney;
    @SerializedName("inprocess_scholarship_money")
    @Expose
    private Integer inprocessScholarshipMoney;
    @SerializedName("disburse_scholarship_money")
    @Expose
    private Integer disburseScholarshipMoney;
    @SerializedName("currency")
    @Expose
    private String currency;

    protected StudentWalletResModel(Parcel in) {
        status = in.readString();
        byte tmpError = in.readByte();
        error = tmpError == 0 ? null : tmpError == 1;
        message = in.readString();
        if (in.readByte() == 0) {
            responseCode = null;
        } else {
            responseCode = in.readInt();
        }
        if (in.readByte() == 0) {
            walletbalance = null;
        } else {
            walletbalance = in.readInt();
        }
        if (in.readByte() == 0) {
            unapprovedScholarshipMoney = null;
        } else {
            unapprovedScholarshipMoney = in.readInt();
        }
        if (in.readByte() == 0) {
            approvedScholarshipMoney = null;
        } else {
            approvedScholarshipMoney = in.readInt();
        }
        if (in.readByte() == 0) {
            disapprovedScholarshipMoney = null;
        } else {
            disapprovedScholarshipMoney = in.readInt();
        }
        if (in.readByte() == 0) {
            inprocessScholarshipMoney = null;
        } else {
            inprocessScholarshipMoney = in.readInt();
        }
        if (in.readByte() == 0) {
            disburseScholarshipMoney = null;
        } else {
            disburseScholarshipMoney = in.readInt();
        }
        currency = in.readString();
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
        if (walletbalance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(walletbalance);
        }
        if (unapprovedScholarshipMoney == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(unapprovedScholarshipMoney);
        }
        if (approvedScholarshipMoney == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(approvedScholarshipMoney);
        }
        if (disapprovedScholarshipMoney == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(disapprovedScholarshipMoney);
        }
        if (inprocessScholarshipMoney == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(inprocessScholarshipMoney);
        }
        if (disburseScholarshipMoney == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(disburseScholarshipMoney);
        }
        dest.writeString(currency);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StudentWalletResModel> CREATOR = new Creator<StudentWalletResModel>() {
        @Override
        public StudentWalletResModel createFromParcel(Parcel in) {
            return new StudentWalletResModel(in);
        }

        @Override
        public StudentWalletResModel[] newArray(int size) {
            return new StudentWalletResModel[size];
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

    public Integer getWalletbalance() {
        return walletbalance;
    }

    public void setWalletbalance(Integer walletbalance) {
        this.walletbalance = walletbalance;
    }

    public Integer getUnapprovedScholarshipMoney() {
        return unapprovedScholarshipMoney;
    }

    public void setUnapprovedScholarshipMoney(Integer unapprovedScholarshipMoney) {
        this.unapprovedScholarshipMoney = unapprovedScholarshipMoney;
    }

    public Integer getApprovedScholarshipMoney() {
        return approvedScholarshipMoney;
    }

    public void setApprovedScholarshipMoney(Integer approvedScholarshipMoney) {
        this.approvedScholarshipMoney = approvedScholarshipMoney;
    }

    public Integer getDisapprovedScholarshipMoney() {
        return disapprovedScholarshipMoney;
    }

    public void setDisapprovedScholarshipMoney(Integer disapprovedScholarshipMoney) {
        this.disapprovedScholarshipMoney = disapprovedScholarshipMoney;
    }

    public Integer getInprocessScholarshipMoney() {
        return inprocessScholarshipMoney;
    }

    public void setInprocessScholarshipMoney(Integer inprocessScholarshipMoney) {
        this.inprocessScholarshipMoney = inprocessScholarshipMoney;
    }

    public Integer getDisburseScholarshipMoney() {
        return disburseScholarshipMoney;
    }

    public void setDisburseScholarshipMoney(Integer disburseScholarshipMoney) {
        this.disburseScholarshipMoney = disburseScholarshipMoney;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
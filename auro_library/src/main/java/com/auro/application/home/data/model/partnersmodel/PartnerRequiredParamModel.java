package com.auro.application.home.data.model.partnersmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PartnerRequiredParamModel  implements Parcelable {

    @SerializedName("email")
    @Expose
    private boolean email;
    @SerializedName("mobile_no")
    @Expose
    private boolean mobileNo;
    @SerializedName("class")
    @Expose
    private boolean _class;
    @SerializedName("name")
    @Expose
    private boolean name;

    protected PartnerRequiredParamModel(Parcel in) {
        email = in.readByte() != 0;
        mobileNo = in.readByte() != 0;
        _class = in.readByte() != 0;
        name = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (email ? 1 : 0));
        dest.writeByte((byte) (mobileNo ? 1 : 0));
        dest.writeByte((byte) (_class ? 1 : 0));
        dest.writeByte((byte) (name ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PartnerRequiredParamModel> CREATOR = new Creator<PartnerRequiredParamModel>() {
        @Override
        public PartnerRequiredParamModel createFromParcel(Parcel in) {
            return new PartnerRequiredParamModel(in);
        }

        @Override
        public PartnerRequiredParamModel[] newArray(int size) {
            return new PartnerRequiredParamModel[size];
        }
    };

    public boolean getEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(boolean mobileNo) {
        this.mobileNo = mobileNo;
    }

    public boolean getClass_() {
        return _class;
    }

    public void setClass_(boolean _class) {
        this._class = _class;
    }

    public boolean getName() {
        return name;
    }

    public void setName(boolean name) {
        this.name = name;
    }
}

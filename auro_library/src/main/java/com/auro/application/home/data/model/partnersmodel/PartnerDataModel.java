package com.auro.application.home.data.model.partnersmodel;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PartnerDataModel implements Parcelable {

    @SerializedName("partner_name")
    @Expose
    private String partnerName;
    @SerializedName("partner_source")
    @Expose
    private String partnerSource;
    @SerializedName("partner_logo")
    @Expose
    private String partnerLogo;
    @SerializedName("partner_id")
    @Expose
    private String partnerId;

    @SerializedName("is_active")
    @Expose
    private  boolean isActive;

    @SerializedName("required_params")
    @Expose
    private PartnerRequiredParamModel partnerRequiredParamModel;

    @SerializedName("partner_desc")
    @Expose
    private String description;


    protected PartnerDataModel(Parcel in) {
        partnerName = in.readString();
        partnerSource = in.readString();
        partnerLogo = in.readString();
        partnerId = in.readString();
        isActive = in.readByte() != 0;
        partnerRequiredParamModel = in.readParcelable(PartnerRequiredParamModel.class.getClassLoader());
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(partnerName);
        dest.writeString(partnerSource);
        dest.writeString(partnerLogo);
        dest.writeString(partnerId);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeParcelable(partnerRequiredParamModel, flags);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PartnerDataModel> CREATOR = new Creator<PartnerDataModel>() {
        @Override
        public PartnerDataModel createFromParcel(Parcel in) {
            return new PartnerDataModel(in);
        }

        @Override
        public PartnerDataModel[] newArray(int size) {
            return new PartnerDataModel[size];
        }
    };

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerSource() {
        return partnerSource;
    }

    public void setPartnerSource(String partnerSource) {
        this.partnerSource = partnerSource;
    }

    public String getPartnerLogo() {
        return partnerLogo;
    }

    public void setPartnerLogo(String partnerLogo) {
        this.partnerLogo = partnerLogo;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public PartnerRequiredParamModel getPartnerRequiredParamModel() {
        return partnerRequiredParamModel;
    }

    public void setPartnerRequiredParamModel(PartnerRequiredParamModel partnerRequiredParamModel) {
        this.partnerRequiredParamModel = partnerRequiredParamModel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
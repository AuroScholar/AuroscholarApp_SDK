package com.auro.application.home.data.model.response;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckVerResModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("version_id")
    @Expose
    private String versionId;
    @SerializedName("older_version")
    @Expose
    private String olderVersion;
    @SerializedName("new_version")
    @Expose
    private String newVersion;
    @SerializedName("priorty")
    @Expose
    private String priorty;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("feature")
    @Expose
    private String feature;


    protected CheckVerResModel(Parcel in) {
        status = in.readString();
        byte tmpError = in.readByte();
        error = tmpError == 0 ? null : tmpError == 1;
        versionId = in.readString();
        olderVersion = in.readString();
        newVersion = in.readString();
        priorty = in.readString();
        message = in.readString();
        feature = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error == null ? 0 : error ? 1 : 2));
        dest.writeString(versionId);
        dest.writeString(olderVersion);
        dest.writeString(newVersion);
        dest.writeString(priorty);
        dest.writeString(message);
        dest.writeString(feature);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckVerResModel> CREATOR = new Creator<CheckVerResModel>() {
        @Override
        public CheckVerResModel createFromParcel(Parcel in) {
            return new CheckVerResModel(in);
        }

        @Override
        public CheckVerResModel[] newArray(int size) {
            return new CheckVerResModel[size];
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

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getOlderVersion() {
        return olderVersion;
    }

    public void setOlderVersion(String olderVersion) {
        this.olderVersion = olderVersion;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getPriorty() {
        return priorty;
    }

    public void setPriorty(String priorty) {
        this.priorty = priorty;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}
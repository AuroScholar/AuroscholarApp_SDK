package com.auro.application.core.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationDataModel implements Parcelable {

    @SerializedName("message")
    @Expose
    private String message = "";
    @SerializedName("title")
    @Expose
    private String title = "";
    @SerializedName("isBackground")
    @Expose
    private Boolean isBackground;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl = "";
    @SerializedName("timestamp")
    @Expose
    private String timestamp = "";
    @SerializedName("navigateto")
    @Expose
    private String navigateto = "";

    public NotificationDataModel() {

    }

    protected NotificationDataModel(Parcel in) {
        message = in.readString();
        title = in.readString();
        byte tmpIsBackground = in.readByte();
        isBackground = tmpIsBackground == 0 ? null : tmpIsBackground == 1;
        imageUrl = in.readString();
        timestamp = in.readString();
        navigateto = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(title);
        dest.writeByte((byte) (isBackground == null ? 0 : isBackground ? 1 : 2));
        dest.writeString(imageUrl);
        dest.writeString(timestamp);
        dest.writeString(navigateto);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationDataModel> CREATOR = new Creator<NotificationDataModel>() {
        @Override
        public NotificationDataModel createFromParcel(Parcel in) {
            return new NotificationDataModel(in);
        }

        @Override
        public NotificationDataModel[] newArray(int size) {
            return new NotificationDataModel[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsBackground() {
        return isBackground;
    }

    public void setIsBackground(Boolean isBackground) {
        this.isBackground = isBackground;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNavigateto() {
        return navigateto;
    }

    public void setNavigateto(String navigateto) {
        this.navigateto = navigateto;
    }

}
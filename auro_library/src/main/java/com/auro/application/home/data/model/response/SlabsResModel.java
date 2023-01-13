package com.auro.application.home.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SlabsResModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("winning_quizes")
    @Expose
    private Integer winningQuizes;
    @SerializedName("slabs")
    @Expose
    private List<SlabModel> slabs = null;
    @SerializedName("response_code")
    @Expose
    private Integer responseCode;

  public   SlabsResModel() {

    }

    protected SlabsResModel(Parcel in) {
        status = in.readString();
        byte tmpError = in.readByte();
        error = tmpError == 0 ? null : tmpError == 1;
        message = in.readString();
        if (in.readByte() == 0) {
            winningQuizes = null;
        } else {
            winningQuizes = in.readInt();
        }
        if (in.readByte() == 0) {
            responseCode = null;
        } else {
            responseCode = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error == null ? 0 : error ? 1 : 2));
        dest.writeString(message);
        if (winningQuizes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(winningQuizes);
        }
        if (responseCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(responseCode);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SlabsResModel> CREATOR = new Creator<SlabsResModel>() {
        @Override
        public SlabsResModel createFromParcel(Parcel in) {
            return new SlabsResModel(in);
        }

        @Override
        public SlabsResModel[] newArray(int size) {
            return new SlabsResModel[size];
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

    public Integer getWinningQuizes() {
        return winningQuizes;
    }

    public void setWinningQuizes(Integer winningQuizes) {
        this.winningQuizes = winningQuizes;
    }

    public List<SlabModel> getSlabs() {
        return slabs;
    }

    public void setSlabs(List<SlabModel> slabs) {
        this.slabs = slabs;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }
}


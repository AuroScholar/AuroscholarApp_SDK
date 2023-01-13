package com.auro.application.home.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SlabModel implements Parcelable {


    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("slab_name")
    @Expose
    private String slabName;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("quiz_log")
    @Expose
    private String quizLog;

    @SerializedName("quiz_count")
    @Expose
    private Integer quizCount;






    @SerializedName("details")
    @Expose
    private List<SlabDetailResModel> details = null;

    @SerializedName("quizOpen")
    @Expose
    private boolean quizOpen;

    protected SlabModel(Parcel in) {
        if (in.readByte() == 0) {
            level = null;
        } else {
            level = in.readInt();
        }
        slabName = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        quizLog = in.readString();
        quizOpen = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (level == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(level);
        }
        dest.writeString(slabName);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(price);
        }
        dest.writeString(quizLog);
        dest.writeByte((byte) (quizOpen ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SlabModel> CREATOR = new Creator<SlabModel>() {
        @Override
        public SlabModel createFromParcel(Parcel in) {
            return new SlabModel(in);
        }

        @Override
        public SlabModel[] newArray(int size) {
            return new SlabModel[size];
        }
    };

    public boolean isQuizOpen() {
        return quizOpen;
    }

    public void setQuizOpen(boolean quizOpen) {
        this.quizOpen = quizOpen;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSlabName() {
        return slabName;
    }

    public void setSlabName(String slabName) {
        this.slabName = slabName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getQuizLog() {
        return quizLog;
    }

    public void setQuizLog(String quizLog) {
        this.quizLog = quizLog;
    }

    public List<SlabDetailResModel> getDetails() {
        return details;
    }

    public void setDetails(List<SlabDetailResModel> details) {
        this.details = details;
    }

}

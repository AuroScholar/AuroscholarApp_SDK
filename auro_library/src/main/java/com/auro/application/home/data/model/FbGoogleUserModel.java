package com.auro.application.home.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FbGoogleUserModel implements Parcelable {

    public String userName;
    public String userEmail;
    public String profilePic;
    public String gender;



    public static final Parcelable.Creator<FbGoogleUserModel> CREATOR = new Parcelable.Creator<FbGoogleUserModel>() {

        @Override
        public FbGoogleUserModel createFromParcel(Parcel parcel) {
            return new FbGoogleUserModel(parcel);
        }

        @Override
        public FbGoogleUserModel[] newArray(int size) {
            return new FbGoogleUserModel[size];
        }
    };

    public FbGoogleUserModel() {

    }

    private FbGoogleUserModel(Parcel parcel) {
        userName = parcel.readString();
        userEmail = parcel.readString();
        profilePic = parcel.readString();
        gender = parcel.readString();



    }


    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(userName);
        parcel.writeString(userEmail);
        parcel.writeString(profilePic);
        parcel.writeString(gender);


    }

    @Override
    public int describeContents() {
        return 0;
    }
}

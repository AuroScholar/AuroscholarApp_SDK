package com.auro.application.home.data.model.signupmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.auro.application.home.data.model.FaceImgItemResModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddStudentStepDataModel  {

	String description;
	boolean status;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
package com.auro.application.home.data.model.response;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DynamiclinkResModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("refferal_mobileno")
    @Expose
    private String refferalMobileno = "";
    @SerializedName("reffer_mobileno")
    @Expose
    private String refferMobileno = "";
    @SerializedName("source")
    @Expose
    private String source = "";
    @SerializedName("navigation_to")
    @Expose
    private String navigationTo = "";
    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("reffer_type")
    @Expose
    private String reffer_type="";

    @SerializedName("open_subject")
    @Expose
    private String openSubject="";

    @SerializedName("is_quiz_open")
    @Expose
    private boolean isQuizOpen;


    @SerializedName("student_class")
    @Expose
    private String studentClass;


    @SerializedName("reffer_user_id")
    @Expose
    private String reffeUserId;

    public  DynamiclinkResModel() {

    }



    protected DynamiclinkResModel(Parcel in) {
        status = in.readString();
        byte tmpError = in.readByte();
        error = tmpError == 0 ? null : tmpError == 1;
        refferalMobileno = in.readString();
        refferMobileno = in.readString();
        source = in.readString();
        navigationTo = in.readString();
        link = in.readString();
        reffer_type = in.readString();
        openSubject = in.readString();
        isQuizOpen = in.readByte() != 0;
        studentClass = in.readString();
        reffeUserId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error == null ? 0 : error ? 1 : 2));
        dest.writeString(refferalMobileno);
        dest.writeString(refferMobileno);
        dest.writeString(source);
        dest.writeString(navigationTo);
        dest.writeString(link);
        dest.writeString(reffer_type);
        dest.writeString(openSubject);
        dest.writeByte((byte) (isQuizOpen ? 1 : 0));
        dest.writeString(studentClass);
        dest.writeString(reffeUserId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DynamiclinkResModel> CREATOR = new Creator<DynamiclinkResModel>() {
        @Override
        public DynamiclinkResModel createFromParcel(Parcel in) {
            return new DynamiclinkResModel(in);
        }

        @Override
        public DynamiclinkResModel[] newArray(int size) {
            return new DynamiclinkResModel[size];
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

    public String getRefferalMobileno() {
        return refferalMobileno;
    }

    public void setRefferalMobileno(String refferalMobileno) {
        this.refferalMobileno = refferalMobileno;
    }

    public String getRefferMobileno() {
        return refferMobileno;
    }

    public void setRefferMobileno(String refferMobileno) {
        this.refferMobileno = refferMobileno;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNavigationTo() {
        return navigationTo;
    }

    public void setNavigationTo(String navigationTo) {
        this.navigationTo = navigationTo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getReffer_type() {
        return reffer_type;
    }

    public void setReffer_type(String reffer_type) {
        this.reffer_type = reffer_type;
    }

    public String getOpenSubject() {
        return openSubject;
    }

    public void setOpenSubject(String openSubject) {
        this.openSubject = openSubject;
    }


    public boolean isQuizOpen() {
        return isQuizOpen;
    }

    public void setQuizOpen(boolean quizOpen) {
        isQuizOpen = quizOpen;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getReffeUserId() {
        return reffeUserId;
    }

    public void setReffeUserId(String reffeUserId) {
        this.reffeUserId = reffeUserId;
    }
}
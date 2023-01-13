package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KycItemDataResModel   implements  Parcelable {

    @SerializedName("document_type_id")
    @Expose
    private Integer documentTypeId;
    @SerializedName("document_path")
    @Expose
    private String documentPath;
    @SerializedName("document_side")
    @Expose
    private String documentSide;
    @SerializedName("document_verified")
    @Expose
    private String documentVerified;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("doc_remarks")
    @Expose
    private String docRemarks;
    @SerializedName("verified_date")
    @Expose
    private String verifiedDate;
    @SerializedName("doc_status")
    @Expose
    private String docStatus;

    protected KycItemDataResModel(Parcel in) {
        if (in.readByte() == 0) {
            documentTypeId = null;
        } else {
            documentTypeId = in.readInt();
        }
        documentPath = in.readString();
        documentSide = in.readString();
        documentVerified = in.readString();
        createdAt = in.readString();
        docRemarks = in.readString();
        verifiedDate = in.readString();
        docStatus = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (documentTypeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(documentTypeId);
        }
        dest.writeString(documentPath);
        dest.writeString(documentSide);
        dest.writeString(documentVerified);
        dest.writeString(createdAt);
        dest.writeString(docRemarks);
        dest.writeString(verifiedDate);
        dest.writeString(docStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KycItemDataResModel> CREATOR = new Creator<KycItemDataResModel>() {
        @Override
        public KycItemDataResModel createFromParcel(Parcel in) {
            return new KycItemDataResModel(in);
        }

        @Override
        public KycItemDataResModel[] newArray(int size) {
            return new KycItemDataResModel[size];
        }
    };

    public Integer getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Integer documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getDocumentSide() {
        return documentSide;
    }

    public void setDocumentSide(String documentSide) {
        this.documentSide = documentSide;
    }

    public String getDocumentVerified() {
        return documentVerified;
    }

    public void setDocumentVerified(String documentVerified) {
        this.documentVerified = documentVerified;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDocRemarks() {
        return docRemarks;
    }

    public void setDocRemarks(String docRemarks) {
        this.docRemarks = docRemarks;
    }

    public String getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(String verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public String getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }
}

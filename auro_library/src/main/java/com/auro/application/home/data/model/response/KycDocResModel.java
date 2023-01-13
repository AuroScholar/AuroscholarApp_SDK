package com.auro.application.home.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KycDocResModel {

    @SerializedName("document_verified")
    @Expose
    private String documentVerified;
    @SerializedName("doc_remarks")
    @Expose
    private String docRemarks;
    @SerializedName("verified_date")
    @Expose
    private String verifiedDate;
    @SerializedName("doc_status")
    @Expose
    private String docStatus;
    @SerializedName("document_type_id")
    @Expose
    private String documentTypeId;
    @SerializedName("document_side")
    @Expose
    private String documentSide;
    @SerializedName("document_path")
    @Expose
    private String documentPath;

    public String getDocumentVerified() {
        return documentVerified;
    }

    public void setDocumentVerified(String documentVerified) {
        this.documentVerified = documentVerified;
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

    public String getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getDocumentSide() {
        return documentSide;
    }

    public void setDocumentSide(String documentSide) {
        this.documentSide = documentSide;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }
}

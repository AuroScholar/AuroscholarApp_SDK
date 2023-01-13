package com.auro.application.home.data.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.auro.application.home.data.model.response.KycDocResModel;
import com.auro.application.teacher.data.model.response.KycItemDataResModel;
import com.auro.application.teacher.data.model.response.TeacherKycStatusResModel;

import java.lang.reflect.ParameterizedType;

public class KYCDocumentDatamodel implements Parcelable {


    int documentId;
    Uri documentURi;
    boolean documentstatus;
    boolean progress;
    boolean backgroundStatus;
    boolean modify;
    String buttonText;
    String documentName;
    String documentDesc;
    String documentFileName;
    String id_name;
    String documentUrl;
    byte[] imageBytes;
    int viewType;
    KycItemDataResModel kycItemDataResModel;
    KycDocResModel studentKycDocResModel;
    int position;

    protected KYCDocumentDatamodel(Parcel in) {
        documentId = in.readInt();
        documentURi = in.readParcelable(Uri.class.getClassLoader());
        documentstatus = in.readByte() != 0;
        progress = in.readByte() != 0;
        backgroundStatus = in.readByte() != 0;
        modify = in.readByte() != 0;
        buttonText = in.readString();
        documentName = in.readString();
        documentDesc = in.readString();
        documentFileName = in.readString();
        id_name = in.readString();
        documentUrl = in.readString();
        imageBytes = in.createByteArray();
        viewType = in.readInt();
        kycItemDataResModel = in.readParcelable(KycItemDataResModel.class.getClassLoader());
        position = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(documentId);
        dest.writeParcelable(documentURi, flags);
        dest.writeByte((byte) (documentstatus ? 1 : 0));
        dest.writeByte((byte) (progress ? 1 : 0));
        dest.writeByte((byte) (backgroundStatus ? 1 : 0));
        dest.writeByte((byte) (modify ? 1 : 0));
        dest.writeString(buttonText);
        dest.writeString(documentName);
        dest.writeString(documentDesc);
        dest.writeString(documentFileName);
        dest.writeString(id_name);
        dest.writeString(documentUrl);
        dest.writeByteArray(imageBytes);
        dest.writeInt(viewType);
        dest.writeParcelable(kycItemDataResModel, flags);
        dest.writeInt(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KYCDocumentDatamodel> CREATOR = new Creator<KYCDocumentDatamodel>() {
        @Override
        public KYCDocumentDatamodel createFromParcel(Parcel in) {
            return new KYCDocumentDatamodel(in);
        }

        @Override
        public KYCDocumentDatamodel[] newArray(int size) {
            return new KYCDocumentDatamodel[size];
        }
    };

    public KycDocResModel getStudentKycDocResModel() {
        return studentKycDocResModel;
    }

    public void setStudentKycDocResModel(KycDocResModel studentKycDocResModel) {
        this.studentKycDocResModel = studentKycDocResModel;
    }

    public KycItemDataResModel getKycItemDataResModel() {
        return kycItemDataResModel;
    }

    public void setKycItemDataResModel(KycItemDataResModel kycItemDataResModel) {
        this.kycItemDataResModel = kycItemDataResModel;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public KycItemDataResModel getTeacherKycStatusResModel() {
        return kycItemDataResModel;
    }

    public void setTeacherKycStatusResModel(KycItemDataResModel kycItemDataResModel) {
        this.kycItemDataResModel = kycItemDataResModel;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public boolean isBackgroundStatus() {
        return backgroundStatus;
    }

    public void setBackgroundStatus(boolean backgroundStatus) {
        this.backgroundStatus = backgroundStatus;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }


    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public boolean isProgress() {
        return progress;
    }

    public void setProgress(boolean progress) {
        this.progress = progress;
    }

    public String getDocumentFileName() {
        return documentFileName;
    }

    public void setDocumentFileName(String documentFileName) {
        this.documentFileName = documentFileName;
    }

    public Uri getDocumentURi() {
        return documentURi;
    }

    public void setDocumentURi(Uri documentURi) {
        this.documentURi = documentURi;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public boolean isDocumentstatus() {
        return documentstatus;
    }

    public void setDocumentstatus(boolean documentstatus) {
        this.documentstatus = documentstatus;
    }

    public KYCDocumentDatamodel() {
    }

    public String getDocumentDesc() {
        return documentDesc;
    }

    public void setDocumentDesc(String documentDesc) {
        this.documentDesc = documentDesc;
    }
}

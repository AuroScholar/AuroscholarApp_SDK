package com.auro.application.home.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.auro.application.home.data.model.response.CheckVerResModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashboardResModel implements Parcelable {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("response_code")
    @Expose
    private String responseCode;
    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;



    @SerializedName("studentclass")
    @Expose
    private String studentclass;
    @SerializedName("registration_id")
    @Expose
    private String registrationId;
    @SerializedName("auroid")
    @Expose
    private String auroid;
    @SerializedName("registrationdate")
    @Expose
    private String registrationdate;
    @SerializedName("partner_source")
    @Expose
    private String partnerSource;
    @SerializedName("partner_logo")
    @Expose
    private String partnerLogo;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("idfront")
    @Expose
    private String idfront;
    @SerializedName("idback")
    @Expose
    private String idback;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("schoolid")
    @Expose
    private String schoolid;


    @SerializedName("private_tution_type")
    @Expose
    private String privateTutionType;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("email_verified")
    @Expose
    private String emailVerified;

    @SerializedName("user_type_id")
    @Expose
    private String userTypeId;

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("school_name")
    @Expose
    private String schoolName;


    @SerializedName("student_demographic_id")
    @Expose
    private String studentDemographicId;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("aadhar_name")
    @Expose
    private String aadharName;
    @SerializedName("last_4_aadhaar_no")
    @Expose
    private String last4AadhaarNo;
    @SerializedName("medium_of_instruction")
    @Expose
    private String mediumOfInstruction;
    @SerializedName("is_private_tution")
    @Expose
    private String isPrivateTution;
    @SerializedName("tution_mode")
    @Expose
    private String tutionMode;
    @SerializedName("mobile_version")
    @Expose
    private String mobileVersion;
    @SerializedName("mobile_type")
    @Expose
    private String mobileType;
    @SerializedName("mobile_model")
    @Expose
    private String mobileModel;
    @SerializedName("device_os_version")
    @Expose
    private String deviceOsVersion;
    @SerializedName("mobile_manufacturer")
    @Expose
    private String mobileManufacturer;
    @SerializedName("build_version")
    @Expose
    private String buildVersion;

    @SerializedName("ip_address")
    @Expose
    private String ipAddress;



    @SerializedName("feature")
    @Expose
    private String feature;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("walletbalance")
    @Expose
    private String walletbalance;

    @SerializedName("disburse_scholarship_money")
    @Expose
    private String disburseScholarshipMoney;
    @SerializedName("currency")
    @Expose
    private String currency;


    @SerializedName("scholarid")
    @Expose
    private String scholarid;

    @SerializedName("student_id")
    @Expose
    private String student_id;

    @SerializedName("student_name")
    @Expose
    private String student_name;

    @SerializedName("email_id")
    @Expose
    private String email_id;

    @SerializedName("regitration_source")
    @Expose
    private String regitration_source;

    @SerializedName("campaign")
    @Expose
    private String campaign;

    @SerializedName("school_type")
    @Expose
    private String school_type;

    @SerializedName("board_type")
    @Expose
    private String board_type;


    @SerializedName("SubjectName")
    @Expose
    private String subjectName;


    @SerializedName("quiz")
    @Expose
    private List<SubjectResModel> subjectResModelList = null;

    @SerializedName("modify")
    @Expose
    private boolean modify;


    @SerializedName("is_kyc_uploaded")
    @Expose
    private String is_kyc_uploaded;

    @SerializedName("is_kyc_verified")
    @Expose
    private String is_kyc_verified;

    @SerializedName("is_payment_lastmonth")
    @Expose
    private String is_payment_lastmonth;

    @SerializedName("is_block")
    @Expose
    private boolean is_block;


    @SerializedName("upgrade")
    @Expose
    private UpgradeResModel upgradeResModel;

    @SerializedName("unapproved_scholarship_money")
    @Expose
    private String unapproved_scholarship_money;

    @SerializedName("disapproved_scholarship_money")
    @Expose
    private String disapproved_scholarship_money;

    @SerializedName("approved_scholarship_money")
    @Expose
    private String approved_scholarship_money;


    @SerializedName("inprocess_scholarship_money")
    @Expose
    private String inProcessScholarShipMoney;


    @SerializedName("lead_qualified")
    @Expose
    String leadQualified;

    @SerializedName("classes")
    @Expose
    private List<String> classes;


    @SerializedName("is_native_image_capturing")
    @Expose
    private boolean is_native_image_capturing;

    @SerializedName("app_version_detials")
    @Expose
    CheckVerResModel checkVerResModel;


    @SerializedName("is_chatbot_enabled")
    @Expose
    private  boolean isChatBotEnabled;


    protected DashboardResModel(Parcel in) {
        status = in.readString();
        error = in.readByte() != 0;
        responseCode = in.readString();
        phonenumber = in.readString();
        studentclass = in.readString();
        registrationId = in.readString();
        auroid = in.readString();
        registrationdate = in.readString();
        partnerSource = in.readString();
        partnerLogo = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        deviceToken = in.readString();
        idfront = in.readString();
        idback = in.readString();
        photo = in.readString();
        profilePic = in.readString();
        schoolid = in.readString();
        privateTutionType = in.readString();
        language = in.readString();
        month = in.readString();
        emailVerified = in.readString();
        userTypeId = in.readString();
        userId = in.readString();
        schoolName = in.readString();
        studentDemographicId = in.readString();
        stateId = in.readString();
        districtId = in.readString();
        address = in.readString();
        pincode = in.readString();
        fatherName = in.readString();
        gender = in.readString();
        dob = in.readString();
        aadharName = in.readString();
        last4AadhaarNo = in.readString();
        mediumOfInstruction = in.readString();
        isPrivateTution = in.readString();
        tutionMode = in.readString();
        mobileVersion = in.readString();
        mobileType = in.readString();
        mobileModel = in.readString();
        deviceOsVersion = in.readString();
        mobileManufacturer = in.readString();
        buildVersion = in.readString();
        ipAddress = in.readString();
        feature = in.readString();
        message = in.readString();
        walletbalance = in.readString();
        disburseScholarshipMoney = in.readString();
        currency = in.readString();
        scholarid = in.readString();
        student_id = in.readString();
        student_name = in.readString();
        email_id = in.readString();
        regitration_source = in.readString();
        campaign = in.readString();
        school_type = in.readString();
        board_type = in.readString();
        subjectName = in.readString();
        subjectResModelList = in.createTypedArrayList(SubjectResModel.CREATOR);
        modify = in.readByte() != 0;
        is_kyc_uploaded = in.readString();
        is_kyc_verified = in.readString();
        is_payment_lastmonth = in.readString();
        is_block = in.readByte() != 0;
        upgradeResModel = in.readParcelable(UpgradeResModel.class.getClassLoader());
        unapproved_scholarship_money = in.readString();
        disapproved_scholarship_money = in.readString();
        approved_scholarship_money = in.readString();
        inProcessScholarShipMoney = in.readString();
        leadQualified = in.readString();
        classes = in.createStringArrayList();
        is_native_image_capturing = in.readByte() != 0;
        checkVerResModel = in.readParcelable(CheckVerResModel.class.getClassLoader());
        isChatBotEnabled = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeString(responseCode);
        dest.writeString(phonenumber);
        dest.writeString(studentclass);
        dest.writeString(registrationId);
        dest.writeString(auroid);
        dest.writeString(registrationdate);
        dest.writeString(partnerSource);
        dest.writeString(partnerLogo);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(deviceToken);
        dest.writeString(idfront);
        dest.writeString(idback);
        dest.writeString(photo);
        dest.writeString(profilePic);
        dest.writeString(schoolid);
        dest.writeString(privateTutionType);
        dest.writeString(language);
        dest.writeString(month);
        dest.writeString(emailVerified);
        dest.writeString(userTypeId);
        dest.writeString(userId);
        dest.writeString(schoolName);
        dest.writeString(studentDemographicId);
        dest.writeString(stateId);
        dest.writeString(districtId);
        dest.writeString(address);
        dest.writeString(pincode);
        dest.writeString(fatherName);
        dest.writeString(gender);
        dest.writeString(dob);
        dest.writeString(aadharName);
        dest.writeString(last4AadhaarNo);
        dest.writeString(mediumOfInstruction);
        dest.writeString(isPrivateTution);
        dest.writeString(tutionMode);
        dest.writeString(mobileVersion);
        dest.writeString(mobileType);
        dest.writeString(mobileModel);
        dest.writeString(deviceOsVersion);
        dest.writeString(mobileManufacturer);
        dest.writeString(buildVersion);
        dest.writeString(ipAddress);
        dest.writeString(feature);
        dest.writeString(message);
        dest.writeString(walletbalance);
        dest.writeString(disburseScholarshipMoney);
        dest.writeString(currency);
        dest.writeString(scholarid);
        dest.writeString(student_id);
        dest.writeString(student_name);
        dest.writeString(email_id);
        dest.writeString(regitration_source);
        dest.writeString(campaign);
        dest.writeString(school_type);
        dest.writeString(board_type);
        dest.writeString(subjectName);
        dest.writeTypedList(subjectResModelList);
        dest.writeByte((byte) (modify ? 1 : 0));
        dest.writeString(is_kyc_uploaded);
        dest.writeString(is_kyc_verified);
        dest.writeString(is_payment_lastmonth);
        dest.writeByte((byte) (is_block ? 1 : 0));
        dest.writeParcelable(upgradeResModel, flags);
        dest.writeString(unapproved_scholarship_money);
        dest.writeString(disapproved_scholarship_money);
        dest.writeString(approved_scholarship_money);
        dest.writeString(inProcessScholarShipMoney);
        dest.writeString(leadQualified);
        dest.writeStringList(classes);
        dest.writeByte((byte) (is_native_image_capturing ? 1 : 0));
        dest.writeParcelable(checkVerResModel, flags);
        dest.writeByte((byte) (isChatBotEnabled ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DashboardResModel> CREATOR = new Creator<DashboardResModel>() {
        @Override
        public DashboardResModel createFromParcel(Parcel in) {
            return new DashboardResModel(in);
        }

        @Override
        public DashboardResModel[] newArray(int size) {
            return new DashboardResModel[size];
        }
    };

    public String getPartnerLogo() {
        return partnerLogo;
    }

    public void setPartnerLogo(String partnerLogo) {
        this.partnerLogo = partnerLogo;
    }

    public String getPartnerSource() {
        return partnerSource;
    }

    public void setPartnerSource(String partnerSource) {
        this.partnerSource = partnerSource;
    }

    public String getInProcessScholarShipMoney() {
        return inProcessScholarShipMoney;
    }

    public void setInProcessScholarShipMoney(String inProcessScholarShipMoney) {
        this.inProcessScholarShipMoney = inProcessScholarShipMoney;
    }

    public String getDisburseScholarshipMoney() {
        return disburseScholarshipMoney;
    }

    public void setDisburseScholarshipMoney(String disburseScholarshipMoney) {
        this.disburseScholarshipMoney = disburseScholarshipMoney;
    }



    public CheckVerResModel getCheckVerResModel() {
        return checkVerResModel;
    }

    public void setCheckVerResModel(CheckVerResModel checkVerResModel) {
        this.checkVerResModel = checkVerResModel;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getLeadQualified() {
        return leadQualified;
    }

    public void setLeadQualified(String leadQualified) {
        this.leadQualified = leadQualified;
    }

    public String getMobileModel() {
        return mobileModel;
    }

    public void setMobileModel(String mobileModel) {
        this.mobileModel = mobileModel;
    }



    public String getMobileVersion() {
        return mobileVersion;
    }

    public void setMobileVersion(String mobileVersion) {
        this.mobileVersion = mobileVersion;
    }

    public String getIsPrivateTution() {
        return isPrivateTution;
    }

    public void setIsPrivateTution(String isPrivateTution) {
        this.isPrivateTution = isPrivateTution;
    }

    public String getPrivateTutionType() {
        return privateTutionType;
    }

    public void setPrivateTutionType(String privateTutionType) {
        this.privateTutionType = privateTutionType;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getIs_kyc_uploaded() {
        return is_kyc_uploaded;
    }

    public void setIs_kyc_uploaded(String is_kyc_uploaded) {
        this.is_kyc_uploaded = is_kyc_uploaded;
    }

    public String getIs_kyc_verified() {
        return is_kyc_verified;
    }

    public void setIs_kyc_verified(String is_kyc_verified) {
        this.is_kyc_verified = is_kyc_verified;
    }

    public String getIs_payment_lastmonth() {
        return is_payment_lastmonth;
    }

    public void setIs_payment_lastmonth(String is_payment_lastmonth) {
        this.is_payment_lastmonth = is_payment_lastmonth;
    }

    public String getAuroid() {
        return auroid;
    }

    public void setAuroid(String auroid) {
        this.auroid = auroid;
    }

    public String getScholarid() {
        return scholarid;
    }

    public void setScholarid(String scholarid) {
        this.scholarid = scholarid;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getStudentclass() {
        return studentclass;
    }

    public void setStudentclass(String studentclass) {
        this.studentclass = studentclass;
    }

    public String getRegistrationdate() {
        return registrationdate;
    }

    public void setRegistrationdate(String registrationdate) {
        this.registrationdate = registrationdate;
    }

    public String getRegitration_source() {
        return regitration_source;
    }

    public void setRegitration_source(String regitration_source) {
        this.regitration_source = regitration_source;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSchool_type() {
        return school_type;
    }

    public void setSchool_type(String school_type) {
        this.school_type = school_type;
    }

    public String getBoard_type() {
        return board_type;
    }

    public void setBoard_type(String board_type) {
        this.board_type = board_type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getWalletbalance() {
        return walletbalance;
    }

    public void setWalletbalance(String walletbalance) {
        this.walletbalance = walletbalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIdfront() {
        return idfront;
    }

    public void setIdfront(String idfront) {
        this.idfront = idfront;
    }

    public String getIdback() {
        return idback;
    }

    public void setIdback(String idback) {
        this.idback = idback;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public List<SubjectResModel> getSubjectResModelList() {
        return subjectResModelList;
    }

    public void setSubjectResModelList(List<SubjectResModel> subjectResModelList) {
        this.subjectResModelList = subjectResModelList;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UpgradeResModel getUpgradeResModel() {
        return upgradeResModel;
    }

    public void setUpgradeResModel(UpgradeResModel upgradeResModel) {
        this.upgradeResModel = upgradeResModel;
    }

    public boolean isIs_block() {
        return is_block;
    }

    public void setIs_block(boolean is_block) {
        this.is_block = is_block;
    }

    public String getApproved_scholarship_money() {
        return approved_scholarship_money;
    }

    public void setApproved_scholarship_money(String approved_scholarship_money) {
        this.approved_scholarship_money = approved_scholarship_money;
    }

    public String getDisapproved_scholarship_money() {
        return disapproved_scholarship_money;
    }

    public void setDisapproved_scholarship_money(String disapproved_scholarship_money) {
        this.disapproved_scholarship_money = disapproved_scholarship_money;
    }

    public String getUnapproved_scholarship_money() {
        return unapproved_scholarship_money;
    }

    public void setUnapproved_scholarship_money(String unapproved_scholarship_money) {
        this.unapproved_scholarship_money = unapproved_scholarship_money;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public boolean isIs_native_image_capturing() {
        return is_native_image_capturing;
    }

    public void setIs_native_image_capturing(boolean is_native_image_capturing) {
        this.is_native_image_capturing = is_native_image_capturing;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isChatBotEnabled() {
        return isChatBotEnabled;
    }

    public void setChatBotEnabled(boolean chatBotEnabled) {
        isChatBotEnabled = chatBotEnabled;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
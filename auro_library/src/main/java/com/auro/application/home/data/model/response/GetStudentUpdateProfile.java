package com.auro.application.home.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetStudentUpdateProfile  {

    @SerializedName("status")
    @Expose
    private String status="";
    @SerializedName("error")
    @Expose
    private boolean error=false;
    @SerializedName("response_code")
    @Expose
    private String responseCode="";
    @SerializedName("phonenumber")
    @Expose
    private String phonenumber="";
    @SerializedName("student_id")
    @Expose
    private String studentId="";
    @SerializedName("student_name")
    @Expose
    private String studentName="";
    @SerializedName("email_id")
    @Expose
    private String emailId="";
    @SerializedName("studentclass")
    @Expose
    private String studentclass="";
    @SerializedName("registration_id")
    @Expose
    private String registrationId="";
    @SerializedName("auroid")
    @Expose
    private String auroid="";
    @SerializedName("registrationdate")
    @Expose
    private String registrationdate="";
    @SerializedName("partner_source")
    @Expose
    private String partnerSource="";
    @SerializedName("partner_logo")
    @Expose
    private String partnerLogo="";
    @SerializedName("latitude")
    @Expose
    private String latitude="";
    @SerializedName("longitude")
    @Expose
    private String longitude="";
    @SerializedName("device_token")
    @Expose
    private String deviceToken="";
    @SerializedName("idfront")
    @Expose
    private String idfront="";
    @SerializedName("idback")
    @Expose
    private String idback="";
    @SerializedName("photo")
    @Expose
    private String photo="";
    @SerializedName("profile_pic")
    @Expose
    private String profilePic="";
    @SerializedName("schoolid")
    @Expose
    private String schoolid="";
    @SerializedName("school_type")
    @Expose
    private String schoolType="";
    @SerializedName("board_type")
    @Expose
    private String boardType="";
    @SerializedName("private_tution_type")
    @Expose
    private String privateTutionType="";
    @SerializedName("language")
    @Expose
    private String language="";
    @SerializedName("month")
    @Expose
    private String month="";
    @SerializedName("email_verified")
    @Expose
    private String emailVerified="";
    @SerializedName("user_type_id")
    @Expose
    private Integer userTypeId;
    @SerializedName("regitration_source")
    @Expose
    private String regitrationSource="";
    @SerializedName("user_id")
    @Expose
    private String userId="";
    @SerializedName("school_name")
    @Expose
    private String schoolName="";
    @SerializedName("is_kyc_verified")
    @Expose
    private String isKycVerified="";
    @SerializedName("is_kyc_uploaded")
    @Expose
    private String isKycUploaded="";
    @SerializedName("student_demographic_id")
    @Expose
    private String studentDemographicId="";
    @SerializedName("state_id")
    @Expose
    private String stateId="";
    @SerializedName("district_id")
    @Expose
    private String districtId="";
    @SerializedName("address")
    @Expose
    private String address="";
    @SerializedName("pincode")
    @Expose
    private String pincode="";
    @SerializedName("father_name")
    @Expose
    private String fatherName="";
    @SerializedName("gender")
    @Expose
    private String gender="";
    @SerializedName("dob")
    @Expose
    private String dob="";
    @SerializedName("aadhar_name")
    @Expose
    private String aadharName="";
    @SerializedName("last_4_aadhaar_no")
    @Expose
    private String last4AadhaarNo="";
    @SerializedName("medium_of_instruction")
    @Expose
    private String mediumOfInstruction="";
    @SerializedName("is_private_tution")
    @Expose
    private String isPrivateTution="";
    @SerializedName("tution_mode")
    @Expose
    private String tutionMode="";
    @SerializedName("mobile_version")
    @Expose
    private String mobileVersion="";
    @SerializedName("mobile_type")
    @Expose
    private String mobileType="";
    @SerializedName("mobile_model")
    @Expose
    private String mobileModel="";
    @SerializedName("device_os_version")
    @Expose
    private String deviceOsVersion="";
    @SerializedName("mobile_manufacturer")
    @Expose
    private String mobileManufacturer="";
    @SerializedName("build_version")
    @Expose
    private String buildVersion="";
    @SerializedName("classes")
    @Expose
    private List<String> classes = new ArrayList<>();
    @SerializedName("ip_address")
    @Expose
    private String ipAddress="";
    @SerializedName("app_version_detials")
    @Expose
    private CheckVerResModel appVersionDetials;
    @SerializedName("is_native_image_capturing")
    @Expose
    private boolean isNativeImageCapturing;
    @SerializedName("is_chatbot_enabled")
    @Expose
    private boolean isChatbotEnabled;
    @SerializedName("feature")
    @Expose
    private String feature="";

    @SerializedName("message")
    @Expose
    private String message="";

    @SerializedName("state_name")
    @Expose
    private String statename="";
    @SerializedName("district_name")
    @Expose
    private String districtname="";

    @SerializedName("translated_school_type")
    @Expose
    private String translated_school_type="";
    @SerializedName("translated_board_type")
    @Expose
    private String translated_board_type="";
    @SerializedName("transalated_private_tution_type")
    @Expose
    private String transalated_private_tution_type="";
    @SerializedName("translated_gender")
    @Expose
    private String translated_gender="";
    @SerializedName("translated_medium_of_instruction")
    @Expose
    private String transalted_medium_of_instruction="";
    @SerializedName("translated_is_private_tution")
    @Expose
    private String translated_is_private_tution="";
    @SerializedName("user_name")
    @Expose
    private String username="";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTranslated_school_type() {
        return translated_school_type;
    }

    public void setTranslated_school_type(String translated_school_type) {
        this.translated_school_type = translated_school_type;
    }

    public String getTranslated_board_type() {
        return translated_board_type;
    }

    public void setTranslated_board_type(String translated_board_type) {
        this.translated_board_type = translated_board_type;
    }

    public String getTransalated_private_tution_type() {
        return transalated_private_tution_type;
    }

    public void setTransalated_private_tution_type(String transalated_private_tution_type) {
        this.transalated_private_tution_type = transalated_private_tution_type;
    }

    public String getTranslated_gender() {
        return translated_gender;
    }

    public void setTranslated_gender(String translated_gender) {
        this.translated_gender = translated_gender;
    }

    public String getTransalted_medium_of_instruction() {
        return transalted_medium_of_instruction;
    }

    public void setTransalted_medium_of_instruction(String transalted_medium_of_instruction) {
        this.transalted_medium_of_instruction = transalted_medium_of_instruction;
    }

    public String getTranslated_is_private_tution() {
        return translated_is_private_tution;
    }

    public void setTranslated_is_private_tution(String translated_is_private_tution) {
        this.translated_is_private_tution = translated_is_private_tution;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    byte[] imageBytes;

    public boolean isError() {
        return error;
    }

    public boolean isNativeImageCapturing() {
        return isNativeImageCapturing;
    }

    public void setNativeImageCapturing(boolean nativeImageCapturing) {
        isNativeImageCapturing = nativeImageCapturing;
    }

    public boolean isChatbotEnabled() {
        return isChatbotEnabled;
    }

    public void setChatbotEnabled(boolean chatbotEnabled) {
        isChatbotEnabled = chatbotEnabled;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getStudentclass() {
        return studentclass;
    }

    public void setStudentclass(String studentclass) {
        this.studentclass = studentclass;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getAuroid() {
        return auroid;
    }

    public void setAuroid(String auroid) {
        this.auroid = auroid;
    }

    public String getRegistrationdate() {
        return registrationdate;
    }

    public void setRegistrationdate(String registrationdate) {
        this.registrationdate = registrationdate;
    }

    public String getPartnerSource() {
        return partnerSource;
    }

    public void setPartnerSource(String partnerSource) {
        this.partnerSource = partnerSource;
    }

    public String getPartnerLogo() {
        return partnerLogo;
    }

    public void setPartnerLogo(String partnerLogo) {
        this.partnerLogo = partnerLogo;
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public String getPrivateTutionType() {
        return privateTutionType;
    }

    public void setPrivateTutionType(String privateTutionType) {
        this.privateTutionType = privateTutionType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getRegitrationSource() {
        return regitrationSource;
    }

    public void setRegitrationSource(String regitrationSource) {
        this.regitrationSource = regitrationSource;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getIsKycVerified() {
        return isKycVerified;
    }

    public void setIsKycVerified(String isKycVerified) {
        this.isKycVerified = isKycVerified;
    }

    public String getIsKycUploaded() {
        return isKycUploaded;
    }

    public void setIsKycUploaded(String isKycUploaded) {
        this.isKycUploaded = isKycUploaded;
    }

    public String getStudentDemographicId() {
        return studentDemographicId;
    }

    public void setStudentDemographicId(String studentDemographicId) {
        this.studentDemographicId = studentDemographicId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAadharName() {
        return aadharName;
    }

    public void setAadharName(String aadharName) {
        this.aadharName = aadharName;
    }

    public String getLast4AadhaarNo() {
        return last4AadhaarNo;
    }

    public void setLast4AadhaarNo(String last4AadhaarNo) {
        this.last4AadhaarNo = last4AadhaarNo;
    }

    public String getMediumOfInstruction() {
        return mediumOfInstruction;
    }

    public void setMediumOfInstruction(String mediumOfInstruction) {
        this.mediumOfInstruction = mediumOfInstruction;
    }

    public String getIsPrivateTution() {
        return isPrivateTution;
    }

    public void setIsPrivateTution(String isPrivateTution) {
        this.isPrivateTution = isPrivateTution;
    }

    public String getTutionMode() {
        return tutionMode;
    }

    public void setTutionMode(String tutionMode) {
        this.tutionMode = tutionMode;
    }

    public String getMobileVersion() {
        return mobileVersion;
    }

    public void setMobileVersion(String mobileVersion) {
        this.mobileVersion = mobileVersion;
    }

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    public String getMobileModel() {
        return mobileModel;
    }

    public void setMobileModel(String mobileModel) {
        this.mobileModel = mobileModel;
    }

    public String getDeviceOsVersion() {
        return deviceOsVersion;
    }

    public void setDeviceOsVersion(String deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    public String getMobileManufacturer() {
        return mobileManufacturer;
    }

    public void setMobileManufacturer(String mobileManufacturer) {
        this.mobileManufacturer = mobileManufacturer;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public CheckVerResModel getAppVersionDetials() {
        return appVersionDetials;
    }

    public void setAppVersionDetials(CheckVerResModel appVersionDetials) {
        this.appVersionDetials = appVersionDetials;
    }

    public boolean getIsNativeImageCapturing() {
        return isNativeImageCapturing;
    }

    public void setIsNativeImageCapturing(boolean isNativeImageCapturing) {
        this.isNativeImageCapturing = isNativeImageCapturing;
    }

    public boolean getIsChatbotEnabled() {
        return isChatbotEnabled;
    }

    public void setIsChatbotEnabled(boolean isChatbotEnabled) {
        this.isChatbotEnabled = isChatbotEnabled;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
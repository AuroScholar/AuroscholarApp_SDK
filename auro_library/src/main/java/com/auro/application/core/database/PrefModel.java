package com.auro.application.core.database;


import com.auro.application.core.common.NotificationDataModel;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.AssignmentResModel;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.FbGoogleUserModel;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.QuizResModel;
import com.auro.application.home.data.model.response.CheckVerResModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.data.model.response.FetchStudentPrefResModel;
import com.auro.application.home.data.model.response.GetAllChildDetailResModel;
import com.auro.application.home.data.model.response.LanguageListResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.util.ConversionUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrefModel implements Serializable {

    private boolean isTour;
    private boolean isLogin;
    private boolean isLog;
    private String userLoginId;
    private String userCountry;
    private String countryNameCode;
    private String countryPhoneCode;

    private String currentLatitude;
    private String currentLongitude;
    private boolean isDashboardaApiNeedToCall;
    private String signupPrefilledData;

    private String emailId;

    private String deviceToken;


    private String updateMobileNum;
    private String updateEmailId;

    // Verify Number Response
    // Verify Login Response
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private Integer expiresIn;
    private String scope;
    private String message;
    private String profileTimestamp;
    private boolean introScreen;
    private int userType;
    private String userMobile;
    private String userName;
    private String statusUserCode;

    private String userKYCProfilePhotoPath;
    private String userLanguageCode;
    private String userLanguageId;
    private  String userLanguageShortCode;

    private DashboardResModel dashboardResModel;

    private AssignmentReqModel assignmentReqModel;
    private boolean tooltipStatus;
    private DynamiclinkResModel dynamiclinkResModel;
    private NotificationDataModel notificationDataModel;
    private String refferalLink;
    private LanguageListResModel languageListResModel;

    private MyProfileResModel teacherProfileResModel;
    private GetAllChildDetailResModel childDetailResModel;
    private UserDetailResModel userDetailResModel;

    public UserDetailResModel getUserDetailResModel() {
        return userDetailResModel;
    }

    public void setUserDetailResModel(UserDetailResModel userDetailResModel) {
        this.userDetailResModel = userDetailResModel;
    }

    public GetAllChildDetailResModel getChildDetailResModel() {
        return childDetailResModel;
    }

    public void setChildDetailResModel(GetAllChildDetailResModel childDetailResModel) {
        this.childDetailResModel = childDetailResModel;
    }

    public MyProfileResModel getTeacherProfileResModel() {
        return teacherProfileResModel;
    }

    public void setTeacherProfileResModel(MyProfileResModel teacherProfileResModel) {
        this.teacherProfileResModel = teacherProfileResModel;
    }

    private QuizResModel quizResModel;

    private boolean preLoginDisclaimer;

    private boolean preQuizDisclaimer;

    private boolean preKycDisclaimer;

    private boolean preMoneyTransferDisclaimer;

    private String currentScreenFlag;

    private String studentName;

    private String srId;

    private boolean forgotPassword;

    private boolean isTeacherProfileScreen;
    private FetchStudentPrefResModel fetchStudentPrefResModel;

    private AssignmentResModel assignmentResModel;

    private String userId;

    CheckVerResModel checkVerResModel;

    CheckUserResModel checkUserResModel;

    LanguageMasterDynamic languageMasterDynamic;

    public LanguageMasterDynamic getLanguageMasterDynamic() {
        return languageMasterDynamic;
    }

    public void setLanguageMasterDynamic(LanguageMasterDynamic languageMasterDynamic) {
        this.languageMasterDynamic = languageMasterDynamic;
    }

    private List<String> studentClasses = new ArrayList<>();

    private List<AssignmentReqModel> listAzureImageList = new ArrayList<>();

    private int versionCode;

    UserDetailResModel studentData;

    UserDetailResModel parentData;

    public UserDetailResModel getStudentData() {
        return studentData;
    }

    public void setStudentData(UserDetailResModel studentData) {
        this.studentData = studentData;
    }

    public UserDetailResModel getParentData() {
        return parentData;
    }

    public void setParentData(UserDetailResModel parentData) {
        this.parentData = parentData;
    }

    public CheckUserResModel getCheckUserResModel() {
        return checkUserResModel;
    }

    public void setCheckUserResModel(CheckUserResModel checkUserResModel) {
        this.checkUserResModel = checkUserResModel;
    }


    public CheckVerResModel getCheckVerResModel() {
        return checkVerResModel;
    }

    public void setCheckVerResModel(CheckVerResModel checkVerResModel) {
        this.checkVerResModel = checkVerResModel;
    }


    public String getUserKYCProfilePhotoPath() {
        return userKYCProfilePhotoPath;
    }

    public void setUserKYCProfilePhotoPath(String userKYCProfilePhotoPath) {
        this.userKYCProfilePhotoPath = userKYCProfilePhotoPath;
    }

    public String getUserLanguageCode() {
        return userLanguageCode;
    }

    public void setUserLanguageCode(String userLanguageCode) {
        this.userLanguageCode = userLanguageCode;
    }

    public DashboardResModel getDashboardResModel() {
        return dashboardResModel;
    }

    public void setDashboardResModel(DashboardResModel dashboardResModel) {
        this.dashboardResModel = dashboardResModel;
    }

    public AssignmentReqModel getAssignmentReqModel() {
        return assignmentReqModel;
    }

    public void setAssignmentReqModel(AssignmentReqModel assignmentReqModel) {
        this.assignmentReqModel = assignmentReqModel;
    }

    public boolean isTooltipStatus() {
        return tooltipStatus;
    }

    public void setTooltipStatus(boolean tooltipStatus) {
        this.tooltipStatus = tooltipStatus;
    }

    public List<AssignmentReqModel> getListAzureImageList() {
        return listAzureImageList;
    }

    public void setListAzureImageList(List<AssignmentReqModel> listAzureImageList) {
        this.listAzureImageList = listAzureImageList;
    }

    public FbGoogleUserModel getFbuserModel() {
        return fbuserModel;
    }

    public void setFbuserModel(FbGoogleUserModel fbuserModel) {
        this.fbuserModel = fbuserModel;
    }

    private FbGoogleUserModel fbuserModel;

    public String getProfileTimestamp() {
        return profileTimestamp;
    }

    public void setProfileTimestamp(String profileTimestamp) {
        this.profileTimestamp = profileTimestamp;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isTour() {
        return isTour;
    }

    public void setTour(boolean tour) {
        isTour = tour;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }


    public boolean isLogin() {
        return isLogin;
    }

    public String getSignupPrefilledData() {
        return signupPrefilledData;
    }

    public void setSignupPrefilledData(String signupPrefilledData) {
        this.signupPrefilledData = signupPrefilledData;
    }

    public boolean isDashboardaApiNeedToCall() {
        return isDashboardaApiNeedToCall;
    }

    public void setDashboardaApiNeedToCall(boolean dashboardaApiNeedToCall) {
        isDashboardaApiNeedToCall = dashboardaApiNeedToCall;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getCountryNameCode() {
        return countryNameCode;
    }

    public void setCountryNameCode(String countryNameCode) {
        this.countryNameCode = countryNameCode;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }


    public String getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(String currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public String getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUpdateMobileNum() {
        return updateMobileNum;
    }

    public void setUpdateMobileNum(String updateMobileNum) {
        this.updateMobileNum = updateMobileNum;
    }

    public String getUpdateEmailId() {
        return updateEmailId;
    }

    public void setUpdateEmailId(String updateEmailId) {
        this.updateEmailId = updateEmailId;
    }

    public boolean isIntroScreen() {
        return introScreen;
    }

    public void setIntroScreen(boolean introScreen) {
        this.introScreen = introScreen;
    }


    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getStudentClass() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel != null && prefModel.getStudentData() != null && !prefModel.getStudentData().getGrade().isEmpty()) {
            return ConversionUtil.INSTANCE.convertStringToInteger(prefModel.getStudentData().getGrade());
        }
        return 0;
    }

  /*  public void setStudentClass(int studentClass) {
        this.studentClass = studentClass;
    }*/

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DynamiclinkResModel getDynamiclinkResModel() {
        return dynamiclinkResModel;
    }

    public void setDynamiclinkResModel(DynamiclinkResModel dynamiclinkResModel) {
        this.dynamiclinkResModel = dynamiclinkResModel;
    }

    public NotificationDataModel getNotificationDataModel() {
        return notificationDataModel;
    }

    public void setNotificationDataModel(NotificationDataModel notificationDataModel) {
        this.notificationDataModel = notificationDataModel;
    }

    public String getRefferalLink() {
        return refferalLink;
    }

    public void setRefferalLink(String refferalLink) {
        this.refferalLink = refferalLink;
    }

    public List<String> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(List<String> studentClasses) {
        this.studentClasses = studentClasses;
    }

    public boolean isLog() {
        return isLog;
    }

    public void setLog(boolean log) {
        isLog = log;
    }

    public QuizResModel getQuizResModel() {
        return quizResModel;
    }

    public void setQuizResModel(QuizResModel quizResModel) {
        this.quizResModel = quizResModel;
    }

    public boolean isPreLoginDisclaimer() {
        return preLoginDisclaimer;
    }

    public void setPreLoginDisclaimer(boolean preLoginDisclaimer) {
        this.preLoginDisclaimer = preLoginDisclaimer;
    }

    public boolean isPreQuizDisclaimer() {
        return preQuizDisclaimer;
    }

    public void setPreQuizDisclaimer(boolean preQuizDisclaimer) {
        this.preQuizDisclaimer = preQuizDisclaimer;
    }

    public boolean isPreMoneyTransferDisclaimer() {
        return preMoneyTransferDisclaimer;
    }

    public void setPreMoneyTransferDisclaimer(boolean preMoneyTransferDisclaimer) {
        this.preMoneyTransferDisclaimer = preMoneyTransferDisclaimer;
    }

    public boolean isPreKycDisclaimer() {
        return preKycDisclaimer;
    }

    public void setPreKycDisclaimer(boolean preKycDisclaimer) {
        this.preKycDisclaimer = preKycDisclaimer;
    }

    public String getStatusUserCode() {
        return statusUserCode;
    }

    public void setStatusUserCode(String statusUserCode) {
        this.statusUserCode = statusUserCode;
    }

    public String getCurrentScreenFlag() {
        return currentScreenFlag;
    }

    public void setCurrentScreenFlag(String currentScreenFlag) {
        this.currentScreenFlag = currentScreenFlag;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSrId() {
        return srId;
    }

    public void setSrId(String srId) {
        this.srId = srId;
    }

    public boolean isForgotPassword() {
        return forgotPassword;
    }

    public void setForgotPassword(boolean forgotPassword) {
        this.forgotPassword = forgotPassword;
    }

    public boolean isTeacherProfileScreen() {
        return isTeacherProfileScreen;
    }

    public void setTeacherProfileScreen(boolean teacherProfileScreen) {
        isTeacherProfileScreen = teacherProfileScreen;
    }

    public FetchStudentPrefResModel getFetchStudentPrefResModel() {
        return fetchStudentPrefResModel;
    }

    public void setFetchStudentPrefResModel(FetchStudentPrefResModel fetchStudentPrefResModel) {
        this.fetchStudentPrefResModel = fetchStudentPrefResModel;
    }

    public AssignmentResModel getAssignmentResModel() {
        return assignmentResModel;
    }

    public void setAssignmentResModel(AssignmentResModel assignmentResModel) {
        this.assignmentResModel = assignmentResModel;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public LanguageListResModel getLanguageListResModel() {
        return languageListResModel;
    }

    public void setLanguageListResModel(LanguageListResModel languageListResModel) {
        this.languageListResModel = languageListResModel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLanguageId() {
        return userLanguageId;
    }

    public void setUserLanguageId(String userLanguageId) {
        this.userLanguageId = userLanguageId;
    }

    public String getUserLanguageShortCode() {
        return userLanguageShortCode;
    }

    public void setUserLanguageShortCode(String userLanguageShortCode) {
        this.userLanguageShortCode = userLanguageShortCode;
    }
}

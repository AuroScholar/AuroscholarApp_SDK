package com.auro.application.home.data.datasource.remote;


import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.auro.application.core.common.AppConstant;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.data.model.KYCInputModel;
import com.auro.application.home.data.model.LanguageMasterReqModel;
import com.auro.application.home.data.model.OtpOverCallReqModel;
import com.auro.application.home.data.model.PartnersLoginReqModel;
import com.auro.application.home.data.model.PendingKycDocsModel;
import com.auro.application.home.data.model.RefferalReqModel;
import com.auro.application.home.data.model.SaveImageReqModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.SetPasswordReqModel;
import com.auro.application.home.data.model.UpdatePrefReqModel;
import com.auro.application.home.data.model.VerifyOtpReqModel;
import com.auro.application.home.data.model.passportmodels.PassportReqModel;
import com.auro.application.home.data.model.response.CertificateResModel;
import com.auro.application.home.data.model.response.CheckUserValidResModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.signupmodel.InstructionModel;
import com.auro.application.home.data.model.signupmodel.UserSlabsRequest;
import com.auro.application.home.data.model.signupmodel.InstructionModel;
import com.auro.application.home.data.model.signupmodel.request.LoginReqModel;
import com.auro.application.home.data.model.signupmodel.request.RegisterApiReqModel;
import com.auro.application.home.data.model.signupmodel.request.SetUsernamePinReqModel;
import com.auro.application.home.data.repository.HomeRepo.DashboardRemoteData;
import com.auro.application.teacher.data.model.request.SendInviteNotificationReqModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.ViewUtil;
import com.auroscholar.final_auroscholarapp_sdk.SDKDataModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class HomeRemoteDataSourceImp implements DashboardRemoteData {
    HomeRemoteApi homeRemoteApi;

    public HomeRemoteDataSourceImp(HomeRemoteApi homeRemoteApi) {
        this.homeRemoteApi = homeRemoteApi;
    }

    @Override
    public Single<Response<JsonObject>> getDashboardData(AuroScholarDataModel model) {
        PrefModel prefModel2 = AuroAppPref.INSTANCE.getModelInstance();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AppConstant.DashBoardParams.USER_ID, prefModel2.getUserId());
        params.put(AppConstant.DashBoardParams.LANGUAGE, prefModel2.getUserLanguageId());
        params.put(AppConstant.DashBoardParams.MODULES, "details,wallet,quizes");
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.REGISTRATION_SOURCE_NEW, AppConstant.ParamsValue.REGISTRATION_SOURCE_VAL);
        params.put(AppConstant.DashBoardParams.PARTNER_SOURCE, AppConstant.ParamsValue.PARTNER_SOURCE_VAL);
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,prefModel2.getUserLanguageId());
        return homeRemoteApi.getDashboardSDKData(params);
    }


    @Override
    public Single<Response<JsonObject>> getAzureData(AssignmentReqModel azureReqModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String partner_unique_id = prefModel.getPartneruniqueid();
        RequestBody registration_id = RequestBody.create(okhttp3.MultipartBody.FORM, azureReqModel.getRegistration_id());
        RequestBody exam_id = RequestBody.create(okhttp3.MultipartBody.FORM, azureReqModel.getEklavvya_exam_id());
        RequestBody exam_name = RequestBody.create(okhttp3.MultipartBody.FORM, azureReqModel.getExam_name());
        RequestBody examAuroId = RequestBody.create(okhttp3.MultipartBody.FORM, azureReqModel.getExamId());
        RequestBody quiz_attempt = RequestBody.create(okhttp3.MultipartBody.FORM, azureReqModel.getQuiz_attempt());
        RequestBody subject = RequestBody.create(okhttp3.MultipartBody.FORM, azureReqModel.getSubject());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), azureReqModel.getImageBytes());
        RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, azureReqModel.getUserId());
        RequestBody langVersion = RequestBody.create(okhttp3.MultipartBody.FORM, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        RequestBody apiVersion = RequestBody.create(okhttp3.MultipartBody.FORM, AppConstant.ParamsValue.API_VERSION_VAL);
        //RequestBody partner_unique_idvalue = RequestBody.create(okhttp3.MultipartBody.FORM, partner_unique_id);

        MultipartBody.Part student_photo = MultipartBody.Part.createFormData("exam_face_img", "image.jpg", requestFile);
        return homeRemoteApi.getAzureApiData(registration_id, exam_id, exam_name, quiz_attempt, subject, examAuroId, userId, langVersion, apiVersion, student_photo);
    }

    @Override
    public Single<Response<JsonObject>> inviteFriendListApi() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(AppConstant.DashBoardParams.USER_ID, AuroAppPref.INSTANCE.getModelInstance().getUserId());
        return homeRemoteApi.inviteFriendListApi(params);
    }

    @Override
    public Single<Response<JsonObject>> sendInviteNotificationApi(SendInviteNotificationReqModel reqModel) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(AppConstant.SendInviteNotificationApiParam.SENDER_MOBILE_NUMBER, reqModel.getSender_mobile_no());
        params.put(AppConstant.SendInviteNotificationApiParam.RECEIVER_MOBILE_NUMBER, reqModel.getReceiver_mobile_no());
        params.put(AppConstant.SendInviteNotificationApiParam.NOTIFICATION_TITLE, reqModel.getNotification_title());
        params.put(AppConstant.SendInviteNotificationApiParam.NOTIFICATION_MESSAGE, reqModel.getNotification_message());
        return homeRemoteApi.sendInviteNotificationApi(params);
    }

    @Override
    public Single<Response<JsonObject>> uploadProfileImage(List<KYCDocumentDatamodel> list, KYCInputModel kycInputModel) {
        RequestBody phonenumber = RequestBody.create(okhttp3.MultipartBody.FORM, kycInputModel.getUser_phone());
        RequestBody aadhar_phone = RequestBody.create(okhttp3.MultipartBody.FORM, kycInputModel.getAadhar_phone());
        RequestBody aadhar_dob = RequestBody.create(okhttp3.MultipartBody.FORM, kycInputModel.getAadhar_dob());
        RequestBody aadhar_name = RequestBody.create(okhttp3.MultipartBody.FORM, kycInputModel.getAadhar_name());
        RequestBody aadhar_no = RequestBody.create(okhttp3.MultipartBody.FORM, kycInputModel.getAadhar_no());
        RequestBody school_phone = RequestBody.create(okhttp3.MultipartBody.FORM, kycInputModel.getSchool_phone());
        RequestBody school_dob = RequestBody.create(okhttp3.MultipartBody.FORM, kycInputModel.getSchool_dob());
        RequestBody user_id = RequestBody.create(okhttp3.MultipartBody.FORM, AuroAppPref.INSTANCE.getModelInstance().getUserId());
        RequestBody langVersion = RequestBody.create(okhttp3.MultipartBody.FORM, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        RequestBody apiVersion = RequestBody.create(okhttp3.MultipartBody.FORM, AppConstant.ParamsValue.API_VERSION_VAL);
        MultipartBody.Part id_proof_front = ConversionUtil.INSTANCE.makeMultipartRequest(list.get(0));
        MultipartBody.Part id_proof_back = ConversionUtil.INSTANCE.makeMultipartRequest(list.get(1));
        MultipartBody.Part school_id_card = ConversionUtil.INSTANCE.makeMultipartRequest(list.get(2));
        MultipartBody.Part student_photo = ConversionUtil.INSTANCE.makeMultipartRequest(list.get(3));

        return homeRemoteApi.uploadImage(phonenumber,
                aadhar_name,
                aadhar_dob,
                aadhar_phone,
                aadhar_no,
                school_dob,
                school_phone,
                user_id,
                langVersion,
                apiVersion,
                id_proof_front,
                id_proof_back,
                school_id_card,
                student_photo);
    }


    @Override
    public Single<Response<JsonObject>> studentUpdateProfile(GetStudentUpdateProfile model) {

        if (model.getDeviceToken() == null) {
            model.setDeviceToken("");
        }
        try {
            RequestBody buildVersion = RequestBody.create(okhttp3.MultipartBody.FORM, model.getBuildVersion());
            AppLogger.e("update profile api-", "step 10 " + model.getBuildVersion());
            RequestBody partnersource = RequestBody.create(okhttp3.MultipartBody.FORM, "AURO3VE4j7");//model.getPartnerSource()
            AppLogger.e("update profile api-", "step 11 - " + "AURO3VE4j7");
            RequestBody schoolName = RequestBody.create(okhttp3.MultipartBody.FORM, model.getSchoolName());
            AppLogger.e("update profile api-", "step 12" + model.getSchoolName());
            RequestBody email = RequestBody.create(okhttp3.MultipartBody.FORM, model.getEmailId());
            AppLogger.e("update profile api-", "step 13" + model.getEmailId());
            RequestBody boardType = RequestBody.create(okhttp3.MultipartBody.FORM, model.getBoardType());
            AppLogger.e("update profile api-", "step 14" + model.getBoardType());
            RequestBody gender = RequestBody.create(okhttp3.MultipartBody.FORM, model.getGender());
            AppLogger.e("update profile api-", "step 15" + model.getGender());
            RequestBody registrationSource = RequestBody.create(okhttp3.MultipartBody.FORM, "AuroScholar");
            AppLogger.e("update profile api-", "step 16" + model.getRegitrationSource());
            RequestBody shareType = RequestBody.create(okhttp3.MultipartBody.FORM, "");
            AppLogger.e("update profile api-", "step 17" + "");
            RequestBody deviceToken = RequestBody.create(okhttp3.MultipartBody.FORM, model.getDeviceToken());
            AppLogger.e("update profile api-", "step 18" + model.getDeviceToken());
            RequestBody email_verified = RequestBody.create(okhttp3.MultipartBody.FORM, "N");
            AppLogger.e("update profile api-", "step 19" + model.getEmailVerified());
            RequestBody shareIdentity = RequestBody.create(okhttp3.MultipartBody.FORM, "");
            AppLogger.e("update profile api-", "step 20" + "");
            RequestBody userPartnerId = RequestBody.create(okhttp3.MultipartBody.FORM, "");
            AppLogger.e("update profile api-", "step 21" + "");
            RequestBody ipAddress = RequestBody.create(okhttp3.MultipartBody.FORM, model.getIpAddress());
            AppLogger.e("update profile api-", "step 22" + model.getIpAddress());
            RequestBody mobileVersion = RequestBody.create(okhttp3.MultipartBody.FORM, model.getMobileVersion());
            AppLogger.e("update profile api-", "step 23" + model.getMobileVersion());
            RequestBody mobileModel = RequestBody.create(okhttp3.MultipartBody.FORM, model.getMobileModel());
            AppLogger.e("update profile api-", "step 24" + model.getMobileModel());
            RequestBody privateTutionType = RequestBody.create(okhttp3.MultipartBody.FORM, model.getPrivateTutionType());
            AppLogger.e("update profile api-", "step 25" + model.getPrivateTutionType());
            RequestBody isPrivateTutionType = RequestBody.create(okhttp3.MultipartBody.FORM, model.getIsPrivateTution());
            AppLogger.e("update profile api-", "step 26" + model.getIsPrivateTution());
            RequestBody latitude = RequestBody.create(okhttp3.MultipartBody.FORM, model.getLatitude());
            AppLogger.e("update profile api-", "step 27" + model.getLatitude());
            RequestBody longitude = RequestBody.create(okhttp3.MultipartBody.FORM, model.getLongitude());
            AppLogger.e("update profile api-", "step 28" + model.getLongitude());
            RequestBody language = RequestBody.create(okhttp3.MultipartBody.FORM, model.getLanguage());
            AppLogger.e("update profile api-", "step 29" + model.getLanguage());
            RequestBody manufacturer = RequestBody.create(okhttp3.MultipartBody.FORM, model.getMobileManufacturer());
            AppLogger.e("update profile api-", "step 30" + model.getMobileManufacturer());
            RequestBody stateId = RequestBody.create(okhttp3.MultipartBody.FORM, model.getStateId());
            AppLogger.e("update profile api-", "step 31" + model.getStateId());
            RequestBody districtId = RequestBody.create(okhttp3.MultipartBody.FORM, model.getDistrictId());
            AppLogger.e("update profile api-", "step 32" + model.getDistrictId());
            RequestBody firstName = RequestBody.create(okhttp3.MultipartBody.FORM, model.getStudentName());
            AppLogger.e("update profile api-", "step 33" + model.getStudentName());
            RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, model.getUserId());
            AppLogger.e("update profile api-", "step 34" + model.getUserId());
            RequestBody schoolType = RequestBody.create(okhttp3.MultipartBody.FORM, model.getSchoolType());
            AppLogger.e("update profile api-", "step 35" + model.getSchoolType());
            MultipartBody.Part profilePic = ConversionUtil.INSTANCE.makeMultipartRequestProfile(model.getImageBytes());
            AppLogger.e("update profile api-", "step 36" + model.getImageBytes());

            RequestBody langVersion = RequestBody.create(okhttp3.MultipartBody.FORM, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
            RequestBody apiVersion = RequestBody.create(okhttp3.MultipartBody.FORM, AppConstant.ParamsValue.API_VERSION_VAL);
            RequestBody userPreferenceLanguage = RequestBody.create(okhttp3.MultipartBody.FORM, AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId());

            return homeRemoteApi.studentUpdateProfile(buildVersion, partnersource, schoolName, email, boardType, gender,
                    registrationSource, shareType, deviceToken, email_verified, shareIdentity, userPartnerId, ipAddress, mobileVersion, mobileModel,
                    privateTutionType, isPrivateTutionType, latitude, longitude, language, manufacturer, stateId, districtId, firstName, schoolType, userId, langVersion, apiVersion,
                    userPreferenceLanguage,profilePic
                    );
        } catch (Exception e) {
            AppLogger.e("update profile api-", "step 37 eception--" + e.getMessage());
        }
        return null;
    }



    @Override
    public Single<Response<JsonObject>> postDemographicData(GetStudentUpdateProfile demographicResModel) {
        Map<String, String> params = new HashMap<String, String>();

        params.put(AppConstant.DashBoardParams.USER_ID, demographicResModel.getUserId());
        params.put(AppConstant.DashBoardParams.PARTNER_SOURCE, demographicResModel.getPartnerSource());
        params.put(AppConstant.DashBoardParams.REGISTRATION_SOURCE, demographicResModel.getRegitrationSource());
        params.put(AppConstant.DemographicType.GENDER, demographicResModel.getGender());
        params.put(AppConstant.DemographicType.BOARD_TYPE, demographicResModel.getBoardType());
        params.put(AppConstant.DemographicType.SCHOOL_TYPE, demographicResModel.getSchoolType());
        params.put(AppConstant.DemographicType.LANGUAGE, demographicResModel.getLanguage());
        params.put(AppConstant.DemographicType.LATITUDE, demographicResModel.getLatitude());
        params.put(AppConstant.DemographicType.LONGITUDE, demographicResModel.getLongitude());
        params.put(AppConstant.DemographicType.MOBILE_MODEL, demographicResModel.getMobileModel());
        params.put(AppConstant.DemographicType.MOBILE_MANUFACTURER, demographicResModel.getMobileManufacturer());
        params.put(AppConstant.DemographicType.MOBILE_VERSION, demographicResModel.getMobileVersion());
        params.put(AppConstant.DemographicType.IS_PRIVATE_TUTION, demographicResModel.getIsPrivateTution());
        params.put(AppConstant.DemographicType.PRIVATE_TUTION_TYPE, demographicResModel.getPrivateTutionType());
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        //   return homeRemoteApi.studentUpdateProfile(params);
        return null;
    }

    @Override
    public Single<Response<JsonObject>> getStoreOnlineData(String modifiedTime) {
        return null;
    }

    @Override
    public Single<Response<JsonObject>> sendOtpHomeRepo(SendOtpReqModel reqModel) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AppConstant.SendOtpRequest.PHONENUMBER, reqModel.getMobileNo());
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        reqModel.setApiVersion(AppConstant.ParamsValue.API_VERSION_VAL);
        reqModel.setLangVersion(AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel.getUserType() == AppConstant.UserType.TEACHER) {
            reqModel.setIsType(AppConstant.userTypeLogin.API_PARAM_TEACHER);
            params.put(AppConstant.SendOtpRequest.USER_TYPE, AppConstant.userTypeLogin.API_PARAM_TEACHER);
        } else {
            reqModel.setIsType(AppConstant.userTypeLogin.API_PARAM_STUDENT);
            params.put(AppConstant.SendOtpRequest.USER_TYPE, AppConstant.userTypeLogin.API_PARAM_STUDENT);
        }
        int userType = prefModel.getUserType();
        AppLogger.e("enter Number Activity --", "" + userType);
       /* if (userType == AppConstant.UserType.TEACHER) {
            return homeRemoteApi.oldSendOtpApi(params);
        } else {
            return homeRemoteApi.sendOTP(reqModel);
        }*/

        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.sendOTP(reqModel);

    }

    @Override
    public Single<Response<JsonObject>> verifyOtpHomeRepo(VerifyOtpReqModel reqModel) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AppConstant.SendOtpRequest.PHONENUMBER, reqModel.getMobileNumber());
        params.put(AppConstant.SendOtpRequest.OTPVERIFY, reqModel.getOtpVerify());
        params.put(AppConstant.SendOtpRequest.USER_TYPE, reqModel.getUserType());
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int userType = prefModel.getUserType();
        AppLogger.e("enter Number Activity --", "" + userType);
      /*  if (userType == AppConstant.UserType.TEACHER) {
            return homeRemoteApi.verifyOTP(params);
        } else {
            return homeRemoteApi.verifyOTP(reqModel);
        }*/


        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));

        return homeRemoteApi.verifyOTP(reqModel);

    }

    @Override
    public Single<Response<JsonObject>> versionApiCheck() {
        return homeRemoteApi.getVersionApiCheck();
    }

    @Override
    public Single<Response<JsonObject>> state() {
        return homeRemoteApi.getState();
    }


    @Override
    public Single<Response<JsonObject>> getLanguageList() {
        return homeRemoteApi.getLanguageList();
    }

    @Override
    public Single<Response<JsonObject>> checkUserValidApi(CheckUserApiReqModel reqModel) {
        reqModel.setApiVersion(AppConstant.ParamsValue.API_VERSION_VAL);
        reqModel.setLangVersion(AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        reqModel.setUserPreferedLanguageId(Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.checkUserApi(reqModel);
    }

    @Override
    public Single<Response<JsonObject>> changeGradeApi(CheckUserValidResModel reqModel) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AppConstant.DashBoardParams.USER_ID, reqModel.getMobileNo());
        params.put(AppConstant.SendOtpRequest.STUDENT_CLASS, reqModel.getStudentClass());
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.changeGrade(params);
    }

    @Override
    public Single<Response<JsonObject>> getAssignmentId(AssignmentReqModel assignmentReqModel) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AppConstant.AzureApiParams.SUBJECT, assignmentReqModel.getSubject());
        params.put(AppConstant.DashBoardParams.USER_ID, assignmentReqModel.getUserId());
        params.put(AppConstant.AssignmentApiParams.EXAM_NAME, assignmentReqModel.getExam_name());
        params.put(AppConstant.AssignmentApiParams.QUIZ_ATTEMPT, assignmentReqModel.getQuiz_attempt());
        params.put(AppConstant.AssignmentApiParams.EXAMLANG, "E");
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        params.put("partner_unique_id",Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getPartneruniqueid()));

        return homeRemoteApi.getAssignmentId(params);
    }


    @Override
    public Single<Response<JsonObject>> acceptInviteApi(SendInviteNotificationReqModel reqModel) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AppConstant.SendInviteNotificationApiParam.CHALLENGE_BY, reqModel.getSender_mobile_no());
        params.put(AppConstant.SendInviteNotificationApiParam.CHALLENGE_TO, reqModel.getReceiver_mobile_no());
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE, Integer.parseInt( AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.acceptInviteApi(params);
    }

    @Override
    public Single<Response<JsonObject>> upgradeClass(AuroScholarDataModel model) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(AppConstant.MOBILE_NUMBER, model.getMobileNumber());
        params.put(AppConstant.DashBoardParams.STUDENT_CLASS, String.valueOf(model.getStudentClass()));
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);

        return homeRemoteApi.gradeUpgrade(params);
    }

    @Override
    public Single<Response<JsonObject>> partnersApi() {
        Map<String, Object> params = new HashMap<String, Object>();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        params.put(AppConstant.DashBoardParams.USER_ID, prefModel.getUserId());
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE, Integer.parseInt(prefModel.getUserLanguageId()));
        return homeRemoteApi.partnersApi(params);
    }

    @Override
    public Single<Response<JsonObject>> partnersLoginApi(PartnersLoginReqModel reqModel) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AppConstant.PartnersLoginApi.PARTNERS_ID, reqModel.getPartnerId());
        params.put(AppConstant.PartnersLoginApi.PARTNERS_NAME, reqModel.getPartnerName());
        params.put(AppConstant.PartnersLoginApi.STUDENT_NAME, reqModel.getStudentName());
        params.put(AppConstant.PartnersLoginApi.STUDENT_EMAIL, reqModel.getStudentEmail());
        params.put(AppConstant.PartnersLoginApi.STUDENT_PASSWORD, reqModel.getStudentPassword());
        params.put(AppConstant.PartnersLoginApi.STUDENT_CLASS, reqModel.getStudentClass());
        params.put(AppConstant.PartnersLoginApi.MOBILE_NUMBER, reqModel.getStudentMobile());
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,reqModel.getUserPreferedLanguageId());
        return homeRemoteApi.partnersLoginApi(params);
    }

    public Single<Response<JsonObject>> findFriendApi(double lat, double longt, double radius) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AppConstant.DemographicType.LATITUDE, lat);
        params.put(AppConstant.DemographicType.LONGITUDE, longt);
        params.put(AppConstant.DemographicType.RADIUS, radius);
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.findFriendApi(params);
    }

    @Override
    public Single<Response<JsonObject>> sendFriendRequestApi(int requested_by_id, int requested_user_id, String requested_by_phone, String requested_user_phone) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("requested_by_id", requested_by_id);
        params.put("requested_user_id", requested_user_id);
        params.put("requested_by_phone", requested_by_phone);
        params.put("requested_user_phone", requested_user_phone);
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.sendFriendRequestApi(params);
    }

    @Override
    public Single<Response<JsonObject>> friendRequestListApi(int requested_user_id) {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("requested_user_id", requested_user_id);
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.friendRequestListApi(params);
    }

    @Override
    public Single<Response<JsonObject>> friendAcceptApi(int friend_request_id, String request_status) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("friend_request_id", String.valueOf(friend_request_id));
        params.put("request_status", request_status);
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId());
        return homeRemoteApi.friendAcceptApi(params);
    }

    @Override
    public Single<Response<JsonObject>> getDynamicDataApi(DynamiclinkResModel model) {
        AppLogger.e("callRefferApi", "step 6" + new Gson().toJson(model));
        Map<String, Object> params = new HashMap<String, Object>();
        // params.put(AppConstant.RefferalApiCode.REFFERAL_MOBILENO, model.getRefferalMobileno());
        // params.put(AppConstant.RefferalApiCode.REFFER_MOBILENO, model.getRefferMobileno());
        params.put(AppConstant.RefferalApiCode.REFFER_USER_ID, model.getReffeUserId());
        params.put(AppConstant.RefferalApiCode.SOURCE, model.getSource());
        params.put(AppConstant.RefferalApiCode.NAVIGATION_TO, model.getNavigationTo());
        params.put(AppConstant.RefferalApiCode.REFFER_TYPE, model.getReffer_type());
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.getRefferalapi(params);
    }

    /*For Add student*/
    @Override
    public Single<Response<JsonObject>> sendRefferalDataApi(RefferalReqModel model) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AppConstant.SendRefferalApiCode.REFERRED_USER_ID, model.getReferredUserId());
        params.put(AppConstant.SendRefferalApiCode.REFERRED_BY_ID, model.getReferredById());
        params.put(AppConstant.SendRefferalApiCode.REFERRED_USER_MOBILE, model.getReferredUserMobile());
        //  params.put(AppConstant.SendRefferalApiCode.REFERRED_BY_PHONE, model.getRefferMobileno());
        // params.put(AppConstant.SendRefferalApiCode.MESSAGE, "save it");
        // params.put(AppConstant.SendRefferalApiCode.SUCCESS, "true");
        params.put(AppConstant.SendRefferalApiCode.REFERRED_BY_TYPE, model.getReferredByType());
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.sendRefferalapi(model);
    }

    @Override
    public Single<Response<JsonObject>> getCertificateApi(CertificateResModel model) {
        Map<String, Object> params = new HashMap<String, Object>();
        //  params.put(AppConstant.DashBoardParams.USER_ID, model.getRegistrationId());
        //params.put(AppConstant.DashBoardParams.STUDENT_NAME, model.getStudentName());
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,Integer.parseInt( AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.getCertificateApi(params);
    }


    @Override
    public Single<Response<JsonObject>> uploadStudentExamImage(SaveImageReqModel reqModel) {
        RequestBody exam_id = RequestBody.create(okhttp3.MultipartBody.FORM, reqModel.getExamId());
        RequestBody registration_id = RequestBody.create(okhttp3.MultipartBody.FORM, reqModel.getRegistration_id());
        RequestBody is_mobile = RequestBody.create(okhttp3.MultipartBody.FORM, "1");
        RequestBody quiz_id = RequestBody.create(okhttp3.MultipartBody.FORM, reqModel.getQuizId());
        RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, reqModel.getUserId());
        RequestBody img_normal_path = RequestBody.create(okhttp3.MultipartBody.FORM, reqModel.getImgNormalPath());
        RequestBody img_path = RequestBody.create(okhttp3.MultipartBody.FORM, reqModel.getImgPath());
        RequestBody langVersion = RequestBody.create(okhttp3.MultipartBody.FORM, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        RequestBody apiVersion = RequestBody.create(okhttp3.MultipartBody.FORM, AppConstant.ParamsValue.API_VERSION_VAL);

        MultipartBody.Part student_photo = ConversionUtil.INSTANCE.makeMultipartRequestForExamImage(reqModel.getImageBytes());
        return homeRemoteApi.uploadImage(exam_id, registration_id, is_mobile, quiz_id, img_normal_path, img_path, userId, langVersion, apiVersion, student_photo);
    }

   /* @Override
    public Single<Response<JsonObject>> uploadStudentExamImage(SaveQuestionResModel saveQuestionResModel) {
        RequestBody exam_id = RequestBody.create(okhttp3.MultipartBody.FORM,saveQuestionResModel.getExamAssignmentID());
        MultipartBody.Part student_photo = ConversionUtil.INSTANCE.makeMultipartRequestForExamImage(saveQuestionResModel.getImageBytes());
        return quizRemoteApi.uploadImage(exam_id,
                student_photo);
    }
*/

    @Override
    public Single<Response<JsonObject>> passportApi(PassportReqModel reqModel) {
        Map<String, String> headers = new HashMap<>();
        return homeRemoteApi.passportApi(reqModel);
    }

    @Override
    public Single<Response<JsonObject>> forgotPassword(SetPasswordReqModel reqModel) {
        reqModel.setApiVersion(AppConstant.ParamsValue.API_VERSION_VAL);
        reqModel.setLangVersion(AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        reqModel.setUserPreferedLanguageId(Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.forgotPassword(reqModel);
    }

    @Override
    public Single<Response<JsonObject>> loginApi(LoginReqModel reqModel) {
        reqModel.setApiVersion(AppConstant.ParamsValue.API_VERSION_VAL);
        reqModel.setLangVersion(AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        reqModel.setUserPreferedLanguageId(Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.loginApi(reqModel);
    }

    @Override
    public Single<Response<JsonObject>> getTeacherDashboardApi(AuroScholarDataModel auroScholarDataModel) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(AppConstant.MOBILE_NUMBER, auroScholarDataModel.getMobileNumber());
        params.put(AppConstant.DashBoardParams.REGISTRATION_SOURCE, auroScholarDataModel.getRegitrationSource());
        params.put(AppConstant.DashBoardParams.PARTNER_SOURCE, AppConstant.AURO_ID);
        params.put(AppConstant.DashBoardParams.IP_ADDRESS, AppUtil.getIpAdress());
        params.put(AppConstant.DashBoardParams.BUILD_VERSION, AppUtil.getAppVersionName());
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        return homeRemoteApi.getTeacherDashboardApi(params);
    }


    @Override
    public Single<Response<JsonObject>> preferenceSubjectList() {
        Map<String, Object> requestMap = new HashMap<>();

            requestMap.put(AppConstant.DashBoardParams.STUDENT_CLASS,AuroAppPref.INSTANCE.getModelInstance().getUserclass());
            requestMap.put(AppConstant.DashBoardParams.LANGUAGE, AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId());
        //requestMap.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
       // requestMap.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        //requestMap.put(AppConstant.Language.USER_PREFERED_LANGUAGE,Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageCode()));
        return homeRemoteApi.preferenceSubjectList(requestMap);
    }

    @Override
    public Single<Response<JsonObject>> fetchStudentPreferenceApi(FetchStudentPrefReqModel reqModel) {
        reqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getUserId());
        reqModel.setApiVersion(AppConstant.ParamsValue.API_VERSION_VAL);
        reqModel.setLangVersion(AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        reqModel.setUserPreferedLanguageId(Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.fetchStudentPreferenceApi(reqModel);
    }

    @Override
    public Single<Response<JsonObject>> updateStudentPreference(UpdatePrefReqModel reqModel) {
        reqModel.setApiVersion(AppConstant.ParamsValue.API_VERSION_VAL);
        reqModel.setLangVersion(AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
       // reqModel.setUserPreferedLanguageId(Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageCode()));
        return homeRemoteApi.updateStudentPreference(reqModel);
    }



    @Override
    public Single<Response<JsonObject>> getNoticeInstruction(){
        return homeRemoteApi.getNoticeInstruction();
    }

    @Override
    public Single<Response<JsonObject>> getUserProfile(String userId) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put(AppConstant.DashBoardParams.USER_ID, userId);
        requestMap.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        requestMap.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        requestMap.put(AppConstant.Language.USER_PREFERED_LANGUAGE,  AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId());
        return homeRemoteApi.getUserProfile(requestMap);
    }



    @Override
    public Single<Response<JsonObject>> registerApi(RegisterApiReqModel request) {
        request.setApiVersion(AppConstant.ParamsValue.API_VERSION_VAL);
        request.setLangVersion(AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        request.setUserPreferedLanguageId(Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.registerApi(request);
    }


    @Override
    public Single<Response<JsonObject>> setUsernamePinApi(SetUsernamePinReqModel reqModel) {
        reqModel.setApiVersion(AppConstant.ParamsValue.API_VERSION_VAL);
        reqModel.setLangVersion(AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        reqModel.setUserPreferedLanguageId(Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.setUsernamePinApi(reqModel);
    }

    @Override
    public Single<Response<JsonObject>> loginUsingPinApi(SetUsernamePinReqModel request) {
        request.setApiVersion(AppConstant.ParamsValue.API_VERSION_VAL);
        request.setLangVersion(AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        request.setUserPreferedLanguageId(Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.loginUsingPinApi(request);
    }

    @Override
    public Single<Response<JsonObject>> setUserPinApi(SetUsernamePinReqModel request) {
        request.setApiVersion(AppConstant.ParamsValue.API_VERSION_VAL);
        request.setLangVersion(AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        request.setUserPreferedLanguageId(Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.setPinApi(request);
    }

    @Override
    public Single<Response<JsonObject>> getWalletStatusApi(SetPasswordReqModel request) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(AppConstant.DashBoardParams.USER_ID, request.getUserId());
        requestMap.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        requestMap.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        requestMap.put(AppConstant.Language.USER_PREFERED_LANGUAGE,request.getUserPreferedLanguageId());
        return homeRemoteApi.getWalletStatus(requestMap);
    }

    @Override
    public Single<Response<JsonObject>> getStudentKycStatus() {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(AppConstant.DashBoardParams.USER_ID, AuroAppPref.INSTANCE.getModelInstance().getUserId());
        // requestMap.put(AppConstant.DashBoardParams.USER_ID, "620551");
        requestMap.put(AppConstant.Language.USER_PREFERED_LANGUAGE,Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        return homeRemoteApi.getStudentKycStatus(requestMap);
    }
    @Override
    public Single<Response<JsonObject>> parentUpdateProfile(String name, int stateid, int districtid, String gender,String emailid) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(AppConstant.ParentProfileParams.USER_ID, AuroAppPref.INSTANCE.getModelInstance().getUserId());
         requestMap.put(AppConstant.ParentProfileParams.FULL_NAME, name);
        requestMap.put(AppConstant.ParentProfileParams.STATE_ID, stateid);
        requestMap.put(AppConstant.ParentProfileParams.DISTRICT_ID, districtid);
        requestMap.put(AppConstant.ParentProfileParams.GENDER,gender);
        requestMap.put(AppConstant.ParentProfileParams.EMAILDID,emailid);
        return homeRemoteApi.parentUpdateProfile(requestMap);
    }





    @Override
    public Single<Response<JsonObject>> getInstructionsApi(InstructionModel id) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(AppConstant.DashBoardParams.INSTRUCTIONS_CODE, id.getInstructionCode());
        requestMap.put(AppConstant.Language.LANGUAGE_ID,id.getLanguageId());
        return homeRemoteApi.getInstructions(requestMap);
    }

    @Override
    public Single<Response<JsonObject>> getSlabsApi(UserSlabsRequest id) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(AppConstant.DashBoardParams.USER_ID, id.getUserId());
        requestMap.put(AppConstant.DashBoardParams.EXAM_MONTH,id.getExamMonth());
        requestMap.put(AppConstant.Language.USER_PREFERED_LANGUAGE,id.getUserPreferedLanguageId());
        return homeRemoteApi.getSlabApi(requestMap);
    }

    @Override
    public Single<Response<JsonObject>> pendingKycDocs(PendingKycDocsModel pendingKycDocsModel) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(AppConstant.DashBoardParams.USER_ID, pendingKycDocsModel.getUserId());
        requestMap.put(AppConstant.DashBoardParams.IS_AGREE, pendingKycDocsModel.getIsAgree());
        requestMap.put(AppConstant.Language.USER_PREFERED_LANGUAGE,pendingKycDocsModel.getUserPreferedLanguageId());

        return homeRemoteApi.pendingKycDocs(requestMap);
    }

    @Override
    public Single<Response<JsonObject>> getMsgPopUp(PendingKycDocsModel pendingKycDocsModel) {
        AppLogger.v("UserIdPradeep", "UserId"+pendingKycDocsModel.getUserId());
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(AppConstant.DashBoardParams.USER_ID, pendingKycDocsModel.getUserId());
        requestMap.put(AppConstant.Language.USER_PREFERED_LANGUAGE,pendingKycDocsModel.getUserPreferedLanguageId());
        return homeRemoteApi.getMsgPopUp(requestMap);
    }

    @Override
    public Single<Response<JsonObject>> getDynamicLang(LanguageMasterReqModel languageMasterReqModel) {
        AppLogger.v("Language_pradeep", "Dynamic language Step 7");
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(AppConstant.Language.LANGUAGE_ID, languageMasterReqModel.getLanguageId());
        requestMap.put(AppConstant.Language.USER_TYPE_ID, languageMasterReqModel.getUserTypeId());
        return homeRemoteApi.getDynamicLanguage(requestMap);
    }

    @Override
    public Single<Response<JsonObject>> optOverCall(OtpOverCallReqModel request) {
        Map<String, String> requestMap = new HashMap<>();
        return homeRemoteApi.otpOverCall(request);
    }

    @Override
    public Single<Response<JsonObject>> parentUpdateProfile() {
        return null;
    }

}

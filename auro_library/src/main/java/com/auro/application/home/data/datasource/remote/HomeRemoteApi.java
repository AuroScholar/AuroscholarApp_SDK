package com.auro.application.home.data.datasource.remote;

import com.auro.application.core.common.AppConstant;
import com.auro.application.core.network.URLConstant;

import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.OtpOverCallReqModel;
import com.auro.application.home.data.model.RefferalReqModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.SetPasswordReqModel;
import com.auro.application.home.data.model.VerifyOtpReqModel;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.UpdatePrefReqModel;
import com.auro.application.home.data.model.passportmodels.PassportReqModel;
import com.auro.application.home.data.model.signupmodel.request.LoginReqModel;
import com.auro.application.home.data.model.signupmodel.request.RegisterApiReqModel;
import com.auro.application.home.data.model.signupmodel.request.SetUsernamePinReqModel;
import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HomeRemoteApi {


    @POST(URLConstant.OTP_SEND_API)
    Single<Response<JsonObject>> sendOTP(@Body SendOtpReqModel reqModel);


    @POST(URLConstant.OTP_VERIFY)
    Single<Response<JsonObject>> verifyOTP(@Body VerifyOtpReqModel reqModel);

    @POST(URLConstant.FORGOT_PASSWORD_API)
    Single<Response<JsonObject>> forgotPassword(@Body SetPasswordReqModel reqModel);


    @POST(URLConstant.LOGIN_API)
    Single<Response<JsonObject>> loginApi(@Body LoginReqModel reqModel);

    @GET(URLConstant.GET_STATE)

    Single<Response<JsonObject>> getState();

    @GET(URLConstant.VERSION_API)
    Single<Response<JsonObject>> getVersionApiCheck();

    @POST(URLConstant.USER_CHECK)
    Single<Response<JsonObject>> checkUserApi(@Body CheckUserApiReqModel reqModel);

    @POST(URLConstant.SET_USERNAME_PIN_API)
    Single<Response<JsonObject>> setUsernamePinApi(@Body SetUsernamePinReqModel reqModel);


    @POST(URLConstant.SET_USER_PIN_API)
    Single<Response<JsonObject>> setPinApi(@Body SetUsernamePinReqModel reqModel);

    @POST(URLConstant.LOGIN_USING_PIN_API)
    Single<Response<JsonObject>> loginUsingPinApi(@Body SetUsernamePinReqModel reqModel);


    @POST(URLConstant.GRADE_UPGRADE)
    @FormUrlEncoded
    Single<Response<JsonObject>> changeGrade(@FieldMap Map<String, Object> params);


    @POST(URLConstant.DASHBOARD_SDK_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> getDashboardSDKData(@FieldMap Map<String, Object> params);


    @POST(URLConstant.DEMOGRAPHIC_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> postDemographicData(@FieldMap  Map<String, String> params);

    @Multipart
    @POST(URLConstant.UPLOAD_IMAGE_URL)
    Single<Response<JsonObject>> uploadImage(@Part(AppConstant.MOBILE_NUMBER) RequestBody description,
                                             @Part(AppConstant.DocumentType.AADHAR_NAME) RequestBody aadhar_name,
                                             @Part(AppConstant.DocumentType.AADHAR_DOB) RequestBody aadhar_dob,
                                             @Part(AppConstant.DocumentType.AADHAR_PHONE) RequestBody aadhar_phone,
                                             @Part(AppConstant.DocumentType.AADHAR_NO) RequestBody aadhar_no,
                                             @Part(AppConstant.DocumentType.SCHOOL_DOB) RequestBody school_dob,
                                             @Part(AppConstant.DocumentType.SCHOOL_PHONE) RequestBody schhol_phone,
                                             @Part(AppConstant.DashBoardParams.USER_ID) RequestBody user_id,
                                             @Part(AppConstant.DashBoardParams.LANGUAGE_VERSION) RequestBody langVersion,
                                             @Part(AppConstant.DashBoardParams.API_VERSION) RequestBody apiVersion,
                                             @Part(AppConstant.Language.USER_PREFERED_LANGUAGE) RequestBody userpreferedlangid,

                                             @Part MultipartBody.Part id_front,
                                             @Part MultipartBody.Part id_back,
                                             @Part MultipartBody.Part student_id,
                                             @Part MultipartBody.Part student_photo);


    @POST(URLConstant.UPDATE_STUDENT_PROFILE)
    @Multipart
    Single<Response<JsonObject>> studentUpdateProfile(
            @Part(AppConstant.DashBoardParams.BUILD_VERSION) RequestBody build_Version,
            @Part(AppConstant.DashBoardParams.PARTNER_SOURCE) RequestBody partnerSource,
            @Part(AppConstant.DashBoardParams.SCHOOL_NAME) RequestBody schoolName,
            @Part(AppConstant.EMAIL_ID) RequestBody emailId,
            @Part(AppConstant.BOARD_TYPE) RequestBody boardType,
            @Part(AppConstant.GENDER) RequestBody gender,
            @Part(AppConstant.DashBoardParams.REGISTRATION_SOURCE) RequestBody registrationSource,
            @Part(AppConstant.DashBoardParams.SHARE_TYPE) RequestBody shareType,
            @Part(AppConstant.DashBoardParams.DEVICE_TOKEN) RequestBody deviceToken,
            @Part(AppConstant.DashBoardParams.IS_EMAIL_VERIFIED) RequestBody emailVerified,
            @Part(AppConstant.DashBoardParams.SHARE_IDENTITY) RequestBody shareIdentity,
            @Part(AppConstant.DashBoardParams.USER_PARTNER_ID) RequestBody userPartnerId,
            @Part(AppConstant.DashBoardParams.IP_ADDRESS) RequestBody ipAddress,
            @Part(AppConstant.MOBILE_VERSION) RequestBody mobileVersion,
            @Part(AppConstant.MOBILE_MODEL) RequestBody mobileModel,
            @Part(AppConstant.DemographicType.PRIVATE_TUTION_TYPE) RequestBody privateTutorType,
            @Part(AppConstant.DemographicType.IS_PRIVATE_TUTION) RequestBody isPrivateTutor,
            @Part(AppConstant.DemographicType.LATITUDE) RequestBody latitude,
            @Part(AppConstant.DemographicType.LONGITUDE) RequestBody longitude,
            @Part(AppConstant.LANGUAGE_MEDIUM) RequestBody languageMedium,
            @Part(AppConstant.MOBILE_MANUFACTURER) RequestBody mobileManufature,
            @Part(AppConstant.DemographicType.STATE_ID) RequestBody stateCode,
            @Part(AppConstant.DemographicType.DISTRICT_ID) RequestBody distcitcode,
            @Part(AppConstant.USER_FIRSTNAME) RequestBody firstName,
            @Part(AppConstant.SCHOOL_TYPE) RequestBody schoolType,
            @Part(AppConstant.DashBoardParams.USER_ID) RequestBody userId,
            @Part(AppConstant.DashBoardParams.LANGUAGE_VERSION) RequestBody langVersion,
            @Part(AppConstant.DashBoardParams.API_VERSION) RequestBody apiVersion,
            @Part(AppConstant.Language.USER_PREFERED_LANGUAGE) RequestBody userPreferenceLanguage,
            @Part MultipartBody.Part studentProfileImage);





    @POST(URLConstant.GET_ASSIGNMENT_ID)
    @FormUrlEncoded
    Single<Response<JsonObject>> getAssignmentId(@FieldMap Map<String, Object> params);

    @Multipart
    @POST(URLConstant.AZURE_API)
    Single<Response<JsonObject>> getAzureApiData(
            @Part(AppConstant.AzureApiParams.REGISTRATION_ID) RequestBody registration_id,
            @Part(AppConstant.AzureApiParams.EKLAVVYA_EXAM_ID) RequestBody exam_id,
            @Part(AppConstant.AzureApiParams.EXAM_NAME) RequestBody exam_name,
            @Part(AppConstant.AzureApiParams.QUIZ_ATTEMPT) RequestBody quiz_attempt,
            @Part(AppConstant.AzureApiParams.SUBJECT) RequestBody subject,
            @Part(AppConstant.AzureApiParams.EXAM_ID) RequestBody examId,
            @Part(AppConstant.DashBoardParams.USER_ID) RequestBody userId,
            @Part(AppConstant.DashBoardParams.LANGUAGE_VERSION) RequestBody langVersion,
            @Part(AppConstant.DashBoardParams.API_VERSION) RequestBody apiVersion,
           // @Part(AppConstant.DashBoardParams.partner_unique_id) RequestBody partner_unique_id,
            @Part MultipartBody.Part exam_face_img);


    @POST(URLConstant.INVITE_FRIEND_LIST_API)
    Single<Response<JsonObject>> inviteFriendListApi(@Body Map<String, String> params);

    @POST(URLConstant.SEND_NOTIFICATION_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> sendInviteNotificationApi(@FieldMap Map<String, String> params);

    @POST(URLConstant.ACCEPT_STUDENT_INVITE)
    @FormUrlEncoded
    Single<Response<JsonObject>> acceptInviteApi(@FieldMap Map<String, Object> params);

    @POST(URLConstant.FIND_FRIEND_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> findFriendApi(@FieldMap Map<String, Object> params);

    @POST(URLConstant.SEND_FRIEND_REQUEST_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> sendFriendRequestApi(@FieldMap Map<String, Object> params);

    @POST(URLConstant.FRIEND_REQUEST_LIST_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> friendRequestListApi(@FieldMap Map<String, Integer> params);

    @POST(URLConstant.FRIEND_ACCEPT_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> friendAcceptApi(@FieldMap Map<String, String> params);


    @POST(URLConstant.GRADE_UPGRADE)
    @FormUrlEncoded
    Single<Response<JsonObject>> gradeUpgrade(@FieldMap Map<String, String> params);

    @POST(URLConstant.REFFER_API)
    Single<Response<JsonObject>> getRefferalapi(@Body Map<String, Object> params);

    @POST(URLConstant.SEND_REFERRAL_API)
    Single<Response<JsonObject>> sendRefferalapi(@Body RefferalReqModel reqModel);

    @POST(URLConstant.CERTIFICATE_API)
    Single<Response<JsonObject>> getCertificateApi(@Body Map<String, Object> params);


    @Multipart
    @POST(URLConstant.EXAM_IMAGE_API)
    Single<Response<JsonObject>> uploadImage(@Part(AppConstant.AssignmentApiParams.EKLAVYA_EXAM_ID) RequestBody description,
                                             @Part(AppConstant.AssignmentApiParams.REGISTRATION_ID) RequestBody registration_id,
                                             @Part(AppConstant.AssignmentApiParams.IS_MOBILE) RequestBody isMobile,
                                             @Part(AppConstant.AssignmentApiParams.QUIZ_ID) RequestBody quiz_id,
                                             @Part(AppConstant.AssignmentApiParams.IMG_NORMAL_PATH) RequestBody img_normal_path,
                                             @Part(AppConstant.AssignmentApiParams.IMG_PATH) RequestBody img_path,
                                             @Part(AppConstant.DashBoardParams.USER_ID) RequestBody userid,
                                             @Part(AppConstant.DashBoardParams.LANGUAGE_VERSION) RequestBody langVersion,
                                             @Part(AppConstant.DashBoardParams.API_VERSION) RequestBody apiVersion,
                                             @Part MultipartBody.Part exam_face_image);


    @POST(URLConstant.PASSPORT_API)
    Single<Response<JsonObject>> passportApi(@Body PassportReqModel requestBody);

    @POST(URLConstant.PARTNERS_API)
    Single<Response<JsonObject>> partnersApi(@Body Map<String, Object> params);

    @POST(URLConstant.PARTNERS_LOGIN_API)
    Single<Response<JsonObject>> partnersLoginApi(@Body Map<String, Object> params);



    @POST(URLConstant.GET_PROFILE_UPDATE_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> getTeacherDashboardApi(@FieldMap Map<String, String> params);

    @POST(URLConstant.STUDENT_SUBJECT_PREFERENCE_API)
    Single<Response<JsonObject>> preferenceSubjectList(@Body Map<String, Object> map);

    @POST(URLConstant.FETCH_STUDENT_PREFERENCE_API)
    Single<Response<JsonObject>> fetchStudentPreferenceApi(@Body FetchStudentPrefReqModel reqModel);

    @POST(URLConstant.UPDATE_USER_PREFERENCE)
    Single<Response<JsonObject>> updateStudentPreference(@Body UpdatePrefReqModel reqModel);

    @GET(URLConstant.LANGUAGE_LIST)
    Single<Response<JsonObject>> getLanguageList();


    @POST(URLConstant.GET_USER_PROFILE)
    @FormUrlEncoded
    Single<Response<JsonObject>> getUserProfile(@FieldMap Map<String, String> params);


    @POST(URLConstant.REGISTER_USER_API)
    Single<Response<JsonObject>> registerApi(@Body RegisterApiReqModel reqModel);


    @POST(URLConstant.GET_STUDENT_WALLET_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> getWalletStatus(@FieldMap Map<String, Object> params);

    @POST(URLConstant.LANGUAGE_DYNAMIC)
    Single<Response<JsonObject>> getDynamicLanguage(@Body Map<String, Object> params);

    @POST(URLConstant.STUDENT_GET_KYC_DETAILS)
    Single<Response<JsonObject>> getStudentKycStatus(@Body Map<String, Object> params);

    @POST(URLConstant.UPDATE_PARENT_PROFILE)
    Single<Response<JsonObject>> parentUpdateProfile(@Body Map<String, Object> params);

    @POST(URLConstant.GET_INSTRUCTIONS)
    Single<Response<JsonObject>> getInstructions(@Body Map<String, Object> params);

    @POST(URLConstant.OTP_OVER_CALL)
    Single<Response<JsonObject>> otpOverCall(@Body OtpOverCallReqModel reqmodel);

    @GET(URLConstant.NOTICE_INSTRUCTION)
    Single<Response<JsonObject>> getNoticeInstruction();

    @POST(URLConstant.PENDING_KYC_DOCS)
    @FormUrlEncoded
    Single<Response<JsonObject>> pendingKycDocs(@FieldMap Map<String, Object> params);


    @POST(URLConstant.GET_MSG_POPUP)
    @FormUrlEncoded
    Single<Response<JsonObject>> getMsgPopUp(@FieldMap Map<String, Object> params);


    @POST(URLConstant.GET_SLABS_API)
    Single<Response<JsonObject>> getSlabApi(@Body Map<String, Object> params);

}

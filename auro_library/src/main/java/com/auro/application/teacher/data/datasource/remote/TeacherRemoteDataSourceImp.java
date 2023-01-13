package com.auro.application.teacher.data.datasource.remote;

import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.teacher.data.model.request.AddGroupReqModel;
import com.auro.application.teacher.data.model.request.AddStudentGroupReqModel;
import com.auro.application.teacher.data.model.request.AvailableSlotsReqModel;
import com.auro.application.teacher.data.model.request.BookSlotReqModel;
import com.auro.application.teacher.data.model.request.BookedSlotReqModel;
import com.auro.application.teacher.data.model.request.CancelWebinarSlot;
import com.auro.application.teacher.data.model.request.DeleteStudentGroupReqModel;
import com.auro.application.teacher.data.model.request.SendInviteNotificationReqModel;
import com.auro.application.teacher.data.model.request.TeacherAddStudentInGroupReqModel;
import com.auro.application.teacher.data.model.request.TeacherCreateGroupReqModel;
import com.auro.application.teacher.data.model.request.TeacherReqModel;
import com.auro.application.teacher.data.model.request.TeacherUserIdReq;
import com.auro.application.teacher.data.repository.TeacherRepo;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Part;

public class TeacherRemoteDataSourceImp implements TeacherRepo.TeacherRemoteData {

    TeacherRemoteApi teacherRemoteApi;

    public TeacherRemoteDataSourceImp(TeacherRemoteApi teacherRemoteApi) {
        this.teacherRemoteApi = teacherRemoteApi;
    }




    @Override
    public Single<Response<JsonObject>> updateTeacherProfileApi(TeacherReqModel model) {

        if (model.getDevice_token() == null) {
            model.setDevice_token("");
        }

        AppLogger.v("InfoScreen", "Step respose api update teacherremoteusecase");
        try {
            RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, model.getUser_id());
            AppLogger.e("update profile api-", "step 10 " + model.getUser_id());


            RequestBody teacherName = RequestBody.create(okhttp3.MultipartBody.FORM, model.getTeacher_name());//model.getPartnerSource()
            AppLogger.e("update profile api-", "step 11 - " + "AURO3VE4j7");


            RequestBody userName = RequestBody.create(okhttp3.MultipartBody.FORM, model.getUser_name());
            AppLogger.e("update profile api-", "step 12" + model.getUser_name());

            RequestBody mobileNumber = RequestBody.create(okhttp3.MultipartBody.FORM, model.getMobile_no());
            AppLogger.e("update profile api-", "step 13" + model.getMobile_no());


            RequestBody emaiId = RequestBody.create(okhttp3.MultipartBody.FORM, model.getEmail_id());
            AppLogger.e("update profile api-", "step 14" + model.getEmail_id());


            RequestBody contryId = RequestBody.create(okhttp3.MultipartBody.FORM, model.getCountry_id());
            AppLogger.e("update profile api-", "step 15" + model.getCountry_id());


            RequestBody stateId = RequestBody.create(okhttp3.MultipartBody.FORM, model.getState_id());
            AppLogger.e("update profile api-", "step 16" + model.getSchool_id());


            RequestBody districtId = RequestBody.create(okhttp3.MultipartBody.FORM, model.getDistrict_id());
            AppLogger.e("update profile api-", "step 17" + "");


            RequestBody schoolid = RequestBody.create(okhttp3.MultipartBody.FORM, model.getSchool_id());
            AppLogger.e("update profile api-", "step 18" + model.getSchool_id());


            RequestBody prefrence = RequestBody.create(okhttp3.MultipartBody.FORM, model.getPrefered_language());
            AppLogger.e("update profile api-", "step 19" + model.getPrefered_language());


            RequestBody deviceToken = RequestBody.create(okhttp3.MultipartBody.FORM, model.getDevice_token());
            AppLogger.e("update profile api-", "step 20" + "");


            RequestBody gender = RequestBody.create(okhttp3.MultipartBody.FORM, model.getGender());
            AppLogger.e("update profile api-", "step 21" + "");


            RequestBody dateofBirth = RequestBody.create(okhttp3.MultipartBody.FORM, model.getDate_of_birth());
            AppLogger.e("update profile api-", "step 22" + model.getDate_of_birth());

            MultipartBody.Part profilePic = ConversionUtil.INSTANCE.makeMultipartRequestTeacherProfile(model.getTeacher_profile_pic());
            AppLogger.e("update profile api-", "step 36" + model.getTeacher_profile_pic());


            return teacherRemoteApi.updateTeacherProfileApi( userId,
                    teacherName,
                    userName,
                    mobileNumber,
                    emaiId,
                    gender,
                    contryId,
                    stateId,
                    districtId,
                    schoolid,
                    prefrence,
                    deviceToken,
                    dateofBirth,
                    profilePic);

        } catch (Exception e) {
            AppLogger.e("update profile api-", "step 37 eception--" + e.getMessage());
        }
        return null;
    }



/*    @Override
    public Single<Response<JsonObject>> updateTeacherProfileApi(TeacherReqModel model) {
        Map<String, Object> params = new HashMap<String, String>();
        params.put(AppConstant.TeacherProfileParams.MOBILE_NUMBER, model.getMobile_no());
        params.put(AppConstant.TeacherProfileParams.TEACHER_NAME, model.getTeacher_name());
        params.put(AppConstant.TeacherProfileParams.TEACHER_EMAIL, model.getEmail_id());
        params.put(AppConstant.TeacherProfileParams.SCHOOL_NAME, model.getSchool_id());
        params.put(AppConstant.TeacherProfileParams.STATE_ID, model.getState_id());
        params.put(AppConstant.TeacherProfileParams.DISTRICT_ID, model.getDistrict_id());
        params.put(AppConstant.TeacherProfileParams.TEACHER_CLASS, model.getTeacher_class());
        params.put(AppConstant.TeacherProfileParams.TEACHER_SUBJECT, model.getTeacher_subject());
        return teacherRemoteApi.updateTeacherProfileApi(params);

    }*/

    @Override
    public Single<Response<JsonObject>> getTeacherDashboardApi(AuroScholarDataModel auroScholarDataModel) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(AppConstant.MOBILE_NUMBER, auroScholarDataModel.getMobileNumber());
        params.put(AppConstant.DashBoardParams.REGISTRATION_SOURCE, auroScholarDataModel.getRegitrationSource());
        params.put(AppConstant.DashBoardParams.PARTNER_SOURCE, AppConstant.AURO_ID);
        params.put(AppConstant.DashBoardParams.IP_ADDRESS, AppUtil.getIpAdress());
        params.put(AppConstant.DashBoardParams.BUILD_VERSION, AppUtil.getAppVersionName());
        return teacherRemoteApi.getTeacherDashboardApi(params);
    }

    @Override
    public Single<Response<JsonObject>> getTeacherKycStatusApi(FetchStudentPrefReqModel reqModel) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(AppConstant.DashBoardParams.USER_ID, reqModel.getUserId());
        return teacherRemoteApi.getTeacherKYCStatusApi(params);
    }

    @Override
    public Single<Response<JsonObject>> getTeacherProgressApi(String mobileNumber) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(AppConstant.MOBILE_NUMBER, mobileNumber);
        return teacherRemoteApi.getTeacherProgressApi(params);
    }

    @Override
    public Single<Response<JsonObject>> getProfileTeacherApi(String userid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(AppConstant.DashBoardParams.USER_ID, userid);
        return teacherRemoteApi.getProfileTeacherApi(params);
    }


    @Override
    public Single<Response<JsonObject>> uploadTeacherKYC(List<KYCDocumentDatamodel> list) {
        RequestBody phonenumber = RequestBody.create(MultipartBody.FORM, AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        MultipartBody.Part id_proof_front = ConversionUtil.INSTANCE.makeMultipartRequest(list.get(0));
        MultipartBody.Part id_proof_back = ConversionUtil.INSTANCE.makeMultipartRequest(list.get(1));
        MultipartBody.Part school_id_card = ConversionUtil.INSTANCE.makeMultipartRequest(list.get(2));
        MultipartBody.Part student_photo = ConversionUtil.INSTANCE.makeMultipartRequest(list.get(3));
        return teacherRemoteApi.uploadTeacherKYC(phonenumber,
                id_proof_front,
                id_proof_back,
                school_id_card,
                student_photo);
    }

    @Override
    public Single<Response<JsonObject>> sendInviteNotificationApi(SendInviteNotificationReqModel reqModel) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(AppConstant.SendInviteNotificationApiParam.SENDER_MOBILE_NUMBER, reqModel.getSender_mobile_no());
        params.put(AppConstant.SendInviteNotificationApiParam.RECEIVER_MOBILE_NUMBER, reqModel.getReceiver_mobile_no());
        params.put(AppConstant.SendInviteNotificationApiParam.NOTIFICATION_TITLE, reqModel.getNotification_title());
        params.put(AppConstant.SendInviteNotificationApiParam.NOTIFICATION_MESSAGE, reqModel.getNotification_message());
        return teacherRemoteApi.sendInviteNotificationApi(params);
    }

    @Override
    public Single<Response<JsonObject>> getZohoAppointments() {
        return teacherRemoteApi.getZohoAppointments();
    }

    @Override
    public Single<Response<JsonObject>> bookZohoAppointments(String from_time, String name, String email, String phone_number) {
        return teacherRemoteApi.bookZohoAppointments(from_time, name, email, phone_number);
    }

    @Override
    public Single<Response<JsonObject>> availableWebinarSlots(AvailableSlotsReqModel reqModel) {
        return null;
    }



    @Override
    public Single<Response<JsonObject>> addGroup(AddGroupReqModel reqModel) {
        return null;
    }

    @Override
    public Single<Response<JsonObject>> addStudentInGroup(AddStudentGroupReqModel reqModel) {
        return null;
    }

    @Override
    public Single<Response<JsonObject>> deleteStudentFromGroup(DeleteStudentGroupReqModel reqModel) {
        return null;
    }

    @Override
    public Single<Response<JsonObject>> teacherDashboardSummary(TeacherUserIdReq teacherUserIdReq) {
        return teacherRemoteApi.teacher_dashboard_summary(teacherUserIdReq);
    }

    @Override
    public Single<Response<JsonObject>> teacherClassroom(TeacherUserIdReq teacherUserIdReq) {
        return teacherRemoteApi.teacher_class_room(teacherUserIdReq);
    }

    @Override
    public Single<Response<JsonObject>> teacherBookedSlotApi(BookedSlotReqModel reqModel) {
        return teacherRemoteApi.teacherBookedSlot(reqModel);
    }

    @Override
    public Single<Response<JsonObject>> teacherAvailableSlotsApi(BookedSlotReqModel reqModel) {
        return teacherRemoteApi.teacherAvailableSlotsApi(reqModel);
    }

    @Override
    public Single<Response<JsonObject>> teacherCreateGroupApi(TeacherCreateGroupReqModel reqModel) {
        return teacherRemoteApi.teacherCreateGroupApi(reqModel);
    }

    @Override
    public Single<Response<JsonObject>> teacherAddStudentInGroupApi(TeacherAddStudentInGroupReqModel reqModel) {
        return teacherRemoteApi.teacherAddStudentinGroupApi(reqModel);
    }

    @Override
    public Single<Response<JsonObject>> bookSlotApi(BookSlotReqModel reqModel) {
        return teacherRemoteApi.bookSlotApi(reqModel);
    }

    @Override
    public Single<Response<JsonObject>> cancelSlotApi(CancelWebinarSlot reqModel) {
        return teacherRemoteApi.cancelWebAvailable(reqModel);
    }


    @Override
    public Single<Response<JsonObject>> getDynamicDataApi(DynamiclinkResModel model) {
        Map<String, String> params = new HashMap<String, String>();
        // params.put(AppConstant.RefferalApiCode.REFFERAL_MOBILENO, model.getRefferalMobileno());
        // params.put(AppConstant.RefferalApiCode.REFFER_MOBILENO, model.getRefferMobileno());
        params.put(AppConstant.RefferalApiCode.REFFER_USER_ID, model.getReffeUserId());
        params.put(AppConstant.RefferalApiCode.SOURCE, model.getSource());
        params.put(AppConstant.RefferalApiCode.NAVIGATION_TO, model.getNavigationTo());
        params.put(AppConstant.RefferalApiCode.REFFER_TYPE, model.getReffer_type());
        return teacherRemoteApi.getRefferalapi(model);
    }
}

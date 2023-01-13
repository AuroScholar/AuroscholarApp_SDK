package com.auro.application.teacher.data.datasource.remote;

import com.auro.application.core.common.AppConstant;
import com.auro.application.core.network.URLConstant;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.teacher.data.model.request.BookSlotReqModel;
import com.auro.application.teacher.data.model.request.BookedSlotReqModel;
import com.auro.application.teacher.data.model.request.CancelWebinarSlot;
import com.auro.application.teacher.data.model.request.TeacherAddStudentInGroupReqModel;
import com.auro.application.teacher.data.model.request.TeacherCreateGroupReqModel;
import com.auro.application.teacher.data.model.request.TeacherUserIdReq;
import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface TeacherRemoteApi {



    @POST(URLConstant.GET_PROFILE_UPDATE_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> getTeacherDashboardApi(@FieldMap Map<String, String> params);

    @POST(URLConstant.GET_TEACHER_PROGRESS_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> getTeacherProgressApi(@FieldMap Map<String, String> params);

    @POST(URLConstant.GET_PROFILE_TEACHER_API)
    Single<Response<JsonObject>> getProfileTeacherApi(@Body Map<String, String> params);

    @POST(URLConstant.TEACHER_KYC_STATUS_API)

    Single<Response<JsonObject>> getTeacherKYCStatusApi(@Body Map<String, String> params);



    @POST(URLConstant.TEACHER_KYC_API)
    @Multipart
    Single<Response<JsonObject>> uploadTeacherKYC(
            @Part(AppConstant.DashBoardParams.USER_ID) RequestBody userid,
            @Part MultipartBody.Part govt_id_front,
            @Part MultipartBody.Part govt_id_back,
            @Part MultipartBody.Part school_id_card,
            @Part MultipartBody.Part teacher_photo
    );



    @POST(URLConstant.SEND_NOTIFICATION_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> sendInviteNotificationApi(@FieldMap Map<String, String> params);

    @GET(URLConstant.GET_ZOHO_APPOINTMENT)
    Single<Response<JsonObject>> getZohoAppointments();



    @POST(URLConstant.BOOK_ZOHO_APPOINTMENT)
    @FormUrlEncoded
    Single<Response<JsonObject>> bookZohoAppointments(@Field("from_time") String from_time, @Field("name") String name, @Field("email") String email, @Field("phone_number") String phone_number);

    @POST(URLConstant.TEACHER_CLASSROOM)
    Single<Response<JsonObject>> teacher_class_room(@Body TeacherUserIdReq teacherUserIdReq);


    @POST(URLConstant.TEACHER_DASHBOARD_SUMMARY)
    Single<Response<JsonObject>>  teacher_dashboard_summary(@Body TeacherUserIdReq teacherUserIdReq);


    @POST(URLConstant.TEACHER_BOOKED_SLOT)
    Single<Response<JsonObject>>  teacherBookedSlot(@Body BookedSlotReqModel reqModel);

    @POST(URLConstant.TEACHER_AVAILABLE_SLOTS_API)
    Single<Response<JsonObject>>  teacherAvailableSlotsApi(@Body BookedSlotReqModel reqModel);

    @POST(URLConstant.TEACHER_CREATE_GROUP_API)
    Single<Response<JsonObject>>  teacherCreateGroupApi(@Body TeacherCreateGroupReqModel reqModel);


    @POST(URLConstant.TEACHER_ADD_STUDENT_IN_GROUP_API)
    Single<Response<JsonObject>>  teacherAddStudentinGroupApi(@Body TeacherAddStudentInGroupReqModel reqModel);

    @POST(URLConstant.BOOK_SLOT_API)
    Single<Response<JsonObject>>  bookSlotApi(@Body BookSlotReqModel reqModel);

    @POST(URLConstant.CANCEL_WEBINAR_AVAILABLE)
    Single<Response<JsonObject>> cancelWebAvailable(@Body CancelWebinarSlot cancelWebinarSlot);




    @POST(URLConstant.TEACHER_PROFILE_UPDATE_API)
    @Multipart
    Single<Response<JsonObject>> updateTeacherProfileApi(
            @Part(AppConstant.DashBoardParams.USER_ID) RequestBody userId,
            @Part(AppConstant.TeacherProfileParams.TEACHER_NAME) RequestBody teacherName,
            @Part(AppConstant.TeacherProfileParams.USER_NAME) RequestBody userName,
            @Part(AppConstant.MOBILE_NUMBER) RequestBody mobileNumber,
            @Part(AppConstant.TEACHER_EMAIL_ID) RequestBody emaiId,
            @Part(AppConstant.GENDER) RequestBody gender ,
            @Part(AppConstant.COUNTRY_ID) RequestBody contryId,
            @Part(AppConstant.TeacherProfileParams.STATE_ID) RequestBody stateId,
            @Part(AppConstant.TeacherProfileParams.DISTRICT_ID) RequestBody districtId,
            @Part(AppConstant.SCHOOL_ID) RequestBody schoolid,
            @Part(AppConstant.DashBoardParams.PREFERED_LANGUAGE) RequestBody prefrence,
            @Part(AppConstant.DashBoardParams.DEVICE_TOKEN) RequestBody deviceToken,
            @Part(AppConstant.DashBoardParams.DATE_OF_BIRTH) RequestBody dateofBirth,
            @Part MultipartBody.Part profilePic);


    @POST(URLConstant.REFFER_API)
    Single<Response<JsonObject>> getRefferalapi(@Body DynamiclinkResModel dynamiclinkResModel);

}


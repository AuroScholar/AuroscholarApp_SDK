package com.auro.application.teacher.data.repository;

import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.KYCDocumentDatamodel;

import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.common.StateDataModel;
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
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;

public interface TeacherRepo {

    interface TeacherRemoteData {

        Single<Response<JsonObject>> updateTeacherProfileApi(TeacherReqModel model);

        Single<Response<JsonObject>> getTeacherDashboardApi(AuroScholarDataModel auroScholarDataModel);

        Single<Response<JsonObject>> getTeacherKycStatusApi(FetchStudentPrefReqModel reqModel);

        Single<Response<JsonObject>> getTeacherProgressApi(String mobileNumber);

        Single<Response<JsonObject>> getProfileTeacherApi(String userId);

        Single<Response<JsonObject>> uploadTeacherKYC(List<KYCDocumentDatamodel> list);

        Single<Response<JsonObject>> sendInviteNotificationApi(SendInviteNotificationReqModel reqModel);

        Single<Response<JsonObject>> getZohoAppointments();

        Single<Response<JsonObject>> bookZohoAppointments(String from_time, String name, String email, String phone_number);

        Single<Response<JsonObject>> availableWebinarSlots(AvailableSlotsReqModel reqModel);


        Single<Response<JsonObject>> addGroup(AddGroupReqModel reqModel);

        Single<Response<JsonObject>> addStudentInGroup(AddStudentGroupReqModel reqModel);

        Single<Response<JsonObject>> deleteStudentFromGroup(DeleteStudentGroupReqModel reqModel);

        Single<Response<JsonObject>> teacherDashboardSummary(TeacherUserIdReq teacherUserIdReq);

        Single<Response<JsonObject>> teacherClassroom(TeacherUserIdReq teacherUserIdReq);

        Single<Response<JsonObject>> teacherBookedSlotApi(BookedSlotReqModel reqModel);

        Single<Response<JsonObject>> teacherAvailableSlotsApi(BookedSlotReqModel reqModel);

        Single<Response<JsonObject>> teacherCreateGroupApi(TeacherCreateGroupReqModel reqModel);


        Single<Response<JsonObject>> teacherAddStudentInGroupApi(TeacherAddStudentInGroupReqModel reqModel);


        Single<Response<JsonObject>> bookSlotApi(BookSlotReqModel reqModel);

        Single<Response<JsonObject>> cancelSlotApi(CancelWebinarSlot reqModel);


        Single<Response<JsonObject>> getDynamicDataApi(DynamiclinkResModel model);

    }


    interface TeacherDbData {

        Single<Long[]> insertStateList(List<StateDataModel> stateDataModelList);

        Single<Long[]> insertDistrictList(List<DistrictDataModel> stateDataModelList);

        Single<List<StateDataModel>> getStateList();

        Single<List<DistrictDataModel>> getDistrictList();


        Single<List<DistrictDataModel>> getStateDistrictList(String stateCode);

    }

}

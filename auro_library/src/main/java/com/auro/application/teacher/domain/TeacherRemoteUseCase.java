package com.auro.application.teacher.domain;

import android.util.Log;


import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.NetworkUtil;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.core.network.NetworkUseCase;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.data.model.KYCResListModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.teacher.data.model.common.CommonDataResModel;
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
import com.auro.application.teacher.data.model.request.TeacherDasboardSummaryResModel;
import com.auro.application.teacher.data.model.request.TeacherReqModel;
import com.auro.application.teacher.data.model.request.TeacherUserIdReq;
import com.auro.application.teacher.data.model.response.AvailableSlotListResModel;
import com.auro.application.teacher.data.model.response.BookedSlotListResModel;
import com.auro.application.teacher.data.model.response.MyClassRoomResModel;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.data.model.response.TeacherClassRoomResModel;
import com.auro.application.teacher.data.model.response.TeacherCreateGroupResModel;
import com.auro.application.teacher.data.model.response.TeacherKycStatusResModel;
import com.auro.application.teacher.data.model.response.TeacherProgressModel;
import com.auro.application.teacher.data.model.response.TeacherResModel;
import com.auro.application.teacher.data.model.response.ZohoAppointmentListModel;
import com.auro.application.teacher.data.repository.TeacherRepo;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;

import retrofit2.Response;

import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_200;
import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_400;
import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_401;
import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_FAIL;
import static com.auro.application.core.common.Status.CANCEL_WEBINAR_SLOT;
import static com.auro.application.core.common.Status.DYNAMIC_LINK_API;

import androidx.annotation.NonNull;


public class TeacherRemoteUseCase extends NetworkUseCase {
    TeacherRepo.TeacherRemoteData teacherRemoteData;
    Gson gson = new Gson();

    public TeacherRemoteUseCase(TeacherRepo.TeacherRemoteData teacherRemoteData) {
        this.teacherRemoteData = teacherRemoteData;
    }

    public Single<ResponseApi> sendInviteApi(SendInviteNotificationReqModel reqModel) {
        return teacherRemoteData.sendInviteNotificationApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {


                    return handleResponse(response, Status.SEND_INVITE_API);


                } else {

                    return responseFail(Status.SEND_INVITE_API);
                }
            }
        });
    }

    public Single<ResponseApi> updateTeacherProfileApi(TeacherReqModel reqModel) {
        AppLogger.v("InfoScreen", "Step 4 update teacherremoteusecase");
        return teacherRemoteData.updateTeacherProfileApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, Status.UPDATE_TEACHER_PROFILE_API);

                } else {

                    return responseFail(Status.UPDATE_TEACHER_PROFILE_API);
                }
            }
        });
    }

    public Single<ResponseApi> getProfileTeacherApi(String userId) {

        return teacherRemoteData.getProfileTeacherApi(userId).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    AppLogger.v("GetProfiler","Step - 3 -- ");
                    return handleResponse(response, Status.GET_PROFILE_TEACHER_API);

                } else {

                    AppLogger.v("GetProfiler","Step - 4 -- ");
                    return responseFail(Status.GET_PROFILE_TEACHER_API);
                }
            }
        });
    }


    public Single<ResponseApi> getDynamicDataApi(DynamiclinkResModel model) {
        AppLogger.e("callRefferApi","step 6");
        return teacherRemoteData.getDynamicDataApi(model).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, DYNAMIC_LINK_API);
                } else {
                    return responseFail(DYNAMIC_LINK_API);
                }
            }
        });
    }


    public Single<ResponseApi> getZohoAppointments() {

        return teacherRemoteData.getZohoAppointments().map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, Status.GET_ZOHO_APPOINTMENT);

                } else {

                    return responseFail(Status.GET_ZOHO_APPOINTMENT);
                }
            }
        });
    }

    public Single<ResponseApi> bookZohoAppointments(String from_time, String name, String email, String phone_number) {

        return teacherRemoteData.bookZohoAppointments(from_time, name, email, phone_number).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, Status.GET_ZOHO_APPOINTMENT);

                } else {

                    return responseFail(Status.GET_ZOHO_APPOINTMENT);
                }
            }
        });
    }

    public Single<ResponseApi> uploadTeacherKYC(List<KYCDocumentDatamodel> list) {

        return teacherRemoteData.uploadTeacherKYC(list).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, Status.TEACHER_KYC_API);

                } else {

                    return responseFail(Status.TEACHER_KYC_API);
                }
            }
        });
    }

    public Single<ResponseApi> getTeacherDashboardApi(AuroScholarDataModel auroScholarDataModel) {

        return teacherRemoteData.getTeacherDashboardApi(auroScholarDataModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, Status.GET_TEACHER_DASHBOARD_API);

                } else {

                    return responseFail(Status.GET_TEACHER_DASHBOARD_API);
                }
            }
        });
    }

    public Single<ResponseApi> getTeacherKycStatusApi(FetchStudentPrefReqModel reqmodel) {

        return teacherRemoteData.getTeacherKycStatusApi(reqmodel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, Status.TEACHER_KYC_STATUS_API);

                } else {

                    return responseFail(Status.TEACHER_KYC_STATUS_API);
                }
            }
        });
    }

    public Single<ResponseApi> getTeacherProgressApi(String mobileNumber) {

        return teacherRemoteData.getTeacherProgressApi(mobileNumber).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, Status.GET_TEACHER_PROGRESS_API);

                } else {

                    return responseFail(null);
                }
            }
        });
    }



    /* New Teacher Flow Apis */

    public Single<ResponseApi> availableWebinarSlots(AvailableSlotsReqModel reqModel) {

        return teacherRemoteData.availableWebinarSlots(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {
                AppLogger.e("available_slot_api","step 7");
                if (response != null) {
                    return handleResponse(response, Status.AVAILABLE_WEBINAR_SLOTS);

                } else {

                    return responseFail(null);
                }
            }
        });
    }


    public Single<ResponseApi> addGroup(AddGroupReqModel reqModel) {

        return teacherRemoteData.addGroup(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, Status.ADD_GROUP);

                } else {

                    return responseFail(null);
                }
            }
        });
    }


    public Single<ResponseApi> addStudentInGroup(AddStudentGroupReqModel reqModel) {

        return teacherRemoteData.addStudentInGroup(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, Status.ADD_STUDENT_IN_GROUP);

                } else {

                    return responseFail(null);
                }
            }
        });
    }

    public Single<ResponseApi> deleteStudentInGroup(DeleteStudentGroupReqModel reqModel) {

        return teacherRemoteData.deleteStudentFromGroup(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, Status.DELETE_STUDENT_FROM_GROUP);

                } else {

                    return responseFail(null);
                }
            }
        });
    }

    public Single<ResponseApi> teacherBookedSlotApi(BookedSlotReqModel reqModel) {

        return teacherRemoteData.teacherBookedSlotApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, Status.TEACHER_BOOKED_SLOT_API);

                } else {

                    return responseFail(null);
                }
            }
        });
    }

    public Single<ResponseApi> teacherAvailableSlotsApi(BookedSlotReqModel reqModel) {

        return teacherRemoteData.teacherAvailableSlotsApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {
                if (response != null) {
                    AppLogger.e("available_slot_api","step 6");
                    return handleResponse(response, Status.TEACHER_AVAILABLE_SLOTS_API);
                } else {
                    AppLogger.e("available_slot_api","step 7");
                    return responseFail(null);
                }
            }
        });
    }


    public Single<ResponseApi> teacherCreateGroupApi(TeacherCreateGroupReqModel reqModel) {

        return teacherRemoteData.teacherCreateGroupApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    AppLogger.v("InfoScreen", " callTeacher  Single<ResponseApi>");
                    return handleResponse(response, Status.TEACHER_CREATE_GROUP_API);

                } else {
                    AppLogger.v("InfoScreen", " callTeacher  Single<ResponseApi> fail");
                    return responseFail(Status.TEACHER_CREATE_GROUP_API);
                }
            }
        });
    }

    public Single<ResponseApi> teacherAddStudentInGroupApi(TeacherAddStudentInGroupReqModel reqModel) {

        return teacherRemoteData.teacherAddStudentInGroupApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, Status.TEACHER_ADD_STUDENT_IN_GROUP_API);

                } else {

                    return responseFail(null);
                }
            }
        });
    }


    public Single<ResponseApi> bookSlotApi(BookSlotReqModel reqModel) {

        return teacherRemoteData.bookSlotApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, Status.BOOK_SLOT_API);

                } else {

                    return responseFail(null);
                }
            }
        });
    }


    public Single<ResponseApi> cancelSlotApi(CancelWebinarSlot reqModel){
        return teacherRemoteData.cancelSlotApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    AppLogger.v("CANCELDIALOG","Step 3 ");
                    return handleResponse(response, CANCEL_WEBINAR_SLOT);

                } else {
                    AppLogger.e("cancelSlotApi","step 3.1--");
                    return responseFail(CANCEL_WEBINAR_SLOT);
                }
            }
        });
    }





    public Single<ResponseApi> teacherDashboardSummary(TeacherUserIdReq teacherUserIdReq) {
        AppLogger.e("SummaryData", "Step 4");
        return teacherRemoteData.teacherDashboardSummary(teacherUserIdReq).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(@NonNull Response<JsonObject> response) throws Exception {
                AppLogger.e("SummaryData", "Step 5");
                if (response != null) {
                    return handleResponse(response, Status.TEACHER_DASBOARD_SUMMARY);
                } else {
                    return responseFail(Status.TEACHER_DASBOARD_SUMMARY);
                }
            }
        });
    }

    public Single<ResponseApi> teacherClassRoom(TeacherUserIdReq teacherUserIdReq) {
        return teacherRemoteData.teacherClassroom(teacherUserIdReq).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(@NonNull Response<JsonObject> response) throws Exception {
                if (response != null) {
                    return handleResponse(response, Status.TEACHER_CLASSROOM);
                } else {
                    return responseFail( Status.TEACHER_CLASSROOM);
                }
            }
        });
    }


    private ResponseApi handleResponse(Response<JsonObject> response, Status apiTypeStatus) {

        switch (response.code()) {

            case RES_200:
                return response200(response, apiTypeStatus);

            case RES_401:
                return response401(apiTypeStatus);

            case RES_400:
                return responseFail400(response, apiTypeStatus);

            case RES_FAIL:
                return responseFail(apiTypeStatus);

            default:
                return ResponseApi.fail(AuroApp.getAppContext().getResources().getString(R.string.default_error), apiTypeStatus);
        }
    }

    @Override
    public Single<Boolean> isAvailInternet() {
        return NetworkUtil.hasInternetConnection();
    }

    @Override
    public ResponseApi response200(Response<JsonObject> response, Status status) {
        if (AuroApp.getAuroScholarModel() != null && AuroApp.getAuroScholarModel().getSdkcallback() != null) {
            String jsonString = new Gson().toJson(response.body());
            AuroApp.getAuroScholarModel().getSdkcallback().callBack(jsonString);
        }
        AppLogger.v("InfoScreen", " Step 5  response200  remote usecase");
        switch (status) {

            case UPDATE_TEACHER_PROFILE_API:
                MyProfileResModel resModelnew = gson.fromJson(response.body(), MyProfileResModel.class);

                AppLogger.v("InfoScreen", " Step 6  MyProfileResModel");
                return ResponseApi.success(resModelnew, status);
               // TeacherResModel teacherResModel = gson.fromJson(response.body(), TeacherResModel.class);
              /*  if (!teacherResModel.getError()) {
                    return ResponseApi.success(teacherResModel, status);
                } else {
                    return ResponseApi.fail(teacherResModel.getMessage(), status);
                }*/

            case GET_TEACHER_DASHBOARD_API:
                MyClassRoomResModel myClassRoomResModel = gson.fromJson(response.body(), MyClassRoomResModel.class);
                return ResponseApi.success(myClassRoomResModel, status);

            case GET_PROFILE_TEACHER_API:
                MyProfileResModel resModel = gson.fromJson(response.body(), MyProfileResModel.class);
                AppLogger.v("GetProfiler","Step - 5 -- "+status);

                return ResponseApi.success(resModel, status);

            case GET_TEACHER_PROGRESS_API:
                TeacherProgressModel teacherProgressModel = gson.fromJson(response.body(), TeacherProgressModel.class);
                return ResponseApi.success(teacherProgressModel, status);

            case GET_ZOHO_APPOINTMENT:
                ZohoAppointmentListModel zohoAppointmentListModel = gson.fromJson(response.body(), ZohoAppointmentListModel.class);
                return ResponseApi.success(zohoAppointmentListModel, status);

            case TEACHER_KYC_API:
                KYCResListModel list = new Gson().fromJson(response.body(), KYCResListModel.class);
                return ResponseApi.success(list, status);

            case SEND_INVITE_API:
                TeacherResModel teacherResModel1 = new Gson().fromJson(response.body(), TeacherResModel.class);
                return ResponseApi.success(teacherResModel1, status);

            case TEACHER_DASBOARD_SUMMARY:
                TeacherDasboardSummaryResModel model = new Gson().fromJson(response.body(), TeacherDasboardSummaryResModel.class);
                return ResponseApi.success(model, status);
            case TEACHER_CLASSROOM:
                TeacherClassRoomResModel teacherClassRoomResModel = new Gson().fromJson(response.body(), TeacherClassRoomResModel.class);
                return ResponseApi.success(teacherClassRoomResModel, status);

            case TEACHER_BOOKED_SLOT_API:
                BookedSlotListResModel bookedSlotListResModel = new Gson().fromJson(new Gson().toJson(response.body()), BookedSlotListResModel.class);
                return ResponseApi.success(bookedSlotListResModel, status);

            case TEACHER_AVAILABLE_SLOTS_API:
                AvailableSlotListResModel availableSlotListResModel = new Gson().fromJson(new Gson().toJson(response.body()), AvailableSlotListResModel.class);
                return ResponseApi.success(availableSlotListResModel, status);

            case TEACHER_CREATE_GROUP_API:
                AppLogger.v("InfoScreen", " callTeacher  apiDevelop");
                TeacherCreateGroupResModel teacherCreateGroupResModel = new Gson().fromJson(new Gson().toJson(response.body()), TeacherCreateGroupResModel.class);
                AppLogger.v("InfoScreen", " callTeacher  apiDevelop tester");
                return ResponseApi.success(teacherCreateGroupResModel, status);

            case TEACHER_ADD_STUDENT_IN_GROUP_API:
                try {
                    AppLogger.v("InfoScreen", " callTeacher step 1");
                    CommonDataResModel teacherAddStudentInGroup = new Gson().fromJson(response.body(), CommonDataResModel.class);
                    AppLogger.v("InfoScreen", " callTeacher -" + teacherAddStudentInGroup.getMessage());
                    return ResponseApi.success(teacherAddStudentInGroup, status);
                } catch (Exception e) {
                    AppLogger.v("InfoScreen", " step fail in CreateGroup  TEACHER_ADD_STUDENT_IN_GROUP_API"+ e.getMessage());
                    AppLogger.v("InfoScreen", " callTeacherstep 2-" + e.getMessage());
                    return ResponseApi.fail(null, status);
                }

            case BOOK_SLOT_API:
                CommonDataResModel commonDataResModel = new Gson().fromJson(response.body(), CommonDataResModel.class);
                return ResponseApi.success(commonDataResModel, status);


            case TEACHER_KYC_STATUS_API:
                TeacherKycStatusResModel teacherKycStatusResModel = new Gson().fromJson(response.body(), TeacherKycStatusResModel.class);
                return ResponseApi.success(teacherKycStatusResModel, status);


            case CANCEL_WEBINAR_SLOT:
                CommonDataResModel commonDataCancelResponse = new Gson().fromJson(response.body(), CommonDataResModel.class);
                AppLogger.v("CANCELDIALOG","Step 4 ");
                return ResponseApi.success(commonDataCancelResponse,status);

            case DYNAMIC_LINK_API:
                DynamiclinkResModel dynamiclinkResModel = new Gson().fromJson(response.body(), DynamiclinkResModel.class);
                AppLogger.v("DYNAMIC_LINK_API","Step 4 ");
                return ResponseApi.success(dynamiclinkResModel,status);

        }

        AppLogger.v("GetProfiler","Step - 5.2 -- "+status);
        AppLogger.v("InfoScreen", " callTeacher  Status Step 1"+ status);
        return ResponseApi.fail(null, status);
    }

    @Override
    public ResponseApi response401(Status status) {
        return ResponseApi.authFail(401, status);
    }

    @Override
    public ResponseApi responseFail400(Response<JsonObject> response, Status status) {
        try {
            String errorJson = response.errorBody().string();
            String errorMessage = AppUtil.errorMessageHandler(AuroApp.getAppContext().getResources().getString(R.string.default_error), errorJson);
            return ResponseApi.fail400(errorMessage, null);
        } catch (Exception e) {
            return ResponseApi.fail(AuroApp.getAppContext().getResources().getString(R.string.default_error), status);
        }
    }


    @Override
    public ResponseApi responseFail(Status status) {
        return ResponseApi.fail(AuroApp.getAppContext().getResources().getString(R.string.default_error), status);
    }

}

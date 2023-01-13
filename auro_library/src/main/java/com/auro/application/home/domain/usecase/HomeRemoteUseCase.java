package com.auro.application.home.domain.usecase;


import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.NetworkUtil;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.network.ErrorResponseModel;
import com.auro.application.core.network.NetworkUseCase;
import com.auro.application.home.data.model.AcceptInviteRequest;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.AssignmentResModel;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.AzureResModel;
import com.auro.application.home.data.model.ChallengeAccepResModel;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.DemographicResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.FriendListResDataModel;
import com.auro.application.home.data.model.FriendRequestList;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.data.model.KYCInputModel;
import com.auro.application.home.data.model.KYCResListModel;
import com.auro.application.home.data.model.LanguageMasterReqModel;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.NearByFriendList;
import com.auro.application.home.data.model.OtpOverCallReqModel;
import com.auro.application.home.data.model.PartnersLoginReqModel;
import com.auro.application.home.data.model.PendingKycDocsModel;
import com.auro.application.home.data.model.RefferalReqModel;
import com.auro.application.home.data.model.SaveImageReqModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.SetPasswordReqModel;
import com.auro.application.home.data.model.UpdatePrefReqModel;
import com.auro.application.home.data.model.VerifyOtpReqModel;
import com.auro.application.home.data.model.partnersmodel.PartnerLoginResModel;
import com.auro.application.home.data.model.partnersmodel.PartnerResModel;
import com.auro.application.home.data.model.passportmodels.PassportMonthModel;
import com.auro.application.home.data.model.passportmodels.PassportReqModel;
import com.auro.application.home.data.model.response.CertificateResModel;
import com.auro.application.home.data.model.response.ChangeGradeResModel;
import com.auro.application.home.data.model.response.CheckUserValidResModel;
import com.auro.application.home.data.model.response.CheckVerResModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.data.model.response.FetchStudentPrefResModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.InstructionsResModel;
import com.auro.application.home.data.model.response.LanguageListResModel;
import com.auro.application.home.data.model.response.NoticeInstruction;
import com.auro.application.home.data.model.response.OtpOverCallResModel;
import com.auro.application.home.data.model.response.SendOtpResModel;
import com.auro.application.home.data.model.response.ShowDialogModel;
import com.auro.application.home.data.model.response.SlabsResModel;
import com.auro.application.home.data.model.response.StudentKycStatusResModel;
import com.auro.application.home.data.model.response.StudentWalletResModel;
import com.auro.application.home.data.model.response.SubjectPreferenceResModel;
import com.auro.application.home.data.model.response.UpdatePrefResModel;
import com.auro.application.home.data.model.response.SetPasswordResModel;
import com.auro.application.home.data.model.response.VerifyOtpResModel;
import com.auro.application.home.data.model.signupmodel.InstructionModel;
import com.auro.application.home.data.model.signupmodel.UserSlabsRequest;
import com.auro.application.home.data.model.signupmodel.InstructionModel;
import com.auro.application.home.data.model.signupmodel.request.LoginReqModel;
import com.auro.application.home.data.model.signupmodel.request.RegisterApiReqModel;
import com.auro.application.home.data.model.signupmodel.request.SetUsernamePinReqModel;
import com.auro.application.home.data.model.signupmodel.response.LoginResModel;
import com.auro.application.home.data.model.signupmodel.response.RegisterApiResModel;
import com.auro.application.home.data.model.signupmodel.response.SetUsernamePinResModel;
import com.auro.application.home.data.repository.HomeRepo;
import com.auro.application.teacher.data.model.request.SendInviteNotificationReqModel;
import com.auro.application.teacher.data.model.response.MyClassRoomResModel;
import com.auro.application.teacher.data.model.response.TeacherResModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.TextUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.Response;

import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_200;
import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_400;
import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_401;
import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_FAIL;
import static com.auro.application.core.common.Status.ACCEPT_INVITE_CLICK;
import static com.auro.application.core.common.Status.ACCEPT_INVITE_REQUEST;
import static com.auro.application.core.common.Status.ASSIGNMENT_STUDENT_DATA_API;
import static com.auro.application.core.common.Status.AZURE_API;
import static com.auro.application.core.common.Status.GET_INSTRUCTIONS_API;
import static com.auro.application.core.common.Status.GET_MESSAGE_POP_UP;
import static com.auro.application.core.common.Status.NOTICE_INSTRUCTION;
import static com.auro.application.core.common.Status.GET_SLABS_API;
import static com.auro.application.core.common.Status.OTP_OVER_CALL;
import static com.auro.application.core.common.Status.CERTIFICATE_API;
import static com.auro.application.core.common.Status.CHANGE_GRADE;
import static com.auro.application.core.common.Status.CHECKVALIDUSER;
import static com.auro.application.core.common.Status.DASHBOARD_API;
import static com.auro.application.core.common.Status.DEMOGRAPHIC_API;
import static com.auro.application.core.common.Status.DYNAMIC_LANGUAGE;
import static com.auro.application.core.common.Status.DYNAMIC_LINK_API;
import static com.auro.application.core.common.Status.FETCH_STUDENT_PREFERENCES_API;
import static com.auro.application.core.common.Status.FIND_FRIEND_DATA;
import static com.auro.application.core.common.Status.FRIENDS_REQUEST_LIST;
import static com.auro.application.core.common.Status.GET_TEACHER_DASHBOARD_API;
import static com.auro.application.core.common.Status.GET_USER_PROFILE_DATA;
import static com.auro.application.core.common.Status.GRADE_UPGRADE;
import static com.auro.application.core.common.Status.INVITE_FRIENDS_LIST;
import static com.auro.application.core.common.Status.LANGUAGE_LIST;
import static com.auro.application.core.common.Status.LOGIN_API;
import static com.auro.application.core.common.Status.LOGIN_PIN_API;
import static com.auro.application.core.common.Status.PARTNERS_API;
import static com.auro.application.core.common.Status.PARTNERS_LOGIN_API;
import static com.auro.application.core.common.Status.PASSPORT_API;
import static com.auro.application.core.common.Status.PENDING_KYC_DOCS;
import static com.auro.application.core.common.Status.REFFERAL_API;
import static com.auro.application.core.common.Status.REGISTER_API;
import static com.auro.application.core.common.Status.SEND_FRIENDS_REQUEST;
import static com.auro.application.core.common.Status.SEND_INVITE_API;
import static com.auro.application.core.common.Status.SEND_OTP;
import static com.auro.application.core.common.Status.SEND_REFERRAL_API;
import static com.auro.application.core.common.Status.SET_USERNAME_PIN_API;
import static com.auro.application.core.common.Status.SET_USER_PIN;
import static com.auro.application.core.common.Status.STATE;
import static com.auro.application.core.common.Status.STUDENT_KYC_STATUS_API;
import static com.auro.application.core.common.Status.SUBJECT_PREFRENCE_LIST_API;
import static com.auro.application.core.common.Status.UPDATE_PARENT;
import static com.auro.application.core.common.Status.UPDATE_PREFERENCE_API;
import static com.auro.application.core.common.Status.SET_PASSWORD;
import static com.auro.application.core.common.Status.UPDATE_STUDENT;
import static com.auro.application.core.common.Status.VERIFY_OTP;
import static com.auro.application.core.common.Status.VERSIONAPI;
import static com.auro.application.core.common.Status.WALLET_STATUS_API;

import android.util.Log;

import org.json.JSONObject;


public class HomeRemoteUseCase extends NetworkUseCase {

    HomeRepo.DashboardRemoteData dashboardRemoteData;
    Gson gson = new Gson();

    public HomeRemoteUseCase(HomeRepo.DashboardRemoteData dashboardRemoteData) {
        this.dashboardRemoteData = dashboardRemoteData;
    }


    public Single<ResponseApi> getWalletStatusApi(SetPasswordReqModel reqModel) {
        return dashboardRemoteData.getWalletStatusApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {


                    return handleResponse(response, WALLET_STATUS_API);


                } else {

                    return responseFail(WALLET_STATUS_API);
                }
            }
        });
    }

    public Single<ResponseApi> sendOtpApi(SendOtpReqModel reqModel) {
        return dashboardRemoteData.sendOtpHomeRepo(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {


                    return handleResponse(response, SEND_OTP);


                } else {

                    return responseFail(SEND_OTP);
                }
            }
        });
    }


    public Single<ResponseApi> checkUserApiVerify(CheckUserApiReqModel reqModel) {
        return dashboardRemoteData.checkUserValidApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {
                if (response != null) {

                    return handleResponse(response, CHECKVALIDUSER);

                } else {
                    return responseFail(CHECKVALIDUSER);
                }

            }
        });
    }

    public Single<ResponseApi> changeGradeApi(CheckUserValidResModel reqModel) {
        return dashboardRemoteData.changeGradeApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {
                if (response != null) {

                    return handleResponse(response, CHANGE_GRADE);

                } else {

                    return responseFail(CHANGE_GRADE);
                }

            }
        });
    }

    public Single<ResponseApi> getCertificateApi(CertificateResModel reqModel) {
        return dashboardRemoteData.getCertificateApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {
                if (response != null) {

                    return handleResponse(response, CERTIFICATE_API);

                } else {

                    return responseFail(null);
                }

            }
        });
    }

    public Single<ResponseApi> getPassportApi(PassportReqModel reqModel) {
        return dashboardRemoteData.passportApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {
                if (response != null) {
                    return handleResponse(response, PASSPORT_API);

                } else {

                    return responseFail(null);
                }

            }
        });
    }

    public Single<ResponseApi> getAssignmentId(AssignmentReqModel demographicResModel) {

        return dashboardRemoteData.getAssignmentId(demographicResModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, ASSIGNMENT_STUDENT_DATA_API);

                } else {

                    return responseFail(null);
                }
            }
        });
    }


    public Single<ResponseApi> sendInviteApi(SendInviteNotificationReqModel reqModel) {
        return dashboardRemoteData.sendInviteNotificationApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {


                    return handleResponse(response, SEND_INVITE_API);


                } else {

                    return responseFail(SEND_INVITE_API);
                }
            }
        });
    }

    public Single<ResponseApi> acceptInviteApi(SendInviteNotificationReqModel reqModel) {
        return dashboardRemoteData.acceptInviteApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, ACCEPT_INVITE_CLICK);
                } else {

                    return responseFail(ACCEPT_INVITE_CLICK);
                }
            }
        });
    }

    public Single<ResponseApi> findFriendApi(double lat, double longt, double radius) {
        return dashboardRemoteData.findFriendApi(lat, longt, radius).

                map(new Function<Response<JsonObject>, ResponseApi>() {
                    @Override
                    public ResponseApi apply(Response<JsonObject> response) throws Exception {

                        if (response != null) {
                            return handleResponse(response, FIND_FRIEND_DATA);
                        } else {

                            return responseFail(null);
                        }
                    }
                });
    }


    public Single<ResponseApi> inviteFriendListApi() {

        return dashboardRemoteData.inviteFriendListApi().map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, INVITE_FRIENDS_LIST);

                } else {

                    return responseFail(INVITE_FRIENDS_LIST);
                }
            }
        });
    }

    public Single<ResponseApi> sendFriendRequestApi(int requested_by_id, int requested_user_id, String requested_by_phone, String requested_user_phone) {

        return dashboardRemoteData.sendFriendRequestApi(requested_by_id, requested_user_id, requested_by_phone, requested_user_phone).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, SEND_FRIENDS_REQUEST);

                } else {

                    return responseFail(null);
                }
            }
        });
    }

    public Single<ResponseApi> friendRequestListApi(int requested_user_id) {

        return dashboardRemoteData.friendRequestListApi(requested_user_id).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, FRIENDS_REQUEST_LIST);

                } else {

                    return responseFail(null);
                }
            }
        });
    }

    public Single<ResponseApi> friendAcceptApi(int friend_request_id, String request_status) {

        return dashboardRemoteData.friendAcceptApi(friend_request_id, request_status).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, ACCEPT_INVITE_REQUEST);

                } else {

                    return responseFail(null);
                }
            }
        });
    }

    public Single<ResponseApi> getAzureData(AssignmentReqModel model) {

        return dashboardRemoteData.getAzureData(model).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, AZURE_API);

                } else {
                    return responseFail(AZURE_API);
                }

            }
        });
    }


    public Single<ResponseApi> getStudentKycStatus() {

        return dashboardRemoteData.getStudentKycStatus().map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, STUDENT_KYC_STATUS_API);

                } else {
                    return responseFail(STUDENT_KYC_STATUS_API);
                }

            }
        });
    }

    public Single<ResponseApi> parentUpdateProfile(String name, int stateid, int districtid, String gender,String emailid) {

        return dashboardRemoteData.parentUpdateProfile().map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, UPDATE_PARENT);

                } else {
                    return responseFail(UPDATE_PARENT);
                }

            }
        });
    }

    public Single<ResponseApi> uploadStudentExamImage(SaveImageReqModel params) {
        return dashboardRemoteData.uploadStudentExamImage(params).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, Status.UPLOAD_EXAM_FACE_API);
                } else {
                    return responseFail(Status.UPLOAD_EXAM_FACE_API);
                }
            }
        });
    }

    public Single<ResponseApi> getDashboardData(AuroScholarDataModel model) {
        AppLogger.e("onRefresh-", "Step 04");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel.getDeviceToken() != null && !TextUtil.isEmpty(prefModel.getDeviceToken())) {
            model.setDevicetoken(prefModel.getDeviceToken());
        } else {
            model.setDevicetoken("");

        }

        model.setUserId(prefModel.getStudentData().getUserId());
        return dashboardRemoteData.getDashboardData(model).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    AppLogger.e("onRefresh-", "Step 05");
                    return handleResponse(response, DASHBOARD_API);

                } else {

                    return responseFail(DASHBOARD_API);
                }
            }
        });
    }

    public Single<ResponseApi> postDemographicData(GetStudentUpdateProfile demographicResModel) {

        return dashboardRemoteData.studentUpdateProfile(demographicResModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, UPDATE_STUDENT);

                } else {

                    return responseFail(UPDATE_STUDENT);
                }
            }
        });
    }

    public Single<ResponseApi> verifyOtpApi(VerifyOtpReqModel reqModel) {
        AppLogger.v("OTP_MAIN", "Step 4");
        return dashboardRemoteData.verifyOtpHomeRepo(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    AppLogger.v("OTP_MAIN", "Step 5");
                    return handleResponse(response, VERIFY_OTP);


                } else {

                    return responseFail(null);
                }
            }
        });
    }

    public Single<ResponseApi> setPasswordApi(SetPasswordReqModel reqModel) {
        return dashboardRemoteData.forgotPassword(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {


                    return handleResponse(response, SET_PASSWORD);


                } else {

                    return responseFail(null);
                }
            }
        });
    }

    public Single<ResponseApi> loginApi(LoginReqModel reqModel) {
        return dashboardRemoteData.loginApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {


                    return handleResponse(response, LOGIN_API);


                } else {

                    return responseFail(null);
                }
            }
        });
    }

    public Single<ResponseApi> fetchStudentPreferenceApi(FetchStudentPrefReqModel reqModel) {
        return dashboardRemoteData.fetchStudentPreferenceApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, FETCH_STUDENT_PREFERENCES_API);
                } else {

                    return responseFail(null);
                }
            }
        });
    }


    public Single<ResponseApi> getVersionApiCheck() {

        return dashboardRemoteData.versionApiCheck().map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, VERSIONAPI);

                } else {

                    return responseFail(null);
                }
            }
        });
    }

    public Single<ResponseApi> getState() {

        return dashboardRemoteData.state().map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, STATE);

                } else {

                    return responseFail(STATE);
                }
            }
        });
    }


    public Single<ResponseApi> getLanguageList() {

        return dashboardRemoteData.getLanguageList().map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, LANGUAGE_LIST);
                } else {
                    return responseFail(LANGUAGE_LIST);
                }
            }
        });
    }


    public Single<ResponseApi> uploadProfileImage(List<KYCDocumentDatamodel> list, KYCInputModel kycInputModel) {
        AppLogger.e("chhonker uploadAllDocApi", "step 6");
        return dashboardRemoteData.uploadProfileImage(list, kycInputModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    AppLogger.e("chhonker uploadAllDocApi", "step 7");
                    return handleResponse(response, Status.UPLOAD_PROFILE_IMAGE);

                } else {
                    AppLogger.e("chhonker uploadAllDocApi", "step 8");
                    return responseFail(Status.UPLOAD_PROFILE_IMAGE);
                }
            }
        });
    }

    public Single<ResponseApi> uploadStudentProfile(GetStudentUpdateProfile studentUpdateProfile) {
        AppLogger.e("update profile api-", "step 6");
        return dashboardRemoteData.studentUpdateProfile(studentUpdateProfile).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    AppLogger.e("update profile api-", "step 7");
                    return handleResponse(response, UPDATE_STUDENT);

                } else {
                    AppLogger.e("update profile api-", "step 8");
                    return responseFail(UPDATE_STUDENT);
                }
            }
        });
    }

    public Single<ResponseApi> getUserProfileData(String userId) {

        return dashboardRemoteData.getUserProfile(userId).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {

                    return handleResponse(response, GET_USER_PROFILE_DATA);

                } else {

                    return responseFail(GET_USER_PROFILE_DATA);
                }
            }
        });
    }

    public Single<ResponseApi> upgradeStudentGrade(AuroScholarDataModel model) {

        return dashboardRemoteData.upgradeClass(model).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, GRADE_UPGRADE);
                } else {
                    return responseFail(GRADE_UPGRADE);
                }
            }
        });
    }

    public Single<ResponseApi> partnersApi() {

        return dashboardRemoteData.partnersApi().map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, PARTNERS_API);
                } else {
                    return responseFail(PARTNERS_API);
                }
            }
        });
    }

    public Single<ResponseApi> partnersLoginApi(PartnersLoginReqModel reqModel) {

        return dashboardRemoteData.partnersLoginApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, PARTNERS_LOGIN_API);
                } else {
                    return responseFail(PARTNERS_LOGIN_API);
                }
            }
        });
    }

    public Single<ResponseApi> getDynamicDataApi(DynamiclinkResModel model) {
        AppLogger.e("callRefferApi", "step 4" + new Gson().toJson(model));

        return dashboardRemoteData.getDynamicDataApi(model).map(new Function<Response<JsonObject>, ResponseApi>() {
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

    public Single<ResponseApi> sendRefferalDataApi(RefferalReqModel model) {
        AppLogger.e("SEND_REFERRAL_API", "step 4");
        return dashboardRemoteData.sendRefferalDataApi(model).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    AppLogger.e("SEND_REFERRAL_API", "step 5");
                    return handleResponse(response, SEND_REFERRAL_API);
                } else {
                    AppLogger.e("SEND_REFERRAL_API", "step 6");
                    return responseFail(SEND_REFERRAL_API);
                }
            }
        });
    }

    public Single<ResponseApi> getTeacherDashboardApi(AuroScholarDataModel auroScholarDataModel) {

        return dashboardRemoteData.getTeacherDashboardApi(auroScholarDataModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {
                if (response != null) {
                    return handleResponse(response, GET_TEACHER_DASHBOARD_API);
                } else {
                    return responseFail(GET_TEACHER_DASHBOARD_API);
                }
            }
        });
    }

    public Single<ResponseApi> preferenceSubjectList() {

        return dashboardRemoteData.preferenceSubjectList().map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, SUBJECT_PREFRENCE_LIST_API);
                } else {
                    return responseFail(SUBJECT_PREFRENCE_LIST_API);
                }
            }
        });
    }


    public Single<ResponseApi> updateStudentPreference(UpdatePrefReqModel reqModel) {

        return dashboardRemoteData.updateStudentPreference(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, UPDATE_PREFERENCE_API);
                } else {
                    return responseFail(UPDATE_PREFERENCE_API);
                }
            }
        });
    }


    public Single<ResponseApi> registerApi(RegisterApiReqModel reqModel) {

        return dashboardRemoteData.registerApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, REGISTER_API);
                } else {
                    return responseFail(REGISTER_API);
                }
            }
        });
    }

    public Single<ResponseApi> otpOverCall(OtpOverCallReqModel reqModel) {

        return dashboardRemoteData.optOverCall(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, OTP_OVER_CALL);
                } else {
                    return responseFail(OTP_OVER_CALL);
                }
            }
        });
    }


    public Single<ResponseApi> getInstructionsApi(InstructionModel id) {

        return dashboardRemoteData.getInstructionsApi(id).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, GET_INSTRUCTIONS_API);
                } else {
                    return responseFail(GET_INSTRUCTIONS_API);
                }
            }
        });
    }

    public Single<ResponseApi> getSlabsApi(UserSlabsRequest id) {

        return dashboardRemoteData.getSlabsApi(id).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, GET_SLABS_API);
                } else {
                    return responseFail(GET_SLABS_API);
                }
            }
        });
    }


    public Single<ResponseApi> setUsernamePassword(SetUsernamePinReqModel reqModel) {

        return dashboardRemoteData.setUsernamePinApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, SET_USERNAME_PIN_API);
                } else {
                    return responseFail(SET_USERNAME_PIN_API);
                }
            }
        });
    }


    public Single<ResponseApi> loginUsingPinApi(SetUsernamePinReqModel reqModel) {

        return dashboardRemoteData.loginUsingPinApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, LOGIN_PIN_API);
                } else {
                    return responseFail(LOGIN_PIN_API);
                }
            }
        });
    }

    public Single<ResponseApi> setUserPinApi(SetUsernamePinReqModel reqModel) {

        return dashboardRemoteData.setUserPinApi(reqModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, SET_USER_PIN);
                } else {
                    return responseFail(SET_USER_PIN);
                }
            }
        });
    }




    public Single<ResponseApi> getNoticeInstruction() {

        return dashboardRemoteData.getNoticeInstruction().map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, NOTICE_INSTRUCTION);
                } else {
                    return responseFail(NOTICE_INSTRUCTION);
                }
            }
        });
    }


    public Single<ResponseApi> getLanguageDynamic(LanguageMasterReqModel language) {
        AppLogger.v("Language_pradeep", "Dynamic language Step 4");
        return dashboardRemoteData.getDynamicLang(language).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(@NonNull Response<JsonObject> response) throws Exception {
                if (response != null) {
                    return handleResponse(response, DYNAMIC_LANGUAGE);
                } else {
                    return responseFail(DYNAMIC_LANGUAGE);
                }
            }
        });
    }


    public Single<ResponseApi> pendingKycDocs(PendingKycDocsModel pendingKycDocsModel) {

        return dashboardRemoteData.pendingKycDocs(pendingKycDocsModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, PENDING_KYC_DOCS);
                } else {
                    return responseFail(PENDING_KYC_DOCS);
                }
            }
        });
    }


    public Single<ResponseApi> getMsgPopUp(PendingKycDocsModel pendingKycDocsModel) {
        AppLogger.v("UserIdPradeep", "UserId"+pendingKycDocsModel.getUserId());
        return dashboardRemoteData.getMsgPopUp(pendingKycDocsModel).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, GET_MESSAGE_POP_UP);
                } else {
                    return responseFail(GET_MESSAGE_POP_UP);
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
                try {
                    Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
                    return ResponseApi.fail(details.getDefaultError(), apiTypeStatus);
                } catch (Exception e) {
                    return ResponseApi.fail(AuroApp.getAppContext().getString(R.string.default_error), apiTypeStatus);
                }
        }
    }

    @Override
    public Single<Boolean> isAvailInternet() {
        return NetworkUtil.hasInternetConnection();
    }

    @Override
    public ResponseApi response200(Response<JsonObject> response, Status status) {

        if (status == SEND_OTP) {
            SendOtpResModel sendOtpResModel = gson.fromJson(response.body(), SendOtpResModel.class);
            return ResponseApi.success(sendOtpResModel, status);
        } else if (status == DASHBOARD_API) {
            DashboardResModel dashboardResModel = gson.fromJson(response.body(), DashboardResModel.class);
            return ResponseApi.success(dashboardResModel, status);
        } else if (status == Status.UPLOAD_PROFILE_IMAGE) {
            KYCResListModel list = new Gson().fromJson(response.body(), KYCResListModel.class);
            return ResponseApi.success(list, status);
        } else if (status == DEMOGRAPHIC_API) {
            DemographicResModel demographicResModel = new Gson().fromJson(response.body(), DemographicResModel.class);
            return ResponseApi.success(demographicResModel, status);
        } else if (status == ASSIGNMENT_STUDENT_DATA_API) {
            AssignmentResModel assignmentResModel = new Gson().fromJson(response.body(), AssignmentResModel.class);
            return ResponseApi.success(assignmentResModel, status);
        } else if (status == AZURE_API) {
            AzureResModel azureResModel = new Gson().fromJson(response.body(), AzureResModel.class);
            return ResponseApi.success(azureResModel, status);
        } else if (status == INVITE_FRIENDS_LIST) {
            FriendListResDataModel resDataModel = new Gson().fromJson(response.body(), FriendListResDataModel.class);
            return ResponseApi.success(resDataModel, status);
        } else if (status == SEND_INVITE_API) {
            TeacherResModel teacherResModel1 = new Gson().fromJson(response.body(), TeacherResModel.class);
            return ResponseApi.success(teacherResModel1, status);
        } else if (status == ACCEPT_INVITE_CLICK) {
            ChallengeAccepResModel resModel = new Gson().fromJson(response.body(), ChallengeAccepResModel.class);
            return ResponseApi.success(resModel, status);
        } else if (status == GRADE_UPGRADE) {
            DashboardResModel dashboardResModel = gson.fromJson(response.body(), DashboardResModel.class);
            return ResponseApi.success(dashboardResModel, status);
        } else if (status == FIND_FRIEND_DATA) {
            NearByFriendList nearByFriendList = new Gson().fromJson(response.body(), NearByFriendList.class);
            return ResponseApi.success(nearByFriendList, status);
        } else if (status == SEND_FRIENDS_REQUEST) {
            NearByFriendList nearByFriendList = new Gson().fromJson(response.body(), NearByFriendList.class);
            return ResponseApi.success(nearByFriendList, status);
        } else if (status == FRIENDS_REQUEST_LIST) {
            FriendRequestList friendRequestList = new Gson().fromJson(response.body(), FriendRequestList.class);
            return ResponseApi.success(friendRequestList, status);
        } else if (status == ACCEPT_INVITE_REQUEST) {
            AcceptInviteRequest acceptInviteRequest = new Gson().fromJson(response.body(), AcceptInviteRequest.class);
            return ResponseApi.success(acceptInviteRequest, status);
        } else if (status == CHECKVALIDUSER) {
            CheckUserResModel checkUserValidResModel = gson.fromJson(response.body(), CheckUserResModel.class);
            return ResponseApi.success(checkUserValidResModel, status);
        } else if (status == CHANGE_GRADE) {
            ChangeGradeResModel changeGradeResModel = gson.fromJson(response.body(), ChangeGradeResModel.class);
            return ResponseApi.success(changeGradeResModel, status);
        } else if (status == VERIFY_OTP) {
            AppLogger.v("OTP_MAIN", "Step 6");
            VerifyOtpResModel verifyOtpResModel = gson.fromJson(response.body(), VerifyOtpResModel.class);
            return ResponseApi.success(verifyOtpResModel, status);
        } else if (status == VERSIONAPI) {
            CheckVerResModel checkVerResModel = gson.fromJson(response.body(), CheckVerResModel.class);
            return ResponseApi.success(checkVerResModel, status);
        } else if (status == DYNAMIC_LINK_API) {
            DynamiclinkResModel dynamiclinkResModel = gson.fromJson(response.body(), DynamiclinkResModel.class);
            return ResponseApi.success(dynamiclinkResModel, status);
        } else if (status == CERTIFICATE_API) {
            CertificateResModel certificateResModel = gson.fromJson(response.body(), CertificateResModel.class);
            return ResponseApi.success(certificateResModel, status);
        } else if (status == UPDATE_STUDENT) {
            GetStudentUpdateProfile getStudentUpdateProfile = gson.fromJson(response.body(), GetStudentUpdateProfile.class);
            AppLogger.v("RemoteApi", "" + UPDATE_STUDENT);
            return ResponseApi.success(getStudentUpdateProfile, status);
        } else if (status == PASSPORT_API)
        {
            Log.e("test","..........................................");
            PassportMonthModel passportMonthModel = gson.fromJson(response.body(), PassportMonthModel.class);
            AppLogger.v("RemoteApi", "" + PASSPORT_API);
            return ResponseApi.success(passportMonthModel, status);
        } else if (status == PARTNERS_API) {
            PartnerResModel partnersResModel = gson.fromJson(response.body(), PartnerResModel.class);
            AppLogger.v("RemoteApi", "" + PARTNERS_API);
            return ResponseApi.success(partnersResModel, status);
        } else if (status == PARTNERS_LOGIN_API) {
            PartnerLoginResModel partnerLoginResModel = gson.fromJson(response.body(), PartnerLoginResModel.class);
            AppLogger.v("RemoteApi", "" + PARTNERS_LOGIN_API);
            return ResponseApi.success(partnerLoginResModel, status);
        } else if (status == SET_PASSWORD) {
            SetPasswordResModel setPasswordResModel = gson.fromJson(response.body(), SetPasswordResModel.class);
            AppLogger.v("RemoteApi", "" + SET_PASSWORD);
            return ResponseApi.success(setPasswordResModel, status);
        } else if (status == LOGIN_API) {
            LoginResModel loginResModel = gson.fromJson(response.body(), LoginResModel.class);
            AppLogger.v("RemoteApi", "" + LOGIN_API);
            return ResponseApi.success(loginResModel, status);
        } else if (status == GET_TEACHER_DASHBOARD_API) {
            MyClassRoomResModel myClassRoomResModel = gson.fromJson(response.body(), MyClassRoomResModel.class);
            return ResponseApi.success(myClassRoomResModel, status);
        } else if (status == SUBJECT_PREFRENCE_LIST_API) {
            SubjectPreferenceResModel subjectPreferenceResModel = gson.fromJson(response.body(), SubjectPreferenceResModel.class);
            AppLogger.v("RemoteApi", "" + SUBJECT_PREFRENCE_LIST_API);
            return ResponseApi.success(subjectPreferenceResModel, status);
        } else if (status == FETCH_STUDENT_PREFERENCES_API) {
            FetchStudentPrefResModel fetchStudentPrefResModel = gson.fromJson(response.body(), FetchStudentPrefResModel.class);
            AppLogger.v("RemoteApi", "" + FETCH_STUDENT_PREFERENCES_API);
            return ResponseApi.success(fetchStudentPrefResModel, status);
        } else if (status == UPDATE_PREFERENCE_API) {
            UpdatePrefResModel updatePrefResModel = gson.fromJson(response.body(), UpdatePrefResModel.class);
            AppLogger.v("RemoteApi", "" + UPDATE_PREFERENCE_API);
            return ResponseApi.success(updatePrefResModel, status);
        } else if (status == LANGUAGE_LIST) {
            LanguageListResModel languageListResModel = gson.fromJson(response.body(), LanguageListResModel.class);
            AppLogger.v("RemoteApi", "" + LANGUAGE_LIST);
            return ResponseApi.success(languageListResModel, status);
        }else if(status == NOTICE_INSTRUCTION){
            NoticeInstruction noticeInstruction = gson.fromJson(response.body(), NoticeInstruction.class);
            AppLogger.v("RemoteApi", "" + NOTICE_INSTRUCTION);
            return ResponseApi.success(noticeInstruction, status);
        }
        else if (status == GET_USER_PROFILE_DATA) {
            GetStudentUpdateProfile getStudentUpdateProfile = gson.fromJson(response.body(), GetStudentUpdateProfile.class);
            AppLogger.v("RemoteApi", "" + GET_USER_PROFILE_DATA);
            return ResponseApi.success(getStudentUpdateProfile, status);
        } else if (status == REGISTER_API) {
            RegisterApiResModel resModel = gson.fromJson(response.body(), RegisterApiResModel.class);
            AppLogger.v("RemoteApi", "" + REGISTER_API);
            return ResponseApi.success(resModel, status);
        } else if (status == SET_USERNAME_PIN_API) {
            SetUsernamePinResModel resModel = gson.fromJson(response.body(), SetUsernamePinResModel.class);
            AppLogger.v("RemoteApi", "" + SET_USERNAME_PIN_API);
            return ResponseApi.success(resModel, status);
        } else if (status == LOGIN_PIN_API) {
            SetUsernamePinResModel resModel = gson.fromJson(response.body(), SetUsernamePinResModel.class);
            AppLogger.v("RemoteApi", "" + LOGIN_PIN_API);
            return ResponseApi.success(resModel, status);
        } else if (status == SET_USER_PIN) {
            SetUsernamePinResModel resModel = gson.fromJson(response.body(), SetUsernamePinResModel.class);
            AppLogger.v("RemoteApi", "" + SET_USER_PIN);
            return ResponseApi.success(resModel, status);
        } else if (status == WALLET_STATUS_API) {
            StudentWalletResModel resModel = gson.fromJson(response.body(), StudentWalletResModel.class);
            AppLogger.v("RemoteApi", "" + WALLET_STATUS_API);
            return ResponseApi.success(resModel, status);
        } else if (status == DYNAMIC_LANGUAGE) {
            LanguageMasterDynamic resModel = gson.fromJson(response.body(), LanguageMasterDynamic.class);
            AppLogger.v("Language_pradeep", "Dynamic language Step 5 " + resModel.getDetails());
            return ResponseApi.success(resModel, status);
        } else if (status == REFFERAL_API) {
            DynamiclinkResModel resModel = gson.fromJson(response.body(), DynamiclinkResModel.class);
            AppLogger.v("REFFERAL_API", "REFFERAL_API");
            return ResponseApi.success(resModel, status);
        } else if (status == SEND_REFERRAL_API) {
            DynamiclinkResModel resModel = gson.fromJson(response.body(), DynamiclinkResModel.class);
            AppLogger.v("REFFERAL_API", "REFFERAL_API");
            return ResponseApi.success(resModel, status);
        } else if (status == STUDENT_KYC_STATUS_API) {
            StudentKycStatusResModel resModel = gson.fromJson(response.body(), StudentKycStatusResModel.class);
            AppLogger.v("STUDENT_KYC_STATUS_API", "STUDENT_KYC_STATUS_API");
            return ResponseApi.success(resModel, status);
        } else if (status == OTP_OVER_CALL) {
            OtpOverCallResModel resModel = gson.fromJson(response.body(), OtpOverCallResModel.class);
            return ResponseApi.success(resModel, status);
        } else if (status == GET_INSTRUCTIONS_API) {
            InstructionsResModel resModel = gson.fromJson(response.body(), InstructionsResModel.class);
            return ResponseApi.success(resModel, status);

        } else if (status == PENDING_KYC_DOCS) {
            ErrorResponseModel resModel = gson.fromJson(response.body(), ErrorResponseModel.class);
            return ResponseApi.success(resModel, status);
        }else if (status == GET_MESSAGE_POP_UP) {
            AppLogger.v("Dialog_pradeep","HomeRemote Step1 ");
            ShowDialogModel resModel = gson.fromJson(response.body(), ShowDialogModel.class);
            return ResponseApi.success(resModel, status);
        } else if (status == GET_SLABS_API) {
            SlabsResModel resModel = gson.fromJson(response.body(), SlabsResModel.class);
            return ResponseApi.success(resModel, status);
        }


        AppLogger.v("RemoteApi", status + "status   ----   null");
        return ResponseApi.fail(null, status);
    }

    @Override
    public ResponseApi response401(Status status) {
        return ResponseApi.authFail(401, status);
    }

    @Override
    public ResponseApi responseFail400(Response<JsonObject> response, Status status) {
        AppLogger.v("responseFail400--", "--" + response.code());
        AppLogger.v("responseFail400 body--", "--" + response.body());
        AppLogger.v("responseFail400 errorbody--", "--" + response.errorBody().toString());

        AppLogger.v("responseFail400--", "responseFail400 step 1");

        AppLogger.v("responseFail400--", "responseFail400 step 2");
        try {
            AppLogger.v("responseFail400--", "responseFail400 step 3" + response.body());
            JSONObject jsonObject = new JSONObject(response.errorBody().string());
            String error = jsonObject.getString("message");
            AppLogger.v("responseFail400--", "responseFail400 step 4" + error);
            // ErrorResponseModel errorResponseModel=gson.fromJson(response.errorBody().toString(), ErrorResponseModel.class);
            // AppLogger.v("responseFail400--","responseFail400 step 5" +errorResponseModel);
            return ResponseApi.fail400(error, status);
        } catch (Exception e) {
            AppLogger.v("responseFail400--", "exception step 6");
            AppLogger.v("responseFail400--", "exception--" + e.getMessage());
            return ResponseApi.fail(AuroApp.getAppContext().getResources().getString(R.string.default_error), status);
        }
    }

    @Override
    public ResponseApi responseFail(Status status) {
        return ResponseApi.fail(AuroApp.getAppContext().getString(R.string.default_error), status);
    }
}

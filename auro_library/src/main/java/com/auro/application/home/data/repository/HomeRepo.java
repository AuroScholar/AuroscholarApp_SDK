package com.auro.application.home.data.repository;

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
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.common.StateDataModel;
import com.auro.application.teacher.data.model.request.SendInviteNotificationReqModel;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;

public interface HomeRepo {

    interface DashboardRemoteData {
        Single<Response<JsonObject>> getStoreOnlineData(String modifiedTime);

        Single<Response<JsonObject>> sendOtpHomeRepo(SendOtpReqModel reqModel);

        Single<Response<JsonObject>> verifyOtpHomeRepo(VerifyOtpReqModel reqModel);

        Single<Response<JsonObject>> versionApiCheck();

        Single<Response<JsonObject>> state();

        Single<Response<JsonObject>> checkUserValidApi(CheckUserApiReqModel reqModel);

        Single<Response<JsonObject>> changeGradeApi(CheckUserValidResModel reqModel);

        Single<Response<JsonObject>> getDashboardData(AuroScholarDataModel model);

        Single<Response<JsonObject>> uploadProfileImage(List<KYCDocumentDatamodel> list, KYCInputModel kycInputModel);

        Single<Response<JsonObject>> postDemographicData(GetStudentUpdateProfile demographicResModel);

        Single<Response<JsonObject>> getAssignmentId(AssignmentReqModel assignmentReqModel);

        Single<Response<JsonObject>> getAzureData(AssignmentReqModel azureReqModel);

        Single<Response<JsonObject>> inviteFriendListApi();

        Single<Response<JsonObject>> sendInviteNotificationApi(SendInviteNotificationReqModel reqModel);

        Single<Response<JsonObject>> acceptInviteApi(SendInviteNotificationReqModel reqModel);

        Single<Response<JsonObject>> upgradeClass(AuroScholarDataModel model);

        Single<Response<JsonObject>> partnersApi();

        Single<Response<JsonObject>> partnersLoginApi(PartnersLoginReqModel reqModel);

        Single<Response<JsonObject>> findFriendApi(double lat, double longt, double radius);

        Single<Response<JsonObject>> sendFriendRequestApi(int requested_by_id, int requested_user_id, String requested_by_phone, String requested_user_phone);

        Single<Response<JsonObject>> friendRequestListApi(int requested_by_id);

        Single<Response<JsonObject>> friendAcceptApi(int friend_request_id, String request_status);

        Single<Response<JsonObject>> getDynamicDataApi(DynamiclinkResModel model);

        Single<Response<JsonObject>> studentUpdateProfile(GetStudentUpdateProfile model);

        Single<Response<JsonObject>> sendRefferalDataApi(RefferalReqModel model);

        Single<Response<JsonObject>> getCertificateApi(CertificateResModel model);

        Single<Response<JsonObject>> uploadStudentExamImage(SaveImageReqModel reqModel);

        Single<Response<JsonObject>> passportApi(PassportReqModel reqModel);

        Single<Response<JsonObject>> forgotPassword(SetPasswordReqModel reqModel);

        Single<Response<JsonObject>> loginApi(LoginReqModel reqModel);

        Single<Response<JsonObject>> getTeacherDashboardApi(AuroScholarDataModel auroScholarDataModel);

        Single<Response<JsonObject>> preferenceSubjectList();

        Single<Response<JsonObject>> fetchStudentPreferenceApi(FetchStudentPrefReqModel reqModel);

        Single<Response<JsonObject>> updateStudentPreference(UpdatePrefReqModel reqModel);

        Single<Response<JsonObject>> getLanguageList();

        Single<Response<JsonObject>> getNoticeInstruction();

        Single<Response<JsonObject>> getUserProfile(String userId);

        Single<Response<JsonObject>> registerApi(RegisterApiReqModel request);

        Single<Response<JsonObject>> setUsernamePinApi(SetUsernamePinReqModel request);

        Single<Response<JsonObject>> loginUsingPinApi(SetUsernamePinReqModel request);

        Single<Response<JsonObject>> setUserPinApi(SetUsernamePinReqModel request);

        Single<Response<JsonObject>> getWalletStatusApi(SetPasswordReqModel request);

        Single<Response<JsonObject>> getStudentKycStatus( );

        Single<Response<JsonObject>> getDynamicLang(LanguageMasterReqModel languageMasterReqModel);

        Single<Response<JsonObject>> optOverCall(OtpOverCallReqModel request);

        Single<Response<JsonObject>> parentUpdateProfile();

        Single<Response<JsonObject>> parentUpdateProfile(String name, int stateid, int districtid, String gender, String emailid);

        Single<Response<JsonObject>> getInstructionsApi(InstructionModel id);

        Single<Response<JsonObject>> pendingKycDocs(PendingKycDocsModel pendingKycDocsModel);

        Single<Response<JsonObject>> getMsgPopUp(PendingKycDocsModel pendingKycDocsModel);

        Single<Response<JsonObject>> getSlabsApi(UserSlabsRequest id);


    }


    interface DashboardDbData {
        Single<Long[]> insertStateList(List<StateDataModel> stateDataModelList);

        Single<Long[]> insertDistrictList(List<DistrictDataModel> stateDataModelList);

        Single<List<StateDataModel>> getStateList();

        Single<List<DistrictDataModel>> getDistrictList();


        Single<List<DistrictDataModel>> getStateDistrictList(String stateCode);

    }

}

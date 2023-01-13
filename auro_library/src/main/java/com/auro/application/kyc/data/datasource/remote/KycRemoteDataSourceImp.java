package com.auro.application.kyc.data.datasource.remote;


import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.DemographicResModel;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.data.model.KYCInputModel;
import com.auro.application.home.data.model.PartnersLoginReqModel;
import com.auro.application.home.data.model.SaveImageReqModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.SetPasswordReqModel;
import com.auro.application.home.data.model.StudentProfileModel;
import com.auro.application.home.data.model.UpdatePrefReqModel;
import com.auro.application.home.data.model.VerifyOtpReqModel;
import com.auro.application.home.data.model.passportmodels.PassportReqModel;
import com.auro.application.home.data.model.response.CertificateResModel;
import com.auro.application.home.data.model.response.CheckUserValidResModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.data.model.signupmodel.request.LoginReqModel;
import com.auro.application.home.data.repository.HomeRepo.DashboardRemoteData;
import com.auro.application.kyc.data.repository.KycRepo;
import com.auro.application.teacher.data.model.request.SendInviteNotificationReqModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class KycRemoteDataSourceImp implements KycRepo.KycRemoteData {
    KycRemoteApi kycRemoteApi;

    public KycRemoteDataSourceImp(KycRemoteApi kycRemoteApi) {
        this.kycRemoteApi = kycRemoteApi;
    }



}

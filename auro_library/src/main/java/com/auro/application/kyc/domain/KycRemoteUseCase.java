package com.auro.application.kyc.domain;

import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_200;
import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_400;
import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_401;
import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_FAIL;
import static com.auro.application.core.common.Status.PAYMENT_TRANSFER_API;
import static com.auro.application.core.common.Status.PAYTM_ACCOUNT_WITHDRAWAL;
import static com.auro.application.core.common.Status.PAYTM_UPI_WITHDRAWAL;
import static com.auro.application.core.common.Status.PAYTM_WITHDRAWAL;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.NetworkUtil;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.core.network.NetworkUseCase;
import com.auro.application.kyc.data.repository.KycRepo;
import com.auro.application.payment.data.model.request.PaytmWithdrawalByBankAccountReqModel;
import com.auro.application.payment.data.model.request.PaytmWithdrawalByUPIReqModel;
import com.auro.application.payment.data.model.request.PaytmWithdrawalReqModel;
import com.auro.application.payment.data.model.response.PaytmResModel;
import com.auro.application.payment.data.repository.PaymentRepo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import retrofit2.Response;

public class KycRemoteUseCase extends NetworkUseCase {

    KycRepo.KycRemoteData kycRemoteData;
    Gson gson = new Gson();

    public KycRemoteUseCase(KycRepo.KycRemoteData kycRemoteData) {
        this.kycRemoteData = kycRemoteData;
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

        if (status == PAYTM_UPI_WITHDRAWAL) {
            PaytmResModel paytmResModel = gson.fromJson(response.body(), PaytmResModel.class);
            return ResponseApi.success(paytmResModel, status);
        }

        if (status == PAYTM_WITHDRAWAL) {
            PaytmResModel paytmResModel = gson.fromJson(response.body(), PaytmResModel.class);
            return ResponseApi.success(paytmResModel, status);
        }

        if (status == PAYTM_ACCOUNT_WITHDRAWAL) {
            PaytmResModel paytmResModel = gson.fromJson(response.body(), PaytmResModel.class);
            return ResponseApi.success(paytmResModel, status);
        }
        if (status == PAYMENT_TRANSFER_API) {
            PaytmResModel paytmResModel = gson.fromJson(response.body(), PaytmResModel.class);
            return ResponseApi.success(paytmResModel, status);
        }

        return ResponseApi.fail(null, status);
    }

    @Override
    public ResponseApi response401(Status status) {
        return null;
    }

    @Override
    public ResponseApi responseFail400(Response<JsonObject> response, Status status) {
        return null;
    }

    @Override
    public ResponseApi responseFail(Status status) {
        return null;
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
                return ResponseApi.fail(AuroApp.getAppContext().getString(R.string.default_error), apiTypeStatus);
        }
    }










}

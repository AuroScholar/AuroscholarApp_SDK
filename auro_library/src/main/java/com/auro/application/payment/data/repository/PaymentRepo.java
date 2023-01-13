package com.auro.application.payment.data.repository;

import com.auro.application.home.data.model.AuroScholarDataModel;

import com.auro.application.payment.data.model.request.PaytmWithdrawalByBankAccountReqModel;
import com.auro.application.payment.data.model.request.PaytmWithdrawalByUPIReqModel;
import com.auro.application.payment.data.model.request.PaytmWithdrawalReqModel;
import com.google.gson.JsonObject;

import io.reactivex.Single;
import retrofit2.Response;

public interface PaymentRepo {

    interface PaymentRemoteData {
        Single<Response<JsonObject>> getDashboardData(AuroScholarDataModel model);

        Single<Response<JsonObject>> paytmWithdrawalApi(PaytmWithdrawalReqModel reqModel);

        Single<Response<JsonObject>> paytmWithdrawalByUpiApi(PaytmWithdrawalByUPIReqModel reqModel);

        Single<Response<JsonObject>> paytmWithdrawalByAccountApi(PaytmWithdrawalByBankAccountReqModel reqModel);

        Single<Response<JsonObject>> paymentTransferApi(PaytmWithdrawalByBankAccountReqModel reqModel);
    }




}

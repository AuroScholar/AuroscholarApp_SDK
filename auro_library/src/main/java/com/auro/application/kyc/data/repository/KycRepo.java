package com.auro.application.kyc.data.repository;

import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.payment.data.model.request.PaytmWithdrawalByBankAccountReqModel;
import com.auro.application.payment.data.model.request.PaytmWithdrawalByUPIReqModel;
import com.auro.application.payment.data.model.request.PaytmWithdrawalReqModel;
import com.google.gson.JsonObject;

import io.reactivex.Single;
import retrofit2.Response;

public interface KycRepo {

    interface KycRemoteData {

    }
}

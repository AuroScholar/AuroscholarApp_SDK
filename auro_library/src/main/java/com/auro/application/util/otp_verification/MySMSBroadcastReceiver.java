package com.auro.application.util.otp_verification;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.auro.application.util.AppLogger;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import static android.app.Activity.RESULT_OK;
import static com.auro.application.core.common.AppConstant.PermissionCode.CREDENTIAL_PICKER_REQUEST;

public class MySMSBroadcastReceiver {

    SmsSignInListener smsSignInListener;
    FragmentActivity activity;

    public void initialize(FragmentActivity activity, SmsSignInListener smsSignInListener) {
        this.smsSignInListener = smsSignInListener;
        this.activity = activity;
    }


    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == CREDENTIAL_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // Obtain the phone number from the result
                Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
                AppLogger.v("autoPhone", credentials.getId() + "onActivityResult  Come");
                //EditText.setText(credentials.getId().substring(3)); //get the selected phone number
//Do what ever you want to do with your selected phone number here

                handleSignInResult(credentials);
            }else{
                AppLogger.v("autoPhone","resultCode  Error Come "+resultCode);
            }
        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            AppLogger.v("autoPhone","onActivityResult  Error Come");
            handleSignInResult(null);
        }
    }

    private void handleSignInResult(Credential result) {
        if (result != null) {
            if (smsSignInListener != null) {
                AppLogger.v("autoOtp","handleSignInResult  Sucess Come");
                smsSignInListener.OnPhoneSignSuccess(result);
            } else {
                AppLogger.v("autoOtp","handleSignInResult  Error Come");
                smsSignInListener.OnPhoneSignError();
            }

        }
    }

    public interface SmsSignInListener {
        void OnPhoneSignSuccess(Credential credential);

        void OnPhoneSignError();

    }

}
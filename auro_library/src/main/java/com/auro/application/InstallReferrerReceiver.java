package com.auro.application;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class InstallReferrerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.android.vending.INSTALL_REFERRER")) {

            Bundle extras = intent.getExtras();
            String referrer = String.valueOf(extras.get("url"));
            Uri data = Uri.parse(String.valueOf(intent.getData()));  //("dat=market://details?id=com.auro.application&url=https://auroscholar.com/login.php?jsondata=%7B%22reffer_user_id%22:%22864352%22,%22source%22:%22AURO3VE4j7%22,%22navigation_to%22:%22STUDENT_DASHBOARD%22,%22reffer_type%22:%221%22%7D");
            String param1 = data.getQueryParameter("url");
            SharedPreferences.Editor editor = context.getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
            editor.putString("statusrefferalurl", referrer);
            editor.apply();
            String reffer_user_id = (String) extras.get("reffer_user_id");
            Log.e("reffer_user_id", reffer_user_id);

        }
    }
}

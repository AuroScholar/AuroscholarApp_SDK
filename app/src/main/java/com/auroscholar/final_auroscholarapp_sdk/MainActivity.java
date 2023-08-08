package com.auroscholar.final_auroscholarapp_sdk;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.home.data.model.AuroScholarInputModel;

import com.auro.application.home.presentation.view.activity.SDKActivity;

public class MainActivity extends AppCompatActivity {
    Button btncall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setUserMobile("9874561230");
        prefModel.setUserclass(7);
        prefModel.setPartnersource("Numismatics_WEB");
        prefModel.setApikey("8daac713b6335ab1bdf929fe02904664300f4881e9b211543686ebde2aa41ec9");
        prefModel.setPartneruniqueid("1574886464534233");
        AuroAppPref.INSTANCE.setPref(prefModel);
        startActivity(new Intent(this, SDKActivity.class));
    }

}
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
        //btncall=findViewById(R.id.btncall);
        AuroScholarInputModel inputModel = new AuroScholarInputModel();
        inputModel.setMobileNumber("9874561231");
        inputModel.setStudentClass(String.valueOf(""));
        inputModel.setPartner_unique_id("1574888988");
        inputModel.setPartnerSource("Numismatics_WEB");
        inputModel.setPartner_api_key("8daac713b6335ab1bdf929fe02904664300f4881e9b211543686ebde2aa41ec9");
        inputModel.setActivity((Activity) MainActivity.this);
        AuroScholar.startAuroSDK(inputModel);
       // startActivity(new Intent(MainActivity.this, SDKActivity.class));
       // startActivity(new Intent(MainActivity.this, SDKActivity.class));
//        btncall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, SDKActivity.class));
//            }
//        });
    }

}
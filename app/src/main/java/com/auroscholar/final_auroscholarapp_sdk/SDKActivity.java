package com.auroscholar.final_auroscholarapp_sdk;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.auro.application.core.util.AuroScholar;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auroscholar.final_auroscholarapp_sdk.databinding.ActivitySdkBinding;


public class SDKActivity extends AppCompatActivity {
TextView bt_sdk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdk);
        bt_sdk=findViewById(R.id.bt_sdk);
        bt_sdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGenricSDK("9289180019", "12");
            }
        });
    }

    private void openGenricSDK(String mobileNumber, String student_class) {

        AuroScholarInputModel inputModel = new AuroScholarInputModel();
        inputModel.setMobileNumber(mobileNumber);//mobileNumber
        inputModel.setStudentClass(student_class);//"binding.userClass.getText().toString()"
        inputModel.setRegitrationSource("");
        inputModel.setReferralLink("");
        inputModel.setPartnerSource(""); //this id is provided by auroscholar for valid partner//Demo partner id:AUROJ1i5dA
        inputModel.setActivity(this);
        inputModel.setLanguage("en");
        inputModel.setPartnerLogoUrl(""); //Mandatory
        inputModel.setSchoolName("");//optional Filed
        inputModel.setBoardType("");//optional Filed
        inputModel.setSchoolType("");//optional Filed
        inputModel.setGender("");//optional Filed
        inputModel.setPartnerName("");//Mandatory
        inputModel.setEmail("");//optional Filed
        AuroScholar.startAuroSDK(inputModel);
    }
}
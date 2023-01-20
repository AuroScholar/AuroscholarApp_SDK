package com.auroscholar.final_auroscholarapp_sdk;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.home.data.model.AuroScholarInputModel;

import com.auro.application.home.presentation.view.fragment.BottomSheetUsersDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SDKActivity extends AppCompatActivity {
TextView bt_sdk;
    SDKDataModel checkUserResModel;
EditText mobile_number,user_class,language;

    List<SDKChildModel> userDetails = new ArrayList<>();
    List<SDKChildModel> userDetailsNew = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdk);
        bt_sdk=findViewById(R.id.bt_sdk);
        mobile_number=findViewById(R.id.mobile_number);
        user_class=findViewById(R.id.mobile_number);
        language=findViewById(R.id.language);

        bt_sdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobno = mobile_number.getText().toString();
                String userclass = user_class.getText().toString();
                String languagecode = "1";
                setSDKAPI(mobno,"456456","AURO3VE4j7");
            }
        });
    }

    private void openGenricSDK(String userid,String mobileNumber) {
        AuroScholarInputModel inputModel = new AuroScholarInputModel();
        inputModel.setUserid(userid);//userid
        inputModel.setMobileNumber(mobileNumber);//mobileNumber
        inputModel.setStudentClass("11");//"binding.userClass.getText().toString()"
        inputModel.setRegitrationSource("");
        inputModel.setReferralLink("");
        inputModel.setPartner_unique_id("456456");
        inputModel.setPartnerSource("AURO3VE4j7"); //this id is provided by auroscholar for valid partner//Demo partner id:AUROJ1i5dA
        inputModel.setActivity(this);
        inputModel.setLanguage("1");
        inputModel.setPartnerLogoUrl(""); //Mandatory
        inputModel.setSchoolName("");//optional Filed
        inputModel.setBoardType("");//optional Filed
        inputModel.setSchoolType("");//optional Filed
        inputModel.setGender("");//optional Filed
        inputModel.setPartnerName("");//Mandatory
        inputModel.setEmail("");//optional Filed
        AuroScholar.startAuroSDK(inputModel);
    }
    private void setSDKAPI(String mobno, String partneruniqueid, String partnersource)
    {
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("mobile_no",mobno);
        map_data.put("partner_unique_id",partneruniqueid); //456456
        map_data.put("partner_source",partnersource);

        SDKRemoteApi.Companion.invoke().getSDKData(map_data)
                .enqueue(new Callback<SDKDataModel>() {
                    @Override
                    public void onResponse(Call<SDKDataModel> call, Response<SDKDataModel> response)
                    {
                        try {
                            if (response.code() == 400) {
                                Toast.makeText(SDKActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                            else if (response.code() == 200) {
                                userDetails.clear();
                                userDetailsNew.clear();
                                userDetails = response.body().getUser_details();
                                checkUserResModel = (SDKDataModel) response.body();
                               if (response.body().getUser_details().size() > 1){

                                    for (int i = 0; i<userDetails.size(); i++){
                                        userDetailsNew.add(userDetails.get(i));

                                    }
                                   PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                   prefModel.setChildData(checkUserResModel);
                                   AuroAppPref.INSTANCE.setPref(prefModel);
                                    openBottomSheetDialog();
                                }
                                else{
                                   PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                   prefModel.setChildData(checkUserResModel);
                                   AuroAppPref.INSTANCE.setPref(prefModel);
                                    String userid = response.body().getUser_details().get(0).getUser_id();
                                    openGenricSDK(userid,mobno);
                                }

                            }
                            else {
                                Toast.makeText(SDKActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(SDKActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SDKDataModel> call, Throwable t) {
                        Toast.makeText(SDKActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
    }


    public void openBottomSheetDialog() {
        BottomSheetUsersDialog bottomSheet = new BottomSheetUsersDialog();
        bottomSheet.show(this.getSupportFragmentManager(),
                "ModalBottomSheet");
    }
}
package com.auro.application.home.presentation.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auro.application.R;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.LanguageMasterReqModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.LanguageListResModel;
import com.auro.application.home.presentation.view.fragment.BottomSheetUsersDialog;
import com.auro.application.util.AppLogger;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.alert_dialog.LanguageChangeDialog;
import com.auroscholar.final_auroscholarapp_sdk.SDKChildModel;
import com.auroscholar.final_auroscholarapp_sdk.SDKDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SDKActivity  extends AppCompatActivity {
    TextView bt_sdk;
    SDKDataModel checkUserResModel;
    EditText mobile_number,partner_source,partner_unique_id,partner_api_key,gradeid;
    List<SDKChildModel> userDetails = new ArrayList<>();
    List<SDKChildModel> userDetailsNew = new ArrayList<>();
    LanguageMasterDynamic language;
    LanguageListResModel languageListResModel;
    Details languagedetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdk);
        bt_sdk=findViewById(R.id.bt_sdk);
        mobile_number=findViewById(R.id.mobile_number);
        partner_source=findViewById(R.id.partner_source);
        partner_unique_id=findViewById(R.id.partner_unique_id);
        partner_api_key=findViewById(R.id.partner_api_key);
        gradeid=findViewById(R.id.grade);
        getMultiLanguage();
        getLanguage();

        bt_sdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobno = mobile_number.getText().toString();
                String puniqueid = partner_unique_id.getText().toString();
                String psource = partner_source.getText().toString();
                String apikey = partner_api_key.getText().toString();
                String grade = gradeid.getText().toString();
                String mobno1 = "1010101010";
                String puniqueid1 = "975330";
                String psource1 = "Aeronuts_WEB";
                String apikey1 = "7611f0fafb1e3b96d1a78c57b0650b85985eace9f6aaa365c0b496e9ae1163e7";
                String grade1 = "";
                openGenricSDK(mobno,puniqueid,psource,apikey,grade);
            }
        });
    }
    public void openGenricSDK(String mobileNumber,String partneruniqueid,String partnersource, String partnerapi, String grade ) {
        AuroScholarInputModel inputModel = new AuroScholarInputModel();
        inputModel.setMobileNumber(mobileNumber);
        inputModel.setStudentClass(grade);
        inputModel.setPartner_unique_id(partneruniqueid);
        inputModel.setPartnerSource(partnersource);
        inputModel.setPartner_api_key(partnerapi);
        inputModel.setActivity(this);
        setSDKAPI(mobileNumber,partneruniqueid,partnersource,partnerapi,"");
    }

    private void setSDKAPI(String mobno, String partneruniqueid, String partnersource, String apikey, String grade)
    {

        if (mobno.isEmpty()){
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
        }

        else if (partnersource.isEmpty()){
            Toast.makeText(this, "Please enter partner source", Toast.LENGTH_SHORT).show();

        }
        else if (partneruniqueid.isEmpty()){
            Toast.makeText(this, "Please enter partner unique id", Toast.LENGTH_SHORT).show();

        }
        else if (apikey.isEmpty()){
            Toast.makeText(this, "Please enter partner api key", Toast.LENGTH_SHORT).show();

        }
        else{
            HashMap<String,String> map_data = new HashMap<>();
            map_data.put("mobile_no",mobno);
            map_data.put("partner_unique_id",partneruniqueid); //456456
            map_data.put("partner_source",partnersource);
            map_data.put("partner_api_key",apikey);
            map_data.put("grade",grade);

            RemoteApi.Companion.invoke().getSDKData(map_data)
                    .enqueue(new Callback<SDKDataModel>() {
                        @Override
                        public void onResponse(Call<SDKDataModel> call, Response<SDKDataModel> response)
                        {
                            try {
                                if (response.code() == 400) {
                                    Toast.makeText(SDKActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                }
                                else if (response.code() == 200) {
                                    if (response.body().getMessage().equals("Error! You cannot add more child")){
                                        Toast.makeText(SDKActivity.this, "Error! You cannot add more child", Toast.LENGTH_SHORT).show();
                                    }
                                    else{

                                        userDetails.clear();
                                        userDetailsNew.clear();
                                        userDetails = response.body().getUser_details();
                                        checkUserResModel = (SDKDataModel) response.body();
                                        for (int i = 0; i<userDetails.size(); i++){
                                            userDetailsNew.add(userDetails.get(i));

                                        }
                                        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                        prefModel.setChildData(checkUserResModel);
                                        prefModel.setPartnersource(partnersource);
                                        prefModel.setPartneruniqueid(partneruniqueid);
                                        prefModel.setApikey(apikey);
                                        AuroAppPref.INSTANCE.setPref(prefModel);

                                        openBottomSheetDialog();
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

    }

    public void getLanguage()
    {

        HashMap<String,String> map_data = new HashMap<>();
            map_data.put("language_id","1");
            map_data.put("user_type_id","1");
        RemoteApi.Companion.invoke().getLanguageAPI(map_data)
                    .enqueue(new Callback<LanguageMasterDynamic>() {
                        @Override
                        public void onResponse(Call<LanguageMasterDynamic> call, Response<LanguageMasterDynamic> response)
                        {
                            try {
                                if (response.code() == 400) {
                                    Toast.makeText(SDKActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                }
                                else if (response.code() == 200) {
                                    languagedetail = response.body().getDetails();
                                    language = (LanguageMasterDynamic) response.body();
                                    PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                    prefModel.setLanguageMasterDynamic(language);
                                    AuroAppPref.INSTANCE.setPref(prefModel);

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
                        public void onFailure(Call<LanguageMasterDynamic> call, Throwable t) {
                            Toast.makeText(SDKActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
        }
    public void getMultiLanguage()
    {


        RemoteApi.Companion.invoke().getLanguageAPIList()
                .enqueue(new Callback<LanguageListResModel>() {
                    @Override
                    public void onResponse(Call<LanguageListResModel> call, Response<LanguageListResModel> response)
                    {
                        try {
                            if (response.code() == 400) {
                                Toast.makeText(SDKActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                            else if (response.code() == 200) {

                                languageListResModel = (LanguageListResModel) response.body();
                                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                prefModel.setLanguageListResModel(languageListResModel);
                                AuroAppPref.INSTANCE.setPref(prefModel);

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
                    public void onFailure(Call<LanguageListResModel> call, Throwable t) {
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

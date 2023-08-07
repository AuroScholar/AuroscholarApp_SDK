package com.auro.application.home.presentation.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.common.SdkCallBack;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.network.ErrorResponseModel;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SDKActivity  extends AppCompatActivity {
    TextView bt_sdk;
    EditText mobile_number,partner_source,partner_unique_id,partner_api_key,gradeid;
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
        mobile_number.setText("9874561230");
        partner_unique_id.setText("1574885835783857");
        partner_source.setText("Numismatics_WEB");
        partner_api_key.setText("8daac713b6335ab1bdf929fe02904664300f4881e9b211543686ebde2aa41ec9");
        DaggerWrapper.getComponent(this).doInjection(this);

//        bt_sdk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//                String mobno = mobile_number.getText().toString();
//                    String puniqueid = partner_unique_id.getText().toString();
//                    String psource = partner_source.getText().toString();
//                    String apikey = partner_api_key.getText().toString();
//                    String grade = gradeid.getText().toString();
                    AuroScholarInputModel inputModel = new AuroScholarInputModel();
                    inputModel.getMobileNumber();
                    inputModel.getStudentClass();
                    inputModel.getPartner_unique_id();
                    inputModel.getPartnerSource();
                    inputModel.getPartner_api_key();
                    inputModel.getActivity();
                    AuroScholar.startAuroSDK(inputModel);
//
//            }
//        });
    }


    public void getLanguage(String langid)
    {
        HashMap<String,String> map_data = new HashMap<>();
        if (!langid.isEmpty()|| !langid.equals("")){
            map_data.put("language_id",langid);
        }
        else{
            map_data.put("language_id","1");
        }
        map_data.put("user_type_id","1");
        RemoteApi.Companion.invoke().getLanguageAPI(map_data)
                    .enqueue(new Callback<LanguageMasterDynamic>() {
                        @Override
                        public void onResponse(Call<LanguageMasterDynamic> call, Response<LanguageMasterDynamic> response)
                        {
                            try {
                                if (response.code() == 400) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.errorBody().string());
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(SDKActivity.this,message, Toast.LENGTH_SHORT).show();

                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }


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
                           //     Toast.makeText(SDKActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
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
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(SDKActivity.this,message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }


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
                         //   Toast.makeText(SDKActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LanguageListResModel> call, Throwable t) {
                        Toast.makeText(SDKActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
    }


}

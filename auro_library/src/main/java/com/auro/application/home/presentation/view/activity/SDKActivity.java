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
import com.auro.application.core.application.di.component.DaggerWrapper;
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
    SDKDataModel checkUserResModel;
    EditText mobile_number,partner_source,partner_unique_id,partner_api_key,gradeid;
    List<SDKChildModel> userDetails = new ArrayList<>();
    List<SDKChildModel> userDetailsNew = new ArrayList<>();
    LanguageMasterDynamic language;
    LanguageListResModel languageListResModel;
    Details languagedetail;
    String errormismatch = "";
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
        mobile_number.setText("7984268169");
        partner_unique_id.setText("89456523");
        partner_source.setText("Aeronuts_WEB");
        partner_api_key.setText("7611f0fafb1e3b96d1a78c57b0650b85985eace9f6aaa365c0b496e9ae1163e7");
        DaggerWrapper.getComponent(this).doInjection(this);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel.isLogin()){
            getMultiLanguage();
            getLanguage(prefModel.getUserLanguageId());
            int userclass = prefModel.getUserclass();
            AuroScholarInputModel inputModel = new AuroScholarInputModel();
            inputModel.setMobileNumber(prefModel.getUserMobile());
            inputModel.setStudentClass(String.valueOf(userclass));
            inputModel.setPartner_unique_id(prefModel.getPartneruniqueid());
            inputModel.setPartnerSource(prefModel.getPartnersource());
            inputModel.setPartner_api_key(prefModel.getApikey());
            inputModel.setActivity((Activity) SDKActivity.this);
            AuroScholar.startAuroSDK(inputModel);
        }
       else{
            getMultiLanguage();
            getLanguage("1");
        }


        bt_sdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobno = mobile_number.getText().toString();
                    String puniqueid = partner_unique_id.getText().toString();
                    String psource = partner_source.getText().toString();
                    String apikey = partner_api_key.getText().toString();
                    String grade = gradeid.getText().toString();
                    AuroScholarInputModel inputModel = new AuroScholarInputModel();
                    inputModel.setMobileNumber(mobno);
                    inputModel.setStudentClass(String.valueOf(grade));
                    inputModel.setPartner_unique_id(puniqueid);
                    inputModel.setPartnerSource(psource);
                    inputModel.setPartner_api_key(apikey);
                    inputModel.setActivity((Activity) SDKActivity.this);


                    AuroScholar.startAuroSDK(inputModel);

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
            //map_data.put("grade",grade);

            RemoteApi.Companion.invoke().getSDKData(map_data)
                    .enqueue(new Callback<SDKDataModel>() {
                        @Override
                        public void onResponse(Call<SDKDataModel> call, Response<SDKDataModel> response)
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
                                    prefModel.setUserMobile(mobno);
                                    prefModel.setPartneruniqueid(partneruniqueid);
                                    prefModel.setApikey(apikey);
                                    AuroAppPref.INSTANCE.setPref(prefModel);




                                        if (userDetails.size() ==1 && (userDetails.get(0).is_mapped() == 1||userDetails.get(0).is_mapped().equals(1)||userDetails.get(0).is_mapped().equals("1"))){

                                            String userid_child =  userDetails.get(0).getUser_id();
                                            String user_mobile =  userDetails.get(0).getMobile_no();
                                            String student_name =  userDetails.get(0).getStudent_name();
                                            String user_language =  userDetails.get(0).getUser_prefered_language_id();
                                            int user_grade =  userDetails.get(0).getGrade();
                                            String user_kyc =  userDetails.get(0).getKyc_status();
                                            String user_name = userDetails.get(0).getUser_name();
                                            String partner_logo =  userDetails.get(0).getPartner_logo();
                                            String profile_pic = userDetails.get(0).getProfile_pic();
                                            PrefModel prefModel2 = AuroAppPref.INSTANCE.getModelInstance();
                                            prefModel2.setUserId(userid_child);
                                            prefModel2.setUserMobile(user_mobile);
                                            prefModel2.setUserLanguageId(user_language);
                                            prefModel2.setStudentName(student_name);
                                            prefModel2.setUserclass(user_grade);
                                            prefModel2.setKycstatus(user_kyc);
                                            prefModel2.setUserprofilepic(profile_pic);
                                            prefModel2.setUserName(user_name);
                                            prefModel2.setPartner_logo(partner_logo);
                                            AuroAppPref.INSTANCE.setPref(prefModel2);
                                            getProfile(userid_child,user_language);
                                        }
                                        else{
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
    private void getProfile(String userid, String lang_id)
    {

        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);

        RemoteApi.Companion.invoke().getStudentData(map_data)
                .enqueue(new Callback<GetStudentUpdateProfile>()
                {
                    @Override
                    public void onResponse(Call<GetStudentUpdateProfile> call, Response<GetStudentUpdateProfile> response)
                    {
                        if (response.isSuccessful())
                        {
                            if (lang_id==null||lang_id.equals("null")||lang_id.equals(null)||lang_id.equals("0")||lang_id=="0"||lang_id.equals("")||lang_id.isEmpty()){
                                Intent i = new Intent(SDKActivity.this, AppLanguageActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                            else if (errormismatch.equals("Error! Grade Mismatched")){
                                Intent i = new Intent(SDKActivity.this, ChooseGradeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(i);
                            }

                            else if (response.body().getStatename().equals("")||response.body().getStatename().equals("null")||response.body().getStatename().equals(null)||response.body().getDistrictname().equals("")||response.body().getDistrictname().equals("null")||response.body().getDistrictname().equals(null)||response.body().getStudentName().equals("")||response.body().getStudentName().equals("null")||response.body().getStudentName().equals(null)||
                                    response.body().getStudentclass().equals("")||response.body().getStudentclass().equals("null")||response.body().getStudentclass().equals(null)||response.body().getStudentclass().equals("0")||response.body().getStudentclass().equals(0)){
                                Intent i = new Intent(SDKActivity.this, CompleteStudentProfileWithoutPin.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(i);
                            }
                            else{
                                    PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                    int userclass = prefModel.getUserclass();
                                    AuroScholarInputModel inputModel = new AuroScholarInputModel();
                                    inputModel.setMobileNumber(prefModel.getUserMobile());
                                    inputModel.setStudentClass(String.valueOf(userclass));
                                    inputModel.setPartner_unique_id(prefModel.getPartneruniqueid());
                                    inputModel.setPartnerSource(prefModel.getPartnersource());
                                    inputModel.setPartner_api_key(prefModel.getApikey());
                                    inputModel.setActivity((Activity) SDKActivity.this);
                                    AuroScholar.startAuroSDK(inputModel);

                            }

                        }
                        else
                        {
                            Toast.makeText(SDKActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<GetStudentUpdateProfile> call, Throwable t)
                    {
                        Toast.makeText(SDKActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
    public void openBottomSheetDialog() {
        BottomSheetUsersDialog bottomSheet = new BottomSheetUsersDialog();
        bottomSheet.show(this.getSupportFragmentManager(),
                "ModalBottomSheet");
    }

}

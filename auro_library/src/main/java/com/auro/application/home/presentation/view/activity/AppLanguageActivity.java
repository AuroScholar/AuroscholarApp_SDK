package com.auro.application.home.presentation.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.network.ErrorResponseModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.databinding.ActivityAppLanguageBinding;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.LanguageMasterReqModel;
import com.auro.application.home.data.model.SelectLanguageModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.LanguageListResModel;
import com.auro.application.home.data.model.response.LanguageResModel;
import com.auro.application.home.presentation.view.adapter.LanguageAdapter;
import com.auro.application.home.presentation.viewmodel.AppLanguageViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.firebaseAnalytics.AnalyticsRegistry;
import com.auro.application.util.strings.AppStringDynamic;
import com.auroscholar.final_auroscholarapp_sdk.SDKDataModel;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppLanguageActivity extends BaseActivity implements View.OnClickListener, CommonCallBackListner {

    @Inject
    @Named("AppLanguageActivity")
    ViewModelFactory viewModelFactory;
    ActivityAppLanguageBinding binding;

    private AppLanguageViewModel viewModel;
    private Context mContext;
    LanguageAdapter adapter;
    List<SelectLanguageModel> laugList;
    boolean click = true;
    LanguageMasterDynamic language;
    PrefModel prefModel;
    Details details;
    String lang_id;
    String errormismatch="";
    LanguageMasterDynamic language1;
    Details languagedetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayout());
        //((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        DaggerWrapper.getComponent(this).doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AppLanguageViewModel.class);
        binding.setLifecycleOwner(this);
        mContext = AppLanguageActivity.this;
        AppUtil.loadAppLogo(binding.auroScholarLogo, this);
        //setContentView(R.layout.activity_app_language);

        init();
        setListener();
    }

    @Override
    protected void init() {

        setAdapterLanguage();
        AppStringDynamic.setAppLanguageStrings(binding);

        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        details = prefModel.getLanguageMasterDynamic().getDetails();
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    @Override
    protected void setListener() {
        binding.backButton.setOnClickListener(this);
        binding.userSelectionSheet.RpTeacher.setOnClickListener(this);
        binding.userSelectionSheet.RpStudent.setOnClickListener(this);
        binding.RPTextView9.setText(R.string.choose_language);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_app_language;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.backButton) {
            openFadeOutSelectionLayout();
            setAdapterLanguage();
            click = true;
        } else if (id == R.id.RpTeacher) {
            SharedPreferences.Editor editor1 = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
            editor1.putInt("session_usertype", AppConstant.UserType.TEACHER);

            editor1.apply();
            setValuesInPref(AppConstant.UserType.TEACHER);
            funnelChoose(AppConstant.UserType.TEACHER);
            openLoginActivity();
        } else if (id == R.id.RpStudent) {
            SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
            editor.putInt("session_usertype", AppConstant.UserType.STUDENT);

            editor.apply();
            setValuesInPref(AppConstant.UserType.STUDENT);
            funnelChoose(AppConstant.UserType.TEACHER);
            openLoginActivity();
        }
    }



    private void setValuesInPref(int type) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setUserType(type);
        AuroAppPref.INSTANCE.setPref(prefModel);
    }


    private void openLoginActivity() {
        Intent teacherIntent = new Intent(AppLanguageActivity.this, LoginActivity.class);
        startActivity(teacherIntent);
        finish();
    }
    public void calldashboard(){
        Intent i = new Intent(mContext, CompleteStudentProfileWithoutPin.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(i);
    }

    public void setAdapterLanguage() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        LanguageListResModel languageListResModel = prefModel.getLanguageListResModel();
        laugList = new ArrayList();
        if (languageListResModel != null && !languageListResModel.getLanguages().isEmpty()) {
            for (int i = 0; i < languageListResModel.getLanguages().size(); i++) {
                LanguageResModel languageResModel = languageListResModel.getLanguages().get(i);
                SelectLanguageModel selectLanguageModel = new SelectLanguageModel();
                selectLanguageModel.setLanguageId("" + languageResModel.getLanguageId());
               // selectLanguageModel.setLanguage(languageResModel.getLanguageName());
                selectLanguageModel.setLanguage(languageResModel.getTranslatedLanguageName());
                Log.d("langcode", languageResModel.getTranslatedLanguageName());
                selectLanguageModel.setCheck(false);
                selectLanguageModel.setLanguageShortCode(languageResModel.getShortCode());
                selectLanguageModel.setLanguageCode(languageResModel.getLanguageCode());
                laugList.add(selectLanguageModel);
            }
        }

        binding.recyclerViewlang.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerViewlang.setHasFixedSize(true);
        binding.recyclerViewlang.setNestedScrollingEnabled(false);
        adapter = new LanguageAdapter(laugList, this);
        binding.recyclerViewlang.setAdapter(adapter);
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case MESSAGE_SELECT_CLICK:
                setSDKAPIGrade();
                refreshList(commonDataModel);

                break;
        }
    }


    private void refreshList(CommonDataModel commonDataModel) {
        for (int i = 0; i < laugList.size(); i++) {
            if (i == commonDataModel.getSource()) {
                laugList.get(i).setCheck(true);
                 lang_id = laugList.get(i).getLanguageId();
                setLanguage(laugList.get(i).getLanguageShortCode(),laugList.get(i).getLanguageCode(), laugList.get(i).getLanguageId());
                // openFadeInSelectionLayout();
            } else {
                laugList.get(i).setCheck(false);
            }
        }
        callLanguageMasterApi();
        setSDKAPI(lang_id);
        if (click) {
            callLanguageMasterApi();
            //openFadeInSelectionLayout();
            click = false;
        }
        adapter.setData(laugList);
       // callLanguageMasterApi();
    }

    private void openFadeInSelectionLayout() {
        calldashboard();
        //Animation on button
//        binding.backButton.setVisibility(View.GONE);
//        binding.userSelectionSheet.sheetLayout.setVisibility(View.GONE);
//        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fadein);
//        binding.userSelectionSheet.sheetLayout.startAnimation(anim);

    }

    private void openFadeOutSelectionLayout() {
        //Animation on button
        binding.backButton.setVisibility(View.GONE);
        binding.userSelectionSheet.sheetLayout.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        binding.userSelectionSheet.sheetLayout.startAnimation(anim);

    }


    private void setLanguage(String shortcode,String languageCode, String languageId) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setUserLanguageShortCode(shortcode);
        prefModel.setUserLanguageId(languageId);
        prefModel.setUserLanguageCode(languageCode);
        AuroAppPref.INSTANCE.setPref(prefModel);
        ViewUtil.setLanguage();
        setLocale(languageCode);
    }

    public void setLocale(String lang) {
      /*  Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
        setScreenText();
    }

    private void funnelChoose(int chooseType) {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackSelectTeacherOrStudent(chooseType);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void setScreenText() {
        binding.RPTextView9.setText(this.getString(R.string.auro_app_language));
        binding.subHeadingText.setText(this.getString(R.string.auro_multi_language));
        binding.userSelectionSheet.rpLogin.setText(this.getString(R.string.auro_login_sign_up));
        binding.userSelectionSheet.RPTextView10.setText(this.getString(R.string.auro_choose_any_one_option));
        binding.userSelectionSheet.studentTextview.setText(this.getString(R.string.auro_parent));
        binding.userSelectionSheet.teacherTitle.setText(this.getString(R.string.auro_teacher));

        if (language != null && language.getDetails() != null) {
            try {
                Details details = language.getDetails();
                binding.RPTextView9.setText(details.getAuroAppLanguage());
                binding.subHeadingText.setText(details.getAuroMultiLanguage());
                binding.userSelectionSheet.rpLogin.setText(details.getAuroLoginSignUp());
                binding.userSelectionSheet.RPTextView10.setText(details.getAuroChooseAnyOneOption());
                binding.userSelectionSheet.studentTextview.setText(details.getAuroParent());
                binding.userSelectionSheet.teacherTitle.setText(details.getAuroTeacher());

            } catch (Exception e) {
                AppLogger.e("setScreenText", e.getMessage());
            }
        }
    }

    void callLanguageMasterApi() {
        progressChanges(0, details.getFetch_data() !=null ?details.getFetch_data() : AuroApp.getAppContext().getResources().getString(R.string.fetch_data));
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        LanguageMasterReqModel languageMasterReqModel = new LanguageMasterReqModel();
        languageMasterReqModel.setLanguageId(prefModel.getUserLanguageId());
        languageMasterReqModel.setUserTypeId("1");
        AppLogger.v("Language_pradeep", " DYNAMIC_LANGUAGE Step 1");
        viewModel.checkInternet(Status.DYNAMIC_LANGUAGE, languageMasterReqModel);

    }
    private void setSDKAPI(String langid)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String mobile = prefModel.getUserMobile();
        String partneruniueid = prefModel.getPartneruniqueid();
        String partnersource = prefModel.getPartnersource();
        String userid = prefModel.getUserId();
        String apikey = prefModel.getApikey();

        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("mobile_no",mobile);
        map_data.put("partner_unique_id",partneruniueid); //456456
        map_data.put("partner_source",partnersource);
        map_data.put("partner_api_key",apikey);
        map_data.put("user_id",userid);
        map_data.put("user_prefered_language_id",langid);

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
                                    Toast.makeText(AppLanguageActivity.this,message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }


                            }
                            else if (response.code() == 200) {
                                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                prefModel.setUserLanguageId(langid);
                                AuroAppPref.INSTANCE.setPref(prefModel);
                                getLanguage(langid);
                                          getProfile(userid);
                            }
                            else {
                                Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(mContext, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SDKDataModel> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

    }
    public void getLanguage(String language_id)
    {

        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("language_id",language_id);
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
                                    Toast.makeText(AppLanguageActivity.this,message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }


                            }
                            else if (response.code() == 200) {
                                languagedetail = response.body().getDetails();
                                language1 = (LanguageMasterDynamic) response.body();
                                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                prefModel.setLanguageMasterDynamic(language1);
                                AuroAppPref.INSTANCE.setPref(prefModel);

                            }
                            else {
                                Toast.makeText(AppLanguageActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(AppLanguageActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LanguageMasterDynamic> call, Throwable t) {
                        Toast.makeText(AppLanguageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void setSDKAPIGrade()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String mobile = prefModel.getUserMobile();
        String uniqueid = prefModel.getPartneruniqueid();
        String source = prefModel.getPartnersource();
        String apikey = prefModel.getApikey();
        String userid = prefModel.getUserId();
        String gradeid = prefModel.getUserclass();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("mobile_no",mobile);
        map_data.put("partner_unique_id",uniqueid); //456456
        map_data.put("partner_source",source);
        map_data.put("partner_api_key",apikey);
        map_data.put("user_id",userid);
        map_data.put("grade",gradeid);

        RemoteApi.Companion.invoke().getSDKDataerror(map_data)
                .enqueue(new Callback<ErrorResponseModel>() {
                    @Override
                    public void onResponse(Call<ErrorResponseModel> call, Response<ErrorResponseModel> response)
                    {
                        try {
                            if (response.code() == 400) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    ErrorResponseModel error = (ErrorResponseModel) response.body();
                                    errormismatch = message;

                                    Toast.makeText(AppLanguageActivity.this,message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }


                            }
                            else if (response.code() == 200) {
                                ErrorResponseModel error = (ErrorResponseModel) response.body();
                                 errormismatch = error.getMessage();
                                  //  getProfile(userid,resmessage);
                            }
                            else {
                                Toast.makeText(AppLanguageActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(AppLanguageActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ErrorResponseModel> call, Throwable t) {
                        Toast.makeText(AppLanguageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });


    }


    private void getProfile(String userid)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String partnersource = prefModel.getPartnersource();
        String parnteruniqueid = prefModel.getPartneruniqueid();
        String mobileno = prefModel.getUserMobile();
        int userclass = Integer.parseInt(prefModel.getUserclass());
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

                            if (errormismatch.equals("Error! Grade Mismatched")){
                                Intent i = new Intent(mContext, ChooseGradeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(i);
                            }

                            else if (response.body().getStatename().equals("")||response.body().getStatename().equals("null")||response.body().getStatename().equals(null)||response.body().getDistrictname().equals("")||response.body().getDistrictname().equals("null")||response.body().getDistrictname().equals(null)||response.body().getStudentName().equals("")||response.body().getStudentName().equals("null")||response.body().getStudentName().equals(null)||
                                    response.body().getStudentclass().equals("")||response.body().getStudentclass().equals("null")||response.body().getStudentclass().equals(null)||response.body().getStudentclass().equals("0")||response.body().getStudentclass().equals(0)){
                                Intent i = new Intent(mContext, CompleteStudentProfileWithoutPin.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(i);
                            }
                            else{

                                openGenricSDK(mobileno,partnersource,parnteruniqueid);

                            }

                        }
                        else
                        {
                            Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<GetStudentUpdateProfile> call, Throwable t)
                    {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void openGenricSDK(String mobileNumber,String partneruniqueid,String partnersource  ) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String userclass = prefModel.getUserclass();
        AuroScholarInputModel inputModel = new AuroScholarInputModel();
        inputModel.setMobileNumber(mobileNumber);
        inputModel.setStudentClass(userclass);
        inputModel.setPartner_unique_id(partneruniqueid);
        inputModel.setPartnerSource(partnersource);
        inputModel.setPartner_api_key("");
        inputModel.setActivity((Activity) mContext);
        AuroScholar.startAuroSDK(inputModel);
    }
    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {

                case SUCCESS:
                    progressChanges(1, "");
                    if (responseApi.apiTypeStatus == Status.DYNAMIC_LANGUAGE) {
                        language = (LanguageMasterDynamic) responseApi.data;
                        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                        prefModel.setLanguageMasterDynamic(language);
                        AuroAppPref.INSTANCE.setPref(prefModel);
                        AppLogger.v("Language_pradeep", "DYNAMIC_LANGUAGE-0" + language.getDetails().getAccept());
                        setScreenText();
                    }
                    break;

                case FAIL:
                case NO_INTERNET:
                    AppLogger.e("Error", (String) responseApi.data);
                    progressChanges(2, (String) responseApi.data);
                    break;


                default:
                    AppLogger.e("Error", (String) responseApi.data);
                    progressChanges(2, (String) responseApi.data);
                    break;
            }

        });
    }


    void progressChanges(int status, String msg) {
        AppLogger.e("progressChanges--", "" + status);
        switch (status) {
            case 0:
                binding.customProgressLayout.progressBar.setVisibility(View.VISIBLE);
                binding.customProgressLayout.textMsg.setVisibility(View.VISIBLE);
                binding.customProgressLayout.textMsg.setText(msg);
                binding.customProgressLayout.btRetry.setVisibility(View.GONE);
                binding.customProgressLayout.background.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
                binding.progressLayout.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.progressLayout.setVisibility(View.GONE);
                break;

            case 2:

                binding.customProgressLayout.progressBar.setVisibility(View.GONE);
                binding.customProgressLayout.textMsg.setVisibility(View.VISIBLE);
                binding.customProgressLayout.textMsg.setText(msg);
                binding.customProgressLayout.btRetry.setVisibility(View.VISIBLE);
                binding.customProgressLayout.background.setBackgroundColor(this.getResources().getColor(R.color.color_red));
                binding.customProgressLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callLanguageMasterApi();
                    }
                });
                binding.progressLayout.setVisibility(View.VISIBLE);
                break;
        }

    }
}
package com.auro.application.home.presentation.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
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
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.network.ErrorResponseModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.databinding.ActivityChooseGradeBinding;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.SelectLanguageModel;
import com.auro.application.home.data.model.response.CheckUserValidResModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.presentation.view.adapter.GradeChangeAdapter;
import com.auro.application.home.presentation.viewmodel.ChooseGradeViewModel;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;
import com.auroscholar.final_auroscholarapp_sdk.SDKDataModel;
import com.truecaller.android.sdk.ErrorResponse;

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

public class ChooseGradeActivity extends BaseActivity implements View.OnClickListener, CommonCallBackListner {


    @Inject
    @Named("ChooseGradeActivity")
    ViewModelFactory viewModelFactory;
    ActivityChooseGradeBinding binding;

    private ChooseGradeViewModel viewModel;
    private Context mContext;

    GradeChangeAdapter adapter;
    List<SelectLanguageModel> laugList;

    int grade = 0;
    String isComingFrom = "";

    PrefModel prefModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, getLayout());
//        ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        DaggerWrapper.getComponent(this).doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChooseGradeViewModel.class);
        binding.setLifecycleOwner(this);
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        this.mContext = ChooseGradeActivity.this;
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setLogin(true);
        SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor.putString("statusparentprofile", "false");
        editor.putString("statusfillstudentprofile", "false");
        editor.putString("statussetpasswordscreen", "false");
        editor.putString("statuschoosegradescreen", "true");
        editor.putString("statuschoosedashboardscreen", "false");
        editor.putString("statusopenprofilewithoutpin", "false");
        editor.apply();

        init();
        setListener();

    }

    @Override
    protected void init() {
        AppUtil.loadAppLogo(binding.auroScholarLogo, this);
        if (getIntent() != null) {
            isComingFrom = getIntent().getStringExtra(AppConstant.COMING_FROM);
        }
        setAdapterLanguage();

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }

        AppStringDynamic.setChooseGradeActivty(binding);
    }

    @Override
    protected void setListener() {
        binding.userSelectionSheet.buttonSelect.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_choose_grade;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonSelect) {//change comment here
            setClassInPref(grade);
            if (grade == 0) {
                ViewUtil.showSnackBar(binding.getRoot(), prefModel.getLanguageMasterDynamic().getDetails().getPlease_select_the_grade());
            } else {
                setSDKAPIGrade(String.valueOf(grade),"1");
                //callChangeGradeApi();

            }
        }
    }


    public void setAdapterLanguage() {
        //  String[] listArrayLanguage = getResources().getStringArray(R.array.classes);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        List<String> classList = prefModel.getStudentClasses();
        laugList = new ArrayList();
        if (!TextUtil.checkListIsEmpty(classList)) {
            for (int i = 0; i < classList.size(); i++) {
                SelectLanguageModel selectLanguageModel = new SelectLanguageModel();
                selectLanguageModel.setCheck(false);
                String classname = classList.get(i);
                selectLanguageModel.setStudentClassName(classList.get(i));
                if (classname.equalsIgnoreCase("1")) {
                    selectLanguageModel.setLanguage(classList.get(i) + "st");
                } else if (classname.equalsIgnoreCase("2")) {
                    selectLanguageModel.setLanguage(classList.get(i) + "nd");
                } else if (classname.equalsIgnoreCase("3")) {
                    selectLanguageModel.setLanguage(classList.get(i) + "rd");
                } else {
                    selectLanguageModel.setLanguage(classList.get(i) + "th");
                }

                laugList.add(selectLanguageModel);
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                SelectLanguageModel selectLanguageModel = new SelectLanguageModel();
                String classname = "" + i;
                selectLanguageModel.setStudentClassName("" + i);
                if (classname.equalsIgnoreCase("1")) {
                    selectLanguageModel.setLanguage(classname + "st");
                } else if (classname.equalsIgnoreCase("2")) {
                    selectLanguageModel.setLanguage(classname + "nd");
                } else if (classname.equalsIgnoreCase("3")) {
                    selectLanguageModel.setLanguage(classname + "rd");
                } else {
                    selectLanguageModel.setLanguage(classname + "th");
                }
                selectLanguageModel.setCheck(false);
                laugList.add(selectLanguageModel);
            }
        }

        binding.userSelectionSheet.rvClass.setLayoutManager(new GridLayoutManager(this, 3));
        binding.userSelectionSheet.rvClass.setHasFixedSize(true);
        binding.userSelectionSheet.rvClass.setNestedScrollingEnabled(false);
        adapter = new GradeChangeAdapter(laugList, this);
        binding.userSelectionSheet.rvClass.setAdapter(adapter);
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case MESSAGE_SELECT_CLICK:
                SelectLanguageModel model = (SelectLanguageModel) commonDataModel.getObject();
                grade = ConversionUtil.INSTANCE.convertStringToInteger(model.getStudentClassName());
                SharedPreferences.Editor editor1 = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                editor1.putString("gradeforsubjectpreference", String.valueOf(grade));
                editor1.apply();

                for (int i = 0; i < laugList.size(); i++) {
                    laugList.get(i).setCheck(i == commonDataModel.getSource());
                }
                setSDKAPI(String.valueOf(grade));

                //    reqModel.setNotification_message(list.get(commonDataModel.getSource()).getMessage());
                adapter.setData(laugList);

                break;
        }
    }
    private void setSDKAPI(String gradeid)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String mobile = prefModel.getUserMobile();
        String uniqueid = prefModel.getPartneruniqueid();
        String source = prefModel.getPartnersource();
        String apikey = prefModel.getApikey();
        String userid = prefModel.getUserId();
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
                                        Toast.makeText(ChooseGradeActivity.this,message, Toast.LENGTH_SHORT).show();
                                        if (message.equals("Error! Grade Mismatched")){
                                            buttonSelect(gradeid);
                                        }
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }


                                }
                                else if (response.code() == 200) {
                                    ErrorResponseModel error = (ErrorResponseModel) response.body();
                                    String resmessage = error.getMessage();
                                    if (resmessage.equals("Error! Grade Mismatched")){
                                        buttonSelect(gradeid);
                                    }



                                }
                                else {
                                    Toast.makeText(ChooseGradeActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (Exception e) {
                                Toast.makeText(ChooseGradeActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ErrorResponseModel> call, Throwable t) {
                            Toast.makeText(ChooseGradeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });


    }

    private void setSDKAPIGrade(String gradeid, String gradestatus)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String mobile = prefModel.getUserMobile();
        String uniqueid = prefModel.getPartneruniqueid();
        String source = prefModel.getPartnersource();
        String apikey = prefModel.getApikey();
        String userid = prefModel.getUserId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("mobile_no",mobile);
        map_data.put("partner_unique_id",uniqueid); //456456
        map_data.put("partner_source",source);
        map_data.put("partner_api_key",apikey);
        map_data.put("user_id",userid);
        map_data.put("grade",gradeid);
        map_data.put("update_grade",gradestatus);

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
                                    Toast.makeText(ChooseGradeActivity.this,message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }


                            }
                            else if (response.code() == 200) {
                                if (gradestatus.equals("1")){
                                    PrefModel prefModel1 = AuroAppPref.INSTANCE.getModelInstance();
                                    prefModel1.setUserclass(gradeid);
                                }

                                getProfile();
                            }
                            else {
                                Toast.makeText(ChooseGradeActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(ChooseGradeActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ErrorResponseModel> call, Throwable t) {
                        Toast.makeText(ChooseGradeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });


    }
    public void buttonSelect(String userclass) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your grade is mismatching with our record. Do you want to update ?");
        builder.setMessage("warning : if yes your existing monthly quiz assessment & wallet amount will be erased.");

        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + this.getString(R.string.yes) + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                binding.userSelectionSheet.buttonSelect.setTextColor(AuroApp.getAppContext().getResources().getColor(R.color.white));
                binding.userSelectionSheet.buttonSelect.setBackground(AuroApp.getAppContext().getResources().getDrawable(R.drawable.button_submit));

               // dialog.dismiss();
            }
        });
        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>" + this.getString(R.string.no) + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setSDKAPIGrade(userclass,"2");

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }
    private void getProfile()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String partnersource = prefModel.getPartnersource();
        String parnteruniqueid = prefModel.getPartneruniqueid();
        String mobileno = prefModel.getUserMobile();
        String userid = prefModel.getUserId();
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



                             if (response.body().getStatename().equals("")||response.body().getStatename().equals("null")||response.body().getStatename().equals(null)||response.body().getDistrictname().equals("")||response.body().getDistrictname().equals("null")||response.body().getDistrictname().equals(null)||response.body().getStudentName().equals("")||response.body().getStudentName().equals("null")||response.body().getStudentName().equals(null)||
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
        inputModel.setPartnerSource(prefModel.getPartnersource());
        inputModel.setPartner_api_key("");
        inputModel.setActivity((Activity) mContext);

        AuroScholar.startAuroSDK(inputModel);
    }
    private void setClassInPref(int studentClass) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.getUserclass();
        AuroAppPref.INSTANCE.setPref(prefModel);

    }

    private void openUserProfileActivity() {
        Intent newIntent = new Intent(ChooseGradeActivity.this, StudentProfileActivity.class);
        startActivity(newIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void handleProgress(int i) {
        switch (i) {
            case 0:
                binding.progressbar.pgbar.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.progressbar.pgbar.setVisibility(View.GONE);
                break;
        }
    }


    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    /*do coding here*/
                    break;

                case SUCCESS:
                    // if (isVisible()) {
                    handleProgress(1);
                    // handleApiRes(responseApi);
                    setClassInPref(grade);
                    if (isComingFrom!=null && isComingFrom.equalsIgnoreCase(AppConstant.ComingFromStatus.COMING_FROM_LOGIN_WITH_OTP)) {
                        Intent i = new Intent(this, DashBoardMainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        openUserProfileActivity();
                    }
                    //}
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                    // if(isVisible()) {
                    handleProgress(1);
                    showSnackbarError((String) responseApi.data);
                    // }
                    break;
                default:
                    //if(isVisible()) {
                    handleProgress(1);
                    showSnackbarError((String) responseApi.data);
                    //}
                    break;
            }

        });
    }


    private void callChangeGradeApi() {

        handleProgress(0);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        CheckUserValidResModel reqModel = new CheckUserValidResModel();
        reqModel.setMobileNo(prefModel.getUserId());
        reqModel.setStudentClass("" + grade);
        viewModel.changeGrade(reqModel);
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

}
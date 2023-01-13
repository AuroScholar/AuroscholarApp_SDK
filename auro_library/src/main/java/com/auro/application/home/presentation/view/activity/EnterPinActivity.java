package com.auro.application.home.presentation.view.activity;

import static com.auro.application.core.common.Status.SEND_OTP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.ActivityEnterPinBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.SendOtpResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.signupmodel.request.SetUsernamePinReqModel;
import com.auro.application.home.data.model.signupmodel.response.SetUsernamePinResModel;
import com.auro.application.home.presentation.viewmodel.SetPinViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPinActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    @Named("EnterPinActivity")
    ViewModelFactory viewModelFactory;
    ActivityEnterPinBinding binding;
    SetPinViewModel viewModel;
    UserDetailResModel resModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayout());
        ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SetPinViewModel.class);
        binding.setLifecycleOwner(this);
        if (getIntent() != null) {
            resModel = (UserDetailResModel) getIntent().getParcelableExtra(AppConstant.USER_PROFILE_DATA_MODEL);
        }
        setListener();
        AppStringDynamic.setEnterPinPagetrings(binding);
    }

    @Override
    protected void setListener() {
        binding.btContinue.setOnClickListener(this);
        binding.backButton.setOnClickListener(this);
        binding.forgotPassword.setOnClickListener(this);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_enter_pin;
    }

    @Override
    public void onClick(View v) {
        ViewUtil.hideKeyboard(this);
        int id = v.getId();
        if (id == R.id.bt_continue) {
            callLoginPinApi();
        } else if (id == R.id.back_button) {
            onBackPressed();
        } else if (id == R.id.forgotPassword) {
            sendOtpApi();
        }
    }


    private void callLoginPinApi() {
        AppLogger.e("onClick--", "step 2");
        String pin = binding.pinView.getText().toString();
        Details details1 = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        if (pin.isEmpty()) {
            ViewUtil.showSnackBar(binding.getRoot(), details1.getEnterPin());
            return;
        }
        else if (pin.length() < 4){
            ViewUtil.showSnackBar(binding.getRoot(), "Please enter 4 digits pin");
            return;
        }




            else {
            binding.progressbar.pgbar.setVisibility(View.VISIBLE);
            SetUsernamePinReqModel mreqmodel = new SetUsernamePinReqModel();
            mreqmodel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
            mreqmodel.setPin(pin);
            viewModel.checkInternet(mreqmodel, Status.LOGIN_PIN_API);
        }
    }


    private void observeServiceResponse() {
        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {

                case SUCCESS:
                    AppLogger.e("sendOtpApi-", "setp 2");
                    if (responseApi.apiTypeStatus == SEND_OTP) {
                        AppLogger.e("sendOtpApi-", "setp 3");
                        SendOtpResModel sendOtp = (SendOtpResModel) responseApi.data;
                        if (!sendOtp.getError()) {
                            AppLogger.e("sendOtpApi-", "setp 5");
                            startActivityEnterOtpScreen();
                        } else {
                            ViewUtil.showSnackBar(binding.getRoot(), resModel.getMessage());
                        }
                    } else {
                        AppLogger.e("sendOtpApi-", "setp 4");
                        SetUsernamePinResModel resModel = (SetUsernamePinResModel) responseApi.data;
                        binding.progressbar.pgbar.setVisibility(View.GONE);
                        if (resModel.getError()) {
                            ViewUtil.showSnackBar(binding.getRoot(), resModel.getMessage());
                        } else {
                            checkWhichScreenOpen();
                        }
                    }
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                default:
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    break;
            }

        });
    }

    private void checkWhichScreenOpen() {
        UserDetailResModel resModel = AuroAppPref.INSTANCE.getModelInstance().getStudentData();
        if (AuroAppPref.INSTANCE.getModelInstance().getUserType()==0){
            getProfile();
//            Intent i = new Intent(this, DashBoardMainActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
        }
        else if(AuroAppPref.INSTANCE.getModelInstance().getUserType()==1){
            Intent i = new Intent(this, ParentProfileActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else if (resModel != null && resModel.getGrade().equalsIgnoreCase("0") || resModel.getGrade().equals("0") || resModel.getGrade() == "0") {
            openChooseGradeActivity();
        } else {
            startDashboardActivity();
        }
    }
    private void getProfile()
    {
        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String parentchilduseridforaddchild = prefs.getString("parentchilduseridforaddchild", "");


        String suserid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",suserid);
        //0000898904,123456

        RemoteApi.Companion.invoke().getStudentData(map_data)
                .enqueue(new Callback<GetStudentUpdateProfile>()
                {
                    @Override
                    public void onResponse(Call<GetStudentUpdateProfile> call, Response<GetStudentUpdateProfile> response)
                    {
                        try {


                            if (response.isSuccessful()) {
                                if (response.body().getStatename().equals("") || response.body().getStatename().equals("null") || response.body().getStatename().equals(null) || response.body().getDistrictname().equals("") || response.body().getDistrictname().equals("null") || response.body().getDistrictname().equals(null) || response.body().getStudentName().equals("") || response.body().getStudentName().equals("null") || response.body().getStudentName().equals(null) ||
                                        response.body().getStudentclass().equals("") || response.body().getStudentclass().equals("null") || response.body().getStudentclass().equals(null) || response.body().getStudentclass().equals("0") || response.body().getStudentclass().equals(0)) {

                                    Intent i = new Intent(EnterPinActivity.this, CompleteStudentProfileWithoutPin.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, resModel);
                                    startActivity(i);
                                } else {
                                    Intent i = new Intent(EnterPinActivity.this, DashBoardMainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, resModel);
                                    startActivity(i);
                                }


                            } else {
                                Toast.makeText(EnterPinActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            Toast.makeText(EnterPinActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetStudentUpdateProfile> call, Throwable t)
                    {
                        Toast.makeText(EnterPinActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openChooseGradeActivity() {
        Intent tescherIntent = new Intent(this, ChooseGradeActivity.class);
        tescherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(tescherIntent);
        finish();
    }

    private void startDashboardActivity() {
        Intent i = new Intent(this, DashBoardMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    public void startActivityEnterOtpScreen() {
        Intent i = new Intent(this, OtpActivity.class);
        i.putExtra(getResources().getString(R.string.intent_phone_number), resModel.getUserMobile());
        i.putExtra(AppConstant.COMING_FROM, AppConstant.ComingFromStatus.COMING_FORM_PIN_FORGOT_PIN);
        startActivity(i);
        finish();
    }

    private void sendOtpApi() {
        AppLogger.e("sendOtpApi-", "setp 1");
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
        SendOtpReqModel mreqmodel = new SendOtpReqModel();
        mreqmodel.setMobileNo(resModel.getUserMobile());
        viewModel.checkInternet(mreqmodel, SEND_OTP);
    }

}
package com.auro.application.home.presentation.view.activity;

import android.content.Intent;
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
import com.auro.application.core.common.ErrorStatus;
import com.auro.application.core.common.ResponseStatus;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityForgotPinBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.signupmodel.request.SetUsernamePinReqModel;
import com.auro.application.home.data.model.signupmodel.response.SetUsernamePinResModel;
import com.auro.application.home.presentation.viewmodel.SetPinViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;
import com.google.gson.JsonObject;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentForgotPinActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    @Named("ForgotPinActivity")
    ViewModelFactory viewModelFactory;
    ActivityForgotPinBinding binding;

    SetPinViewModel viewModel;
    UserDetailResModel resModel;
    PrefModel prefModel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        setListener();
        //setContentView(R.layout.activity_set_pin);
    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayout());
      //  ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
       // viewModel = ViewModelProviders.of(this, viewModelFactory).get(SetPinViewModel.class);
        binding.setLifecycleOwner(this);
        if (getIntent() != null) {
            resModel = (UserDetailResModel) getIntent().getParcelableExtra(AppConstant.USER_PROFILE_DATA_MODEL);
        }
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppStringDynamic.setForgetPinActivityStrings(binding);
    }

    @Override
    protected void setListener() {
        binding.backButton.setOnClickListener(this);
        binding.btDoneNew.setOnClickListener(this);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
         //   observeServiceResponse();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_forgot_pin;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_button) {
            onBackPressed();
        } else if (id == R.id.bt_done_new) {
            AppLogger.e("onClick--", "step 1");
            //  callSetPinApi();
            String pin = binding.pinView.getText().toString();
            String confirmpin = binding.confirmPin.getText().toString();
            Details details1 = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

            if (pin.isEmpty() || pin.equals("")) {
                Toast.makeText(this, details1.getEnter_the_pin(), Toast.LENGTH_SHORT).show();
            } else if (pin.length() < 4) {
                Toast.makeText(this, "Please enter 4 digits pin", Toast.LENGTH_SHORT).show();

            } else if (confirmpin.isEmpty() || confirmpin.equals("")) {
                Toast.makeText(this, details1.getEnter_the_confirm_pin(), Toast.LENGTH_SHORT).show();

            } else if (confirmpin.length() < 4) {
                Toast.makeText(this, "Please enter 4 digits confirm pin", Toast.LENGTH_SHORT).show();

            } else if (pin == confirmpin || pin.equals(confirmpin)) {
                String pinnew = binding.pinView.getText().toString();
                setpin();
            } else {
                Toast.makeText(this, details1.getPin_and_confirm_not_match(), Toast.LENGTH_SHORT).show();
            }
        }
    }






    private void callSetPinApi() {
        ViewUtil.hideKeyboard(this);
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        String pin = binding.pinView.getText().toString();
        String confirmPin = binding.confirmPin.getText().toString();
        if (pin.isEmpty() || pin.length() < 4) {
            ViewUtil.showSnackBar(binding.getRoot(),details.getEnter_the_pin() != null ? details.getEnter_the_pin(): AuroApp.getAppContext().getResources().getString(R.string.enter_the_pin) );
            return;
        } else if (confirmPin.isEmpty() || confirmPin.length() < 4) {
            ViewUtil.showSnackBar(binding.getRoot(),details.getEnter_the_confirm_pin() != null ? details.getEnter_the_confirm_pin(): AuroApp.getAppContext().getResources().getString(R.string.enter_the_confirm_pin));
            return;
        } else if (!pin.equalsIgnoreCase(confirmPin)) {
                ViewUtil.showSnackBar(binding.getRoot(), details.getPin_and_confirm_not_match() != null ? details.getPin_and_confirm_not_match(): AuroApp.getAppContext().getResources().getString(R.string.pin_and_confirm_not_match));
            return;
        } else {
            AppLogger.e("callSetPinApi--", "step 1");
            binding.progressbar.pgbar.setVisibility(View.VISIBLE);
            SetUsernamePinReqModel mreqmodel = new SetUsernamePinReqModel();
            mreqmodel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
            mreqmodel.setPin(pin);
            mreqmodel.setUserName("");
            viewModel.checkInternet(mreqmodel, Status.SET_USER_PIN);

        }
    }

    private void observeServiceResponse() {
        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    AppLogger.e("callSetPinApi--", "step 4");
                    SetUsernamePinResModel resModel = (SetUsernamePinResModel) responseApi.data;
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    if (resModel.getError()) {
                        AppLogger.e("callSetPinApi--", "step 5");
                        ViewUtil.showSnackBar(binding.getRoot(), resModel.getMessage());
                    } else {
                        AppLogger.e("callSetPinApi--", "step 6");
                        checkWhichScreenOpen();

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


    private void checkWhichScreenOpen() {
        UserDetailResModel resModel = AuroAppPref.INSTANCE.getModelInstance().getStudentData();
        if (resModel != null && resModel.getGrade().equalsIgnoreCase("0")) {
            openChooseGradeActivity();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finishAffinity();
        }
    }

    private void setpin()
    {

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String userid = prefModel.getCheckUserResModel().getUserDetails().get(0).getUserId();
       String langid = prefModel.getUserLanguageId();
        String pin = binding.pinView.getText().toString();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);
        map_data.put("pin", pin);
        map_data.put("user_prefered_language_id",langid);
        RemoteApi.Companion.invoke().setparentpin(map_data)
                .enqueue(new Callback<CheckUserResModel>()
                {
                    @Override
                    public void onResponse(Call<CheckUserResModel> call, Response<CheckUserResModel> response) {
                        try {
                            if (response.code() == 400) {
                                Toast.makeText(ParentForgotPinActivity.this, "Error! This pin is already used in your other account. Please enter other pin!", Toast.LENGTH_SHORT).show();


                            } else if (response.code() == 200) {
                                Toast.makeText(ParentForgotPinActivity.this, "PIN Updated Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ParentForgotPinActivity.this, LoginActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(ParentForgotPinActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                                Log.d("Responseerror", "onResponse: " + response.body().toString());
                            }
                        } catch (Exception e) {
                            Toast.makeText(ParentForgotPinActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserResModel> call, Throwable t)
                    {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
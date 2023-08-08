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
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityEnterPinBinding;
import com.auro.application.databinding.ActivitySetPinBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.SchoolData;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.signupmodel.response.RegisterApiResModel;
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

public class EnterParentPinActivity extends BaseActivity implements View.OnClickListener {

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
       // ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
       // viewModel = ViewModelProviders.of(this, viewModelFactory).get(SetPinViewModel.class);
        binding.setLifecycleOwner(this);
//        if (getIntent() != null) {
        binding.forgotPassword.setVisibility(View.VISIBLE);
//            resModel = (UserDetailResModel) getIntent().getParcelableExtra(AppConstant.USER_PROFILE_DATA_MODEL);
//        }
        setListener();
        AppStringDynamic.setEnterPinPagetrings(binding);
    }

    @Override
    protected void setListener() {
        binding.btContinue.setOnClickListener(this);
        binding.backButton.setOnClickListener(this);
        binding.forgotPassword.setOnClickListener(this);

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
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            String username = prefModel.getCheckUserResModel().getUserDetails().get(0).getUserName();
            Details details1 = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();


            if (binding.pinView.getText().toString().length() < 4) {
                Toast.makeText(this, "Please enter 4 digits pin", Toast.LENGTH_SHORT).show();
            } else if (binding.pinView.getText().toString().equals("") || binding.pinView.getText().toString().isEmpty()) {
                Toast.makeText(this, details1.getEnter_the_pin(), Toast.LENGTH_SHORT).show();
            } else {
                getProfile(username);
            }
        } else if (id == R.id.back_button) {
            onBackPressed();
        } else if (id == R.id.forgotPassword) {
            Intent intent = new Intent(EnterParentPinActivity.this, ParentForgotPinActivity.class);
            startActivity(intent);
        }
    }



    private void getProfile(String user_name)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_name",user_name);
        map_data.put("user_type", String.valueOf(0));
        map_data.put("user_prefered_language_id",languageid);
        RemoteApi.Companion.invoke().getUserCheck(map_data)
                .enqueue(new Callback<CheckUserResModel>()
                {
                    @Override
                    public void onResponse(Call<CheckUserResModel> call, Response<CheckUserResModel> response)
                    {
                        try {


                            if (response.isSuccessful()) {
                                if (!response.body().getUserDetails().get(0).getPin().isEmpty() || !response.body().getUserDetails().get(0).getPin().equals("")) {
                                    if (response.body().getUserDetails().get(0).getPin().equals(binding.pinView.getText().toString())) {
                                        Intent i = new Intent(getApplicationContext(), ParentProfileActivity.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(EnterParentPinActivity.this, "Invalid Pin", Toast.LENGTH_SHORT).show();
                                    }

                                }


                            } else {
                                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(EnterParentPinActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();

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
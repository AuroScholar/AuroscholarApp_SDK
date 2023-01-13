package com.auro.application.home.presentation.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityEnterPinBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.presentation.viewmodel.SetPinViewModel;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterStudentPinActivity extends BaseActivity implements View.OnClickListener {

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
        binding.forgotPassword.setVisibility(View.GONE);
//            resModel = (UserDetailResModel) getIntent().getParcelableExtra(AppConstant.USER_PROFILE_DATA_MODEL);
//        }
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
            SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
            String childid = prefs.getString("studentuserid", "");
            getProfile(childid);
        } else if (id == R.id.back_button) {
            onBackPressed();
        }
    }



    private void getProfile(String user_name)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        String pin = binding.pinView.getText().toString();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",user_name);
        map_data.put("pin", pin);
        RemoteApi.Companion.invoke().getStudentLogin(map_data)
                .enqueue(new Callback<CheckUserResModel>()
                {
                    @Override
                    public void onResponse(Call<CheckUserResModel> call, Response<CheckUserResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {


                                String childuserid = response.body().getUserId();
                                SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                editor.putString("newuseridfordashboard", childuserid);
                                editor.apply();
                                Intent i = new Intent(getApplicationContext(), DashBoardMainActivity.class);
                                startActivity(i);


                            } else {
                                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(EnterStudentPinActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
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
package com.auro.application.home.presentation.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.home.data.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.ActivitySetPinBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
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

public class UpdateChildPinActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    @Named("SetPinActivity")
    ViewModelFactory viewModelFactory;
    ActivitySetPinBinding binding;

    SetPinViewModel viewModel;
    UserDetailResModel resModel;
    String isComingFrom = "";
    CheckUserResModel checkUserResModel;
    RegisterApiResModel registerApiResModel;
    UserDetailResModel userDetailForSecondStudent;


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
       // ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
       // viewModel = ViewModelProviders.of(this, viewModelFactory).get(SetPinViewModel.class);
        binding.setUserName.setVisibility(View.GONE);
        binding.mobileLayout.setVisibility(View.GONE);

        binding.setLifecycleOwner(this);
        if (getIntent() != null) {
            resModel = (UserDetailResModel) getIntent().getParcelableExtra(AppConstant.USER_PROFILE_DATA_MODEL);
            isComingFrom = getIntent().getStringExtra(AppConstant.COMING_FROM);

        }
        binding.titleFirst.setVisibility(View.GONE);
       // if (isComingFrom.equalsIgnoreCase(AppConstant.ComingFromStatus.COMING_FROM_ADD_STUDENT)) {
            binding.titleFirst.setText(this.getString(R.string.set_pin));
       // } else {
            binding.titleFirst.setText(this.getString(R.string.set_pin));
       // }
        AppStringDynamic.setPinPagetrings(binding);

    }

    @Override
    protected void setListener() {
        binding.backButton.setOnClickListener(this);
        binding.btDoneNew.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_set_pin;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_button) {
            onBackPressed();
        } else if (id == R.id.bt_done_new) {
            AppLogger.e("onClick--", "step 1");
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
                setchildpin(pin);
            } else {
                Toast.makeText(this, details1.getPin_and_confirm_not_match(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setchildpin(String pin)
    {
        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String changepinstudentuserid = prefs.getString("changepinstudentuserid", "");
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",changepinstudentuserid);
        map_data.put("pin",pin);
        RemoteApi.Companion.invoke().setparentuserpin(map_data)
                .enqueue(new Callback<SetUsernamePinResModel>() {
                    @Override
                    public void onResponse(Call<SetUsernamePinResModel> call, Response<SetUsernamePinResModel> response)
                    {
                        try {
                            if (response.code() == 400) {


                                Toast.makeText(UpdateChildPinActivity.this, "Error! This pin is already used in your other account. Please enter other pin!", Toast.LENGTH_SHORT).show();
                                ViewUtil.showSnackBar(binding.getRoot(), response.message());

                            } else if (response.isSuccessful()) {


                                String message = response.body().getMessage();
                                Toast.makeText(UpdateChildPinActivity.this, message, Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i = new Intent(getApplicationContext(), ChildAccountsActivity.class);
                                startActivity(i);
                                finish();


                            } else {
                                ViewUtil.showSnackBar(binding.getRoot(), response.message());
                                Toast.makeText(UpdateChildPinActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(UpdateChildPinActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SetUsernamePinResModel> call, Throwable t)
                    {
                        ViewUtil.showSnackBar(binding.getRoot(),t.getMessage());
                        Toast.makeText(UpdateChildPinActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }




















}
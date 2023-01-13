
package com.auro.application.home.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.network.URLConstant;
import com.auro.application.core.util.uiwidget.RPEditText;
import com.auro.application.databinding.ActivityRegisterUserBinding;
import com.auro.application.home.data.model.SetPasswordReqModel;
import com.auro.application.home.presentation.viewmodel.LoginScreenViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;

import javax.inject.Inject;
import javax.inject.Named;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, CommonCallBackListner {
    private static String TAG = RegisterActivity.class.getSimpleName();
    @Inject
    @Named("RegisterActivity")
    ViewModelFactory viewModelFactory;
    ActivityRegisterUserBinding binding;
    LoginScreenViewModel viewModel;
    String comingFrom = "";

    PrefModel prefModel;
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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginScreenViewModel.class);
        binding.setLifecycleOwner(this);
        if (getIntent() != null) {
            comingFrom = getIntent().getStringExtra(AppConstant.COMING_FROM);
        }
        // setCustomDialerAdapter();
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        setListener();
        ViewUtil.customTextView(binding.termsCondition, this);

        if (comingFrom.equalsIgnoreCase(AppConstant.FROM_SET_PASSWORD)) {
            binding.headText.setText(prefModel.getLanguageMasterDynamic().getDetails().getSetANewPassword());
            binding.RPAccept.setText(prefModel.getLanguageMasterDynamic().getDetails().getSetPassword());
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setCurrentScreenFlag(AppConstant.CurrentFlagStatus.SET_PASSWORD);
            AuroAppPref.INSTANCE.setPref(prefModel);
        } else {
            binding.headText.setText(prefModel.getLanguageMasterDynamic().getDetails().getResetPassword());
            binding.RPAccept.setText(prefModel.getLanguageMasterDynamic().getDetails().getResetPassword());

        }
        AppUtil.loadAppLogo(binding.auroScholarLogo,this);

    }

    @Override
    protected void setListener() {
        binding.backButton.setOnClickListener(this);
        binding.RPAccept.setOnClickListener(this);
        binding.passwordIcon.setOnClickListener(this);
        binding.confirmpasswordIcon.setOnClickListener(this);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);

        } else {
            observeServiceResponse();
        }
        binding.RPButtonSendOtp.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_register_user;
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    /*Loading code here*/
                    break;

                case SUCCESS:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getSet_password_sucessfully());
                    checkForGradeScreen();
                    break;

                case NO_INTERNET:
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    break;

                case AUTH_FAIL:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    break;

                case FAIL_400:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    break;

                default:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    break;
            }

        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_button) {
            finish();
        } else if (id == R.id.RPAccept) {
            setPasswordApi();
        } else if (id == R.id.passwordIcon) {
            handleIconClickPassword(binding.etPassword, binding.passwordIcon);
        } else if (id == R.id.confirmpasswordIcon) {
            handleIconClickPassword(binding.etconfirmPassword, binding.confirmpasswordIcon);
        }
    }

    void handleIconClickPassword(RPEditText editText, ImageView passwordIcon) {
        String password = editText.getText().toString();
        TransformationMethod transformationMethod = editText.getTransformationMethod();
        if (transformationMethod == null) {
            editText.setTransformationMethod(new PasswordTransformationMethod());
            passwordIcon.setColorFilter(null); // Add tint color
        } else {
            editText.setTransformationMethod(null);
            passwordIcon.setColorFilter(getResources().getColor(R.color.blue_color)); // Add tint color
        }
        if (!TextUtil.isEmpty(password)) {
            editText.setSelection(password.length());
        }
    }



    private void checkForGradeScreen() {



        if (!TextUtil.isEmpty(comingFrom) && comingFrom.equalsIgnoreCase(AppConstant.FROM_FORGOT_PASSWORD)) {
            openLoginActivity();
        } else {
            int studentClass=0;
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            if(!prefModel.getStudentData().getGrade().isEmpty()) {
                studentClass = ConversionUtil.INSTANCE.convertStringToInteger(prefModel.getStudentData().getGrade());
            }

            AppLogger.e(TAG, "--" + prefModel.getStudentName());
            AppLogger.e(TAG, "--" +studentClass);
            if (prefModel.getUserType() == AppConstant.userTypeLogin.STUDENT && studentClass > 0) {
                startDashboardActivity();
            } else if (prefModel.getUserType() == AppConstant.userTypeLogin.STUDENT && studentClass== 0) {
                openChooseGradeActivity();
            } else {
                startDashboardActivity();
            }
        }
    }

    private void openLoginActivity() {
        Intent newIntent = new Intent(this, LoginActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
        finish();
    }

    private void openChooseGradeActivity() {
        setEmptyForCurrentFlag();
        Intent tescherIntent = new Intent(this, ChooseGradeActivity.class);
        startActivity(tescherIntent);
        finish();
    }

    private void setEmptyForCurrentFlag() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setCurrentScreenFlag("");
        AuroAppPref.INSTANCE.setPref(prefModel);
    }

    private void startDashboardActivity() {
        setEmptyForCurrentFlag();
        Intent i = new Intent(this, DashBoardMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void setPasswordApi() {
        ViewUtil.hideKeyboard(this);
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etconfirmPassword.getText().toString();
        if (TextUtil.isEmpty(password)) {
            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getEnter_the_password());
            return;
        } else if (password.length() < 6) {
            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getPassword_should_have());
            return;
        } else if (TextUtil.isEmpty(confirmPassword)) {
            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getEnter_get_confirm_password());
            return;
        } else if (confirmPassword.length() < 6) {
            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getConfirm_password_should_have());
            return;
        } else if (!password.equals(confirmPassword)) {
            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getPassword_confirm_password());
            return;
        }


        SetPasswordReqModel reqmodel = new SetPasswordReqModel();
        reqmodel.setMobileNo(prefModel.getUserName());
        reqmodel.setPassword(password);
        reqmodel.setUserId(prefModel.getStudentData().getUserId());
        viewModel.checkInternet(reqmodel, Status.SET_PASSWORD);
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getSource()) {
            case AppConstant.PRIVACY_POLICY_TEXT:
                openWebActivty(URLConstant.PRIVACY_POLICY);

                break;

            case AppConstant.TERMS_CONDITION_TEXT:
                openWebActivty(URLConstant.TERM_CONDITION);
                break;

        }
    }
    private void openWebActivty(String link) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(AppConstant.WEB_LINK, link);
        startActivity(intent);
    }

}
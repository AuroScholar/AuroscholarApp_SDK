package com.auro.application.home.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.auro.application.databinding.EnterNumberActivityLayoutBinding;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.response.SendOtpResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.signupmodel.DialerModel;
import com.auro.application.home.presentation.view.adapter.CustomDialerAdapter;
import com.auro.application.home.presentation.viewmodel.LoginScreenViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.firebaseAnalytics.AnalyticsRegistry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;


public class EnterNumberActivity extends BaseActivity implements View.OnClickListener, CommonCallBackListner {


    private static String TAG = LoginActivity.class.getSimpleName();
    @Inject
    @Named("EnterNumberActivity")
    ViewModelFactory viewModelFactory;
    EnterNumberActivityLayoutBinding binding;
    LoginScreenViewModel viewModel;
    CheckUserResModel checkUserResModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setLanguageonUi(this);
        init();


    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayout());
        ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginScreenViewModel.class);
        binding.setLifecycleOwner(this);

        setCustomDialerAdapter();
        setListener();
        ViewUtil.customTextView(binding.termsCondition, this);

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setSrId("");
        AuroAppPref.INSTANCE.setPref(prefModel);

    }


    @Override
    protected void setListener() {
        binding.backButton.setOnClickListener(this);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);

        } else {
            observeServiceResponse();
        }
        binding.RPButtonSendOtp.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.enter_number_activity_layout;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.RPButtonSendOtp) {
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            int userType = prefModel.getUserType();
            AppLogger.e("enter Number Activity --", "" + userType);
            if (userType == AppConstant.UserType.TEACHER) {
                oldSendOtpApi();
            } else {
                sendOtpApiReqPass();
            }
        } else if (id == R.id.back_button) {
            finish();
        }
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

        AppLogger.e(TAG, "commonEventListner step 1");
        switch (commonDataModel.getClickType()) {
            case DIALER_CALL_BACK_LISTNER:
                DialerModel dialerModel = (DialerModel) commonDataModel.getObject();
                binding.numberEdit.append(dialerModel.getNumber());
                if (binding.numberEdit.length() == 10) {
                    buttonSelect(true);
                } else {
                    buttonSelect(false);
                }
                break;

            case CLEAR_EDIT_TEXT:
                String text = binding.numberEdit.getText().toString();
                if (text.length() > 0) {
                    binding.numberEdit.setText(text.substring(0, text.length() - 1));
                    buttonSelect(false);
                }

                break;

            case PRIVACY_POLICY_TEXT:
                openWebActivty(URLConstant.PRIVACY_POLICY);
                AppLogger.e(TAG, "commonEventListner step 2");

                break;

            case TERMS_CONDITION_TEXT:
                openWebActivty(URLConstant.TERM_CONDITION);
                AppLogger.e(TAG, "commonEventListner step 3");

                break;

        }

    }

    private void openWebActivty(String link) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(AppConstant.WEB_LINK, link);
        startActivity(intent);
    }


    private void setCustomDialerAdapter() {

        GridLayoutManager gridlayout = new GridLayoutManager(this, 3);
        gridlayout.setOrientation(LinearLayoutManager.VERTICAL);
        binding.dialerRecyclerView.setLayoutManager(gridlayout);
        binding.dialerRecyclerView.setHasFixedSize(true);
        binding.dialerRecyclerView.setNestedScrollingEnabled(false);
        CustomDialerAdapter customDialerAdapter = new CustomDialerAdapter(this, getDialarList(), this);
        binding.dialerRecyclerView.setAdapter(customDialerAdapter);
    }

    public void buttonSelect(boolean status) {
        if (status) {
            binding.RPButtonSendOtp.setVisibility(View.VISIBLE);
            binding.RPButtonSendOtp.setTextColor(AuroApp.getAppContext().getResources().getColor(R.color.white));
            binding.RPButtonSendOtp.setBackground(AuroApp.getAppContext().getResources().getDrawable(R.drawable.button_submit));
        } else {
            binding.RPButtonSendOtp.setVisibility(View.GONE);
            binding.RPButtonSendOtp.setTextColor(AuroApp.getAppContext().getResources().getColor(R.color.auro_buton_text_blue));
            binding.RPButtonSendOtp.setBackground(AuroApp.getAppContext().getResources().getDrawable(R.drawable.button_unsubmit));
        }
    }

    private List<DialerModel> getDialarList() {
        List<DialerModel> dialerModelList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            DialerModel dialerModel = new DialerModel();
            if (i == 11) {
                dialerModel.setNumber("0");
            } else {
                dialerModel.setNumber("" + i);
            }

            dialerModelList.add(dialerModel);
        }

        return dialerModelList;
    }


    private void sendOtpApiReqPass() {
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
        String phonenumber = binding.numberEdit.getText().toString();
        SendOtpReqModel mreqmodel = new SendOtpReqModel();
        mreqmodel.setMobileNo(phonenumber);
        viewModel.checkInternet(mreqmodel, Status.SEND_OTP);
    }


    private void oldSendOtpApi() {
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
        String phonenumber = binding.numberEdit.getText().toString();
        SendOtpReqModel mreqmodel = new SendOtpReqModel();
        mreqmodel.setMobileNo(phonenumber);
        viewModel.checkInternet(mreqmodel, Status.SEND_OTP);
    }


    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    /*Loading code here*/
                    break;

                case SUCCESS:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    if (responseApi.apiTypeStatus == Status.SEND_OTP ) {
                        SendOtpResModel sendOtp = (SendOtpResModel) responseApi.data;
                        startActivityDashboardScreen(sendOtp.getOtp());

                    } else if (responseApi.apiTypeStatus == Status.CHECKVALIDUSER) {
                        AppLogger.e(TAG, "-- step 2");
                        checkUserResModel = (CheckUserResModel) responseApi.data;
                        handleResponseCode(checkUserResModel);
                    }
                    break;

                case NO_INTERNET:
                    // closeDialog();
                    //   showSnackbarError((String) responseApi.data);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    break;

                case AUTH_FAIL:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    break;
                case FAIL_400:
// When Authrization is fail
                    // closeDialog();
                    // showSnackbarError((String) responseApi.data);
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    break;

                default:
                    //closeDialog();
                    //showSnackbarError((String) responseApi.data);
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    break;
            }

        });
    }


    public void startActivityDashboardScreen(int otp) {
        funnelOtpAsk();
        Intent i = new Intent(this, OtpActivity.class);
        i.putExtra(getResources().getString(R.string.intent_phone_number), binding.numberEdit.getText().toString());
        i.putExtra(getResources().getString(R.string.userOtp), otp);
        startActivity(i);
    }

    private void funnelOtpAsk() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int userType = prefModel.getUserType();
        if (userType == AppConstant.UserType.TEACHER) {
            AnalyticsRegistry.INSTANCE.getModelInstance().trackLoginScreen(AppConstant.UserType.TEACHER);
        } else {
            AnalyticsRegistry.INSTANCE.getModelInstance().trackLoginScreen(AppConstant.UserType.STUDENT);
        }

    }


    private void callCheckUser() {
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
        String phonenumber = binding.numberEdit.getText().toString();
        CheckUserApiReqModel checkUserApiReqModel = new CheckUserApiReqModel();
        checkUserApiReqModel.setEmailId("");
        checkUserApiReqModel.setMobileNo("");
        checkUserApiReqModel.setUserName(phonenumber);

        viewModel.checkInternet(checkUserApiReqModel, Status.CHECKVALIDUSER);
    }

    private void handleResponseCode(CheckUserResModel checkUserResModel) {
        AppLogger.e(TAG, "--" + checkUserResModel.getCode());
        AppLogger.e(TAG, "--" + checkUserResModel.getStudentName());
      /*  PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setUserMobile(checkUserResModel.getUserMobile());
        prefModel.setStatusUserCode(checkUserResModel.getCode());
        prefModel.setStudentClasses(checkUserResModel.getClasses());
        prefModel.setStudentName(checkUserResModel.getStudentName());
        prefModel.setStudentClass(ConversionUtil.INSTANCE.convertStringToInteger(checkUserResModel.getGrade()));
        AuroAppPref.INSTANCE.setPref(prefModel);*/
        checkUserForOldUser();

        switch (checkUserResModel.getCode()) {
            case AppConstant.UsercheckApiCode.USER_NOT_FOUND:
            case AppConstant.UsercheckApiCode.SR_ID_NOT_FOUND:
                showSnackbarError("User Not Found");
                break;

            default:
                sendOtpApiReqPass();
                break;
        }
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }
    void checkUserForOldUser() {
        if (checkUserResModel != null && !checkUserResModel.getUserDetails().isEmpty() && checkUserResModel.getUserDetails().size() == 1) {
            UserDetailResModel resModel = checkUserResModel.getUserDetails().get(0);
            if (resModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_STUDENT)) {
                setDatainPref(resModel);
            }
        } else if (checkUserResModel != null && !checkUserResModel.getUserDetails().isEmpty() && checkUserResModel.getUserDetails().size() == 2) {
            for (UserDetailResModel resModel : checkUserResModel.getUserDetails()) {
                setDatainPref(resModel);
            }
        } else {
            for (UserDetailResModel resModel : checkUserResModel.getUserDetails()) {
                if (resModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_PARENT)) {
                    setDatainPref(resModel);
                }
            }

        }
    }

    private void setDatainPref(UserDetailResModel resModel) {
        if (resModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_STUDENT)) {
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setStudentData(resModel);
            //prefModel.setStudentClass(ConversionUtil.INSTANCE.convertStringToInteger(resModel.getGrade()));
            prefModel.setStudentClasses(checkUserResModel.getClasses());
            AuroAppPref.INSTANCE.setPref(prefModel);
        } else {
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setParentData(resModel);
            // prefModel.setStudentClass(ConversionUtil.INSTANCE.convertStringToInteger(resModel.getGrade()));
            prefModel.setStudentClasses(checkUserResModel.getClasses());
            AuroAppPref.INSTANCE.setPref(prefModel);
        }
    }
}
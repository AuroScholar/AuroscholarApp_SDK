package com.auro.application.home.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivitySetPinBinding;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.signupmodel.request.RegisterApiReqModel;
import com.auro.application.home.data.model.signupmodel.request.SetUsernamePinReqModel;
import com.auro.application.home.data.model.signupmodel.response.RegisterApiResModel;
import com.auro.application.home.data.model.signupmodel.response.SetUsernamePinResModel;
import com.auro.application.home.presentation.viewmodel.SetPinViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;

import javax.inject.Inject;
import javax.inject.Named;

public class SetPinActivity extends BaseActivity implements View.OnClickListener {

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
        ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SetPinViewModel.class);
        binding.setLifecycleOwner(this);
        if (getIntent() != null) {
            resModel = (UserDetailResModel) getIntent().getParcelableExtra(AppConstant.USER_PROFILE_DATA_MODEL);
            isComingFrom = getIntent().getStringExtra(AppConstant.COMING_FROM);

        }
        if (isComingFrom.equalsIgnoreCase(AppConstant.ComingFromStatus.COMING_FROM_ADD_STUDENT)) {
            binding.titleFirst.setText(this.getString(R.string.hey_enter_set));
        } else {
            binding.titleFirst.setText(this.getString(R.string.add_student));
        }
        AppStringDynamic.setPinPagetrings(binding);

    }

    @Override
    protected void setListener() {
        binding.backButton.setOnClickListener(this);
        binding.btDoneNew.setOnClickListener(this);

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
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

            if (binding.etUsername.getText().toString().isEmpty() || binding.etUsername.getText().toString().equals("")) {
                Toast.makeText(this, details1.getEnter_user_name(), Toast.LENGTH_SHORT).show();
            }
            if (binding.etUsername.getText().toString().length() < 5) {
                Toast.makeText(this, "Enter minimum 5 characters", Toast.LENGTH_SHORT).show();
            } else if (binding.etUsername.getText().toString().startsWith(" ")) {
                Toast.makeText(this, "Can not enter space in username", Toast.LENGTH_SHORT).show();

            } else if (pin.isEmpty() || pin.equals("")) {
                Toast.makeText(this, details1.getEnter_the_pin(), Toast.LENGTH_SHORT).show();
            } else if (pin.length() < 4) {
                Toast.makeText(this, "Please enter 4 digits  pin", Toast.LENGTH_SHORT).show();
            } else if (confirmpin.isEmpty() || confirmpin.equals("")) {
                Toast.makeText(this, details1.getEnter_the_confirm_pin(), Toast.LENGTH_SHORT).show();

            } else if (confirmpin.length() < 4) {
                Toast.makeText(this, "Please enter 4 digits confirm pin", Toast.LENGTH_SHORT).show();

            } else if (pin == confirmpin || pin.equals(confirmpin)) {
                checkValidation();
            } else {
                Toast.makeText(this, details1.getPin_and_confirm_not_match(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void checkValidation() {
        AppLogger.e("onClick--", "step 2");
        String username = binding.etUsername.getText().toString();
        String pin = binding.pinView.getText().toString();
        String confirmPin = binding.confirmPin.getText().toString();
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        if (username.isEmpty()) {
            ViewUtil.showSnackBar(binding.getRoot(),details.getEnter_user_name() );
            return;
        } else if (pin.isEmpty() || pin.length() < 4) {
            ViewUtil.showSnackBar(binding.getRoot(), details.getEnter_the_pin());
            return;
        } else if (confirmPin.isEmpty() || confirmPin.length() < 4) {
            ViewUtil.showSnackBar(binding.getRoot(),details.getEnter_the_confirm_pin() );
            return;
        } else if (!pin.equalsIgnoreCase(confirmPin)) {
            ViewUtil.showSnackBar(binding.getRoot(),details.getPin_and_confirm_not_match());
            return;
        } else {
            binding.progressbar.pgbar.setVisibility(View.VISIBLE);
            if (isComingFrom.equalsIgnoreCase(AppConstant.ComingFromStatus.COMING_FROM_ADD_STUDENT_STEP_2)) {
                callCheckUser();
            } else {
                callSetPinApi();
            }
        }
    }

    void callSetPinApiFromAddStudent(String userId) {
        SetUsernamePinReqModel mreqmodel = new SetUsernamePinReqModel();
        mreqmodel.setUserId(userId);
        mreqmodel.setPin(binding.pinView.getText().toString());
        mreqmodel.setUserName(binding.etUsername.getText().toString());
        viewModel.checkInternet(mreqmodel, Status.SET_USERNAME_PIN_API);
    }

    void callSetPinApi() {
        SetUsernamePinReqModel mreqmodel = new SetUsernamePinReqModel();
        mreqmodel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        mreqmodel.setPin(binding.pinView.getText().toString());
        mreqmodel.setUserName(binding.etUsername.getText().toString());
        viewModel.checkInternet(mreqmodel, Status.SET_USERNAME_PIN_API);
    }


    private void callRegisterApi(String userMobile) {
        RegisterApiReqModel reqmodel = new RegisterApiReqModel();
        reqmodel.setMobileNumber(userMobile);
        reqmodel.setUserType("" + AuroAppPref.INSTANCE.getModelInstance().getUserType());
        viewModel.checkInternet(reqmodel, Status.REGISTER_API);
    }


    private void observeServiceResponse() {
        AppLogger.e("observeServiceResponse","step 1");
        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    if (responseApi.apiTypeStatus == Status.REGISTER_API) {
                        registerApiResModel = (RegisterApiResModel) responseApi.data;
                        if (!registerApiResModel.getError()) {
                            callSetPinApiFromAddStudent((registerApiResModel.getUserId()));
                        } else {
                            ViewUtil.showSnackBar(binding.getRoot(), registerApiResModel.getMessage());
                        }
                    } else if (responseApi.apiTypeStatus == Status.CHECKVALIDUSER) {

                        AppLogger.e("observeServiceResponse","step 2");
                        checkUserResModel = (CheckUserResModel) responseApi.data;
                        if (checkUserResModel.getError()) {
                            ViewUtil.showSnackBar(binding.getRoot(), resModel.getMessage());
                        } else {
                            AppLogger.e("observeServiceResponse","step 3");
                            checkUserForOldUser();
                        }


                    } else {
                        SetUsernamePinResModel resModel = (SetUsernamePinResModel) responseApi.data;
                        if (resModel.getError()) {
                            ViewUtil.showSnackBar(binding.getRoot(), resModel.getMessage());
                            callCheckUser();
                        } else {
                            if (isComingFrom.equalsIgnoreCase(AppConstant.ComingFromStatus.COMING_FROM_ADD_STUDENT_STEP_2)) {
                                openLoginActivity();
                            } else if (isComingFrom.equalsIgnoreCase(AppConstant.ComingFromStatus.COMING_FROM_ADD_STUDENT_STEP_1)) {
                                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                UserDetailResModel userDetailResModel = prefModel.getStudentData();
                                userDetailResModel.setUserName(binding.etUsername.getText().toString());
                                userDetailResModel.setSetPin(true);
                                userDetailResModel.setUsername(true);
                                prefModel.setStudentData(userDetailResModel);
                                AuroAppPref.INSTANCE.setPref(prefModel);

                                finish();
                            } else {
                                checkWhichScreenOpen();
                            }
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

    private void openLoginActivity() {
//        AuroAppPref.INSTANCE.clearPref();
//        Intent newIntent = new Intent(this, SplashScreenAnimationActivity.class);
//        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(newIntent);
        finish();
    }

    void checkUserForOldUser() {
        AppLogger.e("observeServiceResponse","step 3.1");
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
        AppLogger.e("observeServiceResponse","step 4");
        AppLogger.e("observeServiceResponse","step 5-"+checkDataForApiCallRegister());

        if (checkDataForApiCallRegister()) {
            AppLogger.e("observeServiceResponse","step 6"+resModel.getUserMobile());

            callRegisterApi(resModel.getUserMobile());
        } else {
            AppLogger.e("observeServiceResponse","step 7"+userDetailForSecondStudent.getUserId());
            callSetPinApiFromAddStudent(userDetailForSecondStudent.getUserId());
        }

    }

    private void setDatainPref(UserDetailResModel resModel) {
        if (resModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_STUDENT)) {
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setStudentData(resModel);
            prefModel.setCheckUserResModel(checkUserResModel);
            prefModel.setStudentClasses(checkUserResModel.getClasses());
            AuroAppPref.INSTANCE.setPref(prefModel);
        } else {
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setParentData(resModel);
            prefModel.setCheckUserResModel(checkUserResModel);
            prefModel.setStudentClasses(checkUserResModel.getClasses());
            AuroAppPref.INSTANCE.setPref(prefModel);
        }

    }

    void proceedToSetPinApi() {
        UserDetailResModel model = new UserDetailResModel();
        for (UserDetailResModel resModel : checkUserResModel.getUserDetails()) {
            if (resModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_STUDENT) && resModel.getUserName().equalsIgnoreCase(binding.etUsername.getText().toString())) {
                model = resModel;
                break;
            }
        }

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
        UserDetailResModel resModel2 = AuroAppPref.INSTANCE.getModelInstance().getParentData();
        if (resModel2.getIsMaster().equals("1")){
                                    Intent i = new Intent(this, ParentProfileActivity.class);
                        i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
                        startActivity(i);
        }
        else{
            if (resModel != null && resModel.getGrade().equalsIgnoreCase("0")) {
                openChooseGradeActivity();
            } else {
                startDashboardActivity();
            }
        }

    }


    private void callCheckUser() {

        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
        CheckUserApiReqModel checkUserApiReqModel = new CheckUserApiReqModel();
        checkUserApiReqModel.setMobileNo("");
        checkUserApiReqModel.setEmailId("");

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (!TextUtil.isEmpty(prefModel.getSrId())) {
            checkUserApiReqModel.setUserName(prefModel.getSrId());
        } else {

//            checkUserApiReqModel.setUserName(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserMobile());
            checkUserApiReqModel.setUserName(AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel().getUserDetails().get(1).getUserMobile());

        }
        checkUserApiReqModel.setUserType("" + prefModel.getUserType());
        AppLogger.e("callCheckUser","");
        viewModel.checkInternet(checkUserApiReqModel, Status.CHECKVALIDUSER);
    }

    private boolean checkDataForApiCallRegister() {
        for (UserDetailResModel model : checkUserResModel.getUserDetails()) {
            if (model.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_STUDENT)) {
                if (!model.getSetPin()) {
                    userDetailForSecondStudent = model;
                    return false;
                }
            }
        }
        //if username is empty then dont need to call register api
        return true;
    }
}
package com.auro.application.home.presentation.view.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.SdkCallBack;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.network.URLConstant;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.databinding.ActivityOtpBinding;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.OtpOverCallReqModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.VerifyOtpReqModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.OtpOverCallResModel;
import com.auro.application.home.data.model.response.SendOtpResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.response.VerifyOtpResModel;
import com.auro.application.home.data.model.signupmodel.DialerModel;
import com.auro.application.home.data.model.signupmodel.request.RegisterApiReqModel;
import com.auro.application.home.data.model.signupmodel.response.RegisterApiResModel;
import com.auro.application.home.presentation.view.adapter.CustomDialerAdapter;
import com.auro.application.home.presentation.viewmodel.OtpScreenViewModel;
import com.auro.application.teacher.data.model.response.MyClassRoomResModel;
import com.auro.application.teacher.presentation.view.activity.TeacherProfileActivity;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.firebaseAnalytics.AnalyticsRegistry;
import com.auro.application.util.otp_verification.SMSReceiver;
import com.auro.application.util.strings.AppStringDynamic;
import com.auro.application.util.timer.Hourglass;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import in.aabhasjindal.otptextview.OTPListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OtpActivity extends BaseActivity implements View.OnClickListener, CommonCallBackListner, OTPListener, SMSReceiver.OTPReceiveListener {


    private static final String TAG = OtpActivity.class.getSimpleName();
    @Inject
    @Named("OtpActivity")
    ViewModelFactory viewModelFactory;
    ActivityOtpBinding binding;
    OtpScreenViewModel viewModel;
    String otptext;
    Hourglass timer;
    StringBuilder stringBuilder = new StringBuilder();
    String phoneNumber = "";
    private SMSReceiver smsReceiver;
    VerifyOtpResModel verifyOtp;
    String isComingFrom = "";
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
        //((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        DaggerWrapper.getComponent(this).doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(OtpScreenViewModel.class);
        binding.setLifecycleOwner(this);
        setCustomDialerAdapter();
        setListener();
        initRecordingTimer();
        buttonSelect(false);
        getIntentFromPreviousScreen();
        ViewUtil.customTextView(binding.termsCondition, this);
        AppUtil.loadAppLogo(binding.auroScholarLogo, this);
        AppStringDynamic.setOtpPagetrings(binding);
        binding.optOverCallTxt.setOnClickListener(this);
        binding.optOverCallTxt.setPaintFlags( binding.optOverCallTxt.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        // forTestingUser();
    }

    void forTestingUser() {
        if (phoneNumber.equalsIgnoreCase("7503600686")) {
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            int userType = prefModel.getUserType();
            if (userType == AppConstant.UserType.TEACHER) {
                openTeacherActivity();
            } else {
                callCheckUser();
            }
        }
    }

    @Override
    protected void setListener() {
        binding.resendBtn.setOnClickListener(this);
        binding.backButton.setOnClickListener(this);

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
        startSMSListener();

    }


    private void callOverOTPApi() {
        OtpOverCallReqModel reqModel=new OtpOverCallReqModel();
        reqModel.setIsType(AuroAppPref.INSTANCE.getModelInstance().getUserType());
        reqModel.setMobileNo(phoneNumber);
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
        viewModel.checkInternet(reqModel, Status.OTP_OVER_CALL);
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
            checkUserApiReqModel.setUserName(phoneNumber);
        }
        checkUserApiReqModel.setUserType("" + prefModel.getUserType());
        viewModel.checkInternet(checkUserApiReqModel, Status.CHECKVALIDUSER);
    }


    private void verifyOtpRxApi() {

        AppLogger.e(TAG, "verifyOtpRxApi step 1");
        VerifyOtpReqModel mverifyOtpRequestModel = new VerifyOtpReqModel();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int type = prefModel.getUserType();
        if (type == AppConstant.UserType.TEACHER) {
            mverifyOtpRequestModel.setUserType(type);
        } else {
            mverifyOtpRequestModel.setUserType(type);
            mverifyOtpRequestModel.setResgistrationSource("AuroScholr");
        }
        mverifyOtpRequestModel.setDeviceToken(prefModel.getDeviceToken());
        mverifyOtpRequestModel.setMobileNumber(phoneNumber);
        mverifyOtpRequestModel.setOtpVerify(otptext);
        mverifyOtpRequestModel.setUserType(type);
        AppLogger.e(TAG, "verifyOtpRxApi step 2");
        if (!TextUtil.isEmpty(prefModel.getSrId())) {
            AppLogger.e(TAG, "verifyOtpRxApi step 3");
            mverifyOtpRequestModel.setSrId(prefModel.getSrId());
        } else {
            AppLogger.e(TAG, "verifyOtpRxApi step 4");
            mverifyOtpRequestModel.setSrId("");
        }
        AppLogger.e(TAG, "verifyOtpRxApi step 5");
        viewModel.checkInternet(mverifyOtpRequestModel, Status.VERIFY_OTP);
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
    }

    private void observeServiceResponse() {
        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    if (responseApi.apiTypeStatus == Status.VERIFY_OTP) {
                        verifyOtp = (VerifyOtpResModel) responseApi.data;
                        handleVerifyResponse();
                    }
                    if (responseApi.apiTypeStatus == Status.CHECKVALIDUSER) {
                        checkUserResModel = (CheckUserResModel) responseApi.data;
                        checkUserType();
                        AppLogger.e(TAG, "Step 4");
                    }
                    if (responseApi.apiTypeStatus == Status.GET_TEACHER_DASHBOARD_API) {
                        AppUtil.myClassRoomResModel = (MyClassRoomResModel) responseApi.data;
                        handleTeacherDashboardApiResponse();
                    }
                    if (responseApi.apiTypeStatus == Status.SEND_OTP) {
                        AppLogger.e(TAG, "Step 5");
                        binding.progressbar.pgbar.setVisibility(View.GONE);
                        SendOtpResModel sendOtp = (SendOtpResModel) responseApi.data;
                        if (!sendOtp.getError()) {
                            initRecordingTimer();
                            buttonSelect(false);
                            AppLogger.e(TAG, "Step 6");
                            // otptext = String.valueOf(sendOtp.getOtp());
                        } else {
                            ViewUtil.showSnackBar(binding.getRoot(), sendOtp.getMessage());
                            AppLogger.e(TAG, "Step 7");
                        }
                    }

                    if (responseApi.apiTypeStatus == Status.REGISTER_API) {
                        RegisterApiResModel registerApiResModel = (RegisterApiResModel) responseApi.data;
                        if (!registerApiResModel.getError()) {
                            callCheckUser();
                        } else {
                            ViewUtil.showSnackBar(binding.getRoot(), registerApiResModel.getMessage());
                        }
                    }

                    if (responseApi.apiTypeStatus == Status.OTP_OVER_CALL) {
                        binding.progressbar.pgbar.setVisibility(View.GONE);
                        binding.optOverCallTxt.setVisibility(View.GONE);
                        OtpOverCallResModel otpOverCallResModel = (OtpOverCallResModel) responseApi.data;
                        if (!otpOverCallResModel.getError()) {
                            initRecordingTimer();
                            buttonSelect(false);
                            AppLogger.e(TAG, "Step 6");
                            // otptext = String.valueOf(sendOtp.getOtp());
                        } else {
                            ViewUtil.showSnackBar(binding.getRoot(), otpOverCallResModel.getMessage());
                            AppLogger.e(TAG, "Step 7");
                        }
                    }
                    break;

                case NO_INTERNET:
                    AppLogger.e(TAG, "Step 8");
                    // closeDialog();
                    //   showSnackbarError((String) responseApi.data);
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    break;

                case AUTH_FAIL:
                    setOTPEmpty();
                    AppLogger.e(TAG, "Step 9");
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    break;
                case FAIL_400:
                    setOTPEmpty();
                    AppLogger.e(TAG, "Step 10");
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    break;

                default:
                    setOTPEmpty();
                    AppLogger.e(TAG, "Step 11");
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    break;
            }
        });
    }

    private void sendOtpApiReqPass() {
        SendOtpReqModel mreqmodel = new SendOtpReqModel();
        mreqmodel.setMobileNo(phoneNumber);
        viewModel.checkInternet(mreqmodel, Status.SEND_OTP);
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
    }


    private void callRegisterApi() {
        RegisterApiReqModel reqmodel = new RegisterApiReqModel();
        reqmodel.setMobileNumber(phoneNumber);
        reqmodel.setUserType("" + AuroAppPref.INSTANCE.getModelInstance().getUserType());
        viewModel.checkInternet(reqmodel, Status.REGISTER_API);
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
    }

    private void callSrIdRegisterApi() {
        RegisterApiReqModel reqmodel = new RegisterApiReqModel();
        reqmodel.setMobileNumber(phoneNumber);
        reqmodel.setUserType("" + AuroAppPref.INSTANCE.getModelInstance().getUserType());
        reqmodel.setSrId(AuroAppPref.INSTANCE.getModelInstance().getSrId());
        viewModel.checkInternet(reqmodel, Status.REGISTER_API);
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
    }

    private void setOTPEmpty() {
        otptext = "";
        binding.otpView.setText("");
    }

    private void checkUserResponse(CheckUserResModel userValidResModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setUserMobile(userValidResModel.getUserMobile());
        prefModel.setLogin(true);
        prefModel.setStudentClasses(userValidResModel.getClasses());
        prefModel.setStatusUserCode(userValidResModel.getCode());
        prefModel.setUserId(userValidResModel.getUserId());
        if (!TextUtil.isEmpty(userValidResModel.getEmailId())) {
            prefModel.setEmailId(userValidResModel.getEmailId());
        }
        prefModel.setLogin(true);
        AuroAppPref.INSTANCE.setPref(prefModel);


    }

    private void checkForGradeScreen() {
       /* PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel.getUserType() == AppConstant.userTypeLogin.STUDENT && prefModel.getStudentClass() > 0) {
            startDashboardActivity();
        } else if (prefModel.getUserType() == AppConstant.userTypeLogin.STUDENT && prefModel.getStudentClass() == 0) {
            openChooseGradeActivity();
        } else {
            startDashboardActivity();
        }*/
    }

    private void openChooseGradeActivity() {

        Intent tescherIntent = new Intent(this, ChooseGradeActivity.class);
        tescherIntent.putExtra(AppConstant.COMING_FROM, AppConstant.ComingFromStatus.COMING_FROM_LOGIN_WITH_OTP);
        startActivity(tescherIntent);
        finish();
    }

    private void openUserProfileActivity() {
//        Intent newIntent = new Intent(this, ParentProfileActivity.class);
//        startActivity(newIntent);
//        finish();
        Intent newIntent = new Intent(this, DashBoardMainActivity.class);
        startActivity(newIntent);
        finish();
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_otp;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.resend_btn) {
            startSMSListener();
            sendOtpApiReqPass();
        } else if (id == R.id.back_button) {
            finish();
        } else if (id == R.id.RPAccept) {
        } else if (id == R.id.opt_over_call_txt) {
            callOverOTPApi();
        }

    }

    void openSetPasswordActivity(String status) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor.putString("session_userid", AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel().getUserDetails().get(0).getUserId());
        editor.putString("session_username", AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel().getUserDetails().get(0).getUserName());
        editor.apply();

        Intent i = new Intent(this, ResetPasswordActivity.class);
        i.putExtra(AppConstant.COMING_FROM, status);
        startActivity(i);
        finish();
    }

    private void getIntentFromPreviousScreen() {
        AppLogger.e("getIntentFromPreviousScreen", "step 1");
        if (getIntent().hasExtra(getResources().getString(R.string.intent_phone_number))) {
            phoneNumber = getIntent().getStringExtra(getResources().getString(R.string.intent_phone_number));
            AppLogger.e("getIntentFromPreviousScreen", "step 2-" + phoneNumber);
            if (getIntent() != null) {
                isComingFrom = getIntent().getStringExtra(AppConstant.COMING_FROM);
            }
            if (!TextUtil.isEmpty(phoneNumber)) {
                binding.mobileNumberText.setText(phoneNumber);
            }
        }
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case DIALER_CALL_BACK_LISTNER:
                DialerModel dialerModel = (DialerModel) commonDataModel.getObject();
                otptext = binding.otpView.getText() + dialerModel.getNumber();
                binding.otpView.setText(otptext);
                AppLogger.e(TAG, otptext);
                if (!TextUtil.isEmpty(otptext) && otptext.length() == 6) {
                    AppLogger.e(TAG, otptext + "--if condition");
                    verifyOtpRxApi();
                }
                break;

            case CLEAR_EDIT_TEXT:
                String text = binding.otpView.getText().toString();
                if (text.length() > 0) {
                    binding.otpView.setText(text.substring(0, text.length() - 1));
                }

                break;

            case PRIVACY_POLICY_TEXT:
                openWebActivty(URLConstant.PRIVACY_POLICY);

                break;

            case TERMS_CONDITION_TEXT:
                openWebActivty(URLConstant.TERM_CONDITION);
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

    @Override
    public void onInteractionListener() {

    }

    @Override
    public void onOTPComplete(String otp) {
        otptext = otp;
    }

    public void initRecordingTimer() {
        binding.resendTimerTxt.setVisibility(View.VISIBLE);
        binding.optOverCallTxt.setVisibility(View.GONE);

        timer = new Hourglass() {
            @Override
            public void onTimerTick(final long timeRemaining) {
                int seconds = (int) (timeRemaining / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                try {
                    Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
                    binding.resendTimerTxt.setText(details.getYouCanResendIn() + " " + String.format("%02d", minutes)
                            + ":" + String.format("%02d", seconds));
                } catch (Exception e) {
                    AppLogger.e("initRecordingTimer", e.getMessage());
                }
            }

            @Override
            public void onTimerFinish() {
                binding.resendTimerTxt.setVisibility(View.GONE);
                binding.optOverCallTxt.setVisibility(View.VISIBLE);
                buttonSelect(true);
            }
        };
        timer.setTime(120 * 1000 + 1000);
        timer.startTimer();
    }

    public void buttonSelect(boolean status) {
        if (status) {
            binding.resendBtn.setEnabled(true);
            binding.resendBtn.setTextColor(AuroApp.getAppContext().getResources().getColor(R.color.white));
            binding.resendBtn.setBackground(AuroApp.getAppContext().getResources().getDrawable(R.drawable.button_submit));
        } else {
            binding.resendBtn.setEnabled(false);
            binding.resendBtn.setTextColor(AuroApp.getAppContext().getResources().getColor(R.color.auro_buton_text_blue));
            binding.resendBtn.setBackground(AuroApp.getAppContext().getResources().getDrawable(R.drawable.button_unsubmit));
        }
    }

    private void checkUserType() {
        checkUserForOldUser();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setCheckUserResModel(checkUserResModel);
        int userType = prefModel.getUserType();
        if (userType == AppConstant.UserType.TEACHER) {
            if (checkUserResModel != null && checkUserResModel.getUserDetails() != null && checkUserResModel.getUserDetails().size() > 0) {
                if (prefModel.getStudentData() != null && !prefModel.getStudentData().getSetUserPass()) {
                    openSetPasswordActivity(AppConstant.FROM_SET_PASSWORD);
                } else {
                    setLogin();
                    openTeacherActivity();
                }
            } else {
                callRegisterApi();
            }
        } else {
            funnelOtpVerify(AppConstant.UserType.STUDENT);
            checkUserCode();
        }


    }


    private void handleTeacherDashboardApiResponse() {
        if (AppUtil.myClassRoomResModel != null && !TextUtil.isEmpty(AppUtil.myClassRoomResModel.getTeacherResModel().getStateId()) && !TextUtil.isEmpty(AppUtil.myClassRoomResModel.getTeacherResModel().getDistrictId())) {

            //setLogin();
            openTeacherActivity();
        } else {
            setLogin();
            teacherProfileActivity();
        }
    }

    private void callTeacherProfileActivity() {
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
        AuroScholarDataModel auroScholarDataModel = new AuroScholarDataModel();
        auroScholarDataModel.setMobileNumber(phoneNumber);
        viewModel.checkInternet(auroScholarDataModel, Status.GET_TEACHER_DASHBOARD_API);

    }

    private void checkUserCode() {
        switch (checkUserResModel.getCode()) {
            case AppConstant.UsercheckApiCode.USER_CHECK_CODE:
                callSrIdRegisterApi();
                break;
            case AppConstant.UsercheckApiCode.USER_NOT_FOUND:
                callRegisterApi();
                break;
            case AppConstant.UsercheckApiCode.PASS_NULL:

                break;
            case AppConstant.UsercheckApiCode.PASS_NOT_NULL:

                break;
            default:
                if (checkUserResModel != null && checkUserResModel.getUserDetails().size() > 0 && checkUserResModel.getUserDetails().size() < 3) {
                    UserDetailResModel userDetailResModel = AuroAppPref.INSTANCE.getModelInstance().getStudentData();
                    UserDetailResModel parentData = AuroAppPref.INSTANCE.getModelInstance().getParentData();
                    if (parentData != null && !parentData.getSetUserPass() && parentData.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_PARENT)) {
                        openSetPasswordActivity(AppConstant.FROM_SET_PASSWORD);
                    } else if (!userDetailResModel.getSetPin() && userDetailResModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_STUDENT)) {
                        openSetPinActivity(AuroAppPref.INSTANCE.getModelInstance().getStudentData());
                    }

                } else {
                    startDashboardActivity();
                }
                break;
        }

    }


    private void openSetPinActivity(UserDetailResModel resModel) {
        Intent intent = new Intent(this, ForgotPinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, resModel);
        startActivity(intent);
    }

    private void openTeacherActivity() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setUserMobile(phoneNumber);
        AuroAppPref.INSTANCE.setPref(prefModel);
        AuroScholarDataModel auroScholarDataModel = new AuroScholarDataModel();
        auroScholarDataModel.setMobileNumber(phoneNumber);
        if (prefModel.getDynamiclinkResModel() != null && !TextUtil.isEmpty(prefModel.getDynamiclinkResModel().getSource())) {
            auroScholarDataModel.setRegitrationSource(prefModel.getDynamiclinkResModel().getSource());
        } else {
            auroScholarDataModel.setRegitrationSource("AuroScholr");
        }
        auroScholarDataModel.setShareType("teacher");
        auroScholarDataModel.setShareIdentity("AuroScholr");
        auroScholarDataModel.setActivity(this);
        auroScholarDataModel.setReferralLink("");
        auroScholarDataModel.setEmailVerified(true);
        auroScholarDataModel.setFragmentContainerUiId(R.id.home_container);
        auroScholarDataModel.setSdkcallback(new SdkCallBack() {
            @Override
            public void callBack(String message) {
                /*Api response here*/

            }

            @Override
            public void logOut() {
                AppLogger.e("Chhonker", "Logout");
                AuroAppPref.INSTANCE.clearPref();
                Intent intent = new Intent(OtpActivity.this, SplashScreenAnimationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();
            }

            @Override
            public void commonCallback(Status status, Object o) {
                switch (status) {
                    case BOOK_TUTOR_SESSION_CLICK:
                        AppLogger.e("Chhonker", "commonCallback");
                        break;
                }
            }
        });
        AuroScholar.startTeacherSDK(auroScholarDataModel);
        finish();
    }

    private void funnelOtpVerify(int chooseTypeOtpVerify) {

        if (chooseTypeOtpVerify == AppConstant.UserType.TEACHER) {
            AnalyticsRegistry.INSTANCE.getModelInstance().trackOtpScreen(AppConstant.UserType.TEACHER);
        } else {
            AnalyticsRegistry.INSTANCE.getModelInstance().trackOtpScreen(AppConstant.UserType.STUDENT);
        }
    }

    private void startSMSListener() {
        try {
            smsReceiver = new SMSReceiver();
            smsReceiver.setOTPListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(this);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AppLogger.v("autoPhone", "Successfully started retriever");
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    AppLogger.v("autoPhone", "Failed to start retriever");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onOTPReceived(String otp) {
        // showToast("OTP Received: " + otp);
        String oneTimeCode = parseOneTimeCode(otp); // define this function
        otptext = oneTimeCode;

        AppLogger.v("autoPhone", "onOTPReceived" + otptext);
        verifyOtpRxApi();
        // showToast(oneTimeCode);
        binding.otpView.setText(oneTimeCode);
        AppLogger.v("autoPhone", oneTimeCode);
        // Toast.makeText(activity, "AutoPhone"+oneTimeCode, Toast.LENGTH_SHORT).show();

        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
            smsReceiver = null;
        }
    }

    @Override
    public void onOTPTimeOut() {
        // showToast("OTP Time out");
        AppLogger.v("autoPhone", "onOTPTimeOut" + otptext);
    }

    @Override
    public void onOTPReceivedError(String error) {
        showToast(error);
        AppLogger.v("autoPhone", "onOTPReceivedError" + error);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
            AppLogger.v("autoPhone", "onDestroy");
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private String parseOneTimeCode(String message) {
        //simple number extractor
        String otpmessage = message.replaceAll("[^0-9]", "");
        return otpmessage.substring(0, 6);
    }

    private void startDashboardActivity() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String loginwithotpstatus = prefs.getString("loginwithotpstatus", "");

        if (AuroAppPref.INSTANCE.getModelInstance().getUserType() == AppConstant.UserType.TEACHER) {
            Intent i = new Intent(this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            PrefModel prefModel2 = AuroAppPref.INSTANCE.getModelInstance();
            String childid =  AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();


            getProfile(childid);


        }

    }


    private void getProfile(String userid)
    {

        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);
        //0000898904,123456

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
                                Intent i = new Intent(OtpActivity.this, CompleteStudentProfileWithoutPin.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);

                            }
                            else{
                                SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
                                String loginwithotpstatus = prefs.getString("loginwithotpstatus", "");
                                if (loginwithotpstatus.equals("true")){
                                    Intent i = new Intent(OtpActivity.this, DashBoardMainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                    editor.putString("loginwithotpstatus","false");
                                    editor.apply();
                                }
                                else{
                                    Intent i = new Intent(OtpActivity.this, DashBoardMainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                    editor.putString("loginwithotpstatus","false");
                                    editor.apply();

                                }
                            }


                        }
                        else
                        {
                            Toast.makeText(OtpActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<GetStudentUpdateProfile> call, Throwable t)
                    {
                        Toast.makeText(OtpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void teacherProfileActivity() {
        Intent newIntent = new Intent(this, TeacherProfileActivity.class);
        startActivity(newIntent);
        finish();
    }

    private void setLogin() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setUserMobile(phoneNumber);
        prefModel.setLogin(true);
        prefModel.setTeacherProfileScreen(true);
        AuroAppPref.INSTANCE.setPref(prefModel);


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
            prefModel.setCheckUserResModel(checkUserResModel);
            prefModel.setStudentData(resModel);
            //prefModel.setStudentClass(ConversionUtil.INSTANCE.convertStringToInteger(resModel.getGrade()));
            prefModel.setStudentClasses(checkUserResModel.getClasses());
            AuroAppPref.INSTANCE.setPref(prefModel);
        } else {
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setParentData(resModel);
            prefModel.setCheckUserResModel(checkUserResModel);
            // prefModel.setStudentClass(ConversionUtil.INSTANCE.convertStringToInteger(resModel.getGrade()));
            prefModel.setStudentClasses(checkUserResModel.getClasses());
            AuroAppPref.INSTANCE.setPref(prefModel);
        }
    }


    void handleVerifyResponse() {
        AppLogger.e(TAG, "Step 1");
        if (!verifyOtp.getError()) {
            AppLogger.e(TAG, "Step 1.1");
            if (isComingFrom != null && isComingFrom.equalsIgnoreCase(AppConstant.ComingFromStatus.COMING_FROM_PASSWORD_NOT_SET)) {
                openSetPasswordActivity(AppConstant.FROM_FORGOT_PASSWORD);
              //  Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            }
            else if (isComingFrom != null && isComingFrom.equalsIgnoreCase(AppConstant.ComingFromStatus.COMING_FORM_PIN_FORGOT_PIN)) {
                AppLogger.e(TAG, "Step 1.2");
               // Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                openForgotPinActivity();
            }
            else if (AuroAppPref.INSTANCE.getModelInstance().isForgotPassword()) {
             // Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                AppLogger.e(TAG, "Step 1.3");
                openSetPasswordActivity(AppConstant.FROM_FORGOT_PASSWORD);
            }
            else if (isComingFrom != null && isComingFrom.equalsIgnoreCase(AppConstant.ComingFromStatus.COMING_FROM_LOGIN_WITH_OTP)) {
              // Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                AppLogger.e(TAG, "Step 1.4");
                if (AuroAppPref.INSTANCE.getModelInstance().getUserType() == AppConstant.UserType.TEACHER) {
                  //  Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
                    AppLogger.e(TAG, "Step 1.5");
                    openTeacherActivity();
                }
                else if (AuroAppPref.INSTANCE.getModelInstance().getUserType() == AppConstant.UserType.STUDENT) {
                  // Toast.makeText(this, "6", Toast.LENGTH_SHORT).show();
                        AppLogger.e(TAG, "Step 1.6");

                     //  String studentClass = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getGrade();
                    String studentClass = AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel().getUserDetails().get(1).getGrade();
                        if (studentClass == "0") {
                          //  Toast.makeText(this, "7", Toast.LENGTH_SHORT).show();
                            AppLogger.e(TAG, "Step 1.7");
                            openChooseGradeActivity();
                        }
                        else {
                        //    Toast.makeText(this, "8", Toast.LENGTH_SHORT).show();
                            AppLogger.e(TAG, "Step 1.8");
                            startDashboardActivity();
                        }

                    }
                 else {
                    if (AuroAppPref.INSTANCE.getModelInstance().getParentData().getIsMaster() == AppConstant.UserType.USER_TYPE_PARENT) {
                    //    Toast.makeText(this, "9", Toast.LENGTH_SHORT).show();
                        openUserProfileActivity();
                    }
                }
            }
            else {
               // Toast.makeText(this, "10", Toast.LENGTH_SHORT).show();
                AppLogger.e(TAG, "Step 1.9");
                callCheckUser();
            }
            AppLogger.e(TAG, "Step 2");
        }
        else {
          //  Toast.makeText(this, "12", Toast.LENGTH_SHORT).show();
            AppLogger.e(TAG, "Step 3");
            setOTPEmpty();
            ViewUtil.showSnackBar(binding.getRoot(), verifyOtp.getMessage());
        }
    }

    private void openForgotPinActivity() {
        UserDetailResModel userDetailResModel = AuroAppPref.INSTANCE.getModelInstance().getStudentData();
        Intent intent = new Intent(this, ForgotPinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, userDetailResModel);
        startActivity(intent);

    }
}
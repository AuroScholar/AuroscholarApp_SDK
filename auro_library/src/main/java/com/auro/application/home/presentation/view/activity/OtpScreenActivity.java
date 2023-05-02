package com.auro.application.home.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.auro.application.R;
import com.auro.application.home.data.base_component.BaseActivity;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityOtpScreenBinding;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.VerifyOtpReqModel;
import com.auro.application.home.data.model.response.CheckUserValidResModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.data.model.response.SendOtpResModel;
import com.auro.application.home.data.model.response.VerifyOtpResModel;
import com.auro.application.home.presentation.viewmodel.OtpScreenViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.ViewUtil;

import com.auro.application.util.otp_verification.MySMSBroadcastReceiver;
import com.auro.application.util.otp_verification.SMSReceiver;
import com.auro.application.util.timer.Hourglass;
import com.auro.application.core.network.URLConstant;
import com.auro.application.util.TextUtil;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import javax.inject.Inject;
import javax.inject.Named;

import in.aabhasjindal.otptextview.OTPListener;

public class OtpScreenActivity extends BaseActivity implements OTPListener, View.OnClickListener, CommonCallBackListner,  SMSReceiver.OTPReceiveListener {


    @Inject
    @Named("OtpScreenActivity")
    ViewModelFactory viewModelFactory;

    OtpScreenViewModel viewModel;
    Activity activity;
    private static final String TAG = OtpScreenActivity.class.getSimpleName();
    ActivityOtpScreenBinding binding;
    String otptext = "";
    String phoneNumber = "";
    Hourglass timer;

    MySMSBroadcastReceiver mySMSBroadcastReceiver;
    private SMSReceiver smsReceiver;

    Task<Void> task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerWrapper.getComponent(this).doInjection(this);
        ViewUtil.setLanguageonUi(this);
        init();
        setListener();
    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayout());
        //((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
      //  DaggerWrapper.getComponent(this).doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(OtpScreenViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        activity = OtpScreenActivity.this;


        if (getIntent().hasExtra(getResources().getString(R.string.intent_phone_number))) {
          /*  trueProfile = getIntent().getParcelableExtra("profile");
            userType = getIntent().getStringExtra("userType");*/
            //otptext = String.valueOf(getIntent().getIntExtra(getResources().getString(R.string.userOtp), 0));
            phoneNumber = getIntent().getStringExtra(getResources().getString(R.string.intent_phone_number));
            binding.textOtpShow.setText(this.getString(R.string.opt_sent_number_txt) + " " + phoneNumber);

        }


        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);

        } else {
            observeServiceResponse();
        }
        ViewUtil.customTextView(binding.RPTextView7, this);
        initRecordingTimer();
    }

    @Override
    protected void setListener() {
        binding.otpView.setOtpListener(this);
        binding.RPButtonConfirm.setOnClickListener(this);
        binding.resendText.setOnClickListener(this);
        binding.backButton.backButton.setOnClickListener(this);
        binding.codeEditMobileno.setOnClickListener(this);

        startSMSListener();


    }

    @Override
    protected int getLayout() {
        return R.layout.activity_otp_screen;
    }

    @Override
    public void onInteractionListener() {
    }

    @Override
    public void onOTPComplete(String otp) {
        otptext = otp;
        //Toast.makeText(this, "he OTP is" + otp, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.RPButtonConfirm) {
            if (!otptext.isEmpty() && otptext.length() == 6) {
                binding.otpView.showSuccess();
                ViewUtil.hideKeyboard(this);
                verifyOtpRxApi();
                // startActivityToOtpScreen();
            } else {
                otptext = "";
                binding.otpView.showError();
                showSnackbarError(this.getString(R.string.enter_otp_txt));

            }
        } else if (id == R.id.resendText) {
            startSMSListener();
            sendOtpApiReqPass();
        } else if (id == R.id.code_editMobileno) {
            onBackPressed();
        }

    }


    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void verifyOtpRxApi() {
        VerifyOtpReqModel mverifyOtpRequestModel = new VerifyOtpReqModel();
        mverifyOtpRequestModel.setMobileNumber(phoneNumber);
        mverifyOtpRequestModel.setOtpVerify(otptext);
        viewModel.checkInternet(mverifyOtpRequestModel,Status.VERIFY_OTP);
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
    }

    private void startDashboardActivity() {

        Intent i = new Intent(OtpScreenActivity.this, DashBoardMainActivity.class);
        startActivity(i);

    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    //For ProgressBar
                    //  openProgressDialog();
                    //Toast.makeText(activity, "Loading...", Toast.LENGTH_SHORT).show();


                    break;

                case SUCCESS:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    if (responseApi.apiTypeStatus == Status.VERIFY_OTP) {
                        VerifyOtpResModel verifyOtp = (VerifyOtpResModel) responseApi.data;
                        if (!verifyOtp.getError()) {
                            callCheckUser(verifyOtp);
                        } else {
                            ViewUtil.showSnackBar(binding.getRoot(), verifyOtp.getMessage());
                        }
                    }

                    if (responseApi.apiTypeStatus == Status.CHECKVALIDUSER) {
                        CheckUserValidResModel userValidResModel = (CheckUserValidResModel) responseApi.data;
                        checkUserResponse(userValidResModel);
                    }

                    if (responseApi.apiTypeStatus == Status.SEND_OTP) {
                        binding.progressbar.pgbar.setVisibility(View.GONE);
                        SendOtpResModel sendOtp = (SendOtpResModel) responseApi.data;
                        if (!sendOtp.getError()) {
                            initRecordingTimer();
                            otptext = String.valueOf(sendOtp.getOtp());
                        } else {
                            ViewUtil.showSnackBar(binding.getRoot(), sendOtp.getMessage());
                        }

                    }
                    break;

                case NO_INTERNET:
                    // closeDialog();
                    //   showSnackbarError((String) responseApi.data);
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
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

    private void checkUserResponse(CheckUserValidResModel userValidResModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setUserMobile(phoneNumber);
        prefModel.setLogin(true);
        if (!userValidResModel.getError()) {
          //  prefModel.setStudentClass(ConversionUtil.INSTANCE.convertStringToInteger(userValidResModel.getStudentClass()));
            if (!TextUtil.isEmpty(userValidResModel.getEmailId())) {
                prefModel.setEmailId(userValidResModel.getEmailId());
            }
            prefModel.setLogin(true);


        }else {
            prefModel.setStudentClasses(userValidResModel.getClasses());
            DynamiclinkResModel dynamiclinkResModel = prefModel.getDynamiclinkResModel();
            if (dynamiclinkResModel != null && !TextUtil.isEmpty(dynamiclinkResModel.getStudentClass())) {
                int studentClass = ConversionUtil.INSTANCE.convertStringToInteger(dynamiclinkResModel.getStudentClass());
               // prefModel.setStudentClass(studentClass);
                prefModel.setLogin(true);
            }
        }
        AppLogger.v("SAVE_PREF","set log true");
        AuroAppPref.INSTANCE.setPref(prefModel);
        startDashboardActivity();
    }

    private void sendOtpApiReqPass() {

        SendOtpReqModel mreqmodel = new SendOtpReqModel();
        mreqmodel.setMobileNo(phoneNumber);
        viewModel.checkInternet(mreqmodel,Status.SEND_OTP);
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
    }


    public void initRecordingTimer() {
        binding.codeValidText.setVisibility(View.VISIBLE);
        binding.timerLayout.setVisibility(View.VISIBLE);
        binding.resendText.setVisibility(View.GONE);
        timer = new Hourglass() {
            @Override
            public void onTimerTick(final long timeRemaining) {

                int seconds = (int) (timeRemaining / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                binding.timerText.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onTimerFinish() {
                binding.codeValidText.setVisibility(View.GONE);
                binding.timerLayout.setVisibility(View.GONE);
                binding.resendText.setVisibility(View.VISIBLE);
            }
        };
        timer.setTime(120 * 1000 + 1000);
        timer.startTimer();

    }

    private void callCheckUser(VerifyOtpResModel verifyOtp) {
        CheckUserApiReqModel checkUserApiReqModel = new CheckUserApiReqModel();
        checkUserApiReqModel.setMobileNo(phoneNumber);
        checkUserApiReqModel.setEmailId("");
        viewModel.checkInternet(checkUserApiReqModel,Status.CHECKVALIDUSER);
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


    private String parseOneTimeCode(String message) {
        //simple number extractor
        String otpmessage = message.replaceAll("[^0-9]", "");
        return otpmessage.substring(0, 6);
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
                    AppLogger.v("autoPhone","Successfully started retriever");
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
        otptext= oneTimeCode;
        verifyOtpRxApi();
       // showToast(oneTimeCode);
        binding.otpView.setOTP(oneTimeCode);
        AppLogger.v("autoPhone",oneTimeCode);
       // Toast.makeText(activity, "AutoPhone"+oneTimeCode, Toast.LENGTH_SHORT).show();

        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
            smsReceiver = null;
        }
    }

    @Override
    public void onOTPTimeOut() {
       // showToast("OTP Time out");
    }

    @Override
    public void onOTPReceivedError(String error) {
        showToast(error);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
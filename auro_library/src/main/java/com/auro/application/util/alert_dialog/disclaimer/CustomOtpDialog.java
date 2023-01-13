package com.auro.application.util.alert_dialog.disclaimer;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.PaymentOtpDialogBinding;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.util.AppLogger;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;
import com.auro.application.util.timer.Hourglass;

import in.aabhasjindal.otptextview.OTPListener;


public class CustomOtpDialog extends Dialog implements OTPListener, View.OnClickListener {

    public Activity activity;
    private PaymentOtpDialogBinding binding;
    PrefModel prefModel;
    String otptext = "";
    Hourglass timer;
    String TAG = "CustomOtpDialog";


    public CustomOtpDialog(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.payment_otp_dialog, null, false);
        setContentView(binding.getRoot());

        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppStringDynamic.setCustomOtpStrings(binding);
        binding.mobileNumberText.setText(prefModel.getStudentData().getUserMobile());
        binding.RPVerify.setOnClickListener(this);
        binding.optOverCallTxt.setOnClickListener(this);
        binding.closeButton.setOnClickListener(this);
        initRecordingTimer();
        binding.otpView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppLogger.d(TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
                otptext = s.toString();
                if (!TextUtil.isEmpty(otptext) && otptext.length() == 6) {
                    ((DashBoardMainActivity) activity).verifyOtpRxApi(otptext);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onInteractionListener() {

    }

    @Override
    public void onOTPComplete(String otp) {
        otptext = otp;
        AppLogger.d(TAG, "onOTPComplete--" + otp);


    }

    public void showProgress() {
        binding.RPVerify.setVisibility(View.GONE);
        binding.optOverCallTxt.setVisibility(View.GONE);
        binding.progressBar2.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        binding.RPVerify.setVisibility(View.GONE);
        binding.progressBar2.setVisibility(View.GONE);
        binding.optOverCallTxt.setVisibility(View.GONE);
    }

    public void showSnackBar(String msg) {
        ViewUtil.showSnackBar(binding.getRoot(), msg);
    }


    public void initRecordingTimer() {
        binding.resendTimerTxt.setVisibility(View.VISIBLE);
        timer = new Hourglass() {
            @Override
            public void onTimerTick(final long timeRemaining) {
                int seconds = (int) (timeRemaining / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                binding.resendTimerTxt.setText(prefModel.getLanguageMasterDynamic().getDetails().getYou_can_send_otp() + " " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onTimerFinish() {
                binding.RPVerify.setVisibility(View.VISIBLE);
                binding.optOverCallTxt.setVisibility(View.VISIBLE);
                binding.resendTimerTxt.setVisibility(View.GONE);
            }
        };
        timer.setTime(120 * 1000 + 1000);
        timer.startTimer();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.resend_timer_txt) {
            ((DashBoardMainActivity) activity).sendOtpApiReqPass();
            initRecordingTimer();
        } else if (id == R.id.RPVerify) {
            dismiss();
            ((DashBoardMainActivity) activity).sendOtpApiReqPass();
        } else if (id == R.id.opt_over_call_txt) {
            dismiss();
            ((DashBoardMainActivity) activity).callOverOTPApi();
        } else if (id == R.id.closeButton) {
            dismiss();
        }
    }
}
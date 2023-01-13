package com.auro.application.util.alert_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.AskChoiceNumberBinding;
import com.auro.application.databinding.CustomKycDialogBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.response.CheckUserValidResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.util.AppUtil;
import com.auro.application.util.TextUtil;


public class ValidatePhoneDialog extends Dialog implements View.OnClickListener {

    AskChoiceNumberBinding binding;
    CustomKycDialogBinding customKycDialogBinding;
    CommonCallBackListner commonCallBackListner;

    public ValidatePhoneDialog(Activity context,CommonCallBackListner listner) {
        super(context);
        commonCallBackListner = listner;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.ask_choice_number, null, false);
        setContentView(binding.getRoot());
        setListner();
        setDataOnUi();
    }


    private void setListner() {
        binding.icClose.setOnClickListener(this);
        binding.RPButtonSendOtp.setOnClickListener(this);
        binding.RPButtonChangeNumber.setOnClickListener(this);
    }


    private void setDataOnUi() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        UserDetailResModel resModel=prefModel.getCheckUserResModel().getUserDetails().get(0);
        if (prefModel != null ) {
          //  String mobile= resModel.getUserMobile();
            String mobile = resModel.getUserMobile().substring(0, 3) + "XXXX" + resModel.getUserMobile().substring(7, 10);
            if (!TextUtil.isEmpty(mobile)) {
                binding.phoneVerifyText.setText("For Verification We Will Send OTP On This " + mobile);
            } else {
                binding.phoneVerifyText.setText("");
            }
            if (!TextUtil.isEmpty(prefModel.getStudentName())) {
                binding.titleFirst.setText("Hi, " + resModel.getStudentName());
            }

        }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.icClose) {
            dismiss();
        } else if (id == R.id.RPButtonSendOtp) {
            if (commonCallBackListner != null) {
                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.SEND_OTP, ""));
            }
        } else if (id == R.id.RPButtonChangeNumber) {
            if (commonCallBackListner != null) {
                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.CHANGE_NUMBER, ""));
            }
        }
    }


}

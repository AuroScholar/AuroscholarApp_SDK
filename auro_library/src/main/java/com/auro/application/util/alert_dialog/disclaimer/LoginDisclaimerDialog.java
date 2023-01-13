package com.auro.application.util.alert_dialog.disclaimer;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.LoginDiscalimerDialogLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.response.InstructionsResModel;
import com.auro.application.util.AppUtil;
import com.fueled.snippety.core.Snippety;
import com.fueled.snippety.core.Truss;


public class LoginDisclaimerDialog extends Dialog {

    public Activity activity;
    private LoginDiscalimerDialogLayoutBinding binding;
    PrefModel prefModel;
    CommonCallBackListner callBackListner;

    public LoginDisclaimerDialog(Activity context) {
        super(context);
        this.activity = context;

    }

    public LoginDisclaimerDialog(Activity context, CommonCallBackListner callBackListner) {
        super(context);
        this.activity = context;
        this.callBackListner = callBackListner;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.login_discalimer_dialog_layout, null, false);
        setContentView(binding.getRoot());
        setText();
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        binding.closeBt.setVisibility(View.GONE);
        binding.closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefModel.isPreLoginDisclaimer()) {
                    dismiss();
                    callBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.LOGIN_DISCLAMER_DIALOG, ""));
                } else {
                    activity.finishAffinity();
                }
            }
        });

        binding.RPAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                callBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.LOGIN_DISCLAMER_DIALOG, ""));

                prefModel = AuroAppPref.INSTANCE.getModelInstance();
                prefModel.setPreLoginDisclaimer(true);
                AuroAppPref.INSTANCE.setPref(prefModel);
            }
        });
    }


    private void setText() {


        try {
            Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
            binding.tvTitle.setText(details.getDisclaimer());
            binding.RPAccept.setText(details.getAcceptAndUseTheApp());
            int leadWidth = activity.getResources().getDimensionPixelOffset(R.dimen.space_medium);
            int gapWidth = activity.getResources().getDimensionPixelOffset(R.dimen.space_xlarge);
            leadWidth = 0;
            binding.tvMessage.setText(new Truss()
                    .appendln(details.getCheckDiscalimerFirstText(), new Snippety().bullet(leadWidth, gapWidth))
                    .appendln()
                    .appendln(details.getCheckDiscalimerSecondText(), new Snippety().bullet(leadWidth, gapWidth))
                    .appendln()
                    .appendln(details.getCheckDiscalimerThirdText(), new Snippety().bullet(leadWidth, gapWidth))
                    .appendln()
                    .build());
        } catch (Exception e) {
            setData();
        }


    }

    void setData() {
        int leadWidth = activity.getResources().getDimensionPixelOffset(R.dimen.space_medium);
        int gapWidth = activity.getResources().getDimensionPixelOffset(R.dimen.space_xlarge);
        leadWidth = 0;
        binding.tvMessage.setText(new Truss()
                .appendln(activity.getResources().getString(R.string.check_discalimer_first_text), new Snippety().bullet(leadWidth, gapWidth))
                .appendln()
                .appendln(activity.getResources().getString(R.string.check_discalimer_second_text), new Snippety().bullet(leadWidth, gapWidth))
                .appendln()
                .appendln(activity.getResources().getString(R.string.check_discalimer_third_text), new Snippety().bullet(leadWidth, gapWidth))
                .appendln()
                .build());
    }

   public  void setAfterLoginInstruction(InstructionsResModel instructionsResModel) {
        if(isShowing()) {
            binding.tvMessage.setText(Html.fromHtml(instructionsResModel.getData()));
        }
    }


}
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
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.AmParentDialogBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.response.InstructionsResModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.util.ViewUtil;


public class DisclaimerMoneyTransferDialog extends Dialog {

    public Activity activity;
    private AmParentDialogBinding binding;
    PrefModel prefModel;
    Details details;

    public DisclaimerMoneyTransferDialog(Activity context) {
        super(context);
        this.activity = context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.am_parent_dialog, null, false);
        setContentView(binding.getRoot());
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        binding.tvParent.setVisibility(View.VISIBLE);
        binding.tvMessage.setText("I hereby submit voluntarily at my/our own discretion, the bank account details and the Aadhar card for the purpose of transferring micro-scholarships which will be received to my account. \n");
        binding.RlKycCheck.setVisibility(View.GONE);
        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        binding.tvParent.setText(details.getParentSection());
        binding.RPAccept.setText(details.getContinueExit());
        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prefModel.isPreMoneyTransferDisclaimer()) {
                    dismiss();
                    ((DashBoardMainActivity) activity).popBackStack();
                }
            }
        });

        binding.RPAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.checkIsParent.isChecked()) {
                    dismiss();
                    prefModel.setPreMoneyTransferDisclaimer(false);
                    AuroAppPref.INSTANCE.setPref(prefModel);
                } else {
                    ViewUtil.showSnackBar(binding.getRoot(),details.getPlease_select_checkbox());
                }
            }
        });
    }

    public void setMessage(InstructionsResModel instructionsResModel) {
        if (isShowing()) {
            binding.tvMessage.setText(Html.fromHtml(instructionsResModel.getData()));
        }
    }
}
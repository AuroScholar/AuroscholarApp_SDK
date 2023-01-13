package com.auro.application.util.alert_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.common.ValidationModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.AskDetailLayoutBinding;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.PartnersLoginReqModel;
import com.auro.application.home.data.model.StudentProfileModel;
import com.auro.application.home.data.model.partnersmodel.PartnerDataModel;
import com.auro.application.home.data.model.partnersmodel.PartnerRequiredParamModel;
import com.auro.application.home.presentation.viewmodel.StudentProfileViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;


public class AskDetailCustomDialog extends Dialog implements View.OnClickListener {

    public Context context;
    private final String msg;
    private final String tittle;
    private AskDetailLayoutBinding binding;
    PartnerDataModel partnerDataModel;
    CommonCallBackListner commonCallBackListner;
    PartnersLoginReqModel partnersLoginReqModel;
    DashboardResModel dashboardResModel;
    StudentProfileViewModel viewModel;


    private boolean viewEmailStatus = false;
    private boolean viewNameStatus = false;

    public AskDetailCustomDialog(Context context, CustomDialogModel customDialogModel, CommonCallBackListner listner, PartnerDataModel model, PartnersLoginReqModel partnersLoginReqModel) {
        super(context);
        this.context = context;
        this.msg = customDialogModel.getContent();
        this.tittle = customDialogModel.getTitle();
        this.commonCallBackListner = listner;
        partnerDataModel = model;
        this.partnersLoginReqModel = partnersLoginReqModel;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.ask_detail_layout, null, false);
        setContentView(binding.getRoot());
        //  binding.tvTitle.setText(tittle);
        binding.btDone.setOnClickListener(this);
        binding.closeButton.setOnClickListener(this);
        setDataOnUI();
    }


    private void setDataOnUI() {
        DashboardResModel dashboardResModel = AuroAppPref.INSTANCE.getModelInstance().getDashboardResModel();
        PartnerRequiredParamModel model = partnerDataModel.getPartnerRequiredParamModel();

        if (model != null && model.getEmail()) {
            if (dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getEmail_id())) {
                binding.emailLayout.setVisibility(View.GONE);
            } else {
                viewEmailStatus = true;
                binding.emailLayout.setVisibility(View.VISIBLE);
            }
        }

        if (model != null && model.getName()) {
            if (dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getStudent_name())) {
                binding.nameLayout.setVisibility(View.GONE);
            } else {
                viewNameStatus = true;
                binding.nameLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btDone) {
            if (viewNameStatus) {
                String name = binding.textName.getText().toString();
                if (TextUtil.isEmpty(name)) {
                    showSnackBar("Please Enter Name");
                    return;
                }
            }
            if (viewEmailStatus) {
                String email = binding.textEmail.getText().toString();
                if (TextUtil.isEmpty(email)) {
                    showSnackBar("Please Enter Email");
                    return;
                } else if (!TextUtil.isEmpty(email) && !TextUtil.isValidEmail(email)) {
                    showSnackBar("Please Enter Valid Email");
                    return;
                }
            }

            senCallback();
        } else if (id == R.id.close_button) {
            dismiss();
        }

    }


    void showSnackBar(String msg) {
        ViewUtil.showSnackBar(binding.getRoot(), msg);
    }

    private void senCallback() {
        dashboardResModel = AuroAppPref.INSTANCE.getModelInstance().getDashboardResModel();
        if (TextUtil.isEmpty(partnersLoginReqModel.getStudentEmail())) {
            partnersLoginReqModel.setStudentEmail(binding.textEmail.getText().toString());
            if (dashboardResModel != null) {
                dashboardResModel.setEmail_id(partnersLoginReqModel.getStudentEmail());
            }
        }

        if (TextUtil.isEmpty(partnersLoginReqModel.getStudentName())) {
            partnersLoginReqModel.setStudentName(binding.textName.getText().toString());
            if (dashboardResModel != null) {
                dashboardResModel.setStudent_name(partnersLoginReqModel.getStudentEmail());
            }
        }
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setDashboardResModel(dashboardResModel);
        AuroAppPref.INSTANCE.setPref(prefModel);



        if (commonCallBackListner != null) {
            commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.NAME_DONE_CLICK, partnersLoginReqModel));
        }
    }


    public void showPorgress() {
        binding.btDone.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        dismiss();
        binding.btDone.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }

}
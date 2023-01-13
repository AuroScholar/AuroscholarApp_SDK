package com.auro.application.util.alert_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.ExitSelectionLayoutBinding;
import com.auro.application.util.AppUtil;

public class ExitDialog extends Dialog implements View.OnClickListener {

    public Activity context;
    private ExitSelectionLayoutBinding binding;
    String msg;

    public ExitDialog(@NonNull Activity context, String msg) {
        super(context);
        this.context = context;
        this.msg=msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.exit_selection_layout, null, false);
        binding.tvMessage.setText(msg);
        setContentView(binding.getRoot());
        setListener();
    }


    protected void setListener() {
        binding.closeButton.setOnClickListener(this);
        binding.hindiLayout.setOnClickListener(this);
        binding.englishLayout.setOnClickListener(this);
        binding.btnYes.setOnClickListener(this);
    }


    protected int getLayout() {
        return R.layout.exit_selection_layout;
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.button) {
            dismiss();
        } else if (id == R.id.close_button) {
            dismiss();
        } else if (id == R.id.btn_yes) {
            dismiss();
            if (AppUtil.commonCallBackListner != null) {
                AppUtil.commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.EXIT_DIALOG_CLICK, ""));
            }
        }
    }


}

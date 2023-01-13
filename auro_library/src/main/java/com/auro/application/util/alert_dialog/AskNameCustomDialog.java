package com.auro.application.util.alert_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.auro.application.databinding.AskNameLayoutBinding;
import com.auro.application.databinding.UpdateDialogCustomBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.util.AppUtil;
import com.auro.application.util.strings.AppStringDynamic;


public class AskNameCustomDialog extends Dialog implements View.OnClickListener {

    public Context context;
    private final String msg;
    private final String tittle;
    private AskNameLayoutBinding binding;


    CommonCallBackListner commonCallBackListner;


    public AskNameCustomDialog(Context context, CustomDialogModel customDialogModel, CommonCallBackListner listner) {
        super(context);
        this.context = context;
        this.msg = customDialogModel.getContent();
        this.tittle = customDialogModel.getTitle();
        this.commonCallBackListner = listner;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.ask_name_layout, null, false);
        setContentView(binding.getRoot());
        //  binding.tvTitle.setText(tittle);

        AppStringDynamic.setAskNameCustomDialogStrings(binding);
        binding.btDone.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btDone) {
            dismiss();
            if (commonCallBackListner != null) {
                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.NAME_DONE_CLICK, binding.textName.getText().toString()));
            }
        }

    }


}
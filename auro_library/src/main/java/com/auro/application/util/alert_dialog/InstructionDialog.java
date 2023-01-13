package com.auro.application.util.alert_dialog;

import android.app.Dialog;
import android.content.Context;
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
import com.auro.application.databinding.InstructionDialogCustomBinding;
import com.auro.application.home.data.model.response.InstructionsResModel;
import com.auro.application.util.AppUtil;


public class InstructionDialog extends Dialog {

    public Context context;
    private InstructionDialogCustomBinding binding;
    CommonCallBackListner commonCallBackListner;
    InstructionsResModel instructionsResModel;

    public InstructionDialog(Context context, CommonCallBackListner commonCallBackListner, InstructionsResModel instructionsResModel) {
        super(context);
        this.context = context;
        this.commonCallBackListner = commonCallBackListner;
        this.instructionsResModel = instructionsResModel;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.instruction_dialog_custom, null, false);
        setContentView(binding.getRoot());
        binding.tvTitle.setText(prefModel.getLanguageMasterDynamic().getDetails().getDialog_quiz_instruction());
        binding.RPAccept.setText(prefModel.getLanguageMasterDynamic().getDetails().getDialog_accept_continue());
        binding.tvMessage.setText(Html.fromHtml(instructionsResModel.getData()));
        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.RPAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (commonCallBackListner != null) {
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.ACCEPT_INSTRUCTION_CALLBACK, ""));
                }
            }
        });

    }


}
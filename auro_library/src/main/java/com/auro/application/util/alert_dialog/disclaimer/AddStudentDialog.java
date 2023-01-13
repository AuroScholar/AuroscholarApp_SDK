package com.auro.application.util.alert_dialog.disclaimer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.AddStudentDialogBinding;
import com.auro.application.databinding.AddedStudentLayoutBinding;
import com.auro.application.databinding.UserNotRegisteredDialogLayoutBinding;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.util.AppUtil;
import com.fueled.snippety.core.Snippety;
import com.fueled.snippety.core.Truss;


public class AddStudentDialog extends Dialog {

    public Activity activity;
    private AddStudentDialogBinding binding;
    PrefModel prefModel;
    CommonCallBackListner listner;

    public AddStudentDialog(Activity context, CommonCallBackListner commonCallBackListner) {
        super(context);
        this.activity = context;
        this.listner = commonCallBackListner;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.add_student_dialog, null, false);
        setContentView(binding.getRoot());
        //setText();
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        binding.closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dismiss();
            }
        });

        binding.RPAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listner!=null)
                {
                    listner.commonEventListner(AppUtil.getCommonClickModel(0,Status.REGISTER_CALLBACK,""));
                }


            }
        });
    }


    private void setText() {

        int leadWidth = activity.getResources().getDimensionPixelOffset(R.dimen.space_medium);
        int gapWidth = activity.getResources().getDimensionPixelOffset(R.dimen.space_xlarge);
        leadWidth = 0;
        binding.tvMessage.setText(new Truss()
                .appendln("I/we hereby allow my/our child to use the Auro Scholar application to take quizzes, learn and win scholarships. In consideration of this, I will not allow, promote or use any unfair means to help my child to get scholarships or while taking quizzes.", new Snippety().bullet(leadWidth, gapWidth))
                .appendln()
                .appendln("I/We declare to provide all the demographic and personal information voluntarily furnished by my/our child is true, correct and complete and has been under my/our supervision and Auro Scholar is not liable for any incorrect information provided by me/us.", new Snippety().bullet(leadWidth, gapWidth))
                .appendln()
                .appendln("I grant my consent to Auro Scholar to use the details provided, The Scholarship shall be transferred to my bank account post their consent.", new Snippety().bullet(leadWidth, gapWidth))
                .appendln()
                .build());

    }

}
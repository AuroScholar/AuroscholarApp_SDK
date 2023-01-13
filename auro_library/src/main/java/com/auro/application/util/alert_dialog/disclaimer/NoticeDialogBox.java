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
import com.auro.application.databinding.NoticeDialogLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.response.InstructionsResModel;
import com.auro.application.home.data.model.response.NoticeInstruction;
import com.auro.application.home.data.model.response.ResponseNoticeData;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.fueled.snippety.core.Snippety;
import com.fueled.snippety.core.Truss;

/**
 * Created by Pradeep Kumar Baral on 27/4/22.
 */
public class NoticeDialogBox extends Dialog {

    public Activity activity;
    private NoticeDialogLayoutBinding binding;
    PrefModel prefModel;
    CommonCallBackListner commonCallBackListner;

    public NoticeDialogBox(Activity context, CommonCallBackListner commonCallBackListner) {
        super(context);
        this.activity = context;
        this.commonCallBackListner = commonCallBackListner;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.notice_dialog_layout, null, false);
        setContentView(binding.getRoot());
        AppLogger.v("Notice","Open Notice Dialog - NoticeDialogBox ");
        setText();
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        //binding.closeBt.setVisibility(View.GONE);
        binding.closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.NOTICE_DIALOG, ""));
                dismiss();
               /* if (prefModel.isPreLoginDisclaimer()) {

                } else {
                    activity.finishAffinity();
                }*/
            }
        });
/*
        binding.RPAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                prefModel = AuroAppPref.INSTANCE.getModelInstance();
                prefModel.setPreLoginDisclaimer(true);
                AuroAppPref.INSTANCE.setPref(prefModel);
            }
        });*/
    }


    private void setText() {

    }


    void setData() {

    }

    public  void setAfterLoginInstruction(NoticeInstruction instructionsResModel) {
        if(isShowing()) {
            ResponseNoticeData responseNoticeData =  instructionsResModel.getData();
            if(!TextUtil.isEmpty(responseNoticeData.getMsgText()) || !responseNoticeData.getMsgText().equals("")){
                binding.tvTitle.setText(responseNoticeData.getTitle());
                binding.tvMessage.setText(responseNoticeData.getMsgText());
                binding.noticeImage.setVisibility(View.GONE);
                binding.tvMessage.setVisibility(View.VISIBLE);
            }else{
                ViewUtil.loadGalleryImage(responseNoticeData.getImg(),binding.noticeImage);
                binding.noticeImage.setVisibility(View.VISIBLE);
                binding.tvTitle.setText(responseNoticeData.getTitle());
                binding.tvMessage.setVisibility(View.GONE);
            }
           // binding.tvMessage.setText(Html.fromHtml(instructionsResModel.getData()));
        }
    }


}
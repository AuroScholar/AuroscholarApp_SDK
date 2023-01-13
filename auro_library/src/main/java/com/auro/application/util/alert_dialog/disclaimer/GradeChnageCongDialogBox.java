package com.auro.application.util.alert_dialog.disclaimer;

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
import com.auro.application.databinding.GradeCongDialogBinding;
import com.auro.application.databinding.NoticeDialogLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.response.NoticeInstruction;
import com.auro.application.home.data.model.response.ResponseNoticeData;
import com.auro.application.home.data.model.response.ShowDialogModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.fueled.snippety.core.Snippety;
import com.fueled.snippety.core.Truss;

/**
 * Created by Pradeep Kumar Baral on 27/4/22.
 */
public class GradeChnageCongDialogBox extends Dialog {

    public Activity activity;
    private GradeCongDialogBinding binding;
    PrefModel prefModel;
    CommonCallBackListner callBackListner;

    public GradeChnageCongDialogBox(Activity context, CommonCallBackListner commonCallBackListner) {
        super(context);
        this.activity = context;
        this.callBackListner = commonCallBackListner;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.grade_cong_dialog, null, false);
        setContentView(binding.getRoot());
        AppLogger.v("Notice","Open Notice Dialog - NoticeDialogBox ");
        //setText();
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        //binding.closeBt.setVisibility(View.GONE);
        binding.RlyesClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.GRADE_CHANGE_DIALOG, "Y"));
                dismiss();
               /* if (prefModel.isPreLoginDisclaimer()) {
                    dismiss();
                } else {
                    activity.finishAffinity();
                }*/
            }
        });

        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.GRADE_CHANGE_DIALOG, "N"));
                dismiss();
               /* prefModel = AuroAppPref.INSTANCE.getModelInstance();
                prefModel.setPreLoginDisclaimer(true);
                AuroAppPref.INSTANCE.setPref(prefModel);*/
            }
        });
    }


    private void setText() {


        try {
          /*  Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
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
                    .build());*/
        } catch (Exception e) {
           // setData();
        }


    }


    void setData() {
        int leadWidth = activity.getResources().getDimensionPixelOffset(R.dimen.space_medium);
        int gapWidth = activity.getResources().getDimensionPixelOffset(R.dimen.space_xlarge);
        leadWidth = 0;
       /* binding.tvMessage.setText(new Truss()
                .appendln(activity.getResources().getString(R.string.check_discalimer_first_text), new Snippety().bullet(leadWidth, gapWidth))
                .appendln()
                .appendln(activity.getResources().getString(R.string.check_discalimer_second_text), new Snippety().bullet(leadWidth, gapWidth))
                .appendln()
                .appendln(activity.getResources().getString(R.string.check_discalimer_third_text), new Snippety().bullet(leadWidth, gapWidth))
                .appendln()
                .build());*/
    }

    public  void setAfterLoginInstruction(ShowDialogModel showDialogModel) {
        if(isShowing()) {
            if(!showDialogModel.getImgUrl().equals("") && showDialogModel.getImgUrl() != null){
                   ViewUtil.loadGalleryImage(showDialogModel.getImgUrl(),binding.backgroundImage);
                   binding.titleGrade.setText(showDialogModel.getMessage());
            }

           /* if(!TextUtil.isEmpty(responseNoticeData.getMsgText()) || !responseNoticeData.getMsgText().equals("")){
                binding.tvTitle.setText(responseNoticeData.getTitle());
                binding.tvMessage.setText(responseNoticeData.getMsgText());
                binding.noticeImage.setVisibility(View.GONE);
                binding.tvMessage.setVisibility(View.VISIBLE);
            }else{
                ViewUtil.loadGalleryImage(responseNoticeData.getImg(),binding.noticeImage);
                binding.noticeImage.setVisibility(View.VISIBLE);
                binding.tvTitle.setText(responseNoticeData.getTitle());
                binding.tvMessage.setVisibility(View.GONE);
            }*/
           // binding.tvMessage.setText(Html.fromHtml(instructionsResModel.getData()));
        }
    }


}
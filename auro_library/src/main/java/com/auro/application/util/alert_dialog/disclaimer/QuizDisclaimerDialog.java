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
import com.auro.application.databinding.LoginDiscalimerDialogLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.util.AppUtil;
import com.fueled.snippety.core.Snippety;
import com.fueled.snippety.core.Truss;


public class QuizDisclaimerDialog extends Dialog {

    public Activity activity;
    private LoginDiscalimerDialogLayoutBinding binding;
    PrefModel prefModel;
    CommonCallBackListner commonCallBackListner;

    public QuizDisclaimerDialog(Activity context,CommonCallBackListner listner) {
        super(context);
        this.activity = context;
        this.commonCallBackListner=listner;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.login_discalimer_dialog_layout, null, false);
        setContentView(binding.getRoot());
        setText();
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
                prefModel.setPreQuizDisclaimer(false);
                AuroAppPref.INSTANCE.setPref(prefModel);
                if(commonCallBackListner!=null)
                {
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.ACCEPT_PARENT_BUTTON,""));
                }

            }
        });
    }


    private void setText() {

        try {
            Details  details=AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
            binding.RPAccept.setText(details.getAcceptAndStartQuiz());
            binding.tvTitle.setText(details.getDisclaimer());

            int leadWidth = activity.getResources().getDimensionPixelOffset(R.dimen.space_medium);
            int gapWidth = activity.getResources().getDimensionPixelOffset(R.dimen.space_xlarge);
            leadWidth = 0;
            binding.tvMessage.setText(new Truss()
                    .appendln(details.getQuizDisclaimerTextOne(), new Snippety().bullet(leadWidth, gapWidth))
                    .appendln()
                    .build());

        }catch (Exception e)
        {
            binding.RPAccept.setText(R.string.accept_and_start_quiz);

            int leadWidth = activity.getResources().getDimensionPixelOffset(R.dimen.space_medium);
            int gapWidth = activity.getResources().getDimensionPixelOffset(R.dimen.space_xlarge);
            leadWidth = 0;
            binding.tvMessage.setText(new Truss()
                    .appendln(activity.getResources().getString(R.string.quiz_disclaimer_text_one), new Snippety().bullet(leadWidth, gapWidth))
                    .appendln()
                    .build());
        }
    }
    /* .appendln("Parent will not help the student in quiz", new Snippety().bullet(leadWidth, gapWidth))
                .appendln()
                .appendln("Money will be transferred to the parent", new Snippety().bullet(leadWidth, gapWidth))*/

}
package com.auro.application.util.alert_dialog;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.databinding.DialogCustomBinding;
import com.auro.application.databinding.UpdateDialogCustomBinding;


public class UpdateCustomDialog extends Dialog implements View.OnClickListener {

    public Context context;
    private final String msg;
    private final String tittle;
    private UpdateDialogCustomBinding binding;

    OkCallcack okCallcack;
    SecondCallcack secondCallcack;
    FirstCallcack firstCallcack;


    public UpdateCustomDialog(Context context, CustomDialogModel customDialogModel) {
        super(context);
        this.context = context;
        this.msg = customDialogModel.getContent();
        this.tittle = customDialogModel.getTitle();


    }

    public void setOkCallback(OkCallcack okCallcack) {
        this.okCallcack = okCallcack;
    }

    public void setFirstCallcack(FirstCallcack firstCallcack) {
        this.firstCallcack = firstCallcack;
    }

    public void setSecondCallcack(SecondCallcack secondCallcack) {
        this.secondCallcack = secondCallcack;
    }




    public void showProgress(boolean isShow, String btnTxt) {
        if (isShow) {
            binding.btnYes.setText("");
            binding.btnYes.setClickable(false);
        } else {
            binding.btnYes.setText(btnTxt);
            binding.btnYes.setClickable(true);

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.update_dialog_custom, null, false);
        setContentView(binding.getRoot());

        binding.tvTitle.setText(tittle);
        binding.tvMessage.setText(msg);
        binding.btnYes.setOnClickListener(this);




    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_yes) {
            try {
                dismiss();
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.auro.application")));
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
        }

    }

    public interface OkCallcack {
        void clickOkCallback();
    }

    public interface SecondCallcack {
        void clickYesCallback();
    }

    public interface FirstCallcack {
        void clickNoCallback();
    }


}
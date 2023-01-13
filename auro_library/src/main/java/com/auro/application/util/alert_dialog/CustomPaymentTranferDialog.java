package com.auro.application.util.alert_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.databinding.DialogCustomBinding;
import com.auro.application.databinding.DialogPaymentTransferBinding;
import com.bumptech.glide.Glide;

public class CustomPaymentTranferDialog extends Dialog implements View.OnClickListener {


    public Context context;
    private final String msg;
    private String firstBtnTxt;
    private String secondBtnTxt;
    private String singleBtnTxt;
    private final String tittle;
    private final boolean istwoBtnRequired;
    private DialogPaymentTransferBinding binding;

    CustomDialog.OkCallcack okCallcack;
    CustomDialog.SecondCallcack secondCallcack;
    CustomDialog.FirstCallcack firstCallcack;
    CustomDialogModel customDialogModel;


    public CustomPaymentTranferDialog(Context context,CustomDialogModel customDialogModel) {
        super(context);
        this.context = context;
        this.msg = customDialogModel.getContent();
        this.tittle = customDialogModel.getTitle();
        this.istwoBtnRequired = customDialogModel.isTwoButtonRequired();
        this.customDialogModel =customDialogModel;


    }

    public void setOkCallback(CustomDialog.OkCallcack okCallcack) {
        this.okCallcack = okCallcack;
    }

    public void setFirstCallcack(CustomDialog.FirstCallcack firstCallcack) {
        this.firstCallcack = firstCallcack;
    }

    public void setSecondCallcack(CustomDialog.SecondCallcack secondCallcack) {
        this.secondCallcack = secondCallcack;
    }

    public void setFirstBtnTxt(String firstBtnTxt) {
        this.firstBtnTxt = firstBtnTxt;
    }

    public void setSingleBtnTxt(String singleBtnTxt) {
        this.singleBtnTxt = singleBtnTxt;
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


    public void setSecondBtnTxt(String secondBtnTxt) {
        this.secondBtnTxt = secondBtnTxt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_payment_transfer, null, false);
        setContentView(binding.getRoot());

        binding.tvTitle.setText(tittle);
        binding.tvMessage.setText(msg);

        if (istwoBtnRequired) {
            binding.btnYes.setVisibility(View.VISIBLE);
         //   binding.btnNo.setVisibility(View.GONE);
            binding.btnYes.setText(secondBtnTxt);
          // binding.btnNo.setText(firstBtnTxt);
            binding.btnYes.setOnClickListener(this);
          //  binding.btnNo.setOnClickListener(this);
        } else {
            binding.btnYes.setVisibility(View.GONE);
           // binding.btnNo.setVisibility(View.GONE);
            binding.btnYes.setText(singleBtnTxt);
            binding.btnYes.setOnClickListener(this);
        }
        binding.tvTitle.setVisibility(View.GONE);
        if(customDialogModel.getContent().contains("Request accepted")){
            binding.tvTitle.setText("Payment Successful");
            binding.checkIcon.setBackground(AuroApp.getAppContext().getResources().getDrawable(R.drawable.whitecheck));
            binding.relativeLayoutTitle.setBackgroundColor(context.getResources().getColor(R.color.payment_color_green));

        }else{
            binding.tvTitle.setText("Transation Fail");
            binding.relativeLayoutTitle.setBackgroundColor(context.getResources().getColor(R.color.red_dark));
            binding.checkIcon.setBackground(AuroApp.getAppContext().getResources().getDrawable(R.drawable.ic_canclepayment));

        }


    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_no) {
            firstCallcack.clickNoCallback();
        } else if (view.getId() == R.id.btn_yes) {
            if (istwoBtnRequired) {
                secondCallcack.clickYesCallback();
            } else {
                okCallcack.clickOkCallback();
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

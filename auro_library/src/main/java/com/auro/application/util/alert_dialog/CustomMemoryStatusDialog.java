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
import com.auro.application.databinding.DialogMemoryStatusBinding;
import com.auro.application.databinding.DialogPaymentTransferBinding;
import com.auro.application.util.AppLogger;

import static com.auro.application.util.MemoryStatus.humanReadableByteCountSI;

public class CustomMemoryStatusDialog extends Dialog implements View.OnClickListener {


    public Context context;
    private final long freeSpace;
    private final long totalSpace;
    private String firstBtnTxt;
    private String secondBtnTxt;
    private String singleBtnTxt;
    private final String tittle;
    private final boolean istwoBtnRequired;
    private DialogMemoryStatusBinding binding;

    CustomDialog.OkCallcack okCallcack;
    CustomDialog.SecondCallcack secondCallcack;
    CustomDialog.FirstCallcack firstCallcack;
    CustomDialogModel customDialogModel;


    public CustomMemoryStatusDialog(Context context, CustomDialogModel customDialogModel) {
        super(context);
        this.context = context;
        this.freeSpace = customDialogModel.getFreeSpace();
        this.totalSpace = customDialogModel.getTotalMemory();
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_memory_status, null, false);
        setContentView(binding.getRoot());

            binding.btnYes.setOnClickListener(this);
            binding.tvTitleMemory.setText("Low Internal Storage");





        String formatSize = humanReadableByteCountSI(freeSpace);
        binding.tvFreeSpace.setText(formatSize+" Free");
        String formatTotalSize = humanReadableByteCountSI(totalSpace);
        binding.tvTotalSpace.setText("Internal Storage ("+formatTotalSize+")");



        float freeSpaceint = Float.valueOf(formatSize.replaceAll("[^\\d.]", "")); // remove all non-numeric symbols
        float totalSpaceint = Float.valueOf(formatTotalSize.replaceAll("[^\\d.]", ""));
        int remainingfree = (int)totalSpaceint - (int)freeSpaceint;
        binding.indicator.setProgress(remainingfree);

        binding.indicator.setMax((int)totalSpaceint);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_no) {
            firstCallcack.clickNoCallback();
        } else if (view.getId() == R.id.btn_yes) {
          /*  if (istwoBtnRequired) {*/
                secondCallcack.clickYesCallback();
          /*  } else {
                okCallcack.clickOkCallback();
            }*/
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

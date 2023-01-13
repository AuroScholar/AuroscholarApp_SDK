package com.auro.application.util.alert_dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.databinding.CustomActionSnackbarBinding;
import com.auro.application.databinding.CustomSnackbarBinding;
import com.google.android.material.snackbar.Snackbar;

public class InstallSnackbar {


    InstallSnackbar(){

    }

    public static View showSnackbarClickAction(View relative, String msg, int color,OnClickButton onClickButton) { // Create the Snackbar
        CustomActionSnackbarBinding  binding;

        Snackbar snackbar = Snackbar.make(relative, "", Snackbar.LENGTH_LONG);

        //inflate view
        LayoutInflater inflater = (LayoutInflater) relative.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // View snackView = inflater.inflate(R.layout.app_custom_snackbar, null);
        binding = DataBindingUtil.inflate(inflater, R.layout.custom_action_snackbar, null, false);
        // White background
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        snackbar.getView().setBackgroundColor(color);
        //TextView tv_msg = snackView.findViewById(R.id.textMsg);
        binding.textMsg.setText(msg);
        binding.background.setBackgroundColor(color);
        binding.btninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton.onClickinstall();
            }
        });
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        if (binding.getRoot().getParent() != null){
            ((ViewGroup) binding.getRoot().getParent()).removeView(binding.getRoot());

        }
        snackBarView.addView(binding.getRoot(), 0);
        snackbar.setDuration(2000);
        snackbar.show();
        snackBarView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                snackBarView.getViewTreeObserver().removeOnPreDrawListener(this);
                snackbar.setBehavior(null);
                return true;
            }
        });



        return binding.getRoot();

    }
    public interface OnClickButton{
        void onClickinstall();
    }


}

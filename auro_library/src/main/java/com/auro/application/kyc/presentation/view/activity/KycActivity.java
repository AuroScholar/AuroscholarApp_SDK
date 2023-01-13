package com.auro.application.kyc.presentation.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.auro.application.R;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.kyc.presentation.view.fragment.KycNewScreenFragment;
import com.auro.application.util.alert_dialog.LanguageChangeDialog;

import java.util.Objects;

public class KycActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);

        openFragment(new KycNewScreenFragment());
    }
    private void openFragment(Fragment fragment) {

        FragmentUtil.addFragment(this, fragment, R.id.frameLayout, 0);

    }

    public void openChangeLanguageDialog() {
        LanguageChangeDialog languageChangeDialog = new LanguageChangeDialog(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(languageChangeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        languageChangeDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(languageChangeDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        languageChangeDialog.setCancelable(true);
        languageChangeDialog.show();

    }

}
package com.auro.application.util.alert_dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.PartnerDetailDialogBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.partnersmodel.PartnerDataModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;


public class PartnerDetailDialog extends Dialog implements View.OnClickListener {

    public Context context;
    private PartnerDetailDialogBinding binding;
    CommonCallBackListner commonCallBackListner;
    PartnerDataModel model;


    public PartnerDetailDialog(Context context, CommonCallBackListner listner, PartnerDataModel partnerDataModel) {
        super(context);
        this.context = context;
        this.commonCallBackListner = listner;
        this.model=partnerDataModel;
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.partner_detail_dialog, null, false);
        setContentView(binding.getRoot());
        init();

        try
        {
            Details details= AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
            binding.RPButtonExplore.setText(details.getStartNow()+" "+model.getPartnerName());

        }catch (Exception e)
        {
            AppLogger.e("PartnerDetailDialog",e.getMessage());
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.close_button) {
            dismiss();
        } else if (id == R.id.RPButtonExplore) {
            dismiss();
            if (commonCallBackListner != null) {
                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.PARTNER_EXPLORE, ""));

            }
        }

    }


    private void init() {
        ImageUtil.loadCircleImage(binding.img,model.getPartnerLogo());
        binding.RPTextView.setText(model.getDescription());
        binding.RPTextViewTitle.setText(model.getPartnerName());
        binding.RPButtonExplore.setOnClickListener(this);
    }
}
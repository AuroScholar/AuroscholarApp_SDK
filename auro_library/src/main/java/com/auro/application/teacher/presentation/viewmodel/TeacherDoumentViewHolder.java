package com.auro.application.teacher.presentation.viewmodel;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.FragmentKycInfoItemLayoutBinding;

import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.teacher.data.model.response.KycItemDataResModel;
import com.auro.application.teacher.data.model.response.TeacherKycStatusResModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;

public class TeacherDoumentViewHolder extends RecyclerView.ViewHolder {

    FragmentKycInfoItemLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    int position;
    KYCDocumentDatamodel model;

    public TeacherDoumentViewHolder(@NonNull FragmentKycInfoItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindUser(KYCDocumentDatamodel model, int position, CommonCallBackListner commonCallBackListner) {
        this.commonCallBackListner = commonCallBackListner;
        this.position = position;
        this.model = model;
        KycItemDataResModel resModel = model.getTeacherKycStatusResModel();
        binding.documentName.setText(model.getDocumentName());
        binding.documentDesc.setText(model.getDocumentDesc());

        if (!resModel.getDocumentPath().isEmpty()) {
            binding.uploadIcon.setImageDrawable(binding.getRoot().getContext().getDrawable(R.drawable.ic_tick));
            binding.uploadText.setVisibility(View.GONE);
            if (!resModel.getDocRemarks().isEmpty()) {
                binding.statusDescLayout.setVisibility(View.VISIBLE);
                binding.docStatusTxt.setText(resModel.getDocRemarks());
                if (resModel.getDocStatus().equalsIgnoreCase(AppConstant.TeacherKYCDocumentStatus.APPROVE)) {
                    binding.docStatusImage.setImageDrawable(binding.getRoot().getContext().getDrawable(R.drawable.ic_document_verified_icon));
                } else {
                    setOnClickListner(binding);
                    binding.uploadIcon.setImageDrawable(binding.getRoot().getContext().getDrawable(R.drawable.ic_upload_icon));
                    binding.uploadText.setText("Re-Upload");
                    binding.uploadText.setVisibility(View.VISIBLE);
                    binding.docStatusImage.setImageDrawable(binding.getRoot().getContext().getDrawable(R.drawable.ic_document_blur_icon));
                }
            } else {
                binding.statusDescLayout.setVisibility(View.GONE);
            }
        } else {
            binding.uploadIcon.setImageDrawable(binding.getRoot().getContext().getDrawable(R.drawable.ic_upload_icon));
            binding.uploadText.setVisibility(View.VISIBLE);
            binding.uploadText.setText("Upload");
            binding.statusDescLayout.setVisibility(View.GONE);
            setOnClickListner(binding);
        }

        /*binding.txtDocumentName.setText(model.getDocumentName());
        if (!model.getDocumentFileName().equalsIgnoreCase(AuroApp.getAppContext().getResources().getString(R.string.no_file_chosen))) {
            binding.txtFileName.setVisibility(View.VISIBLE);
            binding.txtFileName.setText(model.getDocumentFileName());
            binding.docImg.setOnClickListener(null);
        } else {
            binding.txtFileName.setVisibility(View.GONE);
        }
        if (model.isModify()) {
            binding.docImg.setImageDrawable(AuroApp.getAppContext().getResources().getDrawable(R.drawable.ic_auro_check));
        } else {
            binding.docImg.setImageDrawable(AuroApp.getAppContext().getResources().getDrawable(R.drawable.ic_auro_upload));
            binding.docImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.DOCUMENT_CLICK, model));
                    }

                }
            });
        }*/


    }

    void setOnClickListner(FragmentKycInfoItemLayoutBinding binding) {
        binding.uploadButtonStatusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLogger.e("setOnClickListner - ","step 1");
                if (commonCallBackListner != null) {
                    AppLogger.e("setOnClickListner - ","step 2");
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.DOCUMENT_CLICK, model));
                }

            }
        });
    }
}

package com.auro.application.teacher.presentation.view.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.FragmentKycInfoItemLayoutBinding;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.data.model.response.KycDocResModel;
import com.auro.application.teacher.data.model.response.KycItemDataResModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;

public class StudentDoumentViewHolder extends RecyclerView.ViewHolder {

    FragmentKycInfoItemLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    int position;
    KYCDocumentDatamodel model;

    public StudentDoumentViewHolder(@NonNull FragmentKycInfoItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindUser(KYCDocumentDatamodel model, int position, CommonCallBackListner commonCallBackListner) {
        this.commonCallBackListner = commonCallBackListner;
        this.position = position;
        this.model = model;
        KycDocResModel resModel = model.getStudentKycDocResModel();
        binding.documentName.setText(model.getDocumentName());
        binding.documentDesc.setText(model.getDocumentDesc());


        if (resModel != null && resModel.getDocumentPath() != null && !resModel.getDocumentPath().isEmpty()) {
            binding.uploadText.setVisibility(View.GONE);
            if (resModel.getDocRemarks() != null && !resModel.getDocRemarks().isEmpty()) {
                binding.statusDescLayout.setVisibility(View.VISIBLE);
                binding.docStatusTxt.setText(resModel.getDocRemarks());
                if (resModel.getDocStatus() != null && resModel.getDocStatus().equalsIgnoreCase(AppConstant.TeacherKYCDocumentStatus.APPROVE)) {
                    binding.docStatusImage.setImageDrawable(binding.getRoot().getContext().getDrawable(R.drawable.ic_document_verified_icon));
                    binding.uploadIcon.setVisibility(View.VISIBLE);
                    binding.uploadIcon.setImageDrawable(binding.getRoot().getContext().getDrawable(R.drawable.ic_tick));
                } else {
                    setOnClickListner(binding);
                    binding.uploadIcon.setImageDrawable(binding.getRoot().getContext().getDrawable(R.drawable.ic_upload_icon));
                    binding.uploadText.setText(AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails().getKyc_reupload());//
                    binding.uploadText.setVisibility(View.VISIBLE);
                    binding.docStatusImage.setImageDrawable(binding.getRoot().getContext().getDrawable(R.drawable.ic_document_blur_icon));
                }
            } else {
                if (resModel.getDocRemarks() != null && !resModel.getDocRemarks().isEmpty()) {
                    binding.documentDesc.setText(R.string.uploaded);
                } else {
                    binding.documentDesc.setText(model.getDocumentDesc());
                }
                setOnClickListner(binding);
                binding.statusDescLayout.setVisibility(View.GONE);
            }
        } else {
            binding.uploadIcon.setImageDrawable(binding.getRoot().getContext().getDrawable(R.drawable.ic_upload_icon));
            binding.uploadText.setVisibility(View.VISIBLE);
            binding.uploadText.setText(AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails().getUpload());//R.string.uplaod_txt
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
                AppLogger.e("setOnClickListner - ", "step 1");
                if (commonCallBackListner != null) {
                    AppLogger.e("setOnClickPradeep", "step 2 "+model.getDocumentName());
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.DOCUMENT_CLICK, model));
                }

            }
        });
    }
}

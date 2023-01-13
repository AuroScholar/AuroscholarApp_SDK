package com.auro.application.teacher.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.databinding.DocumentUploadItemLayoutBinding;
import com.auro.application.teacher.data.model.response.StudentWalletTeacherResModel;

import java.util.List;

public class StudentWalletInfoAdapter  extends RecyclerView.Adapter<StudentWalletInfoAdapter.StudentWalletHolder> {

    List<StudentWalletTeacherResModel> mValues;
    Context mContext;
    DocumentUploadItemLayoutBinding  binding;
    CommonCallBackListner listner;

    public StudentWalletInfoAdapter(Context context, List<StudentWalletTeacherResModel> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner = listner;
    }

    public void updateList(List<StudentWalletTeacherResModel> values) {
        mValues = values;
        notifyDataSetChanged();
    }
    public class StudentWalletHolder extends RecyclerView.ViewHolder {
        DocumentUploadItemLayoutBinding binding;

        public StudentWalletHolder(DocumentUploadItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(StudentWalletTeacherResModel resModel, int position) {
            binding.backgroundBox.setBackground(resModel.getDrawable());
            binding.textkycUpload.setText(resModel.getTotalValue());
            binding.textupload.setText(resModel.getNameOfDocument());

           /* if (resModel.isSelect()) {
                binding.selectImg.setImageDrawable(AuroApp.getAppContext().getResources().getDrawable(R.drawable.ic_check));
            } else {
                binding.selectImg.setImageDrawable(AuroApp.getAppContext().getResources().getDrawable(R.drawable.ic_uncheck));
            }
            //resModel.setCertificateImage("https://image.slidesharecdn.com/b1c107f5-eaf4-4dd9-8acc-df180578c33c-160501092731/95/ismail-british-council-certificate-1-638.jpg?cb=1462094874");
            ImageUtil.loadNormalImage(binding.certificateImg, resModel.getCertificateImage());*/
        }

    }

    @Override
    public StudentWalletInfoAdapter.StudentWalletHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.document_upload_item_layout, viewGroup, false);
        return new StudentWalletInfoAdapter.StudentWalletHolder(binding);
    }


    @Override
    public void onBindViewHolder(StudentWalletInfoAdapter.StudentWalletHolder Vholder, int position) {
        Vholder.setData(mValues.get(position), position);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void updateData() {

    }

}

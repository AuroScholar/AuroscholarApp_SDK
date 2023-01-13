package com.auro.application.teacher.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.databinding.SendMessageImageLayoutBinding;
import com.auro.application.teacher.data.model.response.TotalStudentResModel;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.TextUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ClassHolder>{

    List<TotalStudentResModel> mValues;
    Context mContext;
    SendMessageImageLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;

    public StudentListAdapter(Context mContext, List<TotalStudentResModel> mValues, CommonCallBackListner commonCallBackListner) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    public void setData( List<TotalStudentResModel> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public StudentListAdapter.ClassHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
       // return null;
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.send_message_image_layout, viewGroup, false);
        return new StudentListAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StudentListAdapter.ClassHolder Vholder, int position) {
        Vholder.setData(mValues, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {


        SendMessageImageLayoutBinding binding;

        public ClassHolder(@NonNull @NotNull SendMessageImageLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(List<TotalStudentResModel> mValues, int position) {
            TotalStudentResModel totalStudentResModel=mValues.get(position);

            if(!TextUtil.isEmpty(totalStudentResModel.getStudentName())) {
                binding.tvStudentName.setText(mValues.get(position).getStudentName());
            }
            if(!TextUtil.isEmpty(""+totalStudentResModel.getTotalScore())) {
                binding.tvStudentScore.setText("Score: "+mValues.get(position).getTotalScore());
            }
            if(!TextUtil.isEmpty(totalStudentResModel.getProfilePic())) {
                ImageUtil.loadCircleImage(binding.studentImage,mValues.get(position).getProfilePic());
            }
        }
    }
}

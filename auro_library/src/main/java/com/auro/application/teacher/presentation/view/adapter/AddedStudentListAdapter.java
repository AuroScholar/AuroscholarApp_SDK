package com.auro.application.teacher.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.AddedStudentLayoutBinding;
import com.auro.application.teacher.data.model.response.TotalStudentResModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.TextUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddedStudentListAdapter extends RecyclerView.Adapter<AddedStudentListAdapter.ClassHolder> {
    List<TotalStudentResModel> mValues;
    Context mContext;
    AddedStudentLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;

    public AddedStudentListAdapter(Context mContext, List<TotalStudentResModel> mValues, CommonCallBackListner commonCallBackListner) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    public void setData( List<TotalStudentResModel> mValues) {
        AppLogger.e("ADD_STUDENT ","step 6");
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public AddedStudentListAdapter.ClassHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        // return null;
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.added_student_layout, viewGroup, false);
        return new AddedStudentListAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddedStudentListAdapter.ClassHolder Vholder, int position) {
        Vholder.setData(mValues, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {


        AddedStudentLayoutBinding binding;

        public ClassHolder(@NonNull AddedStudentLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(List<TotalStudentResModel> mValues, int position) {
            TotalStudentResModel totalStudentResModel=mValues.get(position);
            ImageUtil.loadCircleImage(binding.studentImage,totalStudentResModel.getProfilePic());
            if(!TextUtil.isEmpty(totalStudentResModel.getStudentName())) {
                binding.studentName.setText(totalStudentResModel.getStudentName());
            }else
            {
                binding.studentName.setText("NA");
            }
            binding.removeBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.REMOVE_STUDENT, totalStudentResModel));
                    }
                }
            });
        }
    }
}

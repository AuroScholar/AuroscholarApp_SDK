package com.auro.application.teacher.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.AddedStudentLayoutBinding;
import com.auro.application.databinding.GroupStudentListItemBinding;
import com.auro.application.teacher.data.model.response.TotalStudentResModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.TextUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class GroupStudentListAdapter extends RecyclerView.Adapter<GroupStudentListAdapter.ClassHolder> {
    List<TotalStudentResModel> mValues;
    Context mContext;
    GroupStudentListItemBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;

    public GroupStudentListAdapter(Context mContext, List<TotalStudentResModel> mValues, CommonCallBackListner commonCallBackListner) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    public void setData(List<TotalStudentResModel> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public GroupStudentListAdapter.ClassHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        // return null;
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.group_student_list_item, viewGroup, false);
        return new GroupStudentListAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GroupStudentListAdapter.ClassHolder Vholder, int position) {
        Vholder.setData(mValues, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {


        GroupStudentListItemBinding binding;

        public ClassHolder(@NonNull GroupStudentListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(List<TotalStudentResModel> mValues, int position) {
            TotalStudentResModel totalStudentResModel = mValues.get(position);
            ImageUtil.loadCircleImage(binding.studentImage, totalStudentResModel.getProfilePic());

            if (!TextUtil.isEmpty(totalStudentResModel.getStudentName())) {
                binding.studentName.setText(totalStudentResModel.getStudentName());
            } else {
                binding.studentName.setText("NA");
            }
            binding.addBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppLogger.e("ADD_STUDENT ","step 1");
                    if (commonCallBackListner != null) {
                        AppLogger.e("ADD_STUDENT ","step 2");
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.ADD_STUDENT, totalStudentResModel));
                    }
                }
            });

        }
    }
}

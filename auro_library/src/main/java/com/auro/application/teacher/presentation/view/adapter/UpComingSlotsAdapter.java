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
import com.auro.application.databinding.UpcomingItemLayoutBinding;
import com.auro.application.teacher.data.model.response.AvailableSlotResModel;
import com.auro.application.util.AppUtil;
import com.auro.application.util.DateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UpComingSlotsAdapter extends RecyclerView.Adapter<UpComingSlotsAdapter.ClassHolder>{

    List<AvailableSlotResModel> mValues;
    Context mContext;
    UpcomingItemLayoutBinding  binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;

    public UpComingSlotsAdapter(Context mContext, List<AvailableSlotResModel> mValues, CommonCallBackListner commonCallBackListner) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    @NotNull
    @Override
    public UpComingSlotsAdapter.ClassHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // return null;
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.upcoming_item_layout, viewGroup, false);
        return new UpComingSlotsAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UpComingSlotsAdapter.ClassHolder Vholder, int position) {
        Vholder.setData(mValues, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {


        UpcomingItemLayoutBinding binding;

        public ClassHolder(@NonNull UpcomingItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(List<AvailableSlotResModel> mValues, int position) {
            AvailableSlotResModel availableSlotResModel=mValues.get(position);
            String dateName= DateUtil.getDate(availableSlotResModel.getDate());
            binding.tvDateTitle.setText(dateName);
            binding.UpCommingOnClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (commonCallBackListner != null) {

                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.ONCLICKFORTIMESLOT, mValues.get(position)));
                    }
                }
            });
        }
    }
}


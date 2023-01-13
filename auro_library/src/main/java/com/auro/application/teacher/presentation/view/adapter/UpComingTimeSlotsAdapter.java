package com.auro.application.teacher.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.UpcomingItemLayoutBinding;
import com.auro.application.databinding.UpcomingTimeItemLayoutBinding;
import com.auro.application.teacher.data.model.response.AvailableSlotResModel;
import com.auro.application.teacher.data.model.response.TimeSlotResModel;
import com.auro.application.util.AppUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UpComingTimeSlotsAdapter extends RecyclerView.Adapter<UpComingTimeSlotsAdapter.UpClassHolder> {

    List<TimeSlotResModel> mValues;
    Context mContext;
    UpcomingTimeItemLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;

    public UpComingTimeSlotsAdapter(Context mContext, List<TimeSlotResModel> mValues, CommonCallBackListner commonCallBackListner) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    @NotNull
    @Override
    public UpComingTimeSlotsAdapter.UpClassHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // return null;
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.upcoming_time_item_layout, viewGroup, false);
        return new UpComingTimeSlotsAdapter.UpClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UpComingTimeSlotsAdapter.UpClassHolder Vholder, int position) {
        Vholder.setData(mValues, position);
        TimeSlotResModel timeSlotResModel = mValues.get(position);
        Vholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList(position);
            }
        });
    }

    private void updateList(int position) {
        for (int i = 0; i < mValues.size(); i++) {
            if (i == position) {
                mValues.get(i).setSelected(true);

            } else {
                mValues.get(i).setSelected(false);

            }
        }

        if(commonCallBackListner!=null)
        {
            commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.BOOK_SLOT_CLICK,mValues.get(position)));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public  class UpClassHolder extends RecyclerView.ViewHolder {
        UpcomingTimeItemLayoutBinding binding;

        public UpClassHolder(@NonNull UpcomingTimeItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(List<TimeSlotResModel> mValues, int position) {

            TimeSlotResModel timeSlotResModel = mValues.get(position);
            binding.txtClass.setText(timeSlotResModel.getStartTime());
            if(timeSlotResModel.isSelected())
            {
                binding.txtClass.setTextColor( ContextCompat.getColor(mContext, R.color.white));
                binding.buttonClick.setBackground(ContextCompat.getDrawable(mContext, R.drawable.class_bule_border_background));

            }else {
                binding.buttonClick.setBackground(ContextCompat.getDrawable(mContext, R.drawable.class_borader_background));
                binding.txtClass.setTextColor( ContextCompat.getColor(mContext, R.color.grey_color));

            }

        }
    }
}


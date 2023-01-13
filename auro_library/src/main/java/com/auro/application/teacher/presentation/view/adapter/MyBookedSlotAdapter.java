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
import com.auro.application.databinding.BookedSlotsItemLayoutBinding;
import com.auro.application.databinding.HorizantalImageItemLayoutBinding;
import com.auro.application.teacher.data.model.response.BookedSlotResModel;
import com.auro.application.teacher.data.model.response.UserImageInGroupResModel;
import com.auro.application.util.AppUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyBookedSlotAdapter extends RecyclerView.Adapter<MyBookedSlotAdapter.ClassHolder> {

    List<BookedSlotResModel> mValues;
    Context mContext;
    BookedSlotsItemLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;

    public MyBookedSlotAdapter(Context mContext, List<BookedSlotResModel> mValues, CommonCallBackListner commonCallBackListner) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    @NonNull
    @Override
    public MyBookedSlotAdapter.ClassHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.booked_slots_item_layout, viewGroup, false);
        return new MyBookedSlotAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyBookedSlotAdapter.ClassHolder Vholder, int position) {
        Vholder.setData(mValues, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setListInAdapter(List<BookedSlotResModel> list){

        mValues = list;
        notifyDataSetChanged();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {
        BookedSlotsItemLayoutBinding binding;

        public ClassHolder(@NonNull @NotNull BookedSlotsItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(List<BookedSlotResModel> mValues, int position) {
            BookedSlotResModel bookedSlotResModel=mValues.get(position);
            binding.title.setText(bookedSlotResModel.getWebinarName());
            binding.date.setText(bookedSlotResModel.getDate());

            binding.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.CANCEL_CLICK, mValues.get(position)));
                    }
                }
            });
        }
    }
}

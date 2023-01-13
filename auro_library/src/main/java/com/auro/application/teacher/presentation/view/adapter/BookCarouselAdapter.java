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
import com.auro.application.databinding.DocumentUploadItemLayoutBinding;
import com.auro.application.databinding.UpdateConfirmTimeDialogBinding;
import com.auro.application.teacher.data.model.response.TimeSlotResModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.DateUtil;
import com.auro.application.util.ViewUtil;

import java.util.List;

public class BookCarouselAdapter extends RecyclerView.Adapter<BookCarouselAdapter.ItemViewHolder> {
    UpdateConfirmTimeDialogBinding binding;

    List<TimeSlotResModel> items;
    Context mContext;
    CommonCallBackListner listner;
    String date;
    BookCarouselAdapter.ItemViewHolder holder;
    public BookCarouselAdapter(Context mContext, CommonCallBackListner listner, List<TimeSlotResModel> listTimeSlot,String date) {
        this.mContext = mContext;
        this.listner = listner;
        this.items = listTimeSlot;
        this.date = date;
    }

    @NonNull
    @Override
    public BookCarouselAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.update_confirm_time_dialog, viewGroup, false);
        return new BookCarouselAdapter.ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookCarouselAdapter.ItemViewHolder holder, int position) {
        this.holder = holder;
        holder.setBinding(items.get(position),date,position);
    }

  /*  public void setOpenProgressBar(int value,String message){
        switch(value){
            case 0:
                holder.binding.progressGif.setVisibility(View.VISIBLE);
                holder.binding.tvSelectShot.setVisibility(View.GONE);
                break;
            case 1:
                holder.binding.progressGif.setVisibility(View.GONE);
                holder.binding.tvSelectShot.setVisibility(View.VISIBLE);
                break;
            case 2:
                ViewUtil.showSnackBar(binding.getRoot(),message );
                break;
        }

    }*/

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<TimeSlotResModel> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        UpdateConfirmTimeDialogBinding binding;

        public ItemViewHolder(@NonNull UpdateConfirmTimeDialogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBinding(TimeSlotResModel timeSlotResModel, String date, int position) {
            binding.tvCount.setText(timeSlotResModel.getSeatsLeft() + "");
            binding.tvTopic.setText(timeSlotResModel.getWebinarName());
            String datenew = DateUtil.getDate(date) + " " + date;
            binding.tvDayTitle.setText(datenew);
            binding.tvTimenew.setText(timeSlotResModel.getStartTime());
            binding.tvSelectShot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppLogger.v("BookSlot", "Step 1 " + listner);
                    if (listner != null) {
                        AppLogger.v("BookSlot", "Step 2 " + listner);
                        listner.commonEventListner(AppUtil.getCommonClickModel(position, Status.BOOK_WEBINAR_SLOT, timeSlotResModel));
                        setOpenProgressBar(0, "");
                    }
                }
            });
        }

        public void setOpenProgressBar(int value, String message) {
            switch (value) {
                case 0:
                    binding.progressGif.setVisibility(View.VISIBLE);
                    binding.tvSelectShot.setVisibility(View.GONE);
                    break;
                case 1:
                    binding.progressGif.setVisibility(View.GONE);
                    binding.tvSelectShot.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    ViewUtil.showSnackBar(binding.getRoot(), message);
                    break;
            }
        }
    }
}

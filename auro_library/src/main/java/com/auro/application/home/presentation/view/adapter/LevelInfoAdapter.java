package com.auro.application.home.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.RatingLevelLayoutBinding;
import com.auro.application.databinding.ScholarshipItemLayoutBinding;
import com.auro.application.home.data.model.response.SlabModel;
import com.auro.application.util.AppUtil;

import java.util.ArrayList;
import java.util.List;


public class LevelInfoAdapter extends RecyclerView.Adapter<LevelInfoAdapter.ViewHolder> {

    List<SlabModel> mValues;
    Context mContext;
    RatingLevelLayoutBinding binding;
    CommonCallBackListner listner;

    public LevelInfoAdapter(Context context, List<SlabModel> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner = listner;
    }

    public void updateList(ArrayList<SlabModel> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RatingLevelLayoutBinding binding;

        public ViewHolder(RatingLevelLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(SlabModel resModel, int position) {
            binding.quizAmount.setText(AuroApp.getAppContext().getResources().getString(R.string.rs)+resModel.getPrice());
            binding.rating.setRating(resModel.getDetails().size());
        }

    }

    @Override
    public LevelInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.rating_level_layout, viewGroup, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position), position);
        Vholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.commonEventListner(AppUtil.getCommonClickModel(1, Status.ITEM_CLICK, mValues.get(position)));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void updateData() {

    }

}

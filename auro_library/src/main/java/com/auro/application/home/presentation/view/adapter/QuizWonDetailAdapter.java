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
import com.auro.application.databinding.ScholarshipItemLayoutBinding;
import com.auro.application.databinding.ScholarshipQuizItemLayoutBinding;
import com.auro.application.home.data.model.response.SlabDetailResModel;
import com.auro.application.home.data.model.response.SlabModel;
import com.auro.application.util.AppUtil;

import java.util.ArrayList;
import java.util.List;


public class QuizWonDetailAdapter extends RecyclerView.Adapter<QuizWonDetailAdapter.ViewHolder> {

    List<SlabDetailResModel> mValues;
    Context mContext;
    ScholarshipQuizItemLayoutBinding binding;
    CommonCallBackListner listner;
    SlabModel slabModel;

    public QuizWonDetailAdapter(Context context, SlabModel values, CommonCallBackListner listner) {
        slabModel = values;
        mValues = values.getDetails();
        mContext = context;
        this.listner = listner;
    }

    public void updateList(ArrayList<SlabDetailResModel> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ScholarshipQuizItemLayoutBinding binding;

        public ViewHolder(ScholarshipQuizItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(SlabDetailResModel resModel, int position) {
            binding.subjectName.setText(resModel.getSubject());
            binding.quizName.setText(resModel.getQuizName());
            binding.amount.setText(resModel.getQuizName());
            binding.amount.setText(AuroApp.getAppContext().getResources().getString(R.string.rs) + slabModel.getPrice());

        }

    }

    @Override
    public QuizWonDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.scholarship_quiz_item_layout, viewGroup, false);
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

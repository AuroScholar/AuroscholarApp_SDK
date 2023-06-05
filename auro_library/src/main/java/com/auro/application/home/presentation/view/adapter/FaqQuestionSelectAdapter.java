package com.auro.application.home.presentation.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.databinding.FaqQuestionItemLayoutBinding;
import com.auro.application.home.data.FaqQuesData;

import java.util.List;

public class FaqQuestionSelectAdapter extends RecyclerView.Adapter<FaqQuestionSelectAdapter.QuestionHolder> {

    List<FaqQuesData> list;
    CommonCallBackListner commonCallBackListner;
   Context mcontext;

    private final int checkedPosition = 0;

    public FaqQuestionSelectAdapter(Context mContext, List<FaqQuesData> list) {
        this.list = list;
        this.mcontext = mContext;

    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        FaqQuestionItemLayoutBinding languageItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.faq_question_item_layout, viewGroup, false);
        return new QuestionHolder(languageItemLayoutBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {
        holder.bindUser(list.get(position), position, commonCallBackListner);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {
        FaqQuestionItemLayoutBinding binding;

        public QuestionHolder(@NonNull FaqQuestionItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bindUser(FaqQuesData model, int position, CommonCallBackListner commonCallBackListner) {
            binding.tvques.setText(list.get(position).getTransalatedQuestion());
            binding.tvans.setText(list.get(position).getTransalateAnswer());




            binding.imgopen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.tvans.setVisibility(View.VISIBLE);
                    binding.imgopen.setVisibility(View.GONE);
                    binding.imgclose.setVisibility(View.VISIBLE);
                    binding.tvques.setTextColor(Color.parseColor("#41BDFE"));
                    binding.tvans.setTextColor(Color.parseColor("#4D626F"));
                    binding.consParent.setBackgroundColor(Color.parseColor("#FAFDFF"));

                }
            });
            binding.imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.tvans.setVisibility(View.GONE);
                    binding.imgopen.setVisibility(View.VISIBLE);
                    binding.imgclose.setVisibility(View.GONE);
                    binding.tvques.setTextColor(Color.parseColor("#000000"));
                    binding.consParent.setBackgroundColor(Color.parseColor("#FFFFFF"));

                }
            });


        }
    }

    public void setData(List<FaqQuesData> list) {
        this.list = list;
        notifyDataSetChanged();
    }



}
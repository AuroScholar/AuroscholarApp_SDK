package com.auro.application.home.presentation.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.FaqQuesCategoryItemLayoutBinding;
import com.auro.application.home.data.FaqCatData;
import com.auro.application.util.AppUtil;

import java.util.List;

public class FaqCategorySelectAdapter extends RecyclerView.Adapter<FaqCategorySelectAdapter.CategoryHolder> {
    boolean isSeclected = false;
    List<FaqCatData> list;
    CommonCallBackListner commonCallBackListner;
   Context mcontext;

    private final int checkedPosition = 0;

    public FaqCategorySelectAdapter(Context mContext, List<FaqCatData> list,CommonCallBackListner commonCallBackListner) {
        this.list = list;
        this.mcontext = mContext;
        this.commonCallBackListner=commonCallBackListner;

    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        FaqQuesCategoryItemLayoutBinding languageItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.faq_ques_category_item_layout, viewGroup, false);
        return new CategoryHolder(languageItemLayoutBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.bindUser(list.get(position), position, commonCallBackListner);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        FaqQuesCategoryItemLayoutBinding binding;

        public CategoryHolder(@NonNull FaqQuesCategoryItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bindUser(FaqCatData model, int position, CommonCallBackListner commonCallBackListner) {
            binding.tvcategory.setText(list.get(position).getTranslateCategoryName());


            binding.consParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (isSeclected == true) {
                    if (binding.consParent.getBackground().getConstantState().equals(mcontext.getResources().getDrawable(R.drawable.bg_faq_ques_box).getConstantState())){
                        binding.consParent.setBackgroundResource(R.drawable.bg_faq_ques_activate_box);
                    binding.tvcategory.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.tvcategory.setTypeface(binding.tvcategory.getTypeface(), Typeface.BOLD);

                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.ITEM_CLICK, list.get(position)));

                } else if  (binding.consParent.getBackground().getConstantState().equals(mcontext.getResources().getDrawable(R.drawable.bg_faq_ques_activate_box).getConstantState())){

                    binding.consParent.setBackgroundResource(R.drawable.bg_faq_ques_box);
                    binding.tvcategory.setTextColor(Color.parseColor("#707070"));
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.ITEM_LONG_CLICK, list.get(position)));

                }


            }

            });
        }
    }
        public void setData(List<FaqCatData> list) {
            this.list = list;
            notifyDataSetChanged();
        }



}
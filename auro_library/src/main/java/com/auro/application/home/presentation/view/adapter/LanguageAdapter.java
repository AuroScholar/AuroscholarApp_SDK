package com.auro.application.home.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.FriendsBoardItemLayoutBinding;
import com.auro.application.databinding.FriendsInviteItemLayoutBinding;
import com.auro.application.databinding.LanguageItemLayoutBinding;
import com.auro.application.home.data.model.FriendsLeaderBoardModel;
import com.auro.application.home.data.model.SelectLanguageModel;
import com.auro.application.home.presentation.view.viewholder.LeaderBoardItemViewHolder;
import com.auro.application.teacher.data.model.SelectResponseModel;
import com.auro.application.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageHolder> {

    List<SelectLanguageModel> list;
    CommonCallBackListner commonCallBackListner;

    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private final int checkedPosition = 0;
    public LanguageAdapter(List<SelectLanguageModel> list, CommonCallBackListner commonCallBackListner) {
        this.list = list;
        this.commonCallBackListner = commonCallBackListner;
    }

    @NonNull
    @Override
    public LanguageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LanguageItemLayoutBinding languageItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.language_item_layout, viewGroup, false);
        return new LanguageHolder(languageItemLayoutBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull LanguageHolder holder, int position) {
        holder.bindUser(list.get(position), position, commonCallBackListner);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LanguageHolder extends RecyclerView.ViewHolder {
        LanguageItemLayoutBinding binding;

        public LanguageHolder(@NonNull LanguageItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bindUser(SelectLanguageModel model, int position, CommonCallBackListner commonCallBackListner) {
            binding.RpLanguage.setText(model.getLanguage());

            if (model.isCheck()) {
                binding.checkItem.setVisibility(View.VISIBLE);
                binding.checkItem.setImageDrawable(AuroApp.getAppContext().getResources().getDrawable(R.drawable.ic_auro_check));
            } else {
                //binding.checkItem.setImageDrawable(AuroApp.getAppContext().getResources().getDrawable(R.drawable.circle_auro_outline));
                binding.checkItem.setVisibility(View.GONE);
            }
            binding.clickItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.MESSAGE_SELECT_CLICK, model));
                    }
                }
            });


        }
    }

    public void setData(List<SelectLanguageModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


}

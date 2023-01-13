package com.auro.application.home.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.PartnersItemLayoutBinding;
import com.auro.application.home.data.model.partnersmodel.PartnerDataModel;
import com.auro.application.util.AppUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class PartnersAdapter extends RecyclerView.Adapter<PartnersAdapter.PartnersHolder> {

    List<PartnerDataModel> list;
    CommonCallBackListner commonCallBackListner;

    public PartnersAdapter(List<PartnerDataModel> list, CommonCallBackListner commonCallBackListner) {
        this.list = list;
        this.commonCallBackListner = commonCallBackListner;
    }

    @NonNull
    @Override
    public PartnersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        PartnersItemLayoutBinding languageItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.partners_item_layout, viewGroup, false);
        return new PartnersHolder(languageItemLayoutBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull PartnersHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonCallBackListner != null) {
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.PARTNERS_CLICK, list.get(position)));
                }
            }
        });
        holder.bindUser(list.get(position), position, commonCallBackListner);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PartnersHolder extends RecyclerView.ViewHolder {
        PartnersItemLayoutBinding binding;

        public PartnersHolder(@NonNull PartnersItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bindUser(PartnerDataModel model, int position, CommonCallBackListner commonCallBackListner) {
            binding.RPTextViewTitle.setText(model.getPartnerName());
            Glide.with(binding.itemImage.getContext()).load(model.getPartnerLogo())
                    .apply(RequestOptions.placeholderOf(R.drawable.image_place_holder)
                            .error(R.drawable.image_place_holder)
                            .centerCrop()
                            .dontAnimate()
                            .priority(Priority.IMMEDIATE)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(binding.itemImage);

        }
    }

    public void setData(List<PartnerDataModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


}
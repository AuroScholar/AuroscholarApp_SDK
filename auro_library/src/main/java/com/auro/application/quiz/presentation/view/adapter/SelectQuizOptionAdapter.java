package com.auro.application.quiz.presentation.view.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.SendQuizItemLayoutBinding;
import com.auro.application.quiz.data.model.submitQuestionModel.OptionResModel;
import com.auro.application.teacher.data.model.SelectResponseModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;

import java.util.List;

public class SelectQuizOptionAdapter extends RecyclerView.Adapter<SelectQuizOptionAdapter.ViewHolder> {

    List<OptionResModel> list;
    Context mContext;
    CommonCallBackListner commonCallBackListner;


    public SelectQuizOptionAdapter(Context context, List<OptionResModel> list, CommonCallBackListner commonCallBackListner) {
        mContext = context;
        this.list = list;
        this.commonCallBackListner = commonCallBackListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        SendQuizItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.send_quiz_item_layout, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // holder.setData(list.get(position), position);
        holder.bindUser(list.get(position), position, commonCallBackListner);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<OptionResModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        SendQuizItemLayoutBinding binding;


        public ViewHolder(SendQuizItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindUser(OptionResModel model, int position, CommonCallBackListner commonCallBackListner) {
            binding.msgText.setText(Html.fromHtml(model.getOption()));
            String url = "";
            if (model.isCheck()) {
                binding.checkIcon.setImageDrawable(AuroApp.getAppContext().getResources().getDrawable(R.drawable.ic_auro_check));
            } else {
                binding.checkIcon.setImageDrawable(AuroApp.getAppContext().getResources().getDrawable(R.drawable.circle_auro_outline));

            }
            binding.msgText.setVisibility(View.GONE);
            binding.optionImage.setVisibility(View.VISIBLE);
         /*   Uri myUri = Uri.parse("www.eklavvya.in/EklavvyaoptionImages/20211023095306Assett_Gr3_Ch17_Q.4_(d).jpg");
            ImageUtil.loaWithoutCropImage(binding.optionImage,"https://"+myUri.toString());*/

            AppLogger.e("loadNormalImage--",model.getOption());
            if (containsURL(model.getOption())) {
                binding.msgText.setVisibility(View.GONE);
                binding.optionImage.setVisibility(View.VISIBLE);
                ImageUtil.loaWithoutCropImage(binding.optionImage,model.getOption());
                AppLogger.e("loadNormalImage--",model.getOption());
            } else {
                binding.msgText.setVisibility(View.VISIBLE);
                binding.optionImage.setVisibility(View.GONE);
            }
            binding.llayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < list.size(); i++) {
                        if (i == position) {
                            list.get(i).setCheck(true);
                        } else {
                            list.get(i).setCheck(false);
                        }
                    }

                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.OPTION_SELECT_API, model));
                    }
                    notifyDataSetChanged();
                }
            });
            binding.optionImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.OPTION_IMAGE_CLICK, model.getOption()));
                    }
                }
            });

        }
    }



    private boolean containsURL(String content) {
        if(content.toLowerCase().contains("http://") || content.toLowerCase().contains("https://") || content.toLowerCase().contains("www.") )
        {
            return  true;
        }
        return  false;
    }
}

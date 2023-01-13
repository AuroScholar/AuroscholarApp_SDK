package com.auro.application.home.presentation.view.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.FragmentCertificateItemBinding;
import com.auro.application.databinding.ScholarshipItemLayoutBinding;
import com.auro.application.home.data.model.response.APIcertificate;
import com.auro.application.home.data.model.response.SlabModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;


public class QuizDetailInfoAdapter extends RecyclerView.Adapter<QuizDetailInfoAdapter.ViewHolder> {

    List<SlabModel> mValues;
    Context mContext;
    ScholarshipItemLayoutBinding binding;
    CommonCallBackListner listner;

    public QuizDetailInfoAdapter(Context context, List<SlabModel> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner = listner;
    }

    public void updateList(ArrayList<SlabModel> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ScholarshipItemLayoutBinding binding;

        public ViewHolder(ScholarshipItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(SlabModel resModel, int position) {
            AppLogger.e("GET_SLABS_API","QuizDetailInfoAdapter "+resModel.getDetails().size());
            binding.quizAmount.setText(AuroApp.getAppContext().getResources().getString(R.string.rs) + resModel.getPrice());
            binding.levelName.setText("Level " + resModel.getLevel());
            binding.totalQuizCount.setText(resModel.getDetails().size() + " Quiz Complete");
            if (resModel.getDetails().size() == 0) {
                binding.downArrowIcon.setVisibility(View.GONE);
            }
            setAdapter(resModel);
            expand(binding.wonQuizList, 1000, resModel.isQuizOpen());
            binding.downArrowIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callOnClick(position);
                }
            });
        }

    }

    @Override
    public QuizDetailInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.scholarship_item_layout, viewGroup, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position), position);

        Vholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOnClick(position);
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

    public void setAdapter(SlabModel resModel) {
        binding.wonQuizList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.wonQuizList.setHasFixedSize(true);
        binding.wonQuizList.setNestedScrollingEnabled(false);
        QuizWonDetailAdapter adapter = new QuizWonDetailAdapter(mContext, resModel, null);
        binding.wonQuizList.setAdapter(adapter);
    }

    private void callOnClick(int position) {
        int value = binding.wonQuizList.getVisibility();
        if (value == 0) {
            mValues.get(position).setQuizOpen(false);
            expand(binding.wonQuizList, 1000, false);
        } else {
            mValues.get(position).setQuizOpen(true);
            expand(binding.wonQuizList, 1000, true);
        }
        for (int i = 0; i < mValues.size(); i++) {
            if (position != i) {
                mValues.get(i).setQuizOpen(false);
            }
        }
        notifyDataSetChanged();
    }

    public void expand(final View v, int duration, boolean expand) {
        // final boolean expand = v.getVisibility() != View.VISIBLE;
        AppLogger.e("chhonker", "view visibility" + expand);
        if (expand) {
            binding.downArrowIcon.setRotation(180);
        } else {
            binding.downArrowIcon.setRotation(0);
        }

        int prevHeight = v.getHeight();
        int height = 0;
        if (expand) {
            int measureSpecParams = View.MeasureSpec.getSize(View.MeasureSpec.UNSPECIFIED);
            v.measure(measureSpecParams, measureSpecParams);
            height = v.getMeasuredHeight();
        }

        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, height);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (expand) {
                    v.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!expand) {
                    v.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

}

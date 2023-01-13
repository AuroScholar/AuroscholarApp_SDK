package com.auro.application.home.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.AddStudentStepItemLayoutBinding;
import com.auro.application.databinding.BottomSheetAddUserStepLayoutBinding;
import com.auro.application.databinding.StudentUserLayoutBinding;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.signupmodel.AddStudentStepDataModel;
import com.auro.application.util.AppUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StepsAddChildAdapter extends RecyclerView.Adapter<StepsAddChildAdapter.ClassHolder> {


    List<AddStudentStepDataModel> mValues;
    Context mContext;
    AddStudentStepItemLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;

    public StepsAddChildAdapter(Context mContext, List<AddStudentStepDataModel> mValues, CommonCallBackListner commonCallBackListner) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    public void setData(List<AddStudentStepDataModel> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ClassHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.add_student_step_item_layout, viewGroup, false);
        return new StepsAddChildAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassHolder Vholder, @SuppressLint("RecyclerView") int position) {
       // Vholder.setData(mValues, position);

        binding.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonCallBackListner != null) {
                    if (position==0 && !mValues.get(position).isStatus()) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.CLICK_BACK, mValues.get(position)));
                    }

                    if (position==1 && !mValues.get(position).isStatus() && mValues.get(0).isStatus()) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.CLICK_BACK, mValues.get(position)));
                    }
                }
            }
        });
        binding.mainLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonCallBackListner != null) {
                    if (position==0 && !mValues.get(position).isStatus()) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.CLICK_BACK, mValues.get(position)));
                    }

                    if (position==1 && !mValues.get(position).isStatus() && mValues.get(0).isStatus()) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.CLICK_OPENPROFILEBACK, mValues.get(position)));
                    }
                }
            }
        });


//        Vholder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (commonCallBackListner != null) {
//                    if (position==0 && !mValues.get(position).isStatus()) {
//                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.CLICK_BACK, mValues.get(position)));
//                    }
//
//                    if (position==1 && !mValues.get(position).isStatus() && mValues.get(0).isStatus()) {
//                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.CLICK_BACK, mValues.get(position)));
//                    }
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {

        AddStudentStepItemLayoutBinding binding;

        public ClassHolder(@NonNull @NotNull AddStudentStepItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(List<AddStudentStepDataModel> mValues, int position) {
            binding.tvStep.setText(AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails().getStep()+"" + (position + 1));
            binding.tvStepDesc.setText(mValues.get(position).getDescription());

            if (mValues.get(position).isStatus()) {
                binding.imgIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_done_icon));
            } else {
                binding.imgIcon.setImageDrawable(mContext.getDrawable(R.drawable.forward_vector));
                if (position > 0 && !mValues.get(position - 1).isStatus()) {
                    binding.imgIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.light_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
                    binding.tvStep.setTextColor(mContext.getResources().getColor(R.color.light_grey));
                    binding.tvStepDesc.setTextColor(mContext.getResources().getColor(R.color.light_grey));
                }else
                {
                    binding.tvStep.setTextColor(mContext.getResources().getColor(R.color.blue_color));
                    binding.tvStepDesc.setTextColor(mContext.getResources().getColor(R.color.black));
                }
            }
        }
    }
}

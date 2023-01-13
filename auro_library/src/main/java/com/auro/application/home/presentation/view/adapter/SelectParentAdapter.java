package com.auro.application.home.presentation.view.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.StudentUserLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.presentation.view.activity.EnterParentPinActivity;
import com.auro.application.home.presentation.view.activity.EnterPinActivity;
import com.auro.application.home.presentation.view.activity.ParentProfileActivity;
import com.auro.application.home.presentation.view.activity.ResetPasswordActivity;
import com.auro.application.home.presentation.view.activity.SetParentPinActivity;
import com.auro.application.home.presentation.view.activity.SetPinActivity;
import com.auro.application.util.AppUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SelectParentAdapter extends RecyclerView.Adapter<SelectParentAdapter.ClassHolder> {


    List<UserDetailResModel> mValues;
    Context mContext;
    StudentUserLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;

    public SelectParentAdapter(Context mContext, List<UserDetailResModel> mValues, CommonCallBackListner commonCallBackListner) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    @NonNull
    @Override
    public ClassHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.student_user_layout, viewGroup, false);
        return new SelectParentAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassHolder Vholder, @SuppressLint("RecyclerView") int position) {
        Vholder.setData(mValues, position);
        Vholder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mValues.get(position).getPin().isEmpty() || mValues.get(position).getPin().equals("")){
                    Intent i = new Intent(mContext, SetParentPinActivity.class);
                    i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
                    i.putExtra("parentusername", mValues.get(position).getUserName());
                    i.putExtra("parentuserid", mValues.get(position).getUserId());
                    mContext.startActivity(i);
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                    editor.putString("usertype", "ParentLogin");
                    editor.putString("parentuserid", mValues.get(0).getUserId());
                    editor.putString("parentusername", mValues.get(0).getUserName());
                    editor.commit();
                }
                else{
                    String userid = mValues.get(0).getUserId();
                    String username = mValues.get(0).getUserName();
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                    editor.putString("usertype", "ParentLogin");
                    editor.putString("parentuserid", userid);
                    editor.putString("parentusername", username);
                    editor.commit();

                    Intent i = new Intent(mContext, EnterParentPinActivity.class);
                    i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
                    i.putExtra("parentusername", mValues.get(position).getUserName());
                    i.putExtra("parentuserid", mValues.get(position).getUserId());
                    mContext.startActivity(i);
                }
            }
        });
//        if(!AuroAppPref.INSTANCE.getModelInstance().isLogin()) {
//            Vholder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (commonCallBackListner != null) {
//                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.CLICK_PARENTPROFILE, mValues.get(position)));
//                    }
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {

        StudentUserLayoutBinding binding;
        PrefModel prefModel;
        Details details;

        public ClassHolder(@NonNull @NotNull StudentUserLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            prefModel = AuroAppPref.INSTANCE.getModelInstance();
            details = prefModel.getLanguageMasterDynamic().getDetails();
        }

        public void setData(List<UserDetailResModel> mValues, int position) {
//            if (mValues.size() - 1 == position) {
//                binding.cardView.setVisibility(View.GONE);
//                binding.cardViewButton.setVisibility(View.VISIBLE);
//            } else {


            String userName = details.getUsername()!=null ? details.getUsername() : AuroApp.getAppContext().getResources().getString(R.string.username);
            String name = details.getName()!=null ? details.getName() : AuroApp.getAppContext().getResources().getString(R.string.name);;
            String grade = details.getGradeStudent()!=null ? details.getGradeStudent() : AuroApp.getAppContext().getResources().getString(R.string.grade_student_new);;
                binding.cardView.setVisibility(View.VISIBLE);
            binding.txtchangepin.setVisibility(View.GONE);
                binding.cardViewButton.setVisibility(View.GONE);
                binding.tvStudentUserName.setText(userName+": " + mValues.get(position).getUserName());
                binding.tvStudentName.setText(name+": " + mValues.get(position).getStudentName());
            if (mValues.get(position).getProfilepic().equals("")||mValues.get(position).getProfilepic().equals("null")||mValues.get(position).getProfilepic().isEmpty()){
                Glide.with(mContext)
                        .load(mContext.getResources().getIdentifier("my_drawable_image_name", "drawable",mContext.getPackageName()))
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.account_circle)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL))
                        .into(binding.studentImage);
            }
            else {


                  Glide.with(mContext).load(mValues.get(position).getProfilepic())
                          .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))
                          .circleCrop()).into(binding.studentImage);
            }

                binding.tvStudentGrade.setVisibility(View.GONE);
                binding.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mValues.get(position).getPin().isEmpty() || mValues.get(position).getPin().equals("")){
                            Intent i = new Intent(mContext, SetPinActivity.class);
                        i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
                        mContext.startActivity(i);
                        }
                        else{
                            Intent i = new Intent(mContext, EnterParentPinActivity.class);
                            i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
                            mContext.startActivity(i);
                        }



//                        Intent i = new Intent(mContext, ParentProfileActivity.class);
//                        i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
//                        mContext.startActivity(i);
                    }
                });
             //   binding.tvStudentGrade.setText(grade+": " + mValues.get(position).getGrade());
          //  }

        }
    }
}

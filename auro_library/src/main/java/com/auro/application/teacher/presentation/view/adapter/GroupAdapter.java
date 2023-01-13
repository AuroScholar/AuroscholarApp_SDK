package com.auro.application.teacher.presentation.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.HorizantalImageItemLayoutBinding;
import com.auro.application.teacher.data.model.response.TeacherGroupRes;
import com.auro.application.teacher.data.model.response.UserImageInGroupResModel;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.textDrawable.TextDrawable;
//import com.facebook.internal.AppCall;



import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ClassHolder> {

    List<TeacherGroupRes> mValues;
    Context mContext;
    HorizantalImageItemLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;

    public GroupAdapter(Context mContext, List<TeacherGroupRes> mValues, CommonCallBackListner commonCallBackListner) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    @NotNull
    @Override
    public GroupAdapter.ClassHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        // return null;
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.horizantal_image_item_layout, viewGroup, false);
        return new GroupAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GroupAdapter.ClassHolder Vholder, int position) {
        Vholder.setData(mValues, position);
        Vholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonCallBackListner != null) {
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.GROUP_CLICK_CALLBACK, mValues.get(position)));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {


        HorizantalImageItemLayoutBinding binding;

        public ClassHolder(@NonNull @NotNull HorizantalImageItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(List<TeacherGroupRes> mValues, int position) {
            TeacherGroupRes teacherGroupRes = mValues.get(position);
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            TextDrawable drawable = TextDrawable.builder()
                    .buildRect("" + teacherGroupRes.getGroupName().charAt(0), color);
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(AuroApp.getAppContext().getResources(), drawableToBitmap(drawable));
            circularBitmapDrawable.setCircular(true);
            binding.groupName.setText(teacherGroupRes.getGroupName());
            binding.groupImage.setImageDrawable(circularBitmapDrawable);
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }


        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 96; // Replaced the 1 by a 96
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 96; // Replaced the 1 by a 96

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}

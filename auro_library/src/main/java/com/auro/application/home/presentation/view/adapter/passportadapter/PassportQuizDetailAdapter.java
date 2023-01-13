package com.auro.application.home.presentation.view.adapter.passportadapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.PassportQuizItemLayoutBinding;
import com.auro.application.databinding.PassportQuizLayoutBinding;
import com.auro.application.home.data.model.passportmodels.PassportQuizDetailModel;
import com.auro.application.home.data.model.passportmodels.PassportQuizGridModel;
import com.auro.application.home.data.model.passportmodels.PassportQuizModel;
import com.auro.application.home.data.model.response.APIcertificate;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;

import java.util.ArrayList;
import java.util.List;


public class PassportQuizDetailAdapter extends RecyclerView.Adapter<PassportQuizDetailAdapter.ViewHolder> {

    List<PassportQuizGridModel> mValues;
    Context mContext;
    PassportQuizItemLayoutBinding binding;
    CommonCallBackListner listner;

    public PassportQuizDetailAdapter(Context context, List<PassportQuizGridModel> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner = listner;
    }

    public void updateList(ArrayList<PassportQuizGridModel> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        PassportQuizItemLayoutBinding binding;

        public ViewHolder(PassportQuizItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(APIcertificate resModel, int position) {

        }

    }

    @Override
    public PassportQuizDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.passport_quiz_item_layout, viewGroup, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ViewHolder Vholder, @SuppressLint("RecyclerView") int position) {
        //AppLogger.e("TransactionsFragment","Quiz detail Adpater size-"+mValues.size());
       // AppLogger.e("TransactionsFragment","Quiz detail Adpater size-"+mValues.get(position).getQuizData());
        //   Vholder.setData(mValues.get(position), position);
        binding.quizTitle.setText(mValues.get(position).getQuizHead());
        binding.quizData.setText(mValues.get(position).getQuizData());
        binding.quizData.setTextColor(mValues.get(position).getQuizColor());
        binding.quizIcon.setImageResource(mValues.get(position).getQuizImagePath());
     int allsize = mValues.size();
        SharedPreferences.Editor editor = mContext.getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor.putInt("allsize",allsize);
        editor.apply();
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
        SharedPreferences prefs = mContext.getSharedPreferences("My_Pref", MODE_PRIVATE);
        int allsize = prefs.getInt("allsize", 0);
        Log.d("allsize", String.valueOf(allsize));
//        if (mValues.size() == 0){
            return mValues.size();
//        }
//        else{
//            return mValues.size();
//        }


    }

    private void updateData() {

    }

}

package com.auro.application.home.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.databinding.FragmentKycInfoItemLayoutBinding;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.teacher.presentation.view.adapter.StudentDoumentViewHolder;
import com.auro.application.teacher.presentation.viewmodel.TeacherDoumentViewHolder;

import java.util.ArrayList;
import java.util.List;

public class StudentKycDocumentAdapter extends RecyclerView.Adapter {


    List<KYCDocumentDatamodel> list;
    CommonCallBackListner commonCallBackListner;

    public StudentKycDocumentAdapter(List<KYCDocumentDatamodel> list, CommonCallBackListner commonCallBackListner) {
        this.list = list;
        this.commonCallBackListner = commonCallBackListner;
    }
    public void updateList(ArrayList<KYCDocumentDatamodel> values) {
        this.list = values;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case AppConstant.FriendsLeaderBoard.STUDENT_DOC_ADAPTER:
                FragmentKycInfoItemLayoutBinding teacherDocumentItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.fragment_kyc_info_item_layout, viewGroup, false);
                return new StudentDoumentViewHolder(teacherDocumentItemLayoutBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = list.get(position).getViewType();

        switch (viewType) {
            case AppConstant.FriendsLeaderBoard.STUDENT_DOC_ADAPTER:
                ((StudentDoumentViewHolder) holder).bindUser(list.get(position), position,commonCallBackListner);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (list.get(position).getViewType()) {
            case AppConstant.FriendsLeaderBoard.STUDENT_DOC_ADAPTER:
                return AppConstant.FriendsLeaderBoard.STUDENT_DOC_ADAPTER;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

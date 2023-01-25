package com.auro.application.home.presentation.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.BottomStudentListBinding;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.ParentProfileActivity;
import com.auro.application.home.presentation.view.adapter.SelectYourChildAdapter;

import com.auroscholar.final_auroscholarapp_sdk.SDKChildModel;
import com.auroscholar.final_auroscholarapp_sdk.SDKDataModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetUsersDialog extends BottomSheetDialogFragment implements CommonCallBackListner {
    BottomStudentListBinding binding;
    SDKDataModel checkUserResModel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_student_list, container, false);
        checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getChildData();
        setAdapterAllListStudent(checkUserResModel.getUser_details());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setAdapterAllListStudent(List<SDKChildModel> totalStudentList) {
        List<SDKChildModel> list = new ArrayList<>();
        for (SDKChildModel resmodel : totalStudentList) {
                list.add(resmodel);
        }
        binding.studentList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
       binding.studentList.setHasFixedSize(true);
       SelectYourChildAdapter studentListAdapter = new SelectYourChildAdapter(getActivity(), list, this);
       binding.studentList.setAdapter(studentListAdapter);
       if(list.size()<5){
           binding.btnaddstudent.setVisibility(View.VISIBLE);
       }
       else{
           binding.btnaddstudent.setVisibility(View.GONE);
       }
        binding.btnaddstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetAddUserDialog bottomSheet = new BottomSheetAddUserDialog();
                bottomSheet.show(getActivity().getSupportFragmentManager(),
                        "ModalBottomSheet");

            }
        });
    }
    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case CLICK_PARENTPROFILE:
                Intent i = new Intent(getActivity(), ParentProfileActivity.class);
                i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
              startActivity(i);
                break;

        case CLICK_CHILDPROFILE:
        Intent i1 = new Intent(getActivity(), DashBoardMainActivity.class);
        i1.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
        startActivity(i1);
        break;
    }

    }





}


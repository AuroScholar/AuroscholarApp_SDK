package com.auro.application.home.presentation.view.fragment;


import static com.auro.application.core.common.Status.CLICK_CHILDPROFILE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.BottomStudentListBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.EnterPinActivity;
import com.auro.application.home.presentation.view.activity.ForgotPinActivity;
import com.auro.application.home.presentation.view.activity.ParentProfileActivity;
import com.auro.application.home.presentation.view.activity.SetPinActivity;
import com.auro.application.home.presentation.view.adapter.SelectParentAdapter;
import com.auro.application.home.presentation.view.adapter.SelectYourChildAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetUsersDialog extends BottomSheetDialogFragment implements CommonCallBackListner {
    BottomStudentListBinding binding;
    CheckUserResModel checkUserResModel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_student_list, container, false);
        checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel();
        setAdapterAllListStudent(checkUserResModel.getUserDetails());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }

    public void setAdapterAllListStudent(List<UserDetailResModel> totalStudentList) {
        List<UserDetailResModel> list = new ArrayList<>();
        List<UserDetailResModel> plist = new ArrayList<>();
        for (UserDetailResModel resmodel : totalStudentList) {
            if (resmodel.getIsMaster().equalsIgnoreCase("0")) {
                list.add(resmodel);
            }

        }
        for (UserDetailResModel resmodel : totalStudentList) {
            if (resmodel.getIsMaster().equalsIgnoreCase("1")) {
                plist.add(resmodel);
            }

        }
/*
        if (AuroAppPref.INSTANCE.getModelInstance().isLogin()) {
            UserDetailResModel model = new UserDetailResModel();
            model.setStudentName("Add Student");
            list.add(model);
        }*/


       binding.studentList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
       binding.studentList.setHasFixedSize(true);
       SelectYourChildAdapter studentListAdapter = new SelectYourChildAdapter(getActivity(), list, this);
       binding.studentList.setAdapter(studentListAdapter);


       binding.parentList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
       binding.parentList.setHasFixedSize(true);
       SelectParentAdapter studentListAdapter2 = new SelectParentAdapter(getActivity(), plist, this);
       binding.parentList.setAdapter(studentListAdapter2);

    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case CLICK_PARENTPROFILE:
                Intent i = new Intent(getActivity(), ParentProfileActivity.class);
                i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
              startActivity(i);
//                UserDetailResModel resModel = (UserDetailResModel) commonDataModel.getObject();
//                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
//                prefModel.setStudentData(resModel);
//                AuroAppPref.INSTANCE.setPref(prefModel);
//                openSetPinActivity((UserDetailResModel) commonDataModel.getObject());
                break;

        case CLICK_CHILDPROFILE:
        Intent i1 = new Intent(getActivity(), DashBoardMainActivity.class);
        i1.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
        startActivity(i1);
//                UserDetailResModel resModel = (UserDetailResModel) commonDataModel.getObject();
//                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
//                prefModel.setStudentData(resModel);
//                AuroAppPref.INSTANCE.setPref(prefModel);
//                openSetPinActivity((UserDetailResModel) commonDataModel.getObject());
        break;
    }

    }


    private void openSetPinActivity(UserDetailResModel resModel) {
        if (!resModel.isUsername() && !resModel.getSetPin()) {
            Intent intent = new Intent(getActivity(), SetPinActivity.class);
            intent.putExtra(AppConstant.COMING_FROM, AppConstant.ComingFromStatus.COMING_FROM_BOTTOM_SHEET);
            intent.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, resModel);
            startActivity(intent);
            dismiss();
        } if (!resModel.getSetPin()) {
            Intent intent = new Intent(getActivity(), ForgotPinActivity.class);
            intent.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, resModel);
            startActivity(intent);
            dismiss();
        }
        else {
            Intent intent = new Intent(getActivity(), EnterPinActivity.class);
            intent.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, resModel);
            startActivity(intent);
            dismiss();
        }

    }


}


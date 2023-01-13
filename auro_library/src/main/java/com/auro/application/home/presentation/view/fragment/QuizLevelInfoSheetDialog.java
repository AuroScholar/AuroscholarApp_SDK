package com.auro.application.home.presentation.view.fragment;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.ScholarshipInfoBottomsheetBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.response.SlabModel;
import com.auro.application.home.data.model.response.SlabsResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.presentation.view.activity.SetPinActivity;
import com.auro.application.home.presentation.view.adapter.LevelInfoAdapter;
import com.auro.application.home.presentation.view.adapter.QuizDetailInfoAdapter;
import com.auro.application.home.presentation.view.adapter.QuizLevelAdapter;
import com.auro.application.home.presentation.view.adapter.StepsAddChildAdapter;
import com.auro.application.util.AppLogger;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.reflect.Field;
import java.util.List;

public class QuizLevelInfoSheetDialog extends BottomSheetDialogFragment implements CommonCallBackListner {
    ScholarshipInfoBottomsheetBinding binding;
    SlabsResModel slabsResModel;
    StepsAddChildAdapter studentListAdapter;
    List<SlabModel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {

            binding = DataBindingUtil.inflate(inflater, R.layout.scholarship_info_bottomsheet, container, false);
        if(getArguments()!=null) {
            slabsResModel = getArguments().getParcelable(AppConstant.QUIZ_RES_MODEL);
        }
        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
        Details details = model.getDetails();
        binding.txtdetail.setText(details.getScholarship());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);



        setAdapter();

    }


    public void setAdapter() {

        AppLogger.e("GET_SLABS_API","QuizLevelInfoSheetDialog - "+slabsResModel.getSlabs());
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setNestedScrollingEnabled(false);
        QuizDetailInfoAdapter adapter = new QuizDetailInfoAdapter(getActivity(), slabsResModel.getSlabs(), this);
        binding.recyclerview.setAdapter(adapter);

        //setListner();
    }

    private void setListner()
    {
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) binding.getRoot());
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState > BottomSheetBehavior.STATE_DRAGGING)
                    bottomSheet.post(new Runnable() {
                        @Override public void run() {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    });
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case CLICK_BACK:

                break;
        }

    }

    private void openSetPinActivity(UserDetailResModel resModel, String type) {
        Intent intent = new Intent(getActivity(), SetPinActivity.class);
        intent.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, resModel);
        intent.putExtra(AppConstant.COMING_FROM, type);
        startActivity(intent);
    }




    @Override
    public void onResume() {
        super.onResume();
    }


}


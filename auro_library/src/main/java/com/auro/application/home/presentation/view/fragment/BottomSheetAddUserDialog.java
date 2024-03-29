package com.auro.application.home.presentation.view.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.BottomSheetAddUserStepLayoutBinding;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.ParentProfileDataModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.signupmodel.AddStudentStepDataModel;
import com.auro.application.home.presentation.view.activity.AppLanguageActivity;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithPinActivity;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithoutPin;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.SDKActivity;
import com.auro.application.home.presentation.view.activity.SetPinActivity;
import com.auro.application.home.presentation.view.adapter.StepsAddChildAdapter;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.strings.AppStringDynamic;
import com.auroscholar.final_auroscholarapp_sdk.SDKDataModel;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetAddUserDialog extends BottomSheetDialogFragment implements CommonCallBackListner {
    BottomSheetAddUserStepLayoutBinding binding;
    CheckUserResModel checkUserResModel;
    StepsAddChildAdapter studentListAdapter;
    List<AddStudentStepDataModel> list;
    String auto_userid;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_add_user_step_layout, container, false);
        AppStringDynamic.setBottomSheetAddUserStrings(binding);
        checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel();
        setAdapterAllListStudent(checkUserResModel.getUserDetails());
        if (AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel().getUserDetails().size()>2){
            binding.mainLayout.setVisibility(View.GONE);
        }
        binding.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailResModel resModel = AuroAppPref.INSTANCE.getModelInstance().getStudentData();
                UserDetailResModel userDetailResModel = AuroAppPref.INSTANCE.getModelInstance().getStudentData();
                openSetPinActivity(resModel, AppConstant.ComingFromStatus.COMING_FROM_ADD_STUDENT_STEP_2);
            }
        });
        binding.mainLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAutoRegister();

            }
        });
        return binding.getRoot();
    }

    private void setAutoRegister()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String mobileno = prefModel.getUserMobile();
        String partnersource = prefModel.getPartnersource();
        String partneruniqueid = prefModel.getPartneruniqueid();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("mobile_no",mobileno);
        map_data.put("partner_unique_id",partneruniqueid);
        map_data.put("partner_source",partnersource);
        map_data.put("add_new","1");
        map_data.put("partner_api_key","7611f0fafb1e3b96d1a78c57b0650b85985eace9f6aaa365c0b496e9ae1163e7");
        RemoteApi.Companion.invoke().getSDKData(map_data)
                .enqueue(new Callback<SDKDataModel>() {
                    @Override
                    public void onResponse(Call<SDKDataModel> call, Response<SDKDataModel> response)
                    {
                        try {
                            if (response.code() == 400) {
                                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            }
                            else if (response.code() == 200) {


                                for (int i = 0; i<response.body().getUser_details().size(); i++){
                                    auto_userid = response.body().getUser_details().get(0).getUser_id();

                                }
                                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();

                                prefModel.setUserId(auto_userid);
                                AuroAppPref.INSTANCE.setPref(prefModel);
                                Intent i1 = new Intent(getActivity(), AppLanguageActivity.class);
                                i1.putExtra("auto_userid",auto_userid);
                                startActivity(i1);




                            }
                            else {
                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(getActivity(), "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SDKDataModel> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);

    }

    public void setAdapterAllListStudent(List<UserDetailResModel> totalStudentList) {
        list = new ArrayList<>();
        AddStudentStepDataModel model = new AddStudentStepDataModel();
        model.setDescription(AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails().getSet_your_pin()+"");
        model.setStatus(false);
        list.add(model);

        AddStudentStepDataModel model2 = new AddStudentStepDataModel();
        model2.setDescription(AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails().getSet_your_pin()+"");
        model2.setStatus(false);
        list.add(model2);
        UserDetailResModel userDetailResModel = AuroAppPref.INSTANCE.getModelInstance().getParentData();
        String parentusername = AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel().getUserDetails().get(0).getUserName();
     //   getProfile(parentusername);
        binding.studentList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.studentList.setHasFixedSize(true);
//        studentListAdapter = new StepsAddChildAdapter(getActivity(),list, this);
//        binding.studentList.setAdapter(studentListAdapter);

    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case CLICK_BACK:
                AddStudentStepDataModel model = (AddStudentStepDataModel) commonDataModel.getObject();
                UserDetailResModel resModel = AuroAppPref.INSTANCE.getModelInstance().getStudentData();
                if (commonDataModel.getSource() == 1) {
                    openSetPinActivity(resModel, AppConstant.ComingFromStatus.COMING_FROM_ADD_STUDENT_STEP_2);
                } else {
                    openSetPinActivity(resModel, AppConstant.ComingFromStatus.COMING_FROM_ADD_STUDENT_STEP_1);
                }
                break;
            case CLICK_OPENPROFILEBACK:
                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                Bundle bundle = new Bundle();
                StudentProfileFragment studentProfile = new StudentProfileFragment();
                bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, prefModel.getDashboardResModel());
                bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.STUDENT_PROFILE);
                studentProfile.setArguments(bundle);
                openFragment(studentProfile);
                break;
        }

    }
    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(getActivity(), fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
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
        UserDetailResModel userDetailResModel = AuroAppPref.INSTANCE.getModelInstance().getParentData();
        String parentusername = AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel().getUserDetails().get(0).getUserName();
      //  getProfile(parentusername);
//        UserDetailResModel userDetailResModel = AuroAppPref.INSTANCE.getModelInstance().getStudentData();
//        if (userDetailResModel.isUsername()) {
//            list.get(0).setStatus(true);
//        }
//        studentListAdapter.setData(list);
//        AppLogger.e("BottomSheetAddUserDialog", "BottomSheetAddUserDialog onresume caliing..");
    }


    private void getProfile(String user_name)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_name",user_name);
        map_data.put("user_type", String.valueOf(0));
        map_data.put("user_prefered_language_id",languageid);
        RemoteApi.Companion.invoke().getUserCheck(map_data)
                .enqueue(new Callback<CheckUserResModel>()
                {
                    @Override
                    public void onResponse(Call<CheckUserResModel> call, Response<CheckUserResModel> response)
                    {
                        if (response.isSuccessful())
                        {
                                    if (response.body().getUserDetails().size()<=2 && (response.body().getUserDetails().get(1).getPin().isEmpty()||response.body().getUserDetails().get(1).getPin().equals(""))){
                                        binding.mainLayout.setVisibility(View.VISIBLE);
                                        binding.mainLayout2.setVisibility(View.GONE);
                                    }
                                    else{
                                        binding.mainLayout.setVisibility(View.GONE);
                                        binding.mainLayout2.setVisibility(View.VISIBLE);
                                    }








                        }
                        else
                        {
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserResModel> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


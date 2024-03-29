package com.auro.application.home.presentation.view.fragment;


import static android.content.Context.MODE_PRIVATE;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.auro.application.home.presentation.view.activity.AppLanguageActivity;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithoutPin;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.ParentProfileActivity;
import com.auro.application.home.presentation.view.activity.SDKActivity;
import com.auro.application.home.presentation.view.adapter.SelectYourChildAdapter;

import com.auro.application.util.RemoteApi;
import com.auroscholar.final_auroscholarapp_sdk.SDKChildModel;
import com.auroscholar.final_auroscholarapp_sdk.SDKDataModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetUsersDialog extends BottomSheetDialogFragment implements CommonCallBackListner {
    BottomStudentListBinding binding;
    SDKDataModel checkUserResModel;
    String auto_userid;
    static List<SDKChildModel> userDetails = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_student_list, container, false);
       binding.imgclose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dismiss();
               PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
               prefModel.setLogin(false);
                AuroAppPref.INSTANCE.clearPref();
               SharedPreferences preferences =getActivity().getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = preferences.edit();
               editor.clear();
               editor.apply();
           }
       });
        SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        String statustoclose = prefs.getString("statustoclose","");
//        if (statustoclose.equals("true")||statustoclose== "true"){
//
//
//        }
//        else {
            checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getChildData();
            setAdapterAllListStudent(checkUserResModel.getUser_details());
     //   }
            binding.btnaddstudent.setOnClickListener(new View.OnClickListener() {
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
        String apikey = prefModel.getApikey();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("mobile_no",mobileno);
        map_data.put("partner_unique_id",partneruniqueid);
        map_data.put("partner_source",partnersource);
        map_data.put("add_new","1");
        map_data.put("partner_api_key",apikey);
        RemoteApi.Companion.invoke().getSDKData(map_data)
                .enqueue(new Callback<SDKDataModel>() {
                    @Override
                    public void onResponse(Call<SDKDataModel> call, Response<SDKDataModel> response)
                    {
                        try {
                            if (response.code() == 400) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }


                            }
                            else if (response.code() == 200) {
                                userDetails.clear();
                                userDetails = response.body().getUser_details();
                                checkUserResModel = (SDKDataModel) response.body();
                                for (int i = 0; i<response.body().getUser_details().size(); i++){
                                    auto_userid = response.body().getUser_details().get(0).getUser_id();

                                }
                                String userid_child =  userDetails.get(0).getUser_id();
                                String user_mobile =  userDetails.get(0).getMobile_no();
                                String student_name =  userDetails.get(0).getStudent_name();
                                String user_language =  userDetails.get(0).getUser_prefered_language_id();
                                int user_grade =  userDetails.get(0).getGrade();
                                String user_kyc =  userDetails.get(0).getKyc_status();
                                String user_name = userDetails.get(0).getUser_name();
                                String partner_logo =  userDetails.get(0).getPartner_logo();
                                String profile_pic = userDetails.get(0).getProfile_pic();
                                PrefModel prefModel2 = AuroAppPref.INSTANCE.getModelInstance();

                                prefModel2.setChildData(checkUserResModel);
                                prefModel2.setPartnersource(partnersource);
                                prefModel2.setUserMobile(mobileno);
                                prefModel2.setPartneruniqueid(partneruniqueid);
                                prefModel2.setApikey(apikey);


                                prefModel2.setUserId(auto_userid);
                                prefModel2.setUserMobile(user_mobile);
                                prefModel2.setUserLanguageId(user_language);
                                prefModel2.setStudentName(student_name);
                                prefModel2.setUserclass(user_grade);
                                prefModel2.setKycstatus(user_kyc);
                                prefModel2.setUserprofilepic(profile_pic);
                                prefModel2.setUserName(user_name);
                                prefModel2.setPartner_logo(partner_logo);
                                AuroAppPref.INSTANCE.setPref(prefModel2);


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


    public void show(FragmentManager fragmentManager, String modalBottomSheet) {
    }
}


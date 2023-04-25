package com.auro.application.home.presentation.view.adapter;

import static android.content.Context.MODE_PRIVATE;

import static com.auro.application.core.application.AuroApp.auroScholarDataModel;
import static com.auro.application.core.application.AuroApp.context;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.network.ErrorResponseModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.databinding.StudentUserLayoutBinding;


import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.presentation.view.activity.AppLanguageActivity;
import com.auro.application.home.presentation.view.activity.ChooseGradeActivity;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithoutPin;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.EnterPinActivity;
import com.auro.application.home.presentation.view.activity.SDKActivity;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.RemoteApi;


import com.auroscholar.final_auroscholarapp_sdk.SDKChildModel;
import com.auroscholar.final_auroscholarapp_sdk.SDKDataModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectYourChildAdapter extends RecyclerView.Adapter<SelectYourChildAdapter.ClassHolder> {



    List<SDKChildModel> mValues;
    Context mContext;
    SDKChildModel checkUserResModel;
    SDKDataModel checkUserResModel2;
    StudentUserLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;
    String comingFromText = "";
    String errormismatch="";
    AuroScholarInputModel auroScholarInputModel;


    public SelectYourChildAdapter(Context mContext, List<SDKChildModel> mValues, CommonCallBackListner commonCallBackListner) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    @NonNull
    @Override
    public ClassHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.student_user_layout, viewGroup, false);
        return new SelectYourChildAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassHolder Vholder, @SuppressLint("RecyclerView") int position) {
        Vholder.setData(mValues, position);
        Vholder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = mValues.get(position).getUser_id();
                int gradeid = mValues.get(position).getGrade();
                SharedPreferences.Editor editor = mContext.getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                editor.putString("usertype", "StudentLogin");
                editor.putString("studentuserid", userid);
                editor.putString("studentgradeid", String.valueOf(gradeid));
                editor.apply();
                checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getChildData().getUser_details().get(position);
                checkUserResModel2 = AuroAppPref.INSTANCE.getModelInstance().getChildData();
                String userid_child =  mValues.get(position).getUser_id();
                    String user_mobile =  mValues.get(position).getMobile_no();
                    String student_name =  mValues.get(position).getStudent_name();
                    String user_language =  mValues.get(position).getUser_prefered_language_id();
                    int user_grade =  mValues.get(position).getGrade();
                    String user_kyc =  mValues.get(position).getKyc_status();
                String user_name =  mValues.get(position).getUser_name();
                String partner_logo =  mValues.get(position).getPartner_logo();
                String profile_pic =  mValues.get(position).getProfile_pic();
                    SDKChildModel resModel = AuroAppPref.INSTANCE.getModelInstance().getChildData().getUser_details().get(position);
                    setDatainPref(resModel);
                    PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                    prefModel.setChildData(checkUserResModel2);
                    prefModel.setUserId(userid_child);
                    prefModel.setUserMobile(user_mobile);
                    prefModel.setUserLanguageId(user_language);
                    prefModel.setStudentName(student_name);
                    prefModel.setUserclass(user_grade);
                    prefModel.setKycstatus(user_kyc);
                prefModel.setUserprofilepic(profile_pic);
                prefModel.setUserName(user_name);
                prefModel.setPartner_logo(partner_logo);
                AuroAppPref.INSTANCE.setPref(prefModel);
                setSDKAPI(userid_child,user_language);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {

        StudentUserLayoutBinding binding;
        PrefModel prefModel;
        //  Details details;

        public ClassHolder(@NonNull @NotNull StudentUserLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            prefModel = AuroAppPref.INSTANCE.getModelInstance();
            // details = prefModel.getLanguageMasterDynamic().getDetails();
        }

        public void setData(List<SDKChildModel> mValues, int position) {
            if (mValues.get(position).getProfile_pic().isEmpty()){
                Glide.with(mContext).load(R.drawable.ic_profile)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))
                                .circleCrop()).into(binding.studentImage);
            }
            else{
                Glide.with(mContext).load(mValues.get(position).getProfile_pic())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))
                                .circleCrop()).into(binding.studentImage);
            }
            binding.txtchangepin.setVisibility(View.GONE);
            binding.cardView.setVisibility(View.VISIBLE);
            binding.cardViewButton.setVisibility(View.GONE);
            binding.tvStudentUserName.setText("Username: " + mValues.get(position).getUser_name());
            binding.tvStudentName.setText("Name: " + mValues.get(position).getStudent_name());
            binding.tvStudentGrade.setText("Grade: " + mValues.get(position).getGrade());
            //  }
        }
    }

    void checkUserForOldStudentUser(SDKDataModel checkUserResModel, int position) {
        if ((checkUserResModel != null)  && checkUserResModel.getUser_details().size() == 1){
            SDKChildModel resModel = checkUserResModel.getUser_details().get(position);
            setDatainPref(resModel);
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setChildData(checkUserResModel);
            AuroAppPref.INSTANCE.setPref(prefModel);
            Intent i = new Intent(mContext, DashBoardMainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(i);


        }


    }



    private void setDatainPref(SDKChildModel resModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setChildrenData(resModel);
        AuroAppPref.INSTANCE.setPref(prefModel);

    }
    private void setSDKAPI(String userid, String langid)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String mobile = prefModel.getUserMobile();
        String partneruniueid = prefModel.getPartneruniqueid();
        String partnersource = prefModel.getPartnersource();
        String apikey = prefModel.getApikey();
            HashMap<String,String> map_data = new HashMap<>();
            map_data.put("mobile_no",mobile);
            map_data.put("partner_unique_id",partneruniueid); //456456
            map_data.put("partner_source",partnersource);
            map_data.put("partner_api_key",apikey);
        map_data.put("user_id",userid);

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
                                        Toast.makeText(mContext,message, Toast.LENGTH_SHORT).show();

                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                                else if (response.code() == 200) {
                                    setSDKAPIGrade();
                                    getProfile(userid,langid);
                                }
                                else {
                                    Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (Exception e) {
                                Toast.makeText(mContext, "Internet connection", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SDKDataModel> call, Throwable t) {
                            Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });

    }
    private void getProfile(String userid, String lang_id)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String partnersource = prefModel.getPartnersource();
        String parnteruniqueid = prefModel.getPartneruniqueid();
        String apikey = prefModel.getApikey();
        int userclass = prefModel.getUserclass();
        String mobileno = prefModel.getUserMobile();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);

        RemoteApi.Companion.invoke().getStudentData(map_data)
                .enqueue(new Callback<GetStudentUpdateProfile>()
                {
                    @Override
                    public void onResponse(Call<GetStudentUpdateProfile> call, Response<GetStudentUpdateProfile> response)
                    {
                        if (response.isSuccessful())
                        {
                            if (lang_id==null||lang_id.equals("null")||lang_id.equals(null)||lang_id.equals("0")||lang_id=="0"||lang_id.equals("")||lang_id.isEmpty()){
                                Intent i = new Intent(mContext, AppLanguageActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(i);
                            }
                            else if (errormismatch.equals("Error! Grade Mismatched")){
                                Intent i = new Intent(mContext, ChooseGradeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(i);
                            }

                            else if (response.body().getStatename().equals("")||response.body().getStatename().equals("null")||response.body().getStatename().equals(null)||response.body().getDistrictname().equals("")||response.body().getDistrictname().equals("null")||response.body().getDistrictname().equals(null)||response.body().getStudentName().equals("")||response.body().getStudentName().equals("null")||response.body().getStudentName().equals(null)||
                                    response.body().getStudentclass().equals("")||response.body().getStudentclass().equals("null")||response.body().getStudentclass().equals(null)||response.body().getStudentclass().equals("0")||response.body().getStudentclass().equals(0)){
                                Intent i = new Intent(mContext, CompleteStudentProfileWithoutPin.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(i);
                            }
                            else{

                                auroScholarDataModel.getActivity().startActivity(new Intent(AuroApp.getAppContext(), DashBoardMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                // openGenricSDK(mobileno,partnersource,parnteruniqueid, apikey);

                            }

                        }
                        else
                        {
                            Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<GetStudentUpdateProfile> call, Throwable t)
                    {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void setSDKAPIGrade()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String mobile = prefModel.getUserMobile();
        String uniqueid = prefModel.getPartneruniqueid();
        String source = prefModel.getPartnersource();
        String apikey = prefModel.getApikey();
        String userid = prefModel.getUserId();
        int gradeid = prefModel.getUserclass();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("mobile_no",mobile);
        map_data.put("partner_unique_id",uniqueid); //456456
        map_data.put("partner_source",source);
        map_data.put("partner_api_key",apikey);
        map_data.put("user_id",userid);
        map_data.put("grade", String.valueOf(gradeid));

        RemoteApi.Companion.invoke().getSDKDataerror(map_data)
                .enqueue(new Callback<ErrorResponseModel>() {
                    @Override
                    public void onResponse(Call<ErrorResponseModel> call, Response<ErrorResponseModel> response)
                    {
                        try {
                            if (response.code() == 400) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    errormismatch = message;
                                    Toast.makeText(mContext,message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }


                            }
                            else if (response.code() == 200) {
                                ErrorResponseModel error = (ErrorResponseModel) response.body();
                                errormismatch = error.getMessage();

                                //  getProfile(userid,resmessage);




                            }
                            else {
                                Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(mContext, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ErrorResponseModel> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });


    }
    public void openGenricSDK(String mobileNumber,String partneruniqueid,String partnersource,String apikey  ) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int userclass = prefModel.getUserclass();
        AuroScholarInputModel inputModel = new AuroScholarInputModel();
        inputModel.setMobileNumber(mobileNumber);
        inputModel.setStudentClass(String.valueOf(userclass));
        inputModel.setPartner_unique_id(partneruniqueid);
        inputModel.setPartnerSource(prefModel.getPartnersource());
        inputModel.setPartner_api_key(apikey);
        inputModel.setActivity((Activity) mContext);
        AuroScholar.startAuroSDK(inputModel);
        //  AuroScholar.startAuroSDK(inputModel);
    }
}

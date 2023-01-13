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
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.SendMessageImageLayoutBinding;
import com.auro.application.databinding.StudentUserLayoutBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.StudentResListModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithoutPin;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.EnterPinActivity;
import com.auro.application.home.presentation.view.activity.EnterStudentPinActivity;
import com.auro.application.home.presentation.view.activity.SetPinActivity;
import com.auro.application.teacher.data.model.response.TotalStudentResModel;
import com.auro.application.teacher.presentation.view.adapter.StudentListAdapter;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectYourChildAdapter extends RecyclerView.Adapter<SelectYourChildAdapter.ClassHolder> {


    List<UserDetailResModel> mValues;
    Context mContext;
    CheckUserResModel checkUserResModel;
    StudentUserLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;
    String comingFromText = "";

    public SelectYourChildAdapter(Context mContext, List<UserDetailResModel> mValues, CommonCallBackListner commonCallBackListner) {
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

              //  if (mValues.get(0).getPin().isEmpty() || mValues.get(0).getPin().equals("")){
//                    Intent intent = new Intent(mContext, DashBoardMainActivity.class);
//                    mContext.startActivity(intent);
                 //   binding.cardView.setVisibility(View.GONE);
              //  }
//                else{
                String userid = mValues.get(position).getUserId();
                String gradeid = mValues.get(position).getGrade();
                SharedPreferences.Editor editor = mContext.getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                editor.putString("usertype", "StudentLogin");
                editor.putString("studentuserid", userid);
                editor.putString("studentgradeid", gradeid);
                editor.apply();
                    checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel();
                 int newposition = position+1;
                    checkUserForOldStudentUser(checkUserResModel, newposition);
//                    Intent intent = new Intent(mContext, EnterStudentPinActivity.class);
//                    intent.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, resModel);
//                    mContext.startActivity(intent);
           //  }

            }
        });
//        if(!AuroAppPref.INSTANCE.getModelInstance().isLogin()) {
//            Vholder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (commonCallBackListner != null) {
//                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.CLICK_BACK, mValues.get(position)));
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
            String grade = details.getGradeStudent()!=null ? details.getGradeStudent() : AuroApp.getAppContext().getResources().getString(R.string.grade_student_new);
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
            binding.txtchangepin.setVisibility(View.GONE);
            binding.cardView.setVisibility(View.VISIBLE);
                binding.cardViewButton.setVisibility(View.GONE);
                binding.tvStudentUserName.setText(userName+": " + mValues.get(position).getUserName());
                binding.tvStudentName.setText(name+": " + mValues.get(position).getStudentName());
                binding.tvStudentGrade.setText(grade+": " + mValues.get(position).getGrade());
          //  }


        }
    }

    void checkUserForOldStudentUser(CheckUserResModel checkUserResModel, int position) {
        if (checkUserResModel != null && !checkUserResModel.getUserDetails().isEmpty() ) { //&& checkUserResModel.getUserDetails().size() == 1
            UserDetailResModel resModel = checkUserResModel.getUserDetails().get(position);
            if (checkUserResModel.getUserDetails().get(position).getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_STUDENT)) {
                setDatainPref(resModel);
                   if (checkUserResModel.getUserDetails().size() == 2){

                       getProfile();
//                       Intent i = new Intent(mContext, DashBoardMainActivity.class);
//                       i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                       mContext.startActivity(i);
                   }
                   else{
                       openEnterPinActivity(resModel);
                   }



            }

        }


    }

    private void getProfile()
    {
        String suserid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",suserid);
        //0000898904,123456

        RemoteApi.Companion.invoke().getStudentData(map_data)
                .enqueue(new Callback<GetStudentUpdateProfile>()
                {
                    @Override
                    public void onResponse(Call<GetStudentUpdateProfile> call, Response<GetStudentUpdateProfile> response)
                    {
                        if (response.isSuccessful())
                        {
                            if (response.body().getStatename().equals("")||response.body().getStatename().equals("null")||response.body().getStatename().equals(null)||response.body().getDistrictname().equals("")||response.body().getDistrictname().equals("null")||response.body().getDistrictname().equals(null)||response.body().getStudentName().equals("")||response.body().getStudentName().equals("null")||response.body().getStudentName().equals(null)||
                                    response.body().getStudentclass().equals("")||response.body().getStudentclass().equals("null")||response.body().getStudentclass().equals(null)||response.body().getStudentclass().equals("0")||response.body().getStudentclass().equals(0)){

                                Intent i = new Intent(mContext, CompleteStudentProfileWithoutPin.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(i);
                            }
                            else{

                                Intent i = new Intent(mContext, DashBoardMainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(i);
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

    private void setDatainPref(UserDetailResModel resModel) {
        if (resModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_STUDENT)) {
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setStudentData(resModel);
       //   prefModel.setStudentClasses(checkUserResModel.getClasses());
            AuroAppPref.INSTANCE.setPref(prefModel);
        } else {
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setParentData(resModel);
            prefModel.setStudentClasses(checkUserResModel.getClasses());
            AuroAppPref.INSTANCE.setPref(prefModel);
        }
    }
    private void openEnterPinActivity(UserDetailResModel resModel) {

        Intent intent = new Intent(mContext, EnterPinActivity.class);
        intent.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, resModel);
        mContext.startActivity(intent);

    }
}

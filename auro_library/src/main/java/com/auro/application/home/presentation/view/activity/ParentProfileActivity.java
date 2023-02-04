package com.auro.application.home.presentation.view.activity;

import static com.auro.application.core.common.Status.DISTRICT;
import static com.auro.application.core.common.Status.GENDER;
import static com.auro.application.core.common.Status.SCHOOL;
import static com.auro.application.core.common.Status.STATE;
import static com.auro.application.core.common.Status.UPDATE_STUDENT;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityParentProfileDemoBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.DistrictData;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.ParentProfileDataModel;
import com.auro.application.home.data.model.SchoolData;
import com.auro.application.home.data.model.StateData;
import com.auro.application.home.data.model.StateDataModel;
import com.auro.application.home.data.model.StateDataModelNew;
import com.auro.application.home.data.model.StudentResponselDataModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.presentation.view.adapter.DistrictSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.DistrictSpinnerUserAdapter;
import com.auro.application.home.presentation.view.adapter.GenderSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.StateSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.StateSpinnerUserAdapter;
import com.auro.application.home.presentation.view.fragment.BottomSheetAddUserDialog;
import com.auro.application.home.presentation.viewmodel.ParentProfileViewModel;
import com.auro.application.home.presentation.viewmodel.StudentProfileViewModel;
import com.auro.application.teacher.data.model.common.DistrictDataModel;

import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.DeviceUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.cropper.CropImages;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringDynamic;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import org.openjdk.tools.javah.Gen;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ParentProfileActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnTouchListener, View.OnClickListener, CommonCallBackListner {
    @Inject
    @Named("ParentProfileActivity")  // changed by Ankesh
    ViewModelFactory viewModelFactory;
    private static final int CAMERA_REQUEST = 1888;
    ActivityParentProfileDemoBinding binding;
    ParentProfileViewModel viewModel;
    GetStudentUpdateProfile studentProfileModel = new GetStudentUpdateProfile();
    String TAG = ParentProfileActivity.class.getSimpleName();
    List<StateDataModelNew> stateDataModelNewList;
    List<DistrictDataModel> districtDataModels;
    List<StateDataModelNew> stateDataModelList;
    String stateCode = "";
    String districtCode = "";
    String SchoolName = "";
    String Schoolsearch = "";
    String GenderName="";
    PrefModel prefModel;
    RequestBody  lRequestBody;
    String filename;
    String fbnewToken;
    String maindistrictid,mainstateid;
    List<StateData> state_list = new ArrayList<>();
    List<GenderData> genderList = new ArrayList<>();
    String image_path,exist_path;
    int new_file_size;

    public ParentProfileActivity() {
        // Required empty public constructor
    }

    List<String> genderLines;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayout());
      //  ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        //viewModel = ViewModelProviders.of(this, viewModelFactory).get(ParentProfileViewModel.class);
        binding.setLifecycleOwner(this);
        AppUtil.loadAppLogo(binding.auroScholarLogo, this);
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String parentuserid = prefs.getString("parentuserid", "");



        getProfile(parentuserid);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setLogin(true);
        SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor.putString("statusparentprofile", "true");
        editor.putString("statusfillstudentprofile", "false");
        editor.putString("statussetpasswordscreen", "false");
        editor.putString("statuschoosegradescreen", "false");
        editor.putString("statuschoosedashboardscreen", "false");
        editor.putString("statusopenprofilewithoutpin", "false");
        editor.apply();

        getAllStateList();
        getGender();


        init();
        setListener();
        binding.etState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("stateid", parent.getItemAtPosition(position).toString());
            }
        });
        binding.imageViewLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences =getSharedPreferences("My_Pref",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                SharedPreferences.Editor editor1 = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                editor1.putString("statusparentprofile", "false");
                editor1.putString("statusfillstudentprofile", "false");
                editor1.putString("statussetpasswordscreen", "false");
                editor1.putString("statuschoosegradescreen", "false");
                editor1.putString("statuschoosedashboardscreen", "false");
                editor1.apply();
                finish();
               // AuroAppPref.INSTANCE.clearAuroAppPref();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);
            }
        });
        binding.etState.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    binding.etState.showDropDown();
                }
            }
        });
        binding.etState.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.etState.showDropDown();
                return false;
            }
        });
        binding.etSchoolname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.etSchoolname.showDropDown();
                return false;
            }
        });
        binding.etSchoolname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    binding.etSchoolname.showDropDown();
                }
            }
        });
        binding.etSchoolname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Schoolsearch = s.toString();
                    getSchool(stateCode,districtCode,s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.txtviewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParentProfileActivity.this, ChildAccountsActivity.class);
                startActivity(intent);
            }
        });

//        if (AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel().getUserDetails().size() == 6 || AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel().getUserDetails().size() >= 6){
//            binding.switchProfile.setVisibility(View.GONE);
//        }
//        else{
//            binding.switchProfile.setVisibility(View.VISIBLE);
//        }
        String username = prefModel.getCheckUserResModel().getUserDetails().get(0).getUserName();

        getAddedChild(username);
    }


    @Override
    protected void init() {
        setCurrentFlag(AppConstant.CurrentFlagStatus.SET_PROFILE_SCREEN);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
       // binding.etPhoneNumber.setText(prefModel.getStudentData().getUserMobile());
      //  viewModel.getStateData();
        //viewModel.getDistrictListData();
        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String parentuserid = prefs.getString("parentuserid", "");
        getProfile(parentuserid);
        AppStringDynamic.setParentProfileDemoPageStrings(binding);

    }


    @Override
    protected void setListener() {


        binding.submitbutton.setOnClickListener(this);
        binding.profileImage.setOnClickListener(this);
        binding.editImage.setOnClickListener(this);
        binding.switchProfile.setOnClickListener(this);
        binding.etGender.setOnFocusChangeListener(this);
        binding.etGender.setOnTouchListener(this);

//        binding.etState.setOnFocusChangeListener(this);
//        binding.etState.setOnTouchListener(this);


        binding.etDistict.setOnFocusChangeListener(this);
        binding.etDistict.setOnTouchListener(this);

        binding.tlDistict.setVisibility(View.VISIBLE);


        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            //observeServiceResponse();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_parent_profile_demo;
    }



    public void addDropDownDistrict(List<DistrictData> districtList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + districtList.size());
        DistrictSpinnerAdapter districtSpinnerAdapter = new DistrictSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, districtList, this);
        binding.etDistict.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etDistict.setThreshold(1);//will start working from first character
        binding.etDistict.setTextColor(Color.BLACK);
    }
    public void addDropDownSchool(List<SchoolData> districtList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + districtList.size());
        SchoolSpinnerAdapter districtSpinnerAdapter = new SchoolSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, districtList, this);
        binding.etSchoolname.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etSchoolname.setThreshold(1);//will start working from first character
        binding.etSchoolname.setTextColor(Color.BLACK);
    }
    private void addDropDownGender()
    {
        GenderSpinnerAdapter adapter = new GenderSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, genderList, this);

        //List<String> genederlist = Arrays.asList(getResources().getStringArray(R.array.genderlist_profile));
//        Log.d(TAG, "genderlist: "+genederlist);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,genederlist);
        binding.etGender.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etGender.setThreshold(1);
        binding.etGender.setTextColor(Color.BLACK);//will start working from first character
    }
    public void addDropDownState(List<StateData> stateList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + stateList.size());
        StateSpinnerAdapter stateSpinnerAdapter = new StateSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, stateList, this);
        binding.etState.setAdapter(stateSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etState.setThreshold(1);//will start working from first character
        binding.etState.setTextColor(Color.BLACK);
    }
    private void getAllStateList()
    {

        state_list.clear();
        RemoteApi.Companion.invoke().getStateData()
                .enqueue(new Callback<StateDataModel>()
                {
                    @Override
                    public void onResponse(Call<StateDataModel> call, Response<StateDataModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            String msg = response.body().getMessage();
                            for ( int i=0 ;i < response.body().getStates().size();i++)
                            {
                                String state_id = response.body().getStates().get(i).getState_id();
                                String state_name = response.body().getStates().get(i).getState_name();
                                Log.d(TAG, "onStateResponse: "+state_name);
                                StateData stateData = new StateData(state_name,state_id);
                                state_list.add(stateData);
                            }
                            addDropDownState(state_list);
                        }
                        else
                        {
                            Log.d(TAG, "onResponser: "+response.message().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.StateDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
    private void getAllDistrict(String state_id)
    {
        List<DistrictData> districtList = new ArrayList<>();
        districtList.clear();
        if (!state_id.isEmpty()||!state_id.equals("")){
            HashMap<String,String> map_data = new HashMap<>();
            map_data.put("state_id",state_id);
            RemoteApi.Companion.invoke().getDistrict(map_data)
                    .enqueue(new Callback<com.auro.application.home.data.model.DistrictDataModel>() {
                        @Override
                        public void onResponse(Call<com.auro.application.home.data.model.DistrictDataModel> call, Response<com.auro.application.home.data.model.DistrictDataModel> response)
                        {
                            if (response.isSuccessful())
                            {
                                Log.d(TAG, "onDistrictResponse: "+response.message());
                                for ( int i=0 ;i < response.body().getDistricts().size();i++)
                                {
                                    String city_id = response.body().getDistricts().get(i).getDistrict_id();
                                    String city_name = response.body().getDistricts().get(i).getDistrict_name();
                                    Log.d(TAG, "onDistrictResponse: "+city_name);
                                    DistrictData districtData = new  DistrictData(city_name,city_id);
                                    districtList.add(districtData);

                                }
                                getSchool(stateCode,districtCode,Schoolsearch);
                                addDropDownDistrict(districtList);
                            }
                            else
                            {
                                Log.d(TAG, "onResponseError: "+response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<com.auro.application.home.data.model.DistrictDataModel> call, Throwable t)
                        {
                            Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                        }
                    });
        }
       else{
            HashMap<String,String> map_data = new HashMap<>();
            map_data.put("state_id",mainstateid);
            RemoteApi.Companion.invoke().getDistrict(map_data)
                    .enqueue(new Callback<com.auro.application.home.data.model.DistrictDataModel>() {
                        @Override
                        public void onResponse(Call<com.auro.application.home.data.model.DistrictDataModel> call, Response<com.auro.application.home.data.model.DistrictDataModel> response)
                        {
                            if (response.isSuccessful())
                            {
                                Log.d(TAG, "onDistrictResponse: "+response.message());
                                for ( int i=0 ;i < response.body().getDistricts().size();i++)
                                {
                                    String city_id = response.body().getDistricts().get(i).getDistrict_id();
                                    String city_name = response.body().getDistricts().get(i).getDistrict_name();
                                    Log.d(TAG, "onDistrictResponse: "+city_name);
                                    DistrictData districtData = new  DistrictData(city_name,city_id);
                                    districtList.add(districtData);

                                }
                                addDropDownDistrict(districtList);
                            }
                            else
                            {
                                Log.d(TAG, "onResponseError: "+response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<com.auro.application.home.data.model.DistrictDataModel> call, Throwable t)
                        {
                            Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                        }
                    });
        }



    }
    private void getSchool(String state_id, String district_id, String search)
    {
        List<SchoolData> districtList = new ArrayList<>();
        districtList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("state_id",state_id);
        map_data.put("district_id",district_id);
        map_data.put("search",search);
        RemoteApi.Companion.invoke().getSchool(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.SchoolDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.SchoolDataModel> call, Response<com.auro.application.home.data.model.SchoolDataModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            Log.d(TAG, "onDistrictResponse: "+response.message());
                            for ( int i=0 ;i < response.body().getSchools().size();i++)
                            {
                                int school_id = response.body().getSchools().get(i).getID();
                                String school_name = response.body().getSchools().get(i).getSCHOOL_NAME();
                                Log.d(TAG, "onDistrictResponse: "+school_name);
                                SchoolData districtData = new  SchoolData(school_name,school_id);
                                districtList.add(districtData);

                            }
                            addDropDownSchool(districtList);
                        }
                        else
                        {
                            Log.d(TAG, "onResponseError: "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.SchoolDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }
    private void getGender()
    {
        genderList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("key","Gender");
        map_data.put("language_id",prefModel.getUserLanguageId());
        Log.d("langdata",map_data.toString());
        RemoteApi.Companion.invoke().getGender(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.GenderDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.GenderDataModel> call, Response<com.auro.application.home.data.model.GenderDataModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            Log.d(TAG, "onDistrictResponse: "+response.message());
                            for ( int i=0 ;i < response.body().getResult().size();i++)
                            {
                                int gender_id = response.body().getResult().get(i).getID();
                               String gender_name = response.body().getResult().get(i).getName();
                                String translated_name = response.body().getResult().get(i).getTranslatedName();

                                GenderData districtData = new  GenderData(gender_id,translated_name,gender_name);
                                genderList.add(districtData);

                            }
                         //   binding.etGender.setText(genderList.get(0).getTranslatedName());
                            addDropDownGender();
                        }
                        else
                        {
                            Log.d(TAG, "onResponseError: "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.GenderDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }
    private void getProfile(String userid)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        ProgressDialog progress = new ProgressDialog(ParentProfileActivity.this);
        progress.setTitle(details.getProcessing());
        progress.setMessage(details.getProcessing());
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);

        RemoteApi.Companion.invoke().getParentData(map_data)
                .enqueue(new Callback<ParentProfileDataModel>()
                {
                    @Override
                    public void onResponse(Call<ParentProfileDataModel> call, Response<ParentProfileDataModel> response)
                    {
                        try {


                            if (response.isSuccessful()) {

                                for (int i = 0; i < response.body().getResult().size(); i++) {
                                    String mobileno = response.body().getResult().get(i).getMobile_no();
                                    String email = response.body().getResult().get(i).getEmail_id();
                                    String fullname = response.body().getResult().get(i).getFull_name();
                                    String gender = response.body().getResult().get(i).getGender();
                                    String state = response.body().getResult().get(i).getState_name();
                                    String district = response.body().getResult().get(i).getDistrict_name();
                                    maindistrictid = String.valueOf(response.body().getResult().get(i).getDistrict_id());
                                    mainstateid = String.valueOf(response.body().getResult().get(i).getState_id());
                                    binding.etFullName.setText(fullname);
                                    binding.etEmail.setText(email);

                                    SharedPreferences.Editor editor1 = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                    editor1.putString("parentupdateparentemailid", email);
                                    editor1.apply();
                                    binding.etPhoneNumber.setText(mobileno);
                                    if (gender != null || !gender.equals("") || !gender.equals("null")) {
                                        binding.etGender.setText(gender);
                                        addDropDownGender();
                                    }
//                            else{
//                                getGender();
//                            }

                                    binding.etState.setText(state);
                                    binding.etDistict.setText(district);
                                    exist_path = response.body().getResult().get(i).getProfile_pic().toString();
                                    if (exist_path.equals("") || exist_path.equals("null") || exist_path.isEmpty() || exist_path.equals(null)) {
                                        Glide.with(ParentProfileActivity.this)
                                                .load(ParentProfileActivity.this.getResources().getIdentifier("my_drawable_image_name", "drawable", ParentProfileActivity.this.getPackageName()))
                                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))
                                                .placeholder(R.drawable.circle_user_image)
                                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL))
                                                .into(binding.profileimage);
                                    } else {
                                        exist_path = response.body().getResult().get(i).getProfile_pic().toString();
                                        Glide.with(getApplicationContext()).load(response.body().getResult().get(i).getProfile_pic())
                                                 .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))
                                                .circleCrop()).into(binding.profileimage);

                                    }
                                    if (!mainstateid.isEmpty() || !mainstateid.equals("")) {
                                        getAllDistrict(mainstateid);
                                    }
                                }
                                // addDropDownState(state_list);


                                progress.dismiss();
                            } else {
                                Toast.makeText(ParentProfileActivity.this, "Please check your network", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                                Log.d(TAG, "onResponser: " + response.message().toString());
                            }
                        } catch (Exception e) {
                            Toast.makeText(ParentProfileActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.ParentProfileDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }



    private void updateUser()
    {
        String name = binding.etFullName.getText().toString();
        String gender = GenderName;
        String email = binding.etEmail.getText().toString();
        String state = stateCode;
        String distict = districtCode;
        String languageid = prefModel.getUserLanguageId();
        String userid = prefModel.getCheckUserResModel().getUserDetails().get(0).getUserId();
        String username = prefModel.getCheckUserResModel().getUserDetails().get(0).getUserName();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            fbnewToken = instanceIdResult.getToken();
            Log.e("newToken", fbnewToken);
        });
        RequestBody useridp  = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody namep  = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody stateid  = RequestBody.create(MediaType.parse("text/plain"), stateCode);
        RequestBody districtid  = RequestBody.create(MediaType.parse("text/plain"), districtCode);
        RequestBody gendertype  = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody langid  = RequestBody.create(MediaType.parse("text/plain"), languageid);
        RequestBody emailid  = RequestBody.create(MediaType.parse("text/plain"), email);
        if (image_path == null || image_path.equals("null") || image_path.equals("")||image_path.isEmpty()){
            lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            filename="";
        }
        else{
              lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), studentProfileModel.getImageBytes());
            filename=image_path.substring(image_path.lastIndexOf("/")+1);
        }

        MultipartBody.Part lFile = MultipartBody.Part.createFormData("user_profile_image", filename, lRequestBody);

        RemoteApi.Companion.invoke()
                    .updateparentdetail(useridp,namep,stateid,
                            districtid,gendertype,langid,emailid,lFile)
                    .enqueue(new Callback<StudentResponselDataModel>() {
                        @Override
                        public void onResponse(Call<StudentResponselDataModel> call, Response<StudentResponselDataModel> response)
                        {
                            if (response.code() == 400){
                                String msg = response.body().getMessage();
                                showSnackbarError(msg);
                            }
                            else if (response.isSuccessful())
                            {
                                Log.d(TAG, "onImageResponse: ");
                                String status = response.body().getStatus().toString();
                                String msg = response.body().getMessage();
                                if (status.equalsIgnoreCase("success"))
                                {
                                    Toast.makeText(ParentProfileActivity.this, msg.toString(), Toast.LENGTH_SHORT).show();
                                   // showSnackbarError(msg);
                                }
                            }
                            else
                            {
                                Log.d(TAG, "onImageerrorResponse: "+response.errorBody().toString());
                                showSnackbarError(response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<StudentResponselDataModel> call, Throwable t)
                        {
                            Log.d(TAG, "onImgFailure: "+t.getMessage());
                        }
                    });


    }



    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            if (v.getId() == R.id.etGender) {
                binding.etGender.showDropDown();
            }
            /*else if (v.getId() == R.id.etState) {
                binding.etState.showDropDown();

                AppLogger.v("UpdatePradeep","onFocusChange state ");
            }*/ else if (v.getId() == R.id.etDistict) {
                binding.etDistict.showDropDown();
                AppLogger.v("UpdatePradeep","onFocusChange distict ");
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // binding.etGender.showDropDown();

        if (v.getId() == R.id.etGender) {
            binding.etGender.showDropDown();
        }/* else if (v.getId() == R.id.etState) {
            binding.etState.showDropDown();
            getAllStateList();
            Log.d(TAG, "onTouch: Ankesh");
        }*/ else if (v.getId() == R.id.etDistict) {
            binding.etDistict.showDropDown();
        }

        return false;
    }


    public void callingStudentUpdateProfile() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String name = binding.etFullName.getText().toString();
        String gender = binding.etGender.getText().toString();
        String state = stateCode;
        String distict = districtCode;
        AppLogger.v("callingStudentUpdateProfile", state);
        AppLogger.v("callingStudentUpdateProfile", distict);

        studentProfileModel.setStudentName(name);
        studentProfileModel.setGender(gender);
        studentProfileModel.setUserId(prefModel.getStudentData().getUserId());
        studentProfileModel.setStateId(stateCode);
        studentProfileModel.setDistrictId(districtCode);
        /*Device Detail*/
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            fbnewToken = instanceIdResult.getToken();
            Log.e("newToken", fbnewToken);
        });
        studentProfileModel.setDeviceToken(fbnewToken);
        studentProfileModel.setMobileVersion(DeviceUtil.getVersionName());
        studentProfileModel.setMobileManufacturer(DeviceUtil.getManufacturer(this));
        studentProfileModel.setMobileModel(DeviceUtil.getModelName(this));
        studentProfileModel.setBuildVersion(AppUtil.getAppVersionName());
        studentProfileModel.setIpAddress(AppUtil.getIpAdress());
        studentProfileModel.setLanguage(ViewUtil.getLanguageId());
        studentProfileModel.setSchoolName(SchoolName);
        /*End of Device Detail*/
        if (TextUtil.isEmpty(name)) {
            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getPlease_enetr_the_name());//"Please enter the name"
            return;
        } else if (TextUtil.isEmpty(gender)) {
            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getPlease_enter_the_gender());
            return;
        } else if (TextUtil.isEmpty(state) || state.contains("Please Select state")) {

            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getPlease_enter_select_state());
        } else if (TextUtil.isEmpty(distict) || distict.contains("Please select city")) {
            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getPlease_enter_district());
        } else {
            handleProgress(0, "");
            viewModel.sendStudentProfileInternet(studentProfileModel);
        }
    }

    private void handleProgress(int val, String message) {
        switch (val) {
            case 0:
                binding.progressbar.pgbar.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.progressbar.pgbar.setVisibility(View.GONE);
                break;
            case 2:
                binding.progressbar.pgbar.setVisibility(View.GONE);
                showSnackbarError(message);
                break;
        }
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.editImage || id == R.id.profile_image) {
            if (Build.VERSION.SDK_INT > 26) {
                askPermission();
            } else {
                askPermission();
                // askPermission();
            }
        } else if (id == R.id.skip_for_now) {
            startDashboardActivity();
        } else if (id == R.id.submitbutton) {
            String email = binding.etEmail.getText().toString().trim();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            try {
                Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
                if (binding.etFullName.getText().toString().equals("") || binding.etFullName.getText().toString().isEmpty()) {
                    Toast.makeText(this, details.getPlease_enter_your_name(), Toast.LENGTH_SHORT).show();

                } else if (binding.etFullName.getText().toString().startsWith(" ")) {
                    Toast.makeText(this, "Can not enter space in name", Toast.LENGTH_SHORT).show();
                } else if (binding.etGender.getText().toString().equals("") || binding.etGender.getText().toString().isEmpty() || binding.etGender.getText().toString().equals("Select Gender") || binding.etGender.getText().toString().equals("Gender") || genderList.get(0).getTranslatedName().equals(binding.etGender.getText().toString())) {
                    Toast.makeText(this, details.getPlease_select_gender(), Toast.LENGTH_SHORT).show();

                } else if (binding.etState.getText().toString().equals("") || binding.etState.getText().toString().isEmpty()) {
                    Toast.makeText(this, details.getPlease_select_state(), Toast.LENGTH_SHORT).show();

                } else if (binding.etDistict.getText().toString().equals("") || binding.etDistict.getText().toString().isEmpty()) {
                    Toast.makeText(this, details.getPlease_select_district(), Toast.LENGTH_SHORT).show();

                } else if (!binding.etEmail.getText().toString().isEmpty() && !binding.etEmail.getText().toString().equals("") && !email.matches(emailPattern)) {
                    Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();

                } else if (exist_path == null || exist_path.equals("null") || exist_path.equals("")) {
                    if (image_path == null || image_path.equals("null") || image_path.equals("") || image_path.isEmpty()) {
                        Toast.makeText(this, details.getUpload_your_photo(), Toast.LENGTH_SHORT).show();
                    } else {
                        updateUser();
                    }
                } else {
                    updateUser();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
            }

            // callingStudentUpdateProfile();
        } else if (id == R.id.switchProfile) {
            openBottomSheetDialog();
        }

    }


    private void askPermission() {
//        String rationale = "For Upload Profile Picture. Camera and Storage Permission is Must.";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(this, PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
                ImagePicker.with(ParentProfileActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//                ViewUtil.showSnackBar(binding.getRoot(), rationale);
//            }
//        });
    }
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ParentProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
                    if (cameraIntent.resolveActivity(ParentProfileActivity.this.getPackageManager()) != null) {
                        startActivityForResult(cameraIntent, 1);
                    }


                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        AppLogger.e("StudentProfile", "fragment requestCode=" + requestCode);
//        if (Build.VERSION.SDK_INT > 26) {
//            if (requestCode == 2) {
//
//
//                if (resultCode == RESULT_OK) {
//                    try {
//
//
//                        Uri uri = data.getData();
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                        AppLogger.e("StudentProfile", "image path=" + uri.getPath());
//                        image_path = uri.getPath();
//
//                        Uri selectedImage = data.getData();
//                        String[] filePath = { MediaStore.Images.Media.DATA };
//                        Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
//                        c.moveToFirst();
//                        int columnIndex = c.getColumnIndex(filePath[0]);
//                        String picturePath = c.getString(columnIndex);
//                        c.close();
//                        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                        loadimage(bitmap);
//                        byte[] bytes = AppUtil.encodeToBase64(bitmap, 100);
//                        long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                        int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//                        if (file_size >= 500) {
//                            Toast.makeText(this, "Image size is too large", Toast.LENGTH_SHORT).show();
//
//                            studentProfileModel.setImageBytes(AppUtil.encodeToBase64(bitmap, 50));
//
//                        } else {
//
//                            studentProfileModel.setImageBytes(bytes);
//                        }
//
//
//
//
//
//                    } catch (Exception e) {
//                        AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
//                    }
//
//                }
//
//            }
//            else if (requestCode == 1 ) {
//                if (Build.VERSION.SDK_INT > 26) {
//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    image_path = String.valueOf(data.getExtras().get("data"));
//                    byte[] bytes = AppUtil.encodeToBase64(photo, 100);
//                    long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                    int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//
//
//                    if (file_size >= 500) {
//                        Toast.makeText(this, "Image size is too large", Toast.LENGTH_SHORT).show();
//
//                        studentProfileModel.setImageBytes(AppUtil.encodeToBase64(photo, 50));
//                    } else {
//
//                        studentProfileModel.setImageBytes(bytes);
//                    }
//                    loadimage(photo);
//                }
//                else{
//                    if (resultCode == RESULT_OK) {
//                        AppLogger.v("BigDes", "Sdk step 4");
//                        try {
//                            CropImages.ActivityResult result = CropImages.getActivityResult(data);
//                            Uri resultUri = result.getUri();
//                            image_path =  resultUri.getPath();
//                            Bitmap picBitmap = BitmapFactory.decodeFile(resultUri.getPath());
//                            byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
//                            long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                            int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//
//                            AppLogger.e("StudentProfile", "image size=" + resultUri.getPath());
//                            if (file_size >= 500) {
//                                Toast.makeText(this, "Image size is too large", Toast.LENGTH_SHORT).show();
//
//                                studentProfileModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
//                            } else {
//                                studentProfileModel.setImageBytes(bytes);
//                            }
//
//                            loadimage(picBitmap);
//                        } catch (Exception e) {
//
//                        }
//
//                    }
//                }
//            }
//        }
//        else{
//            if (requestCode == 2) {  //2404
//
//                if (resultCode == RESULT_OK) {
//                    try {
//
//
//                        Uri uri = data.getData();
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                        AppLogger.e("StudentProfile", "image path=" + uri.getPath());
//                        image_path = uri.getPath();
//                        Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
//                        byte[] bytes = AppUtil.encodeToBase64(bitmap, 100);
//                        long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                        int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//
//                        AppLogger.e("StudentProfile", "image size=" + uri.getPath());
//                        if (file_size >= 500) {
//                            Toast.makeText(this, "Image size is too large", Toast.LENGTH_SHORT).show();
//
//                            studentProfileModel.setImageBytes(AppUtil.encodeToBase64(bitmap, 50));
//                        } else {
//
//                            studentProfileModel.setImageBytes(bytes);
//                        }
//                        loadimage(bitmap);
//                    } catch (Exception e) {
//                        AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
//                    }
//
//                }
//
//
//
//
//            }
//            else if (requestCode == 1 ) {
//
//                if (requestCode == 1 && resultCode == Activity.RESULT_OK)
//                {
//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    image_path = String.valueOf(data.getExtras().get("data"));
//                    byte[] bytes = AppUtil.encodeToBase64(photo, 100);
//                    long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                    int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//                    // Toast.makeText(this, image_path.toString(), Toast.LENGTH_SHORT).show();
//
//                    if (file_size >= 500) {
//                        Toast.makeText(this, "Image size is too large", Toast.LENGTH_SHORT).show();
//
//                        studentProfileModel.setImageBytes(AppUtil.encodeToBase64(photo, 50));
//                    } else {
//
//                        studentProfileModel.setImageBytes(bytes);
//                    }
//                    loadimage(photo);
//                }
//
//            }
//        }
//
//
//
//
//
//
//
//
//    }
@Override
public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    AppLogger.e("StudentProfile", "fragment requestCode=" + requestCode);

    if (requestCode == 2404) {
        // CropImages.ActivityResult result = CropImages.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            try {


                Uri uri = data.getData();
                AppLogger.e("StudentProfile", "image path=" + uri.getPath());
                image_path = uri.getPath();
                Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
                byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
                long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
                int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));

                AppLogger.e("StudentProfile", "image size=" + uri.getPath());
                if (file_size >= 500) {
                    studentProfileModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
                } else {
                    studentProfileModel.setImageBytes(bytes);
                }

                //     new_file_size = Integer.parseInt(String.valueOf(studentProfileModel.getImageBytes().length / 1024));
//                    AppLogger.d(TAG, "Image Path  new Size kb- " + mb + "-bytes-" + new_file_size);

//                    Uri selectedImage = data.getData();
//                    String[] filePath = { MediaStore.Images.Media.DATA };
//                    Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
//                    c.moveToFirst();
//                    int columnIndex = c.getColumnIndex(filePath[0]);
//                    image_path = c.getString(columnIndex);
//                    c.close();
//                    Bitmap thumbnail = (BitmapFactory.decodeFile(image_path));
//                    byte[] bytes = AppUtil.encodeToBase64(thumbnail, 100);
//                    long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                    int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//                    if (file_size >= 500) {
//                        studentProfileModel.setImageBytes(AppUtil.encodeToBase64(thumbnail, 50));
//                    } else {
//                        studentProfileModel.setImageBytes(bytes);
//                    }
                loadimage(picBitmap);
            } catch (Exception e) {
                AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            showSnackbarError(ImagePicker.getError(data));
        } else {
            // Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    else if (requestCode == CAMERA_REQUEST ) {

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image_path = String.valueOf(data.getExtras().get("data"));
            byte[] bytes = AppUtil.encodeToBase64(photo, 100);
            long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
            int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
            //   Toast.makeText(ParentProfileActivity.this, image_path.toString(), Toast.LENGTH_SHORT).show();

            if (file_size >= 500) {
                studentProfileModel.setImageBytes(AppUtil.encodeToBase64(photo, 50));
            } else {
                studentProfileModel.setImageBytes(bytes);
            }
            loadimage(photo);
        }


//            CropImages.ActivityResult result = CropImages.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                AppLogger.v("BigDes", "Sdk step 4");
//                try {
//                    Uri resultUri = result.getUri();
//                    image_path =  resultUri.getPath();
//                    Bitmap picBitmap = BitmapFactory.decodeFile(resultUri.getPath());
//                    byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
//                    long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                    int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//
//                    AppLogger.e("StudentProfile", "image size=" + resultUri.getPath());
//                    if (file_size >= 500) {
//                        studentProfileModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
//                    } else {
//                        studentProfileModel.setImageBytes(bytes);
//                    }
//
//                    loadimage(picBitmap);
//                } catch (Exception e) {
//
//                }
//
//            }
//            else if (resultCode == CropImages.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//                AppLogger.e("chhonker", "Exception error=" + error.getMessage());
//            }
    }
}
    private void loadimage(Bitmap picBitmap) {
        RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(binding.profileimage.getContext().getResources(), picBitmap);
        circularBitmapDrawable.setCircular(true);
        binding.profileimage.setImageDrawable(circularBitmapDrawable);
        binding.editImage.setVisibility(View.VISIBLE);
    }

    private void startDashboardActivity() {
        finish();
        Intent i = new Intent(this, DashBoardMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }




    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {


        if (commonDataModel.getClickType()==STATE)
        {
            StateData stateDataModel = (StateData) commonDataModel.getObject();
            binding.etState.setText(stateDataModel.getState_name());
            getAllDistrict(stateDataModel.getState_id());
            Log.d("state_id", stateDataModel.getState_id());
            stateCode = stateDataModel.getState_id();
            binding.etDistict.setText("");
        }
        else if (commonDataModel.getClickType()==DISTRICT)
        {
            DistrictData districtData = (DistrictData) commonDataModel.getObject();
            binding.etDistict.setText(districtData.getDistrict_name());
            districtCode = districtData.getDistrict_id();
            getSchool(stateCode,districtCode,Schoolsearch);
            binding.etSchoolname.setText("");

        }
        else if (commonDataModel.getClickType()==GENDER)
        {
            GenderData gData = (GenderData) commonDataModel.getObject();
            GenderName = gData.getName();
            getGender();
            binding.etGender.setText(gData.getTranslatedName());

        }
        else if (commonDataModel.getClickType()==SCHOOL)
        {
            SchoolData gData = (SchoolData) commonDataModel.getObject();
            binding.etSchoolname.setText(gData.getSCHOOL_NAME());
            SchoolName = gData.getSCHOOL_NAME();

        }


    }



    void openBottomSheetDialog() {
        BottomSheetAddUserDialog bottomSheet = new BottomSheetAddUserDialog();
        bottomSheet.show(this.getSupportFragmentManager(),
                "ModalBottomSheet");
    }


    private void setCurrentFlag(String setProfileScreen) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setCurrentScreenFlag(setProfileScreen);
        prefModel.setLogin(true);
        AuroAppPref.INSTANCE.setPref(prefModel);
    }

    private void getAddedChild(String user_name)
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
                            if (response.body().getUserDetails().size() == 6 || response.body().getUserDetails().size() >=6){


                                binding.switchProfile.setVisibility(View.GONE);
                            }
                            else{
                                binding.switchProfile.setVisibility(View.VISIBLE);
                            }




                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserResModel> call, Throwable t)
                    {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


}
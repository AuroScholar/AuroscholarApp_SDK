package com.auro.application.home.presentation.view.fragment;

import static com.auro.application.core.common.Status.DISTRICT;
import static com.auro.application.core.common.Status.GENDER;
import static com.auro.application.core.common.Status.SCHOOL;
import static com.auro.application.core.common.Status.STATE;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.home.data.base_component.BaseFragment;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityParentProfileDemoBinding;
import com.auro.application.home.data.model.DistrictData;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.SchoolData;
import com.auro.application.home.data.model.StateData;
import com.auro.application.home.data.model.StateDataModel;
import com.auro.application.home.data.model.StudentResponselDataModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.ParentProfileActivity;
import com.auro.application.home.presentation.view.adapter.DistrictSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.GenderSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.StateSpinnerAdapter;
import com.auro.application.home.presentation.viewmodel.ParentProfileViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.DeviceUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ParentProfileFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnTouchListener, View.OnClickListener, CommonCallBackListner {
    @Inject
    @Named("ParentProfileDemoActivity")  // changed by Ankesh
    ActivityParentProfileDemoBinding binding;
    ParentProfileViewModel viewModel;
    GetStudentUpdateProfile studentProfileModel = new GetStudentUpdateProfile();
    String TAG = ParentProfileActivity.class.getSimpleName();
    String stateCode = "";
    String districtCode = "";
    String SchoolName = "";
    String Schoolsearch = "";
    PrefModel prefModel;
    String fbnewToken;
    List<StateData> state_list = new ArrayList<>();
    List<GenderData> genderList = new ArrayList<>();
    String image_path;
    public ParentProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(getActivity(), getLayout());
        //  ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        //viewModel = ViewModelProviders.of(this, viewModelFactory).get(ParentProfileViewModel.class);
        binding.setLifecycleOwner(this);
        AppUtil.loadAppLogo(binding.auroScholarLogo, getActivity());
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        getAllStateList();
        getGender();
        init();
        setListener();
        setToolbar();
        binding.etState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("stateid", parent.getItemAtPosition(position).toString());
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



    }


    @Override
    protected void init() {
        setCurrentFlag(AppConstant.CurrentFlagStatus.SET_PROFILE_SCREEN);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        binding.etPhoneNumber.setText(prefModel.getStudentData().getUserMobile());
        AppStringDynamic.setParentProfileDemoPageStrings(binding);

    }

    @Override
    protected void setToolbar() {

    }


    @Override
    protected void setListener() {

        binding.submitbutton.setOnClickListener(this);
        binding.profileImage.setOnClickListener(this);
        binding.editImage.setOnClickListener(this);
        binding.etGender.setOnFocusChangeListener(this);
        binding.etGender.setOnTouchListener(this);
        binding.etDistict.setOnFocusChangeListener(this);
        binding.etDistict.setOnTouchListener(this);

        binding.tlDistict.setVisibility(View.VISIBLE);



    }

    @Override
    protected int getLayout() {
        return R.layout.activity_parent_profile_demo;
    }





    public void addDropDownDistrict(List<DistrictData> districtList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + districtList.size());
        com.auro.application.home.presentation.view.adapter.DistrictSpinnerAdapter districtSpinnerAdapter = new DistrictSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, districtList, this);
        binding.etDistict.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etDistict.setThreshold(1);//will start working from first character
        binding.etDistict.setTextColor(Color.BLACK);
    }
    public void addDropDownSchool(List<SchoolData> districtList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + districtList.size());
        SchoolSpinnerAdapter districtSpinnerAdapter = new SchoolSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, districtList, this);
        binding.etSchoolname.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etSchoolname.setThreshold(1);//will start working from first character
        binding.etSchoolname.setTextColor(Color.BLACK);
    }
    private void addDropDownGender()
    {
        GenderSpinnerAdapter adapter = new GenderSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, genderList, this);
        List<String> genederlist = Arrays.asList(getResources().getStringArray(R.array.genderlist_profile));
        binding.etGender.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etGender.setThreshold(1);
        binding.etDistict.setTextColor(Color.BLACK);//will start working from first character
    }
    public void addDropDownState(List<StateData> stateList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + stateList.size());
        com.auro.application.home.presentation.view.adapter.StateSpinnerAdapter stateSpinnerAdapter = new StateSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, stateList, this);
        binding.etState.setAdapter(stateSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etState.setThreshold(1);//will start working from first character
        binding.etState.setTextColor(Color.BLACK);
    }
    private void getAllStateList()
    {

        state_list.clear();
        RemoteApi.Companion.invoke().getStateData()
                .enqueue(new Callback<com.auro.application.home.data.model.StateDataModel>()
                {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.StateDataModel> call, Response<StateDataModel> response)
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

    private void updateUser()
    {

        String name = binding.etFullName.getText().toString();
        String gender = binding.etGender.getText().toString();
        String email = binding.etEmail.getText().toString();
        String state = stateCode;
        String distict = districtCode;
        String userid = prefModel.getStudentData().getUserId();
        String username = prefModel.getStudentData().getUserName();


        String mobileversion = DeviceUtil.getVersionName();
        String schoolname = SchoolName;
        RequestBody buildversion  = RequestBody.create(MediaType.parse("text/plain"), mobileversion);
        RequestBody schoolnames  = RequestBody.create(MediaType.parse("text/plain"), schoolname);
        RequestBody emailid  = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody gendertype  = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody devicetokenid  = RequestBody.create(MediaType.parse("text/plain"), "cBc56oNdTQKkHRkRU3oNyS:APA91bEymxwKC8ppGU9vM1WeI6F0S-mvwTsaJuyE6ijkw5bqUEJaaaf54Wf_2J2IPSbTs9t_eV3o1mmqm9pxtKSYtYOWpKVJtWtazol0KHzK3cqxZSpbBt1rDYVeE_kgz2Tme8MuGFgB");
        RequestBody stateid  = RequestBody.create(MediaType.parse("text/plain"), stateCode);
        RequestBody districtid  = RequestBody.create(MediaType.parse("text/plain"), districtCode);
        RequestBody usernamep  = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody useridp  = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),  studentProfileModel.getImageBytes());
        String filename=image_path.substring(image_path.lastIndexOf("/")+1);
        MultipartBody.Part lFile = MultipartBody.Part.createFormData("user_profile_image", filename, lRequestBody);
        RequestBody partnersource  = RequestBody.create(MediaType.parse("text/plain"), "AAGA5h8Btd");


        if (name.isEmpty())
        {
            binding.etFullName.setError("Enter Full Name");
        }
        else if (email.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.etEmail.setError("Enter Valid email id");
        }
        else if (gender.isEmpty())
        {
            binding.etGender.setError("Select Gender");
        }
        else if (state.isEmpty())
        {
            binding.etState.setError("Select State");
        }
        else if (distict.isEmpty())
        {
            binding.etDistict.setError("Select District");
        }
        else
        {
            RemoteApi.Companion.invoke()
                    .updateuserdetail(buildversion,schoolnames,emailid,
                            gendertype,partnersource,devicetokenid,stateid,
                            districtid,usernamep,useridp,usernamep,lFile)
                    .enqueue(new Callback<StudentResponselDataModel>() {
                        @Override
                        public void onResponse(Call<StudentResponselDataModel> call, Response<StudentResponselDataModel> response)
                        {
                            if (response.isSuccessful())
                            {
                                Log.d(TAG, "onImageResponse: ");
                                String status = response.body().getStatus().toString();
                                String msg = response.body().getMessage();
                                if (status.equalsIgnoreCase("success"))
                                {
                                    showSnackbarError(msg);
                                    startDashboardActivity();
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


    }



    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            if (v.getId() == R.id.etGender) {
                binding.etGender.showDropDown();
            }
            else if (v.getId() == R.id.etDistict) {
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
        } else if (v.getId() == R.id.etDistict) {
            binding.etDistict.showDropDown();
        }

        return false;
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
            askPermission();
        } else if (id == R.id.skip_for_now) {
            startDashboardActivity();
        } else if (id == R.id.nextButton) {
            updateUser();
        }

    }


    private void askPermission() {
//        String rationale = "For Upload Profile Picture. Camera and Storage Permission is Must.";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(getActivity(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
                ImagePicker.with(getActivity())
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//
//                ViewUtil.showSnackBar(binding.getRoot(), rationale);
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.e("StudentProfile", "fragment requestCode=" + requestCode);

        if (requestCode == 2404) {

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


                    loadimage(picBitmap);
                } catch (Exception e) {
                    AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showSnackbarError(ImagePicker.getError(data));
            } else {

            }
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
        getActivity().finish();
        Intent i = new Intent(getActivity(), DashBoardMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }


    public void onBackPressed() {
        getActivity().onBackPressed();
        getActivity().finishAffinity();
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
        }
        else if (commonDataModel.getClickType()==DISTRICT)
        {
            DistrictData districtData = (DistrictData) commonDataModel.getObject();
            binding.etDistict.setText(districtData.getDistrict_name());
            districtCode = districtData.getDistrict_id();

        }
        else if (commonDataModel.getClickType()==GENDER)
        {
            GenderData gData = (GenderData) commonDataModel.getObject();
            binding.etGender.setText(gData.getTranslatedName());

        }
        else if (commonDataModel.getClickType()==SCHOOL)
        {
            SchoolData gData = (SchoolData) commonDataModel.getObject();
            binding.etSchoolname.setText(gData.getSCHOOL_NAME());
            SchoolName = gData.getSCHOOL_NAME();

        }


    }






    private void setCurrentFlag(String setProfileScreen) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setCurrentScreenFlag(setProfileScreen);
        prefModel.setLogin(true);
        AuroAppPref.INSTANCE.setPref(prefModel);
    }
}
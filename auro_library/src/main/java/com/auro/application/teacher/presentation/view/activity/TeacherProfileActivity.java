package com.auro.application.teacher.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.SdkCallBack;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.databinding.FragmentUserProfileBinding;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.StateDataModelNew;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.SplashScreenAnimationActivity;
import com.auro.application.home.presentation.view.adapter.DistrictSpinnerUserAdapter;
import com.auro.application.home.presentation.view.adapter.StateSpinnerAdapter;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.request.TeacherReqModel;
import com.auro.application.teacher.presentation.viewmodel.TeacherProfileViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;


public class TeacherProfileActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnTouchListener, View.OnClickListener, CommonCallBackListner {

    @Inject
    @Named("TeacherUserProfileFragment")
    ViewModelFactory viewModelFactory;
    FragmentUserProfileBinding binding;
    TeacherProfileViewModel viewModel;
   // StudentProfileModel studentProfileModel = new StudentProfileModel();
    String TAG = TeacherProfileActivity.class.getSimpleName();
    List<StateDataModelNew> stateDataModelList;
    List<DistrictDataModel> districtDataModels;
    String stateCode = "";
    String districtCode = "";
    TeacherReqModel teacherReqModel = new TeacherReqModel();



    public TeacherProfileActivity() {
        // Required empty public constructor
    }

    List<String> genderLines;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayout());
        ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TeacherProfileViewModel.class);
        binding.setLifecycleOwner(this);

        init();
        setListener();

    }


    @Override
    protected void init() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        binding.etPhoneNumber.setText(prefModel.getUserMobile());
        setUiOnScreen();
        viewModel.getStateListData();
        viewModel.getDistrictListData();
        addDropDownGender();
    }



    @Override
    protected void setListener() {
        binding.skipForNow.setOnClickListener(this);
        binding.nextButton.setOnClickListener(this);
        binding.profileImage.setOnClickListener(this);
        binding.editImage.setOnClickListener(this);
        binding.etGender.setOnFocusChangeListener(this);

        binding.etGender.setOnTouchListener(this);

        binding.etState.setOnFocusChangeListener(this);
        binding.etState.setOnTouchListener(this);


        binding.etDistict.setOnFocusChangeListener(this);
        binding.etDistict.setOnTouchListener(this);

        binding.tlDistict.setVisibility(View.GONE);


        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_profile;
    }

    public void addDropDownGender() {
        genderLines = Arrays.asList(getResources().getStringArray(R.array.genderlist_profile));

        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_dropdown_item, genderLines);
        //Getting the instance of AutoCompleteTextView
        binding.etGender.setThreshold(1);//will start working from first character
        binding.etGender.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etGender.setTextColor(Color.BLACK);
    }

   /* public void addDropDownState() {
        AppLogger.v("StatePradeep","addDropDownState    "+stateDataModelList.size());
       // StateSpinnerUserAdapter stateSpinnerAdapter = new StateSpinnerUserAdapter(this, 0,stateDataModelList,this);
        StateSpinnerAdapter stateSpinnerAdapter = new StateSpinnerAdapter(this, 0,stateDataModelList,this);

        binding.etState.setThreshold(1);//will start working from first character
        binding.etState.setAdapter(stateSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etState.setTextColor(Color.BLACK);
    }*/

    public void addDropDownDistict() {
        DistrictSpinnerUserAdapter districtSpinnerAdapter = new DistrictSpinnerUserAdapter(this,0,districtDataModels,this);
        binding.etDistict.setThreshold(1);//will start working from first character
        binding.etDistict.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etDistict.setTextColor(Color.BLACK);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if(v.getId() == R.id.etGender) {
                binding.etGender.showDropDown();
            }else if(v.getId() == R.id.etState){
                binding.etState.showDropDown();
            }else if(v.getId() == R.id.etDistict){
                binding.etDistict.showDropDown();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
       // binding.etGender.showDropDown();

            if(v.getId() == R.id.etGender) {
                binding.etGender.showDropDown();
            }else if(v.getId() == R.id.etState){
                binding.etState.showDropDown();
            }else if(v.getId() == R.id.etDistict){
                binding.etDistict.showDropDown();
            }

        return false;
    }


    public void callingStudentUpdateProfile() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String name = binding.etFullName.getText().toString();
        String gender = binding.etGender.getText().toString();
        String state = binding.etState.getText().toString();
        String distict = binding.etDistict.getText().toString();


        teacherReqModel.setTeacher_name(name);
       // teacherReqModel.setGender(gender);
        teacherReqModel.setMobile_no(prefModel.getUserMobile());
        teacherReqModel.setState_id(stateCode);
        teacherReqModel.setDistrict_id(districtCode);
      /*  teacherReqModel.setTeacher_email("");
        teacherReqModel.setTeacher_class("");
        teacherReqModel.setTeacher_subject("");
        teacherReqModel.setSchool_name("");*/
        if (TextUtil.isEmpty(name)) {
            showSnackbarError("Please enter the name");
            return;
        }  else if(TextUtil.isEmpty(state)){
            showSnackbarError("Please enter the State");
        }else if(TextUtil.isEmpty(distict)){
            showSnackbarError("Please enter the District");
        }else {
            handleProgress(0, "");
            viewModel.updateTeacherProfileData(teacherReqModel);
        }
    }

    private void observeServiceResponse() {
        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case LOADING:
                    break;
                case SUCCESS:
                   /* if (responseApi.apiTypeStatus == UPDATE_STUDENT) {
                        GetStudentUpdateProfile getStudentUpdateProfile = (GetStudentUpdateProfile) responseApi.data;
                        startDashboardActivity();
                        handleProgress(1, "");
                    }*/
                    if (responseApi.apiTypeStatus == Status.UPDATE_TEACHER_PROFILE_API) {
                        handleProgress(1,"");
                        setPreference();
                    }
                    break;

                case NO_INTERNET:
                    AppLogger.v("Profile","Step1");
                    break;
                case AUTH_FAIL:
                    AppLogger.v("Profile","Step2");
                    break;
                case FAIL_400:
                    handleProgress(2, (String) responseApi.data);
                    break;
                /*For state list*/
                case STATE_LIST_ARRAY:

                    stateDataModelList = (List<StateDataModelNew>) responseApi.data;
                    AppLogger.v("StatePradeep",stateDataModelList.size()+"   -----> ListState");
             //       addDropDownState();




                      /*  if (!TextUtil.isEmpty(stateCode)) {
                            stateSpinner(stateCode);
                            stateCode = "";
                        } else {
                            stateSpinner(stateCode);
                        }*/

                    break;

                case DISTRICT_LIST_DATA:

                        districtDataModels = (List<DistrictDataModel>) responseApi.data;
                        AppLogger.v("StatePradeep",districtDataModels.size()+"   -----> ListDistrict");
                        addDropDownDistict();

                     /*   if (!TextUtil.isEmpty(districtCode)) {
                            districtSpinner(districtCode);
                            districtCode = "";
                        } else {
                            districtSpinner(districtCode);
                        }*/


                    break;

                default:
                    handleProgress(2, (String) responseApi.data);
                    break;
            }

        });
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
            callingStudentUpdateProfile();
        }

    }


    private void askPermission() {
        String rationale ="For Upload Profile Picture. Camera and Storage Permission is Must.";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");
        Permissions.check(this, PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                ImagePicker.with(TeacherProfileActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
                ViewUtil.showSnackBar(binding.getRoot(), rationale);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.e("StudentProfile", "fragment requestCode=" + requestCode)    ;

        if (requestCode == 2404) {
            // CropImages.ActivityResult result = CropImages.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri=data.getData();
                    AppLogger.e("StudentProfile", "image path=" + uri.getPath());

                    Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
                    byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
                    long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
                    int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));

                    AppLogger.e("StudentProfile", "image size=" + uri.getPath());
                    if (file_size >= 500) {
                      //  studentProfileModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
                    } else {
                       // studentProfileModel.setImageBytes(bytes);
                    }
                  //  int new_file_size = Integer.parseInt(String.valueOf(studentProfileModel.getImageBytes().length / 1024));
                  //  AppLogger.d(TAG, "Image Path  new Size kb- " + mb + "-bytes-" + new_file_size);


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
    }

    private void loadimage(Bitmap picBitmap) {
        RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(binding.profileimage.getContext().getResources(), picBitmap);
        circularBitmapDrawable.setCircular(true);
        binding.profileimage.setImageDrawable(circularBitmapDrawable);
        binding.editImage.setVisibility(View.VISIBLE);
    }

    private void startDashboardActivity() {
        Intent i = new Intent(this, DashBoardMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }



    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()){
            case STATE_WISE:

                StateDataModelNew stateDataModel = (StateDataModelNew) commonDataModel.getObject();
                binding.etState.setText(stateDataModel.getStateName());
                stateCode = stateDataModel.getStateId();
                viewModel.getStateDistrictData(stateDataModel.getStateId());
                binding.tlDistict.setVisibility(View.VISIBLE);

                AppLogger.v("StatePradeep","addDropDownState  "+stateDataModel.getStateId());
                break;
            case DISTRICT:
                DistrictDataModel districtDataModel = (DistrictDataModel) commonDataModel.getObject();
                binding.etDistict.setText(districtDataModel.getDistrict_name());
                districtCode = districtDataModel.getDistrict_code();
                AppLogger.v("StatePradeep","addDropDownDistict    "+districtDataModel.getDistrict_code());
                //viewModel.getStateDistrictData(stateDataModel.getState_code());
                break;
        }
    }

    private void openTeacherActivity() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AuroScholarDataModel auroScholarDataModel = new AuroScholarDataModel();
        auroScholarDataModel.setMobileNumber(prefModel.getUserMobile());
        if (prefModel.getDynamiclinkResModel() != null && !TextUtil.isEmpty(prefModel.getDynamiclinkResModel().getSource())) {
            auroScholarDataModel.setRegitrationSource(prefModel.getDynamiclinkResModel().getSource());
        } else {
            auroScholarDataModel.setRegitrationSource("AuroScholr");
        }
        auroScholarDataModel.setShareType("teacher");
        auroScholarDataModel.setShareIdentity("AuroScholr");
        auroScholarDataModel.setActivity(this);
        auroScholarDataModel.setReferralLink("");
        auroScholarDataModel.setEmailVerified(true);
        auroScholarDataModel.setFragmentContainerUiId(R.id.home_container);
        auroScholarDataModel.setSdkcallback(new SdkCallBack() {
            @Override
            public void callBack(String message) {
                /*Api response here*/

            }

            @Override
            public void logOut() {
                AppLogger.e("Chhonker", "Logout");
                AuroAppPref.INSTANCE.clearPref();
                Intent intent = new Intent(TeacherProfileActivity.this, SplashScreenAnimationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();
            }

            @Override
            public void commonCallback(Status status, Object o) {
                switch (status) {
                    case BOOK_TUTOR_SESSION_CLICK:
                        AppLogger.e("Chhonker", "commonCallback");
                        break;
                }
            }
        });
     //   AuroScholar.startTeacherSDK(auroScholarDataModel);

    }


    public void setUiOnScreen(){
        binding.rpTeacherprofile.setText("Teacher Profile");
        binding.profileImage.setVisibility(View.GONE);
        binding.tlGender.setVisibility(View.GONE);
        binding.tiFullName.setHint("Teacher Full Name");
        binding.tlPhoneNumber.setHint("Teacher Phone Number");
        binding.tlGender.setHint("Teacher Gender");
    }
    private void setPreference() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setTeacherProfileScreen(false);
        AuroAppPref.INSTANCE.setPref(prefModel);
        openTeacherActivity();

    }
}
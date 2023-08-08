package com.auro.application.teacher.presentation.view.fragment;

import static android.app.Activity.RESULT_OK;
import static com.auro.application.core.common.Status.GET_PROFILE_TEACHER_API;
import static com.auro.application.core.common.Status.UPDATE_TEACHER_PROFILE_API;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.ValidationModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.FragmentTeacherNewprofileBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.StateDataModelNew;
import com.auro.application.home.presentation.view.activity.SplashScreenAnimationActivity;
import com.auro.application.home.presentation.view.adapter.DistrictSpinnerUserAdapter;
import com.auro.application.home.presentation.view.adapter.StateSpinnerAdapter;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.request.TeacherReqModel;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.data.model.response.School;
import com.auro.application.teacher.presentation.view.adapter.ProfileScreenAdapter;
import com.auro.application.teacher.presentation.view.adapter.SchoolSpinnerAdapter;
import com.auro.application.teacher.presentation.viewmodel.TeacherProfileViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringTeacherDynamic;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;


public class TeacherUserProfileFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnTouchListener, View.OnClickListener, CommonCallBackListner{

    @Inject
    @Named("TeacherUserProfileFragment")
    ViewModelFactory viewModelFactory;
    FragmentTeacherNewprofileBinding binding;
    TeacherProfileViewModel viewModel;
    TeacherReqModel teacherProfileModel = new TeacherReqModel();
    String TAG = TeacherUserProfileFragment.class.getSimpleName();
    List<StateDataModelNew> stateDataModelList;
    List<DistrictDataModel> districtDataModels;
    List<School> schoolDataList;
    String stateCode = "";
    String districtCode = "";
    String schoolName = "";
    String gender = "" ;
    boolean isStateRestore;
    ProfileScreenAdapter mProfileClassAdapter;
    ProfileScreenAdapter mProfileSubjectAdapter;
    MyProfileResModel model;
    HashMap<String, String> subjectHashmap = new HashMap<>();
    HashMap<String, String> classHashmap = new HashMap<>();
    public TeacherUserProfileFragment() {
        // Required empty public constructor
    }
    List<String> genderLines;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (binding != null) {
            isStateRestore = true;
            return binding.getRoot();
        }
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TeacherProfileViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setListener();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void init() {
        // setListener();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        binding.etPhoneNumber.setText(prefModel.getUserMobile());
        viewModel.getStateListData();
        viewModel.getDistrictListData();
        setRecycleView();
        // addDropDownGender();
        handleProgress(0, "");
        AppLogger.v("GetProfiler", "Step -- 1 -- ");
        callGetTeacherApi();
        AppStringTeacherDynamic.setTeacherUserProfileStrings(binding);
    }

    void callGetTeacherApi()
    {

        viewModel.getTeacherProfileData(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    protected void setListener() {
        binding.skipForNow.setOnClickListener(this);
        binding.nextButton.setOnClickListener(this);
        binding.profileImage.setOnClickListener(this);
        binding.editImage.setOnClickListener(this);
        binding.etGenderDrop.setOnFocusChangeListener(this);
        binding.etGenderDrop.setOnTouchListener(this);
        binding.etState.setOnFocusChangeListener(this);
        binding.etState.setOnTouchListener(this);
        binding.etDistict.setOnFocusChangeListener(this);
        binding.etDistict.setOnTouchListener(this);
        binding.tlDistict.setVisibility(View.GONE);
        binding.saveImagebutton.setOnClickListener(this);
        binding.logout.setOnClickListener(this);
        binding.etSchoolName.setOnFocusChangeListener(this);
        binding.etSchoolName.setOnTouchListener(this);


        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_teacher_newprofile;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        AppLogger.v("Pradeep", "CLICK LISNER " + v.getId());
        if (hasFocus) {
            if (v.getId() == R.id.etGenderDrop) {
                binding.etGenderDrop.showDropDown();
            } else if (v.getId() == R.id.etState) {
                binding.etState.showDropDown();
            } else if (v.getId() == R.id.etDistict) {
                binding.etDistict.showDropDown();
            } else if (v.getId() == R.id.etSchoolName) {
                binding.etSchoolName.showDropDown();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // binding.etGender.showDropDown();
        AppLogger.v("Pradeep", "CLICK LISNER " + v.getId());
        if (v.getId() == R.id.etGenderDrop) {
            binding.etGenderDrop.showDropDown();
        } else if (v.getId() == R.id.etState) {
            binding.etState.showDropDown();
        } else if (v.getId() == R.id.etDistict) {
            binding.etDistict.showDropDown();
        } else if (v.getId() == R.id.etSchoolName) {
            binding.etSchoolName.showDropDown();
        }

        return false;
    }

    public void callingStudentUpdateProfile() {


        //652585
        teacherProfileModel.setUser_id(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        teacherProfileModel.setMobile_no(binding.etPhoneNumber.getText().toString());
        teacherProfileModel.setTeacher_name(binding.etFullName.getText().toString());
        teacherProfileModel.setEmail_id(binding.etEmailNumber.getText().toString());
        teacherProfileModel.setGender(binding.etGenderDrop.getText().toString());
        teacherProfileModel.setSchool_id(schoolName);
        teacherProfileModel.setState_id(stateCode);
        teacherProfileModel.setDistrict_id(districtCode);
        AppLogger.v("SaveProfile",schoolName+"school -- state id"+stateCode+" -- district"+districtCode);
        ValidationModel validationModel = viewModel.teacherUseCase.checkTeacherProfileValidation(teacherProfileModel);
        if (validationModel.isStatus()) {
            AppLogger.v("InfoScreen", "Step 1 Calling api update teacherprofile ");
            handleProgress(4, "");
            viewModel.updateTeacherProfileData(teacherProfileModel);
        } else {
            showSnackbarError(validationModel.getMessage());
        }
    }

    private void observeServiceResponse() {
        try {
            AppLogger.v("InfoScreen", "Step 6 observeServiceResponse");
            viewModel.serviceLiveData().observeForever(responseApi -> {
                AppLogger.v("InfoScreen", "Step 7 observeServiceResponse");
                switch (responseApi.status) {
                    case SUCCESS:
                        AppLogger.v("InfoScreen", "Step 8 observeServiceResponse");
                        if (responseApi.apiTypeStatus == UPDATE_TEACHER_PROFILE_API) {
                            AppLogger.v("InfoScreen", "Step 9 observeServiceResponse");
                            handleProgress(5, "");
                            callGetTeacherApi();
                            // model = (MyProfileResModel) responseApi.data;

                            //setDataOnUi();
                        } else if (responseApi.apiTypeStatus == GET_PROFILE_TEACHER_API) {
                            AppLogger.v("InfoScreen", "Step 10 observeServiceResponse");
                            handleProgress(1, "");
                            model = (MyProfileResModel) responseApi.data;
                            setDataOnUi();
                            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                            prefModel.setTeacherProfileResModel(model);
                            AuroAppPref.INSTANCE.setPref(prefModel);
                        }
                        break;

                    case NO_INTERNET:
                    case AUTH_FAIL:
                    case FAIL_400:
                        AppLogger.v("InfoScreen", "Step 11 observeServiceResponse");
                        handleProgress(6, (String) responseApi.data);
                        break;
                    /*For state list*/
                    case STATE_LIST_ARRAY:

                        stateDataModelList = (List<StateDataModelNew>) responseApi.data;
                        AppLogger.v("InfoScreen", "Step 12 observeServiceResponse");
            //            addDropDownState();

                        break;

                    case DISTRICT_LIST_DATA:

                        districtDataModels = (List<DistrictDataModel>) responseApi.data;
                        AppLogger.v("InfoScreen", "Step 13 observeServiceResponse");
                        districtCode = String.valueOf(districtDataModels.get(0).getDistrict_code());
                        binding.etDistict.setText(districtDataModels.get(0).getDistrict_name());
                        addDropDownDistict();

                        break;

                    default:
                        AppLogger.v("InfoScreen", "Step 14 observeServiceResponse");
                        handleProgress(6, (String) responseApi.data);
                        break;
                }

            });
        } catch (Exception e) {
            AppLogger.v("InfoScreen", "BUG  --- " + e.getMessage());
        }

    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void showSnackbarError(String message, int color) {
        ViewUtil.showSnackBar(binding.getRoot(), message, color);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.editImage || id == R.id.profile_image) {
            askPermission();
        } else if (id == R.id.skip_for_now) {//startDashboardActivity();
        } else if (id == R.id.nextButton) {
        } else if (id == R.id.saveImagebutton) {
            callingStudentUpdateProfile();
        } else if (id == R.id.logout) {
            AppLogger.v("TeacherProfile", "Step 1");
            openTeacherLogoutDialog();
        }

    }

    private void openTeacherLogoutDialog() {
        AppLogger.v("TeacherProfile","Step 2");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String yes=getActivity().getString(R.string.yes);
        String no=getActivity().getString(R.string.no);
        builder.setMessage(getString(R.string.sure_to_logout));
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                yes=details.getYes();
                no=details.getNo();
                builder.setMessage(details.getSureToLogout());
            }
        } catch (Exception e) {
            AppLogger.e("Excdeption", e.getMessage());
        }

        // Set the alert dialog yes button click listener
        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>"+yes+"</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when user clicked the Yes button
                // Set the TextView visibility GONE
                // tv.setVisibility(View.GONE);
                dialog.dismiss();
                logout();
                AppUtil.myClassRoomResModel = null;
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>"+no+"</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when No button clicked
                dialog.dismiss();
                     /*   Toast.makeText(getApplicationContext(),
                                "No Button Clicked",Toast.LENGTH_SHORT).show();*/
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();

    }

    private void logout() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.v("TeacherProfile","Step 5");
        AuroAppPref.INSTANCE.clearPref();
        Intent intent = new Intent(getActivity(), SplashScreenAnimationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finishAffinity();

    }

    private void askPermission() {
        String rationale = "For Upload Profile Picture. Camera and Storage Permission is Must.";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");
        Permissions.check(getContext(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                ImagePicker.with(TeacherUserProfileFragment.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
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
        AppLogger.e("StudentProfile", "fragment requestCode=" + requestCode);

        if (requestCode == 2404) {
            // CropImages.ActivityResult result = CropImages.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    AppLogger.e("StudentProfile", "image path=" + uri.getPath());

                    Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
                    byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
                    long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
                    int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));

                    AppLogger.e("StudentProfile", "image size=" + uri.getPath());
                    if (file_size >= 500) {
                        teacherProfileModel.setTeacher_profile_pic(AppUtil.encodeToBase64(picBitmap, 50));
                    } else {
                        teacherProfileModel.setTeacher_profile_pic(bytes);
                    }
                    int new_file_size = Integer.parseInt(String.valueOf(teacherProfileModel.getTeacher_profile_pic().length / 1024));
                    AppLogger.d(TAG, "Image Path  new Size kb- " + mb + "-bytes-" + new_file_size);


                    loadimage(picBitmap);
                } catch (Exception e) {
                    AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showSnackbarError(ImagePicker.getError(data));
            } else {
                Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
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

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case STATE_WISE:

                StateDataModelNew stateDataModel = (StateDataModelNew) commonDataModel.getObject();
                binding.etState.setText(stateDataModel.getStateName());
                stateCode = stateDataModel.getStateId();
                viewModel.getStateDistrictData(stateDataModel.getStateId());
                binding.tlDistict.setVisibility(View.VISIBLE);

                AppLogger.v("StatePradeep", "addDropDownState  " + stateDataModel.getStateId());
                break;
            case DISTRICT:
                DistrictDataModel districtDataModel = (DistrictDataModel) commonDataModel.getObject();
                binding.etDistict.setText(districtDataModel.getDistrict_name());
                districtCode = districtDataModel.getDistrict_code();
                AppLogger.v("StatePradeep", "addDropDownDistict    " + districtDataModel.getDistrict_code());
                //viewModel.getStateDistrictData(stateDataModel.getState_code());
                break;
            case SCHOOL_ID:
                School schoolModel = (School) commonDataModel.getObject();
                binding.etSchoolName.setText(schoolModel.getSchoolName());
                // districtCode = districtDataModel.getDistrict_code();
                schoolName = String.valueOf(schoolModel.getSchoolId());
                AppLogger.v("StatePradeep", "SchoolName    " + schoolModel.getSchoolName());

                break;
        }
    }

    private void handleProgress(int status, String message) {
        switch (status) {
            case 0:

                binding.mainParentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressbarLayout.pgbar.setVisibility(View.VISIBLE);
               // binding.shimmerMyClassroom.startShimmer();
                break;

            case 1:
                binding.mainParentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressbarLayout.pgbar.setVisibility(View.GONE);
               // binding.shimmerMyClassroom.stopShimmer();
                break;
            case 2:
                binding.progressbarLayout.pgbar.setVisibility(View.VISIBLE);
                break;
            case 3:
                binding.progressbarLayout.pgbar.setVisibility(View.GONE);
                break;
            case 4:
                binding.button.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
                break;
            case 5:
                binding.button.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                showSnackbarError(getString(R.string.saved), Color.parseColor("#4bd964"));

                break;
            case 6:
                showSnackbarError(message);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.mainParentLayout.setVisibility(View.GONE);
                binding.progressbarLayout.pgbar.setVisibility(View.GONE);
              //  binding.shimmerMyClassroom.stopShimmer();
                binding.errorLayout.textError.setText(message);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.getTeacherProfileData(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
                    }
                });
                break;
        }

    }

    public void setRecycleView() {
        //for class recycleview
        GridLayoutManager gridlayout = new GridLayoutManager(getActivity(), 2);
        gridlayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recycleViewclass.setLayoutManager(gridlayout);
        binding.recycleViewclass.setHasFixedSize(true);
        binding.recycleViewclass.setNestedScrollingEnabled(false);
        mProfileClassAdapter = new com.auro.application.teacher.presentation.view.adapter.ProfileScreenAdapter(viewModel.teacherUseCase.selectClass(""), getContext(), this);
        binding.recycleViewclass.setAdapter(mProfileClassAdapter);

        //for subject recycle view
        GridLayoutManager gridlayout2 = new GridLayoutManager(getActivity(), 2);
        gridlayout2.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recycleViewsubject.setLayoutManager(gridlayout2);
        binding.recycleViewsubject.setHasFixedSize(true);
        binding.recycleViewsubject.setNestedScrollingEnabled(false);
        mProfileSubjectAdapter = new ProfileScreenAdapter(viewModel.teacherUseCase.selectSubject(""), getContext(), this);
        binding.recycleViewsubject.setAdapter(mProfileSubjectAdapter);
    }

    public void setDataOnUi() {
        binding.etFullName.setText(model.getTeacherName().toString() == null ? "" : model.getTeacherName().toString());
        binding.etPhoneNumber.setText(model.getMobileNo().toString() == null ? "" : model.getMobileNo().toString());
        binding.etEmailNumber.setText(model.getEmailId());
        binding.etGenderDrop.setText(model.getGender());
        schoolDataList = model.getSchools();
        try {
            addDropDownSchool();
     //       addDropDownState();
            addDropDownGender();
        } catch (Exception e) {
            AppLogger.v("SchoolIdCheck", e.getMessage());
        }
        if (!TextUtil.isEmpty(model.getTeacherProfilePic())) {
            ImageUtil.loadCircleImage(binding.profileimage, model.getTeacherProfilePic());
        }

        // binding.etSchoolName.setText(model.);
    }

    public void addDropDownGender() {
        genderLines = Arrays.asList(getResources().getStringArray(R.array.genderlist_profile));
        AppLogger.v("Teacher_profile_pradeep","Gender "+genderLines);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, genderLines);
        //binding.etGenderDrop.setThreshold(1);//will start working from first character
        binding.etGenderDrop.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etGenderDrop.setTextColor(Color.BLACK);


        if (model != null && !TextUtil.isEmpty(model.getGender())) {
            AppLogger.v("InfoLog","Step 2 Gender"+model.getGender());
            gender = model.getGender();
            AppLogger.v("InfoLog","Step 2.1 Gender  "+gender);
            AppLogger.v("Teacher_profile_pradeep","Gender "+genderLines.size());
            for (int i = 0; i < genderLines.size(); i++) {
                AppLogger.v("SchoolIdCheck", "Step 2 gender " + genderLines.get(i).toString());

                if (gender != null && !TextUtil.isEmpty(gender) && model.getGender().equals(genderLines.get(i))) {
                     binding.etGenderDrop.setSelection(i);

                    AppLogger.v("SchoolIdCheck", "Step 3 gender " + gender);
                    break;
                }
            }
        }
    }

   /* public void addDropDownState() {
        StateSpinnerAdapter stateSpinnerAdapter = new StateSpinnerAdapter(getContext(), 0, stateDataModelList, this);

        //StateSpinnerUserAdapter stateSpinnerAdapter = new StateSpinnerUserAdapter(getContext(), 0, stateDataModelList, this);
        binding.etState.setThreshold(1);//will start working from first character
        binding.etState.setAdapter(stateSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etState.setTextColor(Color.BLACK);


        if (model != null && model.getStateId() != 0) {
            AppLogger.v("InfoLog","Step 3 State id"+model.getStateId());
            stateCode= String.valueOf(model.getStateId());
            AppLogger.v("InfoLog","Step 3.1 State id   "+stateCode);
            for (int i = 0; i < stateDataModelList.size(); i++) {

                // AppLogger.v("SchoolIdCheck","Step 2 State "+stateDataModelList.get(i).getState_name()+""+state_name != null && !TextUtil.isEmpty(state_name) && model.getDistrictId() == Integer.parseInt(stateDataModelList.get(i).getState_name()));
                if (stateCode != null && !TextUtil.isEmpty(stateCode) && stateCode.equals(stateDataModelList.get(i).getStateId())) {
                    // binding.etSchoolName.setSelection(i);
                    binding.etState.setText(stateDataModelList.get(i).getStateName());


                    stateCode = String.valueOf(model.getStateId());
                    AppLogger.v("SchoolIdCheck", "Step 3 State pradeep " + stateCode);

                    binding.tlDistict.setVisibility(View.VISIBLE);
                    viewModel.getStateDistrictData(stateDataModelList.get(i).getStateId());
                    // addDropDownDistict();


                    break;
                }

            }
        }
    }*/

    public void addDropDownDistict() {
        DistrictSpinnerUserAdapter districtSpinnerAdapter = new DistrictSpinnerUserAdapter(getContext(), 0, districtDataModels, this);
        binding.etDistict.setThreshold(1);//will start working from first character
        binding.etDistict.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etDistict.setTextColor(Color.BLACK);
        if (model != null && model.getDistrictId() != 0) {

            AppLogger.v("InfoLog","Step 4 District Code"+model.getSchoolId());
            districtCode = String.valueOf(model.getDistrictId());
            AppLogger.v("InfoLog","Step 4.1 District Code  "+districtCode);
            for (int i = 0; i < districtDataModels.size(); i++) {
                if (districtCode != null && !TextUtil.isEmpty(districtCode) && districtCode.equals(districtDataModels.get(i).getDistrict_code())) {
                    // binding.etSchoolName.setSelection(i);
                    // districtCode = districtDataModels.get(i).getDistrict_code();
                    districtCode = String.valueOf(model.getDistrictId());
                    binding.etDistict.setText(districtDataModels.get(i).getDistrict_name());
                    AppLogger.v("SchoolIdCheck", "Step 3 Distict pradeep " + districtCode);
                    break;
                }

            }
        }
    }

    public void addDropDownSchool() {

        ArrayAdapter<String> adapter = new SchoolSpinnerAdapter(getContext(), 0, schoolDataList, this);
        binding.etSchoolName.setThreshold(1);//will start working from first character
        binding.etSchoolName.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etSchoolName.setTextColor(Color.BLACK);

        if (model.getSchoolId() != 0) {
            AppLogger.v("InfoLog","Step 1 SchoolName"+model.getSchoolId());
            schoolName =String.valueOf(model.getSchoolId());
            AppLogger.v("InfoLog","Step 1.1 SchoolName"+schoolName);
            for (int i = 0; i < schoolDataList.size(); i++) {
                AppLogger.v("SchoolIdCheck", "Step 2 School " + schoolDataList.get(i).getSchoolName());
                String schoolName = schoolDataList.get(i).getSchoolName();
                if (schoolName != null && !TextUtil.isEmpty(schoolName) && model.getSchoolId() == schoolDataList.get(i).getSchoolId()) {
                    // binding.etSchoolName.setSelection(i);
                    binding.etSchoolName.setText(schoolName);
                    AppLogger.v("SchoolIdCheck", "Step 3 School " + schoolDataList.get(i).getSchoolName());
                    break;
                }

            }
        }
    }

}
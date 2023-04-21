package com.auro.application.home.presentation.view.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.common.Status;
import com.auro.application.core.common.ValidationModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.databinding.FragmentStudentProfile2Binding;
import com.auro.application.home.data.model.AddNewSchoolResModel;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.BoardData;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.DistrictData;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.GradeData;
import com.auro.application.home.data.model.GradeDataModel;
import com.auro.application.home.data.model.PrivateTutionData;
import com.auro.application.home.data.model.SchoolData;
import com.auro.application.home.data.model.SchoolDataModel;
import com.auro.application.home.data.model.SchoolLangData;
import com.auro.application.home.data.model.SchoolMediumData;
import com.auro.application.home.data.model.SchoolTypeData;
import com.auro.application.home.data.model.StateData;
import com.auro.application.home.data.model.StudentResponselDataModel;
import com.auro.application.home.data.model.TutionData;
import com.auro.application.home.data.model.response.CategorySubjectResModel;
import com.auro.application.home.data.model.response.ChildDetailResModel;
import com.auro.application.home.data.model.response.FetchStudentPrefResModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.KycDocResModel;
import com.auro.application.home.data.model.response.StudentKycStatusResModel;
import com.auro.application.home.data.model.response.SubjectPreferenceResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.presentation.view.activity.CameraActivity;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithPinActivity;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.SetPinActivity;
import com.auro.application.home.presentation.view.activity.StudentProfileActivity;
import com.auro.application.home.presentation.view.adapter.GenderSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.GradeSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.PrivateTutionSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolBoardSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolMediumSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolTypeSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SubjectPrefProfileAdapter;
import com.auro.application.home.presentation.view.adapter.TutionTypeSpinnerAdapter;
import com.auro.application.home.presentation.viewmodel.StudentProfileViewModel;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.common.StateDataModel;

import com.auro.application.teacher.presentation.view.adapter.DistrictSpinnerAdapter;
import com.auro.application.teacher.presentation.view.adapter.StateSpinnerAdapter;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.DeviceUtil;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.alert_dialog.CustomProgressDialog;
import com.auro.application.util.alert_dialog.LanguageChangeDialog;
import com.auro.application.util.alert_dialog.disclaimer.AddStudentDialog;
import com.auro.application.util.cropper.CropImages;
import com.auro.application.util.firebaseAnalytics.AnalyticsRegistry;
import com.auro.application.util.permission.LocationHandler;
import com.auro.application.util.permission.LocationModel;
import com.auro.application.util.permission.LocationUtil;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringDynamic;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.auro.application.core.common.Status.BOARD;
import static com.auro.application.core.common.Status.CHECKVALIDUSER;
import static com.auro.application.core.common.Status.DISTRICT;
import static com.auro.application.core.common.Status.FETCH_STUDENT_PREFERENCES_API;
import static com.auro.application.core.common.Status.GENDER;
import static com.auro.application.core.common.Status.GET_USER_PROFILE_DATA;
import static com.auro.application.core.common.Status.GRADE;
import static com.auro.application.core.common.Status.REGISTER_CALLBACK;
import static com.auro.application.core.common.Status.SCHHOLMEDIUM;
import static com.auro.application.core.common.Status.SCHHOLTYPE;
import static com.auro.application.core.common.Status.SCHOOL;
import static com.auro.application.core.common.Status.SCREEN_TOUCH;
import static com.auro.application.core.common.Status.STATE;
import static com.auro.application.core.common.Status.SUBJECT_PREFRENCE_LIST_API;
import static com.auro.application.core.common.Status.TUTION;
import static com.auro.application.core.common.Status.TUTIONTYPE;
import static com.auro.application.core.common.Status.UPDATE_STUDENT;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentProfileFragment extends BaseFragment implements View.OnClickListener, TextWatcher, View.OnTouchListener, View.OnFocusChangeListener, CommonCallBackListner, GradeChangeFragment.OnClickButton {
    @Inject
    @Named("StudentProfileFragment")
    ViewModelFactory viewModelFactory;
    FragmentStudentProfile2Binding binding;
    StudentProfileViewModel viewModel;
    String getkycstatus;
    DashboardResModel dashboardResModel;
    List<GenderData> genderList = new ArrayList<>();
    List<String> genderListString = new ArrayList<>();
    List<TutionData> tutiontypeList = new ArrayList<>();
    List<PrivateTutionData> privatetutionList = new ArrayList<>();
    List<SchoolTypeData> schooltypeList = new ArrayList<>();
    List<StateData> statelist = new ArrayList<>();
    List<BoardData> boardlist = new ArrayList<>();
    List<SchoolLangData> mediumlist = new ArrayList<>();
    List<GradeData> gradelist = new ArrayList<>();
    private String comingFrom;
    GetStudentUpdateProfile studentProfileModel = new GetStudentUpdateProfile();
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    GetStudentUpdateProfile getStudentUpdateProfile;
    CommonCallBackListner commonCallBackListner;
    boolean isUserEditFieldOpen = true;
    LocationHandler locationHandlerUpdate;
    CustomProgressDialog customProgressDialog;
    boolean firstTimeCome;
    RequestBody email_c;
    String TAG = "StudentProfileFragment";
    PrefModel prefModel;
    private static final int CAMERA_REQUEST = 1888;
    AuroScholarInputModel inputModel;
    List<StateDataModel> stateDataModelList;
    List<DistrictDataModel> districtDataModels;
    String stateCode = "";
    String districtCode = "";
    String Schoolsearch = "";
    String fbnewToken = "Test123";
    String fullname,studentemail,filename,image_path,SchoolName,gradeid,state_Code,district_code;
    String gender_pass,tutiontype_pass,tution_pass,schoolmedium_pass,board_pass, final_state_id,final_district_id,f_state,f_district;
    RequestBody lRequestBody;
    String schooltype_pass = "";
    String GenderName="";
    String Tutiontype="";
    String schooltype="";
    String boardtype="";
    String Schoolmedium="";
    String Tution="";
    String getschool_id;
    String schoolnameprofile;
    RequestBody  schoolname;
    MultipartBody.Part lFile;
    StudentKycStatusResModel studentKycStatusResModel;
    Details details;
    List<SchoolData> districtList = new ArrayList<>();


    public StudentProfileFragment() {
        // Required empty public constructor
    }


    public static StudentProfileFragment newInstance(String param1, String param2) {
        StudentProfileFragment fragment = new StudentProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
//        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        DaggerWrapper.getComponent(getActivity()).doInjection(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StudentProfileViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(),  new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                fbnewToken = instanceIdResult.getToken();
//                Log.v("fbtoken", fbnewToken);
//
//            }
//        });
        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        init();
        // getChildProfile();
        setListener();
        setToolbar();



        binding.autoCompleteTextView1.setAdapter(getEmailAddressAdapter(getActivity()));
        binding.autoCompleteTextView1.setThreshold(1);
        binding.autoCompleteTextView1.setTextColor(Color.BLACK);
        binding.autoCompleteTextView1.setDropDownBackgroundResource(R.color.white);

        return binding.getRoot();

    }

    @Override
    protected void init() {
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String language_id = prefModel.getUserLanguageId();
        firstTimeCome = true;
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.STUDENT_PROFILE_FRAGMENT);
        AppLogger.e("setDashboardResModelToPref--5", "step 1");


        if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
            comingFrom = getArguments().getString(AppConstant.COMING_FROM);

        }

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
        //callSubjectListPreference();
       // observeServiceResponse();





        callingStudentUpdateProfile();

        AppUtil.commonCallBackListner = this;


        checkYCApprovedOrNot();
        AppUtil.loadAppLogo(binding.auroScholarLogo, getActivity());



        binding.editUsername.setText(AuroAppPref.INSTANCE.getModelInstance().getUserName());
        binding.editProfile.setText(AuroAppPref.INSTANCE.getModelInstance().getStudentName());
        binding.editUserid.setText(AuroAppPref.INSTANCE.getModelInstance().getUserId());
        String studentname = AuroAppPref.INSTANCE.getModelInstance().getStudentName();
        binding.UserName.setText(studentname);


        getGrade();
        getAllStateList();
        getGender();
        getTutiontype();
        getPrivatetype();
        getSchooltype();
        getBoardtype();
        getSchoolmedium(language_id);

        AppStringDynamic.setStudentProfilePageStrings(binding);




        binding.etstate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    binding.etstate.showDropDown();
                }
            }
        });
        binding.etstate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.etstate.showDropDown();
                return false;
            }
        });
        binding.etdistrict.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    binding.etdistrict.showDropDown();
                }
            }
        });
        binding.etdistrict.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.etdistrict.showDropDown();
                return false;
            }
        });
        binding.etSchoolname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (districtList!=null||!districtList.isEmpty()){
                    binding.etSchoolname.showDropDown();

                    addDropDownSchool(districtList);
                }


                return false;
            }
        });

        binding.etSchoolname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    if (districtList!=null||!districtList.isEmpty()){
                        binding.etSchoolname.showDropDown();

                        addDropDownSchool(districtList);
                    }


                }
            }
        });
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(true);
                observeServiceResponse();
            }
        });
        Details details1 = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();








    }

    private void checkForAddStudentVisibility() {

        if (studentKycStatusResModel != null && studentKycStatusResModel.getKycStatus().equalsIgnoreCase(AppConstant.DocumentType.APPROVE)) {
            CheckUserResModel checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel();
            int count = 0;
            for (UserDetailResModel model : checkUserResModel.getUserDetails()) {
                if (model.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_STUDENT)) {
                    count++;
                }
            }
            if (checkUserResModel.getUserDetails().size()<=5) {
                setvisibilityAddStudent(false);
            } else {
                setvisibilityAddStudent(false);
            }

        } else {
            setvisibilityAddStudent(false);
        }


    }

    void callGetStudentKycStatus() {
        AppLogger.e("callGetStudentKycStatus-","Step 1");
        CheckUserResModel checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel();
        if (checkUserResModel.getUserDetails().size() <3 ) {
            AppLogger.e("callGetStudentKycStatus-","Step 2");
            viewModel.checkInternetForApi(Status.STUDENT_KYC_STATUS_API, "");
        }
    }

    void setvisibilityAddStudent(boolean status) {
        if (status) {
            binding.switchProfile.setVisibility(View.VISIBLE);
            binding.RPTextView9.setVisibility(View.VISIBLE);
        } else {
            binding.switchProfile.setVisibility(View.GONE);
            binding.RPTextView9.setVisibility(View.GONE);
        }

    }


    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        Details details1 = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.STUDENT_PROFILE_FRAGMENT);
        binding.languageLayout.setOnClickListener(this);
        binding.profileImage.setOnClickListener(this);
        binding.editImage.setOnClickListener(this);
        binding.editemail.addTextChangedListener(this);
        binding.submitbutton.setOnClickListener(this);
        binding.UserName.setOnClickListener(this);
        binding.cancelUserNameIcon.setOnClickListener(this);

        binding.gradeChnage.setOnClickListener(this);
        binding.walletBalText.setOnClickListener(this);
        binding.editemail.setOnClickListener(this);
        binding.inputemailedittext.setOnClickListener(this);
        binding.tilteachertxt.setOnClickListener(this);
        binding.SpinnerGender.setOnTouchListener(this);
        binding.SpinnerSchoolType.setOnTouchListener(this);
        binding.SpinnerBoard.setOnTouchListener(this);
        binding.SpinnerLanguageMedium.setOnTouchListener(this);
        binding.spinnerPrivateTution.setOnTouchListener(this);
        binding.spinnerPrivateType.setOnTouchListener(this);
        binding.gradeLayout.setOnTouchListener(this);
        binding.linearLayout8.setOnTouchListener(this);
        binding.editUserNameIcon.setOnClickListener(this);
        binding.editPhone.setOnTouchListener(this);
        binding.editemail.setOnTouchListener(this);
        binding.editSubjectIcon.setOnClickListener(this);
        binding.switchProfile.setOnClickListener(this);
        binding.etdistrict.setOnFocusChangeListener(this);
        binding.etdistrict.setOnTouchListener(this);
        binding.etgrade.setOnFocusChangeListener(this);
        binding.etgrade.setOnTouchListener(this);

        binding.ettution.setOnFocusChangeListener(this);
        binding.ettution.setOnTouchListener(this);
        binding.ettutiontype.setOnFocusChangeListener(this);
        binding.ettutiontype.setOnTouchListener(this);
        binding.etStudentGender.setOnFocusChangeListener(this);
        binding.etStudentGender.setOnTouchListener(this);
        binding.etSchoolmedium.setOnFocusChangeListener(this);
        binding.etSchoolmedium.setOnTouchListener(this);
        binding.etSchooltype.setOnFocusChangeListener(this);
        binding.etSchooltype.setOnTouchListener(this);
        binding.etSchoolboard.setOnFocusChangeListener(this);
        binding.etSchoolboard.setOnTouchListener(this);



        binding.SpinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (studentProfileModel != null) {
                    studentProfileModel.setGender(binding.SpinnerGender.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                changeTheEditText();
            }
        });

        binding.SpinnerSchoolType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (studentProfileModel != null) {
                    studentProfileModel.setSchoolType(binding.SpinnerSchoolType.getSelectedItem().toString());
                }
                changeTheEditText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                changeTheEditText();
            }
        });



        binding.SpinnerLanguageMedium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (studentProfileModel != null) {
                    studentProfileModel.setLanguage(binding.SpinnerLanguageMedium.getSelectedItem().toString());
                }
                changeTheEditText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                changeTheEditText();
            }
        });

        binding.SpinnerBoard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (studentProfileModel != null) {
                    studentProfileModel.setBoardType(binding.SpinnerBoard.getSelectedItem().toString());
                }
                changeTheEditText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                changeTheEditText();
            }
        });
        binding.addnewschool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.autoCompleteTextView1.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter school name", Toast.LENGTH_SHORT).show();
                }
                else if (binding.autoCompleteTextView1.getText().toString().startsWith(" ")){
                    Toast.makeText(getActivity(), details1.getEnter_space_schoolname(), Toast.LENGTH_SHORT).show();
                }
                else{
                    String search = binding.autoCompleteTextView1.getText().toString();
                    binding.etSchoolname.setText(search);
                    addNewSchool(f_state,f_district,search);
                }
            }
        });

        binding.spinnerPrivateTution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String value = binding.spinnerPrivateTution.getSelectedItem().toString();
                studentProfileModel.setIsPrivateTution(binding.spinnerPrivateTution.getSelectedItem().toString());
                changeTheEditText();
                if (value.equalsIgnoreCase(AppConstant.DocumentType.YES)) {
                    binding.spinnerPrivateType.setVisibility(View.VISIBLE);
                    binding.tvPrivateType.setVisibility(View.VISIBLE);
                    binding.privateTypeArrow.setVisibility(View.VISIBLE);


                } else {
                    binding.spinnerPrivateType.setVisibility(View.GONE);
                    binding.tvPrivateType.setVisibility(View.GONE);
                    binding.privateTypeArrow.setVisibility(View.GONE);
                    studentProfileModel.setPrivateTutionType("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                changeTheEditText();
            }
        });

        binding.spinnerPrivateType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String value = binding.spinnerPrivateType.getSelectedItem().toString();
                if (value.equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_PRIVATE_TUTION)) {
                    studentProfileModel.setPrivateTutionType("");
                    changeTheEditText();

                } else {
                    studentProfileModel.setPrivateTutionType(binding.spinnerPrivateType.getSelectedItem().toString());
                    changeTheEditText();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                changeTheEditText();
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_student_profile_2;
    }

    @Override
    public void onResume() {
        super.onResume();
       /* init();
        setListener();*/
    }
    private void addNewSchool(String state_id, String district_id, String search)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle(details.getProcessing());
        progress.setMessage(details.getProcessing());
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();

        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("school_name", search);
        map_data.put("state_id",state_id);
        map_data.put("district_id",district_id);

        RemoteApi.Companion.invoke().addNewSchool(map_data)
                .enqueue(new Callback<AddNewSchoolResModel>() {
                    @Override
                    public void onResponse(Call<AddNewSchoolResModel> call, Response<AddNewSchoolResModel> response) {
                        if (response.code()==400){
                            progress.dismiss();
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.getString("message");
                                Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        }

                        else if (response.isSuccessful()) {

                            progress.dismiss();
                            getschool_id = String.valueOf(response.body().getSchoolID());

                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();



                        }
                        else{
                            progress.dismiss();
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddNewSchoolResModel> call, Throwable t)
                    {
                        progress.dismiss();
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        //init();
        //  setListener();
    }




    private void setSubjectAdapter(SubjectPreferenceResModel subjectPreferenceResModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        List<CategorySubjectResModel> categorySubjectResModelList = new ArrayList<>();
        FetchStudentPrefResModel fetchStudentPrefResModel = prefModel.getFetchStudentPrefResModel();
        if (fetchStudentPrefResModel != null && !TextUtil.checkListIsEmpty(fetchStudentPrefResModel.getSubjects())) {
            for (CategorySubjectResModel resModel : fetchStudentPrefResModel.getSubjects()) {
                for (CategorySubjectResModel categorySubjectResModel : subjectPreferenceResModel.getSubjects()) {
                    // AppLogger.e("setSubjectAdapter- ", "Step 2 .2-- " + categorySubjectResModel.getSubjectname());
                    if (resModel.getId().equalsIgnoreCase(categorySubjectResModel.getId())) {
                        categorySubjectResModel.setSelected(true);
                        if (resModel.getAttempted() > 0) {
                            categorySubjectResModel.setLock(true);
                        }

                    } else {
                    }
                }
            }
        }


        for (CategorySubjectResModel categorySubjectResModel : subjectPreferenceResModel.getSubjects()) {
            if (categorySubjectResModel.isSelected()) {
                categorySubjectResModelList.add(0, categorySubjectResModel);
            } else {
                categorySubjectResModelList.add(categorySubjectResModel);
            }
        }

        GridLayoutManager gridlayout2 = new GridLayoutManager(getActivity(), 4, LinearLayoutManager.HORIZONTAL, false);
        gridlayout2.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.subjectsRecyclerview.setLayoutManager(gridlayout2);
        binding.subjectsRecyclerview.setHasFixedSize(true);
        binding.subjectsRecyclerview.setNestedScrollingEnabled(false);
        SubjectPrefProfileAdapter mProfileSubjectAdapter = new SubjectPrefProfileAdapter(categorySubjectResModelList, this);
        binding.subjectsRecyclerview.setAdapter(mProfileSubjectAdapter);
    }


    private void setSubjectListVisibility(boolean status) {
        if (status) {
            binding.yourSubjectsLayout.setVisibility(View.VISIBLE);
            binding.subjectsRecyclerview.setVisibility(View.VISIBLE);
        } else {
            binding.yourSubjectsLayout.setVisibility(View.GONE);
            binding.subjectsRecyclerview.setVisibility(View.GONE);
        }
    }

    void callFetchUserPreference() {
        handleProgress(0, "");
        FetchStudentPrefReqModel fetchStudentPrefReqModel = new FetchStudentPrefReqModel();
        fetchStudentPrefReqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getUserId());
        viewModel.checkInternetForApi(FETCH_STUDENT_PREFERENCES_API, fetchStudentPrefReqModel);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.edit_subject_icon) {
            getActivity().finish();
            Intent newIntent = new Intent(getActivity(), SubjectPreferencesActivity.class);
            startActivity(newIntent);
        } else if (id == R.id.language_layout) {
            ((DashBoardMainActivity) getActivity()).openChangeLanguageDialog();
        } else if (id == R.id.back_arrow) {
            getActivity().onBackPressed();
        } else if (id == R.id.editImage) {
            if (Build.VERSION.SDK_INT > 26) {
                askPermission();
            } else {
                askPermission();
            }
        } else if (id == R.id.submitbutton) {
            String email = binding.editemail.getText().toString().trim();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            Details details1 = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
            state_Code = getStudentUpdateProfile.getStateId();

            district_code = getStudentUpdateProfile.getDistrictId();
            if (binding.editProfile.getText().toString().isEmpty() || binding.editProfile.getText().toString().equals("")) {
                Toast.makeText(getActivity(), details1.getEnter_your_name(), Toast.LENGTH_SHORT).show();

            } else if (binding.editProfile.getText().toString().startsWith(" ")) {
                Toast.makeText(getActivity(), details1.getEnter_space_name(), Toast.LENGTH_SHORT).show();
            } else if (!binding.editemail.getText().toString().isEmpty() && !binding.editemail.getText().toString().equals("") && !email.matches(emailPattern)) {

                Toast.makeText(getActivity(), details1.getPlease_enter_valid_email(), Toast.LENGTH_SHORT).show();

            } else if (binding.editemail.getText().toString().startsWith(" ")) {
                Toast.makeText(getActivity(), details1.getEnter_space_email(), Toast.LENGTH_SHORT).show();
            } else if (binding.etStudentGender.getText().toString().equals("Student Gender") || binding.etStudentGender.getText().toString().equals("Please Select Your Gender") || binding.etStudentGender.getText().toString().equals("") || binding.etStudentGender.getText().toString().equals("Select Your Gender") || binding.etStudentGender.getText().toString().equals("Please Select Gender") || binding.etStudentGender.getText().toString().equals("Select Gender") || genderList.get(0).getTranslatedName().equals(binding.etStudentGender.getText().toString())) {
                Toast.makeText(getActivity(), details1.getPlease_select_gender(), Toast.LENGTH_SHORT).show();

            } else if (binding.etstate.getText().toString().equals("") || binding.etstate.getText().toString().isEmpty() || binding.etstate.getText().toString().equals(details1.getState()) || binding.etstate.getText().toString().equals("State")) {
                Toast.makeText(getActivity(), details1.getPlease_select_state(), Toast.LENGTH_SHORT).show();

            } else if (binding.etdistrict.getText().toString().equals("") || binding.etdistrict.getText().toString().isEmpty() || binding.etdistrict.getText().toString().equals("District")) {
                Toast.makeText(getActivity(), details1.getPlease_select_district(), Toast.LENGTH_SHORT).show();

            }


            else if (binding.etSchoolname.getText().toString().isEmpty() || binding.etSchoolname.getText().toString().equals("")) {

                Toast.makeText(getActivity(), details1.getPlease_select_school(), Toast.LENGTH_SHORT).show();
            }

//

            else if (getStudentUpdateProfile.getProfilePic() == null || getStudentUpdateProfile.getProfilePic().equals("") || getStudentUpdateProfile.getProfilePic().equals("null") || getStudentUpdateProfile.getProfilePic().equals(null)) {
                if (image_path == null || image_path.equals("null") || image_path.equals("") || image_path.isEmpty()) {
                    Toast.makeText(getActivity(), details1.getUploadProfilePic(), Toast.LENGTH_SHORT).show();

                } else if (binding.editProfile.getText().toString().startsWith(" ")) {
                    Toast.makeText(getActivity(), details1.getEnter_space_name(), Toast.LENGTH_SHORT).show();
                } else {
                    changeTheEditText();
                    ((DashBoardMainActivity) getActivity()).setProgressVal();
                    updateChild();

                }
            } else {

                ((DashBoardMainActivity) getActivity()).setProgressVal();

                updateChild();

            }
        } else if (id == R.id.editUserNameIcon) {
            AppLogger.v("TextEdit", "   UserName");
            binding.UserName.setVisibility(View.GONE);
            binding.editUserNameIcon.setVisibility(View.GONE);
            binding.editProfileName.setVisibility(View.VISIBLE);
            binding.cancelUserNameIcon.setImageResource(R.drawable.ic_cancel_icon);

            binding.editProfile.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.editProfile, InputMethodManager.SHOW_IMPLICIT);
            binding.cancelUserNameIcon.setVisibility(View.VISIBLE);
        } else if (id == R.id.cancelUserNameIcon) {
            AppLogger.v("TextEdit", "   cancelUserNameIcon");
            if (!TextUtil.isEmpty(binding.editProfile.getText().toString())) {
                binding.UserName.setVisibility(View.VISIBLE);
                binding.editUserNameIcon.setVisibility(View.GONE);
                binding.editUserNameIcon.setImageResource(R.drawable.ic_edit_profile);
                binding.editProfileName.setVisibility(View.VISIBLE);
                binding.cancelUserNameIcon.setVisibility(View.GONE);
                binding.UserName.setText(binding.editProfile.getText().toString());
            } else {
                binding.editProfileName.setError("Enter Student Name");
            }
        } else if (id == R.id.gradeChnage) {
            if (!TextUtil.isEmpty(binding.editProfile.getText().toString())) {
                changeTheEditText();
                openGradeChangeFragment(AppConstant.SENDING_DATA.STUDENT_PROFILE);
            } else {

                binding.editProfileName.setError("Enter Student Name");
            }
        } else if (id == R.id.wallet_bal_text) {
            if (!TextUtil.isEmpty(binding.editProfile.getText().toString())) {
                changeTheEditText();
                getKYCStatus();

            } else {
                binding.editProfileName.setError("Enter Student Name");
            }
        } else if (id == R.id.linearLayout8) {
            if (!TextUtil.isEmpty(binding.editProfile.getText().toString())) {
                changeTheEditText();
            } else {
                binding.editProfileName.setError("Enter Student Name");
            }
        } else if (id == R.id.editPhone) {
            if (!TextUtil.isEmpty(binding.editProfile.getText().toString())) {
                changeTheEditText();
            } else {
                binding.editProfileName.setError("Enter Student Name");
            }
        } else if (id == R.id.editemail) {
            if (!TextUtil.isEmpty(binding.editProfile.getText().toString())) {
                changeTheEditText();
            } else {
                binding.editProfileName.setError("Enter Student Name");
            }
        } else if (id == R.id.tilteachertxt) {
            if (!TextUtil.isEmpty(binding.editProfile.getText().toString())) {
                changeTheEditText();
            } else {
                binding.editProfileName.setError("Enter Student Name");
            }
        } else if (id == R.id.inputemailedittext) {
            if (!TextUtil.isEmpty(binding.editProfile.getText().toString())) {
                changeTheEditText();
            } else {
                binding.editProfileName.setError("Enter Student Name");
            }
        } else if (id == R.id.switchProfile) {
            openBottomSheetDialog();
        }

    }
    private void getKYCStatus()
    {

        String suserid =  AuroAppPref.INSTANCE.getModelInstance().getUserId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",suserid);

        RemoteApi.Companion.invoke().getKYCStatus(map_data)
                .enqueue(new Callback<StudentKycStatusResModel>()
                {
                    @Override
                    public void onResponse(Call<StudentKycStatusResModel> call, Response<StudentKycStatusResModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            getkycstatus = response.body().getKycStatus();
                            String uploadedornot = response.body().getIsKycUploaded();
                            if (uploadedornot.equals("NO")||uploadedornot=="NO"){
                                ViewUtil.showSnackBar(binding.getRoot(),"Please upload your complete KYC");
                            }
                            else if (uploadedornot.equals("YES")||uploadedornot=="YES"){
                                if (getkycstatus.equals("APPROVE")){
                                    openFragment2(new WalletInfoDetailFragment());
                                }
                                else if (getkycstatus.equals("PENDING")){
                                    ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification is pending, wait till it gets verified");

                                }
                                else if (getkycstatus.equals("DISAPPROVE")){
                                    ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification has been disapprove, please reupload documents");

                                }
                                else if (getkycstatus.equals("INPROCESS")){
                                    ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification is in progress, wait till it gets verified");

                                }
                                else if (getkycstatus.equals("REJECTED")){
                                    ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification has been rejected, please reupload documents");

                                }
                                else{
                                    ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification is pending, wait till it gets verified");

                                }
                            }


                        }
                        else
                        {

                            Log.d(TAG, "onResponser: "+response.message().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<StudentKycStatusResModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }

    private void openAddStudentDialog() {
        AddStudentDialog userNotRegisterDialog = new AddStudentDialog(getActivity(), this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(userNotRegisterDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        userNotRegisterDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(userNotRegisterDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        userNotRegisterDialog.setCancelable(false);
        userNotRegisterDialog.show();
    }


    void openBottomSheetDialog() {
        BottomSheetAddUserDialog bottomSheet = new BottomSheetAddUserDialog();
        bottomSheet.show(getActivity().getSupportFragmentManager(),
                "ModalBottomSheet");
    }


    public void openGradeChangeFragment(String source) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.COMING_FROM, source);
        GradeChangeFragment gradeChangeFragment = new GradeChangeFragment(this);
        gradeChangeFragment.setArguments(bundle);
        openFragment2(gradeChangeFragment);
    }


    public void openActivity() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, AppConstant.CAMERA_REQUEST_CODE);
    }



    private void loadimage(Bitmap picBitmap) {
        RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(binding.profileimage.getContext().getResources(), picBitmap);
        circularBitmapDrawable.setCircular(true);
        binding.profileimage.setImageDrawable(circularBitmapDrawable);
        updateProfilePicChild();
    }



    private void observeServiceResponse() {
        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    if (isVisible()) {
                        if (responseApi.apiTypeStatus == UPDATE_STUDENT) {
                            GetStudentUpdateProfile studentProfileModel = new GetStudentUpdateProfile();
                            AppLogger.v("observeServiceResponse", "UPDATE_STUDENT");
                            handleSubmitProgress(1, "");
                            if (studentProfileModel.getError()) {
                                AppLogger.v("sendProfilepradeepApi", "UPDATE_STUDENT"+studentProfileModel.getMessage());
                                showSnackbarError(studentProfileModel.getMessage(), Color.RED);
                            } else {
                                AppLogger.v("sendProfilepradeepApi", "UPDATE_STUDENT");
                                showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getSuccess_fully_save() != null ? prefModel.getLanguageMasterDynamic().getDetails().getSuccess_fully_save() : AuroApp.getAppContext().getResources().getString(R.string.success_fully_save), Color.GREEN);
                            }
                            funnelProfileSubmitScreen();

                            callingStudentUpdateProfile();
                        }
                        else if (responseApi.apiTypeStatus == SUBJECT_PREFRENCE_LIST_API) {
                            SubjectPreferenceResModel subjectPreferenceResModel = (SubjectPreferenceResModel) responseApi.data;
                            if (!TextUtil.checkListIsEmpty(subjectPreferenceResModel.getSubjects())) {
                                setSubjectListVisibility(true);
                                //Toast.makeText(getActivity(), "SUBJECT_PREFRENCE_LIST_API", Toast.LENGTH_SHORT).show();
                                setSubjectAdapter(subjectPreferenceResModel);
                                binding.swipeRefreshLayout.setRefreshing(false);
                            } else {
                                setSubjectListVisibility(false);
                            }
                        }
                        else if (responseApi.apiTypeStatus == FETCH_STUDENT_PREFERENCES_API) {
                            FetchStudentPrefResModel fetchStudentPrefResModel = (FetchStudentPrefResModel) responseApi.data;
                            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                            prefModel.setFetchStudentPrefResModel(fetchStudentPrefResModel);
                            AuroAppPref.INSTANCE.setPref(prefModel);
                            callSubjectListPreference();
                            // Toast.makeText(getActivity(), "FETCH_STUDENT_PREFERENCES_API", Toast.LENGTH_SHORT).show();

                        }
                        else if (responseApi.apiTypeStatus == GET_USER_PROFILE_DATA) {
                            AppLogger.v("GET_USER_PROFILE_DATA callApi", firstTimeCome + "   main");
                            handleProgress(1, "");
                            getStudentUpdateProfile = (GetStudentUpdateProfile) responseApi.data;
                            setDataonUi();
                            AppLogger.v("GET_USER_PROFILE_DATA apiResponse", getStudentUpdateProfile + "");
                        }
                        else if (responseApi.apiTypeStatus == CHECKVALIDUSER) {
                            CheckUserResModel checkUserResModel = (CheckUserResModel) responseApi.data;
                            if (!checkUserResModel.getError()) {
                                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                prefModel.setCheckUserResModel(checkUserResModel);
                                AuroAppPref.INSTANCE.setPref(prefModel);
                                checkForAddStudentVisibility();
                            }

                        }
                        else if (responseApi.apiTypeStatus == Status.STUDENT_KYC_STATUS_API) {
                            studentKycStatusResModel = (StudentKycStatusResModel) responseApi.data;
                            if (!studentKycStatusResModel.getError()) {
                                callCheckUserApi();
                            }
                        }
                    }
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                    }
                    break;





                default:
                    if (isVisible()) {
                        // handleProgress(1, (String) responseApi.data);
                        AppLogger.v("apiData", responseApi.data + "   pradeep");
                        handleProgress(2, (String) responseApi.data);
                        showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getDefaultError() != null ? prefModel.getLanguageMasterDynamic().getDetails().getDefaultError() : getActivity().getString(R.string.default_error));
                    }
                    break;
            }

        });
    }

    private void handleProgress(int status, String message) {
        switch (status) {
            case 0:
                binding.mainParentLayout.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.shimmerMyClassroom.setVisibility(View.VISIBLE);
                binding.shimmerMyClassroom.startShimmer();
                break;

            case 1:
                binding.mainParentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.shimmerMyClassroom.setVisibility(View.GONE);
                binding.shimmerMyClassroom.stopShimmer();
                break;

            case 2:
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.mainParentLayout.setVisibility(View.GONE);
                binding.shimmerMyClassroom.setVisibility(View.GONE);
                binding.shimmerMyClassroom.stopShimmer();
                binding.errorLayout.textError.setText(message);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callingStudentUpdateProfile();
                    }
                });
                break;
        }

    }

    private void handleSubmitProgress(int value, String message) {
        if (value == 0) {
            binding.submitbutton.setText("");
            binding.submitbutton.setEnabled(false);
            binding.progressBar.setVisibility(View.VISIBLE);

        } else if (value == 1) {

            binding.submitbutton.setText(prefModel.getLanguageMasterDynamic().getDetails().getSave()!= null ? prefModel.getLanguageMasterDynamic().getDetails().getSave() : AuroApp.getAppContext().getResources().getString(R.string.save));
            binding.submitbutton.setEnabled(true);
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(((DashBoardMainActivity) getActivity()).binding.naviagtionContent.homeView, message);
    }

    private void showSnackbarError(String message, int color) {
        ViewUtil.showSnackBar(((DashBoardMainActivity) getActivity()).binding.naviagtionContent.homeView, message, color);
    }

    public void setDataonUi() {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle(details.getProcessing());
        progress.setMessage(details.getProcessing());
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();
        if (getStudentUpdateProfile != null) {
            DashboardResModel dashboardResModel = AuroAppPref.INSTANCE.getModelInstance().getDashboardResModel();
            if (dashboardResModel != null) {
                dashboardResModel.setProfilePic(getStudentUpdateProfile.getProfilePic());
                dashboardResModel.setStudent_name(getStudentUpdateProfile.getStudentName());
                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                prefModel.setDashboardResModel(dashboardResModel);
                AuroAppPref.INSTANCE.setPref(prefModel);
            }
            binding.etstate.setText(getStudentUpdateProfile.getStatename());
            state_Code = getStudentUpdateProfile.getStateId();
            f_state = getStudentUpdateProfile.getStateId();
            f_district = getStudentUpdateProfile.getDistrictId();
            getAllDistrict(state_Code);
            // }
//            if (!getStudentUpdateProfile.getDistrictname().isEmpty()){
            binding.etdistrict.setText(getStudentUpdateProfile.getDistrictname());
            district_code = getStudentUpdateProfile.getDistrictId();
            getSchool(state_Code,district_code,Schoolsearch);
            binding.classStudent.setText(getStudentUpdateProfile.getStudentclass() + "");
            if (Integer.parseInt(getStudentUpdateProfile.getStudentclass()) > 10){
                binding.subjectsRecyclerview.setVisibility(View.VISIBLE);
                binding.yourSubjectsLayout.setVisibility(View.VISIBLE);
            }
            else{
                binding.subjectsRecyclerview.setVisibility(View.GONE);
                binding.yourSubjectsLayout.setVisibility(View.GONE);
            }
            if (dashboardResModel != null && !dashboardResModel.getWalletbalance().isEmpty()) {
                binding.walletBalText.setText(dashboardResModel.getWalletbalance());
            } else {
                binding.walletBalText.setText("" + 0);
            }

            binding.editPhone.setText(getStudentUpdateProfile.getPhonenumber());
            if (TextUtil.isEmpty(getStudentUpdateProfile.getStudentName())) {
                progress.dismiss();
                binding.UserName.setHint("Child Name");
                binding.editProfile.setHint("Child Name");
            } else {
                progress.dismiss();
                binding.UserName.setText(getStudentUpdateProfile.getStudentName());
                binding.editProfile.setText(getStudentUpdateProfile.getStudentName());
            }


            if (!TextUtil.isEmpty(getStudentUpdateProfile.getPhonenumber())) {
                binding.icteachername.setVisibility(View.GONE);
            } else {
                binding.icteachername.setVisibility(View.GONE);
            }
            binding.editemail.setText(getStudentUpdateProfile.getEmailId());
            SharedPreferences.Editor editor1 = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
            editor1.putString("updateparentemailid", getStudentUpdateProfile.getEmailId());
            editor1.apply();
            //getStudentUpdateProfile.setUserProfileImage("https://www.edupreneurvillage.com/wp-content/plugins/sticky-buttons/public/partials/wap.png");
            AppLogger.e(TAG, "profile image path-" + getStudentUpdateProfile.getProfilePic());

            if (!TextUtil.isEmpty(getStudentUpdateProfile.getProfilePic())) {
                ImageUtil.loadCircleImage(binding.profileimage, getStudentUpdateProfile.getProfilePic());
                prefModel.setUserprofilepic(getStudentUpdateProfile.getProfilePic());
            }
            if (!getStudentUpdateProfile.getGender().isEmpty()){
                binding.etStudentGender.setText(getStudentUpdateProfile.getTranslated_gender());
                GenderName = getStudentUpdateProfile.getGender();
            }
            else{
                binding.etStudentGender.setText(genderList.get(0).getTranslatedName());
            }
            if (!getStudentUpdateProfile.getSchoolName().isEmpty()){
                binding.etSchoolname.setText(getStudentUpdateProfile.getSchoolName());
            }
            if (!getStudentUpdateProfile.getSchoolType().isEmpty()){
                binding.etSchooltype.setText(getStudentUpdateProfile.getTranslated_school_type());
                schooltype = getStudentUpdateProfile.getSchoolType();
            }
            else{
                binding.etSchooltype.setText(schooltypeList.get(0).getTranslatedName());
            }
            if (!getStudentUpdateProfile.getBoardType().isEmpty()){
                binding.etSchoolboard.setText(getStudentUpdateProfile.getTranslated_board_type());
                boardtype = getStudentUpdateProfile.getBoardType();
            }
            else{
                binding.etSchoolboard.setText(boardlist.get(0).getTranslatedName());
            }
            if (!getStudentUpdateProfile.getMediumOfInstruction().isEmpty()){
                binding.etSchoolmedium.setText(getStudentUpdateProfile.getTransalted_medium_of_instruction());
                Schoolmedium = getStudentUpdateProfile.getMediumOfInstruction();

            }
            if (!getStudentUpdateProfile.getIsPrivateTution().isEmpty()){
                binding.ettution.setText(getStudentUpdateProfile.getTranslated_is_private_tution());
                Tution = getStudentUpdateProfile.getIsPrivateTution();
                if(getStudentUpdateProfile.getIsPrivateTution().equals("Yes")){
                    binding.tltutiontype.setVisibility(View.VISIBLE);
                }
                else{
                    binding.tltutiontype.setVisibility(View.GONE);
                }


            }
            else{
                binding.ettution.setText(privatetutionList.get(0).getTranslatedName());
            }
            if (!getStudentUpdateProfile.getPrivateTutionType().isEmpty()){
                binding.ettutiontype.setText(getStudentUpdateProfile.getTransalated_private_tution_type());
                Tutiontype = getStudentUpdateProfile.getPrivateTutionType();
            }
            else{
                binding.ettutiontype.setText(tutiontypeList.get(0).getTranslatedName());
            }





        }


    }

    private void updateChild() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        fullname = binding.editProfile.getText().toString();
        studentemail = binding.editemail.getText().toString();
        String mobileversion = DeviceUtil.getVersionName();
        String mobilemanufacture = DeviceUtil.getManufacturer(getActivity());
        String modelname = DeviceUtil.getModelName(getActivity());
        String buildversion = AppUtil.getAppVersionName();
        String ipaddress = AppUtil.getIpAdress();
        String username = prefModel.getUserName();
        String languageid = prefModel.getUserLanguageId();
        String childid = prefModel.getUserId();



        schoolnameprofile = binding.etSchoolname.getText().toString();
        if (GenderName.equals("")){
            gender_pass = "Please Select Your Gender";
        }

        else{
            gender_pass = GenderName;
        }
        if (schooltype.equals("")){
            schooltype_pass = "Please Select School";
        }
        else{
            schooltype_pass = schooltype;
        }
        if (boardtype.equals("")){
            board_pass = "Please Select Board";
        }
        else{
            board_pass = boardtype;
        }
        if (Schoolmedium.equals("")){
            schoolmedium_pass = "0";
        }
        else{
            schoolmedium_pass = Schoolmedium;
        }
        if (Tution.equals("")){
            tution_pass = "Please Select Private Tution";
        }
        else{
            tution_pass = Tution;
        }
        if (Tutiontype.equals("")){
            tutiontype_pass = "Please Select Tution Type";
        }
        else{
            tutiontype_pass =  Tutiontype;
        }
        if (stateCode.equals("")||stateCode.isEmpty()){
            final_state_id = state_Code;
        }
        else{
            final_state_id = stateCode;
        }

        if (districtCode.equals("")||districtCode.isEmpty()){
            final_district_id = district_code;
        }
        else{
            final_district_id = districtCode;
        }
        RequestBody language_veriosn = RequestBody.create(MediaType.parse("text/plain"), "0.0.1");
        RequestBody api_veriosn = RequestBody.create(MediaType.parse("text/plain"), "0.0.1");
        RequestBody student_emailid = RequestBody.create(MediaType.parse("text/plain"), studentemail);
        RequestBody student_name = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(fullname));
        RequestBody emptyfield = RequestBody.create(MediaType.parse("text/plain"), "NULL");
        RequestBody name_c = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody prtnersource = RequestBody.create(MediaType.parse("text/plain"), prefModel.getPartnersource());
        RequestBody regsource = RequestBody.create(okhttp3.MultipartBody.FORM, "AuroScholr");//model.getPartnerSource()
        RequestBody sharetype = RequestBody.create(okhttp3.MultipartBody.FORM, "telecaller");//model.getPartnerSource()
        RequestBody devicetoken = RequestBody.create(MediaType.parse("text/plain"), fbnewToken);
        RequestBody userid_c = RequestBody.create(MediaType.parse("text/plain"), childid);
        RequestBody languageid_c = RequestBody.create(MediaType.parse("text/plain"), languageid);
        RequestBody ipaddress_c = RequestBody.create(MediaType.parse("text/plain"), ipaddress);
        RequestBody buildversion_c = RequestBody.create(MediaType.parse("text/plain"), buildversion);
        RequestBody modelname_c = RequestBody.create(MediaType.parse("text/plain"), modelname);
        RequestBody mobilemanufacture_c = RequestBody.create(MediaType.parse("text/plain"), mobilemanufacture);
        RequestBody mobileversion_c = RequestBody.create(MediaType.parse("text/plain"), mobileversion);
        RequestBody gender_c = RequestBody.create(MediaType.parse("text/plain"), gender_pass);
        RequestBody schooltype_c = RequestBody.create(MediaType.parse("text/plain"), schooltype_pass);
        RequestBody schoolboard_c = RequestBody.create(MediaType.parse("text/plain"), board_pass);
        RequestBody languagemedium_c = RequestBody.create(MediaType.parse("text/plain"), schoolmedium_pass);
        RequestBody privatetution_c = RequestBody.create(MediaType.parse("text/plain"), tution_pass);
        RequestBody privatetutiontype_c = RequestBody.create(MediaType.parse("text/plain"), tutiontype_pass);
        RequestBody stateid_c = RequestBody.create(MediaType.parse("text/plain"), final_state_id);
        RequestBody districtid_c = RequestBody.create(MediaType.parse("text/plain"), final_district_id);
        RequestBody schoolname_c = RequestBody.create(MediaType.parse("text/plain"), schoolnameprofile);

        if (image_path == null || image_path.equals("null") || image_path.equals("") || image_path.isEmpty()) {
            lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            filename = "";
        }
        else {
            lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), studentProfileModel.getImageBytes());
            filename = image_path.substring(image_path.lastIndexOf("/") + 1);
        }
        MultipartBody.Part lFile = MultipartBody.Part.createFormData("user_profile_image", filename, lRequestBody);

        RemoteApi.Companion.invoke()
                .updateexistchilddetail(buildversion_c, prtnersource, schoolname_c, student_emailid, schoolboard_c,
                        gender_c, regsource, sharetype, devicetoken, emptyfield,
                        emptyfield, emptyfield, ipaddress_c, mobileversion_c, modelname_c, privatetutiontype_c, privatetution_c,
                        emptyfield, emptyfield, languagemedium_c, mobilemanufacture_c, stateid_c, districtid_c, name_c, schooltype_c,
                        userid_c, language_veriosn,languagemedium_c,api_veriosn,languageid_c,student_name, lFile)
                .enqueue(new Callback<StudentResponselDataModel>() {
                    @Override
                    public void onResponse(Call<StudentResponselDataModel> call, Response<StudentResponselDataModel> response) {
                        try {

                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }



                            }
                            else if (response.isSuccessful()) {
                                Log.d(TAG, "onImageResponse: ");
                                String status = response.body().getStatus().toString();
                                String msg = response.body().getMessage();
                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(getActivity(), msg.toString(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getActivity(), DashBoardMainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                  //  openGenricSDK();
                                }
                            }
                            else {
                                Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onImageerrorResponse: " + response.errorBody().toString());
                                //  showSnackbarError(response.message());
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<StudentResponselDataModel> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "onImgFailure: " + t.getMessage());
                    }
                });
    }



    private void updateProfilePicChild() {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        String childid = prefModel.getUserId();
        String langid = prefModel.getUserLanguageId();
        RequestBody userid_c = RequestBody.create(MediaType.parse("text/plain"), childid);
        RequestBody languageid = RequestBody.create(MediaType.parse("text/plain"), langid);

        if (image_path == null || image_path.equals("null") || image_path.equals("") || image_path.isEmpty()) {
            lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            filename = "";
        }
        else {
            lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), studentProfileModel.getImageBytes());
            filename = image_path.substring(image_path.lastIndexOf("/") + 1);
        }
        MultipartBody.Part lFile = MultipartBody.Part.createFormData("user_profile_image", filename, lRequestBody);
        RemoteApi.Companion.invoke()
                .update_student_photo(userid_c,languageid,lFile)
                .enqueue(new Callback<StudentResponselDataModel>() {
                    @Override
                    public void onResponse(Call<StudentResponselDataModel> call, Response<StudentResponselDataModel> response) {
                        try {

                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }



                            }
                            else if (response.isSuccessful()) {
                                Log.d(TAG, "onImageResponse: ");
                                String status = response.body().getStatus().toString();
                                String msg = response.body().getMessage();
                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(getActivity(), msg.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            else {
                                Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<StudentResponselDataModel> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (binding.editemail.getText().toString().length() > 0 && TextUtil.isValidEmail(binding.editemail.getText().toString())) {

            AppLogger.v("afterTextChanged", "valid email address");
            binding.icemail.setVisibility(View.VISIBLE);

        } else {
            binding.icemail.setVisibility(View.GONE);
            AppLogger.v("afterTextChanged", "Invalid email address");
        }
    }

    public void callingStudentUpdateProfile() {

        if (dashboardResModel != null) {
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            int studentClass = 0;
            studentClass = prefModel.getUserclass();
            SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
            String gradeforsubjectpreferencefinalprofile = prefs.getString("gradeforsubjectpreferencefinalprofile", "");
            if (studentClass > 10 || gradeforsubjectpreferencefinalprofile.equals("11")||gradeforsubjectpreferencefinalprofile.equals("12")||gradeforsubjectpreferencefinalprofile.equals(11)||gradeforsubjectpreferencefinalprofile.equals(12)) {
                if (prefModel.getFetchStudentPrefResModel() != null && !TextUtil.checkListIsEmpty(prefModel.getFetchStudentPrefResModel().getSubjects())) {
                    // callSubjectListPreference();
                    callFetchUserPreference();
                } else {
                    callFetchUserPreference();
                }
            } else {
                setSubjectListVisibility(false);
            }
            handleProgress(0, "");
         /*   StudentProfileModel studentProfileModel = new StudentProfileModel();
            studentProfileModel.setPhonenumber(dashboardResModel.getPhonenumber());*/

            viewModel.getUserProfileApi(AuroAppPref.INSTANCE.getModelInstance().getUserId());

        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            if (v.getId() == R.id.etStudentGender) {
                binding.etStudentGender.showDropDown();
            }
            else if (v.getId() == R.id.etstate) {
                binding.etstate.showDropDown();
            }
            else if (v.getId() == R.id.etdistrict) {
                binding.etdistrict.showDropDown();
            }
//            else if (v.getId() == R.id.etSchoolname) {
//                binding.etSchoolname.showDropDown();
//            }
            else if (v.getId() == R.id.ettution) {
                binding.ettution.showDropDown();
                if (privatetutionList.get(1).equals(Tution)||privatetutionList.get(0).equals(Tution)){
                    binding.tltutiontype.setVisibility(View.VISIBLE);
                }
                else{
                    binding.tltutiontype.setVisibility(View.GONE);
                }
            }
            else if (v.getId() == R.id.ettutiontype) {
                binding.ettutiontype.showDropDown();
            }
            else if (v.getId() == R.id.etSchooltype) {
                binding.etSchooltype.showDropDown();
            }
            else if (v.getId() == R.id.etSchoolmedium) {
                binding.etSchoolmedium.showDropDown();
            }
            else if (v.getId() == R.id.etSchoolboard) {
                binding.etSchoolboard.showDropDown();
            }

            else if (v.getId() == R.id.etgrade) {
                binding.etgrade.showDropDown();
            }
        }
    }

    private void changeTheEditText() {
        AppLogger.v(TAG, "focus lost");
        binding.UserName.setVisibility(View.VISIBLE);
        binding.editUserNameIcon.setVisibility(View.GONE);
        binding.editUserNameIcon.setImageResource(R.drawable.ic_edit_profile);
        binding.editProfileName.setVisibility(View.VISIBLE);
        binding.cancelUserNameIcon.setVisibility(View.GONE);
        if (!TextUtil.isEmpty(binding.editProfile.getText().toString())) {
            binding.UserName.setText(binding.editProfile.getText().toString());
        }
    }

    private void askPermission() {

        ImagePicker.with(getActivity())
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();

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
                    image_path = uri.getPath();

                    Log.d(TAG, "imagepathon: "+image_path);
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



        }
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        if (commonDataModel.getClickType()==REGISTER_CALLBACK)
        {
            addStudent();
        }

        else if (commonDataModel.getClickType()==STATE)
        {
            StateData stateDataModel = (StateData) commonDataModel.getObject();
            binding.etstate.setText(stateDataModel.getState_name());
            getAllDistrict(stateDataModel.getState_id());
            Log.d("state_id", stateDataModel.getState_id());
            binding.etdistrict.setText("");
            binding.etSchoolname.setText("");
            stateCode = stateDataModel.getState_id();
            f_state = stateDataModel.getState_id();
            binding.etSchoolname.dismissDropDown();

        }
        else if (commonDataModel.getClickType()==DISTRICT)
        {
            DistrictData districtData = (DistrictData) commonDataModel.getObject();
            binding.etdistrict.setText(districtData.getDistrict_name());
            districtCode = districtData.getDistrict_id();
            f_district = districtData.getDistrict_id();;
            binding.etSchoolname.setText("");
            getSchool(stateCode,districtCode,Schoolsearch);

        }
        else if (commonDataModel.getClickType()==SCHOOL)
        {
            SchoolData gData = (SchoolData) commonDataModel.getObject();
            binding.etSchoolname.setText(gData.getSCHOOL_NAME());
            SchoolName = gData.getSCHOOL_NAME();


        }
        else if (commonDataModel.getClickType()==GENDER)
        {
            GenderData gData = (GenderData) commonDataModel.getObject();
            binding.etStudentGender.setText(gData.getTranslatedName());
            GenderName = gData.getName();

        }
        else if (commonDataModel.getClickType()==TUTIONTYPE)
        {
            TutionData gData = (TutionData) commonDataModel.getObject();
            binding.ettutiontype.setText(gData.getTranslatedName());
            Tutiontype = gData.getName();

        }
        else if (commonDataModel.getClickType()==TUTION)
        {
            PrivateTutionData gData = (PrivateTutionData) commonDataModel.getObject();
            binding.ettution.setText(gData.getTranslatedName());
            Tution = gData.getName();
            if (privatetutionList.get(1).getTranslatedName().equals(binding.ettution.getText().toString())){
                binding.tltutiontype.setVisibility(View.VISIBLE);
            }
            else{
                binding.tltutiontype.setVisibility(View.GONE);
            }


        }
        else if (commonDataModel.getClickType()==SCHHOLMEDIUM)
        {
            SchoolLangData gData = (SchoolLangData) commonDataModel.getObject();
            binding.etSchoolmedium.setText(gData.getTranslated_language());
            Schoolmedium = String.valueOf(gData.getLanguage_id());

        }
        else if (commonDataModel.getClickType()==SCHHOLTYPE)
        {
            SchoolTypeData gData = (SchoolTypeData) commonDataModel.getObject();
            binding.etSchooltype.setText(gData.getTranslatedName());
            schooltype = gData.getName();

        }
        else if (commonDataModel.getClickType()==BOARD)
        {
            BoardData gData = (BoardData) commonDataModel.getObject();
            binding.etSchoolboard.setText(gData.getTranslatedName());
            boardtype = gData.getName();

        }
        else if (commonDataModel.getClickType()==GRADE)
        {
            GradeData gData = (GradeData) commonDataModel.getObject();
            binding.etgrade.setText(gData.getGrade_id());
            gradeid = String.valueOf(gData.getGrade_id());

        }
    }

    public void addStudent() {
        UserDetailResModel userDetailResModel = AuroAppPref.INSTANCE.getModelInstance().getStudentData();
        Intent intent = new Intent(getActivity(), SetPinActivity.class);
        intent.putExtra(AppConstant.COMING_FROM, AppConstant.ComingFromStatus.COMING_FROM_ADD_STUDENT);
        intent.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, userDetailResModel);
        startActivity(intent);
    }

    private void openProgressDialog() {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            return;
        }
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(getActivity());
        customDialogModel.setTitle("Fetching Your Location...");
        customDialogModel.setTwoButtonRequired(true);
        customProgressDialog = new CustomProgressDialog(customDialogModel);
        Objects.requireNonNull(customProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();
        customProgressDialog.updateDataUi(0);
    }

    private void openChangeLanguageDialog() {
        LanguageChangeDialog languageChangeDialog = new LanguageChangeDialog(getActivity());
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(languageChangeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        languageChangeDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(languageChangeDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        languageChangeDialog.setCancelable(true);
        languageChangeDialog.show();

    }

    public void openKYCFragment(DashboardResModel dashboardResModel) {
        Bundle bundle = new Bundle();
        KYCFragment kycFragment = new KYCFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        kycFragment.setArguments(bundle);
        openFragment(kycFragment);
    }

    public void openKYCViewFragment(DashboardResModel dashboardResModel) {
        Bundle bundle = new Bundle();
        KYCViewFragment kycViewFragment = new KYCViewFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        kycViewFragment.setArguments(bundle);
        openFragment(kycViewFragment);
    }

    private void openFragment(Fragment fragment) {
        ((AppCompatActivity) (this.getContext())).getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(AuroApp.getFragmentContainerUiId(), fragment, StudentProfileFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
    public void openFragment2(Fragment fragment) {
        FragmentUtil.replaceFragment(getActivity(), fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }


    @Override
    public void onClickListener() {
        ((DashBoardMainActivity) getActivity()).openProfileFragment();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (view.getId() == R.id.etStudentGender) {
            binding.etStudentGender.showDropDown();
        }
        else if (view.getId() == R.id.etstate) {
            binding.etstate.showDropDown();
        }
        else if (view.getId() == R.id.etdistrict) {
            binding.etdistrict.showDropDown();
        }
        else if (view.getId() == R.id.ettution) {
            binding.ettution.showDropDown();

            if (privatetutionList.get(1).equals(Tution)||privatetutionList.get(0).equals(Tution)){
                binding.tltutiontype.setVisibility(View.VISIBLE);
            }
            else{
                binding.tltutiontype.setVisibility(View.GONE);
            }
        }
        else if (view.getId() == R.id.ettutiontype) {
            binding.ettutiontype.showDropDown();




        }
        else if (view.getId() == R.id.etSchooltype) {
            binding.etSchooltype.showDropDown();
        }
        else if (view.getId() == R.id.etSchoolmedium) {
            binding.etSchoolmedium.showDropDown();
        }
        else if (view.getId() == R.id.etSchoolboard) {
            binding.etSchoolboard.showDropDown();
        }

        else if (view.getId() == R.id.etgrade) {
            binding.etgrade.showDropDown();
        }




        else if (view.getId() == R.id.grade_layout) {
            changeTheEditText();
            if (!binding.editProfile.getText().toString().equalsIgnoreCase("")) {
                binding.UserName.setText(binding.editProfile.getText().toString());
            }}
        else if (view.getId() == R.id.linearLayout8) {
            changeTheEditText();
            if (!binding.editProfile.getText().toString().equalsIgnoreCase("")) {
                binding.UserName.setText(binding.editProfile.getText().toString());
            }
        }
        else if (view.getId() == R.id.editPhone) {
            changeTheEditText();
            if (!binding.editProfile.getText().toString().equalsIgnoreCase("")) {
                binding.UserName.setText(binding.editProfile.getText().toString());
            }
        }
        else if (view.getId() == R.id.editemail) {
            changeTheEditText();
            if (!binding.editProfile.getText().toString().equalsIgnoreCase("")) {
                binding.UserName.setText(binding.editProfile.getText().toString());
            }

        }






        return false;
    }


    private void funnelProfileSubmitScreen() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentProfileSubmit();
    }

    private void checkYCApprovedOrNot() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        DashboardResModel dashboardResModel = prefModel.getDashboardResModel();
        if (dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getIs_kyc_verified()) && dashboardResModel.getIs_kyc_verified().equalsIgnoreCase(AppConstant.DocumentType.APPROVE)) {
            binding.gradeChnage.setEnabled(false);
            binding.imagevGrade.setVisibility(View.INVISIBLE);
        }

    }

    void callSubjectListPreference() {
        viewModel.checkInternetForApi(SUBJECT_PREFRENCE_LIST_API, "");
    }

    void callCheckUserApi() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        CheckUserApiReqModel checkUserApiReqModel = new CheckUserApiReqModel();
        checkUserApiReqModel.setEmailId("");
        checkUserApiReqModel.setMobileNo("");
        checkUserApiReqModel.setUserType("0");
        checkUserApiReqModel.setUserName(prefModel.getUserMobile());
        viewModel.checkInternetForApi(Status.CHECKVALIDUSER, checkUserApiReqModel);
    }

    private void getSchool(String state_id, String district_id, String search)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();


        districtList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("state_id",f_state);
        map_data.put("district_id",f_district);
        map_data.put("search",search);


        RemoteApi.Companion.invoke().getSchool(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.SchoolDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.SchoolDataModel> call, Response<com.auro.application.home.data.model.SchoolDataModel> response)
                    {
                        if (response.isSuccessful()) {
                            districtList.clear();
                            genderListString.clear();

                            if (response.body().getSchools() != null || !response.body().getSchools().isEmpty()) {
                                Log.d(TAG, "onDistrictResponse: " + response.message());
                                for (int i = 0; i < response.body().getSchools().size(); i++) {
                                    int school_id = response.body().getSchools().get(i).getID();
                                    String school_name = response.body().getSchools().get(i).getSCHOOL_NAME();
                                    genderListString.add(school_name);
                                    Log.d(TAG, "onDistrictResponse: " + school_name);
                                    SchoolData districtData = new SchoolData(school_name, school_id);
                                    districtList.add(districtData);


                                }

                            }
                            else {

                                Toast.makeText(getActivity(), details.getNo_data_found(), Toast.LENGTH_SHORT).show();
                            }


                        }

                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.SchoolDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }


    private void getAllStateList()
    {

        statelist.clear();
        RemoteApi.Companion.invoke().getStateData()
                .enqueue(new Callback<com.auro.application.home.data.model.StateDataModel>()
                {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.StateDataModel> call, Response<com.auro.application.home.data.model.StateDataModel> response)
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
                                statelist.add(stateData);
                            }

                            addDropDownState(statelist);




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
    private void getGrade()
    {

        gradelist.clear();
        RemoteApi.Companion.invoke().getGrade()
                .enqueue(new Callback<GradeDataModel>()
                {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.GradeDataModel> call, Response<com.auro.application.home.data.model.GradeDataModel> response)
                    {
                        if (response.isSuccessful())
                        {

                            for ( int i=0 ;i < response.body().getResult().size();i++)
                            {
                                String state_id = response.body().getResult().get(i).getGrade_id();


                                GradeData stateData = new GradeData(state_id);
                                gradelist.add(stateData);


                            }
                            addDropDownGrade();




                        }
                        else
                        {
                            Log.d(TAG, "onResponser: "+response.message().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.GradeDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
    private void getAllDistrict(String state_id)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle(details.getProcessing());
        progress.setMessage(details.getProcessing());
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();
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
                            progress.dismiss();
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
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        genderList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("key","Gender");
        map_data.put("language_id",prefModel.getUserLanguageId());  //prefModel.getUserLanguageId()
        RemoteApi.Companion.invoke().getAllData(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.GenderDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.GenderDataModel> call, Response<com.auro.application.home.data.model.GenderDataModel> response)
                    {
                        if (response.isSuccessful())
                        {

                            for ( int i=0 ;i < response.body().getResult().size();i++)
                            {
                                int gender_id = response.body().getResult().get(i).getID();
                                String name = response.body().getResult().get(i).getName();

                                String translated_name = response.body().getResult().get(i).getTranslatedName();
                                GenderData districtData = new  GenderData(gender_id,translated_name,name);
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
    private void getTutiontype()
    {
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        tutiontypeList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("key","tution_type");
        map_data.put("language_id",prefModel.getUserLanguageId());
        RemoteApi.Companion.invoke().getAllData(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.GenderDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.GenderDataModel> call, Response<com.auro.application.home.data.model.GenderDataModel> response)
                    {
                        if (response.isSuccessful())
                        {

                            for ( int i=0 ;i < response.body().getResult().size();i++)
                            {
                                int gender_id = response.body().getResult().get(i).getID();
                                String translated_name = response.body().getResult().get(i).getTranslatedName();
                                String name = response.body().getResult().get(i).getName();

                                TutionData districtData = new  TutionData(gender_id,translated_name,name);
                                tutiontypeList.add(districtData);

                            }
                            addDropDownTutionType();
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
    private void getPrivatetype()
    {
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        privatetutionList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("key","private_tution");
        map_data.put("language_id",prefModel.getUserLanguageId());
        RemoteApi.Companion.invoke().getAllData(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.GenderDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.GenderDataModel> call, Response<com.auro.application.home.data.model.GenderDataModel> response)
                    {
                        if (response.isSuccessful())
                        {

                            for ( int i=0 ;i < response.body().getResult().size();i++)
                            {
                                int gender_id = response.body().getResult().get(i).getID();
                                String gender_name = response.body().getResult().get(i).getName();
                                String translated_name = response.body().getResult().get(i).getTranslatedName();

                                PrivateTutionData districtData = new  PrivateTutionData(gender_id,translated_name,gender_name);
                                privatetutionList.add(districtData);

                            }
                            if (privatetutionList.get(1).equals(Tution)||privatetutionList.get(0).equals(Tution)){
                                binding.tltutiontype.setVisibility(View.VISIBLE);
                            }
                            else{
                                binding.tltutiontype.setVisibility(View.GONE);
                            }
                            addDropDownPrivateTution();
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
    private void getSchooltype()
    {
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        schooltypeList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("key","school_type");
        map_data.put("language_id",prefModel.getUserLanguageId());
        RemoteApi.Companion.invoke().getAllData(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.GenderDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.GenderDataModel> call, Response<com.auro.application.home.data.model.GenderDataModel> response)
                    {
                        if (response.isSuccessful())
                        {

                            for ( int i=0 ;i < response.body().getResult().size();i++)
                            {
                                int gender_id = response.body().getResult().get(i).getID();
                                String gender_name = response.body().getResult().get(i).getName();
                                String translated_name = response.body().getResult().get(i).getTranslatedName();

                                SchoolTypeData districtData = new  SchoolTypeData(gender_id,translated_name,gender_name);
                                schooltypeList.add(districtData);

                            }
                            addDropDownSchoolType();
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
    private void getBoardtype()
    {
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        boardlist.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("key","board");
        map_data.put("language_id",prefModel.getUserLanguageId());
        RemoteApi.Companion.invoke().getAllData(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.GenderDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.GenderDataModel> call, Response<com.auro.application.home.data.model.GenderDataModel> response)
                    {
                        if (response.isSuccessful())
                        {

                            for ( int i=0 ;i < response.body().getResult().size();i++)
                            {
                                int gender_id = response.body().getResult().get(i).getID();
                                String gender_name = response.body().getResult().get(i).getName();
                                String translated_name = response.body().getResult().get(i).getTranslatedName();

                                BoardData districtData = new  BoardData(gender_id,translated_name,gender_name);
                                boardlist.add(districtData);

                            }
                            addDropDownBoard();
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
    private void getSchoolmedium(String langid)
    {
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        String langiduser = prefModel.getUserLanguageId();
        mediumlist.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("language_id", langiduser);
        RemoteApi.Companion.invoke().getSchoolmedium(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.SchoolMediumLangDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.SchoolMediumLangDataModel> call, Response<com.auro.application.home.data.model.SchoolMediumLangDataModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            Log.d(TAG, "onDistrictResponse: "+response.message());
                            for ( int i=0 ;i < response.body().getResult().size();i++)
                            {
                                int city_id = response.body().getResult().get(i).getLanguage_id();
                                String city_name = response.body().getResult().get(i).getTranslated_language();
                                String lang_name = response.body().getResult().get(i).getLanguage_name();

                                SchoolLangData districtData = new  SchoolLangData(city_id,city_name,lang_name);
                                mediumlist.add(districtData);

                            }
                            addDropDownMedium();
                        }
                        else
                        {
                            Log.d(TAG, "onResponseError: "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.SchoolMediumLangDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }

    public void addDropDownSchool(List<SchoolData> districtList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + districtList.size());
        SchoolSpinnerAdapter districtSpinnerAdapter = new SchoolSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, districtList, this);
        binding.etSchoolname.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etSchoolname.setThreshold(1);//will start working from first character
        binding.etSchoolname.setTextColor(Color.BLACK);

        // binding.etSchoolname.setBackgroundColor(Color.WHITE);
    }
    private void addDropDownGender()
    {
        GenderSpinnerAdapter adapter = new GenderSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, genderList, this);
        binding.etStudentGender.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etStudentGender.setThreshold(1);
        binding.etStudentGender.setTextColor(Color.BLACK);//will start working from first character
    }
    private void addDropDownBoard()
    {
        SchoolBoardSpinnerAdapter adapter = new SchoolBoardSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, boardlist, this);
        binding.etSchoolboard.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etSchoolboard.setThreshold(1);
        binding.etSchoolboard.setTextColor(Color.BLACK);//will start working from first character
    }
    private void addDropDownMedium()
    {
        SchoolMediumSpinnerAdapter adapter = new SchoolMediumSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, mediumlist, this);
        binding.etSchoolmedium.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etSchoolmedium.setThreshold(1);
        binding.etSchoolmedium.setTextColor(Color.BLACK);//will start working from first character
    }
    private void addDropDownTutionType()
    {
        TutionTypeSpinnerAdapter adapter= new TutionTypeSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, tutiontypeList, this);
        binding.ettutiontype.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.ettutiontype.setThreshold(1);
        binding.ettutiontype.setTextColor(Color.BLACK);//will start working from first character
    }
    private void addDropDownPrivateTution()
    {
        PrivateTutionSpinnerAdapter adapter = new PrivateTutionSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, privatetutionList, this);
        binding.ettution.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.ettution.setThreshold(1);
        binding.ettution.setTextColor(Color.BLACK);
        if (privatetutionList.get(1).equals(Tution)||privatetutionList.get(0).equals(Tution)){
            binding.tltutiontype.setVisibility(View.VISIBLE);
        }
        else{
            binding.tltutiontype.setVisibility(View.GONE);
        }
        //will start working from first character
    }
    private void addDropDownSchoolType()
    {
        SchoolTypeSpinnerAdapter adapter = new SchoolTypeSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, schooltypeList, this);
        binding.etSchooltype.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etSchooltype.setThreshold(1);
        binding.etSchooltype.setTextColor(Color.BLACK);//will start working from first character
    }
    private void addDropDownGrade()
    {
        GradeSpinnerAdapter adapter = new GradeSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, gradelist, this);
        binding.etgrade.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etgrade.setThreshold(1);
        binding.etgrade.setTextColor(Color.BLACK);//will start working from first character
    }
    public void addDropDownDistrict(List<DistrictData> districtList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + districtList.size());
        com.auro.application.home.presentation.view.adapter.DistrictSpinnerAdapter districtSpinnerAdapter = new com.auro.application.home.presentation.view.adapter.DistrictSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, districtList, this);
        binding.etdistrict.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etdistrict.setThreshold(1);//will start working from first character
        binding.etdistrict.setTextColor(Color.BLACK);
    }
    public void addDropDownState(List<StateData> stateList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + stateList.size());
        com.auro.application.home.presentation.view.adapter.StateSpinnerAdapter stateSpinnerAdapter = new com.auro.application.home.presentation.view.adapter.StateSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, stateList, this);
        binding.etstate.setAdapter(stateSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etstate.setThreshold(1);//will start working from first character
        binding.etstate.setTextColor(Color.BLACK);
    }
    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        for (int i = 0; i < genderListString.size(); i++) {
            genderListString.set(i, genderListString.get(i));
        }
        String newschool = binding.autoCompleteTextView1.getText().toString();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.select_dialog_item,genderListString);
        return adapter;
    }

}
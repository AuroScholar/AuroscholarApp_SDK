package com.auro.application.home.presentation.view.activity;

import static com.auro.application.core.common.Status.BOARD;
import static com.auro.application.core.common.Status.DISTRICT;
import static com.auro.application.core.common.Status.GENDER;
import static com.auro.application.core.common.Status.GRADE;
import static com.auro.application.core.common.Status.SCHHOLMEDIUM;
import static com.auro.application.core.common.Status.SCHHOLTYPE;
import static com.auro.application.core.common.Status.SCHOOL;
import static com.auro.application.core.common.Status.STATE;
import static com.auro.application.core.common.Status.TUTION;
import static com.auro.application.core.common.Status.TUTIONTYPE;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.home.data.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.databinding.FragmentStudentUpdateProfileBinding;
import com.auro.application.home.data.model.AddNewSchoolResModel;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.BoardData;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.DistrictData;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.GradeData;
import com.auro.application.home.data.model.GradeDataModel;
import com.auro.application.home.data.model.PrivateTutionData;
import com.auro.application.home.data.model.SchoolData;
import com.auro.application.home.data.model.SchoolLangData;
import com.auro.application.home.data.model.SchoolTypeData;
import com.auro.application.home.data.model.StateData;
import com.auro.application.home.data.model.StudentResponselDataModel;
import com.auro.application.home.data.model.TutionData;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.StudentKycStatusResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.presentation.view.adapter.DistrictSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.GenderSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.GradeSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.PrivateTutionSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolBoardSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolMediumSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolTypeSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.StateSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.TutionTypeSpinnerAdapter;

import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.DeviceUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.CustomDialog;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.strings.AppStringDynamic;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompleteStudentProfileWithoutPin extends BaseActivity implements View.OnClickListener,View.OnTouchListener ,View.OnFocusChangeListener, CommonCallBackListner{

    @Inject
    @Named("CompleteStudentProfileWithoutPin")
    ViewModelFactory viewModelFactory;
    String getschool_id;
    List<String> genderListString = new ArrayList<>();
    List<UserDetailResModel> listchilds = new ArrayList<>();
    List<UserDetailResModel> list = new ArrayList<>();
    private static final int CAMERA_REQUEST = 1888;
    UserDetailResModel checkUserResModel;
    FragmentStudentUpdateProfileBinding binding;
    GetStudentUpdateProfile studentProfileModel = new GetStudentUpdateProfile();

    boolean firstTimeCome;
    String TAG = "StudentProfileFragment";
    PrefModel prefModel;
    String schoolnamesearch;
    String stateCode = "";
    String districtCode = "";
    String Schoolsearch = "";
    String GenderName = "";
    String fbnewToken = "test123";
    String image_path = "";
    String filename = "";
    String SchoolName = "";
    String Tutiontype = "";
    String Tution = "";
    String Schoolmedium = "";
    String boardtype = "";
    String schooltype = "";
    String ExistGrade;
    String ExistGradenew;
    String kycgrade;
    CustomDialog customDialog;
    List<SchoolData> districtList = new ArrayList<>();

    List<GenderData> genderList = new ArrayList<>();
    List<TutionData> tutiontypeList = new ArrayList<>();
    List<PrivateTutionData> privatetutionList = new ArrayList<>();
    List<SchoolTypeData> schooltypeList = new ArrayList<>();
    List<StateData> statelist = new ArrayList<>();
    List<BoardData> boardlist = new ArrayList<>();
    List<SchoolLangData> langlist = new ArrayList<>();
    List<GradeData> gradelist = new ArrayList<>();

    RequestBody lRequestBody;
    String gradeid;

    StudentKycStatusResModel studentKycStatusResModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, getLayout());

        binding.setLifecycleOwner(this);

        AppUtil.loadAppLogo(binding.auroScholarLogo, this);
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        String language_id = prefModel.getUserLanguageId();
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
//            fbnewToken = instanceIdResult.getToken();
//            Log.e("newTokens", fbnewToken);
//        });
        binding.layoutSetusernamepin.setVisibility(View.GONE);
        binding.mainParentLayout.setVisibility(View.VISIBLE);
        binding.usernametxt.setVisibility(View.GONE);
        getProfile();
        getGrade();
        getAllStateList();
        getGender();
        getTutiontype();
        getPrivatetype();
        getSchooltype();
        getBoardtype();
        getSchoolmedium(language_id);
        init();
        setListener();




        SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor.putString("statusparentprofile", "false");
        editor.putString("isLogin","true");
        editor.putString("statusfillstudentprofile", "false");
        editor.putString("statussetpasswordscreen", "false");
        editor.putString("statuschoosegradescreen", "false");
        editor.putString("statusopenprofileteacher", "false");
        editor.putString("statusopendashboardteacher", "false");
        editor.putString("statuschoosedashboardscreen", "false");
        editor.putString("statusopenprofilewithoutpin", "true");
        editor.putString("statussubjectpref","false");
        editor.apply();
        binding.etstate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("stateid", parent.getItemAtPosition(position).toString());
            }
        });
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

        binding.etgrade.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (ExistGrade.equals("0")||ExistGrade.equals(0)||ExistGrade.equals("")||ExistGrade.equals("null")||ExistGrade.equals(null)){
                    binding.etgrade.showDropDown();
                }
                else if (ConversionUtil.INSTANCE.convertStringToInteger(ExistGradenew) > 0){
                    binding.etgrade.showDropDown();
                }
                else{
                    openErrorDialog();
                }
                return false;
            }
        });

        binding.etgrade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    if (ExistGrade.equals("0")||ExistGrade.equals(0)||ExistGrade.equals("")||ExistGrade.equals("null")||ExistGrade.equals(null)){
                        binding.etgrade.showDropDown();
                    }

                    else if (ConversionUtil.INSTANCE.convertStringToInteger(ExistGradenew) > 0){
                        binding.etgrade.showDropDown();
                    }
                    else{
                        openErrorDialog();
                    }
                }
            }
        });


        binding.etschool.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (districtList!=null||!districtList.isEmpty()){
                    binding.etschool.showDropDown();

                    addDropDownSchool(districtList);
                }


                return false;
            }
        });
        binding.etschool.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    if (districtList!=null||!districtList.isEmpty()){
                        binding.etschool.showDropDown();

                        addDropDownSchool(districtList);
                    }


                }
            }
        });
        binding.btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editsearch.getText().toString().isEmpty()||binding.editsearch.getText().toString().equals("")){
                    //   binding.editsearch.setHint("Search school here..");
                }
                else{
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);                    String search = binding.editsearch.getText().toString();
                    binding.etschool.setText("");
                    String search2 = binding.editsearch.getText().toString();
                    getSchoolsearch(stateCode,districtCode,search2);


                }

            }
        });
        binding.editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.editsearch.getText().toString().isEmpty()||binding.editsearch.getText().toString().equals("")){

                    Schoolsearch="";
                    binding.etschool.dismissDropDown();
                    getSchool(stateCode,districtCode,Schoolsearch);

                }
                else{
                    if (s.toString().equals("")||s.toString().isEmpty()){
                        binding.etschool.dismissDropDown();
                    }
                    else{
                        Schoolsearch = s.toString();
                        getSchool(stateCode,districtCode,s.toString());

                    }
                }
            }
        });

        binding.autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem=binding.autoCompleteTextView1.getAdapter().getItem(position).toString();
                String schoolname = binding.autoCompleteTextView1.getText().toString();
                binding.etschool.setText(schoolname);
            }
        });
        binding.autoCompleteTextView1.setAdapter(getEmailAddressAdapter(CompleteStudentProfileWithoutPin.this));
        binding.autoCompleteTextView1.setThreshold(1);
        binding.autoCompleteTextView1.setTextColor(Color.BLACK);
        binding.autoCompleteTextView1.setDropDownBackgroundResource(R.color.white);
        binding.addnewschool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
                if (binding.autoCompleteTextView1.getText().toString().isEmpty()){
                    Toast.makeText(CompleteStudentProfileWithoutPin.this, "Please enter school name", Toast.LENGTH_SHORT).show();
                }
                else if (binding.autoCompleteTextView1.getText().toString().startsWith(" ")){
                    Toast.makeText(CompleteStudentProfileWithoutPin.this, details.getEnter_space_schoolname(), Toast.LENGTH_SHORT).show();
                }
                else{
                    String search = binding.autoCompleteTextView1.getText().toString();
                    binding.etschool.setText(search);
                    addNewSchool(stateCode,districtCode,search);
                }
            }
        });
    }
    @Override
    protected void init() {
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        firstTimeCome = true;
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.STUDENT_PROFILE_FRAGMENT);
        AppUtil.commonCallBackListner = this;
        AppUtil.loadAppLogo(binding.auroScholarLogo, this);

        String parentmobileno = prefModel.getUserMobile();
        binding.editPhone.setText(parentmobileno);
        AppStringDynamic.setStudentUpdateProfilePageStrings(binding);
    }
    @Override
    protected void setListener() {
        binding.languageLayout.setOnClickListener(this);
        binding.profileImage.setOnClickListener(this);
        binding.editImage.setOnClickListener(this);
        binding.btdonenew.setOnClickListener(this);
        binding.submitbutton.setOnClickListener(this);
        binding.edtusername.setOnClickListener(this);
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

    }
    @Override
    protected int getLayout() {
        return R.layout.fragment_student_update_profile;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.editImage) {
            if (Build.VERSION.SDK_INT > 26) {
                askPermission();
            } else {
                askPermission();
            }
        } else if (id == R.id.btdonenew) {
            String pin = binding.pinView.getText().toString();
            String confirmpin = binding.confirmPin.getText().toString();


            Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
            if (binding.etUsername.getText().toString().isEmpty() || binding.etUsername.getText().toString().equals("")) {
                Toast.makeText(this, details.getEnter_user_name(), Toast.LENGTH_SHORT).show();
            } else if (binding.etUsername.getText().toString().length() < 5) {
                Toast.makeText(this, details.getEnter_min_char(), Toast.LENGTH_SHORT).show();

            } else if (binding.etUsername.getText().toString().startsWith(" ")) {
                Toast.makeText(this, details.getEnter_space_username(), Toast.LENGTH_SHORT).show();
            } else if (binding.pinView.getText().toString().isEmpty() || binding.pinView.getText().toString().equals("")) {
                Toast.makeText(this, details.getEnter_the_pin(), Toast.LENGTH_SHORT).show();
            } else if (pin.length() < 4) {
                Toast.makeText(this, details.getEnter_pin_digit(), Toast.LENGTH_SHORT).show();
            } else if (binding.confirmPin.getText().toString().isEmpty() || binding.confirmPin.getText().toString().equals("")) {
                Toast.makeText(this, details.getEnter_the_confirm_pin(), Toast.LENGTH_SHORT).show();
            } else if (confirmpin.length() < 4) {
                Toast.makeText(this, details.getEnter_confirmpin_digit(), Toast.LENGTH_SHORT).show();
            } else if (pin.startsWith(" ")) {
                Toast.makeText(this, details.getEnter_space_pin(), Toast.LENGTH_SHORT).show();

            } else if (confirmpin.startsWith(" ")) {
                Toast.makeText(this, details.getEnter_space_confirmpin(), Toast.LENGTH_SHORT).show();

            } else if (pin == confirmpin || pin.equals(confirmpin)) {

            } else {
                Toast.makeText(this, details.getPin_and_confirm_not_match(), Toast.LENGTH_SHORT).show();
            }


            String email = binding.editemail.getText().toString().trim();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            Details details1 = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

            if (binding.mainParentLayout.getVisibility() == View.VISIBLE) {

                if (binding.edtusername.getText().toString().equals("") || binding.edtusername.getText().toString().isEmpty()) {
                    Toast.makeText(this, details1.getPlease_enetr_the_name(), Toast.LENGTH_SHORT).show();
                } else if (binding.etStudentGender.getText().toString().equals("Student Gender") || binding.etStudentGender.getText().toString().equals("Select Gender") || binding.etStudentGender.getText().toString().equals("") || binding.etStudentGender.getText().toString().isEmpty() || genderList.get(0).getTranslatedName().equals(binding.etStudentGender.getText().toString())) {
                    Toast.makeText(this, details1.getPlease_select_gender(), Toast.LENGTH_SHORT).show();

                } else if (binding.etstate.getText().toString().equals("State") || binding.etstate.getText().toString().equals("") || binding.etstate.getText().toString().isEmpty()) {
                    Toast.makeText(this, details1.getPlease_enter_select_state(), Toast.LENGTH_SHORT).show();

                } else if (binding.etdistrict.getText().toString().equals("District") || binding.etdistrict.getText().toString().equals("City") || binding.etdistrict.getText().toString().equals("") || binding.etdistrict.getText().toString().isEmpty()) {
                    Toast.makeText(this, details1.getPlease_select_district(), Toast.LENGTH_SHORT).show();

                } else if (binding.etSchooltype.getText().toString().equals(schooltypeList.get(0).getTranslatedName())) {
                    Toast.makeText(this, "Please select school type", Toast.LENGTH_SHORT).show();

                } else if (binding.etschool.getText().toString().equals("Select School") || binding.etschool.getText().toString().equals("School Name") || binding.etschool.getText().toString().equals("School Your School") || binding.etschool.getText().toString().isEmpty() || binding.etschool.getText().toString().equals("")) {
                    Toast.makeText(this, details1.getPlease_select_school(), Toast.LENGTH_SHORT).show();

                } else if (binding.etgrade.getText().toString().equals("Select Your Grade") || binding.etgrade.getText().toString().equals("") || binding.etgrade.getText().toString().isEmpty()) {
                    Toast.makeText(this, details1.getSelectYourGrade(), Toast.LENGTH_SHORT).show();

                } else if (image_path == null || image_path.equals("null") || image_path.equals("") || image_path.isEmpty()) {
                    Toast.makeText(this, details1.getUploadProfilePic(), Toast.LENGTH_SHORT).show();

                } else if (!binding.editemail.getText().toString().isEmpty() && !binding.editemail.getText().toString().equals("") && !email.matches(emailPattern)) {

                    Toast.makeText(CompleteStudentProfileWithoutPin.this, details1.getPlease_enter_valid_email(), Toast.LENGTH_SHORT).show();

                } else if (binding.edtusername.getText().toString().startsWith(" ")) {
                    Toast.makeText(this, details1.getEnter_space_username(), Toast.LENGTH_SHORT).show();
                } else if (binding.editemail.getText().toString().startsWith(" ") || binding.editemail.getText().toString().endsWith(" ")) {
                    Toast.makeText(this, details1.getEnter_space_email(), Toast.LENGTH_SHORT).show();
                } else {

                    updateChild();

                }

            } else {

            }
        } else if (id == R.id.submitbutton) {
            String email = binding.editemail.getText().toString().trim();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            Details details1 = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

            if (binding.mainParentLayout.getVisibility() == View.VISIBLE) {

                if (binding.edtusername.getText().toString().equals("") || binding.edtusername.getText().toString().isEmpty()) {
                    Toast.makeText(this, details1.getPlease_enetr_the_name(), Toast.LENGTH_SHORT).show();
                } else if (binding.etStudentGender.getText().toString().equals("Student Gender") || binding.etStudentGender.getText().toString().equals("Select Gender") || binding.etStudentGender.getText().toString().equals("") || binding.etStudentGender.getText().toString().isEmpty() || genderList.get(0).getTranslatedName().equals(binding.etStudentGender.getText().toString())) {
                    Toast.makeText(this, details1.getPlease_select_gender(), Toast.LENGTH_SHORT).show();

                } else if (binding.etstate.getText().toString().equals("State") || binding.etstate.getText().toString().equals("") || binding.etstate.getText().toString().isEmpty()) {
                    Toast.makeText(this, details1.getPlease_enter_select_state(), Toast.LENGTH_SHORT).show();

                } else if (binding.etdistrict.getText().toString().equals("District") || binding.etdistrict.getText().toString().equals("City") || binding.etdistrict.getText().toString().equals("") || binding.etdistrict.getText().toString().isEmpty()) {
                    Toast.makeText(this, details1.getPlease_select_district(), Toast.LENGTH_SHORT).show();

                } else if (binding.etSchooltype.getText().toString().equals(schooltypeList.get(0).getTranslatedName())) {
                    Toast.makeText(this, "Please select school type", Toast.LENGTH_SHORT).show();

                } else if (binding.etschool.getText().toString().equals("Select School") || binding.etschool.getText().toString().equals("School Name") || binding.etschool.getText().toString().equals("School Your School") || binding.etschool.getText().toString().isEmpty() || binding.etschool.getText().toString().equals("")) {
                    Toast.makeText(this, details1.getPlease_select_school(), Toast.LENGTH_SHORT).show();

                } else if (binding.etgrade.getText().toString().equals("Select Your Grade") || binding.etgrade.getText().toString().equals("") || binding.etgrade.getText().toString().isEmpty()) {
                    Toast.makeText(this, details1.getSelectYourGrade(), Toast.LENGTH_SHORT).show();

                } else if (image_path == null || image_path.equals("null") || image_path.equals("") || image_path.isEmpty()) {
                    Toast.makeText(this, details1.getUploadProfilePic(), Toast.LENGTH_SHORT).show();

                } else if (!binding.editemail.getText().toString().isEmpty() && !binding.editemail.getText().toString().equals("") && !email.matches(emailPattern)) {

                    Toast.makeText(CompleteStudentProfileWithoutPin.this, details1.getPlease_enter_valid_email(), Toast.LENGTH_SHORT).show();

                } else if (binding.edtusername.getText().toString().startsWith(" ")) {
                    Toast.makeText(this, details1.getEnter_space_username(), Toast.LENGTH_SHORT).show();
                } else if (binding.editemail.getText().toString().startsWith(" ") || binding.editemail.getText().toString().endsWith(" ")) {
                    Toast.makeText(this, details1.getEnter_space_email(), Toast.LENGTH_SHORT).show();
                } else {

                    updateChild();

                }

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

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(((DashBoardMainActivity) getApplicationContext()).binding.naviagtionContent.homeView, message);
    }

    private void getProfile()
    {
        String childid =  AuroAppPref.INSTANCE.getModelInstance().getUserId();
        String userlangid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",childid);
        map_data.put("user_prefered_language_id",userlangid);

        RemoteApi.Companion.invoke().getStudentData(map_data)
                .enqueue(new Callback<GetStudentUpdateProfile>()
                {
                    @Override
                    public void onResponse(Call<GetStudentUpdateProfile> call, Response<GetStudentUpdateProfile> response)
                    {
                        if (response.isSuccessful()) {


                            String state = response.body().getStatename();
                            String district = response.body().getDistrictname();
                            String email = response.body().getEmailId();
                            ExistGrade = response.body().getStudentclass();
                            ExistGradenew = response.body().getStudentclass();
                            String name = response.body().getStudentName();
                            String schooltype = response.body().getSchoolType();
                            String board = response.body().getBoardType();
                            String schoolmedium = response.body().getTransalted_medium_of_instruction();
                            String isprivatetype = response.body().getIsPrivateTution();
                            String privatetype = response.body().getPrivateTutionType();
                            String schoolname = response.body().getSchoolName();
                            String username = response.body().getUsername();

                            if (!username.equals("null")||!username.equals(null)||!username.equals("")||!username.isEmpty()){
                                binding.editUsername.setText(username);
                            }
                            kycgrade = response.body().getIsKycVerified();

                            if (!state.equals("null")||!state.equals(null)||!state.equals("")||!state.isEmpty()){
                                binding.etstate.setText(state);
                            }
                            if (!district.equals("null")||!district.equals(null)||!district.equals("")||!district.isEmpty()){
                                binding.etdistrict.setText(district);
                            }
                            if (!ExistGrade.equals("null")||!ExistGrade.equals(null)||!ExistGrade.equals("")||!ExistGrade.isEmpty()||!ExistGrade.equals("0")||!ExistGrade.equals(0)){
                                binding.etgrade.setText(ExistGrade);
                            }
                            if (!name.equals("null")||!name.equals(null)||!name.equals("")||!name.isEmpty()){
                                binding.etUsername.setText(name);
                            }
                            if (!email.equals("null")||!email.equals(null)||!email.equals("")||!email.isEmpty()){
                                binding.editemail.setText(email);
                            }
                            if (!schooltype.equals("null")||!schooltype.equals(null)||!schooltype.equals("")||!schooltype.isEmpty()){
                                binding.etSchooltype.setText(schooltype);
                            }
                            if (!board.equals("null")||!board.equals(null)||!board.equals("")||!board.isEmpty()){
                                binding.etSchoolboard.setText(board);
                            }
                            if (!schoolmedium.equals("null")||!schoolmedium.equals(null)||!schoolmedium.equals("")||!schoolmedium.isEmpty()){
                                binding.etSchoolmedium.setText(board);
                            }
                            if (!privatetype.equals("null")||!privatetype.equals(null)||!privatetype.equals("")||!privatetype.isEmpty()){
                                binding.ettutiontype.setText(privatetype);
                            }
                            if (!isprivatetype.equals("null")||!isprivatetype.equals(null)||!isprivatetype.equals("")||!isprivatetype.isEmpty()){
                                binding.ettution.setText(isprivatetype);
                            }
                            if (!schoolname.equals("null")||!schoolname.equals(null)||!schoolname.equals("")||!schoolname.isEmpty()){
                                binding.etschool.setText(schoolname);
                            }










                        }
                        else
                        {
                            Log.d(TAG, "onResponser: "+response.message().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<GetStudentUpdateProfile> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
    private void addNewSchool(String state_id, String district_id, String search)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        ProgressDialog progress = new ProgressDialog(CompleteStudentProfileWithoutPin.this);
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
                                Toast.makeText(CompleteStudentProfileWithoutPin.this,message, Toast.LENGTH_SHORT).show();

                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        }

                        else if (response.isSuccessful()) {

                            progress.dismiss();
                            getschool_id = String.valueOf(response.body().getSchoolID());

                            Toast.makeText(CompleteStudentProfileWithoutPin.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();



                        }
                        else{
                            progress.dismiss();
                            Toast.makeText(CompleteStudentProfileWithoutPin.this, response.message(), Toast.LENGTH_SHORT).show();
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
    private void updateChild()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        String child_name = binding.edtusername.getText().toString();
        String mobileversion =DeviceUtil.getVersionName();
        String mobilemanufacture = DeviceUtil.getManufacturer(this);
        String modelname = DeviceUtil.getModelName(this);
        String buildversion =AppUtil.getAppVersionName();
        String ipaddress = AppUtil.getIpAdress();
        String languageid = prefModel.getUserLanguageId();
        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String childid =  AuroAppPref.INSTANCE.getModelInstance().getUserId();
        String childuserid = childid;
        String gendertype = binding.etStudentGender.getText().toString();

        String parentemail = binding.editemail.getText().toString();
        if (districtList!=null||!districtList.isEmpty()){
            schoolnamesearch = binding.etschool.getText().toString();
        }
        else{
            schoolnamesearch = binding.autoCompleteTextView1.getText().toString();
        }

        if (gradeid == null || gradeid.equals("null") || gradeid.equals("")||gradeid.isEmpty()){
            gradeid = binding.etgrade.getText().toString();
        }
        else{
            gradeid = binding.etgrade.getText().toString();
        }




        RequestBody schoolname_c  = RequestBody.create(MediaType.parse("text/plain"), schoolnamesearch);
        RequestBody gender_c  = RequestBody.create(MediaType.parse("text/plain"), GenderName);
        RequestBody schooltype_c  = RequestBody.create(MediaType.parse("text/plain"), schooltype);
        RequestBody schoolboard_c  = RequestBody.create(MediaType.parse("text/plain"), boardtype);
        RequestBody languagemedium_c  = RequestBody.create(MediaType.parse("text/plain"), Schoolmedium);
        RequestBody privatetution_c  = RequestBody.create(MediaType.parse("text/plain"), Tution);
        RequestBody privatetutiontype_c  = RequestBody.create(MediaType.parse("text/plain"), Tutiontype);
        RequestBody stateid_c  = RequestBody.create(MediaType.parse("text/plain"), stateCode);
        RequestBody districtid_c  = RequestBody.create(MediaType.parse("text/plain"), districtCode);
        RequestBody userid_c  = RequestBody.create(MediaType.parse("text/plain"), childuserid);
        RequestBody languageid_c  = RequestBody.create(MediaType.parse("text/plain"), languageid);
        RequestBody ipaddress_c  = RequestBody.create(MediaType.parse("text/plain"), ipaddress);
        RequestBody buildversion_c  = RequestBody.create(MediaType.parse("text/plain"), buildversion);
        RequestBody modelname_c  = RequestBody.create(MediaType.parse("text/plain"), modelname);
        RequestBody mobilemanufacture_c  = RequestBody.create(MediaType.parse("text/plain"), mobilemanufacture);
        RequestBody mobileversion_c  = RequestBody.create(MediaType.parse("text/plain"), mobileversion);
        RequestBody email_c  = RequestBody.create(MediaType.parse("text/plain"), parentemail);
        RequestBody name_c  = RequestBody.create(MediaType.parse("text/plain"), child_name);
        RequestBody prtnersource  = RequestBody.create(MediaType.parse("text/plain"), prefModel.getPartnersource());
        RequestBody regsource = RequestBody.create(MultipartBody.FORM, "AuroScholr");//model.getPartnerSource()
        RequestBody sharetype = RequestBody.create(MultipartBody.FORM, "telecaller");//model.getPartnerSource()
        RequestBody devicetoken  = RequestBody.create(MediaType.parse("text/plain"), fbnewToken);

        RequestBody  studentname  = RequestBody.create(MediaType.parse("text/plain"), child_name);


        RequestBody language_veriosn  = RequestBody.create(MediaType.parse("text/plain"), "0.0.1");
        RequestBody api_veriosn  = RequestBody.create(MediaType.parse("text/plain"), "0.0.1");
        RequestBody grade_c  = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(gradeid));
        RequestBody emptyfield  = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody isnew_c  = RequestBody.create(MediaType.parse("text/plain"), "1");

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
                .updateaddnewchilddetail(buildversion_c,prtnersource,schoolname_c,email_c,schoolboard_c,
                        gender_c,regsource,sharetype,devicetoken,emptyfield,isnew_c,grade_c,
                        emptyfield,emptyfield,ipaddress_c,mobileversion_c,modelname_c,privatetutiontype_c,privatetution_c,
                        emptyfield,emptyfield,languageid_c,mobilemanufacture_c,stateid_c,districtid_c,name_c,schooltype_c,
                        userid_c,language_veriosn,api_veriosn,languageid_c,studentname,languagemedium_c,lFile)
                .enqueue(new Callback<StudentResponselDataModel>() {
                    @Override
                    public void onResponse(Call<StudentResponselDataModel> call, Response<StudentResponselDataModel> response)
                    {
                        try {
                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(CompleteStudentProfileWithoutPin.this,message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            else if (response.isSuccessful()) {
                                Log.d(TAG, "onImageResponse: ");
                                String status = response.body().getStatus().toString();
                                String msg = response.body().getMessage();
                                if (status.equalsIgnoreCase("success")) {
                                    binding.submitbutton.setVisibility(View.GONE);
                                    Toast.makeText(CompleteStudentProfileWithoutPin.this, msg, Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor1 = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                    editor1.putString("dashboardopen", "true");
                                    editor1.putString("gradeforsubjectpreference", binding.etgrade.getText().toString());
                                    editor1.putString("gradeforsubjectpreference", gradeid);
                                    editor1.putString("gradeforsubjectpreferencefinalprofile", gradeid);
                                    editor1.putString("gradeforsubjectpreferencefinalprofile", binding.etgrade.getText().toString());
                                    editor1.putString("gradeforsubjectpreferencewithoutpin", "true");
                                    editor1.apply();
                                    PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                    prefModel.setUserclass(ConversionUtil.INSTANCE.convertStringToInteger(gradeid));

                                    AuroAppPref.INSTANCE.setPref(prefModel);


                                        AuroScholar.opendashboard();
                                   // openGenricSDK();
                                }
                            }
                            else {

                                Log.d(TAG, "onImageerrorResponse: " + response.errorBody().toString());

                                Toast.makeText(CompleteStudentProfileWithoutPin.this, response.message(), Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            Toast.makeText(CompleteStudentProfileWithoutPin.this, details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<StudentResponselDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onImgFailure: "+t.getMessage());
                    }
                });



    }

    public void openGenricSDK() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int userclass = prefModel.getUserclass();
        AuroScholarInputModel inputModel = new AuroScholarInputModel();
        inputModel.setMobileNumber(prefModel.getUserMobile());
        inputModel.setStudentClass(String.valueOf(userclass));
        inputModel.setPartner_unique_id(prefModel.getPartneruniqueid());
        inputModel.setPartnerSource(prefModel.getPartnersource());
        inputModel.setPartner_api_key(prefModel.getApikey());
        inputModel.setActivity((Activity) this);
        AuroScholar.opendashboard();
       // AuroScholar.startAuroSDK(inputModel);

    }
    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        for (int i = 0; i < genderListString.size(); i++) {
            genderListString.set(i, genderListString.get(i));
        }
        String newschool = binding.autoCompleteTextView1.getText().toString();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.select_dialog_item,genderListString);
        return adapter;
    }
    private void getAddedChild()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        String username = AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel().getUserDetails().get(0).getUserName();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_name",username);
        map_data.put("user_type", String.valueOf(0));
        map_data.put("user_prefered_language_id",languageid);
        RemoteApi.Companion.invoke().getUserCheck(map_data)
                .enqueue(new Callback<CheckUserResModel>()
                {
                    @Override
                    public void onResponse(Call<CheckUserResModel> call, Response<CheckUserResModel> response)
                    {
                        try {


                            if (response.isSuccessful()) {

                                if (!(response.body().getUserDetails() == null) || !(response.body().getUserDetails().isEmpty())) {


                                    listchilds = response.body().getUserDetails();
                                    for (int i = 0; i < listchilds.size(); i++) {
                                        if (listchilds.get(i).getIsMaster().equals("0") || listchilds.get(i).getIsMaster().equals(0)) {
                                            list.add(listchilds.get(i));

                                        }
                                        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                        checkUserResModel = listchilds.get(i);
                                        prefModel.setUserDetailResModel(checkUserResModel);
                                        AuroAppPref.INSTANCE.setPref(prefModel);
                                        checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getUserDetailResModel();
                                    }

//

                                }

                            }
                            else {
                                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(CompleteStudentProfileWithoutPin.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserResModel> call, Throwable t)
                    {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void getSchool(String state_id, String district_id, String search)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();



        districtList.clear();
        HashMap<String,String> map_data = new HashMap<>();

        String stateid = stateCode;
        String districtid = districtCode;
        map_data.put("state_id",stateid);
        map_data.put("district_id",districtid);
        map_data.put("search",search);
        RemoteApi.Companion.invoke().getSchool(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.SchoolDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.SchoolDataModel> call, Response<com.auro.application.home.data.model.SchoolDataModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            genderListString.clear();
                            Log.d(TAG, "onDistrictResponse: "+response.message());
                            for ( int i=0 ;i < response.body().getSchools().size();i++)
                            {
                                int school_id = response.body().getSchools().get(i).getID();
                                String school_name = response.body().getSchools().get(i).getSCHOOL_NAME();

                                Log.d(TAG, "onDistrictResponse: "+school_name);
                                SchoolData districtData = new  SchoolData(school_name,school_id);
                                districtList.add(districtData);
                                genderListString.add(school_name);

                            }

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

    private void getSchoolsearch(String state_id, String district_id, String search)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        ProgressDialog progress = new ProgressDialog(CompleteStudentProfileWithoutPin.this);
        progress.setTitle(details.getProcessing());
        progress.setMessage(details.getProcessing());
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();

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
                            districtList.clear();
                            progress.dismiss();
                            Log.d(TAG, "onDistrictResponse: "+response.message());
                            if (response.body().getSchools()!=null||!response.body().getSchools().isEmpty()) {
                                for (int i = 0; i < response.body().getSchools().size(); i++) {
                                    int school_id = response.body().getSchools().get(i).getID();
                                    String school_name = response.body().getSchools().get(i).getSCHOOL_NAME();

                                    Log.d(TAG, "onDistrictResponse: " + school_name);
                                    SchoolData districtData = new SchoolData(school_name, school_id);
                                    districtList.add(districtData);


                                }
                                if (districtList != null || !districtList.isEmpty()) {
                                    progress.dismiss();
                                    if (districtList != null && districtList.size()!=0) {
                                        binding.etschool.showDropDown();
                                        addDropDownSchool(districtList);
                                    } else {
                                        progress.dismiss();
                                        String schoolsearch = binding.editsearch.getText().toString();
                                        binding.etschool.setText(schoolsearch);
                                        Toast.makeText(CompleteStudentProfileWithoutPin.this, details.getNo_data_found(), Toast.LENGTH_SHORT).show();                                    }

                                }
                                else {
                                    progress.dismiss();
                                    Toast.makeText(CompleteStudentProfileWithoutPin.this, details.getNo_data_found(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                progress.dismiss();
                                String searchname = binding.editsearch.getText().toString();
                                binding.etschool.setText(searchname);
                                Toast.makeText(CompleteStudentProfileWithoutPin.this, details.getNo_data_found(), Toast.LENGTH_SHORT).show();
                            }


                        }
                        else {
                            if (districtList == null || districtList.isEmpty()) {
                                progress.dismiss();
                                String schoolsearch = binding.editsearch.getText().toString();
                                binding.etschool.setText(schoolsearch);
                                Toast.makeText(CompleteStudentProfileWithoutPin.this, details.getNo_data_found(), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.SchoolDataModel> call, Throwable t)
                    {
                        progress.dismiss();
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
                    public void onResponse(Call<GradeDataModel> call, Response<GradeDataModel> response)
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
                    public void onFailure(Call<GradeDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
    private void getAllDistrict(String state_id)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        ProgressDialog progress = new ProgressDialog(CompleteStudentProfileWithoutPin.this);
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

    private void getSchoolmedium(String langid)
    {
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        String langiduser = prefModel.getUserLanguageId();
        langlist.clear();
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
                                langlist.add(districtData);

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

    private void getGender()
    {
        genderList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("key","Gender");
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

                                GenderData districtData = new  GenderData(gender_id,translated_name,gender_name);
                                genderList.add(districtData);
                                binding.etStudentGender.setText(genderList.get(0).getTranslatedName());

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
                                String gender_name = response.body().getResult().get(i).getName();
                                String translated_name = response.body().getResult().get(i).getTranslatedName();

                                TutionData districtData = new  TutionData(gender_id,translated_name,gender_name);
                                tutiontypeList.add(districtData);
                                binding.ettutiontype.setText(tutiontypeList.get(0).getTranslatedName());

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
                                binding.ettution.setText(privatetutionList.get(0).getTranslatedName());

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
                                binding.etSchooltype.setText(schooltypeList.get(0).getTranslatedName());

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
                                binding.etSchoolboard.setText(boardlist.get(0).getTranslatedName());

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

    public void addDropDownSchool(List<SchoolData> districtList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + districtList.size());
        SchoolSpinnerAdapter districtSpinnerAdapter = new SchoolSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, districtList, this);
        binding.etschool.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etschool.setThreshold(1);//will start working from first character
        binding.etschool.setTextColor(Color.BLACK);

    }
    private void addDropDownGender()
    {
        GenderSpinnerAdapter adapter = new GenderSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, genderList, this);
        binding.etStudentGender.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etStudentGender.setThreshold(1);
        binding.etStudentGender.setTextColor(Color.BLACK);//will start working from first character
    }
    private void addDropDownBoard()
    {
        SchoolBoardSpinnerAdapter adapter = new SchoolBoardSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, boardlist, this);
        binding.etSchoolboard.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etSchoolboard.setThreshold(1);
        binding.etSchoolboard.setTextColor(Color.BLACK);//will start working from first character
    }
    private void addDropDownMedium()
    {
        SchoolMediumSpinnerAdapter adapter = new SchoolMediumSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, langlist, this);
        binding.etSchoolmedium.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etSchoolmedium.setThreshold(1);
        binding.etSchoolmedium.setTextColor(Color.BLACK);//will start working from first character
    }
    private void addDropDownTutionType()
    {
        TutionTypeSpinnerAdapter adapter= new TutionTypeSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, tutiontypeList, this);
        binding.ettutiontype.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.ettutiontype.setThreshold(1);
        binding.ettutiontype.setTextColor(Color.BLACK);//will start working from first character
    }
    private void addDropDownPrivateTution()
    {
        PrivateTutionSpinnerAdapter adapter = new PrivateTutionSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, privatetutionList, this);
        binding.ettution.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.ettution.setThreshold(1);
        binding.ettution.setTextColor(Color.BLACK);//will start working from first character
    }
    private void addDropDownSchoolType()
    {
        SchoolTypeSpinnerAdapter adapter = new SchoolTypeSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, schooltypeList, this);
        binding.etSchooltype.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etSchooltype.setThreshold(1);
        binding.etSchooltype.setTextColor(Color.BLACK);//will start working from first character
    }
    private void addDropDownGrade()
    {
        GradeSpinnerAdapter adapter = new GradeSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, gradelist, this);
        binding.etgrade.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etgrade.setThreshold(1);
        binding.etgrade.setTextColor(Color.BLACK);//will start working from first character
    }
    public void addDropDownDistrict(List<DistrictData> districtList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + districtList.size());
        DistrictSpinnerAdapter districtSpinnerAdapter = new DistrictSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, districtList, this);
        binding.etdistrict.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etdistrict.setThreshold(1);//will start working from first character
        binding.etdistrict.setTextColor(Color.BLACK);
    }
    public void addDropDownState(List<StateData> stateList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + stateList.size());
        StateSpinnerAdapter stateSpinnerAdapter = new StateSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, stateList, this);
        binding.etstate.setAdapter(stateSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etstate.setThreshold(1);//will start working from first character
        binding.etstate.setTextColor(Color.BLACK);
    }
    private void askPermission() {

//        ImagePicker.with(CompleteStudentProfileWithoutPin.this)
//                .crop()
//                .compress(1024)
//                .maxResultSize(1080, 1080)
//                .start();

        ImagePicker.create(this) // Pass your activity or fragment
                .returnMode(ReturnMode.ALL) // Set return mode (all images, single image, or none)
                .folderMode(true)           // Enable folder mode (optional)
                .toolbarFolderTitle("Select a folder") // Set folder selection title (optional)
                .toolbarImageTitle("Tap to select")     // Set image selection title (optional)
                .single()                   // Single mode for selecting one image (use multi() for multiple images)
                .limit(1)                   // Limit the number of selected images (optional)
                .showCamera(true)           // Show camera option (optional)
                .start();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.e("StudentProfile", "fragment requestCode=" + requestCode);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            List<Image> images = ImagePicker.getImages(data);
            if (images != null && !images.isEmpty()) {
                // Handle the selected image(s) here
                Image selectedImage = images.get(0); // Get the first selected image
                image_path = selectedImage.getPath(); // Get the image file path

                try {
                    //  Uri uri = data.getData();
                    //image_path = uri.getPath();

                    Log.d(TAG, "imagepathon: " + image_path);
                    Bitmap picBitmap = BitmapFactory.decodeFile(selectedImage.getPath());
                    byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
                    long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
                    int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));


                    if (file_size >= 500) {
                        studentProfileModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
                    } else {
                        studentProfileModel.setImageBytes(bytes);
                    }


                    loadimage(picBitmap);
                } catch (Exception e) {
                    AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
                }
            }

        }
        else if (requestCode == 2404) {
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

            }
//            else if (resultCode == ImagePicker.RESULT_ERROR) {
//                showSnackbarError(ImagePicker.getError(data));
//            }
            else {
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
        if (commonDataModel.getClickType()==STATE)
        {
            StateData stateDataModel = (StateData) commonDataModel.getObject();
            binding.etstate.setText(stateDataModel.getState_name());
            getAllDistrict(stateDataModel.getState_id());
            Log.d("state_id", stateDataModel.getState_id());
            binding.etdistrict.setText("");
            stateCode = stateDataModel.getState_id();

        }
        else if (commonDataModel.getClickType()==DISTRICT)
        {
            DistrictData districtData = (DistrictData) commonDataModel.getObject();
            binding.etdistrict.setText(districtData.getDistrict_name());
            districtCode = districtData.getDistrict_id();
            binding.etschool.setText("");
            getSchool(stateCode,districtCode,Schoolsearch);

        }
        else if (commonDataModel.getClickType()==SCHOOL)
        {
            SchoolData gData = (SchoolData) commonDataModel.getObject();
            binding.etschool.setText(gData.getSCHOOL_NAME());
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
            gradeid = gData.getGrade_id();
            openErrorDialog();

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
            else if (v.getId() == R.id.ettution) {
                binding.ettution.showDropDown();
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
                if (ExistGrade.equals("0")||ExistGrade.equals(0)||ExistGrade.equals("")||ExistGrade.equals("null")||ExistGrade.equals(null)){
                    binding.etgrade.showDropDown();
                }
                else if (ConversionUtil.INSTANCE.convertStringToInteger(ExistGradenew) > 0){
                    binding.etgrade.showDropDown();
                }
                else{
                    openErrorDialog();
                }

            }
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.etStudentGender) {
            binding.etStudentGender.showDropDown();
        }
        else if (v.getId() == R.id.etstate) {
            binding.etstate.showDropDown();
        }
        else if (v.getId() == R.id.etDistict) {
            binding.etdistrict.showDropDown();
        }
        else if (v.getId() == R.id.ettution) {
            binding.ettution.showDropDown();
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
            if (ExistGrade.equals("0")||ExistGrade.equals(0)||ExistGrade.equals("")||ExistGrade.equals("null")||ExistGrade.equals(null)){
                binding.etgrade.showDropDown();
            }
            else if (ConversionUtil.INSTANCE.convertStringToInteger(ExistGradenew) > 0){
                binding.etgrade.showDropDown();
            }
            else{
                openErrorDialog();
            }

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Details details1 = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(details1.getQuizExitTxt())
                .setCancelable(false)
                .setPositiveButton(details1.getYes(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CompleteStudentProfileWithoutPin.this.finish();
                    }
                })
                .setNegativeButton(details1.getNo(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
    private void openErrorDialog() {
        String grade = binding.etgrade.getText().toString();
        if (kycgrade.equals("Pending")||kycgrade == "Pending"){
            if (gradeid == null || gradeid.equals("null") || gradeid.equals("")||gradeid.isEmpty()||gradeid.equals(0)||gradeid.equals("0")){
                gradeid = "0";
            }

          if (ExistGrade == null || ExistGrade.equals("null") || ExistGrade.equals("")||ExistGrade.isEmpty()||ExistGrade.equals(0)||ExistGrade.equals("0")){

          }
          else {
              String message = prefModel.getLanguageMasterDynamic().getDetails().getYou_have_changed_you_grade() + " " + ExistGrade + "th - " + gradeid + "th. " + prefModel.getLanguageMasterDynamic().getDetails().getYou_will_loose_your_current();
              CustomDialogModel customDialogModel = new CustomDialogModel();
              customDialogModel.setContext(CompleteStudentProfileWithoutPin.this);
              customDialogModel.setTitle(prefModel.getLanguageMasterDynamic().getDetails().getInformation() != null ? prefModel.getLanguageMasterDynamic().getDetails().getInformation() : AuroApp.getAppContext().getResources().getString(R.string.information));
              customDialogModel.setContent(message);
              customDialogModel.setTwoButtonRequired(true);
              customDialog = new CustomDialog(CompleteStudentProfileWithoutPin.this, customDialogModel);
              customDialog.setSecondBtnTxt(prefModel.getLanguageMasterDynamic().getDetails().getYes() + "");//Yes
              customDialog.setFirstBtnTxt(prefModel.getLanguageMasterDynamic().getDetails().getNo() + "");//No

              customDialog.setFirstCallcack(new CustomDialog.FirstCallcack() {
                  @Override
                  public void clickNoCallback() {
                      binding.etgrade.setText(ExistGrade);
                      customDialog.dismiss();
                  }
              });

              customDialog.setSecondCallcack(new CustomDialog.SecondCallcack() {
                  @Override
                  public void clickYesCallback() {
                      binding.etgrade.setText(gradeid);
                      customDialog.dismiss();

                  }
              });

              WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
              lp.copyFrom(customDialog.getWindow().getAttributes());
              lp.width = WindowManager.LayoutParams.MATCH_PARENT;
              lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
              customDialog.getWindow().setAttributes(lp);
              Objects.requireNonNull(customDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              customDialog.setCancelable(false);
              customDialog.show();
          }

        }
        else  if (kycgrade.equals("Approve")||kycgrade == "Approve"||kycgrade.equals("Approved")||kycgrade == "Approved"){
            binding.etgrade.setEnabled(false);
        }

    }

}
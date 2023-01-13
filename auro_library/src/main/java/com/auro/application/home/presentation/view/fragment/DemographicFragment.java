package com.auro.application.home.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.ValidationModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.DemographicFragmentLayoutBinding;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.SchoolLangData;
import com.auro.application.home.data.model.SchoolMediumLangDataModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.adapter.SchoolMediumSpinnerAdapter;
import com.auro.application.home.presentation.viewmodel.DemographicViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.DeviceUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.alert_dialog.CustomProgressDialog;
import com.auro.application.util.permission.LocationHandler;
import com.auro.application.util.permission.LocationModel;
import com.auro.application.util.permission.LocationUtil;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringDynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import static com.auro.application.core.common.Status.UPDATE_STUDENT;
import static com.auro.application.util.permission.LocationHandler.REQUEST_CHECK_SETTINGS_GPS;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DemographicFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {

    @Inject
    @Named("DemographicFragment")
    ViewModelFactory viewModelFactory;

    String TAG = "DemographicFragment";
    List<SchoolLangData> mediumlist = new ArrayList<>();
    DemographicFragmentLayoutBinding binding;
    DemographicViewModel demographicViewModel;
    private int pos;
    List<String> genderLines;
    List<String> schooltypeLines;
    List<String> boardLines;
    List<String> languageLines;
    List<String> privateTutionList;
    List<String> privateTutionTypeList;
    DashboardResModel dashboardResModel;
    Resources resources;
    boolean isLocationFine;
    CustomProgressDialog customProgressDialog;

    LocationHandler locationHandlerUpdate;
    GetStudentUpdateProfile demographicResModel = new GetStudentUpdateProfile();
   // DemographicResModel demographicResModel = new DemographicResModel();
   PrefModel prefModel;
   Details details;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        demographicViewModel = ViewModelProviders.of(this, viewModelFactory).get(DemographicViewModel.class);
        binding.setLifecycleOwner(this);
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.DEMOGRAPHIC_FRAGMENT);
        setRetainInstance(true);
        ViewUtil.setLanguageonUi(getActivity());
        AppStringDynamic.setDemoGraphicStrings(binding);
      //  getSchoolmedium();
        return binding.getRoot();
    }


    @Override
    protected void init() {
        binding.toolbarLayout.backArrow.setVisibility(View.VISIBLE);
        setKeyListner();
        if (TextUtil.isEmpty(dashboardResModel.getLatitude()) && TextUtil.isEmpty(dashboardResModel.getLongitude())) {
            /*askPermission();*/
        }

        if (!TextUtil.isEmpty(dashboardResModel.getIsPrivateTution())) {
            demographicResModel.setIsPrivateTution(dashboardResModel.getIsPrivateTution());
        } else {
            demographicResModel.setIsPrivateTution(AppConstant.DocumentType.NO);
            dashboardResModel.setIsPrivateTution(AppConstant.DocumentType.NO);
            demographicResModel.setPrivateTutionType("");
        }

        // Spinner Drop down Gender
        genderLines = Arrays.asList(getResources().getStringArray(R.array.genderlist));
        spinnermethodcall(genderLines, binding.SpinnerGender);
        for (int i = 0; i < genderLines.size(); i++) {
            String gender = genderLines.get(i);
            if (!TextUtil.isEmpty(dashboardResModel.getGender()) && gender.equalsIgnoreCase(dashboardResModel.getGender())) {
                binding.SpinnerGender.setSelection(i);
                break;
            }
        }

        // Spinner Drop down schooltype
        schooltypeLines = Arrays.asList(getResources().getStringArray(R.array.schooltypelist));
        spinnermethodcall(schooltypeLines, binding.SpinnerSchoolType);
        for (int i = 0; i < schooltypeLines.size(); i++) {
            String school = schooltypeLines.get(i);
            if (!TextUtil.isEmpty(dashboardResModel.getSchool_type()) && school.equalsIgnoreCase(dashboardResModel.getSchool_type())) {
                binding.SpinnerSchoolType.setSelection(i);
                break;
            }
        }

        // Spinner Drop down boardlist
        boardLines = Arrays.asList(getResources().getStringArray(R.array.boardlist));
        spinnermethodcall(boardLines, binding.SpinnerBoard);
        for (int i = 0; i < boardLines.size(); i++) {
            String board = boardLines.get(i);
            if (!TextUtil.isEmpty(dashboardResModel.getBoard_type()) && board.equalsIgnoreCase(dashboardResModel.getBoard_type())) {
                binding.SpinnerBoard.setSelection(i);
                break;
            }
        }

        // Spinner Drop down language
        languageLines = Arrays.asList(getResources().getStringArray(R.array.languagelist));
        spinnermethodcall(languageLines, binding.SpinnerLanguageMedium);
        for (int i = 0; i < languageLines.size(); i++) {
            String lang = languageLines.get(i);
            if (!TextUtil.isEmpty(dashboardResModel.getLanguage()) && lang.equalsIgnoreCase(dashboardResModel.getLanguage())) {
                binding.SpinnerLanguageMedium.setSelection(i);
                break;
            }
        }

        // Spinner Drop down tution
        privateTutionList = Arrays.asList(getResources().getStringArray(R.array.privateTutionList));
        spinnermethodcall(privateTutionList, binding.spinnerPrivateTution);
        for (int i = 0; i < privateTutionList.size(); i++) {
            String s = privateTutionList.get(i);
            if (!TextUtil.isEmpty(dashboardResModel.getIsPrivateTution()) && s.equalsIgnoreCase(dashboardResModel.getIsPrivateTution())) {
                binding.spinnerPrivateTution.setSelection(i);
                break;
            }
        }

        // Spinner Drop down tution  Type
        privateTutionTypeList = Arrays.asList(getResources().getStringArray(R.array.privateTypeList));
        spinnermethodcall(privateTutionTypeList, binding.spinnerPrivateType);
        for (int i = 0; i < privateTutionTypeList.size(); i++) {
            String lang = privateTutionTypeList.get(i);
            if (!TextUtil.isEmpty(dashboardResModel.getPrivateTutionType()) && lang.equalsIgnoreCase(dashboardResModel.getPrivateTutionType())) {
                binding.spinnerPrivateType.setSelection(i);
                break;
            }
        }

        prefModel  = AuroAppPref.INSTANCE.getModelInstance();
        details = prefModel.getLanguageMasterDynamic().getDetails();
        demographicResModel.setUserId(prefModel.getStudentData().getUserId());

        demographicResModel.setPartnerSource(AppConstant.PARTNER_AURO_ID);
        demographicResModel.setRegitrationSource(AppConstant.REGISTRATION_SOURCE);
        demographicResModel.setMobileModel(DeviceUtil.getModelName(getActivity()));
        demographicResModel.setMobileVersion(DeviceUtil.getVersionName());
        demographicResModel.setMobileManufacturer(DeviceUtil.getManufacturer(getActivity()));

        if (demographicViewModel != null && demographicViewModel.serviceLiveData().hasObservers()) {
            demographicViewModel.serviceLiveData().removeObservers(this);

        } else {
            observeServiceResponse();
        }

       /* PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel.getUserLanguage().equalsIgnoreCase(AppConstant.LANGUAGE_EN)) {
            setLanguageText(AppConstant.HINDI);
        } else {
            setLanguageText(AppConstant.ENGLISH);
        }*/
        //askPermission();
    }

    private void setLanguageText(String text) {
        binding.toolbarLayout.langEng.setText(text);
    }

    public void spinnermethodcall(List<String> languageLines, AppCompatSpinner spinner) {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, languageLines);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }
    public void spinnermethodcallLanguage(List<SchoolLangData> languageLines, AppCompatSpinner spinner) {
        // Creating adapter for spinner
        ArrayAdapter<SchoolLangData> dataAdapter = new ArrayAdapter<SchoolLangData>(getActivity(), android.R.layout.simple_spinner_item, languageLines);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    @Override
    protected void setToolbar() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected void setListener() {
        binding.submitbutton.setOnClickListener(this);
        binding.toolbarLayout.langEng.setOnClickListener(this);
        binding.toolbarLayout.backArrow.setOnClickListener(this);

        binding.SpinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                demographicResModel.setGender(binding.SpinnerGender.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.SpinnerSchoolType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                demographicResModel.setSchoolType(binding.SpinnerSchoolType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        binding.SpinnerLanguageMedium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                demographicResModel.setLanguage(binding.SpinnerLanguageMedium.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.SpinnerBoard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                demographicResModel.setBoardType(binding.SpinnerBoard.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        binding.spinnerPrivateTution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String value = binding.spinnerPrivateTution.getSelectedItem().toString();
                demographicResModel.setIsPrivateTution(binding.spinnerPrivateTution.getSelectedItem().toString());
                if (value.equalsIgnoreCase(AppConstant.DocumentType.YES)) {
                    binding.spinnerPrivateType.setVisibility(View.VISIBLE);
                    binding.tvPrivateType.setVisibility(View.VISIBLE);
                    binding.privateTypeArrow.setVisibility(View.VISIBLE);
                } else {
                    binding.spinnerPrivateType.setVisibility(View.GONE);
                    binding.tvPrivateType.setVisibility(View.GONE);
                    binding.privateTypeArrow.setVisibility(View.GONE);
                    demographicResModel.setPrivateTutionType("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.spinnerPrivateType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String value = binding.spinnerPrivateType.getSelectedItem().toString();
                if (value.equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_PRIVATE_TUTION)) {
                    demographicResModel.setPrivateTutionType("");
                } else {
                    demographicResModel.setPrivateTutionType(binding.spinnerPrivateType.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }


    @Override
    protected int getLayout() {
        return R.layout.demographic_fragment_layout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
        }
        init();
        setToolbar();
        setListener();
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

    }


    private void observeServiceResponse() {

        demographicViewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    //For ProgressBar
                    handleProgress(0, "");
                    break;

                case SUCCESS:
                    if (responseApi.apiTypeStatus == UPDATE_STUDENT) {
                        GetStudentUpdateProfile studentProfileModel = new GetStudentUpdateProfile();
                        if (!demographicResModel.getError()) {
                            handleProgress(1, "");

                            getActivity().getSupportFragmentManager().popBackStack();
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            showSnackbarError(details.getDefaultError() != null ? details.getDefaultError() : getString(R.string.default_error));
                        }

                    }

                    break;

                case NO_INTERNET:
//On fail
                    handleProgress(2, (String) responseApi.data);
                    break;

                case AUTH_FAIL:
                case FAIL_400:
                default:
                    handleProgress(1, (String) responseApi.data);
                    showSnackbarError(details.getDefaultError() != null ? details.getDefaultError() : getString(R.string.default_error));
                    break;
            }

        });
    }


    private void handleProgress(int value, String message) {
        if (value == 0) {

            binding.submitbutton.setText("");
            binding.submitbutton.setEnabled(false);
            binding.progressBar.setVisibility(View.VISIBLE);

        } else if (value == 1) {

            binding.submitbutton.setText(details.getSubmit() != null ?details.getSubmit():getActivity().getResources().getString(R.string.submit));
            binding.submitbutton.setEnabled(true);
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submitbutton) {
            ValidationModel validation = demographicViewModel.homeUseCase.validateDemographic(demographicResModel);
            if (validation.isStatus()) {
                demographicViewModel.getDemographicData(demographicResModel);
            } else {
                showSnackbarError(validation.getMessage());
            }
        } else if (v.getId() == R.id.lang_eng) {
            reloadFragment();

        } else if (v.getId() == R.id.back_arrow) {
            getActivity().getSupportFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().popBackStack();
        }


    }


    private void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    private void askPermission() {
        String rationale = "Please give location permission for provide you the better service.";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");
        Permissions.check(getActivity(), PermissionUtil.mLocationPermission, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                getCurrentLocation();
                AppLogger.e(TAG, "Location Permission Granted");
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
                //getActivity().getSupportFragmentManager().popBackStack();
                AppLogger.e(TAG, "Location Permission Denied");

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    AppLogger.e(TAG, "GPS on Allow");
                    getCurrentLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    AppLogger.e(TAG, "GPS on Denied");
                    break;
                default:
                    // do nothing here
                    break;

            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationHandlerUpdate != null) {
            locationHandlerUpdate.stopReceivingLocation();
        }
    }

    public void getCurrentLocation() {
        locationHandlerUpdate = new LocationHandler();
        locationHandlerUpdate.setUpGoogleClient(getActivity());
        if (LocationUtil.isGPSEnabled()) {
            callServiceWhenLocationReceived();
        }
    }

    private void callServiceWhenLocationReceived() {
        LocationModel locationModel = LocationUtil.getLocationData();
        openProgressDialog();
        if (locationModel != null && locationModel.getLatitude() != null && !locationModel.getLatitude().isEmpty()) {
            AppLogger.e(TAG, "Location Found");
            if (customProgressDialog != null) {
                customProgressDialog.dismiss();
            }
            demographicResModel.setLatitude(locationModel.getLatitude());
            demographicResModel.setLongitude(locationModel.getLongitude());
        } else {
            Handler handler = new Handler();
            handler.postDelayed(this::callServiceWhenLocationReceived, 2000);
        }
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

    private void setKeyListner() {
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    private void getSchoolmedium()
    {
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        String langiduser = prefModel.getUserLanguageId();
        mediumlist.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("language_id", langiduser);
        RemoteApi.Companion.invoke().getSchoolmedium(map_data)
                .enqueue(new Callback<SchoolMediumLangDataModel>() {
                    @Override
                    public void onResponse(Call<SchoolMediumLangDataModel> call, Response<SchoolMediumLangDataModel> response)
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
                            spinnermethodcallLanguage(mediumlist, binding.SpinnerLanguageMedium);

                        }


                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.SchoolMediumLangDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }


}

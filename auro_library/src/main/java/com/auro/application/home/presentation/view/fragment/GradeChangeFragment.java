package com.auro.application.home.presentation.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.FragmentGradeChangeDialogBinding;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.SelectLanguageModel;
import com.auro.application.home.data.model.response.ChangeGradeResModel;
import com.auro.application.home.data.model.response.CheckUserValidResModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.SplashScreenAnimationActivity;
import com.auro.application.home.presentation.view.activity.OtpActivity;
import com.auro.application.home.presentation.view.activity.SplashScreenAnimationActivity;
import com.auro.application.home.presentation.viewmodel.GradeChangeViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.CustomDialog;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.strings.AppStringDynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;


public class GradeChangeFragment extends BaseFragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    @Inject
    @Named("GradeChangeDialog")
    ViewModelFactory viewModelFactory;
    FragmentGradeChangeDialogBinding binding;
    GradeChangeViewModel viewModel;

    String TAG = "GradeChangeFragment";


    int classtype = 0;
    int studentClass = 0;

    // TODO: Rename and change types of parameters
    private String source;
    List<String> classlist = new ArrayList<>();
    List<String> classlistSecond = new ArrayList<>();

    HashMap<String, Integer> classesMap = new HashMap<>();
    HashMap<String, String> classesNewMap = new HashMap<>();
    OnClickButton onClickButton;
    CustomDialog customDialog;
    DashboardResModel dashboardResModel;
    PrefModel prefModel;


    public GradeChangeFragment() {
    }

    public GradeChangeFragment(OnClickButton onClickButton) {
        this.onClickButton = onClickButton;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            source = getArguments().getString(AppConstant.COMING_FROM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        //((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        DaggerWrapper.getComponent(getActivity()).doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GradeChangeViewModel.class);
        ViewUtil.setLanguageonUi(getActivity());
        setRetainInstance(true);
        init();
        setListener();

      /*  if(!TextUtil.isEmpty(source) && source.equalsIgnoreCase(AppConstant.SENDING_DATA.STUDENT_PROFILE))
        {

        }else {

        }*/
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_yes) {
            if (studentClass != 0) {

                AppLogger.e("Grade change--", "step 1");
                changeGrade();
            } else {
                showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getPlease_select_the_grade());
            }
        }
    }

    private void openErrorDialog() {

        String message = prefModel.getLanguageMasterDynamic().getDetails().getYou_have_changed_you_grade()+"" + prefModel.getUserclass() + "th - " + studentClass + "th."+prefModel.getLanguageMasterDynamic().getDetails().getYou_will_loose_your_current();
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(getActivity());
        customDialogModel.setTitle(prefModel.getLanguageMasterDynamic().getDetails().getInformation() != null ? prefModel.getLanguageMasterDynamic().getDetails().getInformation() : AuroApp.getAppContext().getResources().getString(R.string.information));
        customDialogModel.setContent(message);
        customDialogModel.setTwoButtonRequired(true);
        customDialog = new CustomDialog(getActivity(), customDialogModel);
        customDialog.setSecondBtnTxt(prefModel.getLanguageMasterDynamic().getDetails().getYes()+"");//Yes
        customDialog.setFirstBtnTxt(prefModel.getLanguageMasterDynamic().getDetails().getNo()+"");//No
        customDialog.setFirstCallcack(new CustomDialog.FirstCallcack() {
            @Override
            public void clickNoCallback() {
                customDialog.dismiss();
            }
        });

        customDialog.setSecondCallcack(new CustomDialog.SecondCallcack() {
            @Override
            public void clickYesCallback() {
                customDialog.dismiss();
                callChangeGradeApi();
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


    @Override
    protected void init() {
        ((DashBoardMainActivity) getActivity()).setProgressVal();

        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.e("chhonker-- grade change--", "value here");

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
        if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
        }

        selectClassBuSpinner();

        AppStringDynamic.setGradeChangeScreenStrings(binding);
    }

    @Override
    public void onResume() {
        super.onResume();

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (!prefModel.isLogin()) {
            Intent intent = new Intent(getActivity(), SplashScreenAnimationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finishAffinity();
        }
    }

    @Override
    protected void setToolbar() {
    }

    @Override
    protected void setListener() {
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.GRADE_CHANGE_FRAGMENT);

        binding.btnYes.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_grade_change_dialog;
    }

    public void spinnermethodcall(List<String> languageLines, AppCompatSpinner spinner) {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, languageLines);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    private void setClassInPref(int studentClass) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.getUserclass();
         prefModel.setUserclass(String.valueOf(studentClass));
        AuroAppPref.INSTANCE.setPref(prefModel);
    }

    public void selectClassBuSpinner() {
        // Spinner Drop down Class

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.e(TAG, "Step 1 user type--" + prefModel.getUserType());



        String[] listArrayLanguage = getResources().getStringArray(R.array.classes);
        List<String> classList = prefModel.getStudentClasses();
        if (!TextUtil.checkListIsEmpty(classList)) {
            for (int i = 0; i < 12; i++) {
                SelectLanguageModel selectLanguageModel = new SelectLanguageModel();
                selectLanguageModel.setCheck(false);
                String classname = classList.get(i);
                selectLanguageModel.setStudentClassName(classname);
                if (classname.equalsIgnoreCase("1")) {
                    selectLanguageModel.setLanguage(classList.get(i) + "st");
                } else if (classname.equalsIgnoreCase("2")) {
                    selectLanguageModel.setLanguage(classList.get(i) + "nd");
                } else if (classname.equalsIgnoreCase("3")) {
                    selectLanguageModel.setLanguage(classList.get(i) + "rd");
                } else {
                    selectLanguageModel.setLanguage(classList.get(i) + "th");
                }

                classlist.add(selectLanguageModel.getLanguage());
                classesNewMap.put("" + (i + 1), classList.get(i));
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                SelectLanguageModel selectLanguageModel = new SelectLanguageModel();
                String classname = "" + i;
                if (classname.equalsIgnoreCase("1")) {
                    selectLanguageModel.setLanguage(classname + "st");
                } else if (classname.equalsIgnoreCase("2")) {
                    selectLanguageModel.setLanguage(classname + "nd");
                } else if (classname.equalsIgnoreCase("3")) {
                    selectLanguageModel.setLanguage(classname + "rd");
                } else {
                    selectLanguageModel.setLanguage(classname + "th");
                }
                selectLanguageModel.setCheck(false);
                classlist.add(selectLanguageModel.getLanguage());
                classesNewMap.put("" + i, "" + i);
            }
        }


        AppLogger.e(TAG, "Step 5 user --");
        classlist.add(0, prefModel.getLanguageMasterDynamic().getDetails().getPlease_select_class_student());//"Please Select your class"
        spinnermethodcall(classlist, binding.SpinnerClass);
        binding.SpinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                classtype = position;
                if (position > 0) {
                    studentClass = ConversionUtil.INSTANCE.convertStringToInteger(classesNewMap.get("" + position));

                    PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                    prefModel.setUserclass(String.valueOf(studentClass));
                    AuroAppPref.INSTANCE.setPref(prefModel);
                } else {
                    studentClass = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AppLogger.e(TAG, "Step 8 user --");
    }

    public interface OnClickButton {
        void onClickListener();
    }

    private void changeGrade() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        //  int studentClass = ConversionUtil.INSTANCE.convertStringToInteger(prefModel.getStudentData().getGrade());
        AppLogger.e("Grade change--", "step 2 -- setudent class -" + studentClass);
        if (studentClass != 0) {
            AppLogger.e("Grade change--", "step 3");
            if (source.equalsIgnoreCase(AppConstant.Source.DASHBOARD_NAVIGATION)) {
                AppLogger.e("Grade change--", "step 4 ");
                openErrorDialog();
            } else if (source.equalsIgnoreCase(AppConstant.SENDING_DATA.STUDENT_PROFILE)) {
                AppLogger.e("Grade change--", "step 5 ");
                openErrorDialog();
            } else {
                openErrorDialog();
                AppLogger.e("Grade change--", "step 6 ");
                // onClickButton.onClickListener();
                //  ((DashBoardMainActivity) getActivity()).popBackStack();
            }
        }
        else {
            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getPlease_select_class_student());
        }
    }


    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    /*do coding here*/
                    break;

                case SUCCESS:
                    if (isVisible()) {
                        handleProgress(1);
                        handleApiRes(responseApi);
                        ((DashBoardMainActivity) getActivity()).setupNavigation();
                    }
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                    if (isVisible()) {
                        handleProgress(1);
                        showSnackbarError((String) responseApi.data);
                    }
                    break;
                default:
                    if (isVisible()) {
                        handleProgress(1);
                        showSnackbarError((String) responseApi.data);
                    }
                    break;
            }

        });
    }


    private void handleProgress(int i) {
        switch (i) {
            case 0:
                binding.progressbar.pgbar.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.progressbar.pgbar.setVisibility(View.GONE);
                break;
        }
    }

    private void callChangeGradeApi() {
        handleProgress(0);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        CheckUserValidResModel reqModel = new CheckUserValidResModel();
        reqModel.setMobileNo(prefModel.getUserId());
        reqModel.setStudentClass("" + studentClass);
        prefModel.setUserclass(String.valueOf(studentClass));
        AuroAppPref.INSTANCE.setPref(prefModel);
        viewModel.changeGrade(reqModel);
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void handleApiRes(ResponseApi responseApi) {
        ChangeGradeResModel resModel = (ChangeGradeResModel) responseApi.data;
        if (!resModel.getError()) {
            setClassInPref(studentClass);
            //  setClassInPref(ConversionUtil.INSTANCE.convertStringToInteger(resModel.getNewgrade()));
            if (isVisible()) {
                Intent i = new Intent(getActivity(), DashBoardMainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

                //((DashBoardMainActivity) getActivity()).popBackStack();
            }
        } else {
            showSnackbarError(resModel.getMessage());
            // setClassInPref(ConversionUtil.INSTANCE.convertStringToInteger(resModel.getOldgrade()));
           /* if (isVisible()) {
                ((DashBoardMainActivity) getActivity()).showSnackbar(resModel.getMessage());
            }*/
        }


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    private List<String> getClassList() {
        List<String> classesList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            classesList.add("" + i);
        }
        return classesList;

    }


}
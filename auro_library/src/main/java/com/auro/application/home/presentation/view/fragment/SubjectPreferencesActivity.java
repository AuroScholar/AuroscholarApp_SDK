package com.auro.application.home.presentation.view.fragment;

import static com.auro.application.core.common.Status.FETCH_STUDENT_PREFERENCES_API;
import static com.auro.application.core.common.Status.SUBJECT_PREFRENCE_LIST_API;
import static com.auro.application.core.common.Status.UPDATE_PREFERENCE_API;
import static com.auro.application.core.common.Status.UPDATE_STUDENT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.SubjectPrefLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.UpdatePrefReqModel;
import com.auro.application.home.data.model.response.CategorySubjectResModel;
import com.auro.application.home.data.model.response.FetchStudentPrefResModel;
import com.auro.application.home.data.model.response.SubjectPreferenceResModel;
import com.auro.application.home.data.model.response.UpdatePrefResModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.adapter.SubjectPrefAdapter;
import com.auro.application.home.presentation.viewmodel.HomeViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;


public class SubjectPreferencesActivity extends BaseActivity implements CommonCallBackListner, View.OnClickListener {

    @Inject
    @Named("SubjectPreferencesActivity")
    ViewModelFactory viewModelFactory;
    SubjectPrefLayoutBinding binding;
    HomeViewModel homeViewModel;
    String TAG = SubjectPreferencesActivity.class.getSimpleName();
    SubjectPreferenceResModel subjectPreferenceResModel;
    Details details;
    public SubjectPreferencesActivity() {// Required empty public constructor
    }


    HashMap<String, CategorySubjectResModel> subjectResModelHashMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayout());
       // ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        DaggerWrapper.getComponent(this).doInjection(this);

        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);
        binding.setLifecycleOwner(this);
        init();
        setListener();

        SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor.putString("statusparentprofile", "false");
        editor.putString("isLogin","true");
        editor.putString("statusfillstudentprofile", "false");
        editor.putString("statussetpasswordscreen", "false");
        editor.putString("statuschoosegradescreen", "false");
        editor.putString("statuschoosedashboardscreen", "false");
        editor.putString("statussubjectpref","true");
        editor.putString("statusopenprofileteacher", "false");
        editor.putString("statusopendashboardteacher", "false");
        editor.putString("statusopenprofilewithoutpin", "false");
        editor.apply();

    }


    @Override
    protected void init() {
        AppUtil.loadAppLogo(binding.auroScholarLogo,this);
        binding.btContinue.setOnClickListener(this);
        binding.icClose.setOnClickListener(this);
        setProgressVal();
        callFetchUserPreference();
        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        AppStringDynamic.setSubjectPrefPageStrings(binding);

    }

    public void setProgressVal() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setDashboardaApiNeedToCall(true);
        AuroAppPref.INSTANCE.setPref(prefModel);
    }


    @Override
    protected void setListener() {
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.PARTNERS_FRAGMENT);
        if (homeViewModel != null && homeViewModel.serviceLiveData().hasObservers()) {
            homeViewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    public void setSubjectAdapter(SubjectPreferenceResModel subjectPreferenceResModel) {
        AppLogger.e("setSubjectAdapter- ", "Step 0");
        AppLogger.v("setSubjectAdapter-", "Call---->" + subjectPreferenceResModel.getSubjects().size());
        if (TextUtil.checkListIsEmpty(subjectPreferenceResModel.getSubjects())) {
            return;
        }
        AppLogger.e("setSubjectAdapter- ", "Step 1");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        FetchStudentPrefResModel fetchStudentPrefResModel = prefModel.getFetchStudentPrefResModel();
        if (fetchStudentPrefResModel != null && !TextUtil.checkListIsEmpty(fetchStudentPrefResModel.getSubjects())) {
            AppLogger.e("setSubjectAdapter- ", "Step 2");
            for (CategorySubjectResModel resModel : fetchStudentPrefResModel.getSubjects()) {
                AppLogger.e("setSubjectAdapter- ", "Step 2 .1 ");
                for (CategorySubjectResModel categorySubjectResModel : subjectPreferenceResModel.getSubjects()) {
                    AppLogger.e("setSubjectAdapter- ", "Step 2 .2-- " + categorySubjectResModel.getSubjectname());
                    if (resModel.getId().equalsIgnoreCase(categorySubjectResModel.getId())) {
                        categorySubjectResModel.setSelected(true);
                        subjectResModelHashMap.put(resModel.getId(), resModel);
                        if (resModel.getAttempted() > 0) {
                            categorySubjectResModel.setLock(true);
                        }
                    }

                }
            }




            AppLogger.e("setSubjectAdapter- ", "Step 3");
        }

        try {
            for (CategorySubjectResModel categorySubjectResModel : subjectPreferenceResModel.getSubjects()) {
                AppLogger.e("setSubjectAdapter- ", "Step 3" + categorySubjectResModel.getSubjectCode());
                categorySubjectResModel.setBackgroundImage(getImageFromCode(categorySubjectResModel));
                AppLogger.e("setSubjectAdapter- ", "Step 4" + getImageFromCode(categorySubjectResModel).toString());

            }
        } catch (Exception e) {
            AppLogger.e("setSubjectAdapter- ", "Exception--" + e.getMessage());

        }

        AppLogger.e("setSubjectAdapter- ", "Step 4");
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.subjectRecyclerview.setLayoutManager(layoutManager);
        binding.subjectRecyclerview.setHasFixedSize(true);
        SubjectPrefAdapter subjectadapter = new SubjectPrefAdapter(subjectPreferenceResModel.getSubjects(), this);
        binding.subjectRecyclerview.setAdapter(subjectadapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.subject_pref_layout;
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        AppLogger.e(TAG, "commonEventListner");
        switch (commonDataModel.getClickType()) {
            case ITEM_CLICK:
                CategorySubjectResModel resModel = (CategorySubjectResModel) commonDataModel.getObject();
                subjectResModelHashMap.put(resModel.getId(), resModel);
                break;

            case REMOVE_ITEM:
                CategorySubjectResModel categorySubjectResModel = (CategorySubjectResModel) commonDataModel.getObject();
                subjectResModelHashMap.remove(categorySubjectResModel.getId());
                break;
        }
    }

    @Override
    public void onClick(View v) {
        AppLogger.e(TAG, "On click called");
        int id = v.getId();
        if (id == R.id.ic_close) {
            finish();
            startDashboardActivity();
        } else if (id == R.id.bt_continue) {
            callUpdatePrefApi();
        }
    }

    private void observeServiceResponse() {
        homeViewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    binding.progressBarButton.setVisibility(View.GONE);
                    AppLogger.e("observeServiceResponse ", "Step 0");
                    if (responseApi.apiTypeStatus == SUBJECT_PREFRENCE_LIST_API) {
                        AppLogger.e("observeServiceResponse ", "Step 1");
                        subjectPreferenceResModel = (SubjectPreferenceResModel) responseApi.data;
                        if (!TextUtil.checkListIsEmpty(subjectPreferenceResModel.getSubjects())) {
                            AppLogger.e("observeServiceResponse ", "Step 2");
                            setSubjectAdapter(subjectPreferenceResModel);
                            handleProgress(1, "");

                        } else {
                            handleProgress(4, details.getNo_data_found() != null ? details.getNo_data_found() : AuroApp.getAppContext().getResources().getString(R.string.no_data_found));
                            AppLogger.e("observeServiceResponse ", "Step 3");
                        }
                    } else if (responseApi.apiTypeStatus == UPDATE_STUDENT) {
                        AppLogger.e(TAG, "UPDATE_STUDENT ");


                    } else if (responseApi.apiTypeStatus == FETCH_STUDENT_PREFERENCES_API) {

                        FetchStudentPrefResModel fetchStudentPrefResModel = (FetchStudentPrefResModel) responseApi.data;
                        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                        prefModel.setFetchStudentPrefResModel(fetchStudentPrefResModel);
                        AuroAppPref.INSTANCE.setPref(prefModel);
                        callSubjectListPreference();
                    } else if (responseApi.apiTypeStatus == UPDATE_PREFERENCE_API) {
                        UpdatePrefResModel updatePrefResModel = (UpdatePrefResModel) responseApi.data;
                        if (updatePrefResModel.getError()) {
                            // callSubjectListPreference();
                            ViewUtil.showSnackBar(binding.getRoot(), updatePrefResModel.getMessage());
                        } else {
                            ViewUtil.showSnackBar(binding.getRoot(), updatePrefResModel.getMessage());
                            startDashboardActivity();
                        }
                    }
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                    AppLogger.e("observeServiceResponse ", "Step 4");
                    handleProgress(2, (String) responseApi.data);
                    binding.progressBarButton.setVisibility(View.GONE);
                    break;
                default:
                    AppLogger.e("observeServiceResponse ", "Step 5");
                    handleProgress(2, (String) responseApi.data);
                    binding.progressBarButton.setVisibility(View.GONE);
                    break;
            }

        });
    }


    private void handleProgress(int value, String message) {
        AppLogger.e(TAG, "handleProgress calling - " + value + "-message-" + message);
        if (value == 0) {
            binding.errorConstraint.setVisibility(View.GONE);
            binding.subjectRecyclerview.setVisibility(View.GONE);
            binding.progressBar2.setVisibility(View.VISIBLE);

        } else if (value == 1) {
            binding.errorConstraint.setVisibility(View.GONE);
            binding.progressBar2.setVisibility(View.GONE);
            binding.subjectRecyclerview.setVisibility(View.VISIBLE);

        } else if (value == 2) {
            binding.errorConstraint.setVisibility(View.VISIBLE);
            binding.subjectRecyclerview.setVisibility(View.GONE);
            binding.progressBar2.setVisibility(View.GONE);
            binding.errorLayout.textError.setText(message);
            binding.errorLayout.btRetry.setVisibility(View.VISIBLE);
            binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleProgress(0, "");
                }
            });
        } else if (value == 4) {
            binding.errorConstraint.setVisibility(View.VISIBLE);
            binding.subjectRecyclerview.setVisibility(View.GONE);
            binding.progressBar2.setVisibility(View.GONE);
            binding.errorLayout.textError.setText(message);
            binding.errorLayout.btRetry.setVisibility(View.GONE);
        }

    }

    void callSubjectListPreference() {

        homeViewModel.checkInternetForApi(SUBJECT_PREFRENCE_LIST_API, "");
    }


    void callUpdatePrefApi() {
        /*Make preferences*/
        if (subjectResModelHashMap.size() < 5) {
            ViewUtil.showSnackBar(binding.getRoot(),details.getPlease_select_the_five_subject() != null ?details.getPlease_select_the_five_subject() :AuroApp.getAppContext().getResources().getString(R.string.subject_brief));
            return;
        }

        List<CategorySubjectResModel> categorySubjectResModels = new ArrayList<>();
        String preference = "";
        for (String key : subjectResModelHashMap.keySet()) {
            categorySubjectResModels.add(subjectResModelHashMap.get(key));
        }
        for (int i = 0; i < categorySubjectResModels.size(); i++) {
            if (i == categorySubjectResModels.size() - 1) {  //1
                preference = preference + categorySubjectResModels.get(i).getId();
            } else {
                preference = preference + categorySubjectResModels.get(i).getId() + ",";
            }
        }

        binding.progressBarButton.setVisibility(View.VISIBLE);
        UpdatePrefReqModel reqModel = new UpdatePrefReqModel();


        reqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getUserId());
        reqModel.setUserPreferedLanguageId(Integer.valueOf(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        reqModel.setPreference(preference);
        homeViewModel.checkInternetForApi(Status.UPDATE_PREFERENCE_API, reqModel);
        SharedPreferences preferences = getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("gradeforsubjectpreference");
        editor.apply();

    }


    void callFetchUserPreference() {
        handleProgress(0, "");
        FetchStudentPrefReqModel fetchStudentPrefReqModel = new FetchStudentPrefReqModel();
        fetchStudentPrefReqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getUserId());
        homeViewModel.checkInternetForApi(FETCH_STUDENT_PREFERENCES_API, fetchStudentPrefReqModel);
    }

    private void startDashboardActivity() {
        finish();
        Intent i = new Intent(this, DashBoardMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getImageFromCode(CategorySubjectResModel categorySubjectResModel) {
        switch (categorySubjectResModel.getSubjectCode()) {
            case AppConstant.SubjectCodes.Mathematics:
                return this.getDrawable(R.drawable.ic_maths_horizontal);

            case AppConstant.SubjectCodes.English:
                return this.getDrawable(R.drawable.ic_english_horizontal);

            case AppConstant.SubjectCodes.Hindi:
                return this.getDrawable(R.drawable.ic_hindi_horizontal);

            case AppConstant.SubjectCodes.Social_Science:
                return this.getDrawable(R.drawable.ic_social_science_horizontal);

            case AppConstant.SubjectCodes.Science:
                return this.getDrawable(R.drawable.ic_science_horizontal);

            case AppConstant.SubjectCodes.Physics:
                return this.getDrawable(R.drawable.ic_physics_horizontal);

            case AppConstant.SubjectCodes.Chemistry:
                return this.getDrawable(R.drawable.ic_chemistry_horizontal);

            case AppConstant.SubjectCodes.Biology:
                return this.getDrawable(R.drawable.ic_biology_horizontal);

            case AppConstant.SubjectCodes.History:
                return this.getDrawable(R.drawable.ic_history_horizontal);

            case AppConstant.SubjectCodes.Political_Science:
                return this.getDrawable(R.drawable.ic_political_science_horizontal);

            case AppConstant.SubjectCodes.Geography:
                return this.getDrawable(R.drawable.ic_geographic_horizontal);

            default:
                return this.getDrawable(R.drawable.ic_physics_horizontal);

        }

    }
}
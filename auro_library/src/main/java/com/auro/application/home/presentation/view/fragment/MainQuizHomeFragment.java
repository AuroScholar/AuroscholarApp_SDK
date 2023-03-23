package com.auro.application.home.presentation.view.fragment;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.auro.application.ChatActivity;
import com.auro.application.R;
import com.auro.application.RealTimeFaceDetection.CameraxActivity;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.network.URLConstant;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.databinding.FragmentMainQuizHomeBinding;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.AssignmentResModel;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.DashboardResponselDataModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.QuizResModel;
import com.auro.application.home.data.model.SelectChapterQuizModel;
import com.auro.application.home.data.model.SubjectResModel;
import com.auro.application.home.data.model.passportmodels.PassportQuizMonthModel;
import com.auro.application.home.data.model.response.FetchStudentPrefResModel;

import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.InstructionsResModel;
import com.auro.application.home.data.model.response.SlabsResModel;
import com.auro.application.home.data.model.response.StudentKycStatusResModel;
import com.auro.application.home.data.model.signupmodel.InstructionModel;
import com.auro.application.home.data.model.signupmodel.InstructionModel;
import com.auro.application.home.presentation.view.activity.AppLanguageActivity;
import com.auro.application.home.presentation.view.activity.CameraActivity;
import com.auro.application.home.presentation.view.activity.ChooseGradeActivity;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithoutPin;
import com.auro.application.home.presentation.view.activity.SDKActivity;
import com.auro.application.home.presentation.view.activity.StudentMainDashboardActivity;
import com.auro.application.home.presentation.view.activity.WebActivity;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.adapter.ChapterSelectAdapter;
import com.auro.application.home.presentation.view.adapter.LevelInfoAdapter;
import com.auro.application.home.presentation.view.adapter.SubjectSelectAdapter;
import com.auro.application.home.presentation.viewmodel.QuizViewModel;
import com.auro.application.quiz.presentation.view.fragment.QuizTestNativeFragment;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.DateUtil;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.SeekbarWithIntervals;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.InstructionDialog;
import com.auro.application.util.alert_dialog.disclaimer.QuizDisclaimerDialog;
import com.auro.application.util.firebaseAnalytics.AnalyticsRegistry;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringDynamic;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.instabug.apm.APM;
import com.instabug.bug.BugReporting;
import com.instabug.crash.CrashReporting;
import com.instabug.library.Feature;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.instabug.library.ui.onboarding.WelcomeMessage;
import com.instabug.library.visualusersteps.State;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.auro.application.core.common.Status.ASSIGNMENT_STUDENT_DATA_API;
import static com.auro.application.core.common.Status.DASHBOARD_API;
import static com.auro.application.core.common.Status.GET_INSTRUCTIONS_API;
import static com.auro.application.core.common.Status.GET_SLABS_API;
import static com.auro.application.core.common.Status.LISTNER_FAIL;
import static com.auro.application.home.presentation.view.fragment.CongratulationsDialog.commonCallBackListner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainQuizHomeFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    @Named("MainQuizHomeFragment")
    ViewModelFactory viewModelFactory;
    FragmentMainQuizHomeBinding binding;
    QuizViewModel quizViewModel;
    DashboardResModel dashboardResModel;
    String getkycstatus;
    String TAG = MainQuizHomeFragment.class.getSimpleName();
    QuizResModel quizResModel;
    AssignmentReqModel assignmentReqModel;
    AssignmentResModel testAssignmentResModel;
    ProgressDialog quizProgressDialog;
    InstructionDialog customDialog;
    SlabsResModel slabsResModel;
    int level = 0;
    PrefModel prefModel;
    Details details;

    public MainQuizHomeFragment() {
        // Required empty public constructor
    }

    ChapterSelectAdapter adapter;
    List<SelectChapterQuizModel> laugList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (binding != null) {
            return binding.getRoot();
        }
//        if (!AuroApp.getAuroScholarModel().isApplicationLang()) {
//            String lang = AuroApp.getAuroScholarModel().getLanguage();
//            if (!TextUtil.isEmpty(lang)) {
//                if (lang.equalsIgnoreCase(AppConstant.LANGUAGE_HI) || lang.equalsIgnoreCase(AppConstant.LANGUAGE_EN)) {
//
//                }
//            } else {
//        AuroApp.getAuroScholarModel().setLanguage(AppConstant.LANGUAGE_EN);
//            }
//        }

        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);

        //((AuroApp) requireActivity().getApplication()).getAppComponent().doInjection(this);
        DaggerWrapper.getComponent(getActivity()).doInjection(this);
        quizViewModel = ViewModelProviders.of(this, viewModelFactory).get(QuizViewModel.class);


        binding.setLifecycleOwner(this);
        binding.setQuizViewModel(quizViewModel);
        setRetainInstance(true);
        getKYCChecked();
        init();
        setListener();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((DashBoardMainActivity) getContext()).setListner(this);
    }

    @Override
    protected void init() {
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String partnerlogo = prefModel.getPartner_logo();
        Glide.with(this).load(partnerlogo)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_auro_scholar_logo)
                        .dontAnimate()
                        .priority(Priority.IMMEDIATE)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(binding.partnerLogo);

        details = prefModel.getLanguageMasterDynamic().getDetails();
        prefModel.setLogin(true);
        try {
            binding.RPTextView9.setText(DateUtil.getMonthName() + " " + getActivity().getResources().getString(R.string.scholarship));
            Details details = prefModel.getLanguageMasterDynamic().getDetails();
            if (!TextUtil.isEmpty(DateUtil.getMonthName())) {
                binding.RPTextView9.setText(DateUtil.getMonthName() + " " + details.getScholarship());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }

        dashboardResModel = prefModel.getDashboardResModel();
        checkScreenPreferences();
        //  AppLogger.e("chhonker main quiz Fragment -- grade change--", "" + prefModel.isDashboardaApiNeedToCall());
        if (dashboardResModel != null && !prefModel.isDashboardaApiNeedToCall()) {
            onApiSuccess();
        } else {

            callDasboardApi();
        }
        AppStringDynamic.setMainQuizHometrings(binding);
        // ((DashBoardMainActivity) getActivity()).callSlabsApi(); //Slab Changes
       /* if (slabsResModel == null) {
            slabsResModel = getDummyJson();
        }*/
        // makeQuizInfoAdapter();
    }

    void checkScreenPreferences() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();

        int studentClass = AuroAppPref.INSTANCE.getModelInstance().getUserclass();
        AppLogger.e("checkScreenPreferences --", "" + studentClass);
        if (studentClass > 10) {
            FetchStudentPrefResModel fetchStudentPrefResModel = prefModel.getFetchStudentPrefResModel();
            if (fetchStudentPrefResModel != null && !TextUtil.checkListIsEmpty(fetchStudentPrefResModel.getPreference())) {
                /* Do CODE HERE*/
            } else {
                ((DashBoardMainActivity) getActivity()).setListner(this);
                ((DashBoardMainActivity) getActivity()).callFetchUserPreference();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void setListener() {
        ((DashBoardMainActivity) getActivity()).setListingActiveFragment(DashBoardMainActivity.QUIZ_DASHBOARD_FRAGMENT);
        binding.languageLayout.setOnClickListener(this);
        binding.walleticon.setOnClickListener(this);
        binding.cardView2.setOnClickListener(this);
        binding.privacyPolicy.setOnClickListener(this);
        binding.termsOfUse.setOnClickListener(this);
        binding.imageChat.setOnClickListener(this);
        binding.floatChat.setOnClickListener(this);
        binding.quizSelectionSheet.sheetLayoutQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFadeOutSelectionLayout();
            }
        });
        binding.quizSelectionSheet.cardviewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (quizViewModel != null && quizViewModel.serviceLiveData().hasObservers()) {
            quizViewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }

        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.slabLayout.quizInfoImg.setOnClickListener(this);

    }

    public void setAdapterChapter(SubjectResModel subjectResModel) {
        // binding.quizSelectionSheet.RPScore.setText(getString(R.string.rs) + " " + quizViewModel.homeUseCase.getCurrentMonthWalletBalance(dashboardResModel));
        binding.quizSelectionSheet.RPSubject.setText("" + subjectResModel.getSubject());
        binding.quizSelectionSheet.RPScore.setText(getString(R.string.rs) + " " + subjectResModel.getQuizWonAmount());
        String[] listArrayLanguage = getResources().getStringArray(R.array.classes);
        laugList = new ArrayList();
        for (String lang : listArrayLanguage) {
            SelectChapterQuizModel selectChapterModel = new SelectChapterQuizModel();
            selectChapterModel.setCheck(false);
            laugList.add(selectChapterModel);
        }
        openFadeInSelectionLayout();

        binding.quizSelectionSheet.rvClass.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.quizSelectionSheet.rvClass.setHasFixedSize(true);
        binding.quizSelectionSheet.rvClass.setNestedScrollingEnabled(false);
        adapter = new ChapterSelectAdapter(getActivity(), subjectResModel.getChapter(), this);
        binding.quizSelectionSheet.rvClass.setAdapter(adapter);
    }

    public void setSubjectAdapter(DashboardResModel dashboardResModel) {
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerViewMenu.setLayoutManager(layoutManager);
        binding.recyclerViewMenu.setHasFixedSize(true);
        SubjectSelectAdapter subjectadapter = new SubjectSelectAdapter(dashboardResModel.getSubjectResModelList(), this);
        binding.recyclerViewMenu.setAdapter(subjectadapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_quiz_home;
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        AppLogger.e(TAG, "commonEventListner");
        switch (commonDataModel.getClickType()) {
            case SUBJECT_CLICKED:
                getKYCChecked();
                SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
                String checkkycstatus = prefs.getString("checkkycstatus", "");
                String checkkycstatusmessage = prefs.getString("checkkycstatusmessage","");
                if (checkkycstatus.equals("true")||checkkycstatus.equals(true)){
                    setAdapterChapter((SubjectResModel) commonDataModel.getObject());
                }
                else{
                    showSnackbarError(checkkycstatusmessage);

                }


                break;

            case BACK_CLICK:
                AppLogger.e(TAG, "commonEventListner BACK_CLICK");
                backPressHandling();
                break;

            case NEXT_QUIZ_CLICK:
                quizResModel = (QuizResModel) commonDataModel.getObject();
                callGetQuizInstructionsApi();
                // checkPreQuizDisclaimer();
               /* funnelRetakeQuiz();
                AppLogger.e("chhonker NEXT_QUIZ_CLICK-- ", "ClickCallBack");
                askPermission();*/
                break;

            case START_QUIZ_BUTON:
                quizResModel = (QuizResModel) commonDataModel.getObject();
                callGetQuizInstructionsApi();
                // checkPreQuizDisclaimer();
                break;
            case ACCEPT_PARENT_BUTTON:
                funnelStartQuiz();
                askPermission();
                //
                //openCameraPhotoFragment();
                break;

            case FETCH_STUDENT_PREFERENCES_API:

                break;

            case ACCEPT_INSTRUCTION_CALLBACK:
                /*ACCEPT_INSTRUCTION_CALLBACK Instrucytions call back*/
                askPermission();
                break;

            case LISTNER_FAIL:

                break;

            case GET_SLABS_API:

                AppLogger.e("GET_SLABS_API", "step 1.5--");
                slabsResModel = (SlabsResModel) commonDataModel.getObject();
                AppLogger.e("GET_SLABS_API-", slabsResModel.getMessage());
                makeQuizInfoAdapter();

                break;
        }
    }
    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void getKYCChecked()
    {
        String suserid = AuroAppPref.INSTANCE.getModelInstance().getUserId();
        String langid = AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",suserid);
        map_data.put("user_prefered_language_id",langid);
        RemoteApi.Companion.invoke().getCheckKYC(map_data)
                .enqueue(new Callback<GetStudentUpdateProfile>()
                {
                    @Override
                    public void onResponse(Call<GetStudentUpdateProfile> call, Response<GetStudentUpdateProfile> response)
                    {
                        if (response.code()==400){

                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.getString("message");
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                editor.putString("checkkycstatus", "false");
                                editor.putString("checkkycstatusmessage", message);
                                editor.apply();

                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else if (response.isSuccessful())
                        {
                            String success = response.body().getStatus();
                            String message = response.body().getMessage();
                            if (success.equals("success")){
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                editor.putString("checkkycstatus", "true");
                                editor.apply();
                            }
                            else{
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                editor.putString("checkkycstatus", "false");
                                editor.putString("checkkycstatusmessage", message);
                                editor.apply();
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


    private void askPermission() {

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        callStartQuizActionApi();

    }


    void callStartQuizActionApi() {
        showProgress();
        if (dashboardResModel != null && quizResModel != null) {
            assignmentReqModel = quizViewModel.homeUseCase.getAssignmentRequestModel(dashboardResModel, quizResModel);
            quizViewModel.getAssignExamData(assignmentReqModel);
//            Intent intent = new Intent(getContext(), CameraActivity.class);
//            startActivityForResult(intent, AppConstant.CAMERA_REQUEST_CODE);
        } else {
            ViewUtil.showSnackBar(binding.getRoot(), "Something went wrong");
        }
    }

    public void openCameraPhotoFragment() {
        Intent intent = new Intent(getContext(), CameraActivity.class);
        startActivityForResult(intent, AppConstant.CAMERA_REQUEST_CODE);
        //  startActivity(new Intent(getActivity(), CameraxActivity.class));
    }

    void backPressHandling() {
        int status = binding.quizSelectionSheet.sheetLayoutQuiz.getVisibility();
        AppLogger.e(TAG, "visibility value--" + status);
        if (status == 0) {
            binding.quizSelectionSheet.sheetLayoutQuiz.setVisibility(View.GONE);
        } else {
            ((DashBoardMainActivity) getActivity()).dismissApplication();
        }
    }


    private void openFadeInSelectionLayout() {
        //Animation on button
        binding.quizSelectionSheet.sheetLayoutQuiz.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_to_up);
        binding.quizSelectionSheet.sheetLayoutQuiz.startAnimation(anim);

    }

    private void openFadeOutSelectionLayout() {
        //Animation on button
        binding.quizSelectionSheet.sheetLayoutQuiz.setVisibility(View.GONE);
      /*  Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        binding.quizSelectionSheet.sheetLayout.startAnimation(anim);*/

    }

    @Override
    public void onClick(View v) {
        AppLogger.e(TAG, "On click called");
        if (v.getId() == R.id.sheet_layout_quiz) {
            AppLogger.e(TAG, "On click called sheet_layout 1");
        }
        int id = v.getId();
        if (id == R.id.sheet_layout_quiz) {
            AppLogger.e(TAG, "On click called sheet_layout");
        } else if (id == R.id.language_layout) {
            ((DashBoardMainActivity) getActivity()).openChangeLanguageDialog();
        } else if (id == R.id.cardView2) {
            ((DashBoardMainActivity) getActivity()).openProfileFragment();
        } else if (id == R.id.walleticon) {
            getKYCStatus();
               /* if (quizViewModel.homeUseCase.checkKycStatus(dashboardResModel)) {
                    ((DashBoardMainActivity) getActivity()).openKYCViewFragment(dashboardResModel, 0);
                } else {
                    ((DashBoardMainActivity) getActivity()).openKYCFragment(dashboardResModel, 0);
                }*/
        } else if (id == R.id.float_chat) {// openYuboChatActivty();
            //startActivity(new Intent(getContext(), ChatActivity.class));

            //  Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.privacy_policy) {
            openWebActivty(URLConstant.PRIVACY_POLICY);
        } else if (id == R.id.terms_of_use) {
            openWebActivty(URLConstant.TERM_CONDITION);
        } else if (id == R.id.imageChat) {
            openYuboChatActivty();
        } else if (id == R.id.quiz_info_img) {
            openQuizBottomSheetDialog();
        }

    }
    private void getKYCStatus()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String suserid =  AuroAppPref.INSTANCE.getModelInstance().getUserId();
        String langid =  prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",suserid);
        map_data.put("user_prefered_language_id",langid);

        RemoteApi.Companion.invoke().getKYCStatus(map_data)
                .enqueue(new Callback<StudentKycStatusResModel>()
                {
                    @Override
                    public void onResponse(Call<StudentKycStatusResModel> call, Response<StudentKycStatusResModel> response)
                    {
                        if (response.code()==400){

                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.getString("message");

                                getkycstatus = response.body().getKycStatus();
                                String uploadedornot = response.body().getIsKycUploaded();
                                if (uploadedornot.equals("NO")||uploadedornot=="NO"){
                                    // Toast.makeText(getActivity(), "Please upload you KYC", Toast.LENGTH_SHORT).show();
                                    ViewUtil.showSnackBar(binding.getRoot(),details.getPleaseUploadTheDocument());
                                }
                                else if (uploadedornot.equals("YES")||uploadedornot=="YES"){
                                    if (getkycstatus.equals("APPROVE")){
                                        openWalletInfoFragment();
                                    }
                                    else if (getkycstatus.equals("PENDING")){
                                        //  Toast.makeText(getActivity(), "Your KYC verification is pending, wait till it gets verified", Toast.LENGTH_SHORT).show();
                                        ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification is pending, wait till it gets verified");

                                    }
                                    else if (getkycstatus.equals("DISAPPROVE")){
                                        // Toast.makeText(getActivity(), "Your KYC verification has been disapprove, please reupload documents", Toast.LENGTH_SHORT).show();
                                        ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification has been disapprove, please reupload documents");

                                    }
                                    else if (getkycstatus.equals("INPROCESS")){
                                        Toast.makeText(getActivity(), "Your KYC is under verification", Toast.LENGTH_SHORT).show();
                                        // ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification is in progress, wait till it gets verified");

                                    }
                                    else if (getkycstatus.equals("REJECTED")){
                                        // Toast.makeText(getActivity(), "Your KYC verification has been rejected, please reupload documents", Toast.LENGTH_SHORT).show();
                                        ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification has been rejected, please reupload documents");

                                    }
                                    else{
                                        //  Toast.makeText(getActivity(), "Your KYC verification is pending, wait till it gets verified", Toast.LENGTH_SHORT).show();

                                        ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification is pending, wait till it gets verified");

                                    }
                                }

                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else if (response.isSuccessful())
                        {
                            getkycstatus = response.body().getKycStatus();
                            String uploadedornot = response.body().getIsKycUploaded();
                            if (uploadedornot.equals("NO")||uploadedornot=="NO"){
                               // Toast.makeText(getActivity(), "Please upload you KYC", Toast.LENGTH_SHORT).show();
                                  ViewUtil.showSnackBar(binding.getRoot(),"Please upload your KYC");
                            }
                            else if (uploadedornot.equals("YES")||uploadedornot=="YES"){
                                if (getkycstatus.equals("APPROVE")){
                                    openWalletInfoFragment();
                                }
                                else if (getkycstatus.equals("PENDING")){
                                  //  Toast.makeText(getActivity(), "Your KYC verification is pending, wait till it gets verified", Toast.LENGTH_SHORT).show();
                                    ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification is pending, wait till it gets verified");

                                }
                                else if (getkycstatus.equals("DISAPPROVE")){
                                   // Toast.makeText(getActivity(), "Your KYC verification has been disapprove, please reupload documents", Toast.LENGTH_SHORT).show();
                                    ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification has been disapprove, please reupload documents");

                                }
                                else if (getkycstatus.equals("INPROCESS")){
                                      Toast.makeText(getActivity(), "Your KYC is under verification", Toast.LENGTH_SHORT).show();
                                   // ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification is in progress, wait till it gets verified");

                                }
                                else if (getkycstatus.equals("REJECTED")){
                                   // Toast.makeText(getActivity(), "Your KYC verification has been rejected, please reupload documents", Toast.LENGTH_SHORT).show();
                                     ViewUtil.showSnackBar(binding.getRoot(),"Your KYC verification has been rejected, please reupload documents");

                                }
                                else{
                                  //  Toast.makeText(getActivity(), "Your KYC verification is pending, wait till it gets verified", Toast.LENGTH_SHORT).show();

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
    private void openWalletInfoFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        WalletInfoDetailFragment fragment = new WalletInfoDetailFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(AuroApp.getFragmentContainerUiId(), fragment, WalletInfoDetailFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }


    private void observeServiceResponse() {

        quizViewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    if (responseApi.apiTypeStatus == DASHBOARD_API) {
                        if (isVisible()) {
                            AppLogger.e("callDasboardApi-", "Step 7");
                            binding.swipeRefreshLayout.setRefreshing(false);
                            dashboardResModel = (DashboardResModel) responseApi.data;
                            onApiSuccess();
                        }
                    } else if (responseApi.apiTypeStatus == ASSIGNMENT_STUDENT_DATA_API) {
                        testAssignmentResModel = (AssignmentResModel) responseApi.data;
                        if (!testAssignmentResModel.isError()) {
                            if (quizProgressDialog != null) {
                                quizProgressDialog.dismiss();
                            }
                            openCameraPhotoFragment();
                        } else {
                            if (quizProgressDialog != null) {
                                quizProgressDialog.dismiss();
                            }

                            ViewUtil.showSnackBar(binding.getRoot(), testAssignmentResModel.getMessage());
                        }

                    }
                    else if (responseApi.apiTypeStatus == GET_INSTRUCTIONS_API) {
                        if (quizProgressDialog != null) {
                            quizProgressDialog.dismiss();
                        }
                        InstructionsResModel instructionsResModel = (InstructionsResModel) responseApi.data;
                        if (!instructionsResModel.getError()) {
                            openQuizInstructionDialog(instructionsResModel);
                        } else {
                            askPermission();
                            AppLogger.e("GET_INSTRUCTIONS_API", "else part");
                        }
                    }
                    else if (responseApi.apiTypeStatus == Status.GET_SLABS_API) {
                        AppLogger.e("GET_SLABS_API", "step 1");
                        SlabsResModel slabsResModel=new SlabsResModel();
                        try {
                            slabsResModel = (SlabsResModel) responseApi.data;
                            AppLogger.e("GET_SLABS_API", "step 1.1");
                        }catch (Exception e)
                        {
                            AppLogger.e("GET_SLABS_API", "step 1.2--"+e.getMessage());
                        }

                        if (!slabsResModel.getError()) {

                            if (commonCallBackListner != null) {
                                AppLogger.e("GET_SLABS_API", "step 1.3--");
                                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, GET_SLABS_API,  responseApi.data));
                            }
                        } else {
                            if (commonCallBackListner != null) {
                                AppLogger.e("GET_SLABS_API", "step 1.4--");
                                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, LISTNER_FAIL, (String) responseApi.data));
                            }
                        }


                    }
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                default:
                    if (isVisible()) {

                        if (responseApi.apiTypeStatus == GET_INSTRUCTIONS_API) {
                            askPermission();
                            return;
                        }
                        if (quizProgressDialog != null) {
                            quizProgressDialog.dismiss();
                        }
                        binding.swipeRefreshLayout.setRefreshing(false);
                        /*Disable shimmer on dashboaerd */
                        ((DashBoardMainActivity) getActivity()).setProgressVal();
                        setShimmerValue();
                        handleProgress(1, (String) responseApi.data);
                    }
                    break;
            }

        });
    }


    private void openQuizInstructionDialog(InstructionsResModel instructionsResModel) {
        if (getContext() != null) {
            customDialog = new InstructionDialog(getContext(), this, instructionsResModel);
            // Window window = customDialog.getWindow();
            // window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

    private void setShimmerValue() {
        ((DashBoardMainActivity) getActivity()).setProgressVal();
    }


    private void handleProgress(int value, String message) {
        AppLogger.e(TAG, "handleProgress calling - " + value + "-message-" + message);
        if (value == 0) {
            binding.errorConstraint.setVisibility(View.GONE);
            binding.mainParentLayout.setVisibility(View.GONE);
            binding.imageChat.setVisibility(View.GONE);
            binding.shimmerViewQuiz.setVisibility(View.VISIBLE);
            binding.shimmerViewQuiz.startShimmer();
        }
        else if (value == 1) {
//            ((DashBoardMainActivity) getActivity()).callFetchUserPreference();
//            callDasboardApi();

            binding.errorConstraint.setVisibility(View.GONE);
            binding.mainParentLayout.setVisibility(View.VISIBLE);
            binding.shimmerViewQuiz.setVisibility(View.GONE);
            binding.shimmerViewQuiz.stopShimmer();
            binding.imageChat.setVisibility(View.VISIBLE);


//            binding.errorConstraint.setVisibility(View.GONE);
//            binding.mainParentLayout.setVisibility(View.GONE);
//            binding.shimmerViewQuiz.setVisibility(View.GONE);
//            binding.shimmerViewQuiz.stopShimmer();
            binding.errorLayout.textError.setText(message);
            //  binding.imageChat.setVisibility(View.GONE);
            binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashBoardMainActivity) getActivity()).callFetchUserPreference();
                    callDasboardApi();

                }
            });
        }
        else if (value == 2) {
            binding.errorConstraint.setVisibility(View.GONE);
            binding.mainParentLayout.setVisibility(View.VISIBLE);
            binding.shimmerViewQuiz.setVisibility(View.GONE);
            binding.shimmerViewQuiz.stopShimmer();
            binding.imageChat.setVisibility(View.VISIBLE);

        }
    }


    private void onApiSuccess() {
        handleProgress(1, "");
        updateList(dashboardResModel);
        AppUtil.setDashboardResModelToPref(dashboardResModel);
        if (!dashboardResModel.isError()) {
            ((DashBoardMainActivity) getActivity()).showBottomNavigationView();
            handleProgress(2, "");
            checkStatusforCongratulationDialog();
            if (dashboardResModel != null && dashboardResModel.getStatus().equalsIgnoreCase(AppConstant.FAILED)) {
                handleProgress(1, dashboardResModel.getMessage());
            } else {
                setDataOnUi(dashboardResModel);
            }
        } else {
            ((DashBoardMainActivity) getActivity()).hideBottomNavigationView();
            handleProgress(1, dashboardResModel.getMessage());
        }

        if (dashboardResModel != null && dashboardResModel.getCheckVerResModel() != null && ((DashBoardMainActivity) getActivity()) != null) {
            ((DashBoardMainActivity) getContext()).checkVersion(dashboardResModel.getCheckVerResModel());
        }
        /*Update Dynamic  to empty*/
        AppLogger.e("chhonker--", "setPref step 1--" + AuroAppPref.INSTANCE.getModelInstance().isDashboardaApiNeedToCall());
/*
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        DynamiclinkResModel model = prefModel.getDynamiclinkResModel();
        prefModel.setDynamiclinkResModel(new DynamiclinkResModel());
        AuroAppPref.INSTANCE.setPref(prefModel);*/
        ViewUtil.setProfilePic(binding.imageView6);
    }

    private void updateList(DashboardResModel dashboardResModel) {
        try {

            if (dashboardResModel != null && !dashboardResModel.isError() && !TextUtil.checkListIsEmpty(dashboardResModel.getSubjectResModelList())) {
                for (SubjectResModel subjectResModel : dashboardResModel.getSubjectResModelList()) {
                    for (QuizResModel quizResModel : subjectResModel.getChapter()) {
                        quizResModel.setCoreSubjectName(subjectResModel.getSubject());
                    }
                }
            }
        } catch (Exception e) {
            AppLogger.e("callDasboardApi-", "Step 10-" + e.getMessage());

        }
    }


    private void setDataOnUi(DashboardResModel dashboardResModel) {
        AppLogger.e("callDasboardApi-", "Step 09");
        binding.walletBalText.setText(getString(R.string.rs) + " " + quizViewModel.homeUseCase.getWalletBalance(dashboardResModel));
        setSubjectAdapter(dashboardResModel);
        /*Disable shimmer on dashboaerd */
        AppLogger.e("chhonker--", "call set data");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setDashboardaApiNeedToCall(false);
        AuroAppPref.INSTANCE.setPref(prefModel);
     /*
     //For testing purpose
        dashboardResModel.setPartnerSource("Punjab");
        dashboardResModel.setPartnerLogo("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTXoPs7av_3dWLBqDdhBCuvlaYeG6_nX1D7rA&usqp=CAU");
        */
        AppLogger.e("chhonker--", "call set data--" + AuroAppPref.INSTANCE.getModelInstance().isDashboardaApiNeedToCall());
        if (dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getPartnerSource()) && dashboardResModel.getPartnerSource().equalsIgnoreCase(AppConstant.PARTNER_AURO_ID)) {
            binding.auroScholarBottom.setVisibility(View.GONE);
            binding.auroScholarLogo.setVisibility(View.VISIBLE);
           // AppUtil.loadAppLogo(binding.auroScholarLogo, getActivity());
            //   binding.auroScholarLogo.setImageDrawable(getActivity().getDrawable(R.drawable.auro_scholar_logo));
        } else {
            if (dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getPartnerLogo())) {
                binding.auroScholarBottom.setVisibility(View.VISIBLE);
                //    binding.auroScholarBottom.setImageDrawable(getActivity().getDrawable(R.drawable.auro_scholar_logo));
              //  AppUtil.loadAppLogo(binding.auroScholarLogo, getActivity());
               // ImageUtil.loadNormalImage(binding.auroScholarLogo, dashboardResModel.getPartnerLogo());

            } else {
                binding.auroScholarBottom.setVisibility(View.GONE);
                //  binding.auroScholarLogo.setImageDrawable(getActivity().getDrawable(R.drawable.auro_scholar_logo));
               // AppUtil.loadAppLogo(binding.auroScholarLogo, getActivity());
            }
        }

        if (dashboardResModel != null && dashboardResModel.isChatBotEnabled()) {
            binding.imageChat.setVisibility(View.GONE);
        } else {
            binding.imageChat.setVisibility(View.GONE);
        }
    }

    public void checkStatusforCongratulationDialog() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel != null && prefModel.getAssignmentReqModel() != null) {
            AppLogger.e("chhonker setAssignmentReqModel", "step 1");
            AssignmentReqModel assignmentReqModel = prefModel.getAssignmentReqModel();
            AppLogger.e("chhonker setAssignmentReqModel step 2", assignmentReqModel.getExam_name());
            if (!TextUtil.isEmpty(assignmentReqModel.getExam_name()) && !TextUtil.isEmpty(assignmentReqModel.getQuiz_attempt())) {
                if (dashboardResModel != null && !TextUtil.checkListIsEmpty(dashboardResModel.getSubjectResModelList())) {
                    SubjectResModel subjectResModel = dashboardResModel.getSubjectResModelList().get(assignmentReqModel.getSubjectPos());
                    int finishedTestPos = ConversionUtil.INSTANCE.convertStringToInteger(assignmentReqModel.getExam_name());
                    QuizResModel quizResModel = prefModel.getQuizResModel();
                    for (SubjectResModel model : dashboardResModel.getSubjectResModelList()) {
                        if (model.getSubject().equalsIgnoreCase(quizResModel.getCoreSubjectName())) {
                            quizResModel = model.getChapter().get(quizResModel.getNumber() - 1);
                            break;
                        }
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(quizResModel);
                    String jso2 = gson.toJson(assignmentReqModel);
                    AppLogger.e("chhonker quizresponse", json);
                    AppLogger.e("chhonker assignment", jso2);
                    int score = 0;
                    if (quizResModel.getAttempt() > 0) {
                        if (!TextUtil.isEmpty(quizResModel.getScoreallpoints())) {
                            String[] scoreArray = quizResModel.getScoreallpoints().split(",");
                            if (scoreArray.length > 0) {
                                score = ConversionUtil.INSTANCE.convertStringToInteger(scoreArray[scoreArray.length - 1]);
                            }
                        }
                    }
                    funnelCompleteQuiz();
                    assignmentReqModel.setActualScore(score);

                    AppLogger.e("chhonker score", "" + score);
                    if (String.valueOf(quizResModel.getNumber()).equalsIgnoreCase(assignmentReqModel.getExam_name()) && score > 7) {
                        openCongratulationsDialog(dashboardResModel, assignmentReqModel);
                    } else {
                        openCongratulationsLessScoreDialog(dashboardResModel, assignmentReqModel);
                    }
                }
                AppLogger.e("chhonker setAssignmentReqModel", "null");
                prefModel.setAssignmentReqModel(null);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        AuroAppPref.INSTANCE.setPref(prefModel);
                        AppLogger.e("chhonker--", "setPref step 2--" + AuroAppPref.INSTANCE.getModelInstance().isDashboardaApiNeedToCall());

                    }
                }, 2000);


            }

        }
    }


    private void openCongratulationsDialog(DashboardResModel dashboardResModel, AssignmentReqModel assignmentReqModel) {
        CongratulationsDialog congratulationsDialog = CongratulationsDialog.newInstance(this, dashboardResModel, assignmentReqModel);
        openFragmentDialog(congratulationsDialog);
    }

    private void openCongratulationsLessScoreDialog(DashboardResModel dashboardResModel, AssignmentReqModel assignmentReqModel) {
        ConsgratuationLessScoreDialog congratulationsDialog = ConsgratuationLessScoreDialog.newInstance(this, dashboardResModel, assignmentReqModel);
        openFragmentDialog(congratulationsDialog);
    }

    private void openFragmentDialog(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(AuroApp.getFragmentContainerUiId(), fragment, CongratulationsDialog.class.getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();

    }


    void callDasboardApi() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel.isDashboardaApiNeedToCall()) {
            handleProgress(0, "");
        }

        String deviceToken = prefModel.getDeviceToken();
        if (!TextUtil.isEmpty(deviceToken)) {
            AppLogger.v("DeviceToken_1", deviceToken);
        }
        AppLogger.v("DeviceToken_2", deviceToken);
        quizViewModel.getDashBoardData(AuroApp.getAuroScholarModel());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    String path = data.getStringExtra(AppConstant.PROFILE_IMAGE_PATH);
                    azureImage(path);
                    openQuizTestFragment(dashboardResModel);
                    AppLogger.e("chhonker-", "QuizTestFragment  setp 1");
                    //loadImageFromStorage(path);
                } catch (Exception e) {

                }

            } else {

            }
        }
    }

    private void azureImage(String path) {
        if (quizResModel == null || dashboardResModel == null) {
            return;
        }
        try {
            AppLogger.d(TAG, " azureImage Image Path" + path);
            assignmentReqModel = quizViewModel.homeUseCase.getAssignmentRequestModel(dashboardResModel, quizResModel);
            assignmentReqModel.setEklavvya_exam_id(testAssignmentResModel.getExamAssignmentID());
            assignmentReqModel.setSubject(quizResModel.getSubjectName());
            assignmentReqModel.setExamId(testAssignmentResModel.getExamId());
            assignmentReqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getUserId());
            AppLogger.e("chhonker-", "azureImage  setp 2" + new Gson().toJson(assignmentReqModel));

            Bitmap picBitmap = BitmapFactory.decodeFile(path);
            byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
            long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
            if (mb > 1.5) {
                assignmentReqModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
            } else {
                assignmentReqModel.setImageBytes(bytes);
            }
            long soize = AppUtil.bytesIntoHumanReadable(assignmentReqModel.getImageBytes().length);
            AppLogger.e("chhonker-", "QuizTestFragment  setp 2 mb" + soize);

            quizViewModel.getAzureRequestData(assignmentReqModel);
        } catch (Exception e) {
            AppLogger.e("chhonker-", "QuizTestFragment  setp 2" + e.getMessage());
            /*Do code here when error occur*/
        }
    }

    public void openQuizTestFragment(DashboardResModel dashboardResModel) {
        if (quizResModel == null || dashboardResModel == null) {
            return;
        }

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setDashboardResModel(dashboardResModel);
        prefModel.setQuizResModel(quizResModel);
        prefModel.setAssignmentResModel(testAssignmentResModel);
        AuroAppPref.INSTANCE.setPref(prefModel);
        AppLogger.e("chhonker--", "setPref step 3--" + AuroAppPref.INSTANCE.getModelInstance().isDashboardaApiNeedToCall());

        openQuizNativeFragment();
       /* QuizTestFragment quizTestFragment = new QuizTestFragment();
        openFragment(quizTestFragment);*/
    }
    private void openFragment(Fragment fragment) {
        ((AppCompatActivity) (this.getContext())).getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(AuroApp.getFragmentContainerUiId(), fragment, MainQuizHomeFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void openFragmentTest(Fragment fragment) {
        ((AppCompatActivity) (this.getContext())).getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(AuroApp.getFragmentContainerUiId(), fragment, MainQuizHomeFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commit();
    }


    private void funnelRetakeQuiz() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStartNewQuiz();
    }

    private void funnelStartQuiz() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackRetakeQuiz();
    }

    private void funnelCompleteQuiz() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentCompleteQuiz();
    }

    private void openWebActivty(String link) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra(AppConstant.WEB_LINK, link);
        startActivity(intent);
    }

    private void openYuboChatActivty( ) {
        /*Intent intent = new Intent(getActivity(), YuboChatActivity.class);
        startActivity(intent);*/
    }


    private void checkPreQuizDisclaimer() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (!prefModel.isPreQuizDisclaimer()) {
            QuizDisclaimerDialog askDetailCustomDialog = new QuizDisclaimerDialog(getActivity(), this);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(askDetailCustomDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            askDetailCustomDialog.getWindow().setAttributes(lp);
            Objects.requireNonNull(askDetailCustomDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            askDetailCustomDialog.setCancelable(false);
            askDetailCustomDialog.show();
        }
    }

    @Override
    public void onRefresh() {
        AppLogger.e("onRefresh-", "Step 01");
        handleProgress(0, "");
        callDasboardApi();

    }

    public void showProgress() {
        int THREE_SECONDS = 20 * 1000;
        quizProgressDialog = new ProgressDialog(getActivity());
        quizProgressDialog.setMessage(AuroApp.getAppContext().getResources().getString(R.string.fetch_quiz_data));
        quizProgressDialog.setCancelable(false);
        quizProgressDialog.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (quizProgressDialog != null && quizProgressDialog.isShowing())
                    quizProgressDialog.dismiss();
            }
        }, THREE_SECONDS);
    }


    void openQuizNativeFragment() {
        QuizTestNativeFragment nativeFragment = new QuizTestNativeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        bundle.putParcelable(AppConstant.QUIZ_RES_MODEL, quizResModel);
        nativeFragment.setArguments(bundle);
        openFragmentTest(nativeFragment);
        //startTestActivity();
    }


    void callGetQuizInstructionsApi() {
        showProgress();
        InstructionModel instructionModel = new InstructionModel();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        instructionModel.setInstructionCode(AppConstant.InstructionsType.QUIZ);
        instructionModel.setLanguageId(Integer.parseInt(prefModel.getUserLanguageId()));
        quizViewModel.checkInternet(instructionModel, GET_INSTRUCTIONS_API);
    }
    private void makeQuizInfoAdapter() {//pradeep
        // slabsResModel = getDummyJson();
        AppLogger.e("GET_SLABS_API","MainQuizHomeFragment - "+slabsResModel.getSlabs());
        binding.slabLayout.quizLevelRecycleview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.slabLayout.quizLevelRecycleview.setHasFixedSize(true);
        binding.slabLayout.quizLevelRecycleview.setNestedScrollingEnabled(false);
        LevelInfoAdapter adapter = new LevelInfoAdapter(getActivity(), slabsResModel.getSlabs(), this);
        binding.slabLayout.quizLevelRecycleview.setAdapter(adapter);
        setDataOnSeekBar();
    }

    void openQuizBottomSheetDialog() {//pradeep
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstant.QUIZ_RES_MODEL, slabsResModel);
        QuizLevelInfoSheetDialog bottomSheet = new QuizLevelInfoSheetDialog();
        bottomSheet.setArguments(bundle);
        bottomSheet.show(getActivity().getSupportFragmentManager(),
                "ModalBottomSheet");
    }


    private SlabsResModel getDummyJson() {


        //String jsonStr = "{\"status\":\"success\",\"error\":false,\"message\":\"Success!\",\"winning_quizes\":10,\"slabs\":[{\"level\":1,\"slab_name\":\"1-4\",\"price\":20,\"quiz_log\":\"2\",\"details\":[{\"subject\":\"Science\",\"quiz_name\":\"ElectricCurrentandItsEffect\"},{\"subject\":\"SocialScience\",\"quiz_name\":\"LIFEINDESERTS\"},{\"subject\":\"Science\",\"quiz_name\":\"ElectricCurrentandItsEffect\"},{\"subject\":\"Hindi\",\"quiz_name\":\"टोपी\"}],\"quiz_count\":4},{\"level\":2,\"slab_name\":\"5-8\",\"price\":40,\"quiz_log\":\"2\",\"details\":[{\"subject\":\"Science\",\"quiz_name\":\"ConservationofPlantsandAnimals\"},{\"subject\":\"Hindi\",\"quiz_name\":\"आश्रमकाअनुमानितव्यय\"},{\"subject\":\"Science\",\"quiz_name\":\"ElectricCurrentandItsEffect\"},{\"subject\":\"SocialScience\",\"quiz_name\":\"LIFEINDESERTS\"}],\"quiz_count\":4},{\"level\":3,\"slab_name\":\"9-12\",\"price\":60,\"quiz_log\":\"1\",\"details\":[{\"subject\":\"SocialScience\",\"quiz_name\":\"HUMANRESOURCES\"},{\"subject\":\"English\",\"quiz_name\":\"MeadowSurprises\"}],\"quiz_count\":2},{\"level\":4,\"slab_name\":\"13-16\",\"price\":80,\"quiz_log\":\"0\",\"details\":[],\"quiz_count\":0},{\"level\":5,\"slab_name\":\"17-20\",\"price\":100,\"quiz_log\":\"0\",\"details\":[],\"quiz_count\":0}],\"response_code\":200}";

        String jsonStr="{\"status\":\"success\",\"error\":false,\"message\":\"Success!\",\"winning_quizes\":0,\"slabs\":[{\"level\":1,\"slab_name\":\"1-4\",\"price\":20,\"quiz_log\":\"1\",\"details\":[],\"quiz_count\":0},{\"level\":2,\"slab_name\":\"5-8\",\"price\":40,\"quiz_log\":\"0\",\"details\":[]},{\"level\":3,\"slab_name\":\"9-12\",\"price\":60,\"quiz_log\":\"0\",\"details\":[]},{\"level\":4,\"slab_name\":\"13-16\",\"price\":80,\"quiz_log\":\"0\",\"details\":[]},{\"level\":5,\"slab_name\":\"17-20\",\"price\":100,\"quiz_log\":\"0\",\"details\":[]}],\"response_code\":200}";
        SlabsResModel resModel = new Gson().fromJson(jsonStr, SlabsResModel.class);
        return resModel;
    }



    void seQuizLevelData() {

    }

    void setDataOnSeekBar() {
        level = quizViewModel.getCurrentLevel(slabsResModel);

        try {
            binding.slabLayout.seekbarWithIntervals.setIntervals(getIntervals());
            binding.slabLayout.seekbarWithIntervals.setProgress(level);
            binding.slabLayout.seekbarWithIntervals.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    binding.slabLayout.seekbarWithIntervals.setProgress(level);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    binding.slabLayout.seekbarWithIntervals.setProgress(level);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    binding.slabLayout.seekbarWithIntervals.setProgress(level);
                }
            });
        } catch (Exception e) {
            AppLogger.e("setDataOnSeekBar", "exception--" + e.getMessage());
        }
        binding.slabLayout.levelText.setText("Level\n" + level);

    }

    /*Seekbaer code*/

    private List<String> getIntervals() {
        return new ArrayList<String>() {{
            add("");
            add(" |\nQ1");
            add(" |\nQ2");
            add(" |\nQ3");
            add(" |\nQ4");
            add("   |\nQ5");

        }};
    }


}
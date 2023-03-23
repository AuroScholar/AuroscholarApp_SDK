package com.auro.application.home.presentation.view.activity;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;

import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.common.NotificationDataModel;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.SdkCallBack;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.network.ErrorResponseModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.databinding.ActivityDashBoardMainBinding;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.DashboardResponselDataModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.FbGoogleUserModel;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.GenderDataModel;
import com.auro.application.home.data.model.GetAllChildModel;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.OtpOverCallReqModel;
import com.auro.application.home.data.model.ParentProfileDataModel;
import com.auro.application.home.data.model.PendingKycDocsModel;
import com.auro.application.home.data.model.ReferralPopUpDataModel;
import com.auro.application.home.data.model.RefferalReqModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.StudentResponselDataModel;
import com.auro.application.home.data.model.VerifyOtpReqModel;
import com.auro.application.home.data.model.response.CheckVerResModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.data.model.response.FetchStudentPrefResModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.InstructionsResModel;
import com.auro.application.home.data.model.response.NoticeInstruction;
import com.auro.application.home.data.model.response.OtpOverCallResModel;
import com.auro.application.home.data.model.response.SendOtpResModel;
import com.auro.application.home.data.model.response.ShowDialogModel;
import com.auro.application.home.data.model.response.SlabsResModel;
import com.auro.application.home.data.model.response.VerifyOtpResModel;
import com.auro.application.home.data.model.signupmodel.InstructionModel;
import com.auro.application.home.data.model.signupmodel.UserSlabsRequest;
import com.auro.application.home.presentation.view.adapter.SelectYourParentChildAdapter;
import com.auro.application.home.presentation.view.fragment.CertificateFragment;
import com.auro.application.home.presentation.view.fragment.DemographicFragment;
import com.auro.application.home.presentation.view.fragment.FriendsLeaderBoardListFragment;
import com.auro.application.home.presentation.view.fragment.GradeChangeFragment;
import com.auro.application.home.presentation.view.fragment.KYCFragment;
import com.auro.application.home.presentation.view.fragment.KYCViewFragment;
import com.auro.application.home.presentation.view.fragment.MainQuizHomeFragment;
import com.auro.application.home.presentation.view.fragment.ParentProfileFragment;
import com.auro.application.home.presentation.view.fragment.PrivacyPolicyFragment;
import com.auro.application.home.presentation.view.fragment.StudentKycInfoFragment;
import com.auro.application.home.presentation.view.fragment.StudentProfileFragment;
import com.auro.application.home.presentation.view.fragment.TransactionsFragment;
import com.auro.application.home.presentation.view.fragment.PartnersFragment;
import com.auro.application.home.presentation.view.fragment.SubjectPreferencesActivity;
import com.auro.application.home.presentation.viewmodel.AuroScholarDashBoardViewModel;
import com.auro.application.payment.presentation.view.fragment.SendMoneyFragment;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.DateUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.alert_dialog.LanguageChangeDialog;
import com.auro.application.util.alert_dialog.UpdateCustomDialog;
import com.auro.application.util.alert_dialog.disclaimer.CustomOtpDialog;
import com.auro.application.util.alert_dialog.disclaimer.GradeChnageCongDialogBox;
import com.auro.application.util.alert_dialog.disclaimer.LoginDisclaimerDialog;
import com.auro.application.util.alert_dialog.disclaimer.NoticeDialogBox;
import com.auro.application.util.authenticate.GoogleSignInHelper;
import com.auro.application.util.firebaseAnalytics.AnalyticsRegistry;
import com.auroscholar.final_auroscholarapp_sdk.SDKDataModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.googlejavaformat.Indent;
import com.google.gson.Gson;
import com.instabug.apm.APM;
import com.instabug.bug.BugReporting;
import com.instabug.crash.CrashReporting;
import com.instabug.library.Feature;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.instabug.library.ui.onboarding.WelcomeMessage;
import com.instabug.library.visualusersteps.State;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import static com.auro.application.core.common.Status.DASHBOARD_API;
import static com.auro.application.core.common.Status.GET_SLABS_API;
import static com.auro.application.core.common.Status.LISTNER_FAIL;
import static com.auro.application.core.common.Status.LISTNER_SUCCESS;
import static com.auro.application.core.common.Status.SEND_OTP;
import static com.auro.application.core.common.Status.SEND_REFERRAL_API;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardMainActivity extends BaseActivity implements GradeChangeFragment.OnClickButton, BottomNavigationView.OnNavigationItemSelectedListener,CommonCallBackListner {
    @Inject
    @Named("DashBoardMainActivity")
    ViewModelFactory viewModelFactory;
    public ActivityDashBoardMainBinding binding;
    private AuroScholarDashBoardViewModel viewModel;
    private Context mContext;
    PrefModel prefModel;
    int backPress = 0;
    CustomOtpDialog customOtpDialog;
    LoginDisclaimerDialog loginDisclaimerDialog;
    NoticeDialogBox noticeDialogBox;
    GradeChnageCongDialogBox gradeChnageCongDialogBox;
    AlertDialog alertDialog;


    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    String TAG = "AppUpdate";
    CheckVerResModel checkVerResModel;
    UpdateCustomDialog updateCustomDialog;
    public AlertDialog dialogQuit;
    String typeGradeChange;
    private static int LISTING_ACTIVE_FRAGMENT = 0;
    public static final int QUIZ_DASHBOARD_FRAGMENT = 1;
    public static final int QUIZ_KYC_FRAGMENT = 2;
    public static final int QUIZ_KYC_VIEW_FRAGMENT = 3;
    public static final int WALLET_INFO_FRAGMENT = 4;
    public static final int QUIZ_TEST_FRAGMENT = 5;
    public static final int PRIVACY_POLICY_FRAGMENT = 6;
    public static final int CERTIFICATE_FRAGMENT = 7;
    public static final int KYC_DIRECT_FRAGMENT = 8;
    public static final int CERTIFICATE_DIRECT_FRAGMENT = 9;
    public static final int PAYMENT_DIRECT_FRAGMENT = 10;
    public static final int TRANSACTION_FRAGMENT = 11;
    public static final int LEADERBOARD_FRAGMENT = 12;
    public static final int PAYMENT_FRAGMENT = 13;
    public static final int STUDENT_PROFILE_FRAGMENT = 14;
    public static final int PARTNERS_FRAGMENT = 15;
    public static final int GRADE_CHANGE_FRAGMENT = 16;
    public static final int SEND_MONEY_FRAGMENT = 17;
    public static final int NATIVE_QUIZ_FRAGMENT = 19;
    public static final int DEMOGRAPHIC_FRAGMENT = 20;
    DashboardResModel dashboardResModel;
    AuroScholarInputModel inputModel;
    AuroScholarDataModel auroScholarDataModel;
    CommonCallBackListner commonCallBackListner;
    CommonCallBackListner clickLisner;
    String deviceToken = "";
    public boolean isBackNormal = true;
    SDKDataModel checkUserResModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerWrapper.getComponent(this).doInjection(this);
        ViewUtil.setLanguageonUi(this);


        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("newToken", newToken);
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setDeviceToken(newToken);
            getPreferences(Context.MODE_PRIVATE).edit().putString("fb_device_token", newToken).apply();
        });

        String mobilenumber = AuroAppPref.INSTANCE.getModelInstance().getUserMobile();
        SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor.putString("statusparentprofile", "false");
        editor.putString("statusfillstudentprofile", "false");
        editor.putString("statussetpasswordscreen", "false");
        editor.putString("statuschoosegradescreen", "false");
        editor.putString("statuschoosedashboardscreen", "true");
        editor.putString("statusentermobilenumber",mobilenumber);
        editor.putString("statusopenprofilewithoutpin", "false");
        editor.apply();
        init();
        setListener();
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());  //AuroApp.getAppContext()
        AppLogger.e("DashbaordMain", "oncreate step 1");

    }

    public void callFetchUserPreference() {
        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        //  checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getChildData();
        int gradeid = AuroAppPref.INSTANCE.getModelInstance().getUserclass();
        String gradeforsubjectpreference = String.valueOf(gradeid);
        AppLogger.e("DashbaordMain", "oncreate step 2");
        if (gradeforsubjectpreference.equals("11")||gradeforsubjectpreference.equals("12")||gradeforsubjectpreference.equals(11)||gradeforsubjectpreference.equals(12)) {
            FetchStudentPrefReqModel fetchStudentPrefReqModel = new FetchStudentPrefReqModel();
            fetchStudentPrefReqModel.setUserId(prefModel.getUserId());
            viewModel.checkInternet(Status.FETCH_STUDENT_PREFERENCES_API, fetchStudentPrefReqModel);
        }
    }

    @Override
    protected void init() {

        binding = DataBindingUtil.setContentView(this, getLayout());
        // ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AuroScholarDashBoardViewModel.class);
        binding.setLifecycleOwner(this);
        mContext = DashBoardMainActivity.this;
        setProgressVal();
     //   funnelStudentDashBoard();
        //  checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getChildData();
        setListener();

        callDashboardApi();
        // checkRefferedData();

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        }
        else {
            SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
            String gradeforsubjectpreference = String.valueOf(AuroAppPref.INSTANCE.getModelInstance().getUserclass());

            if (gradeforsubjectpreference.equals("11")||gradeforsubjectpreference.equals("12")||gradeforsubjectpreference.equals(11)||gradeforsubjectpreference.equals(12)){
                SharedPreferences.Editor editor1 = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                editor1.putString("gradeforsubjectpreferencewithoutpin", "false");
                editor1.apply();
                SharedPreferences preferences = getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("gradeforsubjectpreference");
                editor.apply();
            }

            observeServiceResponse();

            getDashboardMenu();

        }
        binding.naviagtionContent.bottomNavigation.setVisibility(View.GONE);
        setupNavigation();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setLogin(true);
        AuroAppPref.INSTANCE.setPref(prefModel);
        callGetInstructionsApi(AppConstant.InstructionsType.AFTER_LOGIN);
        // getRefferalPopUp();
        checkForGradeScreen();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    }

    private void checkForGradeScreen() {
        setHomeFragmentTab();
    }



    @Override
    protected void setListener() {
        clickLisner = this;
        binding.naviagtionContent.bottomNavigation.setOnNavigationItemSelectedListener(this);
        binding.naviagtionContent.bottomSecondnavigation.setOnNavigationItemSelectedListener(this);
        selectMoreNavigationMenu(4);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_dash_board_main;
    }

    public void auroStudentscholarSdk(int status) {
        // checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getChildData();
        inputModel = new AuroScholarInputModel();
        inputModel.setMobileNumber(AuroAppPref.INSTANCE.getModelInstance().getUserMobile());
        String newdeviceToken = deviceToken;
        if (!TextUtil.isEmpty(newdeviceToken)) {
            inputModel.setDeviceToken(newdeviceToken);
            AppLogger.v("sdkDeviceToken", newdeviceToken);
        }
        //Mandatory
        inputModel.setPartner_unique_id(prefModel.getPartneruniqueid());
        inputModel.setPartner_api_key(prefModel.getApikey());
        inputModel.setStudentClass(String.valueOf(AuroAppPref.INSTANCE.getModelInstance().getUserclass()));
        inputModel.setRegitrationSource(prefModel.getPartnersource());
        inputModel.setMobileNumber(prefModel.getUserMobile());
        inputModel.setActivity(this); //Mandatory
        inputModel.setFragmentContainerUiId(R.id.home_container);
        //Mandatory
        inputModel.setReferralLink("");
        inputModel.setPartnerSource(prefModel.getPartnersource()); //this id is provided by auroscholar for valid partner
        inputModel.setSdkcallback(new SdkCallBack() {
            @Override
            public void callBack(String message) {

            }

            @Override
            public void logOut() {
                AppLogger.e("Chhonker", "Logout");
                int userType = prefModel.getUserType();
                prefModel.setLogin(false);
                SharedPreferences preferences =getSharedPreferences("My_Pref",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                SharedPreferences.Editor editor1 = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                editor1.putString("statusparentprofile", "false");
                editor1.putString("statusfillstudentprofile", "false");
                editor1.putString("statussetpasswordscreen", "false");
                editor1.putString("statuschoosegradescreen", "false");
                editor1.putString("statuschoosedashboardscreen", "false");
                editor1.putString("statusopenprofilewithoutpin", "false");

                editor1.apply();
                AuroAppPref.INSTANCE.clearPref();

                funnelStudentLogOut();
                       finishAffinity();
//                Intent intent = new Intent(DashBoardMainActivity.this, SDKActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }

            @Override
            public void commonCallback(Status status, Object o) {
                switch (status) {
                    case NAV_CHANGE_GRADE_CLICK:

                        break;
                }
            }
        });
        setRequiredData();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (status == 0) {
            AppLogger.e("notification ", "step 9 ");

            DynamiclinkResModel model = prefModel.getDynamiclinkResModel();
            dashboardResModel = prefModel.getDashboardResModel();
            if (model != null && !TextUtil.isEmpty(model.getNavigationTo()) && model.getNavigationTo().equalsIgnoreCase(AppConstant.NavigateToScreen.STUDENT_KYC)) {

                if (prefModel.getDashboardResModel() != null && viewModel.homeUseCase.checkKycStatus(dashboardResModel)) {
                    openKYCViewFragment(dashboardResModel, 1);
                } else {
                    openKYCFragment(dashboardResModel, 1);
                }

            } else if (model != null && !TextUtil.isEmpty(model.getNavigationTo()) && model.getNavigationTo().equalsIgnoreCase(AppConstant.NavigateToScreen.STUDENT_CERTIFICATE)) {

                openCertificate();

            } else if (model != null && !TextUtil.isEmpty(model.getNavigationTo()) && model.getNavigationTo().equalsIgnoreCase(AppConstant.NavigateToScreen.PAYMENT_TRANSFER)) {

                if (prefModel.getDashboardResModel() != null) {
                    int approvedMoney = ConversionUtil.INSTANCE.convertStringToInteger(dashboardResModel.getApproved_scholarship_money());
                    if (approvedMoney > 0) {
                        openSendMoneyFragment();
                    } else {
                        // openFragment(AuroScholar.startAuroSDK(inputModel));
                    }
                } else {
                    // openFragment(AuroScholar.startAuroSDK(inputModel));
                }


            } else {
                // openFragment(AuroScholar.startAuroSDK(inputModel));
            }
        } else {
            AppLogger.e("notification ", "step 10 ");

            NotificationDataModel notificationDataModel = prefModel.getNotificationDataModel();
            if (notificationDataModel.getNavigateto().equalsIgnoreCase(AppConstant.NavigateToScreen.STUDENT_KYC)) {
                AppLogger.e("notification ", "step 11 ");
                if (prefModel.getDashboardResModel() != null && viewModel.homeUseCase.checkKycStatus(dashboardResModel)) {
                    openKYCViewFragment(dashboardResModel, 1);
                    AppLogger.e("notification ", "step 12 ");
                } else {
                    openKYCFragment(dashboardResModel, 1);
                    AppLogger.e("notification ", "step 13 ");

                }
            } else if (notificationDataModel.getNavigateto().equalsIgnoreCase(AppConstant.NavigateToScreen.STUDENT_CERTIFICATE)) {
                openCertificate();
            } else if (notificationDataModel.getNavigateto().equalsIgnoreCase(AppConstant.NavigateToScreen.PAYMENT_TRANSFER)) {
                if (prefModel.getDashboardResModel() != null) {

                    int approvedMoney = ConversionUtil.INSTANCE.convertStringToInteger(dashboardResModel.getApproved_scholarship_money());
                    if (approvedMoney > 0) {
                        openSendMoneyFragment();
                    } else {
                        // openFragment(AuroScholar.startAuroSDK(inputModel));
                    }
                } else {
                    // openFragment(AuroScholar.startAuroSDK(inputModel));
                }
            } else if (notificationDataModel.getNavigateto().equalsIgnoreCase(AppConstant.NavigateToScreen.FRIENDS_LEADERBOARD)) {
                openLeaderBoardFragment(new FriendsLeaderBoardListFragment());

            } else {
                // openFragment(AuroScholar.startAuroSDK(inputModel));
            }
            prefModel.setNotificationDataModel(null);
            AuroAppPref.INSTANCE.setPref(prefModel);
        }
    }

    public void callRefferApi() {
        DynamiclinkResModel dynamiclinkResModel = new DynamiclinkResModel();
        dynamiclinkResModel.setReffeUserId(prefModel.getUserId());
        dynamiclinkResModel.setSource(AppConstant.AURO_ID);
        dynamiclinkResModel.setNavigationTo(AppConstant.NavigateToScreen.STUDENT_DASHBOARD);
        dynamiclinkResModel.setReffer_type("" + AppConstant.UserType.STUDENT);
        AppLogger.e("callRefferApi", "step 3" + new Gson().toJson(dynamiclinkResModel));
        viewModel.checkInternet(Status.DYNAMIC_LINK_API, dynamiclinkResModel);
    }


    public void callDashboardApi() {
        inputModel = new AuroScholarInputModel();
        inputModel.setMobileNumber(AuroAppPref.INSTANCE.getModelInstance().getUserMobile());
        String newdeviceToken = deviceToken;
        if (!TextUtil.isEmpty(newdeviceToken)) {
            inputModel.setDeviceToken(newdeviceToken);
            AppLogger.v("sdkDeviceToken", newdeviceToken);
        }
        //Mandatory
        inputModel.setPartner_unique_id(prefModel.getPartneruniqueid());
        inputModel.setPartner_api_key(prefModel.getApikey());
        inputModel.setStudentClass(String.valueOf(AuroAppPref.INSTANCE.getModelInstance().getUserclass()));
        inputModel.setRegitrationSource(prefModel.getPartnersource());
        inputModel.setMobileNumber(prefModel.getUserMobile());
        inputModel.setActivity(this); //Mandatory
        inputModel.setFragmentContainerUiId(R.id.home_container);
        inputModel.setUserid(prefModel.getUserId());
        inputModel.setDeviceToken(prefModel.getDeviceToken());
        //Mandatory
        inputModel.setReferralLink("");
        inputModel.setPartnerSource(prefModel.getPartnersource());
        viewModel.checkInternet(DASHBOARD_API, inputModel);
    }

    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(mContext, fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }
    public void openLeaderBoardFragment(Fragment fragment) {
        ((AppCompatActivity) (this)).getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.home_container, fragment, DashBoardMainActivity.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_VERSION_UPDATE:
                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                    AppLogger.e(TAG, "REQ_CODE_VERSION_UPDATE method calling 1 ");                    // If the update is cancelled or fails,
                    unregisterInstallStateUpdListener();
                }

                break;
        }

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }


    public void dismissApplication() {
        if (backPress == 0) {
            backPress++;
            ViewUtil.showSnackBar(binding.getRoot(),prefModel.getLanguageMasterDynamic().getDetails().getPressAgainForExit());
        } else {
            finishAffinity();
        }
    }

    @Override
    public void onClickListener() {
        openProfileFragment();
    }

    public void checkVersion(CheckVerResModel checkVerResModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setCheckVerResModel(checkVerResModel);
        AuroAppPref.INSTANCE.setPref(prefModel);
        this.checkVerResModel = checkVerResModel;
        if (this.checkVerResModel != null) {
            boolean isStatus = AppUtil.checkForUpdate(prefModel.getCheckVerResModel().getNewVersion());
            int priority = ConversionUtil.INSTANCE.convertStringToInteger(checkVerResModel.getPriorty());
            if (isStatus) {
                if (priority == 4) {
                    openUpdateDialog();
                } else {
                    checkForAppUpdate();
                }

            }
        }
    }

    private void openUpdateDialog() {
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(this);
        customDialogModel.setTitle(getApplicationContext().getResources().getString(R.string.update_auroscholar));
        customDialogModel.setContent(getApplicationContext().getResources().getString(R.string.updateMessage));
        updateCustomDialog = new UpdateCustomDialog(this, customDialogModel);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(updateCustomDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        updateCustomDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(updateCustomDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateCustomDialog.setCancelable(false);
        updateCustomDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNewAppVersionState();
    }

    private void checkForAppUpdate() {
        AppLogger.e(TAG, "checkForAppUpdate method calling 1 ");                    // If the update is cancelled or fails,
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                AppLogger.e(TAG, "checkForAppUpdate method calling 2");
                if (installState.installStatus() == InstallStatus.DOWNLOADED)
                    AppLogger.e(TAG, "checkForAppUpdate method calling 3");
                popupSnackbarForCompleteUpdateAndUnregister();
            }
        };

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            AppLogger.e(TAG, "checkForAppUpdate method calling 4");
            int priority = ConversionUtil.INSTANCE.convertStringToInteger(checkVerResModel.getPriorty());
            AppLogger.e(TAG, "checkForAppUpdate method calling 5 & priority-" + priority);
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                AppLogger.e(TAG, "checkForAppUpdate method calling 6");
                if (priority == 0 || priority == 1) {
                    AppLogger.e(TAG, "checkForAppUpdate method calling 7");
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        AppLogger.e(TAG, "checkForAppUpdate method calling 8");
                        appUpdateManager.registerListener(installStateUpdatedListener);
                        startAppUpdateFlexible(appUpdateInfo);
                    }
                } else {
                    AppLogger.e(TAG, "checkForAppUpdate method calling 9");
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        AppLogger.e(TAG, "checkForAppUpdate method calling 10");
                        startAppUpdateImmediate(appUpdateInfo);
                    }
                }
            }
        });
    }

    private void popupSnackbarForCompleteUpdateAndUnregister() {
        AppLogger.e(TAG, "popupSnackbarForCompleteUpdateAndUnregister method calling 1 ");                    // If the update is cancelled or fails,
        Snackbar snackbar =
                Snackbar.make(binding.getRoot(), "App is downloaded.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }


    private void checkNewAppVersionState() {
        AppLogger.e(TAG, "checkNewAppVersionState method calling 1 ");
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            AppLogger.e(TAG, "checkNewAppVersionState method calling 2 ");
                            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                popupSnackbarForCompleteUpdateAndUnregister();
                                AppLogger.e(TAG, "checkNewAppVersionState method calling 3 ");
                            }
                            AppLogger.e(TAG, "checkNewAppVersionState method calling 4");
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                AppLogger.e(TAG, "checkNewAppVersionState method calling 5 ");
                                startAppUpdateImmediate(appUpdateInfo);
                            }
                        });

    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        AppLogger.e(TAG, "startAppUpdateImmediate method calling 1 ");
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            AppLogger.e(TAG, "startAppUpdateImmediate method calling  Exception 2 ");
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        AppLogger.e(TAG, "startAppUpdateFlexible method calling 1 ");
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            AppLogger.e(TAG, "startAppUpdateFlexible method calling  Exception 2 ");
            unregisterInstallStateUpdListener();
        }
    }


    private void unregisterInstallStateUpdListener() {
        AppLogger.e(TAG, "unregisterInstallStateUpdListener method calling   1 ");
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        AppLogger.e(TAG, "unregisterInstallStateUpdListener method calling   2");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppLogger.e(TAG, "onDestroy method calling ");
        unregisterInstallStateUpdListener();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int itemId = menuItem.getItemId();
        if (itemId == R.id.item_home) {
            selectNavigationMenu(0);
            //openGenricSDK();
            AuroScholar.opendashboard();
         //   auroStudentscholarSdk(0);
        } else if (itemId == R.id.item_passport) {
            funnelPassportScreen();
            selectNavigationMenu(3);
            openTransactionsFragment();
        } else if (itemId == R.id.item_profile) {
            funnelStudentProfileScreen();
            selectNavigationMenu(2);
            openProfileFragment();
        } else if (itemId == R.id.item_more) {
            openitemMore();
        } else if (itemId == R.id.item_back) {
            closeItemMore();
            if (!isBackNormal) {
                selectNavigationMenu(0);
               // openGenricSDK();
                AuroScholar.opendashboard();
                isBackNormal = true;
            }
        } else if (itemId == R.id.item_partner) {
            selectNavigationMenu(1);
            handlePartnertabClick();
            funnelPartnerApp();
        } else if (itemId == R.id.item_logout) {
            openLogoutDialog();
        } else if (itemId == R.id.item_aurofriend) {
            isBackNormal = false;
            funnelStudentleaderBoardScreen();
            openFriendLeaderBoardFragment();
            selectMoreNavigationMenu(1);
        } else if (itemId == R.id.item_kyc) {
            openStudentKycInfoFragment();
            selectMoreNavigationMenu(2);
            isBackNormal = false;
        } else if (itemId == R.id.item_privacy_policy) {
            isBackNormal = false;
            selectMoreNavigationMenu(3);
            handlePrivacyPolicytabClick();
        }

        return false;
    }


    private void openitemMore() {
        int mainMoreBottomNavigation = binding.naviagtionContent.bottomSecondnavigation.getVisibility();
        if (mainMoreBottomNavigation != 0) {
            AppLogger.v("Animation", "mainMoreBottomNavigation 0  item_more--->" + mainMoreBottomNavigation);
            openSwipeRightSelectionLayout(binding.naviagtionContent.bottomSecondnavigation, binding.naviagtionContent.bottomNavigation);
        }
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
        inputModel.setActivity((Activity) mContext);
        AuroScholar.startAuroSDK(inputModel);
    }

    public void closeItemMore() {
        selectMoreNavigationMenu(4);
        int mainMoresSecondNavigation = binding.naviagtionContent.bottomSecondnavigation.getVisibility();
        if (mainMoresSecondNavigation == 0) {
            AppLogger.v("Animation", "mainMoreBottomNavigation 0 item_back --->" + mainMoresSecondNavigation);
            openSwipeLeftSelectionLayout(binding.naviagtionContent.bottomSecondnavigation, binding.naviagtionContent.bottomNavigation);
        }
    }


    public void setHomeFragmentTab() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        NotificationDataModel notificationDataModel = prefModel.getNotificationDataModel();
        AppLogger.e("notification ", "step 1");
        openFragment(new MainQuizHomeFragment());
        selectNavigationMenu(0);

    }

    public void openFragmentOnNotificationstatus(NotificationDataModel notificationDataModel) {
        switch (notificationDataModel.getNavigateto()) {
            case AppConstant.NavigateToScreen.STUDENT_DASHBOARD:
                auroStudentscholarSdk(0);
                AppLogger.e("notification ", "step 4 STUDENT_DASHBOARD");
                break;


            case AppConstant.NavigateToScreen.STUDENT_KYC:
                AppLogger.e("notification ", "step 4 STUDENT_KYC");
                auroStudentscholarSdk(1);
                break;
            case AppConstant.NavigateToScreen.STUDENT_CERTIFICATE:
                AppLogger.e("notification ", "step 6 STUDENT_CERTIFICATE");
                auroStudentscholarSdk(1);
                break;

            case AppConstant.NavigateToScreen.FRIENDS_LEADERBOARD:
                AppLogger.e("notification ", "step 7 FRIENDS_LEADERBOARD");
                auroStudentscholarSdk(1);

                break;

            case AppConstant.NavigateToScreen.PAYMENT_TRANSFER:
                AppLogger.e("notification ", "step 7 FRIENDS_LEADERBOARD");
                auroStudentscholarSdk(1);

                break;

            default:
                AppLogger.e("notification ", "step default");
                auroStudentscholarSdk(0);
                break;


        }

        AppLogger.e("notification ", "step 8");
    }

    public void selectNavigationMenu(int pos) {
        binding.naviagtionContent.bottomNavigation.getMenu().getItem(pos).setChecked(true);
    }

    public void selectMoreNavigationMenu(int pos) {
        binding.naviagtionContent.bottomSecondnavigation.getMenu().getItem(pos).setChecked(true);

    }

    public void popBackStack() {
        backPress = 0;
        getSupportFragmentManager().popBackStack();
    }


    public static void setListingActiveFragment(int listingActiveFragment) {
        LISTING_ACTIVE_FRAGMENT = listingActiveFragment;
    }


    public void alertDialogForQuitQuiz() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.quiz_exit_txt);

        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + this.getString(R.string.yes) + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                popBackStack();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>" + this.getString(R.string.no) + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void observeServiceResponse() {
        AppLogger.v("observeServiceResponse", " response Step 1");

        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);

        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    binding.naviagtionContent.progressbar.pgbar.setVisibility(View.GONE);
                    if (responseApi.apiTypeStatus == DASHBOARD_API) {
                        onApiSuccess(responseApi);
                        DashboardResModel dashboardResModel = (DashboardResModel) responseApi.data;
                        if (commonCallBackListner != null) {
                            commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, LISTNER_SUCCESS, dashboardResModel));
                        }
                    }
                    else if (responseApi.apiTypeStatus == SEND_OTP) {
                        SendOtpResModel sendOtp = (SendOtpResModel) responseApi.data;
                        if (!sendOtp.getError()) {
                            checkOtpDialog();
                        }
                    }
                    else if (responseApi.apiTypeStatus == Status.VERIFY_OTP) {
                        VerifyOtpResModel verifyOtp = (VerifyOtpResModel) responseApi.data;
                        AppLogger.v("OTP_MAIN", "Step 7");
                        if (!verifyOtp.getError()) {
                            AppLogger.v("OTP_MAIN", "Step 8");
                            // checkUserType();
                            if (customOtpDialog != null && customOtpDialog.isShowing()) {
                                customOtpDialog.dismiss();
                                customOtpDialog.hideProgress();
                                if (commonCallBackListner != null) {
                                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.OTP_VERIFY, ""));
                                }
                            }
                            AppLogger.v("OTP_MAIN", "Step 9");
                        } else {
                            AppLogger.v("OTP_MAIN", "Step 10");
                            if (customOtpDialog != null && customOtpDialog.isShowing()) {
                                customOtpDialog.hideProgress();
                                customOtpDialog.showSnackBar(verifyOtp.getMessage());
                            }
                        }
                    }


                    else if (responseApi.apiTypeStatus == Status.FETCH_STUDENT_PREFERENCES_API) {

                        AppLogger.v("OTP_MAIN", "Step 11");
                        FetchStudentPrefResModel fetchStudentPrefResModel = (FetchStudentPrefResModel) responseApi.data;
                        if (fetchStudentPrefResModel.getPreference().isEmpty()) {
                            openSubjectPreferenceScreen();
                            AppLogger.v("OTP_MAIN", "Step 12");
                        } else {
                            AppLogger.v("OTP_MAIN", "Step 13");
                            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                            prefModel.setFetchStudentPrefResModel(fetchStudentPrefResModel);
                            AuroAppPref.INSTANCE.setPref(prefModel);

                            if (commonCallBackListner != null) {
                                AppLogger.v("OTP_MAIN", "Step 14");
                                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.FETCH_STUDENT_PREFERENCES_API, fetchStudentPrefResModel));
                            }
                        }


                    }

                    else if (responseApi.apiTypeStatus == Status.GET_INSTRUCTIONS_API) {
                        InstructionsResModel instructionsResModel = (InstructionsResModel) responseApi.data;
                        AppLogger.v("Notice", "else part  GET_INSTRUCTIONS_API API RESPONSE First"+instructionsResModel.getError());
                        if (!instructionsResModel.getError()) {
                            AppLogger.v("Notice", "else part  GET_INSTRUCTIONS_API API RESPONSE Second"+instructionsResModel.getError());
                            checkDisclaimer(instructionsResModel);
                            AppLogger.v("Notice", "else part  GET_INSTRUCTIONS_API API RESPONSE Third " +instructionsResModel.getError());
                            if (commonCallBackListner != null) {
                                AppLogger.v("Notice", "else part  GET_INSTRUCTIONS_API API RESPONSE Four ");
                                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.GET_INSTRUCTIONS_API, instructionsResModel));
                            }
                        } else {
                            AppLogger.v("Notice"," GET_INSTRUCTIONS_API  ");
                            AppLogger.v("Notice", "else part  GET_INSTRUCTIONS_API");
                        }
                    }
                    else if (responseApi.apiTypeStatus == Status.NOTICE_INSTRUCTION){
                        NoticeInstruction noticeInstruction = (NoticeInstruction) responseApi.data;
                        AppLogger.v("Notice"," NOTICE_INSTRUCTION  last  "+noticeInstruction.getData().getMsgText());
                        openNoticeDialog(noticeInstruction);
                        AppLogger.v("Notice"," NOTICE_INSTRUCTION  last"+noticeInstruction.getData().getId());
                    }
                    else if (responseApi.apiTypeStatus == Status.GET_MESSAGE_POP_UP){
                        ShowDialogModel showDialogModel = (ShowDialogModel) responseApi.data;
                        openGradeDialog(showDialogModel);
                    }
                    else if (responseApi.apiTypeStatus == Status.PENDING_KYC_DOCS){
                        ErrorResponseModel noticeInstruction = (ErrorResponseModel) responseApi.data;
                        if(!noticeInstruction.getMessage().equals("")) {
                            ViewUtil.showSnackBar(binding.getRoot(), noticeInstruction.getMessage());
                        }
                        AppLogger.v("Pending_Pradeep","PENDING_KYC_DOCS");


                    }
                    else if (responseApi.apiTypeStatus == Status.OTP_OVER_CALL) {
                        OtpOverCallResModel otpOverCallResModel = (OtpOverCallResModel) responseApi.data;
                        if (!otpOverCallResModel.getError()) {
                            AppLogger.v("Otp_pradeep", "Step 6");
                            checkOtpDialog();
                        } else {
                            ViewUtil.showSnackBar(binding.getRoot(), otpOverCallResModel.getMessage());
                            AppLogger.e(TAG, "Step 7");
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
                    binding.naviagtionContent.progressbar.pgbar.setVisibility(View.GONE);
                    AppLogger.v("OTP_MAIN", "Step 15");
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, LISTNER_FAIL, (String) responseApi.data));
                    }
                    break;

                case AUTH_FAIL:
                case FAIL_400:
                    AppLogger.v("OTP_MAIN", "Step 16  " + (String) responseApi.data);
                    binding.naviagtionContent.progressbar.pgbar.setVisibility(View.GONE);
                    if (customOtpDialog != null && customOtpDialog.isShowing()) {
                        customOtpDialog.hideProgress();
                        customOtpDialog.showSnackBar((String) responseApi.data);
                    }
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, LISTNER_FAIL, (String) responseApi.data));
                    }


                    break;
            }

        });
    }

    private void onApiSuccess(ResponseApi responseApi) {
        dashboardResModel = (DashboardResModel) responseApi.data;
        AppUtil.setDashboardResModelToPref(dashboardResModel);
        AppUtil.setEmptyToDynamicResponseModel();
    }

    public void dashboardModel(DashboardResModel model) {
        dashboardResModel = model;
        AppUtil.setDashboardResModelToPref(model);
    }

    public void openNoticeDialog( NoticeInstruction noticeInstruction ){
        if(noticeInstruction.getData()!= null && noticeInstruction.getData().getShowDailogue().equals("1")){
            noticeDialogBox = new NoticeDialogBox(this,clickLisner);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(noticeDialogBox.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            noticeDialogBox.getWindow().setAttributes(lp);
            Objects.requireNonNull(noticeDialogBox.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            noticeDialogBox.setCancelable(false);
            noticeDialogBox.show();
            noticeDialogBox.setAfterLoginInstruction(noticeInstruction);
        }else{
            callingCongratsDialog();
        }
    }

    public void callingCongratsDialog(){
        PendingKycDocsModel pendingKycDocsModel = new PendingKycDocsModel();
        pendingKycDocsModel.setUserId(prefModel.getUserId());
        pendingKycDocsModel.setUserPreferedLanguageId(Integer.valueOf(prefModel.getUserLanguageId()));
        viewModel.checkInternet(Status.GET_MESSAGE_POP_UP, pendingKycDocsModel);

    }

    public void openGradeDialog(ShowDialogModel showDialogModel){
        AppLogger.v("Dialog_pradeep","Open Notice Dialog - "+showDialogModel.getShowDailogue().equals("No"));
        if(showDialogModel.getShowDailogue().equals("Yes") && showDialogModel.getImgUrl()!= null && !showDialogModel.getImgUrl().equals("")){
            AppLogger.v("Dialog_pradeep","Enter   - "+showDialogModel.getShowDailogue().equals("NO"));
            gradeChnageCongDialogBox = new GradeChnageCongDialogBox(this,clickLisner);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(gradeChnageCongDialogBox.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            gradeChnageCongDialogBox.getWindow().setAttributes(lp);
            Objects.requireNonNull(gradeChnageCongDialogBox.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            gradeChnageCongDialogBox.setCancelable(false);
            gradeChnageCongDialogBox.show();
            gradeChnageCongDialogBox.setAfterLoginInstruction(showDialogModel);
        }
    }

    public void openKYCFragment(DashboardResModel dashboardResModel, int status) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.home_container);
        if (currentFragment instanceof KYCFragment) {
            AppLogger.e("chhonker", "find the current fragment yes");
            return;
        } else {
            AppLogger.e("chhonker", "find the current fragment not");
        }

        Bundle bundle = new Bundle();
        KYCFragment kycFragment = new KYCFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        if (status != 0) {
            bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.DYNAMIC_LINK);
        }
        kycFragment.setArguments(bundle);
        openFragment(kycFragment);
    }

    public void openKYCViewFragment(DashboardResModel dashboardResModel, int status) {
        Bundle bundle = new Bundle();
        KYCViewFragment kycViewFragment = new KYCViewFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        if (status != 0) {
            bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.DYNAMIC_LINK);
        }
        kycViewFragment.setArguments(bundle);
        openFragment(kycViewFragment);
    }

    public void openCertificate() {
        Bundle bundle = new Bundle();
        CertificateFragment certificateFragment = new CertificateFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.DYNAMIC_LINK);
        certificateFragment.setArguments(bundle);
        openFragment(certificateFragment);
    }


    public void openSendMoneyFragment() {
        Bundle bundle = new Bundle();
        SendMoneyFragment sendMoneyFragment = new SendMoneyFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.DYNAMIC_LINK);
        sendMoneyFragment.setArguments(bundle);
        openFragment(sendMoneyFragment);
    }


    public void setListner(CommonCallBackListner listner) {
        this.commonCallBackListner = listner;
    }


    private void setRequiredData() {
        AuroScholarDataModel auroScholarDataModel = new AuroScholarDataModel();
        auroScholarDataModel.setMobileNumber(prefModel.getUserMobile());
        auroScholarDataModel.setStudentClass(String.valueOf(prefModel.getUserclass()));
        auroScholarDataModel.setRegitrationSource(auroScholarDataModel.getRegitrationSource());
        auroScholarDataModel.setActivity((Activity) DashBoardMainActivity.this);
        auroScholarDataModel.setFragmentContainerUiId(auroScholarDataModel.getFragmentContainerUiId());
        auroScholarDataModel.setReferralLink(auroScholarDataModel.getReferralLink());
        auroScholarDataModel.setDevicetoken(prefModel.getDeviceToken());
        if (TextUtil.isEmpty(auroScholarDataModel.getPartnerSource())) {
            auroScholarDataModel.setPartnerSource("");
        } else {
            auroScholarDataModel.setPartnerSource(auroScholarDataModel.getPartnerSource());
        }
        AuroApp.setAuroModel(auroScholarDataModel);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            AppLogger.e("chhonker-", "Touch Event");
            if (AppUtil.commonCallBackListner != null) {
                AppUtil.commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.SCREEN_TOUCH, ""));
            }
        }
        return super.dispatchTouchEvent(event);
    }


    public void hideBottomNavigationView() {
        binding.naviagtionContent.bottomNavigation.setVisibility(View.GONE);
        AppLogger.e("hideBottomNavigationView", "i am calling");
        BottomNavigationView view = binding.naviagtionContent.bottomNavigation;
        view.clearAnimation();
        view.animate().translationY(view.getHeight()).setDuration(300);
    }
    public void visibilityOfNavigation(int status) {
        if (status == 0) {
            binding.naviagtionContent.bottomNavigationDesgin.setVisibility(View.GONE);
        } else {
            binding.naviagtionContent.bottomNavigationDesgin.setVisibility(View.VISIBLE);
        }
    }

    public void showBottomNavigationView() {
        binding.naviagtionContent.bottomNavigation.setVisibility(View.VISIBLE);
        AppLogger.e("showBottomNavigationView", "i am calling");
        BottomNavigationView view = binding.naviagtionContent.bottomNavigation;
        view.clearAnimation();
        view.animate().translationY(0).setDuration(300);
    }

    public void openTransactionsFragment() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        Bundle bundle = new Bundle();
        TransactionsFragment transactionsFragment = new TransactionsFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, prefModel.getDashboardResModel());
        transactionsFragment.setArguments(bundle);
        openFragment(transactionsFragment);
    }


    public void openStudentKycInfoFragment() {
        StudentKycInfoFragment transactionsFragment = new StudentKycInfoFragment();
        openFragment(transactionsFragment);
    }

    public void openProfileFragment() {
        closeItemMore();
        selectNavigationMenu(2);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        Bundle bundle = new Bundle();
        StudentProfileFragment studentProfile = new StudentProfileFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, prefModel.getDashboardResModel());
        bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.STUDENT_PROFILE);
        studentProfile.setArguments(bundle);
        openFragment(studentProfile);
    }

    public void openPartnersFragment() {
        selectNavigationMenu(1);
        AppLogger.v("Mindler","Step 1 Partner");
        PartnersFragment partnersFragment = new PartnersFragment();
        openFragment(partnersFragment);
    }


    public void openChangeLanguageDialog() {
        LanguageChangeDialog languageChangeDialog = new LanguageChangeDialog(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(languageChangeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        languageChangeDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(languageChangeDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        languageChangeDialog.setCancelable(true);
        languageChangeDialog.show();
    }

    private void openSwipeRightSelectionLayout(View viewAnimation, View secondAnimation) {
        viewAnimation.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.frag_enter_right);
        viewAnimation.startAnimation(anim);

        Animation secondanim = AnimationUtils.loadAnimation(this, R.anim.frag_exit_left);
        secondAnimation.startAnimation(secondanim);
        secondAnimation.setVisibility(View.GONE);

    }

    private void openSwipeLeftSelectionLayout(View viewAnimation, View secondAnimation) {
        secondAnimation.setVisibility(View.VISIBLE);
        Animation secondanim = AnimationUtils.loadAnimation(this, R.anim.frag_enter_left);
        secondAnimation.startAnimation(secondanim);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.frag_exit_right);
        viewAnimation.startAnimation(anim);
        viewAnimation.setVisibility(View.GONE);

    }

    private void
    openLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String yes = this.getString(R.string.yes);
        String no = this.getString(R.string.no);
        builder.setMessage(getString(R.string.sure_to_logout));
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                yes = details.getYes();
                no = details.getNo();
                builder.setMessage(details.getSureToLogout());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + yes + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                logout();
            }
        });

        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>" + no + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void logout() {


        AppLogger.e("Chhonker", "Logout");
        AuroAppPref.INSTANCE.clearPref();
        SharedPreferences preferences =getSharedPreferences("My_Pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        finishAffinity();
    }


    private void openFriendLeaderBoardFragment() {
        FriendsLeaderBoardListFragment fragment = new FriendsLeaderBoardListFragment();
        openFragment(fragment);
    }


    public void showSnackbar(String msg) {
        ViewUtil.showSnackBar(binding.getRoot(), msg);
    }

    private void funnelStudentDashBoard() {
        //AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentDashBoard();
    }

    private void funnelStudentLogOut() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentLogOut();
    }

    private void funnelStudentleaderBoardScreen() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentLeaderBoardScreen();
    }

    private void funnelStudentProfileScreen() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentProfileScreen();

    }

    private void funnelPassportScreen() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentPassportScreen();

    }

    private void funnelPartnerApp() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentPartnerScreen();

    }

    public void setProgressVal() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setDashboardaApiNeedToCall(true);
        AuroAppPref.INSTANCE.setPref(prefModel);

    }

    @Override
    protected void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    private void checkDisclaimer(InstructionsResModel instructionsResModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.v("Notice", "checkDisclaimer outer ");
        if (!prefModel.isPreLoginDisclaimer()) {
            AppLogger.v("Notice", "checkDisclaimer if ");
            loginDisclaimerDialog = new LoginDisclaimerDialog(this,clickLisner);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(loginDisclaimerDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            loginDisclaimerDialog.getWindow().setAttributes(lp);
            Objects.requireNonNull(loginDisclaimerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loginDisclaimerDialog.setCancelable(false);
            loginDisclaimerDialog.show();
            loginDisclaimerDialog.setAfterLoginInstruction(instructionsResModel);
        }else{
            AppLogger.v("Notice", "checkDisclaimer else ");

            if(AppConstant.InstructionsType.SCHOLARSHIP_TRANSFER != typeGradeChange) {
               viewModel.checkInternet(Status.NOTICE_INSTRUCTION, null);
            }

        }
    }


    private void checkOtpDialog() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        customOtpDialog = new CustomOtpDialog(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(customOtpDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        customOtpDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(customOtpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customOtpDialog.setCancelable(false);
        customOtpDialog.show();


    }


    public void callOverOTPApi() {
        OtpOverCallReqModel reqModel=new OtpOverCallReqModel();
        String phonenumber = AuroAppPref.INSTANCE.getModelInstance().getUserMobile();
        reqModel.setIsType(0);
        reqModel.setMobileNo(phonenumber);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.naviagtionContent.progressbar.pgbar.setVisibility(View.GONE);
            }
        }, 10000);
        binding.naviagtionContent.progressbar.pgbar.setVisibility(View.VISIBLE);
        viewModel.checkInternet(Status.OTP_OVER_CALL,reqModel);
    }


    public void sendOtpApiReqPass() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.naviagtionContent.progressbar.pgbar.setVisibility(View.GONE);
            }
        }, 10000);
        binding.naviagtionContent.progressbar.pgbar.setVisibility(View.VISIBLE);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String phonenumber = AuroAppPref.INSTANCE.getModelInstance().getUserMobile();
        SendOtpReqModel mreqmodel = new SendOtpReqModel();
        mreqmodel.setMobileNo(phonenumber);
        viewModel.checkInternet(SEND_OTP, mreqmodel);


    }

    public void verifyOtpRxApi(String otptext) {
        ViewUtil.hideKeyboard(this);
        if (customOtpDialog != null && customOtpDialog.isShowing()) {
            customOtpDialog.showProgress();
            VerifyOtpReqModel mverifyOtpRequestModel = new VerifyOtpReqModel();
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            int type = prefModel.getUserType();
            if (type == AppConstant.UserType.TEACHER) {
                mverifyOtpRequestModel.setUserType(1);
            } else {
                mverifyOtpRequestModel.setUserType(0);
                mverifyOtpRequestModel.setResgistrationSource("AuroScholr");
            }
            String phonenumber = AuroAppPref.INSTANCE.getModelInstance().getUserMobile();
            mverifyOtpRequestModel.setDeviceToken(deviceToken);
            mverifyOtpRequestModel.setMobileNumber(phonenumber);
            mverifyOtpRequestModel.setOtpVerify(otptext);
            mverifyOtpRequestModel.setSrId("");
            AppLogger.v("OTP_MAIN", "Step 1" + otptext);
            viewModel.checkInternet(Status.VERIFY_OTP, mverifyOtpRequestModel);
        }
    }

    public void setupNavigation() {
        int studentClass = AuroAppPref.INSTANCE.getModelInstance().getUserclass();
        if (studentClass < 10) {
            Menu menuDashboard = binding.naviagtionContent.bottomNavigation.getMenu();
            menuDashboard.findItem(R.id.item_partner).setTitle(R.string.certificatesmenuauro);

            Menu backMenuDashboard = binding.naviagtionContent.bottomSecondnavigation.getMenu();
            backMenuDashboard.findItem(R.id.item_privacy_policy).setTitle(R.string.privacy_policy_auro);
        } else {
            Menu menuDashboard = binding.naviagtionContent.bottomNavigation.getMenu();
            menuDashboard.findItem(R.id.item_partner).setTitle(R.string.partner_menuauro);

            Menu backMenuDashboard = binding.naviagtionContent.bottomSecondnavigation.getMenu();
            backMenuDashboard.findItem(R.id.item_privacy_policy).setTitle(R.string.certificatesmenuauro);
        }
    }

    private void handlePrivacyPolicytabClick() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int studentClass = prefModel.getUserclass();
        if (studentClass < 10) {
            openFragment(new PrivacyPolicyFragment());
        } else {
            openCertificate();
        }
    }


    private void handlePartnertabClick() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int studentClass = prefModel.getUserclass();
        if (studentClass < 10) {
            openCertificate();
        } else {
            openPartnersFragment();
        }
    }


    private void openSubjectPreferenceScreen() {
        if (loginDisclaimerDialog != null && loginDisclaimerDialog.isShowing()) {
            loginDisclaimerDialog.dismiss();
        }
        finish();
        Intent newIntent = new Intent(this, SubjectPreferencesActivity.class);
        startActivity(newIntent);
        finish();
    }

    public void openDialogForQuit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(prefModel.getLanguageMasterDynamic().getDetails().getPlease_confirm_if_you_want());
        String yes = prefModel.getLanguageMasterDynamic().getDetails().getYes();//this.getResources().getString(R.string.yes);
        String no = prefModel.getLanguageMasterDynamic().getDetails().getNo();//this.getResources().getString(R.string.no);

        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + yes + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogQuit, int which) {
                if (AppUtil.commonCallBackListner != null) {
                    AppUtil.commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.FINISH_DIALOG_CLICK, ""));
                }
            }
        });
        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>" + no + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogQuit, int which) {

                dialogQuit.dismiss();

            }
        });

        dialogQuit = builder.create();
        dialogQuit.show();
    }


    public void callGetInstructionsApi(String type) {
        typeGradeChange = type;
        String userlangid = AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId();
        int langid = Integer.parseInt(userlangid);
        InstructionModel instructionModel = new InstructionModel();
        instructionModel.setLanguageId(langid);
        instructionModel.setInstructionCode(type);
        viewModel.checkInternet(Status.GET_INSTRUCTIONS_API, instructionModel);
        AppLogger.v("Notice","callGetInstructionsApi " );
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        if(commonDataModel.getClickType()==Status.LOGIN_DISCLAMER_DIALOG){
            AppLogger.v("Notice","clickMethodNotification");
            viewModel.checkInternet(Status.NOTICE_INSTRUCTION, null);
        } else if(commonDataModel.getClickType()==Status.NOTICE_DIALOG){
            callingCongratsDialog();
        } else if(commonDataModel.getClickType() == Status.GRADE_CHANGE_DIALOG){
            String value = (String)commonDataModel.getObject();
            PendingKycDocsModel pendingKycDocsModel = new PendingKycDocsModel();
            pendingKycDocsModel.setUserId("971738");
            pendingKycDocsModel.setIsAgree(value);
            pendingKycDocsModel.setUserPreferedLanguageId(1);
            AppLogger.v("Pending_Pradeep","  calling GRADE_CHANGE_DIALOG   " );
            viewModel.checkInternet(Status.PENDING_KYC_DOCS, pendingKycDocsModel);

        }
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
    private void getDashboardMenu()
    {
        List<GenderData> genderList = new ArrayList<>();
        genderList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("key","Menu");
        map_data.put("language_id",prefModel.getUserLanguageId());

        RemoteApi.Companion.invoke().getGender(map_data)
                .enqueue(new Callback<GenderDataModel>() {
                    @Override
                    public void onResponse(Call<GenderDataModel> call, Response<GenderDataModel> response)
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
                            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                            int studentClass = prefModel.getUserclass();
                            if (studentClass < 10) {
                                Menu menuDashboard = binding.naviagtionContent.bottomNavigation.getMenu();

                                menuDashboard.findItem(R.id.item_partner).setTitle(genderList.get(1).getTranslatedName());



                                Menu backMenuDashboard = binding.naviagtionContent.bottomSecondnavigation.getMenu();
                                backMenuDashboard.findItem(R.id.item_privacy_policy).setTitle(genderList.get(8).getTranslatedName());
                            }
                            else {
                                Menu menuDashboard = binding.naviagtionContent.bottomNavigation.getMenu();
                                menuDashboard.findItem(R.id.item_partner).setTitle(genderList.get(10).getTranslatedName());

                                Menu backMenuDashboard = binding.naviagtionContent.bottomSecondnavigation.getMenu();
                                backMenuDashboard.findItem(R.id.item_privacy_policy).setTitle(genderList.get(1).getTranslatedName());
                            }
                            Menu menuDashboard = binding.naviagtionContent.bottomNavigation.getMenu();
                            menuDashboard.findItem(R.id.item_home).setTitle(genderList.get(0).getTranslatedName());
                            menuDashboard.findItem(R.id.item_profile).setTitle(genderList.get(2).getTranslatedName());
                            menuDashboard.findItem(R.id.item_passport).setTitle(genderList.get(3).getTranslatedName());
                            menuDashboard.findItem(R.id.item_more).setTitle(genderList.get(4).getTranslatedName());

                            Menu backMenuDashboard = binding.naviagtionContent.bottomSecondnavigation.getMenu();
                            backMenuDashboard.findItem(R.id.item_logout).setTitle(genderList.get(5).getTranslatedName());
                            backMenuDashboard.findItem(R.id.item_aurofriend).setTitle(genderList.get(6).getTranslatedName());
                            backMenuDashboard.findItem(R.id.item_kyc).setTitle(genderList.get(7).getTranslatedName());
                            backMenuDashboard.findItem(R.id.item_back).setTitle(genderList.get(9).getTranslatedName());


                        }
                        else
                        {
                            Log.d(TAG, "onResponseError: "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<GenderDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }
}

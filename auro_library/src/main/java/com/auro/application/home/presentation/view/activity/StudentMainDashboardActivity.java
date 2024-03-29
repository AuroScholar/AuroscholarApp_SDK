package com.auro.application.home.presentation.view.activity;

import static com.auro.application.core.common.Status.DASHBOARD_API;
import static com.auro.application.core.common.Status.FETCH_STUDENT_PREFERENCES_API;
import static com.auro.application.core.common.Status.LISTNER_SUCCESS;
import static com.auro.application.core.common.Status.SEND_OTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.home.data.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.common.OnItemClickListener;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityStudentMainDashboardBinding;
import com.auro.application.home.data.model.response.FetchStudentPrefResModel;
import com.auro.application.home.data.model.response.SendOtpResModel;
import com.auro.application.home.data.model.response.VerifyOtpResModel;
import com.auro.application.home.data.datasource.remote.HomeRemoteApi;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.NavItemModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.VerifyOtpReqModel;
import com.auro.application.home.presentation.view.fragment.KYCFragment;
import com.auro.application.home.presentation.view.fragment.KYCViewFragment;
import com.auro.application.home.presentation.view.fragment.StudentProfileFragment;
import com.auro.application.home.presentation.view.fragment.MainQuizHomeFragment;
import com.auro.application.home.presentation.view.fragment.SubjectPreferencesActivity;
import com.auro.application.home.presentation.viewmodel.HomeViewModel;
import com.auro.application.home.presentation.viewmodel.QuizViewModel;
import com.auro.application.teacher.presentation.view.fragment.MyClassRoomGroupFragment;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.disclaimer.CustomOtpDialog;
import com.auro.application.util.alert_dialog.disclaimer.LoginDisclaimerDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

public class StudentMainDashboardActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener, CommonCallBackListner {


    private String TAG = StudentMainDashboardActivity.class.getSimpleName().toString();

    @Inject
    HomeRemoteApi remoteApi;

    @Inject
    @Named("StudentMainDashboardActivity")
    ViewModelFactory viewModelFactory;

    ActivityStudentMainDashboardBinding binding;
    private Context mContext;
    private HomeViewModel viewModel;
    public static int LISTING_ACTIVE_FRAGMENT = 0;
    int backPress = 0;


    public static final int QUIZ_DASHBOARD_FRAGMENT = 1;
    public static final int PROFILE_FRAGMENT = 2;
    public static final int PASSPORT_FRAGMENT = 3;
    public static final int KYC_FRAGMENT = 4;
    public static final int KYC_VIEW_FRAGMENT = 5;
    public static final int SEND_MONEY_FRAGMENT = 6;
    public static final int CERTIFICATES_FRAGMENT = 7;
    public static final int PAYMENT_INFO_FRAGMENT = 8;
    public static final int PRIVACY_POLICY_FRAGMENT = 9;
    public static final int QUIZ_TEST_FRAGMENT = 10;
    public static final int PARTNERS_FRAGMENT = 15;
    public static final int NATIVE_QUIZ_FRAGMENT = 12;


    LoginDisclaimerDialog disclaimerDialog;
    public static CommonCallBackListner commonCallBackListner;

    AuroScholarDataModel auroScholarDataModel;
    ActionBarDrawerToggle mDrawerToggle;
    ArrayList<NavItemModel> mNavItems = new ArrayList<NavItemModel>();

    QuizViewModel quizViewModel;
    CustomOtpDialog customOtpDialog;
    public AlertDialog dialogQuit;


    public static int getListingActiveFragment() {
        return LISTING_ACTIVE_FRAGMENT;
    }

    public static void setListingActiveFragment(int listingActiveFragment) {
        LISTING_ACTIVE_FRAGMENT = listingActiveFragment;
    }


    DashboardResModel dashboardResModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        AuroApp.getAuroScholarModel().setActivity(this);
      //  AuroApp.context = this;
        setTheme(R.style.AppThemeNew);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        init();
        setListener();

    }


    @Override
    protected void init() {

        binding = DataBindingUtil.setContentView(this, getLayout());
        //AuroApp.getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        setDashboardApiCallingInPref(true);
        setLightStatusBar(this);
        if (getIntent() != null && getIntent().getParcelableExtra(AppConstant.AURO_DATA_MODEL) != null) {
            auroScholarDataModel = (AuroScholarDataModel) getIntent().getParcelableExtra(AppConstant.AURO_DATA_MODEL);
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        openFragment(new MainQuizHomeFragment());
        checkDisclaimer();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setUserLanguageCode("en");
        AuroAppPref.INSTANCE.setPref(prefModel);

    }

    public void setDashboardApiCallingInPref(boolean status) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setDashboardaApiNeedToCall(status);
        AuroAppPref.INSTANCE.setPref(prefModel);
    }

    @Override
    protected void setListener() {

        /*set listner here*/
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_student_main_dashboard;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // add LIGHT_STATUS_BAR to flag
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(this.getResources().getColor(R.color.blue_color)); // optional

        }
    }

    @Override
    public void onItemClick(int position) {


    }


    private void setText(String text) {
        popBackStack();
    }


    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(this, fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onBackPressed() {
        backStack();

    }


    private synchronized void backStack() {

        switch (LISTING_ACTIVE_FRAGMENT) {
            case QUIZ_DASHBOARD_FRAGMENT:
                finish();
                break;
            case QUIZ_TEST_FRAGMENT:
                alertDialogForQuitQuiz();
                break;
            case NATIVE_QUIZ_FRAGMENT:
                openDialogForQuit();
                break;
            case KYC_VIEW_FRAGMENT:
                AppLogger.v("newbranch","KYC_VIEW_FRAGMENT  button Step 3");
                openFragment(new MainQuizHomeFragment());
                break;

            default:
                AppLogger.v("newbranch","Student MainActivity  button Step 3");
                popBackStack();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        AppLogger.e("chhonker", "Activity requestCode=" + requestCode);
    }

    public void popBackStack() {
        backPress = 0;
        getSupportFragmentManager().popBackStack();
    }


    private void dismissApplication() {
        if (backPress == 0) {
            backPress++;
        } else {
            finish();
            //  finishAffinity();
        }
    }


    public void setHomeFragmentTab() {
        openFragment(new MyClassRoomGroupFragment());
    }


    @Override
    public void onClick(View view) {

    }


    public void printHashKey(Activity context) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {

                case SUCCESS:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    if (responseApi.apiTypeStatus == Status.DYNAMIC_LINK_API) {
                    } else if (responseApi.apiTypeStatus == SEND_OTP) {
                        SendOtpResModel sendOtp = (SendOtpResModel) responseApi.data;
                        if (!sendOtp.getError()) {
                            checkOtpDialog();
                        }
                    } else if (responseApi.apiTypeStatus == Status.VERIFY_OTP) {
                        VerifyOtpResModel verifyOtp = (VerifyOtpResModel) responseApi.data;
                        AppLogger.e(TAG, "Step 1");
                        if (!verifyOtp.getError()) {
                            // checkUserType();
                            if (customOtpDialog != null && customOtpDialog.isShowing()) {
                                customOtpDialog.dismiss();
                                customOtpDialog.hideProgress();
                                if (commonCallBackListner != null) {
                                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.OTP_VERIFY, ""));
                                }
                            }
                            AppLogger.e(TAG, "Step 2");
                        }else {
                            AppLogger.e(TAG, "Step 3");
                            if (customOtpDialog != null && customOtpDialog.isShowing()) {
                                customOtpDialog.hideProgress();
                                customOtpDialog.showSnackBar(verifyOtp.getMessage());
                            }
                        }
                    } else if (responseApi.apiTypeStatus == Status.FETCH_STUDENT_PREFERENCES_API) {
                        FetchStudentPrefResModel fetchStudentPrefResModel = (FetchStudentPrefResModel) responseApi.data;
                        if (TextUtil.checkListIsEmpty(fetchStudentPrefResModel.getPreference())) {
                            openSubjectPreferenceScreen();
                        } else {
                            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                            prefModel.setFetchStudentPrefResModel(fetchStudentPrefResModel);
                            AuroAppPref.INSTANCE.setPref(prefModel);
                        }


                    } else if (responseApi.apiTypeStatus == DASHBOARD_API) {
                        onApiSuccess(responseApi);
                        DashboardResModel dashboardResModel = (DashboardResModel) responseApi.data;
                        if (commonCallBackListner != null) {
                            commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, LISTNER_SUCCESS, dashboardResModel));
                        }
                    }
                    break;

                case FAIL:
                case NO_INTERNET:
                    AppLogger.e("observeServiceResponse--", responseApi.status.toString());
                    AppLogger.e("observeServiceResponse--", "FAIL  NO_INTERNET ");
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    AppLogger.e("Error", (String) responseApi.data);
                    break;


                default:
                    AppLogger.e("observeServiceResponse--", "default ");
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    AppLogger.e("Error", (String) responseApi.data);
                    break;
            }

        });
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        AppLogger.e("chhonker-", "-commonEventListner");
        switch (commonDataModel.getClickType()) {
            case GET_TEACHER_DASHBOARD_API:

                break;
        }
    }


    public void openProfileFragment() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        Bundle bundle = new Bundle();
        StudentProfileFragment studentProfile = new StudentProfileFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, prefModel.getDashboardResModel());
        bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.STUDENT_PROFILE);
        studentProfile.setArguments(bundle);
        openFragment(studentProfile);
    }
    //pradeep

    public void openKYCFragment(DashboardResModel dashboardResModel) {
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


    public void alertDialogForQuitQuiz() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getResources().getString(R.string.quiz_exit_txt));

        String yes = "<font color='#00A1DB'>" + this.getResources().getString(R.string.yes) + "</font>";
        String no = "<font color='#00A1DB'>" + this.getResources().getString(R.string.no) + "</font>";
        // Set the alert dialog yes button click listener
        builder.setPositiveButton(Html.fromHtml(yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getSupportFragmentManager().popBackStack();
                dialog.dismiss();
            }
        });
        // Set the alert dialog no button click listener
        builder.setNegativeButton(Html.fromHtml(no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }


    public void loadPartnerLogo(ImageView imageView) {
        if (AuroApp.getAuroScholarModel() != null) {
            String imgUrl = AuroApp.getAuroScholarModel().getPartnerLogo();
            if (!TextUtil.isEmpty(imgUrl)) {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(this).load(imgUrl)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_image_placeholder)
                                .dontAnimate()
                                .priority(Priority.IMMEDIATE))
                        .into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }


    private void checkDisclaimer() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (!prefModel.isPreLoginDisclaimer()) {
            disclaimerDialog = new LoginDisclaimerDialog(this);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(disclaimerDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            disclaimerDialog.getWindow().setAttributes(lp);
            Objects.requireNonNull(disclaimerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            disclaimerDialog.setCancelable(false);
            disclaimerDialog.show();
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


    public void sendOtpApiReqPass() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressbar.pgbar.setVisibility(View.GONE);
            }
        }, 10000);
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String phonenumber = "91" + prefModel.getUserMobile();
        SendOtpReqModel mreqmodel = new SendOtpReqModel();
        mreqmodel.setMobileNo(phonenumber);
        viewModel.checkInternetForApi(SEND_OTP,mreqmodel);


    }

    public void verifyOtpRxApi(String otptext) {
        ViewUtil.hideKeyboard(this);
        if (customOtpDialog != null && customOtpDialog.isShowing()) {
            customOtpDialog.showProgress();
            VerifyOtpReqModel mverifyOtpRequestModel = new VerifyOtpReqModel();
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            mverifyOtpRequestModel.setUserType(0);
            mverifyOtpRequestModel.setResgistrationSource("AuroScholr");
            String phonenumber = "91" + prefModel.getUserMobile();
            mverifyOtpRequestModel.setDeviceToken(prefModel.getDeviceToken());
            mverifyOtpRequestModel.setMobileNumber(phonenumber);
            mverifyOtpRequestModel.setOtpVerify(otptext);
            viewModel.checkInternetForApi(Status.VERIFY_OTP,mverifyOtpRequestModel);
        }
    }

    private void openSubjectPreferenceScreen() {
        if (disclaimerDialog != null && disclaimerDialog.isShowing()) {
            disclaimerDialog.dismiss();
        }
        Intent newIntent = new Intent(this, SubjectPreferencesActivity.class);
        startActivity(newIntent);
        finish();
    }

    public static void setListner(CommonCallBackListner listner) {
        commonCallBackListner = listner;
    }

    public void callFetchUserPreference() {
        AppLogger.e("DashbaordMain", "oncreate step 2");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int userclass = prefModel.getUserclass();
        if (userclass > 10) {
            FetchStudentPrefReqModel fetchStudentPrefReqModel = new FetchStudentPrefReqModel();
            fetchStudentPrefReqModel.setMobileNo(AuroAppPref.INSTANCE.getModelInstance().getUserMobile());
            viewModel.checkInternetForApi(FETCH_STUDENT_PREFERENCES_API, fetchStudentPrefReqModel);
            //  viewModel.fetchStudentPreference((FetchStudentPrefReqModel) fetchStudentPrefReqModel);
        }
    }

    public void callDashboardApi() {
        viewModel.checkInternetForApi(DASHBOARD_API, AuroApp.getAuroScholarModel());
    }

    public void openDialogForQuit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getResources().getString(R.string.want_to_quit_quiz));
        // Set the alert dialog yes button click listener
        String yes = this.getResources().getString(R.string.yes);
        String no = this.getResources().getString(R.string.no);

        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + yes + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogQuit, int which) {
                if (AppUtil.commonCallBackListner != null) {
                    AppUtil.commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.FINISH_DIALOG_CLICK, ""));
                }
            }
        });
        // Set the alert dialog no button click listener
        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>" + no + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogQuit, int which) {

                dialogQuit.dismiss();

            }
        });

        dialogQuit = builder.create();
        dialogQuit.show();
        // Display the alert dialog on interface
    }

    private void onApiSuccess(ResponseApi responseApi) {
        dashboardResModel = (DashboardResModel) responseApi.data;
        AppUtil.setDashboardResModelToPref(dashboardResModel);

    }



    public void dashboardModel(DashboardResModel model) {
        dashboardResModel = model;
        AppUtil.setDashboardResModelToPref(model);
    }


    private void callLanguageList() {
        viewModel.checkInternetForApi(Status.LANGUAGE_LIST, "");
    }

}
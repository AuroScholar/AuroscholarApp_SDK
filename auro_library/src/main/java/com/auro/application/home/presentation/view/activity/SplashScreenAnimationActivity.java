package com.auro.application.home.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.NotificationDataModel;
import com.auro.application.core.common.SdkCallBack;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.databinding.ActivitySplashScreenAnimationBinding;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.LanguageMasterReqModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.LanguageListResModel;
import com.auro.application.home.presentation.view.fragment.StudentProfileFragment;
import com.auro.application.home.presentation.view.fragment.SubjectPreferencesActivity;
import com.auro.application.home.presentation.viewmodel.SplashScreenViewModel;
import com.auro.application.teacher.presentation.view.activity.TeacherProfileActivity;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.CustomDialog;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.alert_dialog.CustomMemoryStatusDialog;
import com.auro.application.util.firebase.FirebaseDynamicLink;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import static com.auro.application.core.common.AppConstant.MEMORY_REQUIRED;
import static com.auro.application.util.MemoryStatus.getAvailableInternalMemorySize;
import static com.auro.application.util.MemoryStatus.getTotalExternalMemorySize;
import static com.auro.application.util.MemoryStatus.getTotalInternalMemorySize;
import static com.auro.application.util.MemoryStatus.humanReadableByteCountSI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenAnimationActivity extends BaseActivity {
    @Inject
    @Named("SplashScreenAnimationActivity")
    ViewModelFactory viewModelFactory;
    ActivitySplashScreenAnimationBinding binding;
    Map<Integer, View> animationlist;
    private SplashScreenViewModel viewModel;
    private Context mContext;
    NotificationDataModel notificationDataModel;
    LanguageListResModel languageListResModel;

    Details details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_animation);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("newToken", newToken);
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setDeviceToken(newToken);
            getPreferences(Context.MODE_PRIVATE).edit().putString("fb_device_token", newToken).apply();
        });

        init();
        setListener();

    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayout());
        //((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        DaggerWrapper.getComponent(this).doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashScreenViewModel.class);
        binding.setLifecycleOwner(this);
        mContext = SplashScreenAnimationActivity.this;
        // AppUtil.loadAppLogo(binding.auroScholarLogo);
        checkForVersionCode();
        startAnimation();
       initiateData();
        checkDeepLink();
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
        AuroAppPref.INSTANCE.getModelInstance().setAssignmentReqModel(null);
    }


    private void checkForVersionCode() {


        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();

        AppLogger.e("checkForVersionCode- saved-", "" + prefModel.getVersionCode());
        AppLogger.e("checkForVersionCode- saved-", "" + AppUtil.getVersionCode(this));
        if (prefModel.getVersionCode() == 0) {
            AuroAppPref.INSTANCE.clearAuroAppPref();
            PrefModel prefModel1 = AuroAppPref.INSTANCE.getModelInstance();
            prefModel1.setVersionCode(AppUtil.getVersionCode(this));
            AuroAppPref.INSTANCE.setPref(prefModel1);
            return;
        }

        if (prefModel.getVersionCode() != AppUtil.getVersionCode(this)) {
            AuroAppPref.INSTANCE.clearAuroAppPref();
            PrefModel prefModel1 = AuroAppPref.INSTANCE.getModelInstance();
            prefModel1.setVersionCode(AppUtil.getVersionCode(this));
            AuroAppPref.INSTANCE.setPref(prefModel1);
            return;
        }
    }

    @Override
    protected void setListener() {

    }


    public void startAnimation() {
        animationlist = new HashMap<Integer, View>();
        animationlist.put(1, binding.tvquiz);
        animationlist.put(2, binding.auroScholarLogo);
        animationlist.put(3, binding.tvDesinBy);
        animationlist.put(4, binding.ivChildren);
        animationlist.put(5, binding.ivBooks);
        animationlist.put(6, binding.ivStar);
        animationlist.put(7, binding.ivSecondCircle);
        animationlist.put(8, binding.ivFirstCircle);

        toptoDownAnimation(binding.headerTopParent.mainHeadLayout, animationlist);
    }

    public void toptoDownAnimation(View view, Map<Integer, View> listView) {
        view.setVisibility(View.VISIBLE);
        Animation secondanim = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        view.startAnimation(secondanim);
        secondanim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fadeInAnimation(8);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void fadeInAnimation(int item) {
        if (item != 0) {
            animationlist.get(item).setVisibility(View.VISIBLE);
            Animation secondanim = AnimationUtils.loadAnimation(this, R.anim.fadein);
            animationlist.get(item).startAnimation(secondanim);
            secondanim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (item < 0) {// base case
                        AppLogger.v("Recustion", "Item base case -->" + item);
                        return;
                    } else {
                        AppLogger.v("Recustion", "Item else 1 -->" + item);
                        fadeInAnimation(item - 1);
                        AppLogger.v("Recustion", "Item else 2 -->" + (item - 1));
                        if (item == 1) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // This method will be executed once the timer is over
                                    callLanguageList();
                                    //memoryCheck();
                                    // whichScreenOpen();
                                }
                            }, 1000);
                        }
                        return;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

    }


    private void callLanguageList() {

      //  Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        progressChanges(0, "Fetching data....");

        viewModel.checkInternet(Status.LANGUAGE_LIST, "");

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash_screen_animation;
    }

    void initiateData() {
        /*Set the notification model*/
        if (getIntent() != null) {
            notificationDataModel = getIntent().getParcelableExtra("message");
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setNotificationDataModel(notificationDataModel);
            AuroAppPref.INSTANCE.setPref(prefModel);
            if (notificationDataModel != null) {
                AppLogger.e("chhonker-", " recevied" + notificationDataModel.getNavigateto());
            }
        }

        FirebaseDynamicLink mfirebase = new FirebaseDynamicLink(this);
        //mfirebase.dynamiclinking();
        mfirebase.getFirebaseData();
      //  DaggerWrapper.getComponent(this).doInjection(this);
        ViewUtil.setLanguageonUi(this);

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setDashboardaApiNeedToCall(true);
        AuroAppPref.INSTANCE.setPref(prefModel);
    }

    private void setValuesInPref(int type) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setUserType(type);
        prefModel.setDashboardaApiNeedToCall(true);
        AuroAppPref.INSTANCE.setPref(prefModel);
    }

    private void getProfile()
    {
        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        int susertype = prefs.getInt("session_usertype", 0);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();

        int userType = prefModel.getUserType();
        String username = prefModel.getUserName();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_name",username);
        map_data.put("user_type", String.valueOf(userType));
        map_data.put("user_prefered_language_id",languageid);
        RemoteApi.Companion.invoke().getUserCheck(map_data)
                .enqueue(new Callback<CheckUserResModel>()
                {
                    @Override
                    public void onResponse(Call<CheckUserResModel> call, Response<CheckUserResModel> response)
                    {
                        if (response.isSuccessful())
                        {
                                if (prefModel.isLogin()) {
                                    if (userType == susertype) {
                                        if (prefModel.isLogin()) {
                                            if (prefModel.isTeacherProfileScreen()) {
                                                teacherProfileActivity();
                                            } else {
                                                openTeacherActivity();
                                            }
                                        } else {
                                            openLoginActivity();
                                        }
                                    }

                                    else if (userType == susertype) {
                                        AppLogger.e("whichScreenOpen step 2-", "" + userType);
                                        if (prefModel.isLogin()) {
                                            AppLogger.e("whichScreenOpen step 3-", "" + prefModel.isLogin());
                                            int studentClass = ConversionUtil.INSTANCE.convertStringToInteger(prefModel.getStudentData().getGrade());
                                            AppLogger.e("whichScreenOpen step 4-", "" + studentClass);
                                            AppLogger.e("whichScreenOpen step 5-", "" + prefModel.getCurrentScreenFlag());
                                            if (prefModel.getCheckUserResModel().getUserDetails().get(0).getPassword().isEmpty() || prefModel.getCheckUserResModel().getUserDetails().get(0).getPassword().equals("")){

                                                openSetPasswordActivity();
                                            }
                                           else if (prefModel.getCheckUserResModel().getUserDetails().get(1).getGrade().isEmpty() || prefModel.getCheckUserResModel().getUserDetails().get(1).getGrade().equals("") || prefModel.getCheckUserResModel().getUserDetails().get(1).getGrade().equals("0")){
                                                openSetGrde();

                                            }

                                            else if (prefModel.getCheckUserResModel().getUserDetails().get(1).getStudentName().isEmpty() || prefModel.getCheckUserResModel().getUserDetails().get(1).getStudentName().equals("")){

                                                openSetUserProfileActivity();
                                            }

                                           else if (studentClass > 0) {
                                                Log.d("manju", "" + studentClass);
                                                //  openSetUserProfileActivity();
                                                startDashboardActivity(prefModel.getCheckUserResModel().getUserDetails().get(0).getUserMobile());  //prefModel.getUserMobile()
                                            } else {
                                                startDashboardActivity(prefModel.getCheckUserResModel().getUserDetails().get(0).getUserMobile());  //prefModel.getUserMobile()

                                                // openChooseGradeActivity();
                                            }
                                        }


                                        else {
                                            openLoginActivity();
                                        }
                                    }
                                }
                                else {
                                    openAppLanguageActivity();
                                }





                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserResModel> call, Throwable t)
                    {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void whichScreenOpen() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int userType = prefModel.getUserType();


        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String statusparentprofile = prefs.getString("statusparentprofile", "");
        String statusfillstudentprofile = prefs.getString("statusfillstudentprofile", "");
        String statussetpasswordscreen = prefs.getString("statussetpasswordscreen", "");
        String statuschoosegradescreen = prefs.getString("statuschoosegradescreen", "");
        String statuschoosedashboardscreen = prefs.getString("statuschoosedashboardscreen","");
        String statusentermobilenumber = prefs.getString("statusentermobilenumber","");
        String statusopenprofilewithoutpin = prefs.getString("statusopenprofilewithoutpin","");

        int susertype = prefs.getInt("session_usertype", 0);
        String session_userid = prefs.getString("session_userid", "");
        String session_username = prefs.getString("session_username", "");
        AppLogger.e("whichScreenOpen step 1-", "" + userType + "--prefModel--" + prefModel.isLogin());
     if (prefModel.isLogin()) {
//            if (userType == AppConstant.UserType.TEACHER) {
//                if (prefModel.isLogin()) {
//                    if (prefModel.isTeacherProfileScreen()) {
//                        teacherProfileActivity();
//                    } else {
//                        openTeacherActivity();
//                    }
//                } else {
//                    openLoginActivity();
//                }
//            }
//
//            else if (userType == AppConstant.UserType.STUDENT) {
//                AppLogger.e("whichScreenOpen step 2-", "" + userType);
//                if (prefModel.isLogin()) {
//                    AppLogger.e("whichScreenOpen step 3-", "" + prefModel.isLogin());
//                    int studentClass = ConversionUtil.INSTANCE.convertStringToInteger(prefModel.getStudentData().getGrade());
//                    AppLogger.e("whichScreenOpen step 4-", "" + studentClass);
//                    AppLogger.e("whichScreenOpen step 5-", "" + prefModel.getCurrentScreenFlag());
//                    if (!TextUtil.isEmpty(prefModel.getCurrentScreenFlag()) && prefModel.getCurrentScreenFlag().equalsIgnoreCase(AppConstant.CurrentFlagStatus.SET_PASSWORD)) {
//                        openSetPasswordActivity();
//                    } else if (TextUtil.isEmpty(prefModel.getCurrentScreenFlag()) && prefModel.getCurrentScreenFlag().equalsIgnoreCase(AppConstant.CurrentFlagStatus.SET_PROFILE_SCREEN)) {
//                        openSetUserProfileActivity();
//                    } else if (studentClass > 0) {
//                        // openUserProfileActivity();
//                        Log.d("manju", "" + studentClass);
//                      //  openSetUserProfileActivity();
//                        startDashboardActivity(prefModel.getCheckUserResModel().getUserDetails().get(0).getUserMobile());  //prefModel.getUserMobile()
//                    } else {
//                        startDashboardActivity(prefModel.getCheckUserResModel().getUserDetails().get(0).getUserMobile());  //prefModel.getUserMobile()
//
//                        // openChooseGradeActivity();
//                    }
//                }
//                else {
//                    openLoginActivity();
//                }
//            }
//        }
//        else {
//            openAppLanguageActivity();
//        }


           if (susertype == 1) {
               if (prefModel.isLogin()) {
                   if (prefModel.isTeacherProfileScreen()) {
                       teacherProfileActivity();
                   } else {
                       openTeacherActivity();
                   }
               } else {
                   openLoginActivity();
               }
           } else if (susertype == 0) {
               AppLogger.e("whichScreenOpen step 2-", "" + userType);
//               if (prefModel.isLogin()) {
//                   AppLogger.e("whichScreenOpen step 3-", "" + prefModel.isLogin());
//                   String studentClass = prefModel.getStudentData().getGrade();
//                   AppLogger.e("whichScreenOpen step 4-", "" + studentClass);
//                   AppLogger.e("whichScreenOpen step 5-", "" + prefModel.getCurrentScreenFlag());
//                   if (prefModel.getCheckUserResModel().getUserDetails().get(0).getPassword().isEmpty() || prefModel.getCheckUserResModel().getUserDetails().get(0).getPassword().equals("")) {
//
//                       openSetPasswordActivity();
//                   } else if (prefModel.getCheckUserResModel().getUserDetails().get(1).getGrade().isEmpty() || prefModel.getCheckUserResModel().getUserDetails().get(1).getGrade().equals("") || prefModel.getCheckUserResModel().getUserDetails().get(1).getGrade().equals("0")) {
//                       openSetGrde();
//
//                   } else if (prefModel.getCheckUserResModel().getUserDetails().get(1).getStudentName().isEmpty() || prefModel.getCheckUserResModel().getUserDetails().get(1).getStudentName().equals("")) {
//
//                       openSetUserProfileActivity();
//                   } else if (studentClass =="0") {
//                       Log.d("manju", "" + studentClass);
//                       //  openSetUserProfileActivity();
//                       startDashboardActivity(prefModel.getCheckUserResModel().getUserDetails().get(0).getUserMobile());  //prefModel.getUserMobile()
//                   } else {
//                       startDashboardActivity(prefModel.getCheckUserResModel().getUserDetails().get(0).getUserMobile());  //prefModel.getUserMobile()
//
//                       // openChooseGradeActivity();
//                   }
//               }
               if (statussetpasswordscreen.equals("true")) {

                   openSetPasswordActivity();
               } else if (statuschoosegradescreen.equals("true")) {
                   openSetGrde();
               } else if (statusfillstudentprofile.equals("true")) {
                   openSetUserProfileActivity();
               } else if (statusparentprofile.equals("true")) {
                   openSetParentProfileActivity();
               } else if (statusopenprofilewithoutpin.equals("true")) {
                   Intent i = new Intent(this, CompleteStudentProfileWithoutPin.class);
                   startActivity(i);
                   finish();
               } else if (statuschoosedashboardscreen.equals("true")) {
                   if (!statusentermobilenumber.equals("") || !statusentermobilenumber.isEmpty())
                       startDashboardActivity(statusentermobilenumber);
               } else {
                   openAppLanguageActivity();
               }
           }
           else {
               openAppLanguageActivity();
           }

    }
        else {
           openAppLanguageActivity();
        }

    }

    private void openSetUserProfileActivity() {
        Intent newIntent = new Intent(this, StudentProfileActivity.class); // changed by Ankesh
        startActivity(newIntent);
        finish();
        Log.d("activity", "openSetUserProfileActivity: ");
    }

    private void openSetParentProfileActivity() {
        Intent newIntent = new Intent(this, ParentProfileActivity.class); // changed by Ankesh
        startActivity(newIntent);
        finish();
        Log.d("activity", "openSetUserProfileActivity: ");
    }


    void openSetPasswordActivity() {
        Intent i = new Intent(this, ResetPasswordActivity.class);
        startActivity(i);
        finish();
    }
    void openSetGrde() {
        Intent i = new Intent(this, ChooseGradeActivity.class);
        startActivity(i);
        finish();
    }

    private void startDashboardActivity(String phoneNumber) {
        finish();
        Intent i = new Intent(SplashScreenAnimationActivity.this, DashBoardMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }

    private void openAppLanguageActivity() {
        Intent newIntent = new Intent(SplashScreenAnimationActivity.this, AppLanguageActivity.class);
        startActivity(newIntent);
        finish();
    }

    private void openLoginActivity() {
        Intent newIntent = new Intent(SplashScreenAnimationActivity.this, LoginActivity.class);
        startActivity(newIntent);
        finish();
    }

    void openEnterNumberActivity() {
        Intent intent = new Intent(this, EnterNumberActivity.class);
        startActivity(intent);
        finish();
    }


    private void openUserProfileActivity() {
        Intent newIntent = new Intent(SplashScreenAnimationActivity.this, SubjectPreferencesActivity.class);
        startActivity(newIntent);
        finish();
    }

    private void openChooseGradeActivity() {
        Intent tescherIntent = new Intent(this, ChooseGradeActivity.class);
        startActivity(tescherIntent);
        finish();
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
                Intent intent = new Intent(SplashScreenAnimationActivity.this, SplashScreenAnimationActivity.class);
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
        AuroScholar.startTeacherSDK(auroScholarDataModel);

    }

    private void checkDeepLink() {
        if (getIntent() != null && getIntent().getData() != null) {
            Uri data = getIntent().getData();
            AppLogger.e("chhonker", data.toString());
            String pickuplatitude = data.getQueryParameter("test");
            //  auroscholar://auroscholar.com/action?test=value
            AppLogger.e("chhonker", pickuplatitude);
        }

    }

    public void memoryCheck() {
        long size = getAvailableInternalMemorySize();
        long externalStorage = getTotalExternalMemorySize();

        long totalsize = getTotalInternalMemorySize();
        String formatSize = humanReadableByteCountSI(size);
        String totalformatSize = humanReadableByteCountSI(totalsize);
        String totalexformatSize = humanReadableByteCountSI(externalStorage);


        //check is Memory Aavilable to run our app
        if (MEMORY_REQUIRED >= size) {
            openMemoryDialog(totalsize, size);
        } else {
           // getProfile();
            whichScreenOpen();
        }

        AppLogger.v("MEMORY", "Size of memory in mb or kb -----> " + size + "   " + totalsize + "-----byte  " + formatSize + "////byte ----->" + size + "   Total---" + totalformatSize + "  external" + totalexformatSize);
    }

    private void openMemoryDialog(long totalMemorySpace, long freeSize) {
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(this);
        customDialogModel.setTitle(this.getResources().getString(R.string.auro_low_memory_status));
        customDialogModel.setFreeSpace(freeSize);
        customDialogModel.setTotalMemory(totalMemorySpace);
        customDialogModel.setTwoButtonRequired(true);
        CustomMemoryStatusDialog customDialog = new CustomMemoryStatusDialog(this, customDialogModel);
        customDialog.setSecondBtnTxt("Ok");
        customDialog.setSecondCallcack(new CustomDialog.SecondCallcack() {
            @Override
            public void clickYesCallback() {
                customDialog.dismiss();
                finish();

              /*  if (!resModel.isError()) {
                    ((SendMoneyFragment) getParentFragment()).backButton();
                    customDialog.dismiss();
                } else {
                    customDialog.dismiss();
                }*/
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

    private void teacherProfileActivity() {
        Intent newIntent = new Intent(this, TeacherProfileActivity.class);
        startActivity(newIntent);
        Log.d("activity", "teacherProfileActivity: ");
        finish();
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {

                case SUCCESS:
                    if (responseApi.apiTypeStatus == Status.LANGUAGE_LIST) {
                        AppLogger.e("SUCCESS", responseApi.data.toString());
                        languageListResModel = (LanguageListResModel) responseApi.data;
                        progressChanges(1, "");
                        callLanguageMasterApi();

                    } else if (responseApi.apiTypeStatus == Status.DYNAMIC_LANGUAGE) {
                        LanguageMasterDynamic language = (LanguageMasterDynamic) responseApi.data;
                        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                        prefModel.setLanguageListResModel(languageListResModel);
                        AppLogger.v("Language_pradeep", "LANGUAGE_LIST-0");
                        AppUtil.loadAppLogo(binding.auroScholarLogo, this);
                        prefModel.setLanguageMasterDynamic(language);
                        AuroAppPref.INSTANCE.setPref(prefModel);
                        memoryCheck();
                        AppLogger.v("Language_pradeep", "DYNAMIC_LANGUAGE-0" + language.getDetails().getAccept());
                    }
                    break;

                case FAIL:
                case NO_INTERNET:
                    AppLogger.e("Error", (String) responseApi.data);
                    progressChanges(2, (String) responseApi.data);
                    break;


                default:
                    AppLogger.e("Error", (String) responseApi.data);
                    progressChanges(2, (String) responseApi.data);
                    break;
            }

        });
    }

    void callLanguageMasterApi() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
//        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
//        Details details = model.getDetails();
        progressChanges(0, "Fetching data....");
        LanguageMasterReqModel languageMasterReqModel = new LanguageMasterReqModel();
        if (prefModel.getUserLanguageId()!=null && !prefModel.getUserLanguageId().isEmpty()) {
            languageMasterReqModel.setLanguageId(prefModel.getUserLanguageId());
        } else {
            languageMasterReqModel.setLanguageId("1");

        }
        languageMasterReqModel.setUserTypeId("1");
        AppLogger.v("Language_pradeep", " DYNAMIC_LANGUAGE Step 1");


        viewModel.checkInternet(Status.DYNAMIC_LANGUAGE, languageMasterReqModel);

    }


    void progressChanges(int status, String msg) {
        AppLogger.e("progressChanges--", "" + status);
        switch (status) {
            case 0:
                binding.customProgressLayout.progressBar.setVisibility(View.VISIBLE);
                binding.customProgressLayout.textMsg.setVisibility(View.VISIBLE);
                binding.customProgressLayout.textMsg.setText(msg);
                binding.customProgressLayout.btRetry.setVisibility(View.GONE);
                binding.customProgressLayout.background.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
                binding.progressLayout.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.progressLayout.setVisibility(View.GONE);
                break;

            case 2:

                binding.customProgressLayout.progressBar.setVisibility(View.GONE);
                binding.customProgressLayout.textMsg.setVisibility(View.VISIBLE);
                binding.customProgressLayout.textMsg.setText(msg);
                binding.customProgressLayout.btRetry.setVisibility(View.VISIBLE);
                binding.customProgressLayout.background.setBackgroundColor(this.getResources().getColor(R.color.color_red));
                binding.customProgressLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callLanguageList();
                    }
                });
                binding.progressLayout.setVisibility(View.VISIBLE);
                break;
        }

    }



}
package com.auro.application.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;

import com.auro.application.BuildConfig;
import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.common.ValidationModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.QuizResModel;
import com.auro.application.home.data.model.SubjectResModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.teacher.data.model.response.MyClassRoomResModel;
import com.auro.application.util.network.ProgressRequestBody;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

import static android.content.Context.WIFI_SERVICE;

public class AppUtil {

    public static ProgressRequestBody.UploadCallbacks uploadCallbacksListner;
    public static CommonCallBackListner commonCallBackListner;
    public static MyClassRoomResModel myClassRoomResModel;
    public static DashboardResModel dashboardResModel;
    public static int dashboardQuizScore;

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static int getVersionCode(Context pContext) {
        int lStrVersion = 0;
        try {
            PackageInfo pInfo = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), 0);
            lStrVersion = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return lStrVersion;
    }


    public static String getLocale() {


        return Locale.getDefault().getLanguage().toUpperCase();
    }

    public static CommonDataModel getCommonClickModel(int source, Status clickType, Object object) {
        CommonDataModel commonDataModel = new CommonDataModel();
        commonDataModel.setSource(source);
        commonDataModel.setClickType(clickType);
        commonDataModel.setObject(object);
        return commonDataModel;
    }

    public static float dpToPx(@NonNull Context context, @Dimension(unit = Dimension.DP) int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }


    public static String errorMessageHandler(String defaultMsg, String responseData) {

        JSONObject errorJson = null;
        try {
            errorJson = new JSONObject(responseData);
            JSONObject description = (JSONObject) errorJson.getJSONArray("errorDetails").opt(0);
            // return description.optString(AppConstant.DESCRIPTION);
            return "";
        } catch (Exception e) {
            return defaultMsg;
        }
    }


    public static void openDialer(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    public static void openUrlInBrowser(String url, Context context) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static byte[] encodeToBase64(Bitmap image, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOS);
        byte[] byteArray = byteArrayOS.toByteArray();
        return byteArray;
    }

    public static long bytesIntoHumanReadable(long bytes) {
        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        long gigabyte = megabyte * 1024;
        long terabyte = gigabyte * 1024;

        return (bytes / megabyte);
    }


    public static void setDashboardResModelToPref(DashboardResModel dashboardResModel) {
        AppLogger.e("callDasboardApi-", "Step 8");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setDashboardResModel(dashboardResModel);
        prefModel.setStudentClasses(dashboardResModel.getClasses());
        prefModel.setDashboardaApiNeedToCall(false);
        AuroAppPref.INSTANCE.setPref(prefModel);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                prefModel.setDashboardResModel(dashboardResModel);
                prefModel.setStudentClasses(dashboardResModel.getClasses());
                prefModel.setDashboardaApiNeedToCall(false);
                AuroAppPref.INSTANCE.setPref(prefModel);
            }
        }, 1000);


        AppLogger.e("setDashboardResModelToPref--1", "step 1");

        AppLogger.e("setDashboardResModelToPref--2", AuroAppPref.INSTANCE.getModelInstance().getDashboardResModel().getEmail_id());
    }


    public static String getAppVersionName() {

        try {
            PackageInfo pInfo = AuroApp.getAppContext().getPackageManager().getPackageInfo(AuroApp.getAppContext().getPackageName(), 0);
            AppLogger.e("getAppVersionName pInfo.versionName--", pInfo.versionName);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            AppLogger.e("getAppVersionName--", e.getMessage());
        }
        return "";
    }

    public static boolean checkForUpdate(String newVersion) {
        String existingVersion = getAppVersionName();
        if (existingVersion.isEmpty() || newVersion.isEmpty()) {
            return false;
        }

        existingVersion = existingVersion.replaceAll("\\.", "");
        newVersion = newVersion.replaceAll("\\.", "");

        int existingVersionLength = existingVersion.length();
        int newVersionLength = newVersion.length();

        StringBuilder versionBuilder = new StringBuilder();
        if (newVersionLength > existingVersionLength) {
            versionBuilder.append(existingVersion);
            for (int i = existingVersionLength; i < newVersionLength; i++) {
                versionBuilder.append("0");
            }
            existingVersion = versionBuilder.toString();
        } else if (existingVersionLength > newVersionLength) {
            versionBuilder.append(newVersion);
            for (int i = newVersionLength; i < existingVersionLength; i++) {
                versionBuilder.append("0");
            }
            newVersion = versionBuilder.toString();
        }

        return Integer.parseInt(newVersion) > Integer.parseInt(existingVersion);
    }

    public static String getIpAdress() {
        WifiManager wm = (WifiManager) AuroApp.getAppContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

    public static void setEmptyToDynamicResponseModel() {
        /*Update Dynamic  to empty*/
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setDynamiclinkResModel(new DynamiclinkResModel());
        AuroAppPref.INSTANCE.setPref(prefModel);
    }

    public static ValidationModel updateListAccordingTotheDynamicData(List<SubjectResModel> subjectResModelList) {
        ValidationModel validationModel = new ValidationModel();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        DynamiclinkResModel dynamiclinkResModel = prefModel.getDynamiclinkResModel();
        if (dynamiclinkResModel != null && !TextUtil.isEmpty(dynamiclinkResModel.getOpenSubject())) {
            for (int i = 0; i < subjectResModelList.size(); i++) {
                SubjectResModel resModel = subjectResModelList.get(i);
                if (!TextUtil.isEmpty(resModel.getSubject()) && resModel.getSubject().equalsIgnoreCase(dynamiclinkResModel.getOpenSubject())) {
                    resModel.setQuizOpen(true);
                    validationModel.setStatus(true);
                    validationModel.setMessage("" + i);
                }
            }
        }
        if (dynamiclinkResModel != null && !dynamiclinkResModel.isQuizOpen()) {
            AppUtil.setEmptyToDynamicResponseModel();
        }

        return validationModel;
    }

    public static QuizResModel checkNextQuizAvaibleOrNot(List<SubjectResModel> subjectResModelList, int pos) {
        QuizResModel quizResModel = null;
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        SubjectResModel subjectResModel = subjectResModelList.get(pos);
        AppLogger.e("chhonker- checkNextQuizAvaibleOrNot subjectResModel", subjectResModel.getSubject());

        for (QuizResModel model : subjectResModel.getChapter()) {
            AppLogger.e("chhonker- checkNextQuizAvaibleOrNot", model.getSubjectName() + "-atempt-" + model.getAttempt() + "-score-" + model.getScorepoints());
            DynamiclinkResModel dynamiclinkResModel = prefModel.getDynamiclinkResModel();
            if (dynamiclinkResModel != null) {
                AppLogger.e("chhonker- checkNextQuizAvaibleOrNot--dynamiclinkResModel--", dynamiclinkResModel.getOpenSubject());
            }
            if (dynamiclinkResModel != null &&
                    !TextUtil.isEmpty(dynamiclinkResModel.getOpenSubject())
                    && !TextUtil.isEmpty(subjectResModel.getSubject())
                    && subjectResModel.getSubject().equalsIgnoreCase(dynamiclinkResModel.getOpenSubject())
            ) {
                if (model.getAttempt() < 3 && model.getScorepoints() < 9) {
                    quizResModel = model;
                    AppLogger.e("chhonker- checkNextQuizAvaibleOrNot", model.getName());
                    break;
                }
            }
        }
        if (quizResModel == null) {
            AppLogger.e("chhonker- ", "checkNextQuizAvaibleOrNot null null");
        }
        return quizResModel;
    }


    public static String getUniqueDeviceId() {
        String android_id = Settings.Secure.getString(AuroApp.getAppContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }


    public static String getExamLanguage(AssignmentReqModel assignmentReqModel) {
        AppLogger.e("getExamLanguage--","step 1");
      /* if (assignmentReqModel.getSubject().equalsIgnoreCase("English")) {
            return "E";
        } else if (assignmentReqModel.getSubject().equalsIgnoreCase("Hindi")) {
            return "E";
        } else {
            return assignmentReqModel.getExamlang();
        }*/
        AppLogger.e("getExamLanguage--",AuroAppPref.INSTANCE.getModelInstance().getUserLanguageShortCode());
        return AuroAppPref.INSTANCE.getModelInstance().getUserLanguageShortCode();
    }

    public static void loadAppLogo(ImageView imageView, Activity activity) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.v("AppIcon", "Step-1");
        if (prefModel != null && prefModel.getLanguageListResModel() != null && prefModel.getLanguageListResModel().getAuroLogo() != null && !prefModel.getLanguageListResModel().getAuroLogo().isEmpty()) {
            AppLogger.v("AppIcon", "Step-2" + prefModel.getLanguageListResModel().getAuroLogo());
            ImageUtil.loadAppLogo(imageView, prefModel.getLanguageListResModel().getAuroLogo(), activity);
        } else {
            AppLogger.v("AppIcon", "Step-3");
            imageView.setImageDrawable(imageView.getContext().getDrawable(R.drawable.auro_scholar_logo));
        }
    }


}

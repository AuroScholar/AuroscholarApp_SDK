package com.auro.application.util.firebaseAnalytics;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.util.AppLogger;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import com.google.firebase.analytics.FirebaseAnalytics;

import static org.shadow.apache.commons.lang3.StringUtils.isNumeric;

public class FirebaseAnalyticsEvent {
    FirebaseAnalytics mObjFirebaseAnalytics;


    public FirebaseAnalyticsEvent(AuroApp appContext) {
        mObjFirebaseAnalytics = FirebaseAnalytics.getInstance(appContext);
    }

    public void logEvent(String pStrEventName, Map<String,String> detailsList) {

        try {

            Bundle bundle = new Bundle();

            for (Map.Entry<String,String> eventDetails: detailsList.entrySet()){
                Log.e("EventDetails",eventDetails.getKey()+ " ====" +eventDetails.getValue());
                if(isNumeric( eventDetails.getValue())) {
                    int lIntVal = convertToNumber(eventDetails.getValue());
                    if(lIntVal!=-1){
                        bundle.putInt(eventDetails.getKey(),lIntVal);
                    }
                    else {
                        bundle.putString(eventDetails.getKey(), eventDetails.getValue());
                    }
                }
                else {
                    bundle.putString(eventDetails.getKey(), eventDetails.getValue());
                }
            }

            mObjFirebaseAnalytics.logEvent(pStrEventName, bundle);

            mObjFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

            mObjFirebaseAnalytics.setMinimumSessionDuration(20000);

            mObjFirebaseAnalytics.setSessionTimeoutDuration(500);

        }catch (Exception e){
            Log.e("EventDetailsException",e.getMessage());

        }
    }

    private int convertToNumber(String pValue){
        int lIntResult = -1;
        try {
            if(!pValue.equals("") && checkNumberFormat(pValue)){
                lIntResult = Integer.parseInt(pValue);
            }
        }
        catch (NumberFormatException e){

        }

        return lIntResult;
    }

    private boolean checkNumberFormat(String number) {
        String regex = "[0-9]+";
        return number.matches(regex);
    }

    public boolean isNumeric(String str) {
        boolean result = false;
        try {
            result = str.matches("-?\\d+(\\.\\d+)?");
        }
        catch (Exception e){
        }
        return  result;
    }

    public void trackSplashView() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.SPLASH_SCREEN, bundle);
    }

    public void trackSelectTeacherOrStudent(int  choose){
        if(choose == AppConstant.UserType.TEACHER) {
            Map<String, String> bundle = new HashMap<String, String>();
            logEvent(AppConstant.AnayliticsEvents.CHOOSE_TEACHER, bundle);
        }else if(choose == AppConstant.UserType.STUDENT){
            Map<String, String> bundle = new HashMap<String, String>();
            logEvent(AppConstant.AnayliticsEvents.CHOOSE_STUDENT, bundle);
        }
    }
    public void trackLoginScreen(int chooseType) {
        if(chooseType ==AppConstant.UserType.TEACHER){
            Map<String, String> bundle = new HashMap<String, String>();
            logEvent(AppConstant.AnayliticsEvents.OTP_ASK_TEACHER, bundle);
        }else{
            Map<String, String> bundle = new HashMap<String, String>();
            logEvent(AppConstant.AnayliticsEvents.OTP_ASK_STUDENT, bundle);
        }

    }
    public void trackOtpScreen(int chooseType) {
        if(chooseType ==AppConstant.UserType.TEACHER){
            Map<String, String> bundle = new HashMap<String, String>();
            logEvent(AppConstant.AnayliticsEvents.OTP_VERIFY_TEACHER, bundle);
        }else{
            Map<String, String> bundle = new HashMap<String, String>();
            logEvent(AppConstant.AnayliticsEvents.OTP_VERIFY_STUDENT, bundle);
        }

    }

    public void trackStudentDashBoard() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.STUDENT_DASHBOARD, bundle);
    }
    public void trackStudentLogOut() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.STUDENT_LOGOUT, bundle);
    }
    public void trackTeacherLogOut() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.TEACHER_LOGOUT, bundle);
    }

    public void trackTeacherDashBoard() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.TEACHER_DASHBOARD, bundle);
    }
    public void trackStartNewQuiz() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.STUDENT_START_NEW_QUIZ, bundle);
    }
    public void trackRetakeQuiz() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.STUDENT_RETAKE_QUIZ, bundle);
    }

    public void trackStudentKycScreen() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.STUDENT_KYC_SCREEN, bundle);
    }

    public void trackStudentLeaderBoardScreen() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.STUDENT_LEADER_BOARD, bundle);
    }

    public void trackStudentProfileScreen() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.STUDENT_PROFILE_SCREEN, bundle);
    }

    public void trackStudentPassportScreen() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.STUDENT_PASSPORT_SCREEN, bundle);
    }
    public void trackStudentPartnerScreen() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.STUDENT_PARTNER_SCREEN, bundle);
    }
    public void trackStudentProfileSubmit() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.STUDENT_PROFILE_SUBMIT, bundle);
    }
    public void trackTeacherProfileSubmit() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.TEACHER_PROFILE_SUBMIT, bundle);
    }
    public void trackStudentCompleteQuiz() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.STUDENT_COMPLETE_QUIZ, bundle);
    }
    public void trackStudentInviteFriend() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.STUDENT_INVITE_FRIEND, bundle);
    }
    public void trackTeacherInviteFriend() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.TEACHER_INVITE_FRIEND, bundle);
    }
    public void trackPartnerScreen() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.PARTNER_SCREEN, bundle);
    }

    public void trackMindlerScreen() {
        Map<String, String> bundle = new HashMap<String, String>();
        logEvent(AppConstant.AnayliticsEvents.MINDLER_START_SCREEN, bundle);
    }
}

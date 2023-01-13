package com.auro.application.core.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.activity.SplashScreenAnimationActivity;
import com.auro.application.home.presentation.view.activity.StudentMainDashboardActivity;
import com.auro.application.home.presentation.view.fragment.FriendsLeaderBoardListFragment;

import com.auro.application.home.presentation.view.fragment.MainQuizHomeFragment;
import com.auro.application.util.AppLogger;
import com.auro.application.util.TextUtil;


public class AuroScholar {

    public static Fragment openAuroDashboardFragment(AuroScholarDataModel auroScholarDataModel) {
        if (auroScholarDataModel == null || auroScholarDataModel.getActivity() == null) {
            AppLogger.e("Auro scholar sdk not initialise", "error");
            return null;
        }
        if (auroScholarDataModel != null) {
            String input = auroScholarDataModel.getMobileNumber() + "\n" + auroScholarDataModel.getScholrId() + "\n" + auroScholarDataModel.isEmailVerified() + "\n" +
                    auroScholarDataModel.getRegitrationSource() + "\n" + auroScholarDataModel.getReferralLink();
            AppLogger.e("Auro scholar input data", input);
        }
        if (TextUtil.isEmpty(auroScholarDataModel.getPartnerSource())) {
            auroScholarDataModel.setPartnerSource("");
        }
        if (TextUtil.isEmpty(auroScholarDataModel.getShareIdentity())) {
            auroScholarDataModel.setShareIdentity("");
        }
        if (TextUtil.isEmpty(auroScholarDataModel.getShareType())) {
            auroScholarDataModel.setShareType("");
        }

        if (TextUtil.isEmpty(auroScholarDataModel.getRegitrationSource())) {
            auroScholarDataModel.setRegitrationSource("");
        }
        AuroApp.setAuroModel(auroScholarDataModel);

        if (AuroApp.getAuroScholarModel() != null) {
            switch (AuroApp.getAuroScholarModel().getSdkFragmentType()) {
                case AppConstant.FragmentType.FRIENDS_LEADER_BOARD:
                    if (auroScholarDataModel == null || auroScholarDataModel.getActivity() == null) {
                        AppLogger.e("Auro scholar sdk not initialise 2", "error");
                        return null;
                    }
                    return new FriendsLeaderBoardListFragment();
                default:
                    return new MainQuizHomeFragment();
            }
        } else {
            return new MainQuizHomeFragment();
        }
    }

    /*For generic with PhoneNumber and class*/
    public static Fragment startAuroSDK(AuroScholarInputModel inputModel) {
        AuroScholarDataModel auroScholarDataModel = new AuroScholarDataModel();
        auroScholarDataModel.setMobileNumber(inputModel.getMobileNumber());
        auroScholarDataModel.setStudentClass(inputModel.getStudentClass());
        auroScholarDataModel.setRegitrationSource(inputModel.getRegitrationSource());
        auroScholarDataModel.setActivity(inputModel.getActivity());
        auroScholarDataModel.setFragmentContainerUiId(R.id.home_container);
        auroScholarDataModel.setReferralLink(inputModel.getReferralLink());
        auroScholarDataModel.setLanguage(inputModel.getLanguage());
        auroScholarDataModel.setUserPartnerid(inputModel.getUserPartnerId());
        auroScholarDataModel.setApplicationLang(inputModel.isApplicationLang());
        auroScholarDataModel.setPartnerLogo(inputModel.getPartnerLogoUrl());
        auroScholarDataModel.setGender(inputModel.getGender());
        auroScholarDataModel.setEmail(inputModel.getEmail());
        auroScholarDataModel.setPartnerName(inputModel.getPartnerName());
        if (!TextUtil.isEmpty(inputModel.getSchoolName())) {
            auroScholarDataModel.setSchoolName(inputModel.getSchoolName());
        } else {
            auroScholarDataModel.setSchoolName("");
        }

        if (!TextUtil.isEmpty(inputModel.getBoardType())) {
            auroScholarDataModel.setBoardType(inputModel.getBoardType());
        } else {
            auroScholarDataModel.setBoardType("");
        }


        if (!TextUtil.isEmpty(inputModel.getSchoolType())) {
            auroScholarDataModel.setSchoolType(inputModel.getSchoolType());
        } else {
            auroScholarDataModel.setSchoolType("");
        }


        if (!TextUtil.isEmpty(inputModel.getGender())) {
            auroScholarDataModel.setGender(inputModel.getGender());
        } else {
            auroScholarDataModel.setGender("");
        }

        if (!TextUtil.isEmpty(inputModel.getEmail())) {
            auroScholarDataModel.setEmail(inputModel.getEmail());
        } else {
            auroScholarDataModel.setEmail("");
        }


        if (TextUtil.isEmpty(inputModel.getPartnerSource())) {
            auroScholarDataModel.setPartnerSource("");
        } else {
            auroScholarDataModel.setPartnerSource(inputModel.getPartnerSource());
        }


        if (TextUtil.isEmpty(inputModel.getLanguage()) && inputModel.getLanguage().trim().length() == 0) {
            auroScholarDataModel.setLanguage("");
        } else {
            auroScholarDataModel.setLanguage(inputModel.getLanguage());
        }
        AuroApp.setAuroModel(auroScholarDataModel);
        if (auroScholarDataModel != null && auroScholarDataModel.getActivity() == null) {
            return null;
        }

        // QuizHomeFragment quizHomeFragment = new QuizHomeFragment();
        auroScholarDataModel.getActivity().startActivity(new Intent(auroScholarDataModel.getActivity(), DashBoardMainActivity.class));

        //  return quizHomeFragment;
        return null;
    }


    public static void startTeacherSDK(AuroScholarDataModel auroScholarDataModel) {
        if (auroScholarDataModel != null) {
            String input = auroScholarDataModel.getMobileNumber() + "\n" + auroScholarDataModel.getScholrId() + "\n" + auroScholarDataModel.isEmailVerified() + "\n" +
                    auroScholarDataModel.getRegitrationSource() + "\n" + auroScholarDataModel.getReferralLink();
            AppLogger.e("Auro scholar input data", input);
        }

        if (TextUtil.isEmpty(auroScholarDataModel.getPartnerSource())) {
            auroScholarDataModel.setPartnerSource("");
        }
        if (TextUtil.isEmpty(auroScholarDataModel.getShareIdentity())) {
            auroScholarDataModel.setShareIdentity("");
        }
        if (TextUtil.isEmpty(auroScholarDataModel.getShareType())) {
            auroScholarDataModel.setShareType("");
        }

        if (TextUtil.isEmpty(auroScholarDataModel.getRegitrationSource())) {
            auroScholarDataModel.setRegitrationSource("");
        }
        AuroApp.setAuroModel(auroScholarDataModel);
        auroScholarDataModel.getActivity().startActivity(new Intent(auroScholarDataModel.getActivity(), HomeActivity.class));
    }

    public static void setReferralLink(String referralLink) {
        if (AuroApp.getAuroScholarModel() != null) {
            AuroApp.getAuroScholarModel().setReferralLink(referralLink);
        }
    }


}

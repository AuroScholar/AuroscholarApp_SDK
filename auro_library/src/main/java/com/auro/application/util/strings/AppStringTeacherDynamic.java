package com.auro.application.util.strings;

import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.ActivityAppLanguageBinding;
import com.auro.application.databinding.FragmentClassRoomGroupBinding;
import com.auro.application.databinding.FragmentCreateGroupBinding;
import com.auro.application.databinding.FragmentInformationDashboardBinding;
import com.auro.application.databinding.FragmentKycInfoScreenBinding;
import com.auro.application.databinding.FragmentTeacherNewprofileBinding;
import com.auro.application.databinding.FragmentUpCommingBookBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.util.AppLogger;

public enum AppStringTeacherDynamic {

    INSTANCE;

    static String TAG = "AppStringDynamic";

    /*TeacherKycInfoFragment  Strings*/
    public static void setTeacherKycInfoFragmentStrings(FragmentKycInfoScreenBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPKycInformation.setText(details.getKyc_information());
                binding.RPreupload.setText(details.getReUploadNdocuments());
                binding.RpKycVerification.setText(details.getUploadVerification());
                binding.onceitsDone.setText(details.getOnceItSDone());
                binding.RPUploadDocument.setText(details.getCompleteKyc());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }




    /*TeacherUserProfileStrings*/
    public static void setTeacherUserProfileStrings(FragmentTeacherNewprofileBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.rpTeacherprofile.setText(details.getTeacherProfile());
                binding.tiFullName.setHint(details.getTeacherFullName());
                binding.tlPhoneNumber.setHint(details.getTeacherPhoneNumber());
                binding.tlEmail.setHint(details.getEmailId());
                binding.tlSchool.setHint(details.getSchoolName());
                binding.tlGender.setHint(details.getGender());
                binding.tlState.setHint(details.getState());
                binding.tlDistict.setHint(details.getDistrict());
                binding.button.setText(details.getSaveChanges());
            }
        } catch (Exception e) {
            AppLogger.e(TAG,     e.getMessage());
        }
    }


    /*UpCommingBook Strings*/
    public static void setUpComingBookFragmentStrings(FragmentUpCommingBookBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.bookedTab.setText(details.getChooseSlot());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*MyClassRoomGroupFragment*/
    public static void setMyClassRoomGroupFragmentStrings(FragmentClassRoomGroupBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPmyClassRoom.setText(details.getAuroMyClassroom());
                binding.rpStudentList.setText(details.getAuroStudentList());
                binding.RpRecent.setText(details.getRecent());
                binding.RpGroup.setText(details.getGroup());
                binding.rpChooseStudent.setText(details.getChooseStudent());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*MyInformation*/
    public static void setMyInformationStrings(FragmentInformationDashboardBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPinformation.setText(details.getInformation());
                binding.RPDetailInformation.setText(details.getAuroGrabThisOpportunity());
                binding.RpDahboardtitle.setText(details.getDashboard());
                binding.RpTotalStudent.setText(details.getTotalStudentsInClass());
                binding.Rpscholar.setText(details.getWinningQuiz());
                binding.testReport.setText(details.getTestReports());
                binding.RptotalMMarks.setText(details.getTotalTestTaken());
                binding.RptotalMMarksobtain.setText(details.getTestTakenByUniqueStudents());


            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*MyInformation*/
    public static void setCreateGroupStrings(FragmentCreateGroupBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPCreateGroup.setText(details.getCreateGroup());
                //binding.RPDetailInformation.setText("Grab This opportunity To Become Auro Teacher");
                binding.rpChooseStudent.setText(details.getAdded());
                binding.addedListMessage.setText(details.getNoStudent());
                binding.rpStudentList.setText(details.getStudentsList());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }



}

package com.auro.application.util.strings;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityAppLanguageBinding;
import com.auro.application.databinding.ActivityChooseGradeBinding;
import com.auro.application.databinding.ActivityEnterPinBinding;
import com.auro.application.databinding.ActivityForgotPinBinding;
import com.auro.application.databinding.ActivityLoginBinding;
import com.auro.application.databinding.ActivityOtpBinding;
import com.auro.application.databinding.ActivityParentProfileDemoBinding;
import com.auro.application.databinding.ActivityResetPasswordBinding;
import com.auro.application.databinding.ActivitySetPinBinding;
import com.auro.application.databinding.AmParentDialogBinding;
import com.auro.application.databinding.AskNameLayoutBinding;
import com.auro.application.databinding.BankFragmentLayoutBinding;
import com.auro.application.databinding.BottomSheetAddUserStepLayoutBinding;
import com.auro.application.databinding.DemographicFragmentLayoutBinding;
import com.auro.application.databinding.DialogCongratulations2Binding;
import com.auro.application.databinding.DialogLessScoreCongratulationsBinding;
import com.auro.application.databinding.FragmentCertificateBinding;
import com.auro.application.databinding.FragmentGradeChangeDialogBinding;
import com.auro.application.databinding.FragmentMainQuizHomeBinding;
import com.auro.application.databinding.FragmentParentProfileBinding;
import com.auro.application.databinding.FragmentStudentProfile2Binding;
import com.auro.application.databinding.FragmentStudentUpdateProfileBinding;
import com.auro.application.databinding.FragmentTransactionsBinding;
import com.auro.application.databinding.FragmentUploadDocumentBinding;
import com.auro.application.databinding.FragmentUserProfileBinding;
import com.auro.application.databinding.FragmentWalletInfoDetailBinding;
import com.auro.application.databinding.FriendsLeoboardAddLayoutBinding;
import com.auro.application.databinding.FriendsLeoboardLayoutBinding;
import com.auro.application.databinding.FriendsLeoboardListLayoutBinding;
import com.auro.application.databinding.KycFragmentLayoutBinding;
import com.auro.application.databinding.LanguageSelectionLayoutBinding;
import com.auro.application.databinding.PartnersLayoutBinding;
import com.auro.application.databinding.PaymentOtpDialogBinding;
import com.auro.application.databinding.PaytmFragmentLayoutBinding;
import com.auro.application.databinding.QuizTestNativeLayoutBinding;
import com.auro.application.databinding.SendMoneyFragmentLayoutBinding;
import com.auro.application.databinding.StudentKycInfoLayoutBinding;
import com.auro.application.databinding.SubjectPrefLayoutBinding;
import com.auro.application.databinding.UpiFragmentLayoutBinding;
import com.auro.application.databinding.UserNotRegisteredDialogLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.LanguageMasterReqModel;
import com.auro.application.util.AppLogger;

public enum AppStringDynamic {
    INSTANCE;

    static String TAG = "AppStringDynamic";

    /*Splash Strings*/
    public void setSplashScreen() {

    }

    /*userNot register Strings*/
    public static void setUserNotRegisterStings(UserNotRegisteredDialogLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.tvTitle.setText(details.getDi_welcome() != null ? details.getDi_welcome(): DaggerWrapper.getmContext().getString(R.string.welcome));
                binding.tvMessage.setText(details.getDi_seems()!= null ? details.getDi_seems(): DaggerWrapper.getmContext().getString(R.string.not_register_msg));
                binding.tvNote.setHint(details.getDi_note()!= null ? details.getDi_note(): DaggerWrapper.getmContext().getString(R.string.not_register_note_msg));
                binding.RPAccept.setText(details.getProceed_to_signup()!= null ? details.getProceed_to_signup(): DaggerWrapper.getmContext().getString(R.string.proceed_to_signup));
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }
    /*AppLanguage Strings*/
    public static void setLoginStings(ActivityLoginBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.titleFirst.setText(details.getAuroHey() != null ? details.getAuroHey(): DaggerWrapper.getmContext().getString(R.string.auro_hey));
                binding.headingTwo.setText(details.getLoginToYourAccount() != null ? details.getLoginToYourAccount(): DaggerWrapper.getmContext().getString(R.string.login_to_your_account));
                binding.etMobileNumber.setHint(details.getMobileNumberUsername() != null ? details.getMobileNumberUsername(): DaggerWrapper.getmContext().getString(R.string.mobile_number_username));
                binding.RPAccept.setText(details.getContinueExit() != null ? details.getContinueExit(): DaggerWrapper.getmContext().getString(R.string.accept_continue));
                binding.etPin.setHint(details.getEnter_the_pin());
                binding.etPassword.setHint(details.getPassword() != null ? details.getPassword(): DaggerWrapper.getmContext().getString(R.string.password));
                binding.forgotPassword.setText(details.getForgotPassword() != null ? details.getForgotPassword(): DaggerWrapper.getmContext().getString(R.string.forgot_password));
                binding.loginWithOtp.setText(details.getLoginWithOtp()!= null ? details.getLoginWithOtp(): DaggerWrapper.getmContext().getString(R.string.login_with_otp));
              // binding.termsCondition.setText(details.getTermsOfService());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*Congrats Dialog  Strings*/
    public static void setLessCongratulationsDialogStrings(DialogLessScoreCongratulationsBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPTextView4.setText(details.getImposible_until()!= null ?details.getImposible_until():DaggerWrapper.getmContext().getString(R.string.impossible));
                binding.RPTextView5.setText(details.getYour_score() != null ?details.getYour_score() : DaggerWrapper.getmContext().getString(R.string.you_have_scored));
                binding.txtYou.setText(details.getScholarship_happen()!= null ?details.getScholarship_happen():DaggerWrapper.getmContext().getString(R.string.scholarship_happen));
                binding.btnShare.setText(details.getShare_with()!= null ?details.getShare_with():DaggerWrapper.getmContext().getString(R.string.share_with_friends));
               binding.txtRetakeQuiz.setText(details.getRetakeQuiz());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }
    /*Congrats Dialog  Strings*/
    public static void setCongratulationsDialogStrings(DialogCongratulations2Binding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPTextView6.setText(details.getYour_congrat() != null ?details.getYour_congrat() :DaggerWrapper.getmContext().getString(R.string.congratulation_new));
                binding.RPTextView5.setText(details.getYour_score()!= null ?details.getYour_score() :DaggerWrapper.getmContext().getString(R.string.you_have_scored));
                binding.scoreFifty.setText(details.getGet_scholar()!= null ?details.getGet_scholar():DaggerWrapper.getmContext().getString(R.string.auro_scholar_wallet));
                binding.txtRetakeQuiz.setText(details.getRetakeQuiz());
                binding.txtStartQuiz.setText(details.getNext());
                binding.btnShare.setText(details.getShare_with()!= null ?details.getShare_with():DaggerWrapper.getmContext().getString(R.string.share_with_friends));
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }
    /*BottomSheetAddUser  Strings*/
    public static void setBottomSheetAddUserStrings(BottomSheetAddUserStepLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.tvStudentUserName.setText(details.getUsername() != null ? details.getUsername():AuroApp.getAppContext().getResources().getString(R.string.username));
                binding.tvStudentGrade.setText(details.getTo_add_an_account() != null ? details.getTo_add_an_account():AuroApp.getAppContext().getResources().getString(R.string.to_add_an_account));
               binding.tvStudentGrade.setText(details.getTo_add_an_account());
               binding.tvStep.setText(details.getStep());
               binding.tvStep2.setText(details.getStep());
               binding.tvStepDesc2.setText(details.getAddStudent());
            //   binding.tvStepDesc.setText(details.getSet_your_pin());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }
    /*LoginPage  Strings*/
    public static void setAppLanguageStrings(ActivityAppLanguageBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();

            AppLogger.v("MySetData","Step 1"+details.getWe_have_set_pin()+ "  -  "+details.getPrivacyPolicy());
            AppLogger.v("MySetData",details.getPrivacyPolicy()!= null  ? "Baral": "Pradeep");
            AppLogger.v("MySetData",details.getWe_have_set_pin() != null  ? "Baral": "Pradeep");
            if (model != null) {
                binding.RPTextView9.setText(details.getPrivacyPolicy());
                binding.subHeadingText.setText(details.getAuroMultiLanguage());
                binding.userSelectionSheet.rpLogin.setText(details.getAuroLoginSignUp());
                binding.userSelectionSheet.RPTextView10.setText(details.getAuroChooseAnyOneOption());
                binding.userSelectionSheet.teacherTitle.setText(details.getAuroTeacher());
                binding.userSelectionSheet.studentTextview.setText(details.getAuroParent());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*OTP Page  Strings*/
    public static void setOtpPagetrings(ActivityOtpBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.titleFirst.setText(details.getHeyEnterOtp());
                binding.rpTextview2.setText(details.getEnterOtpBelowNumber());
                binding.resendBtn.setText(details.getResend());
                binding.resendTimerTxt.setText(details.getYouCanResendIn());
                binding.optOverCallTxt.setText(details.getOtp_set_value());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*Reset Password  Page  Strings*/
    public static void setResetPasswordPagetrings(ActivityResetPasswordBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.titleFirst.setText(details.getAuroHey());
                binding.headText.setText(details.getSetPassword());
                binding.etPassword.setHint(details.getPassword());
                binding.etconfirmPassword.setHint(details.getConfirmPassword());
                binding.RPAccept.setText(details.getSetPassword());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*Entern Pin Page  Strings*/
    public static void setEnterPinPagetrings(ActivityEnterPinBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.titleFirst.setText(details.getEnterPin());
                binding.tvEnterPinLogin.setText(details.getEnterThePinForLogin());
                binding.setPinText.setText(details.getEnterPin());
                binding.btContinue.setText(details.getContinueExit());
                binding.forgotPassword.setText(details.getForgotPin());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }



    /*Set Pin Page  Strings*/
    public static void setForgetPinActivityStrings(ActivityForgotPinBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {

                binding.titleFirst.setText(details.getSet_pin() != null   ? details.getSet_pin() : AuroApp.getAppContext().getResources().getString(R.string.set_pin));
                binding.txtWeHave.setText(details.getWe_have_set_pin() != null  ? details.getHeyEnterSet() : AuroApp.getAppContext().getResources().getString(R.string.enter_set_below_number));
                binding.setPinText.setText(details.getSetNewPin() != null  ? details.getSetNewPin(): AuroApp.getAppContext().getResources().getString(R.string.set_new_pin));
                binding.confirmPinText.setText(details.getConfirmPin() != null   ? details.getConfirmPin(): AuroApp.getAppContext().getResources().getString(R.string.confirm_pin));
                binding.btDoneNew.setText(details.getContinueExit() != null   ? details.getContinueExit(): AuroApp.getAppContext().getResources().getString(R.string.accept_continue));
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }



    /*Set Pin Page  Strings*/
    public static void setPinPagetrings(ActivitySetPinBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.titleFirst.setText(details.getHeyEnterSet());
                binding.tvEnterSetBelowNumnber.setText(details.getEnterSetBelowNumber());
                binding.setPinText.setText(details.getEnterNewPin());
                binding.setUserName.setText(details.getSetUsername());
                binding.etUsername.setHint(details.getEnterUsername());
                binding.confirmPinText.setText(details.getConfirmPin());
                binding.btDoneNew.setText(details.getContinueExit());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*Set MainQuizHome Page  Strings*/
    public static void setMainQuizHometrings(FragmentMainQuizHomeBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPTextView10.setText(details.getScoreMsg());
                binding.RPTextView11.setText(details.getSubject());
                binding.privacyPolicy.setText(details.getPrivacyPolicy());
                binding.termsOfUse.setText(details.getTermsOfService());
                binding.quizSelectionSheet.RPhint.setText(details.getAuroYouHaveChance());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*Your Certificates  Page  Strings*/
    public static void setAskNameCustomDialogStrings(AskNameLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.tvTitle.setText(details.getConfirm_your_name()!= null?details.getConfirm_your_name(): AuroApp.getAppContext().getResources().getString(R.string.confirm_your_name));
                binding.textName.setHint(details.getEnter_your_name()!= null?details.getEnter_your_name(): AuroApp.getAppContext().getResources().getString(R.string.enter_your_name));
                binding.btDone.setText(details.getDone_dialog()!= null? details.getDone_dialog(): AuroApp.getAppContext().getResources().getString(R.string.done_dialog));
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*Your Certificates  Page  Strings*/
    public static void setCertificatesPageStrings(FragmentCertificateBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.getScholarshipText.setText(details.getYourCertificates());
                binding.scoreText.setText(details.getCertificateContent());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*Your Certificates  Page  Strings*/
    public static void setKycScreenStrings(StudentKycInfoLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.onceitsDone.setText(details.getOnceItSDone());
                binding.RPreupload.setText(details.getReUploadNdocuments());
                binding.RpYourKycVerify.setText(details.getYourProfileIsVerified());
                binding.errorLayout.btRetry.setText(details.getRetry());
             //   binding.errorLayout.textError.setText(details.getDefaultError());
                binding.RPKycInformation.setText(details.getKyc_information() != null ? details.getKyc_information() :AuroApp.getAppContext().getResources().getString(R.string.kyc_information) );
                binding.RpKycVerification.setText(details.getKycVerification() != null ? details.getKycVerification() :AuroApp.getAppContext().getResources().getString(R.string.kyc_verification_teacher));
                binding.onceitsDone.setText(details.getLet_you_know() != null ? details.getLet_you_know():AuroApp.getAppContext().getResources().getString(R.string.let_you_know));
                binding.RPUploadDocument.setText(details.getCompleteKyc()!= null ? details.getCompleteKyc():AuroApp.getAppContext().getResources().getString(R.string.complete_kyc));
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    public static void setFriendsLeaderBoardAddFragment(FriendsLeoboardListLayoutBinding binding){
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {


                  binding.txtreffereduser.setText(details.getReffered_user());
                binding.txtrefferaluser.setText(details.getRefferal_user());
                binding.friendsBoardText.setText(details.getFriend_leader_board());
                binding.RpFriendsKeBina.setText(details.getFriendsKeBinaKyaFun());
                binding.inviteNow.setText(details.getInviteThem());
                binding.inviteText.setText(details.getInvite());
                binding.headerTopParent.cambridgeHeading.setText(details.getQuestionBankPoweredByCambridge());



            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*student Profile  Page  Strings*/
    public static void setStudentProfilePageStrings(FragmentStudentProfile2Binding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPTextView9.setText(details.getAddStudent());
                binding.grade.setText(details.getGradeStudent());
                binding.editProfileName.setHint(details.getName());
                binding.tilteachertxt.setHint(details.getStudentPhoneNumber());
                binding.usernametxt.setHint(details.getLogin_id());//details.getUsername()
                binding.useridtxt.setHint(details.getUserId());
                binding.inputemailedittext.setHint(details.getEmailId());
                binding.tvGender.setText(details.getGender());
                binding.tlStudentGender.setHint(details.getStudentGender());
                binding.tlSchooltype.setHint(details.getDemo_student_school_type());
                binding.tlSchoolboard.setHint(details.getDemo_student_school_board());
                binding.tlSchoolmedium.setHint(details.getDemo_student__school_language());
                binding.tltution.setHint(details.getDemo_student_taking_private_tutions());
                binding.tltutiontype.setHint(details.getPrivateTypeList());
                binding.tvSchoolType.setText(details.getSchoolType());
                binding.tvBoard.setText(details.getBoard());
                binding.tvLanguageMedium.setText(details.getLanguageMedium());
                binding.tvPrivateTution.setText(details.getTakingPrivateTutions());
                binding.tvPrivateType.setText(details.getTakingType());
                binding.stateTitle.setText(details.getState());
                binding.cityTitle.setText(details.getCity());
                binding.yourSubjects.setText(details.getYourSubjects());
                binding.submitbutton.setText(details.getSave());
                binding.tlstate.setHint(details.getState());
                binding.tldistrict.setHint(details.getDistrict());
                binding.tlSchool.setHint(details.getSchoolName());
                binding.txtsearch.setHint(details.getSearch_school_here()!=null ? details.getSearch_school_here() : "Search school here..");
                binding.autoCompleteTextView1.setHint(details.getSearch_school());
                binding.addnewschool.setText(details.getAdd_school());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    public static void setStudentUpdateProfilePageStrings(FragmentStudentUpdateProfileBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.titleFirst.setText(details.getHeyEnterSet());

                binding.tvEnterSetBelowNumnber.setText(details.getWe_have_set_pin());
                binding.setUserName.setText(details.getSetUsername());
                binding.etUsername.setHint(details.getEnterUsername());
                binding.setPinText.setText(details.getSet_pin());
                binding.confirmPinText.setText(details.getConfirmPin());
                binding.btdonenew.setText(details.getNext());
                binding.RPTextView9.setText(details.getAddStudent());
                binding.grade.setText(details.getGradeStudent());
                binding.tilteachertxt.setHint(details.getStudentPhoneNumber());
                binding.usernametxt.setHint(details.getLogin_id());//details.getUsername()
                binding.useridtxt.setHint(details.getUserId());
                binding.inputemailedittext.setHint(details.getEmailId());
                binding.tvGender.setText(details.getGender());
                binding.tvSchoolType.setText(details.getDemo_student_school_type());
             //   binding.tvBoard.setText(details.getBoard());
                binding.autoCompleteTextView1.setHint(details.getSearch_school());
                binding.addnewschool.setText(details.getAdd_school());
                binding.tvLanguageMedium.setText(details.getLanguageMedium());
                binding.tvPrivateTution.setText(details.getTakingPrivateTutions());
                binding.tvPrivateType.setText(details.getTakingType());
                binding.stateTitle.setText(details.getState());
                binding.cityTitle.setText(details.getCity());
                binding.yourSubjects.setText(details.getYourSubjects());
                binding.submitbutton.setText(details.getSave());
                binding.tiaddusername.setHint(details.getEnter_your_name());
                binding.tlStudentGender.setHint(details.getPlease_select_gender());

                binding.tlSchoolboard.setHint(details.getPlease_select_board());
                binding.tlSchoolmedium.setHint(details.getLanguageMedium());
                binding.tltution.setHint(details.getStudent_taking_private_tutions());
                binding.tltutiontype.setHint(details.getPlease_select_tution_type());
                binding.tlstate.setHint(details.getState());
                binding.tldistrict.setHint(details.getCity());
                binding.tlschool.setHint(details.getSchoolName());
                binding.tlgrade.setHint(details.getSelectYourGrade());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }



    /*Set Student Passport Page  Strings*/
    public static void setPassportStrings(FragmentTransactionsBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPTextView9.setText(details.getStudentPassport());
                binding.RPTextView10.setText(details.getPassportSubTitle());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*Set KYC  Page  Strings*/
    public static void setKycStrings(KycFragmentLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.getScholarshipText.setText(details.getYourWallet());
                binding.warnMsg.setText(details.getUploadDocTextMsg());
                binding.btUploadAll.setText(details.getUpload());
                binding.btModifyAll.setText(details.getModify());
                binding.stepOne.textUploadDocument.setText(details.getUploadDocument());
                binding.stepOne.textUploadDocumentMsg.setText(details.getPleaseUploadTheDocument());
                binding.stepTwo.textDocumentVerification.setText(details.getUploadVerification());
                binding.stepTwo.textVerifyMsg.setText(details.getVerificationIsInProcess());
                binding.stepThree.textQuizVerification.setText(details.getQuizVerification());
                binding.stepThree.textQuizVerifyMsg.setText(details.getQuizIsNotApporved());
                binding.stepFour.textTransfer.setText(details.getTransferScholarship());
                binding.stepFour.textTransferMsg.setText(details.getYouWillSeeTransfer());
                binding.stepFour.btTransferMoney.setText(details.getTransferScholarship());
                binding.cambridgeHeading.cambridgeHeading.setText(details.getQuestionBankPoweredByCambridge());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }



    /*Set Friends Leaderboard Page  Strings*/
    public static void setFriendsLeaderboardStrings(FriendsLeoboardLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.friendsBoardText.setText(details.getFriendsLeadeboard());
                binding.inviteText.setText(details.getInvite());
                binding.friendsBoardText.setText(details.getFriend_leader_board());

                binding.headerTopParent.cambridgeHeading.setText(details.getQuestionBankPoweredByCambridge());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*Set Language change Dialog Page  Strings*/
    public static void setLanguageChangeStrings(LanguageSelectionLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.txtSelect.setText(details.getLanguage());
                binding.button.setText(details.getOk());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*Set Partner  Page  Strings*/
    public static void setPartnerPageStrings(PartnersLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPTextView9.setText(details.getLearnWithOurPartnerApp() != null ? details.getLearnWithOurPartnerApp() :AuroApp.getAppContext().getResources().getString(R.string.learn_with_our_partner_app));
                binding.RPTextView10.setText(details.getSubPartnerTitle() != null ? details.getSubPartnerTitle() : AuroApp.getAppContext().getResources().getString(R.string.sub_partner_title));

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }



    /*Set money Transfer  Page  Strings*/
    public static void setMoneyTransferPageStrings(SendMoneyFragmentLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPTextView9.setText(details.getScholarshipTransfer());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*Set Paytm money  Transfer  Page  Strings*/
    public static void setPaytmMoneyTransferPageStrings(PaytmFragmentLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.walletText.setText(details.getWallet());
                binding.editTextHead.setText(details.getTransferToPaytmAccount());
                binding.numberEdittext.setHint(details.getEnterThePaytmNumber());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*Set UPI money  Transfer  Page  Strings*/
    public static void setUpiMoneyTransferPageStrings(UpiFragmentLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.walletText.setText(details.getWallet());
                binding.editTextHead.setText(details.getTransferToUpiAccount());
                binding.numberEdittext.setHint(details.getEnterTheUpiAddress());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*CustomOtp screen*/

    public static void setParentStrings(AmParentDialogBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.tvParent.setText(details.getParentSection());
                binding.RPAccept.setText(details.getSimple_continue());


            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*CustomOtp screen*/

    public static void setCustomOtpStrings(PaymentOtpDialogBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.txtEnterOtp.setText(details.getEnter_otp_custom());
                binding.otpSend.setText(details.getSend_to_this_number());
                binding.RPVerify.setText(details.getResend());
                binding.resendTimerTxt.setText(details.getOtp_set_value());


            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*Demographic screen*/

    public static void setDemoGraphicStrings(DemographicFragmentLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.demographHead.setText(details.getDemo_graphics());
                binding.tvGender.setText(details.getDemo_student_gender());
                binding.tvSchoolType.setText(details.getDemo_student_school_type());
                binding.tvBoard.setText(details.getDemo_student_school_board());
                binding.tvLanguageMedium.setText(details.getDemo_student__school_language());
                binding.tvPrivateTution.setText(details.getDemo_student_taking_private_tutions());
                binding.submitbutton.setText(details.getSubmit());
                binding.tvPrivateType.setText(details.getDemo_student_type());
            }
        } catch (Exception e) {
            AppLogger.v("Pradeep_Demo", e.getMessage());
        }
    }

    /*Set Bank money  Transfer  Page  Strings*/
    public static void setBankMoneyTransferPageStrings(BankFragmentLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.walletText.setText(details.getWallet());
                binding.editTextHead.setText(details.getTransferToBankAccount());
                binding.accountNumber.setHint(details.getEnterTheAccountNumber());
                binding.confirmAccountNumber.setHint(details.getConfirmTheAccountNumber());
                binding.ifscCode.setHint(details.getIfscCode());
                binding.beneficiaryName.setHint(details.getBebeficiary_name());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*Set  Subject Pref  Page  Strings*/
    public static void setSubjectPrefPageStrings(SubjectPrefLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPTextView9.setText(details.getWelcome() != null ? details.getWelcome() :  AuroApp.getAppContext().getResources().getString(R.string.welcome));
                binding.RPTextView10.setText(details.getSubjectBrief() != null ? details.getSubjectBrief():  AuroApp.getAppContext().getResources().getString(R.string.subject_brief));
                binding.btContinue.setText(details.getContinueExit()!= null ? details.getContinueExit() : AuroApp.getAppContext().getResources().getString(R.string.continue_exit));

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*Set  Subject Pref  Page  Strings*/
    public static void setStudentUploadDocumentStrings(FragmentUploadDocumentBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
              //  binding.txttype.setText(details.gettype);
                binding.txtsupport.setText(details.getSupport_jpg());
                binding.chooseFile.setText(details.getChooseFile() != null ? details.getChooseFile() :  AuroApp.getAppContext().getResources().getString(R.string.choose_file));

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*Set  User Profile  Page  Strings*/
    public static void setUserProfilePageStrings(FragmentUserProfileBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.rpTeacherprofile.setText(details.getMyProfile());
                binding.tlPhoneNumber.setHint(details.getParentPhoneNumber());
                binding.tlGender.setHint(details.getStudentGender());
                binding.tlState.setHint(details.getState());
                binding.tlDistict.setHint(details.getCity());
                binding.tiemail.setHint(details.getEmailId());
                binding.tlSchool.setHint(details.getSchoolName());
                binding.tiFullName.setHint(details.getStudentFullName());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    public static void setParentProfilePageStrings(FragmentParentProfileBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.rpTeacherprofile.setText(details.getMyProfile());
                binding.tlPhoneNumber.setHint(details.getParentPhoneNumber());
                binding.tlGender.setHint("Select Gender");
                binding.tlState.setHint(details.getState());
                binding.tlDistict.setHint(details.getCity());
                binding.tiFullName.setHint("Full Name");

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }
    public static void setParentProfileDemoPageStrings(ActivityParentProfileDemoBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.rpTeacherprofile.setText(details.getMyProfile());
                binding.tiemail.setHint(details.getEmailAddress());
                binding.tlPhoneNumber.setHint(details.getParentPhoneNumber());
                binding.tlGender.setHint(details.getGender());
                binding.tlState.setHint(details.getState());
                binding.tlDistict.setHint(details.getCity());
                binding.tiFullName.setHint(details.getEnter_your_name());
              //  binding.etFullName.setHint(details.getEnter_your_name());
                binding.submitbutton.setText(details.getSave());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /* Set  Quiz Test Native  Page  Strings */
    public static void setQuizTestNativePageStrings(QuizTestNativeLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.finishNextBt.setText(details.getExitQuiz()!= null ? details.getExitQuiz() : AuroApp.getAppContext().getResources().getString(R.string.exit_quiz));//
                binding.saveNextBt.setHint(details.getSaveAmpNext() != null ? details.getSaveAmpNext() : AuroApp.getAppContext().getResources().getString(R.string.save_amp_next));
                binding.headerTopParent.cambridgeHeading.setText(details.getQuestionBankPoweredByCambridge() != null ? details.getQuestionBankPoweredByCambridge() : AuroApp.getAppContext().getResources().getString(R.string.question_bank_powered_by_cambridge));
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*GetWalletInfo  Strings*/
    public static void setWalletInfoScreenStrings(FragmentWalletInfoDetailBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPTextView9.setText(details.getWallet_info());
                binding.RPTextView10.setText(details.getOur_partners());
                binding.btTransferMoney.setText(details.getTransferScholarship());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*GradeChangeFragment  Strings*/
    public static void setGradeChangeScreenStrings(FragmentGradeChangeDialogBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.textEduction.setText(details.getSelectYourGrade());
                binding.btnYes.setText(details.getConfirmGrade());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*ChooseGradeActivity  Strings*/
    public static void setChooseGradeActivty(ActivityChooseGradeBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RpchooseClass.setText(details.getAuroChooseClass());
                binding.RPTextView13.setText(details.getAuroChooseClassDes());
                binding.RPTextView13.setText(details.getAuroChooseClassDes());
                binding.userSelectionSheet.buttonSelect.setText(details.getAuroSetMyAccount());
                binding.userSelectionSheet.tvPleaseBePatience.setText(details.getAuroPleaseBePatience());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }
}

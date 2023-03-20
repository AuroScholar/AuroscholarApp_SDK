package com.auro.application.core.application.di.component;

import android.app.Activity;

import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.di.module.KycModule;
import com.auro.application.core.application.di.module.PaymentModule;
import com.auro.application.core.application.di.module.QuizModule;
import com.auro.application.core.application.di.module.TeacherModule;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.home.presentation.view.activity.AppLanguageActivity;

import com.auro.application.home.presentation.view.activity.ChooseGradeActivity;

import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithPinActivity;
import com.auro.application.home.presentation.view.activity.HomeActivity;


import com.auro.application.home.presentation.view.activity.OtpScreenActivity;


import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.EnterNumberActivity;
import com.auro.application.home.presentation.view.activity.EnterPinActivity;
import com.auro.application.home.presentation.view.activity.ForgotPinActivity;
import com.auro.application.home.presentation.view.activity.LoginActivity;
import com.auro.application.home.presentation.view.activity.OtpActivity;
import com.auro.application.home.presentation.view.activity.RegisterActivity;
import com.auro.application.home.presentation.view.activity.ResetPasswordActivity;
import com.auro.application.home.presentation.view.activity.SDKActivity;
import com.auro.application.home.presentation.view.activity.SetPinActivity;
import com.auro.application.home.presentation.view.activity.SplashScreenAnimationActivity;
import com.auro.application.home.presentation.view.activity.StudentMainDashboardActivity;
import com.auro.application.home.presentation.view.activity.ValidateStudentActivity;
import com.auro.application.home.presentation.view.activity.TeacherDashboardActivity;

import com.auro.application.core.application.di.module.AppModule;
import com.auro.application.core.application.di.module.HomeModule;
import com.auro.application.core.application.di.module.UtilsModule;
import com.auro.application.home.presentation.view.fragment.CertificateFragment;
import com.auro.application.home.presentation.view.fragment.CongratulationsDialog;
import com.auro.application.home.presentation.view.fragment.ConsgratuationLessScoreDialog;
import com.auro.application.home.presentation.view.fragment.DemographicFragment;
import com.auro.application.home.presentation.view.fragment.FriendRequestListDialogFragment;

import com.auro.application.home.presentation.view.fragment.FriendsLeaderBoardAddFragment;
import com.auro.application.home.presentation.view.fragment.FriendsLeaderBoardFragment;
import com.auro.application.home.presentation.view.fragment.FriendsLeaderBoardListFragment;
import com.auro.application.home.presentation.view.fragment.GradeChangeFragment;
import com.auro.application.home.presentation.view.fragment.InviteFriendDialog;
import com.auro.application.home.presentation.view.fragment.KYCFragment;
import com.auro.application.home.presentation.view.fragment.KYCViewFragment;
import com.auro.application.home.presentation.view.fragment.PartnersWebviewFragment;
import com.auro.application.home.presentation.view.fragment.PrivacyPolicyFragment;


import com.auro.application.home.presentation.view.fragment.QuizTestFragment;
import com.auro.application.home.presentation.view.fragment.StudentKycInfoFragment;
import com.auro.application.home.presentation.view.fragment.StudentProfileFragment;
import com.auro.application.home.presentation.view.fragment.StudentUploadDocumentFragment;
import com.auro.application.home.presentation.view.fragment.TransactionsFragment;
import com.auro.application.home.presentation.view.fragment.WalletInfoDetailFragment;
import com.auro.application.home.presentation.view.fragment.MainQuizHomeFragment;
import com.auro.application.home.presentation.view.fragment.PartnersFragment;
import com.auro.application.home.presentation.view.activity.UserProfileActivity;
import com.auro.application.home.presentation.view.fragment.SubjectPreferencesActivity;
import com.auro.application.kyc.presentation.view.fragment.KycNewScreenFragment;
import com.auro.application.kyc.presentation.view.fragment.UploadDocumentFragment;
import com.auro.application.payment.presentation.view.fragment.BankFragment;
import com.auro.application.payment.presentation.view.fragment.PaytmFragment;
import com.auro.application.payment.presentation.view.fragment.SendMoneyFragment;
import com.auro.application.payment.presentation.view.fragment.UPIFragment;
import com.auro.application.quiz.presentation.view.fragment.QuizTestNativeFragment;
import com.auro.application.teacher.presentation.view.activity.TeacherProfileActivity;





import com.auro.application.teacher.presentation.view.fragment.TeacherKycInfoFragment;
import com.auro.application.teacher.presentation.view.fragment.TeacherProfileFragment;
import com.auro.application.teacher.presentation.view.fragment.TeacherSaveDetailFragment;
import com.auro.application.teacher.presentation.view.fragment.BookSlotFragment;
import com.auro.application.teacher.presentation.view.fragment.BookedSlotListFragment;
import com.auro.application.teacher.presentation.view.fragment.CreateGroupFragment;
import com.auro.application.teacher.presentation.view.fragment.InformationDashboardFragment;
import com.auro.application.teacher.presentation.view.fragment.MyClassRoomGroupFragment;
import com.auro.application.teacher.presentation.view.fragment.SlideFragment;
import com.auro.application.teacher.presentation.view.fragment.TeacherUserProfileFragment;
import com.auro.application.teacher.presentation.view.fragment.UpComingBookFragment;
import com.auro.application.util.alert_dialog.UpComingTimeSlotDialog;

import javax.inject.Singleton;

import dagger.Component;


@Component(modules =
        {
                AppModule.class,
                UtilsModule.class,
                HomeModule.class,
                TeacherModule.class,
                PaymentModule.class,
                QuizModule.class,
                KycModule.class,
        })

@Singleton
public interface AppComponent {

    void injectAppContext(AuroApp reciprociApp);

    void doInjection(StudentKycInfoFragment fragment);

    void doInjection(TeacherKycInfoFragment fragment);

    void doInjection(StudentUploadDocumentFragment fragment);

   // void doInjection(StudentMainDashboardActivity homeActivity);

    void doInjection(EnterPinActivity enterPinActivity);

    void doInjection(RegisterActivity activity);

    void doInjection(ForgotPinActivity activity);


    void doInjection(ValidateStudentActivity activity);

    void doInjection(EnterNumberActivity activity);


    void doInjection(OtpActivity otpActivity);



    void doInjection(BookSlotFragment fragment);

    void doInjection(PartnersFragment fragment);

    void doInjection(PartnersWebviewFragment fragment);

    void doInjection(SubjectPreferencesActivity fragment);


    void doInjection(CertificateFragment cardFragment);

    void doInjection(KYCViewFragment cardFragment);

    void doInjection(QuizTestFragment fragment);

    void doInjection(BankFragment fragment);

    void doInjection(PaytmFragment fragment);

   /* void doInjection(SplashScreenActivity splashScreenActivity);*/







    void doInjection(OtpScreenActivity loginScreenActivity);



    void doInjection(GradeChangeFragment dialog);

    void doInjection(PrivacyPolicyFragment privacyPolicyFragment);



    void doInjection(FriendsLeaderBoardFragment friendsLeaderBoardFragment);

    void doInjection(FriendsLeaderBoardAddFragment friendsLeaderBoardAddFragment);

    void doInjection(FriendsLeaderBoardListFragment friendsLeaderBoardListFragment);

    void doInjection(InviteFriendDialog inviteFriendDialog);

    void doInjection(CongratulationsDialog congratulationsDialog);

    void doInjection(TransactionsFragment transactionsFragment);





    void doInjection(TeacherSaveDetailFragment fragment);



    void doInjection(TeacherProfileFragment fragment);



    void doInjection(ConsgratuationLessScoreDialog dialog);



    void doInjection(FriendRequestListDialogFragment fragment);

    void doInjection(KYCFragment fragment);

    void doInjection(HomeActivity homeActivity);

    void doInjection(UPIFragment fragment);



    void doInjection(SendMoneyFragment fragment);

    void doInjection(DemographicFragment fragment);





    void doInjection(StudentProfileFragment fragment);

    void doInjection(WalletInfoDetailFragment fragment);


    /*New  Ui Login Activity*/
    void doInjection(LoginActivity loginActivity);

    void doInjection(ChooseGradeActivity activity);
    void doInjection(CompleteStudentProfileWithPinActivity activity);

    void doInjection(AppLanguageActivity activity);

    void doInjection(DashBoardMainActivity activity);

    void doInjection(MainQuizHomeFragment mainQuizHomeFragment);

    void doInjection(UserProfileActivity fragment);

    void doInjection(SplashScreenAnimationActivity activity);

    //teacher desgin

    void doInjection(InformationDashboardFragment activity);

    void doInjection(TeacherDashboardActivity activity);

    void doInjection(SlideFragment fragment);

    void doInjection(CreateGroupFragment fragment);

    void doInjection(MyClassRoomGroupFragment fragment);

    void doInjection(BookedSlotListFragment fragment);

    void doInjection(UpComingBookFragment fragment);

    void doInjection(ResetPasswordActivity activity);

    void doInjection(TeacherProfileActivity activity);

    void doInjection(QuizTestNativeFragment activity);



    void doInjection(KycNewScreenFragment fragment);


    void doInjection(TeacherUserProfileFragment fragment);

    void doInjection(SetPinActivity activity);

    void doInjection(UploadDocumentFragment fragment);
    void doInjection(UpComingTimeSlotDialog fragment);
    void doInjection(SDKActivity sdkActivity);
    void doInjection(Activity sdkActivity);


}

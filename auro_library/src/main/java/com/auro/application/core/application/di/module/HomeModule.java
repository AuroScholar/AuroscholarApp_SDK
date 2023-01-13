package com.auro.application.core.application.di.module;

import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.home.data.datasource.database.HomeDbDataSourceImp;
import com.auro.application.home.data.datasource.remote.HomeRemoteApi;
import com.auro.application.home.data.datasource.remote.HomeRemoteDataSourceImp;
import com.auro.application.home.data.repository.HomeRepo;
import com.auro.application.home.domain.usecase.HomeDbUseCase;
import com.auro.application.home.domain.usecase.HomeRemoteUseCase;
import com.auro.application.home.domain.usecase.HomeUseCase;


import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class HomeModule {

    //////////////////////////// DashBoard //////////////////////////////////

    @Provides
    @Singleton
    HomeRemoteApi provideDashBoardApi(Retrofit retrofit) {
        return retrofit.create(HomeRemoteApi.class);
    }

    @Provides
    @Singleton
    HomeRepo.DashboardDbData provideDashboardDbDataSourceImp() {
        return new HomeDbDataSourceImp();
    }


    @Provides
    @Singleton
    HomeRepo.DashboardRemoteData provideDashboardRemoteDataSourceImp(HomeRemoteApi homeRemoteApi) {
        return new HomeRemoteDataSourceImp(homeRemoteApi);
    }

    @Provides
    @Singleton
    HomeUseCase provideDashboardUseCase() {
        return new HomeUseCase();
    }



    @Provides
    @Singleton
    HomeDbUseCase provideHomeDbUseCase(HomeRepo.DashboardDbData homeDbDashboard) {
        return new HomeDbUseCase(homeDbDashboard);
    }


    @Provides
    @Singleton
    HomeRemoteUseCase provideHomeRemoteUseCase(HomeRepo.DashboardRemoteData dashboardRemoteData) {
        return new HomeRemoteUseCase(dashboardRemoteData);
    }



    @Provides
    @Singleton
    @Named("DineDetailFragment")
    ViewModelFactory provideDineDetailViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("DineHomeFragment")
    ViewModelFactory provideDineHomeFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    //NAccount fragment
    @Provides
    @Singleton
    @Named("RelaxHomeFragment")
    ViewModelFactory provideRelaxHomeFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }



/*    @Provides
    @Singleton
    @Named("SplashScreenActivity")
    ViewModelFactory provideSplashScreenActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }*/

/*
    @Provides
    @Singleton
    @Named("LoginScreenActivity")
    ViewModelFactory provideLoginScreenActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }
*/





    @Provides
    @Singleton
    @Named("OtpScreenActivity")
    ViewModelFactory provideOtpScreenActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }




    @Provides
    @Singleton
    @Named("GradeChangeDialog")
    ViewModelFactory provideGradeChangeDialogViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("KYCFragment")
    ViewModelFactory provideKYCViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("StudentKycInfoFragment")
    ViewModelFactory provideStudentKycInfoViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }
    @Provides
    @Singleton
    @Named("DemographicFragment")
    ViewModelFactory provideDemographicFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("QuizTestFragment")
    ViewModelFactory provideQuizTestFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("KYCViewFragment")
    ViewModelFactory provideKYCViewFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("FriendsLeaderBoardFragment")
    ViewModelFactory provideFriendsLeaderBoardFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("FriendsLeaderBoardListFragment")
    ViewModelFactory provideFriendsLeaderBoardListFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("FriendsLeaderBoardAddFragment")
    ViewModelFactory provideFriendsLeaderBoardAddFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("FriendRequestListDialogFragment")
    ViewModelFactory provideFriendRequestListDialogFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }



    @Provides
    @Singleton
    @Named("InviteFriendDialog")
    ViewModelFactory provideInviteFriendDialogViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("CongratulationsDialog")
    ViewModelFactory provideCongratulationsDialogViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("TransactionsFragment")
    ViewModelFactory provideTransactionsFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

/*
    @Provides
    @Singleton
    @Named("QuizHomeNewFragment")
    ViewModelFactory provideQuizViewNewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }
*/


    @Provides
    @Singleton
    @Named("CertificateFragment")
    ViewModelFactory provideCertificateFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }
    @Provides
    @Singleton
    @Named("StudentProfileFragment")
    ViewModelFactory provideStudentProfileViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }
    @Provides
    @Singleton
    @Named("CompleteStudentProfileWithPinActivity")
    ViewModelFactory provideCompleteStudentProfileWithPinViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }
    @Provides
    @Singleton
    @Named("WalletInfoDetailFragment")
    ViewModelFactory provideWalletAmountViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }
    @Provides
    @Singleton
    @Named("AppLanguageActivity")
    ViewModelFactory provideAppLanguageViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }
    @Provides
    @Singleton
    @Named("ChooseGradeActivity")
    ViewModelFactory provideWalletChooseGradeViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }


    /*New Ui Pages Start*/

    @Provides
    @Singleton
    @Named("LoginActivity")
    ViewModelFactory provideLoginActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("EnterNumberActivity")
    ViewModelFactory provideEnterNumberActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("OtpActivity")
    ViewModelFactory provideOtpActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("DashBoardMainActivity")
    ViewModelFactory provideDashBoardMainViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }
    @Provides
    @Singleton
    @Named("MainQuizHomeFragment")
    ViewModelFactory provideMainQuizViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("UserProfileActivity")
    ViewModelFactory provideUserProfileModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("PartnersFragment")
    ViewModelFactory providePartnersViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("PartnersWebviewFragment")
    ViewModelFactory PartnersWebviewFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("SplashScreenAnimationActivity")
    ViewModelFactory provideSplashScreenAnimationViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("ResetPasswordActivity")
    ViewModelFactory provideResetPasswordViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("RegisterActivity")
    ViewModelFactory provideRegisterActivityViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("ValidateStudentActivity")
    ViewModelFactory provideValidateStudentActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }



    @Provides
    @Singleton
    @Named("SubjectPreferencesActivity")
    ViewModelFactory provideSubjectPreferencesFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("TeacherDashboardActivity")
    ViewModelFactory provideTeacherDashboardViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }



    @Provides
    @Singleton
    @Named("SetPinActivity")
    ViewModelFactory provideSetPinViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("ForgotPinActivity")
    ViewModelFactory provideForgotPinActivityViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }



    @Provides
    @Singleton
    @Named("EnterPinActivity")
    ViewModelFactory provideEnterPinViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("StudentUploadDocumentFragment")
    ViewModelFactory provideStudentUploadDocumentFragmentFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

}

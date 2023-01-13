package com.auro.application.core.application.di.component;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.auro.application.home.domain.usecase.HomeDbUseCase;
import com.auro.application.home.domain.usecase.HomeRemoteUseCase;
import com.auro.application.home.domain.usecase.HomeUseCase;
import com.auro.application.home.presentation.viewmodel.AppLanguageViewModel;
import com.auro.application.home.presentation.viewmodel.AuroScholarDashBoardViewModel;

import com.auro.application.home.presentation.viewmodel.ChooseGradeViewModel;
import com.auro.application.home.presentation.viewmodel.CompleteStudentProfileWithPinViewModel;
import com.auro.application.home.presentation.viewmodel.CongratulationsDialogViewModel;
import com.auro.application.home.presentation.viewmodel.DemographicViewModel;
import com.auro.application.home.presentation.viewmodel.FriendsInviteViewModel;
import com.auro.application.home.presentation.viewmodel.FriendsLeaderShipViewModel;
import com.auro.application.home.presentation.viewmodel.GradeChangeViewModel;
import com.auro.application.home.presentation.viewmodel.HomeViewDashBoardModel;
import com.auro.application.home.presentation.viewmodel.HomeViewModel;
import com.auro.application.home.presentation.viewmodel.IntroScreenViewModel;
import com.auro.application.home.presentation.viewmodel.InviteFriendViewModel;
import com.auro.application.home.presentation.viewmodel.KYCViewModel;
import com.auro.application.home.presentation.viewmodel.LoginScreenViewModel;
import com.auro.application.home.presentation.viewmodel.OtpScreenViewModel;
import com.auro.application.home.presentation.viewmodel.QuizTestViewModel;
import com.auro.application.home.presentation.viewmodel.QuizViewModel;
import com.auro.application.home.presentation.viewmodel.QuizViewNewModel;
import com.auro.application.home.presentation.viewmodel.ScholarShipViewModel;
import com.auro.application.home.presentation.viewmodel.SetPinViewModel;
import com.auro.application.home.presentation.viewmodel.SplashScreenViewModel;
import com.auro.application.home.presentation.viewmodel.StudentProfileViewModel;
import com.auro.application.home.presentation.viewmodel.TransactionsViewModel;
import com.auro.application.home.presentation.viewmodel.WalletAmountViewModel;

import com.auro.application.kyc.domain.KycRemoteUseCase;
import com.auro.application.kyc.domain.KycUseCase;
import com.auro.application.payment.domain.PaymentRemoteUseCase;
import com.auro.application.payment.domain.PaymentUseCase;
import com.auro.application.payment.presentation.viewmodel.SendMoneyViewModel;
import com.auro.application.quiz.domain.QuizNativeRemoteUseCase;
import com.auro.application.quiz.domain.QuizNativeUseCase;
import com.auro.application.quiz.presentation.viewmodel.QuizTestNativeViewModel;
import com.auro.application.teacher.domain.TeacherDbUseCase;
import com.auro.application.teacher.domain.TeacherRemoteUseCase;
import com.auro.application.teacher.domain.TeacherUseCase;
import com.auro.application.teacher.presentation.viewmodel.CreateGroupViewModel;
import com.auro.application.teacher.presentation.viewmodel.MyClassroomViewModel;
import com.auro.application.teacher.presentation.viewmodel.SelectYourAppointmentDialogModel;
import com.auro.application.teacher.presentation.viewmodel.SelectYourMessageDialogModel;
import com.auro.application.teacher.presentation.viewmodel.SliderViewModel;
import com.auro.application.teacher.presentation.viewmodel.TeacherInfoViewModel;
import com.auro.application.teacher.presentation.viewmodel.TeacherKycViewModel;
import com.auro.application.teacher.presentation.viewmodel.TeacherProfileViewModel;
import com.auro.application.teacher.presentation.viewmodel.TeacherSaveDetailViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {


    /*Home Module*/
    private HomeUseCase homeUseCase;
    private HomeDbUseCase homeDbUseCase;
    private HomeRemoteUseCase homeRemoteUseCase;

    /*Payment Module*/
    private PaymentUseCase paymentUseCase;
    private PaymentRemoteUseCase paymentRemoteUseCase;

    /*Teacher Module*/
    TeacherUseCase teacherUseCase;
    TeacherRemoteUseCase teacherRemoteUseCase;
    TeacherDbUseCase teacherDbUseCase;

    /*Quiz  Module*/
    QuizNativeUseCase quizNativeUseCase;
    QuizNativeRemoteUseCase quizNativeRemoteUseCase;

    /*Kyc Module*/
    KycUseCase kycUseCase;
    KycRemoteUseCase kycRemoteUseCase;


    public ViewModelFactory(Object objectOne, Object objectTwo) {
        if (objectOne instanceof PaymentUseCase && objectTwo instanceof PaymentRemoteUseCase) {
            this.paymentUseCase = (PaymentUseCase) objectOne;
            this.paymentRemoteUseCase = (PaymentRemoteUseCase) objectTwo;
        }else if(objectOne instanceof QuizNativeUseCase && objectTwo instanceof QuizNativeRemoteUseCase){
            this.quizNativeUseCase = (QuizNativeUseCase)objectOne ;
            this.quizNativeRemoteUseCase = (QuizNativeRemoteUseCase) objectTwo;
        }else if(objectOne instanceof KycUseCase && objectTwo instanceof KycRemoteUseCase){
            this.kycUseCase = (KycUseCase)objectOne ;
            this.kycRemoteUseCase = (KycRemoteUseCase) objectTwo;
        }

    }


    public ViewModelFactory(Object objectOne, Object objectTwo, Object objectThree) {

        if (objectOne instanceof HomeUseCase && objectTwo instanceof HomeDbUseCase && objectThree instanceof HomeRemoteUseCase) {
            this.homeUseCase = (HomeUseCase) objectOne;
            this.homeDbUseCase = (HomeDbUseCase) objectTwo;
            this.homeRemoteUseCase = (HomeRemoteUseCase) objectThree;
        } else if (objectOne instanceof TeacherUseCase && objectTwo instanceof TeacherDbUseCase && objectThree instanceof TeacherRemoteUseCase) {
            this.teacherUseCase = (TeacherUseCase) objectOne;
            this.teacherDbUseCase = (TeacherDbUseCase) objectTwo;
            this.teacherRemoteUseCase = (TeacherRemoteUseCase) objectThree;
        }

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewDashBoardModel.class)) {

            return (T) new HomeViewDashBoardModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        }  else if (modelClass.isAssignableFrom(SplashScreenViewModel.class)) {

            return (T) new SplashScreenViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(IntroScreenViewModel.class)) {

            return (T) new IntroScreenViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(LoginScreenViewModel.class)) {

            return (T) new LoginScreenViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(AuroScholarDashBoardViewModel.class)) {

            return (T) new AuroScholarDashBoardViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(OtpScreenViewModel.class)) {

            return (T) new OtpScreenViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(GradeChangeViewModel.class)) {

            return (T) new GradeChangeViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(QuizViewModel.class)) {

            return (T) new QuizViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(KYCViewModel.class)) {

            return (T) new KYCViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(ScholarShipViewModel.class)) {

            return (T) new ScholarShipViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(DemographicViewModel.class)) {

            return (T) new DemographicViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(QuizTestViewModel.class)) {

            return (T) new QuizTestViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(SendMoneyViewModel.class)) {
            return (T) new SendMoneyViewModel(paymentUseCase, paymentRemoteUseCase);

        } else if (modelClass.isAssignableFrom(FriendsLeaderShipViewModel.class)) {

            return (T) new FriendsLeaderShipViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(FriendsInviteViewModel.class)) {

            return (T) new FriendsInviteViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(InviteFriendViewModel.class)) {

            return (T) new InviteFriendViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(CongratulationsDialogViewModel.class)) {

            return (T) new CongratulationsDialogViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);
        } else if (modelClass.isAssignableFrom(TransactionsViewModel.class)) {

            return (T) new TransactionsViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);
        } else if (modelClass.isAssignableFrom(MyClassroomViewModel.class)) {

            return (T) new MyClassroomViewModel(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);

        } else if (modelClass.isAssignableFrom(TeacherKycViewModel.class)) {

            return (T) new TeacherKycViewModel(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
        } else if (modelClass.isAssignableFrom(TeacherInfoViewModel.class)) {

            return (T) new TeacherInfoViewModel(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
        } else if (modelClass.isAssignableFrom(TeacherSaveDetailViewModel.class)) {

            return (T) new TeacherSaveDetailViewModel(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
        } else if (modelClass.isAssignableFrom(SelectYourMessageDialogModel.class)) {

            return (T) new SelectYourMessageDialogModel(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
        } else if (modelClass.isAssignableFrom(TeacherProfileViewModel.class)) {

            return (T) new TeacherProfileViewModel(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
        } else if (modelClass.isAssignableFrom(SelectYourAppointmentDialogModel.class)) {

            return (T) new SelectYourAppointmentDialogModel(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
        } else if (modelClass.isAssignableFrom(QuizViewNewModel.class)) {

            return (T) new QuizViewNewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {

            return (T) new HomeViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        }
        else if (modelClass.isAssignableFrom(StudentProfileViewModel.class)) {

            return (T) new StudentProfileViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        }
        else if (modelClass.isAssignableFrom(CompleteStudentProfileWithPinViewModel.class)) {

            return (T) new CompleteStudentProfileWithPinViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        }


        else if(modelClass.isAssignableFrom(WalletAmountViewModel.class)){

            return (T) new WalletAmountViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        }
        else if(modelClass.isAssignableFrom(AppLanguageViewModel.class)){

            return (T) new AppLanguageViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        }
        else if(modelClass.isAssignableFrom(ChooseGradeViewModel.class)){

            return (T) new ChooseGradeViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if(modelClass.isAssignableFrom(QuizViewNewModel.class)){

            return (T) new QuizViewNewModel(homeUseCase,homeDbUseCase,homeRemoteUseCase);

        }else if(modelClass.isAssignableFrom(QuizTestNativeViewModel.class)){

            return (T) new QuizTestNativeViewModel(quizNativeUseCase,quizNativeRemoteUseCase);

        }else if (modelClass.isAssignableFrom(KYCViewModel.class)) {

            return (T) new KYCViewModel(kycUseCase,kycRemoteUseCase);

        }

        else if(modelClass.isAssignableFrom(SliderViewModel.class)){

            return (T) new SliderViewModel(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);

        }
        else if(modelClass.isAssignableFrom(CreateGroupViewModel.class)){

            return (T) new CreateGroupViewModel(teacherUseCase,teacherDbUseCase,teacherRemoteUseCase);

        }

        else if(modelClass.isAssignableFrom(SetPinViewModel.class)){

            return (T) new SetPinViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        }



        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}



package com.auro.application.core.application.di.module;

import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.kyc.data.datasource.remote.KycRemoteApi;
import com.auro.application.kyc.data.datasource.remote.KycRemoteDataSourceImp;
import com.auro.application.kyc.data.repository.KycRepo;
import com.auro.application.kyc.domain.KycRemoteUseCase;
import com.auro.application.kyc.domain.KycUseCase;
import com.auro.application.payment.data.datasource.remote.PaymentRemoteApi;
import com.auro.application.payment.data.datasource.remote.PaymentRemoteDataSourceImp;
import com.auro.application.payment.data.repository.PaymentRepo;
import com.auro.application.payment.domain.PaymentRemoteUseCase;
import com.auro.application.payment.domain.PaymentUseCase;
import com.auro.application.quiz.domain.QuizNativeRemoteUseCase;
import com.auro.application.quiz.domain.QuizNativeUseCase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
@Module
public class KycModule {



    @Provides
    @Singleton
    KycRemoteApi provideKycRemoteApi(Retrofit retrofit) {
        return retrofit.create(KycRemoteApi.class);
    }


    @Provides
    @Singleton
    KycRepo.KycRemoteData provideKycRemoteDataSourceImp(KycRemoteApi kycRemoteApi) {
        return new KycRemoteDataSourceImp(kycRemoteApi);
    }


    @Provides
    @Singleton
    KycUseCase provideKycUseCase() {
        return new KycUseCase();
    }


    @Provides
    @Singleton
    KycRemoteUseCase providekycRemoteUseCase(KycRepo.KycRemoteData kycRemoteData) {
        return new KycRemoteUseCase(kycRemoteData);
    }

    @Provides
    @Singleton
    @Named("KycNewScreenFragment")
    ViewModelFactory provideKycNewFragmentModelFactory(KycUseCase kycUseCase, KycRemoteUseCase KycRemoteUseCase) {
        return new ViewModelFactory(kycUseCase ,KycRemoteUseCase);
    }


}

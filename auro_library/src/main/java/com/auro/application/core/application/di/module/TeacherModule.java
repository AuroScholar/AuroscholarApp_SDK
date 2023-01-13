package com.auro.application.core.application.di.module;


import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.teacher.data.datasource.database.TeacherDbDataSourceImp;
import com.auro.application.teacher.data.datasource.remote.TeacherRemoteApi;
import com.auro.application.teacher.data.datasource.remote.TeacherRemoteDataSourceImp;
import com.auro.application.teacher.data.repository.TeacherRepo;
import com.auro.application.teacher.domain.TeacherDbUseCase;
import com.auro.application.teacher.domain.TeacherRemoteUseCase;
import com.auro.application.teacher.domain.TeacherUseCase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class TeacherModule {

    //////////////////////////// DashBoard //////////////////////////////////

    @Provides
    @Singleton
    TeacherRemoteApi provideTeacherRemoteApi(Retrofit retrofit) {
        return retrofit.create(TeacherRemoteApi.class);
    }

    @Provides
    @Singleton
    TeacherRepo.TeacherDbData provideTeacherDbDataSourceImp() {
        return new TeacherDbDataSourceImp();
    }


    @Provides
    @Singleton
    TeacherRepo.TeacherRemoteData provideTeacherRemoteDataSourceImp(TeacherRemoteApi teacherRemoteApi) {
        return new TeacherRemoteDataSourceImp(teacherRemoteApi);
    }

    @Provides
    @Singleton
    TeacherUseCase provideTeacherUseCase() {
        return new TeacherUseCase();
    }


    @Provides
    @Singleton
    TeacherDbUseCase provideTeacherDbUseCase(TeacherRepo.TeacherDbData teacherDbData) {
        return new TeacherDbUseCase(teacherDbData);
    }


    @Provides
    @Singleton
    TeacherRemoteUseCase provideTeacherRemoteUseCase(TeacherRepo.TeacherRemoteData teacherRemoteData) {
        return new TeacherRemoteUseCase(teacherRemoteData);
    }






    @Provides
    @Singleton
    @Named("UploadDocumentFragment")
    ViewModelFactory provideUploadDocumentFragmentViewModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }



    @Provides
    @Singleton
    @Named("TeacherKycInfoFragment")
    ViewModelFactory provideTeacherKycInfoFragmentViewModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("TeacherSaveDetailFragment")
    ViewModelFactory provideTeacherSaveDetailViewModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("TeacherProfileFragment")
    ViewModelFactory provideTeacherProfileViewModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }



    @Provides
    @Singleton
    @Named("InformationDashboardFragment")
    ViewModelFactory provideInformationDashboardDialogModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("SlideFragment")
    ViewModelFactory provideSliderViewModelModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("BookSlotFragment")
    ViewModelFactory provideBookSlotFragmentViewModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("MyClassRoomGroupFragment")
    ViewModelFactory provideMyClassRoomGroupViewModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("CreateGroupFragment")
    ViewModelFactory provideCreateGroupViewModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("BookedSlotListFragment")
    ViewModelFactory provideBookedSlotListViewModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("UpCommingBookFragment")
    ViewModelFactory provideUpCommingBookFragmentViewModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("TeacherUserProfileFragment")
    ViewModelFactory provideTeacherModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("UpComingTimeSlotDialog")
    ViewModelFactory provideUpCommingTimeSlotViewModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("HomeActivity")
    ViewModelFactory provideHomeActivityModelFactory(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        return new ViewModelFactory(teacherUseCase, teacherDbUseCase, teacherRemoteUseCase);
    }

}

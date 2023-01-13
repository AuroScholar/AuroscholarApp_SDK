package com.auro.application.teacher.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.teacher.domain.TeacherDbUseCase;
import com.auro.application.teacher.domain.TeacherRemoteUseCase;
import com.auro.application.teacher.domain.TeacherUseCase;
import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.util.AppLogger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TeacherKycViewModel extends ViewModel {
    CompositeDisposable compositeDisposable;
    MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public com.auro.application.teacher.domain.TeacherUseCase teacherUseCase;
    com.auro.application.teacher.domain.TeacherRemoteUseCase teacherRemoteUseCase;
    com.auro.application.teacher.domain.TeacherDbUseCase teacherDbUseCase;

    public TeacherKycViewModel(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        this.teacherDbUseCase = teacherDbUseCase;
        this.teacherUseCase = teacherUseCase;
        this.teacherRemoteUseCase = teacherRemoteUseCase;
    }


    public void checkInternet(Status status, Object data)
    {

        Disposable disposable = teacherRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
               switch (status)
               {
                   case TEACHER_KYC_STATUS_API:
                       getTeacherKycStatusApi((FetchStudentPrefReqModel) data);
                       break;

                   case GET_TEACHER_DASHBOARD_API:
                       getTeacherDashboardApi((AuroScholarDataModel) data);
                       break;

                   case  TEACHER_KYC_API:
                       AppLogger.e("calluploadApi-","Step 5");

                       teacherKYCUploadApi((ArrayList<KYCDocumentDatamodel>) data);
                       break;
               }
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });

        getCompositeDisposable().add(disposable);
    }

    private void getTeacherKycStatusApi(FetchStudentPrefReqModel reqModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.getTeacherKycStatusApi(reqModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                serviceLiveData.setValue(ResponseApi.loading(Status.GET_TEACHER_DASHBOARD_API));
                            }
                        })
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        defaultError();
                                    }
                                }));

    }


    private void getTeacherDashboardApi(AuroScholarDataModel auroScholarDataModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.getTeacherDashboardApi(auroScholarDataModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                serviceLiveData.setValue(ResponseApi.loading(Status.GET_TEACHER_DASHBOARD_API));
                            }
                        })
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        defaultError();
                                    }
                                }));

    }


    private void teacherKYCUploadApi(List<KYCDocumentDatamodel> list) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.uploadTeacherKYC(list)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                               // serviceLiveData.setValue(ResponseApi.loading(Status.TEACHER_KYC_API));
                            }
                        })
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.e("calluploadApi-","Step 6");

                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.e("calluploadApi-","Step 7-"+throwable.getMessage());

                                        defaultError();
                                    }
                                }));

    }

    private void defaultError() {
        serviceLiveData.setValue(new ResponseApi(Status.FAIL, AuroApp.getAppContext().getResources().getString(R.string.default_error), null));
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    public LiveData<ResponseApi> serviceLiveData() {

        return serviceLiveData;

    }
}

package com.auro.application.teacher.presentation.viewmodel;

import com.auro.application.teacher.domain.TeacherDbUseCase;
import com.auro.application.teacher.domain.TeacherRemoteUseCase;
import com.auro.application.teacher.domain.TeacherUseCase;
import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SelectYourAppointmentDialogModel extends ViewModel {

    CompositeDisposable compositeDisposable;
    MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public com.auro.application.teacher.domain.TeacherUseCase teacherUseCase;
    com.auro.application.teacher.domain.TeacherRemoteUseCase teacherRemoteUseCase;
    com.auro.application.teacher.domain.TeacherDbUseCase teacherDbUseCase;

    public SelectYourAppointmentDialogModel(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        this.teacherDbUseCase = teacherDbUseCase;
        this.teacherUseCase = teacherUseCase;
        this.teacherRemoteUseCase = teacherRemoteUseCase;
    }


//    public void sendInviteNotificationApi(SendInviteNotificationReqModel reqModel ) {
//
//        Disposable disposable = teacherRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
//            if (hasInternet) {
//                sendInviteApi(reqModel);
//            } else {
//                // please check your internet
//                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getString(R.string.internet_check), Status.NO_INTERNET));
//            }
//
//        });
//
//        getCompositeDisposable().add(disposable);
//
//    }
//
//
//    private void sendInviteApi(SendInviteNotificationReqModel reqModel ) {
//        getCompositeDisposable()
//                .add(teacherRemoteUseCase.sendInviteApi(reqModel)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .doOnSubscribe(new Consumer<Disposable>() {
//                            @Override
//                            public void accept(Disposable __) throws Exception {
//                                /*Do code here*/
//                                serviceLiveData.setValue(ResponseApi.loading(null));
//                            }
//                        })
//                        .subscribe(new Consumer<ResponseApi>() {
//                                       @Override
//                                       public void accept(ResponseApi responseApi) throws Exception {
//                                           serviceLiveData.setValue(responseApi);
//                                       }
//                                   },
//
//                                new Consumer<Throwable>() {
//                                    @Override
//                                    public void accept(Throwable throwable) throws Exception {
//                                        defaultError();
//                                    }
//                                }));
//
//    }

    public void bookZohoAppointment(String from_time, String name, String email, String phone_number) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.bookZohoAppointments(from_time, name, email, phone_number)
                             .subscribeOn(Schedulers.io())
                             .observeOn(AndroidSchedulers.mainThread())
                             .doOnSubscribe(new Consumer<Disposable>() {
                                 @Override
                                 public void accept(Disposable __) throws Exception {
                                     /*Do code here*/
                                     serviceLiveData.setValue(ResponseApi.loading(Status.GET_ZOHO_APPOINTMENT));
                                 }
                             })
                             .subscribe(
                                     new Consumer<ResponseApi>() {
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
                                     }
                             ));

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

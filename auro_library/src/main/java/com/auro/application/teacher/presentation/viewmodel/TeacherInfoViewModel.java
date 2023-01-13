package com.auro.application.teacher.presentation.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.teacher.data.model.request.AddGroupReqModel;
import com.auro.application.teacher.data.model.request.AddStudentGroupReqModel;
import com.auro.application.teacher.data.model.request.AvailableSlotsReqModel;
import com.auro.application.teacher.data.model.request.BookSlotReqModel;
import com.auro.application.teacher.data.model.request.BookedSlotReqModel;
import com.auro.application.teacher.data.model.request.CancelWebinarSlot;
import com.auro.application.teacher.data.model.request.DeleteStudentGroupReqModel;
import com.auro.application.teacher.data.model.request.TeacherCreateGroupReqModel;
import com.auro.application.teacher.domain.TeacherDbUseCase;
import com.auro.application.teacher.domain.TeacherRemoteUseCase;
import com.auro.application.teacher.domain.TeacherUseCase;
import com.auro.application.util.AppLogger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TeacherInfoViewModel extends ViewModel {
    CompositeDisposable compositeDisposable;
    MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public TeacherUseCase teacherUseCase;
    TeacherRemoteUseCase teacherRemoteUseCase;
    TeacherDbUseCase teacherDbUseCase;

    public TeacherInfoViewModel(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        this.teacherDbUseCase = teacherDbUseCase;
        this.teacherUseCase = teacherUseCase;
        this.teacherRemoteUseCase = teacherRemoteUseCase;
    }


    public void checkInternet(Object object, Status status) {
        Disposable disposable = teacherRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                switch (status) {
                    case AVAILABLE_WEBINAR_SLOTS:

                        availableWebinarSlots((AvailableSlotsReqModel) object);
                        break;

                    case ADD_GROUP:
                        addGroup((AddGroupReqModel) object);
                        break;

                    case ADD_STUDENT_IN_GROUP:
                        addStudentInGroup((AddStudentGroupReqModel) object);
                        break;

                    case DELETE_STUDENT_FROM_GROUP:
                        deleteStudentInGroup((DeleteStudentGroupReqModel) object);
                        break;

                    case GET_TEACHER_PROGRESS_API:
                        getTeacherProgressApi((String) object);
                        break;

                    case GET_ZOHO_APPOINTMENT:
                        getZohoAppointment();
                        break;

                    case TEACHER_BOOKED_SLOT_API:
                        teacherBookedWebinarSlots((BookedSlotReqModel) object);
                        break;

                    case TEACHER_AVAILABLE_SLOTS_API:
                        AppLogger.e("available_slot_api","step 2");
                        teacherAvailableSlots((BookedSlotReqModel) object);
                        break;
                    case BOOK_SLOT_API:
                        bookSlotApi((BookSlotReqModel) object);
                        break;
                    case CANCEL_WEBINAR_SLOT:
                        cancelSlotApi((CancelWebinarSlot) object);
                        break;


                }
            } else {
                // please check your internet
                AppLogger.e("available_slot_api","step 2.1");

                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.GET_TEACHER_PROGRESS_API));
            }

        });

        getCompositeDisposable().add(disposable);

    }

    public void getZohoAppointment() {

        Disposable disposable = teacherRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                getCompositeDisposable()
                        .add(teacherRemoteUseCase.getZohoAppointments()
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
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.GET_ZOHO_APPOINTMENT));
            }

        });

        getCompositeDisposable().add(disposable);


    }


    private void getTeacherProgressApi(String mobileNumber) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.getTeacherProgressApi(mobileNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                serviceLiveData.setValue(ResponseApi.loading(Status.GET_TEACHER_PROGRESS_API));
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


    private void availableWebinarSlots(AvailableSlotsReqModel reqModel) {

        getCompositeDisposable()
                .add(teacherRemoteUseCase.availableWebinarSlots(reqModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/

                                serviceLiveData.setValue(ResponseApi.loading(Status.AVAILABLE_WEBINAR_SLOTS));
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
                                        AppLogger.e("available_slot_api","step 6");
                                        defaultError();
                                    }
                                }
                        ));

    }


    private void addGroup(AddGroupReqModel reqModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.addGroup(reqModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                serviceLiveData.setValue(ResponseApi.loading(Status.ADD_GROUP));
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

    private void addStudentInGroup(AddStudentGroupReqModel reqModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.addStudentInGroup(reqModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                serviceLiveData.setValue(ResponseApi.loading(Status.ADD_STUDENT_IN_GROUP));
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

    private void deleteStudentInGroup(DeleteStudentGroupReqModel reqModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.deleteStudentInGroup(reqModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                serviceLiveData.setValue(ResponseApi.loading(Status.DELETE_STUDENT_FROM_GROUP));
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



    private void teacherBookedWebinarSlots(BookedSlotReqModel reqModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.teacherBookedSlotApi(reqModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                               // serviceLiveData.setValue(ResponseApi.loading(Status.TEACHER_BOOKED_SLOT_API));
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

    private void teacherAvailableSlots(BookedSlotReqModel reqModel) {
        AppLogger.e("available_slot_api","step 3");
        getCompositeDisposable()
                .add(teacherRemoteUseCase.teacherAvailableSlotsApi(reqModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Consumer<ResponseApi>() {
                                    @Override
                                    public void accept(ResponseApi responseApi) throws Exception {
                                        AppLogger.e("available_slot_api","step 4");
                                        serviceLiveData.setValue(responseApi);
                                    }
                                },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.e("available_slot_api","step 5");
                                        defaultError();
                                    }
                                }
                        ));

    }

    private void bookSlotApi(BookSlotReqModel reqModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.bookSlotApi(reqModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
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

    private void cancelSlotApi(CancelWebinarSlot reqModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.cancelSlotApi(reqModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Consumer<ResponseApi>() {
                                    @Override
                                    public void accept(ResponseApi responseApi) throws Exception {
                                        AppLogger.e("cancelSlotApi","step 1");
                                        serviceLiveData.setValue(responseApi);
                                    }
                                },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.e("cancelSlotApi","step 2--"+throwable.getMessage());
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

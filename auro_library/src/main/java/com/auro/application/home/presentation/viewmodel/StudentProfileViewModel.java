package com.auro.application.home.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.MessgeNotifyStatus;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.SetPasswordReqModel;
import com.auro.application.home.data.model.StudentProfileModel;
import com.auro.application.home.data.model.VerifyOtpReqModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.domain.usecase.HomeDbUseCase;
import com.auro.application.home.domain.usecase.HomeRemoteUseCase;
import com.auro.application.home.domain.usecase.HomeUseCase;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.common.StateDataModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.TextUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class StudentProfileViewModel  extends ViewModel {

    CompositeDisposable compositeDisposable;
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();
    private final MutableLiveData<MessgeNotifyStatus> notifyLiveData = new MutableLiveData<>();

    public MutableLiveData<String> mobileNumber = new MutableLiveData<>();
    public HomeUseCase homeUseCase;
    private final HomeDbUseCase homeDbUseCase;
    private final HomeRemoteUseCase homeRemoteUseCase;

    public MutableLiveData<String> walletBalance = new MutableLiveData<>();

    public StudentProfileViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase = homeDbUseCase;
        this.homeRemoteUseCase = homeRemoteUseCase;
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

    public void checkInternetForApi(Status status, Object reqmodel) {

        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                switch (status)
                {
                    case FETCH_STUDENT_PREFERENCES_API:
                        fetchStudentPreference((FetchStudentPrefReqModel) reqmodel);
                        break;

                    case SUBJECT_PREFRENCE_LIST_API:
                        preferenceSubjectList();

                    case CHECKVALIDUSER:
                        checkUserVaild((CheckUserApiReqModel) reqmodel);
                        break;

                    case STUDENT_KYC_STATUS_API:
                        getStudentKycStatus();
                        break;

                }
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });
        getCompositeDisposable().add(disposable);

    }

    private void getStudentKycStatus( ) {
        getCompositeDisposable().add(homeRemoteUseCase.getStudentKycStatus().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

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

    private void preferenceSubjectList() {
        getCompositeDisposable()
                .add(homeRemoteUseCase.preferenceSubjectList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
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

    private void fetchStudentPreference(FetchStudentPrefReqModel reqModel) {
        getCompositeDisposable().add(homeRemoteUseCase.fetchStudentPreferenceApi(reqModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

    public void getStateListData() {
        getCompositeDisposable().add(homeDbUseCase.getStateList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<StateDataModel>>() {
                            @Override
                            public void accept(List<StateDataModel> value) throws Exception {
                                if (!TextUtil.checkListIsEmpty(value)) {
                                    serviceLiveData.setValue(new ResponseApi(Status.STATE_LIST_ARRAY, value, Status.STATE_LIST_ARRAY));
                                } else {
                                    insertStateData();
                                }
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


    public void insertStateData() {
        getCompositeDisposable().add(homeDbUseCase.insertStateList(homeUseCase.readStateData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Long[]>() {
                            @Override
                            public void accept(Long[] value) throws Exception {
                                AppLogger.e("chhonker-", "insert data-" + value.length);
                                getStateListData();
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


    public void getDistrictListData() {
        getCompositeDisposable().add(homeDbUseCase.getDistrictList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<DistrictDataModel>>() {
                            @Override
                            public void accept(List<com.auro.application.teacher.data.model.common.DistrictDataModel> value) throws Exception {
                                if (TextUtil.checkListIsEmpty(value)) {
                                    insertDistrictData();
                                } else {

                                }
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


    public void insertDistrictData() {
        getCompositeDisposable().add(homeDbUseCase.insertDistrictList(homeUseCase.readDistrictData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Long[]>() {
                            @Override
                            public void accept(Long[] value) throws Exception {
                                AppLogger.e("chhonker-", "insert data-" + value.length);

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


    public void getStateDistrictData(String stateCode) {
        getCompositeDisposable().add(homeDbUseCase.getDistrictList(stateCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<DistrictDataModel>>() {
                            @Override
                            public void accept(List<DistrictDataModel> value) throws Exception {
                                if (!TextUtil.checkListIsEmpty(value)) {
                                    serviceLiveData.setValue(new ResponseApi(Status.DISTRICT_LIST_DATA, value, Status.DISTRICT_LIST_DATA));
                                }

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


    private void checkUserVaild(CheckUserApiReqModel reqModel) {
        getCompositeDisposable().add(homeRemoteUseCase.checkUserApiVerify(reqModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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





    public void sendStudentProfileInternet(GetStudentUpdateProfile model) {
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                AppLogger.e("update profile api-", "step 2" );
                sendStudentProfile(model);
            } else {
                 serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET,AuroApp.getAppContext().getString(R.string.internet_check),Status.NO_INTERNET));
            }
        });
        getCompositeDisposable().add(disposable);
    }

    private void sendStudentProfile(GetStudentUpdateProfile model) {
        AppLogger.e("update profile api-", "step 3" );
        getCompositeDisposable().add(homeRemoteUseCase.uploadStudentProfile(model).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseApi>() {
                               @Override
                               public void accept(ResponseApi responseApi) throws Exception {
                                   AppLogger.e("update profile api-", "step 4" );
                                   serviceLiveData.setValue(responseApi);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                AppLogger.e("update profile api-", "step 5" );
                                defaultError();
                            }
                        }));
    }


    public void getUserProfileApi(String  userId) {

        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                getUserProfileData(userId);
            } else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET,AuroApp.getAppContext().getString(R.string.internet_check),Status.NO_INTERNET));
            }
        });
        getCompositeDisposable().add(disposable);
    }

    private void getUserProfileData(String  userId) {
        getCompositeDisposable().add(homeRemoteUseCase.getUserProfileData(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

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



    private void defaultError() {
        serviceLiveData.setValue(new ResponseApi(Status.FAIL, AuroApp.getAppContext().getResources().getString(R.string.default_error), null));
    }

}

package com.auro.application.home.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.MessgeNotifyStatus;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.OtpOverCallReqModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.SetPasswordReqModel;
import com.auro.application.home.data.model.VerifyOtpReqModel;
import com.auro.application.home.data.model.signupmodel.request.RegisterApiReqModel;
import com.auro.application.home.domain.usecase.HomeDbUseCase;
import com.auro.application.home.domain.usecase.HomeRemoteUseCase;
import com.auro.application.home.domain.usecase.HomeUseCase;
import com.auro.application.util.AppLogger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OtpScreenViewModel extends ViewModel {


    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomeUseCase homeUseCase;
    public HomeDbUseCase homeDbUseCase;
    public HomeRemoteUseCase homeRemoteUseCase;

    private final MutableLiveData<MessgeNotifyStatus> notifyLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public OtpScreenViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase = homeDbUseCase;
        this.homeRemoteUseCase = homeRemoteUseCase;
    }

    public MutableLiveData<MessgeNotifyStatus> getNotifyLiveData() {
        return notifyLiveData;
    }


    public void checkInternet(Object reqModel, Status status) {
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                switch (status) {
                    case VERIFY_OTP:
                        AppLogger.e("OtpActivity", "verifyOtpRxApi step 6");

                        verifyOtpRxApi((VerifyOtpReqModel) reqModel);
                        break;

                    case SEND_OTP:
                        sendOtpRxApi((SendOtpReqModel) reqModel);
                        break;

                    case CHECKVALIDUSER:
                        checkUserVaild((CheckUserApiReqModel) reqModel);
                        break;

                    case SET_PASSWORD:
                        setPasswordApi((SetPasswordReqModel) reqModel);
                        break;

                    case GET_TEACHER_DASHBOARD_API:
                        getTeacherDashboardApi((AuroScholarDataModel) reqModel);
                        break;

                    case REGISTER_API:
                        registerApi((RegisterApiReqModel) reqModel);
                        break;
                    case OTP_OVER_CALL:
                        optOverCall((OtpOverCallReqModel) reqModel);
                        break;

                }

            } else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getString(R.string.internet_check), Status.SEND_OTP));
            }

        });
        getCompositeDisposable().add(disposable);
    }

    private void verifyOtpRxApi(VerifyOtpReqModel reqModel) {
        AppLogger.e("verifyOtpRxApi", "verifyOtpRxApi step 1");
        try {
            getCompositeDisposable().add(homeRemoteUseCase.verifyOtpApi(reqModel).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResponseApi>() {
                                   @Override
                                   public void accept(ResponseApi responseApi) throws Exception {
                                       AppLogger.e("verifyOtpRxApi", "verifyOtpRxApi step 2");
                                       serviceLiveData.setValue(responseApi);
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    AppLogger.e("verifyOtpRxApi", "verifyOtpRxApi  throwable step 3" + throwable.getMessage());
                                    defaultError();
                                }
                            }));
        } catch (Exception e) {
            AppLogger.e("verifyOtpRxApi", "Exception step 2" + e.getMessage());

        }
    }

    private void optOverCall(OtpOverCallReqModel reqModel) {
        try {
            getCompositeDisposable().add(homeRemoteUseCase.otpOverCall(reqModel).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResponseApi>() {
                                   @Override
                                   public void accept(ResponseApi responseApi) throws Exception {
                                       AppLogger.e("registerApi", "registerApi step 2");
                                       serviceLiveData.setValue(responseApi);
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    AppLogger.e("registerApi", "registerApi  throwable step 3" + throwable.getMessage());
                                    defaultError();
                                }
                            }));
        } catch (Exception e) {
            AppLogger.e("registerApi", "Exception step 2" + e.getMessage());

        }
    }


    private void registerApi(RegisterApiReqModel reqModel) {
        AppLogger.e("registerApi", "registerApi step 1");
        try {
            getCompositeDisposable().add(homeRemoteUseCase.registerApi(reqModel).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResponseApi>() {
                                   @Override
                                   public void accept(ResponseApi responseApi) throws Exception {
                                       AppLogger.e("registerApi", "registerApi step 2");
                                       serviceLiveData.setValue(responseApi);
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    AppLogger.e("registerApi", "registerApi  throwable step 3" + throwable.getMessage());
                                    defaultError();
                                }
                            }));
        } catch (Exception e) {
            AppLogger.e("registerApi", "Exception step 2" + e.getMessage());

        }
    }

    private void sendOtpRxApi(SendOtpReqModel reqModel) {
        getCompositeDisposable().add(homeRemoteUseCase.sendOtpApi(reqModel).subscribeOn(Schedulers.io())
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

    private void setPasswordApi(SetPasswordReqModel reqModel) {
        getCompositeDisposable().add(homeRemoteUseCase.setPasswordApi(reqModel).subscribeOn(Schedulers.io())
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

    private void getTeacherDashboardApi(AuroScholarDataModel auroScholarDataModel) {
        getCompositeDisposable()
                .add(homeRemoteUseCase.getTeacherDashboardApi(auroScholarDataModel)
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


    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    private void defaultError() {
        serviceLiveData.setValue(new ResponseApi(Status.FAIL, AuroApp.getAppContext().getResources().getString(R.string.default_error), null));
    }

    public LiveData<ResponseApi> serviceLiveData() {

        return serviceLiveData;

    }
}

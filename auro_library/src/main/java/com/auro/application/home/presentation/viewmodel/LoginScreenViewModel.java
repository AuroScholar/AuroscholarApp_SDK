package com.auro.application.home.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.MessgeNotifyStatus;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.SetPasswordReqModel;
import com.auro.application.home.data.model.signupmodel.request.LoginReqModel;
import com.auro.application.home.domain.usecase.HomeDbUseCase;
import com.auro.application.home.domain.usecase.HomeRemoteUseCase;
import com.auro.application.home.domain.usecase.HomeUseCase;
import com.auro.application.util.AppLogger;


import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginScreenViewModel extends ViewModel {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomeUseCase homeUseCase;
    public HomeDbUseCase homeDbUseCase;
    public HomeRemoteUseCase homeRemoteUseCase;

    private final MutableLiveData<MessgeNotifyStatus> notifyLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();


    public LoginScreenViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase = homeDbUseCase;
        this.homeRemoteUseCase = homeRemoteUseCase;
    }

    public void checkInternet(Object object, Status status) {
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                switch (status) {
                    case SEND_OTP:

                        SendOtpReqModel reqModel = (SendOtpReqModel) object;
                        sendOtpRxApi(reqModel);
                        break;

                    case CHECKVALIDUSER:
                        CheckUserApiReqModel checkUserApiReqModel = (CheckUserApiReqModel) object;
                        checkUserVaild(checkUserApiReqModel);
                        break;

                    case SET_PASSWORD:
                        setPasswordApi((SetPasswordReqModel) object);
                        break;

                    case LOGIN_API:
                        loginApi((LoginReqModel) object);
                        break;

                    case SEND_OLD_OTP_API:
                        sendOtpRxApi((SendOtpReqModel) object);
                        break;

                }

            } else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getString(R.string.internet_check), Status.SEND_OTP));
            }

        });
        getCompositeDisposable().add(disposable);
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
                                   AppLogger.e("checkUserVaild","-- Step 2");
                                   serviceLiveData.setValue(responseApi);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                AppLogger.e("checkUserVaild","-- Step 1--" +throwable.getMessage());
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

    private void loginApi(LoginReqModel reqModel) {
        getCompositeDisposable().add(homeRemoteUseCase.loginApi(reqModel).subscribeOn(Schedulers.io())
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

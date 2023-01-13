package com.auro.application.home.presentation.viewmodel;

import static com.auro.application.core.common.Status.SEND_OTP;

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
import com.auro.application.home.data.model.signupmodel.request.RegisterApiReqModel;
import com.auro.application.home.data.model.signupmodel.request.SetUsernamePinReqModel;
import com.auro.application.home.data.model.signupmodel.response.SetUsernamePinResModel;
import com.auro.application.home.domain.usecase.HomeDbUseCase;
import com.auro.application.home.domain.usecase.HomeRemoteUseCase;
import com.auro.application.home.domain.usecase.HomeUseCase;
import com.auro.application.util.AppLogger;
import com.google.gson.JsonObject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SetPinViewModel extends ViewModel {


    CompositeDisposable compositeDisposable = new CompositeDisposable();

    HomeUseCase homeUseCase;
    HomeDbUseCase homeDbUseCase;
    HomeRemoteUseCase homeRemoteUseCase;

    private final MutableLiveData<MessgeNotifyStatus> notifyLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public SetPinViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase = homeDbUseCase;
        this.homeRemoteUseCase = homeRemoteUseCase;
    }

    public MutableLiveData<MessgeNotifyStatus> getNotifyLiveData() {
        return notifyLiveData;
    }

    public void checkInternet(Object object, Status status) {
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                switch (status) {
                    case SET_USERNAME_PIN_API:
                        setUserNamePin((SetUsernamePinReqModel) object);
                        break;

                    case LOGIN_PIN_API:
                        loginUsingPin((SetUsernamePinReqModel) object);
                        break;

                    case SET_USER_PIN:


                        setUserPin((SetUsernamePinReqModel) object);
                        break;

                    case SEND_OTP:
                        sendOtpRxApi((SendOtpReqModel) object);
                        break;

                    case REGISTER_API:
                        registerApi((RegisterApiReqModel) object);
                        break;

                    case CHECKVALIDUSER:
                        checkUserVaild((CheckUserApiReqModel) object);
                        break;

                }

            } else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getString(R.string.internet_check), Status.SEND_OTP));
            }

        });
        getCompositeDisposable().add(disposable);
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

    private void setUserNamePin(SetUsernamePinReqModel reqModel) {
        getCompositeDisposable().add(homeRemoteUseCase.setUsernamePassword(reqModel).subscribeOn(Schedulers.io())
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

    private void loginUsingPin(SetUsernamePinReqModel reqModel) {
        getCompositeDisposable().add(homeRemoteUseCase.loginUsingPinApi(reqModel).subscribeOn(Schedulers.io())
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

    private void setUserPin(SetUsernamePinReqModel reqModel) {
        AppLogger.e("callSetPinApi--", "step 2");
        getCompositeDisposable().add(homeRemoteUseCase.setUserPinApi(reqModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseApi>() {
                               @Override
                               public void accept(ResponseApi responseApi) throws Exception {
                                   try {
                                       SetUsernamePinResModel resModel = (SetUsernamePinResModel) responseApi.data;
                                       AppLogger.e("callSetPinApi--", "step 2.1-" + resModel.getMessage());

                                   } catch (Exception e) {
                                       AppLogger.e("callSetPinApi--", "step 2.2-" + e.getMessage());

                                   }

                                   serviceLiveData.setValue(responseApi);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                AppLogger.e("callSetPinApi--", "step 2.3-" + throwable.getMessage());

                                defaultError();
                            }
                        }));
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

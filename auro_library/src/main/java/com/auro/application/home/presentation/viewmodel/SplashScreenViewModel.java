package com.auro.application.home.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.MessgeNotifyStatus;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.home.data.model.LanguageMasterReqModel;
import com.auro.application.home.data.model.StateDataModelNew;
import com.auro.application.home.domain.usecase.HomeDbUseCase;
import com.auro.application.home.domain.usecase.HomeRemoteUseCase;
import com.auro.application.home.domain.usecase.HomeUseCase;
import com.auro.application.util.AppLogger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.auro.application.core.common.Status.STATE;
import static com.auro.application.core.common.Status.VERSIONAPI;

import android.widget.Toast;

public class SplashScreenViewModel extends ViewModel {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    HomeUseCase homeUseCase;
    HomeDbUseCase homeDbUseCase;
    HomeRemoteUseCase homeRemoteUseCase;

    private final MutableLiveData<MessgeNotifyStatus> notifyLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public SplashScreenViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase = homeDbUseCase;
        this.homeRemoteUseCase = homeRemoteUseCase;
    }

    public MutableLiveData<MessgeNotifyStatus> getNotifyLiveData() {
        return notifyLiveData;
    }


    public void checkInternet(Status status, Object reqmodel) {
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                switch (status) {
                    case VERSIONAPI:

                        getVersionApiCall();
                        break;

                    case LANGUAGE_LIST:
                        languageListApiCall();
                        break;

                    case DYNAMIC_LANGUAGE:
                        AppLogger.v("Language","Dynamic language Step 2"+(LanguageMasterReqModel)reqmodel);
                        languageDynamicCall((LanguageMasterReqModel)reqmodel);

                        break;
                    case STATE:
                        AppLogger.v("state","state data"+(StateDataModelNew)reqmodel);

                        getStateCall();
                        break;
                }

            } else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getString(R.string.internet_check), Status.SEND_OTP));
            }

        });
        getCompositeDisposable().add(disposable);
    }


    private void getVersionApiCall() {
        getCompositeDisposable()
                .add(homeRemoteUseCase.getVersionApiCheck()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                serviceLiveData.setValue(ResponseApi.loading(VERSIONAPI));
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

    private void getStateCall() {
        getCompositeDisposable()
                .add(homeRemoteUseCase.getState()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                serviceLiveData.setValue(ResponseApi.loading(STATE));
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



    private void languageListApiCall() {
        getCompositeDisposable()
                .add(homeRemoteUseCase.getLanguageList()
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

    private void languageDynamicCall(LanguageMasterReqModel language) {
        AppLogger.v("Language_pradeep","Dynamic language Step 3");
        getCompositeDisposable()
                .add(homeRemoteUseCase.getLanguageDynamic(language )
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

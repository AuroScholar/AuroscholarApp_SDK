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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WalletAmountViewModel extends ViewModel {
    CompositeDisposable compositeDisposable;
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();


    public MutableLiveData<String> mobileNumber = new MutableLiveData<>();
    public HomeUseCase homeUseCase;
    public HomeDbUseCase homeDbUseCase;
    public HomeRemoteUseCase homeRemoteUseCase;

    public WalletAmountViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase=homeDbUseCase;
        this.homeRemoteUseCase=homeRemoteUseCase;
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }


    public void checkInternet(Object object, Status status) {
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                switch (status) {
                    case WALLET_STATUS_API:
                        AppLogger.e("callGetWalletStatus-","step 2");
                        getWalletStatusApi((SetPasswordReqModel) object);
                        break;

                }

            } else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getString(R.string.internet_check), Status.SEND_OTP));
            }

        });
        getCompositeDisposable().add(disposable);
    }


    private void getWalletStatusApi(SetPasswordReqModel reqModel) {
        getCompositeDisposable().add(homeRemoteUseCase.getWalletStatusApi(reqModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseApi>() {
                               @Override
                               public void accept(ResponseApi responseApi) throws Exception {
                                   serviceLiveData.setValue(responseApi);
                                   AppLogger.e("callGetWalletStatus-","step 3");
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                AppLogger.e("callGetWalletStatus-","step 4" + throwable.getMessage());
                                defaultError(Status.WALLET_STATUS_API);
                                AppLogger.e("callGetWalletStatus-","step 4");
                            }
                        }));
    }



    private void defaultError(Status status) {
        serviceLiveData.setValue(new ResponseApi(Status.FAIL, AuroApp.getAppContext().getResources().getString(R.string.default_error), status));
    }



    public LiveData<ResponseApi> serviceLiveData() {

        return serviceLiveData;

    }

}

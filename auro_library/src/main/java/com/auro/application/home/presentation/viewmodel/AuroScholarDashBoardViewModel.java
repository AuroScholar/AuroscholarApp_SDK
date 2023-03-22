package com.auro.application.home.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.OtpOverCallReqModel;
import com.auro.application.home.data.model.PendingKycDocsModel;
import com.auro.application.home.data.model.RefferalReqModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.VerifyOtpReqModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.data.model.signupmodel.InstructionModel;
import com.auro.application.home.data.model.signupmodel.UserSlabsRequest;
import com.auro.application.home.data.model.signupmodel.InstructionModel;

import com.auro.application.home.domain.usecase.HomeDbUseCase;
import com.auro.application.home.domain.usecase.HomeRemoteUseCase;
import com.auro.application.home.domain.usecase.HomeUseCase;
import com.auro.application.util.AppLogger;
import com.auro.application.util.TextUtil;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.auro.application.core.common.Status.DASHBOARD_API;
import static com.auro.application.core.common.Status.DYNAMIC_LINK_API;
import static com.auro.application.core.common.Status.GET_INSTRUCTIONS_API;
import static com.auro.application.core.common.Status.NOTICE_INSTRUCTION;
import static com.auro.application.core.common.Status.OTP_OVER_CALL;
import static com.auro.application.core.common.Status.GET_SLABS_API;
import static com.auro.application.core.common.Status.SEND_REFERRAL_API;

public class AuroScholarDashBoardViewModel extends ViewModel {


    public HomeUseCase homeUseCase;
    public HomeDbUseCase homeDbUseCase;
    HomeRemoteUseCase homeRemoteUseCase;
    CompositeDisposable compositeDisposable;
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public AuroScholarDashBoardViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
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

    private void defaultError(Status status) {
        serviceLiveData.setValue(new ResponseApi(Status.FAIL, AuroApp.getAppContext().getResources().getString(R.string.default_error), null));
    }


    public void checkInternet(Status status, Object reqmodel) {
        AppLogger.v("QuizNew", "Dashboard step 3");
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                switch (status) {
                    case SEND_REFERRAL_API:
                        AppLogger.e("SEND_REFERRAL_API", "step 1");
                        sendRefferalDataApi((RefferalReqModel) reqmodel);
                        break;

                    case DASHBOARD_API:
                        AppLogger.v("QuizNew", "Dashboard step 4");
                        getDashboardData((AuroScholarInputModel) reqmodel);
                        break;

                    case SEND_OTP:
                        sendOtpRxApi((SendOtpReqModel) reqmodel);
                        break;

                    case VERIFY_OTP:
                        AppLogger.v("OTP_MAIN", "Step 2");
                        verifyOtpRxApi((VerifyOtpReqModel) reqmodel);
                        break;

                    case FETCH_STUDENT_PREFERENCES_API:
                        fetchStudentPreference((FetchStudentPrefReqModel) reqmodel);
                        break;

                    case DYNAMIC_LINK_API:
                        AppLogger.e("callRefferApi", "step 2");
                        getDynamicDataApi((DynamiclinkResModel) reqmodel);
                        break;

                    case GET_INSTRUCTIONS_API:
                        getInstructionsApi((InstructionModel) reqmodel);
                        break;

                    case NOTICE_INSTRUCTION:
                        getNoticeInstruction();
                        break;
                    case OTP_OVER_CALL:
                        optOverCall((OtpOverCallReqModel) reqmodel);
                        break;
                    case GET_MESSAGE_POP_UP:
                        AppLogger.v("UserIdPradeep", "UserId  GET_MESSAGE_POP_UP");
                        getShowDialogChangeGrade((PendingKycDocsModel) reqmodel);

                        break;
                    case PENDING_KYC_DOCS:
                        getPendingKycDocs((PendingKycDocsModel)  reqmodel);
                        break;

                    case GET_SLABS_API:
                        AppLogger.e("GET_SLABS_API", "step 2");
                        getSlabsApi((UserSlabsRequest) reqmodel);
                        break;

                }
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });
        getCompositeDisposable().add(disposable);

    }

    private void getSlabsApi(UserSlabsRequest id) {
        getCompositeDisposable().add(homeRemoteUseCase.getSlabsApi(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseApi>() {
                               @Override
                               public void accept(ResponseApi responseApi) throws Exception {
                                   AppLogger.e("GET_SLABS_API", "step 3");
                                   serviceLiveData.setValue(responseApi);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                AppLogger.e("GET_SLABS_API", "step 4");
                                defaultError(GET_SLABS_API);
                            }
                        }));
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
                                    defaultError(OTP_OVER_CALL);
                                }
                            }));
        } catch (Exception e) {
            AppLogger.e("registerApi", "Exception step 2" + e.getMessage());

        }
    }

    private void getNoticeInstruction( ) {
        getCompositeDisposable().add(homeRemoteUseCase.getNoticeInstruction().subscribeOn(Schedulers.io())
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
                                defaultError(NOTICE_INSTRUCTION);
                            }
                        }));
    }

    private void getPendingKycDocs(PendingKycDocsModel model ) {
        getCompositeDisposable().add(homeRemoteUseCase.pendingKycDocs(model).subscribeOn(Schedulers.io())
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
                                defaultError(NOTICE_INSTRUCTION);
                            }
                        }));
    }

    private void getShowDialogChangeGrade(PendingKycDocsModel model ) {
        AppLogger.v("UserIdPradeep", "UserId"+model.getUserId());
        getCompositeDisposable().add(homeRemoteUseCase.getMsgPopUp(model).subscribeOn(Schedulers.io())
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
                                defaultError(NOTICE_INSTRUCTION);
                            }
                        }));
    }



    private void getInstructionsApi(InstructionModel id) {
        getCompositeDisposable().add(homeRemoteUseCase.getInstructionsApi(id).subscribeOn(Schedulers.io())
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
                                defaultError(GET_INSTRUCTIONS_API);
                            }
                        }));
    }

    private void getDynamicDataApi(DynamiclinkResModel model) {
        AppLogger.e("callRefferApi", "step 3" + new Gson().toJson(model));
        getCompositeDisposable()
                .add(homeRemoteUseCase.getDynamicDataApi(model)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.e("callRefferApi", "step 4");
                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.e("callRefferApi", "step 5" + throwable.getMessage());
                                        defaultError(DYNAMIC_LINK_API);
                                    }
                                }));

    }

    /*For Add student*/
    private void sendRefferalDataApi(RefferalReqModel model) {
        AppLogger.e("SEND_REFERRAL_API", "step 1.2");
        getCompositeDisposable()
                .add(homeRemoteUseCase.sendRefferalDataApi(model)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.e("SEND_REFERRAL_API", "step 2");
                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.e("SEND_REFERRAL_API", "step 3" + throwable.getMessage());
                                        defaultError(SEND_REFERRAL_API);
                                    }
                                }));

    }

    public void getDashboardData(AuroScholarInputModel inputModel) {
        AuroScholarDataModel auroScholarDataModel = new AuroScholarDataModel();
        auroScholarDataModel.setMobileNumber(inputModel.getMobileNumber());
        auroScholarDataModel.setStudentClass(inputModel.getStudentClass());
        auroScholarDataModel.setRegitrationSource(inputModel.getRegitrationSource());
        auroScholarDataModel.setActivity(inputModel.getActivity());
        auroScholarDataModel.setFragmentContainerUiId(inputModel.getFragmentContainerUiId());
        auroScholarDataModel.setReferralLink(inputModel.getReferralLink());

        if (TextUtil.isEmpty(inputModel.getPartnerSource())) {
            auroScholarDataModel.setPartnerSource("");
        } else {
            auroScholarDataModel.setPartnerSource(inputModel.getPartnerSource());
        }

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel.getDeviceToken() != null && !TextUtil.isEmpty(prefModel.getDeviceToken())) {
            auroScholarDataModel.setDevicetoken(prefModel.getDeviceToken());
        } else {
            auroScholarDataModel.setDevicetoken("");
        }
        AppLogger.v("QuizNew", "Dashboard step 5");
        dashBoardApi(auroScholarDataModel);
    }

    private void dashBoardApi(AuroScholarDataModel model) {
        getCompositeDisposable()
                .add(homeRemoteUseCase.getDashboardData(model)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.v("QuizNew", "Dashboard step 6");
                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.v("QuizNew", "Dashboard step 7");
                                        defaultError(DASHBOARD_API);
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
                                defaultError(Status.SEND_OTP);
                            }
                        }));
    }

    private void verifyOtpRxApi(VerifyOtpReqModel reqModel) {
        AppLogger.v("OTP_MAIN", "Step 3");
        getCompositeDisposable().add(homeRemoteUseCase.verifyOtpApi(reqModel).subscribeOn(Schedulers.io())
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
                                defaultError(Status.VERIFY_OTP);
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
                                defaultError(Status.FETCH_STUDENT_PREFERENCES_API);
                            }
                        }));
    }




    public boolean checkKycStatus(DashboardResModel dashboardResModel) {
        return dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getPhoto()) && !TextUtil.isEmpty(dashboardResModel.getSchoolid()) &&
                !TextUtil.isEmpty(dashboardResModel.getIdback()) && !TextUtil.isEmpty(dashboardResModel.getIdfront());
    }
}

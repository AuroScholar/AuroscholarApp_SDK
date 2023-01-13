package com.auro.application.teacher.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.teacher.data.model.request.TeacherAddStudentInGroupReqModel;
import com.auro.application.teacher.data.model.request.TeacherCreateGroupReqModel;
import com.auro.application.teacher.data.model.request.TeacherDasboardSummaryResModel;
import com.auro.application.teacher.data.model.request.TeacherReqModel;
import com.auro.application.teacher.data.model.request.TeacherUserIdReq;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.domain.TeacherDbUseCase;
import com.auro.application.teacher.domain.TeacherRemoteUseCase;
import com.auro.application.teacher.domain.TeacherUseCase;
import com.auro.application.util.AppLogger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MyClassroomViewModel extends ViewModel {
    CompositeDisposable compositeDisposable;
    MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public TeacherUseCase teacherUseCase;
    TeacherRemoteUseCase teacherRemoteUseCase;
    TeacherDbUseCase teacherDbUseCase;

    public MyClassroomViewModel(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        this.teacherDbUseCase = teacherDbUseCase;
        this.teacherUseCase = teacherUseCase;
        this.teacherRemoteUseCase = teacherRemoteUseCase;
    }

    public void checkInternet(Object object, Status status) {
        Disposable disposable = teacherRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                switch (status) {
                    case TEACHER_CLASSROOM:
                        getTeacherClassApi((TeacherUserIdReq) object);
                        break;

                    case TEACHER_CREATE_GROUP_API:
                        AppLogger.v("InfoScreen", " callTeacher  TEACHER_CREATE_GROUP_API");
                        teacherCreateGroupApi((TeacherCreateGroupReqModel)object);
                        break;

                    case TEACHER_ADD_STUDENT_IN_GROUP_API:
                        teacherAddStudentInGroupApi((TeacherAddStudentInGroupReqModel) object);
                        break;


                    case GET_PROFILE_TEACHER_API:
                        getTeacherProfileDataApi(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
                        break;

                    case DYNAMIC_LINK_API:
                        AppLogger.e("callRefferApi","step 2");
                        getDynamicDataApi((DynamiclinkResModel) object);
                        break;

                }
            } else {
                AppLogger.v("InfoScreen", " callTeacher  serviceLiveData");
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });
        getCompositeDisposable().add(disposable);

    }
    private void getDynamicDataApi(DynamiclinkResModel model) {
        AppLogger.e("callRefferApi","step 3");
        getCompositeDisposable()
                .add(teacherRemoteUseCase.getDynamicDataApi(model)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.e("callRefferApi","step 4");
                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.e("callRefferApi","step 5"+ throwable.getMessage());
                                        defaultError();
                                    }
                                }));

    }

    private void getTeacherProfileDataApi(String userId) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.getProfileTeacherApi(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                // serviceLiveData.setValue(ResponseApi.loading(Status.GET_PROFILE_TEACHER_API));
                            }
                        })
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.v("GetProfiler","step 0.1-");
                                           serviceLiveData.setValue(responseApi);
                                           AppLogger.v("GetProfiler","step 0.2-" +((MyProfileResModel)responseApi.data).getCreatedAt());
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.v("GetProfiler","step 0.3-"+throwable.getMessage());
                                        defaultError();
                                    }
                                }));

    }

    public void getTeacherProfileData(AuroScholarDataModel auroScholarDataModel) {
        Disposable disposable = teacherRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                getTeacherDashboardApi(auroScholarDataModel);
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });
        getCompositeDisposable().add(disposable);

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
                                AppLogger.v("InfoScreen", " callTeacher  TEACHER_CREATE_GROUP_API");
                                serviceLiveData.setValue(ResponseApi.loading(null));
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

    public void getTeacherDashboardSummaryData(TeacherUserIdReq dataModel) {
        AppLogger.e("SummaryData", "Step 1");
        Disposable disposable = teacherRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                AppLogger.e("SummaryData", "Step 2");
                getTeacherDashboardSummaryApi(dataModel);
                AppLogger.e("SummaryData", "Step 10");
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });
        getCompositeDisposable().add(disposable);

    }
    private void teacherCreateGroupApi(TeacherCreateGroupReqModel reqModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.teacherCreateGroupApi(reqModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.v("InfoScreen", " callTeacher  responseApi");

                                           serviceLiveData.setValue(responseApi);
                                           if(serviceLiveData.hasObservers())
                                           {
                                               AppLogger.v("InfoScreen", " callTeacher  responseApi");
                                              // AppLogger.v("callAddStudentInGroup"," teacherCreateGroupApi main TeacherApi call back step 0.1");
                                           }
                                           AppLogger.v("InfoScreen", " callTeacher  responseApi else");
                                           //AppLogger.v("callAddStudentInGroup"," teacherCreateGroupApi main TeacherApi call back step 0.2");
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.v("InfoScreen", " callTeacher  throwable");

                                        defaultError();
                                    }
                                }
                        ));

    }

    private void getTeacherDashboardSummaryApi(TeacherUserIdReq auroScholarDataModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.teacherDashboardSummary(auroScholarDataModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.v("getTeacherDashboardSummaryApi", " step 1 ");
                                           try {
                                               AppLogger.v("getTeacherDashboardSummaryApi", " step 2 ");
                                               TeacherDasboardSummaryResModel resModel = (TeacherDasboardSummaryResModel) responseApi.data;
                                           }catch (Exception e)
                                           {
                                               AppLogger.v("getTeacherDashboardSummaryApi", " step 3 " + e.getMessage());
                                           }
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


    private void getTeacherClassApi(TeacherUserIdReq auroScholarDataModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.teacherClassRoom(auroScholarDataModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                //serviceLiveData.setValue(ResponseApi.loading(null));
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
                                        AppLogger.e("chhonker exception","--"+throwable.getMessage());
                                        defaultError();
                                    }
                                }));

    }

    private void teacherAddStudentInGroupApi(TeacherAddStudentInGroupReqModel reqModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.teacherAddStudentInGroupApi(reqModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.v("TEACHER_ADD_STUDENT_IN_GROUP_API", "teacherAddStudentInGroupApi ");
                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.v("TEACHER_ADD_STUDENT_IN_GROUP_API", "teacherAddStudentInGroupApi exception "+throwable.getMessage());
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

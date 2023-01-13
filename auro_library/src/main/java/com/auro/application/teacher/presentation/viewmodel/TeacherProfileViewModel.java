package com.auro.application.teacher.presentation.viewmodel;

import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.common.StateDataModel;
import com.auro.application.teacher.data.model.request.TeacherReqModel;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.domain.TeacherDbUseCase;
import com.auro.application.teacher.domain.TeacherRemoteUseCase;
import com.auro.application.teacher.domain.TeacherUseCase;
import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.util.AppLogger;
import com.auro.application.util.TextUtil;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TeacherProfileViewModel extends ViewModel {

    CompositeDisposable compositeDisposable;
    MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public com.auro.application.teacher.domain.TeacherUseCase teacherUseCase;
    com.auro.application.teacher.domain.TeacherRemoteUseCase teacherRemoteUseCase;
    com.auro.application.teacher.domain.TeacherDbUseCase teacherDbUseCase;

    public TeacherProfileViewModel(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        this.teacherDbUseCase = teacherDbUseCase;
        this.teacherUseCase = teacherUseCase;
        this.teacherRemoteUseCase = teacherRemoteUseCase;
    }


    public void getTeacherProfileData(String userId) {

        Disposable disposable = teacherRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                AppLogger.v("GetProfiler","Step - 2 -- ");
                getTeacherProfileDataApi(userId);
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });

        getCompositeDisposable().add(disposable);

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



    public void updateTeacherProfileData(TeacherReqModel reqModel) {

        Disposable disposable = teacherRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                AppLogger.v("InfoScreen", "Step 2 Calling api update teacherprofile  internet check");
                updateTeacherProfileApi(reqModel);
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });

        getCompositeDisposable().add(disposable);

    }


    private void updateTeacherProfileApi(TeacherReqModel demographicResModel) {
        AppLogger.v("InfoScreen", "Step 3 Calling api update teacherprofile  internet check");
        getCompositeDisposable()
                .add(teacherRemoteUseCase.updateTeacherProfileApi(demographicResModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                               //serviceLiveData.setValue(ResponseApi.loading(Status.UPDATE_TEACHER_PROFILE_API));
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


    public void getStateListData() {
        getCompositeDisposable().add(teacherDbUseCase.getStateList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<com.auro.application.teacher.data.model.common.StateDataModel>>() {
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
        getCompositeDisposable().add(teacherDbUseCase.insertStateList(teacherUseCase.readStateData())
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
        getCompositeDisposable().add(teacherDbUseCase.getDistrictList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<com.auro.application.teacher.data.model.common.DistrictDataModel>>() {
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
        getCompositeDisposable().add(teacherDbUseCase.insertDistrictList(teacherUseCase.readDistrictData())
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
        getCompositeDisposable().add(teacherDbUseCase.getDistrictList(stateCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<com.auro.application.teacher.data.model.common.DistrictDataModel>>() {
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

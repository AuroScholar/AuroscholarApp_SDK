package com.auro.application.quiz.presentation.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.MessgeNotifyStatus;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;

import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.SaveImageReqModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.domain.usecase.HomeUseCase;
import com.auro.application.quiz.data.model.getQuestionModel.FetchQuizResModel;
import com.auro.application.quiz.data.model.response.SaveQuestionResModel;
import com.auro.application.quiz.data.model.submitQuestionModel.SubmitExamReq;
import com.auro.application.quiz.domain.QuizNativeRemoteUseCase;
import com.auro.application.quiz.domain.QuizNativeUseCase;
import com.auro.application.util.AppLogger;

import java.io.ByteArrayOutputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.auro.application.core.common.Status.DASHBOARD_API;
import static com.auro.application.core.common.Status.FETCH_QUIZ_DATA_API;
import static com.auro.application.core.common.Status.FINISH_QUIZ_API;
import static com.auro.application.core.common.Status.SUBMITQUIZ;
import static com.auro.application.core.common.Status.UPLOAD_EXAM_FACE_API;


public class QuizTestNativeViewModel extends ViewModel {


    public MutableLiveData<String> mobileNumber = new MutableLiveData<>();
    public QuizNativeUseCase quizUseCase = new QuizNativeUseCase();



    public QuizNativeUseCase quizNativeUseCase;

    public QuizNativeRemoteUseCase quizNativeRemoteUseCase;


    CompositeDisposable compositeDisposable;
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();
    private MutableLiveData<MessgeNotifyStatus> notifyLiveData = new MutableLiveData<>();

    public QuizTestNativeViewModel(QuizNativeUseCase quizNativeUseCase,QuizNativeRemoteUseCase quizNativeRemoteUseCase) {

        this.quizNativeUseCase = quizNativeUseCase;
        this.quizNativeRemoteUseCase =quizNativeRemoteUseCase;
    }


    public void fetchQuizData(AssignmentReqModel reqmodel) {
        Disposable disposable = quizNativeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                fecthQuizDataApi(reqmodel);
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });
        getCompositeDisposable().add(disposable);

    }

    private void fecthQuizDataApi(AssignmentReqModel reqmodel) {
        getCompositeDisposable()
                .add(quizNativeRemoteUseCase.getFetchQuizData(reqmodel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                //    serviceLiveData.setValue(ResponseApi.loading(null));
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
                                        defaultError(FETCH_QUIZ_DATA_API);                                    }
                                }));

    }

    public void saveQuizData(SaveQuestionResModel reqmodel) {
        Disposable disposable = quizNativeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                saveQuizDataApi(reqmodel);
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });
        getCompositeDisposable().add(disposable);

    }

    private void saveQuizDataApi(SaveQuestionResModel reqmodel) {
        getCompositeDisposable()
                .add(quizNativeRemoteUseCase.saveFetchQuizData(reqmodel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                //    serviceLiveData.setValue(ResponseApi.loading(null));
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
                                        AppLogger.e("saveQuizDataApi","callSaveQuestionApi 3"+throwable.getMessage());

                                        defaultError(FETCH_QUIZ_DATA_API);                                    }
                                }));

    }


    public void finishQuiz(SaveQuestionResModel reqmodel) {
        Disposable disposable = quizNativeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                finishQuizApi(reqmodel);
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });
        getCompositeDisposable().add(disposable);

    }

    private void finishQuizApi(SaveQuestionResModel reqmodel) {
        getCompositeDisposable()
                .add(quizNativeRemoteUseCase.finishQuizApi(reqmodel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                //    serviceLiveData.setValue(ResponseApi.loading(null));
                            }
                        })
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.v("customFinishProgressDialog ", "Success");
                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.v("customFinishProgressDialog throwable", throwable.getMessage());
                                        defaultError(FINISH_QUIZ_API);
                                    }
                        }));

    }



    public void uploadExamFace(SaveImageReqModel reqmodel) {
        Disposable disposable = quizNativeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                AppLogger.v("QuizNew","UPLOAD EMAM STEP 2");
                uploadExamFaceAPi(reqmodel);
            } else {
                AppLogger.v("QuizNew","UPLOAD EMAM STEP 3");
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });
        getCompositeDisposable().add(disposable);

    }

    private void uploadExamFaceAPi(SaveImageReqModel reqmodel) {
        getCompositeDisposable()
                .add(quizNativeRemoteUseCase.uploadStudentExamImage(reqmodel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                AppLogger.v("QuizNew","UPLOAD EXAM step 4");
                                //    serviceLiveData.setValue(ResponseApi.loading(null));
                            }
                        })
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                       //   serviceLiveData.setValue(responseApi);
                                           AppLogger.v("QuizNew","UPLOAD EXAM step 5");
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                       // defaultError(UPLOAD_EXAM_FACE_API);
                                        AppLogger.v("QuizNew","UPLOAD EXAM step 6");
                                    }
                                }));

    }



    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }


    public MutableLiveData<MessgeNotifyStatus> getNotifyLiveData() {
        return notifyLiveData;
    }

    public LiveData<ResponseApi> serviceLiveData() {

        return serviceLiveData;

    }


    private void defaultError(Status status) {
        serviceLiveData.setValue(new ResponseApi(Status.FAIL, AuroApp.getAppContext().getResources().getString(R.string.default_error), status));
    }


    public void getDashBoardData(AuroScholarDataModel model) {

        Disposable disposable = quizNativeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                AppLogger.v("QuizNew","Dashboard step 2");
                dashBoardApi(model);
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getString(R.string.internet_check), Status.NO_INTERNET));
            }
        });

        getCompositeDisposable().add(disposable);

    }

    private void dashBoardApi(AuroScholarDataModel model) {
        AppLogger.v("QuizNew","Dashboard step 3");
        getCompositeDisposable()

                .add(quizNativeRemoteUseCase.getDashboardData(model)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                AppLogger.v("QuizNew","Dashboard step 4");
                                //    serviceLiveData.setValue(ResponseApi.loading(null));
                            }
                        })
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                          // serviceLiveData.setValue(responseApi);
                                           AppLogger.v("QuizNew","Accept subscribe");
                                           AppLogger.v("QuizNew","Dashboard step 5");
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        defaultError(DASHBOARD_API);
                                        AppLogger.v("QuizNew","Dashboard step 6");}
                                }));
    }






    public static byte[] encodeToBase64(Bitmap image, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOS);
        byte[] byteArray = byteArrayOS.toByteArray();
        return byteArray;
    }

    public long  bytesIntoHumanReadable(long bytes) {
        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        long gigabyte = megabyte * 1024;
        long terabyte = gigabyte * 1024;

        return (bytes / megabyte);

/*
        if ((bytes >= 0) && (bytes < kilobyte)) {
            return bytes + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return (bytes / kilobyte) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return (bytes / megabyte) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return (bytes / gigabyte) + " GB";

        } else if (bytes >= terabyte) {
            return (bytes / terabyte) + " TB";

        } else {
            return bytes + " Bytes";
        }*/
    }

}

package com.auro.application.quiz.domain;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.NetworkUtil;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.core.network.NetworkUseCase;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.SaveImageReqModel;
import com.auro.application.quiz.data.model.getQuestionModel.FetchQuizResModel;
import com.auro.application.quiz.data.model.response.SaveQuestionResModel;
import com.auro.application.quiz.data.model.submitQuestionModel.SubmitExamReq;
import com.auro.application.quiz.data.model.submitQuestionModel.SubmitExamResultRes;
import com.auro.application.quiz.data.repository.QuizRepo;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import retrofit2.Response;

import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_200;
import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_400;
import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_401;
import static com.auro.application.core.common.AppConstant.ResponseConstatnt.RES_FAIL;
import static com.auro.application.core.common.Status.DASHBOARD_API;
import static com.auro.application.core.common.Status.FETCH_QUIZ_DATA_API;
import static com.auro.application.core.common.Status.FINISH_QUIZ_API;
import static com.auro.application.core.common.Status.SAVE_QUIZ_DATA_API;
import static com.auro.application.core.common.Status.SUBMITQUIZ;
import static com.auro.application.core.common.Status.UPLOAD_EXAM_FACE_API;

public class QuizNativeRemoteUseCase extends NetworkUseCase {

    //  QuizRepo.QuizRemoteData quizRemoteData;
    // QuizRemoteDataSourceImp quizRemoteDataSourceImp = new QuizRemoteDataSourceImp();
    QuizRepo.QuizRemoteData quizRemoteDataRemoteData;
    Gson gson = new Gson();

    public QuizNativeRemoteUseCase(QuizRepo.QuizRemoteData quizRemoteData) {
        this.quizRemoteDataRemoteData = quizRemoteData;
    }


    public Single<ResponseApi> getFetchQuizData(AssignmentReqModel params) {
        return quizRemoteDataRemoteData.getFetchQuizData(params).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, FETCH_QUIZ_DATA_API);


                } else {

                    return responseFail(FETCH_QUIZ_DATA_API);
                }
            }
        });
    }

    public Single<ResponseApi> saveFetchQuizData(SaveQuestionResModel params) {
        return quizRemoteDataRemoteData.saveFetchQuizData(params).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, SAVE_QUIZ_DATA_API);

                } else {

                    return responseFail(SAVE_QUIZ_DATA_API);
                }
            }
        });
    }

    public Single<ResponseApi> finishQuizApi(SaveQuestionResModel params) {
        return quizRemoteDataRemoteData.finishQuizApi(params).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, FINISH_QUIZ_API);
                } else {
                    return responseFail(FINISH_QUIZ_API);
                }
            }
        });
    }

    public Single<ResponseApi> uploadStudentExamImage(SaveImageReqModel params) {
        return quizRemoteDataRemoteData.uploadStudentExamImage(params).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, UPLOAD_EXAM_FACE_API);
                } else {
                    return responseFail(UPLOAD_EXAM_FACE_API);
                }
            }
        });
    }

    public Single<ResponseApi> getDashboardData(AuroScholarDataModel model) {

        return quizRemoteDataRemoteData.getDashboardData(model).map(new Function<Response<JsonObject>, ResponseApi>() {
            @Override
            public ResponseApi apply(Response<JsonObject> response) throws Exception {

                if (response != null) {
                    return handleResponse(response, DASHBOARD_API);

                } else {

                    return responseFail(DASHBOARD_API);
                }
            }
        });
    }


    private ResponseApi handleResponse(Response<JsonObject> response, Status apiTypeStatus) {

        switch (response.code()) {

            case RES_200:
                return response200(response, apiTypeStatus);

            case RES_401:
                return response401(apiTypeStatus);

            case RES_400:
                return responseFail400(response, apiTypeStatus);

            case RES_FAIL:
                return responseFail(apiTypeStatus);

            default:
                return ResponseApi.fail(AuroApp.getAppContext().getString(R.string.default_error), apiTypeStatus);
        }
    }


    @Override
    public Single<Boolean> isAvailInternet() {
        return NetworkUtil.hasInternetConnection();
    }

    @Override
    public ResponseApi response200(Response<JsonObject> response, Status status) {

        try {
            if (status == FETCH_QUIZ_DATA_API) {
                FetchQuizResModel fetchQuizResModel = gson.fromJson(response.body(), FetchQuizResModel.class);
                return ResponseApi.success(fetchQuizResModel, status);
            } else if (status == SUBMITQUIZ) {
                SubmitExamResultRes submitExamResultRes = gson.fromJson(response.body(), SubmitExamResultRes.class);
                return ResponseApi.success(submitExamResultRes, status);
            } else if (status == FINISH_QUIZ_API) {
                SaveQuestionResModel resModel = gson.fromJson(response.body(), SaveQuestionResModel.class);
                return ResponseApi.success(resModel, status);
            } else if (status == SAVE_QUIZ_DATA_API) {
                SaveQuestionResModel resModel = gson.fromJson(response.body(), SaveQuestionResModel.class);
                return ResponseApi.success(resModel, status);
            } else if (status == DASHBOARD_API) {
                DashboardResModel resModel = gson.fromJson(response.body(), DashboardResModel.class);
                return ResponseApi.success(resModel, status);
            }
        }catch (Exception e)
        {
            AppLogger.e("ApiParceException-",e.getMessage());
        }

        return ResponseApi.fail("Something Went Wrong", status);

    }

    @Override
    public ResponseApi response401(Status status) {
        return ResponseApi.authFail(401, status);
    }

    @Override
    public ResponseApi responseFail400(Response<JsonObject> response, Status status) {
        try {
            String errorJson = response.errorBody().string();
            String errorMessage = AppUtil.errorMessageHandler(AuroApp.getAppContext().getString(R.string.default_error), errorJson);
            return ResponseApi.fail400(errorMessage, null);
        } catch (Exception e) {
            return ResponseApi.fail(AuroApp.getAppContext().getResources().getString(R.string.default_error), status);
        }
    }

    @Override
    public ResponseApi responseFail(Status status) {
        return ResponseApi.fail(AuroApp.getAppContext().getString(R.string.default_error), status);
    }
}

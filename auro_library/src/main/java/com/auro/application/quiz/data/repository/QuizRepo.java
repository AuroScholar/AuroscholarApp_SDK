package com.auro.application.quiz.data.repository;


import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.SaveImageReqModel;
import com.auro.application.quiz.data.model.getQuestionModel.FetchQuizResModel;
import com.auro.application.quiz.data.model.response.SaveQuestionResModel;
import com.auro.application.quiz.data.model.submitQuestionModel.SubmitExamReq;
import com.google.gson.JsonObject;

import io.reactivex.Single;
import retrofit2.Response;


public interface QuizRepo {

    interface QuizRemoteData {
        Single<Response<JsonObject>> getFetchQuizData(AssignmentReqModel params);
        Single<Response<JsonObject>> saveFetchQuizData(SaveQuestionResModel params);
        Single<Response<JsonObject>> finishQuizApi(SaveQuestionResModel params);
        Single<Response<JsonObject>> uploadStudentExamImage(SaveImageReqModel params);
        Single<Response<JsonObject>> getDashboardData(AuroScholarDataModel model);


    }




}

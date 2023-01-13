package com.auro.application.quiz.data.datasource.remote;

import com.auro.application.core.common.AppConstant;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.AuroScholarDataModel;

import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.data.model.KYCInputModel;
import com.auro.application.home.data.model.SaveImageReqModel;
import com.auro.application.quiz.data.model.getQuestionModel.FetchQuizResModel;
import com.auro.application.quiz.data.model.response.SaveQuestionResModel;
import com.auro.application.quiz.data.model.submitQuestionModel.SubmitExamReq;
import com.auro.application.quiz.data.repository.QuizRepo;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class QuizRemoteDataSourceImp implements QuizRepo.QuizRemoteData {

    QuizRemoteApi quizRemoteApi;// = APIClient.getClient().create(QuizRemoteApi.class);

    public QuizRemoteDataSourceImp( QuizRemoteApi quizRemoteApi) {
        this.quizRemoteApi = quizRemoteApi;
    }




    @Override
    public Single<Response<JsonObject>> getFetchQuizData(AssignmentReqModel reqModel) {
        Map<String, Object> params = new HashMap<String, Object>();
      /*  params.put(AppConstant.AssignmentApiParams.REGISTRATION_ID, reqModel.getRegistration_id());
        params.put(AppConstant.AssignmentApiParams.EXAM_NAME, reqModel.getExam_name());
        params.put(AppConstant.AssignmentApiParams.QUIZ_ATTEMPT, reqModel.getQuiz_attempt());
        params.put(AppConstant.AssignmentApiParams.SUBJECT, reqModel.getSubject());
        params.put(AppConstant.AssignmentApiParams.EXAMLANG, reqModel.getExamlang());*/
        params.put(AppConstant.AssignmentApiParams.EXAM_ID, reqModel.getExamId());
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,reqModel.getUserPreferedLanguageId());
        return quizRemoteApi.fetchQuizData(params);
    }


    @Override
    public Single<Response<JsonObject>> saveFetchQuizData(SaveQuestionResModel reqModel) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put(AppConstant.AssignmentApiParams.EXAM_ASSIGNMENT_ID, reqModel.getExamAssignmentID());
        params.put(AppConstant.AssignmentApiParams.QUESTION_ID, reqModel.getQuestionID());
        params.put(AppConstant.AssignmentApiParams.ANSWER_ID, reqModel.getAnswerID());
        params.put(AppConstant.AssignmentApiParams.QUESTION_SERIAL_NUMBER, reqModel.getQuestionSerialNo());
        params.put(AppConstant.AssignmentApiParams.EXAM_ID, reqModel.getExamId());
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,reqModel.getUserPreferedLanguage_id());
        return quizRemoteApi.saveQuizData(params);
    }

    @Override
    public Single<Response<JsonObject>> finishQuizApi(SaveQuestionResModel reqModel) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AppConstant.AssignmentApiParams.EXAM_ASSIGNMENT_ID, reqModel.getExamAssignmentID());
        params.put(AppConstant.AssignmentApiParams.COMPLETE_BY, reqModel.getComplete_by());
        params.put(AppConstant.AssignmentApiParams.EXAM_ID, reqModel.getExamId());
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);
        params.put(AppConstant.Language.USER_PREFERED_LANGUAGE,reqModel.getUserPreferedLanguage_id());
        return quizRemoteApi.finishQuizApi(params);
    }

/*    @Override
    public Single<Response<JsonObject>> uploadStudentExamImage(SaveImageReqModel saveQuestionResModel) {
        RequestBody exam_id = RequestBody.create(okhttp3.MultipartBody.FORM,saveQuestionResModel.getExamAssignmentID());
        MultipartBody.Part student_photo = ConversionUtil.INSTANCE.makeMultipartRequestForExamImage(saveQuestionResModel.getImageBytes());
        return quizRemoteApi.uploadImage(exam_id,
                student_photo);
    }*/

    @Override
    public Single<Response<JsonObject>> uploadStudentExamImage(SaveImageReqModel reqModel) {
        RequestBody exam_id = RequestBody.create(okhttp3.MultipartBody.FORM, reqModel.getExamId());
        RequestBody registration_id = RequestBody.create(okhttp3.MultipartBody.FORM, reqModel.getRegistration_id());
        RequestBody is_mobile = RequestBody.create(okhttp3.MultipartBody.FORM, "1");
        RequestBody quiz_id = RequestBody.create(okhttp3.MultipartBody.FORM, reqModel.getQuizId());
        RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, reqModel.getUserId());
        RequestBody img_normal_path = RequestBody.create(okhttp3.MultipartBody.FORM, reqModel.getImgNormalPath());
        RequestBody img_path = RequestBody.create(okhttp3.MultipartBody.FORM, reqModel.getImgPath());
        RequestBody langVersion = RequestBody.create(okhttp3.MultipartBody.FORM, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        RequestBody apiVersion = RequestBody.create(okhttp3.MultipartBody.FORM,AppConstant.ParamsValue.API_VERSION_VAL);


        MultipartBody.Part student_photo = ConversionUtil.INSTANCE.makeMultipartRequestForExamImage(reqModel.getImageBytes());
        return quizRemoteApi.uploadImage(exam_id, registration_id, is_mobile, quiz_id, img_normal_path, img_path
               ,userId, langVersion,apiVersion ,student_photo);


       /* RequestBody exam_id = RequestBody.create(okhttp3.MultipartBody.FORM,saveQuestionResModel.getExamAssignmentID());
        MultipartBody.Part student_photo = ConversionUtil.INSTANCE.makeMultipartRequestForExamImage(saveQuestionResModel.getImageBytes());
        return quizRemoteApi.uploadImage(exam_id,
                student_photo);*/
    }

    @Override
    public Single<Response<JsonObject>> getDashboardData(AuroScholarDataModel model) {
        Map<String, String> params = new HashMap<String, String>();

        params.put(AppConstant.MOBILE_NUMBER, model.getMobileNumber());
        params.put(AppConstant.DashBoardParams.STUDENT_CLASS, String.valueOf(model.getStudentClass()));
        params.put(AppConstant.DashBoardParams.REGISTRATION_SOURCE, model.getRegitrationSource());
        params.put(AppConstant.DashBoardParams.PARTNER_SOURCE, model.getPartnerSource());
        params.put(AppConstant.DashBoardParams.REFERRER_MOBILE, model.getShareIdentity());
        params.put(AppConstant.DashBoardParams.DEVICE_TOKEN, model.getDevicetoken());
        params.put(AppConstant.DashBoardParams.IP_ADDRESS, AppUtil.getIpAdress());
        params.put(AppConstant.DashBoardParams.BUILD_VERSION, AppUtil.getAppVersionName());
        params.put(AppConstant.DashBoardParams.LANGUAGE, ViewUtil.getLanguageId());
        params.put(AppConstant.DashBoardParams.LANGUAGE_VERSION, AppConstant.ParamsValue.LANGUAGE_VERSION_VAL);
        params.put(AppConstant.DashBoardParams.API_VERSION, AppConstant.ParamsValue.API_VERSION_VAL);

        return quizRemoteApi.getDashboardSDKData(params);
    }




}

package com.auro.application.home.domain.usecase;


import android.app.Activity;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.ValidationModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.ChapterResModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.DemographicResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.FriendsLeaderBoardModel;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.data.model.KYCInputModel;
import com.auro.application.home.data.model.KYCResItemModel;
import com.auro.application.home.data.model.MonthlyScholarShipModel;
import com.auro.application.home.data.model.QuizResModel;
import com.auro.application.home.data.model.QuizTestDataModel;
import com.auro.application.home.data.model.StudentProfileModel;
import com.auro.application.home.data.model.SubjectResModel;
import com.auro.application.home.data.model.response.CertificateResModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.StudentKycStatusResModel;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.common.StateDataModel;
import com.auro.application.teacher.data.model.request.SelectClassesSubject;
import com.auro.application.teacher.data.model.response.MyClassRoomTeacherResModel;
import com.auro.application.teacher.data.model.response.TeacherKycStatusResModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HomeUseCase {


  


    public ValidationModel isUserTypeSelected(int userType, int classtype) {
        if (userType == AppConstant.userTypeLogin.STUDENT) {

            if (classtype <= 5 && classtype != 0) {
                return new ValidationModel(true, AppConstant.userTypeLogin.SELECTED);

            } else {
                return new ValidationModel(false, AppConstant.classLogin.SELECT_CLASS_TYPE);
            }

        } else if (userType == AppConstant.userTypeLogin.TEACHER) {

            return new ValidationModel(true, AppConstant.userTypeLogin.SELECTED);
        } else {
            return new ValidationModel(false, AppConstant.userTypeLogin.SELECT_USERTYPE);
        }


    }

    public List<StateDataModel> readStateData() {
        List<StateDataModel> stateList = new ArrayList<>();
        InputStream inStream = AuroApp.getAppContext().getResources().openRawResource(R.raw.state);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(",");
                if (colums.length != 7) {
                    AppLogger.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                } else {
                    StateDataModel stateDataModel = new StateDataModel();
                    stateDataModel.setState_code(colums[0].replaceAll("\"", ""));
                    stateDataModel.setState_name(colums[1].replaceAll("\"", ""));
                    stateDataModel.setShort_name(colums[2].replaceAll("\"", ""));
                    stateDataModel.setActive_status(colums[3].replaceAll("\"", ""));
                    stateDataModel.setFlag(colums[6].replaceAll("\"", ""));
                    stateList.add(stateDataModel);
                    AppLogger.d("CSVParser", "state_name" + colums[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stateList;
    }


    public List<DistrictDataModel> readDistrictData() {
        List<DistrictDataModel> districtList = new ArrayList<>();
        InputStream inStream = AuroApp.getAppContext().getResources().openRawResource(R.raw.district);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(",");
                if (colums.length != 7) {
                    AppLogger.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                } else {
                    DistrictDataModel districtDataModel = new DistrictDataModel();
                    districtDataModel.setState_code(colums[0].replaceAll("\"", ""));
                    districtDataModel.setDistrict_code(colums[1].replaceAll("\"", ""));
                    districtDataModel.setDistrict_name(colums[2].replaceAll("\"", ""));
                    districtDataModel.setActive_status(colums[3].replaceAll("\"", ""));
                    districtDataModel.setFlag(colums[6].replaceAll("\"", ""));
                    districtList.add(districtDataModel);
                    AppLogger.d("CSVParser", "district_name" + colums[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return districtList;
    }

    public List<SelectClassesSubject> selectClass(String classes) {


        List<SelectClassesSubject> list = new ArrayList<>();

        SelectClassesSubject classes1 = new SelectClassesSubject();
        classes1.setText("1st");
        classes1.setSelected(false);
        classes1.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
        list.add(classes1);

        SelectClassesSubject classes2 = new SelectClassesSubject();
        classes2.setText("7th");
        classes2.setSelected(false);
        classes2.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
        list.add(classes2);

        SelectClassesSubject classes3 = new SelectClassesSubject();
        classes3.setText("2nd");
        classes3.setSelected(false);
        classes3.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
        list.add(classes3);

        SelectClassesSubject classes4 = new SelectClassesSubject();
        classes4.setText("8th");
        classes4.setSelected(false);
        classes4.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
        list.add(classes4);

        SelectClassesSubject classes12 = new SelectClassesSubject();
        classes12.setText("3rd");
        classes12.setSelected(false);
        classes12.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
        list.add(classes12);

        SelectClassesSubject classes5 = new SelectClassesSubject();
        classes5.setText("9th");
        classes5.setSelected(false);
        classes5.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
        list.add(classes5);

        SelectClassesSubject classes6 = new SelectClassesSubject();
        classes6.setText("4th");
        classes6.setSelected(false);
        classes6.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
        list.add(classes6);

        SelectClassesSubject classes7 = new SelectClassesSubject();
        classes7.setText("10th");
        classes7.setSelected(false);
        classes7.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
        list.add(classes7);

        SelectClassesSubject classes8 = new SelectClassesSubject();
        classes8.setText("5th");
        classes8.setSelected(false);
        classes8.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
        list.add(classes8);

        SelectClassesSubject classes9 = new SelectClassesSubject();
        classes9.setText("11th");
        classes9.setSelected(false);
        classes9.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
        list.add(classes9);

        SelectClassesSubject classes10 = new SelectClassesSubject();
        classes10.setText("6th");
        classes10.setSelected(false);
        classes10.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
        list.add(classes10);

        SelectClassesSubject classes11 = new SelectClassesSubject();
        classes11.setText("12th");
        classes11.setSelected(false);
        classes11.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
        list.add(classes11);
        if (classes != null) {
            List<String> newData = getStringList(classes);
            if (!newData.isEmpty()) {
                for (int i = 0; i < newData.size(); i++) {
                    for (int j = 0; j < list.size(); j++) {
                        if (newData.get(i).equalsIgnoreCase(list.get(j).getText())) {
                            SelectClassesSubject classes13 = new SelectClassesSubject();
                            classes13.setText(newData.get(i));
                            classes13.setSelected(true);
                            classes13.setViewType(AppConstant.FriendsLeaderBoard.CLASSESADAPTER);
                            list.set(j, classes13);

                        }
                    }
                }
            }
        }
        return list;

    }

    public List<SelectClassesSubject> selectSubject(String Subject) {

        List<SelectClassesSubject> list = new ArrayList<>();
        SelectClassesSubject classes1 = new SelectClassesSubject();
        classes1.setText("Maths");

        classes1.setViewType(AppConstant.FriendsLeaderBoard.SUBJECTADAPTER);
        list.add(classes1);

        SelectClassesSubject classes2 = new SelectClassesSubject();
        classes2.setText("Social Science");

        classes2.setViewType(AppConstant.FriendsLeaderBoard.SUBJECTADAPTER);
        list.add(classes2);

        SelectClassesSubject classes3 = new SelectClassesSubject();
        classes3.setText("English");

        classes3.setViewType(AppConstant.FriendsLeaderBoard.SUBJECTADAPTER);
        list.add(classes3);
        SelectClassesSubject classes4 = new SelectClassesSubject();
        classes4.setText("Science");

        classes4.setViewType(AppConstant.FriendsLeaderBoard.SUBJECTADAPTER);
        list.add(classes4);
        SelectClassesSubject classes5 = new SelectClassesSubject();
        classes5.setText("Hindi");

        classes5.setViewType(AppConstant.FriendsLeaderBoard.SUBJECTADAPTER);
        list.add(classes5);
        SelectClassesSubject classes6 = new SelectClassesSubject();
        classes6.setText("Other");

        classes6.setViewType(AppConstant.FriendsLeaderBoard.SUBJECTADAPTER);
        list.add(classes6);
        if (Subject != null) {
            List<String> newData = getStringList(Subject);
            if (!newData.isEmpty()) {
                for (int i = 0; i < newData.size(); i++) {
                    for (int j = 0; j < list.size(); j++) {
                        if (newData.get(i).equalsIgnoreCase(list.get(j).getText())) {
                            SelectClassesSubject classes13 = new SelectClassesSubject();
                            classes13.setText(newData.get(i));
                            classes13.setSelected(true);
                            classes13.setViewType(AppConstant.FriendsLeaderBoard.SUBJECTADAPTER);
                            list.set(j, classes13);

                        }
                    }
                }
            }
        }
        return list;

    }

    public List<String> getStringList(String data) {
        String[] array = data.split(",");
        List<String> datainlist = new ArrayList<>(Arrays.asList(array));
        return datainlist;
    }

    public ArrayList<QuizResModel> makeDummyQuizList() {
        ArrayList<QuizResModel> productModelList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            productModelList.add(new QuizResModel());
        }
        return productModelList;
    }


    public ArrayList<KYCDocumentDatamodel> makeAdapterDocumentList(DashboardResModel dashboardResModel, Activity activity) {
        ArrayList<KYCDocumentDatamodel> kycDocumentList = new ArrayList<>();
        try {
            Details details=AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

            KYCDocumentDatamodel kyc_one = new KYCDocumentDatamodel();
            kyc_one.setDocumentId(AppConstant.DocumentType.ID_PROOF_FRONT_SIDE);
            kyc_one.setDocumentName(details.getAadhar_front_side() != null ?details.getAadhar_front_side() : AuroApp.getAppContext().getResources().getString(R.string.upload_govt_id));
            kyc_one.setDocumentFileName(details.getNoFileChosen() != null ? details.getNoFileChosen():AuroApp.getAppContext().getResources().getString(R.string.no_file_chosen));
            kyc_one.setButtonText(details.getChooseFile() != null ? details.getChooseFile(): AuroApp.getAppContext().getResources().getString(R.string.choose_file));
            kyc_one.setDocumentName(details.getIdProofFrontSide());
            kyc_one.setDocumentFileName(details.getNoFileChosen());
            kyc_one.setButtonText(details.getChooseFile());


            if (dashboardResModel != null) {
                kyc_one.setModify(dashboardResModel.isModify());
            }
            kyc_one.setId_name(AppConstant.DocumentType.ID_PROOF);
            if (dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getIdfront())) {
                kyc_one.setDocumentstatus(true);
                kyc_one.setDocumentUrl(dashboardResModel.getIdfront());
            }


            KYCDocumentDatamodel kyc_two = new KYCDocumentDatamodel();
            kyc_two.setDocumentId(AppConstant.DocumentType.ID_PROOF_BACK_SIDE);
            kyc_two.setDocumentName(details.getAadhar_back() != null ? details.getAadhar_back()  : AuroApp.getAppContext().getResources().getString(R.string.aadhar_back));
            kyc_two.setDocumentFileName(details.getNoFileChosen() != null ? details.getNoFileChosen():AuroApp.getAppContext().getResources().getString(R.string.no_file_chosen));
            kyc_two.setButtonText(details.getChooseFile() != null ? details.getChooseFile(): AuroApp.getAppContext().getResources().getString(R.string.choose_file));
            kyc_two.setDocumentName(details.getIdProofBackSide());
            kyc_two.setDocumentFileName(details.getNoFileChosen());
            kyc_two.setButtonText(details.getChooseFile());
            if (dashboardResModel != null) {
                kyc_two.setModify(dashboardResModel.isModify());
            }
            kyc_two.setId_name(AppConstant.DocumentType.ID_PROOF_BACK);
            if (dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getIdback())) {
                kyc_two.setDocumentstatus(true);
                kyc_two.setDocumentUrl(dashboardResModel.getIdback());
            }

            KYCDocumentDatamodel kyc_three = new KYCDocumentDatamodel();
            kyc_three.setDocumentId(AppConstant.DocumentType.SCHOOL_ID_CARD);
            kyc_three.setDocumentName(details.getSchool_ids() != null ? details.getSchool_ids() : AuroApp.getAppContext().getResources().getString(R.string.school_ids));
            kyc_three.setDocumentFileName(details.getNoFileChosen() != null ? details.getNoFileChosen():AuroApp.getAppContext().getResources().getString(R.string.no_file_chosen));
            kyc_three.setButtonText(details.getChooseFile() != null ? details.getChooseFile(): AuroApp.getAppContext().getResources().getString(R.string.choose_file));
            kyc_three.setDocumentName(details.getSchoolIdCard());
            kyc_three.setDocumentFileName(details.getNoFileChosen());
            kyc_three.setButtonText(details.getChooseFile());
            if (dashboardResModel != null) {
                kyc_three.setModify(dashboardResModel.isModify());
            }
            kyc_three.setId_name(AppConstant.DocumentType.SCHOOL_ID);
            if (dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getSchoolid())) {
                kyc_three.setDocumentstatus(true);
                kyc_three.setDocumentUrl(dashboardResModel.getSchoolid());
            }

            KYCDocumentDatamodel kyc_four = new KYCDocumentDatamodel();
            kyc_four.setDocumentId(AppConstant.DocumentType.UPLOAD_YOUR_PHOTO);
            kyc_four.setDocumentName(details.getUpload_your_photo() != null ? details.getUpload_your_photo() :AuroApp.getAppContext().getResources().getString(R.string.upload_profile_pic));
            kyc_four.setDocumentFileName(details.getNoFileChosen() != null ? details.getNoFileChosen():AuroApp.getAppContext().getResources().getString(R.string.no_file_chosen));
            kyc_four.setButtonText(details.getChooseFile() != null ? details.getChooseFile(): AuroApp.getAppContext().getResources().getString(R.string.choose_file));
            kyc_four.setDocumentName(details.getUploadProfilePic());
            kyc_four.setDocumentFileName(details.getNoFileChosen());
            kyc_four.setButtonText(details.getChooseFile());
            if (dashboardResModel != null) {
                kyc_four.setModify(dashboardResModel.isModify());
            }
            kyc_four.setId_name(AppConstant.DocumentType.STUDENT_PHOTO);
            if (dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getPhoto())) {
                kyc_four.setDocumentstatus(true);
                kyc_four.setDocumentUrl(dashboardResModel.getPhoto());
            }

            kycDocumentList.add(kyc_one);
            kycDocumentList.add(kyc_two);
            kycDocumentList.add(kyc_three);
            kycDocumentList.add(kyc_four);
        } catch (Exception e) {
            AppLogger.e("makeAdapterDocumentList", e.getMessage());
        }

        return kycDocumentList;

    }


    public ArrayList<KYCDocumentDatamodel> makeAdapterDocumentList(StudentKycStatusResModel studentKycStatusResModel) {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        ArrayList<KYCDocumentDatamodel> kycDocumentList = new ArrayList<>();
        KYCDocumentDatamodel kyc_one = new KYCDocumentDatamodel();
        kyc_one.setDocumentId(AppConstant.DocumentType.ID_PROOF_FRONT_SIDE);
        kyc_one.setDocumentName(details.getAadhar_front_side() != null ?details.getAadhar_front_side() : AuroApp.getAppContext().getResources().getString(R.string.upload_govt_id));
        kyc_one.setDocumentFileName(details.getNoFileChosen() != null ? details.getNoFileChosen():AuroApp.getAppContext().getResources().getString(R.string.no_file_chosen));
        kyc_one.setButtonText(details.getChooseFile() != null ? details.getChooseFile(): AuroApp.getAppContext().getResources().getString(R.string.choose_file));


        kyc_one.setDocumentDesc(details.getSupport_jpg() != null ? details.getSupport_jpg() : AuroApp.getAppContext().getResources().getString(R.string.support_jpg));
        kyc_one.setViewType(AppConstant.FriendsLeaderBoard.STUDENT_DOC_ADAPTER);
        kyc_one.setId_name(AppConstant.DocumentType.ID_PROOF);
        kyc_one.setStudentKycDocResModel(studentKycStatusResModel.getKycDocsData().get(0));
        kyc_one.setPosition(0);


        KYCDocumentDatamodel kyc_two = new KYCDocumentDatamodel();
        kyc_two.setDocumentId(AppConstant.DocumentType.ID_PROOF_BACK_SIDE);
        kyc_two.setDocumentName(details.getAadhar_back() != null ? details.getAadhar_back()  : AuroApp.getAppContext().getResources().getString(R.string.aadhar_back));
        kyc_two.setDocumentFileName(details.getNoFileChosen() != null ? details.getNoFileChosen():AuroApp.getAppContext().getResources().getString(R.string.no_file_chosen));
        kyc_two.setButtonText(details.getChooseFile() != null ? details.getChooseFile(): AuroApp.getAppContext().getResources().getString(R.string.choose_file));


        kyc_two.setDocumentDesc(details.getSupport_jpg() != null ? details.getSupport_jpg() : AuroApp.getAppContext().getResources().getString(R.string.support_jpg));
        kyc_two.setViewType(AppConstant.FriendsLeaderBoard.STUDENT_DOC_ADAPTER);
        kyc_two.setId_name(AppConstant.DocumentType.ID_PROOF_BACK);
        kyc_two.setStudentKycDocResModel(studentKycStatusResModel.getKycDocsData().get(1));
        kyc_two.setPosition(1);

        KYCDocumentDatamodel kyc_three = new KYCDocumentDatamodel();
        kyc_three.setDocumentId(AppConstant.DocumentType.SCHOOL_ID_CARD);
        kyc_three.setDocumentName(details.getSchool_ids() != null ? details.getSchool_ids() : AuroApp.getAppContext().getResources().getString(R.string.school_ids));
        kyc_three.setDocumentFileName(details.getNoFileChosen() != null ? details.getNoFileChosen():AuroApp.getAppContext().getResources().getString(R.string.no_file_chosen));
        kyc_three.setButtonText(details.getChooseFile() != null ? details.getChooseFile(): AuroApp.getAppContext().getResources().getString(R.string.choose_file));


        kyc_three.setDocumentDesc(details.getSupport_jpg() != null ? details.getSupport_jpg() : AuroApp.getAppContext().getResources().getString(R.string.support_jpg));


        kyc_three.setViewType(AppConstant.FriendsLeaderBoard.STUDENT_DOC_ADAPTER);
        kyc_three.setId_name(AppConstant.DocumentType.SCHOOL_ID);
        kyc_three.setStudentKycDocResModel(studentKycStatusResModel.getKycDocsData().get(2));
        kyc_three.setPosition(2);

        KYCDocumentDatamodel kyc_four = new KYCDocumentDatamodel();
        kyc_four.setDocumentId(AppConstant.DocumentType.UPLOAD_YOUR_PHOTO);
        kyc_four.setDocumentName(details.getUpload_your_photo() != null ? details.getUpload_your_photo() :AuroApp.getAppContext().getResources().getString(R.string.upload_profile_pic));
        kyc_four.setDocumentFileName(details.getNoFileChosen() != null ? details.getNoFileChosen():AuroApp.getAppContext().getResources().getString(R.string.no_file_chosen));
        kyc_four.setDocumentDesc(details.getSupport_jpg() != null ? details.getSupport_jpg() : AuroApp.getAppContext().getResources().getString(R.string.support_jpg));
        kyc_four.setViewType(AppConstant.FriendsLeaderBoard.STUDENT_DOC_ADAPTER);
        kyc_four.setButtonText(details.getChooseFile());
        kyc_four.setId_name(AppConstant.DocumentType.STUDENT_PHOTO);
        kyc_four.setStudentKycDocResModel(studentKycStatusResModel.getKycDocsData().get(3));
        kyc_four.setPosition(3);

        if (AppUtil.myClassRoomResModel != null && AppUtil.myClassRoomResModel.getTeacherResModel() != null) {
            MyClassRoomTeacherResModel model = AppUtil.myClassRoomResModel.getTeacherResModel();
            if (!TextUtil.isEmpty(model.getGovt_id_front())) {
                kyc_one.setModify(true);
            }

            if (!TextUtil.isEmpty(model.getGovt_id_back())) {
                kyc_two.setModify(true);
            }


            if (!TextUtil.isEmpty(model.getSchool_id_card())) {
                kyc_three.setModify(true);
            }

            if (!TextUtil.isEmpty(model.getSchool_id_card())) {
                kyc_four.setModify(true);
            }
        }
        kycDocumentList.add(kyc_one);
        kycDocumentList.add(kyc_two);
        kycDocumentList.add(kyc_three);
        kycDocumentList.add(kyc_four);
        return kycDocumentList;
    }


    public boolean checkUploadButtonStatus(ArrayList<KYCDocumentDatamodel> list) {
        return list.get(0).isDocumentstatus() && list.get(1).isDocumentstatus() &&
                list.get(2).isDocumentstatus() && list.get(3).isDocumentstatus();
    }

    public boolean checkUploadButtonDoc(ArrayList<KYCDocumentDatamodel> list) {
        String noFileChosen = AuroApp.getAppContext().getString(R.string.no_file_chosen);
        return !list.get(0).getDocumentFileName().equalsIgnoreCase(noFileChosen) || !list.get(1).getDocumentFileName().equalsIgnoreCase(noFileChosen) ||
                !list.get(2).getDocumentFileName().equalsIgnoreCase(noFileChosen) || !list.get(3).getDocumentFileName().equalsIgnoreCase(noFileChosen);

    }


    public AssignmentReqModel getAssignmentRequestModel(DashboardResModel dashboardResModel, QuizResModel quizResModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        ;
        AssignmentReqModel assignmentReqModel = new AssignmentReqModel();
        assignmentReqModel.setExam_name(String.valueOf(quizResModel.getNumber()));
        assignmentReqModel.setQuiz_attempt(String.valueOf((quizResModel.getAttempt() + 1)));
        assignmentReqModel.setRegistration_id(dashboardResModel.getAuroid());
        assignmentReqModel.setSubject(quizResModel.getSubjectName());
        assignmentReqModel.setUserId(prefModel.getStudentData().getUserId());
        if (ViewUtil.getLanguage().equalsIgnoreCase(AppConstant.LANGUAGE_EN)) {
            assignmentReqModel.setExamlang("E");
        } else {
            assignmentReqModel.setExamlang("H");
        }
        return assignmentReqModel;
    }


    public ValidationModel validateDemographic(GetStudentUpdateProfile demographicResModel) {


        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        if (demographicResModel.getGender().equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_GENDER)) {
            return new ValidationModel(false, details.getPlease_select_gender());
        }
        if (demographicResModel.getSchoolType().equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_SCHOOL)) {
            return new ValidationModel(false, details.getPlease_select_school());
        }
        if (demographicResModel.getBoardType().equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_BOARD)) {
            return new ValidationModel(false,details.getPlease_select_board());
        }
        if (demographicResModel.getLanguage().equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_LANGUAGE_MEDIUM)) {
            return new ValidationModel(false, details.getPlease_select_language_medium());
        }

        if (demographicResModel.getIsPrivateTution().equalsIgnoreCase(AppConstant.DocumentType.YES)) {
            if (demographicResModel.getPrivateTutionType().equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_PRIVATE_TUTION) || TextUtil.isEmpty(demographicResModel.getPrivateTutionType())) {
                return new ValidationModel(false, details.getPlease_select_tution_type());
            }
        }


        return new ValidationModel(true, "");

    }


    public ValidationModel validateStudentProfile(GetStudentUpdateProfile demographicResModel) {

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        Details details = prefModel.getLanguageMasterDynamic().getDetails();
        if (demographicResModel.getStudentName().equalsIgnoreCase("Guest") || TextUtil.isEmpty(demographicResModel.getStudentName())) {
            return new ValidationModel(false,details.getPlease_enter_your_name());
        }
      /*  if (TextUtil.isEmpty(demographicResModel.getEmailID())) {
            return new ValidationModel(false, AppConstant.SpinnerType.PLEASE_ENTER_EMAIL_ID);
        }*/
        if (demographicResModel.getGender().equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_GENDER)) {
            return new ValidationModel(false,details.getPlease_select_gender());
        }
        if (demographicResModel.getSchoolType().equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_SCHOOL)) {
            return new ValidationModel(false,details.getPlease_select_school());
        }
        if (demographicResModel.getBoardType().equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_BOARD)) {
            return new ValidationModel(false,details.getPlease_select_board());
        }
        if (demographicResModel.getLanguage().equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_LANGUAGE_MEDIUM)) {
            return new ValidationModel(false,details.getPlease_select_language_medium());
        }
        if (demographicResModel.getIsPrivateTution().equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_THE_PRIVATE_TUTION)) {
            return new ValidationModel(false,details.getPlease_select_private_tution());
        }
        if (demographicResModel.getIsPrivateTution().equalsIgnoreCase(AppConstant.DocumentType.YES)) {
            if (demographicResModel.getPrivateTutionType().equalsIgnoreCase(AppConstant.SpinnerType.PLEASE_SELECT_PRIVATE_TUTION) || TextUtil.isEmpty(demographicResModel.getPrivateTutionType())) {
                return new ValidationModel(false,details.getPlease_select_tution_type());
            }
        }
/*        if (demographicResModel.getStateCode().equalsIgnoreCase("")) {
            return new ValidationModel(false, AppConstant.SpinnerType.PLEASE_ENTER_VALID_STATE);
        }
        if (demographicResModel.getDistricts().equalsIgnoreCase("")) {
            return new ValidationModel(false, AppConstant.SpinnerType.PLEASE_ENTER_VALID_DISTRICT);
        }*/

        if (TextUtil.isEmpty(demographicResModel.getStateId())) {
            return new ValidationModel(false, "Please select  the state");
        }
        if (TextUtil.isEmpty(demographicResModel.getDistrictId())) {
            return new ValidationModel(false, "Please select  the state city");
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!TextUtil.isEmpty(demographicResModel.getEmailId()) && !TextUtil.isValidEmail(demographicResModel.getEmailId())) {
            return new ValidationModel(false, AppConstant.SpinnerType.PLEASE_ENTER_VALID_EMAIL);
        }


        return new ValidationModel(true, "");

    }

    public boolean checkKycStatus(DashboardResModel dashboardResModel) {
        return dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getPhoto()) && !TextUtil.isEmpty(dashboardResModel.getSchoolid()) &&
                !TextUtil.isEmpty(dashboardResModel.getIdback()) && !TextUtil.isEmpty(dashboardResModel.getIdfront());
    }

    public boolean checkDemographicStatus(DashboardResModel dashboardResModel) {
        return dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getGender()) && !TextUtil.isEmpty(dashboardResModel.getSchool_type()) &&
                !TextUtil.isEmpty(dashboardResModel.getBoard_type()) && !TextUtil.isEmpty(dashboardResModel.getLanguage()) && !TextUtil.isEmpty(dashboardResModel.getIsPrivateTution());
    }


    public int getQuizWonCount(List<QuizResModel> list) {
        int count = 0;
        for (QuizResModel resModel : list) {
            if (resModel.getScorepoints() > 7) {
                count = count + 1;
            }
        }
        for (int i = 0; i < count; i++) {
            list.get(i).setWonStatus(true);
        }

        return count;
    }


    public List<FriendsLeaderBoardModel> makeListForFriendsLeaderBoard(boolean status) {
        List<FriendsLeaderBoardModel> list = new ArrayList<>();
        FriendsLeaderBoardModel leaderBoardModel_01 = new FriendsLeaderBoardModel();
        leaderBoardModel_01.setScholarshipWon("1000");
        leaderBoardModel_01.setStudentName("Manish");
        leaderBoardModel_01.setStudentScore("90%");
        leaderBoardModel_01.setImagePath("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcS3nRQYJ_9X8Z3LS-yOMwUNU7YGXXTB6SbEHcSqrgAnM7EoCqh_&usqp=CAU");
        if (status) {
            leaderBoardModel_01.setViewType(AppConstant.FriendsLeaderBoard.LEADERBOARD_TYPE);
        } else {
            leaderBoardModel_01.setViewType(AppConstant.FriendsLeaderBoard.LEADERBOARD_INVITE_TYPE);
        }
        list.add(leaderBoardModel_01);


        FriendsLeaderBoardModel leaderBoardModel_02 = new FriendsLeaderBoardModel();
        leaderBoardModel_02.setScholarshipWon("900");
        leaderBoardModel_02.setStudentName("Kuldip");
        leaderBoardModel_02.setStudentScore("80%");
        leaderBoardModel_02.setImagePath("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTHJi1kXOjoXE_tAA1jT6kjvukiCuH3g8q1BYe2apkTQfppxUBN&usqp=CAU");

        if (status) {
            leaderBoardModel_02.setViewType(AppConstant.FriendsLeaderBoard.LEADERBOARD_TYPE);
        } else {
            leaderBoardModel_02.setViewType(AppConstant.FriendsLeaderBoard.LEADERBOARD_INVITE_TYPE);
        }
        list.add(leaderBoardModel_02);

        FriendsLeaderBoardModel leaderBoardModel_03 = new FriendsLeaderBoardModel();
        leaderBoardModel_03.setScholarshipWon("600");
        leaderBoardModel_03.setStudentName("Rajat");
        leaderBoardModel_03.setStudentScore("60%");
        leaderBoardModel_03.setImagePath("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQ-gpVj4yzkUuIi9vRM34su4nqucJrCsVx19sjDHqBXVUYvt5x2&usqp=CAU");
        if (status) {
            leaderBoardModel_03.setViewType(AppConstant.FriendsLeaderBoard.LEADERBOARD_TYPE);
        } else {
            leaderBoardModel_03.setViewType(AppConstant.FriendsLeaderBoard.LEADERBOARD_INVITE_TYPE);
        }
        list.add(leaderBoardModel_03);


        FriendsLeaderBoardModel leaderBoardModel_04 = new FriendsLeaderBoardModel();
        leaderBoardModel_04.setScholarshipWon("500");
        leaderBoardModel_04.setStudentName("Aakash");
        leaderBoardModel_04.setStudentScore("50%");
        leaderBoardModel_04.setImagePath("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQufQ4DogWSfi5pDnsXkUh0cXNX2O0Q0kFGkaM5YTl34eB-87pP&usqp=CAU");
        if (status) {
            leaderBoardModel_04.setViewType(AppConstant.FriendsLeaderBoard.LEADERBOARD_TYPE);
        } else {
            leaderBoardModel_04.setViewType(AppConstant.FriendsLeaderBoard.LEADERBOARD_INVITE_TYPE);
        }
        list.add(leaderBoardModel_04);

        return list;
    }


    public void getTextFromImage(Activity activity, Bitmap bitmap, KYCInputModel kycInputModel, boolean status) {
        TextRecognizer txtRecognizer = new TextRecognizer.Builder(activity).build();
        if (!txtRecognizer.isOperational()) {
            // Shows if your Google Play services is not up to date or OCR is not supported for the device
            AppLogger.e("TAG", "Detector dependencies are not yet available");
        } else {
            // Set the bitmap taken to the frame to perform OCR Operations.
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray items = txtRecognizer.detect(frame);
            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < items.size(); i++) {
                TextBlock item = (TextBlock) items.valueAt(i);
                strBuilder.append(item.getValue());
                strBuilder.append("\n");
                // The following Process is used to show how to use lines & elements as well
                findNameFromAadharCard(items, item, kycInputModel);
            }

            AppLogger.e("TAG" + " Final String ", strBuilder.toString());
            if (status) {
                kycInputModel.setAadhar_phone(extractPhoneNumber(strBuilder.toString()));
                kycInputModel.setAadhar_dob(getDOB(strBuilder.toString()));
            } else {
                kycInputModel.setSchool_phone(extractPhoneNumber(strBuilder.toString()));
                kycInputModel.setSchool_dob(getDOB(strBuilder.toString()));
            }
            kycInputModel.setAadhar_no(validateAadharNumber(strBuilder.toString()));

        }
    }


    public String validateAadharNumber(String aadharNumber) {
        String aadharDigit = "";
        String[] items = aadharNumber.split("\n");
        for (String str : items) {
            str.replaceAll("\\s", "");
            String numberOnly = str.replaceAll("[^0-9]", "");
            if (numberOnly.length() == 12) {
                aadharDigit = numberOnly;
                AppLogger.e("KYCFragment", "Aadhar number == " + numberOnly);
            }
        }
        return aadharDigit;
    }

    public String extractPhoneNumber(String input) {
        List<String> list = new ArrayList<>();
        String phoneNumber = "";
        Iterator<PhoneNumberMatch> existsPhone = PhoneNumberUtil.getInstance().findNumbers(input, "IN").iterator();
        while (existsPhone.hasNext()) {
            phoneNumber = existsPhone.next().number().toString().replaceAll("[^0-9]", "");
            list.add(phoneNumber);
        }
        return phoneNumber;
    }


    public void findNameFromAadharCard(SparseArray items, TextBlock item, KYCInputModel kycInputModel) {
        String name = "";
        for (int j = 0; j < items.size(); j++) {
            for (int k = 0; k < item.getComponents().size(); k++) {
                //extract scanned text lines here
                Text line = item.getComponents().get(k);
                AppLogger.e("KYCFragment" + " lines", line.getValue());

                String lineString = line.getValue().toLowerCase();
                if (lineString.contains("name")) {
                    String[] names = lineString.split(":");
                    name = names[1];
                    AppLogger.e("KYCFragment", "name here:" + name);
                    if (!name.isEmpty()) {
                        kycInputModel.setAadhar_name(name);
                    }
                } else {
                    if (lineString.contains("dob")) {
                        Text dobline = item.getComponents().get(k - 1);
                        name = dobline.getValue();
                        AppLogger.e("KYCFragment", "dob name here:" + name);
                        if (!name.isEmpty()) {
                            kycInputModel.setAadhar_name(name);
                        }
                    }
                }

            }
        }

        AppLogger.e("KYCFragment", "Final name here:" + name);
    }

    public String getDOB(String desc) {
        ArrayList<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d").matcher(desc);
        while (m.find()) {
            allMatches.add(m.group());
        }

        Matcher match_second = Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(desc);
        while (m.find()) {
            AppLogger.e("KYCFragment", "Date == " + match_second.group(0));
            allMatches.add(match_second.group(0));
        }
        if (TextUtil.checkListIsEmpty(allMatches)) {
            return "";
        } else {
            return allMatches.get(0);
        }

    }

    public boolean checkAllUploadedOrNot(List<KYCResItemModel> list) {
        if (TextUtil.checkListIsEmpty(list)) {
            return false;
        }
        int count = 0;
        for (KYCResItemModel resItemModel : list) {
            if (!TextUtil.isEmpty(resItemModel.getUrl())) {
                count++;
            }
        }
        return count == 4;
    }

    public List<MonthlyScholarShipModel> makeListForMonthlyScholarShip() {

        List<MonthlyScholarShipModel> list = new ArrayList<>();
        MonthlyScholarShipModel month1 = new MonthlyScholarShipModel();
        month1.setMonthly("March ScholarShip");
        month1.setMoney("₹150");
        month1.setApproved("Approved");
        month1.setViewType(AppConstant.FriendsLeaderBoard.TRANSACTIONS_ADAPTER);
        list.add(month1);


        MonthlyScholarShipModel month2 = new MonthlyScholarShipModel();
        month2.setMonthly("December ScholarShip");
        month2.setMoney("₹150");
        month2.setApproved("");
        month2.setViewType(AppConstant.FriendsLeaderBoard.TRANSACTIONS_ADAPTER);
        list.add(month2);
        return list;
    }


    public String getWalletBalance(DashboardResModel resModel) {
        if (resModel != null && !TextUtil.isEmpty(resModel.getApproved_scholarship_money()) && !TextUtil.isEmpty(resModel.getUnapproved_scholarship_money())) {
            int approved = ConversionUtil.INSTANCE.convertStringToInteger(resModel.getApproved_scholarship_money());
            int inprogressAmount = ConversionUtil.INSTANCE.convertStringToInteger(resModel.getUnapproved_scholarship_money());
            int totalAmount = approved + inprogressAmount;
            return String.valueOf(totalAmount);
        }
        return "0";
    }

    public String getCurrentMonthWalletBalance(DashboardResModel resModel) {
        int walletBalance = 0;
        if (resModel != null && !TextUtil.checkListIsEmpty(resModel.getSubjectResModelList())) {
            AppLogger.e("walletBalance A-", "A");
            for (SubjectResModel subjectResModel : resModel.getSubjectResModelList()) {
                AppLogger.e("walletBalance B-", subjectResModel.getSubject());
                if (subjectResModel != null && !TextUtil.checkListIsEmpty(subjectResModel.getChapter())) {
                    for (QuizResModel model : subjectResModel.getChapter()) {
                        AppLogger.e("walletBalance C-", model.getName());
                        if (model.getAttempt() > 0) {
                            if (!TextUtil.isEmpty(model.getScoreallpoints())) {
                                AppLogger.e("walletBalance 1-", model.getScoreallpoints());
                                String[] scoreArray = model.getScoreallpoints().split(",");
                                if (scoreArray.length > 0) {
                                    for (String val : scoreArray) {
                                        AppLogger.e("walletBalance 2-", val);
                                        int tmpval = ConversionUtil.INSTANCE.convertStringToInteger(val);
                                        if (tmpval > 7) {
                                            AppLogger.e("walletBalance 3-", "" + tmpval);
                                            walletBalance = ++walletBalance;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        AppLogger.e("walletBalance 4-", "" + walletBalance);
        // walletBalance =
        return "" + (walletBalance * 50);
    }

    public boolean checkAllQuizAreFinishedOrNot(DashboardResModel dashboardResModel) {
        int totalAttempt = 0;
        for (SubjectResModel subjectResModel : dashboardResModel.getSubjectResModelList()) {
            for (QuizResModel quizResModel : subjectResModel.getChapter()) {
                totalAttempt = quizResModel.getAttempt() + totalAttempt;
            }
        }
        return totalAttempt == 60;

    }

    public List<QuizTestDataModel> makeDummyList() {
        List<QuizTestDataModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                list.add(makeQuizTestModel("Maths", 50));
            }
            if (i == 1) {
                list.add(makeQuizTestModel("Science", 70));
            }
            if (i == 2) {
                list.add(makeQuizTestModel("English", 10));
            }
            if (i == 3) {
                list.add(makeQuizTestModel("Physics", 30));
            }
            if (i == 4) {
                list.add(makeQuizTestModel("Social Science", 60));
            }
        }
        return list;
    }

    private QuizTestDataModel makeQuizTestModel(String subjectName, int percentage) {
        QuizTestDataModel quizTestDataModel = new QuizTestDataModel();
        quizTestDataModel.setSubject(subjectName);
        quizTestDataModel.setScorePercentage(percentage);
        quizTestDataModel.setChapter(makeChapterList());
        return quizTestDataModel;
    }

    private List<ChapterResModel> makeChapterList() {
        List<ChapterResModel> chapterResModelList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                chapterResModelList.add(chapterModel(1, 1, 50, "Polynomials", 6));
            }
            if (i == 1) {
                chapterResModelList.add(chapterModel(2, 2, 50, "Natural Numbers", 10));
            }
            if (i == 2) {
                chapterResModelList.add(chapterModel(2, 3, 50, "Prime Numbers", 6));
            }
            if (i == 3) {
                chapterResModelList.add(chapterModel(3, 4, 50, "Odd Numbers", 10));
            }
        }
        return chapterResModelList;
    }

    private ChapterResModel chapterModel(int attmept, int quizNumer, int amount, String name, int points) {
        ChapterResModel chapterResModel = new ChapterResModel();
        chapterResModel.setAttempt(attmept);
        chapterResModel.setNumber(quizNumer);
        chapterResModel.setScholarshipamount(amount);
        chapterResModel.setName(name);
        chapterResModel.setTotalpoints(points);
        return chapterResModel;
    }

    public List<CertificateResModel> makeCertificateList() {
        List<CertificateResModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CertificateResModel certificateResModel = new CertificateResModel();
            certificateResModel.setPdfPath("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf");
            certificateResModel.setSelect(false);
            list.add(certificateResModel);
        }
        return list;
    }

}

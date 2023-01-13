package com.auro.application.core.common;


public interface AppConstant {

    String SUCCESS = "SUCCESS";
    String ERROR_MSG = "message";
    String PREF_OBJECT = "PREF_OBJECT";
    String PACKAGE_WHATSAPP="com.whatsapp";


    String DEVICE_TYPE = "DEVICE_TYPE";
    String PLATFORM = "PLATFORM";
    String LANGUAGE = "LANGUAGE";
    String PLATFORM_ANDROID = "ANDROID";
    String DEVICE_ID = "DEVICE_ID";
    String AUTH_TOKEN = "AUTH_TOKEN";
    String MODIFIED_TIME = "MODIFIED_TIME";
    String CATEGORY_ID = "faqCategoryId";
    String OUTLET_MODEL = "OUTLET_MODEL";
    String AURO_ID = "AURO3VE4j7";
    String PARTNER_AURO_ID = "AURO3VE4j7";
    String SECRET_ID = "Secret-Id";
    String SECRET_KEY = "Secret-Key";
    String LANG_CODE = "Lang-Code";
    String LANGUAGE_ENG = "English";
    String SECRET_KEY_TOKEN = "f7115915ae4efc1bdab7ae9fc686348848f8cc2e7bf4a9";
    String SECRETE_ID_TOKEN = "2198f0011288666d3694ccf4e7d16c29";
    String USER_PROFILE_DATA_MODEL = "USER_PROFILE_DATA_MODEL";
    String USER_PROFILE_DATA_MODEL_STEP_1 = "USER_PROFILE_DATA_MODEL_STEP_1";

    String REGISTRATION_SOURCE = "Auroscholar";
    String VIV0 = "vivo";
    String SCHOOL_ID = "school_id";

    int SIGN_IN = 100;
    int SIGN_UP = 101;
    int FORGOT_PIN = 102;
    int UPDATE_MOBILE = 103;
    int UPDATE_EMAIL = 104;

    int MEMORY_REQUIRED = 219866200;

    String TITLE_NAME = "TITLE_NAME";
    String LOCATION_DATA = "location_data";

    int DEFAULT_MIN_HOUR = 1;
    int DEFAULT_MIN_MIN = 1;
    int DEFAULT_MAX_HOUR = 24;
    int DEFAULT_MAX_MIN = 60;
    int LEFT_TO_RIGHT = 1;
    int RIGHT_TO_LEFT = 2;
    int NEITHER_LEFT_NOR_RIGHT = 3;
    int TOP_TO_DOWN = 4;
    String QUIZ_ATTEMPTED = "Attempted";

    int TERMS_CONDITION_TEXT = 109;
    int PRIVACY_POLICY_TEXT = 110;
    int SELECT_PROFILE_ACTIVITY = 111;
    String CONTENT_TYPE = "Content-Type";
    String APPLICATION_JSON = "application/json";
    String WEB_LINK = "WEB_LINK";

    // String MOBILE_NUMBER = "MOBILE_NUMBER";
    String TRUE = "true";
    String FALSE = "false";
    String PROFILE_IMAGE_PATH = "PROFILE_IMAGE_PATH";
    String DASHBOARD_RES_MODEL = "DASHBOARD_RES_MODEL";
    String PARTNERS_RES_MODEL = "PARTNERS_RES_MODEL";
    String QUIZ_RES_MODEL = "QUIZ_TEST_MODEL";
    String AURO_DATA_MODEL = "AURO_DATA_MODEL";
    String MOBILE_NUMBER = "mobile_no";
    String USER_FIRSTNAME = "username";
    String EMAIL_ID = "email";
    String TEACHER_EMAIL_ID = "email_id";
    String GRADE = "grade";
    String GENDER = "gender";
    String SCHOOL_TYPE = "school_type";
    String BOARD_TYPE = "board_type";
    String LANGUAGE_MEDIUM = "language";
    String SCHOLAR_SHIP_AMOUNT = "scholarship_amount";
    String STUDENT_PROFILE_UPLOAD = "user_profile_image";
    String TEACHER_PROFILE_UPLOAD = "teacher_profile_pic";

    String MOBILE_MODEL = "mobile_model";
    String MOBILE_MANUFACTURER = "mobile_manufacturer";
    String MOBILE_VERSION = "mobile_version";
    String COUNTRY_ID = "country_id";


    int CAMERA_REQUEST_CODE = 007;
    String FAILED = "failed";
    String LANGUAGE_EN = "en";
    String LANGUAGE_HI = "hi";
    String ENGLISH = "English";
    String HINDI = "हिंदी";


    String COMING_FROM = "COMING_FROM";
    String FROM_SET_PASSWORD = "FROM_SET_PASSWORD";
    String FROM_FORGOT_PASSWORD = "FROM_FORGOT_PASSWORD";

    String PUSH_NOTIFICATION = "pushNotification";
    int NOTIFICATION_ID = 100;
    int NOTIFICATION_ID_BIG_IMAGE = 101;


    interface DashBoardParams {
        String SCHOLAR_ID = "scholr_id";
        String STUDENT_CLASS = "student_class";
        String REGISTRATION_SOURCE = "regitration_source";
        String SHARE_TYPE = "share_type";
        String SHARE_IDENTITY = "share_identity";
        String IS_KYC_UPLOADED = "is_kyc_uploaded";
        String IS_KYC_VERIFIED = "is_kyc_verified";
        String IS_PAYMENT_LASTMONTH = "is_payment_lastmonth";
        String IS_EMAIL_VERIFIED = "email_verified";
        String PARTNER_SOURCE = "partner_source";
        String REFERRER_MOBILE = "referrer_mobile";
        String DEVICE_TOKEN = "device_token";
        String IP_ADDRESS = "ip_address";
        String RESGISTRATION_ID = "registration_id";
        String STUDENT_NAME = "student_name";
        String BUILD_VERSION = "build_version";
        String LANGUAGE = "language";
        /*New Api Params*/
        String USER_ID = "user_id";
        String EXAM_MONTH = "exam_month";
        String MODULES = "modules";
        String USER_PARTNER_ID = "user_partner_id";
        String SCHOOL_NAME = "school_name";
        String PREFERED_LANGUAGE = "prefered_language";
        String DATE_OF_BIRTH = "date_of_birth";
        String LANGUAGE_VERSION = "language_version";
        String API_VERSION = "api_version";
        String REGISTRATION_SOURCE_NEW = "registration_source";
        String INSTRUCTIONS_CODE = "instruction_code";
        String IS_AGREE = "is_agree";

    }


    interface TeacherProfileParams {
        String MOBILE_NUMBER = "mobile_no";
        String TEACHER_NAME = "teacher_name";
        String TEACHER_EMAIL = "teacher_email";
        String SCHOOL_NAME = "school_name";
        String STATE_ID = "state_id";
        String DISTRICT_ID = "district_id";
        String TEACHER_CLASS = "teacher_class";
        String TEACHER_SUBJECT = "teacher_subject";
        String USER_NAME = "user_name";
    }

    interface ParentProfileParams {
        String USER_ID = "user_id";
        String FULL_NAME = "full_name";
        String STATE_ID = "state_id";
        String DISTRICT_ID = "district_id";
        String GENDER = "gender";
        String EMAILDID = "email_id";

    }

    interface ResponseConstatnt {
        int RES_200 = 200;
        int RES_400 = 400;
        int RES_401 = 401;
        int RES_404 = 404;
        int RES_FAIL = -1;

    }

    interface FragmentTransition {
        int LEFT_TO_RIGHT = 1;
        int RIGHT_TO_LEFT = 2;
        int NEITHER_LEFT_NOR_RIGHT = 3;
        int TOP_TO_DOWN = 4;
        int DOWN_TO_TOP = 5;

    }


    interface PermissionCode {
        int LOCATION_PERMISSION_CODE = 783;
        int SEARCH_COUNTRY_REQUEST = 200;
        int STORAGE_PERMISSION_CODE = 784;
        int CAMERA_VIDEO_PERMISSION_CODE = 785;
        int READ_CONTACT_PERMISSION_CODE = 786;
        int REQUEST_CHECK_SETTINGS_GPS = 0x1;
        int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
        int REQUEST_ACTION_SETTINGS_GPS = 777;
        int CREDENTIAL_PICKER_REQUEST = 1;
        int SMS_CONSENT_REQUEST = 2;

    }


    interface Source {
        String NEW_USER_DASHBOARD = "NEW_USER_DASHBOARD";
        String DASHBOARD_NAVIGATION = "DASHBOARD_NAVIGATION";
        String QUIZ_DASHBOARD_FRAGMENT = "QUIZ_DASHBOARD_FRAGMENT";
        String TEACHER_APP_AURO = "TEACHER_APP_AURO";
    }

    interface userTypeLogin {

        String SELECT_USERTYPE = "Please Select the UserType";
        String SELECTED = "";

        int TEACHER = 2;
        int STUDENT = 0;


        String API_PARAM_STUDENT = "0";
        String API_PARAM_TEACHER = "1";

    }

    interface classLogin {
        String SELECT_CLASS_TYPE = "Please Select Your Class";
        String SELECTED = "";
    }

    String GOOGLE_CLIENT_ID = "20523426983-tvknq7eb3v3n7dsnophoki68ftk01g8c.apps.googleusercontent.com";


    interface SendOtpRequest {
        String PHONENUMBER = "mobile_no";
        String OTPVERIFY = "otp_val";
        String EMAILID = "email_id";
        String STUDENT_CLASS = "student_class";
        String USER_TYPE = "user_type";
    }

    interface DateFormats {

        String DD_MM_YY = "dd-MM-yy";  // 01-11-2019
        String DD_MM_YYYY = "dd-MM-yyyy";
        String YYYY_MM_DD = "yyyy-MM-dd";
        String DD_MMM = "dd-MMM";
        String DD_MMMM_YYYY = "dd MMMM yyyy"; // 02 August 2019
        String dd_MMM_yyyy = "dd MMM, yyyy"; // 02 August 2019
        String DD_MM_YY_HH_MM = "dd-MM-yy HH:mm";  // 01-11-2019
        String DD_MMM_HH_MM_AA = "dd MMM, hh:mm aa";
        String YYYYMM = "yyyymm";
    }

    interface DocumentType {
        int ID_PROOF_BACK_SIDE = 783;
        int ID_PROOF_FRONT_SIDE = 784;
        int SCHOOL_ID_CARD = 785;
        int UPLOAD_YOUR_PHOTO = 786;

        String ID_PROOF = "id_proof";
        String ID_PROOF_BACK = "id_proof_back";
        String SCHOOL_ID = "school_id_card";
        String STUDENT_PHOTO = "student_photo";

        String GOVT_ID_FRONT = "govt_id_front";
        String GOVT_ID_BACK = "govt_id_back";
        String TEACHER_PHOTO = "teacher_photo";

        String AADHAR_PHONE = "aadhar_phone";
        String AADHAR_NO = "aadhar_no";
        String AADHAR_DOB = "aadhar_dob";
        String AADHAR_NAME = "aadhar_name";
        String SCHOOL_PHONE = "school_phone";
        String SCHOOL_DOB = "school_dob";

        String YES = "Yes";
        String NO = "No";
        String IN_PROCESS = "Inprocess";
        String REJECTED = "Rejected";
        String APPROVE = "Approve";
        String PENDING = "Pending";
        String DISAPPROVE = "disapprove";
    }


    interface DemographicType {
        String GENDER = "gender";
        String SCHOOL_TYPE = "school_type";
        String BOARD_TYPE = "board_type";
        String LANGUAGE = "language";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String RADIUS = "radius";
        String MOBILE_MODEL = "mobile_model";
        String MOBILE_MANUFACTURER = "mobile_manufacturer";
        String MOBILE_VERSION = "mobile_version";
        String IS_PRIVATE_TUTION = "is_private_tution";
        String PRIVATE_TUTION_TYPE = "private_tution_type";
        String STATE_ID = "state_id";
        String DISTRICT_ID = "district_id";
    }

    interface FragmentType {
        int FRIENDS_LEADER_BOARD = 1;
        int QUIZ_DASHBOARD = 0;

    }

    interface FriendsLeaderBoard {
        int LEADERBOARD_TYPE = 100;
        int LEADERBOARD_INVITE_TYPE = 101;
        int TRANSACTIONS_ADAPTER = 102;
        String QUIZ_DASHBOARD_WEB = "QUIZ_DASHBOARD_WEB";
        int TEACHER_DOC_ADAPTER = 103;
        int SELECTMESSAGEADAPTER = 104;
        int SUBJECTADAPTER = 105;
        int CLASSESADAPTER = 106;
        int STUDENT_DOC_ADAPTER = 107;

    }

    interface SpinnerType {
        String PLEASE_SELECT_GENDER = "Please Select Gender";
        String PLEASE_SELECT_LANGUAGE_MEDIUM = "Please Select Language Medium";
        String PLEASE_SELECT_BOARD = "Please Select Board";
        String PLEASE_SELECT_SCHOOL = "Please Select School";
        String PLEASE_SELECT_PRIVATE_TUTION = "Please Select Tution Type";
        String PLEASE_ENTER_EMAIL_ID = "Please Enter Your EmailId";
        String PLEASE_ENTER_YOUR_NAME = "Please Enter Your Name";
        String PLEASE_SELECT_THE_PRIVATE_TUTION = "Please Select Private Tution";
        String PLEASE_ENTER_VALID_EMAIL = "Please Enter Valid Email";
        String PLEASE_ENTER_VALID_STATE = "Please Enter Valid State";
        String PLEASE_ENTER_VALID_DISTRICT = "Please Enter Valid District ";

    }


    interface AssignmentApiParams {
        String REGISTRATION_ID = "registration_id";
        String EXAM_NAME = "exam_name";
        String QUIZ_ATTEMPT = "quiz_attempt";
        String EXAMLANG = "examlang";
        String EKLAVYA_EXAM_ID = "eklavvya_exam_id";
        String EXAM_FACE_IMAGE = "exam_face_img";
        String IS_MOBILE = "is_mobile";
        String IMG_NORMAL_PATH = "img_normal_path";
        String IMG_PATH = "img_path";
        String QUIZ_ID = "quiz_id";
        String SUBJECT = "subject";
        String QUESTION_SERIAL_NUMBER = "QuestionSerialNo";
        String EXAM_ASSIGNMENT_ID = "ExamAssignmentID";
        String COMPLETE_BY = "complete_by";
        String ANSWER_ID = "AnswerID";
        String QUESTION_ID = "QuestionID";
        String EXAM_ID = "exam_id";
    }
/*
    interface AssignmentApiParams {
        String REGISTRATION_ID = "registration_id";
        String EXAM_NAME = "exam_name";
        String QUIZ_ATTEMPT = "quiz_attempt";
        String EXAMLANG = "examlang";
        String SUBJECT = "subject";
        String EXAM_ASSIGNMENT_ID="ExamAssignmentID";
        String ANSWER_ID="AnswerID";
        String QUESTION_ID="QuestionID";
        String QUESTION_SERIAL_NUMBER="QuestionSerialNo";
        String EXAM_FACE_IMAGE="exam_face_img";
        String EKLAVYA_EXAM_ID="eklavvya_exam_id";
        String COMPLETE_BY="complete_by";
    }*/

    interface AzureApiParams {
        String REGISTRATION_ID = "registration_id";
        String EXAM_NAME = "exam_name";
        String QUIZ_ATTEMPT = "quiz_attempt";
        String EKLAVVYA_EXAM_ID = "eklavvya_exam_id";
        String EXAM_FACE_IMG = "exam_face_img";
        String SUBJECT = "subject";
        String EXAM_ID = "exam_id";
    }


    interface SdkType {
        int TEACHER_SDK = 100;
        int STUDENT_SDK = 101;
    }

    interface SENDING_DATA {
        String STUDENT_DATA = "STUDENT_DATA";
        String APPOINTMENT_DATA = "APPOINTMENT_DATA";
        String CERTIFICATE_DATA = "CERTIFICATE_DATA";
        String DYNAMIC_LINK = "DYNAMIC_LINK";
        String NORMAL_LOGIN = "NORMAL_LOGIN";
        String STUDENT_PROFILE = "STUDENT_PROFILE";
        String CLASSROOM_RESPONSE_MODEL = "CLASSROOM_RESPONSE_MODEL";
    }

    interface SdkFragmentType {
        int TEACHER_DASHBOARD_FRAGMENT = 100;
        int TEACHER_PROFILE_FRAGMENT = 102;
        int TEACHER_KYC_FRAGMENT = 103;
    }

    interface CLickType {
        int SUBJECT_CLICK = 100;
        int CLASS_CLICK = 101;
        int SEND_MESSAGE_CLICK = 102;
        int DOCUMENT_CLICK = 103;
        int MESSAGE_SELECT_CLICK = 104;
    }


    interface SendInviteNotificationApiParam {
        String SENDER_MOBILE_NUMBER = "sender_mobile_no";
        String RECEIVER_MOBILE_NUMBER = "receiver_mobile_no";
        String NOTIFICATION_TITLE = "notification_title";
        String NOTIFICATION_MESSAGE = "notification_message";
        String CHALLENGE_BY = "challenge_by";
        String CHALLENGE_TO = "challenge_to";
    }


    interface paytmApiParam {
        String RECEIVER_MOBILE_NUMBER = "mobile_no";
        String RECEIVER_DISBURSEMENT_MONTH = "disbursement_month";
        String RECEIVER_DISBURSEMENT = "disbursement";
        String RECEIVER_PURPOSE = "purpose";
        String BENEFICIARY_MOBILE_NUMBER = "beneficiarymobileno";
        String BENEFICIARY_NAME = "beneficiaryname";

        String STUDENT_MOBILE_NUM = "StudentMobileNo";
        String STUDENT_ID = "StudentID";
        String PAYMENT_MODE = "PaymentMode";
        String MONTH = "Month";
        String BENEFICIARY_MOBILE_NUM = "beneficiaryMobileNo";
        String BENEFICIARY_NAME_NEW = "beneficiaryName";
        String BANK_ACCOUNT_NUM = "BankAccountNo";
        String IFSC_CODE = "IfscCode";
        String UPI_ADDRESS = "UPIAddress";
        String AMOUNT = "Amount";
        String PURPOSE = "purpose";
        String PARTNER_SOURCE = "PartnerSource";
        String USERID = "UserID";
        //purpose
    }

    interface paytmAccountTransferParam {
        String RECEIVER_MOBILE_NUMBER = "mobile_no";
        String RECEIVER_DISBURSEMENT_MONTH = "disbursement_month";
        String RECEIVER_DISBURSEMENT = "disbursement";
        String RECEIVER_PURPOSE = "purpose";
        String RECEIVER_BANKACCOUNT_NO = "bankaccountno";
        String RECEIVER_IFSCCODE = "ifsccode";
    }

    interface paytmUPITransfer {
        String RECEIVER_MOBILE_NUMBER = "mobile_no";
        String RECEIVER_UPI_ADDRESS = "upiaddress";
        String RECEIVER_DISBURSEMENT_MONTH = "disbursement_month";
        String RECEIVER_DISBURSEMENT = "disbursement";
        String RECEIVER_PURPOSE = "purpose";
    }

    interface ifscCode {
        String EMPTYSTRING = "Please Enter IFSC Code";
        String VALISDIFSCCODE = "Please Enter valid IFSC Code";
        String VALID = "";
    }

    interface bankAccountNumber {
        String EMPTYSTRING = "Please Enter Bank Account Number";
        String VALIDACCOUNTNUMBER = "Please Enter Valid Account Number";
        String VALIDCONFIRMACCOUNTNUMBER = "Plase Enter Valid Confirm Account Number";
        String VALID = "";
        String BANKACCOUNTMATCH = "Account Number Mismatch";
        String BENEFICIARY_NAME = "Please Enter Beneficiary Name";
    }

    interface phoneNumber {
        String EMPTYSTRING = "Please Enter Phone Number";
        String VALIDPHONENUMBER = "Plase Enter Valid Phone Number";
        String VALID = "";
    }

    interface PaytmResponseCode {
        String DE_002 = "DE_002";
    }

    interface RefferalApiCode {
        String REFFERAL_MOBILENO = "refferal_mobileno";
        String REFFER_MOBILENO = "reffer_mobileno";
        String SOURCE = "source";
        String NAVIGATION_TO = "navigation_to";
        String REFFER_TYPE = "reffer_type";
        String REFFER_USER_ID = "reffer_user_id";
    }

    interface NavigateToScreen {
        String STUDENT_DASHBOARD = "STUDENT_DASHBOARD";
        String FRIENDS_LEADERBOARD = "STUDENT_FRIEND_LEADERBOARD";
        String TEACHER_DASHBOARD = "TEACHER_DASHBOARD";
        String STUDENT_KYC = "STUDENT_KYC";
        String PAYMENT_TRANSFER = "STUDENT_TRANSFER_MONEY";
        String STUDENT_CERTIFICATE = "STUDENT_CERTIFICATE";
        String OPEN_SUBJECT = "OPEN_SUBJECT";
        String QUIZ_DIRECT_OPEN = "QUIZ_DIRECT_OPEN";
        String STUDENT_PROFILE = "STUDENT_PROFILE";
        String TEACHER = "teacher";
        String STUDENT = "student";

    }

    interface SendRefferalApiCode {
        String REFERRED_BY_ID = "referred_by_id";
        String REFERRED_USER_ID = "referred_user_id";
        String REFERRED_BY_PHONE = "referred_by_phone";
        String REFERRED_USER_MOBILE = "referred_user_mobile";
        String MESSAGE = "message";
        String SUCCESS = "success";
        String REFERRED_BY_TYPE = "referred_by_type";
    }

    interface PaymenMode {
        String WALLET = "wallet";
        String UPI = "upi";
        String BANK_Transfer = "bank";

    }

    interface UserType {
        int NOTHING = 0;
        int TEACHER = 1;
        int STUDENT = 0;
        String USER_TYPE_STUDENT = "0";
        String USER_TYPE_PARENT = "1";
    }


    interface AnayliticsEvents {
        String SPLASH_SCREEN = "Auro_Splash_Screen";
        String CHOOSE_TEACHER = "Auro_CHOOSE_TEACHER";
        String CHOOSE_STUDENT = "Auro_CHOOSE_STUDENT";
        String OTP_ASK_STUDENT = "Auro_OTP_ASK_STUDENT";
        String OTP_ASK_TEACHER = "Auro_OTP_ASK_TEACHER";
        String OTP_VERIFY_STUDENT = "Auro_OTP_VERIFY_STUDENT";
        String OTP_VERIFY_TEACHER = "Auro_OTP_VERIFY_TEACHER";
        String STUDENT_DASHBOARD = "Auro_STUDENT_DASHBOARD";
        String TEACHER_DASHBOARD = "Auro_TEACHER_DASHBOARD";
        String STUDENT_LOGOUT = "Auro_STUDENT_LOGOUT";
        String TEACHER_LOGOUT = "Auro_TEACHER_LOGOUT";
        String STUDENT_START_NEW_QUIZ = "Auro_STUDENT_START_QUIZ";
        String STUDENT_RETAKE_QUIZ = "Auro_STUDENT_RETAKE_QUIZ";
        String STUDENT_KYC_SCREEN = "Auro_STUDENT_KYC_SCREEN";
        String STUDENT_LEADER_BOARD = "Auro_STUDENT_LEADER_BOARD";
        String STUDENT_PROFILE_SCREEN = "Auro_STUDENT_PROFILE_SCREEN";
        String STUDENT_PASSPORT_SCREEN = "Auro_STUDENT_PASSPORT_SCREEN";
        String STUDENT_PARTNER_SCREEN = "Auro_STUDENT_PARTNER_SCREEN";
        String STUDENT_PROFILE_SUBMIT = "Auro_STUDENT_PROFILE_SUBMIT";
        String TEACHER_PROFILE_SUBMIT = "Auro_TEACHER_PROFILE_SUBMIT";
        String STUDENT_COMPLETE_QUIZ = "Auro_STUDENT_COMPLETE_QUIZ";
        String STUDENT_INVITE_FRIEND = "Auro_STUDENT_INVITE_FRIEND";
        String TEACHER_INVITE_FRIEND = "Auro_TEACHER_INVITE_FRIEND";
        String PARTNER_SCREEN = "Auro_PARTNER_SCREEN";
        String MINDLER_START_SCREEN = "Auro_MINDLER_START_ON_CLICK";
    }

    interface PartnersLoginApi {
        String PARTNERS_ID = "partner_id";
        String PARTNERS_NAME = "partner_name";
        String STUDENT_NAME = "student_name";
        String STUDENT_EMAIL = "email";
        String STUDENT_PASSWORD = "student_password";
        String MOBILE_NUMBER = "mobile_no";
        String STUDENT_CLASS = "class";

    }

    interface TeacherDashBoardApi {
        String TEACHER_USER_ID = "user_id";
    }

    interface UsercheckApiCode {
        String USER_NOT_FOUND = "USER_NOT_FOUND"; //code for user not registered. And not found in db
        String SR_ID_USER = "SR_ID_USER";//code for punjab registered student
        String PASS_NOT_NULL = "PASS_NOT_NULL"; //code for user registered in table and password is set
        String PASS_NULL = "PASS_NULL";//code for user registered in table and password is not null
        String JSON_FAIL = "JSON_FAIL";//code for param error if params are not in json format.
        String INVALID_METHOD = "INVALID_METHOD";// code for method error if method is not post.
        String VALIDATE_ERROR = "VALIDATE_ERROR"; //code for validation error if params is not sent.
        String SR_ID_NOT_FOUND = "SR_ID_NOT_FOUND"; //when user is not found in punjab table

        String USER_CHECK_CODE = "PRE_REGISTERED_USER"; //when user is not found in punjab table
    }

    interface CurrentFlagStatus {
        String SET_PASSWORD = "SET_PASSWORD"; //code for user not registered. And not found in db
        String SET_PROFILE_SCREEN = "SET_PROFILE_SCREEN";

    }

    interface SubjectCodes {
        String Mathematics = "MATH";
        String English = "ENGLISH";
        String Hindi = "HINDI";
        String Social_Science = "SOCIAL_SCIENCE";
        String Science = "SCIENCE";
        String Physics = "PHYSICS";
        String Chemistry = "CHEMISTRY";
        String Biology = "BIOLOGY";
        String History = "HISTORY";
        String Political_Science = "POLITICAL_SCIENCE";
        String Geography = "GEOGRAPHY";
    }

    interface ComingFromStatus {
        String SR_ID_USER = "SR_ID_USER";
        String NON_SR_ID_USER = "NON_SR_ID_USER";
        String COMING_FORM_PIN_FORGOT_PIN = "COMING_FORM_PIN_FORGOT_PIN";
        String COMING_FROM_LOGIN_WITH_OTP = "COMING_FROM_LOGIN_WITH_OTP";
        String COMING_FROM_BOTTOM_SHEET = "COMING_FROM_BOTTOM_SHEET";
        String COMING_FROM_ADD_STUDENT = "COMING_FROM_ADD_STUDENT";
        String COMING_FROM_PASSWORD_NOT_SET = "COMING_FROM_PASSWORD_NOT_SET";
        String COMING_FROM_ADD_STUDENT_STEP_1 = "COMING_FROM_ADD_STUDENT_STEP_1";
        String COMING_FROM_ADD_STUDENT_STEP_2 = "COMING_FROM_ADD_STUDENT_STEP_2";
    }

    interface ManufacturerName {
        String SAMSUNG = "SAMSUNG";
    }

    interface QuizNative {
        String IMAGEINLARGE = "IMAGEINLARGE";
    }

    interface QuizFinishStatus {
        String STUDENT = "student";
        String AUTO_SUBMITTED = "auto submitted";
        String EXIT_BY_STUDENT = "exit by student";
    }

    interface QuizTestNativeScreen {
        int QUIZOPTIONADAPTER = 107;
    }

    interface Headers {
        String SECRET_ID = "Secret-Id";
        String SECRET_KEY = "Secret-Key";
        String LANG_CODE = "Lang-Code";
        String SECRET_ID_VALUE = "2198f0011288666d3694ccf4e7d16c29";
        String SECRET_KEY_VALUE = "f7115915ae4efc1bdab7ae9fc686348848f8cc2e7bf4a9";
        String LANG_CODE_VALUE = "English";
    }

    interface TeacherKYCDocumentStatus {
        String PENDING = "PENDING";
        String APPROVE = "APPROVE";
        String DISAPPROVE = "DISAPPROVE";
        String FRONT = "FRONT";
        String BACK = "BACK";
        String ID_PROOF = "id_proof";
        String ID_PROOF_BACK = "id_proof_back";
        String SCHOOL_ID_CARD = "school_id_card";
        String TEACHER_PHOTO = "teacher_photo";
        String TEACHER_RES_MODEL_DATA = "TEACHER_RES_MODEL_DATA";
        String TEACHER_DOCUMENT_MODEL_DATA = "TEACHER_DOCUMENT_MODEL_DATA";


        int ID_PROOF_ID = 1;
        int ID_PROOF_BACK_ID = 2;
        int SCHOOL_ID_CARD_ID = 3;
        int TEACHER_PHOTO_ID = 4;
    }


    interface ParamsValue {
        String LANGUAGE_VERSION_VAL = "0.0.1";
        String API_VERSION_VAL = "0.0.1";
        String REGISTRATION_SOURCE_VAL = "AuroScholar";
        String PARTNER_SOURCE_VAL = "AURO3VE4j7";
    }

    interface Language{
        String LANGUAGE_ID = "language_id";
        String USER_TYPE_ID = "user_type_id";
        String USER_PREFERED_LANGUAGE = "user_prefered_language_id";
    }

    interface  InstructionsType
    {
        String KYC="KYC";
        String QUIZ="QUIZ";
        String AFTER_LOGIN="AFTER_LOGIN";
        String SCHOLARSHIP_TRANSFER="SCHOLARSHIP_TRANSFER";
    }
}

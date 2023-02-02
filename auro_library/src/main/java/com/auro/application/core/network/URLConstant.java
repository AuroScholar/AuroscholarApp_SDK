package com.auro.application.core.network;

import com.auro.application.BuildConfig;

public interface URLConstant {
    //  String BASE_URL = BuildConfig.BASE_URL; // live
   // String BASE_URL = "https://app.auroscholar.org/api/";
   String BASE_URL = "https://staging.auroscholar.com/api/";
   // String BASE_URL = "192.168.0.2/auroscholar-product/api/"
    String OTP_SEND_API = BASE_URL + "send_otp";
    String OTP_VERIFY = BASE_URL + "verify_otp_new";
    String VERSION_API = BASE_URL + "check_api_version.php";
    String USER_CHECK = BASE_URL + "user_check_new";
    String GRADE_UPGRADE = BASE_URL + "grade_update";
    String DASHBOARD_SDK_API = BASE_URL + "student_full_details";
    String DEMOGRAPHIC_API = BASE_URL + "update_user_details";
    String AZURE_API

            = BASE_URL + "save_pre_quiz_image_l";
    String UPLOAD_IMAGE_URL = BASE_URL + "upload_kyc_documents_ocr";
    String GET_ASSIGNMENT_ID = BASE_URL + "start_quiz";
    String GET_STATE = BASE_URL + "states";
    String GET_DISTRICT = BASE_URL + "districts";

    String TEST_URL = "https://assessment.eklavvya.com/exam/StartExam?";
    String PRIVACY_POLICY = "https://auroscholar.com/privacy_policy";
    String STUDENT_SUBJECT_PREFERENCE_API = BASE_URL + "fetch_subject_list";
    String FETCH_STUDENT_PREFERENCE_API = BASE_URL + "fetch_student_preference";
    String UPDATE_USER_PREFERENCE = BASE_URL + "update_student_preference";
    String SET_USERNAME_PIN_API = BASE_URL + "set_username_pin";
    String LOGIN_USING_PIN_API = BASE_URL + "student_pin_login";
    String STUDENT_GET_KYC_DETAILS = BASE_URL + "get_kyc_details";
    String OTP_OVER_CALL = BASE_URL + "send_voice_otp";
    String TEACHER_PROFILE_UPDATE_API = BASE_URL + "teacher_profile_update";

    String GET_PROFILE_UPDATE_API = BASE_URL + "students_score_data_api.php ";

    String GET_PROFILE_TEACHER_API = BASE_URL + "teacher_profile";

    String GET_TEACHER_PROGRESS_API = BASE_URL + "teacher_progress.php ";

    String TEACHER_KYC_API = BASE_URL + "teacher_kyc_upload";

    String INVITE_FRIEND_LIST_API = BASE_URL + "student_referral_data";

    String SEND_NOTIFICATION_API = BASE_URL + "push_notification.php";

    String GET_ZOHO_APPOINTMENT = BASE_URL + "get_zoho_slot.php";

    String BOOK_ZOHO_APPOINTMENT = BASE_URL + "book_zoho_slot.php";

    String ACCEPT_STUDENT_INVITE = BASE_URL + "student_challenge_accepted.php";

    String PAYTM_API = BASE_URL + "paytm_wallet_transfer.php";

    String PAYTM_UPI_TRANSFER = BASE_URL + "paytm_upi_transfer.php";

    String PAYTM_ACCOUNT_TRANSFRER = BASE_URL + "paytm_account_transfer.php";

    String FIND_FRIEND_API = BASE_URL + "find_friend.php";

    String SEND_FRIEND_REQUEST_API = BASE_URL + "friend_request.php";

    String FRIEND_REQUEST_LIST_API = BASE_URL + "friend_request_list.php";

    String FRIEND_ACCEPT_API = BASE_URL + "friend_accepted.php";

    String TERM_CONDITION = "https://auroscholar.com/terms_of_use";


    String REFFER_API = BASE_URL + "reffer";

    String SEND_REFERRAL_API = BASE_URL + "refferal";

    //  String CERTIFICATE_API  = BASE_URL + "certificate_data_api.php";

    String CERTIFICATE_API = BASE_URL + "gen_certificate_api";

    String NEW_PAYMENT_API = "http://auro.auroscholar.com/api/Payment/PostTransaction";

    String EXAM_IMAGE_API = BASE_URL + "exam_img_l";

    String UPDATE_STUDENT_PROFILE = BASE_URL + "update_user_details";
 String UPDATE_PARENT_PROFILE = BASE_URL + "update_parent";

    String GET_USER_PROFILE = BASE_URL + "get_user_details";

    String PASSPORT_API = BASE_URL +  "passport";    //"passport"; //student_passport_details

    String PARTNERS_API = BASE_URL + "guest_partner_list";

    String PARTNERS_LOGIN_API = BASE_URL + "partner_login_api";

    String FORGOT_PASSWORD_API = BASE_URL + "set_user_password";

    String LOGIN_API = BASE_URL + "student_login";

    //https://staging.auroscholar.org/apis/teacher/teacher_classroom.php
    String BASETEACHERURL = BASE_URL;

    String TEACHER_CLASSROOM = BASETEACHERURL + "teacher_classroom";

    String TEACHER_DASHBOARD_SUMMARY = BASETEACHERURL + "teacher_dashboard_summary";

    String TEACHER_BOOKED_SLOT = BASETEACHERURL + "teacher_booked_webinar_slots";

    String TEACHER_AVAILABLE_SLOTS_API = BASETEACHERURL + "teacher_available_webinar_slots";

    String BOOK_SLOT_API = BASETEACHERURL + "teacher_webinar_booking";

    String CANCEL_WEBINAR_AVAILABLE = BASETEACHERURL + "teacher_webinar_delete";



    String TEACHER_CREATE_GROUP_API = BASETEACHERURL + "teacher_group_add";
    String TEACHER_ADD_STUDENT_IN_GROUP_API = BASETEACHERURL + "teacher_group_student_add";


    String FETCH_QUIZ_DATA = BASE_URL + "fetch_quiz_question";


    String SAVE_QUIZ_DATA = BASE_URL + "save_quiz";

    //String FINISH_QUIZ_DATA  = BASE_URL + "submit_final_quiz_test.php";

    String FINISH_QUIZ_DATA = BASE_URL + "submit_final_quiz";//  //submit_final_quiz_test


    String REGISTER_USER_API = BASE_URL + "register_user";

    String SET_PASSWORD_API = BASE_URL + "set_user_password";

    String SET_USER_PIN_API = BASE_URL + "set_user_pin";

    String TEACHER_KYC_STATUS_API = BASE_URL + "teacher_kyc_status";

    String GET_STUDENT_WALLET_API = BASE_URL + "get_sudent_wallet";


    String LANGUAGE_LIST = BASE_URL + "languages_list";

    String LANGUAGE_DYNAMIC = BASE_URL + "get_language_master";

    String REFFERAL_API = "refferal";

    String GET_INSTRUCTIONS = "get_instructions";

    String NOTICE_INSTRUCTION = "get_extra_notes";

    String PENDING_KYC_DOCS = "pending_kyc_docs";

    String GET_MSG_POPUP =  "get_msg_popup";

    String GET_SLABS_API = BASE_URL + "get_user_slab";

}

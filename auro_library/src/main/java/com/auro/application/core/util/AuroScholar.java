package com.auro.application.core.util;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.LanguageListResModel;
import com.auro.application.home.presentation.view.activity.AppLanguageActivity;
import com.auro.application.home.presentation.view.activity.ChooseGradeActivity;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithoutPin;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.activity.SDKActivity;
import com.auro.application.home.presentation.view.activity.SplashScreenAnimationActivity;
import com.auro.application.home.presentation.view.activity.StudentMainDashboardActivity;
import com.auro.application.home.presentation.view.activity.StudentProfileActivity;
import com.auro.application.home.presentation.view.fragment.BottomSheetUsersDialog;
import com.auro.application.home.presentation.view.fragment.FriendsLeaderBoardListFragment;

import com.auro.application.home.presentation.view.fragment.MainQuizHomeFragment;
import com.auro.application.util.AppLogger;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auroscholar.final_auroscholarapp_sdk.SDKChildModel;
import com.auroscholar.final_auroscholarapp_sdk.SDKDataModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuroScholar {
    static AuroScholarDataModel auroScholarDataModel;
    static List<SDKChildModel> userDetails = new ArrayList<>();
    static List<SDKChildModel> userDetailsNew = new ArrayList<>();
    static SDKDataModel checkUserResModel;
    static String errormismatch = "";
    static LanguageMasterDynamic language;
    static LanguageListResModel languageListResModel;
    static Details languagedetail;
    static ProgressDialog progress;


    /*For generic with PhoneNumber and class*/
    public static Fragment startAuroSDK(AuroScholarInputModel inputModel) {
        auroScholarDataModel = new AuroScholarDataModel();
        auroScholarDataModel.setMobileNumber(inputModel.getMobileNumber());
        auroScholarDataModel.setStudentClass(inputModel.getStudentClass());
        auroScholarDataModel.setActivity(inputModel.getActivity());
        auroScholarDataModel.setFragmentContainerUiId(R.id.home_container);
        auroScholarDataModel.setUserPartnerid(inputModel.getPartner_unique_id());
        auroScholarDataModel.setPartnerSource(inputModel.getPartnerSource());
        auroScholarDataModel.setApikey(inputModel.getPartner_api_key());
        DaggerWrapper.getComponent(inputModel.getActivity()).doInjection(inputModel.getActivity());
       AuroApp.setAuroModel(auroScholarDataModel);
       getMultiLanguage();
        getLanguage("1");
        setSDKAPI(inputModel.getMobileNumber(),inputModel.getPartner_unique_id(),inputModel.getPartnerSource(),inputModel.getPartner_api_key(),inputModel.getStudentClass());

        return null;
    }

    private static void setSDKAPI(String mobno, String partneruniqueid, String partnersource, String apikey, String grade)
    {
        progress = new ProgressDialog(auroScholarDataModel.getActivity());
        progress.setTitle("Processing..");
        progress.setMessage("fetching data");
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();
        HashMap<String,String> map_data = new HashMap<>();

            map_data.put("mobile_no",mobno);
            map_data.put("partner_unique_id",partneruniqueid);
            map_data.put("partner_source",partnersource);
            map_data.put("partner_api_key",apikey);
            RemoteApi.Companion.invoke().getSDKData(map_data)
                    .enqueue(new Callback<SDKDataModel>() {
                        @Override
                        public void onResponse(Call<SDKDataModel> call, Response<SDKDataModel> response)
                        {
                            try {
                                if (response.code() == 400) {
                                    JSONObject jsonObject = null;
                                    try {
                                        progress.dismiss();
                                        jsonObject = new JSONObject(response.errorBody().string());
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(auroScholarDataModel.getActivity(), message, Toast.LENGTH_SHORT).show();


                                    } catch (JSONException | IOException e) {
                                        progress.dismiss();
                                        e.printStackTrace();
                                    }


                                }
                                else if (response.code() == 200) {
                                    progress.dismiss();
                                    userDetails.clear();
                                    userDetailsNew.clear();
                                    userDetails = response.body().getUser_details();
                                    checkUserResModel = (SDKDataModel) response.body();
                                    for (int i = 0; i<userDetails.size(); i++){
                                        userDetailsNew.add(userDetails.get(i));

                                    }
                                    PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                    prefModel.setChildData(checkUserResModel);
                                    prefModel.setPartnersource(partnersource);
                                    prefModel.setUserMobile(mobno);
                                    prefModel.setPartneruniqueid(partneruniqueid);
                                    prefModel.setApikey(apikey);
                                    AuroAppPref.INSTANCE.setPref(prefModel);
                                    if (userDetails.size() ==1 && (userDetails.get(0).is_mapped() == 1||userDetails.get(0).is_mapped().equals(1)||userDetails.get(0).is_mapped().equals("1"))){

                                        String userid_child =  userDetails.get(0).getUser_id();
                                        String user_mobile =  userDetails.get(0).getMobile_no();
                                        String student_name =  userDetails.get(0).getStudent_name();
                                        String user_language =  userDetails.get(0).getUser_prefered_language_id();
                                        int user_grade =  userDetails.get(0).getGrade();
                                        String user_kyc =  userDetails.get(0).getKyc_status();
                                        String user_name = userDetails.get(0).getUser_name();
                                        String partner_logo =  userDetails.get(0).getPartner_logo();
                                        String profile_pic = userDetails.get(0).getProfile_pic();
                                        PrefModel prefModel2 = AuroAppPref.INSTANCE.getModelInstance();
                                        prefModel2.setUserId(userid_child);
                                        prefModel2.setUserMobile(user_mobile);
                                        prefModel2.setUserLanguageId(user_language);
                                        prefModel2.setStudentName(student_name);
                                        prefModel2.setUserclass(user_grade);
                                        prefModel2.setKycstatus(user_kyc);
                                        prefModel2.setUserprofilepic(profile_pic);
                                        prefModel2.setUserName(user_name);
                                        prefModel2.setPartner_logo(partner_logo);
                                        AuroAppPref.INSTANCE.setPref(prefModel2);
                                        getProfile(userid_child,user_language);
                                    }
                                    else{

                                        progress.dismiss();
                                        openBottomSheetDialog();
                                    }
                                }
                                else {
                                    progress.dismiss();
                                    Toast.makeText(auroScholarDataModel.getActivity(), response.message(), Toast.LENGTH_SHORT).show();

                                }
                            }
                            catch (Exception e) {
                                progress.dismiss();
                                Toast.makeText(auroScholarDataModel.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                        @Override
                        public void onFailure(Call<SDKDataModel> call, Throwable t) {
                            progress.dismiss();
                            Toast.makeText(auroScholarDataModel.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });


    }

    public static void opendashboard(){
        auroScholarDataModel.getActivity().startActivity(new Intent(auroScholarDataModel.getActivity(), DashBoardMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }


    public static void openBottomSheetDialog() {
        BottomSheetUsersDialog bottomSheet = new BottomSheetUsersDialog();
        bottomSheet.show(((FragmentActivity)auroScholarDataModel.getActivity()).getSupportFragmentManager(),
                "ModalBottomSheet");
    }

    private static void getProfile(String userid, String lang_id)
    {
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);
        RemoteApi.Companion.invoke().getStudentData(map_data)
                .enqueue(new Callback<GetStudentUpdateProfile>()
                {
                    @Override
                    public void onResponse(Call<GetStudentUpdateProfile> call, Response<GetStudentUpdateProfile> response)
                    {
                        if (response.isSuccessful())
                        {

                            if (lang_id==null||lang_id.equals("null")||lang_id.equals(null)||lang_id.equals("0")||lang_id=="0"||lang_id.equals("")||lang_id.isEmpty()){
//                                Intent i = new Intent(this, AppLanguageActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(i);
                                progress.dismiss();
                                auroScholarDataModel.getActivity().startActivity(new Intent(auroScholarDataModel.getActivity(), AppLanguageActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }
                            else if (errormismatch.equals("Error! Grade Mismatched")){
//                                Intent i = new Intent(SDKActivity.this, ChooseGradeActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(i);
                               progress.dismiss();
                                auroScholarDataModel.getActivity().startActivity(new Intent(auroScholarDataModel.getActivity(), ChooseGradeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                            }

                            else if (response.body().getStatename().equals("")||response.body().getStatename().equals("null")||response.body().getStatename().equals(null)||response.body().getDistrictname().equals("")||response.body().getDistrictname().equals("null")||response.body().getDistrictname().equals(null)||response.body().getStudentName().equals("")||response.body().getStudentName().equals("null")||response.body().getStudentName().equals(null)||
                                    response.body().getStudentclass().equals("")||response.body().getStudentclass().equals("null")||response.body().getStudentclass().equals(null)||response.body().getStudentclass().equals("0")||response.body().getStudentclass().equals(0)){
//                                Intent i = new Intent(SDKActivity.this, CompleteStudentProfileWithoutPin.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(i);
                               progress.dismiss();
                                auroScholarDataModel.getActivity().startActivity(new Intent(auroScholarDataModel.getActivity(), CompleteStudentProfileWithoutPin.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                            }
                            else{
                                progress.dismiss();
                                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                int userclass = prefModel.getUserclass();
                                AuroScholarInputModel inputModel = new AuroScholarInputModel();
                                inputModel.setMobileNumber(prefModel.getUserMobile());
                                inputModel.setStudentClass(String.valueOf(userclass));
                                inputModel.setPartner_unique_id(prefModel.getPartneruniqueid());
                                inputModel.setPartnerSource(prefModel.getPartnersource());
                                inputModel.setPartner_api_key(prefModel.getApikey());
                                auroScholarDataModel.getActivity().startActivity(new Intent(auroScholarDataModel.getActivity(), DashBoardMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<GetStudentUpdateProfile> call, Throwable t)
                    {

                    }
                });
    }

    public static void getLanguage(String langid)
    {
        HashMap<String,String> map_data = new HashMap<>();
        if (!langid.isEmpty()|| !langid.equals("")){
            map_data.put("language_id",langid);
        }
        else{
            map_data.put("language_id","1");
        }
        map_data.put("user_type_id","1");
        RemoteApi.Companion.invoke().getLanguageAPI(map_data)
                .enqueue(new Callback<LanguageMasterDynamic>() {
                    @Override
                    public void onResponse(Call<LanguageMasterDynamic> call, Response<LanguageMasterDynamic> response)
                    {
                        try {
                            if (response.code() == 400) {
                                progress.dismiss();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");


                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if (response.code() == 200) {
                                progress.dismiss();
                                languagedetail = response.body().getDetails();
                                language = (LanguageMasterDynamic) response.body();
                                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                prefModel.setLanguageMasterDynamic(language);
                                AuroAppPref.INSTANCE.setPref(prefModel);

                            }
                            else {
                            }
                        }
                        catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(Call<LanguageMasterDynamic> call, Throwable t) {
                    }

                });
    }
    public static void getMultiLanguage()
    {
        RemoteApi.Companion.invoke().getLanguageAPIList()
                .enqueue(new Callback<LanguageListResModel>() {
                    @Override
                    public void onResponse(Call<LanguageListResModel> call, Response<LanguageListResModel> response)
                    {
                        try {
                            if (response.code() == 400) {
                                progress.dismiss();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");


                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }


                            }
                            else if (response.code() == 200) {
                                 progress.dismiss();
                                languageListResModel = (LanguageListResModel) response.body();
                                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                prefModel.setLanguageListResModel(languageListResModel);
                                AuroAppPref.INSTANCE.setPref(prefModel);

                            }
                            else {
                                Toast.makeText(auroScholarDataModel.getActivity(), response.message(), Toast.LENGTH_SHORT).show();

                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(auroScholarDataModel.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LanguageListResModel> call, Throwable t) {
                    }

                });
    }

}

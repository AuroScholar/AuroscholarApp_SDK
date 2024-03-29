package com.auro.application.home.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.home.data.base_component.BaseActivity;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.BottomStudentListBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.presentation.view.adapter.SelectYourParentChildAdapter;
import com.auro.application.util.RemoteApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChildAccountsActivity extends BaseActivity implements CommonCallBackListner {
    BottomStudentListBinding binding;
    PrefModel prefModel;
    RecyclerView studentList;
    List<UserDetailResModel> listchilds = new ArrayList<>();
    List<UserDetailResModel> list = new ArrayList<>();
    List<UserDetailResModel> listuserdetails = new ArrayList<>();
    CheckUserResModel checkUserResModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.viewchildaccount_activity);
      //  binding.setLifecycleOwner(this);
        studentList = findViewById(R.id.studentList);
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        studentList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
       studentList.setHasFixedSize(true);

        String username = prefModel.getCheckUserResModel().getUserDetails().get(0).getUserName();
        getAddedChild(username);
//        checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel();
//        setAdapterAllListStudent(checkUserResModel.getUserDetails());
    }

    public void setAdapterAllListStudent(List<UserDetailResModel> totalStudentList) {
        List<UserDetailResModel> list = new ArrayList<>();

        for (UserDetailResModel resmodel : totalStudentList) {
            if (resmodel.getIsMaster().equalsIgnoreCase("0") ) {
                if (resmodel.getGrade().equals(0)||resmodel.getGrade().equals("0")){

                }
                else{
                    list.add(resmodel);
                }

            }

        }










    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case CLICK_PARENTPROFILE:
                Intent i = new Intent(this, ParentProfileActivity.class);
                i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
                startActivity(i);
                break;

            case CLICK_CHILDPROFILE:
                Intent i1 = new Intent(this, DashBoardMainActivity.class);
                i1.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
                startActivity(i1);
                break;
        }

    }


    @Override
    protected void init() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected int getLayout() {
        return R.layout.viewchildaccount_activity;
    }

//    private void getAddedChild()
//    {
//        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
//
//        ProgressDialog progress = new ProgressDialog(ChildAccountsActivity.this);
//        progress.setTitle(details.getProcessing());
//        progress.setMessage(details.getProcessing());
//        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
//        progress.show();
//        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
//        String parentuserid = prefs.getString("parentuserid", "");
//        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
//        String user_id = prefModel.getCheckUserResModel().getUserDetails().get(0).getUserId();
//        HashMap<String,String> map_data = new HashMap<>();
//        map_data.put("user_id",user_id);
//        RemoteApi.Companion.invoke().getAllChild(map_data)
//                .enqueue(new Callback<GetAllChildModel>()
//                {
//                    @Override
//                    public void onResponse(Call<GetAllChildModel> call, Response<GetAllChildModel> response)
//                    {
//
//                            if (response.isSuccessful())
//                            {
//
//
//                                    listchilds = response.body().getUserDetails();
//
//                                    Log.e("childlist",response.body().getUserDetails().toString());
//                                    for (int i =0; i<listchilds.size(); i++){
//                                        // if (listchilds.get(i).getIsMaster().equals("0")||listchilds.get(i).getIsMaster().equals(0)){
//                                        list.add(listchilds.get(i));
//                                       // listuserdetails.addAll(list.get(i));
//                                       // Toast.makeText(ChildAccountsActivity.this, listuserdetails.get(i).getUserName(), Toast.LENGTH_SHORT).show();
//
//                                        // }
//                                    }
//
//                                    SelectYourParentChildAdapter studentListAdapter = new SelectYourParentChildAdapter(ChildAccountsActivity.this, list, ChildAccountsActivity.this);
//                                    studentList.setAdapter(studentListAdapter);
//                                    progress.dismiss();
//
//
//                            }
//                            else
//                            {
//                                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
//                            }
//
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<GetAllChildModel> call, Throwable t)
//                    {
//                        progress.dismiss();
//                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }


    private void getAddedChild(String user_name)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_name",user_name);
        map_data.put("user_type", String.valueOf(0));
        map_data.put("user_prefered_language_id",languageid);
        RemoteApi.Companion.invoke().getUserCheck(map_data)
                .enqueue(new Callback<CheckUserResModel>()
                {
                    @Override
                    public void onResponse(Call<CheckUserResModel> call, Response<CheckUserResModel> response)
                    {
                        try {


                            if (response.isSuccessful()) {

                                list.clear();
                                if (!(response.body().getUserDetails() == null) || !(response.body().getUserDetails().isEmpty())) {


                                    listchilds = response.body().getUserDetails();
                                    for (int i = 0; i < listchilds.size(); i++) {
                                        if (listchilds.get(i).getIsMaster().equals("0") || listchilds.get(i).getIsMaster().equals(0)) {
                                            if (listchilds.get(i).getGrade().equals(0) || listchilds.get(i).getGrade().equals("0")) {

                                            } else {
                                                list.add(listchilds.get(i));
                                            }


                                        }
                                    }
//                                checkUserResModel = (CheckUserResModel) response.body().getUserDetails();
//                                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
//                                prefModel.setCheckUserResModel(checkUserResModel);
//                                AuroAppPref.INSTANCE.setPref(prefModel);
                                    SelectYourParentChildAdapter studentListAdapter = new SelectYourParentChildAdapter(ChildAccountsActivity.this, list, ChildAccountsActivity.this);
                                    studentList.setAdapter(studentListAdapter);
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ChildAccountsActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserResModel> call, Throwable t)
                    {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
          super.onBackPressed();
      Intent intent = new Intent(ChildAccountsActivity.this, ParentProfileActivity.class);
      startActivity(intent);
      finish();


    }
}





















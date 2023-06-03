package com.auro.application.home.presentation.view.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.home.data.base_component.BaseFragment;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.FragmentCertificateBinding;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.passportmodels.PassportQuizMonthModel;
import com.auro.application.home.data.model.passportmodels.PassportQuizTopicModel;
import com.auro.application.home.data.model.passportmodels.PassportSubjectQuizMonthModel;
import com.auro.application.home.data.model.passportmodels.PassportTopicQuizMonthModel;
import com.auro.application.home.data.model.response.APIcertificate;
import com.auro.application.home.data.model.response.CertificateResModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.adapter.CertificateAdapter;
import com.auro.application.home.presentation.viewmodel.TransactionsViewModel;
import com.auro.application.payment.presentation.view.fragment.SendMoneyFragment;

import com.auro.application.teacher.data.model.response.PassportMonthResModel;
import com.auro.application.teacher.data.model.response.TeacherStudentPassportDetailResModel;
import com.auro.application.util.AppUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.AskNameCustomDialog;
import com.auro.application.util.alert_dialog.CertificateDialog;
import com.auro.application.util.alert_dialog.CustomDialogModel;

import com.auro.application.util.strings.AppStringDynamic;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CertificateFragment extends BaseFragment implements View.OnClickListener, CommonCallBackListner {


    @Inject
    @Named("CertificateFragment")
    ViewModelFactory viewModelFactory;
    boolean userClick = false;
    FragmentCertificateBinding binding;
    TransactionsViewModel viewModel;
    DashboardResModel dashboardResModel;
    String TAG = "CertificateFragment";
    CertificateResModel certificateResModel;
    HashMap<Integer, String> hashMap = new HashMap<>();
    private String comingFrom;
    String studentName = "";
    Details details;
    List<APIcertificate> listchilds1 = new ArrayList<>();
    List<APIcertificate> list1 = new ArrayList<>();
    List<String> monthList = new ArrayList<>();
    List<String> topicList = new ArrayList<>();
    List<String> subjectList = new ArrayList<>();
    String monthid,subjectid,topicid;
    List<PassportMonthResModel> monthlist = new ArrayList<>();
    List<PassportSubjectQuizMonthModel> subjectlist = new ArrayList<>();
    List<PassportTopicQuizMonthModel> topiclist= new ArrayList<>();
    PrefModel prefModel;

    public CertificateFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
      //  ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        DaggerWrapper.getComponent(getActivity()).doInjection(this);
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionsViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        ViewUtil.setLanguageonUi(getActivity());
        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter();
        init();
        setToolbar();
        setListener();
    }

    @Override
    protected void init() {
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.CERTIFICATE_FRAGMENT);

        if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
            comingFrom = getArguments().getString(AppConstant.COMING_FROM);

        }

        ViewUtil.setLanguageonUi(getActivity());
        //callCertificateApi();


        setListener();


       // ViewUtil.setProfilePic(binding.imageView6);
        getProfile();
        AppUtil.loadAppLogo(binding.auroScholarLogo,getActivity());
        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        AppStringDynamic.setCertificatesPageStrings(binding);
        getStudentMonth();
    }
    private void getProfile()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String userid = prefModel.getUserId();

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
                            String profilepicurl = response.body().getProfilePic();
                            Glide.with(getActivity()).load(profilepicurl)
                                    .apply(RequestOptions.placeholderOf(R.drawable.imageplaceholder_ico)
                                            .error(R.drawable.imageplaceholder_ico)
                                            .dontAnimate()
                                            .priority(Priority.IMMEDIATE)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                    ).into(binding.imageView6);
                        }
                        else
                        {
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<GetStudentUpdateProfile> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.CERTIFICATE_FRAGMENT);
        binding.languageLayout.setOnClickListener(this);
        binding.cardView2.setOnClickListener(this);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            getCertificateList("","","");

            // observeServiceResponse();
        }
        binding.monthParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userClick = true;
                binding.monthSpinner.performClick();
            }
        });
        binding.downloadIcon.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_certificate;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_arrow) {
            getActivity().onBackPressed();
        } else if (id == R.id.download_icon) {//askPermission();
        } else if (id == R.id.language_layout) {
            ((DashBoardMainActivity) getActivity()).openChangeLanguageDialog();
        } else if (id == R.id.cardView2) {

            ((DashBoardMainActivity) getActivity()).openProfileFragment();
        }
    }

    private void askPermission() {

        if (list1 != null && !TextUtil.checkListIsEmpty(certificateResModel.getStudentuseridbasedcertificate())) {
            if (hashMap.size() > 0) {
                for (Map.Entry<Integer, String> map : hashMap.entrySet()) {
                    downloadFile(map.getValue());
                }
            } else {
                ViewUtil.showSnackBar(binding.getRoot(),details.getPlease_select_certificate()!= null ? details.getPlease_select_certificate() : AuroApp.getAppContext().getString(R.string.no_certificate_for_download));
            }
        } else {
            ViewUtil.showSnackBar(binding.getRoot(), details.getNo_certificate_for_download()!= null ? details.getNo_certificate_for_download() : AuroApp.getAppContext().getResources().getString(R.string.please_select_certificate));

        }

    }

    private void downloadFile(String url) {
        if (!TextUtil.isEmpty(url)) {
            DownloadManager downloadManager = (DownloadManager) AuroApp.getAppContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
            downloadManager.enqueue(request);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getCertificateList(String monthname , String subjectid, String topicid)
    {

        HashMap<String,String> map_data = new HashMap<>();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String userid = prefModel.getUserId();
        map_data.put("UserId",userid);
        map_data.put("SubjectId",subjectid);
        if (topicid.equals("0")||topicid.equals(0)){
            map_data.put("TopicId","");
        }
        else{
            map_data.put("TopicId",topicid);
        }
        map_data.put("ExamMonth", monthname);
        RemoteApi.Companion.invoke().getCertificate(map_data)
                .enqueue(new Callback<CertificateResModel>()
                {
                    @Override
                    public void onResponse(Call<CertificateResModel> call, Response<CertificateResModel> response)
                    {
                        try {
                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            else if (response.isSuccessful()) {

                                if (response.body().getStatus().equals("success")){
                                    listchilds1.clear();
                                    list1.clear();
                                    if (!(response.body().getStudentuseridbasedcertificate() == null || response.body().getStudentuseridbasedcertificate().isEmpty() || response.body().getStudentuseridbasedcertificate().equals("") || response.body().getStudentuseridbasedcertificate().equals("null"))) {

                                        listchilds1 = response.body().getStudentuseridbasedcertificate();
                                        certificateResModel = response.body();
                                        for (int i = 0; i < listchilds1.size(); i++) {
                                            list1.add(listchilds1.get(i));
                                        }
                                        binding.certificateRecyclerView.setVisibility(View.VISIBLE);
                                        CertificateAdapter kyCuploadAdapter = new CertificateAdapter(getActivity(), list1, null);
                                        binding.certificateRecyclerView.setAdapter(kyCuploadAdapter);

                                    }
                                    else{
                                        binding.certificateRecyclerView.setVisibility(View.GONE);
                                    }
                                }

                            }

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CertificateResModel> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }
    private void getStudentMonth()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String userid = prefModel.getUserId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);
        RemoteApi.Companion.invoke().getStudentPassportMonth(map_data)
                .enqueue(new Callback<TeacherStudentPassportDetailResModel>()
                {
                    @Override
                    public void onResponse(Call<TeacherStudentPassportDetailResModel> call, Response<TeacherStudentPassportDetailResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                monthlist.clear();
                                monthList.clear();
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    monthlist.add(response.body().getData().get(i));
                                    monthList.add(response.body().getData().get(i).getDateName());

                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, monthList);
                                binding.monthSpinner.setAdapter(adapter);
                                binding.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        binding.monthTitle.setText(monthList.get(position));
                                        binding.monthTitle.setTextColor(Color.WHITE);
                                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                                        monthid = monthlist.get(position).getDate();
                                        getPassportSubject(monthid);
                                        getCertificateList(monthid,"","");
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        binding.monthSpinner.performClick();
                                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                                    }
                                });

                            }
                        }
                        catch (Exception e) {

                            Toast.makeText(getActivity(), details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<TeacherStudentPassportDetailResModel> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getPassportSubject(String monthname)
    {

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String userid = prefModel.getUserId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);
        map_data.put("month",monthname);


        RemoteApi.Companion.invoke().getQuizMonthSubject(map_data)
                .enqueue(new Callback<PassportQuizMonthModel>()
                {
                    @Override
                    public void onResponse(Call<PassportQuizMonthModel> call, Response<PassportQuizMonthModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                subjectlist.clear();
                                subjectList.clear();
                                for (int i = 0; i < response.body().getPassportSubjectModelList().size(); i++) {
                                    subjectlist.add(response.body().getPassportSubjectModelList().get(i));
                                    subjectList.add(response.body().getPassportSubjectModelList().get(i).getSubject());




                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, subjectList);
                                binding.subjectSpinner.setAdapter(adapter);


                                binding.subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        // binding.subjectSpinner.performClick();
                                        binding.subjectTitle.setText(subjectList.get(position));
                                        binding.subjectTitle.setTextColor(Color.WHITE);
                                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                                        String subjectname = subjectlist.get(position).getSubject();
                                        subjectid = subjectlist.get(position).getSubject_id();
                                        if (subjectname.equals("All")){
                                            getCertificateList(monthid,"","");
                                        }
                                        else{
                                            getCertificateList(monthid,subjectid,"");
                                        }

                                        getTopics(monthname,subjectname);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        binding.subjectSpinner.performClick();
                                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                                    }
                                });

                            } else {
                                Log.d(TAG, "onResponser: " + response.message().toString());
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PassportQuizMonthModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
    private void getTopics(String monthname , String subjectname)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageId = prefModel.getUserLanguageId();
        String subjecttext = binding.subjectTitle.getText().toString();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("month",monthname);
        map_data.put("grade","");
        map_data.put("subject",subjectname);
        map_data.put("user_prefered_language_id",languageId);

        RemoteApi.Companion.invoke().getQuizTopic(map_data)
                .enqueue(new Callback<PassportQuizTopicModel>()
                {
                    @Override
                    public void onResponse(Call<PassportQuizTopicModel> call, Response<PassportQuizTopicModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                topiclist.clear();
                                topicList.clear();
                                for (int i = 0; i < response.body().getPassportSubjectModelList().size(); i++) {
                                    topiclist.add(response.body().getPassportSubjectModelList().get(i));
                                    topicList.add(response.body().getPassportSubjectModelList().get(i).getQuiz_name());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, topicList);
                                binding.topicSpinner.setAdapter(adapter);


                                binding.topicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        binding.topicTitle.setText(topicList.get(position));
                                        binding.topicTitle.setTextColor(Color.WHITE);
                                        String topicname = binding.topicTitle.getText().toString();
                                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                                        topicid = String.valueOf(topiclist.get(position).getQuiz_id());
                                        if (topicname.equals("All")){
                                            getCertificateList(monthid,subjectid,"");
                                        }
                                        else{
                                            getCertificateList(monthid,subjectid,topicid);
                                        }


                                    }


                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        binding.topicSpinner.performClick();
                                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                                    }
                                });

                            } else {
                                Log.d(TAG, "onResponser: " + response.message().toString());
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PassportQuizTopicModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }

    public void openSendMoneyFragment() {
        Bundle bundle = new Bundle();
        SendMoneyFragment sendMoneyFragment = new SendMoneyFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        sendMoneyFragment.setArguments(bundle);
        openFragment(sendMoneyFragment);
    }

    private void openFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(AuroApp.getFragmentContainerUiId(), fragment, CertificateFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }


    public void setAdapter() {
        binding.certificateRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

        binding.certificateRecyclerView.setHasFixedSize(true);

    }

    private void openCertificateDialog(APIcertificate object) {
        CertificateDialog yesNoAlert = CertificateDialog.newInstance(object);
        yesNoAlert.show(getParentFragmentManager(), null);

    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case ITEM_CLICK:
                openCertificateDialog((APIcertificate) commonDataModel.getObject());
                break;






            case DOCUMENT_CLICK:

                APIcertificate gData = (APIcertificate) commonDataModel.getObject();
                openWhatsApp(gData.getDownloalink());


                break;

            default:

                break;
        }
    }
    private void openWhatsApp(String message) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Certificate shareable link "+message);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:

                    break;

                case SUCCESS:
                    if (isVisible()) {
                        certificateResModel = (CertificateResModel) responseApi.data;
                        if (certificateResModel.getError()) {
                            if (certificateResModel.getStatus().equalsIgnoreCase(AppConstant.DocumentType.NO)) {
                                openAskNameDialog();
                            }
                            handleProgress(3, certificateResModel.getStatus());

                        } else {
                            if (!TextUtil.checkListIsEmpty(certificateResModel.getStudentuseridbasedcertificate())) {
                                handleProgress(1, "");
                                setAdapter();
                            } else {
                                handleProgress(3, details.getNo_certificate());
                            }
                        }
                    }
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                    }
                    break;
                default:
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                    }
                    break;
            }

        });
    }


    private void handleProgress(int status, String msg) {
        switch (status) {
            case 0:
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.certificateRecyclerView.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.GONE);
                break;
            case 1:
                binding.progressBar.setVisibility(View.GONE);
                binding.certificateRecyclerView.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                break;

            case 2:
                binding.progressBar.setVisibility(View.GONE);
                binding.certificateRecyclerView.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.errorLayout.textError.setText(msg);
                binding.errorLayout.errorIcon.setVisibility(View.GONE);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //   callCertificateApi();
                    }
                });
                break;
            case 3:
                binding.progressBar.setVisibility(View.GONE);
                binding.certificateRecyclerView.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.errorLayout.textError.setText(msg);
                binding.errorLayout.errorIcon.setVisibility(View.GONE);
                binding.errorLayout.btRetry.setVisibility(View.GONE);

                break;
        }
    }

    private void callCertificateApi() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel.getDashboardResModel() != null) {
            handleProgress(0, "");
            CertificateResModel certificateResModel = new CertificateResModel();
            //    certificateResModel.setRegistrationId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
            // certificateResModel.setStudentName(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getStudentName());
            viewModel.getCertificate(certificateResModel);
        }
    }

    private void handleNavigationProgress(int status, String msg) {
        if (status == 0) {
            binding.transparet.setVisibility(View.VISIBLE);
            binding.certificate.setVisibility(View.VISIBLE);

            binding.errorConstraint.setVisibility(View.GONE);
        } else if (status == 1) {
            binding.transparet.setVisibility(View.GONE);
            binding.certificate.setVisibility(View.VISIBLE);

            binding.errorConstraint.setVisibility(View.GONE);
        } else if (status == 2) {
            binding.transparet.setVisibility(View.GONE);
            binding.certificate.setVisibility(View.GONE);
            binding.errorConstraint.setVisibility(View.VISIBLE);
            binding.errorLayout.textError.setText(msg);
            binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashBoardMainActivity) getActivity()).callDashboardApi();
                }
            });
        }
    }

    private void openAskNameDialog() {
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(getActivity());


        customDialogModel.setTitle(details.getUpdate_auro()!= null?details.getUpdate_auro(): AuroApp.getAppContext().getString(R.string.update_auroscholar));
        customDialogModel.setContent(details.getAuro_update_msg()!= null?details.getAuro_update_msg():AuroApp.getAppContext().getString(R.string.updateMessage));
        AskNameCustomDialog updateCustomDialog = new AskNameCustomDialog(getActivity(), customDialogModel, this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(updateCustomDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        updateCustomDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(updateCustomDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateCustomDialog.setCancelable(true);
        updateCustomDialog.show();
    }

}

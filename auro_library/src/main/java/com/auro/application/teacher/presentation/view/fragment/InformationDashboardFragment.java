package com.auro.application.teacher.presentation.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.FragmentInformationDashboardBinding;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.teacher.data.model.request.TeacherDasboardSummaryResModel;
import com.auro.application.teacher.data.model.request.TeacherUserIdReq;
import com.auro.application.teacher.data.model.response.StudentWalletTeacherResModel;
import com.auro.application.teacher.presentation.view.adapter.AutoScrollPagerAdapter;
import com.auro.application.teacher.presentation.view.adapter.StudentWalletInfoAdapter;
import com.auro.application.teacher.presentation.viewmodel.MyClassroomViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringTeacherDynamic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;


public class InformationDashboardFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {


    @Inject
    @Named("InformationDashboardFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "InformationDashboardFragment";
    FragmentInformationDashboardBinding binding;
    MyClassroomViewModel viewModel;
    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 1000;
    FloatingActionButton floatingActionButton;


    boolean isStateRestore;
    List<StudentWalletTeacherResModel> listUploadData;

    public InformationDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyClassroomViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setListener();
        setToolbar();
        floatingActionButton = binding.getRoot().findViewById(R.id.floatingActionButton);
       floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    @Override
    protected void init() {
        AppStringTeacherDynamic.setMyInformationStrings(binding);
        HomeActivity.setListingActiveFragment(HomeActivity.TEACHER_DASHBOARD_FRAGMENT);
        ViewUtil.setTeacherProfilePic(binding.imageView6);
        addViewPager();

    }


    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        binding.rlBookMySeat.setOnClickListener(this);
        binding.languageLayout.setOnClickListener(this);
        AppLogger.e("SummaryData", "Stem 0");

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
        callGetDashbaordSummaryData();

    }

    void callGetDashbaordSummaryData()
    {
        handleProgress(0, "");
        TeacherUserIdReq teacherUserIdReq = new TeacherUserIdReq();
        teacherUserIdReq.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        // teacherUserIdReq.setUserId("538709");
        viewModel.getTeacherDashboardSummaryData(teacherUserIdReq);

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_information_dashboard;
    }

    public void addViewPager() {
        AutoScrollPagerAdapter autoScrollPagerAdapter = new AutoScrollPagerAdapter(getActivity().getSupportFragmentManager());
        // AutoScrollViewPager viewPager = findViewById(R.id.view_pager);
        binding.viewPager.setAdapter(autoScrollPagerAdapter);

        binding.tabs.setupWithViewPager(binding.viewPager);
        // start auto scroll
        binding.viewPager.startAutoScroll();
        // set auto scroll time in mili
        binding.viewPager.setInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);
        // enable recycling using true
        binding.viewPager.setCycle(true);
    }

    public List<StudentWalletTeacherResModel> listUpload(TeacherDasboardSummaryResModel teacherDasboardl) {
        listUploadData = new ArrayList<>();
        StudentWalletTeacherResModel studentModel = new StudentWalletTeacherResModel();
        studentModel.setNameOfDocument("KYC Uploaded");
        studentModel.setTotalValue(teacherDasboardl.getKycUpload().toString());
        studentModel.setDrawable(getActivity().getResources().getDrawable(R.drawable.kyc_teacher_uploaded));
        listUploadData.add(studentModel);

        StudentWalletTeacherResModel studentModel1 = new StudentWalletTeacherResModel();
        studentModel1.setNameOfDocument("KYC Approved");
        studentModel1.setTotalValue(teacherDasboardl.getKycApproved().toString());
        studentModel1.setDrawable(getActivity().getResources().getDrawable(R.drawable.upload_card_background));
        listUploadData.add(studentModel1);


        StudentWalletTeacherResModel studentModel2 = new StudentWalletTeacherResModel();
        studentModel2.setNameOfDocument("Approved Scholarship");
        studentModel2.setTotalValue(teacherDasboardl.getWinningStudent().toString());
        studentModel2.setDrawable(getActivity().getResources().getDrawable(R.drawable.approved_scholarschip));
        listUploadData.add(studentModel2);

        StudentWalletTeacherResModel studentModel3 = new StudentWalletTeacherResModel();
        studentModel3.setNameOfDocument("Disbursed Scholarship");
        studentModel3.setDrawable(getActivity().getResources().getDrawable(R.drawable.disbursed_scholarship));
        studentModel3.setTotalValue(teacherDasboardl.getDisbursedScholarship().toString());
        listUploadData.add(studentModel3);

        return listUploadData;


    }

    public void setAdapter() {
        // List<CertificateResModel> list = viewModel.homeUseCase.makeCertificateList();
        binding.rvWalletTeachet.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.rvWalletTeachet.setHasFixedSize(true);
        StudentWalletInfoAdapter kyCuploadAdapter = new StudentWalletInfoAdapter(getActivity(), listUploadData, this);
        binding.rvWalletTeachet.setAdapter(kyCuploadAdapter);
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            AppLogger.v("observeServiceResponse", "InformationDashbaordFragment");


            switch (responseApi.status) {
                case LOADING:
                    if (isVisible()) {
                        if (!isStateRestore) {
                            handleProgress(0, "");
                        }
                    }
                    break;

                case SUCCESS:
                    AppLogger.v("InfoScreen", " step 2 ");
                    if (responseApi.apiTypeStatus == Status.TEACHER_DASBOARD_SUMMARY) {
                        AppLogger.v("InfoScreen", " step 3 ");
                        handleProgress(1, "");
                        TeacherDasboardSummaryResModel resModel = (TeacherDasboardSummaryResModel) responseApi.data;
                        AppLogger.v("InfoScreen", " step 3.1  " + resModel.getMessage());
                        setDataOnInitializeView(resModel);
                    }
                    break;

                case FAIL:
                case NO_INTERNET:
                default:
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                    }
                    break;
            }

        });
    }

    private void handleProgress(int status, String message) {
        switch (status) {
            case 0:
                binding.mainParentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressbar.pgbar.setVisibility(View.VISIBLE);
                // binding.shimmerMyClassroom.startShimmer();
                break;

            case 1:
                binding.mainParentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressbar.pgbar.setVisibility(View.GONE);
                //  binding.shimmerMyClassroom.stopShimmer();
                break;

            case 2:
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.mainParentLayout.setVisibility(View.GONE);
                binding.progressbar.pgbar.setVisibility(View.GONE);
                // binding.shimmerMyClassroom.stopShimmer();
                binding.errorLayout.textError.setText(message);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callGetDashbaordSummaryData();
                    }
                });
                break;
        }

    }

    public void setDataOnInitializeView(TeacherDasboardSummaryResModel teacherDasboardl) {
        AppLogger.e("InfoScreen -=total student-", "" + teacherDasboardl.getAllWinQuiz());
        //  AppLogger.e("InfoScreen -=total student-", "" + totalStudent);
        String totalStudent = teacherDasboardl.getTotalStudent();
        String totalWiningQuiz = teacherDasboardl.getAllWinQuiz();
        String totalTestTaken = teacherDasboardl.getTotalQuizTaken();
        String uniqueStudentTakenQuiz = teacherDasboardl.getUniqueStudentTakenQuiz();

        binding.tvTotalStudent.setText(totalStudent + "");
        binding.tvTotalWinning.setText(totalWiningQuiz + "");
        binding.Rpmarks.setText(totalTestTaken + "");
        binding.RpoptainMarks.setText(uniqueStudentTakenQuiz + "");
        listUpload(teacherDasboardl);
        setAdapter();


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rlBook_my_seat) {
            ((HomeActivity) getActivity()).openFragment(new BookSlotFragment());
        } else if (id == R.id.language_layout) {
            ((HomeActivity) getActivity()).openChangeLanguageDialog();
        }

    }



}
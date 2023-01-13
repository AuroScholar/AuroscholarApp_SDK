package com.auro.application.home.presentation.view.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.FragmentTransactionsBinding;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.ParentProfileDataModel;
import com.auro.application.home.data.model.SchoolData;
import com.auro.application.home.data.model.passportmodels.PassportMonthModel;
import com.auro.application.home.data.model.passportmodels.PassportQuizDetailModel;
import com.auro.application.home.data.model.passportmodels.PassportQuizGridModel;
import com.auro.application.home.data.model.passportmodels.PassportQuizModel;
import com.auro.application.home.data.model.passportmodels.PassportQuizMonthModel;
import com.auro.application.home.data.model.passportmodels.PassportReqModel;
import com.auro.application.home.data.model.passportmodels.PassportSubjectModel;
import com.auro.application.home.data.model.passportmodels.PassportSubjectQuizMonthModel;
import com.auro.application.home.data.model.response.GetAllReferChildDetailResModel;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithoutPin;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.ParentProfileActivity;
import com.auro.application.home.presentation.view.adapter.GradeSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.MontlyWiseAdapter;
import com.auro.application.home.presentation.view.adapter.PassportSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.ReferredlListStudentAdapter;
import com.auro.application.home.presentation.view.adapter.SubjectQuizSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.passportadapter.PassportMonthAdapter;
import com.auro.application.home.presentation.viewmodel.TransactionsViewModel;
import com.auro.application.payment.presentation.view.fragment.SendMoneyFragment;
import com.auro.application.teacher.data.model.common.MonthDataModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.DateUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionsFragment extends BaseFragment implements View.OnClickListener {
//    @Inject
//    @Named("TransactionsFragment")
//    ViewModelFactory viewModelFactory;
//    FragmentTransactionsBinding binding;
//    TransactionsViewModel viewModel;
//    MontlyWiseAdapter leaderBoardAdapter;
//    Resources resources;
//    DashboardResModel dashboardResModel;
//    List<MonthDataModel> monthDataModelList;
//    String TAG = "TransactionsFragment";
//    boolean isFirstTime = false;
//    MonthDataModel spinnerMonth;
//    MonthDataModel spinnerSubject;
//    boolean userClick = false;
//    boolean isMonthSelected = false;
//    PassportSpinnerAdapter subjectSpinner;
//    List<MonthDataModel> subjectResModelList;
//    Details details;
//    public TransactionsFragment() {
//        // Required empty public constructor
//    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
//
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        // return inflater.inflate(R.layout.fragment_transactions, container, false);
//
//        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
//        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
//        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionsViewModel.class);
//        binding.setLifecycleOwner(this);
//        setRetainInstance(true);
//        ViewUtil.setLanguageonUi(getActivity());
//        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
//        return binding.getRoot();
//    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        init();
//        setToolbar();
//        setListener();
//    }
//    @Override
//    protected void init() {
//
//        if (getArguments() != null) {
//            dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
//        }
//        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.TRANSACTION_FRAGMENT);
//
//        setListener();
//
//        monthSpinner();
//        SubjectSpinner(null, 0);
//
//        spinnerMonth = new MonthDataModel();
//        spinnerMonth.setMonthNumber(DateUtil.getcurrentMonthNumber() + 1);
//        spinnerMonth.setYear(DateUtil.getcurrentYearNumber());
//        spinnerMonth.setMonth(DateUtil.getMonthNameSpinner());
//
//        AppLogger.e(TAG, "current month-" + DateUtil.getMonthNameSpinner());
//
//
//        spinnerSubject = new MonthDataModel();
//        spinnerSubject.setMonth("All");
//
//        selectCurrentMonthInSpinner();
//        checkCallApiStatus();
//        ViewUtil.setProfilePic(binding.imageView6);
//        AppUtil.loadAppLogo(binding.auroScholarLogo,getActivity());
//        AppStringDynamic.setPassportStrings(binding);
//    }
//    private void selectCurrentMonthInSpinner() {
//        AppLogger.e(TAG, "selectCurrentMonthInSpinner step 1");
//        if (!TextUtil.checkListIsEmpty(monthDataModelList)) {
//            AppLogger.e(TAG, "selectCurrentMonthInSpinner step 2");
//            for (int i = 0; i < monthDataModelList.size(); i++) {
//
//                MonthDataModel monthDataModel = monthDataModelList.get(i);
//                AppLogger.e(TAG, "selectCurrentMonthInSpinner step 3 month -" + monthDataModel.getMonth() + "-current month-" + DateUtil.getMonthNameSpinner());
//
//                if (monthDataModel.getMonth().equalsIgnoreCase(DateUtil.getMonthNameSpinner()) || monthDataModel.getMonth().contains(DateUtil.getMonthNameSpinner())) {
//                    binding.monthSpinner.setSelection(i);
//                    binding.monthTitle.setText(monthDataModel.getMonth());
//                }
//            }
//        }
//    }
//    @Override
//    protected void setToolbar() {
//
//    }
//    @Override
//    protected void setListener() {
//        binding.cardView2.setOnClickListener(this);
//        binding.languageLayout.setOnClickListener(this);
//        binding.monthParentLayout.setOnClickListener(this);
//        binding.subjectParentLayout.setOnClickListener(this);
//        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
//            viewModel.serviceLiveData().removeObservers(this);
//        } else {
//            observeServiceResponse();
//        }
//    }
//    @Override
//    protected int getLayout() {
//        return R.layout.fragment_transactions;
//
//    }
//    public void setPassportAdapter(PassportMonthModel passportMonthModel) {
//        binding.monthlyWiseList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        binding.monthlyWiseList.setHasFixedSize(true);
//        binding.monthlyWiseList.setNestedScrollingEnabled(false);
//        PassportMonthAdapter passportSpinnerAdapter = new PassportMonthAdapter(getActivity(), makePassportList(passportMonthModel), null);
//        binding.monthlyWiseList.setAdapter(passportSpinnerAdapter);
//    }
//    private List<PassportMonthModel> makePassportList(PassportMonthModel passportResModel) {
//        AppLogger.v("Months", passportResModel.getMonthName() + "   months");
//        List<PassportMonthModel> monthModelList = new ArrayList<>();
//        passportResModel.setMonthName(binding.monthTitle.getText().toString());
//        passportResModel.setExpanded(true);
//        for (int i = 0; i < passportResModel.getPassportSubjectModelList().size(); i++) {
//
//            SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
//            int positionsubject = prefs.getInt("positionsubject", 0);
//            String existpositionsubject = prefs.getString("existpositionsubject", "");
//            String positionsubjectstatus = prefs.getString("positionsubjectstatus", "");
//            String existpositionsubjectforall = prefs.getString("existpositionsubjectforall", "");
//            if (existpositionsubject.equals("true") && !existpositionsubjectforall.equals("All")) {  //
//                PassportSubjectModel passportSubjectModel = passportResModel.getPassportSubjectModelList().get(i);
//
//
//                if (i == 0) {
//                    passportSubjectModel.setExpanded(true);
//
//
//                }
//                // AppLogger.e(TAG, "step  1-" + passportResModel.getPassportSubjectModelList().size());
//                for (PassportQuizModel quizModel : passportSubjectModel.getQuizData()) {
//                    // AppLogger.e(TAG, "step  2-" + passportSubjectModel.getQuizData().size());
//                    for (PassportQuizDetailModel detailModel : quizModel.getQuizDetail()) {
//                        //  AppLogger.e(TAG, "step  3-" + quizModel.getQuizDetail().size());
//                        List<PassportQuizGridModel> gridList = new ArrayList<>();
//                        for (int j = 0; j < 4; j++) {
//                            PassportQuizGridModel gridModel = new PassportQuizGridModel();
//                            if (j == 0) {
//                                gridModel.setQuizHead(details.getQuiz_number() != null ? details.getQuiz_number() : "Quiz No");
//                                gridModel.setQuizData(detailModel.getExamName());
//                                gridModel.setQuizImagePath(R.drawable.quiz);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.black));
//                                gridList.add(gridModel);
//                            }
//                            if (j == 1) {
//                                gridModel.setQuizHead(details.getQuiz_attempt() != null ? details.getQuiz_attempt() : "Quiz Attempt");
//                                gridModel.setQuizData(detailModel.getQuizAttempt());
//                                gridModel.setQuizImagePath(R.drawable.attempt);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.black));
//                                gridList.add(gridModel);
//                            }
//                            if (j == 2) {
//                                gridModel.setQuizHead(details.getScore() != null ? details.getScore() : "Score");
//                                gridModel.setQuizData(detailModel.getScore() + "/10");
//                                gridModel.setQuizImagePath(R.drawable.score);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.ufo_green));
//                                gridList.add(gridModel);
//                            }
//                            if (j == 3) {
//                                gridModel.setQuizHead(details.getResult_status() != null ? details.getResult_status() : "Status");
//                                gridModel.setQuizData(detailModel.getAmount_status());
//                                gridModel.setQuizImagePath(R.drawable.status);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.ufo_green));
//                                gridList.add(gridModel);
//
//
//                            }
//
//
//                        }
//                        AppLogger.e(TAG, "step  4-" + gridList.size());
//                        detailModel.setPassportQuizGridModelList(gridList);
//
//                    }
//                }
//            }
//            else if (positionsubjectstatus.equals("true") && !existpositionsubjectforall.equals("All")) {  //
//                PassportSubjectModel passportSubjectModel = passportResModel.getPassportSubjectModelList().get(positionsubject);
//                if (i == 0) {
//                    passportSubjectModel.setExpanded(true);
//                }
//                // AppLogger.e(TAG, "step  1-" + passportResModel.getPassportSubjectModelList().size());
//                for (PassportQuizModel quizModel : passportSubjectModel.getQuizData()) {
//                    // AppLogger.e(TAG, "step  2-" + passportSubjectModel.getQuizData().size());
//                    for (PassportQuizDetailModel detailModel : quizModel.getQuizDetail()) {
//                        //  AppLogger.e(TAG, "step  3-" + quizModel.getQuizDetail().size());
//                        List<PassportQuizGridModel> gridList = new ArrayList<>();
//                        for (int j = 0; j < 4; j++) {
//                            PassportQuizGridModel gridModel = new PassportQuizGridModel();
//                            if (j == 0) {
//                                gridModel.setQuizHead(details.getQuiz_number() != null ? details.getQuiz_number() : "Quiz No");
//                                gridModel.setQuizData(detailModel.getExamName());
//                                gridModel.setQuizImagePath(R.drawable.quiz);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.black));
//                                gridList.add(gridModel);
//                            }
//                            if (j == 1) {
//                                gridModel.setQuizHead(details.getQuiz_attempt() != null ? details.getQuiz_attempt() : "Quiz Attempt");
//                                gridModel.setQuizData(detailModel.getQuizAttempt());
//                                gridModel.setQuizImagePath(R.drawable.attempt);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.black));
//                                gridList.add(gridModel);
//                            }
//                            if (j == 2) {
//                                gridModel.setQuizHead(details.getScore() != null ? details.getScore() : "Score");
//                                gridModel.setQuizData(detailModel.getScore() + "/10");
//                                gridModel.setQuizImagePath(R.drawable.score);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.ufo_green));
//                                gridList.add(gridModel);
//                            }
//                            if (j == 3) {
//                                gridModel.setQuizHead(details.getResult_status() != null ? details.getResult_status() : "Status");
//                                gridModel.setQuizData(detailModel.getAmount_status());
//                                gridModel.setQuizImagePath(R.drawable.status);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.ufo_green));
//                                gridList.add(gridModel);
//
//
//                            }
//
//
//                        }
//                        AppLogger.e(TAG, "step  4-" + gridList.size());
//                        detailModel.setPassportQuizGridModelList(gridList);
//
//                    }
//                }
//            }
//            else if (existpositionsubjectforall.equals("All")) {
//
//                PassportSubjectModel passportSubjectModel = passportResModel.getPassportSubjectModelList().get(i);
//                if (i == 0) {
//                    passportSubjectModel.setExpanded(true);
//                }
//                // AppLogger.e(TAG, "step  1-" + passportResModel.getPassportSubjectModelList().size());
//                for (PassportQuizModel quizModel : passportSubjectModel.getQuizData()) {
//                    // AppLogger.e(TAG, "step  2-" + passportSubjectModel.getQuizData().size());
//                    for (PassportQuizDetailModel detailModel : quizModel.getQuizDetail()) {
//                        //  AppLogger.e(TAG, "step  3-" + quizModel.getQuizDetail().size());
//                        List<PassportQuizGridModel> gridList = new ArrayList<>();
//                        for (int j = 0; j < 4; j++) {
//                            PassportQuizGridModel gridModel = new PassportQuizGridModel();
//                            if (j == 0) {
//                                gridModel.setQuizHead(details.getQuiz_number() != null ? details.getQuiz_number() : "Quiz No");
//                                gridModel.setQuizData(detailModel.getExamName());
//                                gridModel.setQuizImagePath(R.drawable.quiz);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.black));
//                                gridList.add(gridModel);
//                            }
//                            if (j == 1) {
//                                gridModel.setQuizHead(details.getQuiz_attempt() != null ? details.getQuiz_attempt() : "Quiz Attempt");
//                                gridModel.setQuizData(detailModel.getQuizAttempt());
//                                gridModel.setQuizImagePath(R.drawable.attempt);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.black));
//                                gridList.add(gridModel);
//                            }
//                            if (j == 2) {
//                                gridModel.setQuizHead(details.getScore() != null ? details.getScore() : "Score");
//                                gridModel.setQuizData(detailModel.getScore() + "/10");
//                                gridModel.setQuizImagePath(R.drawable.score);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.ufo_green));
//                                gridList.add(gridModel);
//                            }
//                            if (j == 3) {
//                                gridModel.setQuizHead(details.getResult_status() != null ? details.getResult_status() : "Status");
//                                gridModel.setQuizData(detailModel.getAmount_status());
//                                gridModel.setQuizImagePath(R.drawable.status);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.ufo_green));
//                                gridList.add(gridModel);
//
//
//                            }
//
//
//                        }
//                        AppLogger.e(TAG, "step  4-" + gridList.size());
//                        detailModel.setPassportQuizGridModelList(gridList);
//
//                    }
//                }
//            }
//            else {
//                PassportSubjectModel passportSubjectModel = passportResModel.getPassportSubjectModelList().get(i);
//                if (i == 0) {
//                    passportSubjectModel.setExpanded(true);
//                }
//                // AppLogger.e(TAG, "step  1-" + passportResModel.getPassportSubjectModelList().size());
//                for (PassportQuizModel quizModel : passportSubjectModel.getQuizData()) {
//                    // AppLogger.e(TAG, "step  2-" + passportSubjectModel.getQuizData().size());
//                    for (PassportQuizDetailModel detailModel : quizModel.getQuizDetail()) {
//                        //  AppLogger.e(TAG, "step  3-" + quizModel.getQuizDetail().size());
//                        List<PassportQuizGridModel> gridList = new ArrayList<>();
//                        for (int j = 0; j < 4; j++) {
//                            PassportQuizGridModel gridModel = new PassportQuizGridModel();
//                            if (j == 0) {
//                                gridModel.setQuizHead(details.getQuiz_number() != null ? details.getQuiz_number() : "Quiz No");
//                                gridModel.setQuizData(detailModel.getExamName());
//                                gridModel.setQuizImagePath(R.drawable.quiz);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.black));
//                                gridList.add(gridModel);
//                            }
//                            if (j == 1) {
//                                gridModel.setQuizHead(details.getQuiz_attempt() != null ? details.getQuiz_attempt() : "Quiz Attempt");
//                                gridModel.setQuizData(detailModel.getQuizAttempt());
//                                gridModel.setQuizImagePath(R.drawable.attempt);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.black));
//                                gridList.add(gridModel);
//                            }
//                            if (j == 2) {
//                                gridModel.setQuizHead(details.getScore() != null ? details.getScore() : "Score");
//                                gridModel.setQuizData(detailModel.getScore() + "/10");
//                                gridModel.setQuizImagePath(R.drawable.score);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.ufo_green));
//                                gridList.add(gridModel);
//                            }
//                            if (j == 3) {
//                                gridModel.setQuizHead(details.getResult_status() != null ? details.getResult_status() : "Status");
//                                gridModel.setQuizData(detailModel.getAmount_status());
//                                gridModel.setQuizImagePath(R.drawable.status);
//                                gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.ufo_green));
//                                gridList.add(gridModel);
//
//
//                            }
//
//
//                        }
//                        AppLogger.e(TAG, "step  4-" + gridList.size());
//                        detailModel.setPassportQuizGridModelList(gridList);
//
//                    }
//                }
//                //  }
//
//
//                //  }
//            }
//        }
//        monthModelList.add(passportResModel);
//        return monthModelList;
//    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.back_arrow:
//                getActivity().getSupportFragmentManager().popBackStack();
//                break;
//            case R.id.lang_eng:
//                changeLanguage();
//                break;
//
//            case R.id.bt_transfer_money:
//                openSendMoneyFragment();
//                break;
//
//            case R.id.month_parent_layout:
//                userClick = true;
//                binding.monthSpinner.performClick();
//                break;
//
//            case R.id.subject_parent_layout:
//                userClick = true;
//                binding.subjectSpinner.performClick();
//                break;
//
//            case R.id.language_layout:
//                ((DashBoardMainActivity) getActivity()).openChangeLanguageDialog();
//                break;
//
//            case R.id.cardView2:
//                ((DashBoardMainActivity) getActivity()).openProfileFragment();
//                break;
//        }
//    }
//    private void changeLanguage() {
//
//        reloadFragment();
//    }
//    private void reloadFragment() {
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        if (Build.VERSION.SDK_INT >= 26) {
//            ft.setReorderingAllowed(false);
//        }
//        ft.detach(this).attach(this).commit();
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }
//    public void openSendMoneyFragment() {
//        Bundle bundle = new Bundle();
//        SendMoneyFragment sendMoneyFragment = new SendMoneyFragment();
//        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
//        sendMoneyFragment.setArguments(bundle);
//        openFragment(sendMoneyFragment);
//    }
//    private void openFragment(Fragment fragment) {
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .setReorderingAllowed(true)
//                .replace(AuroApp.getFragmentContainerUiId(), fragment, TransactionsFragment.class
//                        .getSimpleName())
//                .addToBackStack(null)
//                .commitAllowingStateLoss();
//
//    }
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        setRetainInstance(true);
//    }
//    private void monthSpinner() {
//        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
//        DashboardResModel dashboardResModel = prefModel.getDashboardResModel();
//        String date = dashboardResModel.getRegistrationdate();
//
//        monthDataModelList = DateUtil.monthDataModelList(date);
//        AppLogger.e("monthSpinner-","Step 1-"+monthDataModelList.size());
//        if (!TextUtil.checkListIsEmpty(monthDataModelList)) {
//            AppLogger.e("monthSpinner-","Step 2-");
//            PassportSpinnerAdapter monthSpinnerAdapter = new PassportSpinnerAdapter(monthDataModelList);
//            binding.monthSpinner.setAdapter(monthSpinnerAdapter);
//            binding.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    if (userClick) {
//                        userClick = false;
//                        isMonthSelected = true;
//                        spinnerMonth = monthDataModelList.get(position);
//                        binding.monthTitle.setText(monthDataModelList.get(position).getMonth());
//                        checkCallApiStatus();
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//            AppLogger.e("monthSpinner-","Step 3-");
//            int year = DateUtil.getcurrentYearNumber();
//            int month = DateUtil.getcurrentMonthNumber() + 1;
//
//            AppLogger.e("monthSpinner-","current Step 4- Year" +year+"--month--"+month);
//
//            for (int i = 0; i < monthDataModelList.size(); i++) {
//                MonthDataModel model = monthDataModelList.get(i);
//                AppLogger.e("monthSpinner-","Step 5- Year" +model.getYear()+"--month--"+model.getMonthNumber());
//                if (year == model.getYear() && month == model.getMonthNumber()) {
//                    AppLogger.e("monthSpinner-","Step 6-");
//                    binding.monthSpinner.setSelection(i);
//                }
//            }
//        }
//    }
//    private void SubjectSpinner(PassportMonthModel passportMonthModel, int status) {
//        subjectResModelList = new ArrayList<>();
//
//        if (status == 1) {
//            for (String subjectName : passportMonthModel.getSubjects()) {
//                MonthDataModel monthDataModelnew = new MonthDataModel();
//                monthDataModelnew.setMonth(subjectName);
//                subjectResModelList.add(monthDataModelnew);
//            }
//            if (!TextUtil.checkListIsEmpty(passportMonthModel.getSubjects()) && passportMonthModel.getSubjects().size() > 1) {
//                MonthDataModel monthDataModel = new MonthDataModel();
//                monthDataModel.setMonth("All");
//                subjectResModelList.add(monthDataModel);
//            }
//
//        }
//        else {
//            MonthDataModel monthDataModel = new MonthDataModel();
//            monthDataModel.setMonth("All");
//            subjectResModelList.add(monthDataModel);
//            binding.subjectTitle.setText(monthDataModel.getMonth());
//        }
//
//        if (!TextUtil.checkListIsEmpty(subjectResModelList)) {
//            if (subjectSpinner == null) {
//                subjectSpinner = new PassportSpinnerAdapter(Collections.emptyList());
//                binding.subjectSpinner.setAdapter(subjectSpinner);
//            }
//            subjectSpinner.setDataList(subjectResModelList);
//            binding.subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    AppLogger.e(TAG, "on subject selected");
//                    if (userClick) {
//                        userClick = false;
//                 int sizelist  = subjectResModelList.size();
//                 Log.d("sizesubjects", String.valueOf(sizelist));
//
//                     // spinnerSubject = subjectResModelList.get(position);
//                      //  AppLogger.e(TAG, "on subject selected" + spinnerSubject.getMonth());
//                        binding.subjectTitle.setText(subjectResModelList.get(position).getMonth());
//                                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
//                            editor.putInt("positionsubject", position);
//                        editor.putString("positionsubjectstatus", "true");
//                        editor.putString("existpositionsubject", "false");
//                        editor.putString("existpositionsubjectforall", subjectResModelList.get(position).getMonth());
//                            editor.apply();
//                        checkCallApiStatus();
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//        }
//        if (!TextUtil.checkListIsEmpty(subjectResModelList) && subjectResModelList.size() > 1) {
//            if (isMonthSelected) {
//                isMonthSelected = false;
//                MonthDataModel monthDataModel = subjectResModelList.get(subjectResModelList.size() - 1);
//                binding.subjectSpinner.setSelection(subjectResModelList.size() - 1);
//                binding.subjectTitle.setText(monthDataModel.getMonth());
//            } else {
//                setCurrentSubject();
//            }
//        }
//        if (!TextUtil.checkListIsEmpty(subjectResModelList) && subjectResModelList.size() == 1) {
//            binding.subjectSpinner.setSelection(0);
//            binding.subjectTitle.setText(subjectResModelList.get(0).getMonth());
//        }
//    }
//    private void setCurrentSubject() {
//        AppLogger.e(TAG, "setCurrentSubject step 1");
//        if (!TextUtil.checkListIsEmpty(subjectResModelList)) {
//            AppLogger.e(TAG, "setCurrentSubject step 2");
//            for (int i = 0; i < subjectResModelList.size(); i++) {
//                String subjectName = binding.subjectTitle.getText().toString();
//                MonthDataModel monthDataModel = subjectResModelList.get(i);
//                AppLogger.e(TAG, "setCurrentSubject step 3 month -" + monthDataModel.getMonth() + "-current month-" + subjectName);
//                if (monthDataModel.getMonth().equalsIgnoreCase(subjectName)) {
//                    AppLogger.e(TAG, "setCurrentSubject step 4 found -" + monthDataModel.getMonth() + "-current month-" + subjectName);
//                    binding.subjectSpinner.setSelection(i);
//                    binding.subjectTitle.setText(monthDataModel.getMonth());
//                }
//            }
//        }
//    }
//    private void checkCallApiStatus() {
//        if (spinnerSubject != null && !TextUtil.isEmpty(spinnerSubject.getMonth()) && spinnerMonth != null && !TextUtil.isEmpty(spinnerMonth.getMonth())) {
//            callTransportApi();
//        }
//    }
//    private void callTransportApi() {
//        int monthnum = spinnerMonth.getMonthNumber();
//        String monNum = "" + monthnum;
//        if (monthnum < 10) {
//            monNum = "0" + monthnum;
//        }
//        String isall = "0";
//        if (!TextUtil.isEmpty(spinnerSubject.getMonth()) & spinnerSubject.getMonth().equalsIgnoreCase("All")) {
//            isall = "1";
//        }
//        String month = "" + spinnerMonth.getYear() + monNum;
//        AppLogger.e("callTransportApi-", month);
//        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
//        handleProgress(0, "");
//        PassportReqModel passportReqModel = new PassportReqModel();
//        passportReqModel.setUserid(prefModel.getStudentData().getUserId());
//        passportReqModel.setMonth(month);
//        passportReqModel.setSubject(spinnerSubject.getMonth());
//        passportReqModel.setIsAll(isall);
//        passportReqModel.setUserPreferedLanguageId(Integer.parseInt(prefModel.getUserLanguageId()));
//        // passportReqModel.setIsAll();
//        viewModel.getPassportInternetCheck(passportReqModel);
//        /*spinnerSubject=null;
//        spinnerMonth=null;*/
//    }
//    private void observeServiceResponse() {
//
//        viewModel.serviceLiveData().observeForever(responseApi -> {
//
//            switch (responseApi.status) {
//
//                case LOADING:
//
//                    break;
//
//                case SUCCESS:
//                    if (isVisible()) {
//                        PassportMonthModel passportMonthModel = (PassportMonthModel) responseApi.data;
//                        AppLogger.e(TAG, "SUCCESS 1");
//                        SubjectSpinner(passportMonthModel, 1);
//                        if (!TextUtil.checkListIsEmpty(passportMonthModel.getPassportSubjectModelList())) {
//                            setPassportAdapter(passportMonthModel);
//                            handleProgress(1, details.getNo_data_found() != null ? details.getNo_data_found()  :  AuroApp.getAppContext().getResources().getString(R.string.no_data_found));
//                            AppLogger.e(TAG, "SUCCESS 2");
//                        } else {
//                            AppLogger.e(TAG, "SUCCESS 3");
//                            handleProgress(2,  details.getNo_data_found() != null ? details.getNo_data_found()  :  AuroApp.getAppContext().getResources().getString(R.string.no_data_found));
//                        }
//                    }
//                    break;
//
//                case NO_INTERNET:
//                case AUTH_FAIL:
//                case FAIL_400:
//                default:
//                    if(isVisible()) {
//                        AppLogger.e(TAG, "Fail 4");
//                        handleProgress(3, (String) responseApi.data);
//                    }
//                    break;
//
//            }
//        });
//    }
//    public void handleProgress(int i, String message) {
//        switch (i) {
//            case 0:
//                binding.monthlyWiseList.setVisibility(View.GONE);
//                binding.errorConstraint.setVisibility(View.GONE);
//                binding.progressBar2.setVisibility(View.VISIBLE);
//                break;
//
//            case 1:
//                binding.monthlyWiseList.setVisibility(View.VISIBLE);
//                binding.errorConstraint.setVisibility(View.GONE);
//                binding.progressBar2.setVisibility(View.GONE);
//                break;
//
//            case 2:
//                binding.monthlyWiseList.setVisibility(View.GONE);
//                binding.errorConstraint.setVisibility(View.VISIBLE);
//                binding.errorLayout.btRetry.setVisibility(View.GONE);
//                binding.errorLayout.errorIcon.setVisibility(View.GONE);
//                binding.errorLayout.textError.setText(message);
//                binding.progressBar2.setVisibility(View.GONE);
//                break;
//            case 3:
//                binding.monthlyWiseList.setVisibility(View.GONE);
//                binding.errorConstraint.setVisibility(View.VISIBLE);
//                binding.errorLayout.btRetry.setVisibility(View.VISIBLE);
//                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        checkCallApiStatus();
//                    }
//                });
//                binding.errorLayout.errorIcon.setVisibility(View.GONE);
//                binding.errorLayout.textError.setText(message);
//                binding.progressBar2.setVisibility(View.GONE);
//                break;
//        }
//    }

    @Inject
    @Named("TransactionsFragment")
    ViewModelFactory viewModelFactory;
    FragmentTransactionsBinding binding;
    TransactionsViewModel viewModel;
    MontlyWiseAdapter leaderBoardAdapter;
    Resources resources;
    DashboardResModel dashboardResModel;
    List<MonthDataModel> monthDataModelList;
    String TAG = "TransactionsFragment";
    boolean isFirstTime = false;
    MonthDataModel spinnerMonth;
    PassportSubjectQuizMonthModel spinnerSubject2;
    MonthDataModel spinnerSubject;
    boolean userClick = false;
    boolean isMonthSelected = false;
    PassportSpinnerAdapter subjectSpinner;
    List<MonthDataModel> subjectResModelList;
    Details details;
    List<PassportSubjectQuizMonthModel> listsubject = new ArrayList<>();
    List<PassportSubjectQuizMonthModel> list1= new ArrayList<>();
    List<String> demoList = new ArrayList<>();

    public TransactionsFragment() {
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
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_transactions, container, false);

        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
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

        init();
        setToolbar();
        setListener();
//        binding.subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (userClick) {
//                    userClick = false;
//                    binding.subjectSpinner.performClick();
//                    binding.subjectTitle.setText(list1.get(position).getSubject());
//                    binding.subjectSpinner.setAdapter(subjectSpinner);
//                    callTransportApi();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        binding.subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppLogger.e(TAG, "on subject selected");
                if (userClick) {
                    userClick = false;
                    spinnerSubject2 = list1.get(position);
                    AppLogger.e(TAG, "on subject selected" + spinnerSubject2.getSubject());
                    binding.subjectTitle.setText(demoList.get(position));
                    binding.subjectSpinner.setAdapter(subjectSpinner);
                    checkCallApiStatus();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        binding.subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                  binding.subjectSpinner.getSelectedItem().toString();
//             binding.subjectTitle.setText(binding.subjectSpinner.getSelectedItem().toString());
//                checkCallApiStatus();
//                }
//
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//
//            }
//        });
    }

    @Override
    protected void init() {

        if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
        }
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.TRANSACTION_FRAGMENT);

        setListener();

        monthSpinner();
       // getPassport();
       // SubjectSpinner(null, 0);

        spinnerMonth = new MonthDataModel();
        spinnerMonth.setMonthNumber(DateUtil.getcurrentMonthNumber() + 1);
        spinnerMonth.setYear(DateUtil.getcurrentYearNumber());
        spinnerMonth.setMonth(DateUtil.getMonthNameSpinner());

        AppLogger.e(TAG, "current month-" + DateUtil.getMonthNameSpinner());


        spinnerSubject = new MonthDataModel();
        spinnerSubject.setMonth("All");

        selectCurrentMonthInSpinner();
        //checkCallApiStatus();
        ViewUtil.setProfilePic(binding.imageView6);
        AppUtil.loadAppLogo(binding.auroScholarLogo,getActivity());
        AppStringDynamic.setPassportStrings(binding);
    }

    private void selectCurrentMonthInSpinner() {
        AppLogger.e(TAG, "selectCurrentMonthInSpinner step 1");
        if (!TextUtil.checkListIsEmpty(monthDataModelList)) {
            AppLogger.e(TAG, "selectCurrentMonthInSpinner step 2");
            for (int i = 0; i < monthDataModelList.size(); i++) {

                MonthDataModel monthDataModel = monthDataModelList.get(i);
                AppLogger.e(TAG, "selectCurrentMonthInSpinner step 3 month -" + monthDataModel.getMonth() + "-current month-" + DateUtil.getMonthNameSpinner());

                if (monthDataModel.getMonth().equalsIgnoreCase(DateUtil.getMonthNameSpinner()) || monthDataModel.getMonth().contains(DateUtil.getMonthNameSpinner())) {
                    binding.monthSpinner.setSelection(i);
                    binding.monthTitle.setText(monthDataModel.getMonth());
                    getPassport();
                }
            }
        }
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        binding.cardView2.setOnClickListener(this);
        binding.languageLayout.setOnClickListener(this);
        binding.monthParentLayout.setOnClickListener(this);
        binding.subjectParentLayout.setOnClickListener(this);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_transactions;

    }

    public void setPassportAdapter(PassportMonthModel passportMonthModel) {
        binding.monthlyWiseList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.monthlyWiseList.setHasFixedSize(true);
        binding.monthlyWiseList.setNestedScrollingEnabled(false);
        PassportMonthAdapter passportSpinnerAdapter = new PassportMonthAdapter(getActivity(), makePassportList(passportMonthModel), null);
        binding.monthlyWiseList.setAdapter(passportSpinnerAdapter);
    }

    private List<PassportMonthModel> makePassportList(PassportMonthModel passportResModel) {
        AppLogger.v("Months", passportResModel.getMonthName() + "   months");
        List<PassportMonthModel> monthModelList = new ArrayList<>();
        passportResModel.setMonthName(binding.monthTitle.getText().toString());
        passportResModel.setExpanded(true);
        for (int i = 0; i < passportResModel.getPassportSubjectModelList().size(); i++) {
            PassportSubjectModel passportSubjectModel = passportResModel.getPassportSubjectModelList().get(i);
            if (i == 0) {
                passportSubjectModel.setExpanded(true);
            }
            AppLogger.e(TAG, "step  1-" + passportResModel.getPassportSubjectModelList().size());
            for (PassportQuizModel quizModel : passportSubjectModel.getQuizData()) {
                AppLogger.e(TAG, "step  2-" + passportSubjectModel.getQuizData().size());
                for (PassportQuizDetailModel detailModel : quizModel.getQuizDetail()) {
                    AppLogger.e(TAG, "step  3-" + quizModel.getQuizDetail().size());
                    List<PassportQuizGridModel> gridList = new ArrayList<>();
                    for (int j = 0; j < 4; j++) {
                        PassportQuizGridModel gridModel = new PassportQuizGridModel();
                        if (j == 0) {
                            gridModel.setQuizHead(details.getQuiz_number() !=null  ? details.getQuiz_number() : "Quiz No");
                            gridModel.setQuizData(detailModel.getExamName());
                            gridModel.setQuizImagePath(R.drawable.quiz);
                            gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.black));
                            gridList.add(gridModel);
                        }
                        if (j == 1) {
                            gridModel.setQuizHead(details.getQuiz_attempt()!=null  ? details.getQuiz_attempt():"Quiz Attempt");
                            gridModel.setQuizData(detailModel.getQuizAttempt());
                            gridModel.setQuizImagePath(R.drawable.attempt);
                            gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.black));
                            gridList.add(gridModel);
                        }
                        if (j == 2) {
                            gridModel.setQuizHead(details.getScore()!=null  ?details.getScore() :"Score");
                            gridModel.setQuizData(detailModel.getScore() + "/10");
                            gridModel.setQuizImagePath(R.drawable.score);
                            gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.ufo_green));
                            gridList.add(gridModel);
                        }
                        if (j == 3) {
                            gridModel.setQuizHead(details.getResult_status() != null?details.getResult_status():"Status");
                            gridModel.setQuizData(detailModel.getAmount_status());
                            gridModel.setQuizImagePath(R.drawable.status);
                            gridModel.setQuizColor(ContextCompat.getColor(getContext(), R.color.ufo_green));
                            gridList.add(gridModel);
                        }
                    }
                    AppLogger.e(TAG, "step  4-" + gridList.size());
                    detailModel.setPassportQuizGridModelList(gridList);

                }
            }
        }
        monthModelList.add(passportResModel);
        return monthModelList;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_arrow) {
            getActivity().getSupportFragmentManager().popBackStack();
        } else if (id == R.id.lang_eng) {
            changeLanguage();
        } else if (id == R.id.bt_transfer_money) {
            openSendMoneyFragment();
        } else if (id == R.id.month_parent_layout) {
            userClick = true;
            binding.monthSpinner.performClick();
        } else if (id == R.id.subject_parent_layout) {
            userClick = true;

            //  binding.subjectSpinner.performClick();
        } else if (id == R.id.language_layout) {
            ((DashBoardMainActivity) getActivity()).openChangeLanguageDialog();
        } else if (id == R.id.cardView2) {
            ((DashBoardMainActivity) getActivity()).openProfileFragment();
        }
    }

    private void changeLanguage() {

        reloadFragment();
    }


    private void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }


    @Override
    public void onResume() {
        super.onResume();

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
                .replace(AuroApp.getFragmentContainerUiId(), fragment, TransactionsFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    private void monthSpinner() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        DashboardResModel dashboardResModel = prefModel.getDashboardResModel();
        String date = dashboardResModel.getRegistrationdate();

        monthDataModelList = DateUtil.monthDataModelList(date);
        AppLogger.e("monthSpinner-","Step 1-"+monthDataModelList.size());
        if (!TextUtil.checkListIsEmpty(monthDataModelList)) {
            AppLogger.e("monthSpinner-","Step 2-");
            PassportSpinnerAdapter monthSpinnerAdapter = new PassportSpinnerAdapter(monthDataModelList);
            binding.monthSpinner.setAdapter(monthSpinnerAdapter);
            binding.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (userClick) {
                        userClick = false;
                        isMonthSelected = true;
                        spinnerMonth = monthDataModelList.get(position);
                        binding.monthTitle.setText(monthDataModelList.get(position).getMonth());
                       // checkCallApiStatus();
                        getPassport();
                        checkCallApiStatus();


                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            AppLogger.e("monthSpinner-","Step 3-");
            int year = DateUtil.getcurrentYearNumber();
            int month = DateUtil.getcurrentMonthNumber() + 1;

            AppLogger.e("monthSpinner-","current Step 4- Year" +year+"--month--"+month);

            for (int i = 0; i < monthDataModelList.size(); i++) {
                MonthDataModel model = monthDataModelList.get(i);
                AppLogger.e("monthSpinner-","Step 5- Year" +model.getYear()+"--month--"+model.getMonthNumber());
                if (year == model.getYear() && month == model.getMonthNumber()) {
                    AppLogger.e("monthSpinner-","Step 6-");
                    binding.monthSpinner.setSelection(i);
                }
            }
        }
    }

    private void SubjectSpinner(PassportMonthModel passportMonthModel, int status) {
        subjectResModelList = new ArrayList<>();
        if (status == 1) {
            for (String subjectName : passportMonthModel.getSubjects()) {
                MonthDataModel monthDataModelnew = new MonthDataModel();

                monthDataModelnew.setMonth(subjectName);

                subjectResModelList.add(monthDataModelnew);
            }
            if (!TextUtil.checkListIsEmpty(passportMonthModel.getSubjects()) && passportMonthModel.getSubjects().size() > 1) {
                MonthDataModel monthDataModel = new MonthDataModel();
                monthDataModel.setMonth("All");
                subjectResModelList.add(monthDataModel);



            }

        } else {
            MonthDataModel monthDataModel = new MonthDataModel();
            monthDataModel.setMonth("All");
            subjectResModelList.add(monthDataModel);
            binding.subjectTitle.setText(monthDataModel.getMonth());
        }
        /*MonthDataModel monthDataModel_1 = new MonthDataModel();
        monthDataModel_1.setMonth("Social Science");
        subjectResModelList.add(monthDataModel_1);
        MonthDataModel monthDataModel_2 = new MonthDataModel();
        monthDataModel_2.setMonth("English");
        subjectResModelList.add(monthDataModel_2);*/
        if (!TextUtil.checkListIsEmpty(subjectResModelList)) {
            if (subjectSpinner == null) {
                subjectSpinner = new PassportSpinnerAdapter(Collections.emptyList());
                binding.subjectSpinner.setAdapter(subjectSpinner);
            }
            subjectSpinner.setDataList(subjectResModelList);

        }
        if (!TextUtil.checkListIsEmpty(subjectResModelList) && subjectResModelList.size() > 1) {
            if (isMonthSelected) {
                isMonthSelected = false;
                MonthDataModel monthDataModel = subjectResModelList.get(subjectResModelList.size() - 1);
                binding.subjectSpinner.setSelection(subjectResModelList.size() - 1);
                binding.subjectTitle.setText(monthDataModel.getMonth());
            } else {
                setCurrentSubject();
            }
        }
        if (!TextUtil.checkListIsEmpty(subjectResModelList) && subjectResModelList.size() == 1) {
            binding.subjectSpinner.setSelection(0);
            binding.subjectTitle.setText(subjectResModelList.get(0).getMonth());
        }
    }

    private void setCurrentSubject() {
        AppLogger.e(TAG, "setCurrentSubject step 1");
        if (!TextUtil.checkListIsEmpty(subjectResModelList)) {
            AppLogger.e(TAG, "setCurrentSubject step 2");
            for (int i = 0; i < subjectResModelList.size(); i++) {
                String subjectName = binding.subjectTitle.getText().toString();
                MonthDataModel monthDataModel = subjectResModelList.get(i);
                AppLogger.e(TAG, "setCurrentSubject step 3 month -" + monthDataModel.getMonth() + "-current month-" + subjectName);
                if (monthDataModel.getMonth().equalsIgnoreCase(subjectName)) {
                    AppLogger.e(TAG, "setCurrentSubject step 4 found -" + monthDataModel.getMonth() + "-current month-" + subjectName);
                    binding.subjectSpinner.setSelection(i);
                    binding.subjectTitle.setText(monthDataModel.getMonth());
                }
            }
        }
    }


    private void checkCallApiStatus() {
       // if (spinnerSubject != null && !TextUtil.isEmpty(spinnerSubject.getMonth()) && spinnerMonth != null && !TextUtil.isEmpty(spinnerMonth.getMonth())) {
            callTransportApi();
      //  }
    }

    private void callTransportApi() {
        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle(details.getProcessing());
        progress.setMessage(details.getProcessing());
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();
        int monthnum = spinnerMonth.getMonthNumber();
        String monNum = "" + monthnum;
        if (monthnum < 10) {
            monNum = "0" + monthnum;
        }
        String isall = "0";
        if (binding.subjectTitle.getText().toString().equals("All")){
       // if (!TextUtil.isEmpty(spinnerSubject.getMonth()) & spinnerSubject.getMonth().equalsIgnoreCase("All")) {
            isall = "1";
        }
        else{
            isall = "0";
        }
        String month = "" + spinnerMonth.getYear() + monNum;
        AppLogger.e("callTransportApi-", month);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        handleProgress(0, "");
        PassportReqModel passportReqModel = new PassportReqModel();
        passportReqModel.setUserid(prefModel.getStudentData().getUserId());
        passportReqModel.setMonth(month);
        String subject = binding.subjectTitle.getText().toString();
        passportReqModel.setSubject(subject);    //spinnerSubject.getMonth()
       // passportReqModel.setSubject(spinnerSubject.getMonth());

        passportReqModel.setIsAll(isall);
        passportReqModel.setUserPreferedLanguageId(Integer.parseInt(prefModel.getUserLanguageId()));
        // passportReqModel.setIsAll();
        viewModel.getPassportInternetCheck(passportReqModel);
        progress.cancel();
        /*spinnerSubject=null;
        spinnerMonth=null;*/
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:

                    break;

                case SUCCESS:
                    if (isVisible()) {
                        PassportMonthModel passportMonthModel = (PassportMonthModel) responseApi.data;
                        AppLogger.e(TAG, "SUCCESS 1");
                       // SubjectSpinner(passportMonthModel, 1);
                        if (!TextUtil.checkListIsEmpty(passportMonthModel.getPassportSubjectModelList())) {
                            setPassportAdapter(passportMonthModel);
                            handleProgress(1, details.getNo_data_found() != null ? details.getNo_data_found()  :  AuroApp.getAppContext().getResources().getString(R.string.no_data_found));
                            AppLogger.e(TAG, "SUCCESS 2");
                        } else {
                            AppLogger.e(TAG, "SUCCESS 3");
                          //  setPassportAdapter(passportMonthModel);
                            handleProgress(2,  details.getNo_data_found() != null ? details.getNo_data_found()  :  AuroApp.getAppContext().getResources().getString(R.string.no_data_found));
                        }
                    }
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                default:
                    if(isVisible()) {
                        AppLogger.e(TAG, "Fail 4");
                        handleProgress(3, (String) responseApi.data);
                    }
                    break;

            }
        });
    }

    public void handleProgress(int i, String message) {
        switch (i) {
            case 0:
                binding.monthlyWiseList.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressBar2.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.monthlyWiseList.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressBar2.setVisibility(View.GONE);
                break;

            case 2:
                binding.monthlyWiseList.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.errorLayout.btRetry.setVisibility(View.GONE);
                binding.errorLayout.errorIcon.setVisibility(View.GONE);
                binding.errorLayout.textError.setText(message);
                binding.progressBar2.setVisibility(View.GONE);
                break;
            case 3:
                binding.monthlyWiseList.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.errorLayout.btRetry.setVisibility(View.VISIBLE);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkCallApiStatus();
                    }
                });
                binding.errorLayout.errorIcon.setVisibility(View.GONE);
                binding.errorLayout.textError.setText(message);
                binding.progressBar2.setVisibility(View.GONE);
                break;
        }
    }

    private void getPassport()
    {
        int monthnum = spinnerMonth.getMonthNumber();
        String monNum = "" + monthnum;
        if (monthnum < 10) {
            monNum = "0" + monthnum;
        }

        String month = "" + spinnerMonth.getYear() + monNum;
        String suserid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        AppLogger.e("callTransportApi-", month);
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",suserid);
        map_data.put("month",month);


        RemoteApi.Companion.invoke().getQuizMonthSubject(map_data)
                .enqueue(new Callback<PassportQuizMonthModel>()
                {
                    @Override
                    public void onResponse(Call<PassportQuizMonthModel> call, Response<PassportQuizMonthModel> response)
                    {
                        try {


                            if (response.isSuccessful()) {
                                list1.clear();
                                demoList.clear();
                                //listsubject = response.body().getPassportSubjectModelList();
                                for (int i = 0; i < response.body().getPassportSubjectModelList().size(); i++) {
                                    list1.add(response.body().getPassportSubjectModelList().get(i));
                                    demoList.add(response.body().getPassportSubjectModelList().get(i).getSubject());
                                    // Toast.makeText(getActivity(), list1.get(i).getSubject(), Toast.LENGTH_SHORT).show();
                                    //  callTransportApi();

                                }


                                Log.d(TAG, "onResponse: ListCount" + list1.size());
                                //       SubjectQuizSpinnerAdapter adapter = new SubjectQuizSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, list1);

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, demoList);

                                binding.subjectSpinner.setAdapter(adapter);


                                binding.subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        // binding.subjectSpinner.performClick();
                                        binding.subjectTitle.setText(demoList.get(position));
                                        binding.subjectTitle.setTextColor(Color.WHITE);
                                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                                        //   Toast.makeText(getActivity(), demoList.get(position), Toast.LENGTH_SHORT).show();
                                        checkCallApiStatus();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        binding.subjectSpinner.performClick();
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

}








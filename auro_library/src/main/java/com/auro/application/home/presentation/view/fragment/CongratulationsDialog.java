package com.auro.application.home.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseDialog;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.DialogCongratulations2Binding;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.QuizResModel;
import com.auro.application.home.data.model.SubjectResModel;
import com.auro.application.home.presentation.viewmodel.CongratulationsDialogViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.strings.AppStringDynamic;
import com.bumptech.glide.Glide;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

public class CongratulationsDialog extends BaseDialog implements View.OnClickListener, CommonCallBackListner {


    @Inject
    @Named("CongratulationsDialog")
    ViewModelFactory viewModelFactory;
    DialogCongratulations2Binding binding;
    CongratulationsDialogViewModel viewModel;
    public static String bundledashboardResModel = "dashboardResModel";
    public static String bundleassignmentReqModel = "assignmentReqModel";

    private static final String commonCallBackListnerbundle = "commonCallBackListner";
    private static final String dashboardResModelbundle = "dashboardResModel";
    private static final String assignmentReqModelbundle = "assignmentReqModel";
    Context mcontext;
    DashboardResModel dashboardResModel;
    AssignmentReqModel assignmentReqModel;
    static CommonCallBackListner commonCallBackListner;
    int marks;
    SubjectResModel subjectResModel;
    QuizResModel quizResModel;
    int finishedTestPos;


    private static final String TAG = CongratulationsDialog.class.getSimpleName();

/*
    public CongratulationsDialog(Context mcontext, DashboardResModel dashboardResModel, AssignmentReqModel assignmentReqModel, CommonCallBackListner commonCallBackListner) {
        this.mcontext = mcontext;
        this.dashboardResModel = dashboardResModel;
        this.assignmentReqModel = assignmentReqModel;
        this.commonCallBackListner = commonCallBackListner;
    }
*/

    public CongratulationsDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(getActivity().getResources().getString(R.string.bundledashboardresmodel));
            assignmentReqModel = getArguments().getParcelable(getActivity().getResources().getString(R.string.bundleassignmentreqmodel));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CongratulationsDialogViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setListener();
        return binding.getRoot();
    }

    @Override
    protected void init() {
        binding.btnShare.setOnClickListener(this);
        binding.icClose.setOnClickListener(this);
        binding.txtStartQuiz.setOnClickListener(this);
        binding.txtRetakeQuiz.setOnClickListener(this);
        AppStringDynamic.setCongratulationsDialogStrings(binding);
        Glide.with(this).load(R.raw.confetti_4).into(binding.backgroundSprincle11);

        // create random object
        Random randomno = new Random();
  /*      if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(dashboardResModelbundle);
            assignmentReqModel = getArguments().getParcelable(assignmentReqModelbundle);
        }*/
        binding.tickerView.setPreferredScrollingDirection(TickerView.ScrollingDirection.DOWN);
        binding.tickerView.setCharacterLists(TickerUtils.provideNumberList());
        quizResModel = AuroAppPref.INSTANCE.getModelInstance().getQuizResModel();
        for (SubjectResModel model : dashboardResModel.getSubjectResModelList()) {
            if (model.getSubject().equalsIgnoreCase(quizResModel.getCoreSubjectName())) {
                subjectResModel = model;
                quizResModel = model.getChapter().get(quizResModel.getNumber() - 1);
                break;
            }
        }
        //   subjectResModel = dashboardResModel.getSubjectResModelList().get(assignmentReqModel.getSubjectPos());
        // finishedTestPos = ConversionUtil.INSTANCE.convertStringToInteger(assignmentReqModel.getExam_name());
        //  quizResModel = subjectResModel.getChapter().get(finishedTestPos - 1);
        marks = assignmentReqModel.getActualScore() * 10;

        for (int i = 1; i <= marks; i++) {
            binding.tickerView.setText(i + "%");
        }

        if (!TextUtil.isEmpty(dashboardResModel.getStudent_name())) {
            binding.RPTextView4.setVisibility(View.VISIBLE);
            binding.RPTextView4.setText(dashboardResModel.getStudent_name());
        } else {
            binding.RPTextView4.setVisibility(View.GONE);
        }

        if (checkAllQuizAreFinishedOrNot()) {
            binding.txtStartQuiz.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_congratulations_2;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnShare) {
            shareWithFriends();
            dismiss();
        } else if (id == R.id.icClose) {
            dismiss();
        } else if (id == R.id.txtRetakeQuiz) {
        } else if (id == R.id.txtStartQuiz) {
            AppLogger.e("chhonker txtStartQuiz", "-clicked");
            makeQuiz();
            dismiss();
        }
    }

    public static CongratulationsDialog newInstance(CommonCallBackListner listner, DashboardResModel dashboardResModel, AssignmentReqModel assignmentReqModel) {


        CongratulationsDialog fragment = new CongratulationsDialog();
        commonCallBackListner = listner;
        Bundle args = new Bundle();
        args.putParcelable(bundledashboardResModel, dashboardResModel);
        args.putParcelable(bundleassignmentReqModel, assignmentReqModel);
        fragment.setArguments(args);
        return fragment;
    }


    public void shareWithFriends() {
        String completeLink = getActivity().getResources().getString(R.string.invite_friend_refrral);
        if (AuroApp.getAuroScholarModel() != null && !TextUtil.isEmpty(AuroApp.getAuroScholarModel().getReferralLink())) {
            completeLink = completeLink + " " + AuroApp.getAuroScholarModel().getReferralLink();
        } else {
            completeLink = completeLink + " https://rb.gy/np9uh5";
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, completeLink);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        dismiss();
        getActivity().startActivity(shareIntent);
    }

    private String generateChars(Random random, String list, int numDigits) {
        final char[] result = new char[numDigits];
        for (int i = 0; i < numDigits; i++) {
            result[i] = list.charAt(random.nextInt(list.length()));
        }
        return new String(result);
    }


    private void makeQuiz() {
       // AppLogger.e("chhonker makeQuiz", "-lastPos--" + lastPos);
        for (int i = 0; i < subjectResModel.getChapter().size(); i++) {
            AppLogger.e("chhonker getChapter", "-i--" + i);
            AppLogger.e("chhonker getChapter name", "-i--" + subjectResModel.getChapter().get(i).getName());
            AppLogger.e("chhonker subject  name", "-i--" + subjectResModel.getChapter().get(i).getSubjectName());
            QuizResModel model = subjectResModel.getChapter().get(i);
            if (model.getAttempt() == 0) {
                quizResModel = model;
                sendClickCallBack(quizResModel);
                break;
            }
            /*if (quizResModel.getNumber() != (i+1)) {
                if (subjectResModel.getChapter().get(i).getAttempt() < 3) {
                    AppLogger.e("chhonker getAttempt", "-i--"+subjectResModel.getChapter().get(i).getAttempt());
                    sendClickCallBack(subjectResModel.getChapter().get(i));
                    break;
                }
            }*/
        }
    }


    private boolean checkAllQuizAreFinishedOrNot() {
        int totalAttempt = 0;
        int atleastOneAttempt = 0;
        if (subjectResModel != null) {
            for (QuizResModel quizResModel : subjectResModel.getChapter()) {
                totalAttempt = quizResModel.getAttempt() + totalAttempt;
                if (quizResModel.getAttempt() > 0) {
                    atleastOneAttempt = atleastOneAttempt + 1;
                }
            }
            return atleastOneAttempt == 4;
        }
        return false;
    }


    private void sendClickCallBack(QuizResModel quizResModel) {
        if (commonCallBackListner != null) {
            AppLogger.e("chhonker sendClickCallBack-- ", "sendClickCallBack");
            commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.NEXT_QUIZ_CLICK, quizResModel));
        }
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

    }
}

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
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.DialogLessScoreCongratulationsBinding;
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
import com.google.android.material.shape.CornerFamily;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

public class ConsgratuationLessScoreDialog extends BaseDialog implements View.OnClickListener{


    @Inject
    @Named("CongratulationsDialog")
    ViewModelFactory viewModelFactory;
    public static String bundledashboardResModel = "dashboardResModel";
    public static String bundleassignmentReqModel = "assignmentReqModel";



    DialogLessScoreCongratulationsBinding binding;
    CongratulationsDialogViewModel viewModel;
    Context mcontext;
    static CommonCallBackListner commonCallBackListner;
    DashboardResModel dashboardResModel;
    AssignmentReqModel assignmentReqModel;

    int marks;
    int finishedTestPos;
    SubjectResModel subjectResModel;
    QuizResModel quizResModel;



    private static final String TAG = ConsgratuationLessScoreDialog.class.getSimpleName();


    public ConsgratuationLessScoreDialog( ) {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) throws RuntimeException{
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected void init() {
        setListener();

        AppStringDynamic.setLessCongratulationsDialogStrings(binding);
        binding.tickerView.setPreferredScrollingDirection(TickerView.ScrollingDirection.DOWN);
        float radius = getResources().getDimension(R.dimen._10sdp);
        binding.imgtryagain.setShapeAppearanceModel(binding.imgtryagain.getShapeAppearanceModel()
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build());
        binding.tickerView.setCharacterLists(TickerUtils.provideNumberList());
/*        if (getArguments() != null) {

            dashboardResModel = getArguments().getParcelable(getActivity().getResources().getString(R.string.bundledashboardresmodel));
            assignmentReqModel = getArguments().getParcelable(getActivity().getResources().getString(R.string.bundleassignmentreqmodel));
        }*/

       AppLogger.e("chhonker check dialog 1","step 1");
         quizResModel = AuroAppPref.INSTANCE.getModelInstance().getQuizResModel();
        for (SubjectResModel model : dashboardResModel.getSubjectResModelList()) {
            AppLogger.e("chhonker check dialog 2","subject a -"+model.getSubject()+"----"+quizResModel.getCoreSubjectName());
            if (model.getSubject().equalsIgnoreCase(quizResModel.getCoreSubjectName())) {
                subjectResModel=model;
                quizResModel = model.getChapter().get(quizResModel.getNumber() - 1);
                AppLogger.e("chhonker check dialog 3","subject a -"+model.getSubject()+"----"+quizResModel.getCoreSubjectName());

                break;
            }
            AppLogger.e("chhonker check dialog 4","subject a -"+model.getSubject()+"----"+quizResModel.getCoreSubjectName());

        }
       // subjectResModel = dashboardResModel.getSubjectResModelList().get(assignmentReqModel.getSubjectPos());
       // finishedTestPos = ConversionUtil.INSTANCE.convertStringToInteger(assignmentReqModel.getExam_name());
       // quizResModel = subjectResModel.getChapter().get(finishedTestPos - 1);
        marks = assignmentReqModel.getActualScore() * 10;
        for (int i = 0; i <= marks; i++) {
            binding.tickerView.setText(i + "%");
        }

        if (ConversionUtil.INSTANCE.convertStringToInteger(assignmentReqModel.getQuiz_attempt()) < 3) {
            binding.txtRetakeQuiz.setVisibility(View.VISIBLE);
            binding.btntutor.setVisibility(View.GONE);
        } else {
            binding.txtRetakeQuiz.setVisibility(View.GONE);
            binding.btntutor.setVisibility(View.GONE);
        }

        if (checkAllQuizAreFinishedOrNot()) {
            binding.txtStartQuiz.setVisibility(View.VISIBLE);
            binding.txtRetakeQuiz.setVisibility(View.GONE);
            binding.btntutor.setVisibility(View.GONE);
        }

        binding.btnShare.setVisibility(View.VISIBLE);



        if (!TextUtil.isEmpty(dashboardResModel.getLeadQualified()) && dashboardResModel.getLeadQualified().equalsIgnoreCase(AppConstant.DocumentType.YES)) {
            binding.btntutor.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setToolbar() {

    }

    public static ConsgratuationLessScoreDialog newInstance(CommonCallBackListner listner, DashboardResModel dashboardResModel, AssignmentReqModel assignmentReqModel) {
        commonCallBackListner=listner;
        ConsgratuationLessScoreDialog fragment = new ConsgratuationLessScoreDialog();
        Bundle args = new Bundle();
        args.putParcelable(bundledashboardResModel, dashboardResModel);
        args.putParcelable(bundleassignmentReqModel, assignmentReqModel);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    protected void setListener() {
        binding.icClose.setOnClickListener(this);
        binding.txtStartQuiz.setOnClickListener(this);
        binding.txtRetakeQuiz.setOnClickListener(this);
        binding.btntutor.setOnClickListener(this);
        binding.btnShare.setOnClickListener(this);

    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_less_score_congratulations;
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
            sendClickCallBack(quizResModel);
            dismiss();
        } else if (id == R.id.txtStartQuiz) {
            makeQuiz();
            dismiss();
        } else if (id == R.id.btntutor) {
            if (AuroApp.getAuroScholarModel() != null && AuroApp.getAuroScholarModel().getSdkcallback() != null) {
                AuroApp.getAuroScholarModel().getSdkcallback().commonCallback(Status.BOOK_TUTOR_SESSION_CLICK, "");
            }
        }

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
        sendIntent.putExtra(Intent.EXTRA_TEXT,completeLink);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        dismiss();
        getActivity().startActivity(shareIntent);
    }

    private void makeQuiz() {
        int lastPos = finishedTestPos - 1;
        for (int i = 0; i < subjectResModel.getChapter().size(); i++) {
            if (i != lastPos) {
                if (subjectResModel.getChapter().get(i).getAttempt() < 3) {
                    sendClickCallBack(subjectResModel.getChapter().get(i));
                    break;
                }
            }
        }
    }

    private boolean checkAllQuizAreFinishedOrNot() {
        if(quizResModel!=null) {
            int totalAttempt = 0;
            for (QuizResModel quizResModel : subjectResModel.getChapter()) {
                totalAttempt = quizResModel.getAttempt() + totalAttempt;
            }
            return totalAttempt == 12;
        }
        return false;
    }

    private void sendClickCallBack(QuizResModel quizResModel) {
        if (commonCallBackListner != null) {
            commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.NEXT_QUIZ_CLICK, quizResModel));
        }
    }



}

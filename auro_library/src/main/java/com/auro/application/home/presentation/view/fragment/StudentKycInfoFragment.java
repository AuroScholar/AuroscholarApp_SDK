package com.auro.application.home.presentation.view.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.StudentKycInfoLayoutBinding;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.data.model.ParentProfileDataModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.InstructionsResModel;
import com.auro.application.home.data.model.response.StudentKycStatusResModel;
import com.auro.application.home.data.model.signupmodel.InstructionModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.activity.ParentProfileActivity;
import com.auro.application.home.presentation.view.adapter.StudentKycDocumentAdapter;
import com.auro.application.home.presentation.viewmodel.KYCViewModel;
import com.auro.application.kyc.presentation.view.fragment.UploadDocumentFragment;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.data.model.response.TeacherKycStatusResModel;
import com.auro.application.teacher.presentation.view.adapter.TeacherKycDocumentAdapter;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.disclaimer.DisclaimerKycDialog;
import com.auro.application.util.strings.AppStringDynamic;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudentKycInfoFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    @Inject
    @Named("StudentKycInfoFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "TeacherKycInfoFragment";
    StudentKycInfoLayoutBinding binding;
    KYCViewModel kycViewModel;
    ObjectAnimator animator_1;
    StudentKycStatusResModel studentKycStatusResModel;

    Map<Integer, Integer> animationlist;
    int valueCountImage = 1;

    public StudentKycInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        DaggerWrapper.getComponent(getActivity()).doInjection(this);
        kycViewModel = ViewModelProviders.of(this, viewModelFactory).get(KYCViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setListener();
        setToolbar();

        return binding.getRoot();
    }

    @Override
    protected void init() {
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.QUIZ_KYC_FRAGMENT);

        AppStringDynamic.setKycScreenStrings(binding);
        MyProfileResModel model = AuroAppPref.INSTANCE.getModelInstance().getTeacherProfileResModel();
        if (model != null && model.getTeacherName() != null && !model.getTeacherName().isEmpty()) {
            binding.teacherName.setText(model.getTeacherName());
        }
        if (model != null && model.getTeacherProfilePic() != null && !model.getTeacherProfilePic().isEmpty()) {
            ViewUtil.setTeacherProfilePic(binding.imageView6);
        }
        kycScannerBanner(binding.scannerLayout, binding.scannerBar);
        kycScannerBanner(binding.relativeLayout2, binding.scannerLayout);
        startAnimation();
        getProfile();
        //ViewUtil.setProfilePic(binding.imageView6);
        DashboardResModel dashboardResModel = AuroAppPref.INSTANCE.getModelInstance().getDashboardResModel();
        if (dashboardResModel.getStudent_name() != null && !dashboardResModel.getStudent_name().isEmpty()) {
            binding.teacherName.setText(dashboardResModel.getStudent_name());
        }
        callGetStudentKycStatus();
        AppUtil.commonCallBackListner = this;

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        InstructionModel instructionModel = new InstructionModel();
        instructionModel.setInstructionCode(AppConstant.InstructionsType.KYC);

        instructionModel.setLanguageId(Integer.parseInt(prefModel.getUserLanguageId()));

        kycViewModel.checkInternet(instructionModel, Status.GET_INSTRUCTIONS_API);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppLogger.e("onViewCreated--", "I am calling");
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        HomeActivity.setListingActiveFragment(HomeActivity.TEACHER_KYC_FRAGMENT);

        if (kycViewModel != null && kycViewModel.serviceLiveData().hasObservers()) {
            kycViewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }


        binding.languageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).openChangeLanguageDialog();
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.student_kyc_info_layout;
    }

    public void kycScannerBanner(View outer, View inner) {

        //Scanner overlay
        animator_1 = null;

        ViewTreeObserver vto = outer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                outer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    outer.getViewTreeObserver().
                            removeGlobalOnLayoutListener(this);

                } else {
                    outer.getViewTreeObserver().
                            removeOnGlobalLayoutListener(this);
                }

                AppLogger.v("Layout", outer.getY() + " -  " + outer.getX() + "  -" + outer.getWidth());
                float destination = 0;

                if (outer.getId() == R.id.scannerLayout) {
                    destination = outer.getWidth() - 60;
                    animator_1 = ObjectAnimator.ofFloat(inner, "translationX",
                            outer.getY(),
                            destination);
                } else if (outer.getId() == R.id.relativeLayout2) {
                    destination = outer.getWidth() - 320;
                    animator_1 = ObjectAnimator.ofFloat(inner, "translationX",
                            outer.getY(),
                            destination);

                }

                animator_1.setRepeatMode(ValueAnimator.REVERSE);
                animator_1.setRepeatCount(ValueAnimator.INFINITE);
                animator_1.setInterpolator(new AccelerateDecelerateInterpolator());
                animator_1.setDuration(3000);
                animator_1.start();

            }
        });

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
    public void startAnimation() {
        animationlist = new HashMap<Integer, Integer>();
        animationlist.put(1, R.drawable.ic_full_kyc_2);
        animationlist.put(2, R.drawable.ic_full_kyc_3);
        animationlist.put(3, R.drawable.ic_full_kyc_1);


        fadeInOutAnimation(valueCountImage);
    }


    public void fadeInOutAnimation(int value) {

        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);

        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);


        final AnimationSet set = new AnimationSet(false);

        set.addAnimation(fadeIn);
        set.addAnimation(fadeOut);
        fadeOut.setStartOffset(3000);
        set.setDuration(3000);
        binding.doucmetImage.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.doucmetImage.startAnimation(set);
                if (value == 3) {
                    binding.doucmetImage.setImageResource(animationlist.get(value));
                    valueCountImage = 1;
                    fadeInOutAnimation(valueCountImage);
                } else {
                    binding.doucmetImage.setImageResource(animationlist.get(value));
                    fadeInOutAnimation(valueCountImage++);
                }
            }
        });
    }


    public void setAdapter() {
        ArrayList<KYCDocumentDatamodel> kycDocumentDatamodelArrayList = kycViewModel.homeUseCase.makeAdapterDocumentList(studentKycStatusResModel);
        binding.kycRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.kycRecycleview.setHasFixedSize(true);
        binding.kycRecycleview.setNestedScrollingEnabled(false);
        StudentKycDocumentAdapter studentKycDocumentAdapter = new StudentKycDocumentAdapter(kycDocumentDatamodelArrayList, this);
        binding.kycRecycleview.setAdapter(studentKycDocumentAdapter);
        int count = 0;
        for (KYCDocumentDatamodel kycDocumentDatamodel : kycDocumentDatamodelArrayList) {
            if (kycDocumentDatamodel.isModify()) {
                count++;
            }
        }

    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case DOCUMENT_CLICK:
                StudentUploadDocumentFragment uploadDocumentFragment = new StudentUploadDocumentFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_DOCUMENT_MODEL_DATA, (KYCDocumentDatamodel) commonDataModel.getObject());
                bundle.putParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_RES_MODEL_DATA, studentKycStatusResModel);
                uploadDocumentFragment.setArguments(bundle);
                openFragment(uploadDocumentFragment);
                break;

            case UPLOAD_DOC_CALLBACK:
                callGetStudentKycStatus();
                break;
        }
    }


    private void observeServiceResponse() {

        kycViewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    if (isVisible()) {
                        if (responseApi.apiTypeStatus == Status.STUDENT_KYC_STATUS_API) {
                            studentKycStatusResModel = (StudentKycStatusResModel) responseApi.data;
                            if (studentKycStatusResModel.getError()) {
                                handleProgress(2, studentKycStatusResModel.getMessage());
                            } else {
                                handleProgress(1, "");
                                setAdapter();
                                AppLogger.e("TEACHER_KYC_STATUS_API-", studentKycStatusResModel.getKycStatus());
                            }
                        } else if (responseApi.apiTypeStatus == Status.GET_INSTRUCTIONS_API) {
                            InstructionsResModel instructionsResModel = (InstructionsResModel) responseApi.data;
                            if (!instructionsResModel.getError()) {
                                checkDisclaimerKYCDialog(instructionsResModel);
                            } else {
                                AppLogger.e("GET_INSTRUCTIONS_API","else part");
                            }
                        }
                    }
                    break;

                case FAIL:
                case NO_INTERNET:
                default:
                    if (isVisible()) {
                        handleProgress(3, ((String) responseApi.data));
                    }
                    break;
            }
        });
    }
    private void checkDisclaimerKYCDialog(InstructionsResModel instructionsResModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (!prefModel.isPreKycDisclaimer()) {
            DisclaimerKycDialog askDetailCustomDialog = new DisclaimerKycDialog(getActivity());
            askDetailCustomDialog.setInstructionData(instructionsResModel);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(askDetailCustomDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            askDetailCustomDialog.getWindow().setAttributes(lp);
            Objects.requireNonNull(askDetailCustomDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            askDetailCustomDialog.setCancelable(false);
            askDetailCustomDialog.show();

        }
    }


    private void openFragment(Fragment fragment) {
        FragmentUtil.addFragment(getContext(), fragment, R.id.mainFrameLayout, 0);
    }


    private void handleProgress(int status, String msg) {
        if (status == 0) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.errorConstraint.setVisibility(View.GONE);
            binding.kycRecycleview.setVisibility(View.GONE);
        } else if (status == 1) {
            binding.progressBar.setVisibility(View.GONE);
            binding.errorConstraint.setVisibility(View.GONE);
            binding.kycRecycleview.setVisibility(View.VISIBLE);
        } else if (status == 2) {
            binding.progressBar.setVisibility(View.GONE);
            binding.kycRecycleview.setVisibility(View.GONE);
            binding.errorConstraint.setVisibility(View.VISIBLE);
            binding.errorLayout.textError.setText(msg);
            binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callGetStudentKycStatus();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {

    }

    void callGetStudentKycStatus() {
        handleProgress(0, "");
        kycViewModel.checkInternet("", Status.STUDENT_KYC_STATUS_API);
    }




}

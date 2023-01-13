package com.auro.application.teacher.presentation.view.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.FragmentKycInfoScreenBinding;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.kyc.presentation.view.fragment.UploadDocumentFragment;
import com.auro.application.teacher.data.model.response.MyClassRoomResModel;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.data.model.response.TeacherKycStatusResModel;
import com.auro.application.teacher.presentation.view.adapter.TeacherKycDocumentAdapter;
import com.auro.application.teacher.presentation.viewmodel.TeacherKycViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.network.ProgressRequestBody;
import com.auro.application.util.strings.AppStringDynamic;
import com.auro.application.util.strings.AppStringTeacherDynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;


public class TeacherKycInfoFragment extends BaseFragment implements CommonCallBackListner {


    @Inject
    @Named("TeacherKycInfoFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "TeacherKycInfoFragment";
    FragmentKycInfoScreenBinding binding;
    TeacherKycViewModel kycViewModel;
    ObjectAnimator animator_1;
    TeacherKycStatusResModel teacherKycStatusResModel;

    Map<Integer, Integer> animationlist;
    int valueCountImage = 1;

    public TeacherKycInfoFragment() {
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
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        kycViewModel = ViewModelProviders.of(this, viewModelFactory).get(TeacherKycViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setListener();
        setToolbar();

        return binding.getRoot();
    }

    @Override
    protected void init() {
        MyProfileResModel model = AuroAppPref.INSTANCE.getModelInstance().getTeacherProfileResModel();
        if (model != null && model.getTeacherName() != null && !model.getTeacherName().isEmpty()) {
            binding.teacherName.setText(model.getTeacherName());
        }

        if (model != null && model.getTeacherProfilePic() != null && !model.getTeacherProfilePic().isEmpty()) {
            ViewUtil.setTeacherProfilePic(binding.imageView6);
        }

        kycScannerBanner(binding.scannerLayout, binding.scannerBar);
        kycScannerBanner(binding.relativeLayout2, binding.scannerLayout);
        //  openFragment(new UploadDocumentFragment());
        startAnimation();
        //ViewUtil.setTeacherProfilePic(binding.profileImage);
        //ViewUtil.setLanguageonUi(getActivity());
        AppStringTeacherDynamic.setTeacherKycInfoFragmentStrings(binding);

    }


    @Override
    public void onResume() {
        super.onResume();
        callTeacherKycStatusApi();
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
        return R.layout.fragment_kyc_info_screen;
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
                try {
                    binding.doucmetImage.startAnimation(set);
                    if (value == 3) {
                        binding.doucmetImage.setImageResource(animationlist.get(value));
                        valueCountImage = 1;
                        fadeInOutAnimation(valueCountImage);
                    } else {
                        binding.doucmetImage.setImageResource(animationlist.get(value));
                        fadeInOutAnimation(valueCountImage++);
                    }
                } catch (Exception e) {

                }
            }
        });
    }


    public void setAdapter() {
        ArrayList<KYCDocumentDatamodel> kycDocumentDatamodelArrayList = kycViewModel.teacherUseCase.makeAdapterDocumentList(teacherKycStatusResModel);
        binding.kycRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.kycRecycleview.setHasFixedSize(true);
        binding.kycRecycleview.setNestedScrollingEnabled(false);
        TeacherKycDocumentAdapter mteacherKycDocumentAdapter = new TeacherKycDocumentAdapter(kycDocumentDatamodelArrayList, this);
        binding.kycRecycleview.setAdapter(mteacherKycDocumentAdapter);
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
                AppLogger.v("Doc_pradeep","Get select");
                UploadDocumentFragment uploadDocumentFragment = new UploadDocumentFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_DOCUMENT_MODEL_DATA, (KYCDocumentDatamodel) commonDataModel.getObject());
                bundle.putParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_RES_MODEL_DATA, teacherKycStatusResModel);
                uploadDocumentFragment.setArguments(bundle);
                openFragment(uploadDocumentFragment);
                break;
            case UPLOAD_TEACHER_DOC_CALLBACK:
                callTeacherKycStatusApi();
                break;
        }
    }

    private void callTeacherKycStatusApi() {
        handleProgress(0, "");
        FetchStudentPrefReqModel reqModel = new FetchStudentPrefReqModel();
        reqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        // reqModel.setUserId("576232");
        kycViewModel.checkInternet(Status.TEACHER_KYC_STATUS_API, reqModel);
    }

    private void observeServiceResponse() {

        kycViewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    if (isVisible()) {
                        if (responseApi.apiTypeStatus == Status.TEACHER_KYC_STATUS_API) {
                            teacherKycStatusResModel = (TeacherKycStatusResModel) responseApi.data;
                            if (teacherKycStatusResModel.getError()) {
                                handleProgress(2, teacherKycStatusResModel.getMessage());
                            } else {
                                handleProgress(1, "");
                                setAdapter();
                                AppLogger.e("TEACHER_KYC_STATUS_API-", teacherKycStatusResModel.getKycStatus());

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
                    callTeacherKycStatusApi();
                }
            });
        }
    }


}
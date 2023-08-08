package com.auro.application.kyc.presentation.view.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.databinding.FragmentKycNewScreenBinding;
import com.auro.application.home.presentation.viewmodel.KYCViewModel;
import com.auro.application.kyc.presentation.view.activity.KycActivity;
import com.auro.application.util.AppLogger;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KycNewScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KycNewScreenFragment extends BaseFragment {


    @Inject
    @Named("KycNewScreenFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "KycNewScreenFragment";
    FragmentKycNewScreenBinding binding;
    KYCViewModel kycViewModel;
    ObjectAnimator animator_1;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Map<Integer, Integer> animationlist;
    int valueCountImage = 1;

    public KycNewScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KycNewScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KycNewScreenFragment newInstance(String param1, String param2) {
        KycNewScreenFragment fragment = new KycNewScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
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
        kycScannerBanner(binding.scannerLayout, binding.scannerBar);
        kycScannerBanner(binding.relativeLayout2, binding.scannerLayout);
        openFragment(new UploadDocumentFragment());
        startAnimation();
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {

        binding.languageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((KycActivity) getActivity()).openChangeLanguageDialog();
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_kyc_new_screen;
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


    private void openFragment(Fragment fragment) {

        FragmentUtil.addFragment(getContext(), fragment, R.id.mainFrame, 0);

    }

}
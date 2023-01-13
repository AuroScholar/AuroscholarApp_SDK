package com.auro.application.teacher.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.databinding.FragmentSliderBinding;
import com.auro.application.teacher.presentation.viewmodel.SliderViewModel;
import com.auro.application.util.AppLogger;

import javax.inject.Inject;
import javax.inject.Named;

public class SlideFragment extends BaseFragment {

    @Inject
    @Named("SlideFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "SlideFragment";
    FragmentSliderBinding binding;
    private SliderViewModel viewModel;

    private static final String ARG_SECTION_NUMBER = "section_number";
    @StringRes
    private static final int[] PAGE_IMAGE =
            new int[] {
                    R.drawable.ic_view_pager, R.drawable.ic_view_pager, R.drawable.ic_view_pager
            };

    public static SlideFragment newInstance(int index) {
        SlideFragment fragment = new SlideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SliderViewModel.class);
       // viewModel = ViewModelProviders.of(this).get(SliderViewModel.class);
        int index = 1;
        AppLogger.v("View_pager","SlideFragment index"+(index));
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        viewModel.setIndex(index);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setListener();
        setToolbar();
        return binding.getRoot();


       /* View root = inflater.inflate(R.layout.fragment_slider, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        final ImageView imageView = root.findViewById(R.id.imageView);
        return root;*/
    }

    @Override
    protected void init() {
        AppLogger.v("View_pager","SlideFragment Integer");
        viewModel.getText().observe(getActivity(), new Observer<Integer>() {
            @Override public void onChanged(Integer index) {
             //   textView.setText(PAGE_TITLES[index]);
                AppLogger.v("View_pager","SlideFragment Integer"+(index));
                binding.imageViewSlider.setImageResource(PAGE_IMAGE[index]);
            }
        });
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_slider;
    }
}

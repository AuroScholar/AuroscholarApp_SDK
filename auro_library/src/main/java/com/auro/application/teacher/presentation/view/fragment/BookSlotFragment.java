package com.auro.application.teacher.presentation.view.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.FragmentBookSlotBinding;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.teacher.presentation.view.adapter.TeacherProgressAdapter;
import com.auro.application.teacher.presentation.view.adapter.ViewPagerBookingAdapter;
import com.auro.application.teacher.presentation.viewmodel.TeacherInfoViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.alert_dialog.CustomProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

public class BookSlotFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {
    @Inject
    @Named("BookSlotFragment")
    ViewModelFactory viewModelFactory;
    FragmentBookSlotBinding binding;
    TeacherInfoViewModel viewModel;
    boolean isStateRestore;
    BottomSheetBehavior bottomSheetBehavior;
    CustomProgressDialog customProgressDialog;
    Resources resources;
    ViewPagerBookingAdapter viewPagerAdapter;

    TeacherProgressAdapter mteacherProgressAdapter;
    View mIndicator;
    private int indicatorWidth;


    public BookSlotFragment() {
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
        if (binding != null) {
            isStateRestore = true;
            return binding.getRoot();
        }
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TeacherInfoViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        return binding.getRoot();
    }

    @Override
    protected void init() {
        setListener();
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomsheet.bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        for (int i = 0; i < getTabList().size(); i++) {
            binding.bottomsheet.tab.addTab(binding.bottomsheet.tab.newTab().setCustomView(getViewForEachTab(i)));
        }

        initialiseTabs();


    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        HomeActivity.setListingActiveFragment(HomeActivity.TEACHER_BOOK_SLOT_GROUP_FRAGMENT);
        binding.backButton.setOnClickListener(this);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }

    }

    private void openProgressDialog() {
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(getActivity());
        customDialogModel.setTitle(getActivity().getResources().getString(R.string.getting_webinar_slots));
        // customDialogModel.setContent(getActivity().getResources().getString(R.string.bullted_list));
        customDialogModel.setTwoButtonRequired(false);
        try {
            customProgressDialog = new CustomProgressDialog(customDialogModel);
            Objects.requireNonNull(customProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customProgressDialog.setCancelable(false);
            customProgressDialog.show();
            customProgressDialog.setProgressDialogText(getActivity().getResources().getString(R.string.getting_webinar_slots));
        } catch (Exception e) {

        }

        // customProgressDialog.updateDataUi(0);
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {

                case LOADING:

                    break;

                case SUCCESS:

                    break;

                case FAIL:
                    break;
                case NO_INTERNET:


                    break;

                default:


                    break;
            }

        });
    }


    public void handleProgress(int status) {
        switch (status) {
            case 0:

                break;
            case 1:

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resources = ViewUtil.getCustomResource(getActivity());

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_book_slot;
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case WEBINAR_CLICK:
                viewModel.checkInternet("", Status.GET_ZOHO_APPOINTMENT);
                break;
        }
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.e("chhonker", "fragment requestCode=" + requestCode);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_button) {
            getActivity().onBackPressed();
        }
    }


    public void initialiseTabs() {

        setViewPagerAdapter();
        binding.bottomsheet.tab.setSelectedTabIndicatorHeight(0);

        binding.bottomsheet.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setSelectedTabText(tab.getPosition());
                if (binding.bottomsheet.tab.getSelectedTabPosition() == 0) {
                    setTabBG(R.drawable.book_slot_tab_bg, R.drawable.tab_bg);
                } else {
                    setTabBG(R.drawable.tab_bg, R.drawable.book_slot_tab_bg);
                }
                binding.bottomsheet.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do code here

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do code here
                if (binding.bottomsheet.tab.getSelectedTabPosition() == 0) {
                    setSelectedTabText(0);
                    setTabBG(R.drawable.book_slot_tab_bg, R.drawable.tab_bg);
                    // binding.bottomsheet.tab.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.transparent));

                }

            }
        });
        setTabBG(R.drawable.book_slot_tab_bg, R.drawable.tab_bg);
        setSelectedTabText(0);
        //  binding.bottomsheet.tab.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.blue_color));
    }

    public void setViewPagerAdapter() {

        viewPagerAdapter = new ViewPagerBookingAdapter(getActivity(), getChildFragmentManager(), binding.bottomsheet.tab.getTabCount());
        binding.bottomsheet.viewPager.setAdapter(viewPagerAdapter);
        binding.bottomsheet.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.bottomsheet.tab));

      /*  viewPagerAdapter = new ViewPagerBookingAdapter(getActivity(), getChildFragmentManager(),2);
        viewPagerAdapter.addFragment(BookedSlotListFragment.newInstance(), "Booked Slot");
        viewPagerAdapter.addFragment(UpCommingBookFragment.newInstance(), "Upcoming Slot");
        binding.bottomsheet.viewPager.setAdapter(viewPagerAdapter);
        binding.bottomsheet.tab.setupWithViewPager(binding.bottomsheet.viewPager);
        binding.bottomsheet.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.bottomsheet.tab));*/
    }

    public void setPositonInViewpager(int positon) {
        binding.bottomsheet.viewPager.setCurrentItem(positon);

    }

    private void setSelectedTabText(int pos) {
        for (int i = 0; i < binding.bottomsheet.tab.getTabCount(); i++) {
            View view = binding.bottomsheet.tab.getTabAt(i).getCustomView();
            TextView text = view.findViewById(R.id.text_one);
            //text.setTextSize(getResources().getDimension(R.dimen._10sdp));
            if (i == pos) {
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Poppins-Regular.ttf");
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                text.setTypeface(face);

            } else {
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Poppins-Regular.ttf");
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                text.setTypeface(face);

            }
        }
    }

    public List<String> getTabList() {
        List<String> tabList = new ArrayList<>();
        tabList.add("Booked Slot");
        tabList.add("Upcoming Slot");
        return tabList;
    }

    private View getViewForEachTab(int tabNo) {
        View view = getLayoutInflater().inflate(R.layout.tab_teacher_layout, null);
        TextView tabTitle = view.findViewById(R.id.text_one);
        //tabTitle.setTextSize(8);
        tabTitle.setText(getTabList().get(tabNo));

        return view;

    }

    private void setTabBG(int tab1, int tab2) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ViewGroup tabStrip = (ViewGroup) binding.bottomsheet.tab.getChildAt(0);
            View tabView1 = tabStrip.getChildAt(0);
            View tabView2 = tabStrip.getChildAt(1);
            if (tabView1 != null) {
                int paddingStart = tabView1.getPaddingStart();
                int paddingTop = tabView1.getPaddingTop();
                int paddingEnd = tabView1.getPaddingEnd();
                int paddingBottom = tabView1.getPaddingBottom();
                ViewCompat.setBackground(tabView1, AppCompatResources.getDrawable(tabView1.getContext(), tab1));
                ViewCompat.setPaddingRelative(tabView1, paddingStart, paddingTop, paddingEnd, paddingBottom);
            }

            if (tabView2 != null) {
                int paddingStart = tabView2.getPaddingStart();
                int paddingTop = tabView2.getPaddingTop();
                int paddingEnd = tabView2.getPaddingEnd();
                int paddingBottom = tabView2.getPaddingBottom();
                ViewCompat.setBackground(tabView2, AppCompatResources.getDrawable(tabView2.getContext(), tab2));
                ViewCompat.setPaddingRelative(tabView2, paddingStart, paddingTop, paddingEnd, paddingBottom);
            }
        }
    }


    public void openFragment(Fragment fragment) {
        FragmentUtil.addFragment(getContext(), fragment, R.id.bookFrame, 0);
    }

    public void replaceFragment(Fragment fragment) {

        FragmentUtil.replaceFragment(getContext(), fragment, R.id.bookFrame, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }

}

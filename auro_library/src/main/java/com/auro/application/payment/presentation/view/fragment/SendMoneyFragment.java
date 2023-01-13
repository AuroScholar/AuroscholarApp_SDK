package com.auro.application.payment.presentation.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.SendMoneyFragmentLayoutBinding;
import com.auro.application.home.data.model.response.InstructionsResModel;
import com.auro.application.home.data.model.response.StudentWalletResModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.fragment.KYCViewFragment;
import com.auro.application.payment.presentation.view.adapter.ViewPagerAdapter;
import com.auro.application.payment.presentation.viewmodel.SendMoneyViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.disclaimer.DisclaimerMoneyTransferDialog;
import com.auro.application.util.strings.AppStringDynamic;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;


public class SendMoneyFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {


    @Inject
    @Named("SendMoneyFragment")
    ViewModelFactory viewModelFactory;
    SendMoneyFragmentLayoutBinding binding;
    SendMoneyViewModel viewModel;
    ViewPagerAdapter viewPagerAdapter;
    StudentWalletResModel studentWalletResModel;
    private static final String TAG = "SendMoneyFragment";
    private String comingFrom;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setLanguageonUi(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SendMoneyViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        AppUtil.loadAppLogo(binding.auroScholarLogo, getActivity());
        return binding.getRoot();
    }


    @Override
    protected void init() {
        if (getArguments() != null) {
            comingFrom = getArguments().getString(AppConstant.COMING_FROM);
            studentWalletResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
        }
        for (int i = 0; i < getTabList().size(); i++) {
            binding.dineHomeTabs.addTab(binding.dineHomeTabs.newTab().setCustomView(getViewForEachTab(i)));
        }
      /*  if (comingFrom != null && comingFrom.equalsIgnoreCase(AppConstant.SENDING_DATA.DYNAMIC_LINK)) {
            handleNavigationProgress(0, "");
            AppLogger.i(TAG, "Log DynamicLink");
            ((DashBoardMainActivity) getActivity()).setListner(this);
            ((DashBoardMainActivity) getActivity()).callDashboardApi();
        } else {
            initialiseTabs();
        }*/
        initialiseTabs();
        ViewUtil.setProfilePic(binding.imageView6);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.e("chhonker  sendmoney init --", "" + prefModel.isDashboardaApiNeedToCall());
        AppStringDynamic.setMoneyTransferPageStrings(binding);

        callGetInstructionsApi();


    }

    void callGetInstructionsApi() {
        ((DashBoardMainActivity) getActivity()).setListner(this);
        ((DashBoardMainActivity) getActivity()).callGetInstructionsApi(AppConstant.InstructionsType.SCHOLARSHIP_TRANSFER);
    }


    public void initialiseTabs() {

        setViewPagerAdapter();
        binding.dineHomeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setSelectedTabText(tab.getPosition());
                binding.dineHomeTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.blue_color));
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do code here

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do code here
                if (binding.dineHomeTabs.getSelectedTabPosition() == 0) {
                    setSelectedTabText(0);
                    binding.dineHomeTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.blue_color));

                }

            }
        });
        setSelectedTabText(0);
        binding.dineHomeTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.blue_color));
    }


    public void setViewPagerAdapter() {
        viewPagerAdapter = new ViewPagerAdapter(getActivity(), getChildFragmentManager(), binding.dineHomeTabs.getTabCount(), studentWalletResModel);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.dineHomeTabs));
    }

    private View getViewForEachTab(int tabNo) {
        View view = getLayoutInflater().inflate(R.layout.tab_text_layout, null);
        TextView tabTitle = view.findViewById(R.id.text_one);
        //tabTitle.setTextSize(8);
        tabTitle.setText(getTabList().get(tabNo));
        return view;
    }

    private void setSelectedTabText(int pos) {
        for (int i = 0; i < binding.dineHomeTabs.getTabCount(); i++) {
            View view = binding.dineHomeTabs.getTabAt(i).getCustomView();
            TextView text = view.findViewById(R.id.text_one);
            if (i == pos) {
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Poppins-Regular.ttf");
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.blue_color));
                text.setTypeface(face);
            } else {
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Poppins-Regular.ttf");
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                text.setTypeface(face);
            }
        }
    }


    public List<String> getTabList() {
        List<String> tabList = new ArrayList<>();
        tabList.add("Paytm");
        //tabList.add("UPI");
        tabList.add("Bank");
        return tabList;
    }


    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.SEND_MONEY_FRAGMENT);
        binding.backButton.setOnClickListener(this);
        binding.cardView2.setOnClickListener(this);

    }


    @Override
    protected int getLayout() {
        return R.layout.send_money_fragment_layout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
        }
        init();
        setToolbar();
        setListener();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((DashBoardMainActivity) getActivity()).setProgressVal();


        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.e("chhonker  sendmoney onResume --", "" + prefModel.isDashboardaApiNeedToCall());
        AppLogger.e(TAG, "onResume calling");

    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        AppLogger.e("GET_INSTRUCTIONS_API--", "LISTNER_SUCCESS step 1");
        switch (commonDataModel.getClickType()) {
            case LISTNER_SUCCESS:
                handleNavigationProgress(1, "");
                studentWalletResModel = (StudentWalletResModel) commonDataModel.getObject();
                //((DashBoardMainActivity) getActivity()).dashboardModel(studentWalletResModel);
                initialiseTabs();
                break;
            case LISTNER_FAIL:
                handleNavigationProgress(2, (String) commonDataModel.getObject());
                break;

            case GET_INSTRUCTIONS_API:
                if (isVisible()) {
                    AppLogger.e("GET_INSTRUCTIONS_API--", "LISTNER_SUCCESS step 2");
                    InstructionsResModel instructionsResModel = (InstructionsResModel) commonDataModel.getObject();
                    checkDisclaimerMoneyDialog(instructionsResModel);
                }
                break;
        }
    }


    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_button) {
            getActivity().onBackPressed();
        } else if (id == R.id.cardView2) {
            ((DashBoardMainActivity) getActivity()).openProfileFragment();
        }


    }

    private void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    public void openKycMoneyFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
        Bundle bundle = new Bundle();
        KYCViewFragment sendMoneyFragment = new KYCViewFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, studentWalletResModel);
        sendMoneyFragment.setArguments(bundle);
        openFragment(sendMoneyFragment);
    }

    private void openFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(AuroApp.getFragmentContainerUiId(), fragment, KYCViewFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();

    }


    public void onBackPressed() {
        // getActivity().getSupportFragmentManager().popBackStack();
        getActivity().onBackPressed();
    }

    public void backButton() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setDashboardaApiNeedToCall(true);
        AuroAppPref.INSTANCE.setPref(prefModel);
        PrefModel prefModel2 = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.e("chhonker backButton --", "" + prefModel2.isDashboardaApiNeedToCall());
        ((DashBoardMainActivity) getActivity()).selectNavigationMenu(0);
        ((DashBoardMainActivity) getActivity()).auroStudentscholarSdk(0);
    }

    private void handleNavigationProgress(int status, String msg) {
        if (status == 0) {
            binding.transparet.setVisibility(View.VISIBLE);
            binding.paymenttransfer.setVisibility(View.VISIBLE);

            binding.errorConstraint.setVisibility(View.GONE);
        } else if (status == 1) {
            binding.transparet.setVisibility(View.GONE);
            binding.paymenttransfer.setVisibility(View.VISIBLE);

            binding.errorConstraint.setVisibility(View.GONE);
        } else if (status == 2) {
            binding.transparet.setVisibility(View.GONE);
            binding.paymenttransfer.setVisibility(View.GONE);

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

    private void checkDisclaimerMoneyDialog(InstructionsResModel instructionsResModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (!prefModel.isPreMoneyTransferDisclaimer()) {
            DisclaimerMoneyTransferDialog askDetailCustomDialog = new DisclaimerMoneyTransferDialog(getActivity());
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(askDetailCustomDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            askDetailCustomDialog.getWindow().setAttributes(lp);
            Objects.requireNonNull(askDetailCustomDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            askDetailCustomDialog.setCancelable(false);
            askDetailCustomDialog.show();
            askDetailCustomDialog.setMessage(instructionsResModel);
        }
    }

}

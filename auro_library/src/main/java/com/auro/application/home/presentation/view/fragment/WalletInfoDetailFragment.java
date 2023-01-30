package com.auro.application.home.presentation.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.FragmentWalletInfoDetailBinding;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.SetPasswordReqModel;
import com.auro.application.home.data.model.WalletResponseAmountResModel;
import com.auro.application.home.data.model.response.StudentWalletResModel;

import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.adapter.WalletAdapter;
import com.auro.application.home.presentation.viewmodel.WalletAmountViewModel;
import com.auro.application.payment.presentation.view.fragment.SendMoneyFragment;
import com.auro.application.util.AppLogger;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;


public class WalletInfoDetailFragment extends BaseFragment implements View.OnClickListener, CommonCallBackListner {

    @Inject
    @Named("WalletInfoDetailFragment")
    ViewModelFactory viewModelFactory;
    FragmentWalletInfoDetailBinding binding;
    WalletAmountViewModel viewModel;
    StudentWalletResModel studentWalletResModel;
    WalletAdapter walletAdapter;

    public WalletInfoDetailFragment() {
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
      //  ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        DaggerWrapper.getComponent(getActivity()).doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WalletAmountViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        ViewUtil.setLanguageonUi(getActivity());
        init();
        setToolbar();
        setListener();
        return binding.getRoot();
    }

    @Override
    protected void init() {
        setAdapter();
        ViewUtil.setProfilePic(binding.imageView6);
        callGetWalletStatus();
        AppStringDynamic.setWalletInfoScreenStrings(binding);
    }

    public List<WalletResponseAmountResModel> getListWallet() {

        try {

            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();

            String rs = getActivity().getResources().getString(R.string.rs);
            WalletResponseAmountResModel model1 = new WalletResponseAmountResModel();
            if (!details.getWlAmountRejected().isEmpty()) {
                model1.setText(details.getWlAmountRejected());
            } else {
                model1.setText(getString(R.string.wl_amount_rejected));
            }

            model1.setDrawable(getResources().getDrawable(R.drawable.wallet_card_reject_background));
            if (studentWalletResModel != null && !TextUtil.isEmpty("" + studentWalletResModel.getDisapprovedScholarshipMoney())) {
                model1.setAmount(rs + studentWalletResModel.getDisapprovedScholarshipMoney());
            } else {
                model1.setAmount(rs + "0");
            }

            WalletResponseAmountResModel model2 = new WalletResponseAmountResModel();
            if (!details.getWlVerificationInProcess().isEmpty()) {
                model2.setText(details.getWlVerificationInProcess());
            } else {
                model2.setText(getString(R.string.wl_verification_in_process));
            }
            model2.setDrawable(getResources().getDrawable(R.drawable.wallet_card_process_background));
            if (studentWalletResModel != null && !TextUtil.isEmpty("" + studentWalletResModel.getUnapprovedScholarshipMoney())) {
                model2.setAmount(rs + studentWalletResModel.getUnapprovedScholarshipMoney());
            } else {
                model2.setAmount(rs + "0");
            }

            WalletResponseAmountResModel model3 = new WalletResponseAmountResModel();

            if (!details.getWlScholarshipAmountaApproved().isEmpty()) {
                model3.setText(details.getWlScholarshipAmountaApproved());
            } else {
                model3.setText(getString(R.string.wl_scholarship_amounta_approved));
            }

            model3.setDrawable(getResources().getDrawable(R.drawable.wallet_card_approve_background));
            if (studentWalletResModel != null && !TextUtil.isEmpty("" + studentWalletResModel.getApprovedScholarshipMoney())) {
                model3.setAmount(rs + studentWalletResModel.getApprovedScholarshipMoney());
            } else {
                model3.setAmount(rs + "0");
            }

            WalletResponseAmountResModel model4 = new WalletResponseAmountResModel();
            if (!details.getWlPaymentInProcess().isEmpty()) {
                model4.setText(details.getWlPaymentInProcess());
            } else {
                model4.setText(getString(R.string.wl_payment_in_process));
            }
            model4.setDrawable(getResources().getDrawable(R.drawable.wallet_card_payment_process_background));

            if (studentWalletResModel != null && !TextUtil.isEmpty("" + studentWalletResModel.getInprocessScholarshipMoney())) {
                model4.setAmount(rs + studentWalletResModel.getInprocessScholarshipMoney());
            } else {
                model4.setAmount(rs + "0");
            }

            WalletResponseAmountResModel model5 = new WalletResponseAmountResModel();
            if (!details.getWlScholarshipDisbursed().isEmpty()) {
                model5.setText(details.getWlScholarshipDisbursed());
            } else {
                model5.setText(getString(R.string.wl_scholarship_disbursed));
            }
            model5.setDrawable(getResources().getDrawable(R.drawable.wallet_card_disbursed_background));
            if (studentWalletResModel != null && !TextUtil.isEmpty("" + studentWalletResModel.getDisburseScholarshipMoney())) {
                model5.setAmount(rs + studentWalletResModel.getDisburseScholarshipMoney());
            } else {
                model5.setAmount(rs + "0");
            }
            List<WalletResponseAmountResModel> list = new ArrayList<>();
            list.add(model1);
            list.add(model2);
            list.add(model3);
            list.add(model4);
            list.add(model5);
            return list;
        } catch (Exception e) {

        }
        return Collections.EMPTY_LIST;
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.WALLET_INFO_FRAGMENT);
        binding.cardView2.setOnClickListener(this);
        binding.languageLayout.setOnClickListener(this);
        binding.btTransferMoney.setOnClickListener(this);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);

        } else {
            observeServiceResponse();
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_wallet_info_detail;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.cardView2) {
            ((DashBoardMainActivity) getActivity()).openProfileFragment();
            ((DashBoardMainActivity) getActivity()).closeItemMore();
            ((DashBoardMainActivity) getActivity()).selectNavigationMenu(2);
        } else if (id == R.id.language_layout) {
            ((DashBoardMainActivity) getActivity()).openChangeLanguageDialog();
        } else if (id == R.id.bt_transfer_money) {
            openSendMoneyFragment();
        }
    }


    public void openSendMoneyFragment() {

        Bundle bundle = new Bundle();
        SendMoneyFragment sendMoneyFragment = new SendMoneyFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, studentWalletResModel);
        sendMoneyFragment.setArguments(bundle);
        openFragment(sendMoneyFragment);
    }

    private void openFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(AuroApp.getFragmentContainerUiId(), fragment, KYCFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();

    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

    }

    public void setAdapter() {
        // List<CertificateResModel> list = viewModel.homeUseCase.makeCertificateList();
        binding.RvwalletAmount.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.RvwalletAmount.setHasFixedSize(true);
        walletAdapter = new WalletAdapter(getActivity(), getListWallet(), this);
        binding.RvwalletAmount.setAdapter(walletAdapter);
    }

    private void observeServiceResponse() {
        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    studentWalletResModel = (StudentWalletResModel) responseApi.data;
                    if (studentWalletResModel.getError()) {
                        handleProgress(2, studentWalletResModel.getMessage());
                        binding.btTransferMoney.setVisibility(View.GONE);

                    } else {
                        handleProgress(1, "");
                        setDataOnUi(studentWalletResModel);
                        if (studentWalletResModel.getApprovedScholarshipMoney() > 0) {
                            binding.btTransferMoney.setVisibility(View.VISIBLE);
                        } else {
                            binding.btTransferMoney.setVisibility(View.GONE);
                        }
                    }
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                default:
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                    }
                    break;
            }

        });
    }

    private void callGetWalletStatus() {
        AppLogger.e("callGetWalletStatus-", "step 1");
        handleProgress(0, "");
        SetPasswordReqModel reqModel = new SetPasswordReqModel();
        reqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getUserId());
        reqModel.setUserPreferedLanguageId(Integer.valueOf(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        viewModel.checkInternet(reqModel, Status.WALLET_STATUS_API);
    }

    private void handleProgress(int status, String msg) {
        switch (status) {
            case 0:
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.RvwalletAmount.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.GONE);
                break;
            case 1:
                binding.progressBar.setVisibility(View.GONE);
                binding.RvwalletAmount.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                break;

            case 2:
                binding.progressBar.setVisibility(View.GONE);
                binding.RvwalletAmount.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.errorLayout.textError.setText(msg);
                binding.errorLayout.errorIcon.setVisibility(View.GONE);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callGetWalletStatus();
                    }
                });
                break;
            case 3:
                binding.progressBar.setVisibility(View.GONE);
                binding.RvwalletAmount.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.errorLayout.textError.setText(msg);
                binding.errorLayout.errorIcon.setVisibility(View.GONE);
                binding.errorLayout.btRetry.setVisibility(View.GONE);

                break;
        }
    }

    void setDataOnUi(StudentWalletResModel studentWalletResMode) {
        walletAdapter.updateList(getListWallet());
    }
}
package com.auro.application.home.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.KycFragmentLayoutBinding;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.adapter.KYCViewDocAdapter;
import com.auro.application.home.presentation.viewmodel.KYCViewModel;
import com.auro.application.payment.presentation.view.fragment.SendMoneyFragment;
import com.auro.application.util.AppLogger;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.disclaimer.DisclaimerKycDialog;
import com.auro.application.util.strings.AppStringDynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import static com.auro.application.core.common.Status.AZURE_API;


public class KYCViewFragment extends BaseFragment implements View.OnClickListener, CommonCallBackListner, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    @Named("KYCViewFragment")
    ViewModelFactory viewModelFactory;


    KycFragmentLayoutBinding binding;
    String TAG = "KYCViewFragment";
    KYCViewModel kycViewModel;
    KYCViewDocAdapter kycViewDocAdapter;

    private DashboardResModel dashboardResModel;
    ArrayList<KYCDocumentDatamodel> kycDocumentDatamodelArrayList;
    Resources resources;

    private String comingFrom;


    /*Face Image Params*/
    List<AssignmentReqModel> faceModelList;
    int faceCounter = 0;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        kycViewModel = ViewModelProviders.of(this, viewModelFactory).get(KYCViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setKycViewModel(kycViewModel);
        setRetainInstance(true);
        ViewUtil.setLanguageonUi(getActivity());
        // checkDisclaimerKYCDialog();
        return binding.getRoot();
    }

    public void setAdapter() {
        this.kycDocumentDatamodelArrayList = kycViewModel.homeUseCase.makeAdapterDocumentList(dashboardResModel, getActivity());
        binding.documentUploadRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.documentUploadRecyclerview.setHasFixedSize(true);
        kycViewDocAdapter = new KYCViewDocAdapter(getActivity(), kycViewModel.homeUseCase.makeAdapterDocumentList(dashboardResModel, getActivity()));
        binding.documentUploadRecyclerview.setAdapter(kycViewDocAdapter);
    }

    @Override
    protected void init() {
        if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
            comingFrom = getArguments().getString(AppConstant.COMING_FROM);
        }
        binding.btUploadAll.setVisibility(View.GONE);
        AppStringDynamic.setKycStrings(binding);
        if (comingFrom != null && comingFrom.equalsIgnoreCase(AppConstant.SENDING_DATA.DYNAMIC_LINK)) {
            handleNavigationProgress(0, "");
            DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.KYC_DIRECT_FRAGMENT);
            AppLogger.i(TAG, "Log DynamicLink");
            ((DashBoardMainActivity) getActivity()).setListner(this);
            ((DashBoardMainActivity) getActivity()).callDashboardApi();
        } else {
            DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.QUIZ_KYC_VIEW_FRAGMENT);
            setDataOnUi();
        }


    }

    private void setDataOnUi() {
        if (dashboardResModel != null) {
            if (!TextUtil.isEmpty(dashboardResModel.getWalletbalance())) {
                //binding.walletBalText.setText(getString(R.string.rs) + " " + dashboardResModel.getWalletbalance());
                binding.walletBalText.setText(getString(R.string.rs) + " " + kycViewModel.homeUseCase.getWalletBalance(dashboardResModel));
            }
            setDataStepsOfVerifications();
        }
        binding.cambridgeHeading.cambridgeHeading.setTextColor(AuroApp.getAppContext().getResources().getColor(R.color.white));


    }

    private void setLanguageText(String text) {
        binding.toolbarLayout.langEng.setText(text);
    }

    @Override
    protected void setToolbar() {
        /*Do code here*/
    }

    @Override
    protected void setListener() {
        /*Do code here*/
        binding.toolbarLayout.backArrow.setVisibility(View.VISIBLE);
        binding.toolbarLayout.backArrow.setOnClickListener(this);
        binding.btModifyAll.setOnClickListener(this);
        binding.walletInfo.setOnClickListener(this);
        binding.toolbarLayout.langEng.setOnClickListener(this);
        binding.swipeRefreshLayout.setOnRefreshListener(this);

        if (kycViewModel != null && kycViewModel.serviceLiveData().hasObservers()) {
            kycViewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }


    @Override
    protected int getLayout() {
        return R.layout.kyc_fragment_layout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setToolbar();
        setListener();
        setAdapter();

        checkForFaceImage();
    }


    private void checkForFaceImage() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel != null && !TextUtil.checkListIsEmpty(prefModel.getListAzureImageList()) && prefModel.getListAzureImageList().size() > 0) {
            faceModelList = prefModel.getListAzureImageList();
            if (faceModelList.get(0) != null) {
                kycViewModel.sendAzureImageData(faceModelList.get(0));
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setKeyListner();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_modify_all) {
            openKYCFragment();
        } else if (v.getId() == R.id.lang_eng) {
            reloadFragment();
        } else if (v.getId() == R.id.back_arrow) {
            getActivity().onBackPressed();
        } else if (v.getId() == R.id.bt_transfer_money) {
            //callNumber();
            openSendMoneyFragment();
        } else if (v.getId() == R.id.wallet_info) {

            openWalletInfoFragment();
        }
    }

    public void openSendMoneyFragment() {
        // getActivity().getSupportFragmentManager().popBackStack();
        //funnel
        Bundle bundle = new Bundle();
        SendMoneyFragment sendMoneyFragment = new SendMoneyFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        sendMoneyFragment.setArguments(bundle);
        openFragment(sendMoneyFragment);
    }


    private void openTransactionFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        TransactionsFragment fragment = new TransactionsFragment();
        fragment.setArguments(bundle);
        openFragment(fragment);
    }

    private void openWalletInfoFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        WalletInfoDetailFragment fragment = new WalletInfoDetailFragment();
        fragment.setArguments(bundle);
        openFragment(fragment);
    }

    public void callNumber() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:9667480783"));
        startActivity(callIntent);
    }


    private void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    public void openKYCFragment() {
        dashboardResModel.setModify(true);
        Bundle bundle = new Bundle();
        KYCFragment kycFragment = new KYCFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        kycFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().popBackStack();
        openFragment(kycFragment);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        // CustomSnackBar.INSTANCE.dismissCartSnackbar();
    }

    private void setDataStepsOfVerifications() {
        try {

       /* dashboardResModel.setIs_kyc_uploaded("Yes");
        dashboardResModel.setIs_kyc_verified("Rejected");
        dashboardResModel.setIs_payment_lastmonth("Yes");*/
            Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
            AppLogger.e("chhonker step ", "kyc Step 1");
            if (dashboardResModel == null) {
                return;
            }
            if (!TextUtil.isEmpty(dashboardResModel.getIs_kyc_uploaded()) && dashboardResModel.getIs_kyc_uploaded().equalsIgnoreCase(AppConstant.DocumentType.YES)) {
                AppLogger.e("chhonker step ", "kyc Step 2");
                binding.stepOne.tickSign.setVisibility(View.VISIBLE);
                binding.stepOne.textUploadDocumentMsg.setText(R.string.document_uploaded);
                binding.stepOne.textUploadDocumentMsg.setText(details.getDocumentUploaded());
                binding.btModifyAll.setVisibility(View.VISIBLE);
                binding.stepOne.textUploadDocumentMsg.setTextColor(ContextCompat.getColor(getActivity(), R.color.ufo_green));
                if (!TextUtil.isEmpty(dashboardResModel.getIs_kyc_verified()) && dashboardResModel.getIs_kyc_verified().equalsIgnoreCase(AppConstant.DocumentType.IN_PROCESS)) {
                    AppLogger.e("chhonker step ", "kyc Step 3");
                    binding.stepTwo.textVerifyMsg.setText(getString(R.string.verification_is_in_process));
                    binding.stepTwo.textVerifyMsg.setText(details.getVerificationIsInProcess());
                    binding.stepTwo.textVerifyMsg.setVisibility(View.VISIBLE);
                    binding.btModifyAll.setVisibility(View.VISIBLE);
                } else if (!TextUtil.isEmpty(dashboardResModel.getIs_kyc_verified()) &&
                        dashboardResModel.getIs_kyc_verified().equalsIgnoreCase(AppConstant.DocumentType.APPROVE)) {
                    AppLogger.e("chhonker step ", "kyc Step 4");
                    binding.stepTwo.textVerifyMsg.setText(R.string.document_verified);
                    binding.stepTwo.textVerifyMsg.setText(details.getDocumentVerified());
                    binding.stepTwo.textVerifyMsg.setVisibility(View.VISIBLE);
                    binding.stepTwo.tickSign.setVisibility(View.VISIBLE);
                    binding.btModifyAll.setVisibility(View.GONE);
                    binding.stepTwo.textVerifyMsg.setTextColor(ContextCompat.getColor(getActivity(), R.color.ufo_green));
                    int approvedMoney = ConversionUtil.INSTANCE.convertStringToInteger(dashboardResModel.getApproved_scholarship_money());

                    if (approvedMoney < 1) {
                        AppLogger.e("chhonker step ", "kyc Step 5");
                  /*  binding.stepThree.tickSign.setVisibility(View.GONE);
                    binding.stepThree.textQuizVerifyMsg.setText(AuroApp.getAppContext().getResources().getString(R.string.scholarship_approved));
                    binding.stepFour.textTransferMsg.setText(R.string.successfully_transfered);
                    binding.stepFour.textTransferMsg.setTextColor(ContextCompat.getColor(getActivity(), R.color.ufo_green));
                    binding.stepFour.tickSign.setVisibility(View.GONE);
                    binding.stepFour.btTransferMoney.setVisibility(View.GONE);*/
                    } else {
                        AppLogger.e("chhonker step ", "kyc Step 6");
                        binding.stepThree.tickSign.setVisibility(View.VISIBLE);
                        binding.stepThree.textQuizVerifyMsg.setText(getActivity().getResources().getString(R.string.scholarship_approved));
                        binding.stepThree.textQuizVerifyMsg.setText(details.getScholarshipApproved());
                        binding.stepFour.textTransferMsg.setTextColor(ContextCompat.getColor(getActivity(), R.color.ufo_green));
                        binding.stepFour.textTransferMsg.setText(R.string.call_our_customercare);
                        binding.stepFour.textTransferMsg.setText(details.getCallOurCustomercare());
                        binding.stepFour.tickSign.setVisibility(View.VISIBLE);
                        binding.stepFour.btTransferMoney.setVisibility(View.VISIBLE);
                        binding.stepFour.btTransferMoney.setOnClickListener(this);
                    }
                } else if (!TextUtil.isEmpty(dashboardResModel.getIs_kyc_verified()) && (dashboardResModel.getIs_kyc_verified().equalsIgnoreCase(AppConstant.DocumentType.REJECTED) || dashboardResModel.getIs_kyc_verified().equalsIgnoreCase(AppConstant.DocumentType.DISAPPROVE))) {
                    AppLogger.e("chhonker step ", "kyc Step 7");
                    binding.stepTwo.textVerifyMsg.setText(R.string.declined);
                    binding.stepTwo.textVerifyMsg.setText(details.getDeclined());
                    binding.stepTwo.textVerifyMsg.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_red));
                    binding.stepTwo.textVerifyMsg.setVisibility(View.VISIBLE);
                    binding.stepTwo.tickSign.setVisibility(View.VISIBLE);
                    binding.stepTwo.tickSign.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_cancel_icon));
                    binding.stepFour.textTransferMsg.setTextColor(ContextCompat.getColor(getActivity(), R
                            .color.auro_dark_blue));
                    binding.stepFour.textTransferMsg.setText(R.string.you_will_see_transfer);
                    binding.stepFour.textTransferMsg.setText(details.getYouWillSeeTransfer());
                    binding.stepFour.btTransferMoney.setVisibility(View.GONE);
                    binding.stepFour.tickSign.setVisibility(View.GONE);
                } else if (!TextUtil.isEmpty(dashboardResModel.getIs_kyc_verified()) && dashboardResModel.getIs_kyc_verified().equalsIgnoreCase(AppConstant.DocumentType.PENDING)) {
                    AppLogger.v("pradeep", "kyc Step 7 Kyc Fragment" + dashboardResModel.getIs_kyc_verified());
                    binding.stepTwo.textVerifyMsg.setText(getString(R.string.verification_pending));
                    binding.stepTwo.textVerifyMsg.setText(details.getVerificationPending());
                    binding.stepTwo.textVerifyMsg.setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
//delete code later by pradeep
        //   binding.stepFour.btTransferMoney.setOnClickListener(this);
        //  binding.stepFour.btTransferMoney.setVisibility(View.VISIBLE);

    }

    private void observeServiceResponse() {
        kycViewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {
                case LOADING:
                    /*Do handling in background*/
                    break;

                case SUCCESS:
                    if (responseApi.apiTypeStatus == AZURE_API) {
                        sendFaceImageOnServer();
                    }

                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                default:
                    updateFaceListInPref();
                    break;

            }

        });
    }

    private void sendFaceImageOnServer() throws IllegalStateException {
        if (!TextUtil.checkListIsEmpty(faceModelList)) {
            try {
                faceModelList.get(faceCounter).setUploaded(true);
                faceCounter++;
                if (faceModelList.size() > faceCounter) {
                    kycViewModel.sendAzureImageData(faceModelList.get(faceCounter));
                } else {
                    updateFaceListInPref();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "IndexOutOfBoundsException", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void updateFaceListInPref() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel != null) {
            List<AssignmentReqModel> newList = new ArrayList<>();
            for (AssignmentReqModel model : faceModelList) {
                if (model != null && !model.isUploaded()) {
                    newList.add(model);
                }
            }
            prefModel.setListAzureImageList(newList);
            AuroAppPref.INSTANCE.setPref(prefModel);
        }
    }

    private void setKeyListner() {
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case LISTNER_SUCCESS:
                binding.swipeRefreshLayout.setRefreshing(false);
                handleNavigationProgress(1, "");
                dashboardResModel = (DashboardResModel) commonDataModel.getObject();
                ((DashBoardMainActivity) getActivity()).dashboardModel(dashboardResModel);
                setDataOnUi();
                break;

            case LISTNER_FAIL:
                binding.swipeRefreshLayout.setRefreshing(false);
                handleNavigationProgress(2, (String) commonDataModel.getObject());

                break;
        }
    }

    private void handleNavigationProgress(int status, String msg) {
        if (status == 0) {
            binding.transparet.setVisibility(View.VISIBLE);
            binding.kycbackground.setVisibility(View.VISIBLE);

            binding.errorConstraint.setVisibility(View.GONE);
        } else if (status == 1) {
            binding.transparet.setVisibility(View.GONE);
            binding.kycbackground.setVisibility(View.VISIBLE);

            binding.errorConstraint.setVisibility(View.GONE);
        } else if (status == 2) {
            binding.transparet.setVisibility(View.GONE);
            binding.kycbackground.setVisibility(View.GONE);

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


    private void checkDisclaimerKYCDialog() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (!prefModel.isPreKycDisclaimer()) {
            DisclaimerKycDialog askDetailCustomDialog = new DisclaimerKycDialog(getActivity());
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

    @Override
    public void onRefresh() {
        ((DashBoardMainActivity) getActivity()).setListner(this);
        ((DashBoardMainActivity) getActivity()).callDashboardApi();
    }
}

package com.auro.application.payment.presentation.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.ValidationModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.BankFragmentLayoutBinding;
import com.auro.application.home.data.model.response.StudentWalletResModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.payment.data.model.request.PaytmWithdrawalByBankAccountReqModel;
import com.auro.application.payment.data.model.response.PaytmResModel;
import com.auro.application.payment.presentation.viewmodel.SendMoneyViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.DateUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.CustomDialog;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.alert_dialog.CustomPaymentTranferDialog;
import com.auro.application.util.alert_dialog.CustomProgressDialog;
import com.auro.application.util.strings.AppStringDynamic;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import static com.auro.application.core.common.Status.PAYMENT_TRANSFER_API;
import static com.auro.application.core.common.Status.PAYTM_ACCOUNT_WITHDRAWAL;


public class BankFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {

    @Inject
    @Named("BankFragment")
    ViewModelFactory viewModelFactory;
    BankFragmentLayoutBinding binding;
    SendMoneyViewModel viewModel;
    boolean isStateRestore;
    private final String TAG = "BankFragment";
    CustomProgressDialog customProgressDialog;
    StudentWalletResModel studentWalletResModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setLanguageonUi(getActivity());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SendMoneyViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        return binding.getRoot();
    }


    @Override
    protected void init() {
        if (getArguments() != null) {
            studentWalletResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
        }
        binding.walletBalText.setText("₹" + studentWalletResModel.getApprovedScholarshipMoney() + ".00");
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
        ((DashBoardMainActivity) getActivity()).setProgressVal();
        AppStringDynamic.setBankMoneyTransferPageStrings(binding);
    }


    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        binding.sendButton.setOnClickListener(this);
        setKeyListner();
    }


    @Override
    protected int getLayout() {
        return R.layout.bank_fragment_layout;
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
        AppLogger.e("onResume-", "Bank Fragment");
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case OTP_VERIFY:
                paytmentTransferApi();
                break;
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_button) {
            String accountnumber = binding.accountNumber.getText().toString();
            String confirmaccountnumber = binding.confirmAccountNumber.getText().toString();
            String ifscCode = binding.ifscCode.getText().toString();
            String beneficiary_name = binding.beneficiaryName.getText().toString();
            if (binding.beneficiaryName.getText().toString().startsWith(" ")){
                Toast.makeText(getActivity(), "Can not enter space in beneficiary name", Toast.LENGTH_SHORT).show();
            }
            else    if (binding.ifscCode.getText().toString().startsWith(" ")||binding.ifscCode.getText().toString().endsWith(" ")||binding.ifscCode.getText().toString().contains(" ")){
                Toast.makeText(getActivity(), "Can not enter space in IFSC", Toast.LENGTH_SHORT).show();
            }
            else {
                ValidationModel bankAccountvalidation = viewModel.paymentUseCase.isValidBankAccountNumber(accountnumber, ifscCode, confirmaccountnumber,beneficiary_name);

                if (bankAccountvalidation.isStatus()) {
                    ((DashBoardMainActivity) getActivity()).setListner(this);
                    ((DashBoardMainActivity) getActivity()).sendOtpApiReqPass();
                } else {
                    showSnackbarError(bankAccountvalidation.getMessage());
                }
            }


        }

    }


    private void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }


    private void paytmentTransferApi() {
        String accountnumber = binding.accountNumber.getText().toString();
        String ifscCode = binding.ifscCode.getText().toString();
        String beneficiaryName = binding.beneficiaryName.getText().toString();
        PaytmWithdrawalByBankAccountReqModel reqModel = new PaytmWithdrawalByBankAccountReqModel();
        reqModel.setMobileNo(AuroApp.getAuroScholarModel().getMobileNumber());
        reqModel.setStudentId(AuroAppPref.INSTANCE.getModelInstance().getDashboardResModel().getStudent_id());
            /*reqModel.setMobileNo("9654234507");
            reqModel.setStudentId("4077466");*/
        reqModel.setPaymentMode(AppConstant.PaymenMode.BANK_Transfer);
        reqModel.setDisbursementMonth(DateUtil.getMonthNameForPayment());
        reqModel.setBeneficiary_name(beneficiaryName);
        reqModel.setBankaccountno(accountnumber);
        reqModel.setIfsccode(ifscCode);
        reqModel.setAmount(""+studentWalletResModel.getApprovedScholarshipMoney());
        //  reqModel.setAmount("1");
        reqModel.setPurpose("Payment Transfer");
        viewModel.paytmentTransfer(reqModel);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    //For ProgressBar

                    openProgressDialog();

                    break;

                case SUCCESS:
                    if (responseApi.apiTypeStatus == PAYTM_ACCOUNT_WITHDRAWAL) {
                        closeDialog();
                        PaytmResModel mpaytm = (PaytmResModel) responseApi.data;
                        mpaytm.getResponse().replaceAll("\\\\", "");
                        //openPaymentDialog(mpaytm.getResponse());
                    } else if (responseApi.apiTypeStatus == PAYMENT_TRANSFER_API) {
                        closeDialog();
                        PaytmResModel mpaytm = (PaytmResModel) responseApi.data;
                        openPaymentDialog(mpaytm);
                    }

                    break;

                case NO_INTERNET:

                    closeDialog();
                    showSnackbarError((String) responseApi.data);

                    break;
                case AUTH_FAIL:
                case FAIL_400:

                    closeDialog();
                    showSnackbarError((String) responseApi.data);
                    break;

                default:
                    Log.d(TAG, "observeServiceResponse: default");
                    closeDialog();
                    showSnackbarError((String) responseApi.data);
                    break;
            }

        });
    }

    private void openProgressDialog() {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            return;
        }
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(getActivity());
        customDialogModel.setTitle("Processing your payment...");
        customDialogModel.setTwoButtonRequired(true);
        customProgressDialog = new CustomProgressDialog(customDialogModel);
        Objects.requireNonNull(customProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();
        customProgressDialog.updateDataUi(0);
    }

    public void closeDialog() {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
    }


    private void openPaymentDialog(PaytmResModel resModel) {
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(getActivity());
        customDialogModel.setTitle(getActivity().getResources().getString(R.string.information));
        customDialogModel.setContent(resModel.getMessage());
     /*   if (message.contains(AppConstant.PaytmResponseCode.DE_002)) {
            customDialogModel.setContent(getActivity().getResources().getString(R.string.requested_accepted));
        } else {
            customDialogModel.setContent(getActivity().getResources().getString(R.string.payment_failed_error_msg));
            HashMap<String, String> stringStringHashMap = PaytmError.initMapping();
            for (Map.Entry<String, String> entry : stringStringHashMap.entrySet()) {
                String key = entry.getKey();
                if (message.contains(key)) {
                    customDialogModel.setContent(entry.getValue());
                }
            }
        }*/
        customDialogModel.setTwoButtonRequired(true);
        CustomPaymentTranferDialog customDialog = new CustomPaymentTranferDialog(getActivity(), customDialogModel);
        customDialog.setSecondBtnTxt("Ok");
        customDialog.setSecondCallcack(new CustomDialog.SecondCallcack() {
            @Override
            public void clickYesCallback() {
                if (!resModel.isError()) {
                    ((SendMoneyFragment) getParentFragment()).backButton();
                    customDialog.dismiss();
                } else {
                    customDialog.dismiss();
                }
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(customDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        customDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(customDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setCancelable(false);
        customDialog.show();

    }

    private void setKeyListner() {
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    ((SendMoneyFragment) getParentFragment()).onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

}

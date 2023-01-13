package com.auro.application.home.presentation.view.fragment;

import static android.app.Activity.RESULT_OK;
import static com.auro.application.core.common.Status.AZURE_API;
import static com.auro.application.core.common.Status.UPLOAD_PROFILE_IMAGE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

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
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.KycFragmentLayoutBinding;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.data.model.KYCInputModel;
import com.auro.application.home.data.model.KYCResItemModel;
import com.auro.application.home.data.model.KYCResListModel;
import com.auro.application.home.presentation.view.activity.CameraActivity;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.adapter.KYCuploadAdapter;
import com.auro.application.home.presentation.viewmodel.KYCViewModel;
import com.auro.application.payment.presentation.view.fragment.SendMoneyFragment;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.alert_dialog.CustomProgressDialog;
import com.auro.application.util.alert_dialog.disclaimer.DisclaimerKycDialog;
import com.auro.application.util.cropper.CropImageViews;
import com.auro.application.util.cropper.CropImages;
import com.auro.application.util.strings.AppStringDynamic;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;


public class KYCFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    @Named("KYCFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "KYCFragment";
    KycFragmentLayoutBinding binding;
    KYCViewModel kycViewModel;
    KYCuploadAdapter kyCuploadAdapter;
    private int pos;
    private DashboardResModel dashboardResModel;
    private String comingFrom;
    ArrayList<KYCDocumentDatamodel> kycDocumentDatamodelArrayList;
    private boolean uploadBtnStatus;
    Resources resources;
    KYCInputModel kycInputModel = new KYCInputModel();
    CustomProgressDialog customProgressDialog;


    /*Face Image Params*/
    List<AssignmentReqModel> faceModelList;
    int faceCounter = 0;
    Uri resultUri;

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
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.v("CheckKyc", "Prefrence" + prefModel.isPreKycDisclaimer());
        if (!prefModel.isPreKycDisclaimer()) {
            checkDisclaimerKYCDialog();
        }
        return binding.getRoot();
    }

    public void setAdapter() {
        this.kycDocumentDatamodelArrayList = kycViewModel.homeUseCase.makeAdapterDocumentList(dashboardResModel, getActivity());
        binding.documentUploadRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.documentUploadRecyclerview.setHasFixedSize(true);
        kyCuploadAdapter = new KYCuploadAdapter(getActivity(), kycViewModel.homeUseCase.makeAdapterDocumentList(dashboardResModel, getActivity()), this);
        binding.documentUploadRecyclerview.setAdapter(kyCuploadAdapter);
    }


    @Override
    protected void init() {
        if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
            comingFrom = getArguments().getString(AppConstant.COMING_FROM);
        }
        AppStringDynamic.setKycStrings(binding);
        if (comingFrom != null && comingFrom.equalsIgnoreCase(AppConstant.SENDING_DATA.DYNAMIC_LINK)) {
            AppLogger.i(TAG, "Log DynamicLink");
            DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.KYC_DIRECT_FRAGMENT);
            ((DashBoardMainActivity) getActivity()).setListner(this);
            ((DashBoardMainActivity) getActivity()).callDashboardApi();
        } else {
            DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.QUIZ_KYC_FRAGMENT);
            setDataonFragment();
        }

    }


    private void setDataOnUi() {
        if (dashboardResModel != null) {
            setDataStepsOfVerifications();
            if (!TextUtil.isEmpty(dashboardResModel.getWalletbalance())) {
                // binding.walletBalText.setText(AuroApp.getAppContext().getResources().getString(R.string.rs) + " " + dashboardResModel.getWalletbalance());
                binding.walletBalText.setText(getActivity().getResources().getString(R.string.rs) + " " + kycViewModel.homeUseCase.getWalletBalance(dashboardResModel));
            }
        }
        binding.cambridgeHeading.cambridgeHeading.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

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
        //  binding.btUploadAll.setOnClickListener(this);
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
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel != null && prefModel.getUserKYCProfilePhotoPath() != null && !prefModel.getUserKYCProfilePhotoPath().isEmpty()) {
            updateKYCList(prefModel.getUserKYCProfilePhotoPath());
            prefModel.setUserKYCProfilePhotoPath("");
            AuroAppPref.INSTANCE.setPref(prefModel);
        }

        setKeyListner();
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

        switch (commonDataModel.getClickType()) {
            case KYC_BUTTON_CLICK:
                if (!uploadBtnStatus) {
                    onUploadDocClick(commonDataModel);
                }
                break;
            case KYC_RESULT_PATH:
                /*do code here*/

                break;
            case LISTNER_SUCCESS:
                binding.swipeRefreshLayout.setRefreshing(false);
                handleNavigationProgress(1, "");
                dashboardResModel = (DashboardResModel) commonDataModel.getObject();
                dashboardResModel.setModify(true);
                ((DashBoardMainActivity) getActivity()).dashboardModel(dashboardResModel);
                if (checkKycStatus(dashboardResModel)) {
                    ((DashBoardMainActivity) getActivity()).openKYCViewFragment(dashboardResModel, 0);
                } else {
                    ((DashBoardMainActivity) getActivity()).
                            openKYCFragment(dashboardResModel, 0);
                }
                setDataonFragment();
                break;

            case LISTNER_FAIL:
                handleNavigationProgress(2, (String) commonDataModel.getObject());
                break;
        }
    }

    public boolean checkKycStatus(DashboardResModel dashboardResModel) {
        if (dashboardResModel != null && !TextUtil.isEmpty(dashboardResModel.getPhoto()) && !TextUtil.isEmpty(dashboardResModel.getSchoolid()) &&
                !TextUtil.isEmpty(dashboardResModel.getIdback()) && !TextUtil.isEmpty(dashboardResModel.getIdfront())) {
            return true;
        }

        return false;
    }

    private void setDataonFragment() {
        setDataOnUi();
        setToolbar();
        setListener();
        setAdapter();
        /*Check for face image is Exist Or Not*/
        checkForFaceImage();
    }

    public void openActivity() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, 1);
    }

    public void openCameraFragment() {
        CameraFragment cameraFragment = new CameraFragment();
        FragmentUtil.replaceFragment(getActivity(), cameraFragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }



    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
                    if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(cameraIntent, 1);
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.e("StudentProfile", "fragment requestCode=" + requestCode);

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                try {


                    Uri uri = data.getData();
                    updateKYCList(uri.getPath());


                } catch (Exception e) {
                    AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
                }

            }


        }
        else if (requestCode == 1 ) {
            if (resultCode == RESULT_OK) {
                try {
                    String path = data.getStringExtra(AppConstant.PROFILE_IMAGE_PATH);
                    updateKYCList(path);
                    AppLogger.e("chhonker PROFILE_IMAGE_PATH--", "=" + path);
                    // loadImageFromStorage(path);
                } catch (Exception e) {
                    AppLogger.e("chhonker", "Exception error=" + e.getMessage());
                }

            }

        }



    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        AppLogger.e("chhonker", "fragment requestCode=" + requestCode);
//        if (data != null) {
//            resultUri = data.getData();
//        }
//        if (resultUri != null) {
//            AppLogger.e("chhonker", "Exception resultUri=" + resultUri.getPath());
//        }
//        if (requestCode == 2404) {
//            // CropImages.ActivityResult result = CropImages.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                try {
//
//                    try {
//                        resultUri = data.getData();
//                        updateKYCList(resultUri.getPath());
//                    } catch (Exception e) {
//                        AppLogger.e("chhonker", "Exception requestCode=" + e.getMessage());
//                    }
//                } catch (Exception e) {
//                    AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
//                }
//
//            } else if (resultCode == ImagePicker.RESULT_ERROR) {
//                ViewUtil.showSnackBar(binding.getRoot(), ImagePicker.getError(data));
//            } else {
//                ViewUtil.showSnackBar(binding.getRoot(), "Task Cancelled");
//            }
//        }
//        else if (requestCode == CropImages.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImages.ActivityResult result = CropImages.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                AppLogger.v("BigDes", "Sdk step 4");
//                try {
//                    resultUri = result.getUri();
//                    updateKYCList(resultUri.getPath());
//
//                } catch (Exception e) {
//
//                }
//
//            } else if (resultCode == CropImages.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//                AppLogger.e("chhonker", "Exception error=" + error.getMessage());
//            }
//        }
//        else if (requestCode == AppConstant.CAMERA_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                try {
//                    String path = data.getStringExtra(AppConstant.PROFILE_IMAGE_PATH);
//                    updateKYCList(path);
//                    AppLogger.e("chhonker PROFILE_IMAGE_PATH--", "=" + path);
//                    // loadImageFromStorage(path);
//                } catch (Exception e) {
//                    AppLogger.e("chhonker", "Exception error=" + e.getMessage());
//                }
//
//            }
//        }
//
//    }

    private void loadImageFromStorage(String path) {
        try {
            File f = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            binding.resultImage.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateKYCList(String path) {
        try {
            if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.ID_PROOF_FRONT_SIDE) {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_front.jpg");
                processImageData(true);
            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.ID_PROOF_BACK_SIDE) {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_back.jpg");
            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.SCHOOL_ID_CARD) {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_school.jpg");
                processImageData(false);
            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.UPLOAD_YOUR_PHOTO) {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("profile.jpg");
            }

            kycDocumentDatamodelArrayList.get(pos).setDocumentURi(Uri.parse(path));
            File file = new File(kycDocumentDatamodelArrayList.get(pos).getDocumentURi().getPath());
            InputStream is = getActivity().getApplicationContext().getContentResolver().openInputStream(Uri.fromFile(file));


            Bitmap picBitmap = BitmapFactory.decodeFile(path);
            byte[] bytes = KYCViewModel.encodeToBase64(picBitmap, 100);
            long mb = kycViewModel.bytesIntoHumanReadable(bytes.length);
            if (mb > 1.5) {
                AppLogger.e("chhonker", "size of the image greater than 1.5 mb -" + mb);
                kycDocumentDatamodelArrayList.get(pos).setImageBytes(KYCViewModel.encodeToBase64(picBitmap, 50));
                AppLogger.e("chhonker", "size of the image greater than 1.5 mb after conversion -" + kycViewModel.bytesIntoHumanReadable(kycDocumentDatamodelArrayList.get(pos).getImageBytes().length));
            } else {
                AppLogger.e("chhonker", "size of the image less 1.5 mb -" + mb);
                kycDocumentDatamodelArrayList.get(pos).setImageBytes(bytes);
            }


            if (!TextUtil.checkListIsEmpty(kycDocumentDatamodelArrayList)) {
                for (int i = 0; i < kycDocumentDatamodelArrayList.size(); i++) {
                    if (i != pos) {
                        kycDocumentDatamodelArrayList.get(i).setImageBytes(null);
                    }
                }
            }
            kyCuploadAdapter.updateList(kycDocumentDatamodelArrayList);
            uploadAllDocApi();
        } catch (Exception e) {
            AppLogger.e("chhonker", "exception--" + e.getMessage());
            /*Do code here when error occur*/
        }
    }


    private void processImageData(boolean status) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
            kycViewModel.homeUseCase.getTextFromImage(getActivity(), bitmap, kycInputModel, status);
        } catch (Exception e) {

        }
    }


    private void observeServiceResponse() {
        kycViewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case LOADING:
                    if (isVisible()) {
                        if (responseApi.apiTypeStatus == UPLOAD_PROFILE_IMAGE) {
                            openProgressDialog();
                        } else {
                            progressBarHandling(0);
                        }
                    }
                    break;

                case SUCCESS:
                    if (isVisible()) {
                        if (responseApi.apiTypeStatus == UPLOAD_PROFILE_IMAGE) {
                            closeDialog();
                            KYCResListModel kycResListModel = (KYCResListModel) responseApi.data;
                            if (!kycResListModel.isError()) {
                                ((DashBoardMainActivity) getActivity()).setProgressVal();
                                updateListonResponse(kycResListModel);
                                uploadBtnStatus = false;
                            } else {
                                showError(kycResListModel.getMessage());
                                progressBarHandling(1);
                            }
                        } else if (responseApi.apiTypeStatus == AZURE_API) {
                            sendFaceImageOnServer();
                        }
                    }
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                    if (isVisible()) {
                        if (responseApi.apiTypeStatus == UPLOAD_PROFILE_IMAGE) {
                            updateDialogUi();
                        } else {
                            showError((String) responseApi.data);
                            progressBarHandling(1);
                            updateFaceListInPref();
                        }
                    }
                    break;

                default:
                    if (isVisible()) {
                        if (responseApi.apiTypeStatus == UPLOAD_PROFILE_IMAGE) {
                            updateDialogUi();
                        } else {
                            showError((String) responseApi.data);
                            progressBarHandling(1);
                            updateFaceListInPref();
                        }
                    }
                    break;
            }

        });
    }

    private void progressBarHandling(int status) {
        if (status == 0) {
            binding.btUploadAll.setText("");
            binding.btUploadAll.setEnabled(false);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else if (status == 1) {
            binding.btUploadAll.setText(this.getResources().getString(R.string.upload));
            binding.btUploadAll.setEnabled(true);
            binding.progressBar.setVisibility(View.GONE);
            if (kycViewModel.homeUseCase.checkUploadButtonStatus(kycDocumentDatamodelArrayList)) {
                binding.buttonUploadLayout.setVisibility(View.INVISIBLE);
            } else {
                binding.buttonUploadLayout.setVisibility(View.INVISIBLE);
            }
        }

    }

    private void updateListonResponse(KYCResListModel kycResListModel) {
        for (KYCResItemModel resItemModel : kycResListModel.getList()) {
            if (resItemModel.getId_name().equalsIgnoreCase(kycDocumentDatamodelArrayList.get(0).getId_name())) {
                setDataOnResponse(resItemModel, 0, !resItemModel.getError() && resItemModel.getUrl() != null && !resItemModel.getUrl().isEmpty());
            } else if (resItemModel.getId_name().equalsIgnoreCase(kycDocumentDatamodelArrayList.get(1).getId_name())) {
                setDataOnResponse(resItemModel, 1, !resItemModel.getError() && resItemModel.getUrl() != null && !resItemModel.getUrl().isEmpty());
            } else if (resItemModel.getId_name().equalsIgnoreCase(kycDocumentDatamodelArrayList.get(2).getId_name())) {
                setDataOnResponse(resItemModel, 2, !resItemModel.getError() && resItemModel.getUrl() != null && !resItemModel.getUrl().isEmpty());
            } else if (resItemModel.getId_name().equalsIgnoreCase(kycDocumentDatamodelArrayList.get(3).getId_name())) {
                setDataOnResponse(resItemModel, 3, !resItemModel.getError() && resItemModel.getUrl() != null && !resItemModel.getUrl().isEmpty());
            }
        }

    }

    private void setDataOnResponse(KYCResItemModel resItemModel, int pos, boolean b) {
        if (b) {
            kycDocumentDatamodelArrayList.get(pos).setModify(false);
            kycDocumentDatamodelArrayList.get(pos).setDocumentstatus(true);
            kycDocumentDatamodelArrayList.get(pos).setDocumentUrl(resItemModel.getUrl());
        } else {
            kycDocumentDatamodelArrayList.get(pos).setModify(true);
            kycDocumentDatamodelArrayList.get(pos).setDocumentstatus(false);
            kycDocumentDatamodelArrayList.get(pos).setDocumentUrl("");
        }
        kyCuploadAdapter.updateList(kycDocumentDatamodelArrayList);
        progressBarHandling(1);

    }

    private void showError(String message) {
        uploadBtnStatus = false;
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_upload_all) {
            comingFrom = "";
            if (kycViewModel.homeUseCase.checkUploadButtonDoc(kycDocumentDatamodelArrayList)) {
                uploadBtnStatus = true;
                uploadAllDocApi();
            } else {
                ViewUtil.showSnackBar(binding.getRoot(), getString(R.string.document_all_four_error_msg));
            }
        } else if (id == R.id.lang_eng) {
            reloadFragment();
        } else if (id == R.id.back_arrow) {
            getActivity().onBackPressed();
        } else if (id == R.id.bt_transfer_money) {
            openSendMoneyFragment();
        } else if (id == R.id.wallet_info) {
            comingFrom = "";
            openWalletInfoFragment();
        }
    }


    public void callNumber() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:9667480783"));
        startActivity(callIntent);
    }

    public void openSendMoneyFragment() {

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
     /*   getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(AuroApp.getFragmentContainerUiId(), fragment, QuizHomeFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();*/
    }

    private void openWalletInfoFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        WalletInfoDetailFragment fragment = new WalletInfoDetailFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(AuroApp.getFragmentContainerUiId(), fragment, WalletInfoDetailFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }


    private void onUploadDocClick(CommonDataModel commonDataModel) {
        pos = commonDataModel.getSource();
        KYCDocumentDatamodel kycDocumentDatamodel = (KYCDocumentDatamodel) commonDataModel.getObject();
        if (kycDocumentDatamodel.getDocumentId() == AppConstant.DocumentType.UPLOAD_YOUR_PHOTO) {
            openActivity();
        } else {
           /* CropImages.activity()
                    .setGuidelines(CropImageViews.Guidelines.ON)
                    .start(getActivity());*/

            String deviceType = android.os.Build.MODEL;
            if (deviceType.contains(AppConstant.VIV0)) {
                CropImages.activity()
                        .setGuidelines(CropImageViews.Guidelines.ON)
                        .start(getActivity());
            } else {
                ImagePicker.with(getActivity())
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }

        }

    }


    private void uploadAllDocApi() {
        AppLogger.e("chhonker uploadAllDocApi", "step 1");
        kycInputModel.setUser_phone(dashboardResModel.getPhonenumber());
        kycViewModel.uploadProfileImage(kycDocumentDatamodelArrayList, kycInputModel);
    }


    public void openKYCFragment() {
        Bundle bundle = new Bundle();
        KYCFragment kycFragment = new KYCFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        kycFragment.setArguments(bundle);
        openFragment(kycFragment);
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
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
                binding.stepOne.textUploadDocumentMsg.setTextColor(ContextCompat.getColor(getActivity(), R.color.ufo_green));
                if (!TextUtil.isEmpty(dashboardResModel.getIs_kyc_verified()) && dashboardResModel.getIs_kyc_verified().equalsIgnoreCase(AppConstant.DocumentType.IN_PROCESS)) {
                    AppLogger.e("chhonker step ", "kyc Step 3");
                    binding.stepTwo.textVerifyMsg.setText(getString(R.string.verification_is_in_process));
                    binding.stepTwo.textVerifyMsg.setText(details.getVerificationIsInProcess());
                    binding.stepTwo.textVerifyMsg.setVisibility(View.VISIBLE);
                } else if (!TextUtil.isEmpty(dashboardResModel.getIs_kyc_verified()) &&
                        dashboardResModel.getIs_kyc_verified().equalsIgnoreCase(AppConstant.DocumentType.APPROVE)) {
                    AppLogger.e("chhonker step ", "kyc Step 4");
                    binding.stepTwo.textVerifyMsg.setText(R.string.document_verified);
                    binding.stepTwo.textVerifyMsg.setText(details.getDocumentVerified());
                    binding.stepTwo.textVerifyMsg.setVisibility(View.VISIBLE);
                    binding.stepTwo.tickSign.setVisibility(View.VISIBLE);
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
         /*binding.stepFour.btTransferMoney.setOnClickListener(this);
         binding.stepFour.btTransferMoney.setVisibility(View.VISIBLE);*/

    }

    private void sendFaceImageOnServer() {
        if (!TextUtil.checkListIsEmpty(faceModelList)) {
            faceModelList.get(faceCounter).setUploaded(true);
            faceCounter++;
            if (faceModelList.size() > faceCounter) {
                kycViewModel.sendAzureImageData(faceModelList.get(faceCounter));
            } else {
                updateFaceListInPref();
            }
        }
    }

    private void updateFaceListInPref() throws IndexOutOfBoundsException {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel != null && !TextUtil.checkListIsEmpty(faceModelList)) {
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
                    //  getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    private void openProgressDialog() {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            return;
        }
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(getActivity());
        customDialogModel.setTitle("Uploading...");
        customDialogModel.setTwoButtonRequired(true);
        customProgressDialog = new CustomProgressDialog(customDialogModel);
        Objects.requireNonNull(customProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCallcack(new CustomProgressDialog.ButtonClickCallback() {
            @Override
            public void retryCallback() {
                uploadAllDocApi();
                customProgressDialog.updateUI(1);
            }

            @Override
            public void closeCallback() {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName(AuroApp.getAppContext().getResources().getString(R.string.no_file_chosen));
                kyCuploadAdapter.updateList(kycDocumentDatamodelArrayList);
            }
        });
        customProgressDialog.show();
    }


    public void updateDialogUi() {
        if (customProgressDialog != null) {
            customProgressDialog.updateUI(0);
        }

    }

    public void closeDialog() {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
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

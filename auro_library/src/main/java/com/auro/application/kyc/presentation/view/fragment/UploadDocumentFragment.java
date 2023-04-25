package com.auro.application.kyc.presentation.view.fragment;

import static android.app.Activity.RESULT_OK;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseDialog;

import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;

import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.FragmentUploadDocumentBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.data.model.KYCResListModel;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.viewmodel.KYCViewModel;

import com.auro.application.teacher.data.model.response.MyClassRoomResModel;
import com.auro.application.teacher.data.model.response.TeacherKycStatusResModel;
import com.auro.application.teacher.presentation.viewmodel.TeacherKycViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.network.ProgressRequestBody;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
//import com.facebook.appevents.codeless.internal.EventBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;


public class UploadDocumentFragment extends BaseDialog implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {


    @Inject
    @Named("UploadDocumentFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "UploadDocumentFragment";
    FragmentUploadDocumentBinding binding;
    TeacherKycViewModel kycViewModel;
    KYCDocumentDatamodel kycDocumentDatamodel;
    TeacherKycStatusResModel teacherKycStatusResModel;
    ArrayList<KYCDocumentDatamodel> kycDocumentDatamodelArrayList;

    PrefModel prefModel;
    Details details;
    public UploadDocumentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kycDocumentDatamodel = getArguments().getParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_DOCUMENT_MODEL_DATA);
            teacherKycStatusResModel = getArguments().getParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_RES_MODEL_DATA);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

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
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        details = prefModel.getLanguageMasterDynamic().getDetails();
        binding.documentTitle.setText(kycDocumentDatamodel.getDocumentName());
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        HomeActivity.setListingActiveFragment(HomeActivity.TEACHER_KYC_UPLOAD_FRAGMENT);
        AppUtil.uploadCallbacksListner = this;
        binding.uploadIcon.setOnClickListener(this);
        binding.closeButton.setOnClickListener(this);
        binding.parentLayout.setOnClickListener(this);
        if (kycViewModel != null && kycViewModel.serviceLiveData().hasObservers()) {
            kycViewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_upload_document;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.upload_icon) {
            askPermission();
        } else if (id == R.id.parentLayout) {/*Nothing*/
        } else if (id == R.id.closeButton) {
            getActivity().onBackPressed();
        }
    }


    private void askPermission() {
//        String rationale = "For Upload Profile Picture. Camera and Storage Permission is Must.";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(getContext(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
                ImagePicker.with(UploadDocumentFragment.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//                ViewUtil.showSnackBar(binding.getRoot(), rationale);
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.e("StudentProfile", "fragment requestCode=" + requestCode);

        if (requestCode == 2404) {
            // CropImages.ActivityResult result = CropImages.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                AppLogger.v("StudentPradeep", "handleData" );
                handleData(data);
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showSnackbarError(ImagePicker.getError(data));
            } else {
                // Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void setDataOnUI() {

    }


    void handleData(Intent data) {
        try {
            Uri uri = data.getData();
            AppLogger.v("StudentPradeep", "image path=" + uri.getPath());

            Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
            byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
            long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
            int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));

            AppLogger.v("StudentPradeep", "image size=" + uri.getPath());
            File f = new File("" + uri);

            if (!uri.getPath().isEmpty()) {
                AppLogger.v("StudentPradeep", "not empty=" + uri.getPath());
                handleUi(0);
                binding.fileNameTxt.setText(f.getName());
                updateKYCList(uri.getPath());
            }
            if (file_size >= 500) {
                //   studentProfileModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
            } else {
                //   studentProfileModel.setImageBytes(bytes);
            }
            // int new_file_size = Integer.parseInt(String.valueOf(studentProfileModel.getImageBytes().length / 1024));
            //AppLogger.d(TAG, "Image Path  new Size kb- " + mb + "-bytes-" + new_file_size);


            // loadimage(picBitmap);
        } catch (Exception e) {
            AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
            AppLogger.v("StudentPradeep", "exception=" );
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        AppLogger.e("StudentProfile", "onProgressUpdate=" + percentage);
        binding.pbProcessing.setProgress(percentage);
        binding.txtProgress.setText(percentage +" %");
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }


    private void observeServiceResponse() {

        kycViewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {

                case SUCCESS:
                    if (isVisible()) {
                        if (responseApi.apiTypeStatus == Status.TEACHER_KYC_API) {



                            KYCResListModel kycResListModel = (KYCResListModel) responseApi.data;
                            if (!kycResListModel.isError()) {
                                AppLogger.v("Pradeep_ui","TEACHER_KYC_API  Step 1"+responseApi.data);

                                if(AppUtil.commonCallBackListner!=null)
                                {
                                    AppLogger.v("Pradeep_ui","TEACHER_KYC_API Step 2 ");
                                    AppUtil.commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.UPLOAD_TEACHER_DOC_CALLBACK,""));
                                }
                                AppLogger.v("Pradeep_ui","TEACHER_KYC_API  Step 3"+kycResListModel.getMessage());
                                ViewUtil.showSnackBar(binding.getRoot(), kycResListModel.getMessage(), Color.parseColor("#4bd964"));

                                // showSnackbarError(kycResListModel.getMessage());
                                dismiss();
                                handleUi(1);
                            } else {

                                getActivity().onBackPressed();
                                //dismiss();
                                //showSnackbarError(kycResListModel.getMessage());
                            }



                          /*  KYCResListModel kycResListModel = (KYCResListModel) responseApi.data;
                            if (kycResListModel.isError()) {
                                handleUi(1);
                            } else {
                                getActivity().onBackPressed();
                            }*/
                        }
                    }
                    break;

                case FAIL:
                case NO_INTERNET:
                default:
                    if (isVisible()) {
                        showSnackbarError(details.getDefaultError() != null ? details.getDefaultError() : getActivity().getString(R.string.default_error));

                    }

                    break;
            }

        });
    }

    void handleUi(int status) {
        switch (status) {
            case 0:
                binding.selectDocumentLayout.setVisibility(View.GONE);
                binding.uploadingLayout.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.selectDocumentLayout.setVisibility(View.VISIBLE);
                binding.uploadingLayout.setVisibility(View.GONE);
                break;

        }
    }

    private void updateKYCList(String path) {
        try {
            AppLogger.e("calluploadApi-", "Step 1");
            kycDocumentDatamodelArrayList = kycViewModel.teacherUseCase.makeAdapterDocumentList(teacherKycStatusResModel);
            int pos = kycDocumentDatamodel.getPosition();
            if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.ID_PROOF_FRONT_SIDE) {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_front.jpg");
            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.ID_PROOF_BACK_SIDE) {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_back.jpg");
            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.SCHOOL_ID_CARD) {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_school.jpg");
            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.UPLOAD_YOUR_PHOTO) {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("profile.jpg");
            }
            kycDocumentDatamodelArrayList.get(pos).setDocumentURi(Uri.parse(path));
            File file = new File(kycDocumentDatamodelArrayList.get(pos).getDocumentURi().getPath());
            InputStream is = AuroApp.getAppContext().getApplicationContext().getContentResolver().openInputStream(Uri.fromFile(file));
            kycDocumentDatamodelArrayList.get(pos).setImageBytes(kycViewModel.teacherUseCase.getBytes(is));
            AppLogger.e("calluploadApi-", "Step 2");

            uploadAllDocApi();
        } catch (Exception e) {
            AppLogger.e("calluploadApi-", "Step 3-" + e.getMessage());

            /*Do code here when error occur*/
        }
    }

    private void uploadAllDocApi() {
        AppLogger.e("calluploadApi-", "Step 4");

        kycViewModel.checkInternet(Status.TEACHER_KYC_API, kycDocumentDatamodelArrayList);
    }

}
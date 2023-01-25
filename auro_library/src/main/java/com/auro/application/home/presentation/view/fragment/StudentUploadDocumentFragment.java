package com.auro.application.home.presentation.view.fragment;

import static android.app.Activity.RESULT_OK;

import static com.auro.application.core.common.Status.AZURE_API;
import static com.auro.application.core.common.Status.UPLOAD_PROFILE_IMAGE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

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
import com.auro.application.home.data.model.KYCInputModel;
import com.auro.application.home.data.model.KYCResListModel;
import com.auro.application.home.data.model.response.StudentKycStatusResModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.activity.StudentProfileActivity;
import com.auro.application.home.presentation.viewmodel.KYCViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.cropper.CropImages;
import com.auro.application.util.network.ProgressRequestBody;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringDynamic;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;


public class StudentUploadDocumentFragment extends BaseDialog implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {


    @Inject
    @Named("StudentUploadDocumentFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "UploadDocumentFragment";
    FragmentUploadDocumentBinding binding;
    KYCViewModel kycViewModel;
    KYCDocumentDatamodel kycDocumentDatamodel;
    StudentKycStatusResModel studentKycStatusResModel;
    ArrayList<KYCDocumentDatamodel> kycDocumentDatamodelArrayList;

    PrefModel prefModel;
    Details details;

    public StudentUploadDocumentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kycDocumentDatamodel = getArguments().getParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_DOCUMENT_MODEL_DATA);
            studentKycStatusResModel = getArguments().getParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_RES_MODEL_DATA);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        //((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        DaggerWrapper.getComponent(getActivity()).doInjection(this);
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

        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        details =prefModel.getLanguageMasterDynamic().getDetails();
        binding.documentTitle.setText(kycDocumentDatamodel.getDocumentName());
        AppStringDynamic.setStudentUploadDocumentStrings(binding);
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {

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
            if (kycDocumentDatamodel.getDocumentName().equals("Upload Your Photo")) {
                if (Build.VERSION.SDK_INT > 26) {
                    askPermissionCamera();
                } else {
                    askPermissionCamera();
                }
            } else {
                if (Build.VERSION.SDK_INT > 26) {
                    askPermission();
                } else {
                    askPermission();
                    // askPermission();
                }
            }
        } else if (id == R.id.parentLayout) {/*Nothing*/
        } else if (id == R.id.closeButton) {
            dismiss();
        }
    }


    private void askPermission() {

                ImagePicker.with(StudentUploadDocumentFragment.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

    }

    private void askPermissionCamera() {

                ImagePicker.with(StudentUploadDocumentFragment.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .cameraOnly()
                        .start();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.e("StudentProfile", "fragment requestCode=" + requestCode);
        if (Build.VERSION.SDK_INT > 26) {
            if (requestCode == 2404) {


                if (resultCode == RESULT_OK) {
                    try {

                        handleData(data);



                    } catch (Exception e) {
                        AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
                    }

                }

            }
            else if (requestCode == 1 ) {
                if (Build.VERSION.SDK_INT > 26) {
                    handleData(data);

                }
                else{
                    if (resultCode == RESULT_OK) {
                        AppLogger.v("BigDes", "Sdk step 4");
                        try {
                            handleData(data);


                        } catch (Exception e) {

                        }

                    }
                }
            }
        }
        else{
            if (requestCode == 2404) {  //2404

                if (resultCode == RESULT_OK) {
                    try {

                        handleData(data);


                    } catch (Exception e) {
                        AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
                    }

                }




            }
            else if (requestCode == 1 ) {

                if (requestCode == 1 && resultCode == Activity.RESULT_OK)
                {
                    handleData(data);

                }

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
            AppLogger.v("StudentProfile", "image path=" + uri.getPath());

            Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
            byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
            long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
            int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));

            AppLogger.v("StudentProfile", "image size=" + uri.getPath());
            File f = new File("" + uri);

            if (!uri.getPath().isEmpty()) {
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
                        if (responseApi.apiTypeStatus == UPLOAD_PROFILE_IMAGE) {
                            KYCResListModel kycResListModel = (KYCResListModel) responseApi.data;
                            if (!kycResListModel.isError()) {
                                if(AppUtil.commonCallBackListner!=null)
                                {
                                    AppUtil.commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.UPLOAD_DOC_CALLBACK,""));
                                }
                                ViewUtil.showSnackBar(binding.getRoot(), kycResListModel.getMessage(), Color.parseColor("#4bd964"));

                                // showSnackbarError(kycResListModel.getMessage());
                                dismiss();
                                handleUi(1);
                            } else {
                                dismiss();
                                showSnackbarError(kycResListModel.getMessage());
                            }
                        }
                    }
                    break;

                case FAIL:
                case NO_INTERNET:
                default:
                    if (isVisible()) {
                        dismiss();
                        showSnackbarError(details.getDefaultError() != null ? details.getDefaultError() : getString(R.string.default_error));

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
            kycDocumentDatamodelArrayList = kycViewModel.homeUseCase.makeAdapterDocumentList(studentKycStatusResModel);
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
            kycDocumentDatamodelArrayList.get(pos).setImageBytes(kycViewModel.getBytes(is));
            AppLogger.e("calluploadApi-", "Step 2");

            uploadAllDocApi();
        } catch (Exception e) {
            AppLogger.e("calluploadApi-", "Step 3-" + e.getMessage());

            /*Do code here when error occur*/
        }
    }

    private void uploadAllDocApi() {
        KYCInputModel kycInputModel = new KYCInputModel();
        kycInputModel.setUser_phone("");
        kycViewModel.uploadProfileImage(kycDocumentDatamodelArrayList, kycInputModel);
    }

}



//    @Inject
//    @Named("StudentUploadDocumentFragment")
//    ViewModelFactory viewModelFactory;
//    String TAG = "UploadDocumentFragment";
//    FragmentUploadDocumentBinding binding;
//    KYCViewModel kycViewModel;
//    KYCDocumentDatamodel kycDocumentDatamodel;
//    StudentKycStatusResModel studentKycStatusResModel;
//    ArrayList<KYCDocumentDatamodel> kycDocumentDatamodelArrayList;
//
//    PrefModel prefModel;
//    Details details;
//
//    public StudentUploadDocumentFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            kycDocumentDatamodel = getArguments().getParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_DOCUMENT_MODEL_DATA);
//            studentKycStatusResModel = getArguments().getParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_RES_MODEL_DATA);
//
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        // Inflate the layout for this fragment
//        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
//        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
//        kycViewModel = ViewModelProviders.of(this, viewModelFactory).get(KYCViewModel.class);
//        binding.setLifecycleOwner(this);
//        setRetainInstance(true);
//        init();
//        setListener();
//        setToolbar();
//
//        return binding.getRoot();
//    }
//
//    @Override
//    protected void init() {
//
//        prefModel = AuroAppPref.INSTANCE.getModelInstance();
//        details =prefModel.getLanguageMasterDynamic().getDetails();
//        binding.documentTitle.setText(kycDocumentDatamodel.getDocumentName());
//        AppStringDynamic.setStudentUploadDocumentStrings(binding);
//    }
//
//    @Override
//    protected void setToolbar() {
//
//    }
//
//    @Override
//    protected void setListener() {
//
//        AppUtil.uploadCallbacksListner = this;
//        binding.uploadIcon.setOnClickListener(this);
//        binding.closeButton.setOnClickListener(this);
//        binding.parentLayout.setOnClickListener(this);
//        if (kycViewModel != null && kycViewModel.serviceLiveData().hasObservers()) {
//            kycViewModel.serviceLiveData().removeObservers(this);
//        } else {
//            observeServiceResponse();
//        }
//
//    }
//
//    @Override
//    protected int getLayout() {
//        return R.layout.fragment_upload_document;
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.upload_icon:
//                if(kycDocumentDatamodel.getDocumentName().equals("Upload Your Photo")){
//                    askPermissionCamera();
//                }else{
//                    //askPermission();
//                    selectImage();
//                }
//
//                break;
//            case R.id.parentLayout:
//                /*Nothing*/
//                break;
//
//            case R.id.closeButton:
//                dismiss();
//                break;
//        }
//    }
//
//
//    private void askPermission() {
//        String rationale = "For Upload Profile Picture. Camera and Storage Permission is Must.";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(getContext(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
//                ImagePicker.with(StudentUploadDocumentFragment.this)
//                        .crop()                    //Crop image(Optional), Check Customization for more option
//                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
//                        .start();
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//                ViewUtil.showSnackBar(binding.getRoot(), rationale);
//            }
//        });
//    }
//
//    private void askPermissionCamera() {
//        String rationale = "For Upload Profile Picture. Camera and Storage Permission is Must.";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(getContext(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
//                ImagePicker.with(StudentUploadDocumentFragment.this)
//                        .crop()                    //Crop image(Optional), Check Customization for more option
//                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
//                        .cameraOnly()
//                        .start();
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//                ViewUtil.showSnackBar(binding.getRoot(), rationale);
//            }
//        });
//    }
//    private void selectImage() {
//        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo"))
//                {
//
//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
//                    if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                        startActivityForResult(cameraIntent, 1);
//                    }
//
//
//                }
//                else if (options[item].equals("Choose from Gallery"))
//                {
//                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, 2404);
//                }
//                else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
//    //
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        AppLogger.v("StudentPradeep", "fragment  Student requestCode=" + requestCode);
//
//        if (requestCode == 2404) {
//            // CropImages.ActivityResult result = CropImages.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                AppLogger.v("StudentPradeep", "handleData" );
//                handleData(data);
//            } else if (resultCode == ImagePicker.RESULT_ERROR) {
//                showSnackbarError(ImagePicker.getError(data));
//            } else {
//                // Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//    private void showSnackbarError(String message) {
//        ViewUtil.showSnackBar(binding.getRoot(), message);
//    }
//
//    private void setDataOnUI() {
//
//    }
//
//
//    void handleData(Intent data) {
//
//        if (Build.VERSION.SDK_INT > 26) {
//
//
//
//
//                    try {
//
//
//                        Uri uri = data.getData();
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                        AppLogger.e("StudentProfile", "image path=" + uri.getPath());
//                        String image_path = uri.getPath();
//
//                        Uri selectedImage = data.getData();
//                        String[] filePath = { MediaStore.Images.Media.DATA };
//                        Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, null, null, null);
//                        c.moveToFirst();
//                        int columnIndex = c.getColumnIndex(filePath[0]);
//                        String picturePath = c.getString(columnIndex);
//                        c.close();
//                        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                     //   loadimage(bitmap);
//                        byte[] bytes = AppUtil.encodeToBase64(bitmap, 100);
//                        long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                        int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//                        String filename = image_path.substring(image_path.lastIndexOf("/") + 1);
//                        File f = new File("" + uri);
//                        if (!uri.getPath().isEmpty()) {
//                            handleUi(0);
//                            binding.fileNameTxt.setText(f.getName());
//                            updateKYCList(uri.getPath());
//                        }
//                        if (file_size >= 500) {
//                          //  Toast.makeText(this, "Image size is too large", Toast.LENGTH_SHORT).show();
//
//                          //  studentProfileModel.setImageBytes(AppUtil.encodeToBase64(bitmap, 50));
//
//                        } else {
//
//                          //  studentProfileModel.setImageBytes(bytes);
//                        }
//
//
//
//
//
//                    }
//                    catch (Exception e) {
//                        AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
//                    }
//
//
//
//
//
//        }
//        else{
//
//
//
//                    try {
//
//
//                        Uri uri = data.getData();
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                        AppLogger.e("StudentProfile", "image path=" + uri.getPath());
//                        String image_path = uri.getPath();
//                        Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
//                        byte[] bytes = AppUtil.encodeToBase64(bitmap, 100);
//                        long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                        int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//                        String filename = image_path.substring(image_path.lastIndexOf("/") + 1);
//                        File f = new File("" + uri);
//            if (!uri.getPath().isEmpty()) {
//                Toast.makeText(getActivity(), "uploading", Toast.LENGTH_SHORT).show();
//                handleUi(0);
//                binding.fileNameTxt.setText(f.getName());
//                updateKYCList(uri.getPath());
//            }
//                        AppLogger.e("StudentProfile", "image size=" + uri.getPath());
//                        if (file_size >= 500) {
//                            Toast.makeText(getActivity(), "Image size is too large", Toast.LENGTH_SHORT).show();
//
//                           // studentProfileModel.setImageBytes(AppUtil.encodeToBase64(bitmap, 50));
//                        } else {
//
//                           // studentProfileModel.setImageBytes(bytes);
//                        }
//                      //  loadimage(bitmap);
//                    } catch (Exception e) {
//                        AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
//                    }
//
//
//
//
//
//
//
//
//        }
//
//
////        try {
////            Uri uri = data.getData();
////            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
////            AppLogger.v("StudentProfile", "image path=" + uri.getPath());
////           String path = uri.getPath();
////        //    Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
////            byte[] bytes = AppUtil.encodeToBase64(bitmap, 100);
////            long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
////            int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
////
////            AppLogger.v("StudentProfile", "image size=" + uri.getPath());
////           String filename = path.substring(path.lastIndexOf("/") + 1);
////            File f = new File("" + uri);
////
////            if (!uri.getPath().isEmpty()) {
////                handleUi(0);
////                binding.fileNameTxt.setText(filename);
////                updateKYCList(uri.getPath());
////            }
////            if (file_size >= 500) {
////                //   studentProfileModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
////            } else {
////                //   studentProfileModel.setImageBytes(bytes);
////            }
////            // int new_file_size = Integer.parseInt(String.valueOf(studentProfileModel.getImageBytes().length / 1024));
////            //AppLogger.d(TAG, "Image Path  new Size kb- " + mb + "-bytes-" + new_file_size);
////
////
////            // loadimage(picBitmap);
////        } catch (Exception e) {
////            AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
////        }
//    }
//
//    @Override
//    public void onProgressUpdate(int percentage) {
//        AppLogger.e("StudentProfile", "onProgressUpdate=" + percentage);
//        binding.pbProcessing.setProgress(percentage);
//        binding.txtProgress.setText(percentage +" %");
//    }
//
//    @Override
//    public void onError() {
//
//    }
//
//    @Override
//    public void onFinish() {
//
//    }
//
//
//    private void observeServiceResponse() {
//
//        kycViewModel.serviceLiveData().observeForever(responseApi -> {
//            switch (responseApi.status) {
//
//                case SUCCESS:
//                    if (isVisible()) {
//                        if (responseApi.apiTypeStatus == UPLOAD_PROFILE_IMAGE) {
//                            KYCResListModel kycResListModel = (KYCResListModel) responseApi.data;
//                            if (!kycResListModel.isError()) {
//                                if(AppUtil.commonCallBackListner!=null)
//                                {
//                                    AppUtil.commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.UPLOAD_DOC_CALLBACK,""));
//                                }
//                                ViewUtil.showSnackBar(binding.getRoot(), kycResListModel.getMessage(), Color.parseColor("#4bd964"));
//
//                                // showSnackbarError(kycResListModel.getMessage());
//                                dismiss();
//                                handleUi(1);
//                            } else {
//                                dismiss();
//                                showSnackbarError(kycResListModel.getMessage());
//                            }
//                        }
//                    }
//                    break;
//
//                case FAIL:
//                case NO_INTERNET:
//                default:
//                    if (isVisible()) {
//                        dismiss();
//                        showSnackbarError(details.getDefaultError() != null ? details.getDefaultError() : getString(R.string.default_error));
//
//                    }
//
//                    break;
//            }
//
//        });
//    }
//
//    void handleUi(int status) {
//        switch (status) {
//            case 0:
//                binding.selectDocumentLayout.setVisibility(View.GONE);
//                binding.uploadingLayout.setVisibility(View.VISIBLE);
//                break;
//
//            case 1:
//                binding.selectDocumentLayout.setVisibility(View.VISIBLE);
//                binding.uploadingLayout.setVisibility(View.GONE);
//                break;
//
//        }
//    }
//
//    private void updateKYCList(String path) {
//        try {
//            AppLogger.e("calluploadApi-", "Step 1");
//            kycDocumentDatamodelArrayList = kycViewModel.homeUseCase.makeAdapterDocumentList(studentKycStatusResModel);
//            int pos = kycDocumentDatamodel.getPosition();
//            if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.ID_PROOF_FRONT_SIDE) {
//                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_front.jpg");
//            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.ID_PROOF_BACK_SIDE) {
//                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_back.jpg");
//            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.SCHOOL_ID_CARD) {
//                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_school.jpg");
//            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.UPLOAD_YOUR_PHOTO) {
//                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("profile.jpg");
//            }
//            kycDocumentDatamodelArrayList.get(pos).setDocumentURi(Uri.parse(path));
//            File file = new File(kycDocumentDatamodelArrayList.get(pos).getDocumentURi().getPath());
//            Toast.makeText(getActivity(), file.toString(), Toast.LENGTH_SHORT).show();
//
//            InputStream is = AuroApp.getAppContext().getApplicationContext().getContentResolver().openInputStream(Uri.fromFile(file));
//            kycDocumentDatamodelArrayList.get(pos).setImageBytes(kycViewModel.getBytes(is));
//            AppLogger.e("calluploadApi-", "Step 2");
//
//            uploadAllDocApi();
//        } catch (Exception e) {
//            AppLogger.e("calluploadApi-", "Step 3-" + e.getMessage());
//
//            /*Do code here when error occur*/
//        }
//    }
//
//    private void uploadAllDocApi() {
//        KYCInputModel kycInputModel = new KYCInputModel();
//        kycInputModel.setUser_phone("");
//        kycViewModel.uploadProfileImage(kycDocumentDatamodelArrayList, kycInputModel);
//    }


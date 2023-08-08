package com.auro.application.RealTimeFaceDetection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.TransactionTooLargeException;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;

import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.exifinterface.media.ExifInterface;


import com.auro.application.R;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;

import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.ActivityRealTimeFaceDetectionBinding;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.util.AppLogger;

import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.alert_dialog.CustomProgressDialog;

import com.google.android.gms.vision.CameraSource;
import com.google.common.io.Files;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

import java.nio.ByteBuffer;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;




public class CameraxActivity extends BaseActivity implements View.OnClickListener, CommonCallBackListner {
    private TextureView tv;
    private ImageView iv;
    private ImageButton buttonCapture;
    private static final String TAG = "CameraxActivity";
    public static final int REQUEST_CODE_PERMISSION = 101;
    public static final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    ActivityRealTimeFaceDetectionBinding binding;
    private FirebaseVisionImage fbImage;
    public static boolean status;
    CustomProgressDialog customProgressDialog;

    public static CameraX.LensFacing lens = CameraX.LensFacing.FRONT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            // setContentView(R.layout.activity_real_time_face_detection );
            binding = DataBindingUtil.setContentView(this, getLayout());
            binding.setLifecycleOwner(this);
            tv = binding.faceTextureView;
            iv = binding.faceImageView;
            buttonCapture = binding.stillshot;


            if (allPermissionsGranted()) {
                tv.post(this::startCamera);
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION);
            }
            tv.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> updateTransform());
            init();
            setListener();
        }catch (Exception e){

        }
    }

    @Override
    protected void init() {
        binding.captureButtonSecondaryContainer.setVisibility(View.GONE);
        binding.txtFace.setText(AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails().getPut_your_face());
    }

    @Override
    protected void setListener() {


    }

    @Override
    protected int getLayout() {
        return R.layout.activity_real_time_face_detection;
    }

    @SuppressLint("RestrictedApi")
    private void startCamera() {
        initCamera();

    }


    private Bitmap imageProxyToBitmap(ImageProxy image)
    {
        ImageProxy.PlaneProxy planeProxy = image.getPlanes()[0];
        ByteBuffer buffer = planeProxy.getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    private void initCamera() throws IllegalArgumentException {
        CameraX.unbindAll();
        PreviewConfig pc = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            pc = new PreviewConfig
                    .Builder()
                    .setLensFacing(lens)
                    .setTargetResolution(new Size(tv.getWidth(), tv.getHeight()))
                    .build();
        }

        Preview preview = new Preview(pc);

        preview.setOnPreviewOutputUpdateListener(output -> {
            ViewGroup vg = (ViewGroup) tv.getParent();
            vg.removeView(tv);
            vg.addView(tv, 0);
            tv.setSurfaceTexture(output.getSurfaceTexture());
            updateTransform();
        });

        ImageCaptureConfig icc = new ImageCaptureConfig
                .Builder()
                .setLensFacing(lens)
                .setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
                .build();
        ImageCapture imgCap = new ImageCapture(icc);
        ImageButton ib = findViewById(R.id.stillshot);
        ib.setOnClickListener(v ->
        {
            AppLogger.e(TAG,"Step 1");
            openProgressDialog();
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + System.currentTimeMillis() + ".png");

            imgCap.takePicture((Runnable::run),new ImageCapture.OnImageCapturedListener()
            {
                @Override
                public void onCaptureSuccess(ImageProxy image, int rotationDegrees)
                {
                    try {

                        ImageProxy.PlaneProxy planeProxy = image.getPlanes()[0];
                        ByteBuffer buffer = planeProxy.getBuffer();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);

                        ExifInterface exifInterface = new ExifInterface(new ByteArrayInputStream(bytes));
                        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                       // int rotationDegrees = 0;
                        /*switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotationDegrees = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotationDegrees = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotationDegrees = 270;
                                break;
                        }*/


                        int finalRotationDegrees = rotationDegrees;
                        Single<String> single = Single.create(new SingleOnSubscribe<String>() {
                            @Override
                            public void subscribe(SingleEmitter<String> emitter) throws Exception {
                                try {
                                    // convert byte array into bitmap
                                    Bitmap loadedImage = null;
                                    Bitmap loadedImageNew = BitmapFactory.decodeByteArray(bytes, 0,
                                            bytes.length);
                                    loadedImage = rotateBitmap(loadedImageNew, finalRotationDegrees);
                                    //  String path = saveToInternalStorage(loadedImage) + "/profile.jpg";
                                    String path = saveToInternalStorage(loadedImage);

                                    emitter.onSuccess(path);
                                } catch (Exception e) {
                                    emitter.onError(e);
                                }
                            }
                        });


                        new CompositeDisposable().add(single
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        new Consumer<String>() {
                                            @Override
                                            public void accept(String path) throws Exception {
                                                closeDialog();
                                                Intent intent = new Intent();
                                                intent.putExtra(AppConstant.PROFILE_IMAGE_PATH, path);
                                                setResult(Activity.RESULT_OK, intent);
                                                finish();
                                            }
                                        },
                                        new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                            }
                                        }
                                ));


                        //loadImageFromStorage(saveToInternalStorage(loadedImage));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            AppLogger.e(TAG,"Step 2");
           /* imgCap.takePicture(file, (Runnable::run), new ImageCapture.OnImageSavedListener() {
                @Override
                public void onImageSaved(@NonNull File file) {
                    try {

                        String msg = "Image is saved at: " + file.getAbsolutePath();
                        AppLogger.e(TAG,"Image is saved at:" +file.getAbsolutePath());
                        AppLogger.e(TAG,"Step 3");
                        //runOnUiThread(() -> Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show());
                        byte[] bytes = Files.toByteArray(file);
                        ExifInterface exifInterface = new ExifInterface(new ByteArrayInputStream(bytes));
                        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        int rotationDegrees = 0;
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotationDegrees = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotationDegrees = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotationDegrees = 270;
                                break;
                        }

                        int finalRotationDegrees = rotationDegrees;
                        Single<String> single = Single.create(new SingleOnSubscribe<String>() {
                            @Override
                            public void subscribe(SingleEmitter<String> emitter) throws Exception {
                                try {
                                    // convert byte array into bitmap
                                    byte[] bytes = Files.toByteArray(file);
                                    Bitmap loadedImage = null;
                                    Bitmap loadedImageNew = BitmapFactory.decodeByteArray(bytes, 0,
                                            bytes.length);
                                    loadedImage = rotateBitmap(loadedImageNew, finalRotationDegrees);
                                    //  String path = saveToInternalStorage(loadedImage) + "/profile.jpg";
                                    String path = saveToInternalStorage(loadedImage);
                                    AppLogger.e(TAG,"Step 4--"+path);
                                    emitter.onSuccess(path);
                                } catch (Exception e) {
                                    emitter.onError(e);
                                }
                            }
                        });
                        new CompositeDisposable().add(single
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        new Consumer<String>() {
                                            @Override
                                            public void accept(String path) throws Exception {
                                                closeDialog();
                                                Intent intent = new Intent();
                                                intent.putExtra(AppConstant.PROFILE_IMAGE_PATH, path);
                                                setResult(Activity.RESULT_OK, intent);
                                                finish();
                                            }
                                        },
                                        new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                AppLogger.e(TAG,"Step 5--error--"+throwable.getMessage());
                                            }
                                        }
                                ));

                    } catch (Exception e) {
                        e.printStackTrace();
                        AppLogger.e(TAG,"Step 56-error--"+e.getMessage());
                    }
                }

                @Override
                public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {
                    Log.e(TAG, "An error occurred while saving:" + message);
                }
            });*/
        });
        ImageAnalysisConfig iac = new ImageAnalysisConfig
                .Builder()
                .setLensFacing(lens)
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis(iac);
        imageAnalysis.setAnalyzer(Runnable::run,
                new MLKitFacesAnalyzer(tv, iv, lens, this, this,binding.backgroundSqure,true));
        CameraX.bindToLifecycle(this, preview, imgCap, imageAnalysis);
    }

    private void closeDialog() throws IllegalArgumentException {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        try {
            Uri uri = Uri.fromFile(File.createTempFile("profile", ".jpg", getCacheDir()));
            File mypath = new File(uri.getPath());
            FileOutputStream fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return mypath.getAbsolutePath();
        } catch (Exception e) {

        }
        return "";
    }

    private void updateTransform() throws NullPointerException {
        Matrix mat = new Matrix();
        float centerX = tv.getWidth() / 2.0f;
        float centerY = tv.getHeight() / 2.0f;

        float rotationDegrees;
        switch (tv.getDisplay().getRotation()) {
            case Surface.ROTATION_0:
                rotationDegrees = 0;
                break;
            case Surface.ROTATION_90:
                rotationDegrees = 90;
                break;
            case Surface.ROTATION_180:
                rotationDegrees = 180;
                break;
            case Surface.ROTATION_270:
                rotationDegrees = 270;
                break;
            default:
                return;
        }
        mat.postRotate(rotationDegrees, centerX, centerY);
        tv.setTransform(mat);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        if (degree == 0 || bitmap == null) {
            return bitmap;
        }
        final Matrix matrix = new Matrix();
        matrix.setRotate(degree, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (allPermissionsGranted()) {
                tv.post(this::startCamera);
            } else {
               /* Toast.makeText(this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show();*/
                finish();
            }
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {

    }



    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case FACE_DETECT:
                boolean isDetect = (boolean) commonDataModel.getObject();
                if (isDetect) {
                    binding.backgroundSqure.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.square_box) );
                    binding.captureButtonSecondaryContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.backgroundSqure.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.red_square_box) );
                    binding.captureButtonSecondaryContainer.setVisibility(View.GONE);
                }
                break;
            case CAMERA_BITMAP_CALLBACK:
                    Bitmap mBitMap = (Bitmap)commonDataModel.getObject();
                break;


        }

    }

    private void openProgressDialog() {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            return;
        }
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(this);
        customDialogModel.setTitle(this.getResources().getString(R.string.processing));
        customDialogModel.setTwoButtonRequired(true);
        customProgressDialog = new CustomProgressDialog(customDialogModel);
        Objects.requireNonNull(customProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();
        customProgressDialog.updateDataUi(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
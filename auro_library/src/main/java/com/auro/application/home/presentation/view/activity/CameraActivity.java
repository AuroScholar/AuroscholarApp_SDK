package com.auro.application.home.presentation.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.exifinterface.media.ExifInterface;

import com.auro.application.R;
import com.auro.application.home.data.base_component.BaseActivity;
import com.auro.application.core.common.AppConstant;
import com.auro.application.databinding.CameraFragmentLayoutBinding;
import com.auro.application.util.AppLogger;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.camera.CameraOverlay;
import com.auro.application.util.camera.FaceOverlayGraphics;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.alert_dialog.CustomProgressDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CameraActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "CameraActivity";
    CameraSource mCameraSource;
    private static final int RC_HANDLE_GMS = 9001;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    int cameraID = 0;
    Camera.Parameters params;
    Camera camera;
    boolean isFlash = false;
    CameraFragmentLayoutBinding binding;
    public static boolean status;
    private boolean safeToTakePicture = true;
    CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        ViewUtil.setLanguageonUi(this);
        init();
    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayout());
        binding.setLifecycleOwner(this);
        // HomeActivity.setListingActiveFragment(HomeActivity.DEMOGRAPHIC_FRAGMENT);
        if (hasFrontCamera()) {
            cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
            Log.i("CAMERA_FACING_FRONT","cameraFacingFront"+cameraID);
            binding.flashContainer.setVisibility(View.GONE);
        }else {
            binding.flashContainer.setVisibility(View.VISIBLE);
        }


        setListener();
        //  flashIsAvailablesf();

        checkValueEverySecond();
    }

    @Override
    protected void onResume() {
        super.onResume();
        askPermission();
    }

    private void checkValueEverySecond() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status) {
                    binding.stillshot.setEnabled(true);
                    binding.captureButtonSecondaryContainer.animate().alpha(1F).start();
                } else {
                    binding.captureButtonSecondaryContainer.animate().alpha(0F).start();
                    binding.stillshot.setEnabled(false);
                }
                checkValueEverySecond();
            }
        }, 1000);

    }


    private void askPermission() {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }
        else
        {
            createCameraSource(cameraID);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                createCameraSource(cameraID);
            }
            else
            {
            }
        }
    }


    private void createCameraSource(int cameraID) {

        Context context = this.getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();


        detector.setProcessor(
                new MultiProcessor.Builder<>(new CameraActivity.GraphicFaceTrackerFactory())
                        .build());

        if (!detector.isOperational()) {

            AppLogger.e(TAG, "Face detector dependencies are not yet available.");
        }


        mCameraSource = new CameraSource.Builder(context, detector)
                .setFacing(cameraID)
                .setAutoFocusEnabled(true)
                .setRequestedFps(30.0f)
                .build();

        startCameraSource();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopCamera();
    }

    private void stopCamera() {
        if (mCameraSource != null) {
            mCameraSource.stop();
            mCameraSource.release();
            mCameraSource = null;

        }
    }

    private void startCameraSource() {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                this.getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                binding.preview.start(mCameraSource, binding.faceOverlay);
            } catch (IOException e) {
                Log.i(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.switch_orientation) {
            changeCamera();
            // flashIsAvailablesf();
        } else if (v.getId() == R.id.flash_toggle) {
            flashIsAvailable();

        } else if (v.getId() == R.id.stillshot) {
            if (safeToTakePicture) {
                clickPicture();
                safeToTakePicture = false;
            }

        }
    }

    private void changeCamera() {
        if (cameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
            cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
            binding.flashToggle.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_flash_off_black));
            Log.i("ChangeCamera","facing id"+cameraID);


            binding.flashContainer.setVisibility(View.GONE);

        } else {
            cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
            binding.flashContainer.setVisibility(View.VISIBLE);
        }
        binding.faceOverlay.clear();
        mCameraSource.release();
        createCameraSource(cameraID);

        //  flashIsAvailablesf();
    }


    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new CameraActivity.GraphicFaceTracker(binding.faceOverlay);
        }
    }

    private class GraphicFaceTracker extends Tracker<Face> {
        private final CameraOverlay mOverlay;
        private final FaceOverlayGraphics faceOverlayGraphics;

        GraphicFaceTracker(CameraOverlay overlay) {
            mOverlay = overlay;
            faceOverlayGraphics = new FaceOverlayGraphics(overlay);
        }


        @Override
        public void onNewItem(int faceId, Face item) {

            faceOverlayGraphics.setId(faceId);
            status = true;

        }

        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(faceOverlayGraphics);
            faceOverlayGraphics.updateFace(face);
        }

        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {

            mOverlay.remove(faceOverlayGraphics);

        }

        @Override
        public void onDone() {
            mOverlay.remove(faceOverlayGraphics);
            status = false;

        }
    }

    private void clickPicture() {
        openProgressDialog();
        binding.loadingSpinner.setVisibility(View.GONE);
        mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                try {

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

    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        if (degree == 0 || bitmap == null) {
            return bitmap;
        }
        final Matrix matrix = new Matrix();
        matrix.setRotate(degree, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private String SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/auroca_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "auro_profile" + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }
        try {
            boolean f = file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return file.getAbsolutePath();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        try {
            AppLogger.e("chhonker saveToInternalStorage-","--"+bitmapImage.getByteCount());
            Uri uri = Uri.fromFile(File.createTempFile("profile", ".jpg", getCacheDir()));
            AppLogger.e("chhonker saveToInternalStorage-","--"+uri.getPath());
            File mypath = new File(uri.getPath());
            FileOutputStream fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.close();
            AppLogger.e("chhonker saveToInternalStorage-","--"+mypath.getAbsolutePath());
            return mypath.getAbsolutePath();
        } catch (Exception e) {

        }
        return "";
    }



    private void loadImageFromStorage(String path) {
        try {

            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            this.getSupportFragmentManager().popBackStack();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean hasFrontCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.i("hasFrontCamera","FrontCamera"+cameraInfo.facing);
                return true;
            }
        }
        return false;
    }



    private void flashIsAvailable() {
        boolean hasFlash = this.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        Log.i("flashIsAva_outside","hashFlash"+hasFlash+"cameraId"+cameraID);
        if (hasFlash && cameraID == 0) {
            Log.i("flashIsAva_inside","hashFlash"+hasFlash+"cameraId"+cameraID);
            changeFlashStatus();
        }else if(hasFlash && cameraID == 1){
            Log.i("flashIsAva_inside","hashFlash"+hasFlash+"cameraId"+cameraID);
            changeFlashStatus();
        }
    }

    public void changeFlashStatus() {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    camera = (Camera) field.get(mCameraSource);
                    if (camera != null) {
                        params = camera.getParameters();
                        if (!isFlash) {
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            binding.flashToggle.setImageDrawable(this.getDrawable(R.drawable.ic_flash_on_black));
                            isFlash = true;
                            Log.i("changeFlashStatus ","isFlash "+isFlash);
                        } else {
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            binding.flashToggle.setImageDrawable(this.getDrawable(R.drawable.ic_flash_off_black));
                            isFlash = false;
                            Log.i("changeFlashStatus","isFlash "+isFlash);
                        }
                        camera.setParameters(params);
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    @Override
    protected void setListener() {
        binding.stillshot.setOnClickListener(this);
        binding.switchOrientation.setOnClickListener(this);
        binding.flashToggle.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.camera_fragment_layout;
    }

    private void releaseCamera() {
        if (binding.faceOverlay != null) {
            binding.faceOverlay.clear();
        }
        stopCamera();
    }


    private void openProgressDialog() {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            return;
        }
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(this);
        customDialogModel.setTitle("Processing...");
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
}

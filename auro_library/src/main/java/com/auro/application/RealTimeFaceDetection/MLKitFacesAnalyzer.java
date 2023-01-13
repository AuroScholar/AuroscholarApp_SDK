package com.auro.application.RealTimeFaceDetection;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

public class MLKitFacesAnalyzer implements ImageAnalysis.Analyzer {
    private static final String TAG = "MLKitFacesAnalyzer";
    private FirebaseVisionFaceDetector faceDetector;
    private final TextureView tv;
    private final ImageView iv;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint dotPaint, linePaint;
    private float widthScaleFactor = 1.0f;
    private float heightScaleFactor = 1.0f;
    private FirebaseVisionImage fbImage;
    private final CameraX.LensFacing lens;
    private final Context context;
    CommonCallBackListner commonCallBackListner;
    RelativeLayout squareBox;
    boolean isRequired=false;


    public MLKitFacesAnalyzer(TextureView tv, ImageView iv, CameraX.LensFacing lens, Context context, CommonCallBackListner commonCallBackListner, RelativeLayout squareBox,boolean isRequired) {
        this.tv = tv;
        this.iv = iv;
        this.lens = lens;
        this.context = context;
        this.commonCallBackListner = commonCallBackListner;
        this.squareBox = squareBox;
        this.isRequired=isRequired;
    }

    @Override
    public void analyze(ImageProxy image, int rotationDegrees) {
        if (image == null || image.getImage() == null) {
            return;
        }
        int rotation = degreesToFirebaseRotation(rotationDegrees);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fbImage = FirebaseVisionImage.fromMediaImage(image.getImage(), rotation);
        }

        //getSquareCordinates();
        initDetector();

    }

    public void getSquareCordinates(){

    }
    private void initDetector() {
        try {

            bitmap = Bitmap.createBitmap(tv.getWidth(), tv.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            widthScaleFactor = canvas.getWidth() / (fbImage.getBitmap().getWidth() * 1.0f);
            heightScaleFactor = canvas.getHeight() / (fbImage.getBitmap().getHeight() * 1.0f);


            Log.v("Mobile", "canvas.getWidth()" + canvas.getWidth() + "Width------>" + widthScaleFactor + "----  tv--->  " + tv.getWidth() + "   -----BitMap  " + fbImage.getBitmap().getWidth());
            Log.v("Mobile", "canvas.getWidth()" + canvas.getHeight() + "Height------>" + heightScaleFactor + "----  tv--->  " + tv.getHeight() + "    -----BitMap  " + fbImage.getBitmap().getHeight());
            FirebaseVisionFaceDetectorOptions detectorOptions = new FirebaseVisionFaceDetectorOptions
                    .Builder()
                    .enableTracking()
                    .build();
            FirebaseVisionFaceDetector faceDetector = FirebaseVision.getInstance().getVisionFaceDetector(detectorOptions);
            if (commonCallBackListner != null) {
                //Log.e("chhonker-","callback calling 2");
                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.CAMERA_BITMAP_CALLBACK, fbImage.getBitmap()));
                faceDetector.detectInImage(fbImage).addOnSuccessListener(firebaseVisionFaces -> {
                    processFaces(firebaseVisionFaces);
                }).addOnFailureListener(e -> Log.i(TAG, e.toString()));
            }
        }catch (Exception e)
        {
            AppLogger.e("initDetector-",e.getMessage());
        }
    }

    private void processFaces(List<FirebaseVisionFace> faces) {

        if((faces.size()!=0 && faces.size()<2)) {
            for (FirebaseVisionFace face : faces) {



                AppLogger.v("BoxBound", " top translateX: " + face.getBoundingBox().top
                        + " left translateX: " + face.getBoundingBox().left
                        + " bottom translateY: " + face.getBoundingBox().bottom
                        + " right translateX: " + face.getBoundingBox().right);
                Rect offSetViewBounds = new Rect();
                if(isRequired) {

                    squareBox.getGlobalVisibleRect(offSetViewBounds);

                    AppLogger.v("SquareBox",
                            "Left== " +(int)translateX(face.getBoundingBox().left)+" _____ "+ (int) (offSetViewBounds.left + 220)+
                            "  Right== " + (int) translateX(face.getBoundingBox().right) +" ____ "+(int) (offSetViewBounds.right-220) +
                                    "  Top== " +(int) translateY(face.getBoundingBox().top)+" _____ "+ (int) offSetViewBounds.top +
                                    "  Bottom== " + (int) translateY(face.getBoundingBox().bottom)+" ____ "+(int) offSetViewBounds.bottom);

                }


                if ((int)(offSetViewBounds.left+220) <= (int)translateX(face.getBoundingBox().left) &&
                        (int)offSetViewBounds.top <= (int) translateY(face.getBoundingBox().top )&&
                        (int)(offSetViewBounds.right-220)>= (int) translateX(face.getBoundingBox().right) &&
                        (int) offSetViewBounds.bottom >= (int) translateY(face.getBoundingBox().bottom)) {
                    Rect bounds = face.getBoundingBox();
                    float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                    float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
                    // backgroundSqure.setBackground();
                    AppLogger.i("Head", "bounds: " + bounds + "   getHeadEulerAngleY:  " + (int) rotY + "  getHeadEulerAngleZ:  " + (int) rotZ);
                    if((int) rotY <15 && (int) rotY > (-15) &&(int) rotZ<15 && (int) rotZ > (-15)  ){
                        AppLogger.i("Head", "INSIDE bounds: " + bounds + "   getHeadEulerAngleY:  " + (int) rotY + "  getHeadEulerAngleZ:  " + (int) rotZ);
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.FACE_DETECT, true));
                    }else{
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.FACE_DETECT, false));
                    }


                    //backgroundSqure.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.square_box) );
                }else{
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.FACE_DETECT, false));
                    //backgroundSqure.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.red_square_box) );
                }



              /*  if ((face.getBoundingBox().left > 1 && face.getBoundingBox().left < 200) &&
                        (face.getBoundingBox().right > 200 && face.getBoundingBox().right < 950) &&
                        (face.getBoundingBox().top > 100 && face.getBoundingBox().top < 450) &&
                        (face.getBoundingBox().bottom > 300 && face.getBoundingBox().bottom < 1098)) {

                    AppLogger.v("Pradeep_vission", "Face Detect");
                    AppLogger.v("Pradeep_vission", face.getBoundingBox().left + " Left  Face Detect");
                    AppLogger.v("Pradeep_vission", face.getBoundingBox().top + "    Top   Face Detect");
                    AppLogger.v("Pradeep_vission", face.getBoundingBox().right + "   Right  Face Detect");
                    AppLogger.v("Pradeep_vission", face.getBoundingBox().bottom + "   Bottom  Face Detect");
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.FACE_DETECT, true));

                } else {
                    AppLogger.v("Pradeep_vission", "Not Face Detect");
                    AppLogger.v("Pradeep_vission", face.getBoundingBox().left + " Left Not Face Detect");
                    AppLogger.v("Pradeep_vission", face.getBoundingBox().top + "    Top  Not Face Detect");
                    AppLogger.v("Pradeep_vission", face.getBoundingBox().right + "   Right Not  Face Detect");
                    AppLogger.v("Pradeep_vission", face.getBoundingBox().bottom + "   Bottom Not  Face Detect");
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.FACE_DETECT, false));
                }*/
            }
        }else{
            commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.FACE_DETECT, false));
        }
        iv.setImageBitmap(bitmap);
    }
    private float translateY(float y) {
        return y * heightScaleFactor;
    }

    private float translateX(float x) {
        float scaledX = x * widthScaleFactor;
        if (lens == CameraX.LensFacing.FRONT) {
            return canvas.getWidth() - scaledX;
        } else {
            return scaledX;
        }
    }

    private int degreesToFirebaseRotation(int degrees) {
        switch (degrees) {
            case 0:
                return FirebaseVisionImageMetadata.ROTATION_0;
            case 90:
                return FirebaseVisionImageMetadata.ROTATION_90;
            case 180:
                return FirebaseVisionImageMetadata.ROTATION_180;
            case 270:
                return FirebaseVisionImageMetadata.ROTATION_270;
            default:
                throw new IllegalArgumentException("Rotation must be 0, 90, 180, or 270.");
        }
    }


}
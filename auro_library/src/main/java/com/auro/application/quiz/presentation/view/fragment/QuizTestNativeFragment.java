package com.auro.application.quiz.presentation.view.fragment;

import static com.auro.application.RealTimeFaceDetection.CameraxActivity.lens;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.RealTimeFaceDetection.MLKitFacesAnalyzer;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.QuizTestNativeLayoutBinding;

import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.AssignmentResModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.QuizResModel;
import com.auro.application.home.data.model.SaveImageReqModel;


import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.fragment.DemographicFragment;

import com.auro.application.home.presentation.view.fragment.MainQuizHomeFragment;
import com.auro.application.quiz.data.model.getQuestionModel.FetchQuizResModel;
import com.auro.application.quiz.data.model.getQuestionModel.QuestionResModel;
import com.auro.application.quiz.data.model.response.SaveQuestionResModel;
import com.auro.application.quiz.data.model.submitQuestionModel.OptionResModel;
import com.auro.application.quiz.presentation.view.adapter.SelectQuizOptionAdapter;
import com.auro.application.quiz.presentation.viewmodel.QuizTestNativeViewModel;
import com.auro.application.teacher.data.model.SelectResponseModel;


import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.alert_dialog.CustomDialogModel;

import com.auro.application.util.alert_dialog.CustomProgressDialog;


import com.auro.application.util.AppLogger;
import com.auro.application.util.ViewUtil;

import com.auro.application.util.alert_dialog.ExitDialog;
import com.auro.application.util.alert_dialog.InstructionDialog;
import com.auro.application.util.alert_dialog.NativeQuizImageDialog;
import com.auro.application.util.broadcastreceiver.NetworkChangeReceiver;

import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringDynamic;
import com.auro.application.util.timer.Hourglass;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;


public class QuizTestNativeFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener, View.OnTouchListener {

    @Inject
    @Named("QuizTestNativeFragment")
    ViewModelFactory viewModelFactory;
    QuizTestNativeLayoutBinding binding;
    QuizTestNativeViewModel viewModel;
    List<SelectResponseModel> list;
    SelectQuizOptionAdapter mcSelectQuizAdapter;
    int START_TIME_IN_MILLIS = 60000 * 10;
    int COUNTDOWN_INTERVAL = 1000;
    Dialog customInstructionDialog;
    FetchQuizResModel fetchQuizResModel;
    List<QuestionResModel> quizQuestionList;
    /*Camera Params*/
    OptionResModel optionResModel;
    boolean clickPictureStatus;
    CustomProgressDialog customFinishProgressDialog;
    String TAG = QuizTestNativeFragment.class.getSimpleName();
    public AlertDialog dialogQuit;

    // MUST BE CAREFUL USING THIS VARIABLE.
    public static boolean status;
    AlertDialog dialog;
    Dialog dialogFinish;
    ExitDialog attemptedExitDialog;
    Timer timer;
    int countQuiz = 0;
    DashboardResModel dashboardResModel;
    QuizResModel quizResModel;
    AssignmentReqModel assignmentReqModel;

    private BroadcastReceiver mNetworkReceiver;
    public static AlertDialog internetDialog;

    boolean apiCalling = false;
    boolean interDialogOpen = false;
    boolean alreadyAttemptedDialog = false;

    String buttonText = "";
    QuestionResModel questionResModel;
    FaceDetector.Detections<Face> detectionResultsList;
    boolean finishDialogClick;
    String submitStatus;
    boolean submitQuizFromExitDialog = false;
    boolean isQuizStarted = false;
    AssignmentResModel assignmentResModel;
    /*Camera x code */
    Handler handler = new Handler();
    Runnable runnable;
    private TextureView tv;
    private ImageView iv;
    Bitmap bitMapNew;
    CountDownTimer countDownTimerofme;
    PrefModel prefModel ;
    /*End of cmera x code*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
            ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(QuizTestNativeViewModel.class);
            binding.setLifecycleOwner(this);
        }
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.NATIVE_QUIZ_FRAGMENT);
        askPermission();
        if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
            quizResModel = getArguments().getParcelable(AppConstant.QUIZ_RES_MODEL);
        }
        setRetainInstance(true);
        init();
        openInstructionDialog();
        callFetchQuestionApi();
        /*Register Broad Cast*/
        registerNetworkBroadcastForNougat();
        AppUtil.commonCallBackListner = this;
        return binding.getRoot();
    }


    void cameraInitiate() {
        // Start quiz
        tv = binding.faceTextureView;
        iv = binding.faceImageView;
        //camera Activity
        try {
            tv.post(this::startCamera);
            tv.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> updateTransform());

        } catch (IllegalStateException e) {
            AppLogger.e("CamerXPradeep", "Pradeep Kumar Baral");
        }

    }

    @SuppressLint("RestrictedApi")
    private void startCamera() {
        try {
            initCamera();
        } catch (Exception e) {
            AppLogger.e("CameraX expection", e.getMessage());
        }
    }

    private void initCamera() {
        CameraX.unbindAll();
        PreviewConfig pc = new PreviewConfig
                .Builder()
                .setTargetResolution(new Size(tv.getWidth(), tv.getHeight()))
                .setLensFacing(lens)
                .build();

        Preview preview = new Preview(pc);
        preview.setOnPreviewOutputUpdateListener(output -> {
            ViewGroup vg = (ViewGroup) tv.getParent();
            vg.removeView(tv);
            vg.addView(tv, 0);
            tv.setSurfaceTexture(output.getSurfaceTexture());
        });

        ImageAnalysisConfig iac = new ImageAnalysisConfig
                .Builder()
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .setTargetResolution(new Size(tv.getWidth(), tv.getHeight()))
                .setLensFacing(lens)
                .build();


        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.getDashboardResModel().setIs_native_image_capturing(true);
        AppLogger.e("checkNativeCameraEnableOrNot--", "" + prefModel.getDashboardResModel().isIs_native_image_capturing());
        if (prefModel.getDashboardResModel() != null && prefModel.getDashboardResModel().isIs_native_image_capturing()) {
            ImageAnalysis imageAnalysis = new ImageAnalysis(iac);
            imageAnalysis.setAnalyzer(Runnable::run, new MLKitFacesAnalyzer(tv, iv, lens, getActivity(), this, null, false));
            CameraX.bindToLifecycle(this, preview, imageAnalysis);
        }
    }

    private void updateTransform() {
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
    protected void init() {
        setListener();

        setTimerProgress(countQuiz + 1);

        if (dashboardResModel != null && quizResModel != null) {
            assignmentReqModel = viewModel.quizNativeUseCase.getAssignmentRequestModel(dashboardResModel, quizResModel);
            //    quizTestViewModel.getAssignExamData(assignmentReqModel);
        }
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        assignmentResModel = prefModel.getAssignmentResModel();


    }

    private void setQuizData(QuestionResModel model) {
        setTextOfButton();
        questionResModel = model;
        if (!TextUtil.isEmpty(model.getImageName())) {
            binding.quesImg.setVisibility(View.VISIBLE);
            ImageUtil.loadNormalImage(binding.quesImg, model.getImageName());
        } else {
            binding.quesImg.setVisibility(View.GONE);
        }
        String sourceString = "<b>" + (countQuiz + 1) + ". ) " + "</b> " + " " + model.getQuestion();
        binding.subjectTitle.setText(quizResModel.getSubjectName());
        setQuestionText(sourceString);
        setOptionAdapter(model.getOptionResModelList());
    }

    private void setQuestionText(String quesText) {
        binding.quesText.setText(Html.fromHtml(quesText));
    }

    private void callFetchQuestionApi() {
        AppLogger.e(TAG, " callFetchQuestionApi Step 1");
        if (!apiCalling) {
            AppLogger.e(TAG, "callFetchQuestionApi Step 2");
            apiCalling = true;
            if (dashboardResModel != null && quizResModel != null) {
                AppLogger.e(TAG, "callFetchQuestionApi Step 3");
                assignmentReqModel = viewModel.quizUseCase.getAssignmentRequestModel(dashboardResModel, quizResModel);
                assignmentReqModel.setExamId(assignmentResModel.getExamId());
            }
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            assignmentReqModel.setUserPreferedLanguageId( Integer.parseInt(prefModel.getUserLanguageId()));
            viewModel.fetchQuizData(assignmentReqModel);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((DashBoardMainActivity) getActivity()).showBottomNavigationView();
        viewModel.serviceLiveData().removeObservers(this);
        if (timer != null) {
            timer.cancel();
        }

        unregisterNetworkChanges();
        if (dialogQuit != null && dialogQuit.isShowing()) {
            dialogQuit.dismiss();
        }

        if (((DashBoardMainActivity) getActivity()).dialogQuit != null && ((DashBoardMainActivity) getActivity()).dialogQuit.isShowing()) {
            ((DashBoardMainActivity) getActivity()).dialogQuit.dismiss();
        }
        if (timer != null) {
            timer.cancel();
        }

        if (countDownTimerofme != null) {
            countDownTimerofme.cancel();
        }
        closeCameraDialog();
        closeAllDialog();
    }

    void closeCameraDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            closeCameraDialog();
        }
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        binding.progressBar.setOnClickListener(this);
        binding.exitBt.setOnClickListener(this);
        binding.saveNextBt.setOnClickListener(this);
        binding.quesImg.setOnTouchListener(this);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }

        binding.quesImg.setOnClickListener(this);
        binding.finishNextBt.setOnClickListener(this);
        binding.exitBt.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.quiz_test_native_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setToolbar();
        setListener();
        AppStringDynamic.setQuizTestNativePageStrings(binding);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        AppUtil.commonCallBackListner = this;
        AppUtil.dashboardResModel = null;
        ((DashBoardMainActivity) getActivity()).setProgressVal();
        ((DashBoardMainActivity) getActivity()).hideBottomNavigationView();
        if (checkGooglePlayAvailability()) {
            askPermission();
        }
        try {
            AppLogger.e("chhonker", "try block");
          /*  AudioManager mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            mAudioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            mAudioManager.setStreamMute(AudioManager.STREAM_RING, true);
            mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);*/
        } catch (Exception e) {
            AppLogger.e("chhonker", "catch block-" + e.getMessage());
        }
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case OPTION_SELECT_API:
                optionResModel = (OptionResModel) commonDataModel.getObject();
                break;

            case NO_INTERNET_BROADCAST:
                boolean val = (boolean) commonDataModel.getObject();
                if (val) {
                    checkInternetBeforeCallApi();
                } else {
                    openNoInterNetDialog(val);
                }

                break;
            case EXIT_DIALOG_CLICK:
                try{
                    getActivity().getSupportFragmentManager().popBackStack();
                }catch (Exception e){

                }

                break;

            case FINISH_DIALOG_CLICK:
                finishDialogClick = true;
                submitStatus = AppConstant.QuizFinishStatus.EXIT_BY_STUDENT;
                submitQuizFromExitDialog = true;
                callSaveQuestionApi();
                break;

            case LISTNER_SUCCESS:
                AppLogger.v("SaveQuiz", "Step 001" + "Assignment req Model" + quizResModel.getSubjectPos());
                AppUtil.dashboardResModel = (DashboardResModel) commonDataModel.getObject();
                //closeAllDialog();
                // closeProgress();
                customFinishProgressDialog.dismiss();
                getActivity().getSupportFragmentManager().popBackStack();
                /*  getActivity().getSupportFragmentManager().popBackStack();*/
                break;
            case LISTNER_FAIL:
                /*Do code here*/
                closeProgress();
                customFinishProgressDialog.dismiss();
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            case OPTION_IMAGE_CLICK:
                /*Do code here*/
                openImageMaxDialog((String) commonDataModel.getObject());
                break;

            case CAMERA_BITMAP_CALLBACK:
                bitMapNew = (Bitmap) commonDataModel.getObject();
                // captureImage();
                break;

        }
    }

    void checkInternetBeforeCallApi() {
        openNoInterNetDialog(true);
        viewModel.quizNativeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                binding.progressBar.setVisibility(View.VISIBLE);
                callFetchQuestionApi();
            } else {
                // please check your internet
                AppLogger.e(TAG, "checkInternetBeforeCallApi");
                openNoInterNetDialog(false);
            }
        });
    }

    void openNoInterNetDialog(boolean value) {
        if (getActivity() != null) {
            setTextOfButton();
            if (!value) {
                interDialogOpen = true;
                if (internetDialog != null && internetDialog.isShowing()) {
                    internetDialog.dismiss();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                if (!isQuizStarted) {
                    builder.setMessage(getActivity().getResources().getString(R.string.quiz_internet));
                } else {
                    builder.setMessage(getActivity().getResources().getString(R.string.quiz_internet_2));
                }
                // Set the alert internetDialog yes button click listener
                builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + getActivity().getResources().getString(R.string.retry) + "</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        interDialogOpen = false;
                        dialog.dismiss();
                        checkInternetBeforeCallApi();
                    }
                });
                internetDialog = builder.create();
                internetDialog.setCancelable(false);
                // Display the alert internetDialog on interface

                if (customFinishProgressDialog != null && customFinishProgressDialog.isShowing()) {
                    customFinishProgressDialog.dismiss();
                }
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                closeAllDialog();
                internetDialog.show();
            } else {
                if (internetDialog != null && internetDialog.isShowing()) {
                    interDialogOpen = false;
                    internetDialog.dismiss();
                }
            }
        }
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.exit_bt) {
            openDialogForQuit();
            AppLogger.e(TAG, "onClick exit_bt");
        } else if (v.getId() == R.id.save_next_bt) {
            AppLogger.i("Countdown", "onClick");
            if (!interDialogOpen) {
                String buttonText=getActivity().getResources().getString(R.string.save_submit);
                try {
                    LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
                    Details details = model.getDetails();
                    buttonText=details.getSaveSubmit() != null ? details.getSaveSubmit() : AuroApp.getAppContext().getResources().getString(R.string.save_submit);
                } catch (Exception e) {
                    AppLogger.e(TAG, e.getMessage());
                }


                if (binding.saveNextBt.getText().toString().equalsIgnoreCase(buttonText)){
                    openDialogFinishQuiz();
                } else {
                    callSaveQuestionApi();

                }
            }


            // initRecordingTimer();

        } else if (v.getId() == R.id.ques_img) {
            openImageMaxDialog(questionResModel.getImageName());
        } else if (v.getId() == R.id.finish_next_bt) {
            // openDialogForQuit();
            ((DashBoardMainActivity) getActivity()).openDialogForQuit();
        }
    }

    void openImageMaxDialog(String url) {
        NativeQuizImageDialog yesNoAlert = NativeQuizImageDialog.newInstance(url);
        yesNoAlert.show(getParentFragmentManager(), null);
    }

    private boolean checkGooglePlayAvailability() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode == ConnectionResult.SUCCESS) {
            return true;
        } else {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(getActivity(), resultCode, 2404).show();
            }
        }
        return false;
    }

    private void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    /*Camera Permisiipon*/
    private void askPermission() {
//        String rationale = "Please provide camera permission so that you can ...";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(getActivity(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
                cameraInitiate();
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//                getActivity().finish();
//            }
//        });
    }

    void checkNativeCameraEnableOrNot() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.getDashboardResModel().setIs_native_image_capturing(true);
        AppLogger.e("checkNativeCameraEnableOrNot--", "" + prefModel.getDashboardResModel().isIs_native_image_capturing());
        if (prefModel.getDashboardResModel() != null && prefModel.getDashboardResModel().isIs_native_image_capturing()) {
            if (assignmentResModel != null && !TextUtil.isEmpty(assignmentResModel.getExamAssignmentID())) {
                AppLogger.e("checkNativeCameraEnableOrNot--", "==" + prefModel.getDashboardResModel().isIs_native_image_capturing());
                startCamera();
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                }

                captureImage();
                AppLogger.e("checkNativeCameraEnableOrNot--", "previewView VISIBLE");
                binding.previewView.setVisibility(View.VISIBLE);
            }
        } else {
            AppLogger.e("checkNativeCameraEnableOrNot--", "previewView INVISIBLE");

            binding.previewView.setVisibility(View.INVISIBLE);
        }

    }

    void captureImage() {
        runnable = new Runnable() {
            public void run() {
                Bitmap bitmap = bitMapNew;
                if (bitmap != null) {
                    //  binding.Privew.setImageBitmap(bitmap);  todo Pradeep
                    processImage(bitmap);
                }
                captureImage();

            }
        };
        handler.postDelayed(runnable, 10000);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }


    private void checkValueEverySecond() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //   Toast.makeText(getActivity(), "status " + status, Toast.LENGTH_SHORT).show();
                            if (!status) {
                                if (dialog == null) {
                                    //  openDialog();
                                } else {
                                    if (!dialog.isShowing() & !apiCalling && isVisible() && !interDialogOpen && !alreadyAttemptedDialog) {
                                        dialog.show();
                                    }
                                }
                            } else {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }

                        }
                    });
                }

            }

        }, 0, 700);
    }


    void processImage(Bitmap picBitmap) {
        byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
        long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
        int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
        //  AppLogger.d(TAG, "Image Path Size mb- " + mb + "-bytes-" + file_size);

        if (file_size >= 500) {
            assignmentReqModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
        } else {
            assignmentReqModel.setImageBytes(bytes);
        }
        int new_file_size = Integer.parseInt(String.valueOf(assignmentReqModel.getImageBytes().length / 1024));
        //AppLogger.d(TAG, "Image Path  new Size kb- " + mb + "-bytes-" + new_file_size);
        AppLogger.v("QuizNew", "Call send exam api STEP 0");
         callSendExamImageApi();
    }


    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.not_showing_face);
        // Set the alert dialog yes button click listener
        String ok = getActivity().getResources().getString(R.string.ok);
        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + ok + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Set the alert dialog no button click listener
       /* builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>NO</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/

        dialog = builder.create();
        dialog.setCancelable(false);
        // Display the alert dialog on interface
    }


    public void setOptionAdapter(List<OptionResModel> listQuizOption) {
        binding.rvselectQuizOption.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvselectQuizOption.setHasFixedSize(true);
        binding.rvselectQuizOption.setNestedScrollingEnabled(false);
        mcSelectQuizAdapter = new SelectQuizOptionAdapter(getActivity(), listQuizOption, this);
        binding.rvselectQuizOption.setAdapter(mcSelectQuizAdapter);

    }


    void callSaveQuestionMethod() {
        if (countQuiz == quizQuestionList.size() - 1) {
            submitStatus = AppConstant.QuizFinishStatus.AUTO_SUBMITTED;
        }

        try {
            submitStatus = AppConstant.QuizFinishStatus.AUTO_SUBMITTED;
            binding.saveNextBt.setText(getActivity().getResources().getString(R.string.save_submit));
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.saveNextBt.setText(details.getSaveSubmit() != null ? details.getSaveSubmit() : AuroApp.getAppContext().getResources().getString(R.string.save_submit));
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }

        if (!interDialogOpen) {
            callSaveQuestionApi();
              /*  if (optionResModel != null) {
                   //
                } else {*/
               /* if (countQuiz == quizQuestionList.size() - 1) {
                    callSaveQuestionApi();
                    //callFinishQuizApi();
                } else {
                    //   goToNextQuiz();
                    callSaveQuestionApi();
                }*/
            //}
        }
    }


    public void stopmeplease() {
        countDownTimerofme.cancel();
    }

    public void goToNextQuiz() {
        if (countQuiz < quizQuestionList.size()) {
            countQuiz++;
        }
        setTimerProgress(countQuiz + 1);
        if (countQuiz < quizQuestionList.size()) {
            AppLogger.i(TAG, "goToNextQuiz");
            setQuizData(quizQuestionList.get(countQuiz));
        }
        setTextOfButton();

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
     /*   Dialog nagDialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(false);
        nagDialog.setContentView(R.layout.preview_image);
        ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);
        ivPreview.setBackgroundDrawable();
        nagDialog.show();*/

    }

    private void observeServiceResponse() {
        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    isQuizStarted = true;
                    apiCalling = false;
                    AppLogger.e("observeServiceResponse step 1", "SUCCESS");
                    checkResponse(responseApi);
                   /* try {


                    }catch (Exception e)
                    {
                        AppLogger.e("observeServiceResponse step 2", "Exception--"+ e.getMessage());
                    }*/
                    break;

                case NO_INTERNET:
                    apiCalling = false;
                    closeAllDialog();
                    setTextOfButton();
                    openNoInterNetDialog(false);
                    binding.progressBar.setVisibility(View.GONE);
                    AppLogger.e("observeServiceResponse step 3", "NO_INTERNET");
                    break;

                case AUTH_FAIL:
                case FAIL_400:
                    apiCalling = false;
                    AppLogger.e(TAG, "AUTH_FAIL");
                    setTextOfButton();
                    binding.progressBar.setVisibility(View.GONE);
                    closeAllDialog();
                    AppLogger.e("observeServiceResponse step 4", "FAIL_400 AUTH_FAIL");
                    break;
                default:
                    apiCalling = false;
                    closeAllDialog();
                    if (getActivity() != null) {
                        setTextOfButton();
                        binding.progressBar.setVisibility(View.GONE);
                        AppLogger.e("observeServiceResponse step 5", "default");
                    }
                    break;
            }
        });
    }

    private void closeAllDialog() {
        AppLogger.e("closeAllDialog-", "i am calling");
        if (customFinishProgressDialog != null && customFinishProgressDialog.isShowing()) {
            customFinishProgressDialog.dismiss();
            AppLogger.e("closeAllDialog-", "" + customFinishProgressDialog.isShowing());
        }
        if (customInstructionDialog != null && customInstructionDialog.isShowing()) {
            customInstructionDialog.dismiss();
        }
        if (dialogFinish != null && dialogFinish.isShowing()) {
            dialogFinish.dismiss();
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (attemptedExitDialog != null && attemptedExitDialog.isShowing()) {
            alreadyAttemptedDialog = false;
            attemptedExitDialog.dismiss();
        }

        closeProgress();

    }

    private void closeProgress() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.GONE);
            }
        }, 500);
    }

    void closeInstructionDialog() {
        if (customInstructionDialog != null) {
            customInstructionDialog.dismiss();
        }
    }

    private void openInstructionDialog() {
        if (getContext() != null) {
           /* customInstructionDialog = new InstructionDialog(getContext());
            // Window window = customDialog.getWindow();
            // window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(customInstructionDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            customInstructionDialog.getWindow().setAttributes(lp);
            Objects.requireNonNull(customInstructionDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customInstructionDialog.setCancelable(false);
            customInstructionDialog.show();*/
        }

    }


    private void makeOptionsList() {
        AppLogger.e("observeServiceResponse step 6", "makeOptionsList");

        for (QuestionResModel resModel : fetchQuizResModel.getQuestionResModelList()) {
            List<OptionResModel> list = new ArrayList<>();
            AppLogger.e("observeServiceResponse step 7_1", "json full-" + resModel.getOptionDetails());
            String jsonString = resModel.getOptionDetails().replaceAll("\\\\", "");
            AppLogger.e("observeServiceResponse step 7", "json string-" + jsonString);

            Map<String, String> retMap = new Gson().fromJson(
                    jsonString, new TypeToken<HashMap<String, Object>>() {
                    }.getType()
            );
            Iterator myVeryOwnIterator = retMap.keySet().iterator();
            AppLogger.e("observeServiceResponse step 8", "myVeryOwnIterator");
            while (myVeryOwnIterator.hasNext()) {
                String key = (String) myVeryOwnIterator.next();
                String value = (String) retMap.get(key);

                AppLogger.e("observeServiceResponse step 9", "key-" + key);
                try {
                    AppLogger.e("observeServiceResponse   10", "");
                    //AppLogger.e("value_pradeep","Step 0  Value  "+value);
                    if (!TextUtil.isEmpty(value) && !containsURL(value)) {
                        AppLogger.e("observeServiceResponse  if 11", "--" + value);
                        String option[] = value.split("_");
                        OptionResModel optionResModel = new OptionResModel();
                        optionResModel.setCheck(false);
                        optionResModel.setOption(option[1]);
                        optionResModel.setIndex(key);
                        optionResModel.setOptionId(option[0]);
                        list.add(optionResModel);

                    } else {
                        AppLogger.e("observeServiceResponse  else 12", "--" + value);
                        String option[] = value.split("_");
                        OptionResModel optionResModel = new OptionResModel();
                        optionResModel.setCheck(false);
                       // AppLogger.e("value_pradeep","Step 1  Value  "+value.split("https:",2));
                        AppLogger.e("value_pradeep","Step 1  Value array "+option);
                        //AppLogger.e("value_pradeep","Step 3  Value  retrieveLinks "+retrieveLinks(value));
                        String htmlImage = value.substring(value.indexOf("https"));
                        AppLogger.e("value_pradeep","Step 2  Value  "+htmlImage);
                        optionResModel.setOption(htmlImage);
                        optionResModel.setIndex(key);
                        optionResModel.setOptionId(option[0]);
                        list.add(optionResModel);
                    }
                } catch (Exception e) {
                    AppLogger.e("observeServiceResponse  after split exception step 13", e.getMessage());
                    // AppLogger.e("chhonker- after split exception- ", e.getMessage());

                }
            }
            resModel.setOptionResModelList(list);
            AppLogger.e(TAG, "retMap-" + retMap);
        }
    }

    private void handleErrorLayout(int status, String msg) {
        switch (status) {
            case 1:
                AppLogger.e(TAG, "handleErrorLayout 1");
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.mainContentLayout.setVisibility(View.GONE);
                binding.errorLayout.errorIcon.setVisibility(View.GONE);
                binding.errorLayout.textError.setText(msg);
                binding.errorLayout.btRetry.setVisibility(View.GONE);
                break;
            case 2:
                AppLogger.e(TAG, "handleErrorLayout 2");
                binding.errorConstraint.setVisibility(View.GONE);
                binding.mainContentLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void callSaveQuestionApi() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        AppLogger.e(TAG, "callSaveQuestionApi 1");
        if (optionResModel == null) {
            AppLogger.e(TAG, "callSaveQuestionApi 2");
            optionResModel = new OptionResModel();
            optionResModel.setOptionId("0");
            optionResModel.setOption("");
            optionResModel.setCheck(false);
            optionResModel.setIndex("0");
        }
        if (!apiCalling) {
            try {
                apiCalling = true;
                buttonText = binding.saveNextBt.getText().toString();
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.saveNextBt.setText(getActivity().getResources().getString(R.string.saving));
                try {
                    LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
                    Details details = model.getDetails();
                    if (model != null) {
                        binding.saveNextBt.setText(details.getSaving() != null ? details.getSaving() :AuroApp.getAppContext().getResources().getString(R.string.saving));
                    }
                } catch (Exception e) {
                    AppLogger.e(TAG, e.getMessage());
                }
                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();

                SaveQuestionResModel saveQuestionResModel = new SaveQuestionResModel();
                saveQuestionResModel.setAnswerID(optionResModel.getOptionId());
                saveQuestionResModel.setExamAssignmentID(fetchQuizResModel.getExamAssignmentID());
                saveQuestionResModel.setQuestionID("" + quizQuestionList.get(countQuiz).getQuestionID());
                saveQuestionResModel.setQuestionSerialNo("" + (countQuiz + 1));
                saveQuestionResModel.setExamId(assignmentResModel.getExamId());
                saveQuestionResModel.setUserPreferedLanguage_id(Integer.parseInt(prefModel.getUserLanguageId()));
                viewModel.saveQuizData(saveQuestionResModel);
            } catch (Exception e) {

            }
        }
    }

    private void callFinishQuizApi() {
        if (!apiCalling) {
            apiCalling = true;
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            openFinishProgressDialog();
            SaveQuestionResModel saveQuestionResModel = new SaveQuestionResModel();
            saveQuestionResModel.setExamAssignmentID(fetchQuizResModel.getExamAssignmentID());
            saveQuestionResModel.setComplete_by(submitStatus);
            saveQuestionResModel.setExamId(assignmentResModel.getExamId());
            saveQuestionResModel.setUserPreferedLanguage_id(Integer.parseInt(prefModel.getUserLanguageId()));
            viewModel.finishQuiz(saveQuestionResModel);
        }
    }

    private void openDialogFinishQuiz() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getString(R.string.finish_quiz_txt));
        // Set the alert dialog yes button click listener
        String yes = getActivity().getResources().getString(R.string.yes);
        String no = getActivity().getResources().getString(R.string.no);
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                builder.setMessage(details.getFinishQuizTxt() != null ? details.getFinishQuizTxt()  : AuroApp.getAppContext().getResources().getString(R.string.finish_quiz_txt) );//
                yes=details.getYes();
                no=details.getNo();
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }

        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + yes + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogQuit, int which) {
                submitStatus = AppConstant.QuizFinishStatus.STUDENT;
                callSaveQuestionApi();
                dialogQuit.dismiss();

            }
        });
        // Set the alert dialog no button click listener
        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>" + no + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogQuit, int which) {
                dialogQuit.dismiss();

            }
        });



        dialogFinish = builder.create();
        dialogFinish.setCancelable(false);
        dialogFinish.show();
        // Display the alert dialog on interface
    }

    void checkResponse(ResponseApi responseApi) {
        switch (responseApi.apiTypeStatus) {
            case FETCH_QUIZ_DATA_API:
                AppLogger.e(TAG, "progressbar lock ");
                closeProgress();
                fetchQuizResModel = (FetchQuizResModel) responseApi.data;
                if (fetchQuizResModel.getError()) {
                    closeInstructionDialog();
                    openAlreadyAttemptedQuiz(getActivity().getResources().getString(R.string.quiz_error));
                } else {
                    if (fetchQuizResModel != null && fetchQuizResModel.getQuizStatus().equalsIgnoreCase(AppConstant.QUIZ_ATTEMPTED)) {
                        closeInstructionDialog();
                        openAlreadyAttemptedQuiz(getActivity().getResources().getString(R.string.already_finished));
                    } else {

                        quizQuestionList = fetchQuizResModel.getQuestionResModelList();
                        if (fetchQuizResModel.getQuestionResModelList() != null && fetchQuizResModel.getQuestionAttemped() == fetchQuizResModel.getQuestionResModelList().size()) {
                            countQuiz = fetchQuizResModel.getQuestionResModelList().size() - 1;
                            AppLogger.e("chhonke newr-", "coint size--" + countQuiz);
                        } else {
                            if (fetchQuizResModel.getQuestionAttemped() > 0) {
                                countQuiz = fetchQuizResModel.getQuestionAttemped();
                                setTimerProgress(countQuiz + 1);
                                AppLogger.e("chhonker old-", "coint size--" + countQuiz);
                            }
                        }
                        checkNativeCameraEnableOrNot();
                        closeInstructionDialog();
                        makeOptionsList();
                        handleErrorLayout(2, "");
                        setQuizData(quizQuestionList.get(countQuiz));
                        checkValueEverySecond();
                        startCountDownTimer();
                    }
                }
                break;

            case FINISH_QUIZ_API:
                SaveQuestionResModel resModel = (SaveQuestionResModel) responseApi.data;
                AppLogger.v("SaveQuiz", "Step 1" + resModel.getSubmitExamAPIResult().getScore());
                AppLogger.v("SaveQuiz", "Step 2" + resModel.getSubmitExamAPIResult().getSuccessFlag());
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (resModel.getSubmitExamAPIResult() != null && resModel.getSubmitExamAPIResult().getSuccessFlag()) {
                    AppLogger.v("SaveQuiz", "Step 002" + "Assignment req Model" + quizResModel.getSubjectPos());

                    AppUtil.dashboardQuizScore = resModel.getSubmitExamAPIResult().getScore();
                    cancelDialogAfterSubmittingTest();
                }

                stopmeplease();
                break;

            case SAVE_QUIZ_DATA_API:
                closeProgress();
                SaveQuestionResModel data = (SaveQuestionResModel) responseApi.data;
                AppLogger.e(TAG, "SAVE_QUIZ_DATA_API 1");
                if (!data.getError()) {
                    AppLogger.e(TAG, "SAVE_QUIZ_DATA_API 2");
                    if (data.getSaveAnswerResModel().getSave()) {
                        AppLogger.e(TAG, "SAVE_QUIZ_DATA_API 3 --" + binding.saveNextBt.getText().toString());
                        if (submitQuizFromExitDialog) {
                            AppLogger.e(TAG, "SAVE_QUIZ_DATA_API 4");
                            submitQuizFromExitDialog = false;
                            callFinishQuizApi();
                        } else {
                            String text=getActivity().getResources().getString(R.string.save_submit);
                            try {
                                LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
                                Details details = model.getDetails();
                                if (model != null) {
                                    text=details.getSaveSubmit() != null ? details.getSaveSubmit() : AuroApp.getAppContext().getResources().getString(R.string.save_submit);
                                }
                            } catch (Exception e) {
                                AppLogger.e(TAG, e.getMessage());
                            }
                            if (buttonText.equalsIgnoreCase(text)) {
                                AppLogger.e(TAG, "SAVE_QUIZ_DATA_API 5");
                                callFinishQuizApi();
                            } else {
                                AppLogger.e(TAG, "SAVE_QUIZ_DATA_API 6");
                                setTextOfButton();
                                closeProgress();
                                optionResModel = null;
                                goToNextQuiz();
                            }
                        }

                    }
                } else {
                    AppLogger.e(TAG, "SAVE_QUIZ_DATA_API 5");
                    setTextOfButton();
                    closeProgress();
                    optionResModel = null;
                }
                break;

            case DASHBOARD_API:
                DashboardResModel dashboardResModel = (DashboardResModel) responseApi.data;
                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                prefModel.setDashboardResModel(dashboardResModel);
                prefModel.setStudentClasses(dashboardResModel.getClasses());
                prefModel.setDashboardaApiNeedToCall(true);
                AuroAppPref.INSTANCE.setPref(prefModel);
                AppUtil.dashboardResModel = dashboardResModel;
                customFinishProgressDialog.dismiss();
                getActivity().getSupportFragmentManager().popBackStack();
                //   getActivity().finish();
                break;
        }
    }


    private void setTextOfButton() {
        if (quizQuestionList != null && countQuiz == quizQuestionList.size() - 1) {
            binding.saveNextBt.setText(getActivity().getResources().getString(R.string.save_submit));
            try {
                LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
                Details details = model.getDetails();
                if (model != null) {
                    binding.saveNextBt.setText(details.getSaveSubmit() != null ? details.getSaveSubmit() : AuroApp.getAppContext().getResources().getString(R.string.save_submit));
                }
            } catch (Exception e) {
                AppLogger.e(TAG, e.getMessage());
            }
        } else {
            binding.saveNextBt.setText(getActivity().getResources().getString(R.string.save_next));
            try {
                LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
                Details details = model.getDetails();
                if (model != null) {
                    binding.saveNextBt.setText(details.getSaveAmpNext() != null ? details.getSaveAmpNext() : AuroApp.getAppContext().getResources().getString(R.string.save_amp_next));
                }
            } catch (Exception e) {
                AppLogger.e(TAG, e.getMessage());
            }
        }
    }

    private void openFinishProgressDialog() {//finish_your_text
        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
        Details details = model.getDetails();
        AppLogger.e("openFinishProgressDialog-", "openFinishProgressDialog");
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(getActivity());
        customDialogModel.setTitle(details.getFinishYourText() != null ? details.getFinishYourText() : getActivity().getResources().getString(R.string.finish_your_text));
        customDialogModel.setContent(getActivity().getResources().getString(R.string.bullted_list));
        customDialogModel.setTwoButtonRequired(false);
        customFinishProgressDialog = new CustomProgressDialog(getActivity(), customDialogModel);
        Objects.requireNonNull(customFinishProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customFinishProgressDialog.setCancelable(false);
        customFinishProgressDialog.show();
        customFinishProgressDialog.setProgressDialogText(details.getFinishYourText() != null ? details.getFinishYourText() : getActivity().getResources().getString(R.string.finish_your_text));
    }


    private void callSendExamImageApi() {

        if (!TextUtil.isEmpty(assignmentResModel.getExamAssignmentID())) {
            if (assignmentResModel != null && !TextUtil.isEmpty(assignmentResModel.getExamAssignmentID())) {
                SaveImageReqModel saveQuestionResModel = new SaveImageReqModel();
                saveQuestionResModel.setImageBytes(assignmentReqModel.getImageBytes());
                saveQuestionResModel.setExamId(assignmentResModel.getExamAssignmentID());
                saveQuestionResModel.setQuizId(assignmentResModel.getQuizId());
                saveQuestionResModel.setImgNormalPath(assignmentResModel.getImgNormalPath());
                saveQuestionResModel.setImgPath(assignmentResModel.getImgPath());
                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                DashboardResModel dashboardResModel = prefModel.getDashboardResModel();
                saveQuestionResModel.setRegistration_id(dashboardResModel.getAuroid());
                saveQuestionResModel.setUserId(prefModel.getStudentData().getUserId());
                AppLogger.v("QuizNew", "Call send exam api STEP 1");
                viewModel.uploadExamFace(saveQuestionResModel);
            }
        }
    }


    /*BroadCast Code Here*/
    private void registerNetworkBroadcastForNougat() {
        mNetworkReceiver = new NetworkChangeReceiver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            getActivity().unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    private void openAlreadyAttemptedQuiz(String msg) {
        closeInstructionDialog();
    /*    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        // Set the alert dialog yes button click listener
        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>Exit</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogQuit, int which) {
                dialogQuit.dismiss();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        // Set the alert dialog no button click listener

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();*/

        if (getActivity() != null) {
            alreadyAttemptedDialog = true;
            attemptedExitDialog = new ExitDialog(getActivity(), msg);
            attemptedExitDialog.setCancelable(false);
            attemptedExitDialog.show();

        }
        // Display the alert dialog on interface
    }

    private void cancelDialogAfterSubmittingTest() {
        if (!viewModel.quizNativeUseCase.checkDemographicStatus(dashboardResModel)) {
            closeAllDialog();
            AppLogger.v("SaveQuiz", "Step 003" + "Assignment req Model" + quizResModel.getSubjectPos());
          //  openDemographicFragment();
            AppLogger.v("SaveQuiz", "Step 004" + "Assignment req Model" + quizResModel.getSubjectPos());
            ((DashBoardMainActivity) getActivity()).setListner(this);
            AppLogger.v("QuizNew", "Dashboard step 1");
            // ((DashBoardMainActivity)getActivity()).callDashboardApi();
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            //open quiz home fragment
            AppLogger.v("SaveQuiz", "Step 004" + "Assignment req Model" + quizResModel.getSubjectPos());
            ((DashBoardMainActivity) getActivity()).setListner(this);
            AppLogger.v("QuizNew", "Dashboard step 1");
            // ((DashBoardMainActivity)getActivity()).callDashboardApi();
            getActivity().getSupportFragmentManager().popBackStack();
        }
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel != null) {
            assignmentReqModel.setSubjectPos(quizResModel.getSubjectPos());
            prefModel.setAssignmentReqModel(assignmentReqModel);
            AuroAppPref.INSTANCE.setPref(prefModel);
        }
    }

    public void openDemographicFragment() {
        Bundle bundle = new Bundle();
        AppLogger.v("SaveQuiz", "Step 4" + "DemoGraphic Fragment");
        DemographicFragment demographicFragment = new DemographicFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        demographicFragment.setArguments(bundle);
        openFragment(demographicFragment);
    }


    private void openFragment(Fragment fragment) {
        ((AppCompatActivity) (this.getContext())).getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.home_container, fragment, MainQuizHomeFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }


    private void setTimerProgress(int progress) {
        binding.timerProgressbar.setProgress(progress);

    }


    String retrieveLinks(String text) {
        ArrayList links = new ArrayList();

        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String urlStr = m.group();
            char[] stringArray1 = urlStr.toCharArray();

            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {

                char[] stringArray = urlStr.toCharArray();

                char[] newArray = new char[stringArray.length - 2];
                System.arraycopy(stringArray, 1, newArray, 0, stringArray.length - 2);
                urlStr = new String(newArray);
                System.out.println("Finally Url =" + newArray.toString());

            }
            System.out.println("...Url..." + urlStr);
            AppLogger.e("chhonker- extract link--", urlStr);
            links.add(urlStr);
        }
        return (String) links.get(0);
    }

    private boolean containsURL(String content) {
        if (content.toLowerCase().contains("http://") || content.toLowerCase().contains("https://") || content.toLowerCase().contains("www.")) {
            return true;
        }
        return false;
    }

    void callDashboardPartnerApi() {
        if (customFinishProgressDialog == null) {
            openFinishProgressDialog();
        }
        if (customFinishProgressDialog != null && !customFinishProgressDialog.isShowing()) {
            openFinishProgressDialog();
        }
        AppLogger.v("QuizNew", "Dashboard step 1");
        viewModel.getDashBoardData(AuroApp.getAuroScholarModel());
    }

    public void openDialogForQuit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(prefModel.getLanguageMasterDynamic().getDetails().getPlease_confirm_if_you_want() != null ?prefModel.getLanguageMasterDynamic().getDetails().getPlease_confirm_if_you_want()  : AuroApp.getAppContext().getResources().getString(R.string. want_to_quit_quiz));
        // Set the alert dialog yes button click listener
        String yes = getActivity().getResources().getString(R.string.yes);
        String no = getActivity().getResources().getString(R.string.no);

        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + yes + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogQuit, int which) {
                finishDialogClick = true;
                submitStatus = AppConstant.QuizFinishStatus.EXIT_BY_STUDENT;
                submitQuizFromExitDialog = true;
                callSaveQuestionApi();
            }
        });
        // Set the alert dialog no button click listener
        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>" + no + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogQuit, int which) {

                dialogQuit.dismiss();

            }
        });

        dialogQuit = builder.create();
        dialogQuit.show();
        // Display the alert dialog on interface
    }

    void startCountDownTimer() {
        int time = ConversionUtil.INSTANCE.convertStringToInteger(fetchQuizResModel.getMinutesLeft());
        time = time * 1000;
        countDownTimerofme = new CountDownTimer(time, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                int numberOfSeconds = START_TIME_IN_MILLIS / COUNTDOWN_INTERVAL;
                int secondsRemaining = (int) (millisUntilFinished / COUNTDOWN_INTERVAL);
                int factor = 100 / numberOfSeconds;

                NumberFormat dateformat = new DecimalFormat("00");
                long min = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                binding.timerText.setText(dateformat.format(min) + ":" + dateformat.format(seconds));
                //  binding.timerText.setText("" + String.format("%d : %d ", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                //  binding.timerText.setText("" + String.format("%d : %d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                int progressPercentage = (int) (numberOfSeconds - secondsRemaining) * factor;
            }

            @Override
            public void onFinish() {
                binding.timerText.setText("00:00");
                callSaveQuestionMethod();
            }
        };
        countDownTimerofme.start();
    }
}
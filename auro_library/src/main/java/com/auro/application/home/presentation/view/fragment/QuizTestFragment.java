package com.auro.application.home.presentation.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Size;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.RealTimeFaceDetection.MLKitFacesAnalyzer;
import com.auro.application.core.application.AuroApp;
import com.auro.application.home.data.base_component.BaseFragment;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.network.URLConstant;
import com.auro.application.databinding.QuizTestLayoutBinding;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.AssignmentResModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.QuizResModel;
import com.auro.application.home.data.model.SaveImageReqModel;

import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.viewmodel.QuizTestViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.alert_dialog.CustomProgressDialog;
import com.auro.application.util.alert_dialog.InstructionDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import static com.auro.application.RealTimeFaceDetection.CameraxActivity.REQUEST_CODE_PERMISSION;
import static com.auro.application.RealTimeFaceDetection.CameraxActivity.REQUIRED_PERMISSIONS;
import static com.auro.application.RealTimeFaceDetection.CameraxActivity.lens;
import static com.auro.application.core.common.Status.ASSIGNMENT_STUDENT_DATA_API;

/**
 * Created by varun
 */
@SuppressLint("SetJavaScriptEnabled")
public class QuizTestFragment extends BaseFragment implements View.OnClickListener, CommonCallBackListner {
    public static final String TAG = "QuizTestFragment";

    @Inject
    @Named("QuizTestFragment")
    ViewModelFactory viewModelFactory;

    private TextureView tv;
    private ImageView iv;

    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private WebSettings webSettings;
    private ValueCallback<Uri[]> mUploadMessage;
    private String mCameraPhotoPath = null;
    private final long size = 0;
    QuizTestLayoutBinding binding;
    private WebView webView;
    DashboardResModel dashboardResModel;
    QuizTestViewModel quizTestViewModel;
    QuizResModel quizResModel;
    AssignmentResModel assignmentResModel;
    InstructionDialog customDialog;
    Dialog customProgressDialog;

    AssignmentReqModel assignmentReqModel;

    boolean submittingTest = false;
    PrefModel prefModel;
    Details details;

    Bitmap bitMapNew;

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    /*Camera x code */
    Handler handler = new Handler();
    Runnable runnable;
    /*End of cmera x code*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
            //((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
            DaggerWrapper.getComponent(getActivity()).doInjection(this);
            quizTestViewModel = ViewModelProviders.of(this, viewModelFactory).get(QuizTestViewModel.class);
            binding.setLifecycleOwner(this);
            setHasOptionsMenu(true);

        }
        // Start quiz
        tv = binding.faceTextureView;
        iv = binding.faceImageView;

        //camera Activity
        try {
            if (allPermissionsGranted()) {
                tv.post(this::startCamera);
            } else {
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION);
            }
            tv.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> updateTransform());

        } catch (IllegalStateException e) {
            AppLogger.e("CamerXPradeep", "Pradeep Kumar Baral");
        }


        ViewUtil.setLanguageonUi(getActivity());
        setRetainInstance(true);
        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);

    }

    @Override
    public void onDestroy() {
        ((DashBoardMainActivity) getActivity()).showBottomNavigationView();
        ((DashBoardMainActivity) getActivity()).visibilityOfNavigation(1);

        if (webView != null) {
            webView.destroy();
        }
        if (customDialog != null) {
            customDialog.cancel();
        }
        super.onDestroy();

    }


    @Override
    protected void init() {
        //setKeyListner();
        setListener();
        loadTest();
        if (dashboardResModel != null && quizResModel != null) {
            assignmentReqModel = quizTestViewModel.homeUseCase.getAssignmentRequestModel(dashboardResModel, quizResModel);
        //    quizTestViewModel.getAssignExamData(assignmentReqModel);
        }
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        details = prefModel.getLanguageMasterDynamic().getDetails();
    }


    private void observeServiceResponse() {

        quizTestViewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    if (isVisible()) {
                        handleProgress(0, "");
                    }
                    break;
                case SUCCESS:
                    if (responseApi.apiTypeStatus == ASSIGNMENT_STUDENT_DATA_API) {
                        if (isVisible()) {
                            assignmentResModel = (AssignmentResModel) responseApi.data;
                            if (!assignmentResModel.isError()) {
                                loadTest();
                            } else {
                                handleProgress(2, assignmentResModel.getMessage());
                            }
                        }
                    }
                    break;
                case NO_INTERNET:
                    if (isVisible()) {
                        handleProgress(2,details.getInternetCheck()!= null ? details.getInternetCheck(): getActivity().getResources().getString(R.string.internet_check));
                    }
                    break;
                case AUTH_FAIL:
                case FAIL_400:
                default:
                    if (isVisible()) {
                        handleProgress(2, details.getDefaultError() != null ? details.getDefaultError() : getActivity().getString(R.string.default_error));
                    }
                    break;
            }
        });
    }

    void loadTest() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        assignmentResModel = prefModel.getAssignmentResModel();
        String webUrl = URLConstant.TEST_URL + "StudentID=" + assignmentResModel.getStudentID() + "&ExamAssignmentID=" + assignmentResModel.getExamAssignmentID();
        openDialog();
        loadWeb(webUrl);
        checkNativeCameraEnableOrNot();

        ((DashBoardMainActivity) getActivity()).setProgressVal();
    }

    @Override
    protected void setToolbar() {
        /*Do cod ehere*/
    }

    @Override
    protected void setListener() {
        setKeyListner();
        if (quizTestViewModel != null && quizTestViewModel.serviceLiveData().hasObservers()) {
            quizTestViewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.quiz_test_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) throws RuntimeException {
        super.onViewCreated(view, savedInstanceState);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        dashboardResModel = prefModel.getDashboardResModel();
        quizResModel = prefModel.getQuizResModel();

       /* if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
            quizResModel = getArguments().getParcelable(AppConstant.QUIZ_RES_MODEL);
        }*/
        setRetainInstance(true);

        init();
    }

    public void openQuizHomeFragment() throws RuntimeException {
        getActivity().getSupportFragmentManager().popBackStack();//old code//refrence https://stackoverflow.com/questions/53566847/popbackstack-causing-java-lang-illegalstateexception-can-not-perform-this-actio
        // getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void openDemographicFragment() {
        Bundle bundle = new Bundle();
        DemographicFragment demographicFragment = new DemographicFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        demographicFragment.setArguments(bundle);
        openFragment(demographicFragment);
    }

    private void openFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(AuroApp.getFragmentContainerUiId(), fragment, KYCViewFragment.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();

    }

    @Override
    public void onResume() {
        super.onResume();
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.QUIZ_TEST_FRAGMENT);

        //screen short
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setListener();
        ((DashBoardMainActivity) getActivity()).hideBottomNavigationView();
        ((DashBoardMainActivity) getActivity()).visibilityOfNavigation(0);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @SuppressLint("JavascriptInterface")
    private void loadWeb(String webUrl) {
        webView = binding.webView;
        webSettings = webView.getSettings();
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings = binding.webView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36");
        webView.setWebViewClient(new PQClient());
        webView.setWebChromeClient(new PQChromeClient());
        //if SDK version is greater of 19 then activate hardware acceleration otherwise activate software acceleration
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


        webView.addJavascriptInterface(new MyJavaScriptInterface(getActivity()), "ButtonRecognizer");

        webView.loadUrl(webUrl);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_arrow) {
            getActivity().onBackPressed();
        }
    }

    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }


    class MyJavaScriptInterface {

        private final Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void boundMethod(String html) {
            AppLogger.e("checkNativeCameraEnableOrNot--", "previewView  boundMethod INVISIBLE");
            binding.previewView.setVisibility(View.INVISIBLE);
            openProgressDialog();
            AppLogger.e("chhonker bound method", html);
          /*  new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    cancelDialogAfterSubmittingTest();

                }
            }, 8000);
*/
        }
    }

    private void cancelDialogAfterSubmittingTest() {
        if (!submittingTest) {
            submittingTest = true;
            if (customProgressDialog != null) {
                customProgressDialog.cancel();
            }
            if (!quizTestViewModel.homeUseCase.checkDemographicStatus(dashboardResModel)) {
              //  openDemographicFragment();
                openQuizHomeFragment();
            } else {
                openQuizHomeFragment();
            }

        }
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel != null) {
            assignmentReqModel.setSubjectPos(quizResModel.getSubjectPos());
            prefModel.setAssignmentReqModel(assignmentReqModel);
            AuroAppPref.INSTANCE.setPref(prefModel);
        }
    }

    public class PQClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // If url contains mailto link then open Mail Intent
            AppLogger.e("chhonker shouldOverrideUrlLoading", url);


            if (url.contains("mailto:")) {
                // Could be cleverer and use a regex
                //Open links in new browser
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            } else {
                //Stay within this webview and load url
                view.loadUrl(url);
                return true;
            }
        }


        //Show loader on url load
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            AppLogger.e("chhonker onPageStarted", url);
            if (!TextUtil.isEmpty(url)) {
                if (url.equalsIgnoreCase("https://auroscholar.com/index.php") ||
                        url.contains("demographics")
                        || url.contains("dashboard")) {
                    cancelDialogAfterSubmittingTest();
                }
            }
        }

        // Called when all page resources loaded
        public void onPageFinished(WebView view, String url) {
            //webView.loadUrl("<button type=\"button\" value=\"Continue\" onclick=\"Continue.performClick(this.value);\">Continue</button>\n");
            AppLogger.e("chhonker Finished", url);
            loadEvent(clickListener());
            if (!TextUtil.isEmpty(url) && url.equalsIgnoreCase("https://assessment.eklavvya.com/Exam/CandidateExam")) {
                AppLogger.e("chhonker Finished exam", url);
                closeDialog();
            }
            handleProgress(1, "");
        }

        private void closeDialog() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (customDialog != null) {
                        customDialog.cancel();
                    }
                }
            }, 2000);
        }

        private void loadEvent(String javascript) {
            webView.loadUrl("javascript:" + javascript);
        }

        private String clickListener() {
            return getButtons() + "for(var i = 0; i < buttons.length; i++){\n" +
                    "\tbuttons[i].onclick = function(){ console.log('click worked.'); ButtonRecognizer.boundMethod('button clicked'); };\n" +
                    "}";
        }

        private String getButtons() {
            //  return "var buttons = document.getElementsByClassName('col-sm-12'); console.log(buttons.length + ' buttons');\n";
            return "var buttons = document.getElementsByClassName('btn-primary btn btn-style'); console.log(buttons.length + ' buttons');\n";

        }
    }

    public class PQChromeClient extends WebChromeClient {
        @Override
        public void onPermissionRequest(final PermissionRequest request) {
            String[] requestedResources = request.getResources();
            ArrayList<String> permissions = new ArrayList<>();
            ArrayList<String> grantedPermissions = new ArrayList<String>();
            for (int i = 0; i < requestedResources.length; i++) {
                if (requestedResources[i].equals(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                    permissions.add(Manifest.permission.RECORD_AUDIO);
                } else if (requestedResources[i].equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                    permissions.add(Manifest.permission.CAMERA);
                }
            }
            for (int i = 0; i < permissions.size(); i++) {
                if (ContextCompat.checkSelfPermission(getActivity(), permissions.get(i)) != PackageManager.PERMISSION_GRANTED) {
                    continue;
                }
                if (permissions.get(i).equals(Manifest.permission.RECORD_AUDIO)) {
                    grantedPermissions.add(PermissionRequest.RESOURCE_AUDIO_CAPTURE);
                } else if (permissions.get(i).equals(Manifest.permission.CAMERA)) {
                    grantedPermissions.add(PermissionRequest.RESOURCE_VIDEO_CAPTURE);
                }
            }

            if (grantedPermissions.isEmpty()) {
                request.deny();
            } else {
                String[] grantedPermissionsArray = new String[grantedPermissions.size()];
                grantedPermissionsArray = grantedPermissions.toArray(grantedPermissionsArray);
                request.grant(grantedPermissionsArray);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            // pbPageLoading.setProgress(newProgress);
        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            mUploadMessage = filePath;

            int writePermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int cameraPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            if (!(writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED)) {
                try {
                    Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    galleryintent.setType("image/*");

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        //   Log.e("error", "Unable to create Image File", ex);
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        cameraIntent.putExtra(
                                MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile)
                        );
                    }
                    Intent chooser = new Intent(Intent.ACTION_CHOOSER);
                    chooser.putExtra(Intent.EXTRA_INTENT, galleryintent);
                    chooser.putExtra(Intent.EXTRA_TITLE, "Select from:");

                    Intent[] intentArray = {cameraIntent};
                    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                    // startActivityForResult(chooser, REQUEST_PIC);
                    startActivityForResult(chooser, INPUT_FILE_REQUEST_CODE);
                } catch (Exception e) {
                    // TODO: when open file chooser failed
                }
            }
            return true;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    private void handleProgress(int status, String msg) {
        if (status == 0) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.webView.setVisibility(View.GONE);
            binding.errorConstraint.setVisibility(View.GONE);
        } else if (status == 1) {
            binding.progressBar.setVisibility(View.GONE);
            binding.webView.setVisibility(View.VISIBLE);
            binding.errorConstraint.setVisibility(View.GONE);
        } else if (status == 2) {
            binding.progressBar.setVisibility(View.GONE);
            binding.webView.setVisibility(View.GONE);
            binding.errorConstraint.setVisibility(View.VISIBLE);
            binding.errorLayout.textError.setText(msg);
            binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dashboardResModel != null && quizResModel != null) {
                        quizTestViewModel.getAssignExamData(quizTestViewModel.homeUseCase.getAssignmentRequestModel(dashboardResModel, quizResModel));
                    }
                }
            });
        }
    }

    private void openDialog() {

        if (getContext() != null) {
            customDialog = new InstructionDialog(getContext(), null, null);
            // Window window = customDialog.getWindow();
            // window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(customDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            customDialog.getWindow().setAttributes(lp);
            Objects.requireNonNull(customDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customDialog.setCancelable(false);
            customDialog.show();
        }

    }

    private void openProgressDialog() {
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(getActivity());
        customDialogModel.setTitle("Calculating Your Score");
        customDialogModel.setContent(getActivity().getResources().getString(R.string.bullted_list));
        customDialogModel.setTwoButtonRequired(false);
        try {
            customProgressDialog = new CustomProgressDialog(customDialogModel);
            Objects.requireNonNull(customProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customProgressDialog.setCancelable(false);
            customProgressDialog.show();
        } catch (Exception e) {

        }
    }

    private void setKeyListner() {
        this.getView().setFocusableInTouchMode(true);
        binding.toolbarLayout.backArrow.setOnClickListener(this);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    alertDialogForQuitQuiz();
                    return true;
                }
                return false;
            }
        });
    }

    public void alertDialogForQuitQuiz() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.quiz_exit_txt);

        // Set the alert dialog yes button click listener
        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + getActivity().getResources().getString(R.string.yes) + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when user clicked the Yes button
                // Set the TextView visibility GONE
                // tv.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().popBackStack();
                dialog.dismiss();
            }
        });
        // Set the alert dialog no button click listener
        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>" + getActivity().getResources().getString(R.string.no) + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when No button clicked
                dialog.dismiss();
                     /*   Toast.makeText(getApplicationContext(),
                                "No Button Clicked",Toast.LENGTH_SHORT).show();*/
            }
        });
        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }



/*
    private void startCamera() {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(getActivity()));
    }
*/

    @SuppressLint("RestrictedApi")
    private void startCamera() {
        try {
            initCamera();
        } catch (Exception e) {
            //AppLogger.e("CameraX expection",e.getMessage());
        }


    }
  /*  void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        // Query if extension is available (optional).
        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        final ImageCapture imageCapture = builder
                .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .build();

        preview.setSurfaceProvider(binding.previewView.createSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis, imageCapture);
    }*/


    void captureImage() {

        runnable = new Runnable() {
            public void run() {
                Bitmap bitmap = bitMapNew;
                if (bitmap != null) {
                    processImage(bitmap);
                }
                captureImage();

            }
        };
        handler.postDelayed(runnable, 10000);


      /*  handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = bitMapNew;
                if (bitmap != null) {
                    processImage(bitmap);
                }

                captureImage();
            }
        }, 10000);*/
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
        callSendExamImageApi();
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
                saveQuestionResModel.setExamId(assignmentResModel.getExamId());
                saveQuestionResModel.setUserId(prefModel.getUserId());

                quizTestViewModel.uploadExamFace(saveQuestionResModel);
            }
        }
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

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (allPermissionsGranted()) {
                tv.post(this::startCamera);
            } else {
                Toast.makeText(getActivity(),
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show();
                //   finish();
            }
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

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case CAMERA_BITMAP_CALLBACK:
                bitMapNew = (Bitmap) commonDataModel.getObject();
                // captureImage();
                break;
        }
    }


}

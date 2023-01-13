package com.auro.application.home.presentation.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.NetworkUtil;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.QuizTestLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.PartnersLoginReqModel;
import com.auro.application.home.data.model.partnersmodel.PartnerLoginResModel;

import com.auro.application.home.presentation.viewmodel.QuizViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.firebaseAnalytics.AnalyticsRegistry;
//import com.wuadam.awesomewebview.AwesomeWebView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.disposables.Disposable;

import static com.auro.application.core.common.Status.PARTNERS_LOGIN_API;

/**
 * Created by varun
 */

public class PartnersWebviewFragment extends BaseFragment {

    @Inject
    @Named("PartnersWebviewFragment")
    ViewModelFactory viewModelFactory;

    public static final String TAG = "PartnersWebviewFragment";

    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private WebSettings webSettings;
    private ValueCallback<Uri[]> mUploadMessage;
    private String mCameraPhotoPath = null;
    private long size = 0;
    QuizTestLayoutBinding binding;
    private WebView webView;
    QuizViewModel quizViewModel;
    PartnerLoginResModel partnerLoginResModel;
    PartnersLoginReqModel partnersLoginReqModel;

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    int value = 0;
    PrefModel prefModel;
    Details details;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        quizViewModel = ViewModelProviders.of(this, viewModelFactory).get(QuizViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        setRetainInstance(true);
        funnelPartnerScreen();
        AppLogger.v("Fragment","PartnerWebViewFragment ---onCreateView");
        ViewUtil.setLanguageonUi(getActivity());
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {

        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();

    }

    @Override
    protected void init() {
        if (getArguments() != null) {
            partnersLoginReqModel = getArguments().getParcelable(AppConstant.PARTNERS_RES_MODEL);
        }
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        details = prefModel.getLanguageMasterDynamic().getDetails();
        binding.toolbarLayout.backParentLayout.setVisibility(View.GONE);

        setListener();
        callPartnersLoginWebUrl();
    }

    @Override
    protected void setToolbar() {
        /*Do cod ehere*/
    }

    @Override
    protected void setListener() {
       // DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.PARTNERS_FRAGMENT_FRAGMENT);
        binding.toolbarLayout.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    getActivity().onBackPressed();
                AppLogger.v("Fragment","PartnersWebviewFragment ---setListener");
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if (quizViewModel != null && quizViewModel.serviceLiveData().hasObservers()) {
            quizViewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    private void observeServiceResponse() {
        quizViewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    if (responseApi.apiTypeStatus == PARTNERS_LOGIN_API) {
                        partnerLoginResModel = (PartnerLoginResModel) responseApi.data;
                        if (partnerLoginResModel != null && partnerLoginResModel.getError()) {
                            if (!TextUtil.isEmpty(partnerLoginResModel.getPartnerResponseMessage())) {
                                handleProgress(2, partnerLoginResModel.getPartnerResponseMessage());
                            } else {
                                handleProgress(2, "");

                               // openMinderWebview(partnerLoginResModel.getPartnerUrl());
                            }
                        } else {
                            if(!TextUtil.isEmpty(partnerLoginResModel.getPartnerUrl()) && partnerLoginResModel.getPartnerUrl() != null){

                                checkInternet();
                            }else{
                                handleProgress(2,details.getDefaultError() != null ? details.getDefaultError() : getActivity().getString(R.string.default_error) );
                            }

                        }
                    }
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                    handleProgress(2, (String) responseApi.data);
                    break;
                default:
                    handleProgress(2, (String) responseApi.data);
                    break;
            }

        });
    }

    @Override
    protected int getLayout() {
        return R.layout.quiz_test_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public void checkInternet() {
        handleProgress(0, "");
        Disposable disposable = NetworkUtil.hasInternetConnection().subscribe(hasInternet -> {
            if (hasInternet) {
                handleProgress(0, "");
                getActivity().getSupportFragmentManager().popBackStack();
                AppLogger.v("Pradeep","Calling mindler api");
                funnelMindlerScreen();
                openMinderWebview(partnerLoginResModel.getPartnerUrl());
               //loadWeb(partnerLoginResModel.getPartnerUrl());
            } else {
                handleProgress(2, getActivity().getResources().getString(R.string.internet_check));
            }

        });
    }


    @Override
    public void onResume() {
        super.onResume();
        AppLogger.v("Fragment","PartnerWebViewFragment ---OnResume");
        if(value == 1) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        value = 1;
       // DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.PRIVACY_POLICY_FRAGMENT);
    }

    private void loadWeb(String webUrl) {
        webView = binding.webView;
       /* webSettings = webView.getSettings();
        webView.getSettings().getLoadsImagesAutomatically();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setScrollBarStyle(0);

        webView.setWebViewClient(new MyBrowser());

        //if SDK version is greater of 19 then activate hardware acceleration otherwise activate software acceleration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.loadUrl(webUrl);*/


    }

    public class MyBrowser extends WebViewClient {
        public MyBrowser() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            handleProgress(1, "");

        }

    }

    public class PQClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // If url contains mailto link then open Mail Intent
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
            AppLogger.e(TAG, url);
            if (view.getUrl().equalsIgnoreCase("http://auroscholar.com/index.php")
                    || view.getUrl().equalsIgnoreCase("http://auroscholar.com/dashboard.php")) {
                getActivity().getSupportFragmentManager().popBackStack();
            }


        }

        // Called when all page resources loaded
        public void onPageFinished(WebView view, String url) {
            handleProgress(1, "");
            webView.loadUrl("javascript:(function(){ " +
                    "document.getElementById('android-app').style.display='none';})()");
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
                        Log.e("error", "Unable to create Image File", ex);
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
                    callPartnersLoginWebUrl();
                }
            });
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }


    private void callPartnersLoginWebUrl() {

     /*   PartnersLoginReqModel partnersLoginReqModel = new PartnersLoginReqModel();
        partnersLoginReqModel.setPartnerId("2");
        partnersLoginReqModel.setPartnerName("Mindler");
        partnersLoginReqModel.setStudentName("Varun");
        partnersLoginReqModel.setStudentEmail("vksep07@gmail.com");
        partnersLoginReqModel.setStudentPassword("nothing");
        partnersLoginReqModel.setStudentClass("1");
        partnersLoginReqModel.setStudentMobile("0000000007");*/
        partnersLoginReqModel.setUserPreferedLanguageId(Integer.parseInt(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId()));
        handleProgress(0, "");
        quizViewModel.getPartnersLoginData(partnersLoginReqModel);

    }

    private void openMinderWebview(String partnerUrl)
    {
     /*   new AwesomeWebView.Builder(getActivity())
                .webViewGeolocationEnabled(true)
                .webViewCookieEnabled(true)
                .webViewAppJumpEnabled(true)
                .webViewCameraEnabled(true)
                .webViewAudioEnabled(true)
                .showMenuSavePhoto(true)
                .stringResSavePhoto(R.string.save_photo)
                .showToastPhotoSavedOrFailed(true)
                .stringResPhotoSavedTo(R.string.photo_saved_to)
                .stringResPhotoSaveFailed(R.string.photo_save_failed)
                .fileChooserEnabled(true)
                .headersMainPage(false)
//                    .injectJavaScript("javascript: alert(\"This is js inject\")")
                .injectJavaScript("javascript: window.toast.showToast(\"toast by js interface from \" + window.toast.getSimpleName());")
                .injectJavaScriptMainPage(true)
                .webViewUserAgentString("AwesomeWebView")
                .webViewUserAgentAppend(true)
                .statusBarColorRes(R.color.finestWhite)
                .statusBarIconDark(true)
                .show(partnerUrl);*/
        //"https://www.mindler.com/student_signup/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InZpc2hhbDBAbWluZGxlci5jb20iLCJhdXRoX2lkIjoiMjMyNjAzIiwidWlkIjoyNTA1NTd9.IS7iDVc6AvEDYiqjYSgXRmg3hyAQWq5f1htLu8-4K5w

    }

    private void  funnelPartnerScreen(){
        AnalyticsRegistry.INSTANCE.getModelInstance().trackPartnerScreen();
    }

    private void  funnelMindlerScreen(){
        AnalyticsRegistry.INSTANCE.getModelInstance().trackMindlerScreen();
    }
}

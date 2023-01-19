package com.auro.application.home.presentation.view.fragment;

import static com.auro.application.core.common.Status.PARTNERS_API;
import static com.auro.application.core.common.Status.UPDATE_STUDENT;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.DaggerWrapper;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.PartnersLayoutBinding;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.PartnersLoginReqModel;
import com.auro.application.home.data.model.QuizResModel;
import com.auro.application.home.data.model.SelectChapterQuizModel;
import com.auro.application.home.data.model.partnersmodel.PartnerDataModel;
import com.auro.application.home.data.model.partnersmodel.PartnerRequiredParamModel;
import com.auro.application.home.data.model.partnersmodel.PartnerResModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.adapter.ChapterSelectAdapter;
import com.auro.application.home.presentation.view.adapter.PartnersAdapter;
import com.auro.application.home.presentation.view.adapter.SubjectSelectAdapter;
import com.auro.application.home.presentation.viewmodel.QuizViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.AskDetailCustomDialog;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.alert_dialog.PartnerDetailDialog;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringDynamic;
//import com.wuadam.awesomewebview.AwesomeWebView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;


public class PartnersFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {

    @Inject
    @Named("PartnersFragment")
    ViewModelFactory viewModelFactory;

    PartnersLayoutBinding binding;
    QuizViewModel quizViewModel;
    DashboardResModel dashboardResModel;

    String TAG = PartnersFragment.class.getSimpleName();
    QuizResModel quizResModel;
    AssignmentReqModel assignmentReqModel;

    PartnersLoginReqModel partnersLoginReqModel = new PartnersLoginReqModel();
    PartnerDataModel partnerDataModel;
    AskDetailCustomDialog askDetailCustomDialog;
    Details details;
    public PartnersFragment() {
        // Required empty public constructor
    }

    ChapterSelectAdapter adapter;
    SubjectSelectAdapter subjectadapter;
    List<SelectChapterQuizModel> laugList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        //((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        DaggerWrapper.getComponent(getActivity()).doInjection(this);
        quizViewModel = ViewModelProviders.of(this, viewModelFactory).get(QuizViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setQuizViewModel(quizViewModel);
        setRetainInstance(true);
        AppLogger.v("Fragment","PartnersFragment ---onCreateView");
        init();
        setListener();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((DashBoardMainActivity) getActivity()).setListner(this);
    }

    @Override
    protected void init() {
        PrefModel prefModel=AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.e("openPartnerWebViewFragment init-",prefModel.getUserMobile()+"---name"+prefModel.getStudentName());
        callPartnersApi();
        AppUtil.loadAppLogo(binding.auroScholarLogo,getActivity());

        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        AppStringDynamic.setPartnerPageStrings(binding);
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.PARTNERS_FRAGMENT);
        binding.languageLayout.setOnClickListener(this);
        binding.cardView2.setOnClickListener(this);


        if (quizViewModel != null && quizViewModel.serviceLiveData().hasObservers()) {
            quizViewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    public void setSubjectAdapter(PartnerResModel partnerResModel) {
        AppLogger.v("Main", "Call---->" + partnerResModel.getPartner().size());
        if (TextUtil.checkListIsEmpty(partnerResModel.getPartner())) {
            return;
        }
        List<PartnerDataModel> subjectModellist = new ArrayList<>();
        for (PartnerDataModel partnerDataModel : partnerResModel.getPartner()) {
            subjectModellist.add(partnerDataModel);
        }
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.partnerRecyclerview.setLayoutManager(layoutManager);
        binding.partnerRecyclerview.setHasFixedSize(true);
        PartnersAdapter subjectadapter = new PartnersAdapter(subjectModellist, this);
        binding.partnerRecyclerview.setAdapter(subjectadapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.partners_layout;
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        AppLogger.e(TAG, "commonEventListner");
        switch (commonDataModel.getClickType()) {
            case PARTNERS_CLICK:
                partnerDataModel = (PartnerDataModel) commonDataModel.getObject();
                openPartnersDetailDialog();

                break;
            case NAME_DONE_CLICK:
                PartnersLoginReqModel  partnersLoginReqModel=(PartnersLoginReqModel) commonDataModel.getObject();
                sendProfileScreenApi(partnersLoginReqModel.getStudentName(),partnersLoginReqModel.getStudentEmail());
                break;

            case PARTNER_EXPLORE:
                String availablesoon = details.getAvailable_soon() !=null ? details.getAvailable_soon():getString(R.string.it_will_available_soon);
                String message=partnerDataModel.getPartnerName()+" "+availablesoon;
                ViewUtil.showSnackBar(binding.getRoot(),message);
                checkForRequiredParam();
                break;
        }
    }


    private void checkForRequiredParam() {
        PrefModel prefModel=AuroAppPref.INSTANCE.getModelInstance();
        DashboardResModel dashboardResModel = prefModel.getDashboardResModel();
        AppLogger.e("openPartnerWebViewFragment-",prefModel.getUpdateEmailId()+"---name"+prefModel.getStudentName());
        PartnerRequiredParamModel model = partnerDataModel.getPartnerRequiredParamModel();
        partnersLoginReqModel.setStudentName(prefModel.getStudentName());
        partnersLoginReqModel.setStudentEmail(prefModel.getUpdateEmailId());
        if (dashboardResModel != null) {
            AppLogger.e("openPartnerWebViewFragment dashboardResModel -", "mobile number-" + dashboardResModel.getPhonenumber() +
                    "-class-" + dashboardResModel.getStudentclass()  +
                    "-student name-" + dashboardResModel.getStudent_name() + "-student email-" + dashboardResModel.getStudentclass());
            partnersLoginReqModel.setStudentName(dashboardResModel.getStudent_name());
            partnersLoginReqModel.setStudentEmail(dashboardResModel.getEmail_id());
        }
        if (model != null && model.getEmail()) {
            if (dashboardResModel != null && TextUtil.isEmpty(dashboardResModel.getEmail_id())) {
                openAskNameDialog(partnerDataModel);
                return;
            }
        }
        if (model != null && model.getName()) {
            if (dashboardResModel != null && TextUtil.isEmpty(dashboardResModel.getStudent_name())) {
                openAskNameDialog(partnerDataModel);
                return;
            }
        }


        openPartnerWebViewFragment();
    }


    private void openPartnerWebViewFragment() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        partnersLoginReqModel.setPartnerId(partnerDataModel.getPartnerId());
        partnersLoginReqModel.setStudentMobile(prefModel.getStudentData().getUserMobile());
        partnersLoginReqModel.setStudentClass("" + prefModel.getStudentData().getGrade());
        partnersLoginReqModel.setStudentPassword("");
        partnersLoginReqModel.setPartnerName(partnerDataModel.getPartnerName());
        AppLogger.e("openPartnerWebViewFragment-", "--partner id=" + partnersLoginReqModel.getPartnerId() + "-mobile number-" + partnersLoginReqModel.getStudentMobile() +
                "-class-" + partnersLoginReqModel.getStudentClass() + "-partner name-" + partnersLoginReqModel.getPartnerName() +
                "-student name-" + partnersLoginReqModel.getStudentName() + "-student email-" + partnersLoginReqModel.getStudentEmail());
        askPermission();

     //   openMinderWebview();
    }

    private void askPermission() {
        String rationale = "For Upload Profile Picture. Camera and Storage Permission is Must.";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");
        Permissions.check(getActivity(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {

                PartnersWebviewFragment partnersWebviewFragment = new PartnersWebviewFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstant.PARTNERS_RES_MODEL, partnersLoginReqModel);
                partnersWebviewFragment.setArguments(bundle);
                ((DashBoardMainActivity) getActivity()).openLeaderBoardFragment(partnersWebviewFragment);


         /*       ImagePicker.with(getActivity())
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();*/
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
                ViewUtil.showSnackBar(binding.getRoot(), rationale);
            }
        });
    }

    private void openMinderWebview()
    {
    /*    new AwesomeWebView.Builder(getActivity())
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
                .show("https://www.mindler.com/student_signup/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InZpc2hhbDBAbWluZGxlci5jb20iLCJhdXRoX2lkIjoiMjMyNjAzIiwidWlkIjoyNTA1NTd9.IS7iDVc6AvEDYiqjYSgXRmg3hyAQWq5f1htLu8-4K5w");
*/
    }



    @Override
    public void onClick(View v) {
        AppLogger.e(TAG, "On click called");
        if (v.getId() == R.id.sheet_layout_quiz) {
            AppLogger.e(TAG, "On click called sheet_layout 1");
        }
        int id = v.getId();
        if (id == R.id.language_layout) {
            ((DashBoardMainActivity) getActivity()).openChangeLanguageDialog();
        } else if (id == R.id.cardView2) {
            ((DashBoardMainActivity) getActivity()).openProfileFragment();
        }


    }


    private void observeServiceResponse() {

        quizViewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    if (isVisible()) {
                        if (responseApi.apiTypeStatus == PARTNERS_API) {
                            PartnerResModel partnerResModel = (PartnerResModel) responseApi.data;
                            AppLogger.v("Mindler","Step 2 Partner Api -- "+partnerResModel.getPartner());
                            if (!TextUtil.checkListIsEmpty(partnerResModel.getPartner())) {
                                setSubjectAdapter(partnerResModel);
                                handleProgress(1, "");
                                ViewUtil.setProfilePic(binding.imageView6);
                            } else {
                                handleProgress(4, details.getNo_data_found());
                            }
                        } else if (responseApi.apiTypeStatus == UPDATE_STUDENT) {
                            AppLogger.e(TAG, "UPDATE_STUDENT ");

                            hideAskDetailProgress();
                        }
                    }
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                        hideAskDetailProgress();
                    }
                    break;
                default:
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                        hideAskDetailProgress();
                    }
                    break;
            }

        });
    }


    private  void hideAskDetailProgress()
    {
        if (askDetailCustomDialog != null && askDetailCustomDialog.isShowing()) {
            askDetailCustomDialog.hideProgress();
            openPartnerWebViewFragment();
        }
    }

    private void handleProgress(int value, String message) {
        AppLogger.e(TAG, "handleProgress calling - " + value + "-message-" + message);
        if (value == 0) {
            binding.errorConstraint.setVisibility(View.GONE);
            binding.partnerRecyclerview.setVisibility(View.GONE);
            binding.progressBar2.setVisibility(View.VISIBLE);

        } else if (value == 1) {
            binding.errorConstraint.setVisibility(View.GONE);
            binding.progressBar2.setVisibility(View.GONE);
            binding.partnerRecyclerview.setVisibility(View.VISIBLE);

        } else if (value == 2) {
            binding.errorConstraint.setVisibility(View.VISIBLE);
            binding.partnerRecyclerview.setVisibility(View.GONE);
            binding.progressBar2.setVisibility(View.GONE);
            binding.errorLayout.textError.setText(message);
            binding.errorLayout.btRetry.setVisibility(View.VISIBLE);
            binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPartnersApi();
                }
            });
        } else if (value == 4) {
            binding.errorConstraint.setVisibility(View.VISIBLE);
            binding.partnerRecyclerview.setVisibility(View.GONE);
            binding.progressBar2.setVisibility(View.GONE);
            binding.errorLayout.textError.setText(message);
            binding.errorLayout.btRetry.setVisibility(View.GONE);
        }

    }


    private void callPartnersApi() {
        handleProgress(0, "");
        AppLogger.e("partnersApi-", "step 1");
        quizViewModel.getPartnersData();
    }


    private void openAskNameDialog(PartnerDataModel model) {
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(getActivity());
        customDialogModel.setTitle(AuroApp.getAppContext().getResources().getString(R.string.update_auroscholar));
        customDialogModel.setContent(AuroApp.getAppContext().getResources().getString(R.string.updateMessage));
        askDetailCustomDialog = new AskDetailCustomDialog(getActivity(), customDialogModel, this, model, partnersLoginReqModel);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(askDetailCustomDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        askDetailCustomDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(askDetailCustomDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        askDetailCustomDialog.setCancelable(true);
        askDetailCustomDialog.show();
    }

    private void openPartnersDetailDialog() {
        AppLogger.v("Mindler","Step 3 openPartnersDetailDialog");
        PartnerDetailDialog partnerDetailDialog = new PartnerDetailDialog(getActivity(), this, partnerDataModel);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(partnerDetailDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        partnerDetailDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(partnerDetailDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        partnerDetailDialog.setCancelable(true);
        partnerDetailDialog.show();
    }


    public void sendProfileScreenApi(String name, String emailid) {
        if (askDetailCustomDialog != null) {
            DashboardResModel dashboardResModel=AuroAppPref.INSTANCE.getModelInstance().getDashboardResModel();
            askDetailCustomDialog.showPorgress();
            GetStudentUpdateProfile studentProfileModel = new GetStudentUpdateProfile();
            studentProfileModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
            studentProfileModel.setStudentName(name);
            studentProfileModel.setEmailId(emailid);
            studentProfileModel.setPhonenumber(dashboardResModel.getPhonenumber());
            quizViewModel.sendStudentProfileInternet(studentProfileModel);
        }
    }
}
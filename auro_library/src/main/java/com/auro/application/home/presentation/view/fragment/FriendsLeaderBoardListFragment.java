package com.auro.application.home.presentation.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.FriendsLeoboardListLayoutBinding;
import com.auro.application.home.data.model.AssignmentReqModel;
import com.auro.application.home.data.model.ChallengeAccepResModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.FriendListResDataModel;
import com.auro.application.home.data.model.FriendsLeaderBoardModel;
import com.auro.application.home.data.model.GetAllChildModel;
import com.auro.application.home.data.model.GetAllChildReferModel;
import com.auro.application.home.data.model.QuizResModel;
import com.auro.application.home.data.model.SubjectResModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;

import com.auro.application.home.data.model.response.GetAllChildDetailResModel;
import com.auro.application.home.data.model.response.GetAllReferChildDetailResModel;
import com.auro.application.home.presentation.view.activity.CameraActivity;
import com.auro.application.home.presentation.view.activity.ChildAccountsActivity;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.adapter.LeaderBoardAdapter;
import com.auro.application.home.presentation.view.adapter.ReferralListStudentAdapter;
import com.auro.application.home.presentation.view.adapter.ReferredlListStudentAdapter;
import com.auro.application.home.presentation.view.adapter.SelectYourParentChildAdapter;
import com.auro.application.home.presentation.viewmodel.FriendsLeaderShipViewModel;
import com.auro.application.teacher.data.model.request.SendInviteNotificationReqModel;
import com.auro.application.teacher.data.model.response.TeacherResModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.firebaseAnalytics.AnalyticsRegistry;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringDynamic;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import static android.app.Activity.RESULT_OK;
import static com.auro.application.core.common.Status.ACCEPT_INVITE_CLICK;
import static com.auro.application.core.common.Status.AZURE_API;
import static com.auro.application.core.common.Status.DASHBOARD_API;
import static com.auro.application.core.common.Status.DYNAMIC_LINK_API;
import static com.auro.application.core.common.Status.INVITE_FRIENDS_LIST;
import static com.auro.application.core.common.Status.SEND_INVITE_API;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsLeaderBoardListFragment extends BaseFragment implements View.OnClickListener, CommonCallBackListner {

    @Inject
    @Named("FriendsLeaderBoardListFragment")
    ViewModelFactory viewModelFactory;
    List<GetAllReferChildDetailResModel> listchilds = new ArrayList<>();
    List<GetAllReferChildDetailResModel> list = new ArrayList<>();
    List<GetAllReferChildDetailResModel> listchilds1 = new ArrayList<>();
    List<GetAllReferChildDetailResModel> list1= new ArrayList<>();
    RecyclerView referalstudentList;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private static final String TAG = FriendsLeaderBoardListFragment.class.getSimpleName();
    FriendsLeoboardListLayoutBinding binding;
    FriendsLeaderShipViewModel viewModel;
    InviteFriendDialog mInviteBoxDialog;
    FriendListResDataModel resModel;

    LeaderBoardAdapter leaderBoardAdapter;
    boolean isFriendList = true;
    Resources resources;
    boolean isStateRestore;

    FriendsLeaderBoardModel boardModel;
    DashboardResModel dashboardResModel;
    AssignmentReqModel assignmentReqModel;
    QuizResModel quizResModel;
    int itemPos;
    PrefModel prefModel;
    Details details;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendsLeaderShipViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        return binding.getRoot();
    }


    @Override
    protected void init() {
        AppStringDynamic.setFriendsLeaderBoardAddFragment(binding);
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        details = prefModel.getLanguageMasterDynamic().getDetails();
        setListener();
        setDataUi();
        binding.recyclerviewReffered.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerviewReffered.setHasFixedSize(true);
        binding.recyclerviewRefferal.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerviewRefferal.setHasFixedSize(true);
        getAddedChild();
        getRefferedAddedChild();
        viewModel.getDashBoardData(AuroApp.getAuroScholarModel());

    }

    private void setDataUi() {
        if (isFriendList) {
            binding.noFriendLayout.setVisibility(View.GONE);
            binding.boardListLayout.setVisibility(View.VISIBLE);

        } else {

            binding.boardListLayout.setVisibility(View.GONE);
            binding.noFriendLayout.setVisibility(View.VISIBLE);

        }


    }


    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        binding.inviteNow.setOnClickListener(this);
        binding.toolbarLayout.backArrow.setOnClickListener(this);

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);

        } else {
            observeServiceResponse();
        }
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    //For ProgressBar
                    if (responseApi.apiTypeStatus == INVITE_FRIENDS_LIST) {
                        handleProgress(0, "");
                    } else if (responseApi.apiTypeStatus == ACCEPT_INVITE_CLICK) {
                        updateData(true, true);
                    } else if (responseApi.apiTypeStatus == SEND_INVITE_API) {
                        updateData(true, true);
                    }

                    break;

                case SUCCESS:
                    if (responseApi.apiTypeStatus == INVITE_FRIENDS_LIST) {
                        resModel = (FriendListResDataModel) responseApi.data;
                        if (resModel.getError()) {
                            handleProgress(2, resModel.getMessage());
                        } else {
                            handleProgress(1, "");
                            setAdapter();
                        }
                    } else if (responseApi.apiTypeStatus == SEND_INVITE_API) {
                        TeacherResModel resModel = (TeacherResModel) responseApi.data;
                        updateData(false, resModel.getError());
                    } else if (responseApi.apiTypeStatus == ACCEPT_INVITE_CLICK) {
                        ChallengeAccepResModel accepResModel = (ChallengeAccepResModel) responseApi.data;
                        updateData(false, accepResModel.getError());
                        sendToNextQuiz();
                    } else if (responseApi.apiTypeStatus == DASHBOARD_API) {
                        dashboardResModel = (DashboardResModel) responseApi.data;
                    } else if (responseApi.apiTypeStatus == AZURE_API) {

                    } else if (responseApi.apiTypeStatus == DYNAMIC_LINK_API) {
                        binding.inviteProgress.setVisibility(View.GONE);
                        AppLogger.e("DYNAMIC_LINK_API", responseApi.data.toString());
                        DynamiclinkResModel dynamiclinkResModel = (DynamiclinkResModel) responseApi.data;
                        openShareDefaultDialog(dynamiclinkResModel);
                    }
                    break;


                case NO_INTERNET:
                case FAIL_400:
                    binding.inviteProgress.setVisibility(View.GONE);
                    if (responseApi.apiTypeStatus == INVITE_FRIENDS_LIST) {
                        handleProgress(3, (String) responseApi.data);
                    } else if (responseApi.apiTypeStatus == ACCEPT_INVITE_CLICK) {
                        updateData(false, true);
                    } else if (responseApi.apiTypeStatus == SEND_INVITE_API) {
                        updateData(false, true);
                    } else if (responseApi.apiTypeStatus == AZURE_API) {
                        setImageInPref(assignmentReqModel);
                    }

                    showSnackbarError((String) responseApi.data);
                    break;

                default:
                    binding.inviteProgress.setVisibility(View.GONE);
                    if (responseApi.apiTypeStatus == INVITE_FRIENDS_LIST) {
                        handleProgress(3, (String) responseApi.data);
                    } else if (responseApi.apiTypeStatus == ACCEPT_INVITE_CLICK) {
                        updateData(false, true);
                    } else if (responseApi.apiTypeStatus == SEND_INVITE_API) {
                        updateData(false, true);
                    } else if (responseApi.apiTypeStatus == AZURE_API) {
                        setImageInPref(assignmentReqModel);
                    }
                    showSnackbarError((String) responseApi.data);
                    break;
            }

        });
    }

    private void handleProgress(int i, String msg) {
        switch (i) {
            case 0:

                binding.progressBar.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.noFriendLayout.setVisibility(View.GONE);
                binding.boardListLayout.setVisibility(View.GONE);
                break;

            case 1:
                binding.progressBar.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.noFriendLayout.setVisibility(View.GONE);
                binding.boardListLayout.setVisibility(View.VISIBLE);
                break;

            case 2:
                binding.progressBar.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.errorLayout.errorIcon.setVisibility(View.GONE);
                binding.errorLayout.btRetry.setVisibility(View.GONE);
                binding.errorLayout.textError.setText(msg);
                binding.noFriendLayout.setVisibility(View.VISIBLE);
                binding.boardListLayout.setVisibility(View.GONE);
                break;

            case 3:
                binding.progressBar.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.errorLayout.errorIcon.setVisibility(View.VISIBLE);
                binding.errorLayout.textError.setText(msg);
                binding.noFriendLayout.setVisibility(View.GONE);
                binding.boardListLayout.setVisibility(View.GONE);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.getFriendsListData();
                    }
                });
                break;

        }

    }

    private void showSnackbarError(String message) {
        binding.inviteProgress.setVisibility(View.GONE);
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }


    @Override
    protected int getLayout() {
        return R.layout.friends_leoboard_list_layout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            // dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);

        }
        init();
        setToolbar();
        setListener();
    }

    private void setAdapter() {
        if (!TextUtil.checkListIsEmpty(resModel.getFriends())) {
            for (FriendsLeaderBoardModel model : resModel.getFriends()) {
                model.setViewType(AppConstant.FriendsLeaderBoard.LEADERBOARD_TYPE);
            }
            binding.friendsList.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.friendsList.setHasFixedSize(true);
            leaderBoardAdapter = new LeaderBoardAdapter(resModel.getFriends(), this);
            binding.friendsList.setAdapter(leaderBoardAdapter);
        } else {
            handleProgress(2, resModel.getMessage());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.LEADERBOARD_FRAGMENT);
        setKeyListner();
        viewModel.getFriendsListData();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_arrow) {
            getActivity().onBackPressed();
            //openFragment(new QuizHomeFragment());
        } else if (v.getId() == R.id.invite_button) {
         /*   ((DashBoardMainActivity) getActivity()).setListner(this);
            ((DashBoardMainActivity) getActivity()).callRefferApi();
            handleRefferProgress(0);*/
            callLinkGenerateApi();
        } else if (v.getId() == R.id.invite_now) {
           /* ((DashBoardMainActivity) getActivity()).setListner(this);
            ((DashBoardMainActivity) getActivity()).callRefferApi();
            handleRefferProgress(0);*/
            callLinkGenerateApi();
        }
    }

    private void handleRefferProgress(int val) {
        switch (val) {
            case 0:
                binding.progressbar.pgbar.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.progressbar.pgbar.setVisibility(View.GONE);
                break;

        }

    }

    private void checkLinkRef() {
        funnelInviteFriend();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel != null && !TextUtil.isEmpty(prefModel.getRefferalLink())) {
            //openShareDefaultDialog();
        } else {
            callLinkGenerateApi();
        }
    }

    private void openFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(AuroApp.getFragmentContainerUiId(), fragment, InviteFriendDialog.class.getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();

    }

    private void funnelInviteFriend() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentInviteFriend();
    }

    private void openFragmentDialog(Fragment fragment) {
        /* getActivity().getSupportFragmentManager().popBackStack();*/
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(AuroApp.getFragmentContainerUiId(), fragment, InviteFriendDialog.class.getSimpleName())
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


    private void openShareDefaultDialog(DynamiclinkResModel dynamiclinkResModel) {
        String completeLink = getActivity().getResources().getString(R.string.invite_friend_refrral);
        completeLink = completeLink + dynamiclinkResModel.getLink();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, completeLink);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Invite to AuroScholar Scholarship");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        getActivity().startActivity(shareIntent);
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        itemPos = commonDataModel.getSource();
        boardModel = (FriendsLeaderBoardModel) commonDataModel.getObject();
        switch (commonDataModel.getClickType()) {
            case SEND_INVITE_CLICK:
                callSendInviteApi(boardModel);
                break;

            case ACCEPT_INVITE_CLICK:
                acceptChallengeApi();
                break;

            case REFFER_API_SUCCESS:
                if (isVisible()) {
                    AppLogger.e("performClick-", "REFFER_API_SUCCESS");
                    ((HomeActivity) getActivity()).setCommonCallBackListner(null);
                    handleRefferProgress(1);
                    openShareDefaultDialog((DynamiclinkResModel) commonDataModel.getObject());
                }
                break;

            case REFFER_API_ERROR:
                AppLogger.e("performClick-", "REFFER_API_ERROR");
                ((HomeActivity) getActivity()).setCommonCallBackListner(null);
                handleRefferProgress(1);
                ViewUtil.showSnackBar(binding.getRoot(),details.getDefaultError() != null ? details.getDefaultError() : getActivity().getString(R.string.default_error));

                break;
        }
    }

    private void callSendInviteApi(FriendsLeaderBoardModel model) {
        binding.inviteProgress.setVisibility(View.VISIBLE);
        if (!TextUtil.isEmpty(model.getMobileNo())) {
            SendInviteNotificationReqModel reqModel = new SendInviteNotificationReqModel();
            reqModel.setReceiver_mobile_no(model.getMobileNo());
            reqModel.setSender_mobile_no(AuroApp.getAuroScholarModel().getMobileNumber());
            reqModel.setNotification_title("Challenged You");
            String msg = getString(R.string.challenge_msg);
            if (!TextUtil.isEmpty(model.getStudentName())) {
                msg = model.getStudentName() + " " + msg;
            }
            reqModel.setNotification_message(msg);
            viewModel.sendInviteNotificationApi(reqModel);
        }
    }

    private void acceptChallengeApi() {
        if (!TextUtil.isEmpty(boardModel.getMobileNo())) {
            SendInviteNotificationReqModel reqModel = new SendInviteNotificationReqModel();
            reqModel.setReceiver_mobile_no(AuroApp.getAuroScholarModel().getMobileNumber());
            reqModel.setSender_mobile_no(boardModel.getMobileNo());
            viewModel.acceptChalange(reqModel);
        }
    }

    private void updateData(boolean status, boolean sent) {
        if (resModel != null && !TextUtil.checkListIsEmpty(resModel.getFriends())) {
            resModel.getFriends().get(itemPos).setProgress(status);
            if (!sent) {
                if (boardModel.isChallengedYou()) {
                    resModel.getFriends().get(itemPos).setSentText(getActivity().getString(R.string.accept));
                } else {
                    resModel.getFriends().get(itemPos).setSentText(getActivity().getString(R.string.challenge));
                }
                resModel.getFriends().get(itemPos).setSent(true);
            }
            leaderBoardAdapter.setDataList(resModel.getFriends());
        }
    }


    private void setKeyListner() {
        binding.inviteButton.setOnClickListener(this);
        binding.inviteNow.setOnClickListener(this);
    }


    private void sendToNextQuiz() {
        if (dashboardResModel == null) {
            return;
        }
        if (!viewModel.homeUseCase.checkAllQuizAreFinishedOrNot(dashboardResModel)) {
            for (int i = 0; i < dashboardResModel.getSubjectResModelList().size(); i++) {
                SubjectResModel subjectResModel = dashboardResModel.getSubjectResModelList().get(i);
                for (QuizResModel quizResModel : subjectResModel.getChapter()) {
                    if (quizResModel.getAttempt() < 3) {
                        quizResModel.setSubjectPos(i);
                        this.quizResModel = quizResModel;
                    }
                    break;
                }
            }
        } else {
            openAlerDialog();
        }
        if (quizResModel != null) {
            askPermission();
        }
    }


    public void setImageInPref(AssignmentReqModel assignmentReqModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel != null && prefModel.getListAzureImageList() != null) {
            prefModel.getListAzureImageList().add(assignmentReqModel);
            AuroAppPref.INSTANCE.setPref(prefModel);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    String path = data.getStringExtra(AppConstant.PROFILE_IMAGE_PATH);
                    azureImage(path);
                    openQuizTestFragment(dashboardResModel);

                } catch (Exception e) {

                }

            } else {

            }
        }
    }

    private void azureImage(String path) {
        try {
            AppLogger.d(TAG, "Image Path" + path);
            assignmentReqModel = viewModel.homeUseCase.getAssignmentRequestModel(dashboardResModel, quizResModel);
            assignmentReqModel.setEklavvya_exam_id("");
            assignmentReqModel.setSubject(quizResModel.getSubjectName());
            Bitmap picBitmap = BitmapFactory.decodeFile(path);
            byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
            long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
            if (mb > 1.5) {
                assignmentReqModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
            } else {
                assignmentReqModel.setImageBytes(bytes);
            }

            viewModel.getAzureRequestData(assignmentReqModel);
        } catch (Exception e) {
            /*Do code here when error occur*/
        }
    }

    public void openQuizTestFragment(DashboardResModel dashboardResModel) {
        Bundle bundle = new Bundle();
        QuizTestFragment quizTestFragment = new QuizTestFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        bundle.putParcelable(AppConstant.QUIZ_RES_MODEL, quizResModel);
        quizTestFragment.setArguments(bundle);
        openFragment(quizTestFragment);
    }


    private void askPermission() {
//        String rationale = getString(R.string.permission_error_msg);
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(getActivity(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {

                //   openQuizTestFragment(dashboardResModel);
                openCameraPhotoFragment();

//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//                ViewUtil.showSnackBar(binding.getRoot(), rationale);
//            }
//        });
    }

    public void openCameraPhotoFragment() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, AppConstant.CAMERA_REQUEST_CODE);
    }

    public void openAlerDialog() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                getActivity());

// Setting Dialog Title
        alertDialog2.setTitle("Info");

// Setting Dialog Message
        alertDialog2.setMessage("You have taken all your quizzes for the month. \nPractise more and come back next month.");

// Setting Icon to Dialog
        // alertDialog2.setIcon(R.drawable.);
        alertDialog2.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        // ViewUtil.showToast("You clicked on NO");
                        dialog.cancel();
                    }
                });

// Showing Alert Dialog
        alertDialog2.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }


    private void callLinkGenerateApi() {
        binding.inviteProgress.setVisibility(View.VISIBLE);
        DynamiclinkResModel dynamiclinkResModel = new DynamiclinkResModel();
        dynamiclinkResModel.setReffeUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        dynamiclinkResModel.setSource(AppConstant.AURO_ID);
        dynamiclinkResModel.setNavigationTo(AppConstant.NavigateToScreen.STUDENT_DASHBOARD);
        dynamiclinkResModel.setReffer_type("" + AppConstant.UserType.STUDENT);
        AppLogger.e("callRefferApi", "step 3" + new Gson().toJson(dynamiclinkResModel));
        viewModel.getDynamicData(dynamiclinkResModel);
    }

    private void getAddedChild()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String suserid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();

        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",suserid);
        RemoteApi.Companion.invoke().getAllChildRefer(map_data)
                .enqueue(new Callback<GetAllChildReferModel>()
                {
                    @Override
                    public void onResponse(Call<GetAllChildReferModel> call, Response<GetAllChildReferModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            if (!(response.body().getUserDetails() == null) || !(response.body().getUserDetails().isEmpty()) ){


                                listchilds = response.body().getUserDetails().get(0).getRefferalusers();
                                for (int i =0; i<listchilds.size(); i++){
                                    // if (listchilds.get(i).getIsMaster().equals("0")||listchilds.get(i).getIsMaster().equals(0)){
                                    list.add(listchilds.get(i));

                                    // }
                                }
                                ReferralListStudentAdapter studentListAdapter = new ReferralListStudentAdapter(getActivity(), list);
                                binding.recyclerviewRefferal.setAdapter(studentListAdapter);
                            }

                        }
                        else
                        {
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetAllChildReferModel> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getRefferedAddedChild()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String suserid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",suserid);
        RemoteApi.Companion.invoke().getAllChildRefer(map_data)
                .enqueue(new Callback<GetAllChildReferModel>()
                {
                    @Override
                    public void onResponse(Call<GetAllChildReferModel> call, Response<GetAllChildReferModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            if (!(response.body().getUserDetails() == null) || !(response.body().getUserDetails().isEmpty()) ){


                                listchilds1 = response.body().getUserDetails().get(0).getRefferedusers();
                                for (int i =0; i<listchilds1.size(); i++){
                                    // if (listchilds.get(i).getIsMaster().equals("0")||listchilds.get(i).getIsMaster().equals(0)){
                                    list1.add(listchilds1.get(i));

                                    // }
                                }
                                ReferredlListStudentAdapter studentListAdapter = new ReferredlListStudentAdapter(getActivity(), list1);
                                binding.recyclerviewReffered.setAdapter(studentListAdapter);
                            }

                        }
                        else
                        {
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetAllChildReferModel> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}


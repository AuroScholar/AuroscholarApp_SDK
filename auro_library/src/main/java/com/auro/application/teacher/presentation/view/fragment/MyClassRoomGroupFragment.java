package com.auro.application.teacher.presentation.view.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.FragmentClassRoomGroupBinding;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.teacher.data.model.request.TeacherUserIdReq;
import com.auro.application.teacher.data.model.response.TeacherClassRoomResModel;
import com.auro.application.teacher.data.model.response.TeacherGroupRes;
import com.auro.application.teacher.data.model.response.TotalStudentResModel;
import com.auro.application.teacher.data.model.response.UserImageInGroupResModel;
import com.auro.application.teacher.presentation.view.adapter.StudentListAdapter;
import com.auro.application.teacher.presentation.view.adapter.GroupAdapter;
import com.auro.application.teacher.presentation.viewmodel.MyClassroomViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringTeacherDynamic;
//import com.facebook.share.model.ShareLinkContent;
//import com.facebook.share.widget.ShareDialog;

import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;
import me.toptas.fancyshowcase.listener.OnViewInflateListener;

public class MyClassRoomGroupFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {


    @Inject
    @Named("MyClassRoomGroupFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "MyClassRoomGroupFragment";
    FragmentClassRoomGroupBinding binding;
    MyClassroomViewModel viewModel;
    boolean isStateRestore;
    UserImageInGroupResModel userImageInGroupResModel;
    StudentListAdapter studentListAdapter;


    GroupAdapter newClassAdapter;


    TeacherClassRoomResModel resModel;

    public MyClassRoomGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (binding != null) {
            isStateRestore = true;
            return binding.getRoot();
        }
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyClassroomViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        return binding.getRoot();

        // return inflater.inflate(R.layout.fragment_class_room_group, container, false);
    }

    @Override
    protected void init() {
        //ViewUtil.setTeacherProfilePic(binding.imageView6);
        AppStringTeacherDynamic.setMyClassRoomGroupFragmentStrings(binding);
        ViewUtil.setTeacherProfilePic(binding.imageView6);
       /* AppLogger.e("init",""+isAppInstalled(AppConstant.PACKAGE_WHATSAPP));
        if (isAppInstalled(AppConstant.PACKAGE_WHATSAPP)) {
            binding.txtShareWithOther.setVisibility(View.VISIBLE);

        } else {
            binding.txtShareWithOther.setVisibility(View.GONE);
        }*/
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        AppLogger.e("SummaryData", "Stem 0");
        HomeActivity.setListingActiveFragment(HomeActivity.TEACHER_MY_CLASSROOM_FRAGMENT);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
        binding.btAddGroup.setOnClickListener(this);
        binding.txtShareWithOther.setOnClickListener(this);
        binding.whatsAppLayout.setOnClickListener(this);
        callTeacherClassroomApi();

    }


    private void callTeacherClassroomApi() {
        // 652597
        TeacherUserIdReq teacherUserIdReq = new TeacherUserIdReq();
        teacherUserIdReq.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        AppLogger.v("InfoScreen", " step 1 ");
        handleProgress(0, "");
        viewModel.checkInternet(teacherUserIdReq, Status.TEACHER_CLASSROOM);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_class_room_group;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.bt_add_group) {
            openCreateGroupFragment();
        } else if (id == R.id.txtShareWithOther || id == R.id.whats_app_layout) {
            ((HomeActivity) getActivity()).setCommonCallBackListner(this);
            ((HomeActivity) getActivity()).callRefferApi();
            handleRefferProgress(0);
        }
    }

    void openCreateGroupFragment() {

        int count = 0;
        for (TotalStudentResModel model : resModel.getTotalStudentList()) {
            if (model.getGroupId().isEmpty()) {
                CreateGroupFragment createGroupFragment = new CreateGroupFragment();
             /*   Bundle bundle=new Bundle();
                bundle.putParcelable(AppConstant.SENDING_DATA.CLASSROOM_RESPONSE_MODEL,resModel);
                createGroupFragment.setArguments(bundle);*/
                ((HomeActivity) getActivity()).openFragment(createGroupFragment);
                break;
            } else {
                count++;
            }
        }
        if (resModel.getTotalStudentList().size() == count) {
            showSnackbarError("Please Select the student before add in the group");
        }

    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case GROUP_CLICK_CALLBACK:
                TeacherGroupRes teacherGroupRes = (TeacherGroupRes) commonDataModel.getObject();
                if (studentListAdapter != null) {
                    List<TotalStudentResModel> list = updateList(teacherGroupRes);
                    if (!list.isEmpty()) {
                        binding.rvChooseStudent.setVisibility(View.VISIBLE);
                        binding.studentListMessage.setVisibility(View.GONE);
                        studentListAdapter.setData(list);
                    } else {
                        binding.rvChooseStudent.setVisibility(View.GONE);
                        binding.studentListMessage.setVisibility(View.VISIBLE);
                    }

                }
                break;

            case REFFER_API_SUCCESS:
                AppLogger.e("performClick-", "REFFER_API_SUCCESS");
                handleRefferProgress(1);
                if (isVisible()) {
                    AppLogger.e("performClick-", "REFFER_API_SUCCESS");
                    ((HomeActivity) getActivity()).setCommonCallBackListner(null);
                    performClick();
                }
                break;

            case REFFER_API_ERROR:
                if (isVisible()) {
                    AppLogger.e("performClick-", "REFFER_API_ERROR");
                    ((HomeActivity) getActivity()).setCommonCallBackListner(null);
                    handleRefferProgress(1);
                }
                break;
        }
    }

    private void openWhatsApp(String numero, String mensaje) {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + numero + "&text=" + mensaje)));
        } catch (Exception e) {
            ViewUtil.showSnackBar(binding.getRoot(), "Please install the whats app first");
        }

    }

    private void performClick() throws IllegalStateException {
        AppLogger.e("performClick-", "performClick calling 1");
        String completeLink = getActivity().getResources().getString(R.string.teacher_share_msg);
        if (AuroApp.getAuroScholarModel() != null && !TextUtil.isEmpty(AuroApp.getAuroScholarModel().getReferralLink())) {
            completeLink = completeLink + AuroApp.getAuroScholarModel().getReferralLink();
        }
        openWhatsApp("", completeLink);
        // shareWithFriends(completeLink);

    }

    public void shareWithFriends(String link) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Invite to AuroScholar Scholarship");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        getActivity().startActivity(shareIntent);
    }


    public void setAdapterInMyClassRoom(List<TeacherGroupRes> groups) {
        binding.rvItemPic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        binding.rvItemPic.setHasFixedSize(true);
        newClassAdapter = new GroupAdapter(getActivity(), groups, this);
        binding.rvItemPic.setAdapter(newClassAdapter);
    }

    public void setAdapterAllListStudent(List<TotalStudentResModel> totalStudentList) {
        binding.rvChooseStudent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        binding.rvChooseStudent.setHasFixedSize(true);
        studentListAdapter = new StudentListAdapter(getActivity(), totalStudentList, this);
        binding.rvChooseStudent.setAdapter(studentListAdapter);
        if (!totalStudentList.isEmpty()) {
            binding.rvChooseStudent.setVisibility(View.VISIBLE);
            binding.studentListMessage.setVisibility(View.GONE);

        } else {
            binding.rvChooseStudent.setVisibility(View.GONE);
            binding.studentListMessage.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setToolbar();
        setListener();
    }


    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            AppLogger.v("observeServiceResponse", "MyClassRoomGroupFragment");


            switch (responseApi.status) {
                case LOADING:
                    if (isVisible()) {
                        if (!isStateRestore) {
                            handleProgress(0, "");
                        }
                    }
                    break;

                case SUCCESS:
                    AppLogger.v("InfoScreen", " step 2 ");
                    if (responseApi.apiTypeStatus == Status.TEACHER_CLASSROOM) {
                        AppLogger.v("InfoScreen", " step 3 ");
                        TeacherClassRoomResModel commonDataResModel = (TeacherClassRoomResModel) responseApi.data;
                        handleProgress(1, "");
                        resModel = commonDataResModel;
                        AppLogger.v("InfoScreen", " step 4 ");
                        setDataOnInitializeView(resModel);
                    }
                    AppLogger.v("InfoScreen", " step 5 ");

                    break;

                case FAIL:
                case NO_INTERNET:
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                    }
                    break;
                default:
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                    }
                    break;
            }

        });
    }

    private void handleProgress(int status, String message) {
        switch (status) {
            case 0:
                binding.mainParentLayout.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.shimmerMyClassroom.setVisibility(View.VISIBLE);
                binding.shimmerMyClassroom.startShimmer();
                break;

            case 1:
                binding.mainParentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.shimmerMyClassroom.setVisibility(View.GONE);
                binding.shimmerMyClassroom.stopShimmer();
                break;

            case 2:
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.mainParentLayout.setVisibility(View.GONE);
                binding.shimmerMyClassroom.setVisibility(View.GONE);
                binding.shimmerMyClassroom.stopShimmer();
                binding.errorLayout.textError.setText(message);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //viewModel.getTeacherProfileData(auroScholarDataModel);
                    }
                });
                break;
        }

    }

    public void setDataOnInitializeView(TeacherClassRoomResModel teacherDasboardl) {
        AppLogger.e("InfoScreen -=total student-", "" + teacherDasboardl.getTotalStudentList().size());
        if (teacherDasboardl.getTotalStudentList().size() < 2) {
            binding.RPDetailInformation.setText(teacherDasboardl.getTotalStudentList().size() + " Student");
        } else {
            binding.RPDetailInformation.setText(teacherDasboardl.getTotalStudentList().size() + " Students");
        }

        addOneItem(teacherDasboardl);
        setAdapterInMyClassRoom(teacherDasboardl.getGroups());
        setAdapterAllListStudent(teacherDasboardl.getTotalStudentList());
    }

    public void addOneItem(TeacherClassRoomResModel teacherClassRoomResModel) {
        TeacherGroupRes teacherGroupRes = new TeacherGroupRes();
        teacherGroupRes.setGroupId("0");
        teacherGroupRes.setGroupName("All");
        teacherGroupRes.setGroupMembers(Collections.emptyList());
        teacherGroupRes.setGroupCreationDate("" + System.currentTimeMillis());
        List<TeacherGroupRes> groupList = teacherClassRoomResModel.getGroups();
        groupList.add(0, teacherGroupRes);
    }

    public List<TotalStudentResModel> updateList(TeacherGroupRes groupRes) {
        List<TotalStudentResModel> newlist = new ArrayList<>();
        if (groupRes.getGroupId().equalsIgnoreCase("0")) {
            return resModel.getTotalStudentList();
        } else {
            for (TotalStudentResModel studentResModel : resModel.getTotalStudentList()) {
                if (studentResModel.getGroupId().equalsIgnoreCase(groupRes.getGroupId())) {
                    newlist.add(studentResModel);
                }
            }
            return newlist;
        }
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
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

    private boolean isAppInstalled(String packageName) {
        PackageManager packageManager = AuroApp.getAppContext().getPackageManager();
        for (PackageInfo packageInfo : packageManager.getInstalledPackages(0)) {
            AppLogger.e("isAppInstalled-", packageInfo.packageName);
            if (packageInfo.packageName.equals(AppConstant.PACKAGE_WHATSAPP)) {

                return true;
            }
        }

        return false;
    }

}
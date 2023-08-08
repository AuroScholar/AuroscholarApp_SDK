package com.auro.application.teacher.presentation.view.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.FragmentCreateGroupBinding;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.teacher.data.model.common.CommonDataResModel;
import com.auro.application.teacher.data.model.request.TeacherAddStudentInGroupReqModel;
import com.auro.application.teacher.data.model.request.TeacherCreateGroupReqModel;
import com.auro.application.teacher.data.model.request.TeacherUserIdReq;
import com.auro.application.teacher.data.model.response.TeacherClassRoomResModel;
import com.auro.application.teacher.data.model.response.TeacherCreateGroupResModel;
import com.auro.application.teacher.data.model.response.TotalStudentResModel;
import com.auro.application.teacher.presentation.view.adapter.AddedStudentListAdapter;
import com.auro.application.teacher.presentation.view.adapter.GroupStudentListAdapter;
import com.auro.application.teacher.presentation.viewmodel.MyClassroomViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringTeacherDynamic;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;


public class CreateGroupFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {

    @Inject
    @Named("CreateGroupFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "MyClassRoomGroupFragment";
    FragmentCreateGroupBinding binding;
    MyClassroomViewModel viewModel;
    boolean isStateRestore;
    TeacherClassRoomResModel teacherClassRoomResModel;
    AddedStudentListAdapter addedStudentListAdapter;
    List<TotalStudentResModel> totalStudentResModelList = new ArrayList<>();
    GroupStudentListAdapter studentListAdapter;
    TeacherCreateGroupResModel teacherCreateGroupResModel;



    public CreateGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLogger.v("callAddStudentInGroup", " onCreateView ");

       /* if (getArguments() != null) {
            teacherClassRoomResModel = getArguments().getParcelable(AppConstant.SENDING_DATA.CLASSROOM_RESPONSE_MODEL);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppLogger.v("callAddStudentInGroup", " onCreateView ");
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyClassroomViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setListener();

        return binding.getRoot();
    }

    @Override
    protected void init() {

        setListener();
        TeacherUserIdReq teacherUserIdReq = new TeacherUserIdReq();
       // teacherUserIdReq.setUserId("652597");
        teacherUserIdReq.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        viewModel.checkInternet(teacherUserIdReq, Status.TEACHER_CLASSROOM);
        handleProgress(0, "");
        setAdapterAllListAddedStudent(Collections.emptyList());
        //setAdapterAllListStudent(teacherClassRoomResModel.getTotalStudentList());
        AppStringTeacherDynamic.setCreateGroupStrings(binding);
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    public void onResume() {
        super.onResume();
        setListener();

    }

    @Override
    protected void setListener() {
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            AppLogger.v("InfoScreen", " step 0 SetListener");
            observeServiceResponse();
        }

        binding.backgroundSprincle11.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_create_group;
    }


    public void setAdapterAllListAddedStudent(List<TotalStudentResModel> totalStudentList) {
        binding.rvAddedStudentList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvAddedStudentList.setHasFixedSize(true);
        addedStudentListAdapter = new AddedStudentListAdapter(getActivity(), totalStudentList, this);
        binding.rvAddedStudentList.setAdapter(addedStudentListAdapter);
    }

    public void setAdapterAllListStudent(List<TotalStudentResModel> totalStudentList) {
        GridLayoutManager gridlayout = new GridLayoutManager(getActivity(), 4);
        gridlayout.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvUnknowGroup.setLayoutManager(gridlayout);
        binding.rvUnknowGroup.setHasFixedSize(true);
        studentListAdapter = new GroupStudentListAdapter(getActivity(), totalStudentList, this);
        binding.rvUnknowGroup.setAdapter(studentListAdapter);

    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case ADD_STUDENT:
                addStudentInList((TotalStudentResModel) commonDataModel.getObject(), true, commonDataModel.getSource());
                break;

            case REMOVE_STUDENT:
                addStudentInList((TotalStudentResModel) commonDataModel.getObject(), false, commonDataModel.getSource());
                break;
        }
    }

    private void addStudentInList(TotalStudentResModel totalStudentResModel, boolean isAdd, int pos) {
        if (isAdd) {
            totalStudentResModelList.add(totalStudentResModel);

            if (!TextUtil.checkListIsEmpty(totalStudentResModelList)) {
                binding.rvAddedStudentList.setVisibility(View.VISIBLE);
                binding.addedListMessage.setVisibility(View.GONE);
                addedStudentListAdapter.setData(totalStudentResModelList);
                teacherClassRoomResModel.getTotalStudentList().remove(pos);

                studentListAdapter.setData(teacherClassRoomResModel.getTotalStudentList());
                if (teacherClassRoomResModel.getTotalStudentList().size() == 0) {
                    binding.studentListMessage.setVisibility(View.VISIBLE);
                    binding.rvUnknowGroup.setVisibility(View.GONE);
                }
            }
        } else {
            if (!TextUtil.checkListIsEmpty(totalStudentResModelList)) {
                totalStudentResModelList.remove(pos);
                if (totalStudentResModelList.size() == 0) {
                    binding.rvAddedStudentList.setVisibility(View.GONE);
                    binding.addedListMessage.setVisibility(View.VISIBLE);
                }
                addedStudentListAdapter.setData(totalStudentResModelList);
                teacherClassRoomResModel.getTotalStudentList().add(totalStudentResModel);
                studentListAdapter.setData(teacherClassRoomResModel.getTotalStudentList());

                if (teacherClassRoomResModel.getTotalStudentList().size() > 0) {
                    binding.studentListMessage.setVisibility(View.GONE);
                    binding.rvUnknowGroup.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    private void callTeacher() {
       String groupName = binding.etGroupName.getText().toString();
        if(!TextUtil.isEmpty(groupName)){
            TeacherCreateGroupReqModel reqModel = new TeacherCreateGroupReqModel();
            //reqModel.setUserId("652597");
            reqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
            reqModel.setGroupName(groupName);
            handleProgress(0, "");
            AppLogger.v("InfoScreen", " callTeacher  !TextUtil.isEmpty(groupName)");
            viewModel.checkInternet(reqModel, Status.TEACHER_CREATE_GROUP_API);
        }else{
            ViewUtil.showSnackBar(binding.getRoot(), "Enter Group name");
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backgroundSprincle_11) {
            if (teacherCreateGroupResModel == null) {
                AppLogger.v("SizeAuro", " callTeacher  true  " + totalStudentResModelList.size() + "-----");
                if (totalStudentResModelList.size() != 0) {
                    callTeacher();
                } else {
                    ViewUtil.showSnackBar(binding.getRoot(), "Please add student in the group");
                }


            } else {
                AppLogger.v("SizeAuro", " callTeacher  false  " + totalStudentResModelList.size() + "-----");
                //
                ViewUtil.showSnackBar(binding.getRoot(), "Please enter the group name");
            }
        }
    }


    private void observeServiceResponse() {
        AppLogger.v("InfoScreen", " step 0");
        viewModel.serviceLiveData().observeForever(responseApi -> {
            AppLogger.v("InfoScreen", " step 1  serviceLiveData");

            switch (responseApi.status) {
                case SUCCESS:
                    AppLogger.v("InfoScreen", " step 2 "+responseApi.apiTypeStatus);
                    if (responseApi.apiTypeStatus == Status.TEACHER_CREATE_GROUP_API) {

                        teacherCreateGroupResModel = (TeacherCreateGroupResModel)responseApi.data;
                        AppLogger.v("InfoScreen", " step 3 ");
                        //handleProgress(1, "");
                        setOnUiGroupName();
                        callAddStudentInGroup();
                        AppLogger.v("InfoScreen", " step 4 "+ responseApi.data);
                    } else if (responseApi.apiTypeStatus == Status.TEACHER_ADD_STUDENT_IN_GROUP_API) {
                        AppLogger.v("InfoScreen", " step 5 ");
                        CommonDataResModel commonDataResModel = (CommonDataResModel) responseApi.data;
                        handleProgress(1, "");
                        ViewUtil.showSnackBar(binding.getRoot(), commonDataResModel.getMessage());
                        ((HomeActivity) getActivity()).onBackPressed();
                        AppLogger.v("InfoScreen", " step 6 ");
                     }else if(responseApi.apiTypeStatus == Status.TEACHER_CLASSROOM){

                        AppLogger.v("InfoScreen", " step 7 ");
                        teacherClassRoomResModel  = (TeacherClassRoomResModel) responseApi.data;

                        handleProgress(1, "");
                        AppLogger.v("InfoScreen", " step 8 ");
                        setDatainList(teacherClassRoomResModel);

                    }
                    break;

                case FAIL:
                    AppLogger.v("InfoScreen", " step fail in CreateGroup "+responseApi.data);
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                    }
                    break;
                case NO_INTERNET:
                    AppLogger.v("InfoScreen", " step 11 ");
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                    }
                    break;

                default:
                    AppLogger.v("InfoScreen", " step 3 ");

                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                    }
                    break;
            }

        });
    }

    private void handleProgress(int i, String data) {

        switch (i) {
            case 0:
                binding.progressLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                binding.progressLayout.setVisibility(View.GONE);
                break;

            case 2:
                binding.progressLayout.setVisibility(View.GONE);
                ViewUtil.showSnackBar(binding.getRoot(), data);
                break;
        }
    }

    public void setOnUiGroupName(){

        binding.groupNameDefine.setText("Group Name("+teacherCreateGroupResModel.getTeacherGroupName()+")");

    }

    void callAddStudentInGroup() {
        List<String> useridslist = new ArrayList<>();

        TeacherAddStudentInGroupReqModel reqModel = new TeacherAddStudentInGroupReqModel();
        for(TotalStudentResModel model : totalStudentResModelList) {
            useridslist.add(model.getStudentId());
            AppLogger.v("InfoScreen", " callTeacher  false"+model.getStudentId());
        }
        reqModel.setGroupId(teacherCreateGroupResModel.getTeacherGroupId());
        reqModel.setUserIds(useridslist);
        reqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        AppLogger.v("InfoScreen", " callTeacher  false"+useridslist.size());
        viewModel.checkInternet(reqModel, Status.TEACHER_ADD_STUDENT_IN_GROUP_API);
    }
    public void setDataOnInitializeView(TeacherClassRoomResModel teacherDasboardl) {
        AppLogger.e("InfoScreen -=total student-", "" + teacherDasboardl.getTotalStudentList().size());

        setAdapterAllListStudent(teacherDasboardl.getTotalStudentList());
    }

    public void setDatainList(TeacherClassRoomResModel modelNew){

        List<TotalStudentResModel> teacher = new ArrayList<>();
        for(TotalStudentResModel model:modelNew.getTotalStudentList()){
            if(model.getGroupId().isEmpty()){
                teacher.add(model);
            }
        }

        teacherClassRoomResModel.setTotalStudentList(teacher);
        binding.RPDetailInformation.setText(teacherClassRoomResModel.getTotalStudentList().size()+" Students");
        setDataOnInitializeView(teacherClassRoomResModel);
    }

}
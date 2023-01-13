package com.auro.application.teacher.presentation.view.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.auro.application.databinding.FragmentUpCommingBookBinding;
import com.auro.application.teacher.data.model.common.CommonDataResModel;
import com.auro.application.teacher.data.model.request.BookSlotReqModel;
import com.auro.application.teacher.data.model.request.BookedSlotReqModel;
import com.auro.application.teacher.data.model.response.AvailableSlotListResModel;
import com.auro.application.teacher.data.model.response.AvailableSlotResModel;
import com.auro.application.teacher.data.model.response.TimeSlotResModel;
import com.auro.application.teacher.presentation.view.adapter.UpComingSlotsAdapter;
import com.auro.application.teacher.presentation.viewmodel.TeacherInfoViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.UpComingTimeSlotDialog;
import com.auro.application.util.strings.AppStringTeacherDynamic;
import com.bumptech.glide.Glide;

import javax.inject.Inject;
import javax.inject.Named;

public class UpComingBookFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {

    @Inject
    @Named("UpCommingBookFragment")
    ViewModelFactory viewModelFactory;
    FragmentUpCommingBookBinding binding;
    TeacherInfoViewModel viewModel;
    boolean isStateRestore;
    UpComingTimeSlotDialog customDialog;
    AvailableSlotListResModel availableSlotListResModel;
    CommonCallBackListner listner;

    public UpComingBookFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TeacherInfoViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setToolbar();
        setListener();
        return binding.getRoot();
    }

    @Override
    protected void init() {
        callAvailableSlotsApi(0);
        AppStringTeacherDynamic.setUpComingBookFragmentStrings(binding);
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        listner = this;

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_up_comming_book;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case ONCLICKFORTIMESLOT:
                AvailableSlotResModel availableSlotResModel = (AvailableSlotResModel) commonDataModel.getObject();
                openDialog(availableSlotResModel);
                break;

            case BOOK_SLOT_CLICK:
                callBookSlotApi((TimeSlotResModel) commonDataModel.getObject());
                break;
        }

    }

    public void setAdapterAllListStudent() {
        binding.rvChooseSlot.setLayoutManager(new GridLayoutManager(getContext(), 5, GridLayoutManager.VERTICAL, false));
        binding.rvChooseSlot.setHasFixedSize(true);
        UpComingSlotsAdapter newClassAdapter = new UpComingSlotsAdapter(getActivity(), availableSlotListResModel.getAvailableSlots(), this);
        binding.rvChooseSlot.setAdapter(newClassAdapter);
    }


    private void openDialog(AvailableSlotResModel availableSlotResModel) {

        if (getContext() != null) {
            AppLogger.v("AvailableSlot","Step 1"+availableSlotResModel.getTimeSlots().size());
            UpComingTimeSlotDialog upComingTimeSlotDialog = UpComingTimeSlotDialog.newInstance(listner, availableSlotResModel);
            ((BookSlotFragment)getParentFragment()).openFragment(upComingTimeSlotDialog);
        }

    }


    public void callBookSlotApi(TimeSlotResModel timeSlotResModel) {
       /* if (customDialog != null && customDialog.isShowing()) {
            customDialog.updateProgressStatus(0);
        }*/

        BookSlotReqModel reqModel = new BookSlotReqModel();
       // reqModel.setUserId("576232");
        reqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getUserId());
        reqModel.setWebinarId("" + timeSlotResModel.getWebinarId());
        reqModel.setBookingStatus("0");
        viewModel.checkInternet(reqModel, Status.BOOK_SLOT_API);
    }

    public void callAvailableSlotsApi(int value) {
        if(value == 0) {
            AppLogger.e("available_slot_api", "step 1");
            AppLogger.v("BookSlot","Step 3 avilablke slot");
            setProgress(0, "");
            BookedSlotReqModel reqModel = new BookedSlotReqModel();
           // reqModel.setUserId("576232");
            reqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
            viewModel.checkInternet(reqModel, Status.TEACHER_AVAILABLE_SLOTS_API);
        }else{
            AppLogger.v("BookSlot","Step 3 avilablke slot sucess");
            AppLogger.v("available_slot_api", "step 2");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        AppLogger.v("BookSlot", "Step 2 UpCommingBookFragment") ;
    }

    public void setProgress(int status, String msg) {
        AppLogger.e("setProgress" ,"step 1");
        switch (status) {
            case 0:
                binding.rvChooseSlot.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.errorLayout.textError.setVisibility(View.GONE);
                binding.errorLayout.btRetry.setVisibility(View.GONE);
                binding.errorLayout.errorIcon.setVisibility(View.VISIBLE);
                binding.bookedTab.setVisibility(View.GONE);
                Glide.with(this).load(R.raw.loader_gif).into(binding.errorLayout.errorIcon);
                break;

            case 1:
                binding.rvChooseSlot.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.errorLayout.textError.setVisibility(View.GONE);
                binding.errorLayout.btRetry.setVisibility(View.GONE);
                binding.errorLayout.errorIcon.setVisibility(View.GONE);
                binding.errorLayout.errorIcon.setImageDrawable(getActivity().getDrawable(R.drawable.ic_error_outline_black_24dp));
                binding.bookedTab.setVisibility(View.VISIBLE);
                break;

            case 2:
                binding.rvChooseSlot.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.errorLayout.textError.setVisibility(View.VISIBLE);
                binding.errorLayout.btRetry.setVisibility(View.VISIBLE);
                binding.errorLayout.textError.setText(msg);
                binding.bookedTab.setVisibility(View.GONE);
                binding.errorLayout.errorIcon.setImageDrawable(getActivity().getDrawable(R.drawable.ic_error_outline_black_24dp));
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callAvailableSlotsApi(0);
                    }
                });
                binding.errorLayout.errorIcon.setVisibility(View.VISIBLE);
                break;

            case 3:
                binding.rvChooseSlot.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.errorLayout.textError.setVisibility(View.VISIBLE);
                binding.errorLayout.btRetry.setVisibility(View.GONE);
                binding.errorLayout.textError.setText(msg);
                binding.bookedTab.setVisibility(View.GONE);
                binding.errorLayout.errorIcon.setVisibility(View.GONE);
                break;

        }
    }


    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    AppLogger.e("available_slot_api","step 9");
                    if (responseApi.apiTypeStatus == Status.BOOK_SLOT_API) {
                        CommonDataResModel commonDataResModel = (CommonDataResModel) responseApi.data;
                        handleBookSlotApiResponse(commonDataResModel);
                    } else if(responseApi.apiTypeStatus == Status.TEACHER_AVAILABLE_SLOTS_API) {
                        AppLogger.e("available_slot_api","step 10");
                        availableSlotListResModel = (AvailableSlotListResModel) responseApi.data;
                        if (availableSlotListResModel.getError()) {
                            AppLogger.e("available_slot_api","step 11");
                            setProgress(2, availableSlotListResModel.getMessage());
                        } else {
                            AppLogger.e("available_slot_api","step 12");
                            setProgress(1, "");
                            setAdapterAllListStudent();
                        }

                    }
                    break;

                case FAIL:
                case NO_INTERNET:
                default:
                    if (responseApi.apiTypeStatus == Status.BOOK_SLOT_API) {
                        hideDialogProgress();
                    }else {
                        setProgress(2, (String) responseApi.data);
                    }

                    break;

            }

        });
    }

    public void handleBookSlotApiResponse(CommonDataResModel commonDataResModel) {
        hideDialogProgress();
        if (commonDataResModel.getError()) {

        } else {


            ViewUtil.showSnackBar(binding.getRoot(), "Slot Booked Successfully");

        }
    }

    private void hideDialogProgress() {

    }




}

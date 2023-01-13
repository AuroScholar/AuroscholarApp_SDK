package com.auro.application.util.alert_dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseDialog;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.UpdateConfirmFragmentBinding;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.teacher.data.model.common.CommonDataResModel;
import com.auro.application.teacher.data.model.request.BookSlotReqModel;
import com.auro.application.teacher.data.model.response.AvailableSlotResModel;
import com.auro.application.teacher.data.model.response.TimeSlotResModel;
import com.auro.application.teacher.presentation.view.adapter.BookCarouselAdapter;
import com.auro.application.teacher.presentation.view.adapter.CenterZoomLayoutManager;
import com.auro.application.teacher.presentation.view.adapter.CirclePagerIndicatorDecoration;
import com.auro.application.teacher.presentation.view.fragment.BookSlotFragment;
import com.auro.application.teacher.presentation.viewmodel.TeacherInfoViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.ViewUtil;

import javax.inject.Inject;
import javax.inject.Named;


public class UpComingTimeSlotDialog extends BaseDialog implements CommonCallBackListner, View.OnClickListener {

    @Inject
    @Named("UpComingTimeSlotDialog")
    ViewModelFactory viewModelFactory;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static String bundleavailableSlotResModel = "availableSlotResModel";
    private static CommonCallBackListner commonCallBackListner;
    CommonCallBackListner listner;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AvailableSlotResModel availableSlotResModel;
    //CommonCallBackListner commonCallBackListner;
    BookCarouselAdapter leaderBoardAdapter;


    public Context context;
    private UpdateConfirmFragmentBinding binding;
    TeacherInfoViewModel viewModel;

   // private final CommonCallBackListner commonCallBackListner;
   // private final AvailableSlotResModel availableSlotResModel;

    TimeSlotResModel timeSlotResModel;

    public UpComingTimeSlotDialog() {

    }
    public static UpComingTimeSlotDialog newInstance(CommonCallBackListner param1, AvailableSlotResModel param2) {
        UpComingTimeSlotDialog fragment = new UpComingTimeSlotDialog();
        AppLogger.v("AvailableSlot","Step 2"+param2.getTimeSlots().size());
        commonCallBackListner = param1;
        Bundle args = new Bundle();
        args.putParcelable(bundleavailableSlotResModel, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            availableSlotResModel = getArguments().getParcelable(bundleavailableSlotResModel);
            AppLogger.v("AvailableSlot","Step 4"+availableSlotResModel.getTimeSlots().size());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TeacherInfoViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setListener();
        setToolbar();
        return binding.getRoot();
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

        switch (commonDataModel.getClickType()) {
            case BOOK_WEBINAR_SLOT:
                timeSlotResModel = (TimeSlotResModel) commonDataModel.getObject();
                AppLogger.v("BookSlot","Step 3 ");

                callBookSlotApi(timeSlotResModel);
                break;
        }

    }


    @Override
    protected void init() {
        listner = this;
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        binding.closeButton.setOnClickListener(this);

        setCarouselView();

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.update_confirm_fragment;
    }

    public void setCarouselView(){

        binding.itemListHorizontal.setNestedScrollingEnabled(false);
        binding.itemListHorizontal.setLayoutManager(new CenterZoomLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        binding.itemListHorizontal.setHasFixedSize(true);
         leaderBoardAdapter = new BookCarouselAdapter(getContext(),listner,availableSlotResModel.getTimeSlots(), availableSlotResModel.getDate());
        binding.itemListHorizontal.setAdapter(leaderBoardAdapter);
        binding.itemListHorizontal.addItemDecoration(new CirclePagerIndicatorDecoration());
        new PagerSnapHelper().attachToRecyclerView(binding.itemListHorizontal);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.closeButton) {
            dismiss();
        }
    }



    public void callBookSlotApi(TimeSlotResModel timeSlotResModel) {
       /* if (customDialog != null && customDialog.isShowing()) {
            customDialog.updateProgressStatus(0);
        }*/

        BookSlotReqModel reqModel = new BookSlotReqModel();
      //  reqModel.setUserId("576232");
        reqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        reqModel.setWebinarId("" + timeSlotResModel.getWebinarId());
        reqModel.setBookingStatus("0");
        //leaderBoardAdapter.setOpenProgressBar(0,"");
        viewModel.checkInternet(reqModel, Status.BOOK_SLOT_API);
    }


    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    AppLogger.e("available_slot_api","step 9");
                    if (responseApi.apiTypeStatus == Status.BOOK_SLOT_API) {
                        CommonDataResModel commonDataResModel = (CommonDataResModel) responseApi.data;




                        handleBookSlotApiResponse(commonDataResModel);
                    }
                    break;

                case FAIL:
                case NO_INTERNET:
                default:
                    if (responseApi.apiTypeStatus == Status.BOOK_SLOT_API) {
                        showSnackBar("failed to book slot", Color.RED);
                       // leaderBoardAdapter.setOpenProgressBar(3,(String) responseApi.data);
                    }
                    break;

            }

        });
    }

    public void showSnackBar(String message,int color){
        ViewUtil.showSnackBar(binding.getRoot(),message,color );
    }

    public void handleBookSlotApiResponse(CommonDataResModel commonDataResModel){
        ((HomeActivity)getActivity()).openFragment(new BookSlotFragment());
        dismiss();
        showSnackBar(commonDataResModel.getMessage(),  Color.GREEN);
    }

   /* public void setProgress(int status, String msg) {
        AppLogger.e("setProgress" ,"step 1");
        switch (status) {
            case 0:
                binding.errorConstraint.setVisibility(View.GONE);
                binding.rvBookSlot.setVisibility(View.GONE);
                binding.progressGif.setVisibility(View.VISIBLE);

                break;

            case 1:
                binding.rvBookSlot.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressGif.setVisibility(View.GONE);
                break;

            case 2:
                binding.rvBookSlot.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.progressGif.setVisibility(View.GONE);
                binding.errorLayout.textError.setText(msg);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBookedSlotApi();
                    }
                });
                binding.errorLayout.errorIcon.setVisibility(View.VISIBLE);
                break;

        }
    }*/




}
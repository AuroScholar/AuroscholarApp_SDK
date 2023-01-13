package com.auro.application.teacher.presentation.view.fragment;

import static com.auro.application.core.common.Status.CANCEL_WEBINAR_SLOT;
import static com.auro.application.core.common.Status.TEACHER_BOOKED_SLOT_API;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
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
import com.auro.application.databinding.FragmentBookedSlotListBinding;
import com.auro.application.teacher.data.model.request.BookedSlotReqModel;
import com.auro.application.teacher.data.model.request.CancelWebinarSlot;
import com.auro.application.teacher.data.model.response.BookedSlotListResModel;
import com.auro.application.teacher.data.model.response.BookedSlotResModel;
import com.auro.application.teacher.presentation.view.adapter.MyBookedSlotAdapter;
import com.auro.application.teacher.presentation.viewmodel.TeacherInfoViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.ViewUtil;
import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class BookedSlotListFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {

    @Inject
    @Named("BookedSlotListFragment")
    ViewModelFactory viewModelFactory;
    FragmentBookedSlotListBinding binding;
    TeacherInfoViewModel viewModel;
    Resources resources;
    boolean isStateRestore;
    BookedSlotListResModel bookedSlotListResModel;
    MyBookedSlotAdapter newClassAdapter;
    List<BookedSlotResModel> listBook;
    BookedSlotResModel bookedSlotResModel;

    public BookedSlotListFragment() {// Required empty public constructor
    }

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
        //return inflater.inflate(R.layout.fragment_booked_slot_list, container, false);
    }

    @Override
    protected void init() {
        Glide.with(this).load(R.raw.loader_gif).into(binding.progressGif);

        callBookedSlotApi();
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        binding.bookWebinar.setOnClickListener(this);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_booked_slot_list;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bookWebinar) {
            ((BookSlotFragment) getParentFragment()).setPositonInViewpager(1);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        resources = ViewUtil.getCustomResource(getActivity());

    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
//
        switch (commonDataModel.getClickType()) {
            case CANCEL_CLICK:
                //refreshList(commonDataModel);

                BookedSlotResModel bookedSlotResModel = (BookedSlotResModel) commonDataModel.getObject();
                AppLogger.v("BookSlot", "Step 3 ");

                cancelSlotApi(bookedSlotResModel);
                break;
        }
    }

    public void setAdapterAllListStudent() {
        listBook = bookedSlotListResModel.getBookedSlots();
        if (listBook.size() != 0) {
            binding.bookWebinar.setVisibility(View.GONE);
            binding.rvBookSlot.setVisibility(View.VISIBLE);
            binding.rvBookSlot.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
            binding.rvBookSlot.setHasFixedSize(true);
            newClassAdapter = new MyBookedSlotAdapter(getActivity(), listBook, this);
            binding.rvBookSlot.setAdapter(newClassAdapter);
        } else {
            binding.bookWebinar.setVisibility(View.VISIBLE);

        }
    }


    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {

                case SUCCESS:
                    AppLogger.v("CANCELDIALOG", "Step 5");
                    if (responseApi.apiTypeStatus == TEACHER_BOOKED_SLOT_API) {
                        setProgress(1, "");
                        bookedSlotListResModel = (BookedSlotListResModel) responseApi.data;
                        if (bookedSlotListResModel.isError()) {
                            setProgress(2, bookedSlotListResModel.getMessage());
                        } else {
                            setAdapterAllListStudent();
                        }
                    }
                    if (responseApi.apiTypeStatus == CANCEL_WEBINAR_SLOT) {
                        AppLogger.v("CANCELDIALOG", "Step 6 ");


                        listBook.remove(bookedSlotResModel);
                        if (listBook.isEmpty()) {
                            setProgress(1, "");
                        }
                        newClassAdapter.setListInAdapter(listBook);
                    }

                    break;

                case FAIL:
                    setProgress(2, (String) responseApi.data);
                    break;

                case NO_INTERNET:
                    setProgress(2, (String) responseApi.data);
                    break;

                default:
                    setProgress(2, (String) responseApi.data);
                    break;
            }

        });
    }

    public void callBookedSlotApi() {
        setProgress(0, "");
        BookedSlotReqModel reqModel = new BookedSlotReqModel();
        // reqModel.setUserId("576232");
        reqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        viewModel.checkInternet(reqModel, Status.TEACHER_BOOKED_SLOT_API);
    }


    public void cancelSlotApi(BookedSlotResModel bookedSlotResModel) {
        this.bookedSlotResModel = bookedSlotResModel;
        // setProgress(0,"");
        CancelWebinarSlot reqModel = new CancelWebinarSlot();
        reqModel.setUserId("576232");
        reqModel.setWebinarId(bookedSlotResModel.getWebinarId());
        // setProgress(0,"");
        AppLogger.v("CANCELDIALOG", "Step 1 ");
        viewModel.checkInternet(reqModel, CANCEL_WEBINAR_SLOT);
    }

    public void setProgress(int status, String msg) {
        AppLogger.e("setProgress", "step 1");
        switch (status) {
            case 0:
                binding.errorConstraint.setVisibility(View.GONE);
                binding.rvBookSlot.setVisibility(View.GONE);
                binding.bookWebinar.setVisibility(View.GONE);
                binding.progressGif.setVisibility(View.VISIBLE);

                break;

            case 1:
                binding.rvBookSlot.setVisibility(View.VISIBLE);
                binding.bookWebinar.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressGif.setVisibility(View.GONE);
                break;

            case 2:
                binding.rvBookSlot.setVisibility(View.GONE);
                binding.bookWebinar.setVisibility(View.GONE);
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
    }
}
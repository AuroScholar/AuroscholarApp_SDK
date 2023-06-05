package com.auro.application.home.presentation.view.fragment;


import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.BottomCategoryFaqBinding;
import com.auro.application.home.data.FaqCatData;
import com.auro.application.home.data.FaqCategoryDataModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.presentation.view.adapter.FaqCategoryViewAllAdapter;
import com.auro.application.util.RemoteApi;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetQuestionCategoryDialog extends BottomSheetDialogFragment implements CommonCallBackListner {
    BottomCategoryFaqBinding binding;
    List<FaqCatData> faqCategory = new ArrayList<>();
    FaqCategoryViewAllAdapter faqCategorySelectAdapter;
    FaqCatData faqCatData;
    String faqcategoryid="";
    String categorylist="";
    String finalcatlistid="";
    StringBuilder stringBuilder= new StringBuilder();
    HashSet<Integer>categoryIdList =new HashSet<>();
    Set<String>catIds = new HashSet<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_category_faq, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        catIds = prefs.getStringSet("viewall_finalcatlistid",  new HashSet<>());

        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
        Details details = model.getDetails();
//        if (getArguments() != null)  {
//            faqCategory = (FaqCatData)getArguments().getSerializable("categorylistbundle");
//        }

        binding.btnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dismiss();
              openFragment(new FAQFragment());
            }
        });
        binding.txtclearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalcatlistid="";
             faqCategorySelectAdapter.unselectall();
                SharedPreferences.Editor editor2 = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                catIds.clear();
                editor2.putStringSet("viewall_finalcatlistid", catIds);
                editor2.apply();
                dismiss();
                openFragment(new FAQFragment());
            }
        });

            getFaqCategoryList();



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }

    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(getActivity(), fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }
    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case ITEM_CLICK:
                faqCatData = (FaqCatData) commonDataModel.getObject();
                catIds.add(faqCatData.getCategoryId().toString());

                break;
            case ITEM_LONG_CLICK:
                faqCatData = (FaqCatData) commonDataModel.getObject();
                catIds.remove(faqCatData.getCategoryId().toString());
                break;

    }

        SharedPreferences.Editor editor1 = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor1.putStringSet("viewall_finalcatlistid", catIds);
        editor1.apply();

    }
    public void setAdapterInMyClassRoom(List<FaqCatData> faqCategory) {
        binding.recyclerCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerCategory.setHasFixedSize(true);
        faqCategorySelectAdapter = new FaqCategoryViewAllAdapter(getActivity(),faqCategory,this);
        binding.recyclerCategory.setAdapter(faqCategorySelectAdapter);
    }
    public void getFaqCategoryList()
    {

        HashMap<String,String> map_data = new HashMap<>();
        String langid = AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId();

        map_data.put("LanguageId",langid);
        RemoteApi.Companion.invoke().getAllFaqCategory(map_data)
                .enqueue(new Callback<FaqCategoryDataModel>()
                {
                    @Override
                    public void onResponse(Call<FaqCategoryDataModel> call, Response<FaqCategoryDataModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                faqCategory.clear();
                                if (response.body().getStatus().equals("success")){
                                    if (!(response.body().getFaqCategoryList() == null || response.body().getFaqCategoryList().isEmpty() || response.body().getFaqCategoryList().equals("") || response.body().getFaqCategoryList().equals("null"))) {
                                        faqCategory = response.body().getFaqCategoryList();
                                    }
                                    SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
                                    Set<String>  catIds_new = prefs.getStringSet("viewall_finalcatlistid",  new HashSet<>());
                                            for (FaqCatData faqCatData : faqCategory) {
                                                if (catIds_new.contains(faqCatData.getCategoryId().toString())) {
                                                    faqCatData.setChecked(true);
                                                }

                                            }
                                    }

                                    setAdapterInMyClassRoom(faqCategory);
                                }
                            else {

                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FaqCategoryDataModel> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}


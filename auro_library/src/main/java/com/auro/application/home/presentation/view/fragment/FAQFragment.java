package com.auro.application.home.presentation.view.fragment;
import static android.content.Context.MODE_PRIVATE;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.FaqLayoutBinding;
import com.auro.application.home.data.FaqCatData;
import com.auro.application.home.data.FaqCategoryDataModel;
import com.auro.application.home.data.FaqQuesData;
import com.auro.application.home.data.FaqQuestionDataModel;
import com.auro.application.home.data.base_component.BaseFragment;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.adapter.FaqCategorySelectAdapter;
import com.auro.application.home.presentation.view.adapter.FaqQuestionSelectAdapter;
import com.auro.application.util.AppUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FAQFragment extends BaseFragment implements CommonCallBackListner {
    public static final String TAG = "FAQFragment";
    FaqLayoutBinding binding;
    List<FaqQuesData> faqList = new ArrayList<>();;
    List<FaqCatData> faqCategory = new ArrayList<>();
    FaqCatData faqCatData;
    String faqcategoryid = "";
    String categorylist;
    FaqCategorySelectAdapter faqCategorySelectAdapter;
    StringBuilder stringBuilder= new StringBuilder();

    ArrayList<FaqCatData> categorydata = new ArrayList<>();
    public FAQFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
            binding.setLifecycleOwner(this);
        }
        setRetainInstance(true);
        ViewUtil.setLanguageonUi(getActivity());
        getFaqQuestionList("","");

        return binding.getRoot();
    }



    @Override
    protected void init() {
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.FAQ_FRAGMENT);

        binding.recyclerviewFaq.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerviewFaq.setHasFixedSize(true);
        getFaqCategoryList();
        String search = binding.txtsearch.getText().toString();
        SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        Set<String>  viewall_finalcatlistid = prefs.getStringSet("viewall_finalcatlistid", null);
                                      if (viewall_finalcatlistid==null||viewall_finalcatlistid.equals("")||viewall_finalcatlistid.equals("null")||viewall_finalcatlistid.equals(null)){
                                          getFaqQuestionList("","");
                                      }
                                      else{
                                          getFaqQuestionList(search,TextUtils.join(",",viewall_finalcatlistid));
                                      }


        ViewUtil.setProfilePic(binding.imageView6);
        AppUtil.loadAppLogo(binding.auroScholarLogo,getActivity());
        setListener();


    }

    @Override
    protected void setToolbar() {
        /*Do cod ehere*/
    }

    @Override
    protected void setListener() {
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.PRIVACY_POLICY_FRAGMENT);

        binding.imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layoutSearch.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_slide_in_left));

                binding.layoutSearch.setVisibility(View.VISIBLE);
                binding.auroScholarLogo.setVisibility(View.GONE);
                binding.cardView2.setVisibility(View.GONE);
                binding.imgsearch.setVisibility(View.GONE);
            }
        });
        binding.imgcancelhint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtsearch.setText("");
                getFaqQuestionList("",faqcategoryid);
                binding.layoutSearch.setVisibility(View.GONE);
                binding.imgsearch.setVisibility(View.VISIBLE);
                binding.auroScholarLogo.setVisibility(View.VISIBLE);
                binding.cardView2.setVisibility(View.VISIBLE);
            }
        });
        binding.txtviewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openBottomSheetDialog();
            }
        });

        binding.txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = binding.txtsearch.getText().toString();

                SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
                Set<String> viewall_finalcatlistid = prefs.getStringSet("viewall_finalcatlistid",  null);


             //   SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
            //    Set<String> viewall_finalcatlistid = prefs.getStringSet("viewall_finalcatlistid", new HashSet<>());
                if (viewall_finalcatlistid==null||viewall_finalcatlistid.equals("")||viewall_finalcatlistid.equals("null")||viewall_finalcatlistid.equals(null)||viewall_finalcatlistid.size()==0){


                    getFaqQuestionList(search,"");
                }
                else{
                    getFaqQuestionList(search, TextUtils.join(",",viewall_finalcatlistid));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(true);
                SharedPreferences mySPrefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = mySPrefs.edit();
                editor.remove("viewall_finalcatlistid");
                editor.apply();
                getFaqQuestionList("","");
                getFaqCategoryList();

            }
        });


    }

    @Override
    protected int getLayout() {
        return R.layout.faq_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }
    public void setAdapterInMyClassRoom(List<FaqCatData> faqCategory) {
        binding.recyclerviewCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerviewCategories.setHasFixedSize(true);
        faqCategorySelectAdapter = new FaqCategorySelectAdapter(getActivity(),faqCategory,this);
        binding.recyclerviewCategories.setAdapter(faqCategorySelectAdapter);
    }


    void openBottomSheetDialog() {
        BottomSheetQuestionCategoryDialog bottomSheet = new BottomSheetQuestionCategoryDialog();
        bottomSheet.show(getActivity().getSupportFragmentManager(),
                "ModalBottomSheet");

    }

    @Override
    public void onResume() {
        super.onResume();
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.PRIVACY_POLICY_FRAGMENT);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }


    public  void getFaqQuestionList(String search, String cateid )
    {

        HashMap<String,String> map_data = new HashMap<>();
        String langid = AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId();
        if (cateid.endsWith(",") && !cateid.isEmpty()) {
            cateid = cateid.substring(0, cateid.length() - 1);
        }
        map_data.put("LanguageId",langid);
        map_data.put("QuestionSearch",search);
        map_data.put("CategoryId", cateid);
        RemoteApi.Companion.invoke().getAllFaqQuestion(map_data)
                .enqueue(new Callback<FaqQuestionDataModel>()
                {
                    @Override
                    public void onResponse(Call<FaqQuestionDataModel> call, Response<FaqQuestionDataModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                faqList.clear();
                                if (response.body().getStatus().equals("success")){
                                    if (!(response.body().getFaqList() == null || response.body().getFaqList().isEmpty() || response.body().getFaqList().equals("") || response.body().getFaqList().equals("null"))) {
                                        faqList = response.body().getFaqList();
                                        binding.swipeRefreshLayout.setRefreshing(false);
                                        binding.recyclerviewFaq.setVisibility(View.VISIBLE);
                                        FaqQuestionSelectAdapter adapter = new FaqQuestionSelectAdapter(getActivity(),faqList);
                                        binding.recyclerviewFaq.setAdapter(adapter);

                                    }
                                    else{
                                        binding.recyclerviewFaq.setVisibility(View.GONE);
                                    }

                                }

                            }
                            else {

                             //   Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FaqQuestionDataModel> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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
                                        binding.swipeRefreshLayout.setRefreshing(false);
                                        faqCategory = response.body().getFaqCategoryList();


                                    }
                                    setAdapterInMyClassRoom(faqCategory);
                                }


                            }
                            else {

                                //Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {

            case ITEM_CLICK:
                faqCatData = (FaqCatData) commonDataModel.getObject();
                String search = binding.txtsearch.getText().toString();
                faqcategoryid += faqCatData.getCategoryId()+",";
//                 faqcategoryid += String.valueOf(faqCatData.getCategoryId());
                String csvList2 = faqcategoryid;
                String[] items2 = csvList2.split(",");
                List<String> list2 = new ArrayList<String>();
                for(int i=0; i < items2.length; i++){
                    list2.add(items2[i]);
                }
                HashSet<String> hashSet = new HashSet<>(list2);
                list2.clear();
                list2.addAll(hashSet);
                String faqcategoryid2 = TextUtils.join(",", list2);
                categorylist = faqcategoryid2;
                getFaqQuestionList(search, faqcategoryid2);
                break;
            case ITEM_LONG_CLICK:
                faqCatData = (FaqCatData) commonDataModel.getObject();
                String searchtxt = binding.txtsearch.getText().toString();
                String faqcatid = String.valueOf(faqCatData.getCategoryId());
                String csvList = categorylist;
                String[] items = csvList.split(",");
                List<String> list = new ArrayList<String>();
                for(int i=0; i < items.length; i++){
                    list.add(items[i]);
                }
                list.remove(faqcatid);
                HashSet<String> hashSet2 = new HashSet<>(list);
                list.clear();
                list.addAll(hashSet2);
                String faqcategoryid4 = TextUtils.join(",", list);
                categorylist = faqcategoryid4;
                if (categorylist.equals("")||categorylist.isEmpty()){
                    faqcategoryid = "";
                }
                getFaqQuestionList(searchtxt, faqcategoryid4);
                break;

        }
    }
}

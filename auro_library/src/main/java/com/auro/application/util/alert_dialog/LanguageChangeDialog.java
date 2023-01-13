package com.auro.application.util.alert_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.LanguageSelectionLayoutBinding;
import com.auro.application.home.data.model.SelectLanguageModel;
import com.auro.application.home.data.model.response.LanguageListResModel;
import com.auro.application.home.data.model.response.LanguageResModel;
import com.auro.application.home.presentation.view.adapter.LanguageAdapter;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;

import java.util.ArrayList;
import java.util.List;

public class LanguageChangeDialog extends Dialog implements View.OnClickListener, CommonCallBackListner {


    public Activity context;
    private LanguageSelectionLayoutBinding binding;
    List<SelectLanguageModel> laugList;
    LanguageAdapter adapter;
    LanguageListResModel languageListResModel;
    SelectLanguageModel selectLanguageModel;

    public LanguageChangeDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    String lang = ViewUtil.getLanguage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.language_selection_layout, null, false);
        setContentView(binding.getRoot());
        setListner();
        setAdapterLanguage();
        AppStringDynamic.setLanguageChangeStrings(binding);
    }


    private void setListner() {
        binding.closeButton.setOnClickListener(this);
        binding.button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.button) {
            dismiss();
            // ViewUtil.setLanguage(lang);
            ViewUtil.setLocaleInstant(context);
            setLanguage();
        } else if (id == R.id.close_button) {
            dismiss();
        }
    }


    public void setAdapterLanguage() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        LanguageListResModel languageListResModel = prefModel.getLanguageListResModel();
        laugList = new ArrayList();
        if (languageListResModel != null && !languageListResModel.getLanguages().isEmpty()) {
            for (int i = 0; i < languageListResModel.getLanguages().size(); i++) {
                LanguageResModel languageResModel = languageListResModel.getLanguages().get(i);
                SelectLanguageModel selectLanguageModel = new SelectLanguageModel();
                selectLanguageModel.setLanguage(languageResModel.getTranslatedLanguageName());
                selectLanguageModel.setLanguageId("" + languageResModel.getLanguageId());
                selectLanguageModel.setLanguageShortCode(languageResModel.getShortCode());
                if (languageResModel.getLanguageCode().equalsIgnoreCase(ViewUtil.getLanguage())) {
                    selectLanguageModel.setCheck(true);
                } else {
                    selectLanguageModel.setCheck(false);
                }
                selectLanguageModel.setLanguageCode(languageResModel.getLanguageCode());
                laugList.add(selectLanguageModel);
            }
        } else {
            String[] listArrayLanguage = context.getResources().getStringArray(R.array.auro_languagelist);
            for (int i = 0; i < listArrayLanguage.length; i++) {
                SelectLanguageModel selectLanguageModel = new SelectLanguageModel();
                selectLanguageModel.setLanguage(listArrayLanguage[i]);
                selectLanguageModel.setCheck(false);
                if (i == 0) {
                    selectLanguageModel.setLanguageCode("en");
                } else if (i == 1) {
                    selectLanguageModel.setLanguageCode("hi");
                }
                laugList.add(selectLanguageModel);
            }
        }


        binding.recyclerViewlang.setLayoutManager(new GridLayoutManager(context, 2));
        binding.recyclerViewlang.setHasFixedSize(true);
        binding.recyclerViewlang.setNestedScrollingEnabled(false);
        adapter = new LanguageAdapter(laugList, this);
        binding.recyclerViewlang.setAdapter(adapter);
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case MESSAGE_SELECT_CLICK:
                refreshList(commonDataModel);

                break;
        }
    }

    private void refreshList(CommonDataModel commonDataModel) {
        for (int i = 0; i < laugList.size(); i++) {
            if (i == commonDataModel.getSource()) {
                laugList.get(i).setCheck(true);
                selectLanguageModel = laugList.get(i);
                lang = laugList.get(i).getLanguageCode();

            } else {
                laugList.get(i).setCheck(false);
            }
        }

        adapter.setData(laugList);
    }


    private void setLanguage() {
        try {
            if (selectLanguageModel != null) {
                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                prefModel.setUserLanguageCode(selectLanguageModel.getLanguageCode());
                prefModel.setUserLanguageShortCode(selectLanguageModel.getLanguageShortCode());
                prefModel.setUserLanguageId(selectLanguageModel.getLanguageId());
                AuroAppPref.INSTANCE.setPref(prefModel);
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // ViewUtil.setLanguage(lang);
    }

}

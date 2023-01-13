package com.auro.application.teacher.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;


import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.databinding.StateSpinnerItemBinding;
import com.auro.application.teacher.data.model.common.StateDataModel;

import java.util.List;

public class StateSpinnerAdapter extends BaseAdapter {
    StateSpinnerItemBinding binding;
    List<StateDataModel> list;

    LayoutInflater inflter;

    public StateSpinnerAdapter(List<StateDataModel> list) {
        inflter = (LayoutInflater.from(AuroApp.getAppContext().getApplicationContext()));
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.state_spinner_item, parent, false);
        binding.stateTitle.setText(list.get(position).getState_name());
        return binding.getRoot();
    }
}

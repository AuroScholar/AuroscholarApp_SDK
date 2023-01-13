package com.auro.application.payment.presentation.view.adapter;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.auro.application.home.data.model.response.StudentWalletResModel;
import com.auro.application.payment.presentation.view.fragment.BankFragment;
import com.auro.application.payment.presentation.view.fragment.PaytmFragment;
import com.auro.application.core.common.AppConstant;
import com.auro.application.home.data.model.DashboardResModel;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    StudentWalletResModel studentWalletResModel;

    public ViewPagerAdapter(Context c, FragmentManager fm, int totalTabs, StudentWalletResModel studentWalletResModel) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
        this.studentWalletResModel = studentWalletResModel;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
              //  PaytmFragment paytmFragment = new PaytmFragment();

                Bundle bundle = new Bundle();
                PaytmFragment paytmFragment = new PaytmFragment();
                bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, studentWalletResModel);
                paytmFragment.setArguments(bundle);
                return paytmFragment;
           /* case 1:
               // UPIFragment upiFragment = new UPIFragment();

                Bundle bundleupi = new Bundle();
                UPIFragment upiFragment = new UPIFragment();
                bundleupi.putParcelable(AppConstant.DASHBOARD_RES_MODEL, mdashboard);
                upiFragment.setArguments(bundleupi);
                return upiFragment;*/
            case 1:
                //BankFragment bankFragment = new BankFragment();

                Bundle bundlebank = new Bundle();
                BankFragment bankFragment = new BankFragment();
                bundlebank.putParcelable(AppConstant.DASHBOARD_RES_MODEL, studentWalletResModel);
                bankFragment.setArguments(bundlebank);
                return bankFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
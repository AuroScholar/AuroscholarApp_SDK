package com.auro.application.teacher.presentation.view.adapter;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.teacher.presentation.view.fragment.BookedSlotListFragment;
import com.auro.application.teacher.presentation.view.fragment.UpComingBookFragment;

public class ViewPagerBookingAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    DashboardResModel mdashboard;

    public ViewPagerBookingAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
        this.mdashboard = mdashboard;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //  PaytmFragment paytmFragment = new PaytmFragment();

                BookedSlotListFragment paytmFragment = new BookedSlotListFragment();
                return paytmFragment;
            case 1:
                // UPIFragment upiFragment = new UPIFragment();

                UpComingBookFragment upiFragment = new UpComingBookFragment();
                return upiFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
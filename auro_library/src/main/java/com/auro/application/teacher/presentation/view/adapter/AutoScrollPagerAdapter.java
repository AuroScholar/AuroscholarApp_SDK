package com.auro.application.teacher.presentation.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.auro.application.teacher.presentation.view.fragment.SlideFragment;
import com.auro.application.util.AppLogger;

public class AutoScrollPagerAdapter extends FragmentPagerAdapter {
    public AutoScrollPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        // Return a SlideFragment (defined as a static inner class below).
        AppLogger.v("View_pager","index of slider"+(position+1));
        return SlideFragment.newInstance(position + 1);
    }
    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}
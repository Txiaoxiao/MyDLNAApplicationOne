package com.example.asus.mydlnaapplicationone.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context context;
    List<Fragment> listFragment;

    public ViewPagerAdapter(FragmentManager fragmentManager, Context context, List<Fragment> listFragment) {
        super(fragmentManager);
        this.context = context;
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

}
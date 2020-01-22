package com.example.myruns.ui.main;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myruns.HistoryFragment;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int pos){
        return fragments.get(pos);
    }

    @Override
    public int getCount(){
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        if(position == 0)
            return "Start";
        else if(position == 1)
            return "History";
        else if(position == 2)
            return "Settings";
        else
            return null;
    }
}
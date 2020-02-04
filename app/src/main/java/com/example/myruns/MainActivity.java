package com.example.myruns;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;


import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myruns.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Fragment> fragments;
    StartFragment startFragment;
    HistoryFragment historyFragment;
    SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("manualEntry", MODE_PRIVATE);
        prefs.edit().clear().apply();

        startFragment = new StartFragment();
        historyFragment = new HistoryFragment();
        settingsFragment = new SettingsFragment();

        fragments = new ArrayList<Fragment>();
        fragments.add(startFragment);
        fragments.add(historyFragment);
        fragments.add(settingsFragment);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

}
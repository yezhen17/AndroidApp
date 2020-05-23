package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.Adapter.LogonPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogonActivity extends AppCompatActivity {

    @BindView(R.id.logonTabLayout)
    TabLayout tabLayout;

    @BindView(R.id.logonPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        ButterKnife.bind(this);

        tabLayout.addTab(tabLayout.newTab().setText("手机验证"));
        tabLayout.addTab(tabLayout.newTab().setText("基本信息"));
        tabLayout.addTab(tabLayout.newTab().setText("身份验证"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        LogonPagerAdapter pagerAdapter = new LogonPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    //按钮点击事件处理
    @OnClick(R.id.returnButton)
    public void returnToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void nextPage() {
        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition() + 1);
    }
}

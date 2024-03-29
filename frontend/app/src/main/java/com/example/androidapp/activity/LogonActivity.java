package com.example.androidapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.R;
import com.example.androidapp.adapter.LogonPagerAdapter;
import com.example.androidapp.myView.NoSlidingViewPager;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 */
public class LogonActivity extends BaseActivity {
    /******************************
     ************ 变量 ************
     ******************************/
    @BindView(R.id.logonTabLayout)
    TabLayout tabLayout;

    @BindView(R.id.logonPager)
    NoSlidingViewPager viewPager;

    /******************************
     ************ 方法 ************
     ******************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        ButterKnife.bind(this);

        tabLayout.addTab(tabLayout.newTab().setText("用户注册"));
        tabLayout.addTab(tabLayout.newTab().setText("信息完善"));
        tabLayout.addTab(tabLayout.newTab().setText("身份验证"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        LogonPagerAdapter pagerAdapter = new LogonPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setScrollable(false);
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

    public void onNextPage() {
        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition() + 1);
    }

    public void onJumpToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /******************************
     ************ 事件 ************
     ******************************/
    @OnClick(R.id.returnButton)
    public void onClickReturnToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}

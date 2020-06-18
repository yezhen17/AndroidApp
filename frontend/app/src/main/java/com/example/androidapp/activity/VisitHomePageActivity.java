package com.example.androidapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidapp.R;
import com.example.androidapp.adapter.HomepagePagerAdapter;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.entity.WholeProfile;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VisitHomePageActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.btn_return)
    ImageView btn_return;

    @BindView(R.id.btn_focus)
    Button btn_focus;

    @BindView(R.id.btn_chat)
    Button btn_chat;

    @BindView(R.id.homepage_name)
    TextView homepageName;

    ShortProfile shortProfile;
    WholeProfile wholeProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        int id = intent.getIntExtra("id", -1);
        System.out.println(id);
        if (id == -1) {
            shortProfile = intent.getParcelableExtra("profile");
        } else {

        }



        homepageName.setText(shortProfile.name);



        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .init();

        tabLayout.addTab(tabLayout.newTab().setText("个人信息"));
        tabLayout.addTab(tabLayout.newTab().setText("科研信息"));
        tabLayout.addTab(tabLayout.newTab().setText("招生信息"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        HomepagePagerAdapter pagerAdapter = new HomepagePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VisitHomePageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VisitHomePageActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

    }
}
package com.example.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.R;
import com.example.androidapp.adapter.QueryResultPageAdapter;
import com.example.androidapp.fragment.QueryResult.Apply;
import com.example.androidapp.fragment.QueryResult.IntentFragment;
import com.example.androidapp.fragment.QueryResult.ProfileFragment;
import com.example.androidapp.fragment.QueryResult.Recruit;
import com.example.androidapp.fragment.QueryResult.Student;
import com.example.androidapp.fragment.QueryResult.Teacher;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 显示查询结果界面
 */
public class QueryResultActivity extends BaseActivity {
    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.top_bar)
    Toolbar toolbar;

    QueryResultPageAdapter pagerAdapter;

    String[] tabs = {"教师", "学生", "招生意向", "报考意向"};

    boolean teacherInfo;
    boolean studentInfo;
    boolean applyInfo;
    boolean recruitInfo;

    private String query;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);

        ButterKnife.bind(this);

        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .init();

        Intent intent = getIntent();
        query = intent.getStringExtra("query");

        searchView.setIconifiedByDefault(false);
        searchView.setQuery(query, true);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query = searchView.getQuery().toString();
                queryReset();
                loadQueryInfo(viewPager.getCurrentItem());
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        for (String tab : tabs) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        pagerAdapter = new QueryResultPageAdapter(
                getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(4);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                Fragment fragment = pagerAdapter.getRegisteredFragment(position);
                if (fragment instanceof ProfileFragment)
                    ((ProfileFragment) fragment).isFilterOpen = false;
                else ((IntentFragment) fragment).isFilterOpen = false;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                loadQueryInfo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        toolbar.setNavigationOnClickListener(v -> this.finish());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    private void queryReset() {
        ((Teacher) pagerAdapter.getRegisteredFragment(0)).clearQueryResult();
        ((Student) pagerAdapter.getRegisteredFragment(1)).clearQueryResult();
        ((Recruit) pagerAdapter.getRegisteredFragment(2)).clearQueryResult();
        ((Apply) pagerAdapter.getRegisteredFragment(3)).clearQueryResult();
        teacherInfo = false;
        studentInfo = false;
        recruitInfo = false;
        applyInfo = false;
    }

    public void querySet(int position) {
        switch (position) {
            case 0: {
                teacherInfo = true;
                break;
            }
            case 1: {
                studentInfo = true;
                break;
            }
            case 2: {
                recruitInfo = true;
                break;
            }

            default: {
                applyInfo = true;
                break;
            }
        }
    }


    public void loadQueryInfo(int position) {

        switch (position) {
            case 0: {
                if (teacherInfo) return;
                Teacher teacher = (Teacher) pagerAdapter.getRegisteredFragment(0);
                teacher.loadQueryInfo(query);
                break;
            }
            case 1: {
                if (studentInfo) return;
                Student student = (Student) pagerAdapter.getRegisteredFragment(1);
                student.loadQueryInfo(query);
                break;
            }
            case 2: {
                if (recruitInfo) return;
                Recruit recruit = (Recruit) pagerAdapter.getRegisteredFragment(2);
                recruit.loadQueryInfo(query);
                break;
            }
            default: {
                if (applyInfo) return;
                Apply apply = (Apply) pagerAdapter.getRegisteredFragment(3);
                apply.loadQueryInfo(query);
                break;
            }
        }
    }


    public String getQuery() {
        return query;
    }

    public Fragment getCurrentFragment() {
        return pagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
    }

//    @OnClick(R.id.returnButton)
//    public void returnToParent() {
//        finish();
//    }
}

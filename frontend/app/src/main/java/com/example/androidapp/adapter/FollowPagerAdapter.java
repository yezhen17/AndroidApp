package com.example.androidapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidapp.fragment.follow.FollowListFragment;


/**
 * 关注粉丝界面ViewPager适配器
 */
public class FollowPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public FollowPagerAdapter(@NonNull FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FollowListFragment(true);
            case 1:
                return new FollowListFragment(false);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

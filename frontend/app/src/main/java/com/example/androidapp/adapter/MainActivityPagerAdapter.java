package com.example.androidapp.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidapp.fragment.main.ConversationFragment;
import com.example.androidapp.fragment.main.DashboardFragment;
import com.example.androidapp.fragment.main.FollowFragment;
import com.example.androidapp.fragment.main.HomeFragment;
import com.example.androidapp.fragment.main.NotificationFragment;

import org.jetbrains.annotations.NotNull;

/**
 * 主界面ViewPager适配器
 */
public class MainActivityPagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new FollowFragment();
            case 2:
                return new ConversationFragment();
            case 3:
                return new NotificationFragment();
            default:
                return new DashboardFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @NotNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}

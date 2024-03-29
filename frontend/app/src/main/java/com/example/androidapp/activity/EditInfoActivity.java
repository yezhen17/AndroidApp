package com.example.androidapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.R;
import com.example.androidapp.adapter.EditInfoPagerAdapter;
import com.example.androidapp.fragment.homepageEdit.EditApplicationInfoFragment;
import com.example.androidapp.fragment.homepageEdit.EditRecruitmentInfoFragment;
import com.example.androidapp.fragment.homepageEdit.EditSelfInfoFragment;
import com.example.androidapp.fragment.homepageEdit.EditStudyInfoFragment;
import com.example.androidapp.request.information.GetInformationRequest;
import com.example.androidapp.request.user.UpdateInfoRequest;
import com.example.androidapp.util.Hint;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 更改信息
 */
public class EditInfoActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    EditInfoPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);

        ImmersionBar.with(this).statusBarColor(R.color.transparent).init();

        tabLayout.addTab(tabLayout.newTab().setText("个人信息"));
        tabLayout.addTab(tabLayout.newTab().setText("科研信息"));
        tabLayout.addTab(tabLayout.newTab().setText("意向信息"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
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

        pagerAdapter = new EditInfoPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(3);

        toolbar.setNavigationOnClickListener(v -> this.finish());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    @OnClick(R.id.edit_info_save)
    void updateEdit() {
        Fragment first_one = pagerAdapter.getRegisteredFragment(0);
        Fragment second_one = pagerAdapter.getRegisteredFragment(1);
        Fragment third_one = pagerAdapter.getRegisteredFragment(2);

        if (second_one != null) {
            if (!((EditStudyInfoFragment) second_one).checkContent()) {
                Toast.makeText(this, "科研信息->方向不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (third_one != null) {
            if (third_one instanceof EditRecruitmentInfoFragment) {
                if (!((EditRecruitmentInfoFragment) third_one).checkContent()) {
                    Toast.makeText(this, "意向信息->方向不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                if (!((EditApplicationInfoFragment) third_one).checkContent()) {
                    Toast.makeText(this, "意向信息->方向不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        new GetInformationRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                runOnUiThread(()-> Hint.showLongCenterToast(EditInfoActivity.this, "网络异常！"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                // getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        if (first_one != null) ((EditSelfInfoFragment) first_one).update();
                        if (second_one != null) ((EditStudyInfoFragment) second_one).update();
                        if (third_one != null) {
                            if (third_one instanceof EditRecruitmentInfoFragment) {
                                ((EditRecruitmentInfoFragment) third_one).update();
                            } else {
                                ((EditApplicationInfoFragment) third_one).update();
                            }
                        }
                        finish();
                    } else {
                        runOnUiThread(()-> Hint.showLongCenterToast(EditInfoActivity.this, "网络异常！"));
                    }
                } catch (JSONException e) {
                    runOnUiThread(()-> Hint.showLongCenterToast(EditInfoActivity.this, "网络异常！"));
                }
            }
        }).send();


    }
}

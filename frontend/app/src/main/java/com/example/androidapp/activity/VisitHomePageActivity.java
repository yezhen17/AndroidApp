package com.example.androidapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.androidapp.R;
import com.example.androidapp.adapter.HomepagePagerAdapter;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.RecruitmentInfo;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.fragment.homepage.ApplicationInfoFragment;
import com.example.androidapp.fragment.homepage.RecruitmentInfoFragment;
import com.example.androidapp.fragment.homepage.SelfInfoFragment;
import com.example.androidapp.fragment.homepage.StudyInfoFragment;
import com.example.androidapp.myView.FocusButton;
import com.example.androidapp.request.follow.AddToWatchRequest;
import com.example.androidapp.request.follow.DeleteFromWatchRequest;
import com.example.androidapp.request.intention.GetApplyIntentionDetailRequest;
import com.example.androidapp.request.intention.GetApplyIntentionRequest;
import com.example.androidapp.request.intention.GetRecruitIntentionDetailRequest;
import com.example.androidapp.request.intention.GetRecruitIntentionRequest;
import com.example.androidapp.request.user.GetInfoPictureRequest;
import com.example.androidapp.request.user.GetInfoPlusRequest;
import com.example.androidapp.request.user.GetInfoRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.Hint;
import com.example.androidapp.util.MyImageLoader;
import com.example.androidapp.util.SizeConverter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 访问他人主页
 */
public class VisitHomePageActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

    @BindView(R.id.visit_homepage_title)
    TextView title;

    @BindView(R.id.homepage_name)
    TextView name;

    @BindView(R.id.signature)
    TextView signature;

    @BindView(R.id.num_focus)
    TextView numFocus;

    @BindView(R.id.num_focused)
    TextView numFocused;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.btn_focus)
    FocusButton btn_focus;

    @BindView(R.id.top_chat_btn)
    Button btn_chat;

    @BindView(R.id.top_chat_btn2)
    Button button2;

    @BindView(R.id.visit_homepage_appbar)
    AppBarLayout app_bar;

    public String mTitle;
    public String mMajor;
    public String mDegree;
    public String mGender;
    public String mAccount;
    public String mName;
    public String mSchool;
    public String mDepartment;
    public String mSignature;
    public String mPhone;
    public String mEmail;
    public String mHomepage;
    public String mAddress;
    public String mIntroduction;
    public String mUrl;
    public String mDirection;
    public String mInterest;
    public String mResult;
    public String mExperience;
    public String fanNum;
    public String followNum;
    public ArrayList<ApplicationInfo> mApplicationList;
    public ArrayList<RecruitmentInfo> mRecruitmentList;

    HomepagePagerAdapter pagerAdapter;
    ShortProfile shortProfile;
    int id;
    String type;
    boolean isFan;
    boolean isTeacher;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        shortProfile = intent.getParcelableExtra("profile");
        id = intent.getIntExtra("id", -1);
        isTeacher = intent.getBooleanExtra("isTeacher", true);
        isFan = intent.getBooleanExtra("isFan", true);

        if (isTeacher) {
            type = "T";
        } else {
            type = "S";
        }
        mApplicationList = new ArrayList<>();
        mRecruitmentList = new ArrayList<>();

        ImmersionBar.with(this).statusBarColor(R.color.transparent).init();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        tabLayout.addTab(tabLayout.newTab().setText("个人信息"));
        tabLayout.addTab(tabLayout.newTab().setText("科研信息"));
        tabLayout.addTab(tabLayout.newTab().setText("招生信息"));
        tabLayout.setBackgroundColor(Color.WHITE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        pagerAdapter = new HomepagePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), type, id);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                JCVideoPlayer.releaseAllVideos();
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
                switch (position) {
                    case 0: {
                        ((SelfInfoFragment) pagerAdapter.getRegisteredFragment(0)).setInfo();
                        break;
                    }
                    case 1: {
                        ((StudyInfoFragment) pagerAdapter.getRegisteredFragment(1)).setInfo();
                        break;
                    }
                    default: {
                        if (type.equals("S"))
                            ((ApplicationInfoFragment) pagerAdapter.getRegisteredFragment(2)).setInfo();
                        else
                            ((RecruitmentInfoFragment) pagerAdapter.getRegisteredFragment(2)).setInfo();
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        btn_chat.setOnClickListener(v -> {
            Intent intent1 = new Intent(VisitHomePageActivity.this, ChatActivity.class);
            intent1.putExtra("user", BasicInfo.ACCOUNT);
            intent1.putExtra("real_name", mName);
            intent1.putExtra("contact", mAccount);
            intent1.putExtra("contact_id", String.valueOf(id));
            intent1.putExtra("contact_type", type);
            startActivity(intent1);
        });
        button2.setOnClickListener(v -> {
            Intent intent1 = new Intent(VisitHomePageActivity.this, ChatActivity.class);
            intent1.putExtra("user", BasicInfo.ACCOUNT);
            intent1.putExtra("real_name", mName);
            intent1.putExtra("contact", mAccount);
            intent1.putExtra("contact_id", String.valueOf(id));
            intent1.putExtra("contact_type", type);
            startActivity(intent1);
        });
        btn_focus.setPressed_(isFan);
        name.setText(mAccount);

        final int alphaMaxOffset = SizeConverter.dpToPx(150);
        toolbar.getBackground().setAlpha(0);
        title.setAlpha(0);
        button2.setAlpha(1);
        app_bar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            // 设置 toolbar 背景
            if (verticalOffset > -alphaMaxOffset) {
                toolbar.getBackground().setAlpha(255 * -verticalOffset / alphaMaxOffset);
                title.setAlpha(1 * -verticalOffset / alphaMaxOffset);
                button2.setAlpha(1 * -verticalOffset / alphaMaxOffset);
            } else {
                toolbar.getBackground().setAlpha(255);
                title.setAlpha(1);
                button2.setAlpha(1);
            }
        });

        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
        });

        MyImageLoader.loadImage(imgAvatar, new GetInfoPictureRequest(type, String.valueOf(id), String.valueOf(id)).getWholeUrl());
        getInfo();
    }

    @OnClick(R.id.btn_focus)
    void watchOrUnwatch() {

        btn_focus.startLoading(() -> {
            if (isFan) {
                new DeleteFromWatchRequest(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error1", e.toString());
                        runOnUiThread(btn_focus::clickFail);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        Log.e("response", resStr);
                        try {
                            JSONObject jsonObject = new JSONObject(resStr);
                            isFan = false;
                            BasicInfo.removeFromWatchList(id, isTeacher);
                            runOnUiThread(() -> {
                                btn_focus.clickSuccess();
                                fanNum = String.valueOf(Integer.valueOf(fanNum) - 1);
                                numFocused.setText(fanNum);
                            });

                        } catch (JSONException e) {
                            Log.e("error2", e.toString());
                            runOnUiThread(btn_focus::clickFail);
                        }

                    }
                }, String.valueOf(id), isTeacher).send();
            } else {
                new AddToWatchRequest(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error1", e.toString());
                        runOnUiThread(btn_focus::clickFail);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        Log.e("response", resStr);
                        try {
                            JSONObject jsonObject = new JSONObject(resStr);
                            isFan = true;
                            BasicInfo.addToWatchList(shortProfile);
                            runOnUiThread(() -> {
                                btn_focus.clickSuccess();
                                fanNum = String.valueOf(Integer.valueOf(fanNum) + 1);
                                numFocused.setText(fanNum);
                            });


                        } catch (JSONException e) {
                            Log.e("error2", e.toString());
                            runOnUiThread(btn_focus::clickFail);
                        }

                    }
                }, String.valueOf(id), isTeacher).send();
            }
        });
    }

    private synchronized void addCounter() {
        count++;
    }

    /**
     * 获取该用户全部信息
     */
    protected void getInfo() {
        String type_ = "I";
        String teacher_id = null;
        String student_id = null;
        if (id == -1) {
            type_ = "I";
            teacher_id = null;
            student_id = null;
        } else if (type.equals("S")) {
            type_ = "S";
            teacher_id = null;
            student_id = String.valueOf(id);
        } else {
            type_ = "T";
            teacher_id = String.valueOf(id);
            student_id = null;
        }

        count = 0;

        // 获取用户名和类型
        new GetInfoRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                addCounter();
                runOnUiThread(()->Hint.showLongCenterToast(VisitHomePageActivity.this, "网络异常！"));
                finish();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {

                    JSONObject jsonObject = new JSONObject(resStr);
                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        mName = jsonObject.getString("name");
                        mAccount = jsonObject.getString("account");
                        mGender = jsonObject.getString("gender");
                        mSchool = jsonObject.getString("school");
                        mDepartment = jsonObject.getString("department");
                        if (type.equals("S")) {
                            mMajor = jsonObject.getString("major");
                            mDegree = jsonObject.getString("degree");
                        } else {
                            mTitle = jsonObject.getString("title");
                        }
                        addCounter();
                    } else {
                        String info = jsonObject.getString("info");

                        addCounter();
                        runOnUiThread(()->Hint.showLongCenterToast(VisitHomePageActivity.this, "网络异常！"));
                        finish();

                    }
                } catch (JSONException e) {
                    addCounter();
                    runOnUiThread(()->Hint.showLongCenterToast(VisitHomePageActivity.this, "网络异常！"));
                    finish();
                    Log.e("error", e.toString());
                }
            }
        }, type_, teacher_id, student_id).send();

        // 获取个性签名
        new GetInfoPlusRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                addCounter();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {

                    JSONObject jsonObject = new JSONObject(resStr);

                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        mSignature = jsonObject.getString("signature");
                        mPhone = jsonObject.getString("phone");
                        mEmail = jsonObject.getString("email");
                        mHomepage = jsonObject.getString("homepage");
                        mAddress = jsonObject.getString("address");
                        mIntroduction = jsonObject.getString("introduction");
                        if (type.equals("S")) {
                            mInterest = jsonObject.getString("research_interest");
                            mExperience = jsonObject.getString("research_experience");
                            mUrl = jsonObject.getString("promotional_video_url");
                        } else {
                            mDirection = jsonObject.getString("research_fields");
                            mResult = jsonObject.getString("research_achievements");
                            mUrl = jsonObject.getString("promotional_video_url");
                        }
                        fanNum = jsonObject.getString("fan_number");
                        followNum = jsonObject.getString("follow_number");

                        addCounter();
                        while (count != 2) {

                        }

                        runOnUiThread(() -> {
                            title.setText(mName + "的个人主页");
                            signature.setText(mSignature);
                            name.setText(mName);
                            numFocus.setText(followNum);
                            numFocused.setText(fanNum);
                            ((SelfInfoFragment) pagerAdapter.getRegisteredFragment(0)).setInfo();
                        });
                    } else {
                        String info = jsonObject.getString("info");
                        addCounter();
                        Log.e("error", info);
                    }

                } catch (JSONException e) {
                    addCounter();
                    Log.e("error", e.toString());
                }
            }
        }, type_, teacher_id, student_id).send();
        getIntentionInfo();

    }

    private void getIntentionInfo() {
        if (type.equals("T")) {
            // 获取招收意向id列表
            new GetRecruitIntentionRequest(new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    Log.e("response", resStr);
                    try {
                        JSONObject jsonObject = new JSONObject(resStr);
                        Boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            JSONArray array = jsonObject.getJSONArray("recruitment_id_list");
                            List<Integer> enrollmentIdList = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                enrollmentIdList.add(array.getInt(i));
                            }

                            // 按id获取招收意向

                            if (enrollmentIdList != null) {
                                for (int i = 0; i < enrollmentIdList.size(); i++) {
                                    new GetRecruitIntentionDetailRequest(new okhttp3.Callback() {
                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                            Log.e("error", e.toString());
                                        }

                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                            String resStr = response.body().string();
                                            Log.e("response", resStr);
                                            try {

                                                JSONObject jsonObject = new JSONObject(resStr);

                                                Boolean status = jsonObject.getBoolean("status");
                                                if (status) {
                                                    RecruitmentInfo recruitmentInfo = new RecruitmentInfo(
                                                            jsonObject.getString("research_fields"),
                                                            jsonObject.getString("recruitment_type"),
                                                            String.valueOf(jsonObject.getInt("recruitment_number")),
                                                            jsonObject.getString("intention_state"),
                                                            jsonObject.getString("introduction")
                                                    );
                                                    mRecruitmentList.add(recruitmentInfo);

                                                } else {
                                                    String info = jsonObject.getString("info");
                                                }
                                            } catch (JSONException e) {

                                            }
                                        }
                                    }, String.valueOf(enrollmentIdList.get(i))).send();
                                }
                            }

                        } else {
                            String info = jsonObject.getString("info");
                        }
                    } catch (JSONException e) {

                    }
                }
            }, String.valueOf(id)).send();
        } else {
            // 获取申请意向id列表
            new GetApplyIntentionRequest(new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    Log.e("response", resStr);
                    try {

                        JSONObject jsonObject = new JSONObject(resStr);

                        Boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            JSONArray array = jsonObject.getJSONArray("application_id_list");
                            List<Integer> applicationIdList = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                applicationIdList.add(array.getInt(i));
                            }
                            // 按id获取申请意向
                            if (applicationIdList != null) {
                                for (int i = 0; i < applicationIdList.size(); i++) {
                                    new GetApplyIntentionDetailRequest(new okhttp3.Callback() {
                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                            Log.e("error", e.toString());
                                        }

                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                            String resStr = response.body().string();
                                            Log.e("response", resStr);
                                            try {
                                                JSONObject jsonObject = new JSONObject(resStr);
                                                Boolean status = jsonObject.getBoolean("status");
                                                if (status) {
                                                    ApplicationInfo applicationInfo = new ApplicationInfo(
                                                            jsonObject.getString("research_interests"),
                                                            jsonObject.getString("intention_state"),
                                                            jsonObject.getString("introduction")
                                                    );
                                                    mApplicationList.add(applicationInfo);
                                                } else {
                                                    String info = jsonObject.getString("info");
                                                }
                                            } catch (JSONException e) {

                                            }
                                        }
                                    }, String.valueOf(applicationIdList.get(i))).send();
                                }
                            }
                        } else {
                            String info = jsonObject.getString("info");
                        }
                    } catch (JSONException e) {

                    }
                }
            }, String.valueOf(id)).send();
        }
    }
}

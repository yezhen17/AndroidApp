package com.example.androidapp.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidapp.R;
import com.example.androidapp.activity.InfoActivity;
import com.example.androidapp.activity.MainActivity;
import com.example.androidapp.activity.QueryActivity;
import com.example.androidapp.entity.chat.Dialog;
import com.example.androidapp.entity.chat.Message;
import com.example.androidapp.entity.chat.User;
import com.example.androidapp.request.information.SetInformationStateRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.DateUtil3;
import com.example.androidapp.util.LocalPicx;
import com.example.androidapp.util.MyImageLoader;
import com.kingja.loadsir.core.LoadService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class NotificationFragment extends Fragment implements DateFormatter.Formatter {

    @BindView(R.id.imageButton)
    CircleImageView drawerBtn;

    @BindView(R.id.search_view)
    EditText searchView;

    @BindView(R.id.dialogsList)
    DialogsList dialogsList;

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    @BindView(R.id.btn_all_read)
    TextView btnAllRead;

    boolean searchFreeze = false;
    private ImageLoader imageLoader;
    private DialogsListAdapter dialogsAdapter;
    private List<Integer> informationIdList;
    private ArrayList<Dialog> dialogs;
    private ArrayList<Message> messages;
    private LoadService loadService;
    private User user;
    private Unbinder unbinder;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mTimeCounterRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("消息列表轮询", "+1");
            refreshData();
            mHandler.postDelayed(this, 5 * 1000);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, root);
        Log.d("Life", "oncreateview");

        //设置头像
        imageLoader = (imageView, url, payload) -> MyImageLoader.loadImage(imageView, url);
        dialogsAdapter = new DialogsListAdapter<>(imageLoader);

        user = new User("0", "", "xz", false);

        dialogs = new ArrayList<>();
        dialogsAdapter.setItems(dialogs);
        dialogsAdapter.setDatesFormatter(this);

        dialogsList.setAdapter(dialogsAdapter);

        refreshLayout.setOnRefreshListener(refreshlayout -> {
            refreshData();
            refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
        });

        drawerBtn.setOnClickListener(v -> {
            MainActivity parentActivity = (MainActivity) getActivity();
            parentActivity.openDrawer();
        });
        searchView.setOnClickListener(v -> {
            if (!searchFreeze) {
                searchFreeze = true;
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    } finally {
                        searchFreeze = false;
                    }
                }).start();
                Intent intent = new Intent(getActivity(), QueryActivity.class);
                startActivity(intent);
            }
        });

        btnAllRead.setOnClickListener(v -> {
            for (int i = 0; i < dialogs.size(); i++) {
                Dialog dialog = dialogs.get(i);
                // 修改消息状态
                new SetInformationStateRequest(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                        getActivity().runOnUiThread(() -> {
                            dialog.setUnreadCount(0);
                            dialog.getLastMessage().setRead();
                            dialogsAdapter.notifyDataSetChanged();
                        });
                    }
                }, dialogs.get(i).getLastMessage().getId(), "R").send();

            }
        });

        dialogsAdapter.setOnDialogClickListener(dialog -> {

            // 修改消息状态
            new SetInformationStateRequest(new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    getActivity().runOnUiThread(() -> Log.e("e", "更新消息状态失败"));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    getActivity().runOnUiThread(() -> {
                        Log.e("s", "更新消息状态成功");
                        Dialog d = (Dialog) dialog;
                        d.setUnreadCount(0);
                        d.getLastMessage().setRead();
                        dialogsAdapter.notifyDataSetChanged();
                    });
                }
            }, dialog.getLastMessage().getId(), "R").send();

            // 进入页面
            Intent intent = new Intent(getActivity(), InfoActivity.class);
            intent.putExtra("text", dialog.getLastMessage().getText());
            intent.putExtra("dateString", ((Message) dialog.getLastMessage()).getDateString());
            startActivity(intent);
        });
        getAvatar();
        return root;
    }

    public void getAvatar() {
        MyImageLoader.loadImage(drawerBtn);
    }

    private void refreshData() {
        System.out.println(LocalPicx.NOTIFICATION_PASSWORD_CHANGE);
        dialogs.clear();
        if (!BasicInfo.WELCOME_NOTIFICATIONS.isEmpty()) {
            int size = BasicInfo.WELCOME_NOTIFICATIONS.size() - 1;
            int count = 0;
            Message m = BasicInfo.WELCOME_NOTIFICATIONS.get(size);
            while (size >= 0) {
                if (BasicInfo.WELCOME_NOTIFICATIONS.get(size).isRead()) break;
                else count++;
                size--;
            }
            dialogs.add(new Dialog("1", "小管家",
                    LocalPicx.NOTIFICATION_WELCOME,
                    new ArrayList<>(Arrays.asList(user)), m, count));
        }
        if (!BasicInfo.FOLLOW_NOTIFICATIONS.isEmpty()) {
            int size = BasicInfo.FOLLOW_NOTIFICATIONS.size() - 1;
            int count = 0;
            Message m = BasicInfo.FOLLOW_NOTIFICATIONS.get(size);
            while (size >= 0) {
                if (BasicInfo.FOLLOW_NOTIFICATIONS.get(size).isRead()) break;
                else count++;
                size--;
            }
            dialogs.add(new Dialog("1", "新关注提醒",
                    LocalPicx.NOTIFICATION_WATCH,
                    new ArrayList<>(Arrays.asList(user)), m, count));
        }
        if (!BasicInfo.INTENTION_NOTIFICATIONS.isEmpty()) {
            int size = BasicInfo.INTENTION_NOTIFICATIONS.size() - 1;
            int count = 0;
            Message m = BasicInfo.INTENTION_NOTIFICATIONS.get(size);
            while (size >= 0) {
                if (BasicInfo.INTENTION_NOTIFICATIONS.get(size).isRead()) break;
                else count++;
                size--;
            }
            dialogs.add(new Dialog("1", "意向变更",
                    LocalPicx.NOTIFICATION_INTENTION,
                    new ArrayList<>(Arrays.asList(user)), m, count));
        }
        if (!BasicInfo.PWD_CHANGE_NOTIFICATIONS.isEmpty()) {
            int size = BasicInfo.PWD_CHANGE_NOTIFICATIONS.size() - 1;
            int count = 0;
            Message m = BasicInfo.PWD_CHANGE_NOTIFICATIONS.get(size);
            while (size >= 0) {
                if (BasicInfo.PWD_CHANGE_NOTIFICATIONS.get(size).isRead()) break;
                else count++;
                size--;
            }
            dialogs.add(new Dialog("1", "密码更新",
                    LocalPicx.NOTIFICATION_PASSWORD_CHANGE,
                    new ArrayList<>(Arrays.asList(user)), m, count));
        }
        Collections.sort(dialogs, (p1, p2) -> p2.getLastMessage().getCreatedAt().compareTo(p1.getLastMessage().getCreatedAt()));
        dialogsAdapter.notifyDataSetChanged();
    }

    @Override
    public String format(Date date) {
        try {
            String s = DateUtil3.formatDate(date);
            return s;
        } catch (ParseException e) {
            return "";
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTimeCounterRunnable.run();
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mTimeCounterRunnable);
        super.onDestroy();
        unbinder.unbind();
    }

}
package com.example.androidapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.androidapp.UI.home.HomeFragment;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.request.recommend.RecommendFitRequest;
import com.example.androidapp.request.recommend.RecommendFitStudentRequest;
import com.example.androidapp.request.recommend.RecommendFitTeacherRequest;
import com.example.androidapp.request.recommend.RecommendHotRequest;
import com.example.androidapp.request.recommend.RecommendRandomRequest;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends ProfileListFragment {

    boolean isTeacher;



    public RecommendFragment(int num) {
        if (num == 0) isTeacher = true;
        else isTeacher = false;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
//        refreshLayout = ((HomeFragment) getParentFragment()).refreshLayout;
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            getRecommendList(true);

        });
        return root;

    }

    private void getRecommendList(boolean isRefresh) {
        final int[] receiveCount = {0};

        new RecommendRandomRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (receiveCount[0] == 2) refreshLayout.finishRefresh(false);
                receiveCount[0]++;
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    if (isTeacher) jsonArray = (JSONArray) jsonObject.get("teacher_info_list");
                    else jsonArray = (JSONArray) jsonObject.get("student_info_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        new ShortProfile(jsonArray.getJSONObject(i), isTeacher);
                    }
                    if (receiveCount[0] == 2) refreshLayout.finishRefresh(true);
                    receiveCount[0]++;
                } catch (JSONException e) {
                    if (receiveCount[0] == 2) refreshLayout.finishRefresh(false);
                    receiveCount[0]++;
                    Log.e("error", e.toString());
                }
            }
        }, isTeacher).send();

        new RecommendHotRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                if (receiveCount[0] == 2) refreshLayout.finishRefresh(false);
                receiveCount[0]++;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    if (isTeacher) jsonArray = (JSONArray) jsonObject.get("teacher_info_list");
                    else jsonArray = (JSONArray) jsonObject.get("student_info_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        new ShortProfile(jsonArray.getJSONObject(i), isTeacher);
                    }
                    if (receiveCount[0] == 2) refreshLayout.finishRefresh(true);
                    receiveCount[0]++;
                } catch (JSONException e) {
                    Log.e("error", e.toString());
                    if (receiveCount[0] == 2) refreshLayout.finishRefresh(false);
                    receiveCount[0]++;
                }
            }
        }, isTeacher).send();

        new RecommendFitRequest(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (receiveCount[0] == 2) refreshLayout.finishRefresh(false);
                receiveCount[0]++;
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    if (isTeacher) jsonArray = (JSONArray) jsonObject.get("teacher_info_list");
                    else jsonArray = (JSONArray) jsonObject.get("student_info_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        new ShortProfile(jsonArray.getJSONObject(i), isTeacher);
                    }
                    if (receiveCount[0] == 2) refreshLayout.finishRefresh(true);
                    receiveCount[0]++;
                } catch (JSONException e) {
                    Log.e("error", e.toString());
                    if (receiveCount[0] == 2) refreshLayout.finishRefresh(false);
                    receiveCount[0]++;
                }
            }
        }, isTeacher).send();
    }
}

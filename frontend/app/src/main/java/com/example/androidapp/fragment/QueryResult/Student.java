package com.example.androidapp.fragment.QueryResult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidapp.activity.QueryResultActivity;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.request.search.SearchStudentRequest;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 搜索结果（学生）
 */
public class Student extends ProfileFragment {

    public Student() {
        order = new String[]{"最相关（默认）", "关注人数最多"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        return root;
    }

    public void loadQueryInfo(String query) {
        LoadService loadService = LoadSir.getDefault().register(recyclerView, (Callback.OnReloadListener) v -> {

        });
        new SearchStudentRequest(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                loadService.showSuccess();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    JSONArray jsonArray;
                    jsonArray = (JSONArray) jsonObject.get("student_info_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShortProfile shortProfile = new ShortProfile(jsonArray.getJSONObject(i), false);
                        Log.e("..", "! " + shortProfile.isFan + shortProfile.id + " " + shortProfile.name);
                        addProfileItem(false, shortProfile);
                    }
                    adjustList();
                    loadService.showSuccess();
                    ((QueryResultActivity) getActivity()).querySet(1);
                } catch (JSONException e) {
                    loadService.showSuccess();
                }
            }
        }, query).send();
    }

    public void clearQueryResult() {
        int size = mProfileList.size();
        mProfileList.clear();
        mShortProfileAdapter.notifyItemRangeRemoved(0, size);
    }


}

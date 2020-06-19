package com.example.androidapp.fragment.HomepageEdit;

import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Update;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.androidapp.R;
import com.example.androidapp.adapter.ApplicationListAdapter;
import com.example.androidapp.adapter.EditApplicationListAdapter;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.EnrollmentInfo;
import com.example.androidapp.request.intention.ClearAllIntentionRequest;
import com.example.androidapp.request.intention.CreateApplyIntentionRequest;
import com.example.androidapp.request.intention.CreateRecruitIntentionRequest;
import com.example.androidapp.request.intention.DeleteApplyIntentionRequest;
import com.example.androidapp.request.intention.GetApplyIntentionDetailRequest;
import com.example.androidapp.request.intention.GetApplyIntentionRequest;
import com.example.androidapp.request.intention.UpdateApplyIntentionRequest;
import com.example.androidapp.util.BasicInfo;
import com.example.androidapp.util.OptionItems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class EditApplicationInfoFragment extends Fragment implements View.OnClickListener {

  @BindView(R.id.btn_add)
  FloatingActionButton btn_add;

  @BindView(R.id.btn_concern)
  Button btn_concern;

  RecyclerView recyclerView;
  EditApplicationListAdapter adapter;

  ArrayList<ApplicationInfo> mApplicationList;

  private List<Integer> applicationIdList;

  private Unbinder unbinder;

  //To do
  public EditApplicationInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_edit_intention_info, container, false);
    unbinder = ButterKnife.bind(this,view);

    recyclerView = view.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    mApplicationList = new ArrayList<>();
    adapter = new EditApplicationListAdapter(mApplicationList, getContext());//初始化NameAdapter
    adapter.setRecyclerManager(recyclerView);//设置RecyclerView特性

    // 显示目前申请意向

    // 获取申请意向id列表
    new GetApplyIntentionRequest(new okhttp3.Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.e("error", e.toString());
      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        String resStr = response.body().string();
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
        Log.e("response", resStr);
        try {
          // 解析json，然后进行自己的内部逻辑处理
          JSONObject jsonObject = new JSONObject(resStr);

          Boolean status = jsonObject.getBoolean("status");
          if(status){
            JSONArray array = jsonObject.getJSONArray("application_id_list");
            applicationIdList = new ArrayList<>();
            for (int i=0;i<array.length();i++){
              applicationIdList.add(array.getInt(i));
            }

            // 按id获取申请意向
            if(applicationIdList!=null){
              for(int i=0;i<applicationIdList.size();i++){
                new GetApplyIntentionDetailRequest(new okhttp3.Callback() {
                  @Override
                  public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                  }

                  @Override
                  public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
                    Log.e("response", resStr);
                    try {
                      // 解析json，然后进行自己的内部逻辑处理
                      JSONObject jsonObject = new JSONObject(resStr);

                      Boolean status = jsonObject.getBoolean("status");
                      if(status){
                        ApplicationInfo applicationInfo = new ApplicationInfo(
                                jsonObject.getString("research_interests"),
                                jsonObject.getString("intention_state"),
                                jsonObject.getString("introduction")
                        );

                        getActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                            mApplicationList.add(applicationInfo);
                            adapter.notifyDataSetChanged();
                          }
                        });

                      }else{
                        String info = jsonObject.getString("info");
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                      }
                    } catch (JSONException e) {

                    }
                  }
                },String.valueOf(applicationIdList.get(i))).send();
              }
            }


          }else{
            String info = jsonObject.getString("info");
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
          }
        } catch (JSONException e) {

        }
      }
    },String.valueOf(BasicInfo.ID)).send();



    adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        ImageView imageView = (ImageView)view;
        mApplicationList.remove(i);
        adapter.notifyDataSetChanged();
      }
    });

    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
 //       Toast.makeText(getActivity(), "testItemClick " + i, Toast.LENGTH_SHORT).show();
      }
    });

    btn_add.setOnClickListener(this);

    btn_concern.setOnClickListener(this);

    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.btn_concern:
      {

        new ClearAllIntentionRequest(new okhttp3.Callback() {
          @Override
          public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("response", "FA");
          }

          @Override
          public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String resStr = response.body().string();
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
            Log.e("response", resStr);
            try {
              // 解析json，然后进行自己的内部逻辑处理
              JSONObject jsonObject = new JSONObject(resStr);
              Boolean status = jsonObject.getBoolean("status");
              String info = jsonObject.getString("info");
              getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());

              // 全部删除以后再插入
              for(int i=0;i<mApplicationList.size();i++){
                ApplicationInfo applicationInfo = mApplicationList.get(i);

                new CreateApplyIntentionRequest(new okhttp3.Callback() {
                  @Override
                  public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("response", "res");
                  }

                  @Override
                  public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show());
                    Log.e("response", resStr);
                    try {
                      // 解析json，然后进行自己的内部逻辑处理
                      JSONObject jsonObject = new JSONObject(resStr);
                      Boolean status = jsonObject.getBoolean("status");
                      String info = jsonObject.getString("info");
                      getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),info, Toast.LENGTH_LONG).show());
                    } catch (JSONException e) {
                    }
                  }
                },
                        applicationInfo.direction,
                        applicationInfo.profile,
                        applicationInfo.state,
                        null).send();
              }

            } catch (JSONException e) {
            }
          }
        }).send();
        break;
      }
      case R.id.btn_add:
      {
        if(mApplicationList.size()>= BasicInfo.MAX_INTENTION_NUMBER){
          Toast.makeText(getContext(),"已达到意向数量上限",Toast.LENGTH_SHORT).show();
          break;
        }
        Toast.makeText(getActivity(),"添加",Toast.LENGTH_SHORT).show();
        ApplicationInfo applicationInfo = new ApplicationInfo("","进行","");
        mApplicationList.add(applicationInfo);
        adapter.notifyDataSetChanged();
        break;
      }
    }

  }
}

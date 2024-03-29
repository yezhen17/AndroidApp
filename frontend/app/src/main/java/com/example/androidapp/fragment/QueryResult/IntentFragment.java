package com.example.androidapp.fragment.QueryResult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.activity.VisitHomePageActivity;
import com.example.androidapp.adapter.ShortIntentAdapter;
import com.example.androidapp.entity.ShortIntent;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.myView.SelectList;
import com.example.androidapp.util.BasicInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 搜索结果（意向）
 */
public class IntentFragment extends Fragment {

    public boolean isFilterOpen = false;
    @BindView(R.id.orderSpinner)
    protected Spinner orderSpinner;
    @BindView(R.id.selectText)
    protected TextView selectText;
    protected ArrayAdapter<String> spinnerAdapter;
    @BindView(R.id.recycleView)
    protected RecyclerView recyclerView;

    protected ShortIntentAdapter mShortIntentAdapter;
    protected ArrayList<ShortIntent> mIntentList;
    protected ArrayList<ShortIntent> filteredIntentList;
    protected Unbinder unbinder;
    protected String[] order;
    protected int current_order = 0;


    SelectList selectList;
    boolean[] filters = new boolean[]{false, false, false, false};
    private Lock lock = new ReentrantLock();
    private String test_url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592237104788&di=da06c7ee8d8256243940b53531bdeba7&imgtype=0&src=http%3A%2F%2Ftupian.qqjay.com%2Ftou2%2F2018%2F1106%2F60bdf5b88754650e51ccee32bb6ac8ae.jpg";

    public IntentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teacher_result, container, false);
        unbinder = ButterKnife.bind(this, root);
        initViews();

        mIntentList = new ArrayList<>();
        filteredIntentList = new ArrayList<>();
        mShortIntentAdapter = new ShortIntentAdapter(mIntentList, getContext());//初始化NameAdapter
        mShortIntentAdapter.setRecyclerManager(recyclerView);//设置RecyclerView特性

        mShortIntentAdapter.setOnItemClickListener((adapter, view, position) -> {
            visitHomePage(position);
        });

        return root;
    }

    protected void initViews() {
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, order);
        //下拉的样式res
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        orderSpinner.setAdapter(spinnerAdapter);
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                current_order = pos;
                adjustList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.selectText)
    public void openSelectWindow() {
        if (isFilterOpen) {
            if (selectList != null) selectList.dismiss();
        } else {
            isFilterOpen = true;
            selectList = new SelectList(getContext());
            selectList.showPopupWindow(orderSpinner);

        }
    }

    public void setFilterClosed() {
        this.isFilterOpen = false;
    }

    public boolean[] getFilters() {
        return filters;
    }

    void addIntentItem(boolean isRefresh, ShortIntent shortIntent) {
        if (recyclerView.isComputingLayout()) {
            Log.e("errorrecyclerview", "ohno");
            recyclerView.post(() -> {
                if (isRefresh) {
                    for (ShortIntent tmp : mIntentList) {
                        if (tmp.id == shortIntent.id) return;
                    }
                    mIntentList.remove(0);
                    mShortIntentAdapter.notifyItemRemoved(0);
                }
                if (shortIntent.id == BasicInfo.ID && shortIntent.isTeacher == BasicInfo.IS_TEACHER)
                    return; // 如果是自己，筛去
                lock.lock();
                mIntentList.add(shortIntent);
                lock.unlock();
            });


        } else {
            if (isRefresh) {
                for (ShortIntent tmp : mIntentList) {
                    if (tmp.id == shortIntent.id) return;
                }
                mIntentList.remove(0);
                mShortIntentAdapter.notifyItemRemoved(0);
            }
            if (shortIntent.id == BasicInfo.ID && shortIntent.isTeacher == BasicInfo.IS_TEACHER)
                return; // 如果是自己，筛去
            lock.lock();
            mIntentList.add(shortIntent);
            lock.unlock();
        }
    }

    private void visitHomePage(int position) {
        Intent intent = new Intent(getContext(), VisitHomePageActivity.class);
        ShortIntent shortIntent = mIntentList.get(position);
        intent.putExtra("id", shortIntent.id);
        intent.putExtra("isTeacher", shortIntent.isTeacher);
        intent.putExtra("isFan", shortIntent.isFan);
        intent.putExtra("profile", new ShortProfile(shortIntent));
        startActivity(intent);
    }


    public void adjustList() {
        int i = 0;
        ArrayList<Integer> removed = new ArrayList<>();
        mIntentList.addAll(filteredIntentList);
        filteredIntentList.clear();
        for (ShortIntent ShortIntent : mIntentList) {
            if (filters[0] && !ShortIntent.intentionState.equals("O")) {
                removed.add(i);
            }
            i++;
        }
        for (int j = removed.size() - 1; j >= 0; j--) {
            int idx = removed.get(j);
            filteredIntentList.add(mIntentList.get(idx));
            mIntentList.remove(idx);
            mShortIntentAdapter.notifyItemRemoved(idx);
        }

        if (current_order == 0) {
            Collections.sort(mIntentList, (p1, p2) -> Integer.valueOf(p2.relate).compareTo(Integer.valueOf(p1.relate)));
        }
        mShortIntentAdapter.notifyItemRangeChanged(0, mIntentList.size());

    }

    public void filterResult(boolean[] filters) {
        int i = 0;
        for (Boolean filter : filters) {
            this.filters[i] = filter;
            i++;
        }
        adjustList();
    }
}


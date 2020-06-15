package com.example.androidapp.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.component.FocusButton;
import com.example.androidapp.entity.ShortProfile;
import com.example.androidapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ShortProfileAdapter<T> extends MyBaseAdapter {

    private CircleImageView mHead;
    private TextView mName;
    private TextView mAffiliation;
    private FocusButton mWatchBtn;

    public ShortProfileAdapter(List<T> data, Context context){
        super(R.layout.layout_profile_row, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {
        // 这里大多数情况应该不需要初始化
//        mName = viewHolder.getView(R.id.name);
//        mHead = viewHolder.getView(R.id.profile_image);
//        mAffiliation = viewHolder.getView(R.id.affiliation);

        //
    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {
        // 在这里链式赋值就可以了
        ShortProfile data = (ShortProfile) o;
        viewHolder.setText(R.id.name, data.name)
                .setText(R.id.info, data.affiliation + "  " + data.fanNum + "人关注");
        mHead = viewHolder.getView(R.id.profile_image);
        try {
            Picasso.with(mHead.getContext()).load(data.url).placeholder(R.drawable.ic_person_outline_black_24dp).into(mHead);
        } catch (Exception e) {
            System.out.println(e);
        }

        mWatchBtn = viewHolder.getView(R.id.watch_btn);
        //mWatchBtn.setPressed_(true);
    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {
        // 注册按钮点击事件
        viewHolder.addOnClickListener(R.id.watch_btn);
    }
}
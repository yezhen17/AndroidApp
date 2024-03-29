package com.example.androidapp.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.entity.RecruitmentInfo;

import java.util.List;


/**
 * 招生意向列表适配器
 */
public class RecruitmentListAdapter<T> extends MyBaseListAdapter {

    public RecruitmentListAdapter(List<T> data, Context context) {
        super(R.layout.item_recruitment_info, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {
    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {

        RecruitmentInfo data = (RecruitmentInfo) o;
        viewHolder.setText(R.id.direction, data.direction)
                .setText(R.id.number, data.number)
                .setText(R.id.introduction, data.introduction);
        if (data.studentType.equals("UG")) {
            viewHolder.setText(R.id.student_type, "本科生");
        } else if (data.studentType.equals("MT")) {
            viewHolder.setText(R.id.student_type, "硕士生");
        } else if (data.studentType.equals("DT")) {
            viewHolder.setText(R.id.student_type, "博士生");
        }
        if (data.state.equals("O")) {
            viewHolder.setText(R.id.state, "进行");
        } else if (data.state.equals("S")) {
            viewHolder.setText(R.id.state, "成功");
        } else if (data.state.equals("F")) {
            viewHolder.setText(R.id.state, "失败");
        }

    }

    @Override
    protected void setListener(BaseViewHolder viewHolder, Object o) {

    }
}


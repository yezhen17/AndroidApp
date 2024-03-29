package com.example.androidapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidapp.R;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.OptionItems;

import java.util.List;

/**
 * 更改申请意向列表适配器
 */
public class EditApplicationListAdapter<T> extends MyBaseListAdapter {

    public EditApplicationListAdapter(List<T> data, Context context) {
        super(R.layout.item_edit_application_info, data, context);
    }

    @Override
    protected void initView(BaseViewHolder viewHolder, Object o) {

    }

    @Override
    protected void initData(BaseViewHolder viewHolder, Object o) {

        ApplicationInfo data = (ApplicationInfo) o;
        viewHolder.setText(R.id.direction, data.direction)
                .setText(R.id.state, data.state)
                .setText(R.id.profile, data.profile);
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
        viewHolder.addOnClickListener(R.id.state);
        viewHolder.addOnClickListener(R.id.delete);
        ApplicationInfo data = (ApplicationInfo) o;

        FormEditText direction = viewHolder.getView(R.id.direction);
        direction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.direction = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        FormEditText profile = viewHolder.getView(R.id.profile);
        profile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.profile = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        TextView state = viewHolder.getView(R.id.state);
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                OptionsPickerView pvOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        String s = OptionItems.optionsState.get(options1);
                        state.setText(s);
                        if (s.equals("进行")) {
                            data.state = "O";
                        } else if (s.equals("成功")) {
                            data.state = "S";
                        } else if (s.equals("失败")) {
                            data.state = "F";
                        }
                        profile.requestFocus();
                    }
                })
                        .setTitleText("选择状态")
                        .setSubmitText("确定")
                        .setCancelText("取消")
                        .setContentTextSize(20)
                        .setDividerColor(Color.GRAY)
                        .setSelectOptions(0)
                        .build();
                pvOptions.setPicker(OptionItems.optionsState);
                pvOptions.show();
            }
        });
    }
}

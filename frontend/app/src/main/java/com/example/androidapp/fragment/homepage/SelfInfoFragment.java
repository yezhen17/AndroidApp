package com.example.androidapp.fragment.homepage;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidapp.R;
import com.example.androidapp.activity.MainActivity;
import com.example.androidapp.activity.VisitHomePageActivity;
import com.example.androidapp.util.BasicInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 展示信息界面1
 */
public class SelfInfoFragment extends Fragment {

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.account)
    TextView account;

    @BindView(R.id.layout_username)
    LinearLayout layoutAccount;

    @BindView(R.id.gender)
    TextView gender;

    @BindView(R.id.school)
    TextView school;

    @BindView(R.id.department)
    TextView department;

    @BindView(R.id.layout_title)
    LinearLayout layoutTitle;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.layout_major)
    LinearLayout layoutMajor;

    @BindView(R.id.major)
    TextView major;

    @BindView(R.id.layout_degree)
    LinearLayout layoutDegree;

    @BindView(R.id.degree)
    TextView degree;

    @BindView(R.id.layout_teacher_number)
    LinearLayout layoutTeacherNumber;

    @BindView(R.id.teacher_number)
    TextView teacherNumber;

    @BindView(R.id.layout_student_number)
    LinearLayout layoutStudentNumber;

    @BindView(R.id.student_number)
    TextView studentNumber;

    @BindView(R.id.layout_id_number)
    LinearLayout layoutIdNumber;

    @BindView(R.id.id_number)
    TextView idNumber;

    @BindView(R.id.phone)
    TextView phone;

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.homepage)
    TextView homepage;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.introduction)
    TextView introduction;

    private Unbinder unbinder;
    private String type;
    private int id;

    public SelfInfoFragment(String type, int id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof MainActivity) setInfo();
    }

    public void setInfo() {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            account.setText(BasicInfo.ACCOUNT);
            name.setText(BasicInfo.mName);
            if (BasicInfo.mGender.equals("M")) {
                gender.setText("男");
            } else if (BasicInfo.mGender.equals("F")) {
                gender.setText("女");
            } else {
                gender.setText("保密");
            }
            school.setText(BasicInfo.mSchool);
            department.setText(BasicInfo.mDepartment);
            if (type.equals("T")) {
                if (BasicInfo.mTitle.equals("TA")) {
                    title.setText("助理");
                } else if (BasicInfo.mTitle.equals("LT")) {
                    title.setText("讲师");
                } else if (BasicInfo.mTitle.equals("AP")) {
                    title.setText("助理教授");
                } else if (BasicInfo.mTitle.equals("PP")) {
                    title.setText("教授");
                }
                layoutMajor.setVisibility(View.GONE);
                layoutDegree.setVisibility(View.GONE);
                layoutStudentNumber.setVisibility(View.GONE);
            } else {
                major.setText(BasicInfo.mMajor);

                if (BasicInfo.mDegree.equals("UG")) {
                    degree.setText("本科生");
                } else if (BasicInfo.mDegree.equals("MT")) {
                    degree.setText("硕士生");
                } else {
                    degree.setText("博士生");
                }

                layoutTitle.setVisibility(View.GONE);
                layoutTeacherNumber.setVisibility(View.GONE);
            }
            if (type.equals("T")) {
                teacherNumber.setText(BasicInfo.mTeacherNumber);
            } else {
                studentNumber.setText(BasicInfo.mStudentNumber);
            }

            idNumber.setText(BasicInfo.mIdNumber);
            phone.setText(BasicInfo.mPhone);
            email.setText(BasicInfo.mEmail);
            homepage.setText(BasicInfo.mHomepage);
            address.setText(BasicInfo.mAddress);
            introduction.setText(BasicInfo.mIntroduction);
        } else {
            VisitHomePageActivity activity_ = (VisitHomePageActivity) activity;
            layoutAccount.setVisibility(View.GONE);
            name.setText(activity_.mName);
            if (activity_.mGender.equals("M")) {
                gender.setText("男");
            } else if (activity_.mGender.equals("F")) {
                gender.setText("女");
            } else {
                gender.setText("保密");
            }
            school.setText(activity_.mSchool);
            department.setText(activity_.mDepartment);
            if (type.equals("T")) {
                if (activity_.mTitle.equals("TA")) {
                    title.setText("助理");
                } else if (activity_.mTitle.equals("LT")) {
                    title.setText("讲师");
                } else if (activity_.mTitle.equals("AP")) {
                    title.setText("助理教授");
                } else if (activity_.mTitle.equals("PP")) {
                    title.setText("教授");
                }
                layoutMajor.setVisibility(View.GONE);
                layoutDegree.setVisibility(View.GONE);
                layoutStudentNumber.setVisibility(View.GONE);
            } else {
                major.setText(activity_.mMajor);

                if (activity_.mDegree.equals("UG")) {
                    degree.setText("本科生");
                } else if (activity_.mDegree.equals("MT")) {
                    degree.setText("硕士生");
                } else {
                    degree.setText("博士生");
                }
                layoutTitle.setVisibility(View.GONE);
                layoutTeacherNumber.setVisibility(View.GONE);
            }
            teacherNumber.setVisibility(View.GONE);
            studentNumber.setVisibility(View.GONE);
            idNumber.setVisibility(View.GONE);

            phone.setText(activity_.mPhone);
            email.setText(activity_.mEmail);
            homepage.setText(activity_.mHomepage);
            address.setText(activity_.mAddress);
            introduction.setText(activity_.mIntroduction);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}


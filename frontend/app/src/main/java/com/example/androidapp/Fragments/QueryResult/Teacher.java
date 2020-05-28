package com.example.androidapp.Fragments.QueryResult;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.androidapp.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Teacher extends Fragment {
    private Unbinder unbinder;

    public Teacher() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_result, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

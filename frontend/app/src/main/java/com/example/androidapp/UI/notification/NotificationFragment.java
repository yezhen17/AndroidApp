package com.example.androidapp.UI.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.androidapp.R;
import com.google.android.material.tabs.TabLayout;

public class NotificationFragment extends Fragment {

  private NotificationViewModel notificationViewModel;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_notification, container, false);

//    notificationViewModel =
//            ViewModelProviders.of(this).get(NotificationViewModel.class);
//    final TextView textView = root.findViewById(R.id.text_query);
//    notificationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//      @Override
//      public void onChanged(@Nullable String s) {
//        textView.setText(s);
//      }
//    });


    return root;
  }
}

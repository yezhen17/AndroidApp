package com.example.androidapp.request;

import com.example.androidapp.util.Http;
import java.util.HashMap;
import okhttp3.Callback;

public class BasePostRequest {
    private String url;
    private HashMap<String, String> param;
    private Callback callback;

    public BasePostRequest() {
        param = new HashMap<>();
    }

    public void send() {
        Http.sendOkHttpPostRequest(url, param, callback);
    }

}
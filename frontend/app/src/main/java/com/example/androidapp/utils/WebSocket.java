package com.example.androidapp.utils;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.androidapp.MainActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

public class WebSocket {

    private static int RE_TIME = 5;  // 发起重连的时间

    private static WebSocketClient socketClient = null;

    private static String SOCKET_URL = "ws://34.238.156.247:8080/websocket/888888";

    public static void initSocket() {
        try {
            URI u = new URI(SOCKET_URL);
            socketClient = new WebSocketClient(u) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("Socket", "成功连接！");
                }

                @Override
                public void onMessage(String message) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = message;
                    MainActivity.msgHandler.sendMessage(msg);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    socketClient = null;
                    if (code != 1000)   // 1000为正常关闭，不是意外关闭
                        WebSocket.reconnect();
                }

                @Override
                public void onError(Exception ex) {
                    socketClient = null;
                }
            };
            socketClient.connect();
        } catch (Exception e) {

        }
    }

    public static void reconnect() {
        new Thread(() -> {
            try {
                while (socketClient == null || !socketClient.isOpen()) {
                    initSocket();
                    Thread.sleep(RE_TIME * 1000);
                    Log.e("socket", "服务器连接错误！" + RE_TIME + "秒后重连。");
                }
            } catch (Exception e) {

            }
        }).start();
    }

    public static boolean send(JSONObject msg) {
        if (socketClient.isOpen()) {
            Log.e("send msg", msg.toString());
            socketClient.send(msg.toString());
            return true;
        } else {
            return false;
        }
    }
}
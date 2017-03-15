package com.gn.cb.coolweather.util;


import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 用于请求网络
 * Created by CBJ on 2017/3/10.
 */

public class HttpUnit {

    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}

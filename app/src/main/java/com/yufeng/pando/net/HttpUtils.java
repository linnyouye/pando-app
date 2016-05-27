package com.yufeng.pando.net;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lin Youye on 2016/4/9.
 */
public class HttpUtils {

    private static final String TAG = "lyy-HttpUtils";

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient mHttpClient;
    private static Handler mHandler;

    private HttpUtils() {

    }

    public static void init() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        if (mHttpClient == null) {
            mHttpClient = new OkHttpClient();
            mHttpClient.setConnectTimeout(4, TimeUnit.SECONDS);
        }


    }

    public static void post(String url, Map<String, String> data, final OnHttpListener listener) {

        Log.i(TAG, "post: url : " + url);

        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (String key : data.keySet()) {
            if (!TextUtils.isEmpty(data.get(key))) {
                builder.add(key, data.get(key));
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                listenerOnFailure(listener, ApiCode.NET_ERROR);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final int code = response.code();
                if (code == 200) {
                    final String result = response.body().string();
                    Log.i(TAG, "onResponse: " + result);
                    try {
                        JSONObject obj = new JSONObject(result);
                        final int resultCode = obj.getInt(ApiKey.RESULT_CODE);
                        if (resultCode == 0) {
                            listenerOnSuccess(listener, result);
                        } else {
                            listenerOnFailure(listener, resultCode);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listenerOnFailure(listener, ApiCode.JSON_EXCEPTION);
                    }

                } else {
                    listenerOnFailure(listener, code);
                }
            }
        });
    }

    public static void postJson(String url, String json, final OnHttpListener listener) {
        Log.i(TAG, "postJson: url = " + url);
        Log.i(TAG, "postJson: json = " + json);

        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                listenerOnFailure(listener, ApiCode.NET_ERROR);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final int code = response.code();
                if (code == 200) {
                    final String result = response.body().string();
                    Log.i(TAG, "onResponse: " + result);
                    try {
                        JSONObject obj = new JSONObject(result);
                        final int resultCode = obj.getInt(ApiKey.RESULT_CODE);
                        if (resultCode == 0) {
                            listenerOnSuccess(listener, result);
                        } else {
                            listenerOnFailure(listener, resultCode);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listenerOnFailure(listener, ApiCode.JSON_EXCEPTION);
                    }

                } else {
                    listenerOnFailure(listener, code);
                }
            }
        });

    }

    public interface OnHttpListener {
        void onSuccess(String content);

        void onFailed(int code, String msg);
    }

    private static void listenerOnSuccess(final OnHttpListener listener, final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess(msg);
            }
        });
    }

    private static void listenerOnFailure(final OnHttpListener listener, final int code) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onFailed(code, ApiCode.getErrorMsg(code));
                Log.i(TAG, "failed:" + code);
            }
        });
    }


}

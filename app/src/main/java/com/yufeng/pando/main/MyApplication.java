package com.yufeng.pando.main;

import android.app.Application;

import com.yufeng.pando.net.HttpUtils;

/**
 * Created by Lin Youye on 2016/3/19.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HttpUtils.init();
    }
}

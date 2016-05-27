package com.yufeng.pando.net;

import com.yufeng.pando.main.AppInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lin Youye on 2016/4/16.
 */
public class ApiUtils {

    //获取验证码
    public static void getAuthCode(String phone, HttpUtils.OnHttpListener listener) {

        try {
            JSONObject obj = new JSONObject();
            obj.put(ApiKey.PHONE, phone);
            HttpUtils.postJson(ApiUrl.GET_AUTH_CODE, obj.toString(), listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //app 注册
    public static void register(String phone, String password, String code, HttpUtils.OnHttpListener listener) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(ApiKey.PHONE, phone);
            obj.put(ApiKey.PASSWORD, password);
            obj.put(ApiKey.AUTH_CODE, code);
            obj.put(ApiKey.APP_KEY, "9e5877458ff2284e9a9bcd1fc1176b47826cddbdb96452e2b6ef6a31b864c819");
            HttpUtils.postJson(ApiUrl.APP_REGISTER, obj.toString(), listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //app 登录
    public static void login(String phone, String password, HttpUtils.OnHttpListener listener) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(ApiKey.PHONE, phone);
            obj.put(ApiKey.PASSWORD, password);
            HttpUtils.postJson(ApiUrl.APP_LOGIN, obj.toString(), listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //绑定设备
    public static void bindDevice(String deviceKey, HttpUtils.OnHttpListener listener) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(ApiKey.DEVICE_KEY, deviceKey);
            addAuthInfo(obj);
            HttpUtils.postJson(ApiUrl.BIND_DEVICE, obj.toString(), listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获取绑定设备列表
    public static void getBoundDevices() {

    }

    //设置三色灯状态
    public static void setLightStatus(String deviceKey, int color, HttpUtils.OnHttpListener listener) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(ApiKey.DEVICE_KEY, deviceKey);
            obj.put(ApiKey.LIGHT_COLOR, color);
            addAuthInfo(obj);
            HttpUtils.postJson(ApiUrl.SET_LIGHT_COLOR, obj.toString(), listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获取三色灯状态
    public static void getLightStatus(String deviceKey, HttpUtils.OnHttpListener listener) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(ApiKey.DEVICE_KEY, deviceKey);
            addAuthInfo(obj);
            HttpUtils.postJson(ApiUrl.GET_LIGHT_COLOR, obj.toString(), listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void addAuthInfo(JSONObject data) {
        try {
            data.put(ApiKey.SESSION, AppInfo.session);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

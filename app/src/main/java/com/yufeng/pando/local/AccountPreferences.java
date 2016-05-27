package com.yufeng.pando.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lin Youye on 2016/3/23.
 * 储存账号信息
 */
public class AccountPreferences {

    private static final String FILE_NAME = "account";

    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";

    private static final String KEY_DEVICE_KEY = "device_key";

    private static final String KEY_WIFI_SSID = "wifi_ssid";
    private static final String KEY_WIFI_PASSWORD = "wifi_password";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public AccountPreferences(Context context) {
        mPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public void setAccount(String phone, String password) {

        mEditor.putString(KEY_PHONE, phone);
        mEditor.putString(KEY_PASSWORD, password);
        mEditor.commit();

    }

    public String getPhone() {
        return mPreferences.getString(KEY_PHONE, null);
    }

    public String getPassword() {
        return mPreferences.getString(KEY_PASSWORD, null);
    }

    public String getDeviceKey() {
        return mPreferences.getString(KEY_DEVICE_KEY, "efc33f0976615d89a519afc655aeadc31a0d920f72e855bea3614deabe121c76");
    }

    public void setDeviceKey(String deviceKey) {
        mEditor.putString(KEY_DEVICE_KEY, deviceKey);
        mEditor.commit();
    }

    public void setWifiConfig(String ssid, String pwd) {
        mEditor.putString(KEY_WIFI_SSID, ssid);
        mEditor.putString(KEY_WIFI_PASSWORD, pwd);
        mEditor.commit();
    }

    public String getWifiSsid() {
        return mPreferences.getString(KEY_WIFI_SSID, "");
    }

    public String getWifiPassword() {
        return mPreferences.getString(KEY_WIFI_PASSWORD, "");
    }

}

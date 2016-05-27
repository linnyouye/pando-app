package com.yufeng.pando.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.pandocloud.android.config.wifi.WifiConfigManager;
import com.pandocloud.android.config.wifi.WifiConfigMessageHandler;

/**
 * Created by Lin Youye on 2016/5/6.
 */
public class WifiConfigUtils {

    public static final String MODE_HOTSPOT = "hotspot";
    public static final String MODE_SMARTLINK = "smartlink";

    public static void setOnConfigListener(final OnConfigListener listener) {
        WifiConfigManager.setMsgHandler(new WifiConfigMessageHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case WifiConfigManager.CONFIG_SUCCESS:
                        listener.onSuccess(msg.obj.toString());
                        break;
                    default:
                        listener.onFail(msg.what, getErrMsg(msg.what));
                        break;
                }
            }
        }));
    }

    public static void startConfig(Context context, String mode, String ssid, String pwd) {
        WifiConfigManager.startConfig(context, mode, ssid, pwd);
    }

    public static void stopConfig() {
        WifiConfigManager.stopConfig();
    }

    public interface OnConfigListener {
        void onSuccess(String deviceKey);

        void onFail(int code, String msg);
    }

    private static String getErrMsg(int code) {
        switch (code) {
            case WifiConfigManager.CONFIG_FAILED:
                return "密码错误或热点未连接";
            case WifiConfigManager.CONFIG_TIMEOUT:
                return "配置超时";
            case WifiConfigManager.DEVICE_CONNECT_FAILED:
                return "device connect failed";
            case WifiConfigManager.DEVICE_SEND_FAILED:
                return "device send failed";
            case WifiConfigManager.DEVICE_RECV_FAILED:
                return "device recv failed";
            default:
                return "未知错误";
        }

    }
}

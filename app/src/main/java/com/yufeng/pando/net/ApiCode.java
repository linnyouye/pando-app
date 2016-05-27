package com.yufeng.pando.net;

/**
 * Created by Lin Youye on 2016/4/16.
 */
public class ApiCode {

    public static final int OK = 0;
    public static final int NET_ERROR = 50000;
    public static final int JSON_EXCEPTION = 50001;

    public static final int SYSTEM_ERROR = 10001;
    public static final int NO_PRODUCT = 10002;
    public static final int NO_DEVICE = 10003;
    public static final int DEVICE_OFFLINE = 10004;
    public static final int INVALID_STATUS_FORMAT = 10005;
    public static final int INVALID_CONFIG = 10006;
    public static final int INVALID_REQUEST_FORMAT = 10007;
    public static final int NO_PERMISSION = 10008;

    public static final int SEND_SMS_FAIL = 20001;
    public static final int AUTH_CODE_ERROR = 20002;
    public static final int REGISTER_FAIL = 20003;


    public static String getErrorMsg(int code) {
        switch (code) {
            case OK:
                return "操作成功";
            case NET_ERROR:
                return "网络连接失败";
            case SEND_SMS_FAIL:
                return "发送短信失败";
            case AUTH_CODE_ERROR:
                return "验证码错误";
            case REGISTER_FAIL:
                return "手机号已被注册";

            case SYSTEM_ERROR:
                return "系统错误";
            case NO_PRODUCT:
                return "产品不存在";
            case NO_DEVICE:
                return "设备不存在";
            case DEVICE_OFFLINE:
                return "设备当前不在线";
            case INVALID_STATUS_FORMAT:
                return "错误的设备状态请求格式";
            case INVALID_CONFIG:
                return "错误的产品配置";
            case INVALID_REQUEST_FORMAT:
                return "错误的请求格式";
            case NO_PERMISSION:
                return "无权访问";
            default:
                return "操作失败:" + code;
        }
    }

}

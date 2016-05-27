package com.yufeng.pando.net;

/**
 * Created by Lin Youye on 2016/4/9.
 */
public class ApiUrl {

    private static final String HOST = "120.24.255.42";
    private static final String COMMON_URL = "http://" + HOST + "/index.php?m=%s&c=%s&a=%s";

    public static final String GET_AUTH_CODE = String.format(COMMON_URL, "Api", "App", "getAuthCode");
    public static final String APP_REGISTER = String.format(COMMON_URL, "Api", "App", "register");
    public static final String APP_LOGIN = String.format(COMMON_URL, "Api", "App", "login");

    public static final String BIND_DEVICE = String.format(COMMON_URL, "Api", "App", "bindDevice");


    public static final String SET_LIGHT_COLOR = String.format(COMMON_URL, "Api", "App", "setLightColor");
    public static final String GET_LIGHT_COLOR = String.format(COMMON_URL, "Api", "App", "getLightColor");
}

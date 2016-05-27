package com.yufeng.pando.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lyy.mylibrary.ui.ColorPickerDialog;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.yufeng.pando.R;
import com.yufeng.pando.local.AccountPreferences;
import com.yufeng.pando.main.Constant;
import com.yufeng.pando.net.ApiKey;
import com.yufeng.pando.net.ApiUtils;
import com.yufeng.pando.net.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lin Youye on 2016/4/9.
 */
public class DeviceActivity extends BaseActivity {

    private static final String TAGr = "lyy-DeviceActivity";

    private static final int RQ_SCAN = 1;
    private static final int RQ_CONFIG = 2;

    @Bind(R.id.ibtn_scan)
    ImageButton ibtnScan;
    @Bind(R.id.btn_switch_device)
    Button btnSwitchDevice;
    @Bind(R.id.btn_get_color)
    Button btnGetColor;
    @Bind(R.id.tv_status)
    TextView tvStatus;

    @Bind(R.id.btn_color)
    Button btnColor;

    private String mTargetDeviceKey;

    private ColorPickerDialog colorDialog;

    private AccountPreferences mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.bind(this);

        colorDialog = new ColorPickerDialog(this, new ColorPickerDialog.OnColorChangedListener() {
            @Override
            public void colorChanged(int color) {
                setLightColor(color);
            }
        }, 0xffffff);

        mAccount = new AccountPreferences(this);
        mTargetDeviceKey = mAccount.getDeviceKey();

    }

    @OnClick({R.id.ibtn_scan, R.id.btn_switch_device, R.id.btn_color, R.id.btn_get_color})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_scan:
                startActivityForResult(CaptureActivity.class, RQ_SCAN);
                break;
            case R.id.btn_switch_device:
                startActivityForResult(WifiConfigActivity.class, RQ_CONFIG);
                break;
            case R.id.btn_color:
                colorDialog.show();
                break;
            case R.id.btn_get_color:
                getLightColor();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        switch (requestCode) {
            case RQ_SCAN:
                String code = data.getStringExtra("result");
                break;
            case RQ_CONFIG:
                mTargetDeviceKey = data.getStringExtra(Constant.KEY_DEVICE_KEY);
                mAccount.setDeviceKey(mTargetDeviceKey);
                updateView();
                getLightColor();
                break;

        }
    }

    private void updateView() {
        tvStatus.setText("当前关联设备：" + mTargetDeviceKey);
    }

    private void setLightColor(final int color) {

        if (TextUtils.isEmpty(mTargetDeviceKey)) {
            showToast("未关联设备，请先进行WIFI配置");
            return;
        }

        ApiUtils.setLightStatus(mTargetDeviceKey, color & 0x00ffffff, new HttpUtils.OnHttpListener() {
            @Override
            public void onSuccess(String content) {
                btnColor.setBackgroundColor(color);
            }

            @Override
            public void onFailed(int code, String msg) {
                showToast(msg);
            }
        });
    }

    private void getLightColor() {

        if (TextUtils.isEmpty(mTargetDeviceKey)) {
            showToast("未关联设备，请先进行WIFI配置");
            return;
        }

        ApiUtils.getLightStatus(mTargetDeviceKey, new HttpUtils.OnHttpListener() {
            @Override
            public void onSuccess(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    int color = obj.getInt(ApiKey.LIGHT_COLOR);
                    btnColor.setBackgroundColor(color | 0xff000000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                showToast(msg);
            }
        });
    }
}

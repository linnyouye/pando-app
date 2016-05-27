package com.yufeng.pando.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.yufeng.pando.R;
import com.yufeng.pando.local.AccountPreferences;
import com.yufeng.pando.main.Constant;
import com.yufeng.pando.net.WifiConfigUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lin Youye on 2016/5/6.
 */
public class WifiConfigActivity extends BaseActivity {

    @Bind(R.id.et_ssid)
    EditText etSsid;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.btn_config)
    Button btnConfig;

    private AccountPreferences mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_config);
        ButterKnife.bind(this);

        mAccount = new AccountPreferences(this);
        etSsid.setText(mAccount.getWifiSsid());
        etPwd.setText(mAccount.getWifiPassword());

        WifiConfigUtils.setOnConfigListener(new WifiConfigUtils.OnConfigListener() {
            @Override
            public void onSuccess(String deviceKey) {
                cancelProgressDialog();
                showToast("配置成功");
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_DEVICE_KEY, deviceKey);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFail(int code, String msg) {
                cancelProgressDialog();
                showToast(msg);
            }
        });

    }

    @OnClick(R.id.btn_config)
    public void onClick() {

        mAccount.setWifiConfig(etSsid.getText().toString(), etPwd.getText().toString());

        showProgressDialog("正在配置");
        WifiConfigUtils.stopConfig();
        WifiConfigUtils.startConfig(this, WifiConfigUtils.MODE_SMARTLINK, etSsid.getText().toString(), etPwd.getText().toString());


    }
}

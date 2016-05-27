package com.yufeng.pando.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.lyy.mylibrary.ui.ClearEditText;
import com.yufeng.pando.R;
import com.yufeng.pando.local.AccountPreferences;
import com.yufeng.pando.main.AppInfo;
import com.yufeng.pando.net.ApiKey;
import com.yufeng.pando.net.ApiUtils;
import com.yufeng.pando.net.HttpUtils;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterAndLoginActivity extends BaseActivity {

    private static final boolean D = true;
    private static final String TAG = "lyy-RegisterAndLoginActivity";


    @Bind(R.id.title_text)
    TextSwitcher mTitleText;
    @Bind(R.id.et_phone)
    ClearEditText mEtPhone;
    @Bind(R.id.et_password)
    ClearEditText mEtPassword;
    @Bind(R.id.et_auth_code)
    ClearEditText mEtAuthCode;
    @Bind(R.id.auth_code_view)
    LinearLayout mAuthCodeView;
    @Bind(R.id.btn_ok)
    Button mBtnOk;
    @Bind(R.id.btn_left)
    Button mBtnLeft;
    @Bind(R.id.btn_right)
    Button mBtnRight;
    @Bind(R.id.btn_auth_code)
    Button mBtnAuthCode;
    @Bind(R.id.launcher_view)
    View mLauncherView;

    enum State {
        REGISTER, LOGIN, FORGET_PASSWORD
    }

    private State mState;


    private AccountPreferences mAccount;
    private String mPhone;
    private String mPassword;
    private String mAuthCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_login);
        ButterKnife.bind(this);
        initTextSwitcher();

        mBtnRight.setVisibility(View.GONE);

        mAccount = new AccountPreferences(this);
        mPhone = mAccount.getPhone();
        mPassword = mAccount.getPassword();
        mEtPhone.setText(mPhone);
        mEtPassword.setText(mPassword);

        if (TextUtils.isEmpty(mPhone)) {
            setState(State.REGISTER);
            hideLaunchViewDelay();
        } else {
            setState(State.LOGIN);
            login();
        }


    }

    private void hideLaunchViewDelay() {
        //一秒后隐藏 launcher view
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLauncherView();
                    }
                });
            }
        }.start();
    }

    private void initTextSwitcher() {
        mTitleText.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView tv = new TextView(RegisterAndLoginActivity.this);
                tv.setTextSize(20);
                tv.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(Color.WHITE);
                return tv;
            }
        });
        mTitleText.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.title_text_in));
        mTitleText.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.title_text_out));
    }

    private void setState(State state) {
        if (mState == state) {
            return;
        }
        this.mState = state;
        switch (state) {
            case REGISTER:
                mTitleText.setText(getResources().getString(R.string.title_register));
                mBtnOk.setText(R.string.title_register);
                mBtnLeft.setText(R.string.to_login);
                mBtnRight.setText(R.string.title_forget_password);
                mEtPassword.setHint(R.string.hint_password);
                mAuthCodeView.setVisibility(View.VISIBLE);
                break;
            case LOGIN:
                mTitleText.setText(getResources().getString(R.string.title_login));
                mBtnOk.setText(R.string.title_login);
                mBtnLeft.setText(R.string.to_register);
                mBtnRight.setText(R.string.title_forget_password);
                mEtPassword.setHint(R.string.hint_password);
                mAuthCodeView.setVisibility(View.GONE);
                break;
            case FORGET_PASSWORD:
                mTitleText.setText(getResources().getString(R.string.title_forget_password));
                mBtnOk.setText(R.string.title_forget_password);
                mBtnLeft.setText(R.string.title_login);
                mBtnRight.setText("");
                mEtPassword.setHint(R.string.hint_new_password);
                mEtPassword.setText("");
                mAuthCodeView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void hideLauncherView() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLauncherView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLauncherView.startAnimation(anim);
    }

    @OnClick({R.id.btn_ok, R.id.btn_left, R.id.btn_right, R.id.btn_auth_code})
    public void onClick(View view) {
        getData();

        switch (view.getId()) {
            case R.id.btn_ok:
                switch (mState) {
                    case REGISTER:
                        register();
                        break;
                    case FORGET_PASSWORD:
                        break;
                    case LOGIN:
                        login();
                        break;
                }
                break;
            case R.id.btn_left:
                switch (mState) {
                    case REGISTER:
                    case FORGET_PASSWORD:
                        setState(State.LOGIN);
                        break;
                    case LOGIN:
                        setState(State.REGISTER);
                        break;
                }
                break;
            case R.id.btn_right:
                setState(State.FORGET_PASSWORD);
                break;
            case R.id.btn_auth_code:
                getAuthCode();
                break;
        }
    }

    private void register() {
        if (isPhoneValid() && isAuthCodeValid() && isPasswordValid()) {
            JSONObject obj = new JSONObject();

            ApiUtils.register(mPhone, mPassword, mAuthCode, new HttpUtils.OnHttpListener() {
                @Override
                public void onSuccess(String content) {
                    login();
                }

                @Override
                public void onFailed(int code, String msg) {
                    showToast(msg);
                }
            });
        }
    }

    private void login() {
        if (isPhoneValid() && isPasswordValid()) {
            ApiUtils.login(mPhone, mPassword, new HttpUtils.OnHttpListener() {
                @Override
                public void onSuccess(String content) {
                    try {
                        JSONObject obj = new JSONObject(content);
                        AppInfo.session = obj.getString(ApiKey.SESSION);
                        startActivity(DeviceActivity.class);
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(int code, String msg) {
                    showToast(msg);
                    mLauncherView.setVisibility(View.GONE);
                }
            });
        }
    }

    private void getAuthCode() {
        if (isPhoneValid()) {
            ApiUtils.getAuthCode(mPhone, new HttpUtils.OnHttpListener() {
                @Override
                public void onSuccess(String content) {
                    showToast("已发送验证码");
                }

                @Override
                public void onFailed(int code, String msg) {
                    showToast(msg);
                }
            });
        }
    }

    private boolean isPhoneValid() {
        if (TextUtils.isEmpty(mPhone)) {
            showToast("请输入手机号");
            return false;
        }

        return true;
    }

    private boolean isPasswordValid() {
        if (TextUtils.isEmpty(mPassword)) {
            showToast("请输入密码");
            return false;
        }
        return true;
    }

    private boolean isAuthCodeValid() {
        if (TextUtils.isEmpty(mAuthCode)) {
            showToast("请输入验证码");
            return false;
        }
        return true;
    }

    private void getData() {
        mPhone = mEtPhone.getText().toString();
        mPassword = mEtPassword.getText().toString();
        mAuthCode = mEtAuthCode.getText().toString();

        mAccount.setAccount(mPhone, mPassword);
    }

}

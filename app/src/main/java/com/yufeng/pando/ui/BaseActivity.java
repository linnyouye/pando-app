package com.yufeng.pando.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lyy.mylibrary.ui.MyProgressDialog;
import com.yufeng.pando.R;


public class BaseActivity extends Activity {

    protected boolean D = true;
    protected String TAG = "lyy";

    private MyProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        TAG = "lyy-" + getClass().getSimpleName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.status_bar);//通知栏所需颜色
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(true);
    }

    // ----------startActivity-------------------------------------------------------------------
    protected void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    protected void startActivityForResult(Class<?> clz, int requestCode) {
        startActivityForResult(new Intent(this, clz), requestCode);
    }

    // ----------enableReturning-------------------------------------------------------------------
    protected void enableReturning() {
        View view = findViewById(R.id.btnBack);
        if (view != null) {
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    finish();
                }
            });
        }
    }

    protected void enableSwipeBack() {
        setTheme(R.style.SwipeBackTheme);
    }

    // ----------title-------------------------------------------------------------------
    @Override
    public void setTitle(CharSequence title) {
        try {
            ((TextView) findViewById(R.id.title_text)).setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTitle(int textId) {
        try {
            ((TextView) findViewById(R.id.title_text)).setText(textId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------Toast-------------------------------------------------------------------
    public void showToast(CharSequence msg) {
        UIHelper.showToast(this, msg);
    }

    public void showToast(int strId) {
        UIHelper.showToast(this, getResources().getString(strId));
    }

    // ----------ProgressDialog-------------------------------------------------------------------
    public void showProgressDialog(CharSequence msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new MyProgressDialog(this);
        }
        mProgressDialog.show(msg);
    }

    public void showProgressDialog(int strId) {
        showProgressDialog(getResources().getString(strId));
    }

    public void cancelProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


}

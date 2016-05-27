package com.lyy.mylibrary.pullrefreshlayout;

import android.content.Context;
import android.view.View;

/**
 * Created by Lin Youye on 2016/4/6.
 */
public abstract class RefreshView {

    private Context mContext;

    public RefreshView(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public abstract View getView();


    public abstract void onProgress(int percent);

    public abstract void onRefresh(boolean refreshing);
}

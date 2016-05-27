package com.lyy.mylibrary.pullrefreshlayout;

import android.content.Context;
import android.view.View;

/**
 * Created by Lin Youye on 2016/4/7.
 */
public abstract class LoadView {
    private Context mContext;

    public LoadView(Context context) {
        mContext = context;
    }

    public abstract View getView();

    public abstract void onProgress(int percent);

    public abstract void onLoad();

    public abstract void onFinishLoading(boolean canLoadMore);

}

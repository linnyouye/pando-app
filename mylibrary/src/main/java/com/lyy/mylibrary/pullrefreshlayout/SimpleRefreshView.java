package com.lyy.mylibrary.pullrefreshlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lyy.mylibrary.R;


/**
 * Created by Lin Youye on 2016/4/6.
 */
public class SimpleRefreshView extends RefreshView {

    private static final String TAG = "lyy-SimpleRefreshView";

    private View mView;
    private ProgressWheel mProgress;

    public SimpleRefreshView(Context context) {
        super(context);
        mView = LayoutInflater.from(context).inflate(R.layout.simple_refresh_view, null);
        mProgress = (ProgressWheel) mView.findViewById(R.id.progress_wheel);


    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void onProgress(int percent) {

        mProgress.setInstantProgress(Math.min(1.0f, percent / 100.0f));

    }

    @Override
    public void onRefresh(boolean refreshing) {
        if (refreshing) {
            mProgress.spin();
        } else {
            mProgress.stopSpinning();
        }
    }


}

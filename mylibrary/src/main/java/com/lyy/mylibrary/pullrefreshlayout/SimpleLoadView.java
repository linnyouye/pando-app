package com.lyy.mylibrary.pullrefreshlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lyy.mylibrary.R;

/**
 * Created by Lin Youye on 2016/4/7.
 */
public class SimpleLoadView extends LoadView {

    private View mView;
    private TextView mTextView;
    private ProgressWheel mProgress;

    public SimpleLoadView(Context context) {
        super(context);
        mView = LayoutInflater.from(context).inflate(R.layout.simple_load_view, null);
        mTextView = (TextView) mView.findViewById(R.id.tv_no_more);
        mProgress = (ProgressWheel) mView.findViewById(R.id.progress_wheel);
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void onProgress(int percent) {
        mTextView.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
        mProgress.setInstantProgress(Math.min(1, percent / 100f));
    }

    @Override
    public void onLoad() {
        mProgress.spin();
    }

    @Override
    public void onFinishLoading(boolean canLoadMore) {
        mProgress.stopSpinning();
        if (!canLoadMore) {
            mProgress.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
        }
    }
}

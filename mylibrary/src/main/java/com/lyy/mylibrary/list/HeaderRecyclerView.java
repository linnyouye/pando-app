package com.lyy.mylibrary.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Lin Youye on 2016/4/1.
 */
public class HeaderRecyclerView extends RecyclerView {

    private static final String TAG = "lyy-HeaderRecyclerView";

    private View mHeaderView;
    private View mFooterView;

    public HeaderRecyclerView(Context context) {
        super(context);
        init();
    }

    public HeaderRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public HeaderRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {


    }

    public void setHeader(View header) {
        mHeaderView = header;
    }

    public void setFooter(View footer) {
        mFooterView = footer;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (mHeaderView != null || mFooterView != null) {
            HeaderRecyclerViewAdapter ha = new HeaderRecyclerViewAdapter(adapter);
            ha.setHeader(mHeaderView);
            ha.setFooter(mFooterView);
            super.setAdapter(ha);
        } else {
            super.setAdapter(adapter);
        }
    }
}

package com.lyy.mylibrary.pullrefreshlayout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Scroller;

/**
 * Created by Lin Youye on 2016/4/6.
 */
public class PullRefreshLayout extends ViewGroup {

    private static final String TAG = "lyy-MyLayout";

    private int TOUCH_SLOP;

    private int REFRESH_VIEW_HEIGHT = 120;
    private int LOAD_VIEW_HEIGHT = 120;

    private boolean mIsRefreshing = false;
    private boolean mIsLoading = false;
    private boolean mCanRefresh = true;
    private boolean mCanLoadMore = true;

    private OnRefrehLister mOnRefreshListener;
    private OnLoadListener mOnLoadListener;


    private RefreshView mRefreshView;
    private LoadView mLoadView;
    private View mContentView;

    private Scroller mScroller;

    private float mLastY;

    public PullRefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);

    }

    private void init(Context context) {

        TOUCH_SLOP = ViewConfiguration.get(context).getScaledTouchSlop() / 2;

        mRefreshView = new SimpleRefreshView(context);
        mLoadView = new SimpleLoadView(context);

        addView(mRefreshView.getView());
        addView(mLoadView.getView());

        mScroller = new Scroller(context);

        setWillNotDraw(false);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ensureContentView();

        Log.i(TAG, "onLayout: " + mRefreshView.getView().getMeasuredHeight());

        mRefreshView.getView().layout(0, -mRefreshView.getView().getMeasuredHeight(), getMeasuredWidth(), 1);

        mLoadView.getView().layout(0, getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() + 100);

        mContentView.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ensureContentView();

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mRefreshView.getView().measure(widthMeasureSpec, heightMeasureSpec);
        mLoadView.getView().measure(widthMeasureSpec, heightMeasureSpec);
        mContentView.measure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsLoading || mIsRefreshing) {
            return false;
        }

        Log.i(TAG, "onInterceptTouchEvent: " + ev.getAction());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                return false;
            case MotionEvent.ACTION_MOVE:
                float dy = ev.getY() - mLastY;
                mLastY = ev.getY();

                Log.i(TAG, "onInterceptTouchEvent: dy=" + dy);
                Log.i(TAG, "onInterceptTouchEvent: isChildScrollToTop:" + isChildScrollToTop());
                Log.i(TAG, "onInterceptTouchEvent: isChildScrollToBottom:" + isChildScrollToBottom());

                if (dy > TOUCH_SLOP && isChildScrollToTop()) {
                    return true;
                } else if (dy < -TOUCH_SLOP && isChildScrollToBottom()) {
                    return true;
                }
        }
        return false;

    }

    private boolean isChildScrollToTop() {

        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mContentView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mContentView;
                if (absListView.getChildCount() == 0) {
                    return true;
                } else {
                    return absListView.getFirstVisiblePosition() == 0 && absListView.getChildAt(0).getTop() >= absListView.getPaddingTop();
                }
            } else {
                return mContentView.getScrollY() <= 0;
            }
        } else {
            return !ViewCompat.canScrollVertically(mContentView, -1);
        }
    }

    private boolean isChildScrollToBottom() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mContentView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mContentView;
                if (absListView.getChildCount() == 0) {
                    return true;
                } else {
                    return absListView.getLastVisiblePosition() == absListView.getAdapter().getCount() - 1 && absListView.getChildAt(getChildCount() - 1).getBottom() <= absListView.getMeasuredHeight() - absListView.getPaddingBottom();
                }
            } else {
                return mContentView.getScrollY() <= 0;
            }
        } else {
            return !ViewCompat.canScrollVertically(mContentView, 1);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsRefreshing || mIsLoading) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //mLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = event.getY() - mLastY;
                int offset = (int) (dy * 0.6);
                mRefreshView.getView().offsetTopAndBottom(offset);
                mLoadView.getView().offsetTopAndBottom(offset);
                mContentView.offsetTopAndBottom(offset);

                if (mContentView.getTop() > 0 && mCanRefresh) {
                    mRefreshView.onProgress(mContentView.getTop() * 100 / REFRESH_VIEW_HEIGHT);
                } else if (mContentView.getTop() < 0 && mCanLoadMore) {
                    mLoadView.onProgress(-mContentView.getTop() * 100 / LOAD_VIEW_HEIGHT);
                }

                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                if (mContentView.getTop() > REFRESH_VIEW_HEIGHT && mCanRefresh) {
                    //刷新
                    setRefreshing(true);
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefresh();
                    }
                } else if (mContentView.getTop() < -LOAD_VIEW_HEIGHT && mCanLoadMore) {
                    //加载更多
                    setLoading(true, true);
                    if (mOnLoadListener != null) {
                        mOnLoadListener.onLoad();
                    }
                } else {
                    //无事件触发，恢复
                    smoothScrollTo(0);
                }


                break;


        }
        return true;


    }

    private void ensureContentView() {
        if (mContentView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mRefreshView.getView() && child != mLoadView.getView()) {
                    mContentView = child;
                    return;
                }
            }
        }
    }

    private void smoothScrollTo(int destY) {
        ensureContentView();
        if (mScroller == null) {
            mScroller = new Scroller(getContext());
        }
        int duration = 0;
        if (Math.abs(getTop()) > 300) {
            duration = 1000;
        } else {
            duration = 600;
        }
        mScroller.startScroll(0, mContentView.getTop(), 0, destY - mContentView.getTop(), duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int offset = mScroller.getCurrY() - mContentView.getTop();
            mRefreshView.getView().offsetTopAndBottom(offset);
            mLoadView.getView().offsetTopAndBottom(offset);
            mContentView.offsetTopAndBottom(offset);

            postInvalidate();
        }
    }

    public void setRefreshing(boolean refreshing) {
        mIsRefreshing = refreshing;
        smoothScrollTo(mIsRefreshing ? REFRESH_VIEW_HEIGHT : 0);
        mRefreshView.onRefresh(mIsRefreshing);
    }

    public void setLoading(boolean loading, boolean canLoadMore) {
        mIsLoading = loading;
        smoothScrollTo(mIsLoading ? -LOAD_VIEW_HEIGHT : 0);
        if (loading) {
            mLoadView.onLoad();
        } else {
            mLoadView.onFinishLoading(canLoadMore);
            if (!canLoadMore) {
                enableLoad(false);
            }
        }
    }

    public void enableRefresh(boolean enable) {
        mCanRefresh = enable;
    }

    public void enableLoad(boolean enable) {
        mCanLoadMore = enable;
    }

    public void setOnRefreshListener(OnRefrehLister l) {
        mOnRefreshListener = l;
    }

    public void setOnLoadListener(OnLoadListener l) {
        mOnLoadListener = l;
    }

    public interface OnRefrehLister {
        void onRefresh();
    }

    public interface OnLoadListener {
        void onLoad();
    }
}

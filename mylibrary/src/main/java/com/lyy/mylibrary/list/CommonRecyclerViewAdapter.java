package com.lyy.mylibrary.list;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lin Youye on 2016/3/29.
 */
public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter {

    private static final String TAG = "lyy-CommonRecyclerViewAdapter";

    protected List<T> mDatas;
    protected final int mItemLayoutId;

    public CommonRecyclerViewAdapter(int itemLayoutId, List<T> mDatas) {
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myHolder = (MyViewHolder) holder;
        T item = mDatas.get(position);
        onBindData(myHolder, position, item);

    }

    public abstract void onBindData(MyViewHolder helper, int position, T item);


    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        } else {
            return mDatas.size();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final SparseArray<View> mViews;
        private View mConvertView;

        public MyViewHolder(View view) {
            super(view);
            mViews = new SparseArray<>();
            mConvertView = view;
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public void setText(int viewId, CharSequence text) {
            TextView tv = getView(viewId);
            tv.setText(text);
        }

        public void setOnClickListener(int viewId, View.OnClickListener l) {
            View view = null;
            if (viewId == 0) {
                view = mConvertView;
            } else {
                view = getView(viewId);
            }
            view.setClickable(true);
            view.setOnClickListener(l);
        }


    }

}

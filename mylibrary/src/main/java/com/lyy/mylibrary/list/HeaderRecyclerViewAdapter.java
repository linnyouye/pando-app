package com.lyy.mylibrary.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lyy.mylibrary.R;


public class HeaderRecyclerViewAdapter extends RecyclerView.Adapter {
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_FOOTER = 2;
    private static final int TYPE_NORMAL = 3;

    private final RecyclerView.Adapter mAdaptee;

    private View mHeaderView;
    private View mFooterView;


    public HeaderRecyclerViewAdapter(RecyclerView.Adapter adaptee) {
        mAdaptee = adaptee;
    }

    public RecyclerView.Adapter getAdaptee() {
        return mAdaptee;
    }

    public void setHeader(View h) {
        mHeaderView = h;
    }

    public void setFooter(View f) {
        mFooterView = f;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER && hasHeader()) {
            ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.header_container, parent, false);
            mHeaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            view.addView(mHeaderView);
            return new MyViewHolder(view);
        } else if (viewType == TYPE_FOOTER && hasFooter()) {
            ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.header_container, parent, false);
            mFooterView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            view.addView(mFooterView);
            return new MyViewHolder(view);
        }
        return mAdaptee.onCreateViewHolder(parent, TYPE_NORMAL);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            mAdaptee.onBindViewHolder(holder, position - (hasHeader() ? 1 : 0));
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = mAdaptee.getItemCount();
        if (hasHeader()) {
            itemCount += 1;
        }
        if (hasFooter()) {
            itemCount += 1;
        }
        return itemCount;
    }

    private boolean hasHeader() {
        return mHeaderView != null;
    }

    private boolean hasFooter() {
        return mFooterView != null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && hasHeader()) {
            return TYPE_HEADER;
        }
        if (hasHeader()) {
            position--;
        }
        if (position == mAdaptee.getItemCount() && hasFooter()) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }


}
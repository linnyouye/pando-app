package com.lyy.mylibrary.list;

import android.view.View;

import com.lyy.mylibrary.R;

import java.util.List;

/**
 * Created by Lin Youye on 2016/4/4.
 */
public abstract class SimpleTextAdapter<T> extends CommonRecyclerViewAdapter<T> {

    public SimpleTextAdapter(List<T> mDatas) {
        super(R.layout.list_item_simple_text, mDatas);
    }

    public abstract CharSequence getText(int position, T item);

    public void onClick(View view, int position, T item) {

    }

    @Override
    public void onBindData(MyViewHolder helper, final int position, final T item) {
        CharSequence text = getText(position, item);
        helper.setText(R.id.tv_content, text);
        helper.setOnClickListener(R.id.tv_content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleTextAdapter.this.onClick(v, position, item);
            }
        });
    }
}

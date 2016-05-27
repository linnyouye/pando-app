package com.lyy.mylibrary.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyy.mylibrary.R;


public class LinearItem extends LinearLayout {

    private static final boolean D = true;
    private static final String TAG = "lyy-LinearItem";

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SWITCH = 1;

    private ImageView mImgIcon;
    private ImageView mImgArrows;
    private TextView mTvTitle;
    private TextView mTvContent;
    private SlideSwitch mSlideSwitch;

    public LinearItem(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context, null);
    }

    public LinearItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    public LinearItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub

        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        /*
        View view = LayoutInflater.from(context).inflate(R.layout.linear_item, null);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        addView(view);
        */
        LayoutInflater.from(context).inflate(R.layout.linear_item, this, true);

        setGravity(Gravity.CENTER_VERTICAL);


        mImgIcon = (ImageView) findViewById(R.id.imgIcon);
        mImgArrows = (ImageView) findViewById(R.id.imgArrows);
        mTvTitle = (TextView) findViewById(R.id.tvItemTitle);
        mTvContent = (TextView) findViewById(R.id.tvItemContent);
        mSlideSwitch = (SlideSwitch) findViewById(R.id.slideSwitch);

        if (attrs == null) {
            return;
        }

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.abc);
        Drawable icon = a.getDrawable(R.styleable.abc_item_icon);
        if (icon != null) {
            mImgIcon.setImageDrawable(icon);
            mImgIcon.setVisibility(View.VISIBLE);
        } else {
            mImgIcon.setVisibility(View.GONE);
        }
        mTvTitle.setText(a.getString(R.styleable.abc_item_title));
        mTvContent.setText(a.getString(R.styleable.abc_item_content));

        if (a.getInt(R.styleable.abc_item_type, TYPE_NORMAL) == TYPE_SWITCH) {
            setClickable(false);
            mSlideSwitch.setVisibility(View.VISIBLE);
            mTvContent.setVisibility(View.GONE);
        } else {
            mSlideSwitch.setVisibility(View.GONE);
            mTvContent.setVisibility(View.VISIBLE);
        }


        mImgArrows.setVisibility(isClickable() ? View.VISIBLE : View.GONE);

        a.recycle();

    }

}

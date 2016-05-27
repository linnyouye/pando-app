package com.lyy.mylibrary.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lin Youye on 2016/3/29.
 */
public class WebTextView extends TextView {

    private static final String TAG = "lyy-WebTextView";

    private Map<String, Drawable> mDrawableMap;

    private MyImageGetter mImageGetter;

    private String mHtml;

    public WebTextView(Context context) {
        super(context);
        init();
    }

    public WebTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        mDrawableMap = new HashMap<>();
        mImageGetter = new MyImageGetter();
    }

    public void setHtml(String html) {
        this.mHtml = html;
        setText(Html.fromHtml(html, mImageGetter, null));
    }

    class MyImageGetter implements Html.ImageGetter {

        @Override
        public Drawable getDrawable(String source) {

            Log.i(TAG, "getDrawable: " + source);

            Drawable drawable = mDrawableMap.get(source);
            if (drawable == null) {
                new DownloadImageThread(source).start();
                return null;
            } else {
                return drawable;
            }

        }
    }

    class DownloadImageThread extends Thread {

        private String imgUrl;

        public DownloadImageThread(String url) {
            imgUrl = url;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(imgUrl);
                Drawable drawable = Drawable.createFromStream(url.openStream(), "");
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                        .getIntrinsicHeight());
                mDrawableMap.put(imgUrl, drawable);
                postInvalidate();
                post(new Runnable() {
                    @Override
                    public void run() {
                        setHtml(mHtml);
                    }
                });
                Log.i(TAG, "run: post");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "run: error");
            }

        }
    }

}

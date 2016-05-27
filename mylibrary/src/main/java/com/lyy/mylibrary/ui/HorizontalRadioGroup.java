package com.lyy.mylibrary.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.lyy.mylibrary.R;

import java.util.ArrayList;
import java.util.List;


public class HorizontalRadioGroup extends LinearLayout {

	private static final boolean D = true;
	private static final String TAG = "lyy-HorizontalRadioGroup";

	private static final int TYPE_FIRST = 0;
	private static final int TYPE_MIDDLE = 1;
	private static final int TYPE_LAST = 2;

	private Context mContext;
	private Resources mRes;
	private List<CheckBox> mCbList;
	private OnClickListener mClickListener;
	private OnItemSelectedListener mOnItemSelectedListener;
	private int mSelection = -1;

	public HorizontalRadioGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public HorizontalRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public HorizontalRadioGroup(Context context, AttributeSet attrs,
								int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		mContext = context;
		mRes = context.getResources();
		mCbList = new ArrayList<CheckBox>();
		setOrientation(HORIZONTAL);
		setBackgroundResource(R.drawable.bg_horizontal_rg);

		mClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!(v instanceof CheckBox)) {
					return;
				}

				for (int i = 0; i < mCbList.size(); i++) {
					if (mCbList.get(i) == v) {
						setSelection(i);
						return;
					}
				}

			}
		};

	}

	public void setSelection(int index) {
		if (mCbList == null || index < 0 || index >= mCbList.size()
				|| index == mSelection) {
			mCbList.get(index).setChecked(true);
			return;
		}

		for (CheckBox cb : mCbList) {
			cb.setChecked(false);
		}
		mSelection = index;
		mCbList.get(index).setChecked(true);
		if (mOnItemSelectedListener != null) {
			mOnItemSelectedListener.onItemSelected(index);
		}

	}

	public int getSelection() {
		return mSelection;
	}

	public void setOnItemSelectedListener(OnItemSelectedListener l) {
		mOnItemSelectedListener = l;
	}

	public void setItems(List<CharSequence> items) {
		if (items != null) {
			setItems(items.toArray(new String[items.size()]));
		}
	}

	public void setItems(CharSequence[] items) {
		if (items == null || items.length < 2) {
			return;
		}

		addCheckBox(items[0], TYPE_FIRST);
		for (int i = 1; i < items.length - 1; i++) {

			addDivider();
			addCheckBox(items[i], TYPE_MIDDLE);
		}
		addDivider();
		addCheckBox(items[items.length - 1], TYPE_LAST);
	}

	public void setItems(int[] strIDs) {
		if (strIDs == null || strIDs.length < 2) {
			return;
		}
		CharSequence[] items = new CharSequence[strIDs.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = mRes.getString(strIDs[i]);
		}
		setItems(items);
	}

	private void addCheckBox(CharSequence text, int type) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, 1);
		CheckBox checkBox = new CheckBox(mContext);
		checkBox.setLayoutParams(params);
		switch (type) {
		case TYPE_FIRST:
			checkBox.setBackgroundResource(R.drawable.rb_first_selector);
			break;
		case TYPE_LAST:
			checkBox.setBackgroundResource(R.drawable.rb_last_selector);
			break;
		default:
			checkBox.setBackgroundResource(R.drawable.rb_middle_selector);
			break;
		}

		checkBox.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
		checkBox.setGravity(Gravity.CENTER);
		checkBox.setText(text);
		checkBox.setTextColor((ColorStateList) mContext.getResources()
				.getColorStateList(R.color.cb_text));
		addView(checkBox);
		mCbList.add(checkBox);
		checkBox.setOnClickListener(mClickListener);
	}

	private void addDivider() {
		LayoutParams params = new LayoutParams(mContext.getResources()
				.getDimensionPixelSize(R.dimen.rb_line_width),
				LayoutParams.MATCH_PARENT, 0);

		View mVerticalDivider = new LinearLayout(mContext);
		mVerticalDivider.setLayoutParams(params);
		mVerticalDivider.setBackgroundColor(mContext.getResources().getColor(
				R.color.radiogroup_line));

		addView(mVerticalDivider);
	}

	public interface OnItemSelectedListener {
		void onItemSelected(int index);
	}

}

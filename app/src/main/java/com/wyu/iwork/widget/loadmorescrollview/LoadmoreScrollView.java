package com.wyu.iwork.widget.loadmorescrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.wyu.iwork.util.Logger;

public class LoadmoreScrollView extends ScrollView {

	private View mContentView;

	public LoadmoreScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LoadmoreScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LoadmoreScrollView(Context context) {
		super(context);
	}

	LoadmoreOnScrollChanged loadmoreOnScrollChanged = null;

	public void onS(LoadmoreOnScrollChanged t) {
		loadmoreOnScrollChanged = t;
	}

	// 是否正在移动
	public boolean istouch = false;

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		mContentView = getChildAt(0);
		if (istouch) {
			if (//this.getScrollY() + this.getHeight() >= computeVerticalScrollRange()
				mContentView!=null && mContentView.getMeasuredHeight() <= getScrollY() + getHeight() ) {
				istouch = false;
				loadmoreOnScrollChanged.down();
			}
			if (t == 0) {
				istouch = false;
				loadmoreOnScrollChanged.up();
			}
		}

	}

}

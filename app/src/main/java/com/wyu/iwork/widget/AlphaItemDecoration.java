package com.wyu.iwork.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.wyu.iwork.R;
import com.wyu.iwork.model.Contact;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Util;

import java.util.List;

/**
 * @author HuaJian Jiang.
 *         Date 2017/1/3.
 */
public class AlphaItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = AlphaItemDecoration.class.getSimpleName();
    private static final int[] ATTRS = {R.attr.alphaDivider, R.attr.alphaSize, R.attr.alphaColor,
            R.attr.alphaMargin, android.R.attr.listDivider};
    private Rect mBounds = new Rect();
    public static final String[] ALPHA = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private Paint mAlphaPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Drawable mAlpha;
    private Drawable mDivider;
    private List<Contact> mData;
    private int mAlphaMargin;

    public AlphaItemDecoration(Context ctxt, List<Contact> contacts) {
        TypedArray a = ctxt.obtainStyledAttributes(ATTRS);
        mAlpha = a.getDrawable(a.getIndex(0));
        mDivider = a.getDrawable(a.getIndex(4));
        mData = contacts;
        mAlphaPaint.setColor(a.getColor(a.getIndex(2), Color.BLACK));
        mAlphaPaint.setTextSize(a.getDimensionPixelSize(a.getIndex(1), (int) Util.dp2px(ctxt, 14)));
        mAlphaMargin = a.getDimensionPixelSize(a.getIndex(3), (int) Util.dp2px(ctxt, 16));
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        c.save();
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        c.clipRect(left, parent.getPaddingTop(), right,
                parent.getHeight() - parent.getPaddingBottom());
        final int childCount = parent.getChildCount();

        int childAdapterPos;
        View child;
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            childAdapterPos = parent.getChildAdapterPosition(child);
            final String currAlpha = mData.get(childAdapterPos).getAlpha();
            final String beforeAlpha =
                    childAdapterPos != 0 ? mData.get(childAdapterPos - 1).getAlpha() : null;

            if(!TextUtils.isEmpty(currAlpha) && !TextUtils.isEmpty(beforeAlpha)){
                if (!currAlpha.equals(beforeAlpha)) {
                    Logger.e(TAG, "onDraw==>" + currAlpha);
                    parent.getDecoratedBoundsWithMargins(child, mBounds);
                    Logger.e(TAG, "bounds==>" + mBounds.flattenToString());
                    final int top = mBounds.top + Math.round(ViewCompat.getTranslationY(child));
                    final int bottom = top + mAlpha.getIntrinsicHeight();
                    mAlpha.setBounds(left, top, right, bottom);
                    mAlpha.draw(c);
                    final int txtY = (int) (top + mAlpha.getIntrinsicHeight() / 2 -
                                            (mAlphaPaint.descent() + mAlphaPaint.ascent()) / 2);
                    c.drawText(currAlpha, left + mAlphaMargin, txtY, mAlphaPaint);

                }
            }
        }
        c.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state)
    {
        final int childAdapterPos = parent.getChildAdapterPosition(view);
        final String currAlpha = mData.get(childAdapterPos).getAlpha();
        final String beforeAlpha =
                childAdapterPos != 0 ? mData.get(childAdapterPos - 1).getAlpha() : null;

        if(!TextUtils.isEmpty(currAlpha) && !TextUtils.isEmpty(beforeAlpha)){
            if (!currAlpha.equals(beforeAlpha)) {
                outRect.set(0, mAlpha.getIntrinsicHeight(), 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            }
        }
    }
}

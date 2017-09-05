package com.wyu.iwork.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.wyu.iwork.R;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class MyTabLayout extends RadioGroup {
    private static final String TAG = "MyTabLayout";

    private int mFrameColor;
    private float mFrameWidth;
    private float mCornerRadius;

    private Paint mFramePaint;
    private RectF mFrameRect;

    public MyTabLayout(Context context) {
        this(context,null);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta= context.obtainStyledAttributes(attrs,R.styleable.MyTabLayout);
        mFrameColor = ta.getColor(R.styleable.MyTabLayout_color_frame, 0xff000000);
        mFrameWidth = ta.getFloat(R.styleable.MyTabLayout_width_frame, dp2px(context,2));
        mCornerRadius = ta.getFloat(R.styleable.MyTabLayout_radius, dp2px(context,10));
        ta.recycle();
        init();
    }

    public static float dp2px(Context context, int value) {
        return context.getResources().getDisplayMetrics().density * value;
    }

    private Path p=new Path();
    private Paint pa=new Paint();

    private void init() {
        setWillNotDraw(false);
        mFramePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFramePaint.setColor(mFrameColor);
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setStrokeWidth(mFrameWidth);
        mFrameRect = new RectF();
        pa.setColor(mFrameColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mFrameRect.set(0, 0, w, h);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        final int childCount=getChildCount();

        p.reset();
//        p.addArc(0, canvas.getHeight(), mCornerRadius, canvas.getHeight()-mCornerRadius, 180, 90);
//        p.addArc(0,0,mCornerRadius, mCornerRadius,180,90);
//        p.lineTo(80, 0);
//        p.lineTo(80, getHeight());
//        p.rLineTo(-80 + mCornerRadius, getHeight());
        p.addArc(0,0,mCornerRadius, mCornerRadius,180,90);
        p.lineTo(80,0);
        p.lineTo(80,getHeight());
        p.lineTo(mCornerRadius,getHeight());
        p.addArc(0,getHeight()-mCornerRadius,mCornerRadius, getHeight(),90,90);
        p.lineTo(0,mCornerRadius);
        p.lineTo(mCornerRadius,getHeight());
        canvas.drawPath(p,pa);

        super.onDraw(canvas);
        canvas.drawRoundRect(mFrameRect, mCornerRadius, mCornerRadius, mFramePaint);




    }
}

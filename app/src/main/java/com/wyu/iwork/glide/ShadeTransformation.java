package com.wyu.iwork.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import jp.wasabeef.glide.transformations.CropTransformation;

/**
 * Created by lx on 2017/4/8.
 */

public class ShadeTransformation implements Transformation<Bitmap> {

    private Context mContext;
    private BitmapPool mBitmapPool;
    private int mWidth;
    private int mHeight;

    private CropTransformation.CropType mCropType = CropTransformation.CropType.CENTER;

    public ShadeTransformation(Context context) {
        this(Glide.get(context).getBitmapPool());
        mContext = context;
    }

    public ShadeTransformation(BitmapPool pool) {
        this.mBitmapPool = pool;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {


        Bitmap source = resource.get();

        Bitmap bitmap = mBitmapPool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setDither(true);

        canvas.drawBitmap(TransformationUtils.centerCrop(bitmap, source, outWidth, outHeight), null, new Rect(0, 0, outWidth, outHeight), paint);

        GradientDrawable drawable =
                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#80000000"), Color.parseColor("#00000000")});

        drawable.setBounds(0, 0, outWidth, outHeight);

        drawable.draw(canvas);

        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    @Override
    public String getId() {
        return "CropCircleTransformation()";
    }

    private float getTop(float scaledHeight) {
        switch (mCropType) {
            case TOP:
                return 0;
            case CENTER:
                return (mHeight - scaledHeight) / 2;
            case BOTTOM:
                return mHeight - scaledHeight;
            default:
                return 0;
        }
    }
}

package com.wyu.iwork.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

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

public class CropTopTransformation   implements Transformation<Bitmap> {


    private Context mContext;
    private BitmapPool mBitmapPool;
    private int mWidth;
    private int mHeight;

    private CropTransformation.CropType mCropType = CropTransformation.CropType.CENTER;

    public CropTopTransformation(Context context) {
        this(Glide.get(context).getBitmapPool());
        mContext = context;
    }

    public CropTopTransformation(BitmapPool pool) {
        this.mBitmapPool = pool;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {


        Bitmap source = resource.get();
        float sWidth = (float) source.getWidth();
        float sHeight = (float) source.getHeight();

        float wScale = outWidth / sWidth;

        float calculateHeight = wScale * sHeight + 0.5f;

        Bitmap fitBitmap = TransformationUtils.fitCenter(source, mBitmapPool, outWidth, (int) calculateHeight);

        int fWidth = fitBitmap.getWidth();
        int fHeight = fitBitmap.getHeight();

        Bitmap bitmap = mBitmapPool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        }

        Matrix matrix = new Matrix();
        if (fHeight > outHeight) {
            matrix.setTranslate(0, fHeight - outHeight);
        } else {
            matrix.setTranslate(0, outHeight - fHeight);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setDither(true);

        canvas.drawBitmap(fitBitmap, matrix, paint);

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

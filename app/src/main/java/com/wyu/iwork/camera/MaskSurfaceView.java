package com.wyu.iwork.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wyu.iwork.R;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.view.activity.RectCameraActivity;


public class MaskSurfaceView extends FrameLayout {

	private static final String TAG = MaskSurfaceView.class.getSimpleName();

	private MSurfaceView surfaceView;
	private MaskView imageView;
	private int width;
	private int height;
	private int maskWidth;
	private int maskHeight;
	private int screenWidth;
	private int screenHeight;
	
	public MaskSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		surfaceView = new MSurfaceView(context);
		imageView = new MaskView(context);
		this.addView(surfaceView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addView(imageView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
		CameraHelper.getInstance().setMaskSurfaceView(this);
	}
	
	public void setMaskSize(Integer width, Integer height){
		maskHeight = height;
		maskWidth = width;
		Logger.i(TAG,"setMaskSize:widith="+width+"height="+height);
	}
	
	public int[] getMaskSize(){
		return new MaskSize().size;
	}
	
	private class MSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
		private SurfaceHolder holder;
		public MSurfaceView(Context context) {
			super(context);
			this.holder = this.getHolder();
			//translucent半透明 transparent透明
			this.holder.setFormat(PixelFormat.TRANSPARENT);
			this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			this.holder.addCallback(this);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			Logger.i(TAG,"surfaceChanged:w="+w+" h :"+h);
			width = w;
			height = h;
			try {
				CameraHelper.getInstance().openCamera(holder, format, width, height, screenWidth, screenHeight);
			}catch (Exception e){
				((RectCameraActivity)getContext()).finish();
				MsgUtil.shortToastInCenter(getContext(),"请前往设置检查该应用拍照权限是否打开!");
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			CameraHelper.getInstance().releaseCamera();
		}
	}

	private class MaskSize{
		private int[] size;
		private MaskSize(){
			this.size = new int[]{maskWidth, maskHeight, width, height};
		}
	}
	
	private class MaskView extends View {
		private Paint linePaint;
		private Paint rectPaint;
		private Paint cornelPaint;
		public MaskView(Context context) {
			super(context);
			
//			绘制中间透明区域矩形边界的Paint
			linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			linePaint.setColor(context.getResources().getColor(R.color.white));
			linePaint.setStyle(Style.STROKE);
			linePaint.setStrokeWidth(3f);
			linePaint.setAlpha(80);

			//绘制四个角
			cornelPaint = new Paint();
			cornelPaint.setColor(context.getResources().getColor(R.color.colorGray03A9F4));
			cornelPaint.setStyle(Style.STROKE);
			cornelPaint.setStrokeWidth(6f);

			//绘制四周矩形阴影区域
			rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			rectPaint.setColor(context.getResources().getColor(R.color.colorGray66000000));
			rectPaint.setStyle(Style.FILL);
			rectPaint.setAlpha(20);
		}
		@Override
		protected void onDraw(Canvas canvas) {
			if(maskHeight==0 && maskWidth==0){
				return;
			}
			if(maskHeight==height || maskWidth==width){
				return;
			}
			
			if((height>width&&maskHeight<maskWidth) || (height<width&&maskHeight>maskWidth)){
				int temp = maskHeight;
				maskHeight = maskWidth;
				maskWidth = temp;
			}
			
			int h = Math.abs((height-maskHeight)/2)-100;
			int w = Math.abs((width-maskWidth)/2);
			Logger.i(TAG,"cutIMage:h="+h+"w="+w);
//			上
			canvas.drawRect(0, 0, width, h, this.rectPaint);
			Log.i("radiu","("+0+","+0+","+width+","+h+")");
//			右
			//canvas.drawRect(width-w, h, width, height-h, this.rectPaint);
			canvas.drawRect(width-w, h, width, height, this.rectPaint);
			Log.i("radiu","("+(width-w)+","+h+","+width+","+height+")");
//			下
			//canvas.drawRect(0, height-h, width, height, this.rectPaint);
			canvas.drawRect(0, h+maskHeight, width-w, height, this.rectPaint);
			Log.i("radiu","("+0+","+(h+maskHeight)+","+(width-h)+","+height+")");
//			左
			canvas.drawRect(0, h, w, h+maskHeight, this.rectPaint);
			Log.i("radiu","("+0+","+h+","+w+","+h+maskHeight+")");
			
			canvas.drawRect(w, h, w+maskWidth, h+maskHeight, this.linePaint);

			canvas.drawLine(w,h,w+30,h,this.cornelPaint);
			canvas.drawLine(w,h,w,h+30,this.cornelPaint);
			canvas.drawLine(w+maskWidth-30,h,w+maskWidth,h,this.cornelPaint);
			canvas.drawLine(w+maskWidth,h,w+maskWidth,h+30,this.cornelPaint);
			canvas.drawLine(w,h+maskHeight-30,w,h+maskHeight,this.cornelPaint);
			canvas.drawLine(w,h+maskHeight,w+30,h+maskHeight,this.cornelPaint);
			canvas.drawLine(w+maskWidth-30,h+maskHeight,w+maskWidth,h+maskHeight,this.cornelPaint);


			canvas.drawLine(w+maskWidth,h+maskHeight-30,w+maskWidth,h+maskHeight,this.cornelPaint);


			
			super.onDraw(canvas);
		}
	}
}

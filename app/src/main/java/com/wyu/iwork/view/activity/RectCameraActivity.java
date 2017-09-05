package com.wyu.iwork.view.activity;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.camera.CameraHelper;
import com.wyu.iwork.camera.MaskSurfaceView;
import com.wyu.iwork.camera.OnCaptureCallback;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.BusinessCardInfo;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.takephoto.TakePhoto;
import com.wyu.iwork.takephoto.model.InvokeParam;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class RectCameraActivity extends AppCompatActivity implements OnCaptureCallback,View.OnClickListener {

	private static final String TAG = RectCameraActivity.class.getSimpleName();
	private MaskSurfaceView surfaceview;
	private ImageView iv;
//	拍照
	//private Button btn_capture;
	private ImageView iv_takephoto;
	private ImageView flash;//闪光灯
	private TextView tv_picture;
	private TextView cancel;
	private AutoLinearLayout ll_option;
	private AutoLinearLayout ll_again_option;
	private AutoLinearLayout ll_again;
	private AutoLinearLayout ll_sure;
	private TextView tv_scan;
	private AutoRelativeLayout rl_backgroud;

	private AutoLinearLayout ll_loading;
	private TakePhoto takePhoto;

//	拍照后得到的保存的文件路径
	private String filepath;
	private Display mDisplay;
	private int mScreenHeight;
	private int mScreenWidth;
	private static final int CODE_RECT_CAMERA = 200;
	private Gson gson;
	private InvokeParam invokeParam;

	private static final String TYPE_CUSTOM = "NEW";//客户管理
	private static final String TYPE_POTENTIAL = "POTENTIAL_NEW";//潜在客户管理
	private static final String TYPE_SALES = "NEW_SALES";//新建销售线索
	private String type;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.layout_photo_test);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);

		// 自定义颜色
		tintManager.setTintColor(getResources().getColor(R.color.colorBlack));
		getExtras();
		initView();
		initMaskSurfaceView();
		initClick();
	}

	@TargetApi(19)
	protected void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	private void getExtras(){
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		//initMaskSurfaceView();
		super.onResume();
	}

	private void initClick(){
		iv_takephoto.setOnClickListener(this);//拍照
		tv_picture.setOnClickListener(this);//相册
		cancel.setOnClickListener(this);//取消
		flash.setOnClickListener(this);//闪光灯
		ll_again.setOnClickListener(this);//重拍
		ll_sure.setOnClickListener(this);//确定
	}

	private void initMaskSurfaceView(){
		mDisplay = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		mScreenHeight = mDisplay.getHeight()*5/9;
		mScreenWidth = mDisplay.getWidth()*5/9;
		//		设置矩形区域大小
		Logger.i(TAG,"mScreenHeight:"+mScreenHeight+"mScreenWidth:"+mScreenWidth);
		this.surfaceview.setMaskSize(mScreenHeight, mScreenWidth);
	}

	private void initView(){

		surfaceview = (MaskSurfaceView) findViewById(R.id.msv_surface);
		flash = (ImageView) findViewById(R.id.iv_flash);
		iv = (ImageView) findViewById(R.id.image_view);
		ll_option = (AutoLinearLayout) findViewById(R.id.ll_option);
		iv_takephoto = (ImageView) findViewById(R.id.iv_takephoto);
		tv_picture = (TextView) findViewById(R.id.tv_picture);
		cancel = (TextView) findViewById(R.id.tv_cancel);
		ll_again_option = (AutoLinearLayout) findViewById(R.id.ll_again_option);
		ll_again = (AutoLinearLayout) findViewById(R.id.ll_again);
		ll_sure = (AutoLinearLayout) findViewById(R.id.ll_sure);
		ll_loading = (AutoLinearLayout) findViewById(R.id.ll_loading);
		tv_scan = (TextView) findViewById(R.id.tv_scan);
		rl_backgroud = (AutoRelativeLayout) findViewById(R.id.rl_backgroud);
		//进入界面   闪光灯显示   显示拍照按钮
		//初始化界面
		flash.setVisibility(View.VISIBLE);
		ll_option.setVisibility(View.VISIBLE);
		ll_again_option.setVisibility(View.GONE);
	}
	/**
	 * 删除图片文件呢
	 */
	private void deleteFile(){
		if(this.filepath==null || this.filepath.equals("")){
			return;
		}
		File f = new File(this.filepath);
		if(f.exists()){
			f.delete();
		}
	}

	@Override
	public void onCapture(boolean success, String filepath) {
		this.filepath = filepath;
		String message = "拍照成功";
		if(!success){
			message = "拍照失败";
			CameraHelper.getInstance().startPreview();
			iv.setVisibility(View.GONE);
			rl_backgroud.setVisibility(View.GONE);
			this.surfaceview.setVisibility(View.VISIBLE);
		}else{
			iv.setVisibility(View.VISIBLE);
			rl_backgroud.setVisibility(View.VISIBLE);
			this.surfaceview.setVisibility(View.GONE);
			ll_again_option.setVisibility(View.VISIBLE);
			ll_option.setVisibility(View.GONE);
			flash.setVisibility(View.GONE);
			tv_scan.setVisibility(View.GONE);
			iv.setImageBitmap(BitmapFactory.decodeFile(filepath));
		}
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	private boolean isflag = false;
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.iv_takephoto://拍照
				CameraHelper helper = CameraHelper.getInstance();
				helper.tackPicture(RectCameraActivity.this);

				break;
			case R.id.tv_picture://打开相册
				startAlbum();
				break;
			case R.id.tv_cancel://取消拍摄
				deleteFile();
				finish();
				break;
			case R.id.iv_flash://闪光灯
				isflag = !isflag;
				break;
			case R.id.ll_again://重拍
				ll_option.setVisibility(View.VISIBLE);
				ll_again_option.setVisibility(View.GONE);
				flash.setVisibility(View.VISIBLE);
				iv.setVisibility(View.GONE);
				rl_backgroud.setVisibility(View.GONE);
				tv_scan.setVisibility(View.VISIBLE);
				surfaceview.setVisibility(View.VISIBLE);
				deleteFile();
				CameraHelper.getInstance().startPreview();

				break;
			case R.id.ll_sure://确定
				//压缩图片
				//CustomUtils.compressBitmap(BitmapFactory.decodeFile(filepath));
				compressBitmap();
				//uploadImage();
				break;
		}
	}

	private void compressBitmap(){
		ll_loading.setVisibility(View.VISIBLE);
		tv_scan.setVisibility(View.VISIBLE);
		tv_scan.setText("正在扫描中......");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Logger.i("compress",BitmapFactory.decodeFile(filepath).getByteCount()+"");
				Logger.i("compress","filepath=="+filepath);
				//Bitmap map = CustomUtils.compressFile(filepath,RectCameraActivity.this);
				//Bitmap map = CustomUtils.compressBitmap(BitmapFactory.decodeFile(filepath));
				final File file = CustomUtils.ratio(BitmapFactory.decodeFile(filepath),RectCameraActivity.this,100);
				 //File file = CustomUtils.saveBitmap(map);
				Logger.i("compress","fileSize=="+file.length());
				Logger.i("compress","file="+file.getAbsolutePath());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						uploadImage(file);
					}
				});
			}
		}).start();
	}

	private static final int ALBUM_OK = 0;

	private void startAlbum(){
		Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
		albumIntent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(albumIntent, ALBUM_OK);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (ALBUM_OK == requestCode) {
			rl_backgroud.setVisibility(View.VISIBLE);
			iv.setVisibility(View.VISIBLE);
			this.surfaceview.setVisibility(View.GONE);
			ll_again_option.setVisibility(View.VISIBLE);
			ll_option.setVisibility(View.GONE);
			flash.setVisibility(View.GONE);
			Toast.makeText(this, "OK now", Toast.LENGTH_SHORT).show();
			Bitmap bitmap;
			ContentResolver cr = this.getContentResolver();
			if(data == null)
				return;
			Uri uri = data.getData();

			try {
				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				rl_backgroud.setVisibility(View.VISIBLE);
				iv.setVisibility(View.VISIBLE);
				surfaceview.setVisibility(View.GONE);
				iv.setImageBitmap(bitmap);
				Logger.i(TAG,getImagePath(uri));//这是用来读取图片的exif
				filepath = getImagePath(uri);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(requestCode == CODE_RECT_CAMERA && resultCode == 200){
			finish();
		}
	}

	//获取图片的资源路径
	private String getImagePath(Uri uri) {
		if (null == uri) {
			Logger.e("getImagePath", "uri return null");
			return null;
		}

		Logger.e("getImagePath", uri.toString());
		String path = null;
		final String scheme = uri.getScheme();
		if (null == scheme) {
			path = uri.getPath();
		} else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			path = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(uri, proj, null, null,
					null);
			int nPhotoColumn = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (null != cursor) {
				cursor.moveToFirst();
				path = cursor.getString(nPhotoColumn);
			}
			cursor.close();
		}

		return path;
	}

	private void uploadImage(File file){
		UserInfo user = AppManager.getInstance(this).getUserInfo();
		String F = Constant.F;
		String V = Constant.V;
		String RandStr = CustomUtils.getRandStr();
		//File file = new File(filepath);
		String Sign = Md5Util.getSign(F+V+RandStr+user.getUser_id());
		/**
		 * user_id	是	int[11]
		 用户ID
		 face	是	obj
		 [ post用户头像 ] 图片类型:jpg,png,gif 图片大小:小于3M
		 F	是	string[18]
		 请求来源：IOS/ANDROID/WEB
		 V	是	string[20]
		 版本号如：1.0.1
		 RandStr	是	string[50]
		 请求加密随机数 time().|.rand()
		 Sign	是	string[400]
		 请求加密值 F_moffice_encode(F.V.RandStr.user_id)
		 */
		HashMap<String,String> data = new HashMap<>();
		data.put("user_id",user.getUser_id());
		data.put("F",F);
		data.put("V",V);
		data.put("RandStr",RandStr);
		data.put("Sign",Sign);
		String murl = RequestUtils.getRequestUrl(Constant.URL_BUSINESS_CARD_INFO,data);
		Logger.i(TAG,murl);
		OkGo.post(murl).tag(this)
				.params("face",file)
				.execute(new DialogCallback(this) {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						super.onSuccess(s,call,response);
						Logger.i(TAG,s);
						ll_loading.setVisibility(View.GONE);
						tv_scan.setText("请将名片放入扫描区域中......");
						tv_scan.setVisibility(View.GONE);
						parseData(s);
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						super.onError(call, response, e);
						Logger.i(TAG,e.toString());
					}


				});
	}



	private void parseData(String data){
		try {
			if(gson == null){
				gson = new Gson();
			}
			BusinessCardInfo info = gson.fromJson(data,BusinessCardInfo.class);

			if(info.getCode().equals("0")){
				Intent i = null;
				Bundle bundle = new Bundle();
				bundle.putSerializable("info",info);
				if(TYPE_CUSTOM.equals(type)){
					i = new Intent(this,CrmCustomDetailActivity.class);
					i.putExtras(bundle);
					i.putExtra("type","NEW_RECT");
					startActivity(i);
					finish();
				}else if(TYPE_POTENTIAL.equals(type)){
					i = new Intent(this,CrmCustomDetailActivity.class);
					i.putExtras(bundle);
					i.putExtra("type","POTENTIAL_NEW_RECT");
					startActivity(i);
					finish();
				}else if(TYPE_SALES.equals(type)){
					i = new Intent(this,SalesLeadDetailActivity.class);
					i.putExtras(bundle);
					i.putExtra("type","NEW_RECT");
					startActivity(i);
					finish();
				}else{
					i = new Intent(this,CardEditNewActivity.class);
					i.putExtras(bundle);
					i.putExtra("type","3");
					i.putExtra("id","-1");
					startActivityForResult(i,CODE_RECT_CAMERA);
					finish();
				}


			}else{
				MsgUtil.shortToastInCenter(this,info.getMsg());
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}


}

package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.AdTaskSettingAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.AdDetailModel;
import com.wyu.iwork.model.AdTaskModel;
import com.wyu.iwork.takephoto.TakePhoto;
import com.wyu.iwork.takephoto.TakePhotoImpl;
import com.wyu.iwork.takephoto.model.InvokeParam;
import com.wyu.iwork.takephoto.model.TContextWrap;
import com.wyu.iwork.takephoto.model.TResult;
import com.wyu.iwork.takephoto.permission.InvokeListener;
import com.wyu.iwork.takephoto.permission.PermissionManager;
import com.wyu.iwork.takephoto.permission.TakePhotoInvocationHandler;
import com.wyu.iwork.util.AdgetAndReleaseTaskUtil;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.ImageUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.UpLoadFileUtil;
import com.wyu.iwork.view.dialog.CemaraDialog;
import com.wyu.iwork.view.dialog.ThreeItemDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 *         广告联盟 任务设置
 */

public class AdTaskSettingsActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {

    private static String TAG = AdTaskSettingsActivity.class.getSimpleName();

    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;

    /**
     * 广告图片
     */
    @BindView(R.id.activity_ad_task_settings_ad_image)
    ImageView ad_image;

    /**
     * 广告标题
     */
    @BindView(R.id.activity_ad_task_settings_ad_name)
    TextView ad_title;

    /**
     * 广告商家
     */
    @BindView(R.id.activity_ad_task_settings_ad_company)
    TextView ad_company;

    /**
     * 任务价格
     */
    @BindView(R.id.activity_ad_task_settings_ad_price)
    TextView ad_price;

    /**
     * 广告位置
     */
    @BindView(R.id.activity_ad_task_settings_ad_addr)
    TextView ad_addr;

    /**
     * 文章标题
     */
    @BindView(R.id.activity_ad_task_settings_title)
    EditText ad_article_title;
    /**
     * NestedScrollView
     */
    @BindView(R.id.activity_ad_task_settings_nested)
    NestedScrollView scrollView;


    @BindView(R.id.activity_ad_task_settings_recyclerview)
    RecyclerView recyclerView;

    private List<AdTaskModel> list = null;

    private AdTaskSettingAdapter adapter;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private int index = -1;

    private String ad_sense_id = "";//广告任务id

    private LinearLayout camera, probar;

    AdDetailModel model = null;//广告详情

    private ThreeItemDialog threeItemDialog;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));

        }
        return takePhoto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_task_settings);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        getdata();
        init();
    }

    /**
     * 获取相关信息
     */
    private void getdata() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ad_sense_id = bundle.getString("id");
        }
    }

    /**
     * 初始化
     */
    private void init() {
        title.setText("任务设置");
        edit.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScrollSmooth(recyclerView);
        list = new ArrayList<>();
        list.add(new AdTaskModel("", 1));
        adapter = new AdTaskSettingAdapter(this, list);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        getAdDetail();

        threeItemDialog = new ThreeItemDialog(this, new ThreeItemDialog.TwoItemClickListener() {
            @Override
            public void TopClick(Dialog dialog) {
                ad_addr.setText("文章顶部");
            }

            @Override
            public void BottomClick(Dialog dialog) {
                ad_addr.setText("文章底部");
            }
        });
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.action_back, R.id.activity_ad_task_settings_insert_charts, R.id.activity_ad_task_settings_insert_images, R.id.activity_ad_task_settings_commit, R.id.activity_ad_task_settings_ad_station})
    void click(View view) {
        switch (view.getId()) {
            case R.id.action_back://返回
                onBackPressed();
                break;
            case R.id.activity_ad_task_settings_insert_charts://插入文字
                getAdapterContent();
                list.add(new AdTaskModel("", 1));
                adapter.notifyDataSetChanged();
                break;
            case R.id.activity_ad_task_settings_insert_images://插入图片
                getAdapterContent();
                list.add(new AdTaskModel("", 2));
                adapter.notifyDataSetChanged();
                break;
            case R.id.activity_ad_task_settings_ad_station://广告位置
                if (threeItemDialog!=null){
                    threeItemDialog.show();
                }
                break;
            case R.id.activity_ad_task_settings_commit://领取任务并提交
                getAdapterContent();
                String first_msg = list.get(0).getMsg();
                int station = 0;
                String addr = ad_addr.getText().toString();
                if (addr.equals("请选择")) {
                    Toast.makeText(this, "请选择广告位置", Toast.LENGTH_SHORT).show();
                    break;
                } else {

                    if (addr.equals("文章顶部")) {//文章顶部
                        station = 1;
                    } else if (addr.equals("文章底部")) {//文章底部
                        station = 2;
                    }
                }

                String title = ad_article_title.getText().toString();

                List<AdTaskModel> templist = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (!list.get(i).getMsg().isEmpty()) {
                        templist.add(list.get(i));
                    }
                }

                String article_details = JSON.toJSONString(templist);
                templist.clear();
                templist = null;
                String article_detail = article_details.replace("\"", "\\" + "\"");
                Logger.e("article_details", article_details);
                Logger.e("article_detail", article_detail);
                if (title.isEmpty()) {
                    Toast.makeText(this, "请输入文章标题", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (first_msg.isEmpty()) {
                    Toast.makeText(this, "请至少输入一段文字", Toast.LENGTH_SHORT).show();
                    break;
                }

                AdgetAndReleaseTaskUtil.initParama(this, ad_sense_id, station, title, article_details, callback());
                break;
        }
    }


    /**
     * 获取adapter的文本内容
     */

    private void getAdapterContent() {
        int count = adapter.getItemCount();
        int type;
        for (int i = 0; i < count; i++) {
            type = adapter.getItemViewType(i);
            if (type == 1) {
                EditText editText = (EditText) recyclerView.getChildAt(i).findViewById(R.id.item_ad_task_settings_content);
                String content = editText.getText().toString();
                list.get(i).setMsg(content);
            }
        }
    }


    //打开相册
    private void startGallery() {
        takePhoto.onPickFromGallery();
    }

    //打开相机
    private void startCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        takePhoto.onPickFromCapture(imageUri);
    }

    //成功
    @Override
    public void takeSuccess(TResult result) {
        if (result != null) {
            probar.setVisibility(View.VISIBLE);
            final String path = result.getImage().getOriginalPath();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final File file = CustomUtils.ratio(BitmapFactory.decodeFile(path), AdTaskSettingsActivity.this, 1024);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UpLoadFileUtil.init(AdTaskSettingsActivity.this, file, upLoadCallBack(path));
                        }
                    });
                }
            }).start();
        }

    }

    //失败
    @Override
    public void takeFail(TResult result, String msg) {

    }

    //取消
    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    //获取图片
    public void showGetPicPop(int position, LinearLayout cameral, LinearLayout probarl) {
        camera = cameral;
        probar = probarl;
        index = position;
        new CemaraDialog(this, new CemaraDialog.DialogClickListener() {
            @Override
            public void cemaraClick(Dialog dialog) {
                getPermission();
            }

            @Override
            public void galaryClick(Dialog dialog) {
                startGallery();
            }
        }).show();

    }

    /**
     * 申请权限
     */
    private void getPermission() {
        AndPermission.with(this)
                .requestCode(100)
                .permission(Permission.CAMERA)
                .rationale(rationaleListener)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        // 权限申请成功回调。
                        // 这里的requestCode就是申请时设置的requestCode。
                        // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
                        if (requestCode == 100) {
                            startCamera();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        // 权限申请失败回调。
                        if (requestCode == 100) {
                            // TODO ...
                            AndPermission.defaultSettingDialog(AdTaskSettingsActivity.this, 400).show();
                        }
                    }
                })
                .start();
    }

    RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
            AndPermission.rationaleDialog(AdTaskSettingsActivity.this, rationale).show();
        }
    };


    /**
     * 获取广告详情
     */
    private void getAdDetail() {
        String ad_url = Constant.URL_AD_DETAIL;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + ad_sense_id);
        HttpParams map = new HttpParams();
        map.put("ad_sense_id", ad_sense_id);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        OkGo.get(ad_url)
                .tag(TAG)
                .params(map)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.e(TAG, e.getMessage());
                        Toast.makeText(AdTaskSettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            Logger.e(TAG, s);
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            String msg = object.optString("msg");
                            String data = object.optString("data");
                            if ("0".equals(code)) {
                                model = JSON.parseObject(data.toString(), AdDetailModel.class);
                                ImageUtils.LoadImageFitWH(AdTaskSettingsActivity.this, model.getAd_pic_url(), ad_image);//广告图
                                ad_title.setText(model.getTitle());//广告标题
                                ad_company.setText(model.getCompany_name());//广告商家
                                ad_price.setText(model.getUnit_price());//广告价格
                            } else {
                                Toast.makeText(AdTaskSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 上传图片的回调
     */
    private StringCallback upLoadCallBack(final String path) {
        return new StringCallback() {

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Logger.e(TAG, e.getMessage());
                probar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    probar.setVisibility(View.GONE);
                    Logger.e(TAG, s);
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    String msg = object.optString("msg");
                    String data = object.optString("data");
                    if ("0".equals(code)) {
                        list.get(index).setLocal_path(path);
                        list.get(index).setMsg(data);
                        adapter.setData(list);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdTaskSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                super.upProgress(currentSize, totalSize, progress, networkSpeed);
                camera.setVisibility(View.GONE);
            }
        };
    }

    /**
     * 领取并发布任务
     */

    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                Logger.e(TAG, e.getMessage());
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.e(TAG, s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    String msg = object.optString("msg");
                    JSONObject data = object.optJSONObject("data");
                    if ("0".equals(code)) {
                        Intent intent = new Intent(AdTaskSettingsActivity.this, DrawSuccessActivity.class);
                        intent.putExtra("id", data.optString("id"));
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(AdTaskSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }

}

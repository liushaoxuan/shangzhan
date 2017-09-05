package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lx on 2017/2/7.
 * 新建动态——fragment
 */

public class BuildDynamicfragment extends Fragment {

    //发布内容
    @BindView(R.id.activity_build_dynamic_content)
    EditText release_content;
    //添加图片
    @BindView(R.id.activity_build_dynamic_picture)
    ImageView add_picture;
    //艾特人
    @BindView(R.id.activity_build_dynamic_aite)
    ImageView aite;
    //特殊符号
    @BindView(R.id.activity_build_dynamic_special)
    ImageView special_;

    UserInfo userInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = MyApplication.userInfo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.activity_build_dynamic,null);
        ButterKnife.bind(this,view);
        return view;
    }


    /**
     * 注入点击事件
     */
    @OnClick({  R.id.activity_build_dynamic_picture,R.id.activity_build_dynamic_back,R.id.activity_build_dynamic_release,
            R.id.activity_build_dynamic_aite, R.id.activity_build_dynamic_special})
    void Click(View view){
        switch (view.getId()){
            case R.id.activity_build_dynamic_picture://添加图片
                break;
            case R.id.activity_build_dynamic_aite://艾特指定的人
                break;
            case R.id.activity_build_dynamic_special://特殊符号
                break;

            case R.id.activity_build_dynamic_back:
                getActivity().onBackPressed();
                break;
            case R.id.activity_build_dynamic_release://发布动态
                String text = release_content.getText().toString().trim();
                if (text==null ||"".equals(text)){
                    Toast.makeText(getActivity(),"请输入要发布的内容",Toast.LENGTH_LONG).show();
                    break;
                }
                releaseDynamic(text);
                break;
        }
    }

    /**
     * 发布动态
     */
    private void releaseDynamic(String text){
        String url = Constant.URL_ADD_DYNAMIC;
        String F = Constant.F;
        String V = Constant.V;
        String user_id = userInfo==null?"":userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F+V+RandStr+user_id+text);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("text", text);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url,map);
        OkGo.get(url)
                .tag("DynamicFragment")
                .cacheMode(CacheMode.DEFAULT)
                .params("user_id",user_id)
                .params("text",text)
                .params("F",F)
                .params("V",V)
                .params("RandStr",RandStr)
                .params("Sign",sign)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)){
                                Toast.makeText(getActivity(),object.optString("msg"),Toast.LENGTH_LONG).show();
                                getActivity().finish();
                            }else {
                                Toast.makeText(getActivity(),object.optString("msg"),Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Logger.e("response------------>",s);
                    }
                });

    }
}

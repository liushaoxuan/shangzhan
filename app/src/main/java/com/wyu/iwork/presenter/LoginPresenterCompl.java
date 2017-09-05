package com.wyu.iwork.presenter;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.interfaces.ILoginView;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.SharedpreferencesHelp;
import com.wyu.iwork.view.activity.LoginActivity;
import com.wyu.iwork.view.activity.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.LinkedHashMap;
import java.util.Map;
import okhttp3.Call;

/**
 * Created by lx on 2016/12/26.
 */

public class LoginPresenterCompl implements ILoginPresnter {
    ILoginView iLoginView;

    public LoginPresenterCompl(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
    }

    @Override
    public void clear() {
        iLoginView.onClearText();
    }

    @Override
    public void doLogin(final LoginActivity context,String name, String passwd) {
        String radom = Md5Util.getRandom() + "";
        String RandStr = System.currentTimeMillis() + "|" + radom;
        String F = Constant.F;
        String V = Constant.V;
        String sign = Md5Util.getSign(F + V + RandStr + name + Md5Util.getMD5(passwd));
        String url = Constant.URL_SIGN_IN;
        OkGo.get(url)
                .tag("LoginActivity")
                .cacheMode(CacheMode.DEFAULT)
                .params("user_name",name)
                .params("user_pass",Md5Util.getMD5(passwd))
                .params("F", Constant.F)
                .params("V", Constant.V)
                .params("RandStr",RandStr)
                .params("Sign",sign)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)){
                                JSONObject data = object.optJSONObject("data");
                                String user_id = data.optString("user_id");
                                String user_name = data.optString("user_name");
                                String nick_name = data.optString("nick_name");
                                String user_face_img = data.optString("user_face_img");
                                String user_top_img = data.optString("user_top_img");
                                String is_admin = data.optString("is_admin");

                                Map<String,String> map = new LinkedHashMap<>();
                                map.put("user_id",user_id);
                                map.put("user_name",user_name);
                                map.put("nick_name",nick_name);
                                map.put("user_face_img",user_face_img);
                                map.put("user_top_img",user_top_img);
                                map.put("is_admin",is_admin);

                                SharedpreferencesHelp.save(context,"login_config",map);
                                Intent intent = new Intent(context,MainActivity.class);
                                context.startActivity(intent);
                                context.finish();

                            }else {
                                Toast.makeText(context,object.optString("msg"),Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Logger.e("response------------>",s);
                    }
                });
    }
}

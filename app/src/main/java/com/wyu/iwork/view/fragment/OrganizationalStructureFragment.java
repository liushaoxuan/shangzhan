package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.OrganizeParentAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.OrganizedModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by sxliu on 2017/4/12.
 * 组织架构
 */

public class OrganizationalStructureFragment extends Fragment {

    private static String TAG = OrganizationalStructureFragment.class.getSimpleName().toString();
    /**
     * 公司头像
     */
    @BindView(R.id.activity_organizational_structure_company_image)
    ImageView companyimage;
    /**
     * 公司名称
     */
    @BindView(R.id.activity_organizational_structure_company_name)
    TextView  companyName;
    /**
     * 标题
     */
    @BindView(R.id.layout_my_title_bar_title)
    TextView  title;
    /**
     * 菜单
     */
    @BindView(R.id.layout_my_title_bar_save)
    TextView  menu;

    @BindView(R.id.activity_organizational_structure_recyclerview)
    RecyclerView recyclerView;
    private OrganizeParentAdapter adapter;
    private List<OrganizedModel> list;
    private UserInfo userInfo;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        userInfo = MyApplication.userInfo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_organizational_structure,container,false);
        ButterKnife.bind(this,view);
        companyName.setText(userInfo.getCompany());
        menu.setVisibility(View.GONE);
        title.setText("组织架构");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getDataFromServer();
        return view;
    }

    @OnClick(R.id.layout_my_title_bar_back)
    void Click(){
        getActivity().onBackPressed();
    }

    public void getDataFromServer(){
        UserInfo user = MyApplication.userInfo;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        Logger.i(TAG,RandStr);
        String Sign = Md5Util.getSign(F+V+RandStr+user.getCompany_id());

        HashMap<String,String> data = new HashMap<>();
        data.put("company_id",user.getCompany_id());
        data.put("F",F);
        data.put("V",V);
        data.put("Sign",Sign);
        data.put("RandStr",RandStr);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(Constant.URL_COMMU_ORGANZ, data);
        OkGo.get(murl).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)){
                        list.clear();
                        JSONArray array = object.optJSONArray("data");
                        List<OrganizedModel> templist = new ArrayList<OrganizedModel>();
                        templist = JSON.parseArray(array.toString(),OrganizedModel.class);
                        list.addAll(templist);
                        adapter = new OrganizeParentAdapter(getActivity(),list);
                        recyclerView.setAdapter(adapter);
                        templist.clear();
                        templist = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

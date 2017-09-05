package com.wyu.iwork.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.view.activity.FinderCustomerListActivity;
import com.wyu.iwork.widget.MyCustomDialogDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lx on 2017/6/7.
 * crm - 公司查重
 */

public class CompanyDuplicateFragment extends Fragment {

    private static final String TAG = CompanyDuplicateFragment.class.getSimpleName();
    @BindView(R.id.duplicate_company_name)
    EditText duplicate_company_name;

    @BindView(R.id.duplicate_search)
    TextView duplicate_search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_duplicate,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick({R.id.duplicate_search})
    void Click(View v){
        switch (v.getId()){
            case R.id.duplicate_search:
                finderCustomer();
                break;
        }
    }

    private void finderCustomer(){
        //判断姓名和电话是否为空
        if(TextUtils.isEmpty(duplicate_company_name.getText().toString())){
            showRedmineDialog();
        }else{
            Intent intent = new Intent(getActivity(), FinderCustomerListActivity.class);
            intent.putExtra("type",1);
            intent.putExtra("name",duplicate_company_name.getText().toString());
            startActivity(intent);
        }
    }

    //提醒弹窗
    private void showRedmineDialog(){
        new MyCustomDialogDialog(5, getActivity(), R.style.MyDialog, "您查询的客户信息未填写\n请完成填写并查询", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }
}

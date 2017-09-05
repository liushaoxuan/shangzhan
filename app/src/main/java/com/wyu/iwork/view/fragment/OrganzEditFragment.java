package com.wyu.iwork.view.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.UniversalCallback;
import com.wyu.iwork.model.OrganzInfo;
import com.wyu.iwork.model.OrganzSelectorItem;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.presenter.OrganzEditPresenter;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.view.activity.OrganzLevelSelectorActivity;
import com.wyu.iwork.view.dialog.InputDialog;

import org.json.JSONObject;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/27.
 */
public class OrganzEditFragment extends BaseFragment<OrganzInfo> implements UniversalCallback {
    private static final String TAG = OrganzEditFragment.class.getSimpleName();
    private TextView mId;
    private TextView mName;
    private TextView mJump;
    private TextView mTel;
    private TextView mFax;
    private TextView mAddr;
    private OrganzEditPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNetPresenter(mPresenter = new OrganzEditPresenter(getActivity(), getArguments()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, (ViewGroup) inflater
                .inflate(R.layout.fragment_organz_edit, container, false), savedInstanceState);
    }

    @Override
    protected void onInitView(View rootView) {
        mId = (TextView) rootView.findViewById(R.id.id);
        mName = (TextView) rootView.findViewById(R.id.name);
        mJump = (TextView) rootView.findViewById(R.id.jump);
        mTel = (TextView) rootView.findViewById(R.id.tel);
        mFax = (TextView) rootView.findViewById(R.id.fax);
        mAddr = (TextView) rootView.findViewById(R.id.addr);
        mJump.setOnClickListener(this);
        mName.setOnClickListener(this);
        mTel.setOnClickListener(this);
        mFax.setOnClickListener(this);
        mAddr.setOnClickListener(this);
        Bundle args = getArguments();
        OrganzInfo info =
                args != null ? (OrganzInfo) args.getSerializable(Constant.KEY_ENTITY) : null;
        mJump.setTag(info != null ? info.getDepartment_superior_id() : "0");
        mId.setTag("0");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.jump:
                selectUpOrganz();
                break;
            case R.id.name:
            case R.id.tel:
            case R.id.fax:
            case R.id.addr:
                modifyInfo(v);
                break;
        }
    }

    private void selectUpOrganz() {
        UserInfo userInfo = MyApplication.userInfo;
        Bundle args = new Bundle();
        args.putString(Constant.KEY_ID, userInfo != null ? userInfo.getCompany_id() : "-1");
        args.putString(Constant.KEY_ID_2, (String) mId.getTag());
        Intent jump = new Intent(getActivity(), OrganzLevelSelectorActivity.class);
        jump.putExtras(args);
        startActivityForResult(jump, Constant.REQUEST_ENTITY);
    }

    private void modifyInfo(View v) {
        InputDialog inputDialog = new InputDialog();
        Bundle args2 = new Bundle();
        args2.putString(Constant.KEY_PARAM, getString(R.string.hint_input));
        args2.putInt(Constant.KEY_ID, v.getId());
        inputDialog.setCallback(this);
        inputDialog.setArguments(args2);
        inputDialog.show(getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == Constant.REQUEST_ENTITY) {
            OrganzSelectorItem selectedOrganzInfo = (OrganzSelectorItem) data
                    .getSerializableExtra(Constant.KEY_ENTITY);
            mJump.setTag(selectedOrganzInfo.getDepartment_id());
            mJump.setText(selectedOrganzInfo.getDepartment());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveInfo();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void saveInfo() {
        OrganzInfo organzInfo = mPresenter.getOrganzInfo();
        if (organzInfo == null) organzInfo = new OrganzInfo();
        organzInfo.setDepartment_superior_id((String) mJump.getTag());
        organzInfo.setDepartment(mName.getText().toString());
        organzInfo.setTel(mTel.getText().toString());
        organzInfo.setFax(mFax.getText().toString());
        organzInfo.setAddress(mAddr.getText().toString());
        mPresenter.updateInfo(organzInfo);
    }

    @Override
    public Object onFinished(Object... backParams) {
        if ((Integer) backParams[0] == DialogInterface.BUTTON_POSITIVE) {
            String value = (String) backParams[2];
            switch ((Integer) backParams[1]) {
                case R.id.name:
                    mName.setText(value);
                    break;
                case R.id.tel:
                    mTel.setText(value);
                    break;
                case R.id.fax:
                    mFax.setText(value);
                    break;
                case R.id.addr:
                    mAddr.setText(value);
                    break;
            }
        }
        return null;
    }

    @Override
    public void onSuccess(OrganzInfo data, JSONObject origin) {
        super.onSuccess(data, origin);
        if (data != null) {
            //填充加载的部门信息
            populateInfo(data);
        } else {
            MsgUtil.shortToast(getActivity(), R.string.save_success);
            getActivity().finish();
        }
    }

    @Override
    public void onFailure(int errorCode, String errorMsg) {
        //添加信息后保存失败
        if (getArguments() == null) {
            MsgUtil.shortToast(getActivity(), R.string.txt_input_error);
            return;
        }
        //请求信息失败
        super.onFailure(errorCode, errorMsg);
    }

    private void populateInfo(OrganzInfo data) {
        mId.setTag(data.getDepartment_id());
        mId.setText(data.getDepartment_id());
        mName.setText(data.getDepartment());
        mJump.setText(data.getDepartment_superior());
        mTel.setText(data.getTel());
        mFax.setText(data.getFax());
        mAddr.setText(data.getAddress());
    }
}

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

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.UniversalCallback;
import com.wyu.iwork.model.OrganzManageBase;
import com.wyu.iwork.model.OrganzUserRoleSelectorItem;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.presenter.OrganzUserEditPresenter;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.view.activity.OrganzSecSelectorActivity;
import com.wyu.iwork.view.activity.OrganzUserRoleSelectorActivity;
import com.wyu.iwork.view.dialog.InputDialog;

import org.json.JSONObject;

import static com.wyu.iwork.util.Constant.VALUE_TYPE_U;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/29.
 */
public class OrganzUserEditFragment extends BaseFragment<UserInfo> implements UniversalCallback {
    private static final String TAG = OrganzUserEditFragment.class.getSimpleName();
    private TextView phone;
    private TextView name;
    private TextView jump_organz;
    private TextView jump_role;
    private TextView sex;
    private OrganzUserEditPresenter mPresenter;
    private String mActionType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mActionType = args != null ? args.getString(Constant.KEY_ACTION_TYPE) : "";
        Logger.e(TAG, "mActionType=" + mActionType);
        setNetPresenter(mPresenter = new OrganzUserEditPresenter(getActivity(), args));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        return super.onCreateView(inflater,
                (ViewGroup) inflater.inflate(R.layout.fragment_organz_user_edit, container, false),
                savedInstanceState);
    }

    @Override
    protected void onInitView(View rootView) {
        phone = (TextView) rootView.findViewById(R.id.phone);
        name = (TextView) rootView.findViewById(R.id.name);
        jump_organz = (TextView) rootView.findViewById(R.id.jump_organz);
        jump_role = (TextView) rootView.findViewById(R.id.jump_role);
        sex = (TextView) rootView.findViewById(R.id.sex);
        name.setOnClickListener(this);
        jump_organz.setOnClickListener(this);
        jump_role.setOnClickListener(this);
        sex.setOnClickListener(this);
    }

    @Override
    public void onSuccess(UserInfo data, JSONObject origin) {
        super.onSuccess(data, origin);
        final String actionType = mActionType;
        if (actionType.equals(Constant.VALUE_TYPE_R)) {
            inflateUserInfo(data);
        } else if (actionType.equals(VALUE_TYPE_U)) {
            MsgUtil.shortToast(getActivity(), R.string.save_success);
            getActivity().finish();
        }
    }

    private void updateUserInfo() {
        UserInfo info = new UserInfo();
        info.setUser_phone(phone.getText().toString());
        info.setReal_name(name.getText().toString());
        info.setSex(sex.getText().toString());
        info.setDepartment_id((String) jump_organz.getTag());
        info.setRole_id((String) jump_role.getTag());
        info.setUser_id((String) phone.getTag());
        mActionType= Constant.VALUE_TYPE_U;
        mPresenter.updateInfo(info);
    }

    private void inflateUserInfo(UserInfo data) {
        if (data == null) return;
        phone.setText(data.getUser_phone());
        phone.setTag(data.getUser_id());
        name.setText(data.getReal_name());
        jump_organz.setText(data.getDepartment());
        jump_organz.setTag(data.getDepartment_id());
        jump_role.setText(data.getJob());
        jump_role.setTag(data.getRole_id());
        sex.setText(data.getSex());
    }

    @Override
    public Object onFinished(Object... backParams) {
        if ((Integer) backParams[0] == DialogInterface.BUTTON_POSITIVE) {
            String value = (String) backParams[2];
            switch ((Integer) backParams[1]) {
                case R.id.name:
                    name.setText(value);
                    break;
                case R.id.sex:
                    sex.setText(value);
                    break;
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.jump_organz:
                selectUpOrganz();
                return;
            case R.id.jump_role:
                selectRole();
                return;
            case R.id.name:
            case R.id.sex:
                modifyInfo(v);
                break;
        }
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

    private void selectRole() {
        Intent jump = new Intent(getActivity(), OrganzUserRoleSelectorActivity.class);
        startActivityForResult(jump, Constant.REQUEST_ENTITY_2);
    }

    private void selectUpOrganz() {
        Intent jump = new Intent(getActivity(), OrganzSecSelectorActivity.class);
        startActivityForResult(jump, Constant.REQUEST_ENTITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == Constant.REQUEST_ENTITY) {
            OrganzManageBase d = (OrganzManageBase) data.getSerializableExtra(Constant.KEY_ENTITY);
            jump_organz.setText(d.getDepartment());
            jump_organz.setTag(d.getDepartment_id());
        }else if (requestCode == Constant.REQUEST_ENTITY_2) {
            OrganzUserRoleSelectorItem d = (OrganzUserRoleSelectorItem) data
                    .getSerializableExtra(Constant.KEY_ENTITY);
            jump_role.setText(d.getName());
            jump_role.setTag(d.getId());
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
                updateUserInfo();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

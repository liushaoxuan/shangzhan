package com.wyu.iwork.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.CommuChatAdapter;
import com.wyu.iwork.interfaces.DataIView;
import com.wyu.iwork.presenter.CommuChatPresenter;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.activity.NotificationActivity;
import com.wyu.iwork.view.activity.OrganzManageActivity;

import java.util.List;

import io.rong.imlib.model.Conversation;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class CommuChatFragment extends BaseFragment implements DataIView<List<Conversation>> {
    private static final String TAG = "MyFragments";
    private RecyclerView mRv;
    private CommuChatAdapter mAdapter;
    private CommuChatPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new CommuChatPresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        ViewGroup content = (ViewGroup) inflater
                .inflate(R.layout.fragment_commu_chat, container, false);
        mPresenter.attachView(this);
        mPresenter.readData();
        return super.onCreateView(inflater, content, savedInstanceState);
    }


    @Override
    public void onInitView(View rootView) {
        mRv= (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mAdapter = new CommuChatAdapter(getActivity());
        mRv.setAdapter(mAdapter);

        rootView.findViewById(R.id.notification).setOnClickListener(this);
        rootView.findViewById(R.id.organz).setOnClickListener(this);
        rootView.findViewById(R.id.userManage).setOnClickListener(this);
        rootView.findViewById(R.id.invite).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.notification:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;
            case R.id.organz:
                startActivity(new Intent(getActivity(), OrganzManageActivity.class));
                break;
        }
    }

    @Override
    public void onReadFinished(boolean success, List<Conversation> data) {
        if (success && data != null && !data.isEmpty()) {
            Logger.e(TAG, "conversation data===>" + data.toString());
            //mAdapter.insertItems(data);
        }
    }

    @Override
    public void onWriteFinished(boolean success) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }
}

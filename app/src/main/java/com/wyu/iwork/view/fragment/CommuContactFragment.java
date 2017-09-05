package com.wyu.iwork.view.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.CommuContactAdapter;
import com.wyu.iwork.interfaces.ContactIView;
import com.wyu.iwork.model.Contact;
import com.wyu.iwork.presenter.CommuContactPresenter;
import com.wyu.iwork.util.AppUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.widget.AlphaItemDecoration;

import java.util.List;
import java.util.Map;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class CommuContactFragment extends BaseFragment implements ContactIView<List<Contact>> {
    private static final String TAG = "CommuContactFragment";
    private RecyclerView mRv;
    private LinearLayout mAlphaLayout;
    private NestedScrollView mSv;
    private CommuContactPresenter mPresenter;
    private Map<String, Integer> alphaMap;
    private Rect mBounds = new Rect();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new CommuContactPresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        ViewGroup content = (ViewGroup) inflater
                .inflate(R.layout.fragment_commu_contact, container, false);
        return super.onCreateView(inflater, content, savedInstanceState);
    }


    @Override
    public void onInitView(View rootView) {
        mPresenter.attachView(this);
        mSv= (NestedScrollView) rootView.findViewById(R.id.sv);
        mAlphaLayout = (LinearLayout) rootView.findViewById(R.id.alpha_layout);
        mRv = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRv.setAdapter(new CommuContactAdapter(getActivity()));
        if (mPresenter.getCachedData() != null) onLoadFinished(mPresenter.getCachedData());
        else mPresenter.loadContact();
    }

    private void inflateAlpha() {
        for (String alpha : AlphaItemDecoration.ALPHA) {
            TextView alphaView = (TextView) LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_alpha, mAlphaLayout, false);
            alphaView.setText(alpha);
            alphaView.setOnClickListener(this);
            mAlphaLayout.addView(alphaView);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.alpha:
                Integer index = alphaMap.get(((TextView) v).getText().toString());
                if (index == null) return;
                View itemView = mRv.findViewHolderForAdapterPosition(index).itemView;
                Logger.e(TAG,
                        "y=" + itemView.getY() + ",top=" + itemView.getTop() + ",translationY=" +
                        itemView.getTranslationY() + ",itemViewHeight=" + itemView.getHeight());
                mRv.getDecoratedBoundsWithMargins(itemView, mBounds);
                mSv.smoothScrollTo(0, mBounds.top + mRv.getTop());
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadFinished(List<Contact> data) {
        if (data == null || data.isEmpty()) return;
        mRv.addItemDecoration(new AlphaItemDecoration(getActivity(), data));
        ((CommuContactAdapter) mRv.getAdapter()).insertItems(data);
        alphaMap = AppUtil.getAlphaMap(data);
        inflateAlpha();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }
}

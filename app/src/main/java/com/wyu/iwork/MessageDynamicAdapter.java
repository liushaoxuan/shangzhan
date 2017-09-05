package com.wyu.iwork;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.IDynamicView;
import com.wyu.iwork.model.DynamicBean;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.presenter.DynamicPresenterCompl;
import com.wyu.iwork.presenter.IDynamicPresenter;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.view.activity.DynamicDetailActivity;
import com.wyu.iwork.view.activity.PostingDetailsActivity;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by lx on 2017/4/8.
 * 消息——动态——adapter
 */

public class MessageDynamicAdapter  {

}

package com.wyu.iwork.presenter;

import android.content.Context;

import com.wyu.iwork.util.Logger;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * @author HuaJian Jiang.
 *         Date 2017/1/16.
 */
public class CommuChatPresenter extends DataPresenter<List<Conversation>> {

    private static final String TAG = CommuChatPresenter.class.getSimpleName();

    private Context ctxt;

    public CommuChatPresenter(Context ctxt) {
        this.ctxt = ctxt;
    }

    @Override
    public void readData() {
        RongIMClient.getInstance()
                .getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    @Override
                    public void onSuccess(List<Conversation> conversations) {
                        Logger.e(TAG,"readConversationList_onSuccess==>"+ (conversations != null?conversations.toString():"no data"));
                        view.onReadFinished(true, conversations);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Logger.e(TAG,"readConversationList_onError==>"+ errorCode.getMessage());
                        view.onReadFinished(false, null);
                    }
                });
    }

}

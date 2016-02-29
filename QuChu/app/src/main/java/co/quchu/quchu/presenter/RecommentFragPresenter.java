package co.quchu.quchu.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.volley.VolleyError;

import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.m.IRecommendFragModel;
import co.quchu.quchu.m.RecommendFragModel;
import co.quchu.quchu.model.RecommendModelNew;
import co.quchu.quchu.view.fragment.IRecommendFragment;

/**
 * Created by linqipeng on 2016/2/26 10:34
 * email:437943145@qq.com
 * desc:
 */
public class RecommentFragPresenter {

    private IRecommendFragment view;
    private IRecommendFragModel model;
    private Context context;
    private MyHandle handle;

    public RecommentFragPresenter(Context context, IRecommendFragment view) {
        this.context = context;
        this.view = view;
        model = new RecommendFragModel(context);
    }

    public void init() {
        // TODO: 2016/2/26  服务器获取tab数据
        view.initTab();
    }

    public void initTabData(final boolean isDefaultData) {
//        DialogUtil.showProgess(context, "数据加载中...");
        //延时一秒，避免用户快速切换tab造成网络异常
        Message message;
        Bundle bundle = new Bundle();
        bundle.putBoolean("isDefaultData", isDefaultData);
        if (handle == null) {
            message = new Message();
            message.what = 0;
            handle = new MyHandle();
        } else {
            message = handle.obtainMessage();
            message.what = 0;
            handle.removeMessages(0);
        }
        handle.sendMessageDelayed(message, 500);
    }

    class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            model.getTabData(msg.getData().getBoolean("isDefaultData"), new CommonListener<RecommendModelNew>() {
                @Override
                public void successListener(RecommendModelNew response) {
                    DialogUtil.dismissProgess();
                    view.initTabData(false, response.getResult(), response.getPageCount(), response.getPagesNo());
                }

                @Override
                public void errorListener(VolleyError error, String exception, String msg) {
                    DialogUtil.dismissProgess();
                }
            });
        }
    }

    public void loadMore(String type, boolean isDefaultData) {
        model.getTabData(isDefaultData, new CommonListener<RecommendModelNew>() {
            @Override
            public void successListener(RecommendModelNew response) {
                view.loadMore(false, response.getResult(), response.getPageCount(), response.getPagesNo());
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                view.loadMore(true, null, 0, 0);

            }
        });
    }

}

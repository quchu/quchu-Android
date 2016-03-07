package co.quchu.quchu.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.IRecommendFragModel;
import co.quchu.quchu.model.RecommendFragModel;
import co.quchu.quchu.model.RecommendModelNew;
import co.quchu.quchu.model.TagsModel;
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
        DialogUtil.showProgess(context, R.string.loading_dialog_text);
        model.getTab(new CommonListener<List<TagsModel>>() {
            @Override
            public void successListener(List<TagsModel> response) {
                view.initTab(response);
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void initTabData(String selectedTag) {
        //延时一秒，避免用户快速切换tab造成网络异常
        Message message;
        Bundle bundle = new Bundle();
        bundle.putString("selectedTag", selectedTag);
        if (handle == null) {
            message = new Message();
            message.what = 0;
            message.setData(bundle);
            handle = new MyHandle();
        } else {
            message = handle.obtainMessage();
            message.what = 0;
            message.setData(bundle);
            handle.removeMessages(0);
        }
        handle.sendMessageDelayed(message, 500);
    }

    class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            DialogUtil.showProgess(context, R.string.loading_dialog_text);
            model.getTabData(msg.getData().getString("selectedTag", ""), new CommonListener<RecommendModelNew>() {
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

    public void loadMore(String type, int pageNumber) {

        model.loadMore(type, pageNumber, new CommonListener<RecommendModelNew>() {
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

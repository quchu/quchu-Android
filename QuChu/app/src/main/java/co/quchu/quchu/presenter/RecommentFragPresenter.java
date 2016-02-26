package co.quchu.quchu.presenter;

import android.content.Context;

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

    public RecommentFragPresenter(Context context, IRecommendFragment view) {
        this.context = context;
        this.view = view;
        model = new RecommendFragModel(context);
    }

    public void init() {
        // TODO: 2016/2/26  服务器获取tab数据
        view.initTab();
    }

    public void initTabData(boolean isDefaultData) {
        DialogUtil.showProgess(context, "数据加载中...");
        model.getTabData(isDefaultData, new CommonListener<RecommendModelNew>() {
            @Override
            public void successListener(RecommendModelNew response) {
                DialogUtil.dismissProgess();
                view.initTabData(response.getResult(), response.getPageCount(), response.getPagesNo());
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                DialogUtil.dismissProgess();

            }
        });
    }


}

package co.quchu.quchu.presenter;

import android.content.Context;

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
                DialogUtil.dismissProgessDirectly();
                view.initTab(false, response);
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                DialogUtil.dismissProgessDirectly();
                view.initTab(true, null);
            }
        });

    }


    public void initTabData(boolean isRefresh, String selectedTag) {

        System.out.println("getTABDATA init tab data");
        if (!isRefresh) {
            DialogUtil.showProgess(context, R.string.loading_dialog_text);
        }
        model.getTabData(selectedTag, new CommonListener<RecommendModelNew>() {
            @Override
            public void successListener(RecommendModelNew response) {
                System.out.printf("getTABDATA success");
                DialogUtil.dismissProgessDirectly();
                if (response != null)
                    view.initTabData(false, response.getResult(), response.getPageCount(), response.getPagesNo(), response.getRowCount());
                else
                    view.initTabData(true, null, 0, 0, 0);

            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                System.out.printf("getTABDATA error listener");
                DialogUtil.dismissProgessDirectly();
                view.initTabData(true, null, 0, 0, 0);
            }
        });
    }


    public void loadMore(String type, int pageNumber) {

        model.loadMore(type, pageNumber, new CommonListener<RecommendModelNew>() {
            @Override
            public void successListener(RecommendModelNew response) {
                if (response != null)
                    view.loadMore(false, response.getResult(), response.getPageCount(), response.getPagesNo());
                else
                    view.loadMore(true, null, 0, 0);
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                view.loadMore(true, null, 0, 0);

            }
        });
    }
}

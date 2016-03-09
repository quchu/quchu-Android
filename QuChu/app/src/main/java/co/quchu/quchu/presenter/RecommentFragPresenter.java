package co.quchu.quchu.presenter;

import android.content.Context;
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
                DialogUtil.dismissProgess();
                view.initTab(false, response);
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                DialogUtil.dismissProgess();
                view.initTab(true, null);
            }
        });

    }


    public void initTabData(boolean isRefresh, String selectedTag) {


        if (!isRefresh) {
            DialogUtil.showProgess(context, R.string.loading_dialog_text);
        }
        model.getTabData(selectedTag, new CommonListener<RecommendModelNew>() {
            @Override
            public void successListener(RecommendModelNew response) {
                DialogUtil.dismissProgess();
                view.initTabData(false, response.getResult(), response.getPageCount(), response.getPagesNo());
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                DialogUtil.dismissProgess();
                view.initTabData(true, null, 0, 0);
            }
        });
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

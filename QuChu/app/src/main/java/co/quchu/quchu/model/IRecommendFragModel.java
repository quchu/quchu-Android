package co.quchu.quchu.model;

import java.util.List;

import co.quchu.quchu.presenter.CommonListener;

/**
 * Created by linqipeng on 2016/2/26 11:18
 * email:437943145@qq.com
 * desc:
 */
public interface IRecommendFragModel {

    void getTab(CommonListener<List<TagsModel>> listener);

    void getTabData(String isDefaultData, CommonListener<RecommendModelNew> listener);

    void loadMore(String isDefaultData, int pageNumber, CommonListener<RecommendModelNew> listener);
}

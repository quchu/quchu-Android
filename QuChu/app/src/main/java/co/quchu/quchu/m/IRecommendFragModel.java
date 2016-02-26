package co.quchu.quchu.m;

import co.quchu.quchu.model.RecommendModelNew;
import co.quchu.quchu.presenter.CommonListener;

/**
 * Created by linqipeng on 2016/2/26 11:18
 * email:437943145@qq.com
 * desc:
 */
public interface IRecommendFragModel {

    void getTab();

    void getTabData(boolean isDefaultData, CommonListener<RecommendModelNew> listener);

    void loadMore(boolean isDefaultData, int pageNumber, CommonListener<RecommendModelNew> listener);
}

package co.quchu.quchu.view.fragment;

import java.util.List;

import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.TagsModel;

/**
 * Created by linqipeng on 2016/2/26 10:27
 * email:437943145@qq.com
 * desc: 分类界面操作接口
 */
public interface IRecommendFragment {


    /**
     * 加载顶部tab
     */
    void initTab(boolean isError,List<TagsModel> list);

    /**
     * 初始化tab对应的数据
     */
    void initTabData(boolean isError, List<RecommendModel> arrayList, int pageCount, int pageNum, int rowCount);

    /**
     * 加载更多
     */

    void loadMore(boolean isError, List<RecommendModel> arrayList, int pageCount, int pageNum);

}

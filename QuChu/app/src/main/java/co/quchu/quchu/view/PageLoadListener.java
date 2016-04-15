package co.quchu.quchu.view;

/**
 * Created by no21 on 2016/4/15.
 * email:437943145@qq.com
 * desc : 分页加载数据通用回调
 */
public interface PageLoadListener<DT> {
    /**
     * 第一页
     *
     * @param data
     */
    void initData(DT data);

    /**
     * 更多页
     *
     * @param data
     */
    void moreData(DT data);
}

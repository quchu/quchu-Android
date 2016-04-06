package co.quchu.quchu.presenter;

import co.quchu.quchu.model.FavoriteBean;

/**
 * Created by no21 on 2016/4/5.
 * email:437943145@qq.com
 * desc :
 */
public interface IQuchuActivity {
    /**
     * 显示收藏
     */
    void showData(boolean isError, FavoriteBean bean);


}

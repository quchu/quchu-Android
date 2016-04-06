package co.quchu.quchu.presenter;

import co.quchu.quchu.model.QuchuBean;

/**
 * Created by no21 on 2016/4/5.
 * email:437943145@qq.com
 * desc :
 */
public interface IQuchuActivity {
    /**
     * 显示收藏
     */
    void showFavorite(boolean isError, QuchuBean bean);

    /**
     * 显示发现
     */
    void showFind(boolean isError, QuchuBean bean);
}

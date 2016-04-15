package co.quchu.quchu.view.activity;

import co.quchu.quchu.model.PostCardModel;

/**
 * Created by no21 on 2016/4/7.
 * email:437943145@qq.com
 * desc :脚印界面
 */
public interface IFootprintActivity {


    void initData(boolean isError, PostCardModel data);
    void initMoreData(boolean isError, PostCardModel data);
}

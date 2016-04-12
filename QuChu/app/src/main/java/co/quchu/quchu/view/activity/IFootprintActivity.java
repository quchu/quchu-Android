package co.quchu.quchu.view.activity;

import java.util.List;

import co.quchu.quchu.model.PostCardItemModel;

/**
 * Created by no21 on 2016/4/7.
 * email:437943145@qq.com
 * desc :脚印界面
 */
public interface IFootprintActivity {


    void initData(boolean isError, List<PostCardItemModel> data);
}

package co.quchu.quchu.presenter;

import co.quchu.quchu.model.FindBean;

/**
 * Created by no21 on 2016/4/6.
 * email:437943145@qq.com
 * desc :
 */
public interface IFindFragment {


    void showData(boolean isError, FindBean bean);
    void showMoredata(boolean isError, FindBean bean);
}

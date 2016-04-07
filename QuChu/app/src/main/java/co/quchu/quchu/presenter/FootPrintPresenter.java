package co.quchu.quchu.presenter;

import android.content.Context;

import co.quchu.quchu.view.activity.IFootprintActivity;

/**
 * Created by no21 on 2016/4/7.
 * email:437943145@qq.com
 * desc :
 */
public class FootPrintPresenter {
    private Context context;

    private IFootprintActivity view;

    public FootPrintPresenter(Context context, IFootprintActivity view) {
        this.context = context;
        this.view = view;
    }


}

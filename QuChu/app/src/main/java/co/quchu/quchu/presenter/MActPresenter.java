package co.quchu.quchu.presenter;

import android.app.Activity;

import co.quchu.quchu.utils.LogUtils;

/**
 * MActPresenter
 * User: Chenhs
 * Date: 2015-10-21
 */
public class MActPresenter implements BasePresenter {
    private Activity activity;

    public MActPresenter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void _onCreate() {
        LogUtils.json("_onCreate");
    }

    @Override
    public void _onDestroy() {
        LogUtils.json("_onDestroy");
    }

    @Override
    public void _onPause() {
        LogUtils.json("_onPause");
    }

    @Override
    public void _onResume() {
        LogUtils.json("_onResume");
    }
}

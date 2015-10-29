package co.quchu.quchu.view.activity;

import android.os.Bundle;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.helper.Reutils;
import co.quchu.quchu.utils.LogUtils;

/**
 * AtmosphereActivity
 * User: Chenhs
 * Date: 2015-10-29
 */
public class AtmosphereActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atmosphere_item_view);
        LogUtils.json("start");
        Reutils.getUn();
//        AtmosDataHelper.GetAtmosData();
        LogUtils.json("end");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

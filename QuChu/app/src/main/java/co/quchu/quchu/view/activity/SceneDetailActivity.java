package co.quchu.quchu.view.activity;

import android.os.Bundle;

import co.quchu.quchu.base.BaseActivity;

/**
 * Created by Nico on 16/7/11.
 */
public class SceneDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }
}

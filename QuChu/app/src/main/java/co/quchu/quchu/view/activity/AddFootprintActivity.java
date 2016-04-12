package co.quchu.quchu.view.activity;

import android.os.Bundle;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;

/**
 * Created by Nico on 16/4/12.
 */
public class AddFootprintActivity extends BaseActivity {
    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_footprint);
    }
}

package co.quchu.quchu.view.activity;

import android.os.Bundle;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;

public class WhatIsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_is);
        getEnhancedToolbar().getTitleTv().setText("什么是趣友圈");
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }
}

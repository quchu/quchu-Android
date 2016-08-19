package co.quchu.quchu.im;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;

/**
 * 消息首页
 *
 * Created by mwb on 16/8/19.
 */
public class MessageActivity extends BaseBehaviorActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {
        return null;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 0;
    }

    @Override
    protected int activitySetup() {
        return 0;
    }

    @Override
    protected String getPageNameCN() {
        return null;
    }
}

package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;



import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.view.fragment.FootprintListFragment;

/**
 * 我的脚印,如果没有穿id参数 默认显示自己的
 */
public class MyFootprintActivity extends BaseBehaviorActivity {


    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {
        return null;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 121;
    }

    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_my_footprint);
    }


    public static final String REQUEST_KEY_USER_ID = "userId";
    public static final String REQUEST_KEY_USER_AGE = "age";
    public static final String REQUEST_KEY_USER_PHOTO = "photo";
    public static final String REQUEST_KEY_USER_NAME = "name";
    public static final String REQUEST_KEY_USER_FOOTER_COUND = "cound";
    public static final String REQUEST_KEY_USER_FOOTER_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);
        ButterKnife.bind(this);

        initTitle();
        int userId = getIntent().getIntExtra(REQUEST_KEY_USER_ID, AppContext.user.getUserId());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, FootprintListFragment.newInstance(userId)).commit();

    }

    private void initTitle() {
        EnhancedToolbar toolbar = getEnhancedToolbar();
        String title = getIntent().getStringExtra(REQUEST_KEY_USER_FOOTER_TITLE);
        toolbar.getTitleTv().setText(title);

        ImageView rightIv = toolbar.getRightIv();
        rightIv.setImageResource(R.mipmap.ic_add_position);
        rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFootprintActivity.this, PickingQuchuActivity.class);
                intent.putExtra(PickingQuchuActivity.REQUEST_KEY_FROM_MY_FOOTPRINT, true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

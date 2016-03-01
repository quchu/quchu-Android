package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.presenter.FollowPresenter;

/**
 * FollowingActivity
 * User: Chenhs
 * Date: 2016-03-01
 * TA关注的  /我关注的
 */
public class FollowingActivity extends BaseActivity {
    private static final int TAFOLLOWING = 0x01;//TA关注的
    private static final int TAFOLLOWERS = 0x02;//关注TA的
    @Bind(R.id.follow_rv)
    RecyclerView followRv;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    private int followType = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ButterKnife.bind(this);
        initTitleBar();
        followType = getIntent().getIntExtra("FollowType", 0x01);
        setTitleContentView();
        FollowPresenter.getFollowsList(this, AppContext.user.getUserId(), TAFOLLOWING, 1, null);
    }

    private void setTitleContentView() {
        switch (followType) {
            case TAFOLLOWING:
                titleContentTv.setText("TA关注的");
                break;
            case TAFOLLOWERS:
                titleContentTv.setText("关注TA的");
                break;
        }
    }

}

package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.fragment.PostCardDetailFg;
import co.quchu.quchu.view.fragment.PostCardListFg;
import co.quchu.quchu.widget.cardsui.MyCard;


/**
 * PostCardActivity
 * User: Chenhs
 * Date: 2015-11-03
 * 明信片
 */
public class PostCardActivity extends BaseActivity {

    @Bind(R.id.title_content_tv)
    TextView mTitleContentTv;
    @Bind(R.id.postcard_fl)
    FrameLayout postcardFl;
    FragmentTransaction transaction;
    PostCardListFg postCardListFg;
    MyCard.PostCardItemClickListener listener;
    private int fragmentIndex = -1;
    public boolean isFavoritePostCard = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcard);
        ButterKnife.bind(this);
        initTitleBar();
        mTitleContentTv.setText(getTitle());
        isFavoritePostCard = getIntent().getBooleanExtra("isFavoritePostCard", false);
        listener = new MyCard.PostCardItemClickListener() {
            @Override
            public void onPostCardItemClick(PostCardItemModel item) {
                if (SPUtils.getBooleanFromSPMap(PostCardActivity.this, AppKey.IS_POSTCARD_GUIDE, false)) {

                } else {
                    if (postCardListFg != null)
                        transaction = getSupportFragmentManager().beginTransaction();
                    isFragmentStatOk = false;
                    transaction.setCustomAnimations(R.anim.in_bottom_to_to_fg, R.anim.out_top_to_bottom_fg);
                    transaction.replace(R.id.postcard_fl, new PostCardDetailFg(item));
                    transaction.commit();
                    fragmentIndex = 1;
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(0x00), 900);
                }
                //postCardListFg.setInvisiable(true);
            }
        };
        postCardListFg = new PostCardListFg(listener, isFavoritePostCard);
        showListFragment();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("PostCardActivity");
        MobclickAgent.onResume(this);

        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("PostCardActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        LogUtils.json("back");
        fragmentJump();
    }

    private void fragmentJump() {
        if (fragmentIndex == 1) {
            showListFragment();
        } else {
            this.finish();
        }
    }

    public void showListFragment() {
        transaction = getSupportFragmentManager().beginTransaction();
        isFragmentStatOk = false;
        transaction.setCustomAnimations(R.anim.in_bottom_to_to_fg, R.anim.out_top_to_bottom_fg);
        transaction.replace(R.id.postcard_fl, postCardListFg);
        transaction.commit();
        fragmentIndex = 0;
        mHandler.sendMessageDelayed(mHandler.obtainMessage(0x00), 900);
    }


    private boolean isFragmentStatOk = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isFragmentStatOk) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!isFragmentStatOk) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    isFragmentStatOk = true;
                    break;
            }
        }
    };
}

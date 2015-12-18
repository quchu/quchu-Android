package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.utils.LogUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcard);
        ButterKnife.bind(this);
        initTitleBar();
        mTitleContentTv.setText(getTitle());

        listener = new MyCard.PostCardItemClickListener() {
            @Override
            public void onPostCardItemClick(PostCardModel.PostCardItem item) {
                if (postCardListFg != null)
                    transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_bottom_to_to_fg, R.anim.out_top_to_bottom_fg);
                transaction.replace(R.id.postcard_fl, new PostCardDetailFg(item));
                transaction.commit();
                fragmentIndex = 1;
                //    postCardListFg.setInvisiable(true);
            }
        };
        postCardListFg = new PostCardListFg(listener);
        showListFragment();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        LogUtils.json("back");

     //   super.onBackPressed();
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
        transaction.setCustomAnimations(R.anim.in_bottom_to_to_fg, R.anim.out_top_to_bottom_fg);
        transaction.replace(R.id.postcard_fl, postCardListFg);
        transaction.commit();
        fragmentIndex = 0;
    }
}

package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcard);
        ButterKnife.bind(this);
        initTitleBar();
        mTitleContentTv.setText(getTitle());

        listener = new MyCard.PostCardItemClickListener() {
            @Override
            public void onPostCardItemClick(int Pid, String rgbStr) {
                LogUtils.json(Pid + "pid" + rgbStr);
          if (postCardListFg!=null)
              postCardListFg.setInvisiable(true);
                transaction = getSupportFragmentManager().beginTransaction();
              //  transaction.setCustomAnimations(R.anim.in_push_right_to_left,R.anim.out_push_left_to_right);
                transaction.replace(R.id.postcard_fl, new PostCardDetailFg());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };

        postCardListFg = new PostCardListFg(listener);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_top_to_bottom,R.anim.out_bottom_to_top);
        transaction.replace(R.id.postcard_fl, postCardListFg);
        transaction.addToBackStack(null);
        transaction.commit();



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


}

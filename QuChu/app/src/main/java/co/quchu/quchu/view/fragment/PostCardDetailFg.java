package co.quchu.quchu.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.widget.RevealLayout;
import co.quchu.quchu.widget.cardsui.MyCard;

/**
 * FriendsFollowerFg
 * User: Chenhs
 * Date: 2015-11-09
 */
public class PostCardDetailFg extends Fragment {
    View view;

    @Bind(R.id.reveal_layout)
    RevealLayout revealLayout;
    private MyCard.PostCardItemClickListener listener;

    private static final int[] COLOR_LIST = new int[]{
            0xff33b5e5,
            0xff99cc00,
            0xffff8800,
            0xffaa66cc,
            0xffff4444,
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_postcard_detail, null);
        ButterKnife.bind(this, view);
        initPostCardDetailData();
        TextView     mTextView = (TextView) view.findViewById(R.id.text);

        mTextView.setBackgroundColor(COLOR_LIST[2 % 5]);
        mTextView.setText("Fragment " + 2);
        revealLayout.setContentShown(false);
        revealLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    revealLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    revealLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                revealLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        revealLayout.show();
                    }
                }, 50);
            }
        });
        revealLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .add(R.id.container, SimpleFragment.newInstance(mIndex + 1))
                        .commit();*/
            }
        });
        return view;
    }

    private void initPostCardDetailData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}

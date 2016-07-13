package co.quchu.quchu.widget;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by Nico on 16/7/13.
 */
public class EmptyView {

    @Bind(R.id.rlEmptyView)
    RelativeLayout rlEmptyView;
    @Bind(R.id.action_buttton)
    TextView actionButtton;
    @Bind(R.id.massage)
    TextView massage;
    @Bind(R.id.refreshLayout)
    LinearLayout refreshLayout;
    @Bind(R.id.loadingView)
    ImageView loadingView;
    private OnRetryListener mListener;


    public void init(View v) {
        ButterKnife.bind(this, v);
    }

    public void show() {
        rlEmptyView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        loadingView.setVisibility(View.GONE);
        rlEmptyView.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.VISIBLE);
    }

    public void showLoading() {
        refreshLayout.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }

    public void showRetry(String msg, OnRetryListener listener) {
        refreshLayout.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        massage.setText(msg);
        mListener = listener;
        actionButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRetry();
            }
        });
    }

    public void showRetry(OnRetryListener listener) {
        mListener = listener;
        actionButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRetry();
            }
        });
        refreshLayout.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    public void noData(OnRetryListener listener){
        refreshLayout.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        rlEmptyView.setVisibility(View.GONE);
        massage.setText(R.string.string_no_data);
        actionButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRetry();
            }
        });
    }


    public interface OnRetryListener {
        void onRetry();
    }

}

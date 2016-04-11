package co.quchu.quchu.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;


public class FootprintDetailFragment extends BaseFragment {


    @Bind(R.id.draweeViewMain)
    SimpleDraweeView draweeViewMain;
    @Bind(R.id.headImage)
    SimpleDraweeView headImage;
    @Bind(R.id.detail)
    TextView detail;
    @Bind(R.id.container_bottom)
    RelativeLayout containerBottom;
    private View rootView;
    public boolean firstPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_footprint_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        headImage.setImageURI(Uri.parse("res:///" + R.mipmap.ic_launcher));
        detail.setText("safdssssssssssssssssssssssssssssssss网发生的发生的发生大幅盛大pufhqhwfhquewfhweiufghqweipfuqweifhuiqwehfip");
        draweeViewMain.setImageURI(Uri.parse("http://h.hiphotos.bdimg.com/wisegame/pic/item/4c2eb9389b504fc222acac26e6dde71190ef6d26.jpg"));
        if (!firstPage) {
            firstPage = false;
            containerBottom.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    containerBottom.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    float offset = containerBottom.getY() + containerBottom.getHeight();
                    containerBottom.setY(offset);
                }
            });
        }

    }


    public void showing() {
        containerBottom.animate()
                .translationYBy(-containerBottom.getHeight())
                .setDuration(600)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    public void hint() {
        containerBottom.animate()
                .translationYBy(containerBottom.getHeight())
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

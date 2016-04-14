package co.quchu.quchu.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.ImageModel;


public class FootprintDetailFragment extends BaseFragment {


    @Bind(R.id.draweeViewMain)
    SimpleDraweeView draweeViewMain;
    private View rootView;
    public boolean firstPage;
    private ImageModel bean;

    public static final String REQUEST_KEY_IMAGE_ENTITY = "ENTITY";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_footprint_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bean = getArguments().getParcelable(REQUEST_KEY_IMAGE_ENTITY);
        if (bean != null) {
            float ratio = (float) bean.getWidth() / bean.getHeight();
            draweeViewMain.setAspectRatio(ratio);
            draweeViewMain.setImageURI(Uri.parse(bean.getPath()));
        }


    }


//    public void showing() {
//        containerBottom.animate()
//                .translationYBy(-containerBottom.getHeight())
//                .setDuration(600)
//                .setInterpolator(new DecelerateInterpolator())
//                .start();
//    }
//
//    public void hint() {
//        containerBottom.animate()
//                .translationYBy(containerBottom.getHeight())
//                .setDuration(600)
//                .setInterpolator(new AccelerateDecelerateInterpolator())
//                .start();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

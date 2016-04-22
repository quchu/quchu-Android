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

    public static final String REQUEST_KEY_IMAGE_ENTITY = "ENTITY";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_footprint_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageModel bean = getArguments().getParcelable(REQUEST_KEY_IMAGE_ENTITY);
        if (bean != null) {
            float ratio = (float) bean.getWidth() / bean.getHeight();
            draweeViewMain.setAspectRatio(ratio);
            draweeViewMain.setImageURI(Uri.parse(bean.getPath()));
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

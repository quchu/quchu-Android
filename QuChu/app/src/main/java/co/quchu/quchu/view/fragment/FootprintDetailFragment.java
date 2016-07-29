package co.quchu.quchu.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.ImageModel;


public class FootprintDetailFragment extends BaseFragment {



    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_f_foot_print);
    }

    @Bind(R.id.draweeViewMain)
    SimpleDraweeView draweeViewMain;

    public static final String REQUEST_KEY_IMAGE_ENTITY = "ENTITY";


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_footprint_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageModel bean = getArguments().getParcelable(REQUEST_KEY_IMAGE_ENTITY);
        if (bean != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) draweeViewMain.getLayoutParams();
            if (bean.getWidth() > bean.getHeight()) {//宽图
                params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            } else {
                params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            }
            draweeViewMain.setAspectRatio(bean.getWidth() / (float) bean.getHeight());
            draweeViewMain.setImageURI(Uri.parse(bean.getPath()));
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

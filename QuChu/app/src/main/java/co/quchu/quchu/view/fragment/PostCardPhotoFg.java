package co.quchu.quchu.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.photoselected.ImageBDInfo;
import co.quchu.quchu.photoselected.PreviewImageActivity;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.PostCardFromImageActivity;
import co.quchu.quchu.view.adapter.PostCardImageAdapter;

/**
 * PostCardDetailFg
 * User: Chenhs
 * Date: 2016-03-14
 */
public class PostCardPhotoFg extends Fragment {
    public static String POST_CARD_MODEL = "post_card_model";
    @Bind(R.id.postcard_detail_finish_tv)
    TextView postcardDetailFinishTv;
    @Bind(R.id.animation1)
    RelativeLayout animation1;
    @Bind(R.id.add_postcard_image_igv)
    GridView addPostcardImageIgv;
    @Bind(R.id.root_cv)
    CardView rootCv;
    private PostCardItemModel model;
    PostCardImageAdapter adapter;

    public static PostCardPhotoFg newInstance(PostCardItemModel pModel) {
        Bundle args = new Bundle();
        PostCardPhotoFg fragment = new PostCardPhotoFg();
        args.putSerializable(POST_CARD_MODEL, pModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        model = (PostCardItemModel) getArguments().getSerializable(POST_CARD_MODEL);
    }

    ImageBDInfo bdInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_postcard_photo, container, false);
        ButterKnife.bind(this, view);
        bdInfo = new ImageBDInfo();
        adapter = new PostCardImageAdapter(getActivity());
        addPostcardImageIgv.setAdapter(adapter);
        addPostcardImageIgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   Toast.makeText(PostCardImageActivity.this, "点击图片" + position, Toast.LENGTH_SHORT).show();
                View c = addPostcardImageIgv.getChildAt(0);
                int top = c.getTop();
                int firstVisiblePosition = addPostcardImageIgv.getFirstVisiblePosition() / 4;
                int a, b;
                a = position / 4;
                b = position % 4;
                bdInfo.width = (AppContext.Width - (2 * StringUtils.dip2px(34))) / 4;
                bdInfo.height = bdInfo.width;
                bdInfo.x = StringUtils.dip2px(32) + b * bdInfo.width + b * StringUtils.dip2px(4);
                bdInfo.y = StringUtils.dip2px(1) + bdInfo.height * (a - firstVisiblePosition) + top + (a - firstVisiblePosition) * StringUtils.dip2px(2) + addPostcardImageIgv.getTop() - StringUtils.dip2px(1)
                        + StringUtils.dip2px(128);

                Intent intent = new Intent(getActivity(), PreviewImageActivity.class);
                intent.putExtra("data", (Serializable) model);
                intent.putExtra("bdinfo", bdInfo);
                intent.putExtra("index", position);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
        if (null != model) {
            bindingData();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void bindingData() {
        if (!StringUtils.isEmpty(model.getRgb())) {
            rootCv.setCardBackgroundColor(Color.parseColor("#" + model.getRgb()));
        } else {
            rootCv.setCardBackgroundColor(Color.parseColor("#808080"));
        }
        if (model.getImglist() != null && model.getImglist().size() > 0)
            adapter.setData(model.getImglist());
    }

    @OnClick({R.id.postcard_detail_finish_tv})
    public void cardImageClick(View view) {
        switch (view.getId()) {
            case R.id.postcard_detail_finish_tv:
                ((PostCardFromImageActivity) getActivity()).flipOver();
                break;
        }
    }
}

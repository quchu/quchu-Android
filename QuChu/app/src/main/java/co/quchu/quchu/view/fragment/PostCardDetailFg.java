package co.quchu.quchu.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.view.activity.PostCardFromImageActivity;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * PostCardDetailFg
 * User: Chenhs
 * Date: 2016-03-14
 */
public class PostCardDetailFg extends Fragment {
    public static String POST_CARD_MODEL = "post_card_model";
    @Bind(R.id.postcard_detail_finish_tv)
    TextView postcardDetailFinishTv;
    @Bind(R.id.animation1)
    RelativeLayout animation1;
    @Bind(R.id.postcard_detail_photo_sdv)
    SimpleDraweeView postcardDetailPhotoSdv;
    @Bind(R.id.postcard_detail_pname_tv)
    TextView postcardDetailPnameTv;
    @Bind(R.id.postcard_detail_paddress_tv)
    TextView postcardDetailPaddressTv;
    @Bind(R.id.postcard_detail_enter_place_tv)
    TextView postcardDetailEnterPlaceTv;
    @Bind(R.id.postcard_detail_address_tv)
    TextView postcardDetailAddressTv;
    @Bind(R.id.postcard_detail_tel_tv)
    TextView postcardDetailTelTv;
    @Bind(R.id.item_my_postcard_avatar_sdv)
    SimpleDraweeView itemMyPostcardAvatarSdv;
    @Bind(R.id.item_my_postcard_card_tiem_tv)
    TextView itemMyPostcardCardTiemTv;
    @Bind(R.id.item_my_postcard_card_nickname_tv)
    TextView itemMyPostcardCardNicknameTv;
    @Bind(R.id.item_my_postcard_card_prb)
    ProperRatingBar itemMyPostcardCardPrb;
    @Bind(R.id.postcard_comment_tv)
    TextView postcardCommentTv;
    @Bind(R.id.root_cv)
    CardView rootCv;

    private PostCardItemModel model;

    public static PostCardDetailFg newInstance(PostCardItemModel pModel) {

        Bundle args = new Bundle();

        PostCardDetailFg fragment = new PostCardDetailFg();
        args.putSerializable(POST_CARD_MODEL, pModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        model = (PostCardItemModel) getArguments().getSerializable(POST_CARD_MODEL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_postcard_detail_from_image, container, false);
        ButterKnife.bind(this, view);
        if (null != model) {
            bindingDatas();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void bindingDatas() {
        rootCv.setCardBackgroundColor(Color.parseColor("#" + model.getRgb()));
        itemMyPostcardAvatarSdv.setImageURI(Uri.parse(model.getAutorPhoto()));
        itemMyPostcardAvatarSdv.setAspectRatio(1f);
        postcardCommentTv.setText(model.getComment());
        postcardDetailPnameTv.setText(model.getPlcaeName());
        postcardDetailPaddressTv.setText(model.getPlcaeAddress());
        postcardDetailAddressTv.setText(model.getAddress());
        itemMyPostcardCardPrb.setRating(model.getScore());
        itemMyPostcardCardNicknameTv.setText(model.getAutor());
        itemMyPostcardCardTiemTv.setText(model.getTime().substring(0, 10));
        postcardDetailPhotoSdv.setImageURI(Uri.parse(model.getPlcaeCover()));
        postcardDetailPhotoSdv.setAspectRatio(1f);
        postcardDetailTelTv.setText(model.getTel());
    }

    @OnClick({R.id.postcard_detail_finish_tv, R.id.postcard_detail_enter_place_tv})
    public void cardDetailClick(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.postcard_detail_finish_tv:
                ((PostCardFromImageActivity) getActivity()).flipOver();
                break;

            case R.id.postcard_detail_enter_place_tv:
                if (model != null && !model.issys())
                    startActivity(new Intent(getActivity(), QuchuDetailsActivity.class).putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, model.getPlaceId()));
                else
                    Toast.makeText(getActivity(), "系统明信片无法进去趣处!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onResume() {
        MobclickAgent.onPageStart("PostCardDetailActivity");

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("PostCardDetailActivity");
    }
}

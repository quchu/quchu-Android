package co.quchu.quchu.widget.cardsui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.PostCardDetailActivity;
import co.quchu.quchu.view.activity.PostCardImageActivity;
import co.quchu.quchu.widget.cardsui.objects.Card;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

public class MyCard extends Card {

    @Bind(R.id.item_recommend_card_name_tv)
    TextView itemRecommendCardNameTv;
    @Bind(R.id.item_recommend_card_city_tv)
    TextView itemRecommendCardCityTv;
    @Bind(R.id.item_recommend_card_title_textrl)
    RelativeLayout itemRecommendCardTitleTextrl;
    @Bind(R.id.item_recommend_card_photo_sdv)
    SimpleDraweeView itemRecommendCardPhotoSdv;
    @Bind(R.id.item_my_postcard_avatar_sdv)
    SimpleDraweeView itemMyPostcardAvatarSdv;
    @Bind(R.id.item_my_postcard_card_prb)
    ProperRatingBar itemMyPostcardCardPrb;
    @Bind(R.id.item_my_postcard_card_comment_tv)
    TextView itemMyPostcardCardCommentTv;
    @Bind(R.id.item_my_postcard_card_heart_iv)
    ImageView itemMyPostcardCardHeartIv;
    @Bind(R.id.item_my_postcard_heart_rl)
    RelativeLayout itemMyPostcardHeartRl;
    @Bind(R.id.item_recommend_card_collect_iv)
    ImageView itemRecommendCardCollectIv;
    @Bind(R.id.item_recommend_card_collect_rl)
    RelativeLayout itemRecommendCardCollectRl;
    @Bind(R.id.item_recommend_card_interest_iv)
    ImageView itemRecommendCardInterestIv;
    @Bind(R.id.item_recommend_card_interest_rl)
    RelativeLayout itemRecommendCardInterestRl;
    @Bind(R.id.item_recommend_card_reply_Iv)
    ImageView itemRecommendCardReplyIv;
    @Bind(R.id.item_recommend_card_reply_rl)
    RelativeLayout itemRecommendCardReplyRl;
    @Bind(R.id.root_cv)
    CardView rootCv;
    @Bind(R.id.item_my_postcard_card_nickname_tv)
    TextView itemMyPostcardCardNicknameTv;
    @Bind(R.id.item_my_postcard_card_tiem_tv)
    TextView itemMyPostcardCardTiemTv;
    @Bind(R.id.item_recommend_card_photo_num_tv)
    TextView itemRecommendCardPhotoNumTv;
    @Bind(R.id.item_postcard_user_guide_view)
    RelativeLayout item_postcard_user_guide_view;
    private PostCardItemModel item;
    private PostCardItemClickListener listener;
    private Context mContext;


    public MyCard(PostCardItemModel item, PostCardItemClickListener listener, Context activity) {
        this.item = item;
        this.listener = listener;
        this.mContext = activity;

    }


    @Override
    public View getCardContent(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.item_my_postcard_view, null);
        ButterKnife.bind(this, v);
        rootCv.setCardBackgroundColor(Color.parseColor("#" + item.getRgb()));
        itemRecommendCardNameTv.setText(item.getPlcaeName());
        itemRecommendCardCityTv.setText(item.getPlcaeAddress());
        itemMyPostcardCardPrb.setRating(item.getScore());
        itemMyPostcardCardCommentTv.setText(item.getComment());
        itemMyPostcardCardNicknameTv.setText(item.getAutor());
        itemMyPostcardCardTiemTv.setText(StringUtils.isEmpty(item.getTime()) ? "" : item.getTime().substring(0, 10));
        itemRecommendCardPhotoSdv.setImageURI(Uri.parse(item.getPlcaeCover()));
        itemRecommendCardPhotoSdv.setAspectRatio(1.33f);
        itemMyPostcardAvatarSdv.setImageURI(Uri.parse(item.getAutorPhoto()));
        if (item.getImglist() != null && item.getImglist().size() > 0) {
            itemRecommendCardPhotoNumTv.setVisibility(View.VISIBLE);
            itemRecommendCardPhotoNumTv.setText("1/" + item.getImglist().size());
        } else {
            itemRecommendCardPhotoNumTv.setVisibility(View.INVISIBLE);
        }
        if (item.isIsf()) {
            itemRecommendCardCollectIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_detail_collect));
        } else {
            itemRecommendCardCollectIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_detail_uncollect));
        }
        rootCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.json("view is click");
                if (listener != null)
                    listener.onPostCardItemClick(item);
            }
        });
        if (SPUtils.getBooleanFromSPMap(mContext, AppKey.IS_POSTCARD_GUIDE, false)) {
            item_postcard_user_guide_view.setVisibility(View.VISIBLE);
        } else {
            item_postcard_user_guide_view.setVisibility(View.GONE);
        }
        return v;
    }

    @OnClick({R.id.item_recommend_card_reply_rl, R.id.item_recommend_card_photo_sdv})
    public void myCardClick(View view) {
        if (SPUtils.getBooleanFromSPMap(mContext, AppKey.IS_POSTCARD_GUIDE, false)) {
            switch (view.getId()) {
                case R.id.item_recommend_card_photo_sdv:
                    if (item.getImglist().size() > 0) {
                        SPUtils.putBooleanToSPMap(mContext, AppKey.IS_POSTCARD_GUIDE, false);
                        item_postcard_user_guide_view.setVisibility(View.GONE);
                        Intent intent = new Intent(mContext, PostCardImageActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("pCardModel", item);
                        intent.putExtras(mBundle);
                        mContext.startActivity(intent);
                    }
                    break;
            }
        } else {
            switch (view.getId()) {
                case R.id.item_recommend_card_reply_rl:
                    mContext.startActivity(new Intent(mContext, PostCardDetailActivity.class).putExtra("cId", item.getCardId()));
                    break;
                case R.id.item_recommend_card_photo_sdv:
                    if (item.getImglist().size() > 0) {
                        Intent intent = new Intent(mContext, PostCardImageActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("pCardModel", item);
                        intent.putExtras(mBundle);
                        mContext.startActivity(intent);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean convert(View convertCardView) {
        // TODO Auto-generated method stub
        return false;
    }


    public interface PostCardItemClickListener {
        void onPostCardItemClick(PostCardItemModel item);
    }
}

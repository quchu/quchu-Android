package co.quchu.quchu.widget.cardsui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.PostCardDetailActivity;
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
    private PostCardModel.PostCardItem item;
    private PostCardItemClickListener listener;
    private Context mContext;

    public MyCard(PostCardModel.PostCardItem item, PostCardItemClickListener listener, Context activity) {
        this.item = item;
        this.listener = listener;
        this.mContext = activity;
    }


    @Override
    public View getCardContent(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CardView v = (CardView) inflater.inflate(R.layout.item_my_postcard_view, null);
        v.setCardBackgroundColor(Color.parseColor("#" + item.getRgb()));
        ButterKnife.bind(this, v);
        itemRecommendCardNameTv.setText(item.getPlcaeName());
        itemRecommendCardCityTv.setText(item.getPlcaeAddress());
        itemMyPostcardCardPrb.setRating(item.getScore());
        itemMyPostcardCardCommentTv.setText(item.getComment());
        itemMyPostcardCardNicknameTv.setText(item.getAutor());
        itemMyPostcardCardTiemTv.setText(StringUtils.isEmpty(item.getTime()) ? "" : item.getTime().substring(0, 10));
        itemRecommendCardPhotoSdv.setImageURI(Uri.parse(item.getPlcaeCover()));
        itemRecommendCardPhotoSdv.setAspectRatio(1.33f);
        itemMyPostcardAvatarSdv.setImageURI(Uri.parse(item.getAutorPhoto()));
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
        return v;
    }

    @OnClick({R.id.item_recommend_card_reply_rl})
    public void myCardClick(View view) {
        switch (view.getId()) {
            case R.id.item_recommend_card_reply_rl:
                mContext.startActivity(new Intent(mContext, PostCardDetailActivity.class).putExtra("cId",item.getCardId()));
                break;
        }
    }

    @Override
    public boolean convert(View convertCardView) {
        // TODO Auto-generated method stub
        return false;
    }


    public interface PostCardItemClickListener {
        void onPostCardItemClick(PostCardModel.PostCardItem item);
    }
}

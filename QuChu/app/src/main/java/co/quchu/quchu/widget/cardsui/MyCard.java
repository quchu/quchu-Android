package co.quchu.quchu.widget.cardsui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.presenter.PostCardPresenter;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.AddPostCardActivity;
import co.quchu.quchu.view.activity.PostCardDetailActivity;
import co.quchu.quchu.view.activity.PostCardImageActivity;
import co.quchu.quchu.view.activity.PostcarDetailActivity;
import co.quchu.quchu.widget.cardsui.objects.Card;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

public class MyCard extends Card {

    @Bind(R.id.item_recommend_card_name_tv)
    TextView itemRecommendCardNameTv;
    @Bind(R.id.item_recommend_card_city_tv)
    TextView itemRecommendCardCityTv;
    @Bind(R.id.animation1)
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
    private boolean mDisableImage = false;


    public MyCard(PostCardItemModel item, PostCardItemClickListener listener, Context activity,boolean disableImage) {
        this.item = item;
        this.listener = listener;
        this.mContext = activity;
        mDisableImage = disableImage;

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
        if (!mDisableImage){
            itemRecommendCardPhotoSdv.setImageURI(Uri.parse(item.getPlcaeCover()));
        }
        itemRecommendCardPhotoSdv.setAspectRatio(1.33f);
        itemMyPostcardAvatarSdv.setImageURI(Uri.parse(item.getAutorPhoto()));
        if (item.getImglist() != null && item.getImglist().size() > 0) {
            itemRecommendCardPhotoNumTv.setVisibility(View.VISIBLE);
            itemRecommendCardPhotoNumTv.setText("1/" + item.getImglist().size());
        } else {
            itemRecommendCardPhotoNumTv.setVisibility(View.INVISIBLE);
        }
        itemRecommendCardCollectIv.setImageDrawable(context.getResources().getDrawable(item.isIsf() ? R.drawable.ic_detail_collect : R.drawable.ic_detail_uncollect));
        if (item.issys()) {
            itemMyPostcardCardHeartIv.setImageDrawable(context.getResources().getDrawable(item.isIsp() ? R.drawable.ic_detail_heart_full : R.drawable.ic_detail_heart));
        } else {
            if (item.isIsme()) {
                itemMyPostcardCardHeartIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_post_card_editer));
            } else {
                itemMyPostcardCardHeartIv.setImageDrawable(context.getResources().getDrawable(item.isIsp() ? R.drawable.ic_detail_heart_full : R.drawable.ic_detail_heart));
            }
        }
        rootCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.json("view is click");
                if (listener != null) {
//                    listener.onPostCardItemClick(item);

                    // TODO: 2016/3/7
                    Intent intent = new Intent(v.getContext(), PostcarDetailActivity.class);
                    item.setIsf(true);//已收藏列表 公用明信片列表mode 接口没有返回这个字段
                    intent.putExtra(PostcarDetailActivity.REQUEST_PARAMATER_ENTITY, item);
                    Bundle bundle = null;


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                        Pair<View, String> pair = Pair.create(v.findViewById(R.id.animation1), "animation1");
                        Pair<View, String> pair1 = Pair.create(v.findViewById(R.id.animation2), "animation2");
                        Pair<View, String> pair2 = Pair.create(v.findViewById(R.id.animation3), "animation3");

//                        bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                (Activity) v.getContext(), v, PostcarDetailActivity.SHARE_ELEMENT_NAME).toBundle();
                        bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                (Activity) v.getContext(), pair, pair1, pair2).toBundle();
                    }
                    v.getContext().startActivity(intent, bundle);

                }
            }
        });
        if (SPUtils.getBooleanFromSPMap(mContext, AppKey.IS_POSTCARD_GUIDE, false)) {
            item_postcard_user_guide_view.setVisibility(View.VISIBLE);
        } else {
            item_postcard_user_guide_view.setVisibility(View.GONE);
        }
        return v;
    }

    @OnClick({R.id.item_recommend_card_reply_rl, R.id.item_recommend_card_photo_sdv, R.id.item_my_postcard_heart_rl})
    public void myCardClick(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
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
                    mContext.startActivity(new Intent(mContext, PostCardDetailActivity.class).putExtra("cInfo", item));
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
                case R.id.item_my_postcard_heart_rl:
                    if (!item.issys() && item.isIsme()) {
                        Intent intent = new Intent(mContext, AddPostCardActivity.class).putExtra("pName", item.getPlcaeName());
                        intent.putExtra("pId", item.getPlaceId());
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("pCardModel", item);
                        intent.putExtras(mBundle);
                        mContext.startActivity(intent);
                    } else {
                        doParise();
                    }
                    break;
                case R.id.item_recommend_card_collect_rl:
                    setFavorite();
                    break;
            }
        }
    }

    private void doParise() {

        PostCardPresenter.setPraise(mContext, item.isIsp(), true, item.getCardId(), new PostCardPresenter.MyPostCardListener() {
            @Override
            public void onSuccess(PostCardModel model) {
                item.setIsp(!item.isIsp());
                itemMyPostcardCardHeartIv.setImageDrawable(mContext.getResources().getDrawable(item.isIsp() ? R.drawable.ic_detail_heart_full : R.drawable.ic_detail_heart));
                if (item.isIsp()) {
                    Toast.makeText(mContext, "点赞成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "取消点赞!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public boolean convert(View convertCardView) {
        // TODO Auto-generated method stub
        return false;
    }


    public interface PostCardItemClickListener {
        void onPostCardItemClick(PostCardItemModel item);
    }

    private void setFavorite() {

        if (!item.issys()) {
            String favoUrl = "";
            if (item.isIsf()) {
                favoUrl = String.format(NetApi.userDelFavorite, item.getCardId(), NetApi.FavTypeCard);
            } else {
                favoUrl = String.format(NetApi.userFavorite, item.getCardId(), NetApi.FavTypeCard);
            }
            NetService.get(mContext, favoUrl, new IRequestListener() {
                @Override
                public void onSuccess(JSONObject response) {
                    item.setIsf(!item.isIsf());
                    if (item.isIsf()) {
                        Toast.makeText(mContext, "收藏成功!", Toast.LENGTH_SHORT).show();
                        itemRecommendCardCollectIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_detail_collect));
                    } else {
                        Toast.makeText(mContext, "取消收藏!", Toast.LENGTH_SHORT).show();
                        itemRecommendCardCollectIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_detail_uncollect));
                    }
                }

                @Override
                public boolean onError(String error) {

                    return false;
                }
            });
        } else {
            Toast.makeText(mContext, "系统明信片不允许收藏!", Toast.LENGTH_SHORT).show();
        }
    }

}

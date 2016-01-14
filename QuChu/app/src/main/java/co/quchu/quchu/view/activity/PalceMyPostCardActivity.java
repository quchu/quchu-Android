package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * PalceMyPostCardActivity
 * User: Chenhs
 * Date: 2015-12-22
 */
public class PalceMyPostCardActivity extends BaseActivity {
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
    @Bind(R.id.item_my_postcard_card_nickname_tv)
    TextView itemMyPostcardCardNicknameTv;
    @Bind(R.id.item_my_postcard_card_tiem_tv)
    TextView itemMyPostcardCardTiemTv;
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

    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    private int pId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_my_postcard);
        ButterKnife.bind(this);
        initTitleBar();
        titleContentTv.setText(getTitle());
        rootCv.setVisibility(View.INVISIBLE);
        pId = getIntent().getIntExtra("pId", 0);
        getDataFromServer();
    }

    PostCardItemModel pPostCardModel;

    private void getDataFromServer() {
        DialogUtil.showProgess(this, R.string.loading_dialog_text);
        NetService.get(this, String.format(NetApi.getPlaceUserCard, pId), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("pMpcA==" + response);
                Gson gson = new Gson();
                pPostCardModel = gson.fromJson(response.toString(), PostCardItemModel.class);
                if (pPostCardModel != null) {
                    itemRecommendCardNameTv.setText(pPostCardModel.getPlcaeName());
                    itemRecommendCardCityTv.setText(pPostCardModel.getPlcaeAddress());
                    itemRecommendCardPhotoSdv.setImageURI(Uri.parse(pPostCardModel.getPlcaeCover()));
                    itemRecommendCardPhotoSdv.setAspectRatio(1.3f);
                    itemMyPostcardAvatarSdv.setImageURI(Uri.parse(pPostCardModel.getAutorPhoto()));
                    itemMyPostcardCardPrb.setRating(pPostCardModel.getScore());
                    itemMyPostcardCardNicknameTv.setText(pPostCardModel.getAutor());
                    itemMyPostcardCardTiemTv.setText(pPostCardModel.getTime().substring(0, 10));
                    itemMyPostcardCardCommentTv.setText(pPostCardModel.getComment());
                    rootCv.setCardBackgroundColor(Color.parseColor("#" + pPostCardModel.getRgb()));

                    itemRecommendCardCollectIv.setImageDrawable(getResources().getDrawable(pPostCardModel.isIsf() ? R.drawable.ic_detail_collect : R.drawable.ic_detail_uncollect));
                    itemMyPostcardCardHeartIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_post_card_editer));
                    rootCv.setVisibility(View.VISIBLE);
                    DialogUtil.dismissProgess();
                }
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgess();
                return false;
            }
        });
    }

    @OnClick({R.id.root_cv, R.id.item_recommend_card_collect_rl, R.id.item_recommend_card_interest_rl,
            R.id.item_recommend_card_reply_rl, R.id.item_recommend_card_photo_sdv, R.id.item_my_postcard_heart_rl})
    public void cardClick(View view) {
        switch (view.getId()) {
            case R.id.item_recommend_card_collect_rl:
                itemRecommendCardCollectRl.setClickable(false);
                setDetailFavorite();
                break;
            case R.id.item_my_postcard_heart_rl:
                Intent intent = new Intent(this, AddPostCardActivity.class).putExtra("pName", pPostCardModel.getPlcaeName());
                intent.putExtra("pId", pPostCardModel.getPlaceId());
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("pCardModel", pPostCardModel);
                intent.putExtras(mBundle);
                startActivity(intent);
                break;
            case R.id.item_recommend_card_interest_rl:
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(pPostCardModel.getCardId(), pPostCardModel.getPlcaeName(), false);
                shareDialogFg.show(getFragmentManager(), "share_dialog");
                break;
            case R.id.item_recommend_card_photo_sdv:
                if (pPostCardModel.getImglist().size() > 0) {
                    Intent intents = new Intent(this, PostCardImageActivity.class);
                    Bundle mBundles = new Bundle();
                    mBundles.putSerializable("pCardModel", pPostCardModel);
                    intents.putExtras(mBundles);
                    startActivity(intents);
                }
                break;
            case R.id.item_recommend_card_reply_rl:
                startActivity(new Intent(this, PostCardDetailActivity.class).putExtra("cInfo", pPostCardModel));
                break;

        }
    }


    public void setDetailFavorite() {
        String favoUrl = "";
        if (pPostCardModel.isIsf()) {
            favoUrl = String.format(NetApi.userDelFavorite, pPostCardModel.getCardId(), NetApi.FavTypeCard);
        } else {
            favoUrl = String.format(NetApi.userFavorite, pPostCardModel.getCardId(), NetApi.FavTypeCard);
        }
        NetService.get(this, favoUrl, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                itemRecommendCardCollectRl.setClickable(true);
                pPostCardModel.setIsf(!pPostCardModel.isIsf());
                if (pPostCardModel.isIsf()) {
                    Toast.makeText(PalceMyPostCardActivity.this, "收藏成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PalceMyPostCardActivity.this, "取消收藏!", Toast.LENGTH_SHORT).show();
                }
                itemRecommendCardCollectIv.setImageDrawable(getResources().getDrawable(pPostCardModel.isIsf() ? R.drawable.ic_detail_collect : R.drawable.ic_detail_uncollect));
            }

            @Override
            public boolean onError(String error) {
                itemRecommendCardCollectRl.setClickable(true);
                return false;
            }
        });
    }
}

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

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.presenter.PostCardPresenter;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.MoreButtonView;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

public class PostcarDetailActivity extends BaseActivity {

    public static final String REQUEST_PARAMATER_ENTITY = "entity";


    @Bind(R.id.item_recommend_card_name_tv)
    TextView itemRecommendCardNameTv;
    @Bind(R.id.item_recommend_card_city_tv)
    TextView itemRecommendCardCityTv;
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
    @Bind(R.id.item_recommend_card_collect_iv)
    ImageView itemRecommendCardCollectIv;
    @Bind(R.id.item_recommend_card_photo_num_tv)
    TextView itemRecommendCardPhotoNumTv;
    @Bind(R.id.root_cv)
    CardView rootCv;
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRl;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.title_more_rl)
    MoreButtonView moreButtonView;
    private PostCardItemModel item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcar_detail);
        ButterKnife.bind(this);
        titleContentTv.setText("我的明信片");
        item = (PostCardItemModel) getIntent().getSerializableExtra(REQUEST_PARAMATER_ENTITY);
        initPostCardDetailData();
        initTitleBar();
    }

    @Override
    protected int activitySetup() {
        return 0;
    }


    private void initPostCardDetailData() {
        if (item != null) {
            rootCv.setCardBackgroundColor(Color.parseColor("#" + item.getRgb()));
            itemRecommendCardNameTv.setText(item.getPlcaeName());
            itemRecommendCardCityTv.setText(item.getPlcaeAddress());
            itemMyPostcardCardPrb.setRating(item.getScore());
            itemMyPostcardCardCommentTv.setText(item.getComment());
            itemMyPostcardCardNicknameTv.setText(item.getAutor());
            itemMyPostcardCardTiemTv.setText(StringUtils.isEmpty(item.getTime()) ? "" : item.getTime().substring(0, 10));
            itemRecommendCardPhotoSdv.setAspectRatio(1.3f);
            itemMyPostcardAvatarSdv.setImageURI(Uri.parse(item.getAutorPhoto()));
            itemRecommendCardPhotoSdv.setImageURI(Uri.parse(item.getPlcaeCover()));

            itemRecommendCardCollectIv.setImageDrawable(getResources().getDrawable(item.isIsf() ? R.mipmap.ic_detail_collect : R.mipmap.ic_detail_uncollect));
            if (item.issys()) {
                itemMyPostcardCardHeartIv.setImageDrawable(getResources().getDrawable(item.isIsp() ? R.mipmap.ic_detail_heart_full : R.mipmap.ic_detail_heart));
            } else {
                if (item.isIsme()) {
                    itemMyPostcardCardHeartIv.setImageDrawable(getResources().getDrawable(R.mipmap.ic_post_card_editer));
                } else {
                    itemMyPostcardCardHeartIv.setImageDrawable(getResources().getDrawable(item.isIsp() ? R.mipmap.ic_detail_heart_full : R.mipmap.ic_detail_heart));
                }
            }
            if (item.getImglist() != null && item.getImglist().size() > 0) {
                itemRecommendCardPhotoNumTv.setVisibility(View.VISIBLE);
                itemRecommendCardPhotoNumTv.setText("1/" + item.getImglist().size());
            } else {
                itemRecommendCardPhotoNumTv.setVisibility(View.INVISIBLE);
            }
        }
    }

    @OnClick({R.id.item_my_postcard_heart_rl, R.id.item_recommend_card_collect_rl, R.id.item_recommend_card_interest_rl, R.id.item_recommend_card_reply_rl
            , R.id.root_cv, R.id.item_recommend_card_photo_sdv})
    public void cardItemClick(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.root_cv:
                finish();
                break;
            case R.id.item_my_postcard_heart_rl:
                if (!item.issys() && item.isIsme()) {
                    Intent intent = new Intent(this, AddPostCardActivity.class).putExtra("pName", item.getPlcaeName());
                    intent.putExtra("pId", item.getPlaceId());
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("pCardModel", item);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                } else {
                    doParise();
                }
                break;
            case R.id.item_recommend_card_collect_rl:
                setFavorite();
                break;
            case R.id.item_recommend_card_interest_rl:
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(item.getCardId(), item.getPlcaeName(), false);
                shareDialogFg.show(getFragmentManager(), "share_postcard");
                break;
            case R.id.item_recommend_card_reply_rl:
                startActivity(new Intent(this, PostCardDetailActivity.class).putExtra("cInfo", item));
                break;
            case R.id.item_recommend_card_photo_sdv:
                if (item.getImglist().size() > 0) {
                    Intent intent = new Intent(this, PostCardImageActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("pCardModel", item);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
                break;
        }
    }

    private void doParise() {
        PostCardPresenter.setPraise(this, item.isIsp(), true, item.getCardId(), new PostCardPresenter.MyPostCardListener() {
            @Override
            public void onSuccess(PostCardModel model) {
                item.setIsp(!item.isIsp());
                itemMyPostcardCardHeartIv.setImageDrawable(getResources().getDrawable(item.isIsp() ? R.mipmap.ic_detail_heart_full : R.mipmap.ic_detail_heart));
                if (item.isIsp()) {
                    Toast.makeText(PostcarDetailActivity.this, "点赞成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostcarDetailActivity.this, "取消点赞!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }


    private void setFavorite() {
        if (!item.issys()) {
            String favoUrl;
            if (item.isIsf()) {
                favoUrl = String.format(NetApi.userDelFavorite, item.getCardId(), NetApi.FavTypeCard);
            } else {
                favoUrl = String.format(NetApi.userFavorite, item.getCardId(), NetApi.FavTypeCard);
            }
            NetService.get(this, favoUrl, new IRequestListener() {
                @Override
                public void onSuccess(JSONObject response) {
                    item.setIsf(!item.isIsf());
                    if (item.isIsf()) {
                        Toast.makeText(PostcarDetailActivity.this, "收藏成功!", Toast.LENGTH_SHORT).show();
                        itemRecommendCardCollectIv.setImageDrawable(getResources().getDrawable(R.mipmap.ic_detail_collect));
                    } else {
                        Toast.makeText(PostcarDetailActivity.this, "取消收藏!", Toast.LENGTH_SHORT).show();
                        itemRecommendCardCollectIv.setImageDrawable(getResources().getDrawable(R.mipmap.ic_detail_uncollect));
                    }
                }

                @Override
                public boolean onError(String error) {

                    return false;
                }
            });
        } else {
            Toast.makeText(this, "系统明信片不允许收藏!", Toast.LENGTH_SHORT).show();
        }
    }


}

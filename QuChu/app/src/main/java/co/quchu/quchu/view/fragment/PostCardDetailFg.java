package co.quchu.quchu.view.fragment;

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
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.presenter.PostCardPresenter;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.AddPostCardActivity;
import co.quchu.quchu.view.activity.PostCardActivity;
import co.quchu.quchu.view.activity.PostCardDetailActivity;
import co.quchu.quchu.view.activity.PostCardImageActivity;
import co.quchu.quchu.widget.cardsui.MyCard;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * FriendsFollowerFg
 * User: Chenhs
 * Date: 2015-11-09
 */
public class PostCardDetailFg extends Fragment {
    View view;


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
    @Bind(R.id.item_recommend_card_photo_num_tv)
    TextView itemRecommendCardPhotoNumTv;
    @Bind(R.id.root_cv)
    CardView rootCv;
    private MyCard.PostCardItemClickListener listener;
    private PostCardItemModel item;

    public PostCardDetailFg(PostCardItemModel item) {
        this.item = item;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_postcard_detail, null);
        ButterKnife.bind(this, view);
        initPostCardDetailData();
        return view;
    }

    private void initPostCardDetailData() {
        if (item != null) {
            rootCv.setCardBackgroundColor(Color.parseColor("#" + item.getRgb()));
           /*     revealLayoutShow();*/
            itemRecommendCardNameTv.setText(item.getPlcaeName());
            itemRecommendCardCityTv.setText(item.getPlcaeAddress());
            itemMyPostcardCardPrb.setRating(item.getScore());
            itemMyPostcardCardCommentTv.setText(item.getComment());
            itemMyPostcardCardNicknameTv.setText(item.getAutor());
            itemMyPostcardCardTiemTv.setText(StringUtils.isEmpty(item.getTime()) ? "" : item.getTime().substring(0, 10));
            itemRecommendCardPhotoSdv.setImageURI(Uri.parse(item.getPlcaeCover()));
            itemRecommendCardPhotoSdv.setAspectRatio(1.3f);
            itemMyPostcardAvatarSdv.setImageURI(Uri.parse(item.getAutorPhoto()));
           /* if (item.isIsf()) {
                itemRecommendCardCollectIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_detail_collect));
            } else {
                itemRecommendCardCollectIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_detail_uncollect));
            }*/
            itemRecommendCardCollectIv.setImageDrawable(getResources().getDrawable(item.isIsf() ? R.drawable.ic_detail_collect : R.drawable.ic_detail_uncollect));
            if (item.issys()) {
                itemMyPostcardCardHeartIv.setImageDrawable(getActivity().getResources().getDrawable(item.isIsp() ? R.drawable.ic_detail_heart_full : R.drawable.ic_detail_heart));
            } else {
                if (item.isIsme()) {
                    itemMyPostcardCardHeartIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_post_card_editer));
                } else {
                    itemMyPostcardCardHeartIv.setImageDrawable(getResources().getDrawable(item.isIsp() ? R.drawable.ic_detail_heart_full : R.drawable.ic_detail_heart));
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
                ((PostCardActivity) getActivity()).showListFragment();
                break;
            case R.id.item_my_postcard_heart_rl:
                if (!item.issys() && item.isIsme()) {
                    Intent intent = new Intent(getActivity(), AddPostCardActivity.class).putExtra("pName", item.getPlcaeName());
                    intent.putExtra("pId", item.getPlaceId());
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("pCardModel", item);
                    intent.putExtras(mBundle);
                    getActivity().startActivity(intent);
                } else {
                    doParise();
                }
                break;
            case R.id.item_recommend_card_collect_rl:
                setFavorite();
                break;
            case R.id.item_recommend_card_interest_rl:
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(item.getCardId(), item.getPlcaeName(), false);
                shareDialogFg.show(getActivity().getFragmentManager(), "share_postcard");
                break;
            case R.id.item_recommend_card_reply_rl:
                getActivity().startActivity(new Intent(getActivity(), PostCardDetailActivity.class).putExtra("cInfo", item));
                break;
            case R.id.item_recommend_card_photo_sdv:
                if (item.getImglist().size() > 0) {
                    Intent intent = new Intent(getActivity(), PostCardImageActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("pCardModel", item);
                    intent.putExtras(mBundle);
                    getActivity().startActivity(intent);
                }
                break;
        }
    }

    private void doParise() {

        PostCardPresenter.setPraise(getActivity(), item.isIsp(), true, item.getCardId(), new PostCardPresenter.MyPostCardListener() {
            @Override
            public void onSuccess(PostCardModel model) {
                item.setIsp(!item.isIsp());
                itemMyPostcardCardHeartIv.setImageDrawable(getResources().getDrawable(item.isIsp() ? R.drawable.ic_detail_heart_full : R.drawable.ic_detail_heart));
                if (item.isIsp()) {
                    Toast.makeText(getActivity(), "点赞成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "取消点赞!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setFavorite() {
        if (!item.issys()) {
            String favoUrl = "";
            if (item.isIsf()) {
                favoUrl = String.format(NetApi.userDelFavorite, item.getCardId(), NetApi.FavTypeCard);
            } else {
                favoUrl = String.format(NetApi.userFavorite, item.getCardId(), NetApi.FavTypeCard);
            }
            NetService.get(getActivity(), favoUrl, new IRequestListener() {
                @Override
                public void onSuccess(JSONObject response) {
                    item.setIsf(!item.isIsf());
                    if (item.isIsf()) {
                        Toast.makeText(getActivity(), "收藏成功!", Toast.LENGTH_SHORT).show();
                        itemRecommendCardCollectIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_detail_collect));
                    } else {
                        Toast.makeText(getActivity(), "取消收藏!", Toast.LENGTH_SHORT).show();
                        itemRecommendCardCollectIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_detail_uncollect));
                    }
                }

                @Override
                public boolean onError(String error) {

                    return false;
                }
            });
        } else {
            Toast.makeText(getActivity(), "系统明信片不允许收藏!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.json("PostCardDetailFg  onResume");
    }
}

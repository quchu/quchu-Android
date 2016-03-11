package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * PostCardDetailActivity
 * User: Chenhs
 * Date: 2015-12-20
 */
public class PostCardDetailActivity extends BaseActivity {


    @Bind(R.id.postcard_detail_del_tv)
    TextView postcardDetailDelTv;
    @Bind(R.id.postcard_detail_finish_tv)
    TextView postcardDetailFinishTv;
    @Bind(R.id.animation1)
    RelativeLayout itemRecommendCardTitleTextrl;
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
    @Bind(R.id.item_my_postcard_card_prb)
    ProperRatingBar itemMyPostcardCardPrb;
    @Bind(R.id.item_my_postcard_card_nickname_tv)
    TextView itemMyPostcardCardNicknameTv;
    @Bind(R.id.item_my_postcard_card_tiem_tv)
    TextView itemMyPostcardCardTiemTv;
    @Bind(R.id.root_cv)
    CardView rootCv;

    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.postcard_comment_tv)
    TextView postcardCommentTv;
    private int cId = 0;//明信片id
    private PostCardItemModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcard_detail);
        ButterKnife.bind(this);
        initTitleBar();
        titleContentTv.setText(getTitle());
        model = (PostCardItemModel) getIntent().getSerializableExtra("cInfo");
        //   getPostcardDetailData();
        if (model != null) {
            bindingDatas();
        } else {
            Toast.makeText(this, "该明信片已被邮到了火星!", Toast.LENGTH_SHORT).show();
        }
    }
   /* private void getPostcardDetailData() {

        NetService.get(this, String.format(NetApi.getCardDetail, cId), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("/////" + response.toString());
                try {
                    if (response != null && !"null".equals(response.getString("data")) && !StringUtils.isEmpty(response.getString("data"))) {
                        Gson gson = new Gson();
                        model = gson.fromJson(response.toString(), PostCardDetailModel.class);
                        if (model != null) {
                            bindingDatas();
                        }else {
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }*/

    private void bindingDatas() {
        if (model.isIsme()) {
            postcardDetailDelTv.setVisibility(View.VISIBLE);
        } else {
            postcardDetailDelTv.setVisibility(View.GONE);
        }
        if (model.issys()) {
            postcardDetailDelTv.setVisibility(View.GONE);
        }
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


    @OnClick({R.id.postcard_detail_del_tv, R.id.postcard_detail_finish_tv, R.id.postcard_detail_enter_place_tv})
    public void cardDetailClick(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.postcard_detail_finish_tv:
                this.finish();
                break;
            case R.id.postcard_detail_del_tv:
                if (model != null)
                    delCard();
                break;
            case R.id.postcard_detail_enter_place_tv:
                if (model != null && !model.issys())
                    startActivity(new Intent(this, QuchuDetailsActivity.class).putExtra("pId", model.getPlaceId()));
                else
                    Toast.makeText(PostCardDetailActivity.this, "系统明信片无法进去趣处!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void delCard() {
        NetService.post(this, String.format(NetApi.delPostCard, model.getCardId()), null, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                Toast.makeText(PostCardDetailActivity.this, "明信片删除成功", Toast.LENGTH_SHORT).show();
                SPUtils.putBooleanToSPMap(PostCardDetailActivity.this, AppKey.IS_POSTCARD_LIST_NEED_REFRESH, true);
                PostCardDetailActivity.this.finish();
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }
    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("PostCardDetailActivity");

        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("PostCardDetailActivity");
    }
}

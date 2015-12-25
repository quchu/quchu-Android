package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.DetailListViewAdapter;
import co.quchu.quchu.widget.HorizontalNumProgressBar;
import co.quchu.quchu.widget.InnerListView;
import co.quchu.quchu.widget.OutSideScrollView;
import co.quchu.quchu.widget.TagCloudView;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;


/**
 * InterestingDetailsActivity
 * User: Chenhs
 * Date: 2015-12-09
 * 趣处详情
 */
public class InterestingDetailsActivity extends BaseActivity {

    @Bind(R.id.item_card_image_sdv)
    SimpleDraweeView itemCardImageSdv;
    @Bind(R.id.detail_button_collect_rl)
    RelativeLayout detailButtonCollectRl;
    @Bind(R.id.detail_button_share_rl)
    RelativeLayout detailButtonShareRl;
    @Bind(R.id.detail_button_add_postcard_rl)
    RelativeLayout detailButtonAddPostcardRl;
    @Bind(R.id.detail_store_name_tv)
    TextView detailStoreNameTv;
    @Bind(R.id.detail_suggest_prb)
    ProperRatingBar detailSuggestPrb;
    @Bind(R.id.detail_avg_price_tv)
    TextView detailAvgPriceTv;
    @Bind(R.id.detail_store_tagclound_tcv)
    TagCloudView detailStoreTagcloundTcv;
    @Bind(R.id.detail_store_address_tv)
    TextView detailStoreAddressTv;
    @Bind(R.id.detail_store_address_ll)
    LinearLayout detailStoreAddressLl;
    @Bind(R.id.detail_store_phone_tv)
    TextView detailStorePhoneTv;
    @Bind(R.id.detail_store_phone_ll)
    LinearLayout detailStorePhoneLl;
    @Bind(R.id.detail_progress_one)
    HorizontalNumProgressBar detailProgressOne;
    @Bind(R.id.detail_progress_tow)
    HorizontalNumProgressBar detailProgressTow;
    @Bind(R.id.detail_progress_three)
    HorizontalNumProgressBar detailProgressThree;
    @Bind(R.id.detail_box_rb)
    RadioButton detailBoxRb;
    @Bind(R.id.detail_mess_rb)
    RadioButton detailMessRb;
    @Bind(R.id.detail_takeout_rb)
    RadioButton detailTakeoutRb;
    @Bind(R.id.detail_cash_rb)
    RadioButton detailCashRb;
    @Bind(R.id.detail_cardpay_rb)
    RadioButton detailCardpayRb;
    @Bind(R.id.detail_zfb_rb)
    RadioButton detailZfbRb;
    @Bind(R.id.detail_wechat_rb)
    RadioButton detailWechatRb;
    @Bind(R.id.detail_store_business_hours_key_tv)
    TextView detailStoreBusinessHoursKeyTv;
    @Bind(R.id.detail_store_business_hours_value_tv)
    TextView detailStoreBusinessHoursValueTv;
    @Bind(R.id.detail_nearby_tv)
    TextView detailNearbyTv;
    @Bind(R.id.detail_nearby_tcv)
    TagCloudView detailNearbyTcv;

    @Bind(R.id.detail_outside_sv)
    OutSideScrollView detailOutsideSv;
    @Bind(R.id.detail_store_business_hours_ll)
    LinearLayout detailStoreBusinessHoursLl;
    @Bind(R.id.detail_nearby_ll)
    LinearLayout detailNearbyLl;
    @Bind(R.id.detail_button_group_ll)
    LinearLayout detailButtonGroupLl;
    @Bind(R.id.detail_button_collect_out_rl)
    RelativeLayout detailButtonCollectOutRl;
    @Bind(R.id.detail_button_share_out_rl)
    RelativeLayout detailButtonShareOutRl;
    @Bind(R.id.detail_button_add_postcard_out_rl)
    RelativeLayout detailButtonAddPostcardOutRl;
    @Bind(R.id.detail_button_group_out_ll)
    LinearLayout detailButtonGroupOutLl;

    @Bind(R.id.detail_image_list_ilv)
    InnerListView detailImageListIlv;
    @Bind(R.id.detail_been_tv)
    TextView detailBeenTv;
    @Bind(R.id.detail_want_tv)
    TextView detailWantTv;

    @Bind(R.id.detail_button_collect_iv)
    ImageView detailButtonCollectIv;
    @Bind(R.id.detail_button_collect_out_iv)
    ImageView detailButtonCollectOutIv;
    private int pId;
    private float detailButtonGroupLlHeight = 0f;
    public DetailModel dModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesting_detail);
        initTitleBar();
        ButterKnife.bind(this);
        detailButtonGroupLl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                detailButtonGroupLlHeight = detailButtonGroupLl.getY() - (detailButtonGroupOutLl.getHeight() - detailButtonGroupLl.getHeight());
            }
        });
        detailOutsideSv.setOverScrollListener(new OutSideScrollView.OverScrolledListener() {
            @Override
            public void onOverScrolled(int scrollX, int scrollY) {
                //         LogUtils.json("scrollY=" + scrollY + "//detailButtonGroupLl==" + detailButtonGroupLlHeight);
                if (scrollY >= detailButtonGroupLlHeight) {
                    detailButtonGroupOutLl.setVisibility(View.VISIBLE);
                } else {
                    detailButtonGroupOutLl.setVisibility(View.GONE);
                }
            }
        });


        initData();
    }

    private void initData() {
        pId = getIntent().getIntExtra("pId", -1);
        if (-1 == pId) {
            Toast.makeText(this, "该趣处已不存在!", Toast.LENGTH_SHORT).show();
        } else {
            DialogUtil.showProgess(this, "数据加载中...");
            InterestingDetailPresenter.getInterestingData(this, pId, new InterestingDetailPresenter.getDetailDataListener() {
                @Override
                public void getDetailData(DetailModel model) {
                    dModel = model;
                    bindingDetailData();
                    DialogUtil.dismissProgess();
                }
            });
        }
    }

    private void bindingDetailData() {
        itemCardImageSdv.setImageURI(Uri.parse(dModel.getCover()));
        itemCardImageSdv.setAspectRatio(1f);
        detailStoreNameTv.setText(dModel.getName());
        if (StringUtils.isEmpty(dModel.getTraffic())) {
            detailStoreAddressTv.setText(dModel.getAddress());
        } else if (!StringUtils.isEmpty(dModel.getTraffic())) {
            detailStoreAddressTv.setText(String.format(getResources().getString(R.string.detail_address_hint_text), dModel.getAddress(), dModel.getTraffic()));
        }
        detailStorePhoneTv.setText(dModel.getTel());
        detailStoreBusinessHoursValueTv.setText(dModel.getBusinessHours() + " " + dModel.getRestDay());
        detailSuggestPrb.setRating(dModel.getSuggest());
        if (!StringUtils.isEmpty(dModel.getPrice()) && !"0".equals(dModel.getPrice())) {
            detailAvgPriceTv.setText(String.format(getResources().getString(R.string.detail_price_hint_text), dModel.getPrice()));
        } else {
            detailAvgPriceTv.setVisibility(View.INVISIBLE);
        }
        if (StringUtils.isEmpty(dModel.getNearbySpot())) {
            detailNearbyLl.setVisibility(View.GONE);
        } else {
            detailNearbyTcv.setTags(StringUtils.convertStrToArray(dModel.getNearbySpot()));
        }
        if (dModel.getGenes().size() >= 3) {
            detailProgressOne.setProgressName(dModel.getGenes().get(0).getKey());
            detailProgressOne.setProgress(dModel.getGenes().get(0).getValue());
            detailProgressTow.setProgressName(dModel.getGenes().get(1).getKey());
            detailProgressTow.setProgress(dModel.getGenes().get(1).getValue());
            detailProgressThree.setProgressName(dModel.getGenes().get(2).getKey());
            detailProgressThree.setProgress(dModel.getGenes().get(2).getValue());
        }
        if (dModel.getTags().size() > 0) {
            ArrayList<String> tags = new ArrayList<String>();
            for (int i = 0; i < dModel.getTags().size(); i++) {
                tags.add(dModel.getTags().get(i).getZh());
            }
            detailStoreTagcloundTcv.setTags(tags);
        } else {
            detailStoreTagcloundTcv.setVisibility(View.GONE);
        }
        initConvenienceIcons();
        changeBottomBeenBg(dModel.isIsout());
        changeCollectState(dModel.isIsf());
        DetailListViewAdapter dlvAdapter = new DetailListViewAdapter(this, dModel.getImglist());
        detailImageListIlv.setAdapter(dlvAdapter);
        setListViewHeightBasedOnChildren(detailImageListIlv);
        detailOutsideSv.smoothScrollTo(0, 0);

    }

    private void initConvenienceIcons() {
        String iconStr = "";
        if (dModel.getIcons().size() > 0) {
            for (int i = 0; i < dModel.getIcons().size(); i++) {
                iconStr = dModel.getIcons().get(i).getZh();
                switch (iconStr) {
                    case "包厢":
                        detailBoxRb.setChecked(true);
                        break;
                    case "堂食":
                        detailMessRb.setChecked(true);
                        break;
                    case "外送":
                        detailTakeoutRb.setChecked(true);
                        break;
                    case "现金":
                        detailCashRb.setChecked(true);
                        break;
                    case "刷卡":
                        detailCardpayRb.setChecked(true);
                        break;
                    case "支付宝":
                        detailZfbRb.setChecked(true);
                        break;
                    case "微信":
                        detailWechatRb.setChecked(true);
                        break;
                }
            }

        }
    }

    private void changeBottomBeenBg(boolean isOut) {
        if (isOut) {
            detailBeenTv.setTextColor(getResources().getColor(R.color.black));
            detailBeenTv.setBackground(getResources().getDrawable(R.drawable.shape_detail_bottom_full_bg));
        } else {
            detailBeenTv.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
            detailBeenTv.setBackground(getResources().getDrawable(R.drawable.shape_detail_bottom_bg));
        }
    }

    private void changeCollectState(boolean isCollect) {
        if (isCollect) {
            detailButtonCollectIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_detail_collect));
            detailButtonCollectOutIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_detail_collect));
        } else {
            detailButtonCollectIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_detail_uncollect));
            detailButtonCollectOutIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_detail_uncollect));
        }
    }

    @OnClick({R.id.detail_store_phone_ll, R.id.detail_been_tv, R.id.detail_button_collect_out_rl, R.id.detail_button_collect_rl,
            R.id.detail_button_add_postcard_out_rl, R.id.detail_button_add_postcard_rl, R.id.detail_button_share_out_rl, R.id.detail_button_share_rl})
    public void detailClick(View v) {
        switch (v.getId()) {
            case R.id.detail_store_phone_ll:
                callPhone();
                break;
            case R.id.detail_been_tv:
                userBeen();//用户去过
                break;
            case R.id.detail_want_tv:
                //用户想去
                break;
            case R.id.detail_button_add_postcard_out_rl:
            case R.id.detail_button_add_postcard_rl:
                //添加明信片
                Intent intent = new Intent();
                intent.putExtra("pId", dModel.getPid());
                intent.putExtra("pName", dModel.getName());
                intent.setClass(this, PlacePostCardActivity.class);
                startActivity(intent);

                break;
            case R.id.detail_button_collect_out_rl:
            case R.id.detail_button_collect_rl:
                //收藏
                setFavorite();
                break;
            case R.id.detail_button_share_out_rl:
            case R.id.detail_button_share_rl:
                //分享
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(dModel.getPid(), dModel.getName(), true);
                shareDialogFg.show(getFragmentManager(), "share_dialog");
                break;
        }
    }

    private void userBeen() {
        InterestingDetailPresenter.getUserOutPlace(this, pId, new InterestingDetailPresenter.DetailDataListener() {
            @Override
            public void onSuccessCall(String str) {
                changeBottomBeenBg(true);
            }

            @Override
            public void onErrorCall(String str) {

            }
        });
    }

    private void setFavorite() {
        InterestingDetailPresenter.setDetailFavorite(this, pId, dModel.isIsf(), new InterestingDetailPresenter.DetailDataListener() {
            @Override
            public void onSuccessCall(String str) {
                dModel.setIsf(!dModel.isIsf());
                changeCollectState(dModel.isIsf());
                if (dModel.isIsf()) {
                    Toast.makeText(InterestingDetailsActivity.this, "收藏成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InterestingDetailsActivity.this, "取消收藏!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorCall(String str) {

            }
        });
    }

    private void callPhone() {
        if (dModel != null && !StringUtils.isEmpty(dModel.getTel())) {
            Uri uri = Uri.parse("tel:" + dModel.getTel());
            Intent it = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(it);
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        totalHeight = (int) ((listView.getWidth() / 0.75f) * listAdapter.getCount());
       /* for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }*/

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() + StringUtils.dip2px(this, 330)));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
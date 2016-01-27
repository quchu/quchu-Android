package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import java.util.StringTokenizer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherCollectModel;
import co.quchu.quchu.analysis.GatherViewModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.dialog.WantToGoDialogFg;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
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

    @Bind(R.id.detail_outside_sv)
    OutSideScrollView detailOutsideSv;
    @Bind(R.id.detail_store_business_hours_ll)
    LinearLayout detailStoreBusinessHoursLl;
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
    @Bind(R.id.detail_bottom_group_ll)
    LinearLayout detailBottomGroupLl;
    @Bind(R.id.detail_activity_initiator_title_tv)
    TextView detailActivityInitiatorTitleTv;
    @Bind(R.id.detail_activity_initiator_avator_sdv)
    SimpleDraweeView detailActivityInitiatorAvatorSdv;
    @Bind(R.id.detail_activity_initiator_name_tv)
    TextView detailActivityInitiatorNameTv;
    @Bind(R.id.detail_activity_initiator_ll)
    LinearLayout detailActivityInitiatorLl;
    @Bind(R.id.detail_activity_title_tv)
    TextView detailActivityTitleTv;
    @Bind(R.id.detail_activity_info_tv)
    TextView detailActivityInfoTv;
    @Bind(R.id.detail_activity_info_ll)
    LinearLayout detailActivityInfoLl;
    @Bind(R.id.detail_icons_rl)
    RelativeLayout detailIconsRl;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    private int pId, pPosition = 0;
    private float detailButtonGroupLlHeight = 0f;
    public DetailModel dModel;
    private GatherViewModel gatherViewModel;
    private long startViewTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesting_detail);
        initTitleBar();
        ButterKnife.bind(this);
        titleContentTv.setText(getTitle());
        startViewTime = System.currentTimeMillis();
        detailButtonGroupLl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                detailButtonGroupLlHeight = detailButtonGroupLl.getY() - (detailButtonGroupOutLl.getHeight() - detailButtonGroupLl.getHeight());
            }
        });
        detailOutsideSv.setOverScrollListener(new OutSideScrollView.OverScrolledListener() {
            @Override
            public void onOverScrolled(int scrollX, int scrollY) {
                //LogUtils.json("scrollY=" + scrollY + "//detailButtonGroupLl==" + detailButtonGroupLlHeight);
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
        pPosition = getIntent().getIntExtra("pPosition", 0);
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
        gatherViewModel = new GatherViewModel(pId + "");
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
        if (!StringUtils.isEmpty(dModel.getTel())) {
            StringTokenizer token = new StringTokenizer(dModel.getTel(), " ");
            String phoneHtml = "";
            while (token.hasMoreTokens()) {
                String phoneNum = token.nextToken();
                phoneHtml += "<font color=#dcdddd><a href=\"tel:" + phoneNum + "\">" + phoneNum + "</a> </font>  ";
                LogUtils.json(""+phoneNum+"///////=="+phoneHtml);
            }

                LogUtils.json("/////html//=="+phoneHtml);
            detailStorePhoneTv.setText(Html.fromHtml(phoneHtml));
            detailStorePhoneTv.setMovementMethod(LinkMovementMethod.getInstance());
           /* detailStorePhoneTv.setText(dModel.getTel());
            Linkify.addLinks(detailStorePhoneTv, Linkify.PHONE_NUMBERS);*/
        } else {
            detailStorePhoneTv.setText(dModel.getTel());
        }
        detailSuggestPrb.setRating(dModel.getSuggest());
        if (!StringUtils.isEmpty(dModel.getPrice()) && !"0".equals(dModel.getPrice())) {
            detailAvgPriceTv.setText(String.format(getResources().getString(R.string.detail_price_hint_text), dModel.getPrice()));
        } else {
            detailAvgPriceTv.setVisibility(View.INVISIBLE);
        }
        /*if (StringUtils.isEmpty(dModel.getNearbySpot())) {
            detailNearbyLl.setVisibility(View.GONE);
        } else {
            detailNearbyTcv.setTags(StringUtils.convertStrToArray(dModel.getNearbySpot()));
        }*/
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
        if (dModel.isIsActivity()) {
            detailActivityInfoLl.setVisibility(View.VISIBLE);
            detailActivityInitiatorLl.setVisibility(View.VISIBLE);
            detailIconsRl.setVisibility(View.GONE);
            detailStoreBusinessHoursLl.setVisibility(View.GONE);
            detailActivityInitiatorAvatorSdv.setImageURI(Uri.parse(dModel.getAutorPhoto()));
            detailActivityInitiatorNameTv.setText(dModel.getAutor());
            detailActivityInfoTv.setText(dModel.getActivityInfo());
        } else {
            detailActivityInfoLl.setVisibility(View.GONE);
            detailActivityInitiatorLl.setVisibility(View.GONE);
            detailIconsRl.setVisibility(View.VISIBLE);
            detailStoreBusinessHoursLl.setVisibility(View.VISIBLE);
            detailStoreBusinessHoursValueTv.setText(dModel.getBusinessHours() + " " + dModel.getRestDay());
            initConvenienceIcons();
        }

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
            detailBeenTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_detail_bottom_full_bg));
        } else {
            detailBeenTv.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
            detailBeenTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_detail_bottom_bg));
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
            R.id.detail_button_add_postcard_out_rl, R.id.detail_button_add_postcard_rl, R.id.detail_button_share_out_rl,
            R.id.detail_button_share_rl, R.id.detail_want_tv, R.id.detail_store_address_ll})
    public void detailClick(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        if (dModel != null) {
            switch (v.getId()) {
               /* case R.id.detail_store_phone_ll:
                    callPhone();
                    break;*/
                case R.id.detail_been_tv:
                    userBeen();//用户去过
                    break;
                case R.id.detail_want_tv:
                    //用户想去
                    if (dModel.isIsf()) {
                        // startActivity(new Intent(InterestingDetailsActivity.this, ReserveActivity.class).putExtra("PlaceUrl", dModel.getNet()));
                        Uri uri = Uri.parse(dModel.getNet());
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);
                    } else {
                        WantToGoDialogFg lDialog = WantToGoDialogFg.newInstance();
                        lDialog.show(getFragmentManager(), "blur_sample", new Want2GoClickImpl());
                    }
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

                case R.id.detail_store_address_ll:
                    Intent mapIntent = new Intent(InterestingDetailsActivity.this, PlaceMapActivity.class);
                    mapIntent.putExtra("lat", dModel.getLatitude());
                    mapIntent.putExtra("lon", dModel.getLongitude());
                    mapIntent.putExtra("gdlon", dModel.gdLongitude);
                    mapIntent.putExtra("gdlat", dModel.gdLatitude);
                    mapIntent.putExtra("title", dModel.getName());
                    mapIntent.putExtra("placeAddress", dModel.getAddress());
                    startActivity(mapIntent);
                    break;
            }
        }
    }

    private void userBeen() {
        InterestingDetailPresenter.getUserOutPlace(this, pId, dModel.isIsout(), new InterestingDetailPresenter.DetailDataListener() {
            @Override
            public void onSuccessCall(String str) {
                dModel.setIsout(!dModel.isIsout());
                changeBottomBeenBg(dModel.isIsout());
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
                if (AppContext.selectedPlace != null) {
                    AppContext.selectedPlace.setIsf(dModel.isIsf());
                    AppContext.dCardListNeedUpdate = true;
                }
                if (dModel.isIsf()) {
                    Toast.makeText(InterestingDetailsActivity.this, "收藏成功!", Toast.LENGTH_SHORT).show();
                    if (AppContext.gatherList == null)
                        AppContext.gatherList = new ArrayList<>();
                    AppContext.gatherList.add(new GatherCollectModel(GatherCollectModel.collectPlace, dModel.getPid()));
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

    @Override
    protected void onDestroy() {
        if (gatherViewModel == null)
            gatherViewModel = new GatherViewModel(pId + "");
        gatherViewModel.setViewDuration((System.currentTimeMillis() - startViewTime) / 1000);
        if (AppContext.gatherList == null)
            AppContext.gatherList = new ArrayList<>();
        if (gatherViewModel != null)
            AppContext.gatherList.add(gatherViewModel);
        if (dModel != null && dModel.isIsout()) {
            //  AppContext.dCardList.remove(pPosition);
            AppContext.dCardListRemoveIndex = pPosition;
        }
        if (detailOutsideSv != null) {

        }
        LogUtils.json(" AppContext.dCardListRemoveIndex = pPosition;==" + AppContext.dCardListRemoveIndex);
        super.onDestroy();
    }

    public class Want2GoClickImpl implements WantToGoDialogFg.Wan2GoClickListener {

        @Override
        public void collectClick() {
            if (dModel.isIsf()) {
                Toast.makeText(InterestingDetailsActivity.this, "已经收藏成功了!", Toast.LENGTH_SHORT).show();
            } else {
                setFavorite();
            }
        }

        @Override
        public void reserveClick() {
            if (StringUtils.isEmpty(dModel.getNet())) {
                Toast.makeText(InterestingDetailsActivity.this, "还没找到去往你心里的路...", Toast.LENGTH_SHORT).show();
            } else {
                LogUtils.json("webview ==");
                //startActivity(new Intent(InterestingDetailsActivity.this, ReserveActivity.class).putExtra("PlaceUrl", dModel.getNet()));
                Uri uri = Uri.parse(dModel.getNet());
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);

                /*String keywords = "24740909";
                try {
                  //  keywords = URLEncoder.encode("麻辣诱惑", "UTF-8");
                    Uri url = Uri.parse("dianping://shopinfo?id=" + keywords);
                    Intent intent = new Intent(Intent.ACTION_VIEW, url);
                    startActivity(intent);
                } catch (Exception e) {
                    // 没有安装应用，默认打开HTML5站
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dModel.getNet()));
                    startActivity(intent);
                }
*/
            }
        }
    }
}

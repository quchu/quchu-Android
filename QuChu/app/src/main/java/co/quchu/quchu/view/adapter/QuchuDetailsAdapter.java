package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.widget.HorizontalNumProgressBar;
import co.quchu.quchu.widget.SpacesItemDecoration;
import co.quchu.quchu.widget.TagCloudView;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * Created by admin on 2016/3/7.
 */
public class QuchuDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater mLayoutInflater;
    private Activity mAnchorActivity;
    private DetailModel mData;
    private View.OnClickListener mOnItemClickListener;
    public static final int BLOCK_INDEX = 7;

    public QuchuDetailsAdapter(Activity activity, DetailModel dModel, View.OnClickListener onClickListener) {
        if (null == onClickListener) {
            throw new IllegalArgumentException("OnClickListener Cannot be null");
        }
        mAnchorActivity = activity;
        mData = dModel;
        mLayoutInflater = LayoutInflater.from(activity);
        mOnItemClickListener = onClickListener;
    }
    protected static final int LAYOUT_TYPE_INTRO_IMAGE = 0x0001;
    protected static final int LAYOUT_TYPE_ACTIONBAR = 0x0002;
    //protected static final int LAYOUT_TYPE_SIMPLE_INFO = 0x0003;
    protected static final int LAYOUT_TYPE_CONTACT_INFO = 0x0004;
    protected static final int LAYOUT_TYPE_RATING_INFO = 0x0005;
    protected static final int LAYOUT_TYPE_ADDITIONAL_INFO = 0x0006;
    protected static final int LAYOUT_TYPE_OPENING_INFO = 0x0007;
    protected static final int LAYOUT_TYPE_PARTY_STARTER_INFO = 0x0008;
    protected static final int LAYOUT_TYPE_PARTY_INFO = 0x0009;
    protected static final int LAYOUT_TYPE_IMAGE = 0x0010;
    protected static final int LAYOUT_TYPE_NEARBY = 0x0011;
    protected static final int LAYOUT_TYPE_BLANK = 0x0012;

    protected static final int VIEW_TYPES[] = new int[]{
            LAYOUT_TYPE_INTRO_IMAGE,
            LAYOUT_TYPE_ACTIONBAR,
            //LAYOUT_TYPE_SIMPLE_INFO,
            LAYOUT_TYPE_CONTACT_INFO,
            LAYOUT_TYPE_RATING_INFO,
            LAYOUT_TYPE_ADDITIONAL_INFO,
            LAYOUT_TYPE_OPENING_INFO,
            LAYOUT_TYPE_BLANK,
            LAYOUT_TYPE_BLANK,
            LAYOUT_TYPE_IMAGE,
            LAYOUT_TYPE_NEARBY
    };

    protected static final int VIEW_TYPES_PARTY[] = new int[]{
            LAYOUT_TYPE_INTRO_IMAGE,
            LAYOUT_TYPE_ACTIONBAR,
            //LAYOUT_TYPE_SIMPLE_INFO,
            LAYOUT_TYPE_CONTACT_INFO,
            LAYOUT_TYPE_RATING_INFO,
            LAYOUT_TYPE_BLANK,
            LAYOUT_TYPE_OPENING_INFO,
            LAYOUT_TYPE_PARTY_STARTER_INFO,
            LAYOUT_TYPE_PARTY_INFO,
            LAYOUT_TYPE_IMAGE,
            LAYOUT_TYPE_NEARBY
    };


    @Override
    public int getItemViewType(int position) {

        if (position <= (BLOCK_INDEX + 1)) {
            return null!=mData && mData.isIsActivity()? VIEW_TYPES_PARTY[position]:VIEW_TYPES[position];
        } else if (position >= (BLOCK_INDEX + 1) && position < (mData.getImglist().size() + (BLOCK_INDEX + 1))) {
            return LAYOUT_TYPE_IMAGE;
        } else if (position >= (mData.getImglist().size() + (BLOCK_INDEX + 1))) {
            return LAYOUT_TYPE_NEARBY;
        }
        return LAYOUT_TYPE_IMAGE;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case  LAYOUT_TYPE_INTRO_IMAGE:
                return new IntroImageViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_image, parent, false));
            case  LAYOUT_TYPE_ACTIONBAR:
                return new ActionViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_actionbar, parent, false));
//            case  LAYOUT_TYPE_SIMPLE_INFO:
//                return new SimpleInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_simple_info, parent, false));
            case  LAYOUT_TYPE_CONTACT_INFO:
                return new ContactInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_contact_info, parent, false));
            case  LAYOUT_TYPE_RATING_INFO:
                return new RatingInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_rating_info, parent, false));
            case  LAYOUT_TYPE_ADDITIONAL_INFO:
                if (null == mData || mData.isIsActivity()) {
                    return new BlankViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_blank, parent, false));
                } else {
                    return new AdditionalInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_additional_info, parent, false));
                }
            case  LAYOUT_TYPE_OPENING_INFO:
                return new OpeningInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_opening_info, parent, false));
            case  LAYOUT_TYPE_PARTY_STARTER_INFO:
                if (null != mData && mData.isIsActivity()) {
                    return new StarterInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_party_starter_info, parent, false));
                } else {
                    return new BlankViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_blank, parent, false));
                }
            case  LAYOUT_TYPE_PARTY_INFO:
                if (null != mData && mData.isIsActivity()) {
                    return new PartyInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_party_info, parent, false));
                } else {
                    return new BlankViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_blank, parent, false));
                }
            case  LAYOUT_TYPE_IMAGE:
                return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_card_image, parent, false));
            case  LAYOUT_TYPE_NEARBY:
                return new NearByViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_nearby, parent, false));
            default:
                return new BlankViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_blank, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (null == mData) {
            return;
        }
        if (holder instanceof IntroImageViewHolder) {
            if (null != mData.getCover()) {
                ((IntroImageViewHolder) holder).simpleDraweeView.setImageURI(Uri.parse(mData.getCover()));
                ((IntroImageViewHolder) holder).simpleDraweeView.setAspectRatio(1.2f);
            }

            ((IntroImageViewHolder) holder).detail_store_name_tv.setText(null != mData.getName() ? mData.getName() : "");
            ((IntroImageViewHolder) holder).detail_suggest_prb.setRating(mData.getSuggest());
            if (null != mData && !StringUtils.isEmpty(mData.getPrice()) && !"0".equals(mData.getPrice())) {
                ((IntroImageViewHolder) holder).detail_avg_price_tv.setText(String.format(mAnchorActivity.getResources().getString(mData.isIsActivity() ? R.string.detail_price_hint_text_activity : R.string.detail_price_hint_text), mData.getPrice()));
                ((IntroImageViewHolder) holder).detail_avg_price_tv.setVisibility(View.VISIBLE);
            } else {
                ((IntroImageViewHolder) holder).detail_avg_price_tv.setVisibility(View.INVISIBLE);
            }

            ((IntroImageViewHolder) holder).TagCloudView.setVisibility(View.VISIBLE);
            if (null != mData.getTags() && mData.getTags().size() > 0) {
                ArrayList<String> tags = new ArrayList<>();
                for (int i = 0; i < mData.getTags().size(); i++) {
                    tags.add(mData.getTags().get(i).getZh());
                }
                ((IntroImageViewHolder) holder).TagCloudView.setTags(tags);
            } else {
                ((IntroImageViewHolder) holder).TagCloudView.setTags(null);
                ((IntroImageViewHolder) holder).TagCloudView.setVisibility(View.GONE);
            }
        } else if (holder instanceof ActionViewHolder) {
            ((ActionViewHolder) holder).detail_button_collect_iv.setImageResource(mData.isIsf() ? R.mipmap.ic_detail_collect : R.mipmap.ic_detail_uncollect);
            ((ActionViewHolder) holder).detail_button_add_postcard_rl.setOnClickListener(mOnItemClickListener);
            ((ActionViewHolder) holder).detail_button_collect_rl.setOnClickListener(mOnItemClickListener);
            ((ActionViewHolder) holder).detail_button_share_rl.setOnClickListener(mOnItemClickListener);
        }  else if (holder instanceof ContactInfoViewHolder) {
            if (null == mData.getTraffic() || StringUtils.isEmpty(mData.getTraffic())) {
                ((ContactInfoViewHolder) holder).detail_store_address_tv.setText(mData.getAddress());
                ((ContactInfoViewHolder) holder).detail_store_address_ll.setOnClickListener(mOnItemClickListener);

            } else if (!StringUtils.isEmpty(mData.getTraffic())) {
                ((ContactInfoViewHolder) holder).detail_store_address_tv.setText(String.format(mAnchorActivity.getResources().getString(R.string.detail_address_hint_text), mData.getAddress(), mData.getTraffic()));
                ((ContactInfoViewHolder) holder).detail_store_address_ll.setOnClickListener(mOnItemClickListener);
            }
            if (null != mData && !StringUtils.isEmpty(mData.getTel())) {
                StringTokenizer token = new StringTokenizer(mData.getTel(), " ");
                StringBuffer phoneHtml = new StringBuffer();
                while (token.hasMoreTokens()) {
                    String phoneNum = token.nextToken();
                    phoneHtml.append("<font color=#dcdddd><a href=\"tel:").append(phoneNum).append("\">").append(phoneNum).append("</a> </font>  ");
                }
                ((ContactInfoViewHolder) holder).detail_store_phone_tv.setText(Html.fromHtml(phoneHtml.toString()));
                ((ContactInfoViewHolder) holder).detail_store_phone_tv.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                ((ContactInfoViewHolder) holder).detail_store_phone_tv.clearComposingText();
            }
        } else if (holder instanceof RatingInfoViewHolder) {
            if (null != mData.getGenes() && mData.getGenes().size() >= 3) {
                ((RatingInfoViewHolder) holder).detail_progress_one.setProgressName(mData.getGenes().get(0).getKey());
                ((RatingInfoViewHolder) holder).detail_progress_one.setProgress(mData.getGenes().get(0).getValue());
                ((RatingInfoViewHolder) holder).detail_progress_tow.setProgressName(mData.getGenes().get(1).getKey());
                ((RatingInfoViewHolder) holder).detail_progress_tow.setProgress(mData.getGenes().get(1).getValue());
                ((RatingInfoViewHolder) holder).detail_progress_three.setProgressName(mData.getGenes().get(2).getKey());
                ((RatingInfoViewHolder) holder).detail_progress_three.setProgress(mData.getGenes().get(2).getValue());
            }
        } else if (holder instanceof AdditionalInfoViewHolder) {
            String iconStr;
            ((AdditionalInfoViewHolder) holder).detail_box_rb.setChecked(false);
            ((AdditionalInfoViewHolder) holder).detail_cardpay_rb.setChecked(false);
            ((AdditionalInfoViewHolder) holder).detail_cash_rb.setChecked(false);
            ((AdditionalInfoViewHolder) holder).detail_mess_rb.setChecked(false);
            ((AdditionalInfoViewHolder) holder).detail_takeout_rb.setChecked(false);
            ((AdditionalInfoViewHolder) holder).detail_wechat_rb.setChecked(false);
            ((AdditionalInfoViewHolder) holder).detail_zfb_rb.setChecked(false);
            if (!mData.isIsActivity()) {
                ((AdditionalInfoViewHolder) holder).detail_icons_rl.setVisibility(View.VISIBLE);
                if (null != mData.getIcons() && mData.getIcons().size() > 0) {
                    for (int i = 0; i < mData.getIcons().size(); i++) {
                        iconStr = mData.getIcons().get(i).getZh();
                        switch (iconStr) {
                            case "包厢":
                                ((AdditionalInfoViewHolder) holder).detail_box_rb.setChecked(true);
                                break;
                            case "堂食":
                                ((AdditionalInfoViewHolder) holder).detail_mess_rb.setChecked(true);
                                break;
                            case "外送":
                                ((AdditionalInfoViewHolder) holder).detail_takeout_rb.setChecked(true);
                                break;
                            case "现金":
                                ((AdditionalInfoViewHolder) holder).detail_cash_rb.setChecked(true);
                                break;
                            case "刷卡":
                                ((AdditionalInfoViewHolder) holder).detail_cardpay_rb.setChecked(true);
                                break;
                            case "支付宝":
                                ((AdditionalInfoViewHolder) holder).detail_zfb_rb.setChecked(true);
                                break;
                            case "微信":
                                ((AdditionalInfoViewHolder) holder).detail_wechat_rb.setChecked(true);
                                break;
                        }
                    }
                }
            } else {
                ((AdditionalInfoViewHolder) holder).detail_icons_rl.setVisibility(View.GONE);
            }


        } else if (holder instanceof OpeningInfoViewHolder) {
            if (mData.isIsActivity() && null != mData.getBusinessHours() && null != mData.getRestDay()) {
                ((OpeningInfoViewHolder) holder).detail_store_business_hours_key_tv.setText("报名时间");
                ((OpeningInfoViewHolder) holder).detail_store_business_hours_value_tv.setText(mData.getBusinessHours() + " " + mData.getRestDay());
            } else {
                ((OpeningInfoViewHolder) holder).detail_store_business_hours_key_tv.setText("营业时间");
                ((OpeningInfoViewHolder) holder).detail_store_business_hours_value_tv.setText(mData.getBusinessHours() + " " + mData.getRestDay());
            }
        } else if (holder instanceof StarterInfoViewHolder) {

            ((StarterInfoViewHolder) holder).detail_activity_initiator_ll.setVisibility(mData.isIsActivity() ? View.VISIBLE : View.GONE);
            ((StarterInfoViewHolder) holder).detail_activity_initiator_name_tv.setText(null != mData.getAutor() ? mData.getAutor() : "");
            if (null != mData.getAutorPhoto()) {
                ((StarterInfoViewHolder) holder).detail_activity_initiator_avator_sdv.setImageURI(Uri.parse(mData.getAutorPhoto()));
            }

        } else if (holder instanceof PartyInfoViewHolder) {

            ((PartyInfoViewHolder) holder).detail_activity_info_ll.setVisibility(mData.isIsActivity() ? View.VISIBLE : View.GONE);
            ((PartyInfoViewHolder) holder).detail_activity_info_tv.setText(mData.isIsActivity() && null != mData.getActivityInfo() ? mData.getActivityInfo() : "");
        } else if (holder instanceof ImageViewHolder) {
            int imgIndex = position - BLOCK_INDEX;
            if (null != mData.getImglist() && mData.getImglist().size() > imgIndex) {

                if (null != mData.getImglist().get(imgIndex).getImgpath()) {
                    String strUri = mData.getImglist().get(imgIndex).getImgpath();
                    ((ImageViewHolder) holder).item_card_image_sdv.setImageURI(Uri.parse(strUri));

                    if(0==mData.getImglist().get(imgIndex).getWidth()||0==mData.getImglist().get(imgIndex).getHeight()){
                        ((ImageViewHolder) holder).item_card_image_sdv.setAspectRatio(1.2f);
                    }else{
                        ((ImageViewHolder) holder).item_card_image_sdv.setAspectRatio((float) mData.getImglist().get(imgIndex).getWidth() / (float) mData.getImglist().get(imgIndex).getHeight());
                    }
                }
            } else {
                ((ImageViewHolder) holder).item_card_image_sdv.setAspectRatio(1.2f);
            }

        } else if (holder instanceof NearByViewHolder) {
            if (null != mData.getNearPlace()) {
                int imgIndex = position - BLOCK_INDEX;
                if (null != mData.getImglist()) {
                    imgIndex -= mData.getImglist().size();
                }

                ((NearByViewHolder) holder).textView.setText(mData.getNearPlace().get(imgIndex - 1).getTag());
                ((NearByViewHolder) holder).recyclerview.setLayoutManager(new LinearLayoutManager(mAnchorActivity, LinearLayoutManager.HORIZONTAL, false));
                ((NearByViewHolder) holder).recyclerview.setAdapter(new NearbySpotAdapter(mData.getNearPlace().get(imgIndex - 1).getPlaces()));
                if (null!=((NearByViewHolder) holder).recyclerview.getTag() && ((boolean)((NearByViewHolder) holder).recyclerview.getTag())){

                }else{
                    ((NearByViewHolder) holder).recyclerview.addItemDecoration(new SpacesItemDecoration(mAnchorActivity.getResources().getDimensionPixelSize(R.dimen.half_margin)));
                    ((NearByViewHolder) holder).recyclerview.setTag(true);
                }

                ((NearByViewHolder) holder).recyclerview.setOnTouchListener(listener);

            }
        }
    }


    private View.OnTouchListener listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (((RecyclerView) v).getChildCount() >= 0) {
                if (((RecyclerView) v).getChildAt(0).getLeft()-mAnchorActivity.getResources().getDimensionPixelSize(R.dimen.half_margin) == 0) {
                    ((QuchuDetailsActivity) mAnchorActivity).getSwipeBackLayout().setEnableGesture(true);
                } else {
                    ((QuchuDetailsActivity) mAnchorActivity).getSwipeBackLayout().setEnableGesture(false);
                }
            }
            return false;
        }
    };

    @Override
    public int getItemCount() {
        int basicCount = (BLOCK_INDEX + 1);
        if (null != mData && null != mData.getImglist()) {
            basicCount += mData.getImglist().size();
        }
        if (null != mData && null != mData.getNearPlace()) {
            basicCount += mData.getNearPlace().size();
        }
        return basicCount;
    }

    public static class IntroImageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_store_name_tv)
        TextView detail_store_name_tv;
        @Bind(R.id.detail_suggest_prb)
        ProperRatingBar detail_suggest_prb;
        @Bind(R.id.detail_avg_price_tv)
        TextView detail_avg_price_tv;
        @Bind(R.id.detail_store_tagclound_tcv)
        TagCloudView TagCloudView;
        @Bind(R.id.item_card_image_sdv)
        SimpleDraweeView simpleDraweeView;

        IntroImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class ActionViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_button_group_ll)
        LinearLayout detail_button_group_ll;
        @Bind(R.id.detail_button_collect_rl)
        RelativeLayout detail_button_collect_rl;
        @Bind(R.id.detail_button_collect_iv)
        ImageView detail_button_collect_iv;
        @Bind(R.id.detail_button_share_rl)
        RelativeLayout detail_button_share_rl;
        @Bind(R.id.detail_button_add_postcard_rl)
        RelativeLayout detail_button_add_postcard_rl;

        ActionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public static class ContactInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_store_address_ll)
        LinearLayout detail_store_address_ll;
        @Bind(R.id.detail_store_address_tv)
        TextView detail_store_address_tv;
        @Bind(R.id.detail_store_phone_ll)
        LinearLayout detail_store_phone_ll;
        @Bind(R.id.detail_store_phone_tv)
        TextView detail_store_phone_tv;

        ContactInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class RatingInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_progress_one)
        HorizontalNumProgressBar detail_progress_one;
        @Bind(R.id.detail_progress_tow)
        HorizontalNumProgressBar detail_progress_tow;
        @Bind(R.id.detail_progress_three)
        HorizontalNumProgressBar detail_progress_three;

        RatingInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class AdditionalInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_icons_rl)
        RelativeLayout detail_icons_rl;
        @Bind(R.id.detail_box_rb)
        RadioButton detail_box_rb;
        @Bind(R.id.detail_mess_rb)
        RadioButton detail_mess_rb;
        @Bind(R.id.detail_takeout_rb)
        RadioButton detail_takeout_rb;
        @Bind(R.id.detail_cash_rb)
        RadioButton detail_cash_rb;
        @Bind(R.id.detail_cardpay_rb)
        RadioButton detail_cardpay_rb;
        @Bind(R.id.detail_zfb_rb)
        RadioButton detail_zfb_rb;
        @Bind(R.id.detail_wechat_rb)
        RadioButton detail_wechat_rb;

        AdditionalInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class OpeningInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_store_business_hours_key_tv)
        TextView detail_store_business_hours_key_tv;
        @Bind(R.id.detail_store_business_hours_value_tv)
        TextView detail_store_business_hours_value_tv;

        OpeningInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class StarterInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_activity_initiator_ll)
        LinearLayout detail_activity_initiator_ll;
        @Bind(R.id.detail_activity_initiator_title_tv)
        TextView detail_activity_initiator_title_tv;
        @Bind(R.id.detail_activity_initiator_avator_sdv)
        SimpleDraweeView detail_activity_initiator_avator_sdv;
        @Bind(R.id.detail_activity_initiator_name_tv)
        TextView detail_activity_initiator_name_tv;

        StarterInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class PartyInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_activity_info_ll)
        LinearLayout detail_activity_info_ll;
        @Bind(R.id.detail_activity_title_tv)
        TextView detail_activity_title_tv;
        @Bind(R.id.detail_activity_info_tv)
        TextView detail_activity_info_tv;

        PartyInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_card_image_sdv)
        SimpleDraweeView item_card_image_sdv;

        ImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class NearByViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.recyclerview)
        RecyclerView recyclerview;
        @Bind(R.id.tvTagName)
        TextView textView;

        NearByViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class NearbyItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivImage)
        SimpleDraweeView ivImage;
        @Bind(R.id.tvName)
        TextView tvName;

        NearbyItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setLayoutParams(new ViewGroup.LayoutParams((int)(AppContext.Width/3.5f), (int)(AppContext.Width/3.5f)));
        }
    }

    public static class BlankViewHolder extends RecyclerView.ViewHolder {
        BlankViewHolder(View view) {
            super(view);
        }
    }




    class NearbySpotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<DetailModel.Places> mData;

        public NearbySpotAdapter(List<DetailModel.Places> data) {
            this.mData = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NearbyItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quchu_detail_nearby_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((NearbyItemViewHolder) holder).tvName.setText(mData.get(position).getName());
            if (null != mData.get(position).getCover() ) {

                ((NearbyItemViewHolder) holder).ivImage.setImageURI(Uri.parse(mData.get(position).getCover()));
                ((NearbyItemViewHolder) holder).ivImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!KeyboardUtils.isFastDoubleClick()){
                            mAnchorActivity.startActivity(new Intent(mAnchorActivity, QuchuDetailsActivity.class).putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, mData.get(position).getPid()));
                        }

                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (null != mData) {
                return mData.size();
            } else {
                return 0;
            }
        }

    }


}

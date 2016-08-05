package co.quchu.quchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.utils.DateUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by Nico on 16/5/27.
 */
public class ShareQuchuActivity extends BaseActivity {

    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_share_quchu);
    }

    @Bind(R.id.ivQuchuCover)
    SimpleDraweeView ivQuchuCover;
    @Bind(R.id.tvPlaceName)
    TextView tvPlaceName;
    @Bind(R.id.tvPlaceLocation)
    TextView tvPlaceLocation;
    @Bind(R.id.tvShare)
    TextView tvShare;
    @Bind(R.id.tvPhone)
    TextView tvPhone;
    @Bind(R.id.tvMoney)
    TextView tvMoney;
    @Bind(R.id.prbRating)
    RatingBar prbRating;
    @Bind(R.id.tvAuthor)
    TextView tvAuthor;
    @Bind(R.id.tvDate)
    TextView tvDate;

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    public static final String BUNDLE_KEY_QUCHU_MODEL = "BUNDLE_KEY_QUCHU_MODEL";

    public DetailModel mDetailModel;


    public static Intent getStartIntent(Context context, DetailModel detailModel){
        Intent intent = new Intent(context,ShareQuchuActivity.class);
        intent.putExtra(BUNDLE_KEY_QUCHU_MODEL,detailModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_quchu);
        ButterKnife.bind(this);



        mDetailModel = (DetailModel) getIntent().getSerializableExtra(BUNDLE_KEY_QUCHU_MODEL);
        getEnhancedToolbar().getTitleTv().setText(R.string.share_to);
        ivQuchuCover.setImageURI(Uri.parse(mDetailModel.getCover()));
        ivQuchuCover.setAspectRatio(1.5f);
        tvPlaceLocation.setText(mDetailModel.getAddress());

        tvPlaceName.setText(R.string.guess_where_is_it);

        tvPhone.setText(mDetailModel.getTel());
        tvMoney.setText(mDetailModel.getPrice()+"元/人");
        prbRating.setRating(mDetailModel.getSuggest());
        tvAuthor.setText("By "+AppContext.user.getUsername());
        tvDate.setText(DateUtils.getDateToString(DateUtils.DATA_FORMAT_MM_DD_YYYY_CLEAR,System.currentTimeMillis()));



        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url;
                    url = "http://www.quchu.co/h5/place/place_info_"+mDetailModel.getPid()+".html";
                    ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(url,mDetailModel.getName(),mDetailModel.getCover());
                    shareDialogFg.show(getSupportFragmentManager(), "share_dialog");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}

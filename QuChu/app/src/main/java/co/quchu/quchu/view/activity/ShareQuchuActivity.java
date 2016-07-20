package co.quchu.quchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ShareDialogFg;

/**
 * Created by Nico on 16/5/27.
 */
public class ShareQuchuActivity extends BaseActivity {


    @Bind(R.id.ivQuchuCover)
    SimpleDraweeView ivQuchuCover;
    @Bind(R.id.tvPlaceName)
    TextView tvPlaceName;
    @Bind(R.id.tvPlaceLocation)
    TextView tvPlaceLocation;
    @Bind(R.id.tvShareText)
    TextView tvShareText;
    @Bind(R.id.tvShare)
    TextView tvShare;

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    public static final String BUNDLE_KEY_QUCHU_COVER = "BUNDLE_KEY_QUCHU_COVER";
    public static final String BUNDLE_KEY_QUCHU_NAME = "BUNDLE_KEY_QUCHU_NAME";
    public static final String BUNDLE_KEY_QUCHU_ADDRESS = "BUNDLE_KEY_QUCHU_ADDRESS";
    public static final String BUNDLE_KEY_QUCHU_PID = "BUNDLE_KEY_QUCHU_PID";

    public String mQuchuCover;
    public String mQuchuName;
    public String mQuchuAddress;
    public int mQuchuId;
    public boolean mJokingMode = true;


    public static Intent getStartIntent(Context context,String cover,String name,String address,int id){
        Intent intent = new Intent(context,ShareQuchuActivity.class);
        intent.putExtra(BUNDLE_KEY_QUCHU_COVER,cover);
        intent.putExtra(BUNDLE_KEY_QUCHU_NAME,name);
        intent.putExtra(BUNDLE_KEY_QUCHU_ADDRESS,address);
        intent.putExtra(BUNDLE_KEY_QUCHU_PID,id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_quchu);
        ButterKnife.bind(this);

        getEnhancedToolbar().getRightTv().setText(R.string.share_mode_normal);
        getEnhancedToolbar().getRightTv().setTextColor(getResources().getColor(R.color.standard_color_white));
        getEnhancedToolbar().getTitleTv().setText("");

        getEnhancedToolbar().getLeftIv().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        getEnhancedToolbar().getRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJokingMode = !mJokingMode;
                if (!mJokingMode){
                    getEnhancedToolbar().getRightTv().setText(R.string.share_mode_joking);
                    tvPlaceName.setText(mQuchuName);
                    tvPlaceName.setTextColor(getResources().getColor(R.color.standard_color_black));
                    tvPlaceName.setBackgroundColor(getResources().getColor(R.color.transparent));
                }else{
                    getEnhancedToolbar().getRightTv().setText(R.string.share_mode_normal);
                    tvPlaceName.setText(R.string.guess_where_is_it);
                    tvPlaceName.setTextColor(getResources().getColor(R.color.standard_color_white));
                    tvPlaceName.setBackgroundColor(getResources().getColor(R.color.standard_color_black));
                }
            }
        });
        mQuchuCover = getIntent().getStringExtra(BUNDLE_KEY_QUCHU_COVER);
        mQuchuName = getIntent().getStringExtra(BUNDLE_KEY_QUCHU_NAME);
        mQuchuAddress = getIntent().getStringExtra(BUNDLE_KEY_QUCHU_ADDRESS);
        mQuchuId = getIntent().getIntExtra(BUNDLE_KEY_QUCHU_PID,-1);

        ivQuchuCover.setImageURI(Uri.parse(mQuchuCover));
        ivQuchuCover.setAspectRatio(1.5f);
        tvPlaceLocation.setText(mQuchuAddress);

        tvPlaceName.setText(R.string.guess_where_is_it);
        tvPlaceName.setTextColor(getResources().getColor(R.color.standard_color_white));
        tvPlaceName.setBackgroundColor(getResources().getColor(R.color.standard_color_black));

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url;
                    if (mJokingMode){
                        url = "http://www.quchu.co/h5/place/place_info_"+mQuchuId+".html";
                    }else{
                        url = "http://www.quchu.co/h5/place_tk/place_info_"+mQuchuId+".html";
                    }
                    ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(url,mQuchuName,mQuchuCover);
                    shareDialogFg.show(getSupportFragmentManager(), "share_dialog");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}

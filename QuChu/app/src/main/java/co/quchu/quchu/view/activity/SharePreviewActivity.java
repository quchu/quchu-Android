package co.quchu.quchu.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.ImageModel;

public class SharePreviewActivity extends BaseActivity {

    @Bind(R.id.simpleDraweeView)
    SimpleDraweeView simpleDraweeView;
    @Bind(R.id.commemt)
    TextView commemt;
    @Bind(R.id.headImage)
    SimpleDraweeView headImage;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.actionShare)
    TextView actionShare;

    public static final String REQUEST_KEY_COVER = "cover";
    public static final String REQUEST_KEY_USER_NAME = "name";
    public static final String REQUEST_KEY_COMMENT = "comment";
    public static final String REQUEST_KEY_HEAD_IMAGE = "headImage";

    public static final String REQUEST_KEY_FOOTPRINT_ID = "id";
    public static final String REQUEST_KEY_PLACE_NAME = "placeName";
    public static final String REQUEST_KEY_IMAGE_ID = "imageId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_preview);
        ButterKnife.bind(this);
        EnhancedToolbar toolbar = getEnhancedToolbar();
        toolbar.getTitleTv().setText("分享");
        ImageModel coverModel = getIntent().getParcelableExtra(REQUEST_KEY_COVER);
        simpleDraweeView.setAspectRatio(coverModel.getWidth() / (float) coverModel.getHeight());
        simpleDraweeView.setImageURI(Uri.parse(coverModel.getPath()));
        headImage.setImageURI(Uri.parse(getIntent().getStringExtra(REQUEST_KEY_HEAD_IMAGE)));
        name.setText(getIntent().getStringExtra(REQUEST_KEY_USER_NAME));
        commemt.setText(getIntent().getStringExtra(REQUEST_KEY_COMMENT));
        actionShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(getIntent().getIntExtra(REQUEST_KEY_FOOTPRINT_ID, 0),
                        getIntent().getStringExtra(REQUEST_KEY_PLACE_NAME), false, getIntent().getIntExtra(REQUEST_KEY_IMAGE_ID, 0));
                shareDialogFg.show(getSupportFragmentManager(), "share_postcard");
            }
        });
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }
}

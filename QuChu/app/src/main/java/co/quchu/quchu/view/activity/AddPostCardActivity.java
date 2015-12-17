package co.quchu.quchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.photo.Bimp;
import co.quchu.quchu.photo.ImageBucketActivity;
import co.quchu.quchu.utils.FileUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.AddPostCardGridAdapter;
import co.quchu.quchu.widget.InnerGridView;
import co.quchu.quchu.widget.OutSideScrollView;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;
import co.quchu.quchu.widget.ratingbar.RatingListener;

/**
 * AddPostCardActivity
 * User: Chenhs
 * Date: 2015-12-17
 */
public class AddPostCardActivity extends BaseActivity {


    @Bind(R.id.add_postcard_bottom_btn)
    Button addPostcardBottomBtn;
    @Bind(R.id.add_postcard_group_ll)
    RelativeLayout addPostcardGroupLl;
    @Bind(R.id.add_postcard_top_tv)
    TextView addPostcardTopTv;
    @Bind(R.id.add_postcard_top_ll)
    LinearLayout addPostcardTopLl;
    @Bind(R.id.add_postcard_suggest_tv)
    TextView addPostcardSuggestTv;
    @Bind(R.id.add_postcard_suggest_prb)
    ProperRatingBar addPostcardSuggestPrb;
    @Bind(R.id.add_postcard_about_Place_tv)
    EditText addPostcardAboutPlaceTv;
    @Bind(R.id.add_postcard_image_igv)
    InnerGridView addPostcardImageIgv;
    @Bind(R.id.detail_outside_sv)
    OutSideScrollView detailOutsideSv;
    private String pName = "";
    AddPostCardGridAdapter adapter;

    private boolean isNeedUpdate=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_add_postcard);
        pName = getIntent().getStringExtra("pName");
        ButterKnife.bind(this);
        setPRBlistener();
        addPostcardTopTv.setText(String.format("你对%s印象如何?", pName));
        StringUtils.alterTextColor(addPostcardTopTv, 2, 2 + pName.length(), R.color.gene_textcolor_yellow);

        adapter = new AddPostCardGridAdapter(this);
        addPostcardImageIgv.setAdapter(adapter);
        addPostcardImageIgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    LogUtils.json("jump 2selected image view");
                    new PopupWindows(AddPostCardActivity.this, addPostcardImageIgv);
                }
            }
        });
    }

    private void setPRBlistener() {
        addPostcardSuggestPrb.setListener(new RatingListener() {
            @Override
            public void onRatePicked(ProperRatingBar ratingBar) {
                LogUtils.json("rating==" + ratingBar.getRating());
                String[] prbHintText = getResources().getStringArray(R.array.addPostCardSuggetStrings);
                addPostcardSuggestTv.setText(prbHintText[ratingBar.getRating()]);
            }
        });
    }

    private String paths = "";
    private static final int TAKE_PICTURE = 0x000000;


   public void photo() {
       Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       File src = new File(FileUtils.SDPATH);
       if (!src.exists()) {
           src.mkdirs();
       }
       File file = new File(src, String.valueOf(System.currentTimeMillis())
               + ".jpg");
       paths = file.getPath();
       Uri imageUri = Uri.fromFile(file);

       openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
       startActivityForResult(openCameraIntent, TAKE_PICTURE);
   }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                LogUtils.json("path=="+paths);
                if (Bimp.drr.size() < 9 && resultCode == -1) {
                    Bimp.drr.add(paths);
                }
                break;
        }
    }

    protected void onRestart() {
        adapter.update();
        LogUtils.json("on restart");
        super.onRestart();
    }


    @Override
    protected void onResume() {
        if (isNeedUpdate) {
            if (Bimp.drr.size() > 0 && adapter != null)
                adapter.notifyDataSetChanged();
        }
        LogUtils.json("on onResume");
        super.onResume();
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.item_popupwindows, null);

            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(AddPostCardActivity.this,
                            ImageBucketActivity.class);
                    startActivity(intent);
                    isNeedUpdate=true;
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

}

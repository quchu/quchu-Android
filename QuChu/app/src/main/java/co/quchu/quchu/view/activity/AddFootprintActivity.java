package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.dialog.CommonDialog;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.adapter.RatingQuchuDialogAdapter;
import co.quchu.quchu.gallery.GalleryFinal;
import co.quchu.quchu.gallery.model.PhotoInfo;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.model.VisitedInfoModel;
import co.quchu.quchu.net.ImageUpload;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.view.adapter.FindPositionAdapter;
import co.quchu.quchu.widget.SelectedImagePopWin;
import com.android.volley.VolleyError;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 16/4/12.
 */
public class AddFootprintActivity extends BaseBehaviorActivity
    implements FindPositionAdapter.ItemClickListener {

  @Bind(R.id.etContent) EditText etContent;
  @Bind(R.id.recyclerView) RecyclerView recyclerView;
  @Bind(R.id.rvTags) RecyclerView rvTags;
  @Bind(R.id.textLength) TextView textLength;
  @Bind(R.id.rbRating) RatingBar rbRating;
  @Bind(R.id.tvSubmit) TextView tvSubmit;

  TextView titleName;
  List<PhotoInfo> photoInfos;
  List<TagsModel> tags;

  private FindPositionAdapter adapter;
  private PhotoInfo tackImage;

  public static final String REQUEST_KEY_ID = "REQUEST_KEY_ID";
  public static final String REQUEST_KEY_ENTITY = "REQUEST_KEY_ENTITY";
  public static final String REQUEST_KEY_FROM_PAGE_NAME = "REQUEST_KEY_FROM_PAGE_NAME";

  private int pId;
  private int rating = 0;
  private String fromPageName;
  private VisitedInfoModel mData;
  private RatingQuchuDialogAdapter ratingQuchuDialogAdapter;

  private boolean dataChange = false;

  @Override protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override public int getUserBehaviorPageId() {
    return 106;
  }

  @Override protected String getPageNameCN() {
    return getString(R.string.rating);
  }

  public static void enterActivity(Activity from, VisitedInfoModel infoModel, int pid,
      String pageName) {
    Intent intent = new Intent(from, AddFootprintActivity.class);
    intent.putExtra(REQUEST_KEY_ID, pid);
    intent.putExtra(REQUEST_KEY_ENTITY, infoModel);
    intent.putExtra(REQUEST_KEY_FROM_PAGE_NAME, pageName);
    from.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_footprint);
    ButterKnife.bind(this);

    pId = getIntent().getIntExtra(REQUEST_KEY_ID, -1);
    fromPageName = getIntent().getStringExtra(REQUEST_KEY_FROM_PAGE_NAME);
    mData = (VisitedInfoModel) getIntent().getSerializableExtra(REQUEST_KEY_ENTITY);
    rating = mData.getScore();
    tags = mData.getResult();

    for (int i = 0; i < mData.getResult().size(); i++) {
      mData.getResult().get(i).setPraise(false);
    }
    tvSubmit.setBackgroundResource(R.color.colorBorder);

    getEnhancedToolbar().getTitleTv().setText(R.string.rating);

    etContent.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        textLength.setText("剩余" + (140 - s.length()) + "字");
      }
    });

    init();
    ratingQuchuDialogAdapter =
        new RatingQuchuDialogAdapter(tags, new RatingQuchuDialogAdapter.OnItemSelectedListener() {
          @Override public void onSelected(int index, boolean select) {
            tags.get(index).setPraise(!tags.get(index).isPraise());
            ratingQuchuDialogAdapter.notifyDataSetChanged();
          }
        });
    rvTags.setLayoutManager(new GridLayoutManager(AddFootprintActivity.this, 3));
    rvTags.setAdapter(ratingQuchuDialogAdapter);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
    GalleryFinal.setmCallback(null);
  }

  private void init() {
    photoInfos = new ArrayList<>();
    adapter = new FindPositionAdapter();
    tackImage = new PhotoInfo();

    tackImage.setPhotoPath("res:///" + R.mipmap.ic_take_photo);

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

    //rbRating.setRating(rating);
    rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
      @Override public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        tvSubmit.setBackgroundResource(R.color.standard_color_yellow);
        dataChange = true;
        if (v<1){
          ratingBar.setRating(1);
        }
      }
    });

    if (photoInfos.size() < 4) {
      photoInfos.add(tackImage);
    }
    adapter.setImages(photoInfos);
    adapter.setListener(this);

    recyclerView.setAdapter(adapter);
    tvSubmit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (dataChange) {
          List<String> im = new ArrayList<>();
          for (PhotoInfo item : photoInfos) {
            if (item.getPhotoPath().contains("file://")) {
              im.add(Uri.parse(item.getPhotoPath()).getPath());
            } else if (item.getPhotoPath().contains("http://")) {
              im.add(item.getPhotoPath());
            }
          }
          if (im.size() > 0) {
            DialogUtil.showProgess(AddFootprintActivity.this, "母星正在接收中");
            new ImageUpload(AddFootprintActivity.this, im,
                new ImageUpload.UploadResponseListener() {
                  @Override public void finish(String result) {

                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < tags.size(); i++) {
                      if (tags.get(i).isPraise()) {
                        sb.append(tags.get(i).getTagId()).append(",");
                      }
                    }
                    String strTagIds = sb.toString();
                    if (null != strTagIds && strTagIds.endsWith(",")) {
                      strTagIds = strTagIds.substring(0, strTagIds.length() - 1);
                    }

                    saveRating(result, strTagIds, pId, etContent.getText().toString(),
                        (int) rbRating.getRating());
                  }

                  @Override public void error() {
                    DialogUtil.dismissProgessDirectly();
                    Toast.makeText(AddFootprintActivity.this, getString(R.string.network_error),
                        Toast.LENGTH_SHORT).show();
                  }
                });
          } else {
            DialogUtil.showProgess(AddFootprintActivity.this, "母星正在接收中");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < tags.size(); i++) {
              if (tags.get(i).isPraise()) {
                sb.append(tags.get(i).getTagId()).append(",");
              }
            }
            String strTagIds = sb.toString();
            if (null != strTagIds && strTagIds.endsWith(",")) {
              strTagIds = strTagIds.substring(0, strTagIds.length() - 1);
            }
            saveRating("", strTagIds, pId, etContent.getText().toString(), (int) rbRating.getRating());
          }
        } else {
          Toast.makeText(AddFootprintActivity.this, "你还没有做出评价~", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  @Override public void itemClick(boolean isDelete, int position, final PhotoInfo photoInfo) {
    if (!isDelete && position == photoInfos.size()-1 && photoInfo.getPhotoPath().contains("res:///")) {
      View view = getWindow().peekDecorView();
      if (view != null) {
        InputMethodManager inputmanger =
            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }
      new SelectedImagePopWin(AddFootprintActivity.this, recyclerView, photoInfos, 4,
          new GalleryFinal.OnHanlderResultCallback() {
            @Override public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
              for (PhotoInfo info : resultList) {
                String path = info.getPhotoPath();
                if (!path.startsWith("file://") && !path.startsWith("res:///") && !path.startsWith(
                    "http://")) {
                  info.setPhotoPath("file://" + path);
                }
              }
              photoInfos.clear();
              photoInfos.addAll(resultList);
              if (photoInfos.size() < 4) photoInfos.add(tackImage);
              adapter.notifyDataSetChanged();
            }

            @Override public void onHanlderFailure(int requestCode, String errorMsg) {
              Toast.makeText(AddFootprintActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
            }
          });
    }
    if (isDelete) {
      photoInfos.remove(position);
      if (photoInfos.size() < 4 && photoInfos.size() > 0 && !photoInfos.get(photoInfos.size()-1)
          .getPhotoPath()
          .contains("res:///")) {
        photoInfos.add( tackImage);
      }
      adapter.notifyDataSetChanged();
    }
  }

  private void saveRating(String images, String tagIds, int pId, String content, int score) {
    InterestingDetailPresenter.submitDetailRating(this, images, tagIds, pId, content, score,
        new CommonListener() {
          @Override public void successListener(Object response) {
            DialogUtil.dismissProgessDirectly();
            Toast.makeText(AddFootprintActivity.this, "评价提交成功!", Toast.LENGTH_SHORT).show();
            AddFootprintActivity.this.finish();
          }

          @Override public void errorListener(VolleyError error, String exception, String msg) {
            DialogUtil.dismissProgessDirectly();
            Toast.makeText(AddFootprintActivity.this, "评价提交失败!", Toast.LENGTH_SHORT).show();

          }
        });
  }

  @Override public void onBackPressed() {
    if (dataChange) {
      CommonDialog dialog =
          CommonDialog.newInstance("请先保存", "当前修改尚未保存,退出会导致资料丢失,是否保存?", "继续编辑", "退出");
      dialog.setListener(new CommonDialog.OnActionListener() {
        @Override public boolean dialogClick(int clickId) {
          if (clickId != CommonDialog.CLICK_ID_ACTIVE) {
            finish();
          }
          return true;
        }
      });

      dialog.show(getSupportFragmentManager(), "");
    } else {
      super.onBackPressed();
    }
  }


}

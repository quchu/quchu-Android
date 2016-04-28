package co.quchu.quchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.toolsfinal.StringUtils;
import co.quchu.galleryfinal.GalleryFinal;
import co.quchu.galleryfinal.model.PhotoInfo;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.ConfirmDialogFg;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.ImageUpload;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.photoselected.Bimp;
import co.quchu.quchu.presenter.PostCardPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.view.adapter.FindPositionAdapter;
import co.quchu.quchu.widget.SelectedImagePopWin;

/**
 * Created by Nico on 16/4/12.
 */
public class AddFootprintActivity extends BaseActivity implements FindPositionAdapter.ItemClickListener {
    @Bind(R.id.etContent)
    EditText etContent;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tvPickFromMap)
    TextView tvPickFromMap;
    List<PhotoInfo> photoInfos;


    private FindPositionAdapter adapter;
    private PhotoInfo tackImage;

    public static final String REQUEST_KEY_ID = "id";
    public static final String REQUEST_KEY_NAME = "name";
    public static final String REQUEST_KEY_ENTITY = "entity";
    public static final String REQUEST_KEY_IS_EDIT = "edit";

    private int cId = -1;
    private int pId;
    private String pName;
    private PostCardItemModel mData;
    private int REQUEST_PICKING_QUCHU = 0x0001;


    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_footprint);
        ButterKnife.bind(this);

        pId = getIntent().getIntExtra(REQUEST_KEY_ID, -1);
        pName = getIntent().getStringExtra(REQUEST_KEY_NAME);
        //数据是重新封装过的,如果部分属性丢失请返回前面页面添加
        mData = getIntent().getParcelableExtra(REQUEST_KEY_ENTITY);
        pId = mData == null ? pId : mData.getPlaceId();
        cId = mData == null ? cId : mData.getCardId();
        EnhancedToolbar toolbar = getEnhancedToolbar();
        toolbar.getRightTv().setText(R.string.save);
        toolbar.getRightTv().setTextColor(getResources().getColor(R.color.load_progress_yellow));
        boolean isEdit = getIntent().getBooleanExtra(REQUEST_KEY_IS_EDIT, false);
        if (isEdit) {
            TextView titleView = new TextView(this);
            titleView.setText("删除");
            titleView.setGravity(Gravity.CENTER);
            titleView.setTextColor(Color.WHITE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(toolbar.getItemSize(), toolbar.getItemSize());
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.rightMargin = 130;
            toolbar.addCustomView(titleView, params);

            titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    ConfirmDialogFg confirmDialogFg = ConfirmDialogFg.newInstance(getString(R.string.confirm_delete), getString(R.string.caution_action_cannot_revert));
                    confirmDialogFg.show(getFragmentManager(), "confirm");
                    confirmDialogFg.setActionListener(new ConfirmDialogFg.OnActionListener() {
                        @Override
                        public void onClick(int index) {
                            v.setClickable(false);
                            String uri = String.format(Locale.CHINA, NetApi.delPostCard, cId);

                            GsonRequest<Object> request = new GsonRequest<>(uri, Object.class, new ResponseListener<Object>() {
                                @Override
                                public void onErrorResponse(@Nullable VolleyError error) {
                                    Toast.makeText(AddFootprintActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                    v.setClickable(true);
                                }

                                @Override
                                public void onResponse(Object response, boolean result, String errorCode, @Nullable String msg) {
                                    v.setClickable(true);
                                    if (result) {
                                        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_POST_CARD_DELETED, cId, pId));
                                        Toast.makeText(AddFootprintActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(AddFootprintActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            request.start(AddFootprintActivity.this, null);
                        }
                    });

                }
            });
        }
        if (cId > 0) {
            tvPickFromMap.setVisibility(View.INVISIBLE);
        }

        tvPickFromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFootprintActivity.this, PickingQuchuActivity.class);
                startActivityForResult(intent, REQUEST_PICKING_QUCHU);
            }
        });
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICKING_QUCHU && resultCode == RESULT_OK && null != data) {
            pId = data.getIntExtra(PickingQuchuActivity.BUNDLE_KEY_PICKING_RESULT_ID, -1);
            pName = data.getStringExtra(PickingQuchuActivity.BUNDLE_KEY_PICKING_RESULT_NAME);
            tvPickFromMap.setText("在 " + pName);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    private void init() {
        photoInfos = new ArrayList<>();
        adapter = new FindPositionAdapter();

        etContent.setText(null != mData ? mData.getComment() : "");
        tackImage = new PhotoInfo();

        if (mData == null || mData.getImglist().size() < 4) {
            tackImage.setPhotoPath("res:///" + R.mipmap.ic_take_photo);
            photoInfos.add(tackImage);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));


        if (null != mData) {
            for (int i = 0; i < mData.getImglist().size(); i++) {
                PhotoInfo photoModel = new PhotoInfo();
                photoModel.setHeight(mData.getImglist().get(i).getHeight());
                photoModel.setWidth(mData.getImglist().get(i).getWidth());
                photoModel.setPhotoPath(mData.getImglist().get(i).getPath());
                photoModel.setPhotoId(mData.getImglist().get(i).getImgId());
                photoInfos.add(photoModel);
            }
        }


        adapter.setImages(photoInfos);
        adapter.setListener(this);
        if (null == mData) {
            tvPickFromMap.setText("在 " + pName);
        } else {
            tvPickFromMap.setText("在 " + mData.getPlcaeName());
            pId = mData.getPlaceId();
        }

        recyclerView.setAdapter(adapter);
        getEnhancedToolbar().getRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == etContent.getText() || StringUtils.isBlank(etContent.getText().toString())) {
                    Toast.makeText(AddFootprintActivity.this, "你还没输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<String> im = new ArrayList<String>();
                for (PhotoInfo item : photoInfos) {
                    if (item.getPhotoPath().contains("file://")) {
                        im.add(Uri.parse(item.getPhotoPath()).getPath());
                    } else if (item.getPhotoPath().contains("http://")) {
                        im.add(item.getPhotoPath());
                    }
                }
                if (im.size() > 0) {
                    DialogUtil.showProgess(AddFootprintActivity.this, "上传中");
                    new ImageUpload(AddFootprintActivity.this, im, new ImageUpload.UploadResponseListener() {
                        @Override
                        public void finish(String result) {
                            init();
                            saveCard(pId, etContent.getText().toString(), result);
                        }

                        @Override
                        public void error() {
                            DialogUtil.dismissProgessDirectly();
                            Toast.makeText(AddFootprintActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AddFootprintActivity.this, "请至少上传一张图片", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void itemClick(boolean isDelete, int position, final PhotoInfo photoInfo) {
        if (!isDelete && position == 0 && photoInfo.getPhotoPath().contains("res:///")) {
            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            new SelectedImagePopWin(AddFootprintActivity.this, recyclerView, photoInfos, 4, new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    if (photoInfos.size() + resultList.size() > 4 && photoInfos.size() > 0) {
                        photoInfos.remove(0);
                    }
                    for (PhotoInfo info : resultList) {
                        String path = info.getPhotoPath();
                        info.setPhotoPath("file://" + path);
                    }
                    photoInfos.addAll(resultList);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {
                    Toast.makeText(AddFootprintActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (isDelete) {
            photoInfos.remove(position);
            if (photoInfos.size() < 4 && photoInfos.size() > 0 && !photoInfos.get(0).getPhotoPath().contains("res:///")) {
                photoInfos.add(0, tackImage);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void saveCard(final int pId, String comment, String imgs) {
        PostCardPresenter.sacePostCard(this, pId, 0, comment, imgs, cId, new PostCardPresenter.MyPostCardListener() {
            @Override
            public void onSuccess(PostCardModel model) {
                DialogUtil.dismissProgessDirectly();
                if (Bimp.imglist.size() > 0) {
                    EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_QUCHU_DETAIL_UPDATED, pId));
                    Toast.makeText(AddFootprintActivity.this, "脚印修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_POST_CARD_ADDED, pId));
                    Toast.makeText(AddFootprintActivity.this, "脚印添加成功!", Toast.LENGTH_SHORT).show();
                }

                AddFootprintActivity.this.finish();
            }

            @Override
            public void onError(String error) {
                DialogUtil.dismissProgessDirectly();
            }
        });
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("uploadpic");
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd("uploadpic");
        super.onPause();
    }

}

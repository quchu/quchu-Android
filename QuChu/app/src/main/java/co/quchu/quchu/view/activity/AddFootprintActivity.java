package co.quchu.quchu.view.activity;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.CommonDialog;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.gallery.GalleryFinal;
import co.quchu.quchu.gallery.model.PhotoInfo;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.net.ImageUpload;
import co.quchu.quchu.presenter.PostCardPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.view.adapter.FindPositionAdapter;
import co.quchu.quchu.widget.SelectedImagePopWin;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Nico on 16/4/12.
 */
public class AddFootprintActivity extends BaseBehaviorActivity implements FindPositionAdapter.ItemClickListener {

    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {
        return null;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 106;
    }


    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_add_footprint);
    }
    @Bind(R.id.etContent)
    EditText etContent;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    TextView titleName;
    List<PhotoInfo> photoInfos;


    private FindPositionAdapter adapter;
    private PhotoInfo tackImage;

    public static final String REQUEST_KEY_ID = "id";
    public static final String REQUEST_KEY_NAME = "name";
    public static final String REQUEST_KEY_ENTITY = "entity";
    public static final String REQUEST_KEY_FROM_PAGE_NAME = "";

    private int cId = -1;
    private int pId;
    private String fromPageName;
    private String pName;
    private PostCardItemModel mData;


    private boolean dataChange;

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
        fromPageName = getIntent().getStringExtra(REQUEST_KEY_FROM_PAGE_NAME);


        ArrayMap<String,Object> params = new ArrayMap<>();
        params.put("趣处名称",pName);
        params.put("入口名称",fromPageName);
        ZGEvent(params,"添加脚印");

        //ZGEvent("文章名称",dModel.getName(),"趣处分享");

        //数据是重新封装过的,如果部分属性丢失请返回前面页面添加
        mData = getIntent().getParcelableExtra(REQUEST_KEY_ENTITY);
        pId = mData == null ? pId : mData.getPlaceId();
        cId = mData == null ? cId : mData.getCardId();
        EnhancedToolbar toolbar = getEnhancedToolbar();
        titleName = toolbar.getTitleTv();

        toolbar.getRightTv().setText(R.string.save);

        init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        GalleryFinal.setmCallback(null);
    }


    private void init() {
        photoInfos = new ArrayList<>();
        adapter = new FindPositionAdapter();
        etContent.setText(null != mData ? mData.getComment() : "");
        tackImage = new PhotoInfo();

        tackImage.setPhotoPath("res:///" + R.mipmap.ic_take_photo);


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

        if (photoInfos.size() < 4) {
            photoInfos.add(0, tackImage);
        }
        adapter.setImages(photoInfos);
        adapter.setListener(this);
        if (null == mData) {
            titleName.setText(pName);
        } else {
            titleName.setText(mData.getPlcaeName());
            pId = mData.getPlaceId();
        }

        recyclerView.setAdapter(adapter);
        getEnhancedToolbar().getRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataChange()) {
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
                        new ImageUpload(AddFootprintActivity.this, im, new ImageUpload.UploadResponseListener() {
                            @Override
                            public void finish(String result) {

                                saveCard(pId, etContent.getText().toString(), result);
                            }

                            @Override
                            public void error() {
                                DialogUtil.dismissProgessDirectly();
                                Toast.makeText(AddFootprintActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(AddFootprintActivity.this, "至少留下一张图片才能转身离去", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddFootprintActivity.this, "您未做任何修改", Toast.LENGTH_SHORT).show();
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
                    for (PhotoInfo info : resultList) {
                        String path = info.getPhotoPath();
                        if (!path.startsWith("file://") && !path.startsWith("res:///") && !path.startsWith("http://"))
                            info.setPhotoPath("file://" + path);
                    }
                    photoInfos.clear();
                    photoInfos.addAll(resultList);
                    if (photoInfos.size() < 4)
                        photoInfos.add(0, tackImage);
                    adapter.notifyDataSetChanged();
                    dataChange = true;
                }

                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {
                    Toast.makeText(AddFootprintActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (isDelete) {
            dataChange = true;
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
                EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_POST_CARD_ADDED, pId));
                Toast.makeText(AddFootprintActivity.this, "脚印添加成功!", Toast.LENGTH_SHORT).show();
                AddFootprintActivity.this.finish();
            }

            @Override
            public void onError(String error) {
                DialogUtil.dismissProgessDirectly();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (dataChange()) {
            CommonDialog dialog = CommonDialog.newInstance("请先保存", "当前修改尚未保存,退出会导致资料丢失,是否保存?", "继续编辑", "退出");
            dialog.setListener(new CommonDialog.OnActionListener() {
                @Override
                public boolean dialogClick(int clickId) {
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

    private boolean dataChange() {
        String s = null != mData ? mData.getComment() : "";
        return dataChange || !etContent.getText().toString().trim().equals(s);
    }

}

package co.quchu.quchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.CommonDialog;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.gallery.GalleryFinal;
import co.quchu.quchu.gallery.model.PhotoInfo;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.ImageUpload;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.view.adapter.FindPositionAdapter;
import co.quchu.quchu.widget.SelectedImagePopWin;

public class FindPositionActivity extends BaseActivity implements FindPositionAdapter.ItemClickListener {

    @Bind(R.id.desc)
    EditText name;
    @Bind(R.id.position)
    EditText position;
    @Bind(R.id.detail)
    EditText detail;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    List<PhotoInfo> photoInfos;
    private FindPositionAdapter adapter;
    private PhotoInfo tackImage;

    public static final String REQUEST_KEY_NAME = "name";
    public static final String REQUEST_KEY_DESC = "desc";
    public static final String REQUEST_KEY_ID = "id";
    public static final String REQUEST_KEY_POSITION = "position";
    public static final String REQUEST_KEY_IMAGE_LIST = "imageList";
    private String positionText;
    private String descText;
    private String nameText;
    private int id;
    private TextView rightTv;

    private boolean dataChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_position);
        ButterKnife.bind(this);
        EnhancedToolbar toolbar = getEnhancedToolbar();

        TextView titleTv = toolbar.getTitleTv();
        titleTv.setText("添加新趣处");

        rightTv = toolbar.getRightTv();
        rightTv.setText("保存");
        init();
        restore();
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    /**
     * 修改的时候恢复文件
     */
    private void restore() {
        Intent intent = getIntent();
        id = intent.getIntExtra(REQUEST_KEY_ID, -1);
        ArrayList<PhotoInfo> imageList = intent.getParcelableArrayListExtra(REQUEST_KEY_IMAGE_LIST);
        nameText = intent.getStringExtra(REQUEST_KEY_NAME);
        descText = intent.getStringExtra(REQUEST_KEY_DESC);
        positionText = intent.getStringExtra(REQUEST_KEY_POSITION);

        name.setText(nameText);
        detail.setText(descText);
        position.setText(positionText);
        if (imageList != null) {
            photoInfos.addAll(imageList);
            adapter.notifyDataSetChanged();
        }

    }

    private void init() {
        name.setText("");
        detail.setText("");
        position.setText("");
        photoInfos = new ArrayList<>();
        tackImage = new PhotoInfo();
        tackImage.setPhotoPath("res:///" + R.mipmap.ic_take_photo);
        photoInfos.add(tackImage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new FindPositionAdapter();
        adapter.setImages(photoInfos);
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);

        recyclerView.setAdapter(adapter);
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText = FindPositionActivity.this.name.getText().toString().trim();
                positionText = FindPositionActivity.this.position.getText().toString().trim();
                descText = detail.getText().toString().trim();
                if (TextUtils.isEmpty(nameText)) {
                    Toast.makeText(FindPositionActivity.this, "请填写趣处名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<String> im = new ArrayList<>();
                for (PhotoInfo item : photoInfos) {
                    if (item.getPhotoPath().contains("file://")) {
                        im.add(Uri.parse(item.getPhotoPath()).getPath());
                    } else if (item.getPhotoPath().contains("http://")) {
                        im.add(item.getPhotoPath());
                    }
                }
                if (im.size() > 0) {
                    DialogUtil.showProgess(FindPositionActivity.this, "上传中");
                    new ImageUpload(FindPositionActivity.this, im, new ImageUpload.UploadResponseListener() {
                        @Override
                        public void finish(String result) {
                            sendToServer(nameText, positionText, descText, result);
                        }

                        @Override
                        public void error() {
                            DialogUtil.dismissProgessDirectly();
                            Toast.makeText(FindPositionActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    DialogUtil.showProgess(FindPositionActivity.this, "上传中");
                    sendToServer(nameText, positionText, descText, "");
                }
            }
        });
    }

    private void sendToServer(String name, String position, String desc, String Images) {


        Map<String, String> map = new HashMap<>();
        map.put("place.pimage", Images);
        map.put("place.pId", id == 0 ? "" : String.valueOf(id));
        map.put("place.pname", name);
        map.put("place.paddress", position);
        map.put("place.profile", desc);


        GsonRequest<Object> request = new GsonRequest<>(NetApi.findPosition, Object.class, map, new ResponseListener<Object>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(FindPositionActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                DialogUtil.dismissProgessDirectly();
            }

            @Override
            public void onResponse(Object response, boolean result, @Nullable String exception, @Nullable String msg) {
                if (result) {
                    Toast.makeText(FindPositionActivity.this, "增加趣处成功", Toast.LENGTH_SHORT).show();
                    DialogUtil.dismissProgessDirectly();
                    finish();
                } else {
                    Toast.makeText(FindPositionActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.start(this);
    }

    @Override
    public void itemClick(boolean isDelete, int position, final PhotoInfo photoInfo) {
        if (!isDelete && position == 0 && photoInfo.getPhotoPath().contains("res:///")) {
            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            new SelectedImagePopWin(this, recyclerView, photoInfos, 4, new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    dataChange = true;
                    for (PhotoInfo info : resultList) {
                        String path = info.getPhotoPath();
                        if (!path.startsWith("file://") && !path.startsWith("res:///"))
                            info.setPhotoPath("file://" + path);
                    }
                    photoInfos.clear();
                    photoInfos.addAll(resultList);
                    if (photoInfos.size() < 4)
                        photoInfos.add(0, tackImage);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {
                    Toast.makeText(FindPositionActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (isDelete) {
            dataChange = true;
            photoInfos.remove(position);
            if (photoInfos.size() < 8 && photoInfos.size() > 0 && !photoInfos.get(0).getPhotoPath().contains("res:///")) {
                photoInfos.add(0, tackImage);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("lookfornew");
        super.onResume();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dataChange = true;
            }
        };

        name.addTextChangedListener(textWatcher);
        position.addTextChangedListener(textWatcher);
        detail.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd("lookfornew");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    public void onBackPressed() {
        if (dataChange) {
            CommonDialog dialog = CommonDialog.newInstance("请先保存", "当前修改尚未保存,退出会导致资料丢失,是否保存?", "先保存", "取消");
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
}

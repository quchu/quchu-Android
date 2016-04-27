package co.quchu.quchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.galleryfinal.GalleryFinal;
import co.quchu.galleryfinal.model.PhotoInfo;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.ImageUpload;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.view.adapter.FindPositionAdapter;
import co.quchu.quchu.widget.SelectedImagePopWin;

public class FindPositionActivity extends BaseActivity implements FindPositionAdapter.ItemClickListener {

    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.position)
    EditText position;
    @Bind(R.id.detail)
    EditText detail;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.commit)
    TextView commit;
    List<PhotoInfo> photoInfos;
    @Bind(R.id.close)
    ImageView close;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_position);
        ButterKnife.bind(this);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        tackImage.setPhotoPath("res:///" + R.mipmap.ic_add_photo_image);
        photoInfos.add(tackImage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new FindPositionAdapter();
        adapter.setImages(photoInfos);
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);

        recyclerView.setAdapter(adapter);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText = FindPositionActivity.this.name.getText().toString().trim();
                positionText = FindPositionActivity.this.position.getText().toString().trim();
                descText = detail.getText().toString().trim();
                if (TextUtils.isEmpty(nameText) || TextUtils.isEmpty(positionText)) {
                    Toast.makeText(FindPositionActivity.this, "名称和地址不能为空", Toast.LENGTH_SHORT).show();
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
                    DialogUtil.showProgess(FindPositionActivity.this, "上传中");
                    new ImageUpload(FindPositionActivity.this, im, new ImageUpload.UploadResponseListener() {
                        @Override
                        public void finish(String result) {
                            init();
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
        String url;
        if (id != -1) {
            url = String.format(NetApi.findPosition, String.valueOf(id), name, position, desc);
        } else {
            url = String.format(NetApi.findPosition, "", name, position, desc);
        }

        Map<String, String> map = new HashMap<>();
        map.put("place.pimage", Images);


        GsonRequest<Object> request = new GsonRequest<>(Request.Method.POST, url, map, null, new ResponseListener<Object>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(FindPositionActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                DialogUtil.dismissProgessDirectly();
            }

            @Override
            public void onResponse(Object response, boolean result, @Nullable String exception, @Nullable String msg) {
                Toast.makeText(FindPositionActivity.this, "增加趣处成功", Toast.LENGTH_SHORT).show();
                DialogUtil.dismissProgessDirectly();
                init();
            }
        });
        request.start(this, null);
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
                    Toast.makeText(FindPositionActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (isDelete) {
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
}

package co.quchu.quchu.view.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

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
import co.quchu.quchu.widget.MoreButtonView;
import co.quchu.quchu.widget.SelectedImagePopWin;

public class FindPositionActivity extends BaseActivity implements FindPositionAdapter.ItemClickListener {

    @Bind(R.id.recommend_title_more_rl)
    MoreButtonView recommendTitleMoreRl;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.position)
    EditText position;
    @Bind(R.id.detail)
    EditText detail;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.commit)
    Button commit;
    List<PhotoInfo> photoInfos;
    private FindPositionAdapter adapter;
    private PhotoInfo tackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_position);
        ButterKnife.bind(this);
        ImageView image = (ImageView) recommendTitleMoreRl.findViewById(R.id.widget_title_more_iv);
        image.setImageResource(R.drawable.ic_menus_title_more);

        recommendTitleMoreRl.setMoreClick(new MoreButtonView.MoreClicklistener() {
            @Override
            public void moreClick() {
                finish();
            }
        });
        init();
    }

    private void init() {
        photoInfos = new ArrayList<>();
        tackImage = new PhotoInfo();
        tackImage.setPhotoPath("res:///" + R.drawable.ic_add_photo_image);
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
                final String name = FindPositionActivity.this.name.getText().toString().trim();
                final String position = FindPositionActivity.this.position.getText().toString().trim();
                final String desc = detail.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(position)) {
                    Toast.makeText(FindPositionActivity.this, "名称和地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (photoInfos.size() > 0 && !photoInfos.get(0).getPhotoPath().contains("res:///") || photoInfos.size() > 2) {
                    List<String> im = new ArrayList<String>();
                    for (PhotoInfo item : photoInfos) {
                        if (!item.getPhotoPath().contains("res:///")) {
                            im.add(Uri.parse(item.getPhotoPath()).getPath());
                        }
                    }
                    if (im.size() > 0) {
                        DialogUtil.showProgess(FindPositionActivity.this, "上传中");
                        new ImageUpload(FindPositionActivity.this, im, new ImageUpload.UploadResponseListener() {
                            @Override
                            public void finish(String result) {

                                sendToServer(name, position, desc, result);
                            }

                            @Override
                            public void error() {
                                DialogUtil.dismissProgessDirectly();
                            }
                        });
                    } else {
                        DialogUtil.showProgess(FindPositionActivity.this, "上传中");
//                        Toast.makeText(FindPositionActivity.this, "至少上传一张照片", Toast.LENGTH_SHORT).show();
                        sendToServer(name, position, desc, "");
                    }
                } else {
                    DialogUtil.showProgess(FindPositionActivity.this, "上传中");
                    sendToServer(name, position, desc, "");
//                    Toast.makeText(FindPositionActivity.this, "至少上传一张照片", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void sendToServer(String name, String position, String desc, String Images) {
//        Map<String, String> params = new HashMap<>();
//        params.put("place.pname", name);
//        params.put("Place.paddress", position);
//        params.put("Place.profile", desc);
//        params.put("Place.pimage", "");
//        params.put("Place.pId", "");
        String url = String.format(NetApi.findPosition, name, position, desc, Images);


        GsonRequest<Object> request = new GsonRequest<>(Request.Method.POST, url, null, new ResponseListener<Object>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(FindPositionActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                DialogUtil.dismissProgessDirectly();
            }

            @Override
            public void onResponse(Object response, boolean result, @Nullable String exception, @Nullable String msg) {
                Toast.makeText(FindPositionActivity.this, "增加趣处成功", Toast.LENGTH_SHORT).show();
                DialogUtil.dismissProgessDirectly();
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
            new SelectedImagePopWin(this, recyclerView, photoInfos, new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    if (photoInfos.size() + resultList.size() > 8 && photoInfos.size() > 0) {
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
}

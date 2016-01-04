package co.quchu.quchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherRateModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.photo.Bimp;
import co.quchu.quchu.photo.ImageBucketActivity;
import co.quchu.quchu.presenter.PostCardPresenter;
import co.quchu.quchu.utils.FileUtils;
import co.quchu.quchu.utils.ImageUtils;
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
    TextView addPostcardBottomBtn;
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
    PostCardItemModel defaulModel;
    private boolean isNeedUpdate = false;
    int pId;
    private String editTextDefaultText = "";
    String[] prbHintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_add_postcard);
        ButterKnife.bind(this);
        initTitleBar();
        prbHintText = getResources().getStringArray(R.array.addPostCardSuggetStrings);
        initData();
        setPRBlistener();
        if (!StringUtils.isEmpty(pName)) {
            addPostcardTopTv.setText(String.format("你对%s印象如何?", pName));
            StringUtils.alterTextColor(addPostcardTopTv, 2, 2 + pName.length(), R.color.gene_textcolor_yellow);
        }
        adapter = new AddPostCardGridAdapter(this);
        addPostcardImageIgv.setAdapter(adapter);
        addPostcardImageIgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((Bimp.imglist.size() + Bimp.drr.size()) < 9) {
                    if (position == 0) {
                        new PopupWindows(AddPostCardActivity.this, addPostcardImageIgv);
                    }
                } else {

                }
            }
        });
    }


    private void initData() {
        defaulModel = (PostCardItemModel) getIntent().getSerializableExtra("pCardModel");
        if (defaulModel != null) {
            Bimp.imglist = defaulModel.getImglist();
            addPostcardSuggestPrb.setRating(defaulModel.getScore());
            addPostcardAboutPlaceTv.setText(defaulModel.getComment());
            addPostcardAboutPlaceTv.setText(defaulModel.getComment());
            editTextDefaultText = prbHintText[defaulModel.getScore()];
            addPostcardSuggestTv.setText(prbHintText[defaulModel.getScore()]);
        }
        pName = getIntent().getStringExtra("pName");
        pId = getIntent().getIntExtra("pId", 2);
    }

    private void setPRBlistener() {
        addPostcardSuggestPrb.setListener(new RatingListener() {
            @Override
            public void onRatePicked(ProperRatingBar ratingBar) {
                LogUtils.json("rating==" + ratingBar.getRating());

                addPostcardSuggestTv.setText(prbHintText[ratingBar.getRating()]);
                if (StringUtils.isEmpty(addPostcardAboutPlaceTv.getText().toString()) || editTextDefaultText.equals(addPostcardAboutPlaceTv.getText().toString())) {
                    addPostcardAboutPlaceTv.setText(prbHintText[ratingBar.getRating()]);
                    editTextDefaultText = prbHintText[ratingBar.getRating()];
                }
            }
        });
    }

    private String paths = "";
    private static final int TAKE_PICTURE = 0x000000;


    @OnClick({R.id.add_postcard_bottom_btn})
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.add_postcard_bottom_btn:
                addPostcard2Server();
                break;
        }
    }

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
                LogUtils.json("path==" + paths);
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
            isNeedUpdate = false;
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
                    isNeedUpdate = true;
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

    List<String> list;

    private void addPostcard2Server() {
        list = new ArrayList<String>();
        for (int i = 0; i < Bimp.drr.size(); i++) {
            String Str = Bimp.drr.get(i).substring(
                    Bimp.drr.get(i).lastIndexOf("/") + 1,
                    Bimp.drr.get(i).lastIndexOf("."));
            list.add(FileUtils.SDPATH + Str + ".JPEG");
        }
        // 高清的压缩图片全部就在  list 路径里面了
        // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
        // 完成上传服务器后 .........
        /*FileUtils.deleteDir();*/
        if (addPostcardSuggestPrb.getRating() > 0) {
            DialogUtil.showProgess(this, "数据上传中...");
            if (list.size() > 0) {
                getQiNiuToken();
            } else {

                String imageStr = "";
                if (Bimp.imglist.size() > 0) {
                    for (int j = 0; j < Bimp.imglist.size(); j++) {
                        if (j == 0) {
                            imageStr += Bimp.imglist.get(j).getPath();
                        } else {
                            imageStr += "%7C" + Bimp.imglist.get(j).getPath();
                        }
                    }
                }
                saveCard(imageStr);
            }
        } else {
            Toast.makeText(this, "请给该趣处评个分", Toast.LENGTH_SHORT).show();
        }


    }

    private void saveCard(String imageStr) {
        PostCardPresenter.sacePostCard(this, pId, addPostcardSuggestPrb.getRating(), addPostcardAboutPlaceTv.getText().toString(), imageStr, new PostCardPresenter.MyPostCardListener() {
            @Override
            public void onSuccess(PostCardModel model) {
                //   if (Bimp.delImageIdList.size() == 0) {
                AppContext.gatherDataModel.rateList.add(new GatherRateModel(pId + "", addPostcardSuggestPrb.getRating()));
                DialogUtil.dismissProgessDirectly();
                if (Bimp.imglist.size() > 0) {
                    Toast.makeText(AddPostCardActivity.this, "成功修改了趣处!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddPostCardActivity.this, "成功添加了趣处!", Toast.LENGTH_SHORT).show();
                }
                AddPostCardActivity.this.finish();
              /*  } else {
                    delImageFromServer();
                }*/
            }

            @Override
            public void onError(String error) {
                DialogUtil.dismissProgessDirectly();
            }
        });
    }

    private String qiniuToken = "";
    UploadManager uploadManager;

    private void delImageFromServer() {
        String delStr = "";
        if (Bimp.delImageIdList.size() > 0) {
            for (int i = 0; i < Bimp.delImageIdList.size(); i++) {
                if (i == 0) {
                    delStr = Bimp.delImageIdList.get(i) + "";
                } else {
                    delStr += "%7C" + Bimp.delImageIdList.get(i);
                }
            }
        }
        LogUtils.json("delete==" + String.format(NetApi.delPostCardImage, delStr));
        NetService.get(this, String.format(NetApi.delPostCardImage, delStr), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("delete==" + response);
                DialogUtil.dismissProgessDirectly();
                Toast.makeText(AddPostCardActivity.this, "成个添加了趣处!", Toast.LENGTH_SHORT).show();
                AddPostCardActivity.this.finish();
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgessDirectly();
                return false;
            }
        });
    }

    private void getQiNiuToken() {

        NetService.get(this, NetApi.getQiniuToken, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("qiniu token==" + response);

                if (response != null && response.has("token")) {
                    try {
                        qiniuToken = response.getString("token");
                        uploadManager = new UploadManager();
                        addImage2QiNiu();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

    private int imageIndex = 0;
    private String defaulQiNiuFileName = "%d-%d.JPEG";
    private ArrayList<String> imageUrlInQiNiu = new ArrayList<String>();

    private void addImage2QiNiu() {
        // if (uploadManager == null) {
        //    uploadManager = new UploadManager();
        //  }
        LogUtils.json("addImage2QiNiu  addImage2QiNiu  addImage2QiNiu" + list.get(imageIndex));
        uploadManager.put(ImageUtils.Bitmap2Bytes(Bimp.bmp.get(imageIndex), 100), String.format(defaulQiNiuFileName, AppContext.user.getUserId(), System.currentTimeMillis()), qiniuToken,
                new UpCompletionHandler() {

                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        //  res 包含hash、key等信息，具体字段取决于上传策略的设置。
                        if (null != info) {
                            try {
                                if (info.isOK()) {
                                    String url = response.getString("key");
//                                    LogUtils.E("Uploader Time==end=" + System.currentTimeMillis());
//                                    Log.i("qiniu", "upload sucess. and url: " + url);
                                    LogUtils.json("upload success =info=" + info);
                                    LogUtils.json("upload success =response=" + response);
                                    LogUtils.json("key==" + key);
                                    imageUrlInQiNiu.add(url);
                                    imageIndex++;
                                    if (imageIndex < list.size()) {
                                        addImage2QiNiu();
                                    } else {
                                        File file = new File(FileUtils.SDPATH);
                                        FileUtils.RecursionDeleteFile(file);
                                        String imageStr = "";
                                        if (Bimp.imglist.size() > 0) {
                                            for (int j = 0; j < Bimp.imglist.size(); j++) {
                                                if (j == 0) {
                                                    imageStr += Bimp.imglist.get(j).getPath();
                                                } else {
                                                    imageStr += "%7C" + Bimp.imglist.get(j).getPath();
                                                    //   imageStr += "%7C" + Bimp.imglist.get(j).getPath();
                                                }
                                            }
                                            for (int i = 0; i < imageUrlInQiNiu.size(); i++) {
                                                imageStr += "%7C" + "http://7xo7ey.com1.z0.glb.clouddn.com/" + imageUrlInQiNiu.get(i).toString();
                                            }
                                        } else {
                                            for (int i = 0; i < imageUrlInQiNiu.size(); i++) {
                                                if (i == 0) {
                                                    imageStr += "http://7xo7ey.com1.z0.glb.clouddn.com/" + imageUrlInQiNiu.get(i).toString();
                                                } else {
                                                    //   imageStr += '|' + imageUrlInQiNiu.get(i).toString();
                                                    imageStr += "%7C" + "http://7xo7ey.com1.z0.glb.clouddn.com/" + imageUrlInQiNiu.get(i).toString();
                                                }
                                            }
                                        }
                                        saveCard(imageStr);
                                    }

                                } else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new UploadOptions(null, null, false,
                        new UpProgressHandler() {
                            public void progress(String key, double percent) {
                                //     LogUtils.json("upload success ==" + key + ": " + percent);
                            }
                        }, null));
    }

    private void clearImageSelected() {
        Bimp.bmp = new ArrayList<Bitmap>();
        Bimp.drr = new ArrayList<String>();
        Bimp.delImageIdList = new ArrayList<>();
        Bimp.imglist = new ArrayList<>();
        list = new ArrayList<String>();
    }

    @Override
    protected void onDestroy() {
        LogUtils.json("add Postcard is onDestroy");
        clearImageSelected();
        super.onDestroy();
    }

}

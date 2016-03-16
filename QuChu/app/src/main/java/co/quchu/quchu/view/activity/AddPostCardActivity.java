package co.quchu.quchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.galleryfinal.BuildConfig;
import co.quchu.galleryfinal.CoreConfig;
import co.quchu.galleryfinal.FunctionConfig;
import co.quchu.galleryfinal.GalleryFinal;
import co.quchu.galleryfinal.model.PhotoInfo;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherRateModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.photo.Bimp;
import co.quchu.quchu.photoselected.FrescoImageLoader;
import co.quchu.quchu.presenter.PostCardPresenter;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.FileUtils;
import co.quchu.quchu.utils.ImageUtils;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
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
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    List<String> list;

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
    int pId;
    private String editTextDefaultText = "";
    String[] prbHintText;
    List<PhotoInfo> mPhotoList;

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
        mPhotoList = new ArrayList<PhotoInfo>();
        adapter = new AddPostCardGridAdapter(this, mPhotoList);
        addPostcardImageIgv.setAdapter(adapter);
        addPostcardImageIgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((Bimp.imglist.size() + Bimp.drr.size() + mPhotoList.size()) < 5) {
                    if (position == 0) {
                        new PopupWindows(AddPostCardActivity.this, addPostcardImageIgv);
                    }
                } else {

                }
            }
        });

    }

    private FunctionConfig functionConfig;

    private void initGralley() {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        functionConfigBuilder.setMutiSelectMaxSize(mPhotoList.size() >= 5 ? 5 : 5 - mPhotoList.size());
        functionConfigBuilder.setEnableEdit(false);
        functionConfigBuilder.setEnableCrop(true);
        functionConfigBuilder.setEnablePreview(true);
        functionConfigBuilder.setForceCrop(false);//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
        functionConfigBuilder.setForceCropEdit(true);
        functionConfigBuilder.setFilter(mPhotoList);
        functionConfigBuilder.setRotateReplaceSource(true);
        functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
        functionConfig = functionConfigBuilder.build();
        CoreConfig coreConfig = new CoreConfig.Builder(AddPostCardActivity.this, new FrescoImageLoader(AddPostCardActivity.this), null)
                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(null)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
    }


    private void initData() {
        defaulModel = (PostCardItemModel) getIntent().getSerializableExtra("pCardModel");
        pName = getIntent().getStringExtra("pName");
        pId = getIntent().getIntExtra("pId", 2);

        if (null != defaulModel && pId != -1) {
            fillUI();
        } else {
            getFromSetver(pId);
        }
    }

    private void fillUI() {
        Bimp.imglist = defaulModel.getImglist();
        addPostcardSuggestPrb.setRating(defaulModel.getScore());
        addPostcardAboutPlaceTv.setText(defaulModel.getComment());
        addPostcardAboutPlaceTv.setText(defaulModel.getComment());
        if ((int) (defaulModel.getScore() + 0.5f) > prbHintText.length) {
            editTextDefaultText = prbHintText[prbHintText.length - 1];
            addPostcardSuggestTv.setText(prbHintText[prbHintText.length - 1]);
        } else {
            editTextDefaultText = prbHintText[(int) (defaulModel.getScore() + 0.5f)];
            addPostcardSuggestTv.setText(prbHintText[(int) (defaulModel.getScore() + 0.5f)]);
        }
    }

    private void getFromSetver(int pid) {
        if (-1 == pId) {
            return;
        }
        DialogUtil.showProgess(this, R.string.loading_dialog_text);
        PostCardPresenter.getPostCardByPid(this, pid, new PostCardPresenter.MyPostCardItemListener() {
            @Override
            public void onSuccess(PostCardItemModel model) {
                if (null != model) {
                    defaulModel = model;
                    fillUI();
                }
                DialogUtil.dismissProgess();
            }

            @Override
            public void onError(String error) {
                if (null != error) {
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }

                DialogUtil.dismissProgess();
            }
        });
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
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.add_postcard_bottom_btn:
                addPostcard2Server();
                break;
        }
    }

    public void photo() {
  /*      Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File src = new File(FileUtils.SDPATH);
        if (!src.exists()) {
            src.mkdirs();
        }
        File file = new File(src, String.valueOf(System.currentTimeMillis())
                + ".jpg");
        paths = file.getPath();
        Uri imageUri = Uri.fromFile(file);

        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);*/
        initGralley();
        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
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
        //  adapter.update();
        LogUtils.json("on restart");
        super.onRestart();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AddPostCardActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AddPostCardActivity");
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
                 /*   Intent intent = new Intent(AddPostCardActivity.this,
                            ImageBucketActivity.class);
                    startActivity(intent);
                    isNeedUpdate = true;*/
                    initGralley();
                    GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
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
            if (StringUtils.containsEmoji(addPostcardAboutPlaceTv.getText().toString())) {
                Toast.makeText(this, getResources().getString(R.string.content_has_emoji), Toast.LENGTH_SHORT).show();
            } else {
                DialogUtil.showProgess(this, "数据上传中...");
                if (mPhotoList.size() > 0) {
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
                EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_QUCHU_DETAIL_UPDATED, pId));
                AppContext.gatherList.add(new GatherRateModel(pId + "", addPostcardSuggestPrb.getRating()));
                DialogUtil.dismissProgessDirectly();
                if (Bimp.imglist.size() > 0) {
                    Toast.makeText(AddPostCardActivity.this, "成功修改了趣处!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddPostCardActivity.this, "成功添加了趣处!", Toast.LENGTH_SHORT).show();
                }
                SPUtils.putBooleanToSPMap(AddPostCardActivity.this, AppKey.IS_POSTCARD_LIST_NEED_REFRESH, true);
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
    private Bitmap uploadBitmap = null;

    private void addImage2QiNiu() {
        // if (uploadManager == null) {
        //    uploadManager = new UploadManager();
        //  }
        //   LogUtils.json("addImage2QiNiu  addImage2QiNiu  addImage2QiNiu" + list.get(imageIndex));
        if (imageIndex >= mPhotoList.size()) {

            DialogUtil.dismissProgessDirectly();
            return;
        }
        uploadBitmap = ImageUtils.getimage(mPhotoList.get(imageIndex).getPhotoPath());
        if (uploadBitmap != null)
            uploadManager.put(ImageUtils.Bitmap2Bytes(uploadBitmap, 90), String.format(defaulQiNiuFileName,
                    AppContext.user.getUserId(), System.currentTimeMillis()), qiniuToken,
                    //    uploadManager.put(ImageUtils.Bitmap2Bytes(Bimp.bmp.get(imageIndex), 100), String.format(defaulQiNiuFileName, AppContext.user.getUserId(), System.currentTimeMillis()), qiniuToken,
                    new UpCompletionHandler() {

                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            //  res 包含hash、key等信息，具体字段取决于上传策略的设置。
                            if (null != info) {
                                try {
                                    if (info.isOK()) {
                                        if (uploadBitmap != null && !uploadBitmap.isRecycled()) {
                                            uploadBitmap.recycle();
                                            uploadBitmap = null;
                                        }
                                        String url = response.getString("key");
//                                    LogUtils.E("Uploader Time==end=" + System.currentTimeMillis());
//                                    Log.i("qiniu", "upload sucess. and url: " + url);
                                        LogUtils.json("upload success =info=" + info);
                                        LogUtils.json("upload success =response=" + response);
                                        LogUtils.json("key==" + key);
                                        imageUrlInQiNiu.add(url);
                                        imageIndex++;
                                        if (imageIndex < mPhotoList.size()) {
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
        //   GalleryFinal.cleanCacheFile();
        Bimp.bmp = new ArrayList<Bitmap>();
        Bimp.drr = new ArrayList<String>();
        Bimp.max = 0;
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

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                mPhotoList.addAll(resultList);
                //   mPhotoList = resultList;
                adapter.notifyDataSetChanged();
                //   mChoosePhotoListAdapter.notifyDataSetChanged();
              /*  for (int i = 0; i < mPhotoList.size(); i++) {
                    PhotoInfo infos = mPhotoList.get(i);
                    LogUtils.json(infos.getPhotoPath() + "//id=" + infos.getPhotoId() + "//width=" + infos.getWidth() + "//height=" + infos.getHeight());
                }*/
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(AddPostCardActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };
}

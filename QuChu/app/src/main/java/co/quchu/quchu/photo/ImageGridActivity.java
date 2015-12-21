package co.quchu.quchu.photo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.utils.LogUtils;

public class ImageGridActivity extends BaseActivity {
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    public static final String EXTRA_IMAGE_NAME = "imageName";

    List<ImageItem> dataList;
    GridView gridView;
    ImageGridAdapter adapter;
    AlbumHelper helper;
    TextView bt;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ImageGridActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_grid);
        initTitleBar();
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        ((TextView) findViewById(R.id.title_content_tv)).setText(getIntent().getStringExtra(EXTRA_IMAGE_NAME));
        dataList = (List<ImageItem>) getIntent().getSerializableExtra(
                EXTRA_IMAGE_LIST);

        bt = (TextView) findViewById(R.id.bt);
        initView();
        bt.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<String>();
                Collection<String> c = adapter.map.values();
                Iterator<String> it = c.iterator();
                for (; it.hasNext(); ) {
                    list.add(it.next());
                }

				/*if (Bimp.act_bool) {
                    Intent intent = new Intent(ImageGridActivity.this,
							PublishedActivity.class);
					startActivity(intent);
					Bimp.act_bool = false;
				}*/
                for (int i = 0; i < list.size(); i++) {
                    if (Bimp.drr.size() < 9) {
                        try {
                            Bimp.bmp.add(Bimp.revitionImageSize(list.get(i)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bimp.drr.add(list.get(i));
                    }
                }
                LogUtils.json("drr.size==" + Bimp.drr.size() + "max=" + Bimp.max);
                finish();
            }

        });
    }

    /**
     * 鍒濆鍖杤iew瑙嗗浘
     */
    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
                mHandler);
        gridView.setAdapter(adapter);
        if (Bimp.drr.size() + Bimp.imglist.size() > 0) {
            bt.setText("完成" + "(" + (Bimp.drr.size() + Bimp.imglist.size()) + ")");
        }
        adapter.setTextCallback(new ImageGridAdapter.TextCallback() {
            public void onListen(int count) {
                if (count == 0) {
                    bt.setText("完成");
                } else {
                    bt.setText("完成" + "(" +  (Bimp.drr.size() + Bimp.imglist.size()+count) + ")");
                }
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                adapter.notifyDataSetChanged();
            }

        });

    }
}

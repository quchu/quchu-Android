package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.galleryfinal.GalleryFinal;
import co.quchu.galleryfinal.model.PhotoInfo;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
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

    }


    @Override
    public void itemClick(boolean isDelete, int position, final PhotoInfo photoInfo) {
        if (!isDelete && position == 0 && photoInfo.getPhotoPath().contains("res:///")) {

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

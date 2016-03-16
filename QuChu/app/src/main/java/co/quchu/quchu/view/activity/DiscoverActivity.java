package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.galleryfinal.model.PhotoInfo;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.DiscoverModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.DiscoverAdapter;

/**
 * DiscoverActivity
 * User: Chenhs
 * Date: 2015-11-04
 * 我的发现
 */
public class DiscoverActivity extends BaseActivity implements DiscoverAdapter.OnItenClickListener {

    @Bind(R.id.title_content_tv)
    TextView titleContentTv;

    @Bind(R.id.atmosphere_rv)
    RecyclerView atmosphereRv;
    @Bind(R.id.atmosphere_empty_view_fl)
    FrameLayout atmosphereEmptyViewFl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmosphere);
        ButterKnife.bind(this);
        initTitleBar();
        titleContentTv.setText(getTitle());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        atmosphereRv.setLayoutManager(mLayoutManager);
        initDiscoverData(1);

    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("DiscoverActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("DiscoverActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initDiscoverData(int pageNo) {
        NetService.get(this, String.format(NetApi.getProposalPlaceList, pageNo), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("initDiscoverData==" + response);
                try {
                    if (response != null && response.has("result") && !StringUtils.isEmpty(response.getString("result")) && !"null".equals(response.getString("result"))) {
                        Gson gson = new Gson();
                        DiscoverModel model = gson.fromJson(response.toString(), DiscoverModel.class);
                        if (model != null && model.getResult().size() > 0) {
                            DiscoverAdapter adapter = new DiscoverAdapter(DiscoverActivity.this, model.getResult());
                            adapter.setListener(DiscoverActivity.this);
                            atmosphereRv.setAdapter(adapter);
                            atmosphereEmptyViewFl.setVisibility(View.GONE);
                            atmosphereRv.setVisibility(View.VISIBLE);
                        } else {
                            atmosphereEmptyViewFl.setVisibility(View.VISIBLE);
                            atmosphereRv.setVisibility(View.GONE);
                        }
                    } else {
                        atmosphereEmptyViewFl.setVisibility(View.VISIBLE);
                        atmosphereRv.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

    @OnClick(R.id.action_buttton)
    public void flickrClick(View view) {
        this.finish();
    }

    @Override
    public void itemClick(int position, DiscoverModel.ResultEntity entity) {
        String address = entity.getAddress();
        String name = entity.getName();
        int pId = entity.getPId();
        String desc = entity.getInstruction();
        ArrayList<PhotoInfo> photos = new ArrayList<>();
        List<DiscoverModel.ResultEntity.ImageEntity> image = entity.getImage();
        for (DiscoverModel.ResultEntity.ImageEntity item : image) {
            String path = item.getImgpath();
            PhotoInfo info = new PhotoInfo();
            info.setPhotoPath(path);
             photos.add(info);
        }
        Intent intent = new Intent(this, FindPositionActivity.class);
        intent.putExtra(FindPositionActivity.REQUEST_KEY_NAME, name);
        intent.putExtra(FindPositionActivity.REQUEST_KEY_ID, pId);
        intent.putExtra(FindPositionActivity.REQUEST_KEY_POSITION, address);
        intent.putExtra(FindPositionActivity.REQUEST_KEY_DESC, desc);
        intent.putParcelableArrayListExtra(FindPositionActivity.REQUEST_KEY_IMAGE_LIST, photos);
        startActivity(intent);
    }
}

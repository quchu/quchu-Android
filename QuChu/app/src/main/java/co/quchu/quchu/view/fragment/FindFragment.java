package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.galleryfinal.model.PhotoInfo;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.FindBean;
import co.quchu.quchu.presenter.IFindFragment;
import co.quchu.quchu.presenter.QuchuPresenter;
import co.quchu.quchu.view.activity.FindPositionActivity;
import co.quchu.quchu.view.adapter.FindAdapter;
import co.quchu.quchu.widget.ErrorView;

/**
 * Created by no21 on 2016/4/6.
 * email:437943145@qq.com
 * desc :发现
 */
public class FindFragment extends BaseFragment implements IFindFragment, FindAdapter.OnItenClickListener {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.errorView)
    ErrorView errorView;
    private QuchuPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        presenter = new QuchuPresenter(getActivity());
        errorView.showLoading();
        presenter.getFindData(1, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void showData(boolean isError, FindBean bean) {

        if (isError) {
            errorView.showViewDefault(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.getFindData(1, FindFragment.this);
                }
            });
            recyclerView.setVisibility(View.GONE);
        } else {
            FindAdapter adapter = new FindAdapter(bean.getResult());
            adapter.setListener(this);
            errorView.himeView();
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter);
        }


    }

    @Override
    public void itemClick(FindBean.ResultEntity entity) {
        String address = entity.getAddress();
        String name = entity.getName();
        int pId = entity.getPId();
        String desc = entity.getInstruction();
        ArrayList<PhotoInfo> photos = new ArrayList<>();
        List<FindBean.ResultEntity.ImageEntity> image = entity.getImage();
        for (FindBean.ResultEntity.ImageEntity item : image) {
            String path = item.getImgpath();
            PhotoInfo info = new PhotoInfo();
            info.setPhotoPath(path);
            photos.add(info);
        }
        Intent intent = new Intent(getActivity(), FindPositionActivity.class);
        intent.putExtra(FindPositionActivity.REQUEST_KEY_NAME, name);
        intent.putExtra(FindPositionActivity.REQUEST_KEY_ID, pId);
        intent.putExtra(FindPositionActivity.REQUEST_KEY_POSITION, address);
        intent.putExtra(FindPositionActivity.REQUEST_KEY_DESC, desc);
        intent.putParcelableArrayListExtra(FindPositionActivity.REQUEST_KEY_IMAGE_LIST, photos);
        startActivity(intent);
    }
}

package co.quchu.quchu.view.fragment;

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
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.ImageModel;
import co.quchu.quchu.view.adapter.QuchuDetailsAdapter;

/**
 * Created by mwb on 16/11/18.
 */
public class QuchuDetailFragment extends BaseFragment {

  public static final String BUNDLE_KEY_DETAIL_MODEL = "BUNDLE_KEY_DETAIL_MODEL";

  @Bind(R.id.detail_recycler_view) RecyclerView mRecyclerView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_quchu_details, container, false);
    ButterKnife.bind(this, view);

    Bundle bundle = getArguments();

    if (bundle == null) {
      return view;
    }

    DetailModel detailModel = (DetailModel) bundle.getSerializable(BUNDLE_KEY_DETAIL_MODEL);

    List<ImageModel> galleryImgs = new ArrayList<>();
    if (null != detailModel && null != detailModel.getImglist() && detailModel.getImglist().size() > 0) {
      for (int i = 0; i < detailModel.getImglist().size(); i++) {
        galleryImgs.add(detailModel.getImglist().get(i).convert2ImageModel());
      }
    }

    QuchuDetailsAdapter quchuDetailAdapter = new QuchuDetailsAdapter(getActivity(), detailModel);
    quchuDetailAdapter.updateGallery(galleryImgs);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mRecyclerView.setAdapter(quchuDetailAdapter);

    return view;
  }

  @Override
  protected String getPageNameCN() {
    return "趣处详情";
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}

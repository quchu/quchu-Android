package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.ClassifyModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.RecommendActivity;
import co.quchu.quchu.view.adapter.ClassifyAdapter;
import co.quchu.quchu.view.adapter.ClassifyDecoration;
import co.quchu.quchu.widget.ErrorView;

/**
 * ClassifyFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 分类
 */
public class ClassifyFragment extends BaseFragment {
    @Bind(R.id.fragment_firends_rv)
    RecyclerView recyclerView;
    @Bind(R.id.errorView)
    ErrorView errorView;
    private ClassifyAdapter cAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_rv_view, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new ClassifyDecoration(getActivity()));
        getRootTagsData();
        return view;
    }

    /**
     * 获取分类信息
     */
    public void getRootTagsData() {
        GsonRequest<List<ClassifyModel>> request = new GsonRequest<>(NetApi.getRootTags, new TypeToken<List<ClassifyModel>>() {
        }.getType(), new ResponseListener<List<ClassifyModel>>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                recyclerView.setVisibility(View.GONE);
                DialogUtil.dismissProgessDirectly();
                errorView.showViewDefault(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.showProgess(getActivity(), "加载中");
                        getRootTagsData();
                    }
                });
            }

            @Override
            public void onResponse(final List<ClassifyModel> response, boolean result, @Nullable String exception, @Nullable String msg) {
                DialogUtil.dismissProgessDirectly();
                recyclerView.setVisibility(View.VISIBLE);
                errorView.himeView();


                cAdapter = new ClassifyAdapter(getActivity(), response);
                recyclerView.setAdapter(cAdapter);
                cAdapter.setOnItemCliskListener(new ClassifyAdapter.ClasifyClickListener() {
                    @Override
                    public void cItemClick(View view, int position) {
                        if (response.get(position).isIsSend()) {
                            SPUtils.putValueToSPMap(getActivity(), AppKey.USERSELECTEDCLASSIFY, response.get(position).getEn());
                            SPUtils.putUserSelectedClassify(response.get(position).getEn());
                            ((RecommendActivity) getActivity()).selectedClassify();
                        }
                    }
                });
            }
        });
        request.start(getContext(), null);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

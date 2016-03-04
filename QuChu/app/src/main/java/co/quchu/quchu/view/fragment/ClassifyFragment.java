package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.ClassifyModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.RecommendActivity;
import co.quchu.quchu.view.adapter.ClassifyAdapter;
import co.quchu.quchu.view.adapter.ClassifyDecoration;

/**
 * ClassifyFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 分类
 */
public class ClassifyFragment extends BaseFragment {
    @Bind(R.id.fragment_firends_rv)
    RecyclerView fragmentFirendsRv;
    private ClassifyAdapter cAdapter;
    private ArrayList<ClassifyModel> cList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_rv_view, container,false);
        ButterKnife.bind(this, view);
        getRootTagsData();
        return view;
    }

    /**
     * 获取分类信息
     */
    public void getRootTagsData() {
        NetService.get(getActivity(), NetApi.getRootTags, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response.toString());
                if (response.has("data")) {
                    try {
                        JSONArray datas = response.getJSONArray("data");
                        if (datas.length() > 0) {
                            cList = new ArrayList<>();
                            Gson gson = new Gson();
                            for (int i = 0; i < datas.length(); i++) {
                                ClassifyModel classifyModel = gson.fromJson(datas.getString(i), ClassifyModel.class);
                                cList.add(classifyModel);
                                LogUtils.json(datas.getString(i));
                            }
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            fragmentFirendsRv.setLayoutManager(mLayoutManager);
                            fragmentFirendsRv.addItemDecoration(new ClassifyDecoration(getActivity()));

                            cAdapter = new ClassifyAdapter(getActivity(), cList);
                            fragmentFirendsRv.setAdapter(cAdapter);
                            cAdapter.setOnItemCliskListener(new ClassifyAdapter.ClasifyClickListener() {
                                @Override
                                public void cItemClick(View view, int position) {
                                    if (cList.get(position).isIsSend()) {
                                        SPUtils.putValueToSPMap(getActivity(), AppKey.USERSELECTEDCLASSIFY, cList.get(position).getEn());
                                        SPUtils.putUserSelectedClassify(cList.get(position).getEn());
                                        ((RecommendActivity) getActivity()).selectedClassify();
                                    }
                                }
                            });
                        }
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



    public void hintClassify() {
        if (fragmentFirendsRv != null)
            fragmentFirendsRv.setVisibility(View.GONE);
    }

    public void showClassify() {
        if (fragmentFirendsRv != null)
            fragmentFirendsRv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

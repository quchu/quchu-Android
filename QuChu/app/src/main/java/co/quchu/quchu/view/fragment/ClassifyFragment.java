package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import co.quchu.quchu.view.activity.WebViewActivity;
import co.quchu.quchu.view.adapter.ClassifyAdapter;
import co.quchu.quchu.widget.DampView;
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
    @Bind(R.id.dampView)
    DampView dampView;
    private ClassifyAdapter cAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new ClassifyDecoration(getActivity()));
        getRootTagsData();
        return view;
    }

    /**
     * 获取分类信息
     */
    public void getRootTagsData() {

        String uri = String.format(Locale.CHINA, NetApi.getRootTags, SPUtils.getCityId());
        GsonRequest<List<ClassifyModel>> request = new GsonRequest<>(uri, new TypeToken<List<ClassifyModel>>() {
        }.getType(), new ResponseListener<List<ClassifyModel>>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                dampView.setVisibility(View.GONE);
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
                dampView.setVisibility(View.VISIBLE);
                errorView.hideView();


                cAdapter = new ClassifyAdapter(getActivity(), response);
                recyclerView.setAdapter(cAdapter);
                cAdapter.setOnItemCliskListener(new ClassifyAdapter.ClasifyClickListener() {
                    @Override
                    public void cItemClick(View view, int position) {
                        ClassifyModel model = response.get(position);
                        if (model.isIsSend()) {
                            SPUtils.putValueToSPMap(getActivity(), AppKey.USERSELECTEDCLASSIFY, model.getEn());
                            String title;
                            switch (model.getEn()) {
                                case "creative":
                                    title = "灵感之源";
                                    break;
                                case "luxury":
                                    title = "轻奢格调";
                                    break;
                                case "discover":
                                    title = "探索世界";
                                    break;
                                case "social":
                                    title = "有朋自远方来";
                                    break;
                                case "local":
                                    title = "闲来无事";
                                    break;
                                case "culture":
                                    title = "学而不倦";
                                    break;
                                default:
                                    title = model.getZh();
                            }
                            Map<String, String> p = new HashMap<>();
                            p.put("classify_name", title);
                            MobclickAgent.onEvent(getActivity(), "subject_u", p);


                            //http://sit.quchu.co/app-main-service/searchSpecial/getSearchSpecialListByTagId?tagId=283&cityId=1

                            String url = String.format(NetApi.quchu_topic_h5,model.getTagId(),SPUtils.getCityId());
                            Intent intent = new Intent(getActivity(), WebViewActivity.class);
                            intent.putExtra(WebViewActivity.BUNDLE_KEY_WEBVIEW_URL,url);
                            //intent.putExtra(ClassifyDetailActivity.PARAMETER_TITLE, title);
                            getActivity().startActivity(intent);
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

    @Override
    public void onResume() {
        MobclickAgent.onPageStart("h_discovery");
        super.onResume();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd("h_discovery");
        super.onPause();
    }
}

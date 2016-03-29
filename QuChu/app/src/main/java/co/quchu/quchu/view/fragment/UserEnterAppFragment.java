package co.quchu.quchu.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.activity.UserLoginActivity;
import co.quchu.quchu.view.adapter.GeneProgressAdapter;
import co.quchu.quchu.widget.CircleWaveView;

/**
 * UserEnterAppFragment
 * User: Chenhs
 * Date: 2015-11-30
 */
public class UserEnterAppFragment extends BaseFragment {

    @Bind(R.id.user_enter_app_circle_iv)
    SimpleDraweeView userEnterAppCircleIv;
    @Bind(R.id.user_enter_app_cwv)
    CircleWaveView userEnterAppCwv;
    @Bind(R.id.gene_progress_gv)
    GridView geneProgressGv;
    /* @Bind(R.id.user_enter_app_btn)
     Button userEnterAppBtn;*/
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_enter_app, null);
        ButterKnife.bind(this, view);

        if (null != AppContext.user) {
            userEnterAppCircleIv.setImageURI(Uri.parse(AppContext.user.getPhoto()));
            LogUtils.json(AppContext.user.getPhoto() + "      imageurl");
        }
        adapter = new GeneProgressAdapter(getActivity(), GeneProgressAdapter.GENE, null);
        geneProgressGv.setAdapter(adapter);
        setGeneData();
        return view;
    }

    private GeneProgressAdapter adapter;

/*    @OnClick(R.id.user_enter_app_btn)
    public void enterApp(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        ((UserLoginActivity) getActivity()).enterApp();
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void setGeneData() {
        DialogUtil.showProgess(getActivity(), R.string.loading_dialog_text);
        NetService.get(getActivity(), NetApi.getUserGene, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null) {
                    Gson gson = new Gson();
                    MyGeneModel model = gson.fromJson(response.toString(), MyGeneModel.class);
                    if (model != null) {
                        adapter.updateGeneData(model.getGenes());
                        DialogUtil.dismissProgess();
                    }
                    enterHandler.sendMessageDelayed(enterHandler.obtainMessage(1), 1500);
                }
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgess();
                enterHandler.sendMessageDelayed(enterHandler.obtainMessage(1), 1500);
                return false;
            }
        });
    }

    Handler enterHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ((UserLoginActivity) getActivity()).enterApp();
        }
    };
}

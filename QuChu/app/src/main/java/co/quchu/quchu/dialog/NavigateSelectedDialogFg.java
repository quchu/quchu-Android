package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.utils.AppUtil;

/**
 * LocationSelectedDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 * 城市选择弹窗
 */
public class NavigateSelectedDialogFg extends DialogFragment {

    @Bind(R.id.navigate_gd_tv)
    TextView navigateGdTv;
    @Bind(R.id.navigate_bd_tv)
    TextView navigateBdTv;
    @Bind(R.id.navigate_tx_tv)
    TextView navigateTxTv;
    private ArrayList<CityModel> cityList;
    NavigateClickListener listener;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @return well instantiated fragment.
     * Serializable cityList
     */
    public static NavigateSelectedDialogFg newInstance() {
        NavigateSelectedDialogFg fragment = new NavigateSelectedDialogFg();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_navigate_selected, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        if (!AppUtil.isAppInstall(getActivity(),"com.autonavi.minimap")){
            navigateGdTv.setVisibility(View.GONE);
        }
        if(!AppUtil.isAppInstall(getActivity(),"com.tencent.map")){
            navigateTxTv.setVisibility(View.GONE);
        }
        if(!AppUtil.isAppInstall(getActivity(),"com.baidu.BaiduMap")){
            navigateBdTv.setVisibility(View.GONE);
        }
        return builder.create();
    }


    public void setNavigateClickListener(NavigateClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.navigate_gd_tv, R.id.navigate_bd_tv, R.id.navigate_tx_tv})
    public void onClicke(View view) {
        if (null != listener) {
            switch (view.getId()) {
                case R.id.navigate_gd_tv:
                    listener.choiceGd();
                    break;
                case R.id.navigate_bd_tv:
                    listener.choiceBd();
                    break;
                case R.id.navigate_tx_tv:
                    listener.choiceTx();
                    break;
            }
            dismiss();
        }
    }

    public interface NavigateClickListener {
        void choiceGd();

        void choiceBd();

        void choiceTx();
    }
}

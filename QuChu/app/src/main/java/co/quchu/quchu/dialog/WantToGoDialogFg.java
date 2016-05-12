package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.blurdialogfragment.BlurDialogFragment;
import co.quchu.quchu.dialog.adapter.LocationSelectedAdapter;
import co.quchu.quchu.utils.LogUtils;

/**
 * LocationSelectedDialogFg
 * User: Chenhs
 * Date: 2015-12-23
 * “想去”弹窗
 */
public class WantToGoDialogFg extends BlurDialogFragment {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String CITY_LIST_MODEL = "city_list_model";
    @Bind(R.id.dialog_wanttogo_collect)
    TextView dialogWanttogoCollect;
    @Bind(R.id.dialog_wanttogo_reserve)
    TextView dialogWanttogoReserve;


    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @return well instantiated fragment.
     * Serializable cityList
     */
    public static WantToGoDialogFg newInstance() {
        WantToGoDialogFg fragment = new WantToGoDialogFg();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private LocationSelectedAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_wanttogo, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onResume() {
        LogUtils.json("dialog resume");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.json("dialog onPause");
        super.onPause();
    }

    public void show(FragmentManager manager, String tag, Wan2GoClickListener listener) {
        super.show(manager, tag);
        this.listener = listener;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            WantToGoDialogFg.this.dismiss();
        }
    };

    @Override
    protected boolean isDebugEnable() {
        return false;
    }

    @Override
    protected boolean isDimmingEnable() {
        return true;
    }

    @Override
    protected boolean isActionBarBlurred() {
        return true;
    }

    @Override
    protected float getDownScaleFactor() {
        return 3.8f;
    }

    @Override
    protected int getBlurRadius() {
        return 8;
    }

    private int selectedIndex = 0;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private Wan2GoClickListener listener;

    public interface Wan2GoClickListener {
        void collectClick();

        void reserveClick();
    }

    @OnClick({R.id.dialog_wanttogo_collect, R.id.dialog_wanttogo_reserve})
    public void dialogClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_wanttogo_collect:
                listener.collectClick();
                dismiss();
                break;
            case R.id.dialog_wanttogo_reserve:
                listener.reserveClick();
                dismiss();
                break;
        }
    }


}

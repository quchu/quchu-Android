package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.blurdialogfragment.BlurDialogFragment;
import co.quchu.quchu.dialog.adapter.TagsFilterDialogAdapter;
import co.quchu.quchu.model.TagsModel;

/**
 * Created by Nico on 16/4/11.
 */

public class TagsFilterDialog extends BlurDialogFragment {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String BUNDLE_KEY_TAGS = "BUNDLE_KEY_TAGS";
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.rvTags)
    RecyclerView rvTags;
    @Bind(R.id.ivFinish)
    ImageView ivFinish;

    @Bind(R.id.cbSelectAll)
    CheckBox cbSelectAll;


    private ArrayList<TagsModel> mDataset;


    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @param list
     * @return well instantiated fragment.
     * Serializable cityList
     */
    public static TagsFilterDialog newInstance(ArrayList<TagsModel> list) {
        TagsFilterDialog fragment = new TagsFilterDialog();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_KEY_TAGS, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        mDataset = (ArrayList<TagsModel>) args.getSerializable(BUNDLE_KEY_TAGS);
    }


    private TagsFilterDialogAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_tags_filter, null);
        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(view);

        initSelected();
        rvTags.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapter = new TagsFilterDialogAdapter(mDataset,  new TagsFilterDialogAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(int index) {
                mDataset.get(index).setPraise(!mDataset.get(index).isPraise());
                adapter.notifyDataSetChanged();
            }
        });
        rvTags.setAdapter(adapter);
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mListener){
                    mListener.onFinishPicking(mDataset,cbSelectAll.isChecked());
                }
                dismiss();
            }
        });

        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                for (int i = 0; i < mDataset.size(); i++) {
                    mDataset.get(i).setPraise(isChecked);
                }
                adapter.notifyDataSetChanged();
            }
        });
        return dialog;
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
        if (adapter != null && rvTags != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void initSelected() {

    }


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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    private OnFinishPickingListener mListener;
    public interface OnFinishPickingListener{
        void onFinishPicking(List<TagsModel> selection,boolean selectAll);
    }

    public void setPickingListener(OnFinishPickingListener pListener){
        mListener = pListener;
    }
}


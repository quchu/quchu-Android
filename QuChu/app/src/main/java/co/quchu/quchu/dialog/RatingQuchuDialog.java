package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.blurdialogfragment.BlurDialogFragment;
import co.quchu.quchu.dialog.adapter.RatingQuchuDialogAdapter;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * Created by Nico on 16/4/11.
 */

public class RatingQuchuDialog extends BlurDialogFragment {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String BUNDLE_KEY_TAGS = "BUNDLE_KEY_TAGS";
    private static final String BUNDLE_KEY_TAGS_RATING = "BUNDLE_KEY_TAGS_RATING";
    @Bind(R.id.tvTips)
    TextView tvTips;
    @Bind(R.id.prbRating)
    ProperRatingBar prbRating;
    @Bind(R.id.tvTagsLabel)
    TextView tvTagsLabel;
    @Bind(R.id.rvTags)
    RecyclerView rvTags;
    @Bind(R.id.ivFinish)
    ImageView ivFinish;


    private List<TagsModel> mDataset;
    private float mRating;


    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @param list
     * @return well instantiated fragment.
     * Serializable cityList
     */
    public static RatingQuchuDialog newInstance(int rating,ArrayList<TagsModel> list) {
        RatingQuchuDialog fragment = new RatingQuchuDialog();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_KEY_TAGS, list);
        args.putFloat(BUNDLE_KEY_TAGS_RATING,rating);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        mDataset = (ArrayList<TagsModel>) args.getSerializable(BUNDLE_KEY_TAGS);
        mRating = args.getFloat(BUNDLE_KEY_TAGS_RATING);
    }


    private RatingQuchuDialogAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_rating_quchu, null);
        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(view);

        initSelected();
        rvTags.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapter = new RatingQuchuDialogAdapter(mDataset, new RatingQuchuDialogAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(int index) {
                mDataset.get(index).setPraise(!mDataset.get(index).isPraise());
                adapter.notifyDataSetChanged();
            }
        });
        rvTags.setAdapter(adapter);
        prbRating.setRating(mRating);
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
}


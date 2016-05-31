package co.quchu.quchu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.dialog.adapter.RatingQuchuDialogAdapter;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * Created by Nico on 16/4/11.
 */

public class RatingQuchuDialog extends DialogFragment {
    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String BUNDLE_KEY_TAGS = "BUNDLE_KEY_TAGS";
    private static final String BUNDLE_KEY_TAGS_RATING = "BUNDLE_KEY_TAGS_RATING";
    private static final String BUNDLE_KEY_TAGS_USERCOUNT = "BUNDLE_KEY_TAGS_USERCOUNT";
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
    private RatingQuchuDialogAdapter adapter;
    public static final int MAX_TAG_SELECT = 3;
    private int mUserCount = 0;


    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @param list
     * @return well instantiated fragment.
     * Serializable cityList
     */
    public static RatingQuchuDialog newInstance(int userCount, int rating, ArrayList<TagsModel> list) {
        RatingQuchuDialog fragment = new RatingQuchuDialog();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_KEY_TAGS, list);
        args.putFloat(BUNDLE_KEY_TAGS_RATING, rating);
        args.putInt(BUNDLE_KEY_TAGS_USERCOUNT, userCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        mDataset = (ArrayList<TagsModel>) args.getSerializable(BUNDLE_KEY_TAGS);
        mRating = args.getFloat(BUNDLE_KEY_TAGS_RATING);
        mUserCount = args.getInt(BUNDLE_KEY_TAGS_USERCOUNT);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_rating_quchu, null);
        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(view);

        View imageView = view.findViewById(R.id.ivClose);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        rvTags.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapter = new RatingQuchuDialogAdapter(mDataset, new RatingQuchuDialogAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(int index, boolean select) {
                int selected = 0;
                for (int i = 0; i < mDataset.size(); i++) {
                    if (mDataset.get(i).isPraise()) {
                        selected++;
                    }
                }
                if (!select && selected >= MAX_TAG_SELECT) {
                    Toast.makeText(getActivity(), "最多只能选择3个标签", Toast.LENGTH_SHORT).show();
                } else {
                    mDataset.get(index).setPraise(!mDataset.get(index).isPraise());
                    adapter.notifyDataSetChanged();
                }
            }
        });
        rvTags.setAdapter(adapter);
        prbRating.setRating(mRating);
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onFinishPicking(mDataset, prbRating.getRating());
                }
                dismiss();
            }
        });
        tvTips.setText("你的建议会帮助" + mUserCount + "位趣星人");
        return dialog;
    }

    public interface OnFinishPickingListener {
        void onFinishPicking(List<TagsModel> selection, int score);
    }

    public void setPickingListener(OnFinishPickingListener pListener) {
        mListener = pListener;
    }

    private OnFinishPickingListener mListener;

    @Override
    public void onResume() {
        MobclickAgent.onPageStart("evaluate");
        MobclickAgent.onResume(prbRating.getContext());
        super.onResume();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd("evaluate");
        MobclickAgent.onPause(prbRating.getContext());
        super.onPause();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        if (adapter != null && rvTags != null) {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}


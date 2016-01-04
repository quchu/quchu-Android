package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.view.activity.PlanetActivity;

/**
 * UserEnterAppFragment
 * User: Chenhs
 * Date: 2015-11-30
 */
public class UserGuideFragment extends Fragment {

    @Bind(R.id.userguide_start_button)
    TextView userguideStartButton;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_guide, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.userguide_start_button)
    public void startGuide(View view) {
        getActivity().startActivity(new Intent(getActivity(), PlanetActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

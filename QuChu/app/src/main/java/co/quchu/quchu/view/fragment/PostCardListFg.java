package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.presenter.PostCardPresenter;
import co.quchu.quchu.widget.cardsui.MyCard;
import co.quchu.quchu.widget.cardsui.objects.CardStack;
import co.quchu.quchu.widget.cardsui.views.CardUI;

/**
 * FriendsFollowerFg
 * User: Chenhs
 * Date: 2015-11-09
 */
public class PostCardListFg extends Fragment {
    View view;

    @Bind(R.id.postcard_cardsui)
    CardUI postcardCardsui;
    private MyCard.PostCardItemClickListener listener;

    public PostCardListFg(MyCard.PostCardItemClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_postcard_listview, null);
        ButterKnife.bind(this, view);
        initPostCardData();
        return view;
    }

    public void setInvisiable(boolean isInvisiable) {
        if (isInvisiable) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initPostCardData() {
        PostCardPresenter.GetPostCardList(getActivity(), new PostCardPresenter.MyPostCardListener() {
            @Override
            public void onSuccess(PostCardModel model) {
                if (model != null) {


                    CardStack stack = new CardStack(getActivity());
                    MyCard card = null;
                    for (int i = 0; i < 8; i++) {
                        model.getResult().get(0).setCardId(i);
                        model.getResult().get(0).setAddress(i + "");
                        card = new MyCard(model.getResult().get(0), listener);
                        stack.add(card);


                    }

                    postcardCardsui.addStack(stack);

                    // draw cards
                    postcardCardsui.refresh();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}

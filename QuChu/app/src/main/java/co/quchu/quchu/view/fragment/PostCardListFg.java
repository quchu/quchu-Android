package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.PostCardPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.widget.cardsui.MyCard;
import co.quchu.quchu.widget.cardsui.objects.CardStack;
import co.quchu.quchu.widget.cardsui.views.CardUI;

/**
 * FriendsFollowerFg
 * User: Chenhs
 * Date: 2015-11-09
 * 获取我的明信片
 */
public class PostCardListFg extends BaseFragment {
    View view;

    private boolean isFavoritePostCard = false;
    @Bind(R.id.postcard_cardsui)
    CardUI postcardCardsui;
    private MyCard.PostCardItemClickListener listener;
    private PostCardModel pModel;
    private CardStack stack;

    public PostCardListFg(MyCard.PostCardItemClickListener listener, boolean isFavoritePostCard) {
        this.listener = listener;
        this.isFavoritePostCard = isFavoritePostCard;

    }

    public PostCardListFg() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_postcard_listview, null);
        ButterKnife.bind(this, view);
        if (pModel != null)
            dataBinding();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPostCardData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void initPostCardData() {

        PostCardPresenter.GetPostCardList(getActivity(), isFavoritePostCard, new PostCardPresenter.MyPostCardListener() {
            @Override
            public void onSuccess(PostCardModel model) {

                if (model != null) {
                    pModel = model;

                    dataBinding();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }


    private void dataBinding() {

        if (null != postcardCardsui) {

            postcardCardsui.clearCards();
            postcardCardsui.refresh();
            stack = new CardStack(getActivity());
            MyCard card;
            for (int i = 0; i < pModel.getResult().size(); i++) {
                boolean isLast = (i == pModel.getResult().size() - 1);
                card = new MyCard(pModel.getResult().get(i), listener, getActivity(), !isLast);
                stack.add(card);
            }
            postcardCardsui.addStack(stack);
            // draw cards
            postcardCardsui.refresh();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {
        if (event.getFlag() == EventFlags.EVENT_POST_CARD_DELETED) {
            initPostCardData();
        }
    }

}

package co.quchu.quchu.widget.cardsui.objects;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.widget.cardsui.StackAdapter;
import co.quchu.quchu.widget.cardsui.Utils;

@SuppressLint("ShowToast")
public class CardStack extends AbstractCard {
    private static final float _12F = 12f;
    private static final float _45F = 48f;
    private static final String NINE_OLD_TRANSLATION_Y = "translationY";
    private ArrayList<Card> cards;

    private StackAdapter mAdapter;
    private int mPosition;
    private Context mContext;
    private CardStack mStack;

    protected HashMap<String /* id */, Card> mInternalObjects;


    /**
     * Undo Controller
     */


    /**
     * Class to define undobar ui elements
     */


    public CardStack(Context mContext) {
        this.mContext = mContext;
        cards = new ArrayList<Card>();
        mStack = this;
    }

    public CardStack(String title) {
        cards = new ArrayList<Card>();
        mStack = this;

        setTitle(title);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void add(Card newCard) {
        cards.add(newCard);

    }

    public void add(Card newCard, int position) {
        cards.add(position, newCard);
    }

    @Override
    public View getView(Context context) {
        return getView(context, null, false);
    }

    @Override
    public View getView(Context context, boolean swipable) {
        return getView(context, null, swipable);
    }
private int clickIndex=-1;
    RelativeLayout container;
    public View getView(Context context, View convertView, boolean swipable) {

        mContext = context;

        // try to recycle views if possible
        if (convertView != null) {
            Log.d("CardStack", String.format("Checking types.  convertView is %d, need %d", convertView.getId(), R.id.stackRoot));
            // can only convert something with the correct root element
            if (convertView.getId() == R.id.stackRoot)
                if (convert(convertView))
                    return convertView;
        }

        final View view = LayoutInflater.from(context).inflate(
                R.layout.item_stack, null);

        assert view != null;

        container = (RelativeLayout) view
               .findViewById(R.id.stackContainer);

        final int cardsArraySize = cards.size();
        final int lastCardPosition = cardsArraySize - 1;

        Card card;
        View cardView;
        for (int i = 0; i < cardsArraySize; i++) {
            card = cards.get(i);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            int topPx = 8;

            // handle the view

            cardView = card.getView(context);


            // handle the listener
          /*  if (i == lastCardPosition) {
                cardView.setOnClickListener(card.getClickListener());
            }*/
         //   cardView.setOnClickListener(getClickListener(this, container, i));
           /* if (listener != null){
                clickIndex=i;
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onStackItemClick(v, clickIndex);
                    }
                });

            }*/
              /*  cardView.setOnClickListener();*/


            if (i > 0) {
                float dp = (_45F * i) - _12F + 16;
                topPx = Utils.convertDpToPixelInt(context, dp);
            }
            lp.setMargins(0, topPx, 0, Utils.convertDpToPixelInt(context,16));
            cardView.setLayoutParams(lp);
            container.addView(cardView);
        }
        return view;
    }

    private StackItemClickListener listener;

    public void setStackItemClick(StackItemClickListener listener) {
        this.listener = listener;
    }

    public interface StackItemClickListener {
        void onStackItemClick(View v, int position);
    }

    /**
     * Attempt to modify the convertView instead of inflating a new View for this CardStack.
     * If convertView isn't compatible, it isn't modified.
     *
     * @param convertView view to try reusing
     * @return true on success, false if the convertView is not compatible
     */
    private boolean convert(View convertView) {
        // only convert singleton stacks
        if (cards.size() != 1) {
            Log.d("CardStack", "Can't convert view: num cards is " + cards.size());
            return false;
        }

        RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.stackContainer);
        if (container == null) {
            Log.d("CardStack", "Can't convert view: can't find stackContainer");
            return false;
        }

        if (container.getChildCount() != 1) {
            Log.d("CardStack", "Can't convert view: child count is " + container.getChildCount());
            return false;
        }

        // check to see if they're compatible Card types
        Card card = cards.get(0);
        View convertCardView = container.getChildAt(0);

        if (convertCardView == null || convertCardView.getId() != card.getId()) {
            Log.d("CardStack", String.format("Can't convert view: child Id is 0x%x, card Id is 0x%x", convertCardView != null ? convertCardView.getId() : 0, card.getId()));
            return false;
        }

        return card.convert(convertCardView);
    }

    public Card remove(int index) {
        return cards.remove(index);
    }

    public Card get(int i) {
        return cards.get(i);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected float convertDpToPixel(float dp) {
        return Utils.convertDpToPixel(mContext, dp);
    }


    public void setAdapter(StackAdapter stackAdapter) {
        mAdapter = stackAdapter;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }
    private View.OnClickListener getClickListener(final CardStack cardStack,
                                             final RelativeLayout container, final int index) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // init views array
            /*    View[] views = new View[container.getChildCount()];

                for (int i = 0; i < views.length; i++) {
                    views[i] = container.getChildAt(i);
                }

                int last = views.length - 1;*/
                LogUtils.json("index="+index);
            }

        };
    }
}
package co.quchu.quchu.widget.cardsui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import co.quchu.quchu.widget.cardsui.objects.AbstractCard;
import co.quchu.quchu.widget.cardsui.objects.CardStack;

public class StackAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<AbstractCard> mStacks;
    private boolean mSwipeable;

    public StackAdapter(Context context, ArrayList<AbstractCard> stacks
                       ) {
        mContext = context;
        mStacks = stacks;
        mSwipeable = false;

    }

    @Override
    public int getCount() {
        return mStacks.size();
    }

    @Override
    public CardStack getItem(int position) {
        return (CardStack) mStacks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CardStack stack = getItem(position);
        //Enable undo controller!
     //   stack.setEnableUndo(true);
        stack.setAdapter(this);
        stack.setPosition(position);

        // the CardStack can decide whether to use convertView or not
        Log.e("Card","Position="+position);
        convertView = stack.getView(mContext, convertView, mSwipeable);
        return convertView;
    }

    public void setItems(ArrayList<AbstractCard> stacks) {
        mStacks = stacks;
        notifyDataSetChanged();
    }

    public void setSwipeable(boolean b) {
        mSwipeable = b;
    }

    public void setItems(CardStack cardStack, int position) {
        mStacks.set(position, cardStack);
    }

}
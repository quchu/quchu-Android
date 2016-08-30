package co.quchu.quchu.view.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import co.quchu.quchu.R;

/**
 * Created by Nico on 16/8/30.
 */
public class DialogMatchingUsers extends DialogFragment {

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    return inflater.inflate(R.layout.dialog_match_genes,container,false);
  }
}

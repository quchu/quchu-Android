package co.quchu.quchu.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import co.quchu.quchu.R;

/**
 * Created by mwb on 16/10/26.
 */
public class UserGenesDialog extends Dialog {

  public UserGenesDialog(Context context) {
    super(context, R.style.loading_dialog);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_user_genes);
  }
}
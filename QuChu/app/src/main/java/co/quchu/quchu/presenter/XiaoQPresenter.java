package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import co.quchu.quchu.model.XiaoQModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.ResponseListener;

/**
 * Created by mwb on 16/9/20.
 */
public class XiaoQPresenter {

  private final Context mContext;

  public XiaoQPresenter(Context context) {
    mContext = context;
  }

  public void getMessage(final int pageNo, final PageLoadListener<List<XiaoQModel>> listener) {
    GsonRequest<List<XiaoQModel>> request = new GsonRequest<List<XiaoQModel>>("", new TypeToken<List<XiaoQModel>>() {
    }.getType(), new ResponseListener<List<XiaoQModel>>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        if (listener != null) {
          listener.netError(pageNo, "");
        }
      }

      @Override
      public void onResponse(List<XiaoQModel> response, boolean result, String errorCode, @Nullable String msg) {
        if (listener == null) {
          return;
        }

        if (response == null) {
          listener.nullData();
        } else if (pageNo == 1) {
          listener.initData(response);
        } else {
          listener.moreData(response);
        }
      }
    });
    request.start(mContext);
  }
}

package co.quchu.quchu.refactor.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Retrofit Callback
 * <p>
 * Created by mwb on 2016/11/28.
 */
public abstract class BaseCallback<T> implements Callback<T> {

  public abstract void onSuccess(T data);

  public abstract void onFailure(int code, String msg);

  public abstract void onThrowable(Throwable t);

  public abstract void onFinish();

  @Override
  public void onResponse(Call<T> call, Response<T> response) {
    if (response != null) {
      if (response.isSuccessful()) {
        onSuccess(response.body());
      } else {
        onFailure(response.code(), response.errorBody().toString());
      }

    } else {
      onFailure(-1, "请求失败");
    }

    onFinish();
  }

  @Override
  public void onFailure(Call<T> call, Throwable t) {
    onThrowable(t);
    onFinish();
  }
}

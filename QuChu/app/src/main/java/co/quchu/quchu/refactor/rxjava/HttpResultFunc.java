package co.quchu.quchu.refactor.rxjava;

import co.quchu.quchu.refactor.entity.BaseResponse;
import co.quchu.quchu.refactor.exception.ServerException;
import rx.functions.Func1;

/**
 * 拦截服务器响应结果(Json)异常
 * <p>
 * Created by mwb on 16/9/25.
 */
public class HttpResultFunc<T> implements Func1<BaseResponse<T>, T> {

  @Override
  public T call(BaseResponse<T> response) {
    String msg = response.getMsg();
    String exception = response.getException();
    if (!response.isResult()) {
      throw new ServerException(msg, exception);
    }
    return response.getData();
  }
}

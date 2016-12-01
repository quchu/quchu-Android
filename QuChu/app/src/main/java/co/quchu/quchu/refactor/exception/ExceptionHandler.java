package co.quchu.quchu.refactor.exception;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 异常处理
 * <p>
 * Created by mwb on 16/9/25.
 */
public class ExceptionHandler {

  private static String TAG = "ExceptionHandler";

  public static ResponseException handle(Throwable e) {
    ResponseException ex = null;
    if (e instanceof SocketTimeoutException) {
      Log.e(TAG, "SocketTimeoutException");

      ex = new ResponseException(e, "网络出错,请检查网络");

    } else if (e instanceof ConnectException || e.getCause() instanceof ConnectException
        || e instanceof UnknownHostException || e.getCause() instanceof UnknownHostException) {
      Log.e(TAG, "ConnectException or UnknownHostException");

      ex = new ResponseException(e, "网络出错,请检查网络");

    } else if (e instanceof HttpException) {
      Log.e(TAG, "HttpException");

      ex = new ResponseException(e, "网络出错,请检查网络");

    } else if (e instanceof ServerException) {
      ServerException serverException = (ServerException) e;

      Log.e(TAG, "serverException");

      String msg = serverException.getMsg();
      String exception = serverException.getException();
      ex = new ResponseException(serverException, msg, exception);

    } else if (e instanceof JsonParseException
        || e instanceof JSONException
        || e instanceof ParseException) {
      Log.e(TAG, "JsonParseException or JSONException or ParseException");

      ex = new ResponseException(e, "解析错误");

    } else if (e instanceof SSLHandshakeException) {
      Log.e(TAG, "SSLHandshakeException");

      ex = new ResponseException(e, "证书验证错误");

    } else {
      Log.e(TAG, "UnknownException");

      ex = new ResponseException(e, "未知错误");
    }
    return ex;
  }
}

package co.quchu.quchu.refactor.exception;

/**
 * Created by mwb on 16/9/27.
 */
public class ServerException extends RuntimeException {

  private String mMsg;
  private String mException;

  public ServerException(String msg, String exception) {
    mMsg = msg;
    mException = exception;
  }

  public ServerException(String detailMessage) {
    super(detailMessage);
  }

  public String getMsg() {
    return mMsg;
  }

  public String getException() {
    return mException;
  }
}

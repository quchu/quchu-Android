package co.quchu.quchu.refactor.exception;

/**
 * Created by mwb on 16/9/27.
 */
public class ResponseException extends RuntimeException {

  public String mMessage;
  private String mException;

  public ResponseException(Throwable throwable, String message) {
    super(throwable);
    mMessage = message;
  }

  public ResponseException(Throwable throwable, String message, String exception) {
    super(throwable);
    mMessage = message;
    mException = exception;
  }

  @Override
  public String getMessage() {
    return mMessage;
  }

  public String getException() {
    return mException;
  }
}

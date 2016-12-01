package co.quchu.quchu.refactor.exception;

/**
 * Created by mwb on 16/9/27.
 */
public class ApiException extends RuntimeException {

  public ApiException(String msg, String exception) {
  }

  public ApiException(String detailMessage) {
    super(detailMessage);
  }
}

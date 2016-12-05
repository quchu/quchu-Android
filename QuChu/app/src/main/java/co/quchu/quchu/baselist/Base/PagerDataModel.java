package co.quchu.quchu.baselist.Base;

import java.util.List;

/**
 * Created by Nico on 16/11/29.
 */

public class PagerDataModel<E> {

  private DataBean<E> data;
  private String errorCode;
  private String exception;
  private String msg;
  private boolean result;

  public DataBean getData() {
    return data;
  }

  public void setData(DataBean data) {
    this.data = data;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getException() {
    return exception;
  }

  public void setException(String exception) {
    this.exception = exception;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public class DataBean<E> {
    private int pageCount;
    private int pageSize;
    private int pagesNo;
    private int resultCount;
    private int rowCount;
    private int rowEnd;
    private int rowStart;

    private List<E> result;

    public int getPageCount() {
      return pageCount;
    }

    public void setPageCount(int pageCount) {
      this.pageCount = pageCount;
    }

    public int getPageSize() {
      return pageSize;
    }

    public void setPageSize(int pageSize) {
      this.pageSize = pageSize;
    }

    public int getPagesNo() {
      return pagesNo;
    }

    public void setPagesNo(int pagesNo) {
      this.pagesNo = pagesNo;
    }

    public int getResultCount() {
      return resultCount;
    }

    public void setResultCount(int resultCount) {
      this.resultCount = resultCount;
    }

    public int getRowCount() {
      return rowCount;
    }

    public void setRowCount(int rowCount) {
      this.rowCount = rowCount;
    }

    public int getRowEnd() {
      return rowEnd;
    }

    public void setRowEnd(int rowEnd) {
      this.rowEnd = rowEnd;
    }

    public int getRowStart() {
      return rowStart;
    }

    public void setRowStart(int rowStart) {
      this.rowStart = rowStart;
    }

    public List<E> getResult() {
      return result;
    }

    public void setResult(List<E> result) {
      this.result = result;
    }


  }
}


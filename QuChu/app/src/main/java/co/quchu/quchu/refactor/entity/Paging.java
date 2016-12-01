package co.quchu.quchu.refactor.entity;

/**
 * Created by mwb on 2016/11/30.
 */
public class Paging {

  private int pageCount;//总分页数
  private int pageSize;//每一页大小
  private int pagesNo;//当前分页
  private int resultCount;//总数据集
  private int rowCount;//当前分页数据集
  private int rowEnd;//当前分页结束行
  private int rowStart;//当前分页开始行

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
}

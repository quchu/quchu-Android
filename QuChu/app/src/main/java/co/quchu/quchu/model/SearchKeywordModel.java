package co.quchu.quchu.model;

/**
 * Created by Nico on 16/10/19.
 */

public class SearchKeywordModel {
  private int id;
  private String keyword;
  private long timestamp;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override public String toString() {
    return "SearchKeywordModel{" +
        "id=" + id +
        ", keyword='" + keyword + '\'' +
        ", timestamp=" + timestamp +
        '}';
  }
}

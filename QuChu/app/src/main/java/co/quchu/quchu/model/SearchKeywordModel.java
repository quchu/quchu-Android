package co.quchu.quchu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nico on 16/10/19.
 */
public class SearchKeywordModel implements Parcelable {

  private int id;
  private String keyword;
  private long timestamp;

  public SearchKeywordModel() {

  }

  protected SearchKeywordModel(Parcel in) {
    id = in.readInt();
    keyword = in.readString();
    timestamp = in.readLong();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(keyword);
    dest.writeLong(timestamp);
  }

  public static final Creator<SearchKeywordModel> CREATOR = new Creator<SearchKeywordModel>() {
    @Override
    public SearchKeywordModel createFromParcel(Parcel in) {
      return new SearchKeywordModel(in);
    }

    @Override
    public SearchKeywordModel[] newArray(int size) {
      return new SearchKeywordModel[size];
    }
  };

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

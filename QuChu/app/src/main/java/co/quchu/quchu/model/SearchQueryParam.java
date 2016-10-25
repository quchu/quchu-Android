package co.quchu.quchu.model;

/**
 * 搜索的请求参数
 *
 * Created by mwb on 16/10/25.
 */
public class SearchQueryParam {

  //地区
  private String areaId;

  //分类
  private String tagId;

  //商圈
  private String circleId;

  //关键字
  private String value;

  //排序
  private String sortType;

  public String getAreaId() {
    return areaId;
  }

  public void setAreaId(String areaId) {
    this.areaId = areaId;
  }

  public String getTagId() {
    return tagId;
  }

  public void setTagId(String tagId) {
    this.tagId = tagId;
  }

  public String getCircleId() {
    return circleId;
  }

  public void setCircleId(String circleId) {
    this.circleId = circleId;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getSortType() {
    return sortType;
  }

  public void setSortType(String sortType) {
    this.sortType = sortType;
  }
}

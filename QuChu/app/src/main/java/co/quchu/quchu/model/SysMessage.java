package co.quchu.quchu.model;

/**
 * Created by mwb on 16/9/20.
 */
public class SysMessage {

  //0-趣处详情；1-用户；2-文章详情
  public static final String TYPE_QUCHU_DETAIL = "0";
  public static final String TYPE_USER = "1";
  public static final String TYPE_ARTICLE_DETAIL = "2";

  /**
   * id : 180
   * name : 那些隐匿在西子湖畔的文艺咖啡馆
   * image : http://7vzrp0.com5.z0.glb.clouddn.com/xizihupan-max?imageMogr2/thumbnail/800x/format/webp
   * type : 2
   */

  private String id;
  private String name;
  private String image;
  private String type;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}

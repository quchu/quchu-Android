package co.quchu.quchu.model;

/**
 * Created by Nico on 16/9/24.
 */
public class QuchuDetailArticleModel {

  /**
   * id : 1098
   * userName : 测试作者
   * userPhoto : http://7xo7f0.com1.z0.glb.clouddn.com/app-default-avatar-24
   * title : 测试标题
   * url : http://mp.weixin.qq.com/s?src=3&timestamp=1474526525&ver=1&signature=tu7Z7dFoN0UJrPIBvihajHjA3KplU2q3bbJ-GHCeDYP-sBO3IoVu3D9qhBrjzSanlVocPja2BYS--*umHpARIOL9A16cEvJX70sxQ4t1Dg*NqZ7CwyhVWHpoLXimiSK1umWTG-tMhBbQl8ltthOY6JbgJC2kSHlm5G7wwl5FO8U=
   */

  private int id;
  private String userName;
  private String userPhoto;
  private String title;
  private String url;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPhoto() {
    return userPhoto;
  }

  public void setUserPhoto(String userPhoto) {
    this.userPhoto = userPhoto;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}

package co.quchu.quchu.model;

/**
 * Created by mwb on 16/10/24.
 */
public class ArticleKeyword {

  /**
   * netArticleId : 1
   * placeId : 12
   * keyword : 你真帅
   * netArticleUrl : http://mp.weixin.qq.com/s?src=3&timestamp=1474526525&ver=1&signature=tu7Z7dFoN0UJrPIBvihajHjA3KplU2q3bbJ-GHCeDYP-sBO3IoVu3D9qhBrjzSanlVocPja2BYS--*umHpARIOL9A16cEvJX70sxQ4t1Dg*NqZ7CwyhVWHpoLXimiSK1umWTG-tMhBbQl8ltthOY6JbgJC2kSHlm5G7wwl5FO8U==
   */

  private int netArticleId;
  private int placeId;
  private String keyword;
  private String netArticleUrl;

  public int getNetArticleId() {
    return netArticleId;
  }

  public void setNetArticleId(int netArticleId) {
    this.netArticleId = netArticleId;
  }

  public int getPlaceId() {
    return placeId;
  }

  public void setPlaceId(int placeId) {
    this.placeId = placeId;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getNetArticleUrl() {
    return netArticleUrl;
  }

  public void setNetArticleUrl(String netArticleUrl) {
    this.netArticleUrl = netArticleUrl;
  }
}

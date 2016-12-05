package co.quchu.quchu.baselist.Sample;

import java.util.List;

/**
 * Created by Nico on 16/11/30.
 */

public class CommentsModel {



  private int prId;
  private int score;
  private String createDate;
  private String userName;
  private String userPhoneUrl;
  private String sourceUrl;
  private String content;
  private String pqUrl;
  private String sourceContent;


  private List<ImageListBean> imageList;

  public int getPrId() {
    return prId;
  }

  public void setPrId(int prId) {
    this.prId = prId;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPhoneUrl() {
    return userPhoneUrl;
  }

  public void setUserPhoneUrl(String userPhoneUrl) {
    this.userPhoneUrl = userPhoneUrl;
  }

  public String getSourceUrl() {
    return sourceUrl;
  }

  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getPqUrl() {
    return pqUrl;
  }

  public void setPqUrl(String pqUrl) {
    this.pqUrl = pqUrl;
  }

  public String getSourceContent() {
    return sourceContent;
  }

  public void setSourceContent(String sourceContent) {
    this.sourceContent = sourceContent;
  }

  public List<ImageListBean> getImageList() {
    return imageList;
  }

  public void setImageList(List<ImageListBean> imageList) {
    this.imageList = imageList;
  }

  public static class ImageListBean {
    private int width;
    private int height;
    private String pathStr;

    public int getWidth() {
      return width;
    }

    public void setWidth(int width) {
      this.width = width;
    }

    public int getHeight() {
      return height;
    }

    public void setHeight(int height) {
      this.height = height;
    }

    public String getPathStr() {
      return pathStr;
    }

    public void setPathStr(String pathStr) {
      this.pathStr = pathStr;
    }
  }
}

package co.quchu.quchu.model;

import java.util.List;

/**
 * MessageModel
 * User: Chenhs
 * Date: 2016-01-11
 */
public class MessageModel {

  /**
   * 12:趣处ID 13:场景ID 14:文章ID 15:活动 ID 16:城市ID 17：全文本提示
   */
  public static final String TARGET_TYPE_QUCHU = "12";
  public static final String TARGET_TYPE_SCENE = "13";
  public static final String TARGET_TYPE_ARTICLE = "14";
  public static final String TARGET_TYPE_ACTIVITY = "15";
  public static final String TARGET_TYPE_CITY = "16";
  public static final String TARGET_TYPE_TEXT = "17";

  /**
   * pageCount : 1
   * pageSize : 10
   * pagesNo : 1
   * result : [{"come":"praise","content":"点赞了你","form":"631981","formId":1901,"formPhoto":"http://7xodsq.com1.z0.glb.clouddn.com/app-default-avatar-44","interaction":false,"targetId":"862","targetImageUrl":"http://7xodsq.com1.z0.glb.clouddn.com/1914-1464140237456.JPEG?imageMogr2/thumbnail/800x/format/webp","targetType":"11","time":"2016-05-25 09:37:08","type":"praise"}]
   * resultCount : 1
   * rowCount : 1
   * rowEnd : 1
   * rowStart : 0
   */

  private int pageCount;
  private int pageSize;
  private int pagesNo;
  private int resultCount;
  private int rowCount;
  private int rowEnd;
  private int rowStart;
  /**
   * come : praise
   * content : 点赞了你
   * form : 631981
   * formId : 1901
   * formPhoto : http://7xodsq.com1.z0.glb.clouddn.com/app-default-avatar-44
   * interaction : false
   * targetId : 862
   * targetImageUrl : http://7xodsq.com1.z0.glb.clouddn.com/1914-1464140237456.JPEG?imageMogr2/thumbnail/800x/format/webp
   * targetType : 11
   * time : 2016-05-25 09:37:08
   * type : praise
   */

  private List<ResultBean> result;

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

  public List<ResultBean> getResult() {
    return result;
  }

  public void setResult(List<ResultBean> result) {
    this.result = result;
  }

  public static class ResultBean {
    private String come;
    private String title = "趣处通知";
    private String content;
    private String form;
    private int formId;
    private String formPhoto;
    private boolean interaction;
    private int targetId;
    private String url;
    private String targetImageUrl;
    private String targetType;
    private String time;
    private String type;
    private int height;
    private int width;

    public String getUrl() {
      return url;
    }

    public void setUrl(String targetUrl) {
      this.url = url;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getCome() {
      return come;
    }

    public void setCome(String come) {
      this.come = come;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public String getForm() {
      return form;
    }

    public void setForm(String form) {
      this.form = form;
    }

    public int getFormId() {
      return formId;
    }

    public void setFormId(int formId) {
      this.formId = formId;
    }

    public String getFormPhoto() {
      return formPhoto;
    }

    public void setFormPhoto(String formPhoto) {
      this.formPhoto = formPhoto;
    }

    public boolean isInteraction() {
      return interaction;
    }

    public void setInteraction(boolean interaction) {
      this.interaction = interaction;
    }

    public int getTargetId() {
      return targetId;
    }

    public void setTargetId(int targetId) {
      this.targetId = targetId;
    }

    public String getTargetImageUrl() {
      return targetImageUrl;
    }

    public void setTargetImageUrl(String targetImageUrl) {
      this.targetImageUrl = targetImageUrl;
    }

    public String getTargetType() {
      return targetType;
    }

    public void setTargetType(String targetType) {
      this.targetType = targetType;
    }

    public String getTime() {
      return time;
    }

    public void setTime(String time) {
      this.time = time;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public int getHeight() {
      return height;
    }

    public int getWidth() {
      return width;
    }
  }
}

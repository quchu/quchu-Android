package co.quchu.quchu.model;

import java.io.Serializable;

/**
 * Created by Nico on 16/8/26.
 */
public class CommentImageModel implements Serializable {

  /**
   * width : 686
   * height : 426
   * pathStr : http://7xo7f0.com1.z0.glb.clouddn.com/7@3?imageMogr2/thumbnail/800x/format/webp
   */

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

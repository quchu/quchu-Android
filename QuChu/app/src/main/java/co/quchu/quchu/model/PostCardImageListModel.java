package co.quchu.quchu.model;

import java.io.Serializable;

/**
 * PostCardImageListModel
 * User: Chenhs
 * Date: 2015-12-30
 */
public class PostCardImageListModel implements Serializable {

    private int height;
    private int imgId;
    private boolean isCover;
    private boolean isf;
    private String path;
    private String rgb;
    private int width;

    public boolean isf() {
        return isf;
    }

    public void setIsf(boolean isf) {
        this.isf = isf;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public void setIsCover(boolean isCover) {
        this.isCover = isCover;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getImgId() {
        return imgId;
    }

    public boolean isIsCover() {
        return isCover;
    }

    public String getPath() {
        return path;
    }

    public String getRgb() {
        return rgb;
    }

    public int getWidth() {
        return width;
    }
}

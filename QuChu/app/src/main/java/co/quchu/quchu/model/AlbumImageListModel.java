package co.quchu.quchu.model;

import java.io.Serializable;

/**
 * PostCardImageListModel
 * User: Chenhs
 * Date: 2015-12-30
 * 相册照片
 */
public class AlbumImageListModel implements Serializable {
    /**
     * autor : chsd
     * autorId : 108
     * autorPhoto : http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-10
     * cardId : 529
     * height : 960
     * imagId : 3861
     * isf : true
     * path : http://7xodsq.com1.z0.glb.clouddn.com/108-1457596193163.JPEG?imageMogr2/thumbnail/800x/format/webp
     * rgb : 3d7f99
     * time : 2016-03-10 15:49:55
     * width : 576
     */

    private String autor;
    private int autorId;
    private String autorPhoto;
    private int cardId;
    private int height;
    private int imagId;
    private boolean isf;
    private String path;
    private String rgb;
    private String time;
    private int width;

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    public void setAutorPhoto(String autorPhoto) {
        this.autorPhoto = autorPhoto;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setImagId(int imagId) {
        this.imagId = imagId;
    }

    public void setIsf(boolean isf) {
        this.isf = isf;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getAutor() {
        return autor;
    }

    public int getAutorId() {
        return autorId;
    }

    public String getAutorPhoto() {
        return autorPhoto;
    }

    public int getCardId() {
        return cardId;
    }

    public int getHeight() {
        return height;
    }

    public int getImagId() {
        return imagId;
    }

    public boolean isIsf() {
        return isf;
    }

    public String getPath() {
        return path;
    }

    public String getRgb() {
        return rgb;
    }

    public String getTime() {
        return time;
    }

    public int getWidth() {
        return width;
    }
}

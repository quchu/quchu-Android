package co.quchu.quchu.model;

import java.util.List;

/**
 * PostCardModel
 * User: Chenhs
 * Date: 2015-11-12
 */
public class PostCardModel {

    /**
     * address : 思明区鼓浪屿公平路18号。（近笔山洞）
     * autor : 少勇
     * autorPhoto : http://7xo7et.com1.z0.glb.clouddn.com/77-app-default-avatar
     * cardId : 147
     * comment : 在这里输入评价
     * favoNum : 0
     * height : 454
     * imglist : []
     * isf : false
     * isp : false
     * placeId : 16
     * plcaeAddress : 厦门 鼓浪屿
     * plcaeCover : http://7xo7et.com1.z0.glb.clouddn.com/16-default-place-cover
     * plcaeName : 红堂生活旅馆
     * praiseNum : 0
     * rgb : 757366
     * score : 2.0
     * tel : 0592-2517389
     * time : 2015-10-21 06:08:28
     * width : 750
     */

    private String address;
    private String autor;
    private String autorPhoto;
    private int cardId;
    private String comment;
    private int favoNum;
    private int height;
    private boolean isf;
    private boolean isp;
    private int placeId;
    private String plcaeAddress;
    private String plcaeCover;
    private String plcaeName;
    private int praiseNum;
    private String rgb;
    private double score;
    private String tel;
    private String time;
    private int width;
    private List<String> imglist;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setAutorPhoto(String autorPhoto) {
        this.autorPhoto = autorPhoto;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setFavoNum(int favoNum) {
        this.favoNum = favoNum;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setIsf(boolean isf) {
        this.isf = isf;
    }

    public void setIsp(boolean isp) {
        this.isp = isp;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public void setPlcaeAddress(String plcaeAddress) {
        this.plcaeAddress = plcaeAddress;
    }

    public void setPlcaeCover(String plcaeCover) {
        this.plcaeCover = plcaeCover;
    }

    public void setPlcaeName(String plcaeName) {
        this.plcaeName = plcaeName;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setImglist(List<String> imglist) {
        this.imglist = imglist;
    }

    public String getAddress() {
        return address;
    }

    public String getAutor() {
        return autor;
    }

    public String getAutorPhoto() {
        return autorPhoto;
    }

    public int getCardId() {
        return cardId;
    }

    public String getComment() {
        return comment;
    }

    public int getFavoNum() {
        return favoNum;
    }

    public int getHeight() {
        return height;
    }

    public boolean isIsf() {
        return isf;
    }

    public boolean isIsp() {
        return isp;
    }

    public int getPlaceId() {
        return placeId;
    }

    public String getPlcaeAddress() {
        return plcaeAddress;
    }

    public String getPlcaeCover() {
        return plcaeCover;
    }

    public String getPlcaeName() {
        return plcaeName;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public String getRgb() {
        return rgb;
    }

    public double getScore() {
        return score;
    }

    public String getTel() {
        return tel;
    }

    public String getTime() {
        return time;
    }

    public int getWidth() {
        return width;
    }

    public List<String> getImglist() {
        return imglist;
    }
}

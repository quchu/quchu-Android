package co.quchu.quchu.model;

import java.io.Serializable;
import java.util.List;

/**
 * PostCardItemModel
 * User: Chenhs
 * Date: 2015-12-30
 */
public class PostCardItemModel implements Serializable {
    private String address;
    private String autor;
    private int autorId;
    private String autorPhoto;
    private int cardId;
    private String comment;
    private int favoNum;
    private int height;
    private boolean isf;
    private boolean isme;
    private boolean isp;
    private int placeId;
    private String plcaeAddress;
    private String plcaeCover;
    private String plcaeName;
    private int praiseNum;
    private String rgb;
    private float score;
    private String tel;
    private String time;
    private int width;
    private boolean issys = false;

    /**
     * imgId : 469
     * isCover : true
     * path : http://7xo7et.com1.z0.glb.clouddn.com/11_1450428218588.JPEG?imageMogr2/format/webp
     */
    public boolean issys() {
        return issys;
    }

    public void setIssys(boolean issys) {
        this.issys = issys;
    }

    private List<PostCardImageListModel> imglist;

    public void setAddress(String address) {
        this.address = address;
    }

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

    public void setIsme(boolean isme) {
        this.isme = isme;
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

    public void setScore(float score) {
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

    public void setImglist(List<PostCardImageListModel> imglist) {
        this.imglist = imglist;
    }

    public String getAddress() {
        return address;
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

    public boolean isIsme() {
        return isme;
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

    public float getScore() {
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

    public List<PostCardImageListModel> getImglist() {
        return imglist;
    }


}




package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by Nico on 16/4/14.
 */
public class FootprintModel {

    private int cardId;
    private int autorId;
    private int favoNum;
    private int height;
    private int placeId;
    private int praiseNum;
    private int score;
    private int width;
    private String address;
    private String autor;
    private String autorPhoto;
    private String comment;
    private String placeAddress;
    private String placeCover;
    private String placeName;
    private String rgb;
    private String tel;
    private String time;
    private List<ImageModel> imglist;
    private boolean isf;
    private boolean ism;
    private boolean isp;


    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getAutorId() {
        return autorId;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    public int getFavoNum() {
        return favoNum;
    }

    public void setFavoNum(int favoNum) {
        this.favoNum = favoNum;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getAutorPhoto() {
        return autorPhoto;
    }

    public void setAutorPhoto(String autorPhoto) {
        this.autorPhoto = autorPhoto;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlaceCover() {
        return placeCover;
    }

    public void setPlaceCover(String placeCover) {
        this.placeCover = placeCover;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ImageModel> getImglist() {
        return imglist;
    }

    public void setImglist(List<ImageModel> imglist) {
        this.imglist = imglist;
    }

    public boolean isf() {
        return isf;
    }

    public void setIsf(boolean isf) {
        this.isf = isf;
    }

    public boolean ism() {
        return ism;
    }

    public void setIsm(boolean ism) {
        this.ism = ism;
    }

    public boolean isp() {
        return isp;
    }

    public void setIsp(boolean isp) {
        this.isp = isp;
    }
}

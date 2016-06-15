package co.quchu.quchu.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
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

    public static class Entity implements Parcelable {
        public ImageModel image;//大图
        public String head;//头像
        public int autoId;//发布人ID
        public boolean isP;//当前登录用户是否点赞
        public int supportCount;//点赞数
        public String time;//时间
        public int cardId;
        public String PlcaeName;
        public int PlcaeId;
        public String Comment;
        public String name;//发布人姓名

        public Entity() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.image, flags);
            dest.writeString(this.head);
            dest.writeInt(this.autoId);
            dest.writeByte(this.isP ? (byte) 1 : (byte) 0);
            dest.writeInt(this.supportCount);
            dest.writeString(this.time);
            dest.writeInt(this.cardId);
            dest.writeString(this.PlcaeName);
            dest.writeInt(this.PlcaeId);
            dest.writeString(this.Comment);
            dest.writeString(this.name);
        }

        protected Entity(Parcel in) {
            this.image = in.readParcelable(ImageModel.class.getClassLoader());
            this.head = in.readString();
            this.autoId = in.readInt();
            this.isP = in.readByte() != 0;
            this.supportCount = in.readInt();
            this.time = in.readString();
            this.cardId = in.readInt();
            this.PlcaeName = in.readString();
            this.PlcaeId = in.readInt();
            this.Comment = in.readString();
            this.name = in.readString();
        }

        public static final Creator<Entity> CREATOR = new Creator<Entity>() {
            @Override
            public Entity createFromParcel(Parcel source) {
                return new Entity(source);
            }

            @Override
            public Entity[] newArray(int size) {
                return new Entity[size];
            }
        };
    }

    public List<Entity> convertToList() {
        List<Entity> data = new ArrayList<>();
        if (imglist != null)
            for (ImageModel item : imglist) {
                Entity entity = new Entity();
                entity.autoId = this.getAutorId();
                entity.head = this.getAutorPhoto();
                entity.isP = this.isp();
                entity.supportCount = this.getPraiseNum();
                entity.time = this.getTime();
                entity.image = item;
                entity.cardId = this.getCardId();
                entity.PlcaeName = this.getPlaceName();
                entity.PlcaeId = this.getPlaceId();
                entity.Comment = this.getComment();
                entity.name=this.getAutor();
                data.add(entity);
            }
        return data;
    }

    public PostCardItemModel convertToCompatModel() {
        PostCardItemModel model = new PostCardItemModel();
        model.setHeight(this.height);
        model.setWidth(this.width);
        model.setAddress(this.address);
        model.setAutor(this.autor);
        model.setAutorId(this.autorId);
        model.setAutorPhoto(this.autorPhoto);
        model.setCardId(this.cardId);
        model.setComment(this.comment);
        model.setFavoNum(this.favoNum);
        model.setImglist(this.imglist);
        model.setIsf(this.isf);
        model.setIsme(this.ism);
        model.setIsp(this.isp);
        model.setPlaceId(this.placeId);
        model.setPlcaeAddress(this.placeAddress);
        model.setPlcaeCover(this.placeCover);
        model.setPlcaeName(this.placeName);
        model.setIsf(this.isf);
        model.setPraiseNum(this.praiseNum);
        model.setScore(this.score);
        model.setTel(this.tel);
        model.setTime(this.time);
        return model;
    }

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

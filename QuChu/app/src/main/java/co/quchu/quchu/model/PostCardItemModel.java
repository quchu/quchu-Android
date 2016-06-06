package co.quchu.quchu.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PostCardItemModel implements Parcelable {
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
    private String placeAddress;
    private String placeCover;
    private String placeName;
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
    private List<ImageModel> imglist;


    public List<FootprintModel.Entity> convertToList() {
        List<FootprintModel.Entity> data = new ArrayList<>();
        if (imglist != null)
            for (ImageModel item : imglist) {
                FootprintModel.Entity entity = new FootprintModel.Entity();
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





    public void addImageModel(ImageModel item) {
        if (imglist == null) {
            imglist = new ArrayList<>();
        }
        imglist.add(item);
    }

    public boolean isf() {
        return isf;
    }

    public boolean isme() {
        return isme;
    }

    public boolean isp() {
        return isp;
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

    public static Creator<PostCardItemModel> getCREATOR() {
        return CREATOR;
    }









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
        this.placeAddress = plcaeAddress;
    }

    public void setPlcaeCover(String plcaeCover) {
        this.placeCover = plcaeCover;
    }

    public void setPlcaeName(String plcaeName) {
        this.placeName = plcaeName;
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

    public void setImglist(List<ImageModel> imglist) {
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
        return placeAddress == null ? plcaeAddress : placeAddress;
    }

    public String getPlcaeCover() {
        return placeCover == null ? plcaeCover : placeCover;
    }

    public String getPlcaeName() {
        return placeName == null ? plcaeName : placeName;
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

    public List<ImageModel> getImglist() {
        return imglist;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.autor);
        dest.writeInt(this.autorId);
        dest.writeString(this.autorPhoto);
        dest.writeInt(this.cardId);
        dest.writeString(this.comment);
        dest.writeInt(this.favoNum);
        dest.writeInt(this.height);
        dest.writeByte(isf ? (byte) 1 : (byte) 0);
        dest.writeByte(isme ? (byte) 1 : (byte) 0);
        dest.writeByte(isp ? (byte) 1 : (byte) 0);
        dest.writeInt(this.placeId);
        dest.writeString(this.placeAddress);
        dest.writeString(this.placeCover);
        dest.writeString(this.placeName);
        dest.writeString(this.plcaeAddress);
        dest.writeString(this.plcaeCover);
        dest.writeString(this.plcaeName);
        dest.writeInt(this.praiseNum);
        dest.writeString(this.rgb);
        dest.writeFloat(this.score);
        dest.writeString(this.tel);
        dest.writeString(this.time);
        dest.writeInt(this.width);
        dest.writeByte(issys ? (byte) 1 : (byte) 0);
        dest.writeList(this.imglist);
    }

    public PostCardItemModel() {
    }

    protected PostCardItemModel(Parcel in) {
        this.address = in.readString();
        this.autor = in.readString();
        this.autorId = in.readInt();
        this.autorPhoto = in.readString();
        this.cardId = in.readInt();
        this.comment = in.readString();
        this.favoNum = in.readInt();
        this.height = in.readInt();
        this.isf = in.readByte() != 0;
        this.isme = in.readByte() != 0;
        this.isp = in.readByte() != 0;
        this.placeId = in.readInt();
        this.placeAddress = in.readString();
        this.placeCover = in.readString();
        this.placeName = in.readString();
        this.plcaeAddress = in.readString();
        this.plcaeCover = in.readString();
        this.plcaeName = in.readString();
        this.praiseNum = in.readInt();
        this.rgb = in.readString();
        this.score = in.readFloat();
        this.tel = in.readString();
        this.time = in.readString();
        this.width = in.readInt();
        this.issys = in.readByte() != 0;
        this.imglist = new ArrayList<ImageModel>();
        in.readList(this.imglist, ImageModel.class.getClassLoader());
    }

    public static final Creator<PostCardItemModel> CREATOR = new Creator<PostCardItemModel>() {
        @Override
        public PostCardItemModel createFromParcel(Parcel source) {
            return new PostCardItemModel(source);
        }

        @Override
        public PostCardItemModel[] newArray(int size) {
            return new PostCardItemModel[size];
        }
    };
}




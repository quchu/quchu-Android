package co.quchu.quchu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ImageModel
 * User: Chenhs
 * Date: 2015-12-30
 */
public class ImageModel implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.height);
        dest.writeInt(this.imgId);
        dest.writeByte(isCover ? (byte) 1 : (byte) 0);
        dest.writeByte(isf ? (byte) 1 : (byte) 0);
        dest.writeString(this.path);
        dest.writeString(this.rgb);
        dest.writeInt(this.width);
    }

    public ImageModel() {
    }

    protected ImageModel(Parcel in) {
        this.height = in.readInt();
        this.imgId = in.readInt();
        this.isCover = in.readByte() != 0;
        this.isf = in.readByte() != 0;
        this.path = in.readString();
        this.rgb = in.readString();
        this.width = in.readInt();
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel source) {
            return new ImageModel(source);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };
}

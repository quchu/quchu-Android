package co.quchu.quchu.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * RecommendModel
 * User: Chenhs
 * Date: 2015-12-08
 */
public class RecommendModel implements Parcelable {

    /**
     * address : 仙岳路8号桥头堡文创园内
     * cover : http://7xo7et.com1.z0.glb.clouddn.com/20-default-app-place-cover?imageMogr2/format/webp
     * describe : 厦门
     * distance : 26060.471714041065
     * genes : [{"key":"夜生活","value":"90"},{"key":"氛围","value":"71"},{"key":"性价比","value":"80"}]
     * height : 683
     * isActivity : false
     * isf : false
     * isout :false  是否去过
     * latitude : 24.497263415041
     * longitude : 118.09703816664
     * name : 迷待音乐会所
     * pid : 20
     * rgb : b7a867
     * suggest : 4
     * tags : []
     * width : 1024
     */

    private String address;
    private String cover;
    private String described;
    private String distance;
    private int height;
    private boolean isActivity;
    private boolean isf;
    private String latitude;
    private String longitude;
    private String name;
    private int pid;
    private String rgb;
    private float suggest;
    private int width;
    public boolean isout;  //是否去过
    private String price;
    private String areaCircleName;
    private double gdLatitude;
    private double gdLongitude;

    public double getGdLatitude() {
        return gdLatitude;
    }

    public void setGdLatitude(double gdLatitude) {
        this.gdLatitude = gdLatitude;
    }

    public double getGdLongitude() {
        return gdLongitude;
    }

    public void setGdLongitude(double gdLongitude) {
        this.gdLongitude = gdLongitude;
    }

    public String getAreaCircleName() {
        return areaCircleName;
    }

    public void setAreaCircleName(String areaCircleName) {
        this.areaCircleName = areaCircleName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * key : 夜生活
     * value : 90
     */

    private List<GenesEntity> genes;
    private List<TagsEntity> tags;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setDescribe(String described) {
        this.described = described;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setIsActivity(boolean isActivity) {
        this.isActivity = isActivity;
    }

    public void setIsf(boolean isf) {
        this.isf = isf;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public void setSuggest(float suggest) {
        this.suggest = suggest;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setGenes(List<GenesEntity> genes) {
        this.genes = genes;
    }

    public void setTags(List<TagsEntity> tags) {
        this.tags = tags;
    }

    public String getAddress() {
        return address;
    }

    public String getCover() {
        return cover;
    }

    public String getDescribe() {
        return described;
    }

    public String getDistance() {
        return distance;
    }

    public int getHeight() {
        return height;
    }

    public boolean isIsActivity() {
        return isActivity;
    }

    public boolean isIsf() {
        return isf;
    }

    public double getLatitude() {
        double v = 0;


        if (null == latitude) {
            return v;
        }
        try {
            v = Double.parseDouble(latitude);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return v;
    }

    public double getLongitude() {
        double v = 0;
        try {
            v = Double.parseDouble(longitude);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return v;
    }

    public String getName() {
        return name;
    }

    public int getPid() {
        return pid;
    }

    public String getRgb() {
        return rgb;
    }

    public float getSuggest() {
        return suggest;
    }

    public int getWidth() {
        return width;
    }

    public List<GenesEntity> getGenes() {
        return genes;
    }

    public List<TagsEntity> getTags() {
        return tags;
    }

    public static class GenesEntity {
        private String key;
        private String value;

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    public static class TagsEntity {
        private String zh;

        public void setZh(String zh) {
            this.zh = zh;
        }

        public String getZh() {
            return zh;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.cover);
        dest.writeString(this.described);
        dest.writeString(this.distance);
        dest.writeInt(this.height);
        dest.writeByte(this.isActivity ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isf ? (byte) 1 : (byte) 0);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.name);
        dest.writeInt(this.pid);
        dest.writeString(this.rgb);
        dest.writeFloat(this.suggest);
        dest.writeInt(this.width);
        dest.writeByte(this.isout ? (byte) 1 : (byte) 0);
        dest.writeString(this.price);
        dest.writeList(this.genes);
        dest.writeList(this.tags);
    }

    public RecommendModel() {
    }

    protected RecommendModel(Parcel in) {
        this.address = in.readString();
        this.cover = in.readString();
        this.described = in.readString();
        this.distance = in.readString();
        this.height = in.readInt();
        this.isActivity = in.readByte() != 0;
        this.isf = in.readByte() != 0;
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.name = in.readString();
        this.pid = in.readInt();
        this.rgb = in.readString();
        this.suggest = in.readFloat();
        this.width = in.readInt();
        this.isout = in.readByte() != 0;
        this.price = in.readString();
        this.genes = new ArrayList<GenesEntity>();
        in.readList(this.genes, GenesEntity.class.getClassLoader());
        this.tags = new ArrayList<TagsEntity>();
        in.readList(this.tags, TagsEntity.class.getClassLoader());
    }

    public static final Creator<RecommendModel> CREATOR = new Creator<RecommendModel>() {
        @Override
        public RecommendModel createFromParcel(Parcel source) {
            return new RecommendModel(source);
        }

        @Override
        public RecommendModel[] newArray(int size) {
            return new RecommendModel[size];
        }
    };
}
